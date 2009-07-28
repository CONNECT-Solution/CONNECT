package nhinc.wsn;

import org.oasis_open.docs.wsn.b_2.Subscribe;
import org.oasis_open.docs.wsn.b_2.SubscribeResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author webbn
 */
public class SubscribeSender
{
    private static Log log = LogFactory.getLog(SubscribeSender.class);
    
    public SubscribeResponse sendSubscribe(Subscribe subscribe)
    {
        SubscribeResponse response = null;
        String endpointAddress = "http://localhost:8088/mockSubscriptionSinkPortTypeBinding";

        try
        { // Call Web Service Operation
            org.netbeans.j2ee.wsdl.interfaces.subscriptionsink.SubscriptionSinkService service = new org.netbeans.j2ee.wsdl.interfaces.subscriptionsink.SubscriptionSinkService();
            org.netbeans.j2ee.wsdl.interfaces.subscriptionsink.SubscriptionSinkPortType port = service.getSubscriptionSinkPortTypeBindingPort();
            ((javax.xml.ws.BindingProvider) port).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpointAddress);
            // TODO initialize WS operation arguments here
            // TODO process result here
            response = port.subscribe(subscribe);
            System.out.println("Result = " + response);
        } catch (Exception ex)
        {
            log.error("Failed to send subscribe message: " + ex.getMessage(), ex);
        }

        return response;
    }
}
