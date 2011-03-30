/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2011(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
package gov.hhs.fha.nhinc.patientdb.dao;

import gov.hhs.fha.nhinc.patientdb.persistence.HibernateUtil;
import gov.hhs.fha.nhinc.patientdb.model.Phonenumber;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.Criteria;
import org.hibernate.criterion.Expression;

/**
 * PhonenumberDAO Class provides methods to query and update Phonenumber Data to/from MySQL Database using Hibernate
 * @author richard.ettema
 */
public class PhonenumberDAO {

    private static Log log = LogFactory.getLog(PhonenumberDAO.class);

    private static PhonenumberDAO phonenumberDAO = new PhonenumberDAO();

    /**
     * Constructor
     */
    private PhonenumberDAO() {
        log.info("PhonenumberDAO - Initialized");
    }

    /**
     * Singleton instance returned...
     * @return PhonenumberDAO
     */
    public static PhonenumberDAO getPhonenumberDAOInstance() {
        log.debug("getPhonenumberDAOInstance()..");
        return phonenumberDAO;
    }

    // =========================
    // Standard CRUD DML Methods
    // =========================

    /**
     * Create a single <code>Phonenumber</code> record.  The generated id
     * will be available in the phonenumberRecord.
     * @param phonenumberRecord
     * @return boolean
     */
    public boolean create(Phonenumber phonenumberRecord) {
        log.debug("PhonenumberDAO.create() - Begin");
        Session session = null;
        Transaction tx = null;
        boolean result = true;

        if (phonenumberRecord != null) {
            try {
                SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
                session = sessionFactory.openSession();
                tx = session.beginTransaction();
                log.info("Inserting Record...");

                session.persist(phonenumberRecord);

                log.info("Phonenumber Inserted seccussfully...");
                tx.commit();
            } catch (Exception e) {
                result = false;
                if (tx != null) {
                    tx.rollback();
                }
                log.error("Exception during insertion caused by :" + e.getMessage(), e);
            } finally {
                // Actual Phonenumber insertion will happen at this step
                if (session != null) {
                    session.close();
                }
            }
        }
        log.debug("PhonenumberDAO.create() - End");
        return result;
    }

    /**
     * Read (Query) the database to get a <code>Phonenumber</code> record based
     * on a known id.
     * @param id
     * @return Phonenumber
     */
    public Phonenumber read(Long id) {
        log.debug("PhonenumberDAO.read() - Begin");

        if (id == null) {
            log.info("-- id Parameter is required for Phonenumber Query --");
            log.debug("PhonenumberDAO.read() - End");
            return null;
        }

        Session session = null;
        List<Phonenumber> queryList = null;
        Phonenumber foundRecord = null;
        try {
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            session = sessionFactory.openSession();
            log.info("Reading Record...");

            // Build the criteria
            Criteria aCriteria = session.createCriteria(Phonenumber.class);

            aCriteria.add(Expression.eq("id", id));

            queryList = aCriteria.list();

            if (queryList != null && queryList.size() > 0) {
                foundRecord = queryList.get(0);
            }
        } catch (Exception e) {
            log.error("Exception during read occured due to :" + e.getMessage(), e);
        } finally {
            // Flush and close session
            if (session != null) {
                session.flush();
                session.close();
            }
        }
        log.debug("PhonenumberDAO.read() - End");
        return foundRecord;
    }

    /**
     * Update a single <code>Phonenumber</code> record.
     * @param phonenumberRecord
     * @return boolean
     */
    public boolean update(Phonenumber phonenumberRecord) {
        log.debug("PhonenumberDAO.update() - Begin");
        Session session = null;
        Transaction tx = null;
        boolean result = true;

        if (phonenumberRecord != null) {
            try {
                SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
                session = sessionFactory.openSession();
                tx = session.beginTransaction();
                log.info("Updating Record...");

                session.saveOrUpdate(phonenumberRecord);

                log.info("Phonenumber Updated seccussfully...");
                tx.commit();
            } catch (Exception e) {
                result = false;
                if (tx != null) {
                    tx.rollback();
                }
                log.error("Exception during update caused by :" + e.getMessage(), e);
            } finally {
                // Actual Phonenumber update will happen at this step
                if (session != null) {
                    session.close();
                }
            }
        }
        log.debug("PhonenumberDAO.update() - End");
        return result;
    }

    /**
     * Delete a <code>Phonenumber</code> record from the database
     * @param phonenumberRecord
     */
    public void delete(Phonenumber phonenumberRecord) {
        log.debug("PhonenumberDAO.delete() - Begin");

        Session session = null;
        try {
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            session = sessionFactory.openSession();
            log.info("Deleting Record...");

            // Delete the Phonenumber record
            session.delete(phonenumberRecord);
        } catch (Exception e) {
            log.error("Exception during delete occured due to :" + e.getMessage(), e);
        } finally {
            // Flush and close session
            if (session != null) {
                session.flush();
                session.close();
            }
        }
        log.debug("PhonenumberDAO.delete() - End");
    }

    // =========================
    //     Find DML Methods
    // =========================

    /**
     * Read (Query) the database to get a <code>Phonenumber</code> record based
     * on a known patientId.
     * @param patientId
     * @return List<Phonenumber>
     */
    public List<Phonenumber> findPatientPhonenumbers(Long patientId) {
        log.debug("PhonenumberDAO.findPatientPhonenumbers() - Begin");

        if (patientId == null) {
            log.info("-- patientId Parameter is required for Phonenumber Query --");
            log.debug("PhonenumberDAO.findPatientPhonenumbers() - End");
            return null;
        }

        Session session = null;
        List<Phonenumber> queryList = null;
        try {
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            session = sessionFactory.openSession();
            log.info("Reading Record...");

            // Build the criteria
            Criteria aCriteria = session.createCriteria(Phonenumber.class);

            aCriteria.add(Expression.eq("patient.patientId", patientId));

            queryList = aCriteria.list();
        } catch (Exception e) {
            log.error("Exception during read occured due to :" + e.getMessage(), e);
        } finally {
            // Flush and close session
            if (session != null) {
                session.flush();
                session.close();
            }
        }
        log.debug("PhonenumberDAO.findPatientPhonenumbers() - End");
        return queryList;
    }

}
