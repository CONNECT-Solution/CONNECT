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

package com.vangent.hieos.services.xca.gateway.transactions;

import com.vangent.hieos.xutil.metadata.structure.MetadataTypes;
import com.vangent.hieos.xutil.metadata.structure.MetadataSupport;
import com.vangent.hieos.xutil.registry.RegistryUtility;
import com.vangent.hieos.xutil.response.XCAAdhocQueryResponse;
import com.vangent.hieos.xutil.exception.MetadataValidationException;
import com.vangent.hieos.xutil.metadata.structure.ParamParser;
import com.vangent.hieos.xutil.xlog.client.XLogMessage;

// Exceptions.
import com.vangent.hieos.xutil.exception.SchemaValidationException;
import com.vangent.hieos.xutil.exception.XdsFormatException;
import com.vangent.hieos.xutil.exception.XdsInternalException;

import com.vangent.hieos.services.xca.gateway.controller.XCARequestController;
import com.vangent.hieos.services.xca.gateway.controller.XCAAbstractRequestCollection;
import com.vangent.hieos.services.xca.gateway.controller.XCAQueryRequestCollection;
import com.vangent.hieos.services.xca.gateway.controller.XCAQueryRequest;

// XConfig
import com.vangent.hieos.xutil.xconfig.XConfig;
import com.vangent.hieos.xutil.xconfig.XConfigGateway;
import com.vangent.hieos.xutil.xconfig.XConfigRegistry;
import com.vangent.hieos.xutil.xconfig.XConfigHomeCommunity;
import com.vangent.hieos.xutil.xconfig.XConfigEntity;
import com.vangent.hieos.xutil.xconfig.XConfigAssigningAuthority;

// XATNA.
import com.vangent.hieos.xutil.atna.XATNALogger;

import org.apache.axis2.context.MessageContext;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashMap;
import java.util.StringTokenizer;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMText;
import org.apache.axiom.om.OMNamespace;
import org.apache.log4j.Logger;

/**
 * Plugged into current NIST framework.
 *
 * @author Bernie Thuman
 */
public class XCAAdhocQueryRequest extends XCAAbstractTransaction {

    private final static Logger logger = Logger.getLogger(XCAAdhocQueryRequest.class);

    /**
     * 
     * @param gatewayType
     * @param log_message
     * @param messageContext
     */
    public XCAAdhocQueryRequest(GatewayType gatewayType, XLogMessage log_message, MessageContext messageContext) {
        try {
            super.init(gatewayType, log_message, new XCAAdhocQueryResponse(), messageContext);
        } catch (XdsInternalException e) {
            logger.fatal(logger_exception_details(e));
            response.add_error(MetadataSupport.XDSRegistryError,
                    e.getMessage(), this.getClass().getName(), log_message);
        }
    }

    /**
     *
     * @param request
     */
    protected void validateRequest(OMElement request) {
        // Validate SOAP format.
        try {
            mustBeSimpleSoap();
        } catch (XdsFormatException e) {
            response.add_error(MetadataSupport.XDSRegistryError,
                    "SOAP Format Error: " + e.getMessage(),
                    this.getClass().getName(), log_message);
        }

        // Validate namespace.
        OMNamespace ns = request.getNamespace();
        String ns_uri = ns.getNamespaceURI();
        if (ns_uri == null || !ns_uri.equals(MetadataSupport.ebQns3.getNamespaceURI())) {
            response.add_error(MetadataSupport.XDSRegistryError,
                    "Invalid XML namespace on AdhocQueryRequest: " + ns_uri,
                    this.getClass().getName(), log_message);
        }

        // Validate against schema.
        try {
            RegistryUtility.schema_validate_local(request, MetadataTypes.METADATA_TYPE_SQ);
        } catch (SchemaValidationException e) {
            response.add_error(MetadataSupport.XDSRegistryMetadataError,
                    "SchemaValidationException: " + e.getMessage(),
                    this.getClass().getName(), log_message);
        } catch (XdsInternalException e) {
            response.add_error(MetadataSupport.XDSRegistryMetadataError,
                    "SchemaValidationException: " + e.getMessage(),
                    this.getClass().getName(), log_message);
        }

        // Perform ATNA audit (FIXME - may not be best place).
        String ATNAtxn;
        if (this.getGatewayType() == GatewayType.InitiatingGateway) {
            ATNAtxn = XATNALogger.TXN_ITI18;
        } else {
            ATNAtxn = XATNALogger.TXN_ITI38;
        }
        this.performAudit(
                ATNAtxn,
                request,
                null,
                XATNALogger.OutcomeIndicator.SUCCESS,
                XATNALogger.ActorType.REGISTRY);
    }

