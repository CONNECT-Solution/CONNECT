/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.hiemadapter.proxy.subscribe;

import com.sun.xml.ws.developer.WSBindingProvider;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.adaptersubscriptionmanagement.AdapterNotificationProducer;
import gov.hhs.fha.nhinc.adaptersubscriptionmanagement.AdapterNotificationProducerPortType;
import gov.hhs.fha.nhinc.adaptersubscriptionmanagement.AdapterSubscriptionManager;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.SubscribeRequestType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.hiem.dte.marshallers.SubscribeResponseMarshaller;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oasis_open.docs.wsn.b_2.SubscribeResponse;
import org.w3c.dom.Element;
import org.oasis_open.docs.wsn.b_2.Subscribe;
import gov.hhs.fha.nhinc.hiem.dte.marshallers.WsntSubscribeMarshaller;
import java.util.StringTokenizer;

/**
 *
 * @author Jon Hoppesch
 */
public class HiemSubscribeAdapterWebServiceProxy implements HiemSubscribeAdapterProxy {
    //TODO: consider how to handle the exceptions.  Is there a better choice than relying on "runtime exception"

    private static Log log = LogFactory.getLog(HiemSubscribeAdapterWebServiceProxy.class);
    static AdapterNotificationProducer adapterSubscribeService = new AdapterNotificationProducer();
    static AdapterSubscriptionManager adapterUnsubscribeService = new AdapterSubscriptionManager();

    public Element subscribe(Element subscribeElement, AssertionType assertion, NhinTargetSystemType target) throws Exception {
        Element responseElement = null;
        SubscribeResponse response = null;
        AdapterNotificationProducerPortType port = getPort(target);

        WsntSubscribeMarshaller subscribeMarshaller = new WsntSubscribeMarshaller();
        Subscribe subscribe = subscribeMarshaller.unmarshalUnsubscribeRequest(subscribeElement);

        SubscribeRequestType adapterSubcribeRequest = new SubscribeRequestType();
        adapterSubcribeRequest.setSubscribe(subscribe);
        adapterSubcribeRequest.setAssertion(assertion);
        
		int retryCount = gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().getRetryAttempts();
		int retryDelay = gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().getRetryDelay();
        String exceptionText = gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().getExceptionText();
        javax.xml.ws.WebServiceException catchExp = null;
        if (retryCount > 0 && retryDelay > 0 && exceptionText != null && !exceptionText.equalsIgnoreCase("")) {
            int i = 1;
            while (i <= retryCount) {
                try {
                    response = port.subscribe(adapterSubcribeRequest);
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
                            log.error("Thread Got Interrupted while waiting on AdapterSubscriptionManager call :" + iEx);
                        } catch (IllegalArgumentException iaEx) {
                            log.error("Thread Got Interrupted while waiting on AdapterSubscriptionManager call :" + iaEx);
                        }
                        retryDelay = retryDelay + retryDelay; //This is a requirement from Customer
                    } else {
                        log.error("Unable to call AdapterSubscriptionManager Webservice due to  : " + e);
                        throw e;
                    }
                }
            }

            if (i > retryCount) {
                log.error("Unable to call AdapterSubscriptionManager Webservice due to  : " + catchExp);
                throw catchExp;
            }

        } else {
            response = port.subscribe(adapterSubcribeRequest);
        }
		
        SubscribeResponseMarshaller subscribeResponseMarshaller = new SubscribeResponseMarshaller();
        responseElement = subscribeResponseMarshaller.marshal(response);

        return responseElement;
    }

    private AdapterNotificationProducerPortType getPort(NhinTargetSystemType target) throws ConnectionManagerException {
        String serviceName = NhincConstants.HIEM_SUBSCRIBE_ADAPTER_SERVICE_NAME;
        String url = getUrl(target, serviceName);
        return getPort(url);
    }

    private AdapterNotificationProducerPortType getPort(String url) {
        AdapterNotificationProducerPortType port = adapterSubscribeService.getAdapterNotificationProducerPortSoap11();
        gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().initializePort((javax.xml.ws.BindingProvider) port, url);
        WSBindingProvider bp = (WSBindingProvider) port;
//        bp.setOutboundHeaders(Headers.create(new QName("urn:test", "testheader", "myprefix"), "sampleValue"));
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
