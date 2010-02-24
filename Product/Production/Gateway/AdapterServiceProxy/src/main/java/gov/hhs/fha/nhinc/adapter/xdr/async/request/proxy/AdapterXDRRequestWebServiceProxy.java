package gov.hhs.fha.nhinc.adapter.xdr.async.request.proxy;

import gov.hhs.fha.nhinc.adapterxdrrequestsecured.AdapterXDRRequestSecuredPortType;
import gov.hhs.fha.nhinc.adapterxdrrequestsecured.AdapterXDRRequestSecuredService;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;
import java.util.Map;
import javax.xml.ws.BindingProvider;

/**
 *
 *
 * @author Visu Patlolla
 */
public class AdapterXDRRequestWebServiceProxy implements AdapterXDRRequestProxy
{
    private static org.apache.commons.logging.Log logger = org.apache.commons.logging.LogFactory.getLog(AdapterXDRRequestWebServiceProxy.class);

    private static AdapterXDRRequestSecuredService securedAdapterService = new AdapterXDRRequestSecuredService();

    public ihe.iti.xdr._2007.AcknowledgementType provideAndRegisterDocumentSetBRequest(ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType body, AssertionType assertion){
        logger.debug("Entering provideAndRegisterDocumentSetBRequest");

        String url = "";
        ihe.iti.xdr._2007.AcknowledgementType response = null;

        url = getAdapterXDRRequestSecuredUrl();

        if (NullChecker.isNotNullish(url)) {
            AdapterXDRRequestSecuredPortType port = getPort(url);

            SamlTokenCreator tokenCreator = new SamlTokenCreator();
            Map requestContext = tokenCreator.CreateRequestContext(assertion, url, NhincConstants.ADAPTER_XDRREQUEST_SECURED_ACTION);

            ((BindingProvider) port).getRequestContext().putAll(requestContext);

            response = port.provideAndRegisterDocumentSetBRequest(body);

        } else {
            logger.error("The URL for service: " + NhincConstants.ADAPTER_XDR_REQUEST_SECURED_SERVICE_NAME + " is null");
        }

        logger.debug("Existing provideAndRegisterDocumentSetBRequest");

        return response;

    }

    /**
     *
     * @return
     */
    protected String getAdapterXDRRequestSecuredUrl() {
        String url = null;

        try {
            url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.ADAPTER_XDR_REQUEST_SECURED_SERVICE_NAME);

        } catch (ConnectionManagerException ex) {
            logger.error("Error: Failed to retrieve url for service: " + NhincConstants.ADAPTER_XDR_REQUEST_SECURED_SERVICE_NAME, ex);
        }

        return url;
    }

    /**
     *
     * @param url
     * @return
     */
    private AdapterXDRRequestSecuredPortType getPort(String url) {
        AdapterXDRRequestSecuredPortType port = securedAdapterService.getAdapterXDRRequestSecuredPortSoap12();

        logger.info("Setting endpoint address to Adapter XDR Secured Service to " + url);
        ((BindingProvider) port).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);

        return port;
    }

    
}