    /**
     *
     * @param request
     * @throws com.vangent.hieos.xutil.exception.XdsInternalException
     */
    protected void prepareValidRequests(OMElement request) throws XdsInternalException {
        // Get the AdhocQuery & ResponseOption nodes.
        OMElement queryRequest = MetadataSupport.firstChildWithLocalName(request, "AdhocQuery");
        OMElement responseOption = MetadataSupport.firstChildWithLocalName(request, "ResponseOption");
        if (responseOption == null) {
            throw new XdsInternalException("Cannot find /AdhocQueryRequest/ResponseOption element");
        }

        // First check to see if homeCommunityId is required on request.
        if (this.requiresHomeCommunityId(queryRequest)) {
            this.logInfo("Note", "*** Query requires homeCommunityId ***");

            // Now get the homeCommunityId on the request.
            String homeCommunityId = this.getHomeCommunityId(queryRequest);

            // Is it missing?
            if (homeCommunityId == null) {  // Missing homeCommunityId.
                response.add_error(MetadataSupport.XDSMissingHomeCommunityId,
                        "homeCommunityId missing or empty",
                        this.getClass().getName(), log_message);
            } else {  // homeCommunityId is present.
                this.processTargetedHomeRequest(queryRequest, responseOption, homeCommunityId);
            }
        } else { // homeCommunityId is not required (but still may be present).

            // See if the request has a homeCommunityId.
            String homeCommunityId = this.getHomeCommunityId(queryRequest);
            if (homeCommunityId != null) // homeCommunityId is present.
            {
                this.processTargetedHomeRequest(queryRequest, responseOption, homeCommunityId);
            } else {  // homeCommunityId is not present.
                // Now, find communities that can respond to the request (by patient id).
                this.processRequestWithPatientId(request, queryRequest, responseOption);
            }
        }
    }

