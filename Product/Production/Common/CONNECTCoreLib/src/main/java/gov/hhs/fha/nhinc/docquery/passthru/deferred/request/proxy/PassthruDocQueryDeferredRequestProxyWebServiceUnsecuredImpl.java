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

package gov.hhs.fha.nhinc.docquery.passthru.deferred.request.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayQueryRequestType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhincproxydocquerydeferredrequest.NhincProxyDocQueryDeferredRequestPortType;
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
 * @author patlollav
 */
public class PassthruDocQueryDeferredRequestProxyWebServiceUnsecuredImpl implements PassthruDocQueryDeferredRequestProxy {
    //Logger
    private static final Log logger = LogFactory.getLog(PassthruDocQueryDeferredRequestProxyWebServiceUnsecuredImpl.class);

    private static Service cachedService = null;
    private static final String NAMESPACE_URI = "urn:gov:hhs:fha:nhinc:nhincproxydocquerydeferredrequest";
    private static final String SERVICE_LOCAL_PART = "NhincProxyDocQueryDeferredRequest";
    private static final String PORT_LOCAL_PART = "NhincProxyDocQueryDeferredRequestPortSoap";
    private static final String WSDL_FILE = "NhincProxyDocQueryDeferredRequest.wsdl";
    private static final String WS_ADDRESSING_ACTION = "urn:gov:hhs:fha:nhinc:nhincproxydocquerydeferredrequest:CrossGatewayQueryRequest";
    private static final String METHOD_NAME = "crossGatewayQueryRequest";
    
    private WebServiceProxyHelper oProxyHelper = new WebServiceProxyHelper();


    protected Log getLogger()
    {
        return logger;
    }

    protected WebServiceProxyHelper getWebServiceProxyHelper()
    {
        return oProxyHelper;
    }

    /**
     * This method retrieves and initializes the port.
     *
     * @param url The URL for the web service.
     * @return The port object for the web service.
     */
    protected NhincProxyDocQueryDeferredRequestPortType getPort(String url, String wsAddressingAction, AssertionType assertion)
    {
        NhincProxyDocQueryDeferredRequestPortType port = null;
        Service service = getService();
        if (service != null)
        {
            getLogger().debug("Obtained service - creating port.");

            port = service.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART), NhincProxyDocQueryDeferredRequestPortType.class);
            getWebServiceProxyHelper().initializeUnsecurePort((javax.xml.ws.BindingProvider) port, url, wsAddressingAction, assertion);
        }
        else
        {
            getLogger().error("Unable to obtain serivce - no port created.");
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
                cachedService = getWebServiceProxyHelper().createService(WSDL_FILE, NAMESPACE_URI, SERVICE_LOCAL_PART);
            }
            catch (Throwable t)
            {
                getLogger().error("Error creating service: " + t.getMessage(), t);
            }
        }
        return cachedService;
    }

    public DocQueryAcknowledgementType crossGatewayQueryRequest(AdhocQueryRequest msg, AssertionType assertion, NhinTargetSystemType target) {
        getLogger().debug("Begin respondingGatewayCrossGatewayQuery");
        DocQueryAcknowledgementType response = null;

        try
        {
            String url = getWebServiceProxyHelper().getUrlLocalHomeCommunity(NhincConstants.PASSTHRU_DOCUMENT_QUERY_DEFERRED_REQ_SERVICE_NAME);
            
            NhincProxyDocQueryDeferredRequestPortType port = getPort(url, WS_ADDRESSING_ACTION, assertion);

            if(msg == null)
            {
                getLogger().error("Message is null");
            }
            else if(assertion == null)
            {
                getLogger().error("AssertionType is null");
            }
            else if(target == null)
            {
                getLogger().error("target is null");
            }
            else if(port == null)
            {
                getLogger().error("port was null");
            }
            else
            {
                RespondingGatewayCrossGatewayQueryRequestType request = new RespondingGatewayCrossGatewayQueryRequestType();
                request.setAdhocQueryRequest(msg);
                request.setAssertion(assertion);
                request.setNhinTargetSystem(target);

                response = (DocQueryAcknowledgementType)getWebServiceProxyHelper().invokePort(port, NhincProxyDocQueryDeferredRequestPortType.class, METHOD_NAME, request);
            }
        }
        catch (Exception ex)
        {
            getLogger().error("Error calling respondingGatewayCrossGatewayQuery: " + ex.getMessage(), ex);
            response = new DocQueryAcknowledgementType();
            RegistryResponseType regResp = new RegistryResponseType();
            regResp.setStatus(NhincConstants.DOC_QUERY_DEFERRED_REQ_ACK_FAILURE_STATUS_MSG);
            response.setMessage(regResp);
        }

        getLogger().debug("End respondingGatewayCrossGatewayQuery");
        return response;
    }

}
