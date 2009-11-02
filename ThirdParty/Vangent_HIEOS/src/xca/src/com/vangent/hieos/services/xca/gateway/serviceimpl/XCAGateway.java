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

package com.vangent.hieos.services.xca.gateway.serviceimpl;

import com.vangent.hieos.xutil.metadata.structure.Metadata;
import com.vangent.hieos.xutil.metadata.structure.MetadataSupport;
import com.vangent.hieos.xutil.services.framework.ContentValidationService;
import com.vangent.hieos.xutil.services.framework.XAbstractService;
import com.vangent.hieos.services.xca.gateway.transactions.XCARetrieveDocumentSet;
import com.vangent.hieos.services.xca.gateway.transactions.XCAAdhocQueryRequest;
import com.vangent.hieos.services.xca.gateway.transactions.XCAAbstractTransaction;
import com.vangent.hieos.xutil.exception.XdsValidationException;
import com.vangent.hieos.xutil.exception.XdsWSException;
import com.vangent.hieos.xutil.response.Response;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNamespace;
import org.apache.log4j.Logger;

// Axis2 LifeCycle support.
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.description.AxisService;

// XATNA
import com.vangent.hieos.xutil.atna.XATNALogger;

/**
 *
 * @author Bernie Thuman
 */
public class XCAGateway extends XAbstractService implements ContentValidationService {
    private final static Logger logger = Logger.getLogger(XCAGateway.class);

    /**
     * 
     * @param ahqr
     * @return
     */
    public OMElement AdhocQueryRequest(OMElement ahqr) {
        try {
            OMElement startup_error = beginTransaction(getQueryTransactionName(), ahqr, XAbstractService.registry_actor);
            if (startup_error != null) {
                return startup_error;
            }
            log_message.setTestMessage(getQueryTransactionName());
            OMElement ahq = MetadataSupport.firstChildWithLocalName(ahqr, "AdhocQuery");
            if (ahq == null) {
                endTransaction(false);
                return this.start_up_error(ahqr, null, XAbstractService.registry_actor, "XCA" + " only accepts Stored Query - AdhocQuery element not found");
            }

            validateWS();
            validateQueryTransaction(ahqr);

            // Delegate all the hard work to the XCAAdhocQueryRequest class (follows same NIST patterns).
            XCAAdhocQueryRequest transaction =
                    new XCAAdhocQueryRequest(this.getGatewayType(),
                    log_message, getMessageContext());

            // Now, do some work!
            OMElement result = transaction.run(ahqr, this, this);
            endTransaction(transaction.getStatus());
            return result;
        } catch (Exception e) {
            return endTransaction(ahqr, e, XAbstractService.registry_actor, "");
        }
    }

    /**
     *
     * @param rdsr
     * @return
     */
    public OMElement RetrieveDocumentSetRequest(OMElement rdsr) {
        try {
            OMElement startup_error = beginTransaction(getRetTransactionName(), rdsr, XAbstractService.repository_actor);
            if (startup_error != null) {
                return startup_error;
            }
            log_message.setTestMessage(getRetTransactionName());

            // Do some preliminary validation.
            validateWS();
            validateRetrieveTransaction(rdsr);

            // Delegate all the hard work to the XCARetrieveDocumentSet class (follows same NIST patterns).
            XCARetrieveDocumentSet transaction =
                    new XCARetrieveDocumentSet(
                    this.getGatewayType(),
                    log_message, getMessageContext());

            // Now, do the work.
            OMElement result = transaction.run(rdsr, this, this);
            endTransaction(transaction.getStatus());
            return result;
        } catch (Exception e) {
            return endTransaction(rdsr, e, XAbstractService.repository_actor, "");
        }
    }

    /**
     *
     * @return
     */
    private XCAAbstractTransaction.GatewayType getGatewayType() {
        XCAAbstractTransaction.GatewayType gatewayType = XCAAbstractTransaction.GatewayType.InitiatingGateway;
        String actorName = (String) this.getMessageContext().getParameter("ActorName").getValue();
        if (actorName.equals("RespondingGateway")) {
            gatewayType = XCAAbstractTransaction.GatewayType.RespondingGateway;
        }
        return gatewayType;
    }

