/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.xdr;
import gov.hhs.fha.nhinc.nhincproxyxdr.ProxyXDRPortType;
import gov.hhs.fha.nhinc.nhincproxyxdr.ProxyXDRService;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetRequestType;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractorHelper;
import java.util.StringTokenizer;
/**
 *
 * @author dunnek
 */
public class NhinXDRImpl {
    private static Log log = LogFactory.getLog(NhinXDRImpl.class);
    private static final String SERVICE_NAME = "mockxdr";
    public RegistryResponseType documentRepositoryProvideAndRegisterDocumentSetB(
                ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType body,
                javax.xml.ws.WebServiceContext context
                )
    {
        RegistryResponseType response =null;
        RespondingGatewayProvideAndRegisterDocumentSetRequestType request = null;

        request = new RespondingGatewayProvideAndRegisterDocumentSetRequestType();

        request.setProvideAndRegisterDocumentSetRequest(body);
        request.setAssertion(SamlTokenExtractor.GetAssertion(context));

        String homeCommunityId = SamlTokenExtractorHelper.getHomeCommunityId();
        if (NullChecker.isNotNullish(homeCommunityId)) {
            ProxyXDRService service = new ProxyXDRService();
            ProxyXDRPortType port = service.getProxyXDRPort();
			gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().initializePort((javax.xml.ws.BindingProvider) port,SamlTokenExtractorHelper.getEndpointURL(homeCommunityId, SERVICE_NAME));
            
		int retryCount = gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().getRetryAttempts();
		int retryDelay = gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().getRetryDelay();
        String exceptionText = gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().getExceptionText();
        javax.xml.ws.WebServiceException catchExp = null;
        if (retryCount > 0 && retryDelay > 0 && exceptionText != null && !exceptionText.equalsIgnoreCase("")) {
            int i = 1;
            while (i <= retryCount) {
                try {
                    response = port.provideAndRegisterDocumentSetB(request);
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
                            log.error("Thread Got Interrupted while waiting on mockxdr call :" + iEx);
                        } catch (IllegalArgumentException iaEx) {
                            log.error("Thread Got Interrupted while waiting on mockxdr call :" + iaEx);
                        }
                        retryDelay = retryDelay + retryDelay; //This is a requirement from Customer
                    } else {
                        log.error("Unable to call mockxdr Webservice due to  : " + e);
                        throw e;
                    }
                }
            }

            if (i > retryCount) {
                log.error("Unable to call mockxdr Webservice due to  : " + catchExp);
                throw catchExp;
            }

        } else {
            response = port.provideAndRegisterDocumentSetB(request);
        }
		
        } else {
           response = null;
        }
        return response;
    }
}
