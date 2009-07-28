/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.hiem.entity.proxy;

import gov.hhs.fha.nhinc.nhincproxynotificationconsumer.NhincProxyNotificationConsumerPortType;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author jhoppesc
 */
@WebService(serviceName = "NhincProxyNotificationConsumer", portName = "NhincProxyNotificationConsumerPortSoap11", endpointInterface = "gov.hhs.fha.nhinc.nhincproxynotificationconsumer.NhincProxyNotificationConsumerPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhincproxynotificationconsumer", wsdlLocation = "META-INF/wsdl/ProxyHiemNotify/NhincProxyNotificationConsumer.wsdl")
@Stateless
@HandlerChain(file = "ProxyHiemNotifyHeaderHandler.xml")
public class ProxyHiemNotify implements NhincProxyNotificationConsumerPortType {

    @Resource
    private WebServiceContext context;

    public void notify(gov.hhs.fha.nhinc.common.nhinccommonproxy.NotifyRequestType notifyRequest) {
        ProxyHiemNotifyImpl hiemNotifyImpl = new ProxyHiemNotifyImpl();
        hiemNotifyImpl.notify(notifyRequest, context);
    }

}
