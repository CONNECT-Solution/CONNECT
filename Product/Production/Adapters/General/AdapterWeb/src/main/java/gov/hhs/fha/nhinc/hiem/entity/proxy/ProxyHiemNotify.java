package gov.hhs.fha.nhinc.hiem.entity.proxy;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;
import javax.jws.HandlerChain;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "NhincProxyNotificationConsumer", portName = "NhincProxyNotificationConsumerPortSoap11", endpointInterface = "gov.hhs.fha.nhinc.nhincproxynotificationconsumer.NhincProxyNotificationConsumerPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhincproxynotificationconsumer", wsdlLocation = "WEB-INF/wsdl/ProxyHiemNotify/NhincProxyNotificationConsumer.wsdl")
@HandlerChain(file = "ProxyHiemNotifyHeaderHandler.xml")
public class ProxyHiemNotify {

    @Resource
    private WebServiceContext context;
    
    public void notify(gov.hhs.fha.nhinc.common.nhinccommonproxy.NotifyRequestType notifyRequest) {
        new ProxyHiemNotifyImpl().notify(notifyRequest, context);
    }

}
