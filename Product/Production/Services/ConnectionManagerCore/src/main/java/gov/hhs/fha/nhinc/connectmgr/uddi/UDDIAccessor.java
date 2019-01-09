/*
 * Copyright (c) 2009-2019, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.connectmgr.uddi;

import gov.hhs.fha.nhinc.connectmgr.uddi.proxy.UDDIFindBusinessException;
import gov.hhs.fha.nhinc.connectmgr.uddi.proxy.UDDIFindBusinessProxy;
import gov.hhs.fha.nhinc.connectmgr.uddi.proxy.UDDIFindBusinessProxyObjectFactory;
import gov.hhs.fha.nhinc.exchange.ExchangeType;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.uddi.api_v3.BusinessDetail;
import org.uddi.api_v3.BusinessInfo;
import org.uddi.api_v3.BusinessInfos;
import org.uddi.api_v3.BusinessList;
import org.uddi.api_v3.GetBusinessDetail;

/**
 * This class is used to retrieve the connection information from the UDDI server.
 *
 * @author Les Westberg
 */
public class UDDIAccessor {

    private static final Logger LOG = LoggerFactory.getLogger(UDDIAccessor.class);

    private static final String GATEWAY_PROPFILE_NAME = "gateway";
    private static final String UDDI_BUSINESSES_TO_IGNORE = "UDDIBusinessesToIgnore";

    // These are business entities that the UDDI will send us that we should ignore.
    // These are configured in the gateway.properties file and will be used to eliminate
    // some of the entries we get back from the UDDI server.
    // ------------------------------------------------------------------------------------
    private final HashSet<String> mBusinessToIgnore = new HashSet<>();
    private boolean mPropsLoaded = false; // True if the props have been loaded.

    /**
     * This method loads information from the gateway.properties file that are pertinent to this class.
     */
    private void loadProperties() throws UDDIAccessorException {
        if (!mPropsLoaded) {
            try {
                loadBusinessToIgnore(PropertyAccessor.getInstance().getProperty(GATEWAY_PROPFILE_NAME,
                    UDDI_BUSINESSES_TO_IGNORE));
                mPropsLoaded = true;

            } catch (PropertyAccessException e) {
                String sErrorMessage = "Failed to retrieve properties from " + GATEWAY_PROPFILE_NAME
                    + ".properties file.  Error: " + e.getMessage();
                LOG.error(sErrorMessage, e);
                throw new UDDIAccessorException(sErrorMessage, e);
            }
        }
    }

    /**
     * This method extracts the business key from the business info object.
     *
     * @param oBusInfo The business information object containing the data.
     * @return The key that was extracted.
     */
    private static String extractBusinessKey(BusinessInfo oBusInfo) {
        String sKey = "";

        if (oBusInfo != null && oBusInfo.getBusinessKey() != null && oBusInfo.getBusinessKey().length() > 0) {
            sKey = oBusInfo.getBusinessKey();
        }

        return sKey;

    }

    private void removeIgnoredBusinesses(BusinessList businessList) {
        ArrayList<BusinessInfo> ignoredKeyList = new ArrayList<>();
        if (businessList != null && businessList.getBusinessInfos() != null
            && CollectionUtils.isNotEmpty(businessList.getBusinessInfos().getBusinessInfo())) {
            for (BusinessInfo oBusInfo : businessList.getBusinessInfos().getBusinessInfo()) {
                String sKey = extractBusinessKey(oBusInfo);

                if (mBusinessToIgnore.contains(sKey)) {
                    ignoredKeyList.add(oBusInfo);
                }
            }
            businessList.getBusinessInfos().getBusinessInfo().removeAll(ignoredKeyList);
        }
    }

    /**
     * This method is used to retrieve the data from the UDDI server. The data is returned in the form of
     * CMBusinessEntities.
     *
     * @param exchange
     * @return The Business Entities that were retrieved from the UDDI server.
     * @throws gov.hhs.fha.nhinc.connectmgr.uddi.UDDIAccessorException
     *
     */
    public BusinessDetail retrieveFromUDDIServer(ExchangeType exchange) throws UDDIAccessorException {
        loadProperties();

        BusinessList businessList = retrieveBusinessesListFromUDDI(exchange);
        return retrieveBusinessDetail(businessList, exchange);
    }

    /**
     * This method retrieves the business entities from the UDDI server. It does not retrieve the services or bindings.
     * They are retrieved on other calls. This only retrieves the business information.
     *
     * @return the BusinessEntities retrieved from the UDDI server.
     * @throws UDDIAccessorException
     */
    private BusinessList retrieveBusinessesListFromUDDI(ExchangeType exchange) throws UDDIAccessorException {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Retrieving business entities from UDDI using find_business web service call.");
        }

        BusinessList businessList;
        try {
            UDDIFindBusinessProxyObjectFactory uddiFactory = new UDDIFindBusinessProxyObjectFactory();
            UDDIFindBusinessProxy uddiProxy = uddiFactory.getUDDIBusinessInfoProxy();
            businessList = uddiProxy.findBusinessesFromUDDI(exchange);

            removeIgnoredBusinesses(businessList);
        } catch (UDDIFindBusinessException e) {
            String sErrorMessage = "Failed to call 'find_business' web service on the NHIN UDDI server.  Error: "
                + e.getMessage();
            LOG.error(sErrorMessage, e);
            throw new UDDIAccessorException(sErrorMessage, e);
        }

        return businessList;
    }

    private static BusinessDetail retrieveBusinessDetail(BusinessList businessList, ExchangeType exchange) throws
        UDDIAccessorException {

        if (businessList == null) {
            return null;
        }

        BusinessDetail businessDetail;
        BusinessInfos businessInfos = businessList.getBusinessInfos();
        try {
            GetBusinessDetail searchParams = createSearchParamsFromBusinessKeys(businessInfos);

            UDDIFindBusinessProxyObjectFactory uddiFactory = new UDDIFindBusinessProxyObjectFactory();
            UDDIFindBusinessProxy uddiProxy = uddiFactory.getUDDIBusinessInfoProxy();
            businessDetail = uddiProxy.getBusinessDetail(searchParams, exchange);
        } catch (UDDIFindBusinessException e) {
            String sErrorMessage = "Failed to call UDDI web service get_businessDetail method.  Error: "
                + e.getMessage();
            LOG.error(sErrorMessage, e);
            throw new UDDIAccessorException(sErrorMessage, e);
        }

        return businessDetail;
    }

    private static GetBusinessDetail createSearchParamsFromBusinessKeys(BusinessInfos businessInfos) {
        GetBusinessDetail searchParams = new GetBusinessDetail();
        for (BusinessInfo businessInfo : businessInfos.getBusinessInfo()) {
            if (businessInfo.getBusinessKey() != null && businessInfo.getBusinessKey().length() > 0) {
                searchParams.getBusinessKey().add(businessInfo.getBusinessKey());
            }
        }

        return searchParams;
    }

    private void loadBusinessToIgnore(String sValue) {
        if (sValue != null && sValue.length() > 0) {
            String[] saBusiness = sValue.split(";");
            if (saBusiness != null && saBusiness.length > 0) {
                mBusinessToIgnore.addAll(Arrays.asList(saBusiness));
            }
        }
    }
}
