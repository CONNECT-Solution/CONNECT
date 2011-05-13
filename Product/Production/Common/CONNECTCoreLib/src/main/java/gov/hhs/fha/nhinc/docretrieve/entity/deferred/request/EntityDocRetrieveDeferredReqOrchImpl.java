/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.docretrieve.entity.deferred.request;

import gov.hhs.fha.nhinc.asyncmsgs.dao.AsyncMsgRecordDao;
import gov.hhs.fha.nhinc.asyncmsgs.model.AsyncMsgRecord;
import gov.hhs.fha.nhinc.common.eventcommon.DocRetrieveEventType;
import gov.hhs.fha.nhinc.common.eventcommon.DocRetrieveMessageType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayRetrieveRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayRetrieveSecuredRequestType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.connectmgr.data.CMUrlInfo;
import gov.hhs.fha.nhinc.connectmgr.data.CMUrlInfos;
import gov.hhs.fha.nhinc.docretrieve.DocRetrieveDeferredAuditLogger;
import gov.hhs.fha.nhinc.docretrieve.passthru.deferred.request.proxy.PassthruDocRetrieveDeferredReqProxyObjectFactory;
import gov.hhs.fha.nhinc.docretrieve.passthru.deferred.request.proxy.PassthruDocRetrieveDeferredReqProxy;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.policyengine.PolicyEngineChecker;
import gov.hhs.fha.nhinc.policyengine.adapter.proxy.PolicyEngineProxy;
import gov.hhs.fha.nhinc.policyengine.adapter.proxy.PolicyEngineProxyObjectFactory;
import gov.hhs.fha.nhinc.transform.marshallers.JAXBContextHandler;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType.DocumentRequest;
import java.io.ByteArrayOutputStream;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;

/**
 * Implementation class for Entity Document Retrieve Deferred request message
 * @author Sai Valluripalli
 */
public class EntityDocRetrieveDeferredReqOrchImpl {

    private Log log = null;
    private boolean debugEnabled = false;

    /**
     * Constructor
     */
    public EntityDocRetrieveDeferredReqOrchImpl() {
        log = createLogger();
        debugEnabled = log.isDebugEnabled();
    }

    /**
     *
     * @return Log
     */
    private Log createLogger() {
        return (log != null) ? log : LogFactory.getLog(this.getClass());
    }

