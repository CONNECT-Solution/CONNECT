/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.hiem.entity.proxy;

import gov.hhs.fha.nhinc.nhincproxysubscriptionmanagement.NhincProxySubscriptionManagerPortType;
import gov.hhs.fha.nhinc.nhincproxysubscriptionmanagement.ResourceUnknownFault;
import gov.hhs.fha.nhinc.nhincproxysubscriptionmanagement.UnableToDestroySubscriptionFault;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;
import javax.jws.HandlerChain;

/**
 *
 * @author rayj
 */
@WebService(serviceName = "NhincProxySubscriptionManager", portName = "NhincProxySubscriptionManagerPortSoap11", endpointInterface = "gov.hhs.fha.nhinc.nhincproxysubscriptionmanagement.NhincProxySubscriptionManagerPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhincproxysubscriptionmanagement", wsdlLocation = "META-INF/wsdl/NhincProxyUnsubscribe/NhincProxySubscriptionManagement.wsdl")
@Stateless
@HandlerChain(file = "ProxyHiemUnsubscribeHeaderHandler.xml")
public class ProxyHiemUnsubscribe implements NhincProxySubscriptionManagerPortType {

    @Resource
    private WebServiceContext context;

    public org.oasis_open.docs.wsn.b_2.UnsubscribeResponse unsubscribe(gov.hhs.fha.nhinc.common.nhinccommonproxy.UnsubscribeRequestType unsubscribeRequest) throws UnableToDestroySubscriptionFault, ResourceUnknownFault {
        ProxyHiemUnsubscribeImpl hiemUnsubscribeImpl = new ProxyHiemUnsubscribeImpl();
        return hiemUnsubscribeImpl.unsubscribe(unsubscribeRequest, context);
    }
}
