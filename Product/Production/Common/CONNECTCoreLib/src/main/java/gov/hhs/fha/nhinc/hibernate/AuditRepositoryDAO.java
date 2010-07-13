/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.hibernate;

import gov.hhs.fha.nhinc.hibernate.util.*;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import java.util.Date;
import org.hibernate.Criteria;
import org.hibernate.criterion.Expression;

/**
 * AuditRepositoryDAO Class provides methods to query and update Audit Data to/from MySQL Database using Hibernate
 * @author svalluripalli
 */
public class AuditRepositoryDAO {
    //Log4j logging initiated
    private static Log log = LogFactory.getLog(AuditRepositoryDAO.class);
    private static AuditRepositoryDAO auditDAO = new AuditRepositoryDAO();
    public static String JAVA_IO_TMPDIR = "java.io.tmpdir";

    /**
     * Constructor
     */
    private AuditRepositoryDAO() {
        log.info("AuditRepositoryDAO - Initialized");
    }

    /**
     * Singleton instance returned...
     * @return AuditRepositoryDAO
     */
    public static AuditRepositoryDAO getAuditRepositoryDAOInstance() {
        log.debug("getAuditRepositoryDAOInstance()..");
        return auditDAO;
    }

    /**
     * 
     * @param query
     * @param whereClause
     * @return List<AuditRepositoryRecord>
     */
    public List queryAuditRepository(String query) {
        log.debug("AuditRepositoryDAO.getData() Begin");

        Session session = null;

        List<AuditRepositoryRecord> queryList = null;
        try {
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            session = sessionFactory.openSession();
            log.info("Getting Record");
            queryList = session.createSQLQuery(query).addEntity("auditrepository", AuditRepositoryRecord.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Exception in AuditLog.get() occured due to :" + e.getMessage());
        } finally {
            // Actual contact insertion will happen at this step
            if (session != null) {
                session.flush();
                session.close();
            }
        }
 
        log.debug("AuditRepositoryDAO.getData() end");
        return queryList;
    }

    /**
     * 
     * @param auditList
     * @return boolean
     */
    public boolean insertAuditRepository(List<AuditRepositoryRecord> auditList) {
        log.debug("AuditRepositoryDAO.createAuditRepository() - Begin");
        Session session = null;
        Transaction tx = null;
        boolean result = true;
        if (auditList != null && auditList.size() > 0) {
            int size = auditList.size();
            AuditRepositoryRecord auditRecord = null;

            try {
                SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
                session = sessionFactory.openSession();
                tx = session.beginTransaction();
                log.info("Inserting Record...");
                for (int i = 0; i < size; i++) {
                    auditRecord = (AuditRepositoryRecord) auditList.get(i);
                    session.persist(auditRecord);
                }
                log.info("AuditRepository List Inserted seccussfully...");
                tx.commit();
            } catch (Exception e) {
                result = false;
                if (tx != null) {
                    tx.rollback();
                }
                log.error("Error during insertion caused by :" + e.getMessage());
            } finally {
                // Actual event_log insertion will happen at this step
                if (session != null) {
                    session.close();
                }
            }
        }
        log.debug("AuditRepositoryDAO.createAuditRepository() - End");
        return result;
    }

    /**
     * This method does a query to database to get the Audit Log Messages based on user id and/or patient id and/or 
     * community id and/or timeframe
     * @param eUserId
     * @param ePatientId
     * @param startDate
     * @param endDate
     * @return List
     */
    public List queryAuditRepositoryOnCriteria(String eUserId, String ePatientId, Date startDate, Date endDate) {
        log.debug("AuditRepositoryDAO.getAuditRepositoryOnCriteria() Begin");

        if (eUserId == null && ePatientId == null && startDate == null) {
            log.info("-- No - Input Parameters found for Audit Query --");
            log.debug("AuditRepositoryDAO.getAuditRepositoryOnCriteria() End");
            return null;
        }

        Session session = null;
        List<AuditRepositoryRecord> queryList = null;
        try {
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            session = sessionFactory.openSession();
            log.info("Getting Record");

            // Build the criteria 
            Criteria aCriteria = session.createCriteria(AuditRepositoryRecord.class);
            if (eUserId != null && !eUserId.equals("")) {
                aCriteria.add(Expression.eq("userId", eUserId));
            }
            if (ePatientId != null && !ePatientId.equals("")) {
                aCriteria.add(Expression.eq("receiverPatientId", ePatientId));
            }

            if (startDate != null && endDate != null) {
                aCriteria.add(Expression.between("timeStamp", new Date(startDate.getTime()), new Date(endDate.getTime())));
            } else if (startDate != null && endDate == null) {
                aCriteria.add(Expression.ge("timeStamp", new Date(startDate.getTime())));
            }
            queryList = aCriteria.list();
        } catch (Exception e) {
            log.error("Exception in AuditLog.get() occured due to :" + e.getMessage());
        } finally {
            // Actual contact insertion will happen at this step
            if (session != null) {
                session.flush();
                session.close();
            }
        }
        log.debug("AuditRepositoryDAO.getAuditRepositoryOnCriteria() End");
        return queryList;
    }
}
