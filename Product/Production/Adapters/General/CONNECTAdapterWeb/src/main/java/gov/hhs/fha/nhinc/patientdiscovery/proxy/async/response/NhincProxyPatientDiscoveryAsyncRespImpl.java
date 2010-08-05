/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.patientdiscovery.proxy.async.response;

import gov.hhs.fha.nhinc.async.AsyncMessageIdExtractor;
import gov.hhs.fha.nhinc.passthru.patientdiscovery.async.response.proxy.PassthruPatientDiscoveryAsyncRespProxy;
import gov.hhs.fha.nhinc.passthru.patientdiscovery.async.response.proxy.PassthruPatientDiscoveryAsyncRespProxyObjectFactory;
import javax.xml.ws.WebServiceContext;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.ProxyPRPAIN201306UVProxyRequestType;

/**
 *
 * @author JHOPPESC
 */
public class NhincProxyPatientDiscoveryAsyncRespImpl {
    public MCCIIN000002UV01 proxyProcessPatientDiscoveryAsyncResp(ProxyPRPAIN201306UVProxyRequestType request, WebServiceContext context) {
        MCCIIN000002UV01 ack = new MCCIIN000002UV01();

        // Extract the message id value from the WS-Addressing Header and place it in the Assertion Class
        if (request != null &&
                request.getAssertion() != null) {
            AsyncMessageIdExtractor msgIdExtractor = new AsyncMessageIdExtractor();
            request.getAssertion().setMessageId(msgIdExtractor.GetAsyncRelatesTo(context));
        }

        PassthruPatientDiscoveryAsyncRespProxyObjectFactory passthruPatDiscAsyncRespFactory = new PassthruPatientDiscoveryAsyncRespProxyObjectFactory();

        PassthruPatientDiscoveryAsyncRespProxy proxy = passthruPatDiscAsyncRespFactory.getPassthruPatientDiscoveryAsyncRespProxy();

        ack = proxy.proxyProcessPatientDiscoveryAsyncResp(request);

        return ack;
    }

}
