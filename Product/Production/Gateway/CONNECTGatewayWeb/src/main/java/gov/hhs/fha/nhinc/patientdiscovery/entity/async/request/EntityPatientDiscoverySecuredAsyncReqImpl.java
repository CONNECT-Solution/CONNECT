/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.patientdiscovery.entity.async.request;

import gov.hhs.fha.nhinc.async.AsyncMessageIdExtractor;
import gov.hhs.fha.nhinc.asyncmsgs.dao.AsyncMsgRecordDao;
import gov.hhs.fha.nhinc.asyncmsgs.model.AsyncMsgRecord;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.connectmgr.data.CMUrlInfo;
import gov.hhs.fha.nhinc.connectmgr.data.CMUrlInfos;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinpatientdiscovery.async.request.proxy.NhinPatientDiscoveryAsyncReqProxy;
import gov.hhs.fha.nhinc.nhinpatientdiscovery.async.request.proxy.NhinPatientDiscoveryAsyncReqProxyObjectFactory;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscovery201305Processor;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryAuditLogger;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryPolicyChecker;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import gov.hhs.fha.nhinc.transform.subdisc.HL7AckTransforms;
import java.beans.XMLEncoder;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.xml.ws.WebServiceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02SecuredRequestType;
import java.sql.Blob;
import org.hl7.v3.II;
import org.hibernate.Hibernate;

/**
 *
 * @author jhoppesc
 */
public class EntityPatientDiscoverySecuredAsyncReqImpl {

    private static Log log = LogFactory.getLog(EntityPatientDiscoverySecuredAsyncReqImpl.class);

    public MCCIIN000002UV01 processPatientDiscoveryAsyncReq(RespondingGatewayPRPAIN201305UV02SecuredRequestType request, WebServiceContext context) {
        RespondingGatewayPRPAIN201305UV02RequestType unsecureRequest = new RespondingGatewayPRPAIN201305UV02RequestType();
        unsecureRequest.setNhinTargetCommunities(request.getNhinTargetCommunities());
        unsecureRequest.setPRPAIN201305UV02(request.getPRPAIN201305UV02());
        unsecureRequest.setAssertion(SamlTokenExtractor.GetAssertion(context));

        // Extract the message id value from the WS-Addressing Header and place it in the Assertion Class
        if (request != null &&
                unsecureRequest.getAssertion() != null) {
            unsecureRequest.getAssertion().setAsyncMessageId(AsyncMessageIdExtractor.GetAsyncMessageId(context));
        }

        // Audit the Patient Discovery Request Message sent on the Entity Interface
        PatientDiscoveryAuditLogger auditLog = new PatientDiscoveryAuditLogger();
        auditLog.auditEntity201305(unsecureRequest, unsecureRequest.getAssertion(), NhincConstants.AUDIT_LOG_INBOUND_DIRECTION);

        MCCIIN000002UV01 ack = processPatientDiscoveryAsyncReq(unsecureRequest);

        auditLog.auditAck(ack, unsecureRequest.getAssertion(), NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE);

        return ack;
    }

    public MCCIIN000002UV01 processPatientDiscoveryAsyncReq(RespondingGatewayPRPAIN201305UV02RequestType request) {
        MCCIIN000002UV01 ack = new MCCIIN000002UV01();
        CMUrlInfos urlInfoList = null;
        PatientDiscovery201305Processor pd201305Processor = new PatientDiscovery201305Processor();

        if (request != null &&
                request.getPRPAIN201305UV02() != null &&
                request.getAssertion() != null) {
            urlInfoList = getTargets(request.getNhinTargetCommunities());

            //loop through the communities and send request if results were not null
            if (urlInfoList != null &&
                    urlInfoList.getUrlInfo() != null) {
                for (CMUrlInfo urlInfo : urlInfoList.getUrlInfo()) {

                    //create a new request to send out to each target community
                    RespondingGatewayPRPAIN201305UV02RequestType newRequest = new RespondingGatewayPRPAIN201305UV02RequestType();
                    PRPAIN201305UV02 new201305 = pd201305Processor.createNewRequest(request.getPRPAIN201305UV02(), urlInfo.getHcid());

                    newRequest.setAssertion(request.getAssertion());
                    newRequest.setPRPAIN201305UV02(new201305);
                    newRequest.setNhinTargetCommunities(request.getNhinTargetCommunities());

                    //check the policy for the outgoing request to the target community
                    boolean bIsPolicyOk = checkPolicy(newRequest);

                    if (bIsPolicyOk) {
                        addEntryToDatabase(newRequest);

                        ack = sendToProxy(newRequest, urlInfo);
                    } else {
                        ack = HL7AckTransforms.createAckFrom201305(request.getPRPAIN201305UV02(), "Policy Failed");
                    }
                }
            } else {
                log.warn("No targets were found for the Patient Discovery Request");
                ack = HL7AckTransforms.createAckFrom201305(request.getPRPAIN201305UV02(), "No Targets Found");
            }
        }

        return ack;
    }

