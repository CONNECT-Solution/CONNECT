package gov.hhs.fha.nhinc.hiem.entity.proxy;

import gov.hhs.fha.nhinc.nhincproxysubscriptionmanagement.InvalidFilterFault;
import gov.hhs.fha.nhinc.nhincproxysubscriptionmanagement.InvalidMessageContentExpressionFault;
import gov.hhs.fha.nhinc.nhincproxysubscriptionmanagement.InvalidProducerPropertiesExpressionFault;
import gov.hhs.fha.nhinc.nhincproxysubscriptionmanagement.InvalidTopicExpressionFault;
import gov.hhs.fha.nhinc.nhincproxysubscriptionmanagement.NhincProxyNotificationProducerSecuredPortType;
import gov.hhs.fha.nhinc.nhincproxysubscriptionmanagement.NotifyMessageNotSupportedFault;
import gov.hhs.fha.nhinc.nhincproxysubscriptionmanagement.ResourceUnknownFault;
import gov.hhs.fha.nhinc.nhincproxysubscriptionmanagement.SubscribeCreationFailedFault;
import gov.hhs.fha.nhinc.nhincproxysubscriptionmanagement.TopicExpressionDialectUnknownFault;
import gov.hhs.fha.nhinc.nhincproxysubscriptionmanagement.TopicNotSupportedFault;
import gov.hhs.fha.nhinc.nhincproxysubscriptionmanagement.UnacceptableInitialTerminationTimeFault;
import gov.hhs.fha.nhinc.nhincproxysubscriptionmanagement.UnrecognizedPolicyRequestFault;
import gov.hhs.fha.nhinc.nhincproxysubscriptionmanagement.UnsupportedPolicyRequestFault;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author dunnek
 */
@WebService(serviceName = "NhincProxyNotificationProducerSecured", portName = "NhincProxyNotificationProducerPortSoap11", endpointInterface = "gov.hhs.fha.nhinc.nhincproxysubscriptionmanagement.NhincProxyNotificationProducerSecuredPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhincproxysubscriptionmanagement", wsdlLocation = "META-INF/wsdl/ProxyHiemSubscribeSecured/NhincProxySubscriptionManagementSecured.wsdl")
@Stateless
@HandlerChain(file = "ProxyHiemSubscribeHeaderHandler.xml")
public class ProxyHiemSubscribeSecured implements NhincProxyNotificationProducerSecuredPortType
{

    @Resource
    private WebServiceContext context;

    public org.oasis_open.docs.wsn.b_2.SubscribeResponse subscribe(gov.hhs.fha.nhinc.common.nhinccommonproxy.SubscribeRequestSecuredType subscribeRequestSecured) throws UnacceptableInitialTerminationTimeFault, UnrecognizedPolicyRequestFault, InvalidMessageContentExpressionFault, InvalidFilterFault, InvalidProducerPropertiesExpressionFault, InvalidTopicExpressionFault, TopicExpressionDialectUnknownFault, SubscribeCreationFailedFault, UnsupportedPolicyRequestFault, ResourceUnknownFault, NotifyMessageNotSupportedFault, TopicNotSupportedFault
    {
        ProxyHiemSubscribeImpl hiemSubscribeImpl = new ProxyHiemSubscribeImpl();
        try
        {
            return hiemSubscribeImpl.subscribe(subscribeRequestSecured, context);
        }
        catch (org.oasis_open.docs.wsn.bw_2.NotifyMessageNotSupportedFault ex)
        {
            throw new NotifyMessageNotSupportedFault(ex.getMessage(), null);
        }
        catch (org.oasis_open.docs.wsn.bw_2.UnacceptableInitialTerminationTimeFault ex)
        {
            throw new UnacceptableInitialTerminationTimeFault(ex.getMessage(), null);
        }
        catch (org.oasis_open.docs.wsn.bw_2.InvalidTopicExpressionFault ex)
        {
            throw new InvalidTopicExpressionFault(ex.getMessage(), null);
        }
        catch (org.oasis_open.docs.wsn.bw_2.UnrecognizedPolicyRequestFault ex)
        {
            throw new UnrecognizedPolicyRequestFault(ex.getMessage(), null);
        }
        catch (org.oasis_open.docs.wsn.bw_2.UnsupportedPolicyRequestFault ex)
        {
            throw new UnsupportedPolicyRequestFault(ex.getMessage(), null);
        }
        catch (org.oasis_open.docs.wsn.bw_2.InvalidProducerPropertiesExpressionFault ex)
        {
            throw new InvalidProducerPropertiesExpressionFault(ex.getMessage(), null);
        }
        catch (org.oasis_open.docs.wsn.bw_2.TopicNotSupportedFault ex)
        {
            throw new TopicNotSupportedFault(ex.getMessage(), null);
        }
        catch (org.oasis_open.docs.wsn.bw_2.SubscribeCreationFailedFault ex)
        {
            throw new SubscribeCreationFailedFault(ex.getMessage(), null);
        }
        catch (org.oasis_open.docs.wsn.bw_2.TopicExpressionDialectUnknownFault ex)
        {
            throw new TopicExpressionDialectUnknownFault(ex.getMessage(), null);
        }
        catch (org.oasis_open.docs.wsn.bw_2.InvalidFilterFault ex)
        {
            throw new InvalidFilterFault(ex.getMessage(), null);
        }
        catch (org.oasis_open.docs.wsn.bw_2.InvalidMessageContentExpressionFault ex)
        {
            throw new InvalidMessageContentExpressionFault(ex.getMessage(), null);
        }
        catch (org.oasis_open.docs.wsn.bw_2.ResourceUnknownFault ex)
        {
            throw new ResourceUnknownFault(ex.getMessage(), null);
        }

    }
}
