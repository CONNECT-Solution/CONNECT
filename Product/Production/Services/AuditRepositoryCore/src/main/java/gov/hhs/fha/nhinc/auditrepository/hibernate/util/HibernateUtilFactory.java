/**
 *
 */
package gov.hhs.fha.nhinc.auditrepository.hibernate.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author drfernan
 *
 *         Factory class to load the AuditRepositoryCore spring configuration.
 *
 */
public class HibernateUtilFactory {

    private static final Logger LOG = LoggerFactory.getLogger(HibernateUtilFactory.class);

    private static final String AUDIT_REPO_HIBERNATE = "auditRepoHibernateUtil";

    /**
     * Private constructor to hide the public one.
     */
    private HibernateUtilFactory() {

    }

    /**
     * Private class that holds that loads the spring-beans.xml into the Classpath application context.
     *
     * @author drfernan
     *
     */
    private static class ClassPathSingleton {

        private ClassPathSingleton() {
        }

        public static final ClassPathXmlApplicationContext CONTEXT = new ClassPathXmlApplicationContext(
                new String[] { "classpath:spring-beans.xml" });
    }

    /**
     * Method that returns the Audit Repository HibernateUtil.
     *
     * @return
     */
    public static HibernateUtil getAuditRepoHibernateUtil() {
        ClassPathXmlApplicationContext context = ClassPathSingleton.CONTEXT;

        LOG.debug("Memory address getAuditRepoHibernateUtil {}", context.getId());
        gov.hhs.fha.nhinc.auditrepository.hibernate.util.HibernateUtil hibernateUtil = context
                .getBean(AUDIT_REPO_HIBERNATE, gov.hhs.fha.nhinc.auditrepository.hibernate.util.HibernateUtil.class);
        return hibernateUtil;
    }

}
