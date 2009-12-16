/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.subjectdiscovery.entity;

import gov.hhs.fha.nhinc.policyengine.PolicyEngineChecker;
import gov.hhs.fha.nhinc.policyengine.proxy.PolicyEngineProxy;
import gov.hhs.fha.nhinc.policyengine.proxy.PolicyEngineProxyObjectFactory;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import gov.hhs.fha.nhinc.common.eventcommon.SubjectReidentificationEventType;
import gov.hhs.fha.nhinc.common.eventcommon.SubjectReidentificationMessageType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.QualifiedSubjectIdentifierType;
import gov.hhs.fha.nhinc.common.nhinccommon.UserType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.connectionmanager.dao.AssigningAuthorityHomeCommunityMappingDAO;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.connectmgr.data.CMHomeCommunity;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.common.patientcorrelationfacade.RetrievePatientCorrelationsRequestType;
import gov.hhs.fha.nhinc.common.patientcorrelationfacade.RetrievePatientCorrelationsResponseType;
import gov.hhs.fha.nhinc.common.patientcorrelationfacade.RemovePatientCorrelationRequestType;
import gov.hhs.fha.nhinc.common.patientcorrelationfacade.RemovePatientCorrelationResponseType;
import gov.hhs.fha.nhinc.patientcorrelationfacade.proxy.PatientCorrelationFacadeProxy;
import gov.hhs.fha.nhinc.patientcorrelationfacade.proxy.PatientCorrelationFacadeProxyObjectFactory;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import gov.hhs.fha.nhinc.subjectdiscovery.SubjectDiscoveryAuditLog;
import gov.hhs.fha.nhinc.subjectdiscovery.proxy.NhincProxySubjectDiscoveryImpl;
import gov.hhs.fha.nhinc.transform.subdisc.HL7PRPA201301Transforms;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PIXConsumerPRPAIN201301UVRequestType;
import org.hl7.v3.PIXConsumerPRPAIN201301UVProxyRequestType;
import org.hl7.v3.PIXConsumerPRPAIN201309UVRequestType;
import org.hl7.v3.PIXConsumerPRPAIN201309UVProxyRequestType;
import org.hl7.v3.PIXConsumerMCCIIN000002UV01RequestType;
import javax.xml.bind.JAXBElement;
import java.util.List;
import java.util.ArrayList;
import javax.xml.ws.WebServiceContext;
import org.hl7.v3.PIXConsumerPRPAIN201301UVSecuredRequestType;
import org.hl7.v3.PIXConsumerPRPAIN201309UVResponseType;
import org.hl7.v3.PIXConsumerPRPAIN201309UVSecuredRequestType;
import org.hl7.v3.PRPAIN201310UV02;

/**
 *
 * @author mflynn02
 */
public class EntitySubjectDiscoveryImpl {

    private static Log log = LogFactory.getLog(EntitySubjectDiscoveryImpl.class);
    private static String localHomeCommunity = null;

