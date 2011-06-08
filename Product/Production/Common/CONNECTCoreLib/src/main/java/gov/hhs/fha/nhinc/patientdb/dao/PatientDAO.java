/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2011(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
package gov.hhs.fha.nhinc.patientdb.dao;

import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.patientdb.model.Address;
import gov.hhs.fha.nhinc.patientdb.model.Identifier;
import java.sql.Timestamp;

import gov.hhs.fha.nhinc.patientdb.persistence.HibernateUtil;
import gov.hhs.fha.nhinc.patientdb.model.Patient;
import gov.hhs.fha.nhinc.patientdb.model.Phonenumber;
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
import org.hibernate.criterion.Expression;
import org.hibernate.Query;

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

    /**
     * Fetch all the matching patients from all the community and all assigning authorities
     * on a known id.
     * @param Patient
     * @return Patient
     */
    public List<Patient> findPatients(Patient patient) {
        log.debug("PatientDAO.findAllPatients() - Begin");

        Session session = null;
        List<Patient> patientsList = new ArrayList<Patient>();

        try {
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            session = sessionFactory.openSession();

            log.info("Reading Records...");

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
            if (patient.getAddresses() != null && patient.getAddresses().size() > 0) {
                address = patient.getAddresses().get(0);
            }
            Phonenumber phonenumber = new Phonenumber();
            if (patient.getPhonenumbers() != null && patient.getPhonenumbers().size() > 0) {
                phonenumber = patient.getPhonenumbers().get(0);
            }

            // Build the select with query criteria
            StringBuffer sqlSelect = new StringBuffer("SELECT DISTINCT p.patientId, p.dateOfBirth, p.gender, p.ssn, i.id, i.organizationid");
            sqlSelect.append(" FROM patientdb.patient p");
            sqlSelect.append(" INNER JOIN patientdb.identifier i ON p.patientId = i.patientId");
            sqlSelect.append(" INNER JOIN patientdb.personname n ON p.patientId = n.patientId");
            if (address.getAddressId() != null) {
                sqlSelect.append(" INNER JOIN patientdb.address a ON p.patientId = a.patientId");
            }
            if (phonenumber.getPhonenumberId() != null) {
                sqlSelect.append(" INNER JOIN patientdb.phonenumber h ON p.patientId = h.patientId");
            }

            StringBuffer criteriaString = new StringBuffer("");
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

            Query sqlQuery = session.createSQLQuery(sqlSelect.toString())
                    .addScalar("patientId", Hibernate.LONG)
                    .addScalar("dateOfBirth", Hibernate.TIMESTAMP)
                    .addScalar("gender", Hibernate.STRING)
                    .addScalar("ssn", Hibernate.STRING)
                    .addScalar("id", Hibernate.STRING)
                    .addScalar("organizationid", Hibernate.STRING);

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

            log.debug("Final SQL Query is " + sqlQuery.getQueryString());

            List<Object[]> result = sqlQuery.list();

            if (result != null && result.size() > 0) {
                Long[] patientIdArray = new Long[result.size()];
                Timestamp[] dateOfBirthArray = new Timestamp[result.size()];
                String[] genderArray = new String[result.size()];
                String[] ssnArray = new String[result.size()];
                String[] idArray = new String[result.size()];
                String[] organizationIdArray = new String[result.size()];

                int counter = 0;
                for (Object[] row : result) {
                    patientIdArray[counter] = (Long)row[0];
                    dateOfBirthArray[counter] = (Timestamp)row[1];
                    genderArray[counter] = row[2].toString();
                    ssnArray[counter] = row[3].toString();
                    idArray[counter] = row[4].toString();
                    organizationIdArray[counter] = row[5].toString();
                    counter++;
                }

                for (int i=0; i<patientIdArray.length; i++) {
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
                    patientData.setAddresses(AddressDAO.getAddressDAOInstance().findPatientAddresses(patientIdArray[i]));
                    patientData.setPersonnames(PersonnameDAO.getPersonnameDAOInstance().findPatientPersonnames(patientIdArray[i]));
                    patientData.setPhonenumbers(PhonenumberDAO.getPhonenumberDAOInstance().findPatientPhonenumbers(patientIdArray[i]));

                    patientsList.add(patientData);
                }
            }

        } catch (Exception e) {
            log.error("Exception during read occured due to : " + e.getMessage(), e);
        } finally {
            // Flush and close session
            if (session != null) {
                session.flush();
                session.close();
            }
        }
        log.debug("PatientDAO.findPatients() - End");
        return patientsList;
    }

    // ========================
    // Utility / Helper Methods
    // ========================
    /**
     * Return gateway property key perf.monitor.expected.errors value
     * @return String gateway property value
     */
    private static boolean isAllowSSNQuery() {
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
