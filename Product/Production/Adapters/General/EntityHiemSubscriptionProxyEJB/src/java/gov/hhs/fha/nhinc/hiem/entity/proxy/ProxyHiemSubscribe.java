package gov.hhs.fha.nhinc.hiem.entity.proxy;

import gov.hhs.fha.nhinc.nhincproxysubscriptionmanagement.InvalidFilterFault;
import gov.hhs.fha.nhinc.nhincproxysubscriptionmanagement.InvalidMessageContentExpressionFault;
import gov.hhs.fha.nhinc.nhincproxysubscriptionmanagement.InvalidProducerPropertiesExpressionFault;
import gov.hhs.fha.nhinc.nhincproxysubscriptionmanagement.InvalidTopicExpressionFault;
import gov.hhs.fha.nhinc.nhincproxysubscriptionmanagement.NhincProxyNotificationProducerPortType;
import gov.hhs.fha.nhinc.nhincproxysubscriptionmanagement.NotifyMessageNotSupportedFault;
import gov.hhs.fha.nhinc.nhincproxysubscriptionmanagement.ResourceUnknownFault;
import gov.hhs.fha.nhinc.nhincproxysubscriptionmanagement.SubscribeCreationFailedFault;
import gov.hhs.fha.nhinc.nhincproxysubscriptionmanagement.TopicExpressionDialectUnknownFault;
import gov.hhs.fha.nhinc.nhincproxysubscriptionmanagement.TopicNotSupportedFault;
import gov.hhs.fha.nhinc.nhincproxysubscriptionmanagement.UnacceptableInitialTerminationTimeFault;
import gov.hhs.fha.nhinc.nhincproxysubscriptionmanagement.UnrecognizedPolicyRequestFault;
import gov.hhs.fha.nhinc.nhincproxysubscriptionmanagement.UnsupportedPolicyRequestFault;
import javax.ejb.Stateless;
import javax.jws.WebService;

/**
 *
 * @author dunnek
 */
@WebService(serviceName = "NhincProxyNotificationProducer", portName = "NhincProxyNotificationProducerPortSoap11", endpointInterface = "gov.hhs.fha.nhinc.nhincproxysubscriptionmanagement.NhincProxyNotificationProducerPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhincproxysubscriptionmanagement", wsdlLocation = "META-INF/wsdl/ProxyHiemSubscribe/NhincProxySubscriptionManagement.wsdl")
@Stateless
public class ProxyHiemSubscribe implements NhincProxyNotificationProducerPortType
{

    public org.oasis_open.docs.wsn.b_2.SubscribeResponse subscribe(gov.hhs.fha.nhinc.common.nhinccommonproxy.SubscribeRequestType subscribeRequest) throws InvalidMessageContentExpressionFault, TopicExpressionDialectUnknownFault, InvalidProducerPropertiesExpressionFault, InvalidFilterFault, UnrecognizedPolicyRequestFault, TopicNotSupportedFault, UnsupportedPolicyRequestFault, InvalidTopicExpressionFault, ResourceUnknownFault, UnacceptableInitialTerminationTimeFault, SubscribeCreationFailedFault, NotifyMessageNotSupportedFault
    {
        return new ProxyHiemSubscribeImpl().subscribe(subscribeRequest);
    }
}
