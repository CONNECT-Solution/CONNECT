/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.adapterdocumentregistry.proxy;

import gov.hhs.fha.nhinc.adapterdocquery.AdapterDocQueryPortType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import javax.xml.ws.BindingProvider;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayQueryRequestType;
import java.util.StringTokenizer;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;

/**
 *
 * @author rayj
 */
public class AdapterDocumentRegistryWebServiceProxy implements AdapterDocumentRegistryProxy {

    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(AdapterDocumentRegistryWebServiceProxy.class);
    private static gov.hhs.fha.nhinc.adapterdocquery.AdapterDocQuery adapterDocumentRegistryService = new gov.hhs.fha.nhinc.adapterdocquery.AdapterDocQuery();

    public AdhocQueryResponse queryForDocument(AdhocQueryRequest adhocQuery, AssertionType assertion, NhinTargetSystemType target) throws Exception {
        AdapterDocQueryPortType port = getPort(target);

        RespondingGatewayCrossGatewayQueryRequestType request = new RespondingGatewayCrossGatewayQueryRequestType();
        request.setAssertion(assertion);
        request.setAdhocQueryRequest(adhocQuery);
        AdhocQueryResponse result = null;
				
		int retryCount = gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().getRetryAttempts();
		int retryDelay = gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().getRetryDelay();
        String exceptionText = gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().getExceptionText();
        javax.xml.ws.WebServiceException catchExp = null;
        if (retryCount > 0 && retryDelay > 0 && exceptionText != null && !exceptionText.equalsIgnoreCase("")) {
            int i = 1;
            while (i <= retryCount) {
                try {
                    port.respondingGatewayCrossGatewayQuery(request);
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
                            log.error("Thread Got Interrupted while waiting on adapterDocumentRegistryService call :" + iEx);
                        } catch (IllegalArgumentException iaEx) {
                            log.error("Thread Got Interrupted while waiting on adapterDocumentRegistryService call :" + iaEx);
                        }
                        retryDelay = retryDelay + retryDelay; //This is a requirement from Customer
                    } else {
                        log.error("Unable to call adapterDocumentRegistryService Webservice due to  : " + e);
                        throw e;
                    }
                }
            }

            if (i > retryCount) {
                log.error("Unable to call adapterDocumentRegistryService Webservice due to  : " + catchExp);
                throw catchExp;
            }

        } else {
            port.respondingGatewayCrossGatewayQuery(request);
        }
        return result;
    }

    private gov.hhs.fha.nhinc.adapterdocquery.AdapterDocQueryPortType getPort(NhinTargetSystemType target) throws ConnectionManagerException {
        String serviceName = "adapterdocquery";  //todo: move this to const in nhinclib
        String url = getUrl(target, serviceName);
        return getPort(url);
    }

    private gov.hhs.fha.nhinc.adapterdocquery.AdapterDocQueryPortType getPort(String url) {
        gov.hhs.fha.nhinc.adapterdocquery.AdapterDocQueryPortType port = adapterDocumentRegistryService.getAdapterDocQueryPortSoap11();

        gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().initializePort((javax.xml.ws.BindingProvider) port, url);

        return port;
    }

    private String getUrl(NhinTargetSystemType target, String serviceName) throws ConnectionManagerException {
        String url = null;
        url = ConnectionManagerCache.getEndpontURLFromNhinTarget(target, serviceName);
        if (NullChecker.isNullish(url)) {
            url = ConnectionManagerCache.getLocalEndpointURLByServiceName(serviceName);
        }
        return url;
    }
}
