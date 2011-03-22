/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2011(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
package gov.hhs.fha.nhinc.patientdb.dao;

import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import java.sql.Timestamp;

import gov.hhs.fha.nhinc.patientdb.persistence.HibernateUtil;
import gov.hhs.fha.nhinc.patientdb.model.Patient;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.criterion.Expression;

/**
 * PatientDAO Class provides methods to query and update Patient Data to/from MySQL Database using Hibernate
 * @author richard.ettema
 */
public class PatientDAO {

    private static Log log = LogFactory.getLog(PatientDAO.class);

    private static PatientDAO patientDAO = new PatientDAO();

    private static final String ALLOW_SSN_QUERY = "mpi.db.allow.ssn.query";

    /**
     * Constructor
     */
    private PatientDAO() {
        log.info("PatientDAO - Initialized");
    }

    /**
     * Singleton instance returned...
     * @return PatientDAO
     */
    public static PatientDAO getPatientDAOInstance() {
        log.debug("getPatientDAOInstance()..");
        return patientDAO;
    }

    // =========================
    // Standard CRUD DML Methods
    // =========================

    /**
     * Create a single <code>Patient</code> record.  The generated id
     * will be available in the patientRecord.
     * @param patientRecord
     * @return boolean
     */
    public boolean create(Patient patientRecord) {
        log.debug("PatientDAO.create() - Begin");
        Session session = null;
        Transaction tx = null;
        boolean result = true;

        if (patientRecord != null) {
            try {
                SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
                session = sessionFactory.openSession();
                tx = session.beginTransaction();
                log.info("Inserting Record...");

                session.persist(patientRecord);

                log.info("Patient Inserted seccussfully...");
                tx.commit();
            } catch (Exception e) {
                result = false;
                if (tx != null) {
                    tx.rollback();
                }
                log.error("Exception during insertion caused by :" + e.getMessage(), e);
            } finally {
                // Actual Patient insertion will happen at this step
                if (session != null) {
                    session.close();
                }
            }
        }
        log.debug("PatientDAO.create() - End");
        return result;
    }

    /**
     * Read (Query) the database to get a <code>Patient</code> record based
     * on a known id.
     * @param id
     * @return Patient
     */
    public Patient read(Long id) {
        log.debug("PatientDAO.read() - Begin");

        if (id == null) {
            log.info("-- id Parameter is required for Patient Query --");
            log.debug("PatientDAO.read() - End");
            return null;
        }

        Session session = null;
        List<Patient> queryList = null;
        Patient foundRecord = null;
        try {
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            session = sessionFactory.openSession();
            log.info("Reading Record...");

            // Build the criteria
            Criteria aCriteria = session.createCriteria(Patient.class);

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
        log.debug("PatientDAO.read() - End");
        return foundRecord;
    }

    /**
     * Update a single <code>Patient</code> record.
     * @param patientRecord
     * @return boolean
     */
    public boolean update(Patient patientRecord) {
        log.debug("PatientDAO.update() - Begin");
        Session session = null;
        Transaction tx = null;
        boolean result = true;

        if (patientRecord != null) {
            try {
                SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
                session = sessionFactory.openSession();
                tx = session.beginTransaction();
                log.info("Updating Record...");

                session.saveOrUpdate(patientRecord);

                log.info("Patient Updated seccussfully...");
                tx.commit();
            } catch (Exception e) {
                result = false;
                if (tx != null) {
                    tx.rollback();
                }
                log.error("Exception during update caused by :" + e.getMessage(), e);
            } finally {
                // Actual Patient update will happen at this step
                if (session != null) {
                    session.close();
                }
            }
        }
        log.debug("PatientDAO.update() - End");
        return result;
    }

    /**
     * Delete a <code>Patient</code> record from the database
     * @param patientRecord
     */
    public void delete(Patient patientRecord) {
        log.debug("PatientDAO.delete() - Begin");

        Session session = null;
        try {
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            session = sessionFactory.openSession();
            log.info("Deleting Record...");

            // Delete the Patient record
            session.delete(patientRecord);
        } catch (Exception e) {
            log.error("Exception during delete occured due to :" + e.getMessage(), e);
        } finally {
            // Flush and close session
            if (session != null) {
                session.flush();
                session.close();
            }
        }
        log.debug("PatientDAO.delete() - End");
    }

    // ===============================
    // Patient Lookup / Search Methods
    // ===============================

    // ========================
    // Utility / Helper Methods
    // ========================

    /**
     * Return gateway property key perf.monitor.expected.errors value
     * @return String gateway property value
     */
    private static boolean getAllowSSNQuery() {
        boolean result = false;
        try {
            // Use CONNECT utility class to access gateway.properties
            String allowString = PropertyAccessor.getProperty(NhincConstants.GATEWAY_PROPERTY_FILE, ALLOW_SSN_QUERY);
            if (allowString != null && allowString.equalsIgnoreCase("true")) {
                result = true;
            }
        } catch (PropertyAccessException pae) {
            log.error("Error: Failed to retrieve " + ALLOW_SSN_QUERY + " from property file: " + NhincConstants.GATEWAY_PROPERTY_FILE);
            log.error(pae.getMessage());
        } catch (NumberFormatException nfe) {
            log.error("Error: Failed to convert " + ALLOW_SSN_QUERY + " from property file: " + NhincConstants.GATEWAY_PROPERTY_FILE);
            log.error(nfe.getMessage());
        }
        return result;
    }

}
