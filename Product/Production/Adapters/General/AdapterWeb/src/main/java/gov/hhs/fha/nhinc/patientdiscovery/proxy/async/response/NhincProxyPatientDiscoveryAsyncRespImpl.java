/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.patientdiscovery.proxy.async.response;

import gov.hhs.fha.nhinc.passthru.patientdiscovery.async.response.proxy.PassthruPatientDiscoveryAsyncRespProxy;
import gov.hhs.fha.nhinc.passthru.patientdiscovery.async.response.proxy.PassthruPatientDiscoveryAsyncRespProxyObjectFactory;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.ProxyPRPAIN201306UVProxyRequestType;

/**
 *
 * @author JHOPPESC
 */
public class NhincProxyPatientDiscoveryAsyncRespImpl {
    public MCCIIN000002UV01 proxyProcessPatientDiscoveryAsyncResp(ProxyPRPAIN201306UVProxyRequestType request) {
        MCCIIN000002UV01 ack = new MCCIIN000002UV01();

        PassthruPatientDiscoveryAsyncRespProxyObjectFactory passthruPatDiscAsyncRespFactory = new PassthruPatientDiscoveryAsyncRespProxyObjectFactory();

        PassthruPatientDiscoveryAsyncRespProxy proxy = passthruPatDiscAsyncRespFactory.getPassthruPatientDiscoveryAsyncRespProxy();

        ack = proxy.proxyProcessPatientDiscoveryAsyncResp(request);

        return ack;
    }

}
