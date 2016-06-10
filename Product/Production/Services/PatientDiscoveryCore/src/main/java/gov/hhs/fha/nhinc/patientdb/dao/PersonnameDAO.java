/*
 * Copyright (c) 2009-2016, United States Government, as represented by the Secretary of Health and Human Services.
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

import gov.hhs.fha.nhinc.patientdb.model.Personname;
import gov.hhs.fha.nhinc.patientdb.persistence.HibernateUtil;
import gov.hhs.fha.nhinc.patientdb.persistence.HibernateUtilFactory;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Expression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * PersonnameDAO Class provides methods to query and update Personname Data to/from MySQL Database using Hibernate
 *
 * @author richard.ettema
 */
public class PersonnameDAO {

    private static final Logger LOG = LoggerFactory.getLogger(PersonnameDAO.class);

    private static PersonnameDAO personnameDAO = new PersonnameDAO();

    /**
     *
     * Constructor
     */
    private PersonnameDAO() {

        LOG.info("PersonnameDAO - Initialized");

    }

    /**
     *
     * Singleton instance returned...
     *
     * @return PersonnameDAO
     */
    public static PersonnameDAO getPersonnameDAOInstance() {

        LOG.debug("getPersonnameDAOInstance()..");

        return personnameDAO;

    }

    // =========================
    // Standard CRUD DML Methods
    // =========================
    /**
     *
     * Create a single <code>Personname</code> record. The generated id
     *
     * will be available in the personnameRecord.
     *
     * @param personnameRecord
     *
     * @return boolean
     */
    public boolean create(Personname personnameRecord) {

        LOG.debug("PersonnameDAO.create() - Begin");

        Session session = null;

        Transaction tx = null;

        boolean result = true;

        if (personnameRecord != null) {

            try {

                SessionFactory sessionFactory = getSessionFactory();

                session = sessionFactory.openSession();

                tx = session.beginTransaction();

                LOG.info("Inserting Record...");

                session.persist(personnameRecord);

                LOG.info("Personname Inserted seccussfully...");

                tx.commit();

            } catch (Exception e) {

                result = false;

                if (tx != null) {

                    tx.rollback();

                }

                LOG.error("Exception during insertion caused by : {}", e.getMessage(), e);

            } finally {

                // Actual Personname insertion will happen at this step
                if (session != null) {

                    session.close();

                }
            }

        }

        LOG.debug("PersonnameDAO.create() - End");

        return result;

    }

    /**
     *
     * Read (Query) the database to get a <code>Personname</code> record based
     *
     * on a known id.
     *
     * @param id
     *
     * @return Personname
     */
    public Personname read(Long id) {

        LOG.debug("PersonnameDAO.read() - Begin");

        if (id == null) {

            LOG.info("-- id Parameter is required for Personname Query --");

            LOG.debug("PersonnameDAO.read() - End");

            return null;

        }

        Session session = null;

        List<Personname> queryList;

        Personname foundRecord = null;

        try {

            SessionFactory sessionFactory = getSessionFactory();

            session = sessionFactory.openSession();

            LOG.info("Reading Record...");

            // Build the criteria
            Criteria aCriteria = session.createCriteria(Personname.class);

            aCriteria.add(Expression.eq("id", id));

            queryList = aCriteria.list();

            if (queryList != null && queryList.size() > 0) {

                foundRecord = queryList.get(0);

            }

        } catch (Exception e) {

            LOG.error("Exception during read occured due to : {}", e.getMessage(), e);

        } finally {

            // Flush and close session
            if (session != null) {

                session.flush();

                session.close();

            }
        }

        LOG.debug("PersonnameDAO.read() - End");

        return foundRecord;

    }

    /**
     *
     * Update a single <code>Personname</code> record.
     *
     * @param personnameRecord
     *
     * @return boolean
     */
    public boolean update(Personname personnameRecord) {

        LOG.debug("PersonnameDAO.update() - Begin");

        Session session = null;

        Transaction tx = null;

        boolean result = true;

        if (personnameRecord != null) {

            try {

                SessionFactory sessionFactory = getSessionFactory();

                session = sessionFactory.openSession();

                tx = session.beginTransaction();

                LOG.info("Updating Record...");

                session.saveOrUpdate(personnameRecord);

                LOG.info("Personname Updated seccussfully...");

                tx.commit();

            } catch (Exception e) {

                result = false;

                if (tx != null) {

                    tx.rollback();

                }

                LOG.error("Exception during update caused by : {}", e.getMessage(), e);

            } finally {

                // Actual Personname update will happen at this step
                if (session != null) {

                    session.close();

                }
            }

        }

        LOG.debug("PersonnameDAO.update() - End");

        return result;

    }

    /**
     *
     * Delete a <code>Personname</code> record from the database
     *
     * @param personnameRecord
     */
    public void delete(Personname personnameRecord) {

        LOG.debug("PersonnameDAO.delete() - Begin");

        Session session = null;

        try {

            SessionFactory sessionFactory = getSessionFactory();

            session = sessionFactory.openSession();

            LOG.info("Deleting Record...");

            // Delete the Personname record
            session.delete(personnameRecord);

        } catch (Exception e) {

            LOG.error("Exception during delete occured due to : {}", e.getMessage(), e);

        } finally {

            // Flush and close session
            if (session != null) {

                session.flush();

                session.close();

            }
        }

        LOG.debug("PersonnameDAO.delete() - End");

    }

    // =========================
    // Find DML Methods
    // =========================
    /**
     *
     * Read (Query) the database to get all <code>Personname</code> records based
     *
     * on a known patientId.
     *
     * @param patientId
     *
     * @return List<Personname>
     */
    public List<Personname> findPatientPersonnames(Long patientId) {

        LOG.debug("PersonnameDAO.findPatientPersonnames() - Begin");

        if (patientId == null) {

            LOG.info("-- patientId Parameter is required for Personname Query --");

            LOG.debug("PersonnameDAO.findPatientPersonnames() - End");

            return null;

        }

        Session session = null;

        List<Personname> queryList = null;

        try {

            SessionFactory sessionFactory = getSessionFactory();

            session = sessionFactory.openSession();

            LOG.info("Reading Record...");

            // Build the criteria
            Criteria aCriteria = session.createCriteria(Personname.class);

            aCriteria.add(Expression.eq("patient.patientId", patientId));

            queryList = aCriteria.list();

        } catch (Exception e) {

            LOG.error("Exception during read occured due to : {}", e.getMessage(), e);

        } finally {

            // Flush and close session
            if (session != null) {

                session.flush();

                session.close();

            }
        }

        LOG.debug("PersonnameDAO.findPatientPersonnames() - End");

        return queryList;

    }

    /**
     * Returns the sessionFactory belonging to PatientDiscovery HibernateUtil
     *
     * @return
     */
    protected SessionFactory getSessionFactory() {
        HibernateUtil hibernateUtil = HibernateUtilFactory.getPatientDiscHibernateUtil();
        SessionFactory fact = null;
        if (hibernateUtil != null) {
            fact = hibernateUtil.getSessionFactory();
        }
        return fact;
    }

}
