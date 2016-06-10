/**
 *
 */
package gov.hhs.fha.nhinc.patientdb.persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author drfernan
 *
 *         Factory class to load the PatientDiscoveryCore spring configuration.
 */
public class HibernateUtilFactory {

    private static final Logger LOG = LoggerFactory.getLogger(HibernateUtilFactory.class);
    private static final String PATIENT_DISCOVERY_HIBERNATE = "patientDbHibernateUtil";

    /**
     * Private constructor to hide the public one.
     */
    private HibernateUtilFactory() {

    }

    /**
     * Private class that loads the spring-beans.xml into the classpath application context.
     *
     * @author drfernan
     *
     */
    private static class ClassPathSingleton {

        public static final ClassPathXmlApplicationContext CONTEXT = new ClassPathXmlApplicationContext(
                new String[] { "classpath:spring-beans.xml" });

        private ClassPathSingleton() {
        }
    }

    /**
     * Method that retusn the Patient Discovery HibernateUtil.
     *
     * @return
     */
    public static HibernateUtil getPatientDiscHibernateUtil() {
        ClassPathXmlApplicationContext context = ClassPathSingleton.CONTEXT;

        LOG.debug("Memory address getPatientDiscHibernateUtil {}", context.getId());
        return context.getBean(PATIENT_DISCOVERY_HIBERNATE, HibernateUtil.class);
    }

}
