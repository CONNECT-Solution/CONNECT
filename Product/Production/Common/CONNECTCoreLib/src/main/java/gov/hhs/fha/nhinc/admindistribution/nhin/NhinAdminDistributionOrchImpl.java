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

package gov.hhs.fha.nhinc.admindistribution.nhin;
import gov.hhs.fha.nhinc.admindistribution.AdminDistributionAuditLogger;
import gov.hhs.fha.nhinc.admindistribution.AdminDistributionHelper;
import gov.hhs.fha.nhinc.admindistribution.AdminDistributionPolicyChecker;
import gov.hhs.fha.nhinc.admindistribution.adapter.proxy.AdapterAdminDistributionProxyObjectFactory;
import gov.hhs.fha.nhinc.admindistribution.adapter.proxy.AdapterAdminDistributionProxy;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import oasis.names.tc.emergency.edxl.de._1.EDXLDistribution;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewaySendAlertMessageType;
import gov.hhs.fha.nhinc.connectmgr.data.CMUrlInfos;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.connectmgr.data.CMUrlInfo;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewaySendAlertMessageSecuredType;
import gov.hhs.fha.nhinc.admindistribution.passthru.proxy.PassthruAdminDistributionProxy;
import gov.hhs.fha.nhinc.admindistribution.passthru.proxy.PassthruAdminDistributionProxyObjectFactory;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;

/**
 *
 * @author dunnek
 */
public class NhinAdminDistributionOrchImpl {
    private Log log = null;

    public NhinAdminDistributionOrchImpl()
    {
        log = createLogger();
    }
    protected Log createLogger()
    {
        return LogFactory.getLog(getClass());
    }
    public void sendAlertMessage(EDXLDistribution body, AssertionType assertion)
    {
        log.info("begin sendAlert");
        //With the one-way service in a one-machine setup,
        //we were hanging on the next webservice call.
        //sleep allows Glassfish to catch up. Only applies to one box (dev)
        //setups. Please refer to the CONNECT 3.1 Release Notes for more information. 
        this.checkSleep();
        
        AcknowledgementType ack = getLogger().auditNhinAdminDist(body, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION);
        if (ack != null)
        {
            log.debug("ack: " + ack.getMessage());
        }

        if(isServiceEnabled())
        {
            if(this.isInPassThroughMode())
            {
                sendToAgency(body, assertion);
            }
            else if(checkPolicy(body, assertion))
            {
                sendToAgency(body, assertion);
            }
        }
        else
        {
            log.warn("Service is disabled");
        }


        log.info("End sendAlert");
    }
    protected boolean isInPassThroughMode()
    {
        return new AdminDistributionHelper().isInPassThroughMode();
    }
    protected boolean isServiceEnabled()
    {
        return new AdminDistributionHelper().isServiceEnabled();
    }
    protected void sendToAgency(EDXLDistribution body, AssertionType assertion)
    {
        log.debug("begin send to agency");
        this.getAdapterAdminDistProxy().sendAlertMessage(body, assertion);

    }
    protected AdapterAdminDistributionProxy getAdapterAdminDistProxy()
    {
        return this.getAdminFactory().getAdapterAdminDistProxy();
    }
    protected AdapterAdminDistributionProxyObjectFactory getAdminFactory()
    {
        return new AdapterAdminDistributionProxyObjectFactory();
    }
    protected AdminDistributionAuditLogger getLogger()
    {
        return new AdminDistributionAuditLogger();
    }
    protected boolean checkPolicy(EDXLDistribution body, AssertionType assertion) 
    {
        boolean result = false;

        log.debug("begin checkPolicy");
        if (body != null) {
            result =  this.getPolicyChecker().checkIncomingPolicy(body, assertion);
        }
        else
        {
            log.warn("EDXLDistribution was null");
        }

        log.debug("End Check Policy");
        return result;
    }
    protected AdminDistributionPolicyChecker getPolicyChecker()
    {
        return new AdminDistributionPolicyChecker();
    }
    private void checkSleep()
    {
        long sleep = 0;

        try
        {
            sleep=this.getSleepPeriod();
            if(sleep > 0)
            {
                log.debug("Admindistribution is sleeping...");
                Thread.sleep(sleep);
            }
        }
        catch(Exception ex)
        {
            log.error("Unable to sleep thread: " + ex);
        }

        log.debug("End checkSleep()");
    }
    protected long getSleepPeriod()
    {
        String result = "0";
        try
        {
            result = PropertyAccessor.getProperty(NhincConstants.GATEWAY_PROPERTY_FILE, "administrativeDistributionSleepValue");
            log.debug("administrativeDistributionSleepValue = " + result);
        }
        catch(Exception ex)
        {
            log.warn("Unable to retrieve local home community id from Gateway.properties");
            log.warn(ex);
        }
        return Long.parseLong(result);
    }
}
