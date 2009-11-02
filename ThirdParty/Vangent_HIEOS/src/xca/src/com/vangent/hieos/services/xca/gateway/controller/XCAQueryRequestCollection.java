/*
 * This code is subject to the HIEOS License, Version 1.0
 *
 * Copyright(c) 2008-2009 Vangent, Inc.  All rights reserved.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vangent.hieos.services.xca.gateway.controller;

import com.vangent.hieos.xutil.metadata.structure.MetadataTypes;
import com.vangent.hieos.xutil.metadata.structure.MetadataSupport;
import com.vangent.hieos.xutil.registry.RegistryUtility;
import com.vangent.hieos.xutil.response.Response;
import com.vangent.hieos.xutil.xlog.client.XLogMessage;
import com.vangent.hieos.xutil.soap.Soap;
import com.vangent.hieos.xutil.metadata.structure.HomeAttribute;

// Exceptions.
import com.vangent.hieos.xutil.exception.XdsException;
import com.vangent.hieos.xutil.exception.XdsWSException;

// XConfig.
import com.vangent.hieos.xutil.xconfig.XConfig;
import com.vangent.hieos.xutil.xconfig.XConfigEntity;
import com.vangent.hieos.xutil.xconfig.XConfigTransaction;
import com.vangent.hieos.xutil.xconfig.XConfigHomeCommunity;

// XATNA.
import com.vangent.hieos.xutil.atna.XATNALogger;

// Third-party.
import java.util.ArrayList;
import org.apache.axiom.om.OMElement;
import org.apache.log4j.Logger;

/**
 *
 * @author Bernie Thuman
 */
public class XCAQueryRequestCollection extends XCAAbstractRequestCollection {
    private final static Logger logger = Logger.getLogger(XCAQueryRequestCollection.class);

    /**
     *
     * @param uniqueId
     * @param configEntity
     * @param isLocalRequestString uniqueId, XConfigEntity configEntity, boolean isLocalRequest
     */
    public XCAQueryRequestCollection(String uniqueId, XConfigEntity configEntity, boolean isLocalRequest) {
        super(uniqueId, configEntity, isLocalRequest);
    }

    /**
     *
     * @return
     */
    public String getEndpointURL() {
        String txnName = this.isLocalRequest() ? "RegistryStoredQuery" : "CrossGatewayQuery";
        XConfigTransaction txn = this.getConfigEntity().getTransaction(txnName);
        return txn.getEndpointURL();
    }

    /**
     * 
     * @param response
     * @param logMessage
     * @return
     * @throws com.vangent.hieos.xutil.exception.XdsWSException
     * @throws com.vangent.hieos.xutil.exception.XdsException
     */
    public OMElement sendRequests(Response response, XLogMessage logMessage) throws XdsWSException, XdsException {
        OMElement rootRequest = MetadataSupport.om_factory.createOMElement("AdhocQueryRequest", MetadataSupport.ebQns3);

        // Now consolidate all requests to send out.
        ArrayList<XCARequest> allXCARequests = this.getRequests();
        for (XCARequest request : allXCARequests) {
            rootRequest.addChild(((XCAQueryRequest) request).getResponseOption());
            rootRequest.addChild(request.getRequest());
        }

        // Now send them out.
        OMElement result = this.sendTransaction(rootRequest, this.getEndpointURL(), this.isLocalRequest());
        if (result != null) { // to be safe.
            logMessage.addOtherParam("Result (" + this.getEndpointURL() + ")", result.toString());
            // Validate the response against the schema.
            try {
                RegistryUtility.schema_validate_local(result, MetadataTypes.METADATA_TYPE_SQ);
            } catch (Exception e) {
                result = null;  // Ignore the response.
                response.add_error(MetadataSupport.XDSRegistryMetadataError,
                        "Remote Gateway or Registry response did not validate against schema  [id = " +
                        this.getUniqueId() + ", endpoint = " + this.getEndpointURL() + "]",
                        e.toString(), logMessage);
            }

            if ((result != null) && this.isLocalRequest()) {
                XConfigHomeCommunity homeCommunity = XConfig.getInstance().getHomeCommunity();
                this.setHomeAttributeOnResult(result, homeCommunity.getHomeCommunityId());
            }
        }
        return result;
    }

    /**
     *
     * @param request
     * @param endpoint
     * @param isLocalRequest
     * @return
     * @throws com.vangent.hieos.xutil.exception.XdsWSException
     * @throws com.vangent.hieos.xutil.exception.XdsException
     */
    private OMElement sendTransaction(OMElement request, String endpoint, boolean isLocalRequest)
            throws XdsWSException, XdsException {
        String action, expectedReturnAction;
        String ATNAtxn;

        if (isLocalRequest) {
            // For XDS Affinity Domain option.
            action = "urn:ihe:iti:2007:RegistryStoredQuery";
            expectedReturnAction =
                    "urn:ihe:iti:2007:RegistryStoredQueryResponse";
            ATNAtxn = XATNALogger.TXN_ITI18;
        } else {
            action = "urn:ihe:iti:2007:CrossGatewayQuery";
            expectedReturnAction =
                    "urn:ihe:iti:2007:CrossGatewayQueryResponse";
            ATNAtxn = XATNALogger.TXN_ITI38;
        }

        logger.info("*** XCA action: " + action + ", endpoint: " + endpoint + " ***");

        Soap soap = new Soap();
        soap.setAsync(false);
        soap.soapCall(request, endpoint,
                false, // mtom
                true, // addressing
                true, // soap12
                action,
                expectedReturnAction);

        OMElement result = soap.getResult();  // Get the result.

        // Do ATNA auditing (after getting the result since we are only logging positive cases).
        this.performAudit(ATNAtxn, request, endpoint, XATNALogger.OutcomeIndicator.SUCCESS);

        return result;
    }

    /**
     *
     * @param result
     * @param home
     */
    private void setHomeAttributeOnResult(OMElement result, String home) {
        HomeAttribute homeAtt = new HomeAttribute(home);
        homeAtt.set(result);
    }
}
