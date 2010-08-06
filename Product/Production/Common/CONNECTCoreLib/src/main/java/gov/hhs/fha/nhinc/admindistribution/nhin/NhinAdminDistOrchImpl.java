/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.admindistribution.nhin;
import gov.hhs.fha.nhinc.admindistribution.AdminDistributionAuditLogger;
import gov.hhs.fha.nhinc.admindistribution.AdminDistributionPolicyChecker;
import gov.hhs.fha.nhinc.admindistribution.adapter.proxy.AdapterAdminDistObjectFactory;
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
import gov.hhs.fha.nhinc.admindistribution.nhinc.proxy.NhincAdminDistProxy;
import gov.hhs.fha.nhinc.admindistribution.nhinc.proxy.NhincAdminDistObjectFactory;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
/**
 *
 * @author dunnek
 */
public class NhinAdminDistOrchImpl {
    private Log log = null;

    public NhinAdminDistOrchImpl()
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
        AcknowledgementType ack = getLogger().auditNhinAdminDist(body, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION);
        if (ack != null)
        {
            log.debug("ack: " + ack.getMessage());
        }


        if(checkPolicy(body, assertion))
        {
            sendToAgency(body, assertion);
        }

        log.info("End sendAlert");
    }
    protected void sendToAgency(EDXLDistribution body, AssertionType assertion)
    {
        log.debug("begin send to agency");
        getAdminFactory().getAdapterAdminDistProxy().sendAlertMessage(body, assertion);

    }
    protected AdapterAdminDistObjectFactory getAdminFactory()
    {
        return new AdapterAdminDistObjectFactory();
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
            result =  new AdminDistributionPolicyChecker().checkIncomingPolicy(body, assertion);
        }
        else
        {
            log.warn("EDXLDistribution was null");
        }

        log.debug("End Check Policy");
        return result;
    }
}
