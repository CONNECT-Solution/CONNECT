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
package gov.hhs.fha.nhinc.docsubmission.adapter.component.deferred.response.proxy;

import gov.hhs.fha.nhinc.adaptercomponentxdrresponse.AdapterComponentXDRResponsePortType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.AdapterRegistryResponseType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

/**
 * 
 * @author dunnek, Les Westberg
 */
public class AdapterComponentDocSubmissionResponseProxyWebServiceUnsecuredImpl implements
        AdapterComponentDocSubmissionResponseProxy {
    private static Service cachedService = null;
    private static final String NAMESPACE_URI = "urn:gov:hhs:fha:nhinc:adaptercomponentxdrresponse";
    private static final String SERVICE_LOCAL_PART = "AdapterComponentXDRResponse_Service";
    private static final String PORT_LOCAL_PART = "AdapterComponentXDRResponse_Port";
    private static final String WSDL_FILE = "AdapterComponentXDRResponse.wsdl";
    private static final String WS_ADDRESSING_ACTION = NAMESPACE_URI + ":" + "XDRResponseInputMessage";
    private Log log = null;
    private WebServiceProxyHelper oProxyHelper = new WebServiceProxyHelper();

    /**
     * Default constructor.
     */
    public AdapterComponentDocSubmissionResponseProxyWebServiceUnsecuredImpl() {
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
     * Receive document deferred document submission response.
     * 
     * @param body The doc submission response message.
     * @param assertion The assertion information.
     * @return The ACK
     */
    public XDRAcknowledgementType provideAndRegisterDocumentSetBResponse(RegistryResponseType body,
            AssertionType assertion) {
        String endpointUrl = null;
        XDRAcknowledgementType response = new XDRAcknowledgementType();
        String sServiceName = NhincConstants.ADAPTER_COMPONENT_XDR_RESPONSE_SERVICE_NAME;

        try {
            if (body != null) {
                log.debug("Before target system URL look up.");
                endpointUrl = oProxyHelper.getUrlLocalHomeCommunity(sServiceName);
                log.debug("After target system URL look up. URL for service: " + sServiceName + " is: " + endpointUrl);

                if (NullChecker.isNotNullish(endpointUrl)) {
                    AdapterRegistryResponseType adaptResponse = new AdapterRegistryResponseType();
                    adaptResponse.setAssertion(assertion);
                    adaptResponse.setRegistryResponse(body);

                    AdapterComponentXDRResponsePortType port = getPort(endpointUrl,
                            NhincConstants.ADAPTER_XDRRESPONSE_ACTION, WS_ADDRESSING_ACTION, assertion);
                    response = (XDRAcknowledgementType) oProxyHelper.invokePort(port,
                            AdapterComponentXDRResponsePortType.class, "provideAndRegisterDocumentSetBResponse",
                            adaptResponse);
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
    protected AdapterComponentXDRResponsePortType getPort(String url, String serviceAction, String wsAddressingAction,
            AssertionType assertion) {
        AdapterComponentXDRResponsePortType port = null;
        Service service = getService();
        if (service != null) {
            log.debug("Obtained service - creating port.");

            port = service
                    .getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART), AdapterComponentXDRResponsePortType.class);
            oProxyHelper
                    .initializeUnsecurePort((javax.xml.ws.BindingProvider) port, url, wsAddressingAction, assertion);
        } else {
            log.error("Unable to obtain serivce - no port created.");
        }
        return port;
    }
}
