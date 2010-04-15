/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.passthru.patientdiscovery.async.request.proxy;

import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.ProxyPRPAIN201305UVProxyRequestType;

/**
 *
 * @author jhoppesc
 */
public class PassthruPatientDiscoveryAsyncReqNoOpImpl implements PassthruPatientDiscoveryAsyncReqProxy{

    public MCCIIN000002UV01 proxyProcessPatientDiscoveryAsyncReq(ProxyPRPAIN201305UVProxyRequestType proxyProcessPatientDiscoveryAsyncReqRequest) {
        return new MCCIIN000002UV01();
    }

}
