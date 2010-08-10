package gov.hhs.fha.nhinc.docretrieve.entity.deferred.response;

import gov.hhs.fha.nhinc.async.AsyncMessageIdExtractor;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayRetrieveResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayRetrieveSecuredResponseType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.connectmgr.data.CMUrlInfo;
import gov.hhs.fha.nhinc.connectmgr.data.CMUrlInfos;
import gov.hhs.fha.nhinc.docretrieve.DocRetrieveDeferredAuditLogger;
import gov.hhs.fha.nhinc.docretrieve.DocRetrieveDeferredPolicyChecker;
import gov.hhs.fha.nhinc.docretrieve.passthru.deferred.response.proxy.NhincProxyDocRetrieveDeferredRespObjectFactory;
import gov.hhs.fha.nhinc.docretrieve.passthru.deferred.response.proxy.NhincProxyDocRetrieveDeferredRespProxy;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import javax.xml.ws.WebServiceContext;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Sai Valluripalli
 */
public class EntityDocRetrieveDeferredRespOrchImpl {

    private Log log = null;
    private boolean debugEnabled = false;

    /**
     * default constructor
     */
    public EntityDocRetrieveDeferredRespOrchImpl() {
        log = createLogger();
        debugEnabled = false;
    }

    /**
     *
     * @return Log
     */
    protected Log createLogger() {
        return (log != null) ? log : LogFactory.getLog(this.getClass());
    }

    /**
     * 
     * @param crossGatewayRetrieveResponse
     * @param context
     * @return DocRetrieveAcknowledgementType
     */
    public DocRetrieveAcknowledgementType crossGatewayRetrieveResponse(RespondingGatewayCrossGatewayRetrieveResponseType crossGatewayRetrieveResponse, WebServiceContext context) {
        if (debugEnabled) {
            log.debug("-- Begin EntityDocRetrieveDeferredRespOrchImpl.crossGatewayRetrieveResponse(..Unsecured..) --");
        }
        DocRetrieveAcknowledgementType ack = null;
        if (null != crossGatewayRetrieveResponse) {
            RetrieveDocumentSetResponseType response = crossGatewayRetrieveResponse.getRetrieveDocumentSetResponse();
            AssertionType assertion = crossGatewayRetrieveResponse.getAssertion();
            if (null != assertion) {
                assertion.setMessageId(AsyncMessageIdExtractor.GetAsyncMessageId(context));
                assertion.getRelatesToList().addAll(AsyncMessageIdExtractor.GetAsyncRelatesTo(context));
            }
            NhinTargetCommunitiesType target = crossGatewayRetrieveResponse.getNhinTargetCommunities();
            ack = this.crossGatewayRetrieveResponse(response, assertion, target);
        } else {
            ack = buildRegistryErrorAck(" ", "Error processing Entity Response due to null response");
        }
        if (debugEnabled) {
            log.debug("-- End EntityDocRetrieveDeferredRespOrchImpl.crossGatewayRetrieveResponse(..Unsecured..) --");
        }
        return ack;
    }

    /**
     * 
     * @param body
     * @param context
     * @return DocRetrieveAcknowledgementType
     */
    public DocRetrieveAcknowledgementType crossGatewayRetrieveResponse(gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayRetrieveSecuredResponseType body, WebServiceContext context) {
        if (debugEnabled) {
            log.debug("-- Begin EntityDocRetrieveDeferredRespOrchImpl.crossGatewayRetrieveResponse(..secured..) --");
        }
        DocRetrieveAcknowledgementType ack = null;
        if (null != body) {
            RetrieveDocumentSetResponseType response = body.getRetrieveDocumentSetResponse();
            AssertionType assertion = SamlTokenExtractor.GetAssertion(context);
            if (null != assertion) {
                assertion.setMessageId(AsyncMessageIdExtractor.GetAsyncMessageId(context));
                assertion.getRelatesToList().addAll(AsyncMessageIdExtractor.GetAsyncRelatesTo(context));
            }
            NhinTargetCommunitiesType target = body.getNhinTargetCommunities();
            ack = this.crossGatewayRetrieveResponse(response, assertion, target);
        } else {
            ack = buildRegistryErrorAck(" ", "Error processing Entity Response due to null response");
        }
        if (debugEnabled) {
            log.debug("-- End EntityDocRetrieveDeferredRespOrchImpl.crossGatewayRetrieveResponse(..secured..) --");
        }
        return ack;
    }

