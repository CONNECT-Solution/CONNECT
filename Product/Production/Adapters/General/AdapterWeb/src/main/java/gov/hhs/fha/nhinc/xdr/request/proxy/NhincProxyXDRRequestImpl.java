package gov.hhs.fha.nhinc.xdr.request.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import gov.hhs.fha.nhinc.nhincproxyxdrsecured.async.request.ProxyXDRSecuredRequestService;
import gov.hhs.fha.nhinc.nhincproxyxdrsecured.async.request.ProxyXDRSecuredRequestPortType;
import ihe.iti.xdr._2007.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetRequestType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import javax.xml.ws.BindingProvider;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;
import java.util.Map;
import java.util.StringTokenizer;

/**
 *
 * @author Neil Webb
 */
public class NhincProxyXDRRequestImpl
{
    private static Log log = null;
    private static ProxyXDRSecuredRequestService service;

    public NhincProxyXDRRequestImpl()
    {
        log = createLogger();
        service = createService();
    }

    public AcknowledgementType provideAndRegisterDocumentSetBRequest(RespondingGatewayProvideAndRegisterDocumentSetRequestType provideAndRegisterRequestRequest)
    {
        AcknowledgementType response = null;

        String url = getURL();
        if(NullChecker.isNotNullish(url))
        {
            try
            {
                ProxyXDRSecuredRequestPortType port = getPort(url);

                RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType provideAndRegisterRequestSecuredRequest = new RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType();
                provideAndRegisterRequestSecuredRequest.setProvideAndRegisterDocumentSetRequest(provideAndRegisterRequestRequest.getProvideAndRegisterDocumentSetRequest());
                provideAndRegisterRequestSecuredRequest.setNhinTargetSystem(provideAndRegisterRequestRequest.getNhinTargetSystem());

                AssertionType assertion = provideAndRegisterRequestRequest.getAssertion();
                setRequestContext(assertion, url, port);

                // TODO: Audit log

                // TODO: Policy check
				int retryCount = gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().getRetryAttempts();
		int retryDelay = gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().getRetryDelay();
        String exceptionText = gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().getExceptionText();
        javax.xml.ws.WebServiceException catchExp = null;
        if (retryCount > 0 && retryDelay > 0 && exceptionText != null && !exceptionText.equalsIgnoreCase("")) {
            int i = 1;
            while (i <= retryCount) {
                try {
                    response = port.provideAndRegisterDocumentSetBRequest(provideAndRegisterRequestSecuredRequest);
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
                            log.error("Thread Got Interrupted while waiting on ProxyXDRSecuredRequestService call :" + iEx);
                        } catch (IllegalArgumentException iaEx) {
                            log.error("Thread Got Interrupted while waiting on ProxyXDRSecuredRequestService call :" + iaEx);
                        }
                        retryDelay = retryDelay + retryDelay; //This is a requirement from Customer
                    } else {
                        log.error("Unable to call ProxyXDRSecuredRequestService Webservice due to  : " + e);
                        throw e;
                    }
                }
            }

            if (i > retryCount) {
                log.error("Unable to call ProxyXDRSecuredRequestService Webservice due to  : " + catchExp);
                throw catchExp;
            }

        } else {
            response = port.provideAndRegisterDocumentSetBRequest(provideAndRegisterRequestSecuredRequest);
        }
		    }
            catch (Exception ex)
            {
                log.error("Error in Unsecured NHIN Proxy for XDR Request: " + ex.getMessage(), ex);
                response = new AcknowledgementType();
                response.setMessage("Error");
            }
        }
        else
        {
            log.error("The URL for service: " + NhincConstants.NHINC_PROXY_XDR_REQUEST_SECURED_SERVICE_NAME + " is null");
            response = new AcknowledgementType();
            response.setMessage("Error");
        }

        return response;
    }

    protected void setRequestContext(AssertionType assertion, String url, ProxyXDRSecuredRequestPortType port)
    {
        SamlTokenCreator tokenCreator = new SamlTokenCreator();
        Map requestContext = tokenCreator.CreateRequestContext(assertion, url, NhincConstants.NHINC_PROXY_XDR_REQUEST_SERVICE_NAME);
        ((BindingProvider) port).getRequestContext().putAll(requestContext);
    }

    protected Log createLogger()
    {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }

    protected ProxyXDRSecuredRequestService createService()
    {
        return ((service != null) ? service : new ProxyXDRSecuredRequestService());
    }

    protected String getURL()
    {
        String url = "";

        try
        {
            url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.NHINC_PROXY_XDR_REQUEST_SECURED_SERVICE_NAME);
        }
        catch (Exception ex)
        {
            log.error(ex.getMessage(), ex);
        }

        return url;
    }

    protected ProxyXDRSecuredRequestPortType getPort(String url)
    {
        ProxyXDRSecuredRequestPortType port = service.getProxyXDRSecuredRequestPort();

        gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().initializePort((javax.xml.ws.BindingProvider) port, url);

        return port;
    }
}
