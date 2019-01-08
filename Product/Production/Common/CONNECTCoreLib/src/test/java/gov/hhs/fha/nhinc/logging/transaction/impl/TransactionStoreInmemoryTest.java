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
package gov.hhs.fha.nhinc.logging.transaction.impl;

import static org.junit.Assert.assertEquals;

import gov.hhs.fha.nhinc.logging.transaction.model.TransactionRepo;
import java.util.UUID;
import org.junit.Test;

/**
 * The class TransactionStoreInmemoryTest.
 *
 * @author msw
 */
public class TransactionStoreInmemoryTest {

    /**
     * Test happy path.
     */
    @Test
    public void testHappyPath() {
        TransactionStoreInmemory store = new TransactionStoreInmemory();

        String messageId = UUID.randomUUID().toString();
        String transactionId = UUID.randomUUID().toString();

        assertEquals(null, store.getTransactionId(messageId));

        TransactionRepo transactionRepo = new TransactionRepo();
        transactionRepo.setMessageId(messageId);
        transactionRepo.setTransactionId(transactionId);
        store.insertIntoTransactionRepo(transactionRepo);

        assertEquals(transactionId, store.getTransactionId(messageId));
    }

    /**
     * Test with multiple entries path.
     */
    @Test
    public void testWithMultipleEntriesPath() {
        TransactionStoreInmemory store = new TransactionStoreInmemory();

        String messageId1 = UUID.randomUUID().toString();
        String transactionId1 = UUID.randomUUID().toString();
        String messageId2 = UUID.randomUUID().toString();
        String transactionId2 = UUID.randomUUID().toString();
        String messageId3 = UUID.randomUUID().toString();
        String transactionId3 = UUID.randomUUID().toString();

        assertEquals(null, store.getTransactionId(messageId1));
        assertEquals(null, store.getTransactionId(messageId2));
        assertEquals(null, store.getTransactionId(messageId3));

        TransactionRepo transactionRepo1 = new TransactionRepo();
        transactionRepo1.setMessageId(messageId1);
        transactionRepo1.setTransactionId(transactionId1);
        store.insertIntoTransactionRepo(transactionRepo1);
        assertEquals(transactionId1, store.getTransactionId(messageId1));

        TransactionRepo transactionRepo2 = new TransactionRepo();
        transactionRepo2.setMessageId(messageId2);
        transactionRepo2.setTransactionId(transactionId2);
        store.insertIntoTransactionRepo(transactionRepo2);
        assertEquals(transactionId2, store.getTransactionId(messageId2));

        TransactionRepo transactionRepo3 = new TransactionRepo();
        transactionRepo3.setMessageId(messageId3);
        transactionRepo3.setTransactionId(transactionId3);
        store.insertIntoTransactionRepo(transactionRepo3);
        assertEquals(transactionId3, store.getTransactionId(messageId3));
    }

    /**
     * Test mulitple message ids.
     */
    @Test
    public void testMulitpleMessageIds() {
        TransactionStoreInmemory store = new TransactionStoreInmemory();

        String messageId1 = UUID.randomUUID().toString();
        String transactionId1 = UUID.randomUUID().toString();
        String messageId2 = UUID.randomUUID().toString();

        assertEquals(null, store.getTransactionId(messageId1));
        assertEquals(null, store.getTransactionId(messageId2));

        TransactionRepo transactionRepo1 = new TransactionRepo();
        transactionRepo1.setMessageId(messageId1);
        transactionRepo1.setTransactionId(transactionId1);
        store.insertIntoTransactionRepo(transactionRepo1);
        assertEquals(transactionId1, store.getTransactionId(messageId1));

        TransactionRepo transactionRepo2 = new TransactionRepo();
        transactionRepo2.setMessageId(messageId2);
        transactionRepo2.setTransactionId(transactionId1);
        store.insertIntoTransactionRepo(transactionRepo2);
        assertEquals(transactionId1, store.getTransactionId(messageId2));
    }

    /**
     * Test with null transaction repo.
     */
    @Test
    public void testWithNullTransactionRepo() {
        TransactionStoreInmemory store = new TransactionStoreInmemory();

        String messageId = UUID.randomUUID().toString();
        String transactionId = UUID.randomUUID().toString();

        assertEquals(null, store.getTransactionId(messageId));

        TransactionRepo transactionRepo = new TransactionRepo();
        transactionRepo.setMessageId(messageId);
        transactionRepo.setTransactionId(transactionId);
        assertEquals(true, store.insertIntoTransactionRepo(transactionRepo));
    }

}