    public MCCIIN000002UV01 pixConsumerPRPAIN201301UV(PIXConsumerPRPAIN201301UVSecuredRequestType pixConsumerPRPAIN201301UVRequest, WebServiceContext context) {
        log.debug("EntitySubjectDiscoveryImpl.pixConsumerPRPAIN201301UV -- Begin");
        MCCIIN000002UV01 response = new MCCIIN000002UV01();
        boolean isTargeted = false;
        List<NhinTargetCommunityType> targets = null;
        List<CMHomeCommunity> communities = new ArrayList<CMHomeCommunity>();
        NhincProxySubjectDiscoveryImpl nhinSubjectDiscovery = new NhincProxySubjectDiscoveryImpl();

        PIXConsumerPRPAIN201301UVRequestType request = new PIXConsumerPRPAIN201301UVRequestType();
        request.setAssertion(SamlTokenExtractor.GetAssertion(context));
        request.setNhinTargetCommunities(pixConsumerPRPAIN201301UVRequest.getNhinTargetCommunities());
        request.setPRPAIN201301UV02(pixConsumerPRPAIN201301UVRequest.getPRPAIN201301UV02());

        /* Log the 201301 message received */
        SubjectDiscoveryAuditLog auditLog = new SubjectDiscoveryAuditLog();
        AcknowledgementType ack = auditLog.audit(request);

        // Get the local home community id
        try {
            localHomeCommunity = PropertyAccessor.getProperty(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.HOME_COMMUNITY_ID_PROPERTY);
        } catch (PropertyAccessException ex) {
            log.error(ex.getMessage());
            return null;
        }

        // Determine if this is a targeted Subject Added request or not.
        if (pixConsumerPRPAIN201301UVRequest.getNhinTargetCommunities() != null &&
                NullChecker.isNotNullish(pixConsumerPRPAIN201301UVRequest.getNhinTargetCommunities().getNhinTargetCommunity())) {
            targets = pixConsumerPRPAIN201301UVRequest.getNhinTargetCommunities().getNhinTargetCommunity();
            // Transfer NhinTargetCommunities to List<CMHomeCommunity>
            for (NhinTargetCommunityType t : targets) {
                CMHomeCommunity newHome = new CMHomeCommunity();
                newHome.setDescription(t.getHomeCommunity().getDescription());
                newHome.setHomeCommunityId(t.getHomeCommunity().getHomeCommunityId());
                newHome.setName(t.getHomeCommunity().getName());
                communities.add(newHome);
            }
            isTargeted = true;
        }
        if (!isTargeted) {
            // Get all the Communities from ConnectionManager
            try {
                communities = ConnectionManagerCache.getAllCommunities();
            } catch (ConnectionManagerException ex) {
                log.error(ex.getMessage());
                return null;
            }
        }
        log.debug("EntitySubjectDiscoveryImpl.pixConsumerPRPAIN201301UV -- number communities : " + communities.size());

        for (CMHomeCommunity h : communities) {
            boolean patientHasCorrelation = false;
            if (!h.getHomeCommunityId().equals(localHomeCommunity)) {
                log.debug(h.getHomeCommunityId() + " != " + localHomeCommunity);
                // Use the HCID to get Assigning Authority
                AssigningAuthorityHomeCommunityMappingDAO dao = new AssigningAuthorityHomeCommunityMappingDAO();
                List<String> aaResponse = dao.getAssigningAuthoritiesByHomeCommunity(h.getHomeCommunityId());

                // For each AA, look for patient correlations
                if (aaResponse != null && aaResponse.size() > 0) {
                    for (String aa : aaResponse) {
                        QualifiedSubjectIdentifierType qualifiedSubject = assignQualifiedSubjectFrom201301(pixConsumerPRPAIN201301UVRequest.getPRPAIN201301UV02());
                        RetrievePatientCorrelationsRequestType patientRetrieveRequest = new RetrievePatientCorrelationsRequestType();
                        patientRetrieveRequest.setQualifiedPatientIdentifier(qualifiedSubject);
                        patientRetrieveRequest.getTargetAssigningAuthority().add(aa);
                        patientRetrieveRequest.setAssertion(request.getAssertion());

                        // Retreive Patient Correlations this patient
                        PatientCorrelationFacadeProxyObjectFactory patCorrelationFactory = new PatientCorrelationFacadeProxyObjectFactory();
                        PatientCorrelationFacadeProxy proxy = patCorrelationFactory.getPatientCorrelationFacadeProxy();
                        RetrievePatientCorrelationsResponseType results = proxy.retrievePatientCorrelations(patientRetrieveRequest);

                        if (results.getQualifiedPatientIdentifier().size() > 0) {
                            patientHasCorrelation = true;
                            log.debug("EntitySubjectDiscoveryImpl.pixConsumerPRPAIN201301UV -- found patientCorrelation");
                            break;
                        }
                    }
                }

                // If no correlation, then send the request to the community.
                if (!patientHasCorrelation) {
                    log.debug("EntitySubjectDiscoveryImpl.pixConsumerPRPAIN201301UV -- No patientCorrelation; send to community : " +
                            h.getHomeCommunityId());
                    PIXConsumerPRPAIN201301UVProxyRequestType proxyRequest = createProxy201301(request);

                    gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType nhinTarget = new gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType();
                    HomeCommunityType homeCommunity = new HomeCommunityType();
                    homeCommunity.setDescription(h.getDescription());
                    homeCommunity.setHomeCommunityId(h.getHomeCommunityId());
                    homeCommunity.setName(h.getName());
                    nhinTarget.setHomeCommunity(homeCommunity);
                    proxyRequest.setNhinTargetSystem(nhinTarget);
                    log.debug("EntitySubjectDiscoveryImpl.pixConsumerPRPAIN201301UV -- Call to nhinSubjectDiscovery");
                    log.debug("proxyRequest >>" + proxyRequest + "<<");
                    response = nhinSubjectDiscovery.pixConsumerPRPAIN201301UV(proxyRequest);
                }
            }
        }

        /* Log the response */
        PIXConsumerMCCIIN000002UV01RequestType responseType = new PIXConsumerMCCIIN000002UV01RequestType();
        responseType.setMCCIIN000002UV01(response);
        responseType.setAssertion(request.getAssertion());
        ack = auditLog.audit(responseType);

        log.debug("EntitySubjectDiscoveryImpl.pixConsumerPRPAIN201301UV -- End");
        return response;
    }