    /**
     *
     * @param queryRequest
     * @param responseOption
     */
    private void processRequestWithPatientId(OMElement request, OMElement queryRequest, OMElement responseOption) throws XdsInternalException {

        if (this.getGatewayType() == GatewayType.RespondingGateway) {  // RespondingGateway
            // Just go local.
            XConfigRegistry registry = this.getLocalRegistry();
            this.addRequest(queryRequest, responseOption, registry.getName(), registry, true);

        } else { // InitiatingGateway
            // At this point, we know the following:
            //   We need to extract the patientId.
            //   A homeCommunityId is not present.
            // Send requests based upon patient identifier.
            String patientId = this.getPatientId(request, queryRequest);
            if (patientId != null) {
                this.logInfo("Patient ID", patientId);
                // Now get the Assigning Authority within the patient ID.
                String assigningAuthority = this.getAssigningAuthority(patientId);
                if (assigningAuthority == null) {
                    /* --- Go silent here according to XCA spec --- */
                    this.logError("[* Not notifying client *]: Could not parse assigning authority from patient id = " + patientId);
                /* response.add_error(MetadataSupport.XDSRegistryMetadataError,
                "Could not parse assigning authority from patient id = " + patientId,
                "GatewayAdhocQueryRequest.java", log_message); */
                } else {
                    XConfig xconfig = XConfig.getInstance();
                    // Get the configuration for the assigning authority.
                    XConfigAssigningAuthority aa = xconfig.getAssigningAuthority(assigningAuthority);
                    if (aa == null) {
                        /* --- Go silent here according to XCA spec --- */
                        this.logError("[* Not notifying client *]: Could not find assigning authority configuration for patient id = " + patientId);
                    /* response.add_error(MetadataSupport.XDSRegistryError,
                    "Could not find assigning authority configuration for patient id = " + patientId,
                    "GatewayAdhocQueryRequest.java", log_message); */
                    } else {
                        // Ok.  Now we should hopefully be good to go.

                        // Add all remote gateways that can resolve patients within the assigning authority.
                        ArrayList<XConfigGateway> gateways = aa.getGateways();
                        for (XConfigGateway gateway : gateways) {
                            this.addRequest(queryRequest, responseOption, gateway.getHomeCommunityId(), gateway, false);
                        }

                        // Now, we may also need to go to a local registry.

                        // Does the assigning authority configuration include a local registry?
                        XConfigRegistry registry = aa.getLocalRegistry();
                        if (registry != null) {
                            //String homeCommunityId = xconfig.getHomeCommunity().getHomeCommunityId();
                            // Just use the registry name as the key (to avoid conflict with
                            // local homeCommunityId testing).
                            this.addRequest(queryRequest, responseOption, registry.getName(), registry, true);
                        }
                    }
                }

            } else {
                /* --- Go silent here according to XCA spec --- */
                /*
                response.add_error(MetadataSupport.XDSStoredQueryMissingParam, "Can not find patientID in request",
                "GatewayAdhocQueryRequest.java", log_message);
                 */
                this.logError("[* Not notifying client *]: Can not find Patient ID on request");
            }
        }
    }

    /**
     *
     * @param patientId
     * @return
     */
    private String getAssigningAuthority(String patientId) {
        // patientId format = <ID>^^^<AA>
        String assigningAuthority = null;

        // The last token will be the assigning authority ... this is a bit of a hack.
        // Had problems with split() and trying to escape the "^".  Anyway, this should work.
        StringTokenizer st = new StringTokenizer(patientId, "^");
        while (st.hasMoreTokens()) {
            assigningAuthority = st.nextToken();
        }

        return assigningAuthority;
    }

    /**
     *
     * @param request
     * @param queryRequest
     * @return
     * @throws com.vangent.hieos.xutil.exception.XdsInternalException
     */
    private String getPatientId(OMElement request, OMElement queryRequest) throws XdsInternalException {
        HashMap params = null;
        String queryId = this.getStoredQueryId(queryRequest);
        if (queryId == null) {
            return null;  // Early exit (FIXME).
        }
        // Parse the query parameters.
        ParamParser parser = new ParamParser();
        try {
            params = parser.parse(request);
        } catch (MetadataValidationException e) {
            response.add_error(MetadataSupport.XDSRegistryMetadataError,
                    "Problem parsing query parameters",
                    this.getClass().getName(), log_message);
        }
        if (params == null) {
            // Must have caught an exception above.
            return null;  // Early exit.
        }
        String patientId = null;
        if (queryId.equals(MetadataSupport.SQ_FindDocuments)) {
            // $XDSDocumentEntryPatientId
            patientId = (String) params.get("$XDSDocumentEntryPatientId");
        } else if (queryId.equals(MetadataSupport.SQ_FindFolders)) {
            // $XDSFolderPatientId
            patientId = (String) params.get("$XDSFolderPatientId");
        } else if (queryId.equals(MetadataSupport.SQ_FindSubmissionSets)) {
            // $XDSSubmissionSetPatientId
            patientId = (String) params.get("$XDSSubmissionSetPatientId");
        } else if (queryId.equals(MetadataSupport.SQ_GetAll)) {
            // FIXME: NOT IMPLEMENTED [NEED TO FIGURE OUT WHAT TO PULL OUT HERE.
        }
        return patientId;
    }

