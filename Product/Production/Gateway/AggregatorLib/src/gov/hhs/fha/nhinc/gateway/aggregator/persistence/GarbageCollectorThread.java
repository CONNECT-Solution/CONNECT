package gov.hhs.fha.nhinc.gateway.aggregator.persistence;

import gov.hhs.fha.nhinc.gateway.aggregator.dao.AggTransactionDao;
import gov.hhs.fha.nhinc.gateway.aggregator.model.AggTransaction;
import java.util.Date;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 *
 * @author westbergl
 */
public class GarbageCollectorThread extends Thread
{
    private static Log log = LogFactory.getLog(GarbageCollectorThread.class);
    private Date pivotDate = null;

    /**
     * Construct a garbage collector that will clean anything 
     * older than the specified date.
     */
    public GarbageCollectorThread(Date dtPivotDate)
    {
        pivotDate = dtPivotDate;
    }
    

    /**
     * This runs the GarbageColletor against the aggregator tables.  It will
     * delete anything that is older than the pivot date.
     * 
     */
    @Override
    public void run()
    {
        if (pivotDate != null)
        {
            AggTransactionDao oAggTransactionDao = new AggTransactionDao();
            
            try
            {
                int iNumTrans = 0;
                AggTransaction[] oaAggTransaction = oAggTransactionDao.findOlderThan(pivotDate);
                if ((oaAggTransaction != null) &&
                    (oaAggTransaction.length > 0))
                {
                    for (AggTransaction oAggTransaction : oaAggTransaction)
                    {
                        oAggTransactionDao.delete(oAggTransaction);
                        iNumTrans++;
                    }
                }
                
                log.debug("Aggregator garbage collector cleaned out " + iNumTrans + " stale transactions.");
            }
            catch (Exception e)
            {
                String sErrorMessage = "Aggregator garbage collector failed to read entries from the aggregation tables. " +
                                       "Garbage collection is not being done.  Error: " + e.getMessage();  
                log.error(sErrorMessage, e);
                return;
            }
            
        }
        else
        {
            String sErrorMessage = "Cannot run Aggregator garbage collection - pivot date was not set.";  
            log.error(sErrorMessage);
            return;
        }
        
        return;
    }
}
