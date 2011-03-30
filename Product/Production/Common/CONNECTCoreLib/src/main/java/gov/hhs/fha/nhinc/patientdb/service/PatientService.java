/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2011(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
package gov.hhs.fha.nhinc.patientdb.service;

import gov.hhs.fha.nhinc.patientdb.dao.PatientDAO;
import gov.hhs.fha.nhinc.patientdb.model.Patient;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author richard.ettema
 */
public class PatientService {

    private static Log log = LogFactory.getLog(PatientService.class);
    private static PatientService patientService = new PatientService();

    /**
     * Constructor
     */
    private PatientService() {
        log.info("PatientService - Initialized");
    }

    /**
     * Singleton instance returned...
     * @return PatientService
     */
    public static PatientService getPatientService() {
        log.debug("getPatientService()..");
        return patientService;
    }

    /**
     * Return all matching patient records for the given search parameters
     * 
     * @param searchParams
     * @return
     */
    public List<Patient> findPatients(Patient searchParams) {

        log.debug("PatientService.findAllPatients() - Begin");

        List<Patient> results = new ArrayList<Patient>();

        PatientDAO dao = PatientDAO.getPatientDAOInstance();

        results = dao.findPatients(searchParams);

        log.debug("PatientService.findAllPatients() - End");

        return results;
    }

}
