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
package gov.hhs.fha.nhinc.direct.messagemonitoring.impl;

import gov.hhs.fha.nhinc.direct.edge.proxy.DirectEdgeProxy;
import gov.hhs.fha.nhinc.direct.event.DirectEventLogger;
import gov.hhs.fha.nhinc.direct.event.DirectEventType;
import gov.hhs.fha.nhinc.direct.messagemonitoring.dao.MessageMonitoringDAO;
import gov.hhs.fha.nhinc.direct.messagemonitoring.dao.MessageMonitoringDAOException;
import gov.hhs.fha.nhinc.direct.messagemonitoring.dao.impl.MessageMonitoringDAOImpl;
import gov.hhs.fha.nhinc.direct.messagemonitoring.domain.MonitoredMessage;
import gov.hhs.fha.nhinc.direct.messagemonitoring.domain.MonitoredMessageNotification;
import gov.hhs.fha.nhinc.direct.messagemonitoring.util.MessageMonitoringUtil;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.apache.commons.lang.StringUtils;
import org.nhindirect.common.tx.TxUtil;
import org.nhindirect.common.tx.model.TxMessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * All the Message Monitoring API services are exposed through this class. This class will maintain a cache to store all
 * the active messages that are sent out waiting for response.
 *
 * @author Naresh Subramanyan
 */
public class MessageMonitoringAPI {

    private static final Logger LOG = LoggerFactory.getLogger(MessageMonitoringAPI.class);
    // messageId is the key and Trackmessage object the value
    Map<String, MonitoredMessage> messageMonitoringCache = null;
    private static final String STATUS_PENDING = "Pending";
    private static final String STATUS_ERROR = "Error";
    private static final String STATUS_COMPLETED = "Completed";
    private static final String STATUS_PROCESSED = "Processed";
    private static final String STATUS_ARCHIVED = "Archived";
    private static final String MSG_MONITORING_NOT_ENABLED = "Message Monitoring is not enabled.";

    public MessageMonitoringAPI() {
        // set the default value
        messageMonitoringCache = new HashMap<>();
        // Load the cahce from the database
        buildCache();
    }

    private static class SingletonHolder {

        public static final MessageMonitoringAPI INSTANCE = new MessageMonitoringAPI();

        private SingletonHolder() {
        }
    }

    public static MessageMonitoringAPI getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void updateIncomingMessageNotificationStatus(final MimeMessage message) {
        // Always
        // check if the message monitoring is enabled
        if (!MessageMonitoringUtil.isMessageMonitoringEnabled()) {
            LOG.debug(MSG_MONITORING_NOT_ENABLED);
            return;
        }
        LOG.debug("Message Monitoring is enabled.");

        try {
            // find out if its a successful or failed message
            final boolean isMDNProcessed = MessageMonitoringUtil.isIncomingMessageMDNProcessed(message);
            final boolean isMDNDispatched = MessageMonitoringUtil.isIncomingMessageMDNDispatched(message);

            final String parentMessageId = MessageMonitoringUtil.getParentMessageId(message);
            final MonitoredMessage tm = messageMonitoringCache.get(parentMessageId);

            // if the message is not there then ignore it for now
            if (tm == null) {
                LOG.debug("Not able to find the message in the cache..may be its a incoming message or junk message");
                return;
            }

            // ignore any message that comes if the status not in Pending
            if (!(tm.getStatus().equalsIgnoreCase(STATUS_PENDING)
                || tm.getStatus().equalsIgnoreCase(STATUS_PROCESSED))) {
                return;
            }
            // get the mail sender

            final String senderMailId = getSenderEmailId(message);

            MonitoredMessageNotification tmn = getTrackmessagenotification(tm, senderMailId);
            if (tmn == null) {
                tmn = new MonitoredMessageNotification();
            }
            // check if its a MDN or DSN
            // if its an DSN then set the status to Error
            if (TxUtil.getMessageType(message).equals(TxMessageType.DSN)) {
                tmn.setStatus(STATUS_ERROR);
                tm.setStatus(STATUS_ERROR);
            } // if its an MDN and also if delivery is requested
            else if (tm.getDeliveryrequested()) {
                // Update only if MDN is dispatched
                if (isMDNDispatched) {
                    // update the status to completed
                    tmn.setStatus(STATUS_COMPLETED);
                } else { // Update the status to Processed
                    tmn.setStatus(STATUS_PROCESSED);
                }
            } else if (isMDNDispatched || isMDNProcessed) {
                tmn.setStatus(STATUS_COMPLETED);
            } else { // if error
                tmn.setStatus(STATUS_ERROR);
            }
            final Date updatedTime = new Date();
            String incomingMsgReceivedStatus = getIncomingMessagesReceivedStatus(tm);
            if (incomingMsgReceivedStatus.equalsIgnoreCase(STATUS_PENDING)
                || incomingMsgReceivedStatus.equalsIgnoreCase(STATUS_PROCESSED)) {
                tmn.setUpdatetime(updatedTime);
                getMessageMonitoringDAO().updateMessageNotification(tmn);
            } else {
                // set the status to Completed or Error
                tm.setStatus(incomingMsgReceivedStatus);
                tm.setUpdatetime(updatedTime);
                getMessageMonitoringDAO().updateOutgoingMessage(tm);
            }
        } catch (final MessagingException | MessageMonitoringDAOException ex) {
            LOG.info("Failed:{}", ex.getLocalizedMessage(), ex);
        }
    }

