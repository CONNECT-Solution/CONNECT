/*
 * Copyright (c) 2009-2019, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.direct;

import gov.hhs.fha.nhinc.direct.edge.proxy.DirectEdgeProxy;
import gov.hhs.fha.nhinc.direct.edge.proxy.DirectEdgeProxyObjectFactory;
import gov.hhs.fha.nhinc.direct.event.DirectEventLogger;
import gov.hhs.fha.nhinc.direct.messagemonitoring.util.MessageMonitoringUtil;
import gov.hhs.fha.nhinc.exchangemgr.ExchangeManagerException;
import gov.hhs.fha.nhinc.mail.MailSender;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import java.net.MalformedURLException;
import java.net.URL;
import javax.mail.internet.MimeMessage;
import org.nhindirect.gateway.smtp.GatewayState;
import org.nhindirect.gateway.smtp.MessageProcessResult;
import org.nhindirect.gateway.smtp.SmtpAgent;
import org.nhindirect.gateway.smtp.SmtpAgentFactory;
import org.nhindirect.gateway.smtp.config.SmptAgentConfigFactory;
import org.nhindirect.stagent.MessageEnvelope;
import org.nhindirect.stagent.NHINDAddressCollection;
import org.nhindirect.stagent.mail.notifications.NotificationMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class adapts the Direct code responsible for processing messages.
 */
public abstract class DirectAdapter {

    private static final Logger LOG = LoggerFactory.getLogger(DirectAdapter.class);

    private final WebServiceProxyHelper oProxyHelper = new WebServiceProxyHelper();

    static final String DIRECT_CONFIG_SERVICE_NAME = "directconfig";

    private final MailSender externalMailSender;
    private SmtpAgent smtpAgent = null;
    private final DirectEventLogger directEventLogger;

    /**
     * @param externalMailSender external mail sender.
     * @param directEventLogger used to log events.
     */
    public DirectAdapter(MailSender externalMailSender, DirectEventLogger directEventLogger) {
        this.externalMailSender = externalMailSender;
        this.directEventLogger = directEventLogger;
    }

    /**
     * Process a direct message and return {@link MessageProcessResult}. If the returned result is null, a Direct Error
     * event is created, and a DirectException is thrown. If the processed message envelope returned is null, a Direct
     * Error event is created with the notification messages if they are available. Then a {@link DirectException} is
     * thrown. This method is guaranteed to return a populated result, otherwise it throws a DirectException.
     *
     * @param message (mime) to be processed.
     * @return message process result, populated with a processed message envelope.
     */
    protected MessageProcessResult process(MimeMessage message) {

        MessageProcessResult result = processAsDirectMessage(message);
        MessageEnvelope envelope = result.getProcessedMessage();
        if (envelope == null) {
            throw new DirectException("Result Message Envelope is null: " + getErrorNotificationMsgs(result), message);
        }

        return result;
    }

    /**
     * The DirectConfig service endpoint is read from internalConnectionInfo and is converted as URL which is the input
     * argument type for SMPTAgentFactory which creates Agent.
     *
     * @return URL, the directConfig url string is converted into URL format.
     */
    private URL getUrl() {
        String urlString = null;
        URL url = null;
        try {
            urlString = oProxyHelper.getAdapterEndPointFromConnectionManager(DIRECT_CONFIG_SERVICE_NAME);
        } catch (ExchangeManagerException ex) {
            LOG.error("Error reading directConfig Url cannot be found: {}", ex.getLocalizedMessage(), ex);
        }
        try {
            url = new URL(urlString);
        } catch (MalformedURLException ex) {
            LOG.error("Error while converting urlString into Url format {}", ex.getLocalizedMessage(), ex);
        }
        return url;
    }

