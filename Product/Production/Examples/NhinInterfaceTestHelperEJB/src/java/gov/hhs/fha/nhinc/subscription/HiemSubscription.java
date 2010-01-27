package gov.hhs.fha.nhinc.subscription;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;
import org.oasis_open.docs.wsn.bw_2.InvalidFilterFault;
import org.oasis_open.docs.wsn.bw_2.InvalidMessageContentExpressionFault;
import org.oasis_open.docs.wsn.bw_2.InvalidProducerPropertiesExpressionFault;
import org.oasis_open.docs.wsn.bw_2.InvalidTopicExpressionFault;
import org.oasis_open.docs.wsn.bw_2.NotificationProducer;
import org.oasis_open.docs.wsn.bw_2.NotifyMessageNotSupportedFault;
import org.oasis_open.docs.wsn.bw_2.ResourceUnknownFault;
import org.oasis_open.docs.wsn.bw_2.SubscribeCreationFailedFault;
import org.oasis_open.docs.wsn.bw_2.TopicExpressionDialectUnknownFault;
import org.oasis_open.docs.wsn.bw_2.TopicNotSupportedFault;
import org.oasis_open.docs.wsn.bw_2.UnacceptableInitialTerminationTimeFault;
import org.oasis_open.docs.wsn.bw_2.UnrecognizedPolicyRequestFault;
import org.oasis_open.docs.wsn.bw_2.UnsupportedPolicyRequestFault;
import javax.xml.ws.BindingType;

/**
 *
 * @author jhoppesc
 */
@WebService(serviceName = "NotificationProducerService", portName = "NotificationProducerPort", endpointInterface = "org.oasis_open.docs.wsn.bw_2.NotificationProducer", targetNamespace = "http://docs.oasis-open.org/wsn/bw-2", wsdlLocation = "META-INF/wsdl/HiemSubscription/NhinSubscription.wsdl")
@Stateless
@BindingType(value = "http://java.sun.com/xml/ns/jaxws/2003/05/soap/bindings/HTTP/")
public class HiemSubscription implements NotificationProducer {

    @Resource
    private WebServiceContext context;

    public org.oasis_open.docs.wsn.b_2.SubscribeResponse subscribe(org.oasis_open.docs.wsn.b_2.Subscribe subscribeRequest) throws SubscribeCreationFailedFault, ResourceUnknownFault, InvalidFilterFault, NotifyMessageNotSupportedFault, UnrecognizedPolicyRequestFault, UnsupportedPolicyRequestFault, InvalidProducerPropertiesExpressionFault, InvalidTopicExpressionFault, InvalidMessageContentExpressionFault, UnacceptableInitialTerminationTimeFault, TopicNotSupportedFault, TopicExpressionDialectUnknownFault {
        return HiemSubscriptionImpl.subscribe(subscribeRequest, context);
    }
}
