package gov.hhs.fha.nhinc.unsubscribe;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import org.oasis_open.docs.wsn.bw_2.ResourceUnknownFault;
import org.oasis_open.docs.wsn.bw_2.SubscriptionManager;
import org.oasis_open.docs.wsn.bw_2.UnableToDestroySubscriptionFault;

/**
 *
 * @author jhoppesc
 */
@WebService(serviceName = "SubscriptionManagerService", portName = "SubscriptionManagerPort", endpointInterface = "org.oasis_open.docs.wsn.bw_2.SubscriptionManager", targetNamespace = "http://docs.oasis-open.org/wsn/bw-2", wsdlLocation = "META-INF/wsdl/HiemUnsubscribe/NhinSubscription.wsdl")
@Stateless
@BindingType(value = "http://java.sun.com/xml/ns/jaxws/2003/05/soap/bindings/HTTP/")
@HandlerChain(file = "HiemUnsubscribeSoapHeaderHandler.xml")
public class HiemUnsubscribe implements SubscriptionManager {

    @Resource
    private WebServiceContext context;

    public org.oasis_open.docs.wsn.b_2.UnsubscribeResponse unsubscribe(org.oasis_open.docs.wsn.b_2.Unsubscribe unsubscribeRequest) throws UnableToDestroySubscriptionFault, ResourceUnknownFault {
        return new HiemUnsubscribeImpl().unsubscribe(unsubscribeRequest, context);
    }
}
