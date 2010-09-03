/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.docrepositoryadapter.proxy;

import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import ihe.iti.xds_b._2007.DocumentRepositoryService;
import ihe.iti.xds_b._2007.DocumentRepositoryPortType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
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
    private static DocumentRepositoryService service = new DocumentRepositoryService();

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
                url = ConnectionManagerCache.getEndpointURLByServiceName(xdsbHomeCommunityId, NhincConstants.ADAPTER_DOC_REPOSITORY_SERVICE_NAME);
            }

            if(NullChecker.isNullish(url))
            {
                url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.ADAPTER_DOC_REPOSITORY_SERVICE_NAME);
                if(NullChecker.isNotNullish(xdsbHomeCommunityId))
                {
                    log.warn("The endpoint URL retrieved for " + XDS_HOME_COMMUNITY_ID_PROPERTY + " (" + xdsbHomeCommunityId + ") from the " + ADAPTER_PROPERTY_FILE_NAME + ".properties file was not found. The default local adapter doc repository endpoint will be used: " + url);
                }
            }
            else if(log.isDebugEnabled())
            {
                log.debug("The doc repository endpoint URL retrieved for the " + XDS_HOME_COMMUNITY_ID_PROPERTY + " property was found: " + url);
            }

            DocumentRepositoryPortType port = getPort(url);

            response = port.documentRepositoryRetrieveDocumentSet(request);
        } catch (Exception ex) {
            log.error("Error sending message to the adapter document repository: " + ex.getMessage(), ex);
        }

        return response;
    }

    public RegistryResponseType provideAndRegisterDocumentSet(ProvideAndRegisterDocumentSetRequestType body) {
        RegistryResponseType result = null;
        try { // Call Web Service Operation

            String url = null;

            String xdsbHomeCommunityId = PropertyAccessor.getProperty(ADAPTER_PROPERTY_FILE_NAME, XDS_HOME_COMMUNITY_ID_PROPERTY);
            if (xdsbHomeCommunityId != null &&
                    !xdsbHomeCommunityId.equals("")) {
                url = ConnectionManagerCache.getEndpointURLByServiceName(xdsbHomeCommunityId, NhincConstants.ADAPTER_DOC_REPOSITORY_SERVICE_NAME);
            }

            if (url == null) {
                url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.ADAPTER_DOC_REPOSITORY_SERVICE_NAME);
            }
            DocumentRepositoryPortType port = getPort(url);

            result = port.documentRepositoryProvideAndRegisterDocumentSetB(body);
            System.out.println("Result = " + result);
        } catch (Exception ex) {
            log.error("Error sending message to the adapter document repository: " + ex.getMessage(), ex);
        }
        return result;
    }

    private DocumentRepositoryPortType getPort(String url) {
        DocumentRepositoryPortType port = service.getDocumentRepositoryPortSoap();

        log.debug("Setting endpoint address to adapter document repository to " + url);
        ((javax.xml.ws.BindingProvider) port).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);

        return port;
    }
}
