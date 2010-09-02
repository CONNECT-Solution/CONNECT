/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.connectmgr.uddi;

import gov.hhs.fha.nhinc.connectmgr.data.CMBindingDescriptions;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;

import javax.xml.bind.JAXBElement;

import javax.xml.ws.BindingProvider;

import org.uddi.api_v3.BusinessList;
import org.uddi.api_v3.BusinessInfos;
import org.uddi.api_v3.BusinessInfo;
import org.uddi.api_v3.BusinessService;
import org.uddi.api_v3.ServiceInfo;
import org.uddi.api_v3.FindBusiness;
import org.uddi.api_v3.Name;
import org.uddi.api_v3.GetBusinessDetail;
import org.uddi.api_v3.BusinessDetail;
import org.uddi.api_v3.GetServiceDetail;
import org.uddi.api_v3.ServiceDetail;
import org.uddi.api_v3.Description;
import org.uddi.api_v3.BusinessEntity;
import org.uddi.api_v3.KeyedReference;
import org.uddi.api_v3.BindingTemplate;
import org.uddi.v3_service.UDDIService;
import org.uddi.v3_service.UDDIInquiryPortType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import gov.hhs.fha.nhinc.properties.PropertyAccessor;

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
    // URL for the UDDI Server.
    private String m_sUDDIInquiryEndpointURL = "";
    // These are business entities that the UDDI will send us that we should ignore.
    // These are configured in the gateway.properties file and will be used to eliminate
    // some of the entries we get back from the UDDI server.
    //------------------------------------------------------------------------------------
    private HashSet<String> m_hBusinessToIgnore = new HashSet<String>();
    private boolean m_bPropsLoaded = false;         // True if the props have been loaded.

    public UDDIAccessor() {
        log = createLogger();
    }

    protected Log createLogger() {
        return LogFactory.getLog(getClass());
    }

    /**
     * This method loads information from the gateway.properties file that are
     * pertinent to this class.
     */
    private void loadProperties()
            throws UDDIAccessorException {
        if (!m_bPropsLoaded) {
            try {
                String sValue = PropertyAccessor.getProperty(GATEWAY_PROPFILE_NAME, UDDI_INQUIRY_ENDPOINT_URL);
                if ((sValue != null) && (sValue.length() > 0)) {
                    m_sUDDIInquiryEndpointURL = sValue;
                }

                sValue = PropertyAccessor.getProperty(GATEWAY_PROPFILE_NAME, UDDI_BUSINESSES_TO_IGNORE);
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
                String sErrorMessage = "Failed to retrieve properties from " + GATEWAY_PROPFILE_NAME +
                        ".properties file.  Error: " + e.getMessage();
                log.error(sErrorMessage, e);
                throw new UDDIAccessorException(sErrorMessage, e);
            }

            // If we do not have the endpoint URL, then we have a problem.
            //-------------------------------------------------------------
            if ((m_sUDDIInquiryEndpointURL == null) || (m_sUDDIInquiryEndpointURL.length() <= 0)) {
                log.error("Failed to retrieve property: '" + UDDI_INQUIRY_ENDPOINT_URL + "' from " +
                        GATEWAY_PROPFILE_NAME + ".properties file.   UDDI server cannot be accessed.");
            }
        }
    }

    /**
     * This method retrieves the port for the UDDI server with the correct endpoint.
     * 
     * @return 
     */
    private UDDIInquiryPortType getUDDIInquiryWebService()
            throws UDDIAccessorException {
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
            throw new UDDIAccessorException(sErrorMessage, e);
        }

        return oPort;
    }

    /**
     * This method craetes a business entity by extracting the information from 
     * a business info object.
     * 
     * @param oBusInfo The business information that contains the information.
     * @return The translated information.
     */
    private CMBusinessEntity businessEntityFromBusinesInfo(BusinessInfo oBusInfo) {
        CMBusinessEntity oEntity = new CMBusinessEntity();
        boolean bHaveData = false;

        if (oBusInfo != null) {
            // Business Key
            //-------------
            if ((oBusInfo.getBusinessKey() != null) && (oBusInfo.getBusinessKey().length() > 0)) {
                oEntity.setBusinessKey(oBusInfo.getBusinessKey());
                bHaveData = true;
            }

            // Names
            //------
            if ((oBusInfo.getName() != null) &&
                    (oBusInfo.getName().size() > 0)) {
                CMBusinessNames oNames = new CMBusinessNames();
                oEntity.setNames(oNames);
                for (Name oUDDIName : oBusInfo.getName()) {
                    if ((oUDDIName.getValue() != null) && (oUDDIName.getValue().length() > 0)) {
                        oNames.getBusinessName().add(oUDDIName.getValue());
                    }
                }
                bHaveData = true;
            }   // if ((oBusInfo.getName() != null) && ...

            // Description
            //------------
            if ((oBusInfo.getDescription() != null) &&
                    (oBusInfo.getDescription().size() > 0)) {
                CMBusinessDescriptions oDescripts = new CMBusinessDescriptions();
                oEntity.setDescriptions(oDescripts);
                for (Description oUDDIDescript : oBusInfo.getDescription()) {
                    if ((oUDDIDescript.getValue() != null) && (oUDDIDescript.getValue().length() > 0)) {
                        oDescripts.getBusinessDescription().add(oUDDIDescript.getValue());
                    }
                }
                bHaveData = true;
            }

            // Set up for the services.  - This pass will only put in the service key.
            // We will have to do another retrieval to get the rest of the service information.
            //------------------------------------------------------------------------
            if ((oBusInfo.getServiceInfos() != null) &&
                    (oBusInfo.getServiceInfos().getServiceInfo() != null) &&
                    (oBusInfo.getServiceInfos().getServiceInfo().size() > 0)) {
                CMBusinessServices oServices = new CMBusinessServices();
                for (ServiceInfo oUDDIService : oBusInfo.getServiceInfos().getServiceInfo()) {
                    boolean bHaveServiceData = false;
                    CMBusinessService oService = new CMBusinessService();

                    // Service Key
                    //------------
                    if ((oUDDIService.getServiceKey() != null) && (oUDDIService.getServiceKey().length() > 0)) {
                        oService.setServiceKey(oUDDIService.getServiceKey());
                        bHaveServiceData = true;
                    }

                    oService.setInternalWebService(false);      // If it is in UDDI - it is not internal

                    // Service Name - We will pick this up on the detail.
                    //---------------------------------------------------


                    if (bHaveServiceData) {
                        oServices.getBusinessService().add(oService);
                        bHaveData = true;
                    }
                }   // for (ServiceInfo oUDDIService : oBusInfo.getServiceInfos().getServiceInfo())

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

        if ((oBusInfo != null) &&
                (oBusInfo.getBusinessKey() != null) &&
                (oBusInfo.getBusinessKey().length() > 0)) {
            sKey = oBusInfo.getBusinessKey();
        }

        return sKey;

    }

    /**
     * This method retrieves the business entities from the UDDI server.
     * It does not retrieve the services or bindings.  They are retrieved
     * on other calls.  This only retrieves the business information.
     * 
     * @return the BusinessEntities retrieved from the UDDI server.
     * @throws UDDIAccessorException
     */
    private CMBusinessEntities retrieveBusinessesInfoFromUDDI()
            throws UDDIAccessorException {
        CMBusinessEntities oEntities = new CMBusinessEntities();

        if (log.isDebugEnabled()) {
            log.debug("Retrieving business entities from UDDI using find_business web service call.");
        }

        BusinessList oBusinessList = null;

        try {
            UDDIInquiryPortType oPort = getUDDIInquiryWebService();

            // Make the call...
            //-----------------
            FindBusiness oSearchParams = new FindBusiness();
            oSearchParams.setMaxRows(100);
            oBusinessList = oPort.findBusiness(oSearchParams);

        } catch (Exception e) {
            String sErrorMessage = "Failed to call 'find_business' web service on the NHIN UDDI server.  Errro: " +
                    e.getMessage();
            log.error(sErrorMessage, e);
            throw new UDDIAccessorException(sErrorMessage, e);
        }

        // Now lets go through what we have...
        //------------------------------------
        if ((oBusinessList != null) &&
                (oBusinessList.getBusinessInfos() != null) &&
                (oBusinessList.getBusinessInfos().getBusinessInfo() != null) &&
                (oBusinessList.getBusinessInfos().getBusinessInfo().size() > 0)) {
            for (BusinessInfo oBusInfo : oBusinessList.getBusinessInfos().getBusinessInfo()) {
                String sKey = extractBusinessKey(oBusInfo);

                if (!m_hBusinessToIgnore.contains(sKey)) {
                    // Make sure this is not one of the ones we need to filter out...
                    //----------------------------------------------------------------
                    CMBusinessEntity oEntity = null;
                    oEntity = businessEntityFromBusinesInfo(oBusInfo);
                    if (oEntity != null) {
                        oEntities.getBusinessEntity().add(oEntity);
                    }
                }
            }
        }

        return oEntities;
    }

    /**
     * This method returns the business with the specified
     * business key.  If it does not exist in the list, then null is returned.
     * 
     * @param oEntities The list of businesses to search.
     * @param sBusinessKey The business key to look for.
     * @return The item from the list that matches the business key.
     */
    protected CMBusinessEntity findSpecificBusiness(List<CMBusinessEntity> oaEntities, String sBusinessKey) {
        CMBusinessEntity oEntity = null;

        if ((oaEntities != null) &&
                (oaEntities.size() > 0)) {
            for (CMBusinessEntity oTempEntity : oaEntities) {
                if ((oTempEntity.getBusinessKey() != null) &&
                        (oTempEntity.getBusinessKey().equals(sBusinessKey))) {
                    oEntity = oTempEntity;
                    break;          // We found it - get out of the loop...
                }
            }
        }

        return oEntity;
    }

    /**
     * This method returns the service with the specified business key and service key.  
     * If it does not exist in the list, then null is returned.
     * 
     * @param oEntities The list of businesses to search.
     * @param sBusinessKey The business key for the business entity.
     * @param sServiceKey The service key to look for.
     * @return The item from the list that matches the business key.
     */
    private CMBusinessService findSpecificService(List<CMBusinessEntity> oaEntities,
            String sBusinessKey, String sServiceKey) {
        CMBusinessService oService = null;

        CMBusinessEntity oEntity = findSpecificBusiness(oaEntities, sBusinessKey);

        if ((oEntity != null) &&
                (oEntity.getBusinessServices() != null) &&
                (oEntity.getBusinessServices().getBusinessService() != null) &&
                (oEntity.getBusinessServices().getBusinessService().size() > 0)) {
            for (CMBusinessService oTempService : oEntity.getBusinessServices().getBusinessService()) {
                if ((oTempService.getServiceKey() != null) &&
                        (oTempService.getServiceKey().equals(sServiceKey))) {
                    oService = oTempService;
                    break;          // We found it - get out of the loop...
                }
            }
        }

        return oService;
    }

    /**
     * This method looks through the set of keyed reference objects for the one that is specified.
     * Once it finds it, it extracts the keyValue and returns it.
     * 
     * @param oaKeys The keys to be searched.
     * @param sDesiredKey The key to look for.
     * @return The values associated with that key.
     */
    protected List<String> findAndGetValueFromKeyedReference(List<KeyedReference> oaKeys, String sDesiredKey) {
        List<String> oValues = new ArrayList<String>();

        if ((oaKeys != null) &&
                (oaKeys.size() > 0)) {
            for (KeyedReference oKey : oaKeys) {
                if ((oKey.getTModelKey() != null) &&
                        (oKey.getTModelKey().equals(sDesiredKey)) &&
                        (oKey.getKeyValue() != null)) {
                    // May be multiple declarations of States; add them all
                    oValues.add(oKey.getKeyValue());
                }
            }
        }

        return oValues;
    }

    /**
     * This method takes in a list of JAXBElements and searches for a KeyedReference where 
     * the key is the one specified.  If one is found, it will return the value for it.
     * 
     * @param oaElement The list of JAXB elements.
     * @param sKey The key to search for.
     * @return The value associated with that key.
     */
    private String findAndGetValueFromJAXBElementKeyedReference(List<KeyedReference> oaElement, String sKey) {
        String sValue = "";

        if ((oaElement == null) ||
                (oaElement.size() <= 0)) {
            return "";
        }

        for (KeyedReference oKeyRef : oaElement) {
            if ((oKeyRef != null) &&
                    (oKeyRef.getTModelKey() != null) &&
                    (oKeyRef.getTModelKey().equals(sKey)) &&
                    (oKeyRef.getKeyValue() != null)) {
                sValue = oKeyRef.getKeyValue();
            }
        }

        return sValue;
    }

    /**
     * This method loops through the business entities and fills in any pertinent
     * detailed information by calling the UDDI server get_businessDetail function.  Note
     * that this information was not available in the find_business.  In order to get it
     * we have to do separate call.
     * 
     * @param oEntities The businesses to retrieve the detail and the object where the
     *                  details will be placed.
     */
    private void retrieveDetailedBusinessInfoFromUDDI(CMBusinessEntities oEntities)
            throws UDDIAccessorException {
        if ((oEntities == null) ||
                (oEntities.getBusinessEntity() == null) ||
                (oEntities.getBusinessEntity().size() <= 0)) {
            return;         // we are done  there is nothing to retrieve.
        }

        BusinessDetail oResult = null;

        try {
            GetBusinessDetail oSearchParams = new GetBusinessDetail();

            // Load up the list of keys to retrieve the details of...
            //--------------------------------------------------------
            for (CMBusinessEntity oEntity : oEntities.getBusinessEntity()) {
                if ((oEntity.getBusinessKey() != null) && (oEntity.getBusinessKey().length() > 0)) {
                    oSearchParams.getBusinessKey().add(oEntity.getBusinessKey());
                }
            }   // for (CMBusinessEntity oEntity : oEntities.getBusinessEntity())

            UDDIInquiryPortType oPort = getUDDIInquiryWebService();
            oResult = oPort.getBusinessDetail(oSearchParams);
        } catch (Exception e) {
            String sErrorMessage = "Failed to call UDDI web service get_businessDetail method.  Error: " + e.getMessage();
            log.error(sErrorMessage, e);
            throw new UDDIAccessorException(sErrorMessage, e);
        }

        // Now let's process the results...
        //---------------------------------
        if ((oResult != null) &&
                (oResult.getBusinessEntity() != null) &&
                (oResult.getBusinessEntity().size() > 0)) {
            // Now put the returned information back into our structure.
            //-----------------------------------------------------------
            for (BusinessEntity oUDDIEntity : oResult.getBusinessEntity()) {
                if ((oUDDIEntity.getBusinessKey() != null) && (oUDDIEntity.getBusinessKey().length() > 0)) {
                    CMBusinessEntity oEntity = findSpecificBusiness(oEntities.getBusinessEntity(), oUDDIEntity.getBusinessKey());

                    if (oEntity != null) {
                        // Home community ID
                        //------------------
                        if ((oUDDIEntity.getIdentifierBag() != null) &&
                                (oUDDIEntity.getIdentifierBag().getKeyedReference() != null) &&
                                (oUDDIEntity.getIdentifierBag().getKeyedReference().size() > 0)) {
                            List<String> oValues = findAndGetValueFromKeyedReference(oUDDIEntity.getIdentifierBag().getKeyedReference(),
                                    HOME_COMMUNITY_ID_KEY);
                            if (oValues != null && oValues.size() == 1) {
                                for (String sValue : oValues) {
                                    if ((sValue != null) && (sValue.length() > 0)) {
                                        if (sValue.startsWith("urn:oid:")) {
                                            sValue = sValue.substring("urn:oid:".length());
                                        }
                                        oEntity.setHomeCommunityId(sValue);
                                    }
                                }
                            } else {
                                log.debug("A single Home Community value is NOT detected for UDDI Entity " + oUDDIEntity.getBusinessKey());
                            }
                        }   // if ((oUDDIEntity.getIdentifierBag() != null) && ...

                        if ((oUDDIEntity.getCategoryBag() != null) &&
                                (oUDDIEntity.getCategoryBag().getKeyedReference() != null) &&
                                (oUDDIEntity.getCategoryBag().getKeyedReference().size() > 0)) {
                            // Public Key
                            //-----------
                            List<String> oPublicKeys = findAndGetValueFromKeyedReference(oUDDIEntity.getCategoryBag().getKeyedReference(),
                                    PUBLIC_KEY_ID_KEY);
                            if (oPublicKeys != null && oPublicKeys.size() == 1) {
                                for (String sValue : oPublicKeys) {
                                    if ((sValue != null) && (sValue.length() > 0)) {
                                        oEntity.setPublicKey(sValue);
                                    }
                                }
                            } else {
                                log.debug("A single Public Key value is NOT detected for UDDI Entity " + oUDDIEntity.getBusinessKey());
                            }
                            // State names
                            //------------
                            List<String> oStateNames = findAndGetValueFromKeyedReference(oUDDIEntity.getCategoryBag().getKeyedReference(),
                                    STATE_NAME_ID_KEY);
                            if (oStateNames != null && oStateNames.size() > 0) {
                                CMStates oStates = new CMStates();
                                List<String> oStatesList = oStates.getState();
                                for (String sValue : oStateNames) {
                                    if ((sValue != null) && (sValue.length() > 0)) {
                                        oStatesList.add(sValue);
                                    }
                                }
                                oEntity.setStates(oStates);
                            } else {
                                log.debug("No State name is detected for UDDI Entity " + oUDDIEntity.getBusinessKey());
                            }
                        }
                    }   // if (oEntity != nulll)
                }   // if ((oUDDIEntity.getBusinessKey() != null) && (oUDDIEntity.getBusinessKey().length() > 0))
            }   // for (BusinessEntity oUDDIEntity : oResult.getBusinessEntity())

        }   // if ((oResult != null) &&
    }

    /**
     * This method retrieves the service information from the UDDI server for 
     * each of the business entities.
     * 
     * @param oEntities The business entities for which services should be retrieved. The
     *                  information is placed in the appropriate location in this object.
     * @throws UDDIAccessorException
     */
    private void retrieveDetailedServiceInfoFromUDDI(CMBusinessEntities oEntities)
            throws UDDIAccessorException {
        if ((oEntities == null) ||
                (oEntities.getBusinessEntity() == null) ||
                (oEntities.getBusinessEntity().size() <= 0)) {
            return;         // we are done  there is nothing to retrieve.
        }

        ServiceDetail oResult = null;

        try {
            GetServiceDetail oSearchParams = new GetServiceDetail();

            // Load up the list of service keys to retrieve the details of...
            //--------------------------------------------------------
            for (CMBusinessEntity oEntity : oEntities.getBusinessEntity()) {
                if ((oEntity.getBusinessServices() != null) &&
                        (oEntity.getBusinessServices().getBusinessService() != null) &&
                        (oEntity.getBusinessServices().getBusinessService().size() > 0)) {
                    for (CMBusinessService oService : oEntity.getBusinessServices().getBusinessService()) {
                        if ((oService.getServiceKey() != null) && (oService.getServiceKey().length() > 0)) {
                            oSearchParams.getServiceKey().add(oService.getServiceKey());
                        }
                    }   // for (CMBusinessService oService : oEntity.getBusinessServices().getBusinessService())
                }   // if ((oEntity.getBusinessServices() != null) && ...
            }   // for (CMBusinessEntity oEntity : oEntities.getBusinessEntity())

            UDDIInquiryPortType oPort = getUDDIInquiryWebService();
            oResult = oPort.getServiceDetail(oSearchParams);
        } catch (Exception e) {
            String sErrorMessage = "Failed to call UDDI web service get_serviceDetail method.  Error: " + e.getMessage();
            log.error(sErrorMessage, e);
            throw new UDDIAccessorException(sErrorMessage, e);
        }

        // Now let's process the results...
        //---------------------------------
        if ((oResult != null) &&
                (oResult.getBusinessService() != null) &&
                (oResult.getBusinessService().size() > 0)) {
            // Now put the returned information back into our structure.
            //-----------------------------------------------------------
            for (BusinessService oUDDIService : oResult.getBusinessService()) {
                if ((oUDDIService.getServiceKey() != null) &&
                        (oUDDIService.getServiceKey().length() > 0) &&
                        (oUDDIService.getBusinessKey() != null) &&
                        (oUDDIService.getBusinessKey().length() > 0)) {
                    CMBusinessService oService = findSpecificService(oEntities.getBusinessEntity(),
                            oUDDIService.getBusinessKey(),
                            oUDDIService.getServiceKey());

                    if (oService != null) {
                        // Binding Service Name
                        //----------------------
                        if ((oUDDIService.getName() != null) &&
                                (oUDDIService.getName().size() > 0)) {
                            CMBindingNames oNames = new CMBindingNames();
                            oService.setNames(oNames);

                            for (Name oUDDIName : oUDDIService.getName()) {
                                if ((oUDDIName.getValue() != null) && (oUDDIName.getValue().length() > 0)) {
                                    oService.getNames().getName().add(oUDDIName.getValue());
                                }
                            }
                        }   // if ((oUDDIService.getName() != null) &&

                        // Binding Descriptions
                        //---------------------
                        if ((oUDDIService.getDescription() != null) &&
                                (oUDDIService.getDescription().size() > 0)) {
                            CMBindingDescriptions oDescripts = new CMBindingDescriptions();
                            oService.setDescriptions(oDescripts);

                            for (Description oUDDIDescript : oUDDIService.getDescription()) {
                                if ((oUDDIDescript.getValue() != null) && (oUDDIDescript.getValue().length() > 0)) {
                                    oService.getDescriptions().getDescription().add(oUDDIDescript.getValue());
                                }
                            }
                        }   // if ((oUDDIService.getDescription() != null) && ...

                        // Binding Template Information
                        //-----------------------------
                        if ((oUDDIService.getBindingTemplates() != null) &&
                                (oUDDIService.getBindingTemplates().getBindingTemplate() != null) &&
                                (oUDDIService.getBindingTemplates().getBindingTemplate().size() > 0)) {
                            CMBindingTemplates oTemplates = new CMBindingTemplates();
                            for (BindingTemplate oUDDITemplate : oUDDIService.getBindingTemplates().getBindingTemplate()) {
                                CMBindingTemplate oTemplate = new CMBindingTemplate();
                                boolean bHaveData = false;

                                // Endpoint URL
                                //--------------
                                if ((oUDDITemplate.getAccessPoint() != null) &&
                                        (oUDDITemplate.getAccessPoint().getValue() != null) &&
                                        (oUDDITemplate.getAccessPoint().getValue().length() > 0)) {
                                    oTemplate.setEndpointURL(oUDDITemplate.getAccessPoint().getValue());
                                    bHaveData = true;
                                }

                                //Service Version
                                //---------------
                                if ((oUDDITemplate.getCategoryBag() != null) &&
                                        (oUDDITemplate.getCategoryBag().getKeyedReference() != null) &&
                                        (oUDDITemplate.getCategoryBag().getKeyedReference().size() > 0)) {

                                    List<String> oServiceVersions = findAndGetValueFromKeyedReference(oUDDITemplate.getCategoryBag().getKeyedReference(),
                                            SERVICE_VERSION_KEY);
                                    if (oServiceVersions != null && oServiceVersions.size() == 1) {
                                        for (String sValue : oServiceVersions) {
                                            if ((sValue != null) && (sValue.length() > 0)) {
                                                oTemplate.setServiceVersion(sValue);
                                                bHaveData = true;
                                            }
                                        }
                                    } else {
                                        log.debug("A single Service Version is NOT detected for UDDI Service " + oUDDIService.getBusinessKey() + " - " + oUDDIService.getServiceKey());
                                    }
                                }
                                if (bHaveData) {
                                    oTemplates.getBindingTemplate().add(oTemplate);
                                }
                            }

                            if ((oTemplates.getBindingTemplate() != null) &&
                                    (oTemplates.getBindingTemplate().size() > 0)) {
                                oService.setBindingTemplates(oTemplates);
                            }
                        }

                        // Uniform Service Name
                        //
                        // Multiple Uniform Service Names will result in the
                        // creation of oService copies so this should be the last
                        // population to be performed.
                        //----------------------------------------
                        populateUniformServiceNameAndReplicateService(oUDDIService, oEntities, oService);

                    }   // if (oService != null)
                }   // if ((oUDDIService.getServiceKey() != null) &&  ...
            }   // for (BusinessService oUDDIService : oResult.getBusinessService())

        }   // if ((oResult != null) &&
    }

    /**
     * This method is used to populate the Business Service with the retrieved
     * uniform service information. If there is a single service name, the
     * service passed in will be populated with that name.  If there are
     * multiple aliases for a service name, a copy of the service will be
     * created and it will be populated with the alias and then added to the
     * Business Entity structure.
     *
     * @param oUDDIService The information on the service as retrieved from the
     *                     UDDI registry.
     * @param oEntities The complete list of business entities against which the
     *                  desired entity is found.  If multiple uniform service
     *                  names are detected a new service will be added to this
     *                  entity.
     * @param oService  The business service which is updated with the uniform
     *                  service name.  If multiples are detected a copy of this
     *                  service will be made to form a new one.
     */
    public void populateUniformServiceNameAndReplicateService(BusinessService oUDDIService, CMBusinessEntities oEntities, CMBusinessService oService) {
        if ((oUDDIService.getCategoryBag() != null) &&
                (oUDDIService.getCategoryBag().getKeyedReference() != null) &&
                (oUDDIService.getCategoryBag().getKeyedReference().size() > 0)) {

            // Uniform Service Name
            //---------------------
            List<String> oServiceNames = findAndGetValueFromKeyedReference(oUDDIService.getCategoryBag().getKeyedReference(),
                    UNIFORM_SERVICE_NAME_KEY);

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
                log.debug("A Normal Service value is NOT detected for UDDI Service " + oUDDIService.getBusinessKey() + " - " + oUDDIService.getServiceKey());
            }
        }   // if ((oUDDIService.getCategoryBag() != null) &&  ...
    }

    /**
     * This method is used to retrieve the data from the UDDI server.  The
     * data is returned in the form of CMBusinessEntities.
     * 
     * @return The Business Entities that were retrieved from the UDDI server.
     * 
     */
    public CMBusinessEntities retrieveFromUDDIServer()
            throws UDDIAccessorException {
        CMBusinessEntities oEntities = new CMBusinessEntities();

        loadProperties();


        // If we are failing to load the properties or if we do not
        // have the endpoint URL - there is nothing to do...
        //-----------------------------------------------------------
        if ((!m_bPropsLoaded) || (m_sUDDIInquiryEndpointURL == null) ||
                (m_sUDDIInquiryEndpointURL.length() <= 0)) {
            return null;
        }

        // First step is to retrieve the high level business information...
        //------------------------------------------------------------------
        oEntities = retrieveBusinessesInfoFromUDDI();

        // Now lets retrieve the detailed business & service information for 
        // these businesses.
        //--------------------------------------------------------------------------
        if (oEntities != null) {
            retrieveDetailedBusinessInfoFromUDDI(oEntities);
            retrieveDetailedServiceInfoFromUDDI(oEntities);
        }


        return oEntities;
    }

    /**
     * Main method.
     *
     * @param args
     */
    public static void main(String args[]) {
        UDDIAccessor oAccessor = new UDDIAccessor();

        CMBusinessEntities oEntities = null;

        try {
            oEntities = oAccessor.retrieveFromUDDIServer();

            if (oEntities != null) {
                CMUDDIConnectionInfo oUDDIConnectionInfo = new CMUDDIConnectionInfo();
                oUDDIConnectionInfo.setBusinessEntities(oEntities);

                String sXML = CMUDDIConnectionInfoXML.serialize(oUDDIConnectionInfo);
                System.out.println(sXML);
            }
        } catch (Exception e) {
            System.out.println("An unexpected exception occurred: " + e.getMessage());
            e.printStackTrace();
        }



        System.out.println("");
        System.out.println("We are done.");
    }
}
