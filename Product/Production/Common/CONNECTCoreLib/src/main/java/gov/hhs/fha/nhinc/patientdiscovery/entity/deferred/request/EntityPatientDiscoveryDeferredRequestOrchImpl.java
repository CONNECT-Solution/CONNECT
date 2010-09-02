/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.patientdiscovery.entity.deferred.request;

import gov.hhs.fha.nhinc.asyncmsgs.dao.AsyncMsgRecordDao;
import gov.hhs.fha.nhinc.asyncmsgs.model.AsyncMsgRecord;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.connectmgr.data.CMUrlInfo;
import gov.hhs.fha.nhinc.connectmgr.data.CMUrlInfos;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscovery201305Processor;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryAuditLogger;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryPolicyChecker;
import gov.hhs.fha.nhinc.patientdiscovery.nhin.deferred.request.proxy.NhinPatientDiscoveryDeferredReqProxy;
import gov.hhs.fha.nhinc.patientdiscovery.nhin.deferred.request.proxy.NhinPatientDiscoveryDeferredReqProxyObjectFactory;
import gov.hhs.fha.nhinc.patientdiscovery.passthru.deferred.request.proxy.PassthruPatientDiscoveryDeferredRequestProxy;
import gov.hhs.fha.nhinc.patientdiscovery.passthru.deferred.request.proxy.PassthruPatientDiscoveryDeferredRequestProxyObjectFactory;
import gov.hhs.fha.nhinc.transform.subdisc.HL7AckTransforms;
import java.beans.XMLEncoder;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import java.sql.Blob;
import org.hl7.v3.II;
import org.hibernate.Hibernate;

public class EntityPatientDiscoveryDeferredRequestOrchImpl
{

    private Log log = null;

    public EntityPatientDiscoveryDeferredRequestOrchImpl()
    {
        log = createLogger();
    }

    protected Log createLogger()
    {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }

    protected PatientDiscoveryAuditLogger createAuditLogger()
    {
        return new PatientDiscoveryAuditLogger();
    }

    public MCCIIN000002UV01 processPatientDiscoveryAsyncReq(PRPAIN201305UV02 message,
            AssertionType assertion, NhinTargetCommunitiesType targets)
    {
        MCCIIN000002UV01 ack = new MCCIIN000002UV01();
        CMUrlInfos urlInfoList = null;
        PatientDiscovery201305Processor pd201305Processor = new PatientDiscovery201305Processor();

        if (message != null &&
                assertion != null)
        {
            // Audit the Patient Discovery Request Message sent on the Entity Interface
            PatientDiscoveryAuditLogger auditLog = createAuditLogger();

            RespondingGatewayPRPAIN201305UV02RequestType unsecureRequest = new RespondingGatewayPRPAIN201305UV02RequestType();
            unsecureRequest.setNhinTargetCommunities(targets);
            unsecureRequest.setPRPAIN201305UV02(message);
            unsecureRequest.setAssertion(assertion);
            auditLog.auditEntity201305(unsecureRequest, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION);


            urlInfoList = getTargets(targets);

            //loop through the communities and send request if results were not null
            if (urlInfoList != null &&
                    urlInfoList.getUrlInfo() != null)
            {
                for (CMUrlInfo urlInfo : urlInfoList.getUrlInfo())
                {

                    //create a new request to send out to each target community
                    RespondingGatewayPRPAIN201305UV02RequestType newRequest = new RespondingGatewayPRPAIN201305UV02RequestType();
                    PRPAIN201305UV02 new201305 = pd201305Processor.createNewRequest(message, urlInfo.getHcid());

                    newRequest.setAssertion(assertion);
                    newRequest.setPRPAIN201305UV02(new201305);
                    newRequest.setNhinTargetCommunities(targets);

                    //check the policy for the outgoing request to the target community
                    boolean bIsPolicyOk = checkPolicy(newRequest);

                    if (bIsPolicyOk)
                    {
                        addEntryToDatabase(newRequest);

                        ack = sendToProxy(newRequest, urlInfo);
                    } else
                    {
                        ack = HL7AckTransforms.createAckFrom201305(message, "Policy Failed");
                    }
                }
            } else
            {
                log.warn("No targets were found for the Patient Discovery Request");
                ack = HL7AckTransforms.createAckFrom201305(message, "No Targets Found");
            }

            auditLog.auditAck(ack, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE);

        }

        return ack;
    }