    /**
     * 
     * @return
     */
    protected String getQueryTransactionName() {
        XCAAbstractTransaction.GatewayType gatewayType = this.getGatewayType();
        String transactionName = "SQ (XCA)";
        if (gatewayType == XCAAbstractTransaction.GatewayType.RespondingGateway) {
            transactionName = "XGQ (XCA)";
        }
        return transactionName;
    }

    /**
     *
     * @return
     */
    private String getRetTransactionName() {
        XCAAbstractTransaction.GatewayType gatewayType = this.getGatewayType();
        String transactionName = "RET.b (XCA)";
        if (gatewayType == XCAAbstractTransaction.GatewayType.RespondingGateway) {
            transactionName = "XGR (XCA)";
        }
        return transactionName;
    }

    /**
     *
     * @param ahqr
     * @return
     */
    private String getRTransactionName(OMElement ahqr) {
        OMElement ahq = MetadataSupport.firstChildWithLocalName(ahqr, "AdhocQuery");
        if (ahq != null) {
            return "SQ.b";
        } else if (ahqr.getLocalName().equals("SubmitObjectsRequest")) {
            return "SubmitObjectsRequest.b";
        } else {
            return "Unknown";
        }
    }
    
    /**
     *
     * @param ahqr
     * @return
     */
    private boolean isSQ(OMElement ahqr) {
        return MetadataSupport.firstChildWithLocalName(ahqr, "AdhocQuery") != null;
    }

    /**
     *
     * @param sor
     * @throws com.vangent.hieos.xutil.exception.XdsValidationException
     */
    private void validateQueryTransaction(OMElement sor)
            throws XdsValidationException {
        OMNamespace ns = sor.getNamespace();
        String ns_uri = ns.getNamespaceURI();
        if (ns_uri == null || !ns_uri.equals(MetadataSupport.ebQns3.getNamespaceURI())) {
            throw new XdsValidationException("Invalid namespace on " + sor.getLocalName() + " (" + ns_uri + ")");
        }
        String type = getRTransactionName(sor);
        if (!this.isSQ(sor)) {
            throw new XdsValidationException("Only StoredQuery is acceptable on this endpoint");
        }
        //new StoredQueryRequestSoapValidator(XBaseTransaction.xds_b, getMessageContext()).runWithException();
    }

    /**
     * 
     * @param sor
     * @throws XdsValidationException
     */
    private void validateRetrieveTransaction(OMElement sor) throws XdsValidationException {
        OMNamespace ns = sor.getNamespace();
        String ns_uri = ns.getNamespaceURI();
        if (ns_uri == null || !ns_uri.equals(MetadataSupport.xdsB.getNamespaceURI())) {
            throw new XdsValidationException("Invalid namespace on " + sor.getLocalName() + " (" + ns_uri + ")");
        }
    }

    /**
     * 
     * @throws com.vangent.hieos.xutil.exception.XdsWSException
     */
    protected void validateWS() throws XdsWSException {
        checkSOAP12();
        if (isAsync()) {
            throw new XdsWSException("Asynchronous web service request not acceptable on this endpoint" +
                    " - replyTo is " + getMessageContext().getReplyTo().getAddress());
        }
    }

    /**
     *
     * @param m
     * @param response
     * @return
     */
    public boolean runContentValidationService(Metadata m, Response response) {
        return true;
    }

    // BHT (ADDED Axis2 LifeCycle methods):
    /**
     * This will be called during the deployment time of the service.
     * Irrespective of the service scope this method will be called
     */
    @Override
    public void startUp(ConfigurationContext configctx, AxisService service) {
        logger.info("XCAGateway::startUp(): " + service.getParameterValue("ActorName"));
        if (service.getParameterValue("ActorName").equals("InitiatingGateway")) {
            this.ATNAlogStart(XATNALogger.ActorType.INITIATING_GATEWAY);
        } else {
            this.ATNAlogStart(XATNALogger.ActorType.RESPONDING_GATEWAY);
        }
    }

    /**
     * This will be called during the system shut down time. Irrespective
     * of the service scope this method will be called
     */
    @Override
    public void shutDown(ConfigurationContext configctx, AxisService service) {
        logger.info("XCAGateway::shutDown(): " + service.getParameterValue("ActorName"));
        if (service.getParameterValue("ActorName").equals("InitiatingGateway")) {
            this.ATNAlogStop(XATNALogger.ActorType.INITIATING_GATEWAY);
        } else {
            this.ATNAlogStop(XATNALogger.ActorType.RESPONDING_GATEWAY);
        }
    }
}
