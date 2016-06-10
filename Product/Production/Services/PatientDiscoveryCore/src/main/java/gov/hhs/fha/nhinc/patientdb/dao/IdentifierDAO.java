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

import gov.hhs.fha.nhinc.patientdb.model.Identifier;
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
 * IdentifierDAO Class provides methods to query and update Identifier Data to/from MySQL Database using Hibernate
 *
 * @author richard.ettema
 */
public class IdentifierDAO {

    private static final Logger LOG = LoggerFactory.getLogger(IdentifierDAO.class);

    private static IdentifierDAO identifierDAO = new IdentifierDAO();

    /**
     *
     * Constructor
     */
    private IdentifierDAO() {

        LOG.info("IdentifierDAO - Initialized");

    }

    /**
     *
     * Singleton instance returned...
     *
     * @return IdentifierDAO
     */
    public static IdentifierDAO getIdentifierDAOInstance() {

        LOG.debug("getIdentifierDAOInstance()..");

        return identifierDAO;

    }

    // =========================
    // Standard CRUD DML Methods
    // =========================
    /**
     *
     * Create a single <code>Identifier</code> record. The generated id
     *
     * will be available in the identifierRecord.
     *
     * @param identifierRecord
     *
     * @return boolean
     */
    public boolean create(Identifier identifierRecord) {

        LOG.debug("IdentifierDAO.create() - Begin");

        Session session = null;

        Transaction tx = null;

        boolean result = true;

        if (identifierRecord != null) {

            try {

                SessionFactory sessionFactory = getSessionFactory();

                session = sessionFactory.openSession();

                tx = session.beginTransaction();

                LOG.info("Inserting Record...");

                session.persist(identifierRecord);

                LOG.info("Identifier Inserted seccussfully...");

                tx.commit();

            } catch (Exception e) {

                result = false;

                if (tx != null) {

                    tx.rollback();

                }

                LOG.error("Exception during insertion caused by : {}", e.getMessage(), e);

            } finally {

                // Actual Identifier insertion will happen at this step
                if (session != null) {

                    session.close();

                }

            }

        }

        LOG.debug("IdentifierDAO.create() - End");

        return result;

    }

    /**
     *
     * Read (Query) the database to get a <code>Identifier</code> record based
     *
     * on a known id.
     *
     * @param id
     *
     * @return Identifier
     */
    public Identifier read(Long id) {

        LOG.debug("IdentifierDAO.read() - Begin");

        if (id == null) {

            LOG.info("-- id Parameter is required for Identifier Query --");

            LOG.debug("IdentifierDAO.read() - End");

            return null;

        }

        Session session = null;

        List<Identifier> queryList;

        Identifier foundRecord = null;

        try {

            SessionFactory sessionFactory = getSessionFactory();

            session = sessionFactory.openSession();

            LOG.info("Reading Record...");

            // Build the criteria
            Criteria aCriteria = session.createCriteria(Identifier.class);

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

        LOG.debug("IdentifierDAO.read() - End");

        return foundRecord;

    }

    /**
     *
     * Update a single <code>Identifier</code> record.
     *
     * @param identifierRecord
     *
     * @return boolean
     */
    public boolean update(Identifier identifierRecord) {

        LOG.debug("IdentifierDAO.update() - Begin");

        Session session = null;

        Transaction tx = null;

        boolean result = true;

        if (identifierRecord != null) {

            try {

                SessionFactory sessionFactory = getSessionFactory();

                session = sessionFactory.openSession();

                tx = session.beginTransaction();

                LOG.info("Updating Record...");

                session.saveOrUpdate(identifierRecord);

                LOG.info("Identifier Updated seccussfully...");

                tx.commit();

            } catch (Exception e) {

                result = false;

                if (tx != null) {

                    tx.rollback();

                }

                LOG.error("Exception during update caused by : {}", e.getMessage(), e);

            } finally {

                // Actual Identifier update will happen at this step
                if (session != null) {

                    session.close();

                }

            }

        }

        LOG.debug("IdentifierDAO.update() - End");

        return result;

    }

    /**
     *
     * Delete a <code>Identifier</code> record from the database
     *
     * @param identifierRecord
     */
    public void delete(Identifier identifierRecord) {

        LOG.debug("IdentifierDAO.delete() - Begin");

        Session session = null;

        try {

            SessionFactory sessionFactory = getSessionFactory();

            session = sessionFactory.openSession();

            LOG.info("Deleting Record...");

            // Delete the Identifier record
            session.delete(identifierRecord);

        } catch (Exception e) {

            LOG.error("Exception during delete occured due to : {}", e.getMessage(), e);

        } finally {

            // Flush and close session
            if (session != null) {

                session.flush();

                session.close();

            }

        }

        LOG.debug("IdentifierDAO.delete() - End");

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
