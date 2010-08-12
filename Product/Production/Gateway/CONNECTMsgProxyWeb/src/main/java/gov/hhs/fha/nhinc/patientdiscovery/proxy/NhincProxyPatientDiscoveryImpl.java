/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.patientdiscovery.proxy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import gov.hhs.fha.nhinc.patientdiscovery.passthru.NhincPatientDiscoveryOrchImpl;

import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.ProxyPRPAIN201305UVProxyRequestType;
import org.hl7.v3.ProxyPRPAIN201305UVProxySecuredRequestType;

/**
 *
 * @author jhoppesc
 */
public class NhincProxyPatientDiscoveryImpl {

    private Log log = null;

    public NhincProxyPatientDiscoveryImpl() {
        log = createLogger();
    }

    protected Log createLogger() {
        return LogFactory.getLog(getClass());
    }

    protected NhincPatientDiscoveryOrchImpl getNhincPatientDiscoveryProcessor() {
        return new NhincPatientDiscoveryOrchImpl();
    }

    public PRPAIN201306UV02 proxyPRPAIN201305UV(ProxyPRPAIN201305UVProxyRequestType request) {
        log.info("Entering NhincProxyPatientDiscoveryImpl.proxyPRPAIN201305UV");
        PRPAIN201306UV02 response = null;

        if(request == null)
        {
            log.warn("request was null.");
        }
        else
        {
            NhincPatientDiscoveryOrchImpl processor = getNhincPatientDiscoveryProcessor();
            if(processor != null)
            {
                ProxyPRPAIN201305UVProxySecuredRequestType secureRequest = new ProxyPRPAIN201305UVProxySecuredRequestType();
                secureRequest.setPRPAIN201305UV02(request.getPRPAIN201305UV02());
                secureRequest.setNhinTargetSystem(request.getNhinTargetSystem());

                response = processor.proxyPRPAIN201305UV(secureRequest, request.getAssertion());
            }
            else
            {
                log.warn("NhincPatientDiscoveryProcessor was null.");
            }
        }
        log.info("Exiting NhincProxyPatientDiscoveryImpl.proxyPRPAIN201305UV");
        return response;
    }

}
