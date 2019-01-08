/*
 * Copyright (c) 2009-2019, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.mpilib;

import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Singleton class.
 *
 */
public class MiniMpi implements IMPI {

    private static final Logger LOG = LoggerFactory.getLogger(MiniMpi.class);

    private static MiniMpi instance = null;

    private Patients patients = null;
    private String customFileName = "";

    MiniMpi() {
        loadData();
    }

    /**
     * @return an instance of this singleton class
     */
    public static MiniMpi getInstance() {
        if (instance == null) {
            instance = new MiniMpi();
        }
        instance.loadData();

        return instance;
    }

    /**
     * @param fileName the mpi file to laod data from
     * @return an instance of this singleton class
     */
    public static MiniMpi getInstance(String fileName) {
        if (instance == null) {
            instance = new MiniMpi();
        }
        instance.loadData(fileName);

        return instance;
    }

    /**
     *
     */
    public synchronized void reset() {
        patients = null;
        saveData();
    }

    private synchronized Patients searchByDemographics(Patient searchParams, boolean includeOptOutPatient) {
        Patients results = new Patients();
        PatientMatcher matcher = PatientMatcher.getInstance();

        LOG.info("performing a demograpics search");
        for (Patient patient : this.getPatients()) {
            if ((matcher.isPatientOptedInCriteriaMet(patient) || includeOptOutPatient)
                    && matcher.hasMatchByDemographics(patient, searchParams)) {
                results.add(patient);
            }
        }

        return results;
    }

    private synchronized Patients searchById(Patient searchParams, boolean includeOptOutPatient) {
        Patients results = new Patients();
        PatientMatcher matcher = PatientMatcher.getInstance();

        LOG.info("performing an id search");
        for (Patient patient : this.getPatients()) {
            if ((matcher.isPatientOptedInCriteriaMet(patient) || includeOptOutPatient)
                    && matcher.hasMatchByIds(patient, searchParams)) {
                results.add(patient);
            }
        }

        return results;
    }

    private void validateNewPatient(Patient patient) {
        if ((patient.getNames().isEmpty()) || !(patient.getNames().get(0).isValid())) {
            throw new MpiException("New patient must hava a name");
        }

    }

    @Override
    public synchronized Patient addUpdate(Patient newPatient) {
        Patient resultPatient;
        validateNewPatient(newPatient);

        Patients existingPatients = search(newPatient, true, true);

        if (existingPatients.isEmpty()) {
            getPatients().add(newPatient);
            resultPatient = newPatient;
        } else if (existingPatients.size() == 1) {
            resultPatient = existingPatients.get(0);
            resultPatient.getIdentifiers().add(newPatient.getIdentifiers());
            resultPatient.setNames(newPatient.getNames());
            resultPatient.setGender(newPatient.getGender());
            resultPatient.setOptedIn(newPatient.isOptedIn());
        } else {
            throw new MpiException("Failed to update patient as there are more than one matching patients found.");
        }

        saveData();

        return resultPatient;
    }

    /**
     * Method will delete a patient from the MPI.
     * @param patient the Patient to delete
     * @param homeCommunityId the HCID
     */
    public synchronized void delete(Patient patient, String homeCommunityId) {
        Patients existingPatients = search(patient, true, true);

        Identifier id;
        if (existingPatients.isEmpty()) {
            LOG.error("Delete failed.  Patient not found in MPI.");
        } else if (existingPatients.size() == 1) {
            LOG.info("Found 1 entry in MPI for the patient");

            for (int idIdx = 0; idIdx < existingPatients.get(0).getIdentifiers().size(); idIdx++) {
                id = existingPatients.get(0).getIdentifiers().get(idIdx);
                if (homeCommunityId.contentEquals(id.getOrganizationId())) {
                    existingPatients.get(0).getIdentifiers().remove(idIdx);
                    break;
                }
            }

            saveData();

        } else {
            LOG.error("Delete failed.  Multiple instances of the patient were found.");
        }
    }

    @Override
    public synchronized Patients search(Patient searchParams) {
        return search(searchParams, true, false);
    }

    @Override
    public synchronized Patients search(Patient searchParams, boolean searchByDemographics) {
        return search(searchParams, searchByDemographics, false);
    }

    /**
     * Method will search the MPI to see if the patient exists.
     * @param patient the Patient to search for
     * @param searchByDemographics boolean value. True if an attempt to make a demographic search should be made.
     * @param includeOptOutPatient boolean value. True if OptOut patients should be included. False otherwise.
     * @return a Patients object with all the patients found to have matched.
     */
    public synchronized Patients
            search(Patient patient, boolean searchByDemographics, boolean includeOptOutPatient) {
        Patients results = new Patients();

        if (searchByDemographics) {
            LOG.info("searching by demographic");
            results = searchByDemographics(patient, includeOptOutPatient);
        } else {
            LOG.info("no attempt on demographic search");
        }

        if (results.isEmpty()) {
            LOG.info("searching by id");
            results = searchById(patient, includeOptOutPatient);
        } else {
            LOG.info("no attempt on id search");
        }

        if (results != null) {
            LOG.info("result size=" + results.size());
        }
        return results;
    }

    /**
     * @return Patients object for MiniMpi
     */
    public synchronized Patients getPatients() {
        if (patients == null) {
            patients = new Patients();
        }
        return patients;
    }

    private synchronized void setPatients(Patients value) {
        patients = value;
    }

    private synchronized void loadData() {
        MpiDataSaver mpiDataSaver = new MpiDataSaver();
        this.setPatients(mpiDataSaver.loadMpi());
    }

    private synchronized void loadData(String fileName) {
        customFileName = fileName;
        MpiDataSaver mpiDataSaver = new MpiDataSaver();
        this.setPatients(mpiDataSaver.loadMpi(fileName));
    }

    private synchronized void saveData() {
        MpiDataSaver mpiDataSaver = new MpiDataSaver();
        if (NullChecker.isNullish(customFileName)) {
            mpiDataSaver.saveMpi(this.getPatients());
        } else {
            mpiDataSaver.saveMpi(this.getPatients(), customFileName);
        }
    }
}
