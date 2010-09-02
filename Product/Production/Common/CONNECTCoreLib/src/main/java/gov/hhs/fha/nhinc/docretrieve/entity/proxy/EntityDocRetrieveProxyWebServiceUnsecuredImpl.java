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

package gov.hhs.fha.nhinc.docretrieve.entity.proxy;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayRetrieveRequestType;
import gov.hhs.fha.nhinc.entitydocretrieve.EntityDocRetrieve;
import gov.hhs.fha.nhinc.entitydocretrieve.EntityDocRetrievePortType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import java.util.Map;
import javax.xml.ws.BindingProvider;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author dunnek
 */
public class EntityDocRetrieveProxyWebServiceUnsecuredImpl implements EntityDocRetrieveProxy{
    private static Service cachedService = null;
    private static EntityDocRetrieve service = null;

    private static final String NAMESPACE_URI = "urn:gov:hhs:fha:nhinc:entitydocretrieve";
    private static final String SERVICE_LOCAL_PART = "EntityDocRetrieve";
    private static final String PORT_LOCAL_PART = "EntityDocRetrievePortType";
    private static final String WSDL_FILE = "EntityDocRetrieve.wsdl";
    private static final String WS_ADDRESSING_ACTION = NAMESPACE_URI + ":RespondingGateway_CrossGatewayRetrieve";

    private static org.apache.commons.logging.Log log = null;

    public EntityDocRetrieveProxyWebServiceUnsecuredImpl()
    {
        log = createLogger();
        service = getWebService();
    }
    protected Log createLogger()
    {
        return LogFactory.getLog(getClass());
    }
    protected EntityDocRetrieve getWebService()
    {
        return new EntityDocRetrieve();
    }

    public RetrieveDocumentSetResponseType respondingGatewayCrossGatewayRetrieve(RetrieveDocumentSetRequestType body, AssertionType assertion, NhinTargetCommunitiesType targets) {
        RetrieveDocumentSetResponseType response = null;
        
        WebServiceProxyHelper proxyHelper = new WebServiceProxyHelper();



        String serviceName = NhincConstants.ENTITY_DOC_RETRIEVE_SERVICE_NAME;
        String url = this.getUrl(serviceName);
        if (NullChecker.isNotNullish(url))
        {
            EntityDocRetrievePortType port = getPort(url, assertion, serviceName);
            RespondingGatewayCrossGatewayRetrieveRequestType message = new RespondingGatewayCrossGatewayRetrieveRequestType();

            message.setAssertion(assertion);
            message.setNhinTargetCommunities(targets);
            message.setRetrieveDocumentSetRequest(body);


            try
            {
                log.debug("invoke port");
                response = (RetrieveDocumentSetResponseType) proxyHelper.invokePort(port, EntityDocRetrievePortType.class, "respondingGatewayCrossGatewayRetrieve", message);
            }
            catch(Exception ex)
            {
                log.error("Failed to call the web service (" + serviceName + ").  An unexpected exception occurred.  " +
                        "Exception: " + ex.getMessage(), ex);
            }
        }
        return response;

    }
    protected String getUrl(String serviceName)
    {
        String result = "";
        try
        {
            result = this.getWebServiceProxyHelper().getUrlLocalHomeCommunity(serviceName);
        }
        catch(Exception ex)
        {
            log.warn("Unable to retreive url for service: " + serviceName);
            log.warn("Error: " + ex.getMessage(), ex);
        }

        return  result;
    }
    protected WebServiceProxyHelper getWebServiceProxyHelper()
    {
        return new WebServiceProxyHelper();
    }

    protected EntityDocRetrievePortType getPort(String url, AssertionType assertion, String serviceName)
    {
        WebServiceProxyHelper proxyHelper =getWebServiceProxyHelper();

        EntityDocRetrievePortType port = null;
        Service cacheService = getService(WSDL_FILE,NAMESPACE_URI, SERVICE_LOCAL_PART);
        if (cacheService != null)
        {
            log.debug("Obtained service - creating port.");
            port = cacheService.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART), EntityDocRetrievePortType.class);            
            proxyHelper.initializeUnsecurePort((javax.xml.ws.BindingProvider) port, url, WS_ADDRESSING_ACTION, assertion);
         }
        else
        {
            log.error("Unable to obtain serivce - no port created.");
        }
        return port;
    }
    protected Service getService(String wsdl, String uri, String service)
    {
        if (cachedService == null)
        {
            try
            {
                WebServiceProxyHelper proxyHelper = new WebServiceProxyHelper();
                cachedService = proxyHelper.createService(wsdl, uri, service);
            }
            catch (Throwable t)
            {
                log.error("Error creating service: " + t.getMessage(), t);
            }
        }
        return cachedService;
    }
}
