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
package gov.hhs.fha.nhinc.gateway.aggregator.dao;

import gov.hhs.fha.nhinc.gateway.aggregator.AggregatorException;
import gov.hhs.fha.nhinc.gateway.aggregator.model.AggTransaction;
import gov.hhs.fha.nhinc.gateway.aggregator.persistence.HibernateUtil;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 * This class is responsible for the persistence of data into the AGGREGATOR.AGG_TRANSACTION table and also for
 * associated rows in the AGGREGATOR.AGG_MESSAGE_RESULTS table if they are stored at the same time.
 * 
 * @author Les Westberg
 */
public class AggTransactionDao {
    private static final Logger LOG = Logger.getLogger(AggTransactionDao.class);

    /**
     * Default constructor
     */
    public AggTransactionDao() {
    }

    /**
     * This method saves the specified data into the AGGREGATOR.AGG_TRANSACTION and AGGREGATOR_AGG_MESSAGE_RESULTS
     * tables and if this is a new transaction, it will assign a new transaction ID..
     * 
     * @param aggTransaction
     *            The data to be written to the table.
     */
    public void save(AggTransaction oAggTransaction) {

        String sTransactionId = "";
        if ((oAggTransaction != null) && (oAggTransaction.getTransactionId() != null)
                && (oAggTransaction.getTransactionId().length() > 0)) {
            sTransactionId = oAggTransaction.getTransactionId();
        }

        LOG.debug("Performing AggTransaction save for TransactionId: "
                + ((sTransactionId.length() == 0) ? "New Record" : sTransactionId));

        HibernateUtil.save(oAggTransaction, sTransactionId, "TransactionId");

    }

    /**
     * Delete a row in the AGGREGATOR.AGG_TRANSACTION table along with the corresponding entries in the
     * AGGREGATOR.AGG_MESSAGE_RESULTS table.
     * 
     * @param document
     *            Document to delete
     */
    public void delete(AggTransaction oAggTransaction) {
        String sTransactionId = null;
        if (oAggTransaction != null) {
            sTransactionId = oAggTransaction.getTransactionId();
        } else {
            LOG.warn("Attempt to delete AggTransaction but the value was null.");
            return; // there is nothing to delete.
        }

        LOG.debug("Performing AggTransaction delete for TransactionId: " + sTransactionId);

        HibernateUtil.delete(oAggTransaction, sTransactionId, "TransactionId");

    }

    /**
     * Retrieve a record by identifier (TransactionId)
     * 
     * @param sTransactionId
     *            Transaction ID for the transaction being returned.
     * @return Retrieved transaction.
     */
    public AggTransaction findById(String sTransactionId) {
        LOG.debug("Performing AggTransaction findById for TransactionId: " + sTransactionId);

        AggTransaction oAggTransaction = (AggTransaction) HibernateUtil.findById(AggTransaction.class, sTransactionId,
                sTransactionId, "TransactionId");
        return oAggTransaction;

    }

    /**
     * Retrieve records that are older than the specified date and time.
     * 
     * @param dtDateTime
     *            The date in time in which we want to retrieve older records.
     * @return The set of transactions that were retrieved.
     * @throws AggregatorException
     *             This exception is thrown if there is an error message.
     */
    @SuppressWarnings("unchecked")
    // Occurs because of olAggTransaction = query.list(); - but it is safe so suppress the warning
    public AggTransaction[] findOlderThan(Date dtDateTime) throws AggregatorException {
        List<AggTransaction> olAggTransaction = new ArrayList<AggTransaction>();
        SimpleDateFormat oFormat = new SimpleDateFormat("MM/dd/yyyy.HH:mm:ss");

        String sDateTime = oFormat.format(dtDateTime);
        if (sDateTime == null) {
            String sErrorMessage = "AggTransactionDao.findOlderThan(dtDateTime) must be called with a valid date/time but dtDateTime was null.";
            LOG.error(sErrorMessage);
            throw new AggregatorException(sErrorMessage);
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Performing AggTransactionDao.findOlderThan(" + sDateTime + ").");
        }

        Session oSession = null;
        try {
            SessionFactory oSessionFactory = HibernateUtil.getSessionFactory();
            if (oSessionFactory != null) {
                oSession = oSessionFactory.openSession();
                if (oSession != null) {
                    Query query = oSession.getNamedQuery("findOlderThan");
                    query.setParameter("transactionStartTime", dtDateTime);
                    olAggTransaction = query.list();
                } else {
                    LOG.error("Failed to obtain a session from the sessionFactory " + "while calling findOlderThan("
                            + sDateTime + ").  ");
                }
            } else {
                LOG.error("Session factory was null while calling findOlderThan(" + sDateTime + ").");
            }

            if (LOG.isDebugEnabled()) {
                LOG.debug("Completed AggTransactionDao.findOlderThan(" + sDateTime + ").  " + "Result was: "
                        + (((olAggTransaction != null) && (olAggTransaction.size() > 0)) ? "" : "not ") + "found");
            }
        } finally {
            if (oSession != null) {
                try {
                    oSession.close();
                } catch (Throwable t) {
                    LOG.error("Failed to close session" + "while calling findOlderThan(" + sDateTime + ").  "
                            + "Message: " + t.getMessage(), t);
                }
            }
        }

        if ((olAggTransaction != null) && (olAggTransaction.size() > 0)) {
            return olAggTransaction.toArray(new AggTransaction[0]);
        } else {
            return new AggTransaction[0];
        }
    }

}