    public void addOutgoingMessage(final MimeMessage message, final boolean failed) {
        // Always check if message monitoring enabled
        if (!MessageMonitoringUtil.isMessageMonitoringEnabled()) {
            LOG.debug(MSG_MONITORING_NOT_ENABLED);
            return;
        }
        try {
            // get the all recipients
            final InternetAddress recipients[] = (InternetAddress[]) message.getAllRecipients();
            final String senderMailId = getSenderEmailId(message);
            // Mail Subject
            final String mailSubject = message.getSubject();
            // get the message id
            final String messageId = message.getMessageID();
            // created time
            final Date createdTime = new Date();

            final boolean deliveryRequested = MessageMonitoringUtil.isNotificationRequestedByEdge(message);
            // Create the track Message domain
            final MonitoredMessage tm = new MonitoredMessage();
            tm.setSubject(mailSubject);
            tm.setSenderemailid(senderMailId);
            tm.setMessageid(messageId);
            tm.setStatus(failed ? STATUS_ERROR : STATUS_PENDING);
            tm.setDeliveryrequested(deliveryRequested);
            tm.setCreatetime(createdTime);
            tm.setUpdatetime(createdTime);
            final ArrayList recipientsList = new ArrayList();
            final Set messageNotificationSet = new HashSet();
            for (final InternetAddress address : recipients) {
                final String emailId = address.getAddress();
                // create the track message notification objects
                final MonitoredMessageNotification tmn = new MonitoredMessageNotification();
                tmn.setCreatetime(createdTime);
                tmn.setUpdatetime(createdTime);
                tmn.setEmailid(emailId);
                tmn.setStatus(failed ? STATUS_ERROR : STATUS_PENDING);
                messageNotificationSet.add(tmn);
                tmn.setMonitoredmessage(tm);
                recipientsList.add(emailId);
            }
            tm.setRecipients(StringUtils.join(recipientsList, ","));
            tm.setMonitoredmessagenotifications(messageNotificationSet);

            // call the dao to persist the date
            getMessageMonitoringDAO().addOutgoingMessage(tm);

            messageMonitoringCache.put(messageId, tm);

        } catch (final MessagingException | MessageMonitoringDAOException ex) {
            LOG.info("Failed: {}", ex.getLocalizedMessage(), ex);
        }
    }

    /**
     * Build the Message Monitoring cache from the database tables. This will be called when the module is initiated.
     * <p>
     */
    private void buildCache() {
        LOG.debug("Inside buildCache");
        // Always check if message monitoring enabled
        if (!MessageMonitoringUtil.isMessageMonitoringEnabled()) {
            LOG.debug(MSG_MONITORING_NOT_ENABLED);
            return;
        }
        LOG.debug("Message Monitoring is enabled.");
        // get all the Pending rows and add it to the cache
        final List<MonitoredMessage> pendingMessages = getAllPendingMessagesFromDatabase();
        LOG.debug("Total cache rows from database: {}", pendingMessages.size());
        // clear the cache before loading the data from database
        clearCache();
        // load the pending outgoing messages to the cache
        for (final MonitoredMessage trackMessage : pendingMessages) {
            if (!trackMessage.getStatus().equals(STATUS_ARCHIVED)) {
                messageMonitoringCache.put(trackMessage.getMessageid(), trackMessage);
                LOG.debug("Total child rows for the messageId: {}", trackMessage.getMonitoredmessagenotifications().
                    size());
            } else {
                deleteElapsedArchivedMessage(trackMessage);
            }
        }
        LOG.debug("Exiting buildCache.");
    }

