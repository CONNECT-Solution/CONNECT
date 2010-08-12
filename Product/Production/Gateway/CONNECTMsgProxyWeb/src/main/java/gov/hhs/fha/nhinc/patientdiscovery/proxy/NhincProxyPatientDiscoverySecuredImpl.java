/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.patientdiscovery.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.ProxyPRPAIN201305UVProxySecuredRequestType;
import gov.hhs.fha.nhinc.patientdiscovery.passthru.NhincPatientDiscoveryOrchImpl;

/**
 *
 * @author jhoppesc
 */
public class NhincProxyPatientDiscoverySecuredImpl {

    private static Log log = LogFactory.getLog(NhincProxyPatientDiscoverySecuredImpl.class);

    protected NhincPatientDiscoveryOrchImpl getNhincPatientDiscoveryProcessor() {
        return new NhincPatientDiscoveryOrchImpl();
    }
    public PRPAIN201306UV02 proxyPRPAIN201305UV(ProxyPRPAIN201305UVProxySecuredRequestType request, AssertionType assertion) {
        log.debug("Entering NhincProxyPatientDiscoverySecuredImpl.proxyPRPAIN201305UV...");
        PRPAIN201306UV02 response = new PRPAIN201306UV02();

        NhincPatientDiscoveryOrchImpl processor = getNhincPatientDiscoveryProcessor();
        if (processor != null) {

            response = processor.proxyPRPAIN201305UV(request, assertion);
        } else {
            log.warn("NhincPatientDiscoveryProcessor was null.");
        }

        log.debug("Exiting NhincProxyPatientDiscoverySecuredImpl.proxyPRPAIN201305UV...");
        return response;
    }
}
