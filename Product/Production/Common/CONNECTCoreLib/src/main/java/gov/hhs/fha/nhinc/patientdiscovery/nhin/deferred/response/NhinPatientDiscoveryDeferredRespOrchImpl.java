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

package gov.hhs.fha.nhinc.patientdiscovery.nhin.deferred.response;

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
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import java.beans.XMLDecoder;
import java.sql.Blob;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.II;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201306UV02;


/**
 *
 * @author jhoppesc
 */
public class NhinPatientDiscoveryDeferredRespOrchImpl {
    private static Log log = LogFactory.getLog(NhinPatientDiscoveryDeferredRespOrchImpl.class);

    public MCCIIN000002UV01 respondingGatewayPRPAIN201306UV02Orch(PRPAIN201306UV02 body, AssertionType assertion)
    {
        MCCIIN000002UV01 resp = new MCCIIN000002UV01();

        // Audit the incoming Nhin 201306 Message
        PatientDiscoveryAuditLogger auditLogger = new PatientDiscoveryAuditLogger();
        AcknowledgementType ack = auditLogger.auditNhin201306(body, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION);

        // Check if the Patient Discovery Async Response Service is enabled
        if (isServiceEnabled())
        {
            // Perform a policy check
            if (checkPolicy(body, assertion))
            {
                // Obtain the response mode in order to determine how the message is to be processed
                int respModeType = getResponseMode();

                if (respModeType == ResponseFactory.PASSTHRU_MODE)
                {
                    // Nothing to do here, empty target to cover the passthrough case
                } else if (respModeType == ResponseFactory.TRUST_MODE)
                {
                    // Store AA to HCID Mapping
                    storeMapping(body);

                    processRespTrustMode(body, assertion);
                } else
                {
                    // Store AA to HCID Mapping
                    storeMapping(body);

                    // Default is Verify Mode
                    processRespVerifyMode(body, assertion);
                }
            } else
            {
                log.error("Policy Check Failed");
                body.getControlActProcess().getSubject().clear();
            }
        } else
        {
            log.error("Patient Discovery Async Response Service Not Enabled");
            body.getControlActProcess().getSubject().clear();
        }

        resp = sendToAdapter(body, assertion);

        // Audit the responding ack Message
        ack = auditLogger.auditAck(resp, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);

        return resp;
    }

    protected int getResponseMode()
    {
        ResponseFactory respFactory = new ResponseFactory();

        return respFactory.getResponseModeType();
    }

    /**
     * Checks the gateway.properties file to see if the Patient Discovery Async Response Service is enabled.
     *
     * @return Returns true if the servicePatientDiscoveryAsyncReq is enabled in the properties file.
     */
    protected boolean isServiceEnabled()
    {
        return NhinPatientDiscoveryUtils.isServiceEnabled(NhincConstants.NHINC_PATIENT_DISCOVERY_ASYNC_RESP_SERVICE_NAME);
    }

    protected MCCIIN000002UV01 sendToAdapter(PRPAIN201306UV02 body, AssertionType assertion)
    {
        PatientDiscoveryAuditLogger auditLogger = new PatientDiscoveryAuditLogger();
        AcknowledgementType ack = auditLogger.auditAdapter201306(body, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION);

        PatientDiscoveryAdapterSender adapterSender = new PatientDiscoveryAdapterSender();

        MCCIIN000002UV01 resp = adapterSender.sendDeferredRespToAgency(body, assertion);

        ack = auditLogger.auditAck(resp, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE);

        return resp;
    }

    protected void processRespVerifyMode(PRPAIN201306UV02 body, AssertionType assertion)
    {
        // In Verify Mode:
        //    1)  Query MPI to verify the patient is a match.
        //    2)  If a match is found in MPI then proceed with the correlation
        //
        // Note: Currently only the message from the Nhin is returned to the Agency so there is no
        //       need for this method to return a value.
        VerifyMode respProcessor = new VerifyMode();
        PRPAIN201306UV02 resp = respProcessor.processResponse(body, assertion);

        AsyncMsgRecordDao asyncDbDao = new AsyncMsgRecordDao();
        removeExpiredEntries(asyncDbDao);
    }

    protected void processRespTrustMode(PRPAIN201306UV02 body, AssertionType assertion)
    {
        // In Trust Mode:
        //    1)  Query async database for a record corresponding to the message/relatesto id
        //    2)  If a record is found then proceed with correlation
        //
        // Note: Currently only the message from the Nhin is returned to the Agency so there is no
        //       need for this method to return a value.
        II patId = new II();
        AsyncMsgRecordDao asyncDbDao = new AsyncMsgRecordDao();

        List<AsyncMsgRecord> asyncMsgRecs = asyncDbDao.queryByMessageId(assertion.getMessageId());

        if (NullChecker.isNotNullish(asyncMsgRecs))
        {
            AsyncMsgRecord dbRec = asyncMsgRecs.get(0);
            patId = extractPatId(dbRec.getMsgData());

            TrustMode respProcessor = new TrustMode();
            PRPAIN201306UV02 resp = respProcessor.processResponse(body, assertion, patId);

            // Clean up database entry
            cleanupDatabase(dbRec);
        }
    }