    /**
     * Clear the cache.
     */
    public void clearCache() {
        messageMonitoringCache = new HashMap();
    }

    /**
     * Returns all the Successfully Completed outbound messages.
     *
     * @return List
     */
    public List<MonitoredMessage> getAllCompletedMessages() {
        final List<MonitoredMessage> completedMessages = new ArrayList<>();
        // loop through the cache
        for (final MonitoredMessage trackMessage : messageMonitoringCache.values()) {
            if (trackMessage.getStatus().equals(STATUS_COMPLETED)) {
                completedMessages.add(trackMessage);
            }
        }
        return completedMessages;
    }

    /**
     * Returns all the Successfully Completed outbound messages.
     *
     * @return List
     */
    public List<MonitoredMessage> getAllPendingMessages() {
        final List<MonitoredMessage> pendingMessages = new ArrayList<>();
        // loop through the cache
        for (final MonitoredMessage trackMessage : messageMonitoringCache.values()) {
            if (trackMessage.getStatus().equals(STATUS_PENDING)) {
                pendingMessages.add(trackMessage);
            }
        }
        return pendingMessages;
    }

    /**
     * Returns all the Successfully Completed outbound messages.
     *
     * @return List
     */
    public List<MonitoredMessage> getAllPendingMessagesFromDatabase() {
        return getMessageMonitoringDAO().getAllPendingMessages();
    }

    /**
     * Returns all the Failed outbound messages. 1. Processed not received for one or more recipients 2. Dispatched not
     * received for one or more recipients if notification requested by edge. 3. Got Failed DSN/MDN for one or more
     * recipients
     *
     * @return List
     */
    public List<MonitoredMessage> getAllFailedMessages() {
        // loop through the list and find all the pending messages
        final List<MonitoredMessage> failedMessages = new ArrayList<>();
        // loop through the cache
        for (final MonitoredMessage trackMessage : messageMonitoringCache.values()) {
            if (trackMessage.getStatus().equals(STATUS_ERROR)) {
                failedMessages.add(trackMessage);
            }
        }
        return failedMessages;
    }

    public boolean updateMessageMonitoringRetryCount() {

        // its already there, just update the
        final String emailId = null;
        final Date updatedTime = new Date();
        // create the track message notification objects
        final MonitoredMessageNotification tmn = new MonitoredMessageNotification();
        tmn.setUpdatetime(updatedTime);
        tmn.setEmailid(emailId);
        tmn.setStatus(STATUS_PENDING);

        return false;
    }

    // clear the cache
    // clear the database entries
    public void deleteCompletedOutgoingMessages() {
        // loop through the completed or Error List and delete the rows
    }

    protected MessageMonitoringDAO getMessageMonitoringDAO() {
        return MessageMonitoringDAOImpl.getInstance();
    }

    public boolean isRetryOutgoingMessage(final MimeMessage message) {
        final String messageId = MessageMonitoringUtil.getParentMessageId(message);
        return messageMonitoringCache.containsKey(messageId);
    }

    public MonitoredMessageNotification getTrackmessagenotification(final MonitoredMessage trackMessage) {
        // assuming only one recipient
        final String emailId = trackMessage.getRecipients();

        final Iterator iterator = trackMessage.getMonitoredmessagenotifications().iterator();
        // check values
        while (iterator.hasNext()) {
            final MonitoredMessageNotification tmn = (MonitoredMessageNotification) iterator.next();
            // get the correspoding email id
            if (tmn.getEmailid().equalsIgnoreCase(emailId)) {
                return tmn;
            }
        }
        return null;
    }

    public MonitoredMessageNotification getTrackmessagenotification(final MonitoredMessage trackMessage,
        final String emailId) {
        final Iterator iterator = trackMessage.getMonitoredmessagenotifications().iterator();
        // check values
        while (iterator.hasNext()) {
            final MonitoredMessageNotification tmn = (MonitoredMessageNotification) iterator.next();
            // get the correspoding email id
            if (tmn.getEmailid().equalsIgnoreCase(emailId)) {
                return tmn;
            }
        }
        return null;
    }

