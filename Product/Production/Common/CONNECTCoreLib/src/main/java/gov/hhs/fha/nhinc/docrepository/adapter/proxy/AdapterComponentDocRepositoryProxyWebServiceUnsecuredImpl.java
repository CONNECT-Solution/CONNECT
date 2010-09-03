/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.docrepository.adapter.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import ihe.iti.xds_b._2007.DocumentRepositoryPortType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author rayj
 */
public class AdapterComponentDocRepositoryProxyWebServiceUnsecuredImpl implements AdapterComponentDocRepositoryProxy {

    private Log log = null;
    private static Service cachedService = null;
    private static final String NAMESPACE_URI = "urn:ihe:iti:xds-b:2007";
    private static final String SERVICE_LOCAL_PART = "DocumentRepository_Service";
    private static final String PORT_LOCAL_PART = "DocumentRepository_Port_Soap";
    private static final String WSDL_FILE = "AdapterComponentDocRepository.wsdl";
    private static final String WS_ADDRESSING_ACTION = "urn:ihe:iti:2007:RetrieveDocumentSet";
    private WebServiceProxyHelper oProxyHelper = null;

    public AdapterComponentDocRepositoryProxyWebServiceUnsecuredImpl()
    {
        log = createLogger();
        oProxyHelper = createWebServiceProxyHelper();
    }

    protected Log createLogger()
    {
        return LogFactory.getLog(getClass());
    }

    protected WebServiceProxyHelper createWebServiceProxyHelper()
    {
        return new WebServiceProxyHelper();
    }

    /**
     * This method retrieves and initializes the port.
     *
     * @param url The URL for the web service.
     * @return The port object for the web service.
     */
    protected DocumentRepositoryPortType getPort(String url, String wsAddressingAction, AssertionType assertion)
    {
        DocumentRepositoryPortType port = null;
        Service service = getService();
        if (service != null)
        {
            log.debug("Obtained service - creating port.");

            port = service.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART), DocumentRepositoryPortType.class);
            oProxyHelper.initializeUnsecurePort((javax.xml.ws.BindingProvider) port, url, wsAddressingAction, assertion);
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


    public RetrieveDocumentSetResponseType retrieveDocument(RetrieveDocumentSetRequestType msg, AssertionType assertion) {
        log.debug("Begin retrieveDocument");
        RetrieveDocumentSetResponseType response = null;

        try
        {
            String xdsbHomeCommunityId = PropertyAccessor.getProperty(NhincConstants.ADAPTER_PROPERTY_FILE_NAME, NhincConstants.XDS_HOME_COMMUNITY_ID_PROPERTY);
            String url = oProxyHelper.getUrlFromHomeCommunity(xdsbHomeCommunityId, NhincConstants.ADAPTER_DOC_REPOSITORY_SERVICE_NAME);
            DocumentRepositoryPortType port = getPort(url, WS_ADDRESSING_ACTION, assertion);

            if(msg == null)
            {
                log.error("Message was null");
            }
            else if(port == null)
            {
                log.error("port was null");
            }
            else
            {
                response = (RetrieveDocumentSetResponseType)oProxyHelper.invokePort(port, DocumentRepositoryPortType.class, "documentRepositoryRetrieveDocumentSet", msg);
            }
        }
        catch (Exception ex)
        {
            log.error("Error sending Adapter Component Doc Repository Unsecured message: " + ex.getMessage(), ex);
            response = new RetrieveDocumentSetResponseType();
            RegistryResponseType regResp = new RegistryResponseType();

            regResp.setStatus("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure");

            RegistryError registryError = new RegistryError();
            registryError.setCodeContext("Processing Adapter Doc Query document retrieve");
            registryError.setErrorCode("XDSRepostoryError");
            registryError.setSeverity("Error");
            regResp.getRegistryErrorList().getRegistryError().add(registryError);
            response.setRegistryResponse(regResp);
        }

        log.debug("End retrieveDocument");
        return response;
    }

}
