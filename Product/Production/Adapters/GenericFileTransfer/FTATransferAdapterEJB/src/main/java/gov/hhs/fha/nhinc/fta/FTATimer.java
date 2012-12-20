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
package gov.hhs.fha.nhinc.fta;

import gov.hhs.fha.nhinc.common.ftaconfigmanager.FTAConfiguration;
import gov.hhs.fha.nhinc.common.ftaconfigmanager.FTAConfigurationHelper;
import org.apache.log4j.Logger;

/**
 * 
 * @author dunnek
 */
public class FTATimer extends Thread {
    private static FTATimer m_oTheOneAndOnlyTimer = null;
    private static boolean m_bRunnable = false;

    private static final Logger LOG = Logger.getLogger(FTATimer.class);
    private static final int FTA_REFRESH_DURATION_DEFAULT = 1800;
    // private static final String FTA_REFRESH_DURATION_PROPERTY = "FTARefreshDuration";
    private int m_iDurationSeconds = FTA_REFRESH_DURATION_DEFAULT;

    private static FTAConfiguration config = null;

    public static void startTimer() throws FTATimerException {

        if (m_oTheOneAndOnlyTimer == null) {
            m_bRunnable = true;
            m_oTheOneAndOnlyTimer = new FTATimer();
            try {
                m_oTheOneAndOnlyTimer.initialize();
                m_oTheOneAndOnlyTimer.setDaemon(true);
                m_oTheOneAndOnlyTimer.start();
            } catch (FTATimerException e) {
                m_bRunnable = false;
                m_oTheOneAndOnlyTimer.interrupt();
                m_oTheOneAndOnlyTimer = null;
                String sErrorMessage = "Failed to start the FTA  timer.  Error: " + e.getMessage();
                LOG.error(sErrorMessage, e);
                throw new FTATimerException(sErrorMessage, e);
            }

            LOG.info("FTATimer has just been initialized.");
        } else {
            if (m_bRunnable == false) {
                // thread was already intialized, but stopped at one point.
                m_bRunnable = true;
                m_oTheOneAndOnlyTimer.start();
            }
        }
        LOG.info("FTATimer has just been started.");
    }

    public static void stopTimer() {
        LOG.info("FTATimer has just been shut down.");
        m_bRunnable = false;

    }

    private void pause() {
        pause(m_iDurationSeconds * 1000);
    }

    private void pause(int duration) {
        try {
            m_oTheOneAndOnlyTimer.sleep(duration);
        } catch (InterruptedException ex) {
            LOG.error("Failed to sleep.", ex);
        }
    }

    private void initialize() throws FTATimerException {
        LOG.info("begin initialize");

        config = FTAConfigurationHelper.loadFTAConfiguration();
        if (config == null) {
            throw new FTATimerException("Unable to load FTA Configuration.");
        } else if (config.getInboundChannels() == null) {
            throw new FTATimerException("Unable to load FTA Inbound Channels.");
        }
        m_iDurationSeconds = 30;

        LOG.info("end initialize");
    }

    @Override
    public void run() {

        while (m_bRunnable) {
            FTATimerTask task = new FTATimerTask();
            task.setConfiguration(config);

            task.run();
            pause();

            if (m_bRunnable == false) {
                LOG.debug("breaking loop");
                break;
            }
        }
    }
}