    private static String getIncomingMessagesReceivedStatus(final MonitoredMessage trackMessage) {
        boolean failed = false;
        boolean processed = false;

        // if the trackMessage status is Error then return ERROR
        if (trackMessage.getStatus().equalsIgnoreCase(STATUS_ERROR)) {
            return STATUS_ERROR;
        }

        // loop through the incoming message and return STATUS_COMPLETED or STATUS_PENDING or STATUS_FAILED
        final Iterator iterator = trackMessage.getMonitoredmessagenotifications().iterator();
        // check values

        while (iterator.hasNext()) {
            final MonitoredMessageNotification tmn = (MonitoredMessageNotification) iterator.next();
            // get the correspoding email id
            if (tmn.getStatus().equalsIgnoreCase(STATUS_PENDING)) {
                return STATUS_PENDING;
            } else if (tmn.getStatus().equalsIgnoreCase(STATUS_PROCESSED)) {
                processed = true;
            } else if (tmn.getStatus().equalsIgnoreCase(STATUS_ERROR)) {
                failed = true;
            }
        }
        // If not failed rows then retun it as PROCESSED
        if (failed) {
            return STATUS_ERROR;
        } else if (processed) {
            return STATUS_PROCESSED;
        } else {
            return STATUS_COMPLETED;
        }
    }

    public boolean isAllIncomingMessagesReceived(final MimeMessage message) {
        final String parentMessageId = MessageMonitoringUtil.getParentMessageId(message);
        final MonitoredMessage tm = messageMonitoringCache.get(parentMessageId);

        if (tm != null) {
            return tm.getStatus().equalsIgnoreCase(STATUS_COMPLETED) || tm.getStatus().equalsIgnoreCase(STATUS_ERROR);
        }
        // if not able to find then return true
        return true;
    }

    /**
     * This method is called by the poller task to monitor & update the message status and also to notify the edge
     * client with respective status of the
     */
    public void process() {

        LOG.debug("Inside Message Monitoring API process() method.");

        // Always check if the message monitoring is enabled
        if (!MessageMonitoringUtil.isMessageMonitoringEnabled()) {
            LOG.debug(MSG_MONITORING_NOT_ENABLED);
            return;
        }
        // check all the pending messages and update the status
        // 1. Check if the message is elaspsed and yes then update the status to Failed
        // else Completed
        // 2. Check all the completed /failed messages and set the status
        // to Completed or Failed
        checkAndUpdateMessageStatus();
        try {
            // send notification to all the completed
            // or failed messages
            // delete the notified message
            processAllMessages();
        } catch (final MessageMonitoringDAOException ex) {
            LOG.error("Error in Message Monitoring API process().{}", ex.getLocalizedMessage(), ex);
        }
        LOG.debug("Exiting Message Monitoring API process() method.");
    }

    private void checkAndUpdateMessageStatus() {
        LOG.debug("Entering Message Monitoring API checkAndUpdateMessageStatus() method.");
        // get the pending message list
        final List<MonitoredMessage> pendingMessages = getAllPendingMessages();

        for (final MonitoredMessage trackMessage : pendingMessages) {
            // check if the processed message is received, if not check the time limit
            // reached
            if (trackMessage.getStatus().equals(STATUS_PENDING)
                && getIncomingMessagesReceivedStatus(trackMessage).equals(STATUS_PENDING)) {
                if (MessageMonitoringUtil.isProcessedMDNReceiveTimeLapsed(trackMessage.getCreatetime())) {
                    LOG.debug("Processed MDN not received on time for the message ID:" + trackMessage.getMessageid());
                    // update the status to Error
                    trackMessage.setStatus(STATUS_ERROR);
                } // process the next pending message
            } // if the message status is processed then check if the time limit reached for dispatched
            else if (trackMessage.getStatus().equals(STATUS_PENDING)
                && getIncomingMessagesReceivedStatus(trackMessage).equals(STATUS_PROCESSED)) {
                if (MessageMonitoringUtil.isDispatchedMDNReceiveTimeLapsed(trackMessage.getCreatetime())) {
                    LOG.debug("Dispatched MDN not received on time for the message ID:" + trackMessage.getMessageid());
                    // update the status to Error
                    trackMessage.setStatus(STATUS_ERROR);
                } // process the next pending message
            }
        }
        LOG.debug("Exiting Message Monitoring API checkAndUpdateMessageStatus() method.");
    }

