/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.patientdiscovery.proxy.async.request;

import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import javax.xml.ws.WebServiceContext;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.ProxyPRPAIN201305UVProxyRequestType;
import org.hl7.v3.ProxyPRPAIN201305UVProxySecuredRequestType;

/**
 *
 * @author jhoppesc
 */
public class NhincProxyPatientDiscoverySecuredAsyncReqImpl {
    public MCCIIN000002UV01 proxyProcessPatientDiscoveryAsyncReq(ProxyPRPAIN201305UVProxySecuredRequestType request, WebServiceContext context) {
        ProxyPRPAIN201305UVProxyRequestType unsecureRequest = new ProxyPRPAIN201305UVProxyRequestType();
        unsecureRequest.setNhinTargetSystem(request.getNhinTargetSystem());
        unsecureRequest.setPRPAIN201305UV02(request.getPRPAIN201305UV02());
        unsecureRequest.setAssertion(SamlTokenExtractor.GetAssertion(context));

        MCCIIN000002UV01 ack = proxyProcessPatientDiscoveryAsyncReq(unsecureRequest);

        return ack;
    }

    public MCCIIN000002UV01 proxyProcessPatientDiscoveryAsyncReq(ProxyPRPAIN201305UVProxyRequestType request) {
        MCCIIN000002UV01 ack = new MCCIIN000002UV01();

        return ack;
    }

}
