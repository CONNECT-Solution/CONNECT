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

package gov.hhs.fha.nhinc.admindistribution.passthru.proxy;
import oasis.names.tc.emergency.edxl.de._1.EDXLDistribution;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import gov.hhs.fha.nhinc.admindistribution.passthru.PassthruAdminDistributionOrchImpl;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
/**
 *
 * @author dunnek
 */
public class PassthruAdminDistributionProxyJavaImpl implements PassthruAdminDistributionProxy{
   private Log log = null;

    public PassthruAdminDistributionProxyJavaImpl()
    {
        log = createLogger();
    }
    private Log createLogger()
    {
        return LogFactory.getLog(getClass());
    }
    public void sendAlertMessage(EDXLDistribution body, AssertionType assertion, NhinTargetSystemType target,
            NhincConstants.GATEWAY_API_LEVEL apiLevel)
    {
        log.info("begin sendAlert");
        this.getNhincAdminDistImpl().sendAlertMessage(body, assertion, target, apiLevel);

    }
    protected PassthruAdminDistributionOrchImpl getNhincAdminDistImpl()
    {
        return new PassthruAdminDistributionOrchImpl();
    }
}
