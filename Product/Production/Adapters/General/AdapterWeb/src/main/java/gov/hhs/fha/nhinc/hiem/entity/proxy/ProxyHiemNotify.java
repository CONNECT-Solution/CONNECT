package gov.hhs.fha.nhinc.hiem.entity.proxy;

import javax.jws.WebService;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "NhincProxyNotificationConsumer", portName = "NhincProxyNotificationConsumerPortSoap11", endpointInterface = "gov.hhs.fha.nhinc.nhincproxynotificationconsumer.NhincProxyNotificationConsumerPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhincproxynotificationconsumer", wsdlLocation = "WEB-INF/wsdl/ProxyHiemNotify/NhincProxyNotificationConsumer.wsdl")
public class ProxyHiemNotify {

    public void notify(gov.hhs.fha.nhinc.common.nhinccommonproxy.NotifyRequestType notifyRequest) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

}
