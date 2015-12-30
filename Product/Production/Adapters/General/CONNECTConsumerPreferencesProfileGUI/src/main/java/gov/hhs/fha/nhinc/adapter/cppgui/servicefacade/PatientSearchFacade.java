/*
 * Copyright (c) 2009-2015, United States Government, as represented by the Secretary of Health and Human Services.
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
import gov.hhs.fha.nhinc.transform.subdisc.HL7Extractors;
import gov.hhs.fha.nhinc.transform.subdisc.HL7PRPA201305Transforms;
import gov.hhs.fha.nhinc.transform.subdisc.HL7PatientTransforms;
import java.util.ArrayList;
import java.util.List;
import org.hl7.v3.II;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.PRPAIN201306UV02MFMIMT700711UV01Subject1;
import org.hl7.v3.PRPAMT201301UV02Patient;
import org.hl7.v3.PRPAMT201310UV02Patient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author patlollav
 */
public class PatientSearchFacade {

    private static final Logger LOG = LoggerFactory.getLogger(PatientSearchFacade.class);

    /**
     * Search MPI with the given search criteria
     *
     * @param patientSearchCriteria
     * @return patients
     */
    public List<PatientVO> searchPatient(PatientSearchCriteria patientSearchCriteria) throws Exception {

        List<PatientVO> patientVOs;

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
        PRPAMT201301UV02Patient patient = HL7PatientTransforms.create201301Patient(HL7PatientTransforms
                .create201301PatientPerson(patientSearchCriteria.getFirstName(), patientSearchCriteria.getLastName(),
                        null, null, null), patId);
        PRPAIN201305UV02 searchPat = HL7PRPA201305Transforms.createPRPA201305(patient,
                patientSearchCriteria.getOrganizationID(), patientSearchCriteria.getOrganizationID(),
                patientSearchCriteria.getAssigningAuthorityID());
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
        Patient searchPatient;
        PRPAMT201310UV02Patient mpiPatResult;
        PersonNameType name;
        PersonName personName;
        if ((patients != null) && (patients.getControlActProcess() != null)
                && NullChecker.isNotNullish(patients.getControlActProcess().getSubject())) {
            LOG.debug("convertPRPAIN201306UVToPatients - patients size: "
                    + patients.getControlActProcess().getSubject().size());
            for (PRPAIN201306UV02MFMIMT700711UV01Subject1 subj1 : patients.getControlActProcess().getSubject()) {
                if ((subj1.getRegistrationEvent() != null) && (subj1.getRegistrationEvent().getSubject1() != null)
                        && (subj1.getRegistrationEvent().getSubject1().getPatient() != null)) {
                    mpiPatResult = subj1.getRegistrationEvent().getSubject1().getPatient();
                    searchPatient = new Patient();

                    if (NullChecker.isNotNullish(mpiPatResult.getId()) && mpiPatResult.getId().get(0) != null
                            && NullChecker.isNotNullish(mpiPatResult.getId().get(0).getExtension())
                            && NullChecker.isNotNullish(mpiPatResult.getId().get(0).getRoot())) {
                        LOG.debug("convertPRPAIN201306UVToPatients - patients getExtension: "
                                + mpiPatResult.getId().get(0).getExtension());
                        LOG.debug("convertPRPAIN201306UVToPatients - patients getRoot: "
                                + mpiPatResult.getId().get(0).getRoot());
                        searchPatient.getIdentifiers().add(mpiPatResult.getId().get(0).getExtension(),
                                mpiPatResult.getId().get(0).getRoot());
                    }

                    if (mpiPatResult.getPatientPerson() != null && mpiPatResult.getPatientPerson().getValue() != null
                            && mpiPatResult.getPatientPerson().getValue().getName() != null) {
                        name = HL7Extractors.translatePNListtoPersonNameType(mpiPatResult.getPatientPerson().getValue()
                                .getName());
                        if (name != null) {
                            LOG.debug("convertPRPAIN201306UVToPatients - patients name.getGivenName(): "
                                    + name.getGivenName());
                            LOG.debug("convertPRPAIN201306UVToPatients - patients name.getFamilyName(): "
                                    + name.getFamilyName());
                            personName = new PersonName();
                            personName.setFirstName(name.getGivenName());
                            personName.setLastName(name.getFamilyName());
                            searchPatient.getNames().add(personName);
                        }

                    } else {
                        LOG.debug("convertPRPAIN201306UVToPatients - mpiPatResult.getPatientPerson(): null");
                    }

                    mpiPatients.add(searchPatient);

                }
            }

        } else {
            LOG.debug("convertPRPAIN201306UVToPatients - patients size: null");
        }

        return mpiPatients;
    }

    /**
     *
     * @return
     */
    private List<PatientVO> convertMPIPatientToPatientVO(Patients mpiPatients) throws Exception {
        List<PatientVO> patientVOs = new ArrayList<>();

        if (mpiPatients == null || mpiPatients.size() < 1) {
            return patientVOs;
        }

        for (Patient patient : mpiPatients) {
            PatientVO patientVO = new PatientVO();

            PersonName name = patient.getNames().get(0);

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