    /**
     * Entity Implementation method
     * @param crossGatewayRetrieveRequest
     * @return DocRetrieveAcknowledgementType
     */
    public DocRetrieveAcknowledgementType crossGatewayRetrieveRequest(RetrieveDocumentSetRequestType message,
            AssertionType assertion,
            NhinTargetCommunitiesType target) {
        if (debugEnabled) {
            log.debug("Begin EntityDocRetrieveDeferredRequestImpl.crossGatewayRetrieveRequest");
        }
        DocRetrieveAcknowledgementType nhincResponse = null;
        String homeCommunityId = null;
        DocRetrieveDeferredAuditLogger auditLog = new DocRetrieveDeferredAuditLogger();
        try {
            if ((null != message) && (message.getDocumentRequest() != null) && (message.getDocumentRequest().size() > 0)) {
                DocumentRequest docRequest = message.getDocumentRequest().get(0);
                homeCommunityId = docRequest.getHomeCommunityId();
                auditLog.auditDocRetrieveDeferredRequest(message, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE, assertion, homeCommunityId);
                RespondingGatewayCrossGatewayRetrieveSecuredRequestType nhinDocRetrieveMsg = new RespondingGatewayCrossGatewayRetrieveSecuredRequestType();
                // Set document request
                RetrieveDocumentSetRequestType nhinDocRequest = new RetrieveDocumentSetRequestType();
                nhinDocRetrieveMsg.setRetrieveDocumentSetRequest(nhinDocRequest);
                nhinDocRequest.getDocumentRequest().add(docRequest);
                nhinDocRetrieveMsg.setNhinTargetSystem(buildHomeCommunity(docRequest.getHomeCommunityId()));

                //If target is null set the doc request target community.
                if (target == null) {
                    target = createTargetCommunities(docRequest.getHomeCommunityId());
                }

                CMUrlInfos urlInfoList = getEndpoints(target);
                NhinTargetSystemType oTargetSystem = null;
                //loop through the communities and send request if results were not null
                if ((urlInfoList == null) || (urlInfoList.getUrlInfo() != null && urlInfoList.getUrlInfo().isEmpty())) {
                    log.warn("No targets were found for the Document retrieve deferred service Request");
                    nhincResponse = buildRegistryErrorAck();
                } else {
                    nhincResponse = new DocRetrieveAcknowledgementType();
                    if (debugEnabled) {
                        log.debug("Creating NHIN doc retrieve proxy");
                    }
                    PassthruDocRetrieveDeferredReqProxyObjectFactory objFactory = new PassthruDocRetrieveDeferredReqProxyObjectFactory();
                    PassthruDocRetrieveDeferredReqProxy docRetrieveProxy = objFactory.getNhincProxyDocRetrieveDeferredReqProxy();

                    for (CMUrlInfo urlInfo : urlInfoList.getUrlInfo()) {
                        if (isPolicyValid(nhinDocRequest, assertion, urlInfo.getHcid())) {

                            //add the Request to the Initiator AsyncMsgs Table.
                            RespondingGatewayCrossGatewayRetrieveRequestType respondingGatewayCrossGatewayRetreiveRequest = new RespondingGatewayCrossGatewayRetrieveRequestType();
                            respondingGatewayCrossGatewayRetreiveRequest.setRetrieveDocumentSetRequest(message);
                            respondingGatewayCrossGatewayRetreiveRequest.setAssertion(assertion);
                            addEntryToDatabase(respondingGatewayCrossGatewayRetreiveRequest);

                            oTargetSystem = new NhinTargetSystemType();
                            oTargetSystem.setUrl(urlInfo.getUrl());
                            // Call NHIN proxy
                            if (debugEnabled) {
                                log.debug("Calling doc retrieve proxy");
                            }
                            nhincResponse = docRetrieveProxy.crossGatewayRetrieveRequest(message, assertion,
                                    oTargetSystem);
                        } else {
                            nhincResponse = buildRegistryErrorAck();
                        }
                    }



                }
            }
        } catch (Exception ex) {
            log.error("Error sending doc retrieve deferred message..." + ex.getMessage());
            nhincResponse = buildRegistryErrorAck();
            log.error("Fault encountered processing internal document retrieve deferred for community " + homeCommunityId);
        }
        if (null != nhincResponse) {
            // Audit log - response
            auditLog.auditDocRetrieveDeferredAckResponse(nhincResponse.getMessage(), assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE);
        }
        if (debugEnabled) {
            log.debug("End EntityDocRetrieveDeferredRequestImpl.crossGatewayRetrieveRequest");
        }
        return nhincResponse;
    }

    /**
     *
     * @return DocRetrieveAcknowledgementType
     */
    private DocRetrieveAcknowledgementType buildRegistryErrorAck() {
        DocRetrieveAcknowledgementType nhinResponse = new DocRetrieveAcknowledgementType();
        RegistryResponseType registryResponse = new RegistryResponseType();
        nhinResponse.setMessage(registryResponse);
        registryResponse.setStatus("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure");
        return nhinResponse;
    }

    /**
     *
     * @param homeCommunityId
     * @return NhinTargetSystemType
     */
    private NhinTargetSystemType buildHomeCommunity(String homeCommunityId) {
        NhinTargetSystemType nhinTargetSystem = new NhinTargetSystemType();
        HomeCommunityType homeCommunity = new HomeCommunityType();
        homeCommunity.setHomeCommunityId(homeCommunityId);
        nhinTargetSystem.setHomeCommunity(homeCommunity);
        return nhinTargetSystem;
    }

    /**
     * Policy Engine check
     * @param body
     * @param assertion
     * @return boolean
     */
    private boolean isPolicyValid(RetrieveDocumentSetRequestType oEachNhinRequest, AssertionType oAssertion, String hcId) {
        boolean isValid = false;
        DocRetrieveEventType checkPolicy = new DocRetrieveEventType();
        DocRetrieveMessageType checkPolicyMessage = new DocRetrieveMessageType();
        checkPolicyMessage.setRetrieveDocumentSetRequest(oEachNhinRequest);
        checkPolicyMessage.setAssertion(oAssertion);
        checkPolicy.setMessage(checkPolicyMessage);
        checkPolicy.setDirection(NhincConstants.POLICYENGINE_OUTBOUND_DIRECTION);
        checkPolicy.setInterface(NhincConstants.AUDIT_LOG_ENTITY_INTERFACE);
        HomeCommunityType homeCommunity = new HomeCommunityType();
        homeCommunity.setHomeCommunityId(hcId);
        checkPolicy.setReceivingHomeCommunity(homeCommunity);
        PolicyEngineChecker policyChecker = new PolicyEngineChecker();
        CheckPolicyRequestType policyReq = policyChecker.checkPolicyDocRetrieve(checkPolicy);
        PolicyEngineProxyObjectFactory policyEngFactory = new PolicyEngineProxyObjectFactory();
        PolicyEngineProxy policyProxy = policyEngFactory.getPolicyEngineProxy();
        CheckPolicyResponseType policyResp = policyProxy.checkPolicy(policyReq, oAssertion);
        /* if response='permit' */
        if (policyResp.getResponse().getResult().get(0).getDecision().value().equals(NhincConstants.POLICY_PERMIT)) {
            isValid = true;
        }
        return isValid;
    }

