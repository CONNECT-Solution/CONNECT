/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.hiem.processor.entity.handler;

import gov.hhs.fha.nhinc.common.connectionmanager.dao.AssigningAuthorityHomeCommunityMappingDAO;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import org.oasis_open.docs.wsn.b_2.Subscribe;
import org.oasis_open.docs.wsn.bw_2.InvalidTopicExpressionFault;
import org.oasis_open.docs.wsn.bw_2.SubscribeCreationFailedFault;
import org.oasis_open.docs.wsn.bw_2.TopicNotSupportedFault;
import org.w3._2005._08.addressing.EndpointReferenceType;
import org.w3c.dom.Element;
import org.oasis_open.docs.wsn.b_2.SubscribeResponse;
import javax.xml.ws.wsaddressing.W3CEndpointReference;
import gov.hhs.fha.nhinc.hiem.configuration.topicconfiguration.TopicConfigurationEntry;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.proxy.PatientCorrelationProxy;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.proxy.PatientCorrelationProxyObjectFactory;
import gov.hhs.fha.nhinc.common.patientcorrelationfacade.RetrievePatientCorrelationsRequestType;
import java.util.List;
import gov.hhs.fha.nhinc.common.nhinccommon.QualifiedSubjectIdentifierType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.connectmgr.data.CMUrlInfo;
import gov.hhs.fha.nhinc.connectmgr.data.CMUrlInfos;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.xmlCommon.XmlUtility;
import org.hl7.v3.PRPAIN201309UV02;
import org.hl7.v3.RetrievePatientCorrelationsResponseType;
import org.hl7.v3.II;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.parsers.PRPAIN201309UV.PixRetrieveBuilder;
import java.util.ArrayList;

/**
 * Entity subscribe processor for subscribe messages that are patient related.
 * A patient identifier is expected in the subscribe message at the location
 * specified by the topic configuration.
 *
 * @author Neil Webb
 */
class PatientCentricEntitySubscribeHandler extends BaseEntitySubscribeHandler {

    public SubscribeResponse handleSubscribe(TopicConfigurationEntry topicConfig, Subscribe subscribe, Element subscribeElement, AssertionType assertion, NhinTargetCommunitiesType targetCommunitites) throws TopicNotSupportedFault, InvalidTopicExpressionFault, SubscribeCreationFailedFault {
        log.debug("In handleSubscribe - patient id: " + patientIdentifier.getSubjectIdentifier() + ", assigning authority: " + patientIdentifier.getAssigningAuthorityIdentifier());
        SubscribeResponse response = new SubscribeResponse();
        CMUrlInfos urlInfoList = null;

        // Store initial subscription received from agency adapter (the parent subscription)
        EndpointReferenceType parentSubscriptionReference = storeSubscription(subscribe, subscribeElement, assertion, targetCommunitites);
        String parentSubscriptionReferenceXml = null;
        if (parentSubscriptionReference != null) {
            parentSubscriptionReferenceXml = serializeEndpointReferenceType(parentSubscriptionReference);
        }

        // Obtain all the URLs for the targets being sent to
        try {
            urlInfoList = ConnectionManagerCache.getEndpontURLFromNhinTargetCommunities(targetCommunitites, NhincConstants.HIEM_SUBSCRIBE_SERVICE_NAME);
        } catch (ConnectionManagerException ex) {
            log.error("Failed to obtain target URLs");
            return null;
        }

        // Find patient correlations
        List<QualifiedSubjectIdentifierType> correlations = determineTargets(urlInfoList);

        // Common message preparation

        int correlationCount = 1;
        if (correlations != null) {
            log.debug("Processing correlation #" + correlationCount++);
            AssigningAuthorityHomeCommunityMappingDAO assigningAuthorityDao = new AssigningAuthorityHomeCommunityMappingDAO();
            for (QualifiedSubjectIdentifierType correlation : correlations) {
                CMUrlInfo community = new CMUrlInfo();
                String remoteCommunityId = assigningAuthorityDao.getHomeCommunityId(correlation.getAssigningAuthorityIdentifier());
                if (log.isDebugEnabled()) {
                    log.debug("Remote assigning authority id: " + correlation.getAssigningAuthorityIdentifier());
                    log.debug("Mapped remote community id: " + remoteCommunityId);
                }

                community = findTarget(remoteCommunityId, urlInfoList);

                if (community == null) {
                    continue;
                }

                //      Update Subscribe
                updateSubscribe(subscribeElement, correlation);
                if (log.isDebugEnabled()) {
                    log.debug("@@@ Updated subscribe: " + XmlUtility.serializeElementIgnoreFaults(subscribeElement));
                }
                //      Policy check - performed in proxy?
                //      Audit Event - performed in proxy?
                //      Send Subscribe
                Element childSubscribeElement = subscribeElement;
                SubscribeResponse subscribeResponse = sendSubscribeRequest(childSubscribeElement, assertion, community);
//
                //      Store subscription
                if (subscribeResponse != null) {
                    String childSubscriptionReference = null;

                    // Use reflection to get the correct subscription reference object
                    Object subRef = getSubscriptionReference(subscribeResponse);
                    if (subRef != null) {
                        if (subRef.getClass().isAssignableFrom(EndpointReferenceType.class)) {
                            childSubscriptionReference = serializeEndpointReferenceType((EndpointReferenceType) subRef);
                        } else if (subRef.getClass().isAssignableFrom(W3CEndpointReference.class)) {
                            childSubscriptionReference = serializeW3CEndpointReference((W3CEndpointReference) subRef);
                        } else {
                            log.error("Unknown subscription reference type: " + subRef.getClass().getName());
                        }

                        String childSubscribeXml;
                        try {
                            childSubscribeXml = XmlUtility.serializeElement(childSubscribeElement);
                        } catch (Exception ex) {
                            log.error("failed to process subscribe xml", ex);
                            childSubscribeXml = null;
                        }

                        storeChildSubscription(childSubscribeXml, childSubscriptionReference, parentSubscriptionReferenceXml);
                    } else {
                        log.error("Subscription reference was null");
                    }
                } else {
                    log.error("The subscribe response message was null.");
                }
            }
        }
        setSubscriptionReference(response, parentSubscriptionReference);

        return response;
    }

