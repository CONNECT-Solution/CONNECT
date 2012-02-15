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
package gov.hhs.fha.nhinc.patientdiscovery.adapter.proxy;

import gov.hhs.fha.nhinc.adapterpatientdiscovery.AdapterPatientDiscoveryPortType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.ADAPTER_API_LEVEL;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

/**
 * 
 * @author dunnek, Les Westberg
 */
public class AdapterPatientDiscoveryProxyWebServiceUnsecuredImpl implements AdapterPatientDiscoveryProxy {
    private static Service cachedService = null;
    private static final String NAMESPACE_URI = "urn:gov:hhs:fha:nhinc:adapterpatientdiscovery";
    private static final String SERVICE_LOCAL_PART = "AdapterPatientDiscovery";
    private static final String PORT_LOCAL_PART = "AdapterPatientDiscoveryPortSoap";
    private static final String WSDL_FILE = "AdapterPatientDiscovery.wsdl";
    private static final String WS_ADDRESSING_ACTION = "urn:gov:hhs:fha:nhinc:adapterpatientdiscovery:RespondingGateway_PRPA_IN201305UV02RequestMessage";
    private Log log = null;
    private WebServiceProxyHelper oProxyHelper = new WebServiceProxyHelper();

    /**
     * Default constructor.
     */
    public AdapterPatientDiscoveryProxyWebServiceUnsecuredImpl() {
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

    /**
     * This calls the unsecured web service for this method.
     * 
     * @param body The message to be sent to the web service.
     * @param assertion The assertion information to go with the message.
     * @return The response from the web service.
     * @throws PatientDiscoveryException
     */
    public PRPAIN201306UV02 respondingGatewayPRPAIN201305UV02(org.hl7.v3.PRPAIN201305UV02 body, AssertionType assertion)
            throws PatientDiscoveryException {
        String url = null;
        PRPAIN201306UV02 response = new PRPAIN201306UV02();
        String sServiceName = NhincConstants.PATIENT_DISCOVERY_ADAPTER_SERVICE_NAME;

        try {
            if (body != null) {
                log.debug("Before target system URL look up.");
                url = oProxyHelper.getEndPointFromConnectionManagerByAdapterAPILevel(sServiceName, ADAPTER_API_LEVEL.LEVEL_a0);
                log.debug("After target system URL look up. URL for service: " + sServiceName + " is: " + url);

                if (NullChecker.isNotNullish(url)) {
                    RespondingGatewayPRPAIN201305UV02RequestType request = new RespondingGatewayPRPAIN201305UV02RequestType();
                    request.setAssertion(assertion);
                    request.setPRPAIN201305UV02(body);
                    request.setNhinTargetCommunities(null);

                    AdapterPatientDiscoveryPortType port = getPort(url, NhincConstants.ADAPTER_MPI_ACTION,
                            WS_ADDRESSING_ACTION, assertion);
                    response = (PRPAIN201306UV02) oProxyHelper.invokePort(port, AdapterPatientDiscoveryPortType.class,
                            "respondingGatewayPRPAIN201305UV02", request);
                } else {
                    log.error("Failed to call the web service (" + sServiceName + ").  The URL is null.");
                }
            } else {
                log.error("Failed to call the web service (" + sServiceName + ").  The input parameter is null.");
            }
        } catch (Exception e) {
            log.error("Failed to call the web service (" + sServiceName + ").  An unexpected exception occurred.  "
                    + "Exception: " + e.getMessage(), e);
            throw new PatientDiscoveryException(e.fillInStackTrace());
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
    protected AdapterPatientDiscoveryPortType getPort(String url, String serviceAction, String wsAddressingAction,
            AssertionType assertion) {
        AdapterPatientDiscoveryPortType port = null;
        Service service = getService();
        if (service != null) {
            log.debug("Obtained service - creating port.");

            port = service.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART), AdapterPatientDiscoveryPortType.class);
            oProxyHelper
                    .initializeUnsecurePort((javax.xml.ws.BindingProvider) port, url, wsAddressingAction, assertion);
        } else {
            log.error("Unable to obtain serivce - no port created.");
        }
        return port;
    }
}
