/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.nhinhiem.proxy.unsubscribe;

import com.sun.xml.ws.developer.WSBindingProvider;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.hiem.dte.marshallers.WsntUnsubscribeMarshaller;
import gov.hhs.fha.nhinc.hiem.dte.marshallers.WsntUnsubscribeResponseMarshaller;
import gov.hhs.fha.nhinc.hiem.consumerreference.ReferenceParametersElements;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;
import java.util.Map;
import javax.xml.ws.BindingProvider;
import org.oasis_open.docs.wsn.b_2.Unsubscribe;
import org.oasis_open.docs.wsn.b_2.UnsubscribeResponse;
import org.oasis_open.docs.wsn.bw_2.ResourceUnknownFault;
import org.oasis_open.docs.wsn.bw_2.SubscriptionManager;
import org.oasis_open.docs.wsn.bw_2.UnableToDestroySubscriptionFault;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Element;
import org.oasis_open.docs.wsn.bw_2.SubscriptionManagerService;
import com.sun.xml.ws.api.message.Headers;
import com.sun.xml.ws.api.message.Header;
import gov.hhs.fha.nhinc.hiem.dte.SoapUtil;
import gov.hhs.fha.nhinc.xmlCommon.XmlUtility;
import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;

/**
 *
 * @author rayj
 */
public class NhinHiemUnsubscribeWebServiceProxy implements NhinHiemUnsubscribeProxy {

    private static Log log = LogFactory.getLog(NhinHiemUnsubscribeWebServiceProxy.class);
    static SubscriptionManagerService nhinService = new SubscriptionManagerService();

    public Element unsubscribe(Element unsubscribeElement, ReferenceParametersElements referenceParametersElements, AssertionType assertion, NhinTargetSystemType target) throws ResourceUnknownFault, UnableToDestroySubscriptionFault {
        SubscriptionManager port = getPort(target, assertion);
        Element responseElement = null;

        if (port != null) {
            log.debug("attaching reference parameter headers");
            SoapUtil soapUtil = new SoapUtil();
            soapUtil.attachReferenceParameterElements((WSBindingProvider) port, referenceParametersElements);
            
            log.debug("unmarshalling unsubscribe element");
            WsntUnsubscribeMarshaller marshaller = new WsntUnsubscribeMarshaller();
            Unsubscribe unsubscribe = marshaller.unmarshal(unsubscribeElement);

            log.debug("invoking unsubscribe port");
            UnsubscribeResponse response = port.unsubscribe(unsubscribe);

            log.debug("marshalling unsubscribe response");
            WsntUnsubscribeResponseMarshaller responseMarshaller = new WsntUnsubscribeResponseMarshaller();
            responseElement = responseMarshaller.marshal(response);
        }
        return responseElement;
    }

    private SubscriptionManager getPort(NhinTargetSystemType target, AssertionType assertion) {
        String url = null;
        if (target != null) {
            try {
                url = ConnectionManagerCache.getEndpontURLFromNhinTarget(target, NhincConstants.HIEM_SUBSCRIPTION_MANAGER_SERVICE_NAME);
            } catch (ConnectionManagerException ex) {
                log.error("Error: Failed to retrieve url for service: " + NhincConstants.HIEM_SUBSCRIPTION_MANAGER_SERVICE_NAME);
                log.error(ex.getMessage());
            }
        } else {
            log.error("Target system passed into the proxy is null");
        }
        return getPort(url, assertion);
    }

    private SubscriptionManager getPort(String url, AssertionType assertion) {
        SubscriptionManager port = null;
        if (NullChecker.isNotNullish(url)) {
            port = nhinService.getSubscriptionManagerPort();
            gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().initializePort((javax.xml.ws.BindingProvider) port, url);

            SamlTokenCreator tokenCreator = new SamlTokenCreator();
            Map requestContext = tokenCreator.CreateRequestContext(assertion, url, NhincConstants.UNSUBSCRIBE_ACTION);
            ((BindingProvider) port).getRequestContext().putAll(requestContext);
        }
        return port;
    }
}
