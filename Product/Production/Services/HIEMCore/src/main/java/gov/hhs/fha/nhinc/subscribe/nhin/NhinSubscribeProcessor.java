/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above
 *       copyright notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the documentation
 *       and/or other materials provided with the distribution.
 *     * Neither the name of the United States Government nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package gov.hhs.fha.nhinc.subscribe.nhin;

import javax.xml.xpath.XPathExpressionException;

import org.apache.log4j.Logger;
import org.oasis_open.docs.wsn.b_2.SubscribeResponse;
import org.oasis_open.docs.wsn.bw_2.InvalidTopicExpressionFault;
import org.oasis_open.docs.wsn.bw_2.NotifyMessageNotSupportedFault;
import org.oasis_open.docs.wsn.bw_2.SubscribeCreationFailedFault;
import org.oasis_open.docs.wsn.bw_2.TopicNotSupportedFault;
import org.w3c.dom.Element;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.hiem.configuration.ConfigurationManager;
import gov.hhs.fha.nhinc.hiem.configuration.topicconfiguration.TopicConfigurationEntry;
import gov.hhs.fha.nhinc.hiem.configuration.topicconfiguration.TopicConfigurationManager;
import gov.hhs.fha.nhinc.hiem.dte.Namespaces;
import gov.hhs.fha.nhinc.hiem.dte.marshallers.SubscribeResponseMarshaller;
import gov.hhs.fha.nhinc.hiem.processor.common.HiemProcessorConstants;
import gov.hhs.fha.nhinc.hiem.processor.faults.ConfigurationException;
import gov.hhs.fha.nhinc.hiem.processor.faults.SoapFaultFactory;
import gov.hhs.fha.nhinc.subscribe.adapter.proxy.HiemSubscribeAdapterProxy;
import gov.hhs.fha.nhinc.subscribe.adapter.proxy.HiemSubscribeAdapterProxyObjectFactory;
import gov.hhs.fha.nhinc.subscription.repository.roottopicextractor.RootTopicExtractor;
import gov.hhs.fha.nhinc.xmlCommon.XmlUtility;

/**
 * Class used to process an NHIN Subscribe message
 *
 * @author Neil Webb
 */
public class NhinSubscribeProcessor {

    private static final Logger LOG = Logger.getLogger(NhinSubscribeProcessor.class);

    /**
     * Perform processing for an NHIN subscribe message.
     *
     * @param subscribe NHIN subscribe message
     * @param assertion Assertion information extracted from the SOAP header
     * @param rawSubscribeXml Raw subscribe message received in the SOAP message
     * @return Subscribe response message
     * @throws java.lang.Exception
     */
    public SubscribeResponse processNhinSubscribe(Element soapMessage, AssertionType assertion)
            throws NotifyMessageNotSupportedFault, SubscribeCreationFailedFault, TopicNotSupportedFault,
            InvalidTopicExpressionFault {
        LOG.debug("In processNhinSubscribe");

        LOG.debug("extract subscribe from soapmessage");
        Element subscribe = XmlUtility.getSingleChildElement(soapMessage, Namespaces.WSNT, "Subscribe");
        SubscribeResponse subscribeResponse = null;

        String serviceMode;
        try {
            ConfigurationManager config = new ConfigurationManager();
            serviceMode = config.getSubscriptionServiceMode();
        } catch (ConfigurationException ex) {
            throw new SoapFaultFactory()
                    .getUnknownSubscriptionServiceMode("Configuration occurred - unable to determine service mode.");
        }

        LOG.debug("serviceMode=" + serviceMode);
        if (HiemProcessorConstants.HIEM_SERVICE_MODE_PASSTHROUGH.equals(serviceMode)) {
            LOG.debug("In passthrough mode");
            subscribeResponse = passthroughMode(subscribe, assertion);
        } else if (HiemProcessorConstants.HIEM_SERVICE_MODE_NOT_SUPPORTED.equals(serviceMode)) {
            LOG.debug("Subscriptions are not supported");
            throw new SoapFaultFactory().getSubscriptionsNotSupported();
        } else if (HiemProcessorConstants.HIEM_SERVICE_MODE_SUPPORTED.equalsIgnoreCase(serviceMode)) {
            LOG.debug("Subscriptions are supported. Processing subscribe message");
            subscribeResponse = nhinSubscribe(subscribe, assertion);
        } else {
            LOG.error("Unknown subscription service mode: " + serviceMode);
            throw new SoapFaultFactory().getUnknownSubscriptionServiceMode(serviceMode);
        }
        return subscribeResponse;
    }

