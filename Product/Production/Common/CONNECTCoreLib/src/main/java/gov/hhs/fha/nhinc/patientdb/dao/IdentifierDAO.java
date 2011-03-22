/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2011(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
package gov.hhs.fha.nhinc.patientdb.dao;

import gov.hhs.fha.nhinc.patientdb.persistence.HibernateUtil;
import gov.hhs.fha.nhinc.patientdb.model.Identifier;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.Criteria;
import org.hibernate.criterion.Expression;

/**
 * IdentifierDAO Class provides methods to query and update Identifier Data to/from MySQL Database using Hibernate
 * @author richard.ettema
 */
public class IdentifierDAO {

    private static Log log = LogFactory.getLog(IdentifierDAO.class);

    private static IdentifierDAO identifierDAO = new IdentifierDAO();

    /**
     * Constructor
     */
    private IdentifierDAO() {
        log.info("IdentifierDAO - Initialized");
    }

    /**
     * Singleton instance returned...
     * @return IdentifierDAO
     */
    public static IdentifierDAO getIdentifierDAOInstance() {
        log.debug("getIdentifierDAOInstance()..");
        return identifierDAO;
    }

    // =========================
    // Standard CRUD DML Methods
    // =========================

    /**
     * Create a single <code>Identifier</code> record.  The generated id
     * will be available in the identifierRecord.
     * @param identifierRecord
     * @return boolean
     */
    public boolean create(Identifier identifierRecord) {
        log.debug("IdentifierDAO.create() - Begin");
        Session session = null;
        Transaction tx = null;
        boolean result = true;

        if (identifierRecord != null) {
            try {
                SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
                session = sessionFactory.openSession();
                tx = session.beginTransaction();
                log.info("Inserting Record...");

                session.persist(identifierRecord);

                log.info("Identifier Inserted seccussfully...");
                tx.commit();
            } catch (Exception e) {
                result = false;
                if (tx != null) {
                    tx.rollback();
                }
                log.error("Exception during insertion caused by :" + e.getMessage(), e);
            } finally {
                // Actual Identifier insertion will happen at this step
                if (session != null) {
                    session.close();
                }
            }
        }
        log.debug("IdentifierDAO.create() - End");
        return result;
    }

    /**
     * Read (Query) the database to get a <code>Identifier</code> record based
     * on a known id.
     * @param id
     * @return Identifier
     */
    public Identifier read(Long id) {
        log.debug("IdentifierDAO.read() - Begin");

        if (id == null) {
            log.info("-- id Parameter is required for Identifier Query --");
            log.debug("IdentifierDAO.read() - End");
            return null;
        }

        Session session = null;
        List<Identifier> queryList = null;
        Identifier foundRecord = null;
        try {
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            session = sessionFactory.openSession();
            log.info("Reading Record...");

            // Build the criteria
            Criteria aCriteria = session.createCriteria(Identifier.class);

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
        log.debug("IdentifierDAO.read() - End");
        return foundRecord;
    }

    /**
     * Update a single <code>Identifier</code> record.
     * @param identifierRecord
     * @return boolean
     */
    public boolean update(Identifier identifierRecord) {
        log.debug("IdentifierDAO.update() - Begin");
        Session session = null;
        Transaction tx = null;
        boolean result = true;

        if (identifierRecord != null) {
            try {
                SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
                session = sessionFactory.openSession();
                tx = session.beginTransaction();
                log.info("Updating Record...");

                session.saveOrUpdate(identifierRecord);

                log.info("Identifier Updated seccussfully...");
                tx.commit();
            } catch (Exception e) {
                result = false;
                if (tx != null) {
                    tx.rollback();
                }
                log.error("Exception during update caused by :" + e.getMessage(), e);
            } finally {
                // Actual Identifier update will happen at this step
                if (session != null) {
                    session.close();
                }
            }
        }
        log.debug("IdentifierDAO.update() - End");
        return result;
    }

    /**
     * Delete a <code>Identifier</code> record from the database
     * @param identifierRecord
     */
    public void delete(Identifier identifierRecord) {
        log.debug("IdentifierDAO.delete() - Begin");

        Session session = null;
        try {
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            session = sessionFactory.openSession();
            log.info("Deleting Record...");

            // Delete the Identifier record
            session.delete(identifierRecord);
        } catch (Exception e) {
            log.error("Exception during delete occured due to :" + e.getMessage(), e);
        } finally {
            // Flush and close session
            if (session != null) {
                session.flush();
                session.close();
            }
        }
        log.debug("IdentifierDAO.delete() - End");
    }

}
