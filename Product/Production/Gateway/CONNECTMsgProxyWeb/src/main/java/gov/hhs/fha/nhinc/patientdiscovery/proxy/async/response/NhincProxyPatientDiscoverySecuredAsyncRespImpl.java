/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.patientdiscovery.proxy.async.response;

import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryAuditLogger;
import gov.hhs.fha.nhinc.patientdiscovery.nhin.deferred.response.proxy.NhinPatientDiscoveryDeferredRespProxy;
import gov.hhs.fha.nhinc.patientdiscovery.nhin.deferred.response.proxy.NhinPatientDiscoveryDeferredRespProxyObjectFactory;
import gov.hhs.fha.nhinc.service.WebServiceHelper;
import javax.xml.ws.WebServiceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.ProxyPRPAIN201306UVProxyRequestType;
import org.hl7.v3.ProxyPRPAIN201306UVProxySecuredRequestType;

/**
 *
 * @author JHOPPESC
 */
public class NhincProxyPatientDiscoverySecuredAsyncRespImpl
{
    private Log log = null;

    public NhincProxyPatientDiscoverySecuredAsyncRespImpl()
    {
        log = createLogger();
    }

    protected Log createLogger()
    {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }

    public MCCIIN000002UV01 proxyProcessPatientDiscoveryAsyncResp(ProxyPRPAIN201306UVProxySecuredRequestType request, WebServiceContext context)
    {
        ProxyPRPAIN201306UVProxyRequestType unsecureRequest = new ProxyPRPAIN201306UVProxyRequestType();
        unsecureRequest.setNhinTargetSystem(request.getNhinTargetSystem());
        unsecureRequest.setPRPAIN201306UV02(request.getPRPAIN201306UV02());

        WebServiceHelper oHelper = new WebServiceHelper();
        MCCIIN000002UV01 ack = null;

        try
        {
            if (request != null)
            {
                ack = (MCCIIN000002UV01) oHelper.invokeSecureDeferredResponseWebService(this, this.getClass(), "proxyProcessPatientDiscoveryAsyncRespOrch", unsecureRequest, context);
            } else
            {
                log.error("Failed to call the web orchestration (" + this.getClass() + ".proxyProcessPatientDiscoveryAsyncRespOrch).  The input parameter is null.");
            }
        } catch (Exception e)
        {
            log.error("Failed to call the web orchestration (" + this.getClass() + ".proxyProcessPatientDiscoveryAsyncRespOrch).  An unexpected exception occurred.  " +
                    "Exception: " + e.getMessage(), e);
        }

        return ack;
    }

    public MCCIIN000002UV01 proxyProcessPatientDiscoveryAsyncRespOrch(ProxyPRPAIN201306UVProxyRequestType request, AssertionType assertion)
    {
        MCCIIN000002UV01 resp = new MCCIIN000002UV01();

        // Audit the Patient Discovery Request Message sent on the Nhin Interface
        PatientDiscoveryAuditLogger auditLog = new PatientDiscoveryAuditLogger();
        AcknowledgementType ack = auditLog.auditNhin201306(request.getPRPAIN201306UV02(), assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION);

        NhinPatientDiscoveryDeferredRespProxyObjectFactory patientDiscoveryFactory = new NhinPatientDiscoveryDeferredRespProxyObjectFactory();
        NhinPatientDiscoveryDeferredRespProxy proxy = patientDiscoveryFactory.getNhinPatientDiscoveryAsyncRespProxy();

        resp = proxy.respondingGatewayPRPAIN201306UV02(request.getPRPAIN201306UV02(), assertion, request.getNhinTargetSystem());

        // Audit the Patient Discovery Response Message received on the Nhin Interface
        ack = auditLog.auditAck(resp, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);

        return resp;
    }
}
