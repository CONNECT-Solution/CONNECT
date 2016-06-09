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

import gov.hhs.fha.nhinc.patientdb.model.Phonenumber;
import gov.hhs.fha.nhinc.patientdb.persistence.HibernateUtil;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Expression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * PhonenumberDAO Class provides methods to query and update Phonenumber Data to/from MySQL Database using Hibernate
 *
 * @author richard.ettema
 */
public class PhonenumberDAO {

    private static final Logger LOG = LoggerFactory.getLogger(PhonenumberDAO.class);

    private static PhonenumberDAO phonenumberDAO = new PhonenumberDAO();

    private HibernateUtil hibernateUtil;

    /**
     *
     * Constructor
     */
    private PhonenumberDAO() {

        LOG.info("PhonenumberDAO - Initialized");

    }

    /**
     *
     * Singleton instance returned...
     *
     * @return PhonenumberDAO
     */
    public static PhonenumberDAO getPhonenumberDAOInstance() {

        LOG.debug("getPhonenumberDAOInstance()..");

        return phonenumberDAO;

    }

    /**
     * Load HibernateUtil bean.
     *
     * @return hibernateUtil
     */
    protected HibernateUtil getHibernateUtil() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
                new String[] { "classpath:spring-beans.xml" });
        hibernateUtil = context.getBean("patientDbHibernateUtil", HibernateUtil.class);
        return hibernateUtil;
    }

    // =========================
    // Standard CRUD DML Methods
    // =========================
    /**
     *
     * Create a single <code>Phonenumber</code> record. The generated id
     *
     * will be available in the phonenumberRecord.
     *
     * @param phonenumberRecord
     *
     * @return boolean
     */
    public boolean create(Phonenumber phonenumberRecord) {

        LOG.debug("PhonenumberDAO.create() - Begin");

        Session session = null;

        Transaction tx = null;

        boolean result = true;

        if (phonenumberRecord != null) {

            try {

                SessionFactory sessionFactory = getSessionFactory();

                session = sessionFactory.openSession();

                tx = session.beginTransaction();

                LOG.info("Inserting Record...");

                session.persist(phonenumberRecord);

                LOG.info("Phonenumber Inserted seccussfully...");

                tx.commit();

            } catch (Exception e) {

                result = false;

                if (tx != null) {

                    tx.rollback();

                }

                LOG.error("Exception during insertion caused by : {}", e.getMessage(), e);

            } finally {

                // Actual Phonenumber insertion will happen at this step
                if (session != null) {

                    session.close();

                }
            }

        }

        LOG.debug("PhonenumberDAO.create() - End");

        return result;

    }

    /**
     *
     * Read (Query) the database to get a <code>Phonenumber</code> record based
     *
     * on a known id.
     *
     * @param id
     *
     * @return Phonenumber
     */
    public Phonenumber read(Long id) {

        LOG.debug("PhonenumberDAO.read() - Begin");

        if (id == null) {

            LOG.info("-- id Parameter is required for Phonenumber Query --");

            LOG.debug("PhonenumberDAO.read() - End");

            return null;

        }

        Session session = null;

        List<Phonenumber> queryList;

        Phonenumber foundRecord = null;

        try {

            SessionFactory sessionFactory = getSessionFactory();

            session = sessionFactory.openSession();

            LOG.info("Reading Record...");

            // Build the criteria
            Criteria aCriteria = session.createCriteria(Phonenumber.class);

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

        LOG.debug("PhonenumberDAO.read() - End");

        return foundRecord;

    }

    /**
     *
     * Update a single <code>Phonenumber</code> record.
     *
     * @param phonenumberRecord
     *
     * @return boolean
     */
    public boolean update(Phonenumber phonenumberRecord) {

        LOG.debug("PhonenumberDAO.update() - Begin");

        Session session = null;

        Transaction tx = null;

        boolean result = true;

        if (phonenumberRecord != null) {

            try {

                SessionFactory sessionFactory = getSessionFactory();

                session = sessionFactory.openSession();

                tx = session.beginTransaction();

                LOG.info("Updating Record...");

                session.saveOrUpdate(phonenumberRecord);

                LOG.info("Phonenumber Updated seccussfully...");

                tx.commit();

            } catch (Exception e) {

                result = false;

                if (tx != null) {

                    tx.rollback();

                }

                LOG.error("Exception during update caused by : {}", e.getMessage(), e);

            } finally {

                // Actual Phonenumber update will happen at this step
                if (session != null) {

                    session.close();

                }
            }

        }

        LOG.debug("PhonenumberDAO.update() - End");

        return result;

    }

    /**
     *
     * Delete a <code>Phonenumber</code> record from the database
     *
     * @param phonenumberRecord
     */
    public void delete(Phonenumber phonenumberRecord) {

        LOG.debug("PhonenumberDAO.delete() - Begin");

        Session session = null;

        try {

            SessionFactory sessionFactory = getSessionFactory();

            session = sessionFactory.openSession();

            LOG.info("Deleting Record...");

            // Delete the Phonenumber record
            session.delete(phonenumberRecord);

        } catch (Exception e) {

            LOG.error("Exception during delete occured due to : {}", e.getMessage(), e);

        } finally {

            // Flush and close session
            if (session != null) {

                session.flush();

                session.close();

            }
        }

        LOG.debug("PhonenumberDAO.delete() - End");

    }

    // =========================
    // Find DML Methods
    // =========================
    /**
     *
     * Read (Query) the database to get a <code>Phonenumber</code> record based
     *
     * on a known patientId.
     *
     * @param patientId
     *
     * @return List<Phonenumber>
     */
    public List<Phonenumber> findPatientPhonenumbers(Long patientId) {

        LOG.debug("PhonenumberDAO.findPatientPhonenumbers() - Begin");

        if (patientId == null) {

            LOG.info("-- patientId Parameter is required for Phonenumber Query --");

            LOG.debug("PhonenumberDAO.findPatientPhonenumbers() - End");

            return null;

        }

        Session session = null;

        List<Phonenumber> queryList = null;

        try {

            SessionFactory sessionFactory = getSessionFactory();

            session = sessionFactory.openSession();

            LOG.info("Reading Record...");

            // Build the criteria
            Criteria aCriteria = session.createCriteria(Phonenumber.class);

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

        LOG.debug("PhonenumberDAO.findPatientPhonenumbers() - End");

        return queryList;

    }

    protected SessionFactory getSessionFactory() {
        return getHibernateUtil().getSessionFactory();
    }

}
