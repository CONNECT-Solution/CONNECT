/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.docquery.adapter.proxy;

import gov.hhs.fha.nhinc.adapterdocquerysecured.AdapterDocQuerySecuredPortType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 *
 * @author Neil Webb
 */
public class AdapterDocQueryProxyWebServiceSecuredImpl implements AdapterDocQueryProxy
{
    private Log log = null;
    private static Service cachedService = null;
    private static final String NAMESPACE_URI = "urn:gov:hhs:fha:nhinc:adapterdocquerysecured";
    private static final String SERVICE_LOCAL_PART = "AdapterDocQuerySecured";
    private static final String PORT_LOCAL_PART = "AdapterDocQuerySecuredPortSoap";
    private static final String WSDL_FILE = "AdapterDocQuerySecured.wsdl";
    private static final String WS_ADDRESSING_ACTION = "urn:gov:hhs:fha:nhinc:adapterdocquerysecured:AdapterDocQueryRequestSecuredMessage";
    private WebServiceProxyHelper oProxyHelper = null;

    public AdapterDocQueryProxyWebServiceSecuredImpl()
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
    protected AdapterDocQuerySecuredPortType getPort(String url, String serviceAction, String wsAddressingAction, AssertionType assertion)
    {
        AdapterDocQuerySecuredPortType port = null;
        Service service = getService();
        if (service != null)
        {
            log.debug("Obtained service - creating port.");

            port = service.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART), AdapterDocQuerySecuredPortType.class);
            oProxyHelper.initializeSecurePort((javax.xml.ws.BindingProvider) port, url, serviceAction, wsAddressingAction, assertion);
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

    public AdhocQueryResponse respondingGatewayCrossGatewayQuery(AdhocQueryRequest msg, AssertionType assertion)
    {
        AdhocQueryResponse response = null;

        try
        {
            String url = oProxyHelper.getUrlLocalHomeCommunity(NhincConstants.ADAPTER_DOC_QUERY_SECURED_SERVICE_NAME);
            AdapterDocQuerySecuredPortType port = getPort(url, NhincConstants.DOC_QUERY_ACTION, WS_ADDRESSING_ACTION, assertion);

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
                response = (AdhocQueryResponse)oProxyHelper.invokePort(port, AdapterDocQuerySecuredPortType.class, "respondingGatewayCrossGatewayQuery", msg);
            }
        }
        catch (Exception ex)
        {
            log.error("Error sending Adapter Doc Query Secured message: " + ex.getMessage(), ex);
            response = new AdhocQueryResponse();
            response.setStatus("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure");

            RegistryError registryError = new RegistryError();
            registryError.setCodeContext("Processing Adapter Doc Query document query");
            registryError.setErrorCode("XDSRegistryError");
            registryError.setSeverity("Error");
            response.getRegistryErrorList().getRegistryError().add(registryError);
        }

        return response;
    }
}
