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
package gov.hhs.fha.nhinc.direct.messagemonitoring.impl;

import gov.hhs.fha.nhinc.direct.messagemonitoring.dao.MessageMonitoringDAO;
import gov.hhs.fha.nhinc.direct.messagemonitoring.dao.MessageMonitoringDAOException;
import gov.hhs.fha.nhinc.direct.messagemonitoring.dao.impl.MessageMonitoringDAOImpl;
import gov.hhs.fha.nhinc.direct.messagemonitoring.domain.Trackmessage;
import gov.hhs.fha.nhinc.direct.messagemonitoring.domain.Trackmessagenotification;
import gov.hhs.fha.nhinc.direct.messagemonitoring.util.MessageMonitoringUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * All the Message Monitoring API services are exposed through this class. This
 * class will maintain a cache to store all the active messages that are sent
 * out waiting for response.
 *
 * @author Naresh Subramanyan
 */
public class MessageMonitoringAPI {

    private static final Logger LOG = Logger.getLogger(MessageMonitoringAPI.class);

    //messageId is the key and Trackmessage object the value
    Map<String, Trackmessage> messageMonitoringCache = null;

    private static final String STATUS_PENDING = "Pending";
    private static final String STATUS_ERROR = "Error";
    private static final String STATUS_COMPLETED = "Completed";
    private static final String STATUS_PROCESSED = "Processed";
    private static final String STATUS_DISPATCHED = "Dispatched";

    public MessageMonitoringAPI() {
        //set the default value
        messageMonitoringCache = new HashMap<String, Trackmessage>();
        buildCahce();
    }

    private static class SingletonHolder {

        public static final MessageMonitoringAPI INSTANCE = new MessageMonitoringAPI();
    }

    public static MessageMonitoringAPI getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void addOutgoingMessage(MimeMessage message) {
        try {
            //get the all recipients
            InternetAddress recipients[] = (InternetAddress[]) message.getAllRecipients();

            //get the mail sender
            InternetAddress sender = (InternetAddress) message.getSender();
            String senderMailId = sender.getAddress();
            //Mail Subject
            String mailSubject = message.getSubject();
            //get the message id
            String messageId = message.getMessageID();
            //created time
            Date createdTime = new Date();

            boolean deliveryRequested = MessageMonitoringUtil.isNotificationRequestedByEdge(message);
            //Create the track Message domain
            Trackmessage tm = new Trackmessage();
            tm.setSubject(mailSubject);
            tm.setSenderemailid(senderMailId);
            tm.setMessageid(messageId);
            tm.setStatus(STATUS_PENDING);
            tm.setDeliveryrequested(deliveryRequested);
            tm.setCreatetime(createdTime);
            tm.setUpdatetime(createdTime);
            ArrayList recipientsList = new ArrayList();
            Set messageNotificationSet = new HashSet();
            for (InternetAddress address : recipients) {
                String emailId = address.getAddress();
                //create the track message notification objects
                Trackmessagenotification tmn = new Trackmessagenotification();
                tmn.setCreatetime(createdTime);
                tmn.setUpdatetime(createdTime);
                tmn.setEmailid(emailId);
                tmn.setStatus(STATUS_PENDING);
                messageNotificationSet.add(tmn);
                tmn.setTrackmessage(tm);
                recipientsList.add(emailId);
            }
            tm.setRecipients(StringUtils.join(recipientsList, ","));
            tm.setTrackmessagenotifications(messageNotificationSet);

            //call the dao to persist the date
            getMessageMonitoringDAO().addOutgoingMessage(tm);

            messageMonitoringCache.put(messageId, tm);

        } catch (MessagingException ex) {
            LOG.info("Failed:" + ex.getMessage());
        } catch (MessageMonitoringDAOException ex) {
            LOG.info("Failed:" + ex.getMessage());
        }

    }

    /**
     * Clear the cache
     *
     */
    public void clearCache() {
        messageMonitoringCache = new HashMap();
    }

    /**
     * Build the Message Monitoring cache from the database tables. This will be
     * called when the module is initiated.
     *
     */
    public void buildCahce() {
        //get all the Pending rows and add it to the cache
        List<Trackmessage> pendingMessages = getAllPendingMessagesFromDatbase();
        clearCache();
        //load the pending outgoing messages to the cache
        for (Trackmessage trackMessage : pendingMessages) {
            messageMonitoringCache.put(trackMessage.getMessageid(), trackMessage);
        }
    }

