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
package gov.hhs.fha.nhinc.connectmgr.uddi;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.connectmgr.data.CMBindingDescriptions;
import gov.hhs.fha.nhinc.connectmgr.data.CMBusinessEntities;
import gov.hhs.fha.nhinc.connectmgr.data.CMBusinessEntity;
import gov.hhs.fha.nhinc.connectmgr.data.CMBusinessNames;
import gov.hhs.fha.nhinc.connectmgr.data.CMBusinessDescriptions;
import gov.hhs.fha.nhinc.connectmgr.data.CMBusinessServices;
import gov.hhs.fha.nhinc.connectmgr.data.CMBusinessService;
import gov.hhs.fha.nhinc.connectmgr.data.CMBindingNames;
import gov.hhs.fha.nhinc.connectmgr.data.CMBindingTemplate;
import gov.hhs.fha.nhinc.connectmgr.data.CMBindingTemplates;
import gov.hhs.fha.nhinc.connectmgr.data.CMStates;
import gov.hhs.fha.nhinc.connectmgr.data.CMUDDIConnectionInfo;
import gov.hhs.fha.nhinc.connectmgr.data.CMUDDIConnectionInfoXML;

import gov.hhs.fha.nhinc.connectmgr.uddi.proxy.UDDIFindBusinessProxyObjectFactory;
import gov.hhs.fha.nhinc.connectmgr.uddi.proxy.UDDIFindBusinessProxyBase;

import gov.hhs.fha.nhinc.nhinadmindistribution.RespondingGatewayAdministrativeDistributionPortType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;

import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;

import java.util.Iterator;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;

import javax.xml.ws.Service;
import org.uddi.api_v3.BusinessList;
import org.uddi.api_v3.BusinessInfo;
import org.uddi.api_v3.BusinessService;
import org.uddi.api_v3.ServiceInfo;
import org.uddi.api_v3.Name;
import org.uddi.api_v3.GetBusinessDetail;
import org.uddi.api_v3.BusinessDetail;
import org.uddi.api_v3.GetServiceDetail;
import org.uddi.api_v3.ServiceDetail;
import org.uddi.api_v3.Description;
import org.uddi.api_v3.BusinessEntity;
import org.uddi.api_v3.KeyedReference;
import org.uddi.api_v3.BindingTemplate;
import org.uddi.v3_service.UDDIInquiryPortType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.uddi.api_v3.BusinessInfos;

/**
 * This class is used to retrieve the connection information from the UDDI server.
 * 
 * @author Les Westberg
 */
public class UDDIAccessor {

    private Log log = null;

    private static String GATEWAY_PROPFILE_NAME = "gateway";
    private static String UDDI_INQUIRY_ENDPOINT_URL = "UDDIInquiryEndpointURL";
    private static String UDDI_BUSINESSES_TO_IGNORE = "UDDIBusinessesToIgnore";
    private static String HOME_COMMUNITY_ID_KEY = "uddi:nhin:nhie:homecommunityid";
    private static String PUBLIC_KEY_ID_KEY = "uddi:nhin:nhie:publickey";
    private static String STATE_NAME_ID_KEY = "uddi:uddi.org:ubr:categorization:iso3166";
    private static String UNIFORM_SERVICE_NAME_KEY = "uddi:nhin:standard-servicenames";
    private static String SERVICE_VERSION_KEY = "uddi:nhin:versionofservice";

    // These are business entities that the UDDI will send us that we should ignore.
    // These are configured in the gateway.properties file and will be used to eliminate
    // some of the entries we get back from the UDDI server.
    // ------------------------------------------------------------------------------------
    private HashSet<String> m_hBusinessToIgnore = new HashSet<String>();
    private boolean m_bPropsLoaded = false; // True if the props have been loaded.

    public UDDIAccessor() {
        log = createLogger();
    }

    protected Log createLogger() {
        return LogFactory.getLog(getClass());
    }

    /**
     * This method loads information from the gateway.properties file that are pertinent to this class.
     */
    private void loadProperties() throws UDDIAccessorException {
        if (!m_bPropsLoaded) {
            try {
                String sValue = PropertyAccessor.getProperty(GATEWAY_PROPFILE_NAME, UDDI_BUSINESSES_TO_IGNORE);
                if ((sValue != null) && (sValue.length() > 0)) {
                    String saBusiness[] = sValue.split(";");
                    if ((saBusiness != null) && (saBusiness.length > 0)) {
                        for (int i = 0; i < saBusiness.length; i++) {
                            m_hBusinessToIgnore.add(saBusiness[i]);
                        }
                    }
                }

                m_bPropsLoaded = true;

            } catch (Exception e) {
                String sErrorMessage = "Failed to retrieve properties from " + GATEWAY_PROPFILE_NAME
                        + ".properties file.  Error: " + e.getMessage();
                log.error(sErrorMessage, e);
                throw new UDDIAccessorException(sErrorMessage, e);
            }
        }
    }

