/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.patientdiscovery.proxy.async.request;

import gov.hhs.fha.nhinc.passthru.patientdiscovery.async.request.proxy.PassthruPatientDiscoveryAsyncReqProxy;
import gov.hhs.fha.nhinc.passthru.patientdiscovery.async.request.proxy.PassthruPatientDiscoveryAsyncReqProxyObjectFactory;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.ProxyPRPAIN201305UVProxyRequestType;

/**
 *
 * @author jhoppesc
 */
public class NhincProxyPatientDiscoveryAsyncReqImpl {
    public MCCIIN000002UV01 proxyProcessPatientDiscoveryAsyncReq(ProxyPRPAIN201305UVProxyRequestType request) {
        MCCIIN000002UV01 ack = new MCCIIN000002UV01();

        PassthruPatientDiscoveryAsyncReqProxyObjectFactory passthruPatDiscAsyncReqFactory = new PassthruPatientDiscoveryAsyncReqProxyObjectFactory();

        PassthruPatientDiscoveryAsyncReqProxy proxy = passthruPatDiscAsyncReqFactory.getPassthruPatientDiscoveryAsyncReqProxy();

        ack = proxy.proxyProcessPatientDiscoveryAsyncReq(request);

        return ack;
    }

}
