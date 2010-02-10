package gov.hhs.fha.nhinc.gateway.aggregator.dao;

import java.util.Date;
import java.util.List;

import java.text.SimpleDateFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import gov.hhs.fha.nhinc.gateway.aggregator.model.AggTransaction;

import gov.hhs.fha.nhinc.gateway.aggregator.persistence.HibernateUtil;

import gov.hhs.fha.nhinc.gateway.aggregator.AggregatorException;

import java.util.ArrayList;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Criteria;
import org.hibernate.criterion.Expression;

/**
 * This class is responsible for the persistence of data into the
 * AGGREGATOR.AGG_TRANSACTION table and also for associated rows
 * in the AGGREGATOR.AGG_MESSAGE_RESULTS table if they are stored
 * at the same time.
 * 
 * @author Les Westberg
 */
public class AggTransactionDao
{
    private static Log log = LogFactory.getLog(AggTransactionDao.class);

    /**
     * Default constructor
     */
    public AggTransactionDao()
    {
    }
    
    /**
     * This method saves the specified data into the AGGREGATOR.AGG_TRANSACTION
     * and AGGREGATOR_AGG_MESSAGE_RESULTS tables and if this is a new
     * transaction, it will assign a new transaction ID..
     * 
     * @param aggTransaction The data to be written to the table.
     */
    public void save(AggTransaction oAggTransaction)
    {
        
        String sTransactionId = "";
        if ((oAggTransaction != null) &&
            (oAggTransaction.getTransactionId() != null) &&
            (oAggTransaction.getTransactionId().length() > 0))
        {
            sTransactionId = oAggTransaction.getTransactionId();
        }

        log.debug("Performing AggTransaction save for TransactionId: " + 
                  ((sTransactionId.length() == 0) ? "New Record" : sTransactionId));

        HibernateUtil.save(oAggTransaction, sTransactionId, "TransactionId");
        
    }
    
    /**
     * Delete a row in the AGGREGATOR.AGG_TRANSACTION table along
     * with the corresponding entries in the AGGREGATOR.AGG_MESSAGE_RESULTS table.
     * 
     * @param document Document to delete
     */
    public void delete(AggTransaction oAggTransaction)
    {
        String sTransactionId = null;
        if (oAggTransaction != null) 
        {
            sTransactionId = oAggTransaction.getTransactionId();
        }
        else
        {
            log.warn("Attempt to delete AggTransaction but the value was null.");
            return;         // there is nothing to delete.
        }

        log.debug("Performing AggTransaction delete for TransactionId: " + sTransactionId);
        
        HibernateUtil.delete(oAggTransaction, sTransactionId, "TransactionId");
        
    }
    
    /**
     * Retrieve a record by identifier  (TransactionId)
     * 
     * @param sTransactionId Transaction ID for the transaction being returned.
     * @return Retrieved transaction.
     */
    public AggTransaction findById(String sTransactionId)
    {
        log.debug("Performing AggTransaction findById for TransactionId: " + sTransactionId);

        AggTransaction oAggTransaction = 
            (AggTransaction) HibernateUtil.findById(AggTransaction.class, 
                                                    sTransactionId, sTransactionId, 
                                                    "TransactionId");
        return oAggTransaction;

    }
    
    /**
     * Retrieve records that are older than the specified date and time.
     * 
     * @param dtDateTime The date in time in which we want to retrieve older records.
     * @return The set of transactions that were retrieved.
     * @throws AggregatorException This exception is thrown if there is an error message.
     */
    @SuppressWarnings("unchecked") // Occurs because of olAggTransaction = oCriteria.list(); - but it is safe so suppress the warning
    public AggTransaction[] findOlderThan(Date dtDateTime)
        throws AggregatorException
    {
        List<AggTransaction> olAggTransaction = new ArrayList<AggTransaction>();
        SimpleDateFormat oFormat = new SimpleDateFormat("MM/dd/yyyy.HH:mm:ss");
        
        String sDateTime = "";
        if (sDateTime != null)
        {
            sDateTime = oFormat.format(dtDateTime);
        }
        else
        {
            String sErrorMessage = "AggTransactionDao.findOlderThan(dtDateTime) must be called with a valid date/time but dtDateTime was null.";
            log.error(sErrorMessage);
            throw new AggregatorException(sErrorMessage);
        }

        if (log.isDebugEnabled())
        {
            log.debug("Performing AggTransactionDao.findOlderThan(" + sDateTime + ").");
        }

        Session oSession = null;
        try
        {
            SessionFactory oSessionFactory = HibernateUtil.getSessionFactory();
            if (oSessionFactory != null)
            {
                oSession = oSessionFactory.openSession();
                if (oSession != null)
                {
                    Criteria oCriteria = oSession.createCriteria(AggTransaction.class);
                    oCriteria.add(Expression.le("transactionStartTime", dtDateTime));
                    olAggTransaction = oCriteria.list();
                }
                else
                {
                    log.error("Failed to obtain a session from the sessionFactory " + 
                              "while calling findOlderThan(" + sDateTime + ").  ");
                }
            }
            else
            {
                log.error("Session factory was null while calling findOlderThan(" +
                          sDateTime + ").");
            }
            
            if (log.isDebugEnabled())
            {
                log.debug("Completed AggTransactionDao.findOlderThan(" + sDateTime + ").  " +
                          "Result was: " + (((olAggTransaction == null) && (olAggTransaction.size() > 0)) ? "not " : "") + "found");
            }
        }
        finally
        {
            if (oSession != null)
            {
                try
                {
                    oSession.close();
                }
                catch (Throwable t)
                {
                    log.error("Failed to close session" + 
                              "while calling findOlderThan(" + sDateTime + ").  " +
                              "Message: " + t.getMessage(), t);
                }
            }
        }
        
        if ((olAggTransaction != null) &&
            (olAggTransaction.size() > 0))
        {
            return olAggTransaction.toArray(new AggTransaction[0]);
        }
        else
        {
            return new AggTransaction[0];
        }
    }
    
}
