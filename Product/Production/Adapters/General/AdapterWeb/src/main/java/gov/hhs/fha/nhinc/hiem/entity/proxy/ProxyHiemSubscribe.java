package gov.hhs.fha.nhinc.hiem.entity.proxy;

import gov.hhs.fha.nhinc.nhincproxysubscriptionmanagement.InvalidFilterFault;
import gov.hhs.fha.nhinc.nhincproxysubscriptionmanagement.InvalidMessageContentExpressionFault;
import gov.hhs.fha.nhinc.nhincproxysubscriptionmanagement.InvalidProducerPropertiesExpressionFault;
import gov.hhs.fha.nhinc.nhincproxysubscriptionmanagement.InvalidTopicExpressionFault;
import gov.hhs.fha.nhinc.nhincproxysubscriptionmanagement.NotifyMessageNotSupportedFault;
import gov.hhs.fha.nhinc.nhincproxysubscriptionmanagement.ResourceUnknownFault;
import gov.hhs.fha.nhinc.nhincproxysubscriptionmanagement.SubscribeCreationFailedFault;
import gov.hhs.fha.nhinc.nhincproxysubscriptionmanagement.TopicExpressionDialectUnknownFault;
import gov.hhs.fha.nhinc.nhincproxysubscriptionmanagement.TopicNotSupportedFault;
import gov.hhs.fha.nhinc.nhincproxysubscriptionmanagement.UnacceptableInitialTerminationTimeFault;
import gov.hhs.fha.nhinc.nhincproxysubscriptionmanagement.UnrecognizedPolicyRequestFault;
import gov.hhs.fha.nhinc.nhincproxysubscriptionmanagement.UnsupportedPolicyRequestFault;
import javax.jws.WebService;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "NhincProxyNotificationProducer", portName = "NhincProxyNotificationProducerPortSoap11", endpointInterface = "gov.hhs.fha.nhinc.nhincproxysubscriptionmanagement.NhincProxyNotificationProducerPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhincproxysubscriptionmanagement", wsdlLocation = "WEB-INF/wsdl/ProxyHiemSubscribe/NhincProxySubscriptionManagement.wsdl")
public class ProxyHiemSubscribe {

    public org.oasis_open.docs.wsn.b_2.SubscribeResponse subscribe(gov.hhs.fha.nhinc.common.nhinccommonproxy.SubscribeRequestType subscribeRequest) throws ResourceUnknownFault, InvalidTopicExpressionFault, UnacceptableInitialTerminationTimeFault, NotifyMessageNotSupportedFault, UnrecognizedPolicyRequestFault, InvalidProducerPropertiesExpressionFault, SubscribeCreationFailedFault, UnsupportedPolicyRequestFault, InvalidFilterFault, InvalidMessageContentExpressionFault, TopicNotSupportedFault, TopicExpressionDialectUnknownFault {
        return new ProxyHiemSubscribeImpl().subscribe(subscribeRequest);
    }

}
