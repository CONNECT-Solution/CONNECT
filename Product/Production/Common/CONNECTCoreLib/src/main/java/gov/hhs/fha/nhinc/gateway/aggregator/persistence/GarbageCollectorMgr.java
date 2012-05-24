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

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import java.util.Calendar;

/**
 * This class is used to manage the garbage collection on the AGGREGATOR database tables. It will get run each time a
 * startTransaction is called, and it will use settings in the gateway.properties file to determine how often to kick
 * off the garbage collection thread and how hold a transaction must be to be considered stale.
 * 
 * @author Les Westberg
 */
public class GarbageCollectorMgr {
    private static Log log = LogFactory.getLog(GarbageCollectorMgr.class);
    private static Date dtLastRun = new Date(); // The date that the garbage collector was last run.
    private static final String GATEWAY_PROPERTY_FILE = "gateway";
    private static final String GARBAGE_COLLECT_TIME_DURATION = "aggregatorGarbageCollectionTimeDuration";
    private static final String GARBAGE_COLLECT_STALE_DURATION = "aggregatorGarbageCollectionStaleDuration";

    /**
     * Default constructor.
     */
    public GarbageCollectorMgr() {
    }

    /**
     * This method checks to see if we should run the collector based on the time settings in the gateway.properties
     * file. If we should run it, then true is returnd. Otherwise false is returned.
     * 
     * @return TRUE if we should run the garbage collector.
     */
    private static boolean bRunCollector() {
        String sTimeDuration = "";
        int iTimeDuration = 0;
        try {
            sTimeDuration = PropertyAccessor.getInstance().getProperty(GATEWAY_PROPERTY_FILE, GARBAGE_COLLECT_TIME_DURATION);
            iTimeDuration = Integer.parseInt(sTimeDuration);
        } catch (Exception e) {
            String sErrorMessage = "Failed to read and parse property: " + GARBAGE_COLLECT_TIME_DURATION
                    + " from PropertyFile: " + GATEWAY_PROPERTY_FILE + ".propertues.  No "
                    + "garbage collection will be done on the aggregator tables.";
            log.error(sErrorMessage);
            return false;
        }

        if ((sTimeDuration == null) || (sTimeDuration.length() <= 0)) {
            String sErrorMessage = "Failed to read and parse property: " + GARBAGE_COLLECT_TIME_DURATION
                    + " from PropertyFile: " + GATEWAY_PROPERTY_FILE + ".propertues.  No "
                    + "garbage collection will be done on the aggregator tables.";
            log.error(sErrorMessage);
            return false;
        }

        Calendar oCal = Calendar.getInstance();
        oCal.add(Calendar.SECOND, (-1) * iTimeDuration);

        if (oCal.getTime().getTime() > dtLastRun.getTime()) {
            return true;
        }

        return false;
    }

    /**
     * This method checks to see if it is time to run the garbage collection thread based on the settings in the
     * gateway.properties file. If it is, then it spins off a thread to do garbage collection of the aggregator tables.
     */
    public static void runGarbageCollection() {
        // see if we need to run the collector...
        // ----------------------------------------
        if (bRunCollector()) {
            String sStaleDuration = "";
            int iStaleDuration = 0;

            try {
                sStaleDuration = PropertyAccessor.getInstance().getProperty(GATEWAY_PROPERTY_FILE, GARBAGE_COLLECT_STALE_DURATION);
                iStaleDuration = Integer.parseInt(sStaleDuration);
            } catch (Exception e) {
                String sErrorMessage = "Failed to read and parse property: " + GARBAGE_COLLECT_STALE_DURATION
                        + " from PropertyFile: " + GATEWAY_PROPERTY_FILE + ".propertues.  No "
                        + "garbage collection will be done on the aggregator tables.";
                log.error(sErrorMessage);
                return;
            }

            if ((sStaleDuration == null) || (sStaleDuration.length() <= 0)) {
                String sErrorMessage = "Failed to read and parse property: " + GARBAGE_COLLECT_STALE_DURATION
                        + " from PropertyFile: " + GATEWAY_PROPERTY_FILE + ".propertues.  No "
                        + "garbage collection will be done on the aggregator tables.";
                log.error(sErrorMessage);
                return;
            }

            Calendar oCal = Calendar.getInstance();
            oCal.add(Calendar.SECOND, (-1) * iStaleDuration);

            log.debug("Running aggregator garbage collection thread now.");

            GarbageCollectorThread oCollectorThread = new GarbageCollectorThread(oCal.getTime());
            oCollectorThread.run();
            dtLastRun = new Date();

        }
    }

}
