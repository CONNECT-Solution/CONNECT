/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2011(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
package gov.hhs.fha.nhinc.patientdb.dao;

import gov.hhs.fha.nhinc.patientdb.persistence.HibernateUtil;
import gov.hhs.fha.nhinc.patientdb.model.Address;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.Criteria;
import org.hibernate.criterion.Expression;

/**
 * AddressDAO Class provides methods to query and update Address Data to/from MySQL Database using Hibernate
 * @author richard.ettema
 */
public class AddressDAO {

    private static Log log = LogFactory.getLog(AddressDAO.class);

    private static AddressDAO addressDAO = new AddressDAO();

    /**
     * Constructor
     */
    private AddressDAO() {
        log.info("AddressDAO - Initialized");
    }

    /**
     * Singleton instance returned...
     * @return AddressDAO
     */
    public static AddressDAO getAddressDAOInstance() {
        log.debug("getAddressDAOInstance()..");
        return addressDAO;
    }

    // =========================
    // Standard CRUD DML Methods
    // =========================

    /**
     * Create a single <code>Address</code> record.  The generated id
     * will be available in the addressRecord.
     * @param addressRecord
     * @return boolean
     */
    public boolean create(Address addressRecord) {
        log.debug("AddressDAO.create() - Begin");
        Session session = null;
        Transaction tx = null;
        boolean result = true;

        if (addressRecord != null) {
            try {
                SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
                session = sessionFactory.openSession();
                tx = session.beginTransaction();
                log.info("Inserting Record...");

                session.persist(addressRecord);

                log.info("Address Inserted seccussfully...");
                tx.commit();
            } catch (Exception e) {
                result = false;
                if (tx != null) {
                    tx.rollback();
                }
                log.error("Exception during insertion caused by :" + e.getMessage(), e);
            } finally {
                // Actual Address insertion will happen at this step
                if (session != null) {
                    session.close();
                }
            }
        }
        log.debug("AddressDAO.create() - End");
        return result;
    }

    /**
     * Read (Query) the database to get a <code>Address</code> record based
     * on a known id.
     * @param id
     * @return Address
     */
    public Address read(Long id) {
        log.debug("AddressDAO.read() - Begin");

        if (id == null) {
            log.info("-- id Parameter is required for Address Query --");
            log.debug("AddressDAO.read() - End");
            return null;
        }

        Session session = null;
        List<Address> queryList = null;
        Address foundRecord = null;
        try {
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            session = sessionFactory.openSession();
            log.info("Reading Record...");

            // Build the criteria
            Criteria aCriteria = session.createCriteria(Address.class);

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
        log.debug("AddressDAO.read() - End");
        return foundRecord;
    }

    /**
     * Update a single <code>Address</code> record.
     * @param addressRecord
     * @return boolean
     */
    public boolean update(Address addressRecord) {
        log.debug("AddressDAO.update() - Begin");
        Session session = null;
        Transaction tx = null;
        boolean result = true;

        if (addressRecord != null) {
            try {
                SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
                session = sessionFactory.openSession();
                tx = session.beginTransaction();
                log.info("Updating Record...");

                session.saveOrUpdate(addressRecord);

                log.info("Address Updated seccussfully...");
                tx.commit();
            } catch (Exception e) {
                result = false;
                if (tx != null) {
                    tx.rollback();
                }
                log.error("Exception during update caused by :" + e.getMessage(), e);
            } finally {
                // Actual Address update will happen at this step
                if (session != null) {
                    session.close();
                }
            }
        }
        log.debug("AddressDAO.update() - End");
        return result;
    }

    /**
     * Delete a <code>Address</code> record from the database
     * @param addressRecord
     */
    public void delete(Address addressRecord) {
        log.debug("AddressDAO.delete() - Begin");

        Session session = null;
        try {
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            session = sessionFactory.openSession();
            log.info("Deleting Record...");

            // Delete the Address record
            session.delete(addressRecord);
        } catch (Exception e) {
            log.error("Exception during delete occured due to :" + e.getMessage(), e);
        } finally {
            // Flush and close session
            if (session != null) {
                session.flush();
                session.close();
            }
        }
        log.debug("AddressDAO.delete() - End");
    }

    // =========================
    //     Find DML Methods
    // =========================

    /**
     * Read (Query) the database to get all <code>Address</code> records based
     * on a known patientId.
     * @param patientId
     * @return List<Address>
     */
    public List<Address> findPatientAddresses(Long patientId) {
        log.debug("AddressDAO.readPatientAddresses() - Begin");

        if (patientId == null) {
            log.info("-- patientId Parameter is required for Address Query --");
            log.debug("AddressDAO.readPatientAddresses() - End");
            return null;
        }

        Session session = null;
        List<Address> queryList = null;
        try {
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            session = sessionFactory.openSession();
            log.info("Reading Record...");

            // Build the criteria
            Criteria aCriteria = session.createCriteria(Address.class);

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
        log.debug("readPatientAddresses.read() - End");
        return queryList;
    }

}