    private MessageProcessResult processAsDirectMessage(MimeMessage mimeMessage) {
        MessageProcessResult result;
        try {
            NHINDAddressCollection collection = DirectAdapterUtils.getNhindRecipients(mimeMessage);
            URL url = getUrl();
            smtpAgent = getSmtpAgent(url);
            result = smtpAgent.processMessage(mimeMessage, collection, DirectAdapterUtils.getNhindSender(mimeMessage));
        } catch (Exception e) {
            String errorString = "Error occurred while processing message.";
            LOG.error(errorString, e);
            throw new DirectException(errorString, e, mimeMessage);
        }

        if (result == null) {
            throw new DirectException("Message Process Result by Direct is null.", mimeMessage);
        }
        return result;
    }

    /**
     * The SmtpAgent is created after the deployment of Direct. Whenever the DirectSender/DirectReceiver is
     * initiated the SmtpAgent is created and the aAgent is used for further processing of Direct Messages. The
     * SmtpAgent injection is removed from direct.beans.xml whereas ConfigurationService url can be configured in
     * internalConnectionInfo.xml like any other CONNECT service.
     *
     * Also starts the Agent Settings Manager to refresh agent settings cache which stores information related to
     * certificates, anchors, domains, settings etc. The default cache refresh time is 5 minutes and can be overridden
     * using the gateway property AgentSettingsCacheRefreshTime.
     *
     * @param url the directConfig Service url String converted into URL format.
     * @return SmtpAgent SmtpAgent created to process Direct Messages.
     */
    protected SmtpAgent getSmtpAgent(URL url) {

        if (smtpAgent != null) {
            return smtpAgent;
        } else {
            smtpAgent = SmtpAgentFactory.createAgent(url);
            if (MessageMonitoringUtil.getAgentSettingsCacheRefreshActive()) {
                GatewayState gatewayState = GatewayState.getInstance();
                // Update timeout seconds
                MessageMonitoringUtil.updateAgentSettingsCacheTimeoutValue();
                // start Agent Settings Manager
                // set smtpAgent and smtp agent config
                gatewayState.setSmtpAgent(smtpAgent);
                gatewayState.setSmptAgentConfig(SmptAgentConfigFactory.createSmtpAgentConfig(url, null, null));
                // if the agent settings manager is running then stop it and start it again
                if (gatewayState.isAgentSettingManagerRunning()) {
                    LOG.debug("Stop Agent Settings Manager");
                    gatewayState.stopAgentSettingsManager();
                }
                // start the Agent Settings Manager
                LOG.debug("Start Agent Settings Manager");
                gatewayState.startAgentSettingsManager();
            } else {
                LOG.debug("Agent Settings Manager not enabled");
            }
        }
        return smtpAgent;
    }

    /**
     * @return DirectEdgeProxy implementation to handle the direct edge
     */
    protected DirectEdgeProxy getDirectEdgeProxy() {
        DirectEdgeProxyObjectFactory factory = new DirectEdgeProxyObjectFactory();
        return factory.getDirectEdgeProxy();
    }

    /**
     * Log any notification messages that were produced by direct processing.
     *
     * @param result to pull notification messages from
     * @return String representation of notification messages from the result.
     */
    protected String getErrorNotificationMsgs(MessageProcessResult result) {
        StringBuilder builder = new StringBuilder("Inbound Mime Message could not be processed by DIRECT.");
        for (NotificationMessage notif : result.getNotificationMessages()) {
            try {
                builder.append(" " + notif.getContent());
            } catch (Exception e) {
                LOG.warn("Exception while logging notification messages.", e);
            }
        }
        return builder.toString();
    }

    /**
     * @return the externalMailSender
     */
    protected MailSender getExternalMailSender() {
        return externalMailSender;
    }

    /**
     * @return the smtpAgent
     */
    protected SmtpAgent getSmtpAgent() {
        return smtpAgent;
    }

    /**
     * @return the directEventLogger
     */
    protected DirectEventLogger getDirectEventLogger() {
        return directEventLogger;
    }

}
