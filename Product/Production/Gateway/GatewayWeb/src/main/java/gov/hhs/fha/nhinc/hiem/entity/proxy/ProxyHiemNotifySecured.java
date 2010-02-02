package gov.hhs.fha.nhinc.hiem.entity.proxy;

import javax.jws.WebService;
import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;
import javax.jws.HandlerChain;

/**
 *
 * @author Neil Webb
 */
@WebService(serviceName = "NhincProxyNotificationConsumerSecured", portName = "NhincProxyNotificationConsumerPortSoap11", endpointInterface = "gov.hhs.fha.nhinc.nhincproxynotificationconsumersecured.NhincProxyNotificationConsumerSecuredPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhincproxynotificationconsumersecured", wsdlLocation = "WEB-INF/wsdl/ProxyHiemNotifySecured/NhincProxyNotificationConsumerSecured.wsdl")
@HandlerChain(file = "ProxyHiemNotifyHeaderHandler.xml")
public class ProxyHiemNotifySecured
{
    @Resource
    private WebServiceContext context;

    public void notify(gov.hhs.fha.nhinc.common.nhinccommonproxy.NotifyRequestSecuredType notifyRequestSecured)
    {
        new ProxyHiemNotifyImpl().notify(notifyRequestSecured, context);
    }

}
