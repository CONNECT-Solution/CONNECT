/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2011(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
package gov.hhs.fha.nhinc.connectmgr.uddi.proxy;

import gov.hhs.fha.nhinc.nhin_uddi_api_v3.UDDIInquiryPortType;
import gov.hhs.fha.nhinc.nhin_uddi_api_v3.UDDIService;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import javax.xml.ws.BindingProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.uddi.api_v3.BusinessList;

/**
 *
 * @author richard.ettema
 */
public abstract class UDDIFindBusinessProxy {

    private static Log log = LogFactory.getLog(UDDIFindBusinessProxy.class);

    // URL for the UDDI Server.
    protected String m_sUDDIInquiryEndpointURL = "";
    protected static String GATEWAY_PROPFILE_NAME = "gateway";
    protected static String UDDI_INQUIRY_ENDPOINT_URL = "UDDIInquiryEndpointURL";

    /**
     * Override in implementation class
     * @return list of businesses from UDDI
     * @throws UDDIFindBusinessException
     */
    public abstract BusinessList findBusinessesFromUDDI() throws UDDIFindBusinessException;

    /**
     * This method loads information from the gateway.properties file that are
     * pertinent to this class.
     */
    protected void loadProperties() throws UDDIFindBusinessException {

        try {
            String sValue = PropertyAccessor.getProperty(GATEWAY_PROPFILE_NAME, UDDI_INQUIRY_ENDPOINT_URL);
            if ((sValue != null) && (sValue.length() > 0)) {
                m_sUDDIInquiryEndpointURL = sValue;
            }
        } catch (PropertyAccessException e) {
            String sErrorMessage = "Failed to retrieve properties from " + GATEWAY_PROPFILE_NAME + ".properties file.  Error: " + e.getMessage();
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
            UDDIService oService = new UDDIService();
            oPort = oService.getUDDIInquiryPort();

            // Need to load in the correct UDDI endpoint URL address.
            //--------------------------------------------------------
            ((BindingProvider) oPort).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, m_sUDDIInquiryEndpointURL);
        } catch (Exception e) {
            String sErrorMessage = "Failed to retrieve the UDDI Inquiry Web Service port.  Error: " + e.getMessage();
            log.error(sErrorMessage, e);
            throw new UDDIFindBusinessException(sErrorMessage, e);
        }

        return oPort;
    }

}
