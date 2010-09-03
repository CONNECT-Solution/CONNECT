/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.fta;

import gov.hhs.fha.nhinc.common.ftaconfigmanager.FTAConfiguration;
import gov.hhs.fha.nhinc.common.ftaconfigmanager.FTAConfigurationHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author dunnek
 */
public class FTATimer extends Thread
{
    private static FTATimer m_oTheOneAndOnlyTimer = null;
    private static boolean m_bRunnable = false;

    private static Log log = LogFactory.getLog(FTATimer.class);
    private static final int FTA_REFRESH_DURATION_DEFAULT = 1800;
    //private static final String FTA_REFRESH_DURATION_PROPERTY = "FTARefreshDuration";
    private int m_iDurationSeconds = FTA_REFRESH_DURATION_DEFAULT;

    private static FTAConfiguration config = null;

    public static void startTimer()
        throws FTATimerException
    {
        

        if (m_oTheOneAndOnlyTimer == null)
        {
            m_bRunnable = true;
            m_oTheOneAndOnlyTimer = new FTATimer();
            try
            {
                m_oTheOneAndOnlyTimer.initialize();
                m_oTheOneAndOnlyTimer.setDaemon(true);
                m_oTheOneAndOnlyTimer.start();
            }
            catch (FTATimerException e)
            {
                m_bRunnable = false;
                m_oTheOneAndOnlyTimer.interrupt();
                m_oTheOneAndOnlyTimer = null;
                String sErrorMessage = "Failed to start the FTA  timer.  Error: " + e.getMessage();
                log.error(sErrorMessage, e);
                throw new FTATimerException(sErrorMessage, e);
            }

            log.info("FTATimer has just been initialized.");
        }
        else
        {
            if(m_bRunnable == false)
            {
                //thread was already intialized, but stopped at one point.
                m_bRunnable = true;
                m_oTheOneAndOnlyTimer.start();
            }
        }
        log.info("FTATimer has just been started.");
    }

    public static void stopTimer()
    {
        log.info("FTATimer has just been shut down.");
        m_bRunnable = false;      

    }
    private void pause()
    {
            pause(m_iDurationSeconds * 1000);
    }
     private void pause(int duration)
    {
            try
            {
                m_oTheOneAndOnlyTimer.sleep(duration);
            }
            catch (InterruptedException ex)
            {
                log.error("Failed to sleep.", ex);
            }
    }
    private void initialize()
        throws FTATimerException
    {
        log.info("begin initialize");

        config = FTAConfigurationHelper.loadFTAConfiguration();
        if (config==null)
        {
            throw new FTATimerException("Unable to load FTA Configuration.");
        }
        else if( config.getInboundChannels() == null)
        {
            throw new FTATimerException("Unable to load FTA Inbound Channels.");
        }
        m_iDurationSeconds = 30;

        log.info("end initialize");
    }
    @Override
    public void run()
    {


        while (m_bRunnable)
        {
            FTATimerTask task = new FTATimerTask();
            task.setConfiguration(config);
            
            task.run();
            pause();

            if(m_bRunnable == false)
            {
                log.debug("breaking loop");
                break;
            }
        }
    }
}
