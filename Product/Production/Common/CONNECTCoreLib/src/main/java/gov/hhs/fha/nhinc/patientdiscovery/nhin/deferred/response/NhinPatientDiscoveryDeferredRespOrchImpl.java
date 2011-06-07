/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.patientdiscovery.nhin.deferred.response;

import gov.hhs.fha.nhinc.async.AsyncMessageProcessHelper;
import gov.hhs.fha.nhinc.asyncmsgs.dao.AsyncMsgRecordDao;
import gov.hhs.fha.nhinc.asyncmsgs.model.AsyncMsgRecord;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.patientdiscovery.NhinPatientDiscoveryUtils;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscovery201306Processor;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryAdapterSender;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryAuditLogger;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryPolicyChecker;
import gov.hhs.fha.nhinc.patientdiscovery.response.ResponseFactory;
import gov.hhs.fha.nhinc.patientdiscovery.response.TrustMode;
import gov.hhs.fha.nhinc.patientdiscovery.response.VerifyMode;
import gov.hhs.fha.nhinc.transform.subdisc.HL7AckTransforms;
import java.beans.XMLDecoder;
import java.io.ByteArrayInputStream;
import java.sql.Blob;
import java.util.Date;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.II;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02RequestType;
import javax.xml.bind.Unmarshaller;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectId;

/**
 *
 * @author jhoppesc
 */
public class NhinPatientDiscoveryDeferredRespOrchImpl {

    private static Log log = LogFactory.getLog(NhinPatientDiscoveryDeferredRespOrchImpl.class);

    protected AsyncMessageProcessHelper createAsyncProcesser() {
        return new AsyncMessageProcessHelper();
    }

    public MCCIIN000002UV01 respondingGatewayPRPAIN201306UV02Orch(PRPAIN201306UV02 body, AssertionType assertion) {
        MCCIIN000002UV01 resp = new MCCIIN000002UV01();
        String ackMsg = "";

        // Audit the incoming Nhin 201306 Message
        PatientDiscoveryAuditLogger auditLogger = new PatientDiscoveryAuditLogger();
        AcknowledgementType ack = auditLogger.auditNhinDeferred201306(body, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION);

        // ASYNCMSG PROCESSING - RSPRCVD
        AsyncMessageProcessHelper asyncProcess = createAsyncProcesser();

        RespondingGatewayPRPAIN201306UV02RequestType nhinResponse = new RespondingGatewayPRPAIN201306UV02RequestType();
        nhinResponse.setPRPAIN201306UV02(body);
        nhinResponse.setAssertion(assertion);

        String messageId = "";
        if (assertion.getRelatesToList() != null && assertion.getRelatesToList().size() > 0) {
            messageId = assertion.getRelatesToList().get(0);
        }

        boolean bIsQueueOk = asyncProcess.processPatientDiscoveryResponse(messageId, AsyncMsgRecordDao.QUEUE_STATUS_RSPRCVD, AsyncMsgRecordDao.QUEUE_STATUS_RSPRCVDERR, nhinResponse);

        // Check if the Patient Discovery Async Response Service is enabled
        if (isServiceEnabled()) {
            // No need to check policy again as policy was already checked while sending request
            //if (checkPolicy(body, assertion)) {
                // Obtain the response mode in order to determine how the message is to be processed
                int respModeType = getResponseMode();

                if (respModeType == ResponseFactory.PASSTHRU_MODE) {
                    // Nothing to do here, empty target to cover the passthrough case
                } else if (respModeType == ResponseFactory.TRUST_MODE) {
                    // Store AA to HCID Mapping
                    storeMapping(body);

                    processRespTrustMode(body, assertion);
                } else {
                    // Store AA to HCID Mapping
                    storeMapping(body);

                    // Default is Verify Mode
                    processRespVerifyMode(body, assertion);
                }

                resp = sendToAdapter(body, assertion);
            /*} else {
                ackMsg = "Policy Check Failed for incoming Patient Discovery Deferred Response";
                log.warn(ackMsg);

                // Set the error acknowledgement status
                resp = HL7AckTransforms.createAckErrorFrom201306(body, ackMsg);
            }*/
        } else {
            ackMsg = "Patient Discovery Async Response Service Not Enabled";
            log.warn(ackMsg);

            // Set the error acknowledgement status
            resp = HL7AckTransforms.createAckErrorFrom201306(body, ackMsg);
        }

        // ASYNCMSG PROCESSING - RSPRCVDACK
        bIsQueueOk = asyncProcess.processAck(messageId, AsyncMsgRecordDao.QUEUE_STATUS_RSPRCVDACK, AsyncMsgRecordDao.QUEUE_STATUS_RSPRCVDERR, resp);

        // Audit the responding ack Message
        ack = auditLogger.auditAck(resp, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);

        return resp;
    }

    protected int getResponseMode() {
        ResponseFactory respFactory = new ResponseFactory();

        return respFactory.getResponseModeType();
    }

