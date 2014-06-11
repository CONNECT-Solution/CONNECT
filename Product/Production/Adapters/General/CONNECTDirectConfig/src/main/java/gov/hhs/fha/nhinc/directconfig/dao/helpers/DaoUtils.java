package gov.hhs.fha.nhinc.directconfig.dao.helpers;

import gov.hhs.fha.nhinc.directconfig.persistence.HibernateUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class DaoUtils {

    private static final Log log = LogFactory.getLog(DaoUtils.class);

    /**
     * Opens and returns a Session, using the Hibernate SessionFactory.
     * 
     * @return the opened Session.
     */
    public static Session getSession() {
        Session session = null;
        SessionFactory fact = HibernateUtil.getSessionFactory();

        if (fact != null) {
            session = fact.openSession();
        } else {
            log.error("No Session available - SessionFactory is null.");
        }

        return session;
    }

    /**
     * Attempt (safely) to close the Session.
     */
    public static void closeSession(Session session) {
        if (session != null) {
            try {
                session.close();
            } catch (Exception e) {
                log.error("Failed to close session: " + e.getMessage(), e);
            }
        }
    }

    /**
     * Attempt (safely) to rollback the Transaction.
     */
    public static void rollbackTransaction(Transaction tx) {
        if (tx != null) {
            try {
                log.error("Failed to commit transaction, attempting rollback...");
                tx.rollback();
            } catch (Exception e) {
                log.error("Failed to rollback transaction: " + e.getMessage(), e);
            }
        }
    }
}