    /**
     * Document Retrieve Deferred Response implementation method
     * @param response
     * @param assertion
     * @param target
     * @return DocRetrieveAcknowledgementType
     */
    public DocRetrieveAcknowledgementType crossGatewayRetrieveResponse(RetrieveDocumentSetResponseType response, AssertionType assertion, NhinTargetCommunitiesType target) {
        if (debugEnabled) {
            log.debug("Begin EntityDocRetrieveDeferredRespOrchImpl.crossGatewayRetrieveResponse");
        }
        DocRetrieveAcknowledgementType nhinResponse = null;
        String homeCommunityId = null;
        DocRetrieveDeferredAuditLogger auditLog = new DocRetrieveDeferredAuditLogger();
        try {
            if (null != response && (null != assertion) && (null != target)) {
                auditLog.auditDocRetrieveDeferredResponse(response, assertion);
                CMUrlInfos urlInfoList = getEndpoints(target);
                NhinTargetSystemType oTargetSystem = null;
                homeCommunityId = getHomeCommFromTarget(target);
                //loop through the communities and send request if results were not null
                if ((urlInfoList == null) || (urlInfoList.getUrlInfo().isEmpty())) {
                    log.warn("No targets were found for the Document retrieve deferred Response service");
                    nhinResponse = buildRegistryErrorAck("", "No targets were found for the Document retrieve deferred Response service");
                } else {
                    if (debugEnabled) {
                        log.debug("Creating NHIN doc retrieve proxy");
                    }
                    NhincProxyDocRetrieveDeferredRespObjectFactory objFactory = new NhincProxyDocRetrieveDeferredRespObjectFactory();
                    NhincProxyDocRetrieveDeferredRespProxy docRetrieveProxy = objFactory.getNhincProxyDocRetrieveDeferredRespProxy();
                    DocRetrieveDeferredPolicyChecker policyCheck = new DocRetrieveDeferredPolicyChecker();
                    for (CMUrlInfo urlInfo : urlInfoList.getUrlInfo()) {
                        // Call NHIN proxy
                        oTargetSystem = new NhinTargetSystemType();
                        oTargetSystem.setUrl(urlInfo.getUrl());
                        if (policyCheck.checkOutgoingPolicy(response, assertion, homeCommunityId)) {
                            RespondingGatewayCrossGatewayRetrieveSecuredResponseType req = new RespondingGatewayCrossGatewayRetrieveSecuredResponseType();
                            req.setRetrieveDocumentSetResponse(response);
                            // Call NHIN proxy
                            auditLog.auditDocRetrieveDeferredResponse(response, assertion);
                            if (debugEnabled) {
                                log.debug("Calling doc retrieve proxy response");
                            }
                            nhinResponse = docRetrieveProxy.crossGatewayRetrieveResponse(response, assertion, oTargetSystem);
                        }

                    }
                }
                if (debugEnabled) {
                    log.debug("Calling Policy Engine");
                }
            }
        } catch (Exception ex) {
            log.error("Error sending doc retrieve deferred message...");
            nhinResponse = buildRegistryErrorAck(homeCommunityId, ex.getMessage());
            log.error("Fault encountered processing internal document retrieve deferred for community " + homeCommunityId);
        }
        if (null != nhinResponse) {
            // Audit log - response
            auditLog.auditDocRetrieveDeferredAckResponse(nhinResponse.getMessage(), assertion, homeCommunityId);
        }
        if (debugEnabled) {
            log.debug("End EntityDocRetrieveDeferredRespOrchImpl.crossGatewayRetrieveResponse");
        }
        return nhinResponse;
    }

    /**
     *
     * @return DocRetrieveAcknowledgementType
     */
    private DocRetrieveAcknowledgementType buildRegistryErrorAck(String homeCommunityId, String error) {
        DocRetrieveAcknowledgementType nhinResponse = new DocRetrieveAcknowledgementType();
        RegistryResponseType registryResponse = new RegistryResponseType();
        nhinResponse.setMessage(registryResponse);
        RegistryErrorList regErrList = new RegistryErrorList();
        RegistryError regErr = new RegistryError();
        regErrList.getRegistryError().add(regErr);
        regErr.setCodeContext(error + " " + homeCommunityId);
        regErr.setErrorCode("XDSRegistryNotAvailable");
        regErr.setSeverity("Error");
        registryResponse.setRegistryErrorList(regErrList);
        registryResponse.setStatus("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure");
        return nhinResponse;
    }

    /**
     *
     * @param target
     * @return String
     */
    protected String getHomeCommFromTarget(NhinTargetCommunitiesType target) {
        String sHomComm = null;
        if (target.getNhinTargetCommunity() != null &&
                target.getNhinTargetCommunity().size() > 0) {
            NhinTargetCommunityType comm = target.getNhinTargetCommunity().get(0);
            if (comm.getHomeCommunity() != null) {
                sHomComm = comm.getHomeCommunity().getHomeCommunityId();
            }
        }
        return sHomComm;
    }

    /**
     *
     * @param targetCommunities
     * @return CMUrlInfos
     */
    protected CMUrlInfos getEndpoints(NhinTargetCommunitiesType targetCommunities) {
        CMUrlInfos urlInfoList = null;

        try {
            urlInfoList = ConnectionManagerCache.getEndpontURLFromNhinTargetCommunities(targetCommunities, NhincConstants.NHIN_ADMIN_DIST_SERVICE_NAME);
        } catch (ConnectionManagerException ex) {
            log.error("Failed to obtain target URLs", ex);
        }

        return urlInfoList;
    }
    
}