    /**
     * Returns all the Successfully Completed outbound messages.
     *
     * @return List
     */
    public List<Trackmessage> getAllCompletedMessages() {
        List<Trackmessage> completedMessages = new ArrayList<Trackmessage>();
        //loop through the cache
        for (Trackmessage trackMessage : messageMonitoringCache.values()) {
            if (trackMessage.getStatus().equals(STATUS_COMPLETED)){
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
    public List<Trackmessage> getAllPendingMessagesFromDatbase() {
        return getMessageMonitoringDAO().getAllPendingMessages();
    }

    /**
     * Returns all the Failed outbound messages. 1. Processed not received for
     * one or more recipients 2. Dispatched not received for one or more
     * recipients if notification requested by edge. 3. Got Failed DSN/MDN for
     * one or more recipients
     *
     * @return List
     */
    public List<Trackmessage> getAllFailedMessages() {
        //loop through the list and find all the pending messages
        List<Trackmessage> failedMessages = new ArrayList<Trackmessage>();
        //loop through the cache
        for (Trackmessage trackMessage : messageMonitoringCache.values()) {
            if (trackMessage.getStatus().equals(STATUS_ERROR)){
                failedMessages.add(trackMessage);
            }
            else if (trackMessage.getStatus().equals(STATUS_PENDING)){
                //if its pending & if its elapsed then 
                //change the status to Error
            }
        }
        return failedMessages;
    }

    public void updateIncomingMessageNotificatinStatus(MimeMessage message) {

        String messageId;
        try {
            messageId = message.getMessageID();

            //TODO: check if the retry limit has reached
            //if (isRetryLimitReached(messageMonitoringCache.get(messageId))) {
            //update the cache
            //update the database
            //}
            //find out if its a successful or failed message
            boolean isMDNProcessed = MessageMonitoringUtil.isIncomingMessageMDNProcessed(message);
            boolean isMDNDispatched = MessageMonitoringUtil.isIncomingMessageMDNDispatched(message);

            String parentMessageId = MessageMonitoringUtil.getParentMessageId(message);
            Trackmessage tm = messageMonitoringCache.get(parentMessageId);

            //if the message is not there then ignore it for now
            if (tm == null) {
                LOG.debug("Not able to find the message in the cache..may be its a incoming message or junk message");
                return;
            }

            //ignore any message that comes if the status not in Pending
            if (!(tm.getStatus().equalsIgnoreCase(STATUS_PENDING) || tm.getStatus().equalsIgnoreCase(STATUS_PROCESSED))) {
                return;
            }
            //get the mail sender
            InternetAddress sender = (InternetAddress) message.getSender();
            if (sender == null) {
                InternetAddress[] fromAddresses = (InternetAddress[]) message.getFrom();
                sender = fromAddresses[0];
            }
            String senderMailId = sender.getAddress();

            Trackmessagenotification tmn = getTrackmessagenotification(tm, senderMailId);
            //if delivery is requested
            if (tm.getDeliveryrequested()) {
                //Update only if MDN is dispatched
                if (isMDNDispatched) {
                    //update the status to completed
                    tmn.setStatus(STATUS_COMPLETED);
                } else { //Update the status to Processed
                    tmn.setStatus(STATUS_PROCESSED);
                }
            } else if (isMDNDispatched | isMDNProcessed) {
                tmn.setStatus(STATUS_COMPLETED);
            } else { //if error
                tmn.setStatus(STATUS_ERROR);
            }
            Date updatedTime = new Date();
            if (isAllIncomingMessagesReceived(tm).equalsIgnoreCase(STATUS_PENDING)) {
                tmn.setUpdatetime(updatedTime);
                getMessageMonitoringDAO().updateMessageNotification(tmn);
            } else {
                //set the status to Completed or Error
                tm.setStatus(isAllIncomingMessagesReceived(tm));
                tm.setUpdatetime(updatedTime);
                getMessageMonitoringDAO().updateOutgoingMessage(tm);
            }
        } catch (MessagingException ex) {
            LOG.info("Failed:" + ex.getMessage());
        } catch (MessageMonitoringDAOException mde) {
            LOG.info("Failed:" + mde.getMessage());
        }
    }

    public boolean updateMessageMonitoringRetryCount(MimeMessage message) {

        try {
            //get the message id
            String messageId = message.getMessageID();

            //check if the retry limit has reached
            if (isRetryLimitReached(messageMonitoringCache.get(messageId))) {
                //update the cache

                //update the database
            }
            //its already there, just update the
            String emailId = null;
            Date updatedTime = new Date();
            //create the track message notification objects
            Trackmessagenotification tmn = new Trackmessagenotification();
            tmn.setUpdatetime(updatedTime);
            tmn.setEmailid(emailId);
            tmn.setStatus(STATUS_PENDING);
        } catch (MessagingException ex) {
            java.util.logging.Logger.getLogger(MessageMonitoringAPI.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    //clear the cache
    //clear the database entries
    public void deleteCompletedOutgoingMessages() {
        //loop through the completed or Error List and delete the rows

    }

    protected MessageMonitoringDAO getMessageMonitoringDAO() {
        return MessageMonitoringDAOImpl.getInstance();
    }

    public boolean isRetryOutgoingMeesage(MimeMessage message) {
        String messageId = MessageMonitoringUtil.getParentMessageId(message);
        return messageMonitoringCache.containsKey(messageId);
    }

    private boolean isRetryLimitReached(Trackmessage trackMessage) {
        //TODO
        //get the outgoing retry count

        //get the retried count from the cache
        //getTrackmessagenotification(trackMessage).
        //if the retry limit has reached, then update the status to "Error"
        //update the cache and also the trackMessage
        return true;
    }

    public Trackmessagenotification getTrackmessagenotification(Trackmessage trackMessage) throws MessagingException {
        //assuming only one recipient
        String emailId = trackMessage.getRecipients();

        Iterator iterator = trackMessage.getTrackmessagenotifications().iterator();
        // check values
        while (iterator.hasNext()) {
            Trackmessagenotification tmn = (Trackmessagenotification) iterator.next();
            //get the correspoding email id
            if (tmn.getEmailid().equalsIgnoreCase(emailId)) {
                return tmn;
            }
        }
        return null;
    }

    public Trackmessagenotification getTrackmessagenotification(Trackmessage trackMessage, String emailId) throws MessagingException {
        Iterator iterator = trackMessage.getTrackmessagenotifications().iterator();
        // check values
        while (iterator.hasNext()) {
            Trackmessagenotification tmn = (Trackmessagenotification) iterator.next();
            //get the correspoding email id
            if (tmn.getEmailid().equalsIgnoreCase(emailId)) {
                return tmn;
            }
        }
        return null;
    }

    private String isAllIncomingMessagesReceived(Trackmessage trackMessage) {
        boolean failed = false;

        //loop through the 
        //loop through the incoming message and return STATUS_COMPLETED or STATUS_PENDING or STATUS_FAILED
        Iterator iterator = trackMessage.getTrackmessagenotifications().iterator();
        // check values
        while (iterator.hasNext()) {
            Trackmessagenotification tmn = (Trackmessagenotification) iterator.next();
            //get the correspoding email id
            if (tmn.getStatus().equalsIgnoreCase(STATUS_PENDING) || tmn.getStatus().equalsIgnoreCase(STATUS_PROCESSED)) {
                return STATUS_PENDING;
            } else if (tmn.getStatus().equalsIgnoreCase(STATUS_ERROR)) {
                failed = true;
            }
        }
        return failed ? STATUS_ERROR : STATUS_COMPLETED;
    }

    public boolean isAllIncomingMessagesReceived(MimeMessage message) {
        String parentMessageId = MessageMonitoringUtil.getParentMessageId(message);
        Trackmessage tm = messageMonitoringCache.get(parentMessageId);

        if (tm != null) {
            return tm.getStatus().equalsIgnoreCase(STATUS_COMPLETED) || tm.getStatus().equalsIgnoreCase(STATUS_ERROR);
        }
        //if not able to find then return ture
        //TODO: revist this
        return true;
    }
    
    public void process(){
        processAllMessages();
        deleteAllPurgeMessages();
    }

    public void deleteAllPurgeMessages() {
        
    }
    public void processAllMessages() {
        //
        //********FAILED MESSAGES***********
        //get all the failed messages
        //send out a Failed notification to the edge
        
        //set the status to Purge

        //********COMPLETED MESSAGES********
        //get all the completed messages
        
        //send out a successful notification
        
        //update the status to Purge
    }
    
    public void sendEdgeNotification(){
        
    }
}
