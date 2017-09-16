/*
 * Copyright (c) 2009-2017, United States Government, as represented by the Secretary of Health and Human Services.
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
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Expression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * PhonenumberDAO Class provides methods to query and update Phonenumber Data to/from MySQL Database using Hibernate
 *
 * @author richard.ettema
 */
public class PhonenumberDAO extends GenericDaoJpaImpl<Phonenumber, Long> {

    private static final Logger LOG = LoggerFactory.getLogger(PhonenumberDAO.class);
    private static PhonenumberDAO phonenumberDAO = new PhonenumberDAO();

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
    @Override
    public boolean create(Phonenumber phonenumberRecord) {
        LOG.debug("PhonenumberDAO.create() - Begin");
        boolean result = true;
        if (phonenumberRecord != null) {
            result = super.create(phonenumberRecord);
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
        Phonenumber foundRecord = super.read(id, Phonenumber.class);
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
    @Override
    public boolean update(Phonenumber phonenumberRecord) {
        LOG.debug("PhonenumberDAO.update() - Begin");
        boolean result = true;
        if (phonenumberRecord != null) {
            result = super.update(phonenumberRecord);
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
    @Override
    public void delete(Phonenumber phonenumberRecord) {
        LOG.debug("PhonenumberDAO.delete() - Begin");
        if (phonenumberRecord != null) {
            super.delete(phonenumberRecord);
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
            if (sessionFactory != null) {
                session = sessionFactory.openSession();
                LOG.info("Reading Record...");
                // Build the criteria
                Criteria aCriteria = session.createCriteria(Phonenumber.class);
                aCriteria.add(Expression.eq("patient.patientId", patientId));
                queryList = aCriteria.list();
            }
        } catch (HibernateException | NullPointerException e) {
            LOG.error("Exception during read occured due to : {}", e.getMessage(), e);
        } finally {
            // Flush and close session
            if (session != null) {
                try {
                    session.flush();
                    session.close();
                } catch (HibernateException e) {
                    LOG.error("Exception while closing the session after looking for patient's phone numbers: {}",
                        e.getMessage(), e);
                }
            }
        }
        LOG.debug("PhonenumberDAO.findPatientPhonenumbers() - End");
        return queryList;
    }


}
