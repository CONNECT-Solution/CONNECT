/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.hiemunsubscribe;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.jws.HandlerChain;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import org.oasis_open.docs.wsn.bw_2.ResourceUnknownFault;
import org.oasis_open.docs.wsn.bw_2.SubscriptionManager;
import org.oasis_open.docs.wsn.bw_2.UnableToDestroySubscriptionFault;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author jhoppesc
 */
@WebService(serviceName = "SubscriptionManagerService", portName = "SubscriptionManagerPort", endpointInterface = "org.oasis_open.docs.wsn.bw_2.SubscriptionManager", targetNamespace = "http://docs.oasis-open.org/wsn/bw-2", wsdlLocation = "META-INF/wsdl/HiemUnsubscribe/NhinSubscription.wsdl")
@Stateless
@HandlerChain(file = "HiemUnsubscribeSoapHeaderHandler.xml")
@BindingType(value = "http://java.sun.com/xml/ns/jaxws/2003/05/soap/bindings/HTTP/")
public class HiemUnsubscribe implements SubscriptionManager {
    
    @Resource
    private WebServiceContext context;
    private static Log log = LogFactory.getLog(HiemUnsubscribe.class);

    public org.oasis_open.docs.wsn.b_2.UnsubscribeResponse unsubscribe(org.oasis_open.docs.wsn.b_2.Unsubscribe unsubscribeRequest) throws ResourceUnknownFault, UnableToDestroySubscriptionFault {
        return new HiemUnsubscribeImpl().unsubscribe(unsubscribeRequest, context);
    }

}
