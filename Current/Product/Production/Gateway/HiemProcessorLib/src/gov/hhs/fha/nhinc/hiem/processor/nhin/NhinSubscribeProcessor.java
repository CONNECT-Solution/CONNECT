package gov.hhs.fha.nhinc.hiem.processor.nhin;

import gov.hhs.fha.nhinc.hiem.processor.faults.ConfigurationException;
import gov.hhs.fha.nhinc.hiemadapter.proxy.subscribe.HiemSubscribeAdapterProxy;
import javax.xml.xpath.XPathExpressionException;
import org.oasis_open.docs.wsn.b_2.SubscribeResponse;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommon.QualifiedSubjectIdentifierType;
import gov.hhs.fha.nhinc.hiem.configuration.ConfigurationManager;
import gov.hhs.fha.nhinc.hiem.configuration.topicconfiguration.TopicConfigurationEntry;
import gov.hhs.fha.nhinc.hiem.configuration.topicconfiguration.TopicConfigurationManager;
import gov.hhs.fha.nhinc.hiem.dte.Namespaces;
import gov.hhs.fha.nhinc.hiem.dte.marshallers.SubscribeResponseMarshaller;
import gov.hhs.fha.nhinc.hiem.processor.common.HiemProcessorConstants;
import gov.hhs.fha.nhinc.hiem.processor.common.PatientIdExtractor;
import gov.hhs.fha.nhinc.hiem.processor.faults.SoapFaultFactory;
import gov.hhs.fha.nhinc.hiem.processor.nhin.handler.SubscriptionHandler;
import gov.hhs.fha.nhinc.hiem.processor.nhin.handler.SubscriptionHandlerFactory;
import gov.hhs.fha.nhinc.hiemadapter.proxy.subscribe.HiemSubscribeAdapterProxyObjectFactory;
import gov.hhs.fha.nhinc.subscription.repository.roottopicextractor.RootTopicExtractor;
import gov.hhs.fha.nhinc.xmlCommon.XmlUtility;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oasis_open.docs.wsn.bw_2.InvalidTopicExpressionFault;
import org.oasis_open.docs.wsn.bw_2.NotifyMessageNotSupportedFault;
import org.oasis_open.docs.wsn.bw_2.ResourceUnknownFault;
import org.oasis_open.docs.wsn.bw_2.SubscribeCreationFailedFault;
import org.oasis_open.docs.wsn.bw_2.TopicNotSupportedFault;
import org.w3c.dom.Element;

/**
 * Class used to process an NHIN Subscribe message
 * 
 * @author Neil Webb
 */
public class NhinSubscribeProcessor {

    private static Log log = LogFactory.getLog(NhinSubscribeProcessor.class);

    /**
     * Perform processing for an NHIN subscribe message.
     *
     * @param subscribe NHIN subscribe message
     * @param assertion Assertion information extracted from the SOAP header
     * @param rawSubscribeXml Raw subscribe message received in the SOAP message
     * @return Subscribe response message
     * @throws java.lang.Exception
     */
    public SubscribeResponse processNhinSubscribe(Element soapMessage, AssertionType assertion) throws NotifyMessageNotSupportedFault, SubscribeCreationFailedFault, TopicNotSupportedFault, InvalidTopicExpressionFault, ResourceUnknownFault {
        log.debug("In processNhinSubscribe");

        log.debug("extract subscribe from soapmessage");
        Element subscribe = XmlUtility.getSingleChildElement(soapMessage, Namespaces.WSNT, "Subscribe");
        SubscribeResponse subscribeResponse = null;

        String serviceMode;
        try {
            ConfigurationManager config = new ConfigurationManager();
            serviceMode = config.getSubscriptionServiceMode();
        } catch (ConfigurationException ex) {
            throw new SoapFaultFactory().getUnknownSubscriptionServiceMode("Configuration occurred - unable to determine service mode.");
        }

        log.debug("serviceMode=" + serviceMode);
        if (HiemProcessorConstants.HIEM_SERVICE_MODE_PASSTHROUGH.equals(serviceMode)) {
            log.debug("In passthrough mode");
            subscribeResponse = passthroughMode(subscribe, assertion);
        } else if (HiemProcessorConstants.HIEM_SERVICE_MODE_NOT_SUPPORTED.equals(serviceMode)) {
            log.debug("Subscriptions are not supported");
            throw new SoapFaultFactory().getSubscriptionsNotSupported();
        } else if (HiemProcessorConstants.HIEM_SERVICE_MODE_SUPPORTED.equalsIgnoreCase(serviceMode)) {
            log.debug("Subscriptions are supported. Processing subscribe message");
            subscribeResponse = nhinSubscribe(subscribe, assertion);
        } else {
            log.error("Unknown subscription service mode: " + serviceMode);
            throw new SoapFaultFactory().getUnknownSubscriptionServiceMode(serviceMode);
        }
        return subscribeResponse;
    }

