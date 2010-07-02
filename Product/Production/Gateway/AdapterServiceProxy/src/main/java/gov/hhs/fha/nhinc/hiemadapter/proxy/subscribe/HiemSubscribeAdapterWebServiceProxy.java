/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.hiemadapter.proxy.subscribe;

import com.sun.xml.ws.api.message.Headers;
import com.sun.xml.ws.api.message.Header;
import com.sun.xml.ws.developer.WSBindingProvider;
import javax.xml.namespace.QName;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.hiemadapter.proxy.subscribe.HiemSubscribeAdapterProxy;
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
import javax.xml.ws.BindingProvider;
import org.oasis_open.docs.wsn.b_2.Subscribe;
import gov.hhs.fha.nhinc.hiem.dte.marshallers.WsntSubscribeMarshaller;

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

        response = port.subscribe(adapterSubcribeRequest);

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
