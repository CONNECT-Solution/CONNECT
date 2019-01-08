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
package gov.hhs.fha.nhinc.direct.messagemonitoring.util;

import static gov.hhs.fha.nhinc.direct.DirectReceiverImpl.X_DIRECT_FINAL_DESTINATION_DELIVERY_HEADER_VALUE;
import gov.hhs.fha.nhinc.direct.edge.proxy.DirectEdgeProxy;
import gov.hhs.fha.nhinc.direct.edge.proxy.DirectEdgeProxyObjectFactory;
import gov.hhs.fha.nhinc.exchangemgr.ExchangeManagerException;
import gov.hhs.fha.nhinc.messaging.client.CONNECTCXFClientFactory;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClient;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.apache.commons.lang.StringUtils;
import org.nhind.config.ConfigurationService;
import org.nhind.config.Setting;
import org.nhindirect.common.tx.TxDetailParser;
import org.nhindirect.common.tx.TxUtil;
import org.nhindirect.common.tx.impl.DefaultTxDetailParser;
import org.nhindirect.common.tx.model.Tx;
import org.nhindirect.common.tx.model.TxDetail;
import org.nhindirect.common.tx.model.TxDetailType;
import org.nhindirect.common.tx.model.TxMessageType;
import org.nhindirect.gateway.smtp.GatewayState;
import org.nhindirect.stagent.AddressSource;
import org.nhindirect.stagent.NHINDAddress;
import org.nhindirect.stagent.NHINDAddressCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Message Monitoring utility class
 *
 * @author Naresh Subramanyan
 */
public class MessageMonitoringUtil {

    private static final Logger LOG = LoggerFactory.getLogger(MessageMonitoringUtil.class);

    private static final WebServiceProxyHelper oProxyHelper = new WebServiceProxyHelper();
    private static final Class<ConfigurationService> directConfigClazz = ConfigurationService.class;
    private static final String DIRECT_CONFIG_SERVICE_NAME = "directconfig";

    public static final String DISPOSITION_NOTIFICATION_OPTIONS_HEADER_NAME = "Disposition-Notification-Options";
    public static final String DISPOSITION_NOTIFICATION_PROCESSED = "automatic-action/mdn-sent-automatically;processed";
    public static final String DISPOSITION_NOTIFICATION_DISPATCHED
        = "automatic-action/MDN-sent-automatically;dispatched";
    public static final int DEFAULT_OUTBOUND_FAILED_MESSAGE_RETRY_COUNT = 1;
    public static final int DEFAULT_INBOUND_FAILED_MESSAGE_RETRY_COUNT = 1;
    public static final boolean DEFAULT_NOTIFIY_OUTBOUND_SECURITY_FAILURE_IMMEDIATE = true;
    public static final boolean DEFAULT_MESSAGE_MONITORING_ENABLED = true;
    public static final int DEFAULT_PROCESSED_MESSAGE_RECEIVE_TIME_LIMIT = 600000;
    public static final int DEFAULT_DISPATCHED_MESSAGE_RECEIVE_TIME_LIMIT = 1440000;
    public static final String DEFAULT_POSTMASTER_EMAIL_ID_PREFIX = "postmaster";

    public static final String DIRECT_SERVICE_NAME_GET_SETTINGS = "getAllSettings";
    public static final String DIRECT_SERVICE_NAME_GET_SETTING_BY_NAME = "getSettingByName";

    public static final String FAILED_MESSAGE_SUBJECT_PREFIX = "Email Delivery Failed: ";
    public static final String FAILED_MESSAGE_EMAIL_TEXT = "Email delivery failed for the recipient ";
    public static final String SUCCESSFUL_MESSAGE_SUBJECT_PREFIX = "Successfully Delivered: ";
    public static final String SUCCESSFUL_MESSAGE_EMAIL_TEXT = "Message successfully delivered to the recipient ";
    public static final String AGENT_SETTINGS_CACHE_REFRESH_TIME = "AgentSettingsCacheRefreshTime";
    public static final String AGENT_SETTINGS_CACHE_REFRESH_ACTIVE = "AgentSettingsCacheRefreshActive";

    private MessageMonitoringUtil() {
    }

