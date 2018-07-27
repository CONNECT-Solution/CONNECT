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
package gov.hhs.fha.nhinc.persistence;

import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author drfernan
 *
 *         Factory class to load and retrieve beans defined for HibernateUtil utility classes.
 *
 */
public class HibernateUtilFactory {

    private static final Logger LOG = LoggerFactory.getLogger(HibernateUtilFactory.class);

    // HibernateUtil members for the utility classes belonging to CoreLib
    private static gov.hhs.fha.nhinc.logging.transaction.persistance.HibernateUtil transactionHibernateUtil;
    private static gov.hhs.fha.nhinc.event.persistence.HibernateUtil eventHibernateUtil;
    private static gov.hhs.fha.nhinc.asyncmsgs.persistence.HibernateUtil asyncMsgsHibernateUtil;
    private static gov.hhs.fha.nhinc.common.connectionmanager.persistence.HibernateUtil connManHibernateUtil;
    private static gov.hhs.fha.nhinc.docrepository.adapter.persistence.HibernateUtil docRepoHibernateUtil;
    private static gov.hhs.fha.nhinc.patientcorrelation.nhinc.persistence.HibernateUtil patientCorrHibernateUtil;
    private static gov.hhs.fha.nhinc.direct.messagemonitoring.persistence.HibernateUtil msgMonitorHibernateUtil;
    private static gov.hhs.fha.nhinc.patientdiscovery.persistence.HibernateUtil patientDiscoveryHibernateUtil;

    /**
     * Private constructor to hide the public one.
     */
    private HibernateUtilFactory() {

    }

    /**
     * Singleton class that holds the ClassPathXmlApplicationContext
     *
     * @author drfernan
     *
     */
    private static class ClassPathSingleton {

        public static final ClassPathXmlApplicationContext CONTEXT = new ClassPathXmlApplicationContext(
            new String[] { "classpath:CONNECT-context.xml" });

        private ClassPathSingleton() {
        }
    }

    /**
     * Method that returns the Transaction HibernateUtil
     *
     * @return
     */
    public static gov.hhs.fha.nhinc.logging.transaction.persistance.HibernateUtil getTransactionHibernateUtil() {
        ClassPathXmlApplicationContext context = ClassPathSingleton.CONTEXT;

        LOG.debug("Memory address transactionHibernateUtil {}", context.getId());
        if (transactionHibernateUtil == null) {
            transactionHibernateUtil = context.getBean(NhincConstants.TRANSACTION_HIBERNATE_BEAN,
                gov.hhs.fha.nhinc.logging.transaction.persistance.HibernateUtil.class);
        }
        return transactionHibernateUtil;
    }

    public static gov.hhs.fha.nhinc.event.persistence.HibernateUtil getEventHibernateUtil() {
        ClassPathXmlApplicationContext context = ClassPathSingleton.CONTEXT;

        LOG.debug("Memory address eventHibernateUtil {}", context.getId());
        if (eventHibernateUtil == null) {
            eventHibernateUtil = context.getBean(NhincConstants.EVENT_HIBERNATE_BEAN,
                gov.hhs.fha.nhinc.event.persistence.HibernateUtil.class);
        }
        return eventHibernateUtil;
    }

    /**
     * Method that returns the Asynchronous Message HibernateUtil
     */
    public static gov.hhs.fha.nhinc.asyncmsgs.persistence.HibernateUtil getAsyncMsgsHibernateUtil() {
        ClassPathXmlApplicationContext context = ClassPathSingleton.CONTEXT;

        LOG.debug("Memory address in getAsyncMsgsHibernateUtil {}", context.getId());

        if (asyncMsgsHibernateUtil == null) {
            asyncMsgsHibernateUtil = context.getBean(NhincConstants.ASYNC_MSG_HIBERNATE_BEAN,
                gov.hhs.fha.nhinc.asyncmsgs.persistence.HibernateUtil.class);
        }

        return asyncMsgsHibernateUtil;
    }

    /**
     * Method that returns the Connection Manager HibernateUtil
     *
     * @return
     */
    public static gov.hhs.fha.nhinc.common.connectionmanager.persistence.HibernateUtil getConnManHibernateUtil() {
        ClassPathXmlApplicationContext context = ClassPathSingleton.CONTEXT;

        LOG.debug("Memory address getConnManHibernateUtil {}", context.getId());

        if (connManHibernateUtil == null) {
            connManHibernateUtil = context.getBean(NhincConstants.CONNECTION_HIBERNATE_BEAN,
                gov.hhs.fha.nhinc.common.connectionmanager.persistence.HibernateUtil.class);
        }
        return connManHibernateUtil;
    }

    /**
     * Method that returns the Document Repository HibernateUtil
     *
     * @return
     */
    public static gov.hhs.fha.nhinc.docrepository.adapter.persistence.HibernateUtil getDocRepoHibernateUtil() {
        ClassPathXmlApplicationContext context = ClassPathSingleton.CONTEXT;

        LOG.debug("Memory address getDocRepoHibernateUtil {}", context.getId());
        if (docRepoHibernateUtil == null) {
            docRepoHibernateUtil = context.getBean(NhincConstants.DOCREPO_HIBERNATE_BEAN,
                gov.hhs.fha.nhinc.docrepository.adapter.persistence.HibernateUtil.class);
        }
        return docRepoHibernateUtil;
    }

    /**
     * Method that returns the Patient Correlation HibernateUtil.
     *
     * @return
     */
    public static gov.hhs.fha.nhinc.patientcorrelation.nhinc.persistence.HibernateUtil getPatientCorrHibernateUtil() {
        ClassPathXmlApplicationContext context = ClassPathSingleton.CONTEXT;

        LOG.debug("Memory address getPatientCorrHibernateUtil {}", context.getId());
        if (patientCorrHibernateUtil == null) {
            patientCorrHibernateUtil = context.getBean(NhincConstants.PATIENT_CORR_HIBERNATE_BEAN,
                gov.hhs.fha.nhinc.patientcorrelation.nhinc.persistence.HibernateUtil.class);
        }
        return patientCorrHibernateUtil;
    }

    /**
     * Method that returns the Message monitoring HibernateUtil.
     *
     * @return
     */
    public static gov.hhs.fha.nhinc.direct.messagemonitoring.persistence.HibernateUtil getMsgMonitorHibernateUtil() {
        ClassPathXmlApplicationContext context = ClassPathSingleton.CONTEXT;

        LOG.debug("Memory address geMsgMonitorHibernateUtil {}", context.getId());
        if (msgMonitorHibernateUtil == null) {
            msgMonitorHibernateUtil = context.getBean(NhincConstants.MSG_MONITOR_HIBERNATE_BEAN,
                gov.hhs.fha.nhinc.direct.messagemonitoring.persistence.HibernateUtil.class);
        }
        return msgMonitorHibernateUtil;
    }

    public static gov.hhs.fha.nhinc.patientdiscovery.persistence.HibernateUtil getPatientDiscoveryHibernateUtil() {
        ClassPathXmlApplicationContext context = ClassPathSingleton.CONTEXT;

        LOG.debug("Memory address getPatientDiscoveryHibernateUtil {}", context.getId());
        if (patientDiscoveryHibernateUtil == null) {
            patientDiscoveryHibernateUtil = context.getBean(NhincConstants.PATIENT_DISCOVERY_HIBERNATE_BEAN,
                gov.hhs.fha.nhinc.patientdiscovery.persistence.HibernateUtil.class);
        }
        return patientDiscoveryHibernateUtil;
    }

}