    /**
     * This method craetes a business entity by extracting the information from a business info object.
     * 
     * @param oBusInfo The business information that contains the information.
     * @return The translated information.
     */
    private CMBusinessEntity businessEntityFromBusinesInfo(BusinessInfo oBusInfo) {
        CMBusinessEntity oEntity = new CMBusinessEntity();
        boolean bHaveData = false;

        if (oBusInfo != null) {
            // Business Key
            // -------------
            if ((oBusInfo.getBusinessKey() != null) && (oBusInfo.getBusinessKey().length() > 0)) {
                oEntity.setBusinessKey(oBusInfo.getBusinessKey());
                bHaveData = true;
            }

            // Names
            // ------
            if ((oBusInfo.getName() != null) && (oBusInfo.getName().size() > 0)) {
                CMBusinessNames oNames = new CMBusinessNames();
                oEntity.setNames(oNames);
                for (Name oUDDIName : oBusInfo.getName()) {
                    if ((oUDDIName.getValue() != null) && (oUDDIName.getValue().length() > 0)) {
                        oNames.getBusinessName().add(oUDDIName.getValue());
                    }
                }
                bHaveData = true;
            } // if ((oBusInfo.getName() != null) && ...

            // Description
            // ------------
            if ((oBusInfo.getDescription() != null) && (oBusInfo.getDescription().size() > 0)) {
                CMBusinessDescriptions oDescripts = new CMBusinessDescriptions();
                oEntity.setDescriptions(oDescripts);
                for (Description oUDDIDescript : oBusInfo.getDescription()) {
                    if ((oUDDIDescript.getValue() != null) && (oUDDIDescript.getValue().length() > 0)) {
                        oDescripts.getBusinessDescription().add(oUDDIDescript.getValue());
                    }
                }
                bHaveData = true;
            }

            // Set up for the services. - This pass will only put in the service key.
            // We will have to do another retrieval to get the rest of the service information.
            // ------------------------------------------------------------------------
            if ((oBusInfo.getServiceInfos() != null) && (oBusInfo.getServiceInfos().getServiceInfo() != null)
                    && (oBusInfo.getServiceInfos().getServiceInfo().size() > 0)) {
                CMBusinessServices oServices = new CMBusinessServices();
                for (ServiceInfo oUDDIService : oBusInfo.getServiceInfos().getServiceInfo()) {
                    boolean bHaveServiceData = false;
                    CMBusinessService oService = new CMBusinessService();

                    // Service Key
                    // ------------
                    if ((oUDDIService.getServiceKey() != null) && (oUDDIService.getServiceKey().length() > 0)) {
                        oService.setServiceKey(oUDDIService.getServiceKey());
                        bHaveServiceData = true;
                    }

                    oService.setInternalWebService(false); // If it is in UDDI - it is not internal

                    // Service Name - We will pick this up on the detail.
                    // ---------------------------------------------------

                    if (bHaveServiceData) {
                        oServices.getBusinessService().add(oService);
                        bHaveData = true;
                    }
                } // for (ServiceInfo oUDDIService : oBusInfo.getServiceInfos().getServiceInfo())

                if (oServices.getBusinessService().size() > 0) {
                    oEntity.setBusinessServices(oServices);
                }
            }
        }

        if (bHaveData) {
            return oEntity;
        } else {
            return null;
        }
    }

    /**
     * This method extracts the business key from the business info object.
     * 
     * @param oBusInfo The business information object containing the data.
     * @return The key that was extracted.
     */
    private String extractBusinessKey(BusinessInfo oBusInfo) {
        String sKey = "";

        if ((oBusInfo != null) && (oBusInfo.getBusinessKey() != null) && (oBusInfo.getBusinessKey().length() > 0)) {
            sKey = oBusInfo.getBusinessKey();
        }

        return sKey;

    }

    private void removeIgnoredBusinesses(BusinessList businessList) {
        ArrayList<String> ignoredKeyList = new ArrayList<String>();
        if ((businessList != null) && (businessList.getBusinessInfos() != null)
                && (businessList.getBusinessInfos().getBusinessInfo() != null)
                && (businessList.getBusinessInfos().getBusinessInfo().size() > 0)) {
            for (BusinessInfo oBusInfo : businessList.getBusinessInfos().getBusinessInfo()) {
                String sKey = extractBusinessKey(oBusInfo);

                if (m_hBusinessToIgnore.contains(sKey)) {
                    ignoredKeyList.add(sKey);
                }
            }

            businessList.getBusinessInfos().getBusinessInfo().removeAll(ignoredKeyList);
        }
    }