    public PIXConsumerPRPAIN201309UVResponseType pixConsumerPRPAIN201309UV(PIXConsumerPRPAIN201309UVSecuredRequestType pixConsumerPRPAIN201309UVRequest, WebServiceContext context) {
        log.debug("EntitySubjectDiscoveryImpl.pixConsumerPRPAIN201309UV -- Begin");

        PIXConsumerPRPAIN201309UVResponseType response = new PIXConsumerPRPAIN201309UVResponseType();
        NhincProxySubjectDiscoveryImpl nhinSubjectDiscovery = new NhincProxySubjectDiscoveryImpl();
        PRPAIN201310UV02 proxyResponse;

        PIXConsumerPRPAIN201309UVRequestType request = new PIXConsumerPRPAIN201309UVRequestType();
        request.setAssertion(SamlTokenExtractor.GetAssertion(context));
        request.setNhinTargetCommunities(pixConsumerPRPAIN201309UVRequest.getNhinTargetCommunities());
        request.setPRPAIN201309UV02(pixConsumerPRPAIN201309UVRequest.getPRPAIN201309UV02());

        /* Log the 201309 message received */
        SubjectDiscoveryAuditLog auditLog = new SubjectDiscoveryAuditLog();
        AcknowledgementType ack = auditLog.audit(request);
        /*
         * Assign checkpolicy for reidentification
         */
        SubjectReidentificationEventType checkPolicy = new SubjectReidentificationEventType();
        SubjectReidentificationMessageType checkPolicyMessage = new SubjectReidentificationMessageType();
        checkPolicyMessage.setPRPAIN201309UV02(request.getPRPAIN201309UV02());
        checkPolicyMessage.setAssertion(request.getAssertion());
        checkPolicyMessage.setNhinTargetCommunities(request.getNhinTargetCommunities());
        checkPolicy.setMessage(checkPolicyMessage);
        checkPolicy.setDirection(NhincConstants.POLICYENGINE_OUTBOUND_DIRECTION);
        checkPolicy.setInterface(NhincConstants.AUDIT_LOG_ENTITY_INTERFACE);

        /* invoke check policy */
        PolicyEngineChecker policyChecker = new PolicyEngineChecker();
        CheckPolicyRequestType policyReq = policyChecker.checkPolicySubjectReidentification(checkPolicy);
        PolicyEngineProxyObjectFactory policyEngFactory = new PolicyEngineProxyObjectFactory();
        PolicyEngineProxy policyProxy = policyEngFactory.getPolicyEngineProxy();
        CheckPolicyResponseType policyResp = policyProxy.checkPolicy(policyReq);

        /* if response='permit' */
        if (policyResp.getResponse().getResult().get(0).getDecision().value().equals(NhincConstants.POLICY_PERMIT)) {
            PIXConsumerPRPAIN201309UVProxyRequestType proxyRequest = createProxy201309(request);

            log.debug("EntitySubjectDiscoveryImpl.pixConsumerPRPAIN201309UV -- Call to nhinSubjectDiscovery");
            log.debug("proxyRequest >>" + proxyRequest + "<<");
            response.setPRPAIN201310UV02(nhinSubjectDiscovery.pixConsumerPRPAIN201309UV(proxyRequest));


        /*      log proxy response
         */
        }
        log.debug("EntitySubjectDiscoveryImpl.pixConsumerPRPAIN201309UV -- End");
        return response;
    }

