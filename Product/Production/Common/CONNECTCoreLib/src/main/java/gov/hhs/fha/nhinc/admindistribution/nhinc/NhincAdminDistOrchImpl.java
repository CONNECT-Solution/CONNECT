/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.admindistribution.nhinc;
import oasis.names.tc.emergency.edxl.de._1.EDXLDistribution;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import gov.hhs.fha.nhinc.admindistribution.nhin.proxy.NhinAdminDistProxy;
import gov.hhs.fha.nhinc.admindistribution.nhin.proxy.NhinAdminDistObjectFactory;
import gov.hhs.fha.nhinc.admindistribution.AdminDistributionAuditLogger;
import gov.hhs.fha.nhinc.admindistribution.nhin.proxy.NhinAdminDistSecuredWebServiceImpl;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
/**
 *
 * @author dunnek
 */
public class NhincAdminDistOrchImpl {
   private Log log = null;

    public NhincAdminDistOrchImpl()
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
    protected NhinAdminDistProxy getNhinProxy()
    {
        return new NhinAdminDistObjectFactory().getNhinAdminDistProxy();
    }

}
