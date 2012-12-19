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
package gov.hhs.fha.nhinc.fta;

import gov.hhs.fha.nhinc.common.ftaconfigmanager.FTAConfiguration;
import gov.hhs.fha.nhinc.common.ftaconfigmanager.FTAChannel;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import javax.xml.ws.BindingProvider;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.StringWriter;
import java.io.PrintWriter;

import gov.hhs.fha.nhinc.entitynotificationconsumersecured.EntityNotificationConsumerSecured;
import gov.hhs.fha.nhinc.entitynotificationconsumersecured.EntityNotificationConsumerSecuredPortType;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import java.util.Map;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;

import org.oasis_open.docs.wsn.b_2.Notify;

import org.oasis_open.docs.wsn.b_2.NotificationMessageHolderType;
import org.oasis_open.docs.wsn.b_2.TopicExpressionType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.entitynotificationconsumer.EntityNotificationConsumer;
import gov.hhs.fha.nhinc.entitynotificationconsumer.EntityNotificationConsumerPortType;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

/**
 * 
 * @author dunnek
 */
public class FTATimerTask {

    private static final Logger LOG = Logger.getLogger(FTATimerTask.class);
    private FTAConfiguration ftaConfig = null;
    private static Service cachedService = null;
    private static Service cachedSecuredService = null;
    private static WebServiceProxyHelper oProxyHelper = null;
    private static final String NAMESPACE_URI = "";
    private static final String SERVICE_LOCAL_PART = "";
    private static final String PORT_LOCAL_PART = "";
    private static final String WSDL_FILE = "";
    private static final String WS_ADDRESSING_ACTION = "urn:Notify";

    public void setConfiguration(FTAConfiguration config) {
        ftaConfig = config;
    }

    public FTAConfiguration getConfiguration() {
        return ftaConfig;
    }

    public void run() {
        try {
            if (ftaConfig == null) {
                LOG.error("FTA Configuration not loaded.");
                ;
            } else {
                for (FTAChannel channel : ftaConfig.getInboundChannels()) {
                    processDir(channel.getLocation(), channel.getTopic());
                }
            }
        } catch (Throwable t) {
            LOG.error("****** FTATimerTask THROWABLE: " + t.getMessage(), t);

            StringWriter stackTrace = new StringWriter();
            t.printStackTrace(new PrintWriter(stackTrace));
            String sValue = stackTrace.toString();
            if (sValue.indexOf("EJBClassLoader") >= 0) {
                FTATimer.stopTimer();
            }

        }
    }

    private static void processDir(String dirName, String topic) {
        File dir = new File(dirName);

        String[] children = dir.list();

        if (children == null) {
            // Either dir does not exist or is not a directory
        } else {
            for (int i = 0; i < children.length; i++) {

                File child = new File(dir.getAbsolutePath() + "/" + children[i]);

                if (!child.isDirectory()) {
                    LOG.info("FTATimerTask Processing File: " + child.getName());
                    String contents = Util.getFileContents(child);
                    LOG.debug("FTATimerTask File Contents: " + contents);
                    sendNotification(contents, topic);

                    child.delete();

                }

            }
        }

    }

