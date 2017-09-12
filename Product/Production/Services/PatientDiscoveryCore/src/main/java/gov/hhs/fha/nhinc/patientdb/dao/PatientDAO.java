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

import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.patientdb.model.Address;
import gov.hhs.fha.nhinc.patientdb.model.Identifier;
import gov.hhs.fha.nhinc.patientdb.model.Patient;
import gov.hhs.fha.nhinc.patientdb.model.Personname;
import gov.hhs.fha.nhinc.patientdb.model.Phonenumber;
import gov.hhs.fha.nhinc.patientdb.persistence.HibernateUtil;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Expression;
import org.hibernate.type.StandardBasicTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * PatientDAO Class provides methods to query and update Patient Data to/from MySQL Database using Hibernate
 *
 * @author richard.ettema
 */
public class PatientDAO {

    private static final Logger LOG = LoggerFactory.getLogger(PatientDAO.class);
    private static PatientDAO patientDAO = new PatientDAO();
    private HibernateUtil hibernateUtil = new HibernateUtil();

    /**
     * Constructor
     */
    private PatientDAO() {
        LOG.info("PatientDAO - Initialized");
    }

    /**
     * Singleton instance returned...
     *
     * @return PatientDAO
     */
    public static PatientDAO getPatientDAOInstance() {
        LOG.debug("getPatientDAOInstance()..");
        return patientDAO;
    }

    // =========================
    // Standard CRUD DML Methods
    // =========================
    /**
     * Create a single <code>Patient</code> record. The generated id will be available in the patientRecord.
     *
     * @param patientRecord
     * @return boolean
     */
    public boolean create(Patient patientRecord) {
        LOG.debug("PatientDAO.create() - Begin");
        Session session = null;
        Transaction tx = null;
        boolean result = true;

        if (patientRecord != null) {
            try {
                session = hibernateUtil.getSessionFactory().openSession();

                tx = session.beginTransaction();
                LOG.info("Inserting Record...");

                session.persist(patientRecord);

                LOG.info("Patient Inserted seccussfully...");
                tx.commit();
            } catch (HibernateException | NullPointerException e) {
                result = false;
                if (tx != null) {
                    tx.rollback();
                }
                LOG.error("Exception during insertion caused by : {}", e.getMessage(), e);
            } finally {
                // Actual Patient insertion will happen at this step
                if (session != null) {
                    try {
                        session.close();
                    } catch (HibernateException e) {
                        LOG.error("Exception while closing the session: {}", e.getMessage(), e);
                    }
                }
            }
        }
        LOG.debug("PatientDAO.create() - End");
        return result;
    }

