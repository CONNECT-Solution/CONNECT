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
package gov.hhs.fha.nhinc.gateway.aggregator.model;

import gov.hhs.fha.nhinc.util.StringUtil;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import org.apache.log4j.Logger;

/**
 * This class represents one row of the AGGREGATOR.AGG_MESSAGE_RESULTS SQL table.
 * 
 * @author Les Westberg
 */
public class AggMessageResult {
    // Private member variables
    // --------------------------
    private String messageId;
    private String messageKey;
    private Date messageOutTime;
    private Date responseReceivedTime;
    private String responseMessageType;
    private byte[] responseMessageAsByteArray;
    private AggTransaction aggTransaction;
    private static final Logger LOG = Logger.getLogger(AggMessageResult.class);
    
    /**
     * Default constructor.
     */
    public AggMessageResult() {
        clear();
    }

    /**
     * Clear the contents of this object and set it to a default state.
     */
    private void clear() {
        messageId = "";
        messageKey = "";
        messageOutTime = null;
        responseReceivedTime = null;
        responseMessageType = "";
        responseMessageAsByteArray = new byte[0];
        aggTransaction = null;
    }

    /**
     * Returns the Message ID that uniquely identifies this row. This is a UUID.
     * 
     * @return The message ID that uniquely identifies this row.
     */
    public String getMessageId() {
        return messageId;
    }

    /**
     * Sets the Message ID that uniquely identifies this row. This is a UUID.
     * 
     * @param messageId The message ID that uniquely identifies this row.
     */
    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    /**
     * Return the message key that we will use to match up the response that is received.
     * 
     * @return The message key used to match up the response received. This must be unique. It is intended that this
     *         will be an XML string.
     */
    public String getMessageKey() {
        return messageKey;
    }

    /**
     * Sets the message key that we will use to match up the response that is received.
     * 
     * @param messageKey The message key used to match up the response received. This must be unique. It is intended
     *            that this will be an XML string.
     */
    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }

    /**
     * Returns the date and time that the message went out. This will be the same time as the TransactionStartTime in
     * the agg_transaction table for this transaction.
     * 
     * @return The date and time that the message went out.
     */
    public Date getMessageOutTime() {
        return messageOutTime;
    }

    /**
     * Sets the date and time that the message went out. This will be the same time as the TransactionStartTime in the
     * agg_transaction table for this transaction.
     * 
     * @param messageOutTime The date and time that the message went out.
     */
    public void setMessageOutTime(Date messageOutTime) {
        this.messageOutTime = messageOutTime;
    }

    /**
     * Returns the response message. This will be the actual message that has been marshalled to XML. This is what will
     * be aggregated.
     * 
     * @return The XML message that was received as a response to thie message that went out.
     */
    public String getResponseMessage() {
        String responseMessage = "";
        try {
            responseMessage = StringUtil.convertToStringUTF8(responseMessageAsByteArray);
        } catch (UnsupportedEncodingException ex) {
          LOG.error("Error converting String to UTF8 format: "+ex.getMessage());
        }
        return responseMessage;
    }

    /**
     * Sets the response message. This will be the actual message that has been marshalled to XML. This is what will be
     * aggregated.
     * 
     * @param responseMessage The XML message that was received as a response to thie message that went out.
     */
    public void setResponseMessage(String responseMessage) {
        try {
            this.responseMessageAsByteArray = responseMessage.getBytes(StringUtil.UTF8_CHARSET);
        } catch (UnsupportedEncodingException ex) {
          LOG.error("Error converting String to UTF8 format: "+ex.getMessage());
        }
    }

    /**
     * Returns the response message to be aggregated as a byte array.
     * 
     * @return A byte array containing the response message to be aggregated.
     */
    public byte[] getResponseMessageAsByteArray() {
        return responseMessageAsByteArray;
    }

    /**
     * Sets the response message to be aggregated.
     * 
     * @param A byte array containing the response message that is to be aggregated.
     * 
     */
    public void setResponseMessageAsByteArray(byte[] responseMessage) {
        this.responseMessageAsByteArray = responseMessage;
    }

    /**
     * Returns the type of the response message. This is the textual name of the JAXB class that represents the XML
     * string.
     * 
     * @return The type of the response message.
     */
    public String getResponseMessageType() {
        return responseMessageType;
    }

    /**
     * Sets the type of the response message. This is the textual name of the JAXB class that represents the XML string.
     * 
     * @param responseMessageType The type of the response message.
     */
    public void setResponseMessageType(String responseMessageType) {
        this.responseMessageType = responseMessageType;
    }

    /**
     * Returns the date and time that the response was received by the aggregator.
     * 
     * @return The date and time that the response was received by the aggregator.
     */
    public Date getResponseReceivedTime() {
        return responseReceivedTime;
    }

    /**
     * Sets the date and time that the response was received by the aggregator.
     * 
     * @param responseReceivedTime The date and time that the response was received by the aggregator.
     */
    public void setResponseReceivedTime(Date responseReceivedTime) {
        this.responseReceivedTime = responseReceivedTime;
    }

    /**
     * Return the AggTransaction associated with the AggMessageResult.
     * 
     * @return The AggTransaction associated with the AggMessageResult.
     */
    public AggTransaction getAggTransaction() {
        return aggTransaction;
    }

    /**
     * Sets the AggTransaction associated with the AggMessageResult.
     * 
     * @param aggTransaction The AggTransaction associated with the AggMessageResult.
     */
    public void setAggTransaction(AggTransaction aggTransaction) {
        this.aggTransaction = aggTransaction;
    }

}
