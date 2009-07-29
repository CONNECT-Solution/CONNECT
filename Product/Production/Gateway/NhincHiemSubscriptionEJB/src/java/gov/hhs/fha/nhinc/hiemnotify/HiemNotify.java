/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.hiemnotify;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import org.oasis_open.docs.wsn.bw_2.NotificationConsumer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author jhoppesc
 */
@WebService(serviceName = "NotificationConsumerService", portName = "NotificationConsumerPort", endpointInterface = "org.oasis_open.docs.wsn.bw_2.NotificationConsumer", targetNamespace = "http://docs.oasis-open.org/wsn/bw-2", wsdlLocation = "META-INF/wsdl/HiemNotify/NhinSubscription.wsdl")
@Stateless
@HandlerChain(file = "NotifySoapHeaderHandler.xml")
@BindingType(value = "http://java.sun.com/xml/ns/jaxws/2003/05/soap/bindings/HTTP/")
public class HiemNotify implements NotificationConsumer {

    @Resource
    private WebServiceContext context;
    private static Log log = LogFactory.getLog(HiemNotify.class);

    public void notify(org.oasis_open.docs.wsn.b_2.Notify notify) {
        HiemNotifyImpl.notify(notify, context);
    }
}
