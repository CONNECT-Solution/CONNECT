package gov.hhs.fha.nhinc.adapter.xdr.async.response.proxy;

import gov.hhs.fha.nhinc.adapterxdrresponsesecured.AdapterXDRResponseSecuredPortType;
import gov.hhs.fha.nhinc.adapterxdrresponsesecured.AdapterXDRResponseSecuredService;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;
import ihe.iti.xdr._2007.AcknowledgementType;
import java.util.Map;
import java.util.StringTokenizer;
import javax.xml.ws.BindingProvider;
import org.apache.commons.logging.Log;

/**
 *
 *
 * @author Visu Patlolla
 */
public class AdapterXDRResponseWebServiceProxy implements AdapterXDRResponseProxy
{
    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(AdapterXDRResponseWebServiceProxy.class);

    private static AdapterXDRResponseSecuredService securedAdapterService = null;

    /**
     *
     * @param body
     * @param assertion
     * @return
     */
    public ihe.iti.xdr._2007.AcknowledgementType provideAndRegisterDocumentSetBResponse(oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType body, AssertionType assertion){
        
        getLogger().debug("Entering provideAndRegisterDocumentSetBResponse");

        String adapterXDRResponseSecuredEndPointURL = null;

        ihe.iti.xdr._2007.AcknowledgementType response = null;

        adapterXDRResponseSecuredEndPointURL = getAdapterXDRResponseSecuredUrl();

        if (NullChecker.isNotNullish(adapterXDRResponseSecuredEndPointURL)) {
            AdapterXDRResponseSecuredPortType port = getAdapterXDRResponseSecuredPort(adapterXDRResponseSecuredEndPointURL);

            setRequestContext(assertion, adapterXDRResponseSecuredEndPointURL, port);
         
		int retryCount = gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().getRetryAttempts();
		int retryDelay = gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().getRetryDelay();
        String exceptionText = gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().getExceptionText();
        javax.xml.ws.WebServiceException catchExp = null;
        if (retryCount > 0 && retryDelay > 0 && exceptionText != null && !exceptionText.equalsIgnoreCase("")) {
            int i = 1;
            while (i <= retryCount) {
                try {
                    response = port.provideAndRegisterDocumentSetBResponse(body);
                    break;
                } catch (javax.xml.ws.WebServiceException e) {
                    catchExp = e;
                    int flag = 0;
                    StringTokenizer st = new StringTokenizer(exceptionText, ",");
                    while (st.hasMoreTokens()) {
                        if (e.getMessage().contains(st.nextToken())) {
                            flag = 1;
                        }
                    }
                    if (flag == 1) {
                        log.warn("Exception calling ... web service: " + e.getMessage());
                        System.out.println("retrying the connection for attempt [ " + i + " ] after [ " + retryDelay + " ] seconds");
                        log.info("retrying attempt [ " + i + " ] the connection after [ " + retryDelay + " ] seconds");
                        i++;
                        try {
                            Thread.sleep(retryDelay);
                        } catch (InterruptedException iEx) {
                            log.error("Thread Got Interrupted while waiting on AdapterXDRResponseSecuredService call :" + iEx);
                        } catch (IllegalArgumentException iaEx) {
                            log.error("Thread Got Interrupted while waiting on AdapterXDRResponseSecuredService call :" + iaEx);
                        }
                        retryDelay = retryDelay + retryDelay; //This is a requirement from Customer
                    } else {
                        log.error("Unable to call AdapterXDRResponseSecuredService Webservice due to  : " + e);
                        throw e;
                    }
                }
            }

            if (i > retryCount) {
                log.error("Unable to call AdapterXDRResponseSecuredService Webservice due to  : " + catchExp);
                throw catchExp;
            }

        } else {
            response = port.provideAndRegisterDocumentSetBResponse(body);
        }
		
        } else {
            getLogger().error("The URL for service: " + NhincConstants.ADAPTER_XDR_RESPONSE_SECURED_SERVICE_NAME + " is null");
            response = new AcknowledgementType();
            response.setMessage("ERROR: AdapterXDRResponseSecured EndPointURL is null");
        }

        getLogger().debug("Existing provideAndRegisterDocumentSetBResponse");

        return response;

    }

    protected void setRequestContext(AssertionType assertion, String adapterXDRResponseSecuredEndPointURL, AdapterXDRResponseSecuredPortType port)
    {
            SamlTokenCreator tokenCreator = new SamlTokenCreator();
            Map requestContext = tokenCreator.CreateRequestContext(assertion, adapterXDRResponseSecuredEndPointURL, NhincConstants.ADAPTER_XDRRESPONSE_SECURED_ACTION);

        ((BindingProvider) port).getRequestContext().putAll(requestContext);
    }

    /**
     *
     * @return
     */
    protected String getAdapterXDRResponseSecuredUrl() {
        String url = null;

        try {
            url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.ADAPTER_XDR_RESPONSE_SECURED_SERVICE_NAME);

        } catch (ConnectionManagerException ex) {
            log.error("Error: Failed to retrieve url for service: " + NhincConstants.ADAPTER_XDR_RESPONSE_SECURED_SERVICE_NAME, ex);
        }

        return url;
    }

    /**
     *
     * @param url
     * @return
     */
    protected AdapterXDRResponseSecuredPortType getAdapterXDRResponseSecuredPort(String url) {
        AdapterXDRResponseSecuredPortType port = getAdapterXDRResponseSecuredService().getAdapterXDRResponseSecuredPortSoap12();
        gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().initializePort((javax.xml.ws.BindingProvider) port, url);
        return port;
    }

    protected Log getLogger(){
        return log;
    }

    /**
     *
     * @return
     */
    protected AdapterXDRResponseSecuredService getAdapterXDRResponseSecuredService(){
        if (securedAdapterService == null){
            return new AdapterXDRResponseSecuredService();
        }
        else{
            return securedAdapterService;
        }

    }

}