    /**
     * This method returns the business with the specified business key. If it does not exist in the list, then null is
     * returned.
     * 
     * @param oEntities The list of businesses to search.
     * @param sBusinessKey The business key to look for.
     * @return The item from the list that matches the business key.
     */
    protected CMBusinessEntity findSpecificBusiness(List<CMBusinessEntity> oaEntities, String sBusinessKey) {
        CMBusinessEntity oEntity = null;

        if ((oaEntities != null) && (oaEntities.size() > 0)) {
            for (CMBusinessEntity oTempEntity : oaEntities) {
                if ((oTempEntity.getBusinessKey() != null) && (oTempEntity.getBusinessKey().equals(sBusinessKey))) {
                    oEntity = oTempEntity;
                    break; // We found it - get out of the loop...
                }
            }
        }

        return oEntity;
    }

    /**
     * This method returns the service with the specified business key and service key. If it does not exist in the
     * list, then null is returned.
     * 
     * @param oEntities The list of businesses to search.
     * @param sBusinessKey The business key for the business entity.
     * @param sServiceKey The service key to look for.
     * @return The item from the list that matches the business key.
     */
    private CMBusinessService findSpecificService(List<CMBusinessEntity> oaEntities, String sBusinessKey,
            String sServiceKey) {
        CMBusinessService oService = null;

        CMBusinessEntity oEntity = findSpecificBusiness(oaEntities, sBusinessKey);

        if ((oEntity != null) && (oEntity.getBusinessServices() != null)
                && (oEntity.getBusinessServices().getBusinessService() != null)
                && (oEntity.getBusinessServices().getBusinessService().size() > 0)) {
            for (CMBusinessService oTempService : oEntity.getBusinessServices().getBusinessService()) {
                if ((oTempService.getServiceKey() != null) && (oTempService.getServiceKey().equals(sServiceKey))) {
                    oService = oTempService;
                    break; // We found it - get out of the loop...
                }
            }
        }

        return oService;
    }

    /**
     * This method looks through the set of keyed reference objects for the one that is specified. Once it finds it, it
     * extracts the keyValue and returns it.
     * 
     * @param oaKeys The keys to be searched.
     * @param sDesiredKey The key to look for.
     * @return The values associated with that key.
     */
    protected List<String> findAndGetValueFromKeyedReference(List<KeyedReference> oaKeys, String sDesiredKey) {
        List<String> oValues = new ArrayList<String>();

        if ((oaKeys != null) && (oaKeys.size() > 0)) {
            for (KeyedReference oKey : oaKeys) {
                if ((oKey.getTModelKey() != null) && (oKey.getTModelKey().equals(sDesiredKey))
                        && (oKey.getKeyValue() != null)) {
                    // May be multiple declarations of States; add them all
                    oValues.add(oKey.getKeyValue());
                }
            }
        }

        return oValues;
    }

    /**
     * This method takes in a list of JAXBElements and searches for a KeyedReference where the key is the one specified.
     * If one is found, it will return the value for it.
     * 
     * @param oaElement The list of JAXB elements.
     * @param sKey The key to search for.
     * @return The value associated with that key.
     */
    private String findAndGetValueFromJAXBElementKeyedReference(List<KeyedReference> oaElement, String sKey) {
        String sValue = "";

        if ((oaElement == null) || (oaElement.size() <= 0)) {
            return "";
        }

        for (KeyedReference oKeyRef : oaElement) {
            if ((oKeyRef != null) && (oKeyRef.getTModelKey() != null) && (oKeyRef.getTModelKey().equals(sKey))
                    && (oKeyRef.getKeyValue() != null)) {
                sValue = oKeyRef.getKeyValue();
            }
        }

        return sValue;
    }

