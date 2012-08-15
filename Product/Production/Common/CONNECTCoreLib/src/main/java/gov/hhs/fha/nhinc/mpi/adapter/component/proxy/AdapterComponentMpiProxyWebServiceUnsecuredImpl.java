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
package gov.hhs.fha.nhinc.mpi.adapter.component.proxy;

import gov.hhs.fha.nhinc.adaptercomponentmpi.AdapterComponentMpiPortType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.ADAPTER_API_LEVEL;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import javax.xml.ws.Service;
import javax.xml.namespace.QName;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;

/**
 * This is the proxy class for the unsecued AdapterComponentProxy interface.
 * 
 * @author Les Westberg
 */
public class AdapterComponentMpiProxyWebServiceUnsecuredImpl implements AdapterComponentMpiProxy {

    private static Service cachedService = null;
    private static final String NAMESPACE_URI = "urn:gov:hhs:fha:nhinc:adaptercomponentmpi";
    private static final String SERVICE_LOCAL_PART = "AdapterComponentMpiService";
    private static final String PORT_LOCAL_PART = "AdapterComponentMpiPort";
    private static final String WSDL_FILE = "AdapterComponentMpi.wsdl";
    private static final String WS_ADDRESSING_ACTION = "urn:gov:hhs:fha:nhinc:adaptercomponentmpi:FindCandidatesRequest";
    private Log log = null;
    private WebServiceProxyHelper oProxyHelper = new WebServiceProxyHelper();

    /**
     * Default constructor.
     */
    public AdapterComponentMpiProxyWebServiceUnsecuredImpl() {
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
     * Find the matching candidates from the MPI.
     * 
     * @param request The information to use for matching.
     * @param assertion The assertion data.
     * @return The matches that are found.
     */
    public PRPAIN201306UV02 findCandidates(PRPAIN201305UV02 request, AssertionType assertion) {
        String url = null;
        PRPAIN201306UV02 response = new PRPAIN201306UV02();
        String sServiceName = NhincConstants.ADAPTER_COMPONENT_MPI_SERVICE_NAME;

        try {
            if (request != null) {
                log.debug("Before target system URL look up.");
                url = oProxyHelper.getAdapterEndPointFromConnectionManager(sServiceName);
                log.debug("After target system URL look up. URL for service: " + sServiceName + " is: " + url);

                if (NullChecker.isNotNullish(url)) {
                    RespondingGatewayPRPAIN201305UV02RequestType wsRequest = new RespondingGatewayPRPAIN201305UV02RequestType();
                    wsRequest.setPRPAIN201305UV02(request);
                    wsRequest.setAssertion(assertion);
                    AdapterComponentMpiPortType port = getPort(url, NhincConstants.ADAPTER_MPI_ACTION,
                            WS_ADDRESSING_ACTION, assertion);
                    response = (PRPAIN201306UV02) oProxyHelper.invokePort(port, AdapterComponentMpiPortType.class,
                            "findCandidates", wsRequest);
                } else {
                    log.error("Failed to call the web service (" + sServiceName + ").  The URL is null.");
                }
            } else {
                log.error("Failed to call the web service (" + sServiceName + ").  The input parameter is null.");
            }
        } catch (Exception e) {
            log.error("Failed to call the web service (" + sServiceName + ").  An unexpected exception occurred.  "
                    + "Exception: " + e.getMessage(), e);
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
    protected AdapterComponentMpiPortType getPort(String url, String serviceAction, String wsAddressingAction,
            AssertionType assertion) {
        AdapterComponentMpiPortType port = null;
        Service service = getService();
        if (service != null) {
            log.debug("Obtained service - creating port.");

            port = service.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART), AdapterComponentMpiPortType.class);
//            oProxyHelper
//                    .initializeUnsecurePort((javax.xml.ws.BindingProvider) port, url, wsAddressingAction, assertion);
        } else {
            log.error("Unable to obtain serivce - no port created.");
        }
        return port;
    }
}