    /**
     * Checks the gateway.properties file to see if the Patient Discovery Async Response Service is enabled.
     *
     * @return Returns true if the servicePatientDiscoveryAsyncReq is enabled in the properties file.
     */
    protected boolean isServiceEnabled() {
        return NhinPatientDiscoveryUtils.isServiceEnabled(NhincConstants.NHINC_PATIENT_DISCOVERY_ASYNC_RESP_SERVICE_NAME);
    }

    protected MCCIIN000002UV01 sendToAdapter(PRPAIN201306UV02 body, AssertionType assertion) {
        PatientDiscoveryAuditLogger auditLogger = new PatientDiscoveryAuditLogger();
        AcknowledgementType ack = auditLogger.auditAdapterDeferred201306(body, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION);

        PatientDiscoveryAdapterSender adapterSender = new PatientDiscoveryAdapterSender();

        MCCIIN000002UV01 resp = adapterSender.sendDeferredRespToAgency(body, assertion);

        ack = auditLogger.auditAck(resp, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE);

        return resp;
    }

    protected void processRespVerifyMode(PRPAIN201306UV02 body, AssertionType assertion) {
        // In Verify Mode:
        //    1)  Query MPI to verify the patient is a match.
        //    2)  If a match is found in MPI then proceed with the correlation
        //
        // Note: Currently only the message from the Nhin is returned to the Agency so there is no
        //       need for this method to return a value.
        VerifyMode respProcessor = new VerifyMode();
        PRPAIN201305UV02 pRPAIN201305UV02 = getPrpain201305Request(assertion);
        respProcessor.processResponse(pRPAIN201305UV02, body, assertion);
    }

    protected void processRespTrustMode(PRPAIN201306UV02 body, AssertionType assertion) {
        // In Trust Mode:
        //    1)  Query async database for a record corresponding to the message/relatesto id
        //    2)  If a record is found then proceed with correlation
        //
        // Note: Currently only the message from the Nhin is returned to the Agency so there is no
        //       need for this method to return a value.

        II patId = getPatientId(assertion);

        TrustMode respProcessor = new TrustMode();
        respProcessor.processResponse(body, assertion, patId);

    }

    private PRPAIN201305UV02 getPrpain201305Request(AssertionType assertion) {

        AsyncMsgRecordDao asyncDbDao = new AsyncMsgRecordDao();

        String messageId = "";
        if (assertion.getRelatesToList() != null && assertion.getRelatesToList().size() > 0) {
            messageId = assertion.getRelatesToList().get(0);
        }

        List<AsyncMsgRecord> asyncMsgRecs = asyncDbDao.queryByMessageId(messageId);
        RespondingGatewayPRPAIN201305UV02RequestType respondingGatewayPRPAIN201305UV02RequestType = new RespondingGatewayPRPAIN201305UV02RequestType();
        PRPAIN201305UV02 pRPAIN201305UV02 = new PRPAIN201305UV02();
        if (NullChecker.isNotNullish(asyncMsgRecs)) {
            AsyncMsgRecord dbRec = asyncMsgRecs.get(0);
            //patId = extractPatId(dbRec.getMsgData());
            if (dbRec.getMsgData() != null) {
                respondingGatewayPRPAIN201305UV02RequestType = extractRespondingGatewayPRPAIN201305UV02RequestType(dbRec.getMsgData());
            }

            pRPAIN201305UV02 = respondingGatewayPRPAIN201305UV02RequestType.getPRPAIN201305UV02();


        }
        return pRPAIN201305UV02;

    }

    private II getPatientId(AssertionType assertion) {

        II patId = new II();
        PRPAIN201305UV02 pRPAIN201305UV02 = getPrpain201305Request(assertion);
        patId = extractPatientIdFrom201305(pRPAIN201305UV02);

        return patId;
    }

