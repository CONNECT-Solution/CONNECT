/**
 *
 */
package gov.hhs.fha.nhinc.exchangemgr;

import gov.hhs.fha.nhinc.connectmgr.persistance.dao.ExchangeInfoDAOFileImpl;
import gov.hhs.fha.nhinc.exchange.ExchangeInfoType;
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
public class ExchangeSchedulerConfig {

    private static final Logger LOG = LoggerFactory.getLogger(ExchangeSchedulerConfig.class);
    private static final int DEFAULT_EXCHANGE_REFRESH_INTERVAL = 1440; // a day in minutes

    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(1);
        threadPoolTaskScheduler.setThreadNamePrefix("ExchangeThreadPoolTaskScheduler");
        return threadPoolTaskScheduler;
    }


    @Bean
    @SuppressWarnings("unchecked")
    public ScheduledFuture<ExchangeScheduler> deferredTaskManager(ThreadPoolTaskScheduler scheduler) {
        long intervalMinutes = DEFAULT_EXCHANGE_REFRESH_INTERVAL;
        try {
            ExchangeInfoType exInfo = ExchangeInfoDAOFileImpl.getInstance().loadExchangeInfo();
            long interval = exInfo.getRefreshInterval();
            if (interval <= 0) {
                intervalMinutes = DEFAULT_EXCHANGE_REFRESH_INTERVAL;
            } else {
                intervalMinutes = interval;
            }
        } catch (ExchangeManagerException e) {
            LOG.error("Could not set interval rate. Defaulting to {} minutes by default. Error is : {}",
                DEFAULT_EXCHANGE_REFRESH_INTERVAL, e.getMessage());
            LOG.error("Exception Occurred:", e);
        }

        PeriodicTrigger periodicTrigger = new PeriodicTrigger(intervalMinutes, TimeUnit.MINUTES);
        periodicTrigger.setInitialDelay(intervalMinutes);

        return (ScheduledFuture<ExchangeScheduler>) scheduler.schedule(new ExchangeScheduler(),
            periodicTrigger);

    }
}