    /**
     * Read (Query) the database to get a <code>Patient</code> record based on a known id.
     *
     * @param id
     * @return Patient
     */
    public Patient read(Long id) {
        LOG.debug("PatientDAO.read() - Begin");

        if (id == null) {
            LOG.info("-- id Parameter is required for Patient Query --");
            LOG.debug("PatientDAO.read() - End");
            return null;
        }

        Session session = null;
        List<Patient> queryList;
        Patient foundRecord = null;
        try {
            session = hibernateUtil.getSessionFactory().openSession();
            LOG.info("Reading Record...");

            // Build the criteria
            Criteria aCriteria = session.createCriteria(Patient.class);

            aCriteria.add(Expression.eq("id", id));

            queryList = aCriteria.list();

            if (queryList != null && !queryList.isEmpty()) {
                foundRecord = queryList.get(0);
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
                    LOG.error("Exception while closing the session after a read: {}", e.getMessage(), e);
                }
            }
        }
        LOG.debug("PatientDAO.read() - End");
        return foundRecord;
    }

    /**
     * Update a single <code>Patient</code> record.
     *
     * @param patientRecord
     * @return boolean
     */
    public boolean update(Patient patientRecord) {
        LOG.debug("PatientDAO.update() - Begin");
        Session session = null;
        Transaction tx = null;
        boolean result = true;

        if (patientRecord != null) {
            try {
                session = hibernateUtil.getSessionFactory().openSession();
                tx = session.beginTransaction();
                LOG.info("Updating Record...");

                session.saveOrUpdate(patientRecord);

                LOG.info("Patient Updated seccussfully...");
                tx.commit();
            } catch (HibernateException | NullPointerException e) {
                result = false;
                if (tx != null) {
                    tx.rollback();
                }
                LOG.error("Exception during update caused by : {}", e.getMessage(), e);
            } finally {
                // Actual Patient update will happen at this step
                if (session != null) {
                    try {
                        session.close();
                    } catch (HibernateException e) {
                        LOG.error("Exception while closing the session after an update: {}", e.getMessage(), e);
                    }
                }
            }
        }
        LOG.debug("PatientDAO.update() - End");
        return result;
    }

    /**
     * Delete a <code>Patient</code> record from the database
     *
     * @param patientRecord
     */
    public void delete(Patient patientRecord) {
        LOG.debug("PatientDAO.delete() - Begin");

        Session session = null;
        try {
            session = hibernateUtil.getSessionFactory().openSession();
            LOG.info("Deleting Record...");

            // Delete the Patient record
            session.delete(patientRecord);
        } catch (HibernateException | NullPointerException e) {
            LOG.error("Exception during delete occured due to : {}", e.getMessage(), e);
        } finally {
            // Flush and close session
            if (session != null) {
                try {
                    session.flush();
                    session.close();
                } catch (HibernateException e) {
                    LOG.error("Exception while closing the session after a delete: {}", e.getMessage(), e);
                }
            }
        }
        LOG.debug("PatientDAO.delete() - End");
    }

    // ===============================
    // Patient Lookup / Search Methods
    // ===============================
    /**
     * Fetch all the matching patients from all the community and all assigning authorities on a known id.
     *
     * @param Patient
     * @return Patient
     */
    public List<Patient> findPatients(Patient patient) {
        LOG.debug("PatientDAO.findAllPatients() - Begin");

        Session session = null;
        List<Patient> patientsList = new ArrayList<>();

        try {
            session = hibernateUtil.getSessionFactory().openSession();

            LOG.info("Reading Records...");

            // NHIN required query parameters
            String gender = patient.getGender();
            Timestamp dateOfBirth = patient.getDateOfBirth();
            String firstName = patient.getPersonnames().get(0).getFirstName();
            String lastName = patient.getPersonnames().get(0).getLastName();
            // NHIN optional query parameters
            String ssn = patient.getSsn();
            String prefix = patient.getPersonnames().get(0).getPrefix();
            String middleName = patient.getPersonnames().get(0).getMiddleName();
            String suffix = patient.getPersonnames().get(0).getSuffix();

            Address address = new Address();
            if (patient.getAddresses() != null && !patient.getAddresses().isEmpty()) {
                address = patient.getAddresses().get(0);
            }
            Phonenumber phonenumber = new Phonenumber();
            if (patient.getPhonenumbers() != null && !patient.getPhonenumbers().isEmpty()) {
                phonenumber = patient.getPhonenumbers().get(0);
            }

            // Build the select with query criteria
            StringBuilder sqlSelect = new StringBuilder(
                "SELECT DISTINCT p.patientId, p.dateOfBirth, p.gender, p.ssn, i.id, i.organizationid");
            sqlSelect.append(" FROM patientdb.patient p");
            sqlSelect.append(" INNER JOIN patientdb.identifier i ON p.patientId = i.patientId");
            sqlSelect.append(" INNER JOIN patientdb.personname n ON p.patientId = n.patientId");
            if (address.getAddressId() != null) {
                sqlSelect.append(" INNER JOIN patientdb.address a ON p.patientId = a.patientId");
            }
            if (phonenumber.getPhonenumberId() != null) {
                sqlSelect.append(" INNER JOIN patientdb.phonenumber h ON p.patientId = h.patientId");
            }

            StringBuilder criteriaString = new StringBuilder("");
            if (NullChecker.isNotNullish(gender)) {
                if (criteriaString.length() > 0) {
                    criteriaString.append(" AND");
                } else {
                    criteriaString.append(" WHERE");
                }
                criteriaString.append(" p.gender = ?");
            }
            if (dateOfBirth != null) {
                if (criteriaString.length() > 0) {
                    criteriaString.append(" AND");
                } else {
                    criteriaString.append(" WHERE");
                }
                criteriaString.append(" p.dateOfBirth = ?");
            }
            if (NullChecker.isNotNullish(firstName)) {
                if (criteriaString.length() > 0) {
                    criteriaString.append(" AND");
                } else {
                    criteriaString.append(" WHERE");
                }
                criteriaString.append(" n.firstname = ?");
            }
            if (NullChecker.isNotNullish(lastName)) {
                if (criteriaString.length() > 0) {
                    criteriaString.append(" AND");
                } else {
                    criteriaString.append(" WHERE");
                }
                criteriaString.append(" n.lastname = ?");
            }
            if (NullChecker.isNotNullish(ssn)) {
                if (criteriaString.length() > 0) {
                    criteriaString.append(" AND");
                } else {
                    criteriaString.append(" WHERE");
                }
                criteriaString.append(" p.ssn = ?");
            }
            if (NullChecker.isNotNullish(prefix)) {
                if (criteriaString.length() > 0) {
                    criteriaString.append(" AND");
                } else {
                    criteriaString.append(" WHERE");
                }
                criteriaString.append(" n.prefix = ?");
            }
            if (NullChecker.isNotNullish(middleName)) {
                if (criteriaString.length() > 0) {
                    criteriaString.append(" AND");
                } else {
                    criteriaString.append(" WHERE");
                }
                criteriaString.append(" n.middleName = ?");
            }
            if (NullChecker.isNotNullish(suffix)) {
                if (criteriaString.length() > 0) {
                    criteriaString.append(" AND");
                } else {
                    criteriaString.append(" WHERE");
                }
                criteriaString.append(" n.suffix = ?");
            }
            if (address.getAddressId() != null && NullChecker.isNotNullish(address.getStreet1())) {
                if (criteriaString.length() > 0) {
                    criteriaString.append(" AND");
                } else {
                    criteriaString.append(" WHERE");
                }
                criteriaString.append(" a.street1 = ?");
            }
            if (address.getAddressId() != null && NullChecker.isNotNullish(address.getStreet2())) {
                if (criteriaString.length() > 0) {
                    criteriaString.append(" AND");
                } else {
                    criteriaString.append(" WHERE");
                }
                criteriaString.append(" a.street2 = ?");
            }
            if (address.getAddressId() != null && NullChecker.isNotNullish(address.getCity())) {
                if (criteriaString.length() > 0) {
                    criteriaString.append(" AND");
                } else {
                    criteriaString.append(" WHERE");
                }
                criteriaString.append(" a.city = ?");
            }
            if (address.getAddressId() != null && NullChecker.isNotNullish(address.getState())) {
                if (criteriaString.length() > 0) {
                    criteriaString.append(" AND");
                } else {
                    criteriaString.append(" WHERE");
                }
                criteriaString.append(" a.state = ?");
            }
            if (address.getAddressId() != null && NullChecker.isNotNullish(address.getPostal())) {
                if (criteriaString.length() > 0) {
                    criteriaString.append(" AND");
                } else {
                    criteriaString.append(" WHERE");
                }
                criteriaString.append(" a.postal = ?");
            }
            if (phonenumber.getPhonenumberId() != null && NullChecker.isNotNullish(phonenumber.getValue())) {
                if (criteriaString.length() > 0) {
                    criteriaString.append(" AND");
                } else {
                    criteriaString.append(" WHERE");
                }
                criteriaString.append(" h.value = ?");
            }
            sqlSelect.append(criteriaString);

            sqlSelect.append(" ORDER BY i.id, i.organizationid");

            SQLQuery sqlQuery = session.createSQLQuery(sqlSelect.toString())
                .addScalar("patientId", StandardBasicTypes.LONG)
                .addScalar("dateOfBirth", StandardBasicTypes.TIMESTAMP)
                .addScalar("gender", StandardBasicTypes.STRING).addScalar("ssn", StandardBasicTypes.STRING)
                .addScalar("id", StandardBasicTypes.STRING).addScalar("organizationid", StandardBasicTypes.STRING);

            int iParam = 0;
            if (NullChecker.isNotNullish(gender)) {
                sqlQuery.setString(iParam, gender);
                iParam++;
            }
            if (dateOfBirth != null) {
                sqlQuery.setTimestamp(iParam, dateOfBirth);
                iParam++;
            }
            if (NullChecker.isNotNullish(firstName)) {
                sqlQuery.setString(iParam, firstName);
                iParam++;
            }
            if (NullChecker.isNotNullish(lastName)) {
                sqlQuery.setString(iParam, lastName);
                iParam++;
            }
            if (NullChecker.isNotNullish(ssn)) {
                sqlQuery.setString(iParam, ssn);
                iParam++;
            }
            if (NullChecker.isNotNullish(prefix)) {
                sqlQuery.setString(iParam, prefix);
                iParam++;
            }
            if (NullChecker.isNotNullish(middleName)) {
                sqlQuery.setString(iParam, middleName);
                iParam++;
            }
            if (NullChecker.isNotNullish(suffix)) {
                sqlQuery.setString(iParam, suffix);
                iParam++;
            }
            if (address.getAddressId() != null && NullChecker.isNotNullish(address.getStreet1())) {
                sqlQuery.setString(iParam, address.getStreet1());
                iParam++;
            }
            if (address.getAddressId() != null && NullChecker.isNotNullish(address.getStreet2())) {
                sqlQuery.setString(iParam, address.getStreet2());
                iParam++;
            }
            if (address.getAddressId() != null && NullChecker.isNotNullish(address.getCity())) {
                sqlQuery.setString(iParam, address.getCity());
                iParam++;
            }
            if (address.getAddressId() != null && NullChecker.isNotNullish(address.getState())) {
                sqlQuery.setString(iParam, address.getState());
                iParam++;
            }
            if (address.getAddressId() != null && NullChecker.isNotNullish(address.getPostal())) {
                sqlQuery.setString(iParam, address.getPostal());
                iParam++;
            }
            if (phonenumber.getPhonenumberId() != null && NullChecker.isNotNullish(phonenumber.getValue())) {
                sqlQuery.setString(iParam, phonenumber.getValue());
                iParam++;
            }

            LOG.debug("Final SQL Query is " + sqlQuery.getQueryString());

            List<Object[]> result = sqlQuery.list();

            if (result != null && !result.isEmpty()) {
                Long[] patientIdArray = new Long[result.size()];
                Timestamp[] dateOfBirthArray = new Timestamp[result.size()];
                String[] genderArray = new String[result.size()];
                String[] ssnArray = new String[result.size()];
                String[] idArray = new String[result.size()];
                String[] organizationIdArray = new String[result.size()];

                int counter = 0;
                for (Object[] row : result) {
                    patientIdArray[counter] = (Long) row[0];
                    dateOfBirthArray[counter] = (Timestamp) row[1];
                    genderArray[counter] = row[2].toString();
                    ssnArray[counter] = row[3].toString();
                    idArray[counter] = row[4].toString();
                    organizationIdArray[counter] = row[5].toString();
                    counter++;
                }

                for (int i = 0; i < patientIdArray.length; i++) {
                    Patient patientData = new Patient();
                    patientData.setPatientId(patientIdArray[i]);
                    patientData.setDateOfBirth(dateOfBirthArray[i]);
                    patientData.setGender(genderArray[i]);
                    patientData.setSsn(ssnArray[i]);

                    Identifier identifierData = new Identifier();
                    identifierData.getPatient().setPatientId(patientIdArray[i]);
                    identifierData.setId(idArray[i]);
                    identifierData.setOrganizationId(organizationIdArray[i]);

                    patientData.getIdentifiers().add(identifierData);

                    // Populate demographic data
                    patientData
                    .setAddresses(AddressDAO.getAddressDAOInstance().findPatientAddresses(patientIdArray[i]));
                    patientData.setPersonnames(
                        PersonnameDAO.getPersonnameDAOInstance().findPatientPersonnames(patientIdArray[i]));
                    patientData.setPhonenumbers(
                        PhonenumberDAO.getPhonenumberDAOInstance().findPatientPhonenumbers(patientIdArray[i]));

                    patientsList.add(patientData);
                }
            }

        } catch (Exception e) {
            LOG.error("Exception during read occured due to : {}", e.getMessage(), e);
        } finally {
            // Flush and close session
            if (session != null) {
                try {
                    session.flush();
                    session.close();
                } catch (HibernateException e) {
                    LOG.error("Exception while closing the session after looking for patients: {}", e.getMessage(), e);
                }
            }
        }
        LOG.debug("PatientDAO.findPatients() - End");
        return patientsList;
    }

    /**
     * Returns get all patients list
     *
     * @return
     */
    public List<Patient> getAll() {

        Session session = null;
        List<Patient> patients = new ArrayList<>();

        try {
            session = hibernateUtil.getSessionFactory().openSession();

            // BUILD-SELECT-PATIENT AND PERSONNAME
            StringBuilder sSQL = new StringBuilder("SELECT DISTINCT p.patientId, p.dateOfBirth, p.gender, p.ssn");
            sSQL.append(" , n.firstname, n.lastname, n.personnameid, n.prefix, n.middlename, n.suffix");
            sSQL.append(" FROM patientdb.patient p");
            sSQL.append(" INNER JOIN patientdb.personname n ON p.patientId = n.patientId");
            sSQL.append(" ORDER BY n.lastname, n.firstname");

            SQLQuery sqlQuery = session.createSQLQuery(sSQL.toString()).addScalar("patientId", StandardBasicTypes.LONG)
                .addScalar("dateOfBirth", StandardBasicTypes.TIMESTAMP).addScalar("gender", StandardBasicTypes.STRING)
                .addScalar("ssn", StandardBasicTypes.STRING).addScalar("firstname", StandardBasicTypes.STRING)
                .addScalar("lastname", StandardBasicTypes.STRING).addScalar("personnameid", StandardBasicTypes.LONG)
                .addScalar("prefix", StandardBasicTypes.STRING).addScalar("middlename", StandardBasicTypes.STRING)
                .addScalar("suffix", StandardBasicTypes.STRING);

            LOG.debug("Final SQL Query is " + sqlQuery.getQueryString());

            List<Object[]> result = sqlQuery.list();

            if (CollectionUtils.isNotEmpty(result)) {
                Long[] patientIdArray = new Long[result.size()];
                Timestamp[] dateOfBirthArray = new Timestamp[result.size()];
                String[] genderArray = new String[result.size()];
                String[] ssnArray = new String[result.size()];
                String[] firstNameArray = new String[result.size()];
                String[] lastNameArray = new String[result.size()];
                Long[] personnameidArray = new Long[result.size()];
                String[] prefixArray = new String[result.size()];
                String[] middlenameArray = new String[result.size()];
                String[] suffixArray = new String[result.size()];

                int counter = 0;
                for (Object[] row : result) {
                    // COMMON
                    patientIdArray[counter] = (Long) row[0];

                    // PATIENT
                    dateOfBirthArray[counter] = (Timestamp) row[1];
                    genderArray[counter] = (String) row[2];
                    ssnArray[counter] = (String) row[3];

                    // PERSONNAME
                    firstNameArray[counter] = (String) row[4];
                    lastNameArray[counter] = (String) row[5];
                    personnameidArray[counter] = (Long) row[6];
                    prefixArray[counter] = (String) row[7];
                    middlenameArray[counter] = (String) row[8];
                    suffixArray[counter] = (String) row[9];
                    counter++;
                }

                for (int i = 0; i < patientIdArray.length; i++) {
                    Patient patientData = new Patient();
                    patientData.setPatientId(patientIdArray[i]);
                    patientData.setDateOfBirth(dateOfBirthArray[i]);
                    patientData.setGender(genderArray[i]);
                    patientData.setSsn(ssnArray[i]);

                    Personname personnameData = new Personname();
                    personnameData.setFirstName(firstNameArray[i]);
                    personnameData.setLastName(lastNameArray[i]);
                    personnameData.setPersonnameId(personnameidArray[i]);
                    personnameData.setPrefix(prefixArray[i]);
                    personnameData.setMiddleName(middlenameArray[i]);
                    personnameData.setSuffix(suffixArray[i]);

                    patientData.getPersonnames().add(personnameData);

                    patients.add(patientData);
                }
            }

        } catch (HibernateException e) {
            LOG.error("Could not retrieve users: {}", e.getLocalizedMessage(), e);
        } finally {
            HibernateUtil.closeSession(session, false);
        }

        return patients;
    }

}
