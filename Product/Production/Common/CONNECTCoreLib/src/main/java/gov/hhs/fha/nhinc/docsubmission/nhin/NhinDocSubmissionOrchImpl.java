/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.docsubmission.nhin;

import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.docsubmission.XDRAuditLogger;
import gov.hhs.fha.nhinc.docsubmission.XDRPolicyChecker;
import gov.hhs.fha.nhinc.docsubmission.adapter.proxy.AdapterDocSubmissionProxy;
import gov.hhs.fha.nhinc.docsubmission.adapter.proxy.AdapterDocSubmissionProxyObjectFactory;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author JHOPPESC
 */
public class NhinDocSubmissionOrchImpl {

    public static final String XDR_RESPONSE_SUCCESS = "Success";
    public static final String XDR_RESPONSE_FAILURE = "Failure";
    public static final String XDR_POLICY_ERROR = "CONNECTPolicyCheckFailed ";
    public static final String XDR_POLICY_ERROR_CONTEXT = "Policy Check Failed";
    private static Log log = null;

    public NhinDocSubmissionOrchImpl() {
        log = createLogger();
    }

    public RegistryResponseType documentRepositoryProvideAndRegisterDocumentSetB(ProvideAndRegisterDocumentSetRequestType body, AssertionType assertion) {
        RegistryResponseType result = null;

        log.debug("Entering NhinXDRImpl.documentRepositoryProvideAndRegisterDocumentSetB");

        XDRAuditLogger auditLogger = new XDRAuditLogger();
        log.debug("Request object is nul = " + (body == null));
        AcknowledgementType ack = auditLogger.auditNhinXDR(body, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION);
        log.debug(ack.getMessage());
        String localHCID = "";
        try {
            localHCID = PropertyAccessor.getProperty(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.HOME_COMMUNITY_ID_PROPERTY);
        } catch (PropertyAccessException ex) {
            log.error(ex.getMessage());

        }

        if (isPolicyOk(body, assertion, assertion.getHomeCommunity().getHomeCommunityId(), localHCID)) {
            log.debug("Policy Check Succeeded");
            result = forwardToAgency(body, assertion);
        } else {
            log.error("Failed Policy Check");
            result = createFailedPolicyCheckResponse();
        }


        ack = auditLogger.auditNhinXDRResponse(result, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION);

        return result;
    }

    private RegistryResponseType forwardToAgency(ProvideAndRegisterDocumentSetRequestType body, AssertionType assertion)
    {
        log.debug("begin forwardToAgency()");

        RegistryResponseType response = null;

        AdapterDocSubmissionProxyObjectFactory factory = new AdapterDocSubmissionProxyObjectFactory();
        AdapterDocSubmissionProxy proxy = factory.getAdapterDocSubmissionProxy();

        response = proxy.provideAndRegisterDocumentSetB(body, assertion);

        return response;
    }

    protected boolean isPolicyOk(ProvideAndRegisterDocumentSetRequestType newRequest, AssertionType assertion, String senderHCID, String receiverHCID) {
        boolean bPolicyOk = false;

        log.debug("checking the policy engine for the new request to a target community");

        //return true if 'permit' returned, false otherwise
        XDRPolicyChecker policyChecker = new XDRPolicyChecker();
        return policyChecker.checkXDRRequestPolicy(newRequest, assertion,senderHCID ,receiverHCID, NhincConstants.POLICYENGINE_INBOUND_DIRECTION);

    }
    
    protected Log createLogger()
    {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }
    private RegistryResponseType createPositiveAck()
    {
        RegistryResponseType result= new RegistryResponseType();

        result.setStatus(XDR_RESPONSE_SUCCESS);

        return result;
    }
    private RegistryResponseType createFailedPolicyCheckResponse()
    {
        RegistryResponseType result= new RegistryResponseType();
        result.setRegistryErrorList(new RegistryErrorList());

        RegistryError policyError = new RegistryError();
        policyError.setErrorCode(XDR_POLICY_ERROR);
        policyError.setCodeContext(XDR_POLICY_ERROR_CONTEXT);
        policyError.setSeverity("Error");

        result.setStatus(XDR_RESPONSE_FAILURE);
        result.getRegistryErrorList().getRegistryError().add(policyError);

        return result;
    }
   
}
