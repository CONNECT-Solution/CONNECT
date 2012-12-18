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
package gov.hhs.fha.nhinc.gateway.aggregator.persistence;

import gov.hhs.fha.nhinc.gateway.aggregator.dao.AggTransactionDao;
import gov.hhs.fha.nhinc.gateway.aggregator.model.AggTransaction;
import java.util.Date;
import org.apache.log4j.Logger;

/**
 * 
 * @author westbergl
 */
public class GarbageCollectorThread extends Thread {
    private static final Logger LOG = Logger.getLogger(GarbageCollectorThread.class);
    private Date pivotDate = null;

    /**
     * Construct a garbage collector that will clean anything older than the specified date.
     */
    public GarbageCollectorThread(Date dtPivotDate) {
        pivotDate = dtPivotDate;
    }

    /**
     * This runs the GarbageColletor against the aggregator tables. It will delete anything that is older than the pivot
     * date.
     * 
     */
    @Override
    public void run() {
        if (pivotDate != null) {
            AggTransactionDao oAggTransactionDao = new AggTransactionDao();

            try {
                int iNumTrans = 0;
                AggTransaction[] oaAggTransaction = oAggTransactionDao.findOlderThan(pivotDate);
                if ((oaAggTransaction != null) && (oaAggTransaction.length > 0)) {
                    for (AggTransaction oAggTransaction : oaAggTransaction) {
                        oAggTransactionDao.delete(oAggTransaction);
                        iNumTrans++;
                    }
                }

                LOG.debug("Aggregator garbage collector cleaned out " + iNumTrans + " stale transactions.");
            } catch (Exception e) {
                String sErrorMessage = "Aggregator garbage collector failed to read entries from the aggregation tables. "
                        + "Garbage collection is not being done.  Error: " + e.getMessage();
                LOG.error(sErrorMessage, e);
                return;
            }

        } else {
            String sErrorMessage = "Cannot run Aggregator garbage collection - pivot date was not set.";
            LOG.error(sErrorMessage);
            return;
        }

        return;
    }
}
