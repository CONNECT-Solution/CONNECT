/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.adapter.cppgui.servicefacade;

import gov.hhs.fha.nhinc.adapter.cppgui.AssertionCreator;
import gov.hhs.fha.nhinc.adapter.cppgui.PatientSearchCriteria;
import gov.hhs.fha.nhinc.adapter.cppgui.valueobject.PatientVO;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.PersonNameType;
import gov.hhs.fha.nhinc.mpi.adapter.component.proxy.AdapterComponentMpiProxy;
import gov.hhs.fha.nhinc.mpi.adapter.component.proxy.AdapterComponentMpiProxyObjectFactory;
import gov.hhs.fha.nhinc.mpilib.Identifier;
import gov.hhs.fha.nhinc.mpilib.Patient;
import gov.hhs.fha.nhinc.mpilib.Patients;
import gov.hhs.fha.nhinc.mpilib.PersonName;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.transform.subdisc.HL7Extractors;
import gov.hhs.fha.nhinc.transform.subdisc.HL7PRPA201305Transforms;
import gov.hhs.fha.nhinc.transform.subdisc.HL7PatientTransforms;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.II;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.PRPAIN201306UV02MFMIMT700711UV01Subject1;
import org.hl7.v3.PRPAMT201301UV02Patient;
import org.hl7.v3.PRPAMT201310UV02Patient;

/**
 *
 * @author patlollav
 */
public class PatientSearchFacade {

    private static final Log log = LogFactory.getLog(PatientSearchFacade.class);

    /**
     * Search MPI with the given search criteria
     *
     * @param patientSearchCriteria
     * @return patients
     */
    public List<PatientVO> searchPatient(PatientSearchCriteria patientSearchCriteria) throws Exception {

        List<PatientVO> patientVOs = null;

        PRPAIN201305UV02 searchRequest = createPRPAMT201301UVPatient(patientSearchCriteria);

        AdapterComponentMpiProxy mpiProxy = getAdapterComponentMpiProxy();

        AssertionCreator oAssertCreator = new AssertionCreator();
        AssertionType oAssertion = oAssertCreator.createAssertion();
        PRPAIN201306UV02 patients = mpiProxy.findCandidates(searchRequest, oAssertion);

        Patients mpiPatients = convertPRPAIN201306UVToPatients(patients);

        patientVOs = convertMPIPatientToPatientVO(mpiPatients);

        return patientVOs;
    }

    /**
     * 
     * @param patientSearchCriteria
     * @return
     */
    private PRPAIN201305UV02 createPRPAMT201301UVPatient(PatientSearchCriteria patientSearchCriteria) {

        II patId = new II();
        patId.setExtension(patientSearchCriteria.getPatientID());
        patId.setRoot(patientSearchCriteria.getAssigningAuthorityID());
        PRPAMT201301UV02Patient patient = HL7PatientTransforms.create201301Patient(HL7PatientTransforms.create201301PatientPerson(patientSearchCriteria.getFirstName(), patientSearchCriteria.getLastName(), null, null, null), patId);
        PRPAIN201305UV02 searchPat = HL7PRPA201305Transforms.createPRPA201305(patient, patientSearchCriteria.getOrganizationID(), patientSearchCriteria.getOrganizationID(), patientSearchCriteria.getAssigningAuthorityID());
        return searchPat;
    }

    /**
     * 
     * @return
     */
    private AdapterComponentMpiProxy getAdapterComponentMpiProxy() {
        AdapterComponentMpiProxyObjectFactory mpiFactory = new AdapterComponentMpiProxyObjectFactory();
        AdapterComponentMpiProxy mpiProxy = mpiFactory.getAdapterComponentMpiProxy();
        return mpiProxy;
    }

    /**
     * 
     * @param patients
     * @return
     */
    private Patients convertPRPAIN201306UVToPatients(PRPAIN201306UV02 patients) {
        Patients mpiPatients = new Patients();
        Patient searchPatient = null;
        PRPAMT201310UV02Patient mpiPatResult = null;
        PersonNameType name = null;
        PersonName personName = null;
        if ((patients != null) &&
                (patients.getControlActProcess() != null) &&
                NullChecker.isNotNullish(patients.getControlActProcess().getSubject())) {
            log.debug("convertPRPAIN201306UVToPatients - patients size: " + patients.getControlActProcess().getSubject().size());
            for (PRPAIN201306UV02MFMIMT700711UV01Subject1 subj1 : patients.getControlActProcess().getSubject()) {
                if ((subj1.getRegistrationEvent() != null) &&
                        (subj1.getRegistrationEvent().getSubject1() != null) &&
                        (subj1.getRegistrationEvent().getSubject1().getPatient() != null)) {
                    mpiPatResult = subj1.getRegistrationEvent().getSubject1().getPatient();
                    searchPatient = new Patient();

                    if (NullChecker.isNotNullish(mpiPatResult.getId()) &&
                            mpiPatResult.getId().get(0) != null &&
                            NullChecker.isNotNullish(mpiPatResult.getId().get(0).getExtension()) &&
                            NullChecker.isNotNullish(mpiPatResult.getId().get(0).getRoot())) {
                        log.debug("convertPRPAIN201306UVToPatients - patients getExtension: " + mpiPatResult.getId().get(0).getExtension());
                        log.debug("convertPRPAIN201306UVToPatients - patients getRoot: " + mpiPatResult.getId().get(0).getRoot());
                        searchPatient.getIdentifiers().add(mpiPatResult.getId().get(0).getExtension(), mpiPatResult.getId().get(0).getRoot());
                    }

                    if (mpiPatResult.getPatientPerson() != null &&
                            mpiPatResult.getPatientPerson().getValue() != null &&
                            mpiPatResult.getPatientPerson().getValue().getName() != null) {
                        name = HL7Extractors.translatePNListtoPersonNameType(mpiPatResult.getPatientPerson().getValue().getName());
                        if (name != null) {
                            log.debug("convertPRPAIN201306UVToPatients - patients name.getGivenName(): " + name.getGivenName());
                            log.debug("convertPRPAIN201306UVToPatients - patients name.getFamilyName(): " + name.getFamilyName());
                            personName = new PersonName();
                            personName.setFirstName(name.getGivenName());
                            personName.setLastName(name.getFamilyName());
                            searchPatient.setName(personName);
                        }

                    } else {
                        log.debug("convertPRPAIN201306UVToPatients - mpiPatResult.getPatientPerson(): null");
                    }

                    mpiPatients.add(searchPatient);

                }
            }

        } else {
            log.debug("convertPRPAIN201306UVToPatients - patients size: null");
        }

        return mpiPatients;
    }

    /**
     *
     * @return
     */
    private List<PatientVO> convertMPIPatientToPatientVO(Patients mpiPatients) throws Exception {
        List<PatientVO> patientVOs = new ArrayList<PatientVO>();

        if (mpiPatients == null || mpiPatients.size() < 1) {
            return patientVOs;
        }

        for (Patient patient : mpiPatients) {
            PatientVO patientVO = new PatientVO();

            PersonName name = patient.getName();

            if (name != null) {
                patientVO.setFirstName(name.getFirstName());
                patientVO.setLastName(name.getLastName());
            }

            for (Identifier id : patient.getIdentifiers()) {
                patientVO.setPatientID(id.getId());
                patientVO.setOrganizationID(id.getOrganizationId());
            }

            patientVOs.add(patientVO);
        }

        return patientVOs;
    }
}
