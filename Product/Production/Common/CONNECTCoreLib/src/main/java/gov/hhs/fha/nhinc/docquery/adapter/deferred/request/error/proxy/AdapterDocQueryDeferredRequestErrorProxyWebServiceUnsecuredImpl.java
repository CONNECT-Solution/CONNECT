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

package gov.hhs.fha.nhinc.docquery.adapter.deferred.request.error.proxy;

import gov.hhs.fha.nhinc.adapterdocquerydeferredrequesterror.AdapterDocQueryDeferredRequestErrorPortType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.AdapterDocumentQueryDeferredRequestErrorType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import gov.hhs.healthit.nhin.DocQueryAcknowledgementType;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author jhoppesc
 */
public class AdapterDocQueryDeferredRequestErrorProxyWebServiceUnsecuredImpl implements AdapterDocQueryDeferredRequestErrorProxy {
    private Log log = null;
    private static Service cachedService = null;
    private static final String NAMESPACE_URI = "urn:gov:hhs:fha:nhinc:adapterdocquerydeferredrequesterror";
    private static final String SERVICE_LOCAL_PART = "AdapterDocQueryDeferredRequestError";
    private static final String PORT_LOCAL_PART = "AdapterDocQueryDeferredRequestErrorPortSoap";
    private static final String WSDL_FILE = "AdapterDocQueryDeferredRequestError.wsdl";
    private static final String WS_ADDRESSING_ACTION = "urn:gov:hhs:fha:nhinc:adapterdocquerydeferredrequesterror:RespondingGateway_CrossGatewayQueryRequestErrorMessage";
    private WebServiceProxyHelper oProxyHelper = null;

    public AdapterDocQueryDeferredRequestErrorProxyWebServiceUnsecuredImpl()
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
    protected AdapterDocQueryDeferredRequestErrorPortType getPort(String url, String wsAddressingAction, AssertionType assertion)
    {
        AdapterDocQueryDeferredRequestErrorPortType port = null;
        Service service = getService();
        if (service != null)
        {
            log.debug("Obtained service - creating port.");

            port = service.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART), AdapterDocQueryDeferredRequestErrorPortType.class);
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

    public DocQueryAcknowledgementType respondingGatewayCrossGatewayQuery(AdhocQueryRequest msg, AssertionType assertion, String errMsg) {
        log.debug("Begin respondingGatewayCrossGatewayQuery");
        DocQueryAcknowledgementType response = null;

        try
        {
            String url = oProxyHelper.getUrlLocalHomeCommunity(NhincConstants.ADAPTER_DOCUMENT_QUERY_DEFERRED_REQ_ERROR_SERVICE_NAME);
            AdapterDocQueryDeferredRequestErrorPortType port = getPort(url, WS_ADDRESSING_ACTION, assertion);

            if(msg == null)
            {
                log.error("Message was null");
            }
            else if(assertion == null)
            {
                log.error("AssertionType was null");
            }
            else if(port == null)
            {
                log.error("port was null");
            }
            else
            {
                AdapterDocumentQueryDeferredRequestErrorType request = new AdapterDocumentQueryDeferredRequestErrorType();
                request.setAdhocQueryRequest(msg);
                request.setAssertion(assertion);
                request.setErrorMsg(errMsg);

                response = (DocQueryAcknowledgementType)oProxyHelper.invokePort(port, AdapterDocQueryDeferredRequestErrorPortType.class, "respondingGatewayCrossGatewayQuery", request);
            }
        }
        catch (Exception ex)
        {
            log.error("Error calling respondingGatewayCrossGatewayQuery: " + ex.getMessage(), ex);
            response = new DocQueryAcknowledgementType();
            RegistryResponseType regResp = new RegistryResponseType();
            regResp.setStatus(NhincConstants.DOC_QUERY_DEFERRED_REQ_ACK_STATUS_MSG);
            response.setMessage(regResp);
        }

        log.debug("End respondingGatewayCrossGatewayQuery");
        return response;
    }

}