    private static void sendNotification(String contents, String topic) {
        try { // Call Web Service Operation
            String endpointURL = PropertyAccessor.getInstance().getProperty("adapter", "EntityNotificationConsumerURL");
            // String endpointURL = "http://localhost:8088/mockEntityNotificationConsumerBindingSoap11";
            LOG.info("EntitySubscriptionURL :" + endpointURL);

            // TODO initialize WS operation arguments here
            gov.hhs.fha.nhinc.common.nhinccommonentity.NotifyRequestType notifyRequest = new gov.hhs.fha.nhinc.common.nhinccommonentity.NotifyRequestType();
            NotificationMessageHolderType messageHolder = new NotificationMessageHolderType();

            notifyRequest.setAssertion(Util.createAssertion());

            EntityNotificationConsumerPortType port = getPort(endpointURL, notifyRequest.getAssertion());

            NotificationMessageHolderType.Message message = new NotificationMessageHolderType.Message();
            Notify notify = new Notify();

            message.setAny(Util.marshalPayload(contents));

            messageHolder.setMessage(message);

            // TopicExpressionType topicExpression = new TopicExpressionType();
            // topicExpression.setDialect("http://docs.oasis-open.org/wsn/t-1/TopicExpression/Simple");
            // topicExpression.getContent().add(topic);

            TopicExpressionType topicExpression;
            gov.hhs.fha.nhinc.hiem.dte.marshallers.TopicMarshaller marshaller;
            marshaller = new gov.hhs.fha.nhinc.hiem.dte.marshallers.TopicMarshaller();

            // topic =
            // "<wsnt:TopicExpression xmlns:wsnt='http://docs.oasis-open.org/wsn/b-2' Dialect='http://docs.oasis-open.org/wsn/t-1/TopicExpression/Simple' xmlns:nhinc='urn:gov.hhs.fha.nhinc.hiemtopic'>nhinc:testTopic</wsnt:TopicExpression>";
            LOG.info("topic :" + topic);
            topicExpression = (TopicExpressionType) marshaller.unmarshal(topic);

            messageHolder.setTopic(topicExpression);

            notify.getNotificationMessage().add(messageHolder);

            notifyRequest.setAssertion(Util.createAssertion());

            notifyRequest.setNotify(notify);

            // The proxyhelper invocation casts exceptions to generic Exception, trying to use the default method
            // invocation
            AcknowledgementType result = port.notify(notifyRequest);

            LOG.info("Result = " + result);
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }

    private static void sendNotificationSecured(String contents, String topic) {
        try { // Call Web Service Operation
            String endpointURL = PropertyAccessor.getInstance().getProperty("adapter", "EntityNotificationConsumerURL");
            // String endpointURL = "http://localhost:8088/mockEntityNotificationConsumerBindingSoap11";
            LOG.info("EntitySubscriptionURL :" + endpointURL);

            SamlTokenCreator tokenCreator = new SamlTokenCreator();
            AssertionType assertion = Util.createAssertion();

            EntityNotificationConsumerSecuredPortType port = getSecuredPort(endpointURL, assertion);
            // TODO initialize WS operation arguments here
            NotificationMessageHolderType messageHolder = new NotificationMessageHolderType();

            NotificationMessageHolderType.Message message = new NotificationMessageHolderType.Message();
            Notify notify = new Notify();

            message.setAny(Util.marshalPayload(contents));

            messageHolder.setMessage(message);

            TopicExpressionType topicExpression;
            gov.hhs.fha.nhinc.hiem.dte.marshallers.TopicMarshaller marshaller;
            marshaller = new gov.hhs.fha.nhinc.hiem.dte.marshallers.TopicMarshaller();

            // topic =
            // "<wsnt:TopicExpression xmlns:wsnt='http://docs.oasis-open.org/wsn/b-2' Dialect='http://docs.oasis-open.org/wsn/t-1/TopicExpression/Simple' xmlns:nhinc='urn:gov.hhs.fha.nhinc.hiemtopic'>nhinc:testTopic</wsnt:TopicExpression>";
            LOG.info("topic :" + topic);
            topicExpression = (TopicExpressionType) marshaller.unmarshal(topic);

            messageHolder.setTopic(topicExpression);

            notify.getNotificationMessage().add(messageHolder);

            Map requestContext = tokenCreator.CreateRequestContext(assertion, endpointURL,
                    NhincConstants.PAT_CORR_ACTION);
            ((BindingProvider) port).getRequestContext().putAll(requestContext);

            AcknowledgementType result = port.notify(notify);

            LOG.info("Result = " + result);
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }

    protected static EntityNotificationConsumerPortType getPort(String url, AssertionType assertIn) {
        EntityNotificationConsumerPortType oPort = null;
        try {
            Service oService = getService(WSDL_FILE, NAMESPACE_URI, SERVICE_LOCAL_PART);

            if (oService != null) {
                LOG.debug("subscribe() Obtained service - creating port.");
                oPort = oService.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART),
                        EntityNotificationConsumerPortType.class);

                // Initialize secured port
                getWebServiceProxyHelper().initializeUnsecurePort((BindingProvider) oPort, url, WS_ADDRESSING_ACTION,
                        assertIn);
            } else {
                LOG.error("Unable to obtain serivce - no port created.");
            }
        } catch (Throwable t) {
            LOG.error("Error creating service: " + t.getMessage(), t);
        }
        return oPort;
    }

    protected static EntityNotificationConsumerSecuredPortType getSecuredPort(String url, AssertionType assertIn) {
        EntityNotificationConsumerSecuredPortType oPort = null;
        try {
            Service oService = getService(WSDL_FILE, NAMESPACE_URI, SERVICE_LOCAL_PART);

            if (oService != null) {
                LOG.debug("subscribe() Obtained service - creating port.");
                oPort = oService.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART),
                        EntityNotificationConsumerSecuredPortType.class);

                // Initialize secured port
                getWebServiceProxyHelper().initializeSecurePort((BindingProvider) oPort, url,
                        NhincConstants.HIEM_NOTIFY_ENTITY_SERVICE_NAME_SECURED, WS_ADDRESSING_ACTION, assertIn);
            } else {
                LOG.error("Unable to obtain service - no port created.");
            }
        } catch (Throwable t) {
            LOG.error("Error creating service: " + t.getMessage(), t);
        }
        return oPort;
    }

    private static WebServiceProxyHelper getWebServiceProxyHelper() {
        if (oProxyHelper == null) {
            oProxyHelper = new WebServiceProxyHelper();
        }
        return oProxyHelper;
    }

    private static Service getService(String wsdl, String uri, String service) {
        if (cachedService == null) {
            try {
                cachedService = getWebServiceProxyHelper().createService(wsdl, uri, service);
            } catch (Throwable t) {
                LOG.error("Error creating service: " + t.getMessage(), t);
            }
        }
        return cachedService;
    }

    private static Service getSecuredService(String wsdl, String uri, String service) {
        if (cachedSecuredService == null) {
            try {
                cachedSecuredService = getWebServiceProxyHelper().createService(wsdl, uri, service);
            } catch (Throwable t) {
                LOG.error("Error creating service: " + t.getMessage(), t);
            }
        }
        return cachedSecuredService;
    }
}
