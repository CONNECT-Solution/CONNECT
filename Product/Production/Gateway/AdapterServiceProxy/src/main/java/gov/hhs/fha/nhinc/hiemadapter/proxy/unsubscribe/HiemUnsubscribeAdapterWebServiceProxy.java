/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.hiemadapter.proxy.unsubscribe;

import com.sun.xml.ws.developer.WSBindingProvider;
import gov.hhs.fha.nhinc.adaptersubscriptionmanagement.AdapterSubscriptionManager;
import gov.hhs.fha.nhinc.adaptersubscriptionmanagement.AdapterSubscriptionManagerPortType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.UnsubscribeRequestType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.hiem.consumerreference.ReferenceParametersElements;
import gov.hhs.fha.nhinc.hiem.dte.SoapUtil;
import gov.hhs.fha.nhinc.hiem.dte.marshallers.WsntUnsubscribeMarshaller;
import gov.hhs.fha.nhinc.hiem.dte.marshallers.WsntUnsubscribeResponseMarshaller;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.ws.BindingProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oasis_open.docs.wsn.b_2.Unsubscribe;
import org.oasis_open.docs.wsn.b_2.UnsubscribeResponse;
import org.w3c.dom.Element;

/**
 *
 * @author rayj
 */
public class HiemUnsubscribeAdapterWebServiceProxy implements HiemUnsubscribeAdapterProxy {

    private static Log log = LogFactory.getLog(HiemUnsubscribeAdapterWebServiceProxy.class);
    static AdapterSubscriptionManager service = new AdapterSubscriptionManager();

    public Element unsubscribe(Element unsubscribeElement, ReferenceParametersElements referenceParametersElements, AssertionType assertion, NhinTargetSystemType target) {
        Element responseElement = null;
        AdapterSubscriptionManagerPortType port = getPort(target);

        if (port != null) {
            log.debug("attaching reference parameter headers");
            SoapUtil soapUtil = new SoapUtil();
            soapUtil.attachReferenceParameterElements((WSBindingProvider) port, referenceParametersElements);

            log.debug("unmarshalling unsubscribe element");
            WsntUnsubscribeMarshaller unsubscribeMarshaller = new WsntUnsubscribeMarshaller();
            Unsubscribe subscribe = unsubscribeMarshaller.unmarshal(unsubscribeElement);

            log.debug("building unsubscribe message");
            UnsubscribeRequestType adapterUnsubscribeRequest = new UnsubscribeRequestType();
            adapterUnsubscribeRequest.setUnsubscribe(subscribe);
            adapterUnsubscribeRequest.setAssertion(assertion);

            log.debug("invoking unsubscribe port");
            UnsubscribeResponse response = port.unsubscribe(adapterUnsubscribeRequest);

            log.debug("building response");
            WsntUnsubscribeResponseMarshaller unsubscribeResponseMarshaller = new WsntUnsubscribeResponseMarshaller();
            responseElement = unsubscribeResponseMarshaller.marshal(response);
        } else {
            throw new RuntimeException("Unable to create adapter port");
        }
        return responseElement;
    }

    private AdapterSubscriptionManagerPortType getPort(NhinTargetSystemType target) {
        String serviceName = NhincConstants.HIEM_UNSUBSCRIBE_ADAPTER_SERVICE_NAME;
        String url = getUrl(target, serviceName);
        AdapterSubscriptionManagerPortType port = null;
        if (NullChecker.isNotNullish(url)) {
            port = getPort(url);
        }
        return port;
    }

    private AdapterSubscriptionManagerPortType getPort(String url) {
        AdapterSubscriptionManagerPortType port = service.getAdapterSubscriptionManagerPortSoap11();
        gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().initializePort((javax.xml.ws.BindingProvider) port, url);
        return port;
    }

    private String getUrl(NhinTargetSystemType target, String serviceName) {
        String url = null;
        try {
            url = ConnectionManagerCache.getEndpontURLFromNhinTarget(target, serviceName);
        } catch (ConnectionManagerException ex) {
            log.warn("exception occurred accessing url from connection manager (getEndpontURLFromNhinTarget)", ex);
        }
        if (NullChecker.isNullish(url)) {
            try {
                url = ConnectionManagerCache.getLocalEndpointURLByServiceName(serviceName);
            } catch (ConnectionManagerException ex) {
                log.warn("exception occurred accessing url from connection manager (getLocalEndpointURLByServiceName)", ex);
            }
        }
        return url;
    }
}
