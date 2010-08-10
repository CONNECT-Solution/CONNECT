/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.patientdiscovery.proxy.async.response;

import gov.hhs.fha.nhinc.async.AsyncMessageIdExtractor;
import gov.hhs.fha.nhinc.passthru.patientdiscovery.async.response.proxy.PassthruPatientDiscoveryAsyncRespProxy;
import gov.hhs.fha.nhinc.passthru.patientdiscovery.async.response.proxy.PassthruPatientDiscoveryAsyncRespProxyObjectFactory;
import gov.hhs.fha.nhinc.service.WebServiceHelper;
import java.util.List;
import javax.xml.ws.WebServiceContext;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.ProxyPRPAIN201306UVProxyRequestType;

/**
 *
 * @author JHOPPESC
 */
public class NhincProxyPatientDiscoveryAsyncRespImpl {
    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(NhincProxyPatientDiscoveryAsyncRespImpl.class);

    public MCCIIN000002UV01 proxyProcessPatientDiscoveryAsyncResp(ProxyPRPAIN201306UVProxyRequestType request, WebServiceContext context) {
        WebServiceHelper oHelper = new WebServiceHelper();
        MCCIIN000002UV01 ack = new MCCIIN000002UV01();
        PassthruPatientDiscoveryAsyncRespProxyObjectFactory passthruPatDiscAsyncRespFactory = new PassthruPatientDiscoveryAsyncRespProxyObjectFactory();
        PassthruPatientDiscoveryAsyncRespProxy proxy = passthruPatDiscAsyncRespFactory.getPassthruPatientDiscoveryAsyncRespProxy();

        try
        {
            if (request != null && proxy != null)
            {
                ack = (MCCIIN000002UV01) oHelper.invokeDeferredResponseWebService(proxy, proxy.getClass(), "proxyProcessPatientDiscoveryAsyncResp", request.getAssertion(), request, context);
            } else
            {
                log.error("Failed to call the web orchestration (" + proxy.getClass() + ".proxyProcessPatientDiscoveryAsyncResp).  The input parameter is null.");
            }
        } catch (Exception e)
        {
            log.error("Failed to call the web orchestration (" + proxy.getClass() + ".proxyProcessPatientDiscoveryAsyncResp).  An unexpected exception occurred.  " +
                    "Exception: " + e.getMessage(), e);
        }
        return ack;
    }

}
