/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.docrepositoryadapter.proxy;

import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import ihe.iti.xds_b._2007.DocumentRepositoryPortType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

/**
 *
 *
 * @author Neil Webb
 */
public class AdapterDocumentRepositoryWebServiceProxy implements AdapterDocumentRepositoryProxy {

    private static String ADAPTER_PROPERTY_FILE_NAME = "adapter";
    private static String XDS_HOME_COMMUNITY_ID_PROPERTY = "XDSbHomeCommunityId";
    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(AdapterDocumentRepositoryWebServiceProxy.class);
    
    private static Service cachedService = null;
    private static final String NAMESPACE_URI = "urn:ihe:iti:xds-b:2007";
    private static final String SERVICE_LOCAL_PART = "DocumentRepository_Service";
    private static final String PORT_LOCAL_PART = "DocumentRepository_Port_Soap";
    private static final String WSDL_FILE = "AdapterComponentDocRepository.wsdl";
    private WebServiceProxyHelper oProxyHelper = null;
    

    public RetrieveDocumentSetResponseType retrieveDocumentSet(RetrieveDocumentSetRequestType request) {
        RetrieveDocumentSetResponseType response = null;
        try { // Call Web Service Operation
            String url = null;

            String xdsbHomeCommunityId = PropertyAccessor.getProperty(ADAPTER_PROPERTY_FILE_NAME, XDS_HOME_COMMUNITY_ID_PROPERTY);
            if(log.isDebugEnabled())
            {
                log.debug("Value of " + XDS_HOME_COMMUNITY_ID_PROPERTY + " retrieved from the " + ADAPTER_PROPERTY_FILE_NAME + ".properties file: " + xdsbHomeCommunityId);
            }
            if(NullChecker.isNotNullish(xdsbHomeCommunityId))
            {
            	url = oProxyHelper.getUrlFromHomeCommunity(xdsbHomeCommunityId, NhincConstants.ADAPTER_DOC_REPOSITORY_SERVICE_NAME);
            }

            DocumentRepositoryPortType port = getPort(url);

            response = (RetrieveDocumentSetResponseType)oProxyHelper.invokePort(port, DocumentRepositoryPortType.class, "documentRepositoryRetrieveDocumentSet", request);
            
          
        } catch (Exception ex) {
            log.error("Error sending message to the adapter document repository: " + ex.getMessage(), ex);
        }

        return response;
    }

    public RegistryResponseType provideAndRegisterDocumentSet(ProvideAndRegisterDocumentSetRequestType request) {
        RegistryResponseType result = null;
        try { // Call Web Service Operation

            String url = null;

            String xdsbHomeCommunityId = PropertyAccessor.getProperty(ADAPTER_PROPERTY_FILE_NAME, XDS_HOME_COMMUNITY_ID_PROPERTY);
            if (xdsbHomeCommunityId != null &&
                    !xdsbHomeCommunityId.equals("")) {
                url = oProxyHelper.getUrlFromHomeCommunity(xdsbHomeCommunityId, NhincConstants.ADAPTER_DOC_REPOSITORY_SERVICE_NAME);
            }

            DocumentRepositoryPortType port = getPort(url);

            result = (RegistryResponseType)oProxyHelper.invokePort(port, DocumentRepositoryPortType.class, "documentRepositoryProvideAndRegisterDocumentSetB", request);
            
        } catch (Exception ex) {
            log.error("Error sending message to the adapter document repository: " + ex.getMessage(), ex);
        }
        return result;
    }

    private DocumentRepositoryPortType getPort(String url) {
    	DocumentRepositoryPortType port = null;
        Service service = getService();
        if (service != null)
        {
            log.debug("Obtained service - creating port.");

            port = service.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART), DocumentRepositoryPortType.class);
            oProxyHelper.initializeUnsecurePort((javax.xml.ws.BindingProvider) port, url, null, null);
        }
        else
        {
            log.error("Unable to obtain serivce - no port created.");
        }
        return port;
        
    }
    
    /**
     * Retrieve the service class for this web service.
     *
     * @return The service class for this web service.
     */
    protected Service getService()
    {
        if (cachedService == null)
        {
            try
            {
                cachedService = oProxyHelper.createService(WSDL_FILE, NAMESPACE_URI, SERVICE_LOCAL_PART);
            }
            catch (Throwable t)
            {
                log.error("Error creating service: " + t.getMessage(), t);
            }
        }
        return cachedService;
    }
    
}