    private II extractPatId(Blob msgData) {
        II patId = new II();

        if (msgData != null) {
            try {
                XMLDecoder xdec = new XMLDecoder(msgData.getBinaryStream());

                try {
                    Object o = xdec.readObject();
                    patId = (II) o;
                } finally {
                    xdec.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                log.error(ex.getMessage());
            }

            log.debug("Patient Id Retrieved From the Database: " + patId.getExtension() + " " + patId.getRoot());
        } else {
            log.error("Message Data contained in the database was null");
        }

        return patId;
    }

    protected boolean checkPolicy(PRPAIN201306UV02 response, AssertionType assertion) {
        PatientDiscoveryPolicyChecker policyChecker = new PatientDiscoveryPolicyChecker();

        II patIdOverride = new II();

        if (NullChecker.isNotNullish(response.getControlActProcess().getSubject()) &&
                response.getControlActProcess().getSubject().get(0) != null &&
                response.getControlActProcess().getSubject().get(0).getRegistrationEvent() != null &&
                response.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1() != null &&
                response.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient() != null &&
                NullChecker.isNotNullish(response.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId()) &&
                response.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0) != null &&
                NullChecker.isNotNullish(response.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0).getExtension()) &&
                NullChecker.isNotNullish(response.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0).getRoot())) {
            patIdOverride.setExtension(response.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0).getExtension());
            patIdOverride.setRoot(response.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0).getRoot());
        } else {
            patIdOverride = null;
        }

        return policyChecker.check201305Policy(response, patIdOverride, assertion);
    }

    protected void storeMapping(PRPAIN201306UV02 msg) {
        PatientDiscovery201306Processor msgProcessor = new PatientDiscovery201306Processor();
        msgProcessor.storeMapping(msg);
    }

    private RespondingGatewayPRPAIN201305UV02RequestType extractRespondingGatewayPRPAIN201305UV02RequestType(Blob msgData) {
        log.debug("Begin EntityPatientDiscoveryDeferredReqQueueProcessOrchImpl.extractRespondingGatewayPRPAIN201305UV02RequestType()..");
        RespondingGatewayPRPAIN201305UV02RequestType respondingGatewayPRPAIN201305UV02RequestType = new RespondingGatewayPRPAIN201305UV02RequestType();
        try {
            byte[] msgBytes = null;
            if (msgData != null) {
                msgBytes = msgData.getBytes(1, (int) msgData.length());
                ByteArrayInputStream xmlContentBytes = new ByteArrayInputStream(msgBytes);
                JAXBContext context = JAXBContext.newInstance("org.hl7.v3");
                Unmarshaller u = context.createUnmarshaller();
                JAXBElement<RespondingGatewayPRPAIN201305UV02RequestType> root = (JAXBElement<RespondingGatewayPRPAIN201305UV02RequestType>) u.unmarshal(xmlContentBytes);

                respondingGatewayPRPAIN201305UV02RequestType = root.getValue();
                log.debug("End EntityPatientDiscoveryDeferredReqQueueProcessOrchImpl.extractRespondingGatewayPRPAIN201305UV02RequestType()..");
            }
        } catch (Exception e) {
            log.error("Exception during Blob conversion :" + e.getMessage());
            e.printStackTrace();
        }
        return respondingGatewayPRPAIN201305UV02RequestType;
    }

    private String extractAAOID(PRPAIN201305UV02 request) {
        String oid = null;

        if (request != null &&
                request.getControlActProcess() != null &&
                NullChecker.isNotNullish(request.getControlActProcess().getAuthorOrPerformer()) &&
                request.getControlActProcess().getAuthorOrPerformer().get(0) != null &&
                request.getControlActProcess().getAuthorOrPerformer().get(0).getAssignedDevice() != null &&
                request.getControlActProcess().getAuthorOrPerformer().get(0).getAssignedDevice().getValue() != null &&
                NullChecker.isNotNullish(request.getControlActProcess().getAuthorOrPerformer().get(0).getAssignedDevice().getValue().getId()) &&
                request.getControlActProcess().getAuthorOrPerformer().get(0).getAssignedDevice().getValue().getId().get(0) != null &&
                NullChecker.isNotNullish(request.getControlActProcess().getAuthorOrPerformer().get(0).getAssignedDevice().getValue().getId().get(0).getRoot())) {
            oid = request.getControlActProcess().getAuthorOrPerformer().get(0).getAssignedDevice().getValue().getId().get(0).getRoot();
        }

        return oid;
    }

    public II extractPatientIdFrom201305(PRPAIN201305UV02 request) {
        II patId = null;
        String aaId = null;

        if (request != null &&
                request.getControlActProcess() != null) {

            aaId = extractAAOID(request);

            if (NullChecker.isNotNullish(aaId)) {
                if (request.getControlActProcess().getQueryByParameter() != null &&
                        request.getControlActProcess().getQueryByParameter().getValue() != null &&
                        request.getControlActProcess().getQueryByParameter().getValue().getParameterList() != null &&
                        NullChecker.isNotNullish(request.getControlActProcess().getQueryByParameter().getValue().getParameterList().getLivingSubjectId())) {
                    for (PRPAMT201306UV02LivingSubjectId livingSubId : request.getControlActProcess().getQueryByParameter().getValue().getParameterList().getLivingSubjectId()) {
                        for (II id : livingSubId.getValue()) {
                            if (id != null &&
                                    NullChecker.isNotNullish(id.getRoot()) &&
                                    NullChecker.isNotNullish(id.getExtension()) &&
                                    aaId.contentEquals(id.getRoot())) {
                                patId = new II();
                                patId.setRoot(id.getRoot());
                                patId.setExtension(id.getExtension());

                                // break out of inner loop
                                break;
                            }
                        }

                        // If the patient id was found then break out of outer loop
                        if (patId != null) {
                            break;
                        }
                    }
                }
            }
        }

        return patId;
    }
}
