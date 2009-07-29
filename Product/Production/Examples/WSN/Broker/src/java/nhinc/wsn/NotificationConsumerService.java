/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nhinc.wsn;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.jws.WebService;
import org.oasis_open.docs.wsn.bw_2.NotificationConsumer;

/**
 *
 * @author rayj
 */
@WebService(serviceName = "NotificationConsumerService", portName = "NotificationConsumerPort", endpointInterface = "org.oasis_open.docs.wsn.bw_2.NotificationConsumer", targetNamespace = "http://docs.oasis-open.org/wsn/bw-2", wsdlLocation = "META-INF/wsdl/NotificationConsumerService/NhinSubscription.wsdl")
@Stateless
public class NotificationConsumerService implements NotificationConsumer {

    public void notify(org.oasis_open.docs.wsn.b_2.Notify notify) {
        Brain.Publish(notify);
    }
}