    private TopicConfigurationEntry getTopicConfiguration(Element subscribeElement) throws TopicNotSupportedFault,
            InvalidTopicExpressionFault, ConfigurationException {
        RootTopicExtractor rootTopicExtractor = new RootTopicExtractor();

        Element topic;
        try {
            LOG.debug("finding topic from message");
            topic = rootTopicExtractor.extractTopicExpressionElementFromSubscribeElement(subscribeElement);
            LOG.debug("complete with finding topic.  found=" + (topic != null));
        } catch (XPathExpressionException ex) {
            throw new SoapFaultFactory().getUnableToParseTopicExpressionFromSubscribeFault(ex);
        }

        // if (topic == null) {
        // log.debug("topic not found from message, so using reverse compatibility");
        // RootTopicExtractorReverseCompat compat = new RootTopicExtractorReverseCompat();
        // topic = compat.buildReverseCompatTopicExpression(subscribeElement);
        // log.debug("complete with RootTopicExtractorReverseCompat.  found=" + (topic != null));
        // }

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

    private SubscribeResponse nhinSubscribe(Element subscribe, AssertionType assertion) throws TopicNotSupportedFault,
            InvalidTopicExpressionFault, SubscribeCreationFailedFault {
        LOG.debug("Begin nhinSubscribe");
        SubscribeResponse response = null;

        TopicConfigurationEntry topicConfig;
        try {
            LOG.debug("determine topic configuration");
            topicConfig = getTopicConfiguration(subscribe);
            LOG.debug("getTopicConfiguration complete.  isnull=" + (topicConfig == null));

            if (topicConfig == null) {
                throw new SoapFaultFactory().getUnknownTopic(null);
            }
        } catch (ConfigurationException ex) {
            throw new SoapFaultFactory().getTopicConfigurationException(ex);
        }

        SubscriptionHandler subscriptionHandler;
        try {
            LOG.debug("creating subscription handler [SubscriptionHandlerFactory().getSubscriptionHandler();]");
            subscriptionHandler = new SubscriptionHandlerFactory().getSubscriptionHandler();
            LOG.debug("create subscription handler complete.  isnull=" + (subscriptionHandler == null));
        } catch (ConfigurationException ex) {
            throw new SoapFaultFactory().getConfigurationException(ex);
        }

        LOG.debug("Sending subscribe message to message handler");
        response = subscriptionHandler.handleSubscribe(subscribe);

        LOG.debug("End nhinSubscribe");
        return response;
    }



    private SubscribeResponse passthroughMode(Element subscribe, AssertionType assertion)
            throws SubscribeCreationFailedFault {
        LOG.info("initialize HIEM subscribe adapter proxy");
        HiemSubscribeAdapterProxyObjectFactory factory = new HiemSubscribeAdapterProxyObjectFactory();
        HiemSubscribeAdapterProxy proxy = factory.getHiemSubscribeAdapterProxy();

        Element subscribeResponseElement = null;
        NhinTargetSystemType target = null;
        LOG.info("invoke HIEM subscribe adapter proxy");
        try {
            subscribeResponseElement = proxy.subscribe(subscribe, assertion, target);
        } catch (Exception ex) {
            throw new SoapFaultFactory().getFailedToForwardSubscribeToAgencyFault(ex);
        }

        LOG.debug("unmarshalling subscription response");
        SubscribeResponseMarshaller marshaller = new SubscribeResponseMarshaller();
        SubscribeResponse subscribeResponse = marshaller.unmarshal(subscribeResponseElement);

        LOG.info("complete with invoke HIEM subscribe adapter proxy");

        return subscribeResponse;
    }
}
