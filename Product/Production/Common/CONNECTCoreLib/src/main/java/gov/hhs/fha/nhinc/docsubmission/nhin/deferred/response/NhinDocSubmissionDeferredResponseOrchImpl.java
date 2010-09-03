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
package gov.hhs.fha.nhinc.docsubmission.nhin.deferred.response;

import gov.hhs.fha.nhinc.docsubmission.adapter.deferred.response.proxy.AdapterDocSubmissionDeferredResponseProxy;
import gov.hhs.fha.nhinc.docsubmission.adapter.deferred.response.proxy.AdapterDocSubmissionDeferredResponseProxyObjectFactory;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.docsubmission.NhinDocSubmissionUtils;
import gov.hhs.fha.nhinc.docsubmission.XDRAuditLogger;
import gov.hhs.fha.nhinc.docsubmission.XDRPolicyChecker;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author JHOPPESC
 */
public class NhinDocSubmissionDeferredResponseOrchImpl {

    private static final Log logger = LogFactory.getLog(NhinDocSubmissionDeferredResponseOrchImpl.class);

    /**
     *
     * @return
     */
    protected Log getLogger() {
        return logger;
    }

    public XDRAcknowledgementType provideAndRegisterDocumentSetBResponse(RegistryResponseType body, AssertionType assertion) {
        XDRAcknowledgementType result = new XDRAcknowledgementType();
        RegistryResponseType regResp = new RegistryResponseType();
        regResp.setStatus(NhincConstants.XDR_ACK_STATUS_MSG);
        result.setMessage(regResp);

        getLogger().debug("Entering provideAndRegisterDocumentSetBResponse");

        AcknowledgementType ack = getXDRAuditLogger().auditNhinXDRResponse(body, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION);

        getLogger().debug("Audit Log Ack Message:" + ack.getMessage());

        String localHCID = retrieveHomeCommunityID();

        getLogger().debug("Local Home Community ID: " + localHCID);

        // Check if the Patient Discovery Async Request Service is enabled
        if (isServiceEnabled()) {

            // Check if in Pass-Through Mode
            if (!(isInPassThroughMode())) {
                if (isPolicyOk(body, assertion, assertion.getHomeCommunity().getHomeCommunityId(), localHCID)) {
                    getLogger().debug("Policy Check Succeeded");
                    result = forwardToAgency(body, assertion);
                } else {
                    getLogger().error("Policy Check Failed");
                }
            }
            else {
                result = forwardToAgency(body, assertion);
            }
        }
        else {
            getLogger().warn("Document Submission Response Service is not enabled");
        }

        ack = getXDRAuditLogger().auditAcknowledgement(result, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.XDR_RESPONSE_ACTION);

        getLogger().debug("Audit Log Ack Message for Outbound Acknowledgement:" + ack.getMessage());

        getLogger().debug("Exiting provideAndRegisterDocumentSetBResponse");

        return result;
    }

    /**
     * Checks the gateway.properties file to see if the Patient Discovery Async Request Service is enabled.
     *
     * @return Returns true if the servicePatientDiscoveryAsyncReq is enabled in the properties file.
     */
    protected boolean isServiceEnabled() {
        return NhinDocSubmissionUtils.isServiceEnabled(NhincConstants.DOC_SUBMISSION_DEFERRED_RESP_SERVICE_PROP);
    }

    /**
     * Checks to see if the query should  be handled internally or passed through to an adapter.
     *
     * @return Returns true if the patientDiscoveryPassthroughAsyncReq property of the gateway.properties file is true.
     */
    protected boolean isInPassThroughMode() {
        return NhinDocSubmissionUtils.isInPassThroughMode(NhincConstants.DOC_SUBMISSION_DEFERRED_RESP_PASSTHRU_PROP);
    }

    /**
     *
     * @return
     */
    protected String retrieveHomeCommunityID() {
        String localHCID = null;
        try {
            localHCID = PropertyAccessor.getProperty(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.HOME_COMMUNITY_ID_PROPERTY);
        } catch (PropertyAccessException ex) {
            logger.error("Exception while retrieving home community ID", ex);
        }

        return localHCID;
    }

    /**
     *
     * @return
     */
    protected XDRAuditLogger getXDRAuditLogger() {
        return new XDRAuditLogger();
    }

    /**
     *
     * @param body
     * @param context
     * @return
     */
    protected XDRAcknowledgementType forwardToAgency(RegistryResponseType body, AssertionType assertion) {
        getLogger().debug("Entering forwardToAgency");

        AdapterDocSubmissionDeferredResponseProxyObjectFactory factory = new AdapterDocSubmissionDeferredResponseProxyObjectFactory();

        AdapterDocSubmissionDeferredResponseProxy proxy = factory.getAdapterDocSubmissionDeferredResponseProxy();

        XDRAcknowledgementType response = proxy.provideAndRegisterDocumentSetBResponse(body, assertion);

        getLogger().debug("Exiting forwardToAgency");

        return response;
    }

    /**
     *
     * @param newRequest
     * @param assertion
     * @param senderHCID
     * @param receiverHCID
     * @return
     */
    protected boolean isPolicyOk(RegistryResponseType request, AssertionType assertion, String senderHCID, String receiverHCID) {

        boolean isPolicyOk = false;

        getLogger().debug("Check policy");

        XDRPolicyChecker policyChecker = new XDRPolicyChecker();
        isPolicyOk = policyChecker.checkXDRResponsePolicy(request, assertion, senderHCID, receiverHCID, NhincConstants.POLICYENGINE_INBOUND_DIRECTION);

        getLogger().debug("Response from policy engine: " + isPolicyOk);

        return isPolicyOk;

    }
}
