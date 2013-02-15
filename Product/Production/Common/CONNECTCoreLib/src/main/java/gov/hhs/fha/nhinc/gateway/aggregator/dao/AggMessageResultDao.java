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
import gov.hhs.fha.nhinc.gateway.aggregator.model.AggMessageResult;
import gov.hhs.fha.nhinc.gateway.aggregator.persistence.HibernateUtil;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 * This class is responsible for the persistence of data into the AGGREGATOR.AGG_MESSAGE_RESULTS table. This is
 * primarily used if you want to update information in this table independent of changed to the
 * AGGREGATOR.AGG_TRANSACTION table.
 * 
 * @author Les Westberg
 */
public class AggMessageResultDao {
    private static final Logger LOG = Logger.getLogger(AggMessageResultDao.class);

    /**
     * Default constructor.
     */
    public AggMessageResultDao() {
    }

    /**
     * This method saves the specified data into the AGGREGATOR_AGG_MESSAGE_RESULTS tables.
     * 
     * @param AggMessageResult
     *            The data to be written to the table.
     */
    public void save(AggMessageResult oAggMessageResult) {

        String sMessageId = "";
        if ((oAggMessageResult != null) && (oAggMessageResult.getMessageId() != null)
                && (oAggMessageResult.getMessageId().length() > 0)) {
            sMessageId = oAggMessageResult.getMessageId();
        }

        LOG.debug("Performing AggMessageResult save for TransactionId: "
                + ((sMessageId.length() == 0) ? "New Record" : sMessageId));

        HibernateUtil.save(oAggMessageResult, sMessageId, "MessageId");

    }

    /**
     * Delete a row in the AGGREGATOR.AGG_MESSAGE_RESULTS table.
     * 
     * @param document
     *            Document to delete
     */
    public void delete(AggMessageResult oAggMessageResult) {
        String sMessageId = null;
        if (oAggMessageResult != null) {
            sMessageId = oAggMessageResult.getMessageId();
        } else {
            LOG.warn("Attempt to delete AggMessageResults but the value was null.");
            return; // there is nothing to delete.
        }

        LOG.debug("Performing AggMessageResults delete for MessageId: " + sMessageId);

        HibernateUtil.delete(oAggMessageResult, sMessageId, "MessageId");

    }

    /**
     * Retrieve a record by identifier (MessageId)
     * 
     * @param sMessageId
     *            Message ID for the message result being returned.
     * @return Retrieved message result.
     */
    public AggMessageResult findById(String sMessageId) {
        LOG.debug("Performing AggMessageResults findById for MessageId: " + sMessageId);

        AggMessageResult oAggMessageResult = (AggMessageResult) HibernateUtil.findById(AggMessageResult.class,
                sMessageId, sMessageId, "TransactionId");
        return oAggMessageResult;

    }

    /**
     * Retrieve the record based on the specified message key.
     * 
     * @param sTransactionId
     *            The transaction Id of the set of messages.
     * @param sMessageKey
     *            The message key that uniquely identifies this record.
     * @return The row from the table that has this message key.
     * @throws AggregatorException
     *             This is thrown if there is an issue with the passed in parameter.
     */
    @SuppressWarnings("unchecked")
    // Occurs because of olAggTransaction = query.list(); - but it is safe so suppress the warning
    public AggMessageResult findByMessageKey(String sTransactionId, String sMessageKey) throws AggregatorException {
        List<AggMessageResult> olAggMessageResult = new ArrayList<AggMessageResult>();
        if ((sTransactionId == null) || (sTransactionId.length() <= 0)) {
            String sErrorMessage = "AggMessageResultDao.findByMessagekey(sTransactionId, sMessageKey) must be called with a valid transaction Id but it was null or empty.";
            LOG.error(sErrorMessage);
            throw new AggregatorException(sErrorMessage);
        }

        if ((sMessageKey == null) || (sMessageKey.length() <= 0)) {
            String sErrorMessage = "AggMessageResultDao.findByMessagekey(sTransactionId, sMessageKey) must be called with a valid message key but it was null or empty.";
            LOG.error(sErrorMessage);
            throw new AggregatorException(sErrorMessage);
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Performing AggMessageResultsDao.findByMessageKey(" + sTransactionId + ", " + sMessageKey + ").");
        }

        Session oSession = null;
        try {
            SessionFactory oSessionFactory = HibernateUtil.getSessionFactory();
            if (oSessionFactory != null) {
                oSession = oSessionFactory.openSession();
                if (oSession != null) {
                    Query query = oSession.getNamedQuery("findByMessageKey");
                    query.setParameter("transactionId", sTransactionId);
                    query.setParameter("messageKey", sMessageKey);
                    olAggMessageResult = query.list();
                } else {
                    LOG.error("Failed to obtain a session from the sessionFactory " + "while calling findByMessageKey("
                            + sTransactionId + ", " + sMessageKey + ").  ");
                }
            } else {
                LOG.error("Session factory was null while calling findByKey(" + sTransactionId + ", " + sMessageKey
                        + ").");
            }
        } finally {
            if (oSession != null) {
                try {
                    oSession.close();
                } catch (Throwable t) {
                    LOG.error("Failed to close session" + "while calling findByMessageKey(" + sTransactionId + ", "
                            + sMessageKey + ").  " + "Message: " + t.getMessage(), t);
                }
            }
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Completed AggMessageResultDao.findByMessageKey(" + sTransactionId + ", " + sMessageKey + ").  "
                    + "Result was: "
                    + (((olAggMessageResult != null) && (olAggMessageResult.size() > 0)) ? "" : "not ") + "found");
        }

        // Note we should get either 0 or 1 record. There should never be a case when
        // we get more than one record for this key. If we do, the database is corrupted.
        // --------------------------------------------------------------------------------
        if ((olAggMessageResult != null) && (olAggMessageResult.size() == 1)) {
            return olAggMessageResult.toArray(new AggMessageResult[0])[0];
        } else if ((olAggMessageResult != null) && (olAggMessageResult.size() > 1)) {
            String sErrorMessage = "AggMessageResult.findByMessgeKey(" + sTransactionId + ", " + sMessageKey
                    + ") returned " + olAggMessageResult.size()
                    + "results.  It should have only returned 0 or 1 results.";
            LOG.error(sErrorMessage);
            throw new AggregatorException(sErrorMessage);
        } else {
            return null;
        }
    }

}