    private II extractPatId(Blob msgData)
    {
        II patId = new II();

        if (msgData != null)
        {
            try
            {
                XMLDecoder xdec = new XMLDecoder(msgData.getBinaryStream());

                try
                {
                    Object o = xdec.readObject();
                    patId = (II) o;
                } finally
                {
                    xdec.close();
                }
            } catch (Exception ex)
            {
                ex.printStackTrace();
                log.error(ex.getMessage());
            }

            log.debug("Patient Id Retrieved From the Database: " + patId.getExtension() + " " + patId.getRoot());
        } else
        {
            log.error("Message Data contained in the database was null");
        }

        return patId;
    }

    protected boolean checkPolicy(PRPAIN201306UV02 response, AssertionType assertion)
    {
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
                NullChecker.isNotNullish(response.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0).getRoot()))
        {
            patIdOverride.setExtension(response.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0).getExtension());
            patIdOverride.setRoot(response.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0).getRoot());
        } else
        {
            patIdOverride = null;
        }

        return policyChecker.check201305Policy(response, patIdOverride, assertion);
    }

    protected void storeMapping(PRPAIN201306UV02 msg)
    {
        PatientDiscovery201306Processor msgProcessor = new PatientDiscovery201306Processor();
        msgProcessor.storeMapping(msg);
    }

    protected void cleanupDatabase(AsyncMsgRecord dbRec)
    {
        AsyncMsgRecordDao asyncDbDao = new AsyncMsgRecordDao();

        // Delete the specific database record that was passed into this method.
        asyncDbDao.delete(dbRec);

        // Clean up all database entries that have "expired"
        removeExpiredEntries(asyncDbDao);
    }

    private void removeExpiredEntries(AsyncMsgRecordDao asyncDbDao)
    {
        // Read the delta properties from the gateway.properties file
        long value = 0;
        String units = null;

        try
        {
            value = PropertyAccessor.getPropertyLong(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.ASYNC_DB_REC_EXP_VAL_PROP);
        } catch (PropertyAccessException ex)
        {
            log.error("Error: Failed to retrieve " + NhincConstants.ASYNC_DB_REC_EXP_VAL_PROP + " from property file: " + NhincConstants.GATEWAY_PROPERTY_FILE);
            log.error(ex.getMessage());
        }

        try
        {
            units = PropertyAccessor.getProperty(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.ASYNC_DB_REC_EXP_VAL_UNITS_PROP);
        } catch (PropertyAccessException ex)
        {
            log.error("Error: Failed to retrieve " + NhincConstants.ASYNC_DB_REC_EXP_VAL_UNITS_PROP + " from property file: " + NhincConstants.GATEWAY_PROPERTY_FILE);
            log.error(ex.getMessage());
        }

        // Determine the time to query on
        Date expirationValue = calculateExpirationValue(value, units);

        // Query the database for all records older then the calculated time
        List<AsyncMsgRecord> asyncMsgRecs = asyncDbDao.queryByTime(expirationValue);

        // Delete all of the records that were returned from the query
        for (AsyncMsgRecord rec : asyncMsgRecs)
        {
            asyncDbDao.delete(rec);
        }
    }

    private Date calculateExpirationValue(long value, String units)
    {
        Calendar currentTime = Calendar.getInstance(TimeZone.getTimeZone("GMT"));

        // Convert the long to a Long Object and change the sign to negative so our query value ends up in the past.
        Long longObj = Long.valueOf(0 - value);

        if (units.equalsIgnoreCase(NhincConstants.ASYNC_DB_REC_EXP_VAL_UNITS_SEC))
        {
            currentTime.add(Calendar.SECOND, longObj.intValue());
        } else if (units.equalsIgnoreCase(NhincConstants.ASYNC_DB_REC_EXP_VAL_UNITS_MIN))
        {
            currentTime.add(Calendar.MINUTE, longObj.intValue());
        } else if (units.equalsIgnoreCase(NhincConstants.ASYNC_DB_REC_EXP_VAL_UNITS_HOUR))
        {
            currentTime.add(Calendar.HOUR_OF_DAY, longObj.intValue());
        } else
        {
            // Default to days
            currentTime.add(Calendar.DAY_OF_YEAR, longObj.intValue());
        }

        Date expirationValue = currentTime.getTime();

        return expirationValue;
    }

}
