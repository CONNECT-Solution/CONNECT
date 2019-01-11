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
package gov.hhs.fha.nhinc.logging.transaction;

import gov.hhs.fha.nhinc.logging.transaction.factory.TransactionStoreFactory;
import gov.hhs.fha.nhinc.logging.transaction.model.TransactionRepo;
import java.sql.Timestamp;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class TransactionLogger {

    private static final Logger LOG = LoggerFactory.getLogger(TransactionLogger.class);

    TransactionStoreFactory transactionStoreFactory = null;

    /**
     * Constructor.
     */
    public TransactionLogger() {
        transactionStoreFactory = new TransactionStoreFactory();
    }

    /**
     * Gets the TransactionStore.
     *
     * @return The TransactionStore.
     */
    protected TransactionStore getTransactionStore() {
        return transactionStoreFactory.getTransactionStore();
    }

    /**
     * Logs the given transaction if the transactionId and the message id are not blank. The log consists of creating a
     * DB entry and an entry to the Mapped Diagnostic Context.
     *
     * @param transactionId The transactionId for the message
     * @param messageId The messageId for the message
     */
    public void logTransaction(String transactionId, String messageId) {
        createTransactionRecord(messageId, transactionId);
        enableMdcLogging(transactionId, messageId);
    }

    /**
     * Logs the given message id to the same transaction as the passed in related message id. If the latter does not
     * have a transaction associated with it, then no transactions logging occurs.
     *
     * @param relatedMessageId The message id related to the message to be logged
     * @param messageId The message id to be logged
     */
    public void logTransactionFromRelatedMessageId(String relatedMessageId, String messageId) {
        String transactionId = getTransactionStore().getTransactionId(relatedMessageId);
        logTransaction(transactionId, messageId);
    }

    /**
     * Enables MDC logging if the transaction ID and the message ID are not blank.
     *
     * @param transactionId The transactionId for the message
     * @param messageId The messageId for the message
     */
    void enableMdcLogging(String transactionId, String messageId) {
        if (StringUtils.isNotBlank(messageId) && StringUtils.isNotBlank(transactionId)) {
            MDC.put("message-id", messageId);
            MDC.put("transaction-id", transactionId);
        } else {
            LOG.info("pass in transaction-id is null for message id: " + messageId);
        }
    }

    /**
     * Creates a new transaction record and inserts it into the table.
     *
     * @param messageId The messageId from the SOAPHeader
     * @param transactionId The transactionId from the SOAPHeader
     */
    void createTransactionRecord(String messageId, String transactionId) {
        if (StringUtils.isNotBlank(messageId) && StringUtils.isNotBlank(transactionId)) {
            Long newId;

            TransactionRepo transRepo = new TransactionRepo();
            transRepo.setMessageId(messageId);
            transRepo.setTransactionId(transactionId);
            transRepo.setTime(createTimestamp());

            if (getTransactionStore().insertIntoTransactionRepo(transRepo)) {
                newId = transRepo.getId();
                LOG.info("New Transaction Log Id = " + newId);
            } else {
                LOG.warn("Failed to insert new transaction record.");
            }
        }
    }

    /**
     * Returns a timestamp, down to the millisecond.
     *
     * @return timestamp The created timestamp
     */
    private Timestamp createTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }
}
