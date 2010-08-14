/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docquery.passthru.deferred.request.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayQuerySecuredRequestType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhincproxydocquerydeferredrequestsecured.NhincProxyDocQueryDeferredRequestSecuredPortType;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import gov.hhs.healthit.nhin.DocQueryAcknowledgementType;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author patlollav
 */
public class PassthruDocQueryDeferredRequestProxyWebServiceSecuredImpl implements PassthruDocQueryDeferredRequestProxy {
    //Logger
    private static final Log logger = LogFactory.getLog(PassthruDocQueryDeferredRequestProxyWebServiceSecuredImpl.class);

    private static Service cachedService = null;
    private static final String NAMESPACE_URI = "urn:gov:hhs:fha:nhinc:nhincproxydocquerydeferredrequestsecured";
    private static final String SERVICE_LOCAL_PART = "NhincProxyDocQueryDeferredRequestSecured";
    private static final String PORT_LOCAL_PART = "NhincProxyDocQueryDeferredRequestSecuredPortSoap";
    private static final String WSDL_FILE = "NhincProxyDocQueryDeferredRequestSecured.wsdl";
    private static final String WS_ADDRESSING_ACTION = "urn:gov:hhs:fha:nhinc:nhincproxydocquerydeferredrequestsecured:CrossGatewayQueryRequestMessage";
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
    protected NhincProxyDocQueryDeferredRequestSecuredPortType getPort(String url, String serviceAction, String wsAddressingAction, AssertionType assertion)
    {
        NhincProxyDocQueryDeferredRequestSecuredPortType port = null;

        Service service = getService();
        if (service != null)
        {
            getLogger().debug("Obtained service - creating port.");

            port = service.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART), NhincProxyDocQueryDeferredRequestSecuredPortType.class);
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
        getLogger().debug("Begin crossGatewayQueryRequest");
        DocQueryAcknowledgementType response = null;

        try
        {
            String url = getWebServiceProxyHelper().getUrlLocalHomeCommunity(NhincConstants.PASSTHRU_DOCUMENT_QUERY_DEFERRED_REQ_SECURED_SERVICE_NAME);

            NhincProxyDocQueryDeferredRequestSecuredPortType port = getPort(url, NhincConstants.DOC_QUERY_ACTION, WS_ADDRESSING_ACTION, assertion);

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
                getLogger().error("NhinTargetCommunitiesType is null");
            }
            else if(port == null)
            {
                getLogger().error("port was null");
            }
            else
            {
                RespondingGatewayCrossGatewayQuerySecuredRequestType request = new RespondingGatewayCrossGatewayQuerySecuredRequestType();
                request.setAdhocQueryRequest(msg);

                response = (DocQueryAcknowledgementType)getWebServiceProxyHelper().invokePort(port, NhincProxyDocQueryDeferredRequestSecuredPortType.class, METHOD_NAME, msg);
            }
        }
        catch (Exception ex)
        {
            getLogger().error("Error calling crossGatewayQueryRequest: " + ex.getMessage(), ex);
        }

        getLogger().debug("End crossGatewayQueryRequest");
        return response;
    }
}