    /**
     * This method is used to extract the components of the 201301 used when setting the
     * QualifiedSubjectIdentifierType
     */
    private static QualifiedSubjectIdentifierType assignQualifiedSubjectFrom201301(org.hl7.v3.PRPAIN201301UV02 request) {
        log.debug("EntitySubjectDiscoveryImpl.assignQualifiedSubjectFrom201301 -- Begin");
        QualifiedSubjectIdentifierType qualifiedSubject = new QualifiedSubjectIdentifierType();
        if (request.getControlActProcess() != null &&
                request.getControlActProcess().getSubject() != null &&
                request.getControlActProcess().getSubject().get(0) != null &&
                request.getControlActProcess().getSubject().get(0).getRegistrationEvent() != null &&
                request.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1() != null &&
                request.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient() != null &&
                request.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId() != null) {
            org.hl7.v3.II patientId = request.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0);
            qualifiedSubject.setSubjectIdentifier(patientId.getExtension());
            qualifiedSubject.setAssigningAuthorityIdentifier(patientId.getRoot());
        }
        log.debug("EntitySubjectDiscoveryImpl.assignQualifiedSubjectFrom201301 -- End");
        return qualifiedSubject;
    }

    /**
     * This method is used to extract the components of the 201301 used when setting the
     * QualifiedSubjectIdentifierType
     */
    private static PIXConsumerPRPAIN201301UVProxyRequestType createProxy201301(PIXConsumerPRPAIN201301UVRequestType request) {
        log.debug("EntitySubjectDiscoveryImpl.createProxy201301 -- Begin");
        PIXConsumerPRPAIN201301UVProxyRequestType proxyRequest = new PIXConsumerPRPAIN201301UVProxyRequestType();
        // Copy PRPAIN201301UV to Proxy
        if (request.getPRPAIN201301UV02() != null) {
            org.hl7.v3.PRPAIN201301UV02 input = request.getPRPAIN201301UV02();
            if (input.getControlActProcess() != null &&
                    input.getControlActProcess().getSubject() != null) {
                List<org.hl7.v3.PRPAIN201301UV02MFMIMT700701UV01Subject1> subjects = input.getControlActProcess().getSubject();
                if (subjects.get(0).getRegistrationEvent() != null) {
                    org.hl7.v3.PRPAIN201301UV02MFMIMT700701UV01RegistrationEvent regEvent = new org.hl7.v3.PRPAIN201301UV02MFMIMT700701UV01RegistrationEvent();
                    regEvent.setSubject1(request.getPRPAIN201301UV02().getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1());
                    if (regEvent.getSubject1() != null) {
                        if (regEvent.getSubject1().getPatient() != null) {
                            proxyRequest.setPRPAIN201301UV02(HL7PRPA201301Transforms.createPRPA201301(regEvent.getSubject1().getPatient(),
                                    localHomeCommunity, input.getSender().getDevice().getId().get(0).getRoot(),
                                    input.getReceiver().get(0).getDevice().getId().get(0).getRoot()));
                        }
                    }

                }
            }
        }
        if (request.getAssertion() != null) {
            AssertionType assertion = new AssertionType();
            AssertionType input = request.getAssertion();
            if (input.getUserInfo() != null) {
                UserType proxyUser = new UserType();
                if (input.getUserInfo().getOrg() != null) {
                    HomeCommunityType proxyHC = new HomeCommunityType();
                    proxyHC.setDescription(input.getUserInfo().getOrg().getDescription());
                    proxyHC.setHomeCommunityId(input.getUserInfo().getOrg().getHomeCommunityId());
                    proxyHC.setName(input.getUserInfo().getOrg().getName());
                    proxyUser.setOrg(proxyHC);
                }
                if (input.getUserInfo().getPersonName() != null) {
                    gov.hhs.fha.nhinc.common.nhinccommon.PersonNameType proxyPersonName = new gov.hhs.fha.nhinc.common.nhinccommon.PersonNameType();
                    proxyPersonName.setFamilyName(input.getUserInfo().getPersonName().getFamilyName());
                    proxyPersonName.setGivenName(input.getUserInfo().getPersonName().getGivenName());
                    proxyPersonName.setSecondNameOrInitials(input.getUserInfo().getPersonName().getSecondNameOrInitials());
                    proxyUser.setPersonName(proxyPersonName);
                }
                proxyUser.setUserName(input.getUserInfo().getUserName());
                if (input.getUserInfo().getRoleCoded() != null) {
                    proxyUser.setRoleCoded(copyCeType(input.getUserInfo().getRoleCoded()));
                }
                assertion.setUserInfo(proxyUser);
            }
            assertion.setAuthorized(input.isAuthorized());
            assertion.setPurposeOfDisclosureCoded(copyCeType(input.getPurposeOfDisclosureCoded()));
            proxyRequest.setAssertion(assertion);
        }
        log.debug("EntitySubjectDiscoveryImpl.createProxy201301 -- End");
        return proxyRequest;
    }

    /**
     * This method creates a 201309 Proxy message from the initial 201309.
     */
    private static PIXConsumerPRPAIN201309UVProxyRequestType createProxy201309(PIXConsumerPRPAIN201309UVRequestType request) {
        log.debug("EntitySubjectDiscoveryImpl.createProxy201309 -- Begin");
        PIXConsumerPRPAIN201309UVProxyRequestType proxyRequest = new PIXConsumerPRPAIN201309UVProxyRequestType();
        // Copy PRPAIN2013019UV to Proxy
        if (request.getPRPAIN201309UV02() != null) {

            org.hl7.v3.PRPAIN201309UV02 proxy201309 = new org.hl7.v3.PRPAIN201309UV02();
            org.hl7.v3.PRPAIN201309UV02QUQIMT021001UV01ControlActProcess input = request.getPRPAIN201309UV02().getControlActProcess();
            if (input.getQueryByParameter() != null) {
                org.hl7.v3.PRPAMT201307UV02PatientIdentifier patientId = new org.hl7.v3.PRPAMT201307UV02PatientIdentifier();
                patientId.getValue().add(request.getPRPAIN201309UV02().getControlActProcess().getQueryByParameter().getValue().getParameterList().getPatientIdentifier().get(0).getValue().get(0));
                org.hl7.v3.PRPAIN201309UV02QUQIMT021001UV01ControlActProcess controlAct = new org.hl7.v3.PRPAIN201309UV02QUQIMT021001UV01ControlActProcess();
                org.hl7.v3.PRPAMT201307UV02QueryByParameter param = new org.hl7.v3.PRPAMT201307UV02QueryByParameter();
                org.hl7.v3.PRPAMT201307UV02ParameterList paramList = new org.hl7.v3.PRPAMT201307UV02ParameterList();
                paramList.getPatientIdentifier().add(patientId);
                param.setParameterList(paramList);
                org.hl7.v3.ObjectFactory factory = new org.hl7.v3.ObjectFactory();
                JAXBElement oJaxbElement = factory.createPRPAIN201309UV02QUQIMT021001UV01ControlActProcessQueryByParameter(param);

                controlAct.setQueryByParameter(oJaxbElement);
                proxy201309.setControlActProcess(controlAct);
                proxyRequest.setPRPAIN201309UV02(proxy201309);
            }

            if (request.getPRPAIN201309UV02().getReceiver() != null) {
                org.hl7.v3.MCCIMT000100UV01Receiver receiver = new org.hl7.v3.MCCIMT000100UV01Receiver();
                receiver.setDevice(request.getPRPAIN201309UV02().getReceiver().get(0).getDevice());
                proxyRequest.getPRPAIN201309UV02().getReceiver().add(receiver);
            }
            if (request.getPRPAIN201309UV02().getSender() != null) {
                org.hl7.v3.MCCIMT000100UV01Sender sender = new org.hl7.v3.MCCIMT000100UV01Sender();
                sender.setDevice(request.getPRPAIN201309UV02().getSender().getDevice());
                proxyRequest.getPRPAIN201309UV02().setSender(sender);
            }
        }
        if (request.getAssertion() != null) {
            AssertionType assertion = new AssertionType();
            AssertionType input = request.getAssertion();
            if (input.getUserInfo() != null) {
                UserType proxyUser = new UserType();
                if (input.getUserInfo().getOrg() != null) {
                    HomeCommunityType proxyHC = new HomeCommunityType();
                    proxyHC.setDescription(input.getUserInfo().getOrg().getDescription());
                    proxyHC.setHomeCommunityId(input.getUserInfo().getOrg().getHomeCommunityId());
                    proxyHC.setName(input.getUserInfo().getOrg().getName());
                    proxyUser.setOrg(proxyHC);
                }
                if (input.getUserInfo().getPersonName() != null) {
                    gov.hhs.fha.nhinc.common.nhinccommon.PersonNameType proxyPersonName = new gov.hhs.fha.nhinc.common.nhinccommon.PersonNameType();
                    proxyPersonName.setFamilyName(input.getUserInfo().getPersonName().getFamilyName());
                    proxyPersonName.setGivenName(input.getUserInfo().getPersonName().getGivenName());
                    proxyPersonName.setSecondNameOrInitials(input.getUserInfo().getPersonName().getSecondNameOrInitials());
                    proxyUser.setPersonName(proxyPersonName);
                }
                proxyUser.setUserName(input.getUserInfo().getUserName());
                if (input.getUserInfo().getRoleCoded() != null) {
                    proxyUser.setRoleCoded(copyCeType(input.getUserInfo().getRoleCoded()));
                }
                assertion.setUserInfo(proxyUser);
            }
            assertion.setAuthorized(input.isAuthorized());
            assertion.setPurposeOfDisclosureCoded(copyCeType(input.getPurposeOfDisclosureCoded()));
            proxyRequest.setAssertion(assertion);
        }
        if (request.getNhinTargetCommunities() != null &&
                request.getNhinTargetCommunities().getNhinTargetCommunity() != null &&
                request.getNhinTargetCommunities().getNhinTargetCommunity().size() > 0) {

            NhinTargetSystemType targetSystem = new NhinTargetSystemType();
            targetSystem.setHomeCommunity(request.getNhinTargetCommunities().getNhinTargetCommunity().get(0).getHomeCommunity());
            proxyRequest.setNhinTargetSystem(targetSystem);
        }
        log.debug("EntitySubjectDiscoveryImpl.createProxy201309 -- End");
        return proxyRequest;
    }

    /**
     * This method is copy a CeType into another CeType object
     */
    private static gov.hhs.fha.nhinc.common.nhinccommon.CeType copyCeType(gov.hhs.fha.nhinc.common.nhinccommon.CeType input) {
        gov.hhs.fha.nhinc.common.nhinccommon.CeType result = new gov.hhs.fha.nhinc.common.nhinccommon.CeType();
        result.setCode(input.getCode());
        result.setCodeSystem(input.getCodeSystem());
        result.setCodeSystemName(input.getCodeSystemName());
        result.setCodeSystemVersion(input.getCodeSystemVersion());
        result.setDisplayName(input.getDisplayName());
        result.setOriginalText(input.getOriginalText());

        return result;
    }
}
