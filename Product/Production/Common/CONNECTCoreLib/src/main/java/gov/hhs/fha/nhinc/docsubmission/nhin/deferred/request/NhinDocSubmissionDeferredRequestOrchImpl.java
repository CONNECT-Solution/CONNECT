/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.docsubmission.nhin.deferred.request;

import java.sql.Blob;
import gov.hhs.fha.nhinc.docsubmission.adapter.deferred.request.error.proxy.AdapterDocSubmissionDeferredRequestErrorProxy;
import gov.hhs.fha.nhinc.docsubmission.adapter.deferred.request.error.proxy.AdapterDocSubmissionDeferredRequestErrorProxyObjectFactory;
import gov.hhs.fha.nhinc.docsubmission.adapter.deferred.request.proxy.AdapterDocSubmissionDeferredRequestProxy;
import gov.hhs.fha.nhinc.docsubmission.adapter.deferred.request.proxy.AdapterDocSubmissionDeferredRequestProxyObjectFactory;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.docsubmission.NhinDocSubmissionUtils;
import gov.hhs.fha.nhinc.docsubmission.XDRAuditLogger;
import gov.hhs.fha.nhinc.docsubmission.XDRPolicyChecker;
import javax.xml.bind.Marshaller;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.transform.marshallers.JAXBContextHandler;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectListType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;

/**
 *
 * @author JHOPPESC
 */
public class NhinDocSubmissionDeferredRequestOrchImpl {

    public static final String XDR_POLICY_ERROR = "CONNECTPolicyCheckFailed";
    private static final Log logger = LogFactory.getLog(NhinDocSubmissionDeferredRequestOrchImpl.class);

    /**
     *
     * @return
     */
    protected Log getLogger() {
        return logger;
    }

    public XDRAcknowledgementType provideAndRegisterDocumentSetBRequest(ProvideAndRegisterDocumentSetRequestType body, AssertionType assertion) {

        XDRAcknowledgementType result = new XDRAcknowledgementType();
        RegistryResponseType regResp = new RegistryResponseType();
        regResp.setStatus(NhincConstants.XDR_ACK_STATUS_MSG);
        result.setMessage(regResp);

        getLogger().debug("Entering provideAndRegisterDocumentSetBRequest");

        AcknowledgementType ack = getXDRAuditLogger().auditNhinXDR(body, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION);

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
                    result = sendErrorToAgency(body, assertion, "Policy Check Failed");
                }
            }
            else {
                result = forwardToAgency(body, assertion);
            }
        }
        else {
            getLogger().warn("Document Submission Request Service is not enabled");
            result = sendErrorToAgency(body, assertion, "Policy Check Failed");
        }
        ack = getXDRAuditLogger().auditAcknowledgement(result, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.XDR_REQUEST_ACTION);

        getLogger().debug("Audit Log Ack Message for Outbound Acknowledgement:" + ack.getMessage());

        logger.debug(
                "Exiting provideAndRegisterDocumentSetBRequest");





        return result;
    }

    /**
     * Checks the gateway.properties file to see if the Patient Discovery Async Request Service is enabled.
     *
     * @return Returns true if the servicePatientDiscoveryAsyncReq is enabled in the properties file.
     */
    protected boolean isServiceEnabled() {
        return NhinDocSubmissionUtils.isServiceEnabled(NhincConstants.DOC_SUBMISSION_DEFERRED_REQ_SERVICE_PROP);
    }

    /**
     * Checks to see if the query should  be handled internally or passed through to an adapter.
     *
     * @return Returns true if the patientDiscoveryPassthroughAsyncReq property of the gateway.properties file is true.
     */
    protected boolean isInPassThroughMode() {
        return NhinDocSubmissionUtils.isInPassThroughMode(NhincConstants.DOC_SUBMISSION_DEFERRED_REQ_PASSTHRU_PROP);
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
    protected XDRAcknowledgementType forwardToAgency(ProvideAndRegisterDocumentSetRequestType body, AssertionType assertion) {
        getLogger().debug("Entering forwardToAgency");

        AdapterDocSubmissionDeferredRequestProxyObjectFactory factory = new AdapterDocSubmissionDeferredRequestProxyObjectFactory();

        AdapterDocSubmissionDeferredRequestProxy proxy = factory.getAdapterDocSubmissionDeferredRequestProxy();

        XDRAcknowledgementType response = proxy.provideAndRegisterDocumentSetBRequest(body, null, assertion);

        getLogger().debug("Exiting forwardToAgency");

        return response;
    }

    protected XDRAcknowledgementType sendErrorToAgency(ProvideAndRegisterDocumentSetRequestType body, AssertionType assertion, String errMsg) {

        AdapterDocSubmissionDeferredRequestErrorProxyObjectFactory factory = new AdapterDocSubmissionDeferredRequestErrorProxyObjectFactory();
        AdapterDocSubmissionDeferredRequestErrorProxy proxy = factory.getAdapterDocSubmissionDeferredRequestErrorProxy();

        XDRAcknowledgementType adapterResp = proxy.provideAndRegisterDocumentSetBRequestError(body, errMsg, assertion);

        return adapterResp;
    }

    /**
     *
     * @param newRequest
     * @param assertion
     * @param senderHCID
     * @param receiverHCID
     * @return
     */
    protected boolean isPolicyOk(ProvideAndRegisterDocumentSetRequestType request, AssertionType assertion, String senderHCID, String receiverHCID) {

        boolean isPolicyOk = false;

        getLogger().debug("Check policy");

        XDRPolicyChecker policyChecker = new XDRPolicyChecker();
        isPolicyOk =
                policyChecker.checkXDRRequestPolicy(request, assertion, senderHCID, receiverHCID, NhincConstants.POLICYENGINE_INBOUND_DIRECTION);

        getLogger().debug("Response from policy engine: " + isPolicyOk);

        return isPolicyOk;

    }

    private Blob createAssertionBlob(AssertionType assertion) {
        Blob data = null;
        ByteArrayOutputStream baOutStrm = new ByteArrayOutputStream();

        if (assertion != null) {
            baOutStrm.reset();

            // Marshall the Assertion Element into binary data
            try {
                JAXBContextHandler oHandler = new JAXBContextHandler();
                JAXBContext jc = oHandler.getJAXBContext("gov.hhs.fha.nhinc.common.nhinccommon");
                Marshaller marshaller = jc.createMarshaller();
                baOutStrm.reset();

                gov.hhs.fha.nhinc.common.nhinccommon.ObjectFactory factory = new gov.hhs.fha.nhinc.common.nhinccommon.ObjectFactory();
                JAXBElement oJaxbElement = factory.createAssertion(assertion);
                marshaller.marshal(oJaxbElement, baOutStrm);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error(e.getMessage());
            }

            byte[] buffer = baOutStrm.toByteArray();
            logger.debug("Byte Array: " + baOutStrm.toString());

            data =
                    Hibernate.createBlob(buffer);
        }

        return data;
    }


}
