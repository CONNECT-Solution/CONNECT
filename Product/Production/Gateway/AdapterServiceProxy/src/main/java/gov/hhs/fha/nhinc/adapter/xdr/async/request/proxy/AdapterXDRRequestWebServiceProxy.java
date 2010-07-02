package gov.hhs.fha.nhinc.adapter.xdr.async.request.proxy;

import gov.hhs.fha.nhinc.adapterxdrrequestsecured.AdapterXDRRequestSecuredPortType;
import gov.hhs.fha.nhinc.adapterxdrrequestsecured.AdapterXDRRequestSecuredService;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;
import ihe.iti.xdr._2007.AcknowledgementType;
import java.util.Map;
import javax.xml.ws.BindingProvider;
import org.apache.commons.logging.Log;

/**
 *
 *
 * @author Visu Patlolla
 */
public class AdapterXDRRequestWebServiceProxy implements AdapterXDRRequestProxy
{
    private static org.apache.commons.logging.Log logger = org.apache.commons.logging.LogFactory.getLog(AdapterXDRRequestWebServiceProxy.class);

    private static AdapterXDRRequestSecuredService securedAdapterService = null;

    /**
     *
     * @param body
     * @param assertion
     * @return
     */
    public ihe.iti.xdr._2007.AcknowledgementType provideAndRegisterDocumentSetBRequest(ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType body, AssertionType assertion){
        
        getLogger().debug("Entering provideAndRegisterDocumentSetBRequest");

        String adapterXDRRequestSecuredEndPointURL = null;

        ihe.iti.xdr._2007.AcknowledgementType response = null;

        adapterXDRRequestSecuredEndPointURL = getAdapterXDRRequestSecuredUrl();

        if (NullChecker.isNotNullish(adapterXDRRequestSecuredEndPointURL)) {
            AdapterXDRRequestSecuredPortType port = getAdapterXDRRequestSecuredPort(adapterXDRRequestSecuredEndPointURL);

            setRequestContext(assertion, adapterXDRRequestSecuredEndPointURL, port);

            response = port.provideAndRegisterDocumentSetBRequest(body);

        } else {
            getLogger().error("The URL for service: " + NhincConstants.ADAPTER_XDR_REQUEST_SECURED_SERVICE_NAME + " is null");
            response = new AcknowledgementType();
            response.setMessage("ERROR: AdapterXDRRequestSecured EndPointURL is null");
        }

        getLogger().debug("Existing provideAndRegisterDocumentSetBRequest");

        return response;

    }

    protected void setRequestContext(AssertionType assertion, String adapterXDRRequestSecuredEndPointURL, AdapterXDRRequestSecuredPortType port)
    {
            SamlTokenCreator tokenCreator = new SamlTokenCreator();
            Map requestContext = tokenCreator.CreateRequestContext(assertion, adapterXDRRequestSecuredEndPointURL, NhincConstants.ADAPTER_XDRREQUEST_SECURED_ACTION);

        ((BindingProvider) port).getRequestContext().putAll(requestContext);
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
    protected AdapterXDRRequestSecuredPortType getAdapterXDRRequestSecuredPort(String url) {
        AdapterXDRRequestSecuredPortType port = getAdapterXDRRequestSecuredService().getAdapterXDRRequestSecuredPortSoap12();
        gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().initializePort((javax.xml.ws.BindingProvider) port, url);
        return port;
    }

    protected Log getLogger(){
        return logger;
    }

    /**
     *
     * @return
     */
    protected AdapterXDRRequestSecuredService getAdapterXDRRequestSecuredService(){
        if (securedAdapterService == null){
            return new AdapterXDRRequestSecuredService();
        }
        else{
            return securedAdapterService;
        }

    }

}