    protected CMUrlInfos getTargets(NhinTargetCommunitiesType targetCommunities)
    {
        CMUrlInfos urlInfoList = null;

        // Obtain all the URLs for the targets being sent to
        try
        {
            urlInfoList = ConnectionManagerCache.getEndpontURLFromNhinTargetCommunities(targetCommunities, NhincConstants.PATIENT_DISCOVERY_ASYNC_REQ_SERVICE_NAME);
        } catch (ConnectionManagerException ex)
        {
            log.error("Failed to obtain target URLs for service " + NhincConstants.PATIENT_DISCOVERY_ASYNC_REQ_SERVICE_NAME);
            return null;
        }

        return urlInfoList;
    }

    protected boolean checkPolicy(RespondingGatewayPRPAIN201305UV02RequestType request)
    {
        return new PatientDiscoveryPolicyChecker().checkOutgoingPolicy(request);
    }

    protected MCCIIN000002UV01 sendToProxy(RespondingGatewayPRPAIN201305UV02RequestType request, CMUrlInfo urlInfo)
    {
        MCCIIN000002UV01 resp = new MCCIIN000002UV01();

        NhinTargetSystemType oTargetSystemType = new NhinTargetSystemType();
        oTargetSystemType.setUrl(urlInfo.getUrl());

        // Audit the Patient Discovery Request Message sent on the Nhin Interface
        PatientDiscoveryAuditLogger auditLog = new PatientDiscoveryAuditLogger();
        AcknowledgementType ack = auditLog.auditNhin201305(request.getPRPAIN201305UV02(), request.getAssertion(), NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION);

        PassthruPatientDiscoveryDeferredRequestProxyObjectFactory patientDiscoveryFactory = new PassthruPatientDiscoveryDeferredRequestProxyObjectFactory();
        PassthruPatientDiscoveryDeferredRequestProxy proxy = patientDiscoveryFactory.getPassthruPatientDiscoveryDeferredRequestProxy();

        log.debug("Invoking " + proxy + ".processPatientDiscoveryAsyncReq with " + request.getPRPAIN201305UV02()
                + " assertion: " + request.getAssertion() + " and target " + oTargetSystemType + " url: " + oTargetSystemType.getUrl());
        resp = proxy.processPatientDiscoveryAsyncReq(request.getPRPAIN201305UV02(), request.getAssertion(), oTargetSystemType);

        // Audit the Patient Discovery Response Message received on the Nhin Interface
        auditLog.auditAck(resp, request.getAssertion(), NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);

        return resp;
    }

    protected void addEntryToDatabase(RespondingGatewayPRPAIN201305UV02RequestType request)
    {
        List<AsyncMsgRecord> asyncMsgRecs = new ArrayList<AsyncMsgRecord>();
        AsyncMsgRecord rec = new AsyncMsgRecord();
        AsyncMsgRecordDao instance = new AsyncMsgRecordDao();

        // Replace with message id from the assertion class
        rec.setMessageId(request.getAssertion().getMessageId());
        rec.setCreationTime(new Date());
        rec.setServiceName(NhincConstants.PATIENT_DISCOVERY_SERVICE_NAME);
        rec.setMsgData(createBlob(request));
        asyncMsgRecs.add(rec);

        boolean result = instance.insertRecords(asyncMsgRecs);

        if (result == false)
        {
            log.error("Failed to insert asynchronous record in the database");
        }
    }

    private Blob createBlob(RespondingGatewayPRPAIN201305UV02RequestType request)
    {
        Blob data = null;

        PatientDiscovery201305Processor msgProcessor = new PatientDiscovery201305Processor();
        ByteArrayOutputStream baOutStrm = new ByteArrayOutputStream();

        if (request != null &&
                request.getPRPAIN201305UV02() != null)
        {
            II patId = msgProcessor.extractPatientIdFrom201305(request.getPRPAIN201305UV02());
            baOutStrm.reset();

            try
            {
                // Create XML encoder.
                XMLEncoder xenc = new XMLEncoder(baOutStrm);
                try
                {
                    // Write object.
                    xenc.writeObject(patId);
                    xenc.flush();
                } finally
                {
                    xenc.close();
                }
            } catch (Exception ex)
            {
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
