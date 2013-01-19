/*
 * Copyright (c) 2009-13, United States Government, as represented by the Secretary of Health and Human Services.
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import gov.hhs.fha.nhinc.logging.transaction.dao.TransactionDAO;
import gov.hhs.fha.nhinc.logging.transaction.model.TransactionRepo;

import org.apache.log4j.MDC;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

/**
 * @author akong
 * 
 */
public class TransactionLoggerTest {

    private static final String MESSAGE_ID_MDC_KEY = "message-id";
    private static final String TRANSACTION_ID_MDC_KEY = "transaction-id";
    
    private static final String transactionId = "transactionId";
    private static final String messageId = "messageId";
    private static final String relatedMessageId = "relatedMessageId";

    @Before
    public void setup() {
        clearMDC();
    }
    
    @After
    public void tearDown() {
        clearMDC();
    }
    
    @Test
    public void logTransaction() {        
        TransactionDAO transactionDAO = mock(TransactionDAO.class);

        TransactionLogger transactionLogger = new TransactionLogger(transactionDAO);

        when(transactionDAO.insertIntoTransactionRepo(any(TransactionRepo.class))).thenReturn(true);

        transactionLogger.logTransaction(transactionId, messageId);

        verifyLog(transactionDAO, messageId, transactionId);
    }

    @Test
    public void logTransactionWithNullIds() {
        TransactionDAO transactionDAO = mock(TransactionDAO.class);

        TransactionLogger transactionLogger = new TransactionLogger(transactionDAO);

        transactionLogger.logTransaction(null, "messageId");
        verifyNothingLogged(transactionDAO);

        transactionLogger.logTransaction("transactionId", null);
        verifyNothingLogged(transactionDAO);

        transactionLogger.logTransaction(null, null);
        verifyNothingLogged(transactionDAO);
    }

    @Test
    public void logRelatedTransaction() {  
        TransactionDAO transactionDAO = mock(TransactionDAO.class);

        TransactionLogger transactionLogger = new TransactionLogger(transactionDAO);

        when(transactionDAO.getTransactionId(relatedMessageId)).thenReturn(transactionId);
        when(transactionDAO.insertIntoTransactionRepo(any(TransactionRepo.class))).thenReturn(true);
        
        transactionLogger.logTransactionFromRelatedMessageId(relatedMessageId, messageId);
        verifyLog(transactionDAO, messageId, transactionId);
    }
    
    @Test
    public void logNoRelatedTransaction() {        
        TransactionDAO transactionDAO = mock(TransactionDAO.class);

        TransactionLogger transactionLogger = new TransactionLogger(transactionDAO);

        when(transactionDAO.getTransactionId(relatedMessageId)).thenReturn(null);
        
        transactionLogger.logTransactionFromRelatedMessageId(relatedMessageId, messageId);
        verifyNothingLogged(transactionDAO);
    }
    
    private void clearMDC() {
        MDC.remove(MESSAGE_ID_MDC_KEY);
        MDC.remove(TRANSACTION_ID_MDC_KEY);
    }
    
    private void verifyLog(TransactionDAO transactionDAO, String messageId, String transactionId) {
        ArgumentCaptor<TransactionRepo> transRepoArgCaptor = ArgumentCaptor.forClass(TransactionRepo.class);
        verify(transactionDAO).insertIntoTransactionRepo(transRepoArgCaptor.capture());
        
        assertEquals(messageId, transRepoArgCaptor.getValue().getMessageId());
        assertEquals(transactionId, transRepoArgCaptor.getValue().getTransactionId());

        assertEquals(messageId, MDC.get(MESSAGE_ID_MDC_KEY));
        assertEquals(transactionId, MDC.get(TRANSACTION_ID_MDC_KEY)); 
    }

    private void verifyNothingLogged(TransactionDAO transactionDAO) {
        verify(transactionDAO, never()).insertIntoTransactionRepo(any(TransactionRepo.class));
        assertNull(MDC.get(MESSAGE_ID_MDC_KEY));
        assertNull(MDC.get(TRANSACTION_ID_MDC_KEY));
    }
}
