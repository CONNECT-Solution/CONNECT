/**
 *
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
        return context.getBean(NhincConstants.TRANSACTION_HIBERNATE_BEAN,
                gov.hhs.fha.nhinc.logging.transaction.persistance.HibernateUtil.class);
    }

    public static gov.hhs.fha.nhinc.event.persistence.HibernateUtil getEventHibernateUtil() {
        ClassPathXmlApplicationContext context = ClassPathSingleton.CONTEXT;

        LOG.debug("Memory address eventHibernateUtil {}", context.getId());
        return context.getBean(NhincConstants.EVENT_HIBERNATE_BEAN,
                gov.hhs.fha.nhinc.event.persistence.HibernateUtil.class);
    }

    /**
     * Method that returns the Asynchronous Message HibernateUtil
     */
    public static gov.hhs.fha.nhinc.asyncmsgs.persistence.HibernateUtil getAsyncMsgsHibernateUtil() {
        ClassPathXmlApplicationContext context = ClassPathSingleton.CONTEXT;

        LOG.debug("Memory address in getAsyncMsgsHibernateUtil {}", context.getId());
        return context.getBean(NhincConstants.ASYNC_MSG_HIBERNATE_BEAN,
                gov.hhs.fha.nhinc.asyncmsgs.persistence.HibernateUtil.class);
    }

    /**
     * Method that returns the Connection Manager HibernateUtil
     *
     * @return
     */
    public static gov.hhs.fha.nhinc.common.connectionmanager.persistence.HibernateUtil getConnManHibernateUtil() {
        ClassPathXmlApplicationContext context = ClassPathSingleton.CONTEXT;

        LOG.debug("Memory address getConnManHibernateUtil {}", context.getId());
        return context.getBean(NhincConstants.CONNECTION_HIBERNATE_BEAN,
                gov.hhs.fha.nhinc.common.connectionmanager.persistence.HibernateUtil.class);
    }

    /**
     * Method that returns the Document Repository HibernateUtil
     *
     * @return
     */
    public static gov.hhs.fha.nhinc.docrepository.adapter.persistence.HibernateUtil getDocRepoHibernateUtil() {
        ClassPathXmlApplicationContext context = ClassPathSingleton.CONTEXT;

        LOG.debug("Memory address getDocRepoHibernateUtil {}", context.getId());
        return context.getBean(NhincConstants.DOCREPO_HIBERNATE_BEAN,
                gov.hhs.fha.nhinc.docrepository.adapter.persistence.HibernateUtil.class);
    }

    /**
     * Method that returns the Patient Correlation HibernateUtil.
     *
     * @return
     */
    public static gov.hhs.fha.nhinc.patientcorrelation.nhinc.persistence.HibernateUtil getPatientCorrHibernateUtil() {
        ClassPathXmlApplicationContext context = ClassPathSingleton.CONTEXT;

        LOG.debug("Memory address getPatientCorrHibernateUtil {}", context.getId());
        return context.getBean(NhincConstants.PATIENT_CORR_HIBERNATE_BEAN,
                gov.hhs.fha.nhinc.patientcorrelation.nhinc.persistence.HibernateUtil.class);
    }

    /**
     * Method that returns the Message monitoring HibernateUtil.
     *
     * @return
     */
    public static gov.hhs.fha.nhinc.direct.messagemonitoring.persistence.HibernateUtil getMsgMonitorHibernateUtil() {
        ClassPathXmlApplicationContext context = ClassPathSingleton.CONTEXT;

        LOG.debug("Memory address geMsgMonitorHibernateUtil {}", context.getId());
        return context.getBean(NhincConstants.MSG_MONITOR_HIBERNATE_BEAN,
                gov.hhs.fha.nhinc.direct.messagemonitoring.persistence.HibernateUtil.class);
    }

}