    private TopicConfigurationEntry getTopicConfiguration(Element subscribeElement) throws TopicNotSupportedFault, InvalidTopicExpressionFault, ConfigurationException {
        RootTopicExtractor rootTopicExtractor = new RootTopicExtractor();

        Element topic;
        try {
            log.debug("finding topic from message");
            topic = rootTopicExtractor.extractTopicExpressionElementFromSubscribeElement(subscribeElement);
            log.debug("complete with finding topic.  found=" + (topic != null));
        } catch (XPathExpressionException ex) {
            throw new SoapFaultFactory().getUnableToParseTopicExpressionFromSubscribeFault(ex);
        }

//        if (topic == null) {
//            log.debug("topic not found from message, so using reverse compatibility");
//            RootTopicExtractorReverseCompat compat = new RootTopicExtractorReverseCompat();
//            topic = compat.buildReverseCompatTopicExpression(subscribeElement);
//            log.debug("complete with RootTopicExtractorReverseCompat.  found=" + (topic != null));
//        }

        TopicConfigurationEntry topicConfig;
        topicConfig = TopicConfigurationManager.getInstance().getTopicConfiguration(topic);

        if (topicConfig == null) {
            throw new SoapFaultFactory().getUnknownTopic(topic);
        }
        if (!topicConfig.isSupported()) {
            throw new SoapFaultFactory().getKnownTopicNotSupported(topic);
        }

        return topicConfig;
    }

    private SubscribeResponse nhinSubscribe(Element subscribe, AssertionType assertion) throws TopicNotSupportedFault, InvalidTopicExpressionFault, SubscribeCreationFailedFault, ResourceUnknownFault {
        log.debug("Begin nhinSubscribe");
        SubscribeResponse response = null;

        TopicConfigurationEntry topicConfig;
        try {
            log.debug("determine topic configuration");
            topicConfig = getTopicConfiguration(subscribe);
            log.debug("getTopicConfiguration complete.  isnull=" + (topicConfig == null));

            if (topicConfig == null) {
                throw new SoapFaultFactory().getUnknownTopic(null);
            }
        } catch (ConfigurationException ex) {
            throw new SoapFaultFactory().getTopicConfigurationException(ex);
        }

        QualifiedSubjectIdentifierType patientIdentifier = new PatientIdExtractor().extractPatientIdentifier(subscribe, topicConfig);
        performPolicyCheck(subscribe, assertion, patientIdentifier);

        SubscriptionHandler subscriptionHandler;
        try {
            log.debug("creating subscription handler [SubscriptionHandlerFactory().getSubscriptionHandler();]");
            subscriptionHandler = new SubscriptionHandlerFactory().getSubscriptionHandler();
            log.debug("create subscription handler complete.  isnull=" + (subscriptionHandler == null));
        } catch (ConfigurationException ex) {
            throw new SoapFaultFactory().getConfigurationException(ex);
        }

        log.debug("Sending subscribe message to message handler");
        response = subscriptionHandler.handleSubscribe(subscribe);

        log.debug("End nhinSubscribe");
        return response;
    }

    private void performPolicyCheck(Element subscribe, AssertionType assertion, QualifiedSubjectIdentifierType patientIdentifier) {
        // TODO: Call policy check
    }

    private SubscribeResponse passthroughMode(Element subscribe, AssertionType assertion) throws SubscribeCreationFailedFault {
        log.info("initialize HIEM subscribe adapter proxy");
        HiemSubscribeAdapterProxyObjectFactory factory = new HiemSubscribeAdapterProxyObjectFactory();
        HiemSubscribeAdapterProxy proxy = factory.getHiemSubscribeAdapterProxy();

        Element subscribeResponseElement = null;
        NhinTargetSystemType target = null;
        log.info("invoke HIEM subscribe adapter proxy");
        try {
            subscribeResponseElement =  proxy.subscribe(subscribe, assertion, target);
        } catch (Exception ex) {
            throw new SoapFaultFactory().getFailedToForwardSubscribeToAgencyFault(ex);
        }

        log.debug("unmarshalling subscription response");
        SubscribeResponseMarshaller marshaller = new SubscribeResponseMarshaller();
        SubscribeResponse subscribeResponse =marshaller.unmarshal(subscribeResponseElement);

        log.info("complete with invoke HIEM subscribe adapter proxy");

        return subscribeResponse;
    }
}
