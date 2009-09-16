package gov.hhs.fha.nhinc.hiem.entity.proxy;

import gov.hhs.fha.nhinc.nhincproxynotificationconsumer.NhincProxyNotificationConsumerSecuredPortType;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author dunnek
 */
@WebService(serviceName = "NhincProxyNotificationConsumerSecured", portName = "NhincProxyNotificationConsumerPortSoap11", endpointInterface = "gov.hhs.fha.nhinc.nhincproxynotificationconsumer.NhincProxyNotificationConsumerSecuredPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhincproxynotificationconsumer", wsdlLocation = "META-INF/wsdl/ProxyHiemNotifySecured/NhincProxyNotificationConsumerSecured.wsdl")
@Stateless
@HandlerChain(file = "ProxyHiemNotifyHeaderHandler.xml")
public class ProxyHiemNotifySecured implements NhincProxyNotificationConsumerSecuredPortType
{

    @Resource
    private WebServiceContext context;

    public void notify(gov.hhs.fha.nhinc.common.nhinccommonproxy.NotifyRequestSecuredType notifyRequestSecured)
    {
        ProxyHiemNotifyImpl hiemNotifyImpl = new ProxyHiemNotifyImpl();
        hiemNotifyImpl.notify(notifyRequestSecured, context);
    }
}
