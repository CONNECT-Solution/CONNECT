/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above
 *       copyright notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the documentation
 *       and/or other materials provided with the distribution.
 *     * Neither the name of the United States Government nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package gov.hhs.fha.nhinc.patientdiscovery.nhin.proxy;

import ihe.iti.xcpd._2009.RespondingGatewayPortType;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.perfrepo.PerformanceManager;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import javax.xml.ws.BindingProvider;

/**
 *
 * @author jhoppesc
 */
public class NhinPatientDiscoveryProxyWebServiceSecuredImpl implements NhinPatientDiscoveryProxy {

    private static Service cachedService = null;
    private static final String NAMESPACE_URI = "urn:ihe:iti:xcpd:2009";
    private static final String SERVICE_LOCAL_PART = "RespondingGateway_Service";
    private static final String PORT_LOCAL_PART = "RespondingGateway_Port_Soap";
    private static final String WSDL_FILE = "NhinPatientDiscovery.wsdl";
    private static final String WS_ADDRESSING_ACTION = "urn:hl7-org:v3:PRPA_IN201305UV02:CrossGatewayPatientDiscovery";
    private Log log = null;
    private final WebServiceProxyHelper oProxyHelper = new WebServiceProxyHelper();

    /**
     * Default constructor.
     */
    public NhinPatientDiscoveryProxyWebServiceSecuredImpl() {
        log = createLogger();
    }

    /**
     * Creates the log object for logging.
     *
     * @return The log object.
     */
    protected Log createLogger() {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }

    @Override
    public PRPAIN201306UV02 respondingGatewayPRPAIN201305UV02(PRPAIN201305UV02 request, AssertionType assertion,
            NhinTargetSystemType target) throws Exception {
        PRPAIN201306UV02 response = new PRPAIN201306UV02();

        try {
            if (request != null && target != null) {

                log.debug("Before target system URL look up.");
                String url = target.getUrl();
                if (NullChecker.isNullish(url)) {
                    url = ConnectionManagerCache.getInstance().getDefaultEndpointURLByServiceName(
                        target.getHomeCommunity().getHomeCommunityId(), NhincConstants.PATIENT_DISCOVERY_SERVICE_NAME);
                    log.debug("After target system URL look up. URL for service: "
                            + NhincConstants.PATIENT_DISCOVERY_SERVICE_NAME + " is: " + url);
                }

                if (NullChecker.isNotNullish(url)) {
                    RespondingGatewayPortType port = getPort(url, NhincConstants.PATIENT_DISCOVERY_SERVICE_NAME,
                            WS_ADDRESSING_ACTION, assertion);
                    WebServiceProxyHelper wsHelper = new WebServiceProxyHelper();
                    wsHelper.addTargetCommunity(((BindingProvider)port), target);

                    // Log the start of the performance record
                    String targetCommunityId = "";

                    if ((target != null) && (target.getHomeCommunity() != null)) {
                        targetCommunityId = target.getHomeCommunity().getHomeCommunityId();
                    }
                    PerformanceManager.getPerformanceManagerInstance().logPerformanceStart(NhincConstants.PATIENT_DISCOVERY_SERVICE_NAME, NhincConstants.AUDIT_LOG_NHIN_INTERFACE,
                            NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, targetCommunityId);

                    response = (PRPAIN201306UV02) oProxyHelper.invokePort(port, RespondingGatewayPortType.class,
                            "respondingGatewayPRPAIN201305UV02", request);

                    // Log the end of the performance record
                    PerformanceManager.getPerformanceManagerInstance().logPerformanceStop(NhincConstants.PATIENT_DISCOVERY_SERVICE_NAME, NhincConstants.AUDIT_LOG_NHIN_INTERFACE,
                            NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, targetCommunityId);

                } else {
                    log.error("Failed to call the web service (" + NhincConstants.PATIENT_DISCOVERY_SERVICE_NAME
                            + ").  The URL is null.");
                }
            } else {
                log.error("Failed to call the web service (" + NhincConstants.PATIENT_DISCOVERY_SERVICE_NAME
                        + ").  The input parameters are null.");
            }
        } catch (Exception e) {
            log.error("Failed to call the web service (" + NhincConstants.PATIENT_DISCOVERY_SERVICE_NAME
                    + ").  An unexpected exception occurred.  " + "Exception: " + e.getMessage(), e);
            // response = new HL7PRPA201306Transforms().createPRPA201306ForErrors(request,
            // NhincConstants.PATIENT_DISCOVERY_ANSWER_NOT_AVAIL_ERR_CODE);
            throw e;
        }

        return response;
    }

    /**
     * Retrieve the service class for this web service.
     *
     * @return The service class for this web service.
     */
    protected Service getService() {
        if (cachedService == null) {
            try {
                cachedService = oProxyHelper.createService(WSDL_FILE, NAMESPACE_URI, SERVICE_LOCAL_PART);
            } catch (Throwable t) {
                log.error("Error creating service: " + t.getMessage(), t);
            }
        }
        return cachedService;
    }

    /**
     * This method retrieves and initializes the port.
     *
     * @param url The URL for the web service.
     * @param serviceAction The action for the web service.
     * @param wsAddressingAction The action assigned to the input parameter for the web service operation.
     * @param assertion The assertion information for the web service
     * @return The port object for the web service.
     */
    protected RespondingGatewayPortType getPort(String url, String serviceAction, String wsAddressingAction,
            AssertionType assertion) {
        RespondingGatewayPortType port = null;
        Service service = getService();
        if (service != null) {
            log.debug("Obtained service - creating port.");

            port = service.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART), RespondingGatewayPortType.class);
            oProxyHelper.initializeSecurePort((javax.xml.ws.BindingProvider) port, url, serviceAction,
                    wsAddressingAction, assertion);
            oProxyHelper.setPortTimeoutByService((javax.xml.ws.BindingProvider) port,
                    NhincConstants.PATIENT_DISCOVERY_SERVICE_NAME);
        } else {
            log.error("Unable to obtain serivce - no port created.");
        }
        return port;
    }
}