    /**
     * Returns Mail Recipients as a NHINDAddressCollection
     *
     * @param message
     * @return
     * @throws javax.mail.MessagingException
     */
    protected static NHINDAddressCollection getMailRecipients(MimeMessage message) throws MessagingException {
        final NHINDAddressCollection recipients = new NHINDAddressCollection();

        if (message.getAllRecipients() == null) {
            return recipients;
        }

        for (InternetAddress address : (InternetAddress[]) message.getAllRecipients()) {
            String emailId = address.getAddress();
            recipients.add(new NHINDAddress(emailId, (AddressSource) null));
        }
        return recipients;
    }

    protected static Tx getTxToTrack(MimeMessage msg, String sender, NHINDAddressCollection recipients) {

        final TxDetailParser txParser = new DefaultTxDetailParser();

        try {

            final Map<String, TxDetail> details = txParser.getMessageDetails(msg);

            if (sender != null) {
                details.put(TxDetailType.FROM.getType(),
                    new TxDetail(TxDetailType.FROM, sender.toLowerCase(Locale.getDefault())));
            }
            if (recipients != null && !recipients.isEmpty()) {
                details.put(TxDetailType.RECIPIENTS.getType(),
                    new TxDetail(TxDetailType.RECIPIENTS, recipients.toString().toLowerCase(Locale.getDefault())));
            }

            return new Tx(TxUtil.getMessageType(msg), details);
        } catch (Exception e) {
            LOG.error("Failed to parse message to Tx object: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * Returns parent message id from the message
     *
     * @param msg
     * @return
     */
    public static String getParentMessageId(MimeMessage msg) {
        final TxDetailParser txParser = new DefaultTxDetailParser();
        String parentMessageId = getOriginalMessageId(convertMimeMessageToTx(msg, txParser));
        if (NullChecker.isNullish(parentMessageId)) {
            try {
                String[] value = msg.getHeader(org.nhindirect.common.mail.MDNStandard.Headers.OriginalMessageID);
                if (value != null && value.length > 0) {
                    return value[0];
                }
            } catch (MessagingException ex) {
                LOG.error("Error getting message ID: {}", ex.getMessage(), ex);
                return null;
            }
        }
        return parentMessageId;
    }

    /**
     * Returns true of the incoming message is Processed MDN
     *
     * @param msg
     * @return
     */
    public static boolean isIncomingMessageMDNProcessed(MimeMessage msg) {
        final TxDetailParser txParser = new DefaultTxDetailParser();
        Tx tx = convertMimeMessageToTx(msg, txParser);
        final TxMessageType type = tx.getMsgType();
        if (type != TxMessageType.DSN && type != TxMessageType.MDN) {
            return false;
        }

        final TxDetail detail = tx.getDetail(TxDetailType.DISPOSITION);
        return detail != null && !detail.getDetailValue().isEmpty()
            && detail.getDetailValue().equalsIgnoreCase(DISPOSITION_NOTIFICATION_PROCESSED);
    }

    /**
     * Returns true of the incoming message is Dispatched MDN
     *
     * @param msg
     * @return
     */
    public static boolean isIncomingMessageMDNDispatched(MimeMessage msg) {
        final TxDetailParser txParser = new DefaultTxDetailParser();
        Tx tx = convertMimeMessageToTx(msg, txParser);
        final TxMessageType type = tx.getMsgType();
        if (type != TxMessageType.DSN && type != TxMessageType.MDN) {
            return false;
        }

        final TxDetail detail = tx.getDetail(TxDetailType.DISPOSITION);
        return detail != null && !detail.getDetailValue().isEmpty()
            && detail.getDetailValue().equalsIgnoreCase(DISPOSITION_NOTIFICATION_DISPATCHED);
    }

    /**
     * Returns the Original Message Id of the outbound notification
     *
     * @param tx
     * @return
     */
    public static String getOriginalMessageId(Tx tx) {
        final TxMessageType type = tx.getMsgType();
        if (type != TxMessageType.DSN && type != TxMessageType.MDN) {
            return "";
        }

        final TxDetail detail = tx.getDetail(TxDetailType.PARENT_MSG_ID);
        return detail != null && !detail.getDetailValue().isEmpty() ? detail.getDetailValue() : "";

    }

    /**
     * Converts a MimeMessage to a Tx message
     *
     * @param msg
     * @param parser
     * @return
     */
    protected static Tx convertMimeMessageToTx(MimeMessage msg, TxDetailParser parser) {
        if (msg == null) {
            throw new IllegalArgumentException("Invalid parameter received. Message cannot be null.");
        }

        final Map<String, TxDetail> details = parser.getMessageDetails(msg);
        return new Tx(TxUtil.getMessageType(msg), details);
    }

    /**
     * Returns true if the edge requested for notification else false.
     *
     * @param message
     * @return
     */
    public static boolean isNotificationRequestedByEdge(MimeMessage message) {
        String[] headers = null;
        try {
            headers = message.getHeader(DISPOSITION_NOTIFICATION_OPTIONS_HEADER_NAME);
        } catch (MessagingException ex) {
            LOG.error("Failed getting message header: {}", ex.getMessage(), ex);
        }
        if (headers != null) {
            for (String header : headers) {
                if (checkHeaderForDispatchedRequest(header)) {
                    return true;
                }

            }
        }
        return false;
    }

    /**
     * Returns true if the edge requested for notification else false.
     *
     * @param header
     * @return
     */
    public static boolean checkHeaderForDispatchedRequest(String header) {
        // Need a better approach to verify the value
        return StringUtils.contains(header, X_DIRECT_FINAL_DESTINATION_DELIVERY_HEADER_VALUE)
            && StringUtils.contains(header, "true");
    }

    /**
     * Returns the Outbound Failed message retry count. Reads the system property first, if not found then reads the
     * gateway properties, if not found then uses the default value.
     *
     * @return
     */
    public static int getOutboundFailedMessageRetryCount() {
        int messageRetryCount = getPropertyIntegerValue("OutboundFailedMessageRetryCount");
        LOG.info("Outbound Failed Message Retry Count {}", messageRetryCount);
        if (messageRetryCount >= 0) {
            return messageRetryCount;
        }
        return DEFAULT_OUTBOUND_FAILED_MESSAGE_RETRY_COUNT;
    }

    /**
     * Returns the Inbound Failed message retry count. Reads the system property first, if not found then reads the
     * gateway properties, if not found then uses the default value.
     *
     * @return
     */
    public static int getInboundFailedMessageRetryCount() {
        String messageRetryCount = getSetting("INBOUND_FAILED_MESSAGE_RETRY_COUNT");
        LOG.info("Inbound Failed Message Retry Count " + messageRetryCount);
        if (messageRetryCount != null) {
            return Integer.parseInt(messageRetryCount);
        }
        // If not found, then use the default value
        return DEFAULT_INBOUND_FAILED_MESSAGE_RETRY_COUNT;
    }

    /**
     * Return true if the notification to edge needs to be sent immediately. Reads the system property first, if not
     * found then reads the gateway properties, if not found then uses the default value.
     *
     * @return
     */
    public static boolean isNotifyOutboundSecurityFailureImmediate() {
        String notifySecurityFailure = getPropertyStringValue("NotifyOutboundSecurityFailureImmediate");
        LOG.info("isNotifyOutboundSecurityFailureImmediate() " + notifySecurityFailure);
        if (notifySecurityFailure == null) {
            return DEFAULT_NOTIFIY_OUTBOUND_SECURITY_FAILURE_IMMEDIATE;
        }
        // If not found, then use the default value
        return "true".equals(notifySecurityFailure);
    }

    /**
     * Returns the Processed message receive time limit. Reads the system property first, if not found then reads the
     * gateway properties, if not found then uses the default value.
     *
     * @return
     */
    public static int getProcessedMessageReceiveTimeLimit() {
        int processedReceiveTimeLimit = getPropertyIntegerValue("ProcessedMessageReceiveTimeLimit");
        LOG.info("processedReceiveTimeLimit " + processedReceiveTimeLimit);
        // If not found, then use the default value
        return processedReceiveTimeLimit >= 0 ? processedReceiveTimeLimit
            : DEFAULT_PROCESSED_MESSAGE_RECEIVE_TIME_LIMIT;
    }

    /**
     * Returns the Processed message receive time limit. Reads the system property first, if not found then reads the
     * gateway properties, if not found then uses the default value.
     *
     * @return
     */
    public static String getDomainPostmasterEmailId() {
        String postmasterEmailPrefix = getPropertyStringValue("PostmasterEmailIdPrefix");
        LOG.info("postmasterEmailPrefix {}", postmasterEmailPrefix);
        if (postmasterEmailPrefix != null) {
            return postmasterEmailPrefix;
        }
        // If not found, then use the default value
        return DEFAULT_POSTMASTER_EMAIL_ID_PREFIX;
    }

    /**
     * Returns the Dispatched message receive time limit. Reads the system property first, if not found then reads the
     * gateway properties, if not found then uses the default value.
     *
     * @return
     */
    public static int getDispatchedMessageReceiveTimeLimit() {
        int dispatchedReceiveTimeLimit = getPropertyIntegerValue("DispatchedMessageReceiveTimeLimit");
        LOG.info("dispatchedReceiveTimeLimit {}", dispatchedReceiveTimeLimit);
        // If not found, then use the default value
        return dispatchedReceiveTimeLimit >= 0 ? dispatchedReceiveTimeLimit
            : DEFAULT_DISPATCHED_MESSAGE_RECEIVE_TIME_LIMIT;
    }

    public static boolean isMessageMonitoringEnabled() {
        String messageMonitoringEnabled = getPropertyStringValue("MessageMonitoringEnabled");
        if (messageMonitoringEnabled == null) {
            return DEFAULT_MESSAGE_MONITORING_ENABLED;
        } else {
            return "true".equals(messageMonitoringEnabled);
        }
    }

    /**
     * Return true if the message is a DSN or MDN
     *
     * @param message containing the message to be tested.
     * @return true if the envelope exists, the message exists and is an MDN Notification or a DSN notification.
     */
    public static boolean isMdnOrDsn(MimeMessage message) {
        return TxUtil.getMessageType(message).equals(TxMessageType.DSN)
            || TxUtil.getMessageType(message).equals(TxMessageType.MDN);
    }

    /**
     * Returns true if the Processed MDN is not received on time
     *
     * @param createTime message sent time
     * @return
     */
    public static boolean isProcessedMDNReceiveTimeLapsed(Date createTime) {
        // check if the currenttime - createTime > the time limit
        long diff = new Date().getTime() - createTime.getTime();
        LOG.info("Processed MDN time difference:{}", diff);
        LOG.info("getProcessedMessageReceiveTimeLimit -->{}", getProcessedMessageReceiveTimeLimit());
        return diff >= getProcessedMessageReceiveTimeLimit();
    }

    /**
     * Returns true if the Dispatched MDN is not received on time
     *
     * @param createTime message sent time
     * @return
     */
    public static boolean isDispatchedMDNReceiveTimeLapsed(Date createTime) {
        // check if the currenttime - createTime > the time limit
        long diff = new Date().getTime() - createTime.getTime();
        return diff >= getDispatchedMessageReceiveTimeLimit();
    }

    /**
     * Returns a MimeMessage.
     *
     * @param postMasterEmailId
     * @param subject
     * @param recipient
     * @param text
     * @param messageId
     * @return
     * @throws AddressException
     * @throws MessagingException
     */
    public static MimeMessage createMimeMessage(String postMasterEmailId, String subject, String recipient, String text,
        String messageId) throws MessagingException {
        MimeMessage message = new MimeMessage((Session) null);
        message.setSender(new InternetAddress(postMasterEmailId));
        message.setSubject(subject);
        message.setRecipient(RecipientType.TO, new InternetAddress(recipient));
        message.setText(text);
        message.setFrom(new InternetAddress(postMasterEmailId));
        message.setSentDate(new Date());
        message.setHeader(org.nhindirect.common.mail.MDNStandard.Headers.OriginalMessageID, messageId);
        return message;
    }

    /**
     * Returns the default failed message subject prefix
     *
     * @return
     */
    public static String getFailedMessageSubjectPrefix() {
        String failedMessageSubjectPrefix = getPropertyStringValue("FailedMessageSubjectPrefix");
        LOG.info("failedMessageSubjectPrefix {}", failedMessageSubjectPrefix);
        if (failedMessageSubjectPrefix != null) {
            return failedMessageSubjectPrefix;
        }
        return FAILED_MESSAGE_SUBJECT_PREFIX;
    }

    /**
     * Returns the default failed message email text
     *
     * @return
     */
    public static String getFailedMessageEmailText() {
        String failedMessageEmailText = getPropertyStringValue("FailedMessageEmailText");
        LOG.info("failedMessageEmailText {}", failedMessageEmailText);
        if (failedMessageEmailText != null) {
            return failedMessageEmailText;
        }
        return FAILED_MESSAGE_EMAIL_TEXT;
    }

    /**
     * Returns the default successful message subject prefix
     *
     * @return
     */
    public static String getSuccessfulMessageSubjectPrefix() {
        String successfulMessageSubjectPrefix = getPropertyStringValue("SuccessfulMessageSubjectPrefix");
        LOG.info("successfulMessageSubjectPrefix {}", successfulMessageSubjectPrefix);
        if (successfulMessageSubjectPrefix != null) {
            return successfulMessageSubjectPrefix;
        }
        return SUCCESSFUL_MESSAGE_SUBJECT_PREFIX;
    }

    /**
     * Returns the default successful message email text
     *
     * @return
     */
    public static String getSuccessfulMessageEmailText() {
        String successfulMessageEmailText = getPropertyStringValue("SuccessfulMessageEmailText");
        LOG.info("successfulMessageEmailText {}", successfulMessageEmailText);
        if (successfulMessageEmailText != null) {
            return successfulMessageEmailText;
        }
        return SUCCESSFUL_MESSAGE_EMAIL_TEXT;
    }

    /**
     * Get the Direct edge Proxy
     *
     * @return DirectEdgeProxy implementation to handle the direct edge
     */
    public static DirectEdgeProxy getDirectEdgeProxy() {
        DirectEdgeProxyObjectFactory factory = new DirectEdgeProxyObjectFactory();
        return factory.getDirectEdgeProxy();
    }

    /**
     * Returns the domain name from an emailId.
     *
     * @param emailId
     * @return
     */
    public static String getDomainFromEmail(String emailId) {
        if (emailId != null) {
            String[] emailSplit = emailId.split("@");
            if (emailSplit.length >= 2) {
                return emailSplit[1];
            }
        }
        return null;
    }

    /**
     * Get the property value from Setting. Calls the config service to get the value.
     *
     * @param propertyName
     * @return
     *
     */
    public static String getSetting(String propertyName) {
        try {
            Setting setting = (Setting) getClient().invokePort(directConfigClazz,
                DIRECT_SERVICE_NAME_GET_SETTING_BY_NAME, propertyName);
            // if setting is not null
            if (setting != null) {
                return setting.getValue();
            }
        } catch (Exception ex) {
            LOG.error("Failed to call getSetting : {}", ex.getMessage(), ex);
            return null;
        }
        return null;
    }

    /**
     * Returns all the settings by calling the config service.
     *
     * @return
     *
     */
    public static List<Setting> getSettings() {
        try {
            return (List<Setting>) getClient().invokePort(directConfigClazz, DIRECT_SERVICE_NAME_GET_SETTINGS);

        } catch (Exception ex) {
            // log a message
            LOG.error("Failed to call getSetttings: {} ", ex.getMessage(), ex);
            return null;
        }
    }

    /**
     * Returns an unsecured Direct WebService client
     *
     * @return
     *
     */
    private static CONNECTClient<ConfigurationService> getClient() throws ExchangeManagerException {
        // get the direct config URL
        String url = oProxyHelper.getAdapterEndPointFromConnectionManager(DIRECT_CONFIG_SERVICE_NAME);
        LOG.debug("Direct config URL: {}", url);
        ServicePortDescriptor<ConfigurationService> portDescriptor = new DirectConfigUnsecuredServicePortDescriptor();

        return CONNECTCXFClientFactory.getInstance().getCONNECTClientUnsecured(portDescriptor, url, null);
    }

    private static String getPropertyStringValue(String propertyName) {
        try {
            return PropertyAccessor.getInstance().getProperty(NhincConstants.GATEWAY_PROPERTY_FILE, propertyName);
        } catch (PropertyAccessException ex) {
            LOG.info("Property Not found for {}: {}", propertyName, ex.getMessage(), ex);
            return null;
        }
    }

    private static int getPropertyIntegerValue(String propertyName) {
        try {
            return Integer.parseInt(
                PropertyAccessor.getInstance().getProperty(NhincConstants.GATEWAY_PROPERTY_FILE, propertyName));
        } catch (PropertyAccessException ex) {
            LOG.info("Property Not found in getPropertyIntegerValue() for {}: {}", propertyName, ex.getMessage(), ex);
            return -1;
        } catch (NumberFormatException n) {
            LOG.info("Invalid value for the Property: {}", propertyName);
            return -1;
        }
    }

    /**
     * Update the Agent Settings Cache
     */
    public static void updateAgentSettingsCacheTimeoutValue() {
        GatewayState gatewayState = GatewayState.getInstance();
        // Read the agentSettingsCacheRefreshTime property from gateway.properties
        int agentSettingsCacheRefreshTime = getAgentSettingsCacheRefreshTime();
        LOG.trace("AgentSettingsCacheRefreshTime agentSettingsCacheRefreshTime value:{}", agentSettingsCacheRefreshTime);
        boolean refreshRequired = gatewayState.getSettingsUpdateInterval() != agentSettingsCacheRefreshTime / 1000;

        // set the Settings Update Interval value only if the proeprty value retrieved is greater than zero
        if (agentSettingsCacheRefreshTime > 0 && refreshRequired && gatewayState.isAgentSettingManagerRunning()) {
            gatewayState.stopAgentSettingsManager();
            gatewayState.setSettingsUpdateInterval(agentSettingsCacheRefreshTime / 1000);
            gatewayState.startAgentSettingsManager();
            LOG.trace("Abent Settings Cache refreshed..");
        }
    }

    /**
     * Returns the AgentSettingsCacheRefreshTime property value from Gateway properties file. If not found, returns -1
     *
     * @return
     */
    public static int getAgentSettingsCacheRefreshTime() {
        try {
            if (getAgentSettingsCacheRefreshActive()) {
                return Integer.parseInt(PropertyAccessor.getInstance().getProperty(NhincConstants.GATEWAY_PROPERTY_FILE,
                    AGENT_SETTINGS_CACHE_REFRESH_TIME));
            }
            return -1;
        } catch (PropertyAccessException ex) {
            LOG.info("Proeprty Not found for {}: {}", AGENT_SETTINGS_CACHE_REFRESH_TIME, ex.getMessage(), ex);
            return -1;
        } catch (NumberFormatException n) {
            LOG.info("Invalid value for the Proeprty for {}: {}", AGENT_SETTINGS_CACHE_REFRESH_TIME, n.getMessage(), n);
            return -1;
        }
    }

    /**
     * Returns the AgentSettingsCacheRefreshActive property value from Gateway properties file. If not found, returns
     * false
     *
     * @return
     */
    public static boolean getAgentSettingsCacheRefreshActive() {
        try {
            String agentSettingsCacheRefreshActive = PropertyAccessor.getInstance()
                .getProperty(NhincConstants.GATEWAY_PROPERTY_FILE, AGENT_SETTINGS_CACHE_REFRESH_ACTIVE);
            return agentSettingsCacheRefreshActive != null && "true".equals(agentSettingsCacheRefreshActive);
        } catch (PropertyAccessException ex) {
            LOG.error("Property Not found in getAgentSettingsCacheRefreshActive() for {}: {}",
                AGENT_SETTINGS_CACHE_REFRESH_ACTIVE, ex.getMessage(), ex);
            return false;
        } catch (NumberFormatException n) {
            LOG.info("Invalid value for the Proeprty for {}: {}", AGENT_SETTINGS_CACHE_REFRESH_ACTIVE, n.getMessage(),
                n);
            return false;
        }
    }
}
