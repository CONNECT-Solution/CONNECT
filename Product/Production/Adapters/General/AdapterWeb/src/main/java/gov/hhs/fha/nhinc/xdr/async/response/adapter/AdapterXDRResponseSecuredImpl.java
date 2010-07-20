/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.xdr.async.response.adapter;

import gov.hhs.fha.nhinc.adapterxdrresponse.AdapterXDRResponsePortType;
import gov.hhs.fha.nhinc.adapterxdrresponse.AdapterXDRResponseService;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import javax.xml.ws.WebServiceContext;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.AdapterRegistryResponseType;
import java.util.StringTokenizer;

/**
 *
 * @author patlollav
 */
public class AdapterXDRResponseSecuredImpl {

    private static final Log log = LogFactory.getLog(AdapterXDRResponseSecuredImpl.class);
    private static final String ACK_SUCCESS_MESSAGE = "SUCCESS";
    private static AdapterXDRResponseService service = new AdapterXDRResponseService();

    public ihe.iti.xdr._2007.AcknowledgementType provideAndRegisterDocumentSetBResponse(RegistryResponseType body, WebServiceContext context) {
        getLogger().debug("Entering provideAndRegisterDocumentSetBResponse");

        // Log the Registry Response
        getLogger().debug("Registry Response " + body);

        String url = getUrl();
        AdapterXDRResponsePortType port = getPort(url);

        // Call AdapterComponent implementation to process the request.
        AssertionType assertion = SamlTokenExtractor.GetAssertion(context);

        AdapterRegistryResponseType unsecured = new AdapterRegistryResponseType();

        unsecured.setRegistryResponse(body);
        unsecured.setAssertion(assertion);
        ihe.iti.xdr._2007.AcknowledgementType ack = null;
		int retryCount = gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().getRetryAttempts();
		int retryDelay = gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().getRetryDelay();
        String exceptionText = gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().getExceptionText();
        javax.xml.ws.WebServiceException catchExp = null;
        if (retryCount > 0 && retryDelay > 0 && exceptionText != null && !exceptionText.equalsIgnoreCase("")) {
            int i = 1;
            while (i <= retryCount) {
                try {
                    ack = port.provideAndRegisterDocumentSetBResponse(unsecured);
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
                            log.error("Thread Got Interrupted while waiting on AdapterXDRResponseService call :" + iEx);
                        } catch (IllegalArgumentException iaEx) {
                            log.error("Thread Got Interrupted while waiting on AdapterXDRResponseService call :" + iaEx);
                        }
                        retryDelay = retryDelay + retryDelay; //This is a requirement from Customer
                    } else {
                        log.error("Unable to call AdapterXDRResponseService Webservice due to  : " + e);
                        throw e;
                    }
                }
            }

            if (i > retryCount) {
                log.error("Unable to call AdapterXDRResponseService Webservice due to  : " + catchExp);
                throw catchExp;
            }

        } else {
            ack = port.provideAndRegisterDocumentSetBResponse(unsecured);
        }
        getLogger().debug("Exiting provideAndRegisterDocumentSetBResponse");
        return ack;
    }

    protected Log getLogger(){
        return log;
    }

    protected String getUrl() {
        String url = null;

        try {
            url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.ADAPTER_XDR_RESPONSE_SERVICE_NAME);
        } catch (ConnectionManagerException ex) {
            getLogger().error("Error: Failed to retrieve url for service: " + NhincConstants.ADAPTER_XDR_RESPONSE_SERVICE_NAME, ex);
        }

        return url;
    }
    protected AdapterXDRResponsePortType getPort(String url) {
        AdapterXDRResponsePortType port = service.getAdapterXDRResponsePort();
        gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().initializePort((javax.xml.ws.BindingProvider) port, url);
        return port;
    }
}
