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
package gov.hhs.fha.nhinc.connectmgr.uddi;

import gov.hhs.fha.nhinc.properties.PropertyAccessor;

import org.apache.log4j.Logger;

/**
 * This class is used to start a timer which when it wakes up will read the UDDI data from the UDDI server, updte the
 * uddiConnectionInfo.xml file with that data, and then tell the ConnectionManager to refresh the UDDI cache with it.
 * 
 * @author Les Westberg
 */
public class UDDITimer extends Thread {
    private static final Logger LOG = Logger.getLogger(UDDITimer.class);
    private static UDDITimer m_oTheOneAndOnlyTimer = null;
    private static boolean m_bRunnable = false;
    private static final String GATEWAY_PROPERTY_FILE = "gateway";
    private static final String UDDI_REFRESH_DURATION_PROPERTY = "UDDIRefreshDuration";
    private static final int UDDI_REFRESH_DURATION_DEFAULT = 1800; // (30 minutes)
    
    private int m_iDurationSeconds = UDDI_REFRESH_DURATION_DEFAULT;

    /**
     * Default constructor
     */
    private UDDITimer() {
    }

    /**
     * This method is used to crete an instance of the UDDITimer. There should only be exactly one instance of this
     * running at any time. If it exists, it simply returns the one that exists. If it does not exist, then it will
     * create it, start the timer, and return a handle to it.
     * 
     * @throws UDDIAccessorException
     */
    public static void startTimer() throws UDDIAccessorException {
        m_bRunnable = true;

        if (m_oTheOneAndOnlyTimer == null) {
            m_oTheOneAndOnlyTimer = new UDDITimer();
            try {
                m_oTheOneAndOnlyTimer.initialize();
                m_oTheOneAndOnlyTimer.setDaemon(true);
                m_oTheOneAndOnlyTimer.start();
            } catch (Exception e) {
                m_oTheOneAndOnlyTimer = null;
                String sErrorMessage = "Failed to start the UDDI Update Manager timer.  Error: " + e.getMessage();
                LOG.error(sErrorMessage, e);
                throw new UDDIAccessorException(sErrorMessage, e);
            }

            LOG.info("UDDIUpdateManager timer has just been started now.");
        }
    }

    public static void stopTimer() {
        m_bRunnable = false;
    }

    /**
     * This method starts up the timer.
     */
    private void initialize() throws UDDIAccessorException {

        try {
            String sDuration = PropertyAccessor.getInstance().getProperty(GATEWAY_PROPERTY_FILE, UDDI_REFRESH_DURATION_PROPERTY);
            if ((sDuration != null) && (sDuration.length() > 0)) {
                m_iDurationSeconds = Integer.parseInt(sDuration);
            }
        } catch (Exception e) {
            String sErrorMessage = "Failed to read and parse " + UDDI_REFRESH_DURATION_PROPERTY + " from "
                    + GATEWAY_PROPERTY_FILE + ".properties file - using default " + "" + "value of "
                    + UDDI_REFRESH_DURATION_DEFAULT + " seconds.  Error: " + e.getMessage();
            LOG.warn(sErrorMessage, e);
        }
    }

    @Override
    public void run() {
        while (m_bRunnable) {
            UDDITimerTask oUDDITimerTask = new UDDITimerTask();
            oUDDITimerTask.run();
            try {
                LOG.debug("Before reading properties wait status....");
                Thread.sleep(m_iDurationSeconds * 1000);
                LOG.debug("Now read properties....");
            } catch (InterruptedException ex) {
                LOG.error("Failed to sleep.", ex);
            }
        }
    }

    /**
     * Main method used to test this class. This one really should not be run under unit test scenarios because it
     * requires access to the UDDI server.
     * 
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("Starting test.");
        LOG.debug("Log: Starting test.");

        try {
            UDDITimer.startTimer();
            Thread.sleep(70000); // 1 minutes 10 seconds
        } catch (Exception e) {
            System.out.println("An unexpected exception occurred: " + e.getMessage());
            e.printStackTrace();
            System.exit(-1);
        }

        System.out.println("End of test.");

    }
}