    /**
     *
     * @param queryRequest
     * @param responseOption
     * @param homeCommunityId
     * @throws com.vangent.hieos.xutil.exception.XdsInternalException
     */
    private void processTargetedHomeRequest(OMElement queryRequest, OMElement responseOption, String homeCommunityId) throws XdsInternalException {
        this.logInfo("HomeCommunityId", homeCommunityId);
        // See if this is for the local community.
        XConfig xconfig = XConfig.getInstance();
        String localHomeCommunityId = xconfig.getHomeCommunity().getHomeCommunityId();
        if (homeCommunityId.equals(localHomeCommunityId)) {  // Destined for the local home.
            this.logInfo("Note", "Going local for homeCommunityId: " + homeCommunityId);

            // XDSAffinityDomain option - get the local registry.
            XConfigRegistry localRegistry = this.getLocalRegistry();
            if (localRegistry != null) {
                // Add the local request.
                // Just use the registry name as the key (to avoid conflict with
                // local homeCommunityId testing).
                this.addRequest(queryRequest, responseOption, localRegistry.getName(), localRegistry, true);
            }
        } else if (this.getGatewayType() == GatewayType.InitiatingGateway) {  // Going remote.
            this.logInfo("Note", "Going remote for homeCommunityId: " + homeCommunityId);
            // See if we know about a remote gateway that can respond.
            XConfigGateway gatewayConfig = XConfig.getInstance().getGateway(homeCommunityId);
            if (gatewayConfig == null) {
                response.add_error(MetadataSupport.XDSUnknownCommunity,
                        "Do not understand homeCommunityId " + homeCommunityId,
                        this.getClass().getName(), log_message);
            } else {
                // This request is good (targeted for a remote community.
                this.addRequest(queryRequest, responseOption, homeCommunityId, gatewayConfig, false);
            }
        } else {
            response.add_error(MetadataSupport.XDSUnknownCommunity,
                    "Do not understand homeCommunityId " + homeCommunityId,
                    this.getClass().getName(), log_message);
        }
    }

    /**
     * 
     * @return
     * @throws com.vangent.hieos.xutil.exception.XdsInternalException
     */
    private XConfigRegistry getLocalRegistry() throws XdsInternalException {
        // Get the gateway configuration.
        XConfig xconfig = XConfig.getInstance();
        XConfigHomeCommunity homeCommunity = xconfig.getHomeCommunity();

        // Return the proper registry configuration based upon the gateway configuration.
        XConfigGateway gateway;
        if (this.getGatewayType() == GatewayType.InitiatingGateway) {
            gateway = homeCommunity.getInitiatingGateway();
        } else {
            gateway = homeCommunity.getRespondingGateway();
        }

        // Get the gateway's local registry.
        XConfigRegistry registry = gateway.getLocalRegistry();
        if (registry == null) {
            response.add_error(MetadataSupport.XDSRegistryNotAvailable,
                    "Can not find local registry endpoint",
                    this.getClass().getName(), log_message);
        }
        return registry;
    }

    /**
     *
     * @param queryRequest
     * @return
     */
    private boolean requiresHomeCommunityId(OMElement queryRequest) {
        boolean requires = true;
        String queryId = this.getStoredQueryId(queryRequest);
        if (queryId == null) {
            requires = false;
        }
        if (queryId.equals(MetadataSupport.SQ_FindDocuments)) {
            requires = false;
        }
        if (queryId.equals(MetadataSupport.SQ_FindFolders)) {
            requires = false;
        }
        if (queryId.equals(MetadataSupport.SQ_FindSubmissionSets)) {
            requires = false;
        }
        if (queryId.equals(MetadataSupport.SQ_GetAll)) {
            requires = false;
        }
        this.logInfo("Note", "query " + queryId + " requires homeCommunityId = " + requires);
        return requires;
    }

    /**
     *
     * @param queryRequest - <AdhocQuery> XML node
     * @return
     */
    private String getStoredQueryId(OMElement queryRequest) {
        return queryRequest.getAttributeValue(MetadataSupport.id_qname);
    }