    /*
     * A new DirectTesting Flag and delayTime Flag are added in gateway.properties It waits for n number of minutes to
     * delete the record from MessageMonitored table. Since the MessageId form MessageMonitored table is used to read
     * MessageId in order to make event assertions for Automated testing
     */
    public void processAllMessages() throws MessageMonitoringDAOException {
        LOG.debug("Inside Message Monitoring API processAllMessages() method.");
        // ********FAILED MESSAGES***********
        // get all the failed messages
        final List<MonitoredMessage> failedMessages = getAllFailedMessages();
        for (final MonitoredMessage trackMessage : failedMessages) {
            // send out a Failed notification to the edge
            sendFailedEdgeNotification(trackMessage);
            // delete the message
            messageMonitoringCache.remove(trackMessage.getMessageid());
            if (getDirectTestFlag(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.DIRECTTESTING_FLAG)) {
                deleteElapsedArchivedMessageList();
                trackMessage.setStatus(STATUS_ARCHIVED);
                getMessageMonitoringDAO().updateOutgoingMessage(trackMessage);
            } else {
                deleteFromMessageMonitoringDB(trackMessage);
            }
        }
        // ********COMPLETED MESSAGES********
        // get all the completed messages
        final List<MonitoredMessage> completedMessages = getAllCompletedMessages();
        for (final MonitoredMessage trackMessage : completedMessages) {
            // send out a successful notification
            sendSuccessEdgeNotification(trackMessage);
            // delete the message
            messageMonitoringCache.remove(trackMessage.getMessageid());
            if (getDirectTestFlag(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.DIRECTTESTING_FLAG)) {
                deleteElapsedArchivedMessageList();
                trackMessage.setStatus(STATUS_ARCHIVED);
                getMessageMonitoringDAO().updateOutgoingMessage(trackMessage);
            } else {
                deleteFromMessageMonitoringDB(trackMessage);
            }
        }

        LOG.debug("Exiting Message Monitoring API processAllMessages() method.");
    }

    /**
     * Sends out a Successful SMTP edge notification.
     *
     * @param trackMessage
     */
    public void sendSuccessEdgeNotification(final MonitoredMessage trackMessage) {
        LOG.debug("Inside Message Monitoring API sendSuccessEdgeNotification() method.");

        final String subject = MessageMonitoringUtil.getSuccessfulMessageSubjectPrefix() + trackMessage.getSubject();
        final String emailText = MessageMonitoringUtil.getSuccessfulMessageEmailText() + trackMessage.getRecipients();
        final String postmasterEmailId = MessageMonitoringUtil.getDomainPostmasterEmailId() + "@"
            + MessageMonitoringUtil.getDomainFromEmail(trackMessage.getSenderemailid());
        // logic goes here
        final DirectEdgeProxy proxy = MessageMonitoringUtil.getDirectEdgeProxy();
        MimeMessage message = null;
        String errorMsg = null;
        try {
            message = MessageMonitoringUtil.createMimeMessage(postmasterEmailId, subject,
                trackMessage.getSenderemailid(), emailText, trackMessage.getMessageid());
            proxy.provideAndRegisterDocumentSetB(message);
            // Log the failed QOS event
            getDirectEventLogger().log(DirectEventType.DIRECT_EDGE_NOTIFICATION_SUCCESSFUL, message);
        } catch (final MessagingException ex) {
            errorMsg = ex.getLocalizedMessage();
            LOG.error(errorMsg, ex);
        }
        LOG.debug("Exiting Message Monitoring API sendSuccessEdgeNotification() method.");
    }

