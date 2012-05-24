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
package gov.hhs.fha.nhinc.connectmgr.uddi.proxy;

import gov.hhs.fha.nhinc.nhin_uddi_api_v3.UDDIInquiryPortType;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.uddi.api_v3.BusinessDetail;
import org.uddi.api_v3.BusinessList;
import org.uddi.api_v3.GetBusinessDetail;

/**
 * 
 * @author richard.ettema
 */
public abstract class UDDIFindBusinessProxyBase {

    private static Log log = LogFactory.getLog(UDDIFindBusinessProxyBase.class);

    // URL for the UDDI Server.
    protected String m_sUDDIInquiryEndpointURL = "";
    protected static String GATEWAY_PROPFILE_NAME = "gateway";
    protected static String UDDI_INQUIRY_ENDPOINT_URL = "UDDIInquiryEndpointURL";

    private static Service cachedService = null;
    private static WebServiceProxyHelper oProxyHelper = null;
    private static final String NAMESPACE_URI = "urn:gov:hhs:fha:nhinc:nhin_uddi_api_v3";
    private static final String SERVICE_LOCAL_PART = "UDDI_Service";
    private static final String PORT_LOCAL_PART = "UDDI_Inquiry_Port";
    private static final String WSDL_FILE = "NhinUddiAPIV3.wsdl";

    /**
     * Override in implementation class
     * 
     * @return list of businesses from UDDI
     * @throws UDDIFindBusinessException
     */
    public abstract BusinessList findBusinessesFromUDDI() throws UDDIFindBusinessException;

    public abstract BusinessDetail getBusinessDetail(GetBusinessDetail searchParams) throws UDDIFindBusinessException;

    /**
     * This method loads information from the gateway.properties file that are pertinent to this class.
     */
    protected void loadProperties() throws UDDIFindBusinessException {

        try {
            String sValue = PropertyAccessor.getInstance().getProperty(GATEWAY_PROPFILE_NAME, UDDI_INQUIRY_ENDPOINT_URL);
            if ((sValue != null) && (sValue.length() > 0)) {
                m_sUDDIInquiryEndpointURL = sValue;
            }
        } catch (PropertyAccessException e) {
            String sErrorMessage = "Failed to retrieve properties from " + GATEWAY_PROPFILE_NAME
                    + ".properties file.  Error: " + e.getMessage();
            log.error(sErrorMessage, e);
            throw new UDDIFindBusinessException(sErrorMessage, e);
        }

    }

    /**
     * This method retrieves the port for the UDDI server with the correct endpoint.
     * 
     * @return port type from UDDI inquiry service
     */
    protected UDDIInquiryPortType getUDDIInquiryWebService() throws UDDIFindBusinessException {
        UDDIInquiryPortType oPort = null;

        try {
            Service oService = getService(WSDL_FILE, NAMESPACE_URI, SERVICE_LOCAL_PART);

            if (oService != null) {
                log.debug("getUDDIInquiryWebService() Obtained UDDI service - creating port.");
                oPort = oService.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART), UDDIInquiryPortType.class);

                // Load in the correct UDDI endpoint URL address.
                getWebServiceProxyHelper().initializeUnsecurePort((BindingProvider) oPort, m_sUDDIInquiryEndpointURL,
                        null, null);
            } else {
                log.error("Unable to obtain serivce - no port created.");
            }
        } catch (Exception e) {
            String sErrorMessage = "Failed to retrieve the UDDI Inquiry Web Service port.  Error: " + e.getMessage();
            log.error(sErrorMessage, e);
            throw new UDDIFindBusinessException(sErrorMessage, e);
        }
        return oPort;
    }

    protected WebServiceProxyHelper getWebServiceProxyHelper() {
        if (oProxyHelper == null) {
            oProxyHelper = new WebServiceProxyHelper();
        }
        return oProxyHelper;
    }

    protected Service getService(String wsdl, String uri, String service) {
        if (cachedService == null) {
            try {
                cachedService = getWebServiceProxyHelper().createService(wsdl, uri, service);
            } catch (Throwable t) {
                log.error("Error creating service: " + t.getMessage(), t);
            }
        }
        return cachedService;
    }
}
