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
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jws.HandlerChain;
import javax.jws.WebService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author Jon Hoppesch
 */
@WebService(serviceName = "NhincProxyNotificationProducer", portName = "NhincProxyNotificationProducerPortSoap11", endpointInterface = "gov.hhs.fha.nhinc.nhincproxysubscriptionmanagement.NhincProxyNotificationProducerPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhincproxysubscriptionmanagement", wsdlLocation = "META-INF/wsdl/ProxyHiemSubscribe/NhincProxySubscriptionManagement.wsdl")
@Stateless
@HandlerChain(file = "ProxyHiemSubscribeHeaderHandler.xml")
public class ProxyHiemSubscribe implements NhincProxyNotificationProducerPortType {

    @Resource
    private WebServiceContext context;

    private static Log log = LogFactory.getLog(ProxyHiemSubscribe.class);

    public org.oasis_open.docs.wsn.b_2.SubscribeResponse subscribe(gov.hhs.fha.nhinc.common.nhinccommonproxy.SubscribeRequestType subscribeRequest) throws NotifyMessageNotSupportedFault, SubscribeCreationFailedFault, TopicNotSupportedFault, InvalidTopicExpressionFault, ResourceUnknownFault, InvalidMessageContentExpressionFault, TopicExpressionDialectUnknownFault, UnrecognizedPolicyRequestFault, InvalidFilterFault, InvalidProducerPropertiesExpressionFault, UnsupportedPolicyRequestFault, UnacceptableInitialTerminationTimeFault {
        ProxyHiemSubscribeImpl hiemSubscribeImpl = new ProxyHiemSubscribeImpl();
        try {
            return hiemSubscribeImpl.subscribe(subscribeRequest, context);
        } catch (org.oasis_open.docs.wsn.bw_2.NotifyMessageNotSupportedFault ex) {
            throw new NotifyMessageNotSupportedFault(ex.getMessage(), null);
        } catch (org.oasis_open.docs.wsn.bw_2.UnacceptableInitialTerminationTimeFault ex) {
            throw new UnacceptableInitialTerminationTimeFault(ex.getMessage(), null);
        } catch (org.oasis_open.docs.wsn.bw_2.InvalidTopicExpressionFault ex) {
            throw new InvalidTopicExpressionFault(ex.getMessage(), null);
        } catch (org.oasis_open.docs.wsn.bw_2.UnrecognizedPolicyRequestFault ex) {
            throw new UnrecognizedPolicyRequestFault(ex.getMessage(), null);
        } catch (org.oasis_open.docs.wsn.bw_2.UnsupportedPolicyRequestFault ex) {
            throw new UnsupportedPolicyRequestFault(ex.getMessage(), null);
        } catch (org.oasis_open.docs.wsn.bw_2.InvalidProducerPropertiesExpressionFault ex) {
            throw new InvalidProducerPropertiesExpressionFault(ex.getMessage(), null);
        } catch (org.oasis_open.docs.wsn.bw_2.TopicNotSupportedFault ex) {
            throw new TopicNotSupportedFault(ex.getMessage(), null);
        } catch (org.oasis_open.docs.wsn.bw_2.SubscribeCreationFailedFault ex) {
            throw new SubscribeCreationFailedFault(ex.getMessage(), null);
        } catch (org.oasis_open.docs.wsn.bw_2.TopicExpressionDialectUnknownFault ex) {
            throw new TopicExpressionDialectUnknownFault(ex.getMessage(), null);
        } catch (org.oasis_open.docs.wsn.bw_2.InvalidFilterFault ex) {
            throw new InvalidFilterFault(ex.getMessage(), null);
        } catch (org.oasis_open.docs.wsn.bw_2.InvalidMessageContentExpressionFault ex) {
            throw new InvalidMessageContentExpressionFault(ex.getMessage(), null);
        } catch (org.oasis_open.docs.wsn.bw_2.ResourceUnknownFault ex) {
            throw new ResourceUnknownFault(ex.getMessage(), null);
        }

    }
}
