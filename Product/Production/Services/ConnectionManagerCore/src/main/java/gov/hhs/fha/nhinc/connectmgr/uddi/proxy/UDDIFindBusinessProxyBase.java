/*
 * Copyright (c) 2009-2015, United States Government, as represented by the Secretary of Health and Human Services.
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.uddi.api_v3.BusinessDetail;
import org.uddi.api_v3.BusinessList;
import org.uddi.api_v3.GetBusinessDetail;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClient;
import gov.hhs.fha.nhinc.messaging.client.UDDIBaseClient;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;
import gov.hhs.fha.nhinc.nhin_uddi_api_v3.UDDIInquiryPortType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;

/**
 *
 * @author richard.ettema
 */
public abstract class UDDIFindBusinessProxyBase implements UDDIFindBusinessProxy {

    private static final Logger LOG = LoggerFactory.getLogger(UDDIFindBusinessProxyBase.class);

    // URL for the UDDI Server.
    protected String uddiInquiryUrl = "";
    protected static String GATEWAY_PROPFILE_NAME = "gateway";
    protected static String UDDI_INQUIRY_ENDPOINT_URL = "UDDIInquiryEndpointURL";


    /* (non-Javadoc)
     * @see gov.hhs.fha.nhinc.connectmgr.uddi.proxy.UDDIFindBusinessProxy#findBusinessesFromUDDI()
     */
    @Override
    public abstract BusinessList findBusinessesFromUDDI() throws UDDIFindBusinessException;

    /* (non-Javadoc)
     * @see gov.hhs.fha.nhinc.connectmgr.uddi.proxy.UDDIFindBusinessProxy#getBusinessDetail(org.uddi.api_v3.GetBusinessDetail)
     */
    @Override
    public BusinessDetail getBusinessDetail(GetBusinessDetail searchParams) throws UDDIFindBusinessException {

        BusinessDetail businessDetail = null;
        try {
            loadProperties();
            ServicePortDescriptor<UDDIInquiryPortType> portDescriptor = new UDDIFindBusinessProxyServicePortDescriptor();
            CONNECTClient<UDDIInquiryPortType> client = getCONNECTClientUnsecured(portDescriptor, uddiInquiryUrl, null);
            businessDetail = (BusinessDetail) client.invokePort(UDDIInquiryPortType.class, "getBusinessDetail",
                searchParams);

        } catch (Exception e) {
            String sErrorMessage = "Failed to call 'getBusinessDetail' web service on the NHIN UDDI server.  Error: "
                + e.getMessage();
            LOG.error(sErrorMessage, e);
            throw new UDDIFindBusinessException(sErrorMessage, e);
        }

        return businessDetail;
    }

    /**
     * This method loads information from the gateway.properties file that are pertinent to this class.
     */
    protected void loadProperties() throws UDDIFindBusinessException {

        try {
            String sValue = PropertyAccessor.getInstance().getProperty(GATEWAY_PROPFILE_NAME, UDDI_INQUIRY_ENDPOINT_URL);
            if ((sValue != null) && (sValue.length() > 0)) {
                uddiInquiryUrl = sValue;
            }
        } catch (PropertyAccessException e) {
            String sErrorMessage = "Failed to retrieve properties from " + GATEWAY_PROPFILE_NAME
                + ".properties file.  Error: " + e.getMessage();
            LOG.error(sErrorMessage, e);
            throw new UDDIFindBusinessException(sErrorMessage, e);
        }

    }

    protected CONNECTClient<UDDIInquiryPortType> getCONNECTClientUnsecured(
        ServicePortDescriptor<UDDIInquiryPortType> portDescriptor, String url, AssertionType assertion) {

        return new UDDIBaseClient<UDDIInquiryPortType>(portDescriptor, url);
    }

    protected int getMaxResults(){
        int maxResults; //default value
        try {
            String resultEntry =
                PropertyAccessor.getInstance().getProperty(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.MAX_UDDI_RESULTS_PROPERTY);
            if(NullChecker.isNotNullish(resultEntry)){
                maxResults = Integer.parseInt(resultEntry);
            }else {
                maxResults = -1;
            }
        } catch (PropertyAccessException ex) {
            LOG.error("Unable to access property for " + NhincConstants.MAX_UDDI_RESULTS_PROPERTY, ex);
            maxResults = -1;
        } catch (NumberFormatException ex) {
            LOG.error(NhincConstants.MAX_UDDI_RESULTS_PROPERTY + " entry in wrong format.", ex);
            maxResults = -1;
        }
        return maxResults;
    }
}
