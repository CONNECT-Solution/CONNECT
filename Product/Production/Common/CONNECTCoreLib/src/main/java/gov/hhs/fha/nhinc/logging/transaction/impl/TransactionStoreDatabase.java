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
import gov.hhs.fha.nhinc.logging.transaction.dao.TransactionDAO;
import gov.hhs.fha.nhinc.logging.transaction.model.TransactionRepo;

/**
 * Database backed implementation of the TransactionStore interface.
 *
 * @author msw
 */
public class TransactionStoreDatabase implements TransactionStore {

    /** The dao. */
    private TransactionDAO dao = null;

    /**
     * Instantiates a new transaction store database.
     */
    public TransactionStoreDatabase() {
        dao = getTransactionDAO();
    }

    /**
     * Gets the transaction dao.
     *
     * @return the transaction dao
     */
    protected TransactionDAO getTransactionDAO() {
        return TransactionDAO.getInstance();
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
        if (dao != null) {
            inserted = dao.insertIntoTransactionRepo(transactionRepo);
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
        if (dao != null) {
            transactionId = dao.getTransactionId(messageId);
        }
        return transactionId;
    }

}
