/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services. 
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
package gov.hhs.fha.nhinc.patientdb.dao;

import gov.hhs.fha.nhinc.patientdb.model.Address;
import gov.hhs.fha.nhinc.patientdb.persistence.HibernateUtil;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Expression;

/**
 * 
 * AddressDAO Class provides methods to query and update Address Data to/from MySQL Database using Hibernate
 * 
 * @author richard.ettema
 */

public class AddressDAO {

    private static Log log = LogFactory.getLog(AddressDAO.class);

    private static AddressDAO addressDAO = new AddressDAO();

    /**
     * 
     * Constructor
     */

    private AddressDAO() {

        log.info("AddressDAO - Initialized");

    }

    /**
     * 
     * Singleton instance returned...
     * 
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
     * 
     * Create a single <code>Address</code> record. The generated id
     * 
     * will be available in the addressRecord.
     * 
     * @param addressRecord
     * 
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
     * 
     * Read (Query) the database to get a <code>Address</code> record based
     * 
     * on a known id.
     * 
     * @param id
     * 
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
     * 
     * Update a single <code>Address</code> record.
     * 
     * @param addressRecord
     * 
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
     * 
     * Delete a <code>Address</code> record from the database
     * 
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

    // Find DML Methods

    // =========================

    /**
     * 
     * Read (Query) the database to get all <code>Address</code> records based
     * 
     * on a known patientId.
     * 
     * @param patientId
     * 
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
