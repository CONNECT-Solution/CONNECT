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
package gov.hhs.fha.nhinc.mail;

import gov.hhs.fha.nhinc.event.persistence.HibernateUtil;
import org.apache.log4j.Logger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * Class to Manage the Spring Task Scheduler
 *
 * @author Naresh Subramanyan
 */
public class ManageTaskScheduler {

    private static final Logger LOG = Logger.getLogger(ManageTaskScheduler.class);
    private ThreadPoolTaskScheduler scheduler;

    /**
     * Constructor
     *
     *
     */
    public ManageTaskScheduler(ThreadPoolTaskScheduler scheduler) {
        this.scheduler = scheduler;
    }

    /**
     * Method is called when spring initializes the class
     *
     * @throws Exception
     */
    public void init() throws Exception {
        LOG.info("Inside init method -->TaskScheduler Instance:" + scheduler);
        //Initialize the HibernateUtil within the appserver context
        if (HibernateUtil.getSessionFactory() != null) {
            LOG.info("Inside init method --> HibernateUtil.getSessionFactory()..getClass().getName():" + HibernateUtil.getSessionFactory().getClass().getName());
        } else {
            LOG.info("Inside init method --> HibernateUtil.getSessionFactory():" + HibernateUtil.getSessionFactory());
        }
    }

    /**
     * Method is called to shutdown the Spring default ThreadPoolTaskScheduler
     *
     */
    public void clean() {
        System.out.println("Inside clean method -->TaskScheduler Instance:" + scheduler);
        LOG.info("Inside clean method -->TaskScheduler Instance:" + scheduler);
        //shutdown the scheduler thread if its running
        if (scheduler != null) {
            scheduler.shutdown();
        }
    }
}
