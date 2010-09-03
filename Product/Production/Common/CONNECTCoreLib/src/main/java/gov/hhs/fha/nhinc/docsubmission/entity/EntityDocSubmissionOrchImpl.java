/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.docsubmission.entity;

import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommon.UrlInfoType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType;
import gov.hhs.fha.nhinc.docsubmission.XDRAuditLogger;
import gov.hhs.fha.nhinc.docsubmission.XDRPolicyChecker;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.transform.policy.SubjectHelper;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import gov.hhs.fha.nhinc.docsubmission.passthru.proxy.PassthruDocSubmissionProxyObjectFactory;
import gov.hhs.fha.nhinc.docsubmission.passthru.proxy.PassthruDocSubmissionProxy;

public class EntityDocSubmissionOrchImpl
{

    private Log log = null;

    public EntityDocSubmissionOrchImpl()
    {
        log = createLogger();
    }

    protected Log createLogger()
    {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }

    public RegistryResponseType provideAndRegisterDocumentSetB(ProvideAndRegisterDocumentSetRequestType msg,
            AssertionType assertion, NhinTargetCommunitiesType targets, UrlInfoType urlInfo)
    {
        log.debug("Entering EntityDocSubmissionOrchImpl.provideAndRegisterDocumentSetB");
        RegistryResponseType response = new RegistryResponseType();

        //log the incomming request from the adapter
        RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType secureRequest = new RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType();
        secureRequest.setProvideAndRegisterDocumentSetRequest(msg);
        secureRequest.setNhinTargetCommunities(targets);
        secureRequest.setUrl(urlInfo);
        logEntityXDRRequest(secureRequest, assertion);

        //check the policy for the outgoing request to the target community
        boolean bIsPolicyOk = isPolicyOk(secureRequest, assertion);
        if (bIsPolicyOk)
        {
            //send request to targeted community
            response = getResponseFromTarget(secureRequest, assertion);
        } else
        {
            RegistryErrorList regErrList = new RegistryErrorList();
            regErrList.setHighestSeverity("urn:oasis:names:tc:ebxml-regrep:ErrorSeverityType:Error");
            RegistryError regErr = new RegistryError();
            regErrList.getRegistryError().add(regErr);
            regErr.setCodeContext("Policy Check Failed");
            regErr.setErrorCode("CONNECTPolicyCheckFailed");
            regErr.setSeverity("urn:oasis:names:tc:ebxml-regrep:ErrorSeverityType:Error");
            response.setRegistryErrorList(regErrList);
            response.setStatus("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure");
            log.error("Sending Policy Check Deny in the Registry Response");
        }

        //log the response received on the Nhin Interface
        logResponseFromNhin(response, assertion);

        log.debug("Leaving EntityDocSubmissionOrchImpl.provideAndRegisterDocumentSetB");
        return response;
    }

    private void logEntityXDRRequest(RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType request, AssertionType assertion)
    {
        // Audit the XDR Request Message sent on the Nhin Interface
        AcknowledgementType ack = new XDRAuditLogger().auditEntityXDR(request, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION);
    }

    private void logResponseFromNhin(RegistryResponseType response, AssertionType assertion)
    {
        AcknowledgementType ack = new XDRAuditLogger().auditEntityXDRResponse(response, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION);
    }

    protected boolean isPolicyOk(RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType request, AssertionType assertion)
    {
        boolean bPolicyOk = false;

        if (request != null &&
                request.getNhinTargetCommunities() != null &&
                NullChecker.isNotNullish(request.getNhinTargetCommunities().getNhinTargetCommunity()) &&
                request.getNhinTargetCommunities().getNhinTargetCommunity().get(0) != null &&
                request.getNhinTargetCommunities().getNhinTargetCommunity().get(0).getHomeCommunity() != null &&
                NullChecker.isNotNullish(request.getNhinTargetCommunities().getNhinTargetCommunity().get(0).getHomeCommunity().getHomeCommunityId()))
        {

            SubjectHelper subjHelp = new SubjectHelper();
            String senderHCID = subjHelp.determineSendingHomeCommunityId(assertion.getHomeCommunity(), assertion);
            String receiverHCID = request.getNhinTargetCommunities().getNhinTargetCommunity().get(0).getHomeCommunity().getHomeCommunityId();
            String direction = NhincConstants.POLICYENGINE_OUTBOUND_DIRECTION;
            log.debug("Checking the policy engine for the " + direction + " request from " + senderHCID + " to " + receiverHCID);

            //return true if 'permit' returned, false otherwise
            XDRPolicyChecker policyChecker = new XDRPolicyChecker();
            bPolicyOk = policyChecker.checkXDRRequestPolicy(request.getProvideAndRegisterDocumentSetRequest(), assertion, senderHCID, receiverHCID, direction);
        } else
        {
            log.warn("EntityXDRSecuredImpl check on policy requires a non null receiving home community ID specified in the RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType");
        }
        log.debug("EntityXDRSecuredImpl check on policy returns: " + bPolicyOk);
        return bPolicyOk;

    }