    /**
     * This method is used to populate the Business Service with the retrieved uniform service information. If there is
     * a single service name, the service passed in will be populated with that name. If there are multiple aliases for
     * a service name, a copy of the service will be created and it will be populated with the alias and then added to
     * the Business Entity structure.
     * 
     * @param oUDDIService The information on the service as retrieved from the UDDI registry.
     * @param oEntities The complete list of business entities against which the desired entity is found. If multiple
     *            uniform service names are detected a new service will be added to this entity.
     * @param oService The business service which is updated with the uniform service name. If multiples are detected a
     *            copy of this service will be made to form a new one.
     */
    public void populateUniformServiceNameAndReplicateService(BusinessService oUDDIService,
            CMBusinessEntities oEntities, CMBusinessService oService) {
        if ((oUDDIService.getCategoryBag() != null) && (oUDDIService.getCategoryBag().getKeyedReference() != null)
                && (oUDDIService.getCategoryBag().getKeyedReference().size() > 0)) {

            // Uniform Service Name
            // ---------------------
            List<String> oServiceNames = findAndGetValueFromKeyedReference(oUDDIService.getCategoryBag()
                    .getKeyedReference(), UNIFORM_SERVICE_NAME_KEY);

            if (oServiceNames != null && oServiceNames.size() > 0) {

                // The existance of more than one service name will create multiple services for the entity
                int serviceCount = 0;
                for (String sValue : oServiceNames) {
                    if ((sValue != null) && (sValue.length() > 0)) {
                        if (serviceCount == 0) {
                            oService.setUniformServiceName(sValue);
                        } else {
                            CMBusinessService oServiceCopy = oService.createCopy();
                            oServiceCopy.setUniformServiceName(sValue);
                            CMBusinessEntity oEntity = findSpecificBusiness(oEntities.getBusinessEntity(),
                                    oUDDIService.getBusinessKey());
                            oEntity.getBusinessServices().getBusinessService().add(oServiceCopy);
                        }
                        serviceCount++;
                    }
                }
            } else {
                log.debug("A Normal Service value is NOT detected for UDDI Service " + oUDDIService.getBusinessKey()
                        + " - " + oUDDIService.getServiceKey());
            }
        } // if ((oUDDIService.getCategoryBag() != null) && ...
    }

    /**
     * This method is used to retrieve the data from the UDDI server. The data is returned in the form of
     * CMBusinessEntities.
     * 
     * @return The Business Entities that were retrieved from the UDDI server.
     * 
     */
    public BusinessDetail retrieveFromUDDIServer() throws UDDIAccessorException {
        loadProperties();

        BusinessList businessList = retrieveBusinessesListFromUDDI();
        BusinessDetail businessDetail = retrieveBusinessDetail(businessList);

        return businessDetail;
    }

    /**
     * This method retrieves the business entities from the UDDI server. It does not retrieve the services or bindings.
     * They are retrieved on other calls. This only retrieves the business information.
     * 
     * @return the BusinessEntities retrieved from the UDDI server.
     * @throws UDDIAccessorException
     */
    private BusinessList retrieveBusinessesListFromUDDI() throws UDDIAccessorException {

        if (log.isDebugEnabled()) {
            log.debug("Retrieving business entities from UDDI using find_business web service call.");
        }

        BusinessList businessList = null;
        try {
            UDDIFindBusinessProxyObjectFactory uddiFactory = new UDDIFindBusinessProxyObjectFactory();
            UDDIFindBusinessProxyBase uddiProxy = uddiFactory.getUDDIBusinessInfoProxy();
            businessList = uddiProxy.findBusinessesFromUDDI();

            removeIgnoredBusinesses(businessList);
        } catch (Exception e) {
            String sErrorMessage = "Failed to call 'find_business' web service on the NHIN UDDI server.  Error: "
                    + e.getMessage();
            log.error(sErrorMessage, e);
            throw new UDDIAccessorException(sErrorMessage, e);
        }

        return businessList;
    }

    private BusinessDetail retrieveBusinessDetail(BusinessList businessList) throws UDDIAccessorException {

        if (businessList == null) {
            return null;
        }

        BusinessDetail businessDetail = null;
        BusinessInfos businessInfos = businessList.getBusinessInfos();
        try {
            GetBusinessDetail searchParams = createSearchParamsFromBusinessKeys(businessInfos);

            UDDIFindBusinessProxyObjectFactory uddiFactory = new UDDIFindBusinessProxyObjectFactory();
            UDDIFindBusinessProxyBase uddiProxy = uddiFactory.getUDDIBusinessInfoProxy();
            businessDetail = uddiProxy.getBusinessDetail(searchParams);
        } catch (Exception e) {
            String sErrorMessage = "Failed to call UDDI web service get_businessDetail method.  Error: "
                    + e.getMessage();
            log.error(sErrorMessage, e);
            throw new UDDIAccessorException(sErrorMessage, e);
        }

        return businessDetail;
    }

    private GetBusinessDetail createSearchParamsFromBusinessKeys(BusinessInfos businessInfos) {
        GetBusinessDetail searchParams = new GetBusinessDetail();
        for (BusinessInfo businessInfo : businessInfos.getBusinessInfo()) {
            if ((businessInfo.getBusinessKey() != null) && (businessInfo.getBusinessKey().length() > 0)) {
                searchParams.getBusinessKey().add(businessInfo.getBusinessKey());
            }
        }

        return searchParams;
    }

}