    private List<QualifiedSubjectIdentifierType> determineTargets(CMUrlInfos targets) {
        List<QualifiedSubjectIdentifierType> correlations = new ArrayList<QualifiedSubjectIdentifierType>();
        RetrievePatientCorrelationsRequestType request = new RetrievePatientCorrelationsRequestType();
        request.setQualifiedPatientIdentifier(patientIdentifier);

        if ((targets != null) && (NullChecker.isNotNullish(targets.getUrlInfo()))) {
            for (CMUrlInfo targetCommunity : targets.getUrlInfo()) {
                request.getTargetHomeCommunity().add(targetCommunity.getHcid());
            }
        }
        PRPAIN201309UV02 patCorrelationRequest = PixRetrieveBuilder.createPixRetrieve(request);

        PatientCorrelationProxy proxy = new PatientCorrelationProxyObjectFactory().getPatientCorrelationProxy();
        RetrievePatientCorrelationsResponseType response = proxy.retrievePatientCorrelations(patCorrelationRequest, new AssertionType());

        if (response != null &&
                response.getPRPAIN201310UV02() != null &&
                response.getPRPAIN201310UV02().getControlActProcess() != null &&
                NullChecker.isNotNullish(response.getPRPAIN201310UV02().getControlActProcess().getSubject()) &&
                response.getPRPAIN201310UV02().getControlActProcess().getSubject().get(0) != null &&
                response.getPRPAIN201310UV02().getControlActProcess().getSubject().get(0).getRegistrationEvent() != null &&
                response.getPRPAIN201310UV02().getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1() != null &&
                response.getPRPAIN201310UV02().getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient() != null &&
                NullChecker.isNotNullish(response.getPRPAIN201310UV02().getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId())) {
            for (II id : response.getPRPAIN201310UV02().getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId()) {
                QualifiedSubjectIdentifierType subId = new QualifiedSubjectIdentifierType();
                subId.setAssigningAuthorityIdentifier(id.getRoot());
                subId.setSubjectIdentifier(id.getExtension());
                correlations.add(subId);
            }

        }

        return correlations;
    }

    private CMUrlInfo findTarget (String hcid, CMUrlInfos urlInfoList) {
        CMUrlInfo target = null;

        for (CMUrlInfo entry : urlInfoList.getUrlInfo()) {
            if (entry.getHcid().equalsIgnoreCase(hcid)) {
                target = entry;
            }
        }
        return target;
    }
}
