/*
 * Copyright (c) 2009-2018, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.mail;

import gov.hhs.fha.nhinc.event.persistence.HibernateUtil;
import gov.hhs.fha.nhinc.persistence.HibernateUtilFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * Class to Manage the Spring Task Scheduler
 *
 * @author Naresh Subramanyan
 */
public class ManageTaskScheduler {

    private static final Logger LOG = LoggerFactory.getLogger(ManageTaskScheduler.class);
    private ThreadPoolTaskScheduler scheduler;

    /**
     * Constructor
     * <p>
     * <p>
     * @param scheduler
     */
    public ManageTaskScheduler(ThreadPoolTaskScheduler scheduler) {
        this.scheduler = scheduler;
    }

    /**
     * Method is called when spring initializes the class
     * <p>
     */
    public void init() {
        LOG.info("Inside init method -->TaskScheduler Instance:" + scheduler);
        // Initialize the HibernateUtil within the appserver context
        HibernateUtil hibernateUtil = HibernateUtilFactory.getEventHibernateUtil();
        if (hibernateUtil.getSessionFactory() != null) {
            LOG.info("Inside init method --> HibernateUtil.getSessionFactory()..getClass().getName(): {}",
                hibernateUtil.getSessionFactory().getClass().getName());
        } else {
            LOG.info("Inside init method --> HibernateUtil.getSessionFactory(): {}", hibernateUtil.getSessionFactory());
        }
    }

    /**
     * Method is called to shutdown the Spring default ThreadPoolTaskScheduler
     * <p>
     */
    public void clean() {
        LOG.info("Inside clean method -->TaskScheduler Instance:" + scheduler);
        // shutdown the scheduler thread if its running
        if (scheduler != null) {
            scheduler.shutdown();
        }
    }
}
