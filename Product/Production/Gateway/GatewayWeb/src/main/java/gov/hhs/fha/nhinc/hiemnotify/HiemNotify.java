package gov.hhs.fha.nhinc.hiemnotify;

import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;
import javax.jws.HandlerChain;

/**
 *
 * @author Neil Webb
 */
@WebService(serviceName = "NotificationConsumerService", portName = "NotificationConsumerPort", endpointInterface = "org.oasis_open.docs.wsn.bw_2.NotificationConsumer", targetNamespace = "http://docs.oasis-open.org/wsn/bw-2", wsdlLocation = "WEB-INF/wsdl/HiemNotify/NhinSubscription.wsdl")
@HandlerChain(file = "NotifySoapHeaderHandler.xml")
@BindingType(value = "http://www.w3.org/2003/05/soap/bindings/HTTP/")
public class HiemNotify
{
    @Resource
    private WebServiceContext context;

    public void notify(org.oasis_open.docs.wsn.b_2.Notify notify)
    {
        HiemNotifyImpl.notify(notify, context);
    }

}
