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

package gov.hhs.fha.nhinc.admindistribution.passthru;
import oasis.names.tc.emergency.edxl.de._1.EDXLDistribution;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import gov.hhs.fha.nhinc.admindistribution.nhin.proxy.NhinAdminDistributionProxy;
import gov.hhs.fha.nhinc.admindistribution.nhin.proxy.NhinAdminDistributionProxyObjectFactory;
import gov.hhs.fha.nhinc.admindistribution.AdminDistributionAuditLogger;
import gov.hhs.fha.nhinc.admindistribution.nhin.proxy.NhinAdminDistributionProxyWebServiceSecuredImpl;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
/**
 *
 * @author dunnek
 */
public class PassthruAdminDistributionOrchImpl {
   private Log log = null;

    public PassthruAdminDistributionOrchImpl()
    {
        log = createLogger();
    }
    protected Log createLogger()
    {
        return LogFactory.getLog(getClass());
    }
    public void sendAlertMessage(EDXLDistribution body, AssertionType assertion, NhinTargetSystemType target)
    {
        log.info("begin sendAlert");
        //TODO: LogRequest        
        AcknowledgementType ack = getLogger().auditNhincAdminDist(body, assertion, target, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION);
        if (ack != null)
        {
            log.debug("ack: " + ack.getMessage());
        }

        getNhinProxy().sendAlertMessage(body, assertion, target);        

    }
    protected AdminDistributionAuditLogger getLogger()
    {
        return new AdminDistributionAuditLogger();
    }
    protected NhinAdminDistributionProxy getNhinProxy()
    {
        return new NhinAdminDistributionProxyObjectFactory().getNhinAdminDistProxy();
    }

}
