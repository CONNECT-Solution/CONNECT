/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.patientdiscovery;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.PRPAIN201306UV02;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractorHelper;
import javax.xml.ws.WebServiceContext;
import org.hl7.v3.ProxyPRPAIN201305UVProxyRequestType;
import gov.hhs.fha.nhinc.nhincproxypatientdiscovery.NhincProxyPatientDiscovery;
import gov.hhs.fha.nhinc.nhincproxypatientdiscovery.NhincProxyPatientDiscoveryPortType;
import java.util.StringTokenizer;

/**
 *
 * @author dunnek
 */
public class NhinPatientDiscoveryImpl {

    private static Log log = LogFactory.getLog(NhinPatientDiscoveryImpl.class);
    private static final String SERVICE_NAME = "mockpatientdiscovery";

    public org.hl7.v3.PRPAIN201306UV02 proxyPRPAIN201305UV(org.hl7.v3.PRPAIN201305UV02 body, WebServiceContext context) {
        PRPAIN201306UV02 response = null;
        ProxyPRPAIN201305UVProxyRequestType request = new ProxyPRPAIN201305UVProxyRequestType();

        request.setPRPAIN201305UV02(body);
        request.setAssertion(SamlTokenExtractor.GetAssertion(context));

        String homeCommunityId = null;
        if (body != null &&
                NullChecker.isNotNullish(body.getReceiver()) &&
                body.getReceiver().get(0) != null &&
                body.getReceiver().get(0).getDevice() != null &&
                body.getReceiver().get(0).getDevice().getAsAgent() != null &&
                body.getReceiver().get(0).getDevice().getAsAgent().getValue() != null &&
                body.getReceiver().get(0).getDevice().getAsAgent().getValue().getRepresentedOrganization() != null &&
                body.getReceiver().get(0).getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue() != null &&
                NullChecker.isNotNullish(body.getReceiver().get(0).getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId()) &&
                body.getReceiver().get(0).getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0) != null &&
                NullChecker.isNotNullish(body.getReceiver().get(0).getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0).getRoot())) {
            homeCommunityId = body.getReceiver().get(0).getDevice().getAsAgent().getValue().getRepresentedOrganization().getValue().getId().get(0).getRoot();
        } else {
            homeCommunityId = SamlTokenExtractorHelper.getHomeCommunityId();
        }

        if (NullChecker.isNotNullish(homeCommunityId)) {
            NhincProxyPatientDiscovery service = new NhincProxyPatientDiscovery();
            NhincProxyPatientDiscoveryPortType port = service.getNhincProxyPatientDiscoveryPort();
			gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().initializePort((javax.xml.ws.BindingProvider) port, SamlTokenExtractorHelper.getEndpointURL(homeCommunityId, SERVICE_NAME));
            
		int retryCount = gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().getRetryAttempts();
		int retryDelay = gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().getRetryDelay();
        String exceptionText = gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().getExceptionText();
        javax.xml.ws.WebServiceException catchExp = null;
        if (retryCount > 0 && retryDelay > 0 && exceptionText != null && !exceptionText.equalsIgnoreCase("")) {
            int i = 1;
            while (i <= retryCount) {
                try {
                    response = port.proxyPRPAIN201305UV(request);
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
                            log.error("Thread Got Interrupted while waiting on mockpatientdiscovery call :" + iEx);
                        } catch (IllegalArgumentException iaEx) {
                            log.error("Thread Got Interrupted while waiting on mockpatientdiscovery call :" + iaEx);
                        }
                        retryDelay = retryDelay + retryDelay; //This is a requirement from Customer
                    } else {
                        log.error("Unable to call mockpatientdiscovery Webservice due to  : " + e);
                        throw e;
                    }
                }
            }
            if (i > retryCount) {
                log.error("Unable to call mockpatientdiscovery Webservice due to  : " + catchExp);
                throw catchExp;
            }
        } else {
            response = port.proxyPRPAIN201305UV(request);
        }
		
        } else {
            response = null;
        }
        return response;
    }
}
