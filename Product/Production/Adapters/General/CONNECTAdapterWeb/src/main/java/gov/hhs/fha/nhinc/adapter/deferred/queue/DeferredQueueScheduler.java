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
package gov.hhs.fha.nhinc.adapter.deferred.queue;

import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.PeriodicTrigger;

@EnableScheduling
@Configuration
@ComponentScan(basePackages = { "gov.hhs.fha.nhinc" })
public class DeferredQueueScheduler {

    private static final Logger LOG = LoggerFactory.getLogger(DeferredQueueScheduler.class);
    private static final String GATEWAY_PROPERTY_FILE = "gateway";
    private static final String DEFERRED_QUEUE_REFRESH_DURATION_PROPERTY = "DeferredQueueRefreshDuration";
    private static final int DEFERRED_QUEUE_REFRESH_DURATION_DEFAULT = 600; // (10 minutes)

    @Bean(name = "deferredQueueTaskScheduler", destroyMethod = "shutdown")
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(1);
        threadPoolTaskScheduler.setThreadNamePrefix("DeferredQueueThreadPoolTaskScheduler");
        // do not wait for completion of the task
        threadPoolTaskScheduler.setWaitForTasksToCompleteOnShutdown(false);
        return threadPoolTaskScheduler;
    }


    @Bean
    @SuppressWarnings("unchecked")
    public ScheduledFuture<DeferredQueueTimerTask> deferredTaskManager(ThreadPoolTaskScheduler scheduler) {
        // 10 minutes by default.
        int intervalSeconds = DEFERRED_QUEUE_REFRESH_DURATION_DEFAULT;
        try {
            String sDuration = PropertyAccessor.getInstance().getProperty(GATEWAY_PROPERTY_FILE,
                DEFERRED_QUEUE_REFRESH_DURATION_PROPERTY, String.valueOf(DEFERRED_QUEUE_REFRESH_DURATION_DEFAULT));

            intervalSeconds = Integer.parseInt(sDuration);

        } catch (NumberFormatException e) {
            LOG.error("Could not set interval rate. Defaulting to {} seconds by default. Error is : {}",
                DEFERRED_QUEUE_REFRESH_DURATION_DEFAULT, e.getMessage());
            LOG.error("Exception Occurred:", e);
        }

        PeriodicTrigger periodicTrigger = new PeriodicTrigger(intervalSeconds, TimeUnit.SECONDS);
        periodicTrigger.setInitialDelay(intervalSeconds);

        scheduler.setRemoveOnCancelPolicy(true);
        scheduler.setWaitForTasksToCompleteOnShutdown(false);
        return (ScheduledFuture<DeferredQueueTimerTask>) scheduler.schedule(new DeferredQueueTimerTask(),
            periodicTrigger);

    }
}