    private RegistryResponseType getResponseFromTarget(RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType request, AssertionType assertion)
    {
        log.debug("Entering EntityXDRSecuredImpl.getResponseFromTarget...");
        RegistryResponseType nhinResponse = new RegistryResponseType();

        if (request != null &&
                request.getNhinTargetCommunities() != null &&
                NullChecker.isNotNullish(request.getNhinTargetCommunities().getNhinTargetCommunity()) &&
                request.getNhinTargetCommunities().getNhinTargetCommunity().get(0) != null &&
                request.getNhinTargetCommunities().getNhinTargetCommunity().get(0).getHomeCommunity() != null &&
                NullChecker.isNotNullish(request.getNhinTargetCommunities().getNhinTargetCommunity().get(0).getHomeCommunity().getHomeCommunityId()))
        {
            NhinTargetSystemType targetSystemType = new NhinTargetSystemType();
            targetSystemType.setHomeCommunity(request.getNhinTargetCommunities().getNhinTargetCommunity().get(0).getHomeCommunity());

            //format request for nhincProxyPatientDiscoveryImpl call
            gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType proxySecuredRequestType = new gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType();
            proxySecuredRequestType.setNhinTargetSystem(targetSystemType);
            proxySecuredRequestType.setProvideAndRegisterDocumentSetRequest(request.getProvideAndRegisterDocumentSetRequest());

            try
            {
                nhinResponse = callNhinXDRProxy(proxySecuredRequestType, assertion);
            } catch (Throwable t)
            {
                nhinResponse = new RegistryResponseType();
                RegistryErrorList regErrList = new RegistryErrorList();
                RegistryError regErr = new RegistryError();
                regErrList.getRegistryError().add(regErr);
                regErr.setCodeContext("Fault encountered processing provideAndRegisterDocumentSetB for community " + request.getNhinTargetCommunities().getNhinTargetCommunity().get(0).getHomeCommunity().getHomeCommunityId());
                regErr.setErrorCode("XDSRegistryBusy");
                regErr.setSeverity("urn:oasis:names:tc:ebxml-regrep:ErrorSeverityType:Error");
                nhinResponse.setRegistryErrorList(regErrList);
                nhinResponse.setStatus("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure");
                log.error("Fault encountered processing provideAndRegisterDocumentSetB for community " + request.getNhinTargetCommunities().getNhinTargetCommunity().get(0).getHomeCommunity().getHomeCommunityId());
                log.error("Nhinc Proxy for XDR throws: " + t.getMessage() + "\n");
                t.printStackTrace();
            }
        } else
        {
            log.warn("There was not a target community provided in the Entity message");
        }
        log.debug("Leaving EntityXDRSecuredImpl.getResponseFromTarget");
        return nhinResponse;
    }

    private RegistryResponseType callNhinXDRProxy(gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType body, AssertionType assertion)
    {
        RegistryResponseType response = null;

        XDRAuditLogger auditLog = new XDRAuditLogger();
        AcknowledgementType ack = auditLog.auditXDR(body, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION);
        log.debug("ack: " + ack.getMessage());

        // Core Refactor will create these
        PassthruDocSubmissionProxyObjectFactory factory = new PassthruDocSubmissionProxyObjectFactory();
        PassthruDocSubmissionProxy proxy = factory.getPassthruDocSubmissionProxy();

        log.debug("Invoking " + proxy + ".provideAndRegisterDocumentSetB with " + body.getProvideAndRegisterDocumentSetRequest()
                + " assertion: " + assertion + " and target " + body.getNhinTargetSystem());
        response = proxy.provideAndRegisterDocumentSetB(body.getProvideAndRegisterDocumentSetRequest(), assertion, body.getNhinTargetSystem());

        ack = auditLog.auditNhinXDRResponse(response, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION);

        log.debug("ack: " + ack.getMessage());
        return response;
    }
}
