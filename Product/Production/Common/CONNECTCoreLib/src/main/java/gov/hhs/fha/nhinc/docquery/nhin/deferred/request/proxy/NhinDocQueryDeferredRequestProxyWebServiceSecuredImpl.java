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

package gov.hhs.fha.nhinc.docquery.nhin.deferred.request.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import gov.hhs.healthit.nhin.DocQueryAcknowledgementType;
import ihe.iti.xds_b._2007.RespondingGatewayQueryDeferredRequestPortType;
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
public class NhinDocQueryDeferredRequestProxyWebServiceSecuredImpl implements NhinDocQueryDeferredRequestProxy {

    //Logger
    private static final Log logger = LogFactory.getLog(NhinDocQueryDeferredRequestProxyWebServiceSecuredImpl.class);

    private static Service cachedService = null;
    private static final String NAMESPACE_URI = "urn:ihe:iti:xds-b:2007";
    private static final String SERVICE_LOCAL_PART = "RespondingGateway_QueryDeferredRequest_Service";
    private static final String PORT_LOCAL_PART = "RespondingGateway_QueryDeferredRequest_Port_Soap";
    private static final String WSDL_FILE = "NhinDocQueryDeferredRequest.wsdl";
    private static final String WS_ADDRESSING_ACTION = "urn:ihe:iti:2007:CrossGatewayQuery";
    private WebServiceProxyHelper oProxyHelper = new WebServiceProxyHelper();


    protected Log getLogger(){
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
    protected RespondingGatewayQueryDeferredRequestPortType getPort(String url, String serviceAction, String wsAddressingAction, AssertionType assertion)
    {
        RespondingGatewayQueryDeferredRequestPortType port = null;
        Service service = getService();
        if (service != null)
        {
            getLogger().debug("Obtained service - creating port.");

            port = service.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART), RespondingGatewayQueryDeferredRequestPortType.class);
            getWebServiceProxyHelper().initializeSecurePort((javax.xml.ws.BindingProvider) port, url, serviceAction, wsAddressingAction, assertion);
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
                cachedService = oProxyHelper.createService(WSDL_FILE, NAMESPACE_URI, SERVICE_LOCAL_PART);
            }
            catch (Throwable t)
            {
                getLogger().error("Error creating service: " + t.getMessage(), t);
            }
        }
        return cachedService;
    }

    public DocQueryAcknowledgementType respondingGatewayCrossGatewayQuery(AdhocQueryRequest msg, AssertionType assertion, NhinTargetSystemType target) {
        getLogger().debug("Begin respondingGatewayCrossGatewayQuery");

        DocQueryAcknowledgementType response = null;

        try
        {
            String url = getWebServiceProxyHelper().getUrlFromTargetSystem(target, NhincConstants.NHIN_DOCUMENT_QUERY_DEFERRED_REQ_SERVICE_NAME);
            RespondingGatewayQueryDeferredRequestPortType port = getPort(url, NhincConstants.DOC_QUERY_ACTION, WS_ADDRESSING_ACTION, assertion);

            if(msg == null)
            {
                getLogger().error("Message was null");
            }
            else if(assertion == null)
            {
                getLogger().error("AssertionType was null");
            }
            else if(port == null)
            {
                getLogger().error("port was null");
            }
            else
            {
                response = (DocQueryAcknowledgementType)getWebServiceProxyHelper().invokePort(port, RespondingGatewayQueryDeferredRequestPortType.class, "respondingGatewayCrossGatewayQuery", msg);
            }
        }
        catch (Exception ex)
        {
            getLogger().error("Error calling respondingGatewayCrossGatewayQuery: " + ex.getMessage(), ex);
            response = new DocQueryAcknowledgementType();
            RegistryResponseType regResp = new RegistryResponseType();
            regResp.setStatus(NhincConstants.DOC_QUERY_DEFERRED_RESP_ACK_STATUS_MSG);
            response.setMessage(regResp);
        }

        getLogger().debug("End respondingGatewayCrossGatewayQuery");
        return response;
    }

}
