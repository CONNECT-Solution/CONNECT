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

import gov.hhs.fha.nhinc.logging.transaction.TransactionStore;
import gov.hhs.fha.nhinc.logging.transaction.model.TransactionRepo;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * In memory implementation of the TransactionStore interface.
 *
 * @author msw
 */
public class TransactionStoreInmemory implements TransactionStore {

    /** The map. */
    Map<String, List<String>> map = null;

    /**
     * Instantiates a new transaction store inmemory.
     */
    public TransactionStoreInmemory() {
        map = getMap();
    }

    /**
     * Gets the map.
     *
     * @return the map
     */
    protected Map<String, List<String>> getMap() {
        return new HashMap<>();
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.logging.transaction.TransactionStore#insertIntoTransactionRepo(gov.hhs.fha.nhinc.logging.
     * transaction.model.TransactionRepo)
     */
    @Override
    public boolean insertIntoTransactionRepo(TransactionRepo transactionRepo) {
        boolean inserted = false;
        if (transactionRepo != null && map != null) {
            String messageId = transactionRepo.getMessageId();
            String transactionId = transactionRepo.getTransactionId();
            List<String> transactionIds;

            if (map.containsKey(messageId)) {
                transactionIds = map.get(messageId);
            } else {
                transactionIds = new LinkedList<>();
            }

            if (!transactionIds.contains(transactionId)) {
                transactionIds.add(transactionId);
            }

            map.put(messageId, transactionIds);
            inserted = true;
        }
        return inserted;
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.logging.transaction.TransactionStore#getTransactionId(java.lang.String)
     */
    @Override
    public String getTransactionId(String messageId) {
        String transactionId = null;
        if (map != null) {
            List<String> transactionIds = map.get(messageId);
            if (transactionIds != null) {
                transactionId = transactionIds.get(0);
            }
        }
        return transactionId;
    }

}