    protected CMUrlInfos getTargets(NhinTargetCommunitiesType targetCommunities) {
        CMUrlInfos urlInfoList = null;

        // Obtain all the URLs for the targets being sent to
        try {
            urlInfoList = ConnectionManagerCache.getEndpontURLFromNhinTargetCommunities(targetCommunities, NhincConstants.PATIENT_DISCOVERY_ASYNC_REQ_SERVICE_NAME);
        } catch (ConnectionManagerException ex) {
            log.error("Failed to obtain target URLs for service " + NhincConstants.PATIENT_DISCOVERY_ASYNC_REQ_SERVICE_NAME);
            return null;
        }

        return urlInfoList;
    }

    protected boolean checkPolicy(RespondingGatewayPRPAIN201305UV02RequestType request) {
        return new PatientDiscoveryPolicyChecker().checkOutgoingPolicy(request);
    }

    protected MCCIIN000002UV01 sendToProxy(RespondingGatewayPRPAIN201305UV02RequestType request, CMUrlInfo urlInfo) {
        MCCIIN000002UV01 resp = new MCCIIN000002UV01();

        NhinTargetSystemType oTargetSystemType = new NhinTargetSystemType();
        oTargetSystemType.setUrl(urlInfo.getUrl());

        // Audit the Patient Discovery Request Message sent on the Nhin Interface
        PatientDiscoveryAuditLogger auditLog = new PatientDiscoveryAuditLogger();
        AcknowledgementType ack = auditLog.auditNhin201305(request.getPRPAIN201305UV02(), request.getAssertion(), NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION);

        NhinPatientDiscoveryAsyncReqProxyObjectFactory patientDiscoveryFactory = new NhinPatientDiscoveryAsyncReqProxyObjectFactory();
        NhinPatientDiscoveryAsyncReqProxy proxy = patientDiscoveryFactory.getNhinPatientDiscoveryAsyncReqProxy();

        resp = proxy.respondingGatewayPRPAIN201305UV02(request.getPRPAIN201305UV02(), request.getAssertion(), oTargetSystemType);

        // Audit the Patient Discovery Response Message received on the Nhin Interface
        auditLog.auditAck(resp, request.getAssertion(), NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);

        return resp;
    }

    protected void addEntryToDatabase(RespondingGatewayPRPAIN201305UV02RequestType request) {
        List<AsyncMsgRecord> asyncMsgRecs = new ArrayList<AsyncMsgRecord>();
        AsyncMsgRecord rec = new AsyncMsgRecord();
        AsyncMsgRecordDao instance = new AsyncMsgRecordDao();

        // Replace with message id from the assertion class
        rec.setMessageId(request.getAssertion().getAsyncMessageId());
        rec.setCreationTime(new Date());
        rec.setServiceName(NhincConstants.PATIENT_DISCOVERY_SERVICE_NAME);
        rec.setMsgData(createBlob(request));
        asyncMsgRecs.add(rec);

        boolean result = instance.insertRecords(asyncMsgRecs);

        if (result == false) {
            log.error("Failed to insert asynchronous record in the database");
        }
    }

    private Blob createBlob(RespondingGatewayPRPAIN201305UV02RequestType request) {
        Blob data = null;

        PatientDiscovery201305Processor msgProcessor = new PatientDiscovery201305Processor();
        ByteArrayOutputStream baOutStrm = new ByteArrayOutputStream();
        
        if (request != null &&
                request.getPRPAIN201305UV02() != null) {
            II patId = msgProcessor.extractPatientIdFrom201305(request.getPRPAIN201305UV02());
            baOutStrm.reset();

            try {
                // Create XML encoder.
                XMLEncoder xenc = new XMLEncoder(baOutStrm);
                try {
                    // Write object.
                    xenc.writeObject(patId);
                    xenc.flush();
                } finally {
                    xenc.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                log.error(ex.getMessage());
            }

            byte[] buffer = baOutStrm.toByteArray();
            log.debug("Byte Array: " + baOutStrm.toString());

            data = Hibernate.createBlob(buffer);
        }

        return data;
    }
}