    /**
     *
     * @param targetCommunities
     * @return CMUrlInfos
     */
    protected CMUrlInfos getEndpoints(NhinTargetCommunitiesType targetCommunities) {
        CMUrlInfos urlInfoList = null;

        try {
            urlInfoList = ConnectionManagerCache.getEndpontURLFromNhinTargetCommunities(targetCommunities, NhincConstants.NHIN_DOCRETRIEVE_DEFERRED_REQUEST);
        } catch (ConnectionManagerException ex) {
            log.error("Failed to obtain target URLs", ex);
        }

        return urlInfoList;
    }

    /**
     *
     * @param request
     * @return void
     */
    protected void addEntryToDatabase(RespondingGatewayCrossGatewayRetrieveRequestType request) {
        log.debug("EntityDocRetrieveDeferredReqOrchImpl :addEntryToDatabase : Begin");
        List<AsyncMsgRecord> asyncMsgRecs = new ArrayList<AsyncMsgRecord>();
        AsyncMsgRecord rec = new AsyncMsgRecord();
        AsyncMsgRecordDao instance = new AsyncMsgRecordDao();

        // Replace with message id from the assertion class
        rec.setMessageId(request.getAssertion().getMessageId());
        rec.setCreationTime(new Date());
        rec.setServiceName(NhincConstants.DOC_RETRIEVE_SERVICE_NAME);
        rec.setMsgData(createBlob(request));
        asyncMsgRecs.add(rec);

        boolean result = instance.insertRecords(asyncMsgRecs);

        if (result == false) {
            log.error("Failed to insert asynchronous record in the database");
        }
        log.debug("EntityDocRetrieveDeferredReqOrchImpl :addEntryToDatabase : End ");
    }

    /**
     *
     * @param request
     * @return Blob
     */
    private Blob createBlob(RespondingGatewayCrossGatewayRetrieveRequestType request) {
        Blob asyncMessage = null;
        try {
            log.debug("EntityDocRetrieveDeferredReqOrchImpl :createBlob : Begin");
            JAXBContextHandler oHandler = new JAXBContextHandler();
            JAXBContext jc = oHandler.getJAXBContext("gov.hhs.fha.nhinc.common.nhinccommonentity");
            Marshaller marshaller = jc.createMarshaller();
            ByteArrayOutputStream baOutStrm = new ByteArrayOutputStream();
            baOutStrm.reset();
            gov.hhs.fha.nhinc.common.nhinccommonentity.ObjectFactory factory = new gov.hhs.fha.nhinc.common.nhinccommonentity.ObjectFactory();
            JAXBElement<RespondingGatewayCrossGatewayRetrieveRequestType> oJaxbElement = factory.createRespondingGatewayCrossGatewayRetrieveRequest(request);
            baOutStrm.close();
            marshaller.marshal(oJaxbElement, baOutStrm);
            byte[] buffer = baOutStrm.toByteArray();
            asyncMessage = Hibernate.createBlob(buffer);
            log.debug("EntityDocRetrieveDeferredReqOrchImpl :createBlob : End");
        } catch (Exception e) {
            log.error("Exception during Blob conversion :" + e.getMessage());
            e.printStackTrace();
        }
        return asyncMessage;
    }

    private NhinTargetCommunitiesType createTargetCommunities(String homeCommunityId) {
        NhinTargetCommunitiesType result = new NhinTargetCommunitiesType();
        NhinTargetCommunityType community = new NhinTargetCommunityType();
        HomeCommunityType hc = new HomeCommunityType();
        hc.setHomeCommunityId(homeCommunityId);
        community.setHomeCommunity(hc);
        result.getNhinTargetCommunity().add(community);

        return result;

    }
}
