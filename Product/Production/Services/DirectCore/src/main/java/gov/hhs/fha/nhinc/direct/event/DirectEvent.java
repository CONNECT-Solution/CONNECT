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
package gov.hhs.fha.nhinc.direct.event;

import gov.hhs.fha.nhinc.event.BaseEvent;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.common.collect.ImmutableList;
import gov.hhs.fha.nhinc.event.Event;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;

/**
 * {@link Event} Implementation for Direct.
 */
public class DirectEvent extends BaseEvent {

    private static final Logger LOG = Logger.getLogger(DirectEvent.class);

    private static final String XML_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

    /*
     * JSON fields and values. (should use a standard enum here? )
     */
    private static final String ACTION = "action";
    private static final String MESSAGE_ID = "message_id";
    private static final String TIMESTAMP = "timestamp";
    private static final String STATUSES = "statuses";
    private static final String ERROR_MSG = "error_msg";
    private static final String SENDER = "sender";
    private static final String RECIPIENT = "recipient";
    private static final String SERVICE_TYPE = "Direct";
    private static final String PROPERTY_FILE_NAME = "gateway";
    private static final String PROPERTY_NAME = "localHomeCommunityId";
    private static final List<String> STATUS_SUCCESS = ImmutableList.of("success");
    private static final List<String> STATUS_ERROR = ImmutableList.of("error");

    private final String name;

    /**
     * @param name of the triggered event.
     */
    public DirectEvent(String name) {
        this.name = name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getEventName() {
        return name;
    }

    /**
     * Builder for the Direct Event.
     */
    public static final class Builder {

        private MimeMessage message;
        private String errorMsg;

        /**
         * @param mimeMessage source for messageid, sender, recips, etc
         * @return this builder.
         */
        public Builder mimeMessage(MimeMessage mimeMessage) {
            this.message = mimeMessage;
            return this;
        }

        /**
         * Create an event with an error status.
         *
         * @param str error message encountered.
         * @return this builder.
         */
        public Builder errorMsg(String str) {
            this.errorMsg = str;
            return this;
        }

        /**
         * Build a direct event.
         *
         * @param type - {@link DirectEventType} to build.
         * @return the created event.
         */
        public DirectEvent build(DirectEventType type) {

            String eventName = type.toString();
            final DirectEvent event = new DirectEvent(eventName);
            event.setTransactionID("");
            event.setServiceType(SERVICE_TYPE);
            try {
                event.setInitiatorHcid(PropertyAccessor.getInstance().getProperty(PROPERTY_FILE_NAME, PROPERTY_NAME));
                event.setRespondingHcid(PropertyAccessor.getInstance().getProperty(PROPERTY_FILE_NAME, PROPERTY_NAME));
            } catch (PropertyAccessException e) {
                LOG.error("Error reading local home community from gateway properties file.", e);
            }
            JSONObject jsonDescription = new JSONObject();
            addToJSON(jsonDescription, TIMESTAMP, formatDateForXml(new Date()));
            addToJSON(jsonDescription, ACTION, eventName);
            addToJSON(jsonDescription, ERROR_MSG, errorMsg);
            if (message != null) {
                try {
                    addToJSON(jsonDescription, SENDER, message.getSender());
                    addToJSON(jsonDescription, RECIPIENT, message.getAllRecipients());

                    String messageId = message.getMessageID();
                    event.setMessageID(messageId);
                    addToJSON(jsonDescription, MESSAGE_ID, messageId);

                } catch (MessagingException e) {
                    LOG.error("Error building JSON (Messaging Exception)", e);
                }
            }

            if (errorMsg == null) {
                addToJSON(jsonDescription, STATUSES, STATUS_SUCCESS);
            } else {
                addToJSON(jsonDescription, STATUSES, STATUS_ERROR);
            }

            event.setDescription(jsonDescription.toString());
            return event;
        }

        private void addToJSON(JSONObject jsonObject, String key, Object value) {
            try {
                jsonObject.put(key, value);
            } catch (JSONException e) {
                LOG.error("Exception while building JSON description for direct event.", e);
            }
        }
    }

    private static String formatDateForXml(Date date) {
        return new SimpleDateFormat(XML_DATE_FORMAT, Locale.getDefault()).format(date);
    }
}
