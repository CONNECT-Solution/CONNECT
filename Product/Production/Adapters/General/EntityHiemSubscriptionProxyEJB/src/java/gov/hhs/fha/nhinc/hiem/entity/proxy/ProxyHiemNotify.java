package gov.hhs.fha.nhinc.hiem.entity.proxy;

import gov.hhs.fha.nhinc.nhincproxynotificationconsumer.NhincProxyNotificationConsumerPortType;
import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;
import javax.jws.HandlerChain;

/**
 *
 * @author dunnek
 */
@WebService(serviceName = "NhincProxyNotificationConsumer", portName = "NhincProxyNotificationConsumerPortSoap11", endpointInterface = "gov.hhs.fha.nhinc.nhincproxynotificationconsumer.NhincProxyNotificationConsumerPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhincproxynotificationconsumer", wsdlLocation = "META-INF/wsdl/ProxyHiemNotify/NhincProxyNotificationConsumer.wsdl")
@Stateless
@HandlerChain(file = "ProxyHiemNotifyHeaderHandler.xml")
public class ProxyHiemNotify implements NhincProxyNotificationConsumerPortType
{
    @Resource
    private WebServiceContext context;

    public void notify(gov.hhs.fha.nhinc.common.nhinccommonproxy.NotifyRequestType notifyRequest)
    {
        new ProxyHiemNotifyImpl().notify(notifyRequest, context);
    }
}