    /**
     * Return home community id on request.  Return null if not present.
     *
     * @param queryRequest - <AdhocQuery> XML node
     * @return homeCommunitId string if present, otherwise null.
     */
    private String getHomeCommunityId(OMElement queryRequest) {
        String homeCommunityId = queryRequest.getAttributeValue(MetadataSupport.home_qname);
        if (homeCommunityId == null || homeCommunityId.equals("")) {
            homeCommunityId = null;
        }
        return homeCommunityId;
    }

    /**
     * 
     * @param queryRequest
     * @param responseOption
     * @param uniqueId
     * @param configEntity
     * @param isLocalRequest
     * @return
     */
    private XCAQueryRequest addRequest(OMElement queryRequest, OMElement responseOption, String uniqueId, XConfigEntity configEntity, boolean isLocalRequest) {
        XCARequestController requestController = this.getRequestController();
        // FIXME: Logic is a bit problematic -- need to find another way.
        XCAAbstractRequestCollection requestCollection = requestController.getRequestCollection(uniqueId);
        if (requestCollection == null) {
            requestCollection = new XCAQueryRequestCollection(uniqueId, configEntity, isLocalRequest);
            requestController.setRequestCollection(requestCollection);
        }

        XCAQueryRequest xcaRequest = new XCAQueryRequest(queryRequest);
        requestCollection.addRequest(xcaRequest);
        xcaRequest.setResponseOption(responseOption);  // Need this also!!
        return xcaRequest;
    }

    /**
     *
     * @param allResponses
     * @throws com.vangent.hieos.xutil.exception.XdsInternalException
     */
    protected boolean consolidateResponses(ArrayList<OMElement> allResponses) throws XdsInternalException {
        boolean atLeastOneSuccess = false;

        // FIXME: Should we Util.deep_copy() here?
        //OMElement rootResponseNode = response.getRawResponse();  // e.g. <AdhocQueryResponse>
        for (OMElement responseNode : allResponses) {
            // See if the registry response has a success status.
            String status = responseNode.getAttributeValue(MetadataSupport.status_qname);
            this.logInfo("Note", "*** Response Status = " + status + " ***");
            if (status.endsWith("Success")) {
                atLeastOneSuccess = true;
            }

            // Should only be one <RegistryObjectList> at most, but loop anyway.
            ArrayList<OMElement> regObjListNodes = MetadataSupport.decendentsWithLocalName(responseNode, "RegistryObjectList");
            for (OMElement regObjList : regObjListNodes) {
                // Add each child of <RegistryObjectList> to the query result.
                for (Iterator it = regObjList.getChildren(); it.hasNext();) {
                    // DEBUG (START)
                    Object nextNode = it.next();
                    // DEBUG (END)
                    OMElement queryResultNode = null;
                    try {
                        queryResultNode = (OMElement) nextNode;
                    } catch (Exception e) {
                        OMText textNode = (OMText) nextNode;
                        // Only have seen this problem with Intersystems XCA
                        logger.error("***** BUG: " + nextNode.getClass().getName());
                        logger.error(" -- Node -- ");
                        logger.error("isBinary: " + textNode.isBinary());
                        logger.error("isCharacters: " + textNode.isCharacters());
                        logger.error("isOptimized: " + textNode.isOptimized());
                        logger.error(textNode.getText());
                    }
                    response.addQueryResults(queryResultNode);
                }
            }

            // Consolidate all registry errors into the consolidated error list.
            ArrayList<OMElement> registryErrorLists = MetadataSupport.decendentsWithLocalName(responseNode, "RegistryErrorList");

            // Should only be one <RegistryErrorList> at most, but loop anyway.
            for (OMElement registryErrorList : registryErrorLists) {
                response.addRegistryErrorList(registryErrorList, null);  // Place into the final list.
            }

        }
        return atLeastOneSuccess;
    }
}
