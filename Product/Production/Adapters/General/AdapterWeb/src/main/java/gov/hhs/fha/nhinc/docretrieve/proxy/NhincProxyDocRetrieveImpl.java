package gov.hhs.fha.nhinc.docretrieve.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;
import java.util.Map;
import javax.xml.ws.BindingProvider;
import gov.hhs.fha.nhinc.nhincproxydocretrievesecured.NhincProxyDocRetrieveSecured;
import gov.hhs.fha.nhinc.nhincproxydocretrievesecured.NhincProxyDocRetrieveSecuredPortType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import java.util.StringTokenizer;

/**
 *
 *
 * @author Neil Webb
 */
public class NhincProxyDocRetrieveImpl
{
    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(NhincProxyDocRetrieveImpl.class);
    private static NhincProxyDocRetrieveSecured service = new NhincProxyDocRetrieveSecured();

    public RetrieveDocumentSetResponseType respondingGatewayCrossGatewayRetrieve(gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayRetrieveRequestType respondingGatewayCrossGatewayRetrieveRequest)
    {
        RetrieveDocumentSetResponseType result = null;
        log.debug("Begin NhincProxyDocRetrieveImpl.respondingGatewayCrossGatewayRetrieve");

        try
        {
            // Get endpoint
            String url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.NHINC_PROXY_DOC_RETRIEVE_SECURED_SERVICE_NAME);
            NhincProxyDocRetrieveSecuredPortType port = getPort(url);

            // Set Assertion
            AssertionType assertIn = respondingGatewayCrossGatewayRetrieveRequest.getAssertion();
            SamlTokenCreator tokenCreator = new SamlTokenCreator();
            Map requestContext = tokenCreator.CreateRequestContext(assertIn, url, NhincConstants.DOC_QUERY_ACTION);
            ((BindingProvider) port).getRequestContext().putAll(requestContext);
            
            // Create body
            gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayRetrieveSecuredRequestType body = new gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayRetrieveSecuredRequestType();
            body.setRetrieveDocumentSetRequest(respondingGatewayCrossGatewayRetrieveRequest.getRetrieveDocumentSetRequest());
            body.setNhinTargetSystem(respondingGatewayCrossGatewayRetrieveRequest.getNhinTargetSystem());

            // Send message
            log.debug("Calling secure NHIN Proxy doc retrieve.");
            int retryCount = gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().getRetryAttempts();
			int retryDelay = gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().getRetryDelay();
        String exceptionText = gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().getExceptionText();
        javax.xml.ws.WebServiceException catchExp = null;
        if (retryCount > 0 && retryDelay > 0 && exceptionText != null && !exceptionText.equalsIgnoreCase("")) {
            int i = 1;
            while (i <= retryCount) {
                try {
                    result = port.respondingGatewayCrossGatewayRetrieve(body);
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
                            log.error("Thread Got Interrupted while waiting on NhincProxyDocRetrieveSecured call :" + iEx);
                        } catch (IllegalArgumentException iaEx) {
                            log.error("Thread Got Interrupted while waiting on NhincProxyDocRetrieveSecured call :" + iaEx);
                        }
                        retryDelay = retryDelay + retryDelay; //This is a requirement from Customer
                    } else {
                        log.error("Unable to call NhincProxyDocRetrieveSecured Webservice due to  : " + e);
                        throw e;
                    }
                }
            }

            if (i > retryCount) {
                log.error("Unable to call NhincProxyDocRetrieveSecured Webservice due to  : " + catchExp);
                throw catchExp;
            }

        } else {
            result = port.respondingGatewayCrossGatewayRetrieve(body);
        }
		
        }
        catch (Exception ex)
        {
            log.error("Error calling NHIN Proxy doc retrieve secured service: " + ex.getMessage(), ex);
        }

        log.debug("End NhincProxyDocRetrieveImpl.respondingGatewayCrossGatewayRetrieve");
        return result;
    }
    
    private NhincProxyDocRetrieveSecuredPortType getPort(String url) {
        NhincProxyDocRetrieveSecuredPortType port = service.getNhincProxyDocRetrieveSecuredPortSoap11();
        gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().initializePort((javax.xml.ws.BindingProvider) port, url);
        return port;
    }
}
