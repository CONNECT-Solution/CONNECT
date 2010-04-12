/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.xdr.entity;

import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import gov.hhs.fha.nhinc.transform.policy.SubjectHelper;
import gov.hhs.fha.nhinc.xdr.XDRAuditLogger;
import gov.hhs.fha.nhinc.xdr.XDRPolicyChecker;
import gov.hhs.fha.nhinc.xdr.proxy.NhincProxyXDRSecuredImpl;
import javax.xml.ws.WebServiceContext;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class EntityXDRSecuredImpl {

    private Log log = null;

    public EntityXDRSecuredImpl() {
        log = createLogger();
    }

    protected Log createLogger() {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }

    public RegistryResponseType provideAndRegisterDocumentSetB(RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType request, WebServiceContext context) {
        RegistryResponseType response = null;

        if (request != null && context != null) {
            AssertionType assertion = SamlTokenExtractor.GetAssertion(context);
            response = provideAndRegisterDocumentSetB(request, assertion);
        } else {
            log.error("Invocation of EntityXDRSecuredImpl.provideAndRegisterDocumentSetB has null parameters; request = " + request + " cpntext = " + context);
        }
        return response;
    }

    private RegistryResponseType provideAndRegisterDocumentSetB(RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType request, AssertionType assertion) {
        log.debug("Entering EntityXDRSecuredImpl.provideAndRegisterDocumentSetB");
        RegistryResponseType response = new RegistryResponseType();

        //log the incomming request from the adapter
        logEntityXDRRequest(request, assertion);

        //check the policy for the outgoing request to the target community
        boolean bIsPolicyOk = isPolicyOk(request, assertion);
        if (bIsPolicyOk) {
            //send request to targeted community
            response = getResponseFromTarget(request, assertion);
        } else {
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

        log.debug("Leaving EntityXDRSecuredImpl.provideAndRegisterDocumentSetB");
        return response;
    }

    private void logEntityXDRRequest(RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType request, AssertionType assertion) {
        // Audit the XDR Request Message sent on the Nhin Interface
        AcknowledgementType ack = new XDRAuditLogger().auditEntityXDR(request, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION);
    }

    private void logResponseFromNhin(RegistryResponseType response, AssertionType assertion) {
        AcknowledgementType ack = new XDRAuditLogger().auditEntityXDRResponse(response, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION);
    }

    protected boolean isPolicyOk(RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType request, AssertionType assertion) {
        boolean bPolicyOk = false;

        if (request != null && request.getNhinTargetSystem() != null && request.getNhinTargetSystem().getHomeCommunity() != null && request.getNhinTargetSystem().getHomeCommunity().getHomeCommunityId() != null) {

            SubjectHelper subjHelp = new SubjectHelper();
            String senderHCID = subjHelp.determineSendingHomeCommunityId(assertion.getHomeCommunity(), assertion);
            String receiverHCID = request.getNhinTargetSystem().getHomeCommunity().getHomeCommunityId();
            String direction = NhincConstants.POLICYENGINE_OUTBOUND_DIRECTION;
            log.debug("Checking the policy engine for the " + direction + " request from " + senderHCID + " to " + receiverHCID);

            //return true if 'permit' returned, false otherwise
            XDRPolicyChecker policyChecker = new XDRPolicyChecker();
            bPolicyOk = policyChecker.checkXDRRequestPolicy(request.getProvideAndRegisterDocumentSetRequest(), assertion, senderHCID, receiverHCID, direction);
        } else {
            log.warn("EntityXDRSecuredImpl check on policy requires a non null receiving home community ID specified in the RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType");
        }
        log.debug("EntityXDRSecuredImpl check on policy returns: " + bPolicyOk);
        return bPolicyOk;

    }

    private RegistryResponseType getResponseFromTarget(RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType request, AssertionType assertion) {
        log.debug("Entering EntityXDRSecuredImpl.getResponseFromTarget...");
        RegistryResponseType nhinResponse = new RegistryResponseType();

        NhincProxyXDRSecuredImpl proxy = new NhincProxyXDRSecuredImpl();

        //format request for nhincProxyPatientDiscoveryImpl call
        gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType proxySecuredRequestType = new gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType();
        proxySecuredRequestType.setNhinTargetSystem(request.getNhinTargetSystem());
        proxySecuredRequestType.setProvideAndRegisterDocumentSetRequest(request.getProvideAndRegisterDocumentSetRequest());

        try {
            nhinResponse = proxy.provideAndRegisterDocumentSetB(proxySecuredRequestType, assertion);
        } catch (Throwable t) {
            nhinResponse = new RegistryResponseType();
            RegistryErrorList regErrList = new RegistryErrorList();
            RegistryError regErr = new RegistryError();
            regErrList.getRegistryError().add(regErr);
            regErr.setCodeContext("Fault encountered processing provideAndRegisterDocumentSetB for community " + request.getNhinTargetSystem().getHomeCommunity().getHomeCommunityId());
            regErr.setErrorCode("XDSRegistryBusy");
            regErr.setSeverity("urn:oasis:names:tc:ebxml-regrep:ErrorSeverityType:Error");
            nhinResponse.setRegistryErrorList(regErrList);
            nhinResponse.setStatus("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure");
            log.error("Fault encountered processing provideAndRegisterDocumentSetB for community " + request.getNhinTargetSystem().getHomeCommunity().getHomeCommunityId());
            log.error("Nhinc Proxy for XDR throws: " + t.getMessage() + "\n");
            t.printStackTrace();
        }
        log.debug("Leaving EntityXDRSecuredImpl.getResponseFromTarget");
        return nhinResponse;
    }
}
