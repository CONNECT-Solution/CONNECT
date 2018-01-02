/*
 * Copyright (c) 2009-2017, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.exchangemgr;

import gov.hhs.fha.nhinc.connectmgr.persistance.dao.ExchangeInfoDAOFileImpl;
import gov.hhs.fha.nhinc.exchange.ExchangeInfoType;
import gov.hhs.fha.nhinc.exchangemgr.util.ExchangeDataUpdateMgr;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author tjafri
 */
public class ExchangeScheduler extends Thread {

    private static final Logger LOG = LoggerFactory.getLogger(ExchangeScheduler.class);
    private boolean m_bRunnable = false;
    private long refreshInterval;
    private static final int DEFAULT_EXCHANGE_REFRESH_INTERVAL = 1440; // a day in minutes

    /**
     * Get an instance
     *
     * @return
     */
    protected static ExchangeScheduler getInstance() {
        return InstanceHolder.instance;
    }

    /**
     * This method is used to create an instance of the ExchangeScheduler. There should only be exactly one instance of
     * this running at any time. If it exists, it simply returns the one that exists. If it does not exist, then it will
     * create it, start the timer, and return a handle to it.
     *
     * @throws ExchangeSchedulerException
     */
    public void startTimer() throws ExchangeSchedulerException {
        m_bRunnable = true;

        try {
            getInstance().initialize();
            getInstance().setName("ExchangeScheduler Thread");
            getInstance().setDaemon(true);
            getInstance().start();
        } catch (Exception e) {
            throw new ExchangeSchedulerException("Failed to start the Exchange Scheduler: " + e.getLocalizedMessage(), e);
        }
        LOG.info("ExchangeScheduler has just been started now.");
    }

    public void stopTimer() {
        m_bRunnable = false;
    }

    @Override
    public void run() {
        LOG.info("ExchangeScheduled inside run");
        while (m_bRunnable) {
            getInstance().initialize();//need to initialize scheduler everytime as User can update refreshInterval from AdminGUI
            ExchangeDataUpdateMgr exTask = new ExchangeDataUpdateMgr();
            exTask.task();
            try {
                LOG.info("Putting {} for sleep for {} milliseconds", getInstance().getName(), refreshInterval);
                Thread.sleep(refreshInterval);
            } catch (InterruptedException ex) {
                LOG.error("Failed to put {} to sleep: {}", getInstance().getName(), ex.getLocalizedMessage(), ex);
            }
        }
    }

    private void initialize() {
        try {
            ExchangeInfoType exInfo = ExchangeInfoDAOFileImpl.getInstance().loadExchangeInfo();
            long interval = exInfo.getRefreshInterval();
            if (interval <= 0) {
                refreshInterval = TimeUnit.MINUTES.toMillis(DEFAULT_EXCHANGE_REFRESH_INTERVAL);
            } else {
                refreshInterval = TimeUnit.MINUTES.toMillis(interval);
            }
        } catch (ExchangeManagerException ex) {
            LOG.error("Unable to load exchange information {}", ex.getLocalizedMessage(), ex);
        }
    }

    public static class InstanceHolder {

        private static final ExchangeScheduler instance = new ExchangeScheduler();
    }
}