    /**
     * Sends out a Failed SMTP edge notification.
     *
     * @return List
     */
    private void sendFailedEdgeNotification(final MonitoredMessage trackMessage) {
        LOG.debug("Inside Message Monitoring API sendFailedEdgeNotification() method.");
        final String subject = MessageMonitoringUtil.getFailedMessageSubjectPrefix() + trackMessage.getSubject();
        final String emailText = MessageMonitoringUtil.getFailedMessageEmailText() + trackMessage.getRecipients();
        final String postmasterEmailId = MessageMonitoringUtil.getDomainPostmasterEmailId() + "@"
            + MessageMonitoringUtil.getDomainFromEmail(trackMessage.getSenderemailid());
        // logic goes here
        final DirectEdgeProxy proxy = MessageMonitoringUtil.getDirectEdgeProxy();
        MimeMessage message = null;
        String errorMsg = null;
        try {
            message = MessageMonitoringUtil.createMimeMessage(postmasterEmailId, subject,
                trackMessage.getSenderemailid(), emailText, trackMessage.getMessageid());
            proxy.provideAndRegisterDocumentSetB(message);
            // Log the failed QOS event
            getDirectEventLogger().log(DirectEventType.DIRECT_EDGE_NOTIFICATION_FAILED, message);
        } catch (final AddressException ex) {
            errorMsg = ex.getLocalizedMessage();
            LOG.error("Unable to send FailEdgeNotification {}", errorMsg, ex);
        } catch (final MessagingException ex) {
            errorMsg = ex.getLocalizedMessage();
            LOG.error(errorMsg, ex);
        }
        LOG.debug("Exiting Message Monitoring API sendFailedEdgeNotification() method.");
    }

    /**
     * Returns the Direct event logger instance.
     *
     * @return the directEventLogger
     */
    private static DirectEventLogger getDirectEventLogger() {
        return DirectEventLogger.getInstance();
    }

    private static boolean getDirectTestFlag(final String fileName, final String property) {
        boolean directTestFlag = false;
        final String directTestingFlag = getDirectTestingParam(fileName, property);
        if (directTestingFlag != null && !directTestingFlag.isEmpty() && directTestingFlag.equals("true")) {
            directTestFlag = true;
        }
        return directTestFlag;
    }

    private static String getDirectTestingParam(final String fileName, final String property) {
        String directTestingParam = null;
        try {
            directTestingParam = PropertyAccessor.getInstance().getProperty(fileName, property);
        } catch (final PropertyAccessException e) {
            LOG.debug("Error while retrieving property from property file", e);
        }
        return directTestingParam;
    }

    private static boolean getDirectTestingDelay(final String fileName, final String property, final Date updateDate) {
        final String delayTime = getDirectTestingParam(fileName, property);
        final Date currentDateTime = new Date();
        final int delayInMinutes = Integer.parseInt(delayTime);
        final Date updateDateTime = getupdateDate(updateDate, delayInMinutes);
        if (currentDateTime.after(updateDateTime)) {
            return true;
        }
        return false;
    }

    private static Date getupdateDate(final Date updateDate, final int delayUpdateTime) {
        final Calendar cal = Calendar.getInstance();
        cal.setTime(updateDate);
        cal.add(Calendar.MINUTE, delayUpdateTime);
        return cal.getTime();
    }

    private static void deleteFromMessageMonitoringDB(final MonitoredMessage trackMessage) {
        try {
            MessageMonitoringDAOImpl.getInstance().deleteCompletedMessages(trackMessage);
        } catch (final MessageMonitoringDAOException ex) {
            LOG.debug("Error While deleting Message from MessageMonitoring Table: {}", ex);
        }
        LOG.debug("Completed message deleted.");
    }

    private void deleteElapsedArchivedMessageList() {
        final List<MonitoredMessage> pendingDBMessages = getAllPendingMessagesFromDatabase();
        for (final MonitoredMessage trackMessage : pendingDBMessages) {
            if (trackMessage.getStatus().equals(STATUS_ARCHIVED)) {
                deleteElapsedArchivedMessage(trackMessage);
            }
        }
    }

    private static void deleteElapsedArchivedMessage(final MonitoredMessage trackMessage) {
        if (getDirectTestingDelay(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.MESSAGEMONITORING_DELAYINMINUTES,
            trackMessage.getUpdatetime())) {
            deleteFromMessageMonitoringDB(trackMessage);
        }
    }

    private static String getSenderEmailId(final MimeMessage message) throws MessagingException {
        InternetAddress sender;
        sender = (InternetAddress) message.getSender();
        if (sender == null) {
            final InternetAddress[] fromAddresses = (InternetAddress[]) message.getFrom();
            if (fromAddresses.length > 0) {
                sender = fromAddresses[0];
            }
        }
        return sender == null ? null : sender.getAddress();
    }

}
