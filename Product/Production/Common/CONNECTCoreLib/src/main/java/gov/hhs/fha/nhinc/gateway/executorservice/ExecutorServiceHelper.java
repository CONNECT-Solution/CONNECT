/*
 * Copyright (c) 2009-2019, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.gateway.executorservice;

import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Singleton class that holds the ExecutorService configs as follows - concurrentPoolSize is the size of the pool for
 * the executor service - largejobPoolSize is the size of the pool for the large job executor service -
 * largejobSizePercent is used in checkExecutorTaskIsLarge to determine if a task should be executed using the large job
 * executor service. If the task will fanout to a list of n targets, then the task is a large job if n >=
 * largejobSizePercent * concurrentPoolSize
 *
 * // * - timeoutValues Map // * URLConnection offers setConnectTimeout() and setReadTimeout() methods // * to set the
 * web service urlconnection timeouts. // * Connect timeout is time to establish the http/https urlconnection in millis
 * // * Request timeout is the read timeout for the http/https urlconnection // * (after urlconnection established) in
 * millis // * // * Note that the timeout settings are metro based // * CONNECT_TIMEOUT_NAME =
 * "com.sun.xml.ws.connect.timeout" // * REQUEST_TIMEOUT_NAME = "com.sun.xml.ws.request.timeout"
 *
 * @author paul.eftis
 */
public class ExecutorServiceHelper {

    private static final Logger LOG = LoggerFactory.getLogger(ExecutorServiceHelper.class);

    // default pool size is 100
    private int concurrentPoolSize;
    // default large job pool size is 200
    private int largejobPoolSize;
    // default large job size percent is 75%
    private double largejobSizePercent;

    // timeoutValues no longer used (timeouts set in WebServiceProxyHelper)
    // timeoutValues Map contains web service client timeouts
    private Map timeoutValues = new HashMap();

    // private constructor to ensure singleton
    private ExecutorServiceHelper() {
        try {
            PropertyAccessor propertyAccessor = PropertyAccessor.getInstance();

            // get executor service pool sizes
            String concurrentPoolSizeStr = propertyAccessor.getProperty(NhincConstants.GATEWAY_PROPERTY_FILE,
                    NhincConstants.CONCURRENT_POOL_SIZE);
            concurrentPoolSize = Integer.parseInt(concurrentPoolSizeStr);
            String largejobPoolSizeStr = propertyAccessor.getProperty(NhincConstants.GATEWAY_PROPERTY_FILE,
                    NhincConstants.LARGEJOB_POOL_SIZE);
            largejobPoolSize = Integer.parseInt(largejobPoolSizeStr);
            // get large job percentage
            String largejobSizePercentStr = propertyAccessor.getProperty(NhincConstants.GATEWAY_PROPERTY_FILE,
                    NhincConstants.LARGEJOB_SIZE_PERCENT);

            // convert to a decimal percent; throws exception for illegal Integer values
            largejobSizePercent = (double) new Integer(largejobSizePercentStr) / 100.0;

            if (largejobSizePercent <= 0 || largejobSizePercent >= 1) {
                throw new NumberFormatException("largejobSizePercentString must be between 0 and 100, exclusive");
            }
        } catch (Exception e) {
            LOG.error("ExecutorServiceHelper exception loading config properties so using default values");
            outputCompleteException(e);
            // set default pool size to 100
            concurrentPoolSize = 100;
            // set default large job pool size to 200
            largejobPoolSize = 200;
            // set default large job size percent to 75%
            largejobSizePercent = .75;
        }
        LOG.debug("ExecutorServiceHelper created singleton instance and "
                + "set executor service configuration parameters: " + "concurrentPoolSize=" + concurrentPoolSize
                + " largejobPoolSize=" + largejobPoolSize + " largejobSizePercent=" + largejobSizePercent);
    }

    private static class SingletonHolder {

        public static final ExecutorServiceHelper INSTANCE = new ExecutorServiceHelper();
    }

    // singleton
    public static ExecutorServiceHelper getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public int getExecutorPoolSize() {
        return concurrentPoolSize;
    }

    public int getLargeJobExecutorPoolSize() {
        return largejobPoolSize;
    }

    public double getLargeJobPercentage() {
        return largejobSizePercent;
    }

    public Map getTimeoutValues() {
        return timeoutValues;
    }

    /**
     * Used to determine if a task should be executed using the large job executor service. If targetListCount >=
     * largejobSizePercent * concurrentPoolSize then it is a large job.
     *
     * @param targetListCount is the fan-out count for the task
     * @return boolean true if task should be run using large job executor service
     */
    public boolean checkExecutorTaskIsLarge(int targetListCount) {
        boolean bigJob = false;
        Double maxSize = new Double(largejobSizePercent * concurrentPoolSize);
        if (targetListCount >= maxSize.intValue()) {
            bigJob = true;
            LOG.debug("checkExecutorTaskIsLarge has large job size=" + targetListCount
                    + " so returning LargeJobExecutor");
        }
        return bigJob;
    }

    /**
     * Useful util to dump complete exception stack trace
     *
     * @param e
     */
    public static void outputCompleteException(Exception e) {
        LOG.error("EXCEPTION: " + e.getLocalizedMessage(), e);
    }

    /**
     * Useful util to output exception info in a formatted string
     *
     * @param ex
     * @param target
     * @param serviceName
     * @return
     */
    public static String getFormattedExceptionInfo(Exception ex, NhinTargetSystemType target, String serviceName) {
        String err = "EXCEPTION: " + ex.getClass().getCanonicalName() + "\r\nEXCEPTION Cause Message: "
                + ex.getMessage() + "\r\n";
        Throwable cause = ex.getCause();
        if (cause != null) {
            err += "EXCEPTION Cause: " + cause.getClass().getCanonicalName() + "\r\n";
            err += "EXCEPTION Cause Message: " + cause.getMessage();
        }
        return err;
    }

}
