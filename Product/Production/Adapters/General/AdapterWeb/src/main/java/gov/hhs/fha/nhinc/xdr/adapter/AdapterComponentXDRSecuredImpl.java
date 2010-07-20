package gov.hhs.fha.nhinc.xdr.adapter;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.xml.ws.WebServiceContext;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.AdapterProvideAndRegisterDocumentSetRequestType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import gov.hhs.fha.nhinc.nhincadapterxdr.AdapterXDRPortType;
import gov.hhs.fha.nhinc.nhincadapterxdr.AdapterXDRService;
import java.util.StringTokenizer;


/**
 *
 * @author dunnek
 */
public class AdapterComponentXDRSecuredImpl {
    private static Log log = null;
    private static AdapterXDRService service = null;


    public AdapterComponentXDRSecuredImpl()
    {
        log = createLogger();
        service = createService();
    }
    public RegistryResponseType provideAndRegisterDocumentSetb(ProvideAndRegisterDocumentSetRequestType body, WebServiceContext context) {
        log.debug("begin provideAndRegisterDocumentSetb()");
        AssertionType assertion = SamlTokenExtractor.GetAssertion(context);
        AdapterProvideAndRegisterDocumentSetRequestType unsecured = new AdapterProvideAndRegisterDocumentSetRequestType();
        String url = "";
        AdapterXDRPortType port = null;
        RegistryResponseType result = null;
        XDRHelper helper = new XDRHelper();

        url = getUrl();
        port = getPort(url);
        
        unsecured.setAssertion(assertion);
        unsecured.setProvideAndRegisterDocumentSetRequest(body);
				
		int retryCount = gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().getRetryAttempts();
		int retryDelay = gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().getRetryDelay();
        String exceptionText = gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().getExceptionText();
        javax.xml.ws.WebServiceException catchExp = null;
        if (retryCount > 0 && retryDelay > 0 && exceptionText != null && !exceptionText.equalsIgnoreCase("")) {
            int i = 1;
            while (i <= retryCount) {
                try {
                    result = port.provideAndRegisterDocumentSetb(unsecured);
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
                            log.error("Thread Got Interrupted while waiting on provideAndRegisterDocumentSetb call :" + iEx);
                        } catch (IllegalArgumentException iaEx) {
                            log.error("Thread Got Interrupted while waiting on provideAndRegisterDocumentSetb call :" + iaEx);
                        }
                        retryDelay = retryDelay + retryDelay; //This is a requirement from Customer
                    } else {
                        log.error("Unable to call provideAndRegisterDocumentSetb Webservice due to  : " + e);
                        throw e;
                    }
                }
            }

            if (i > retryCount) {
                log.error("Unable to call provideAndRegisterDocumentSetb Webservice due to  : " + catchExp);
                throw catchExp;
            }

        } else {
            result = port.provideAndRegisterDocumentSetb(unsecured);
        }
        log.debug("end provideAndRegisterDocumentSetb()");
		return result;
    }

    protected Log createLogger()
    {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }

    protected AdapterXDRService createService()
    {
        return new AdapterXDRService();
    }
    protected String getUrl() {
        String url = null;
 
        try {
            url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.ADAPTER_XDR_SERVICE_NAME);
        } catch (ConnectionManagerException ex) {
            log.error("Error: Failed to retrieve url for service: " + NhincConstants.ADAPTER_XDR_SERVICE_NAME);
            log.error(ex.getMessage());
        }
 

        return url;
    }
    protected AdapterXDRPortType getPort(String url) {

        AdapterXDRPortType port = service.getAdapterXDRPort();

        gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().initializePort((javax.xml.ws.BindingProvider) port, url);

        return port;
    }
}
