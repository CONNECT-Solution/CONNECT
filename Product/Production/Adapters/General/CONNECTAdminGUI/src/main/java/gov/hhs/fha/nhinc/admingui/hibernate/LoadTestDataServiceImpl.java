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
package gov.hhs.fha.nhinc.admingui.hibernate;

import gov.hhs.fha.nhinc.admingui.services.LoadTestDataService;
import gov.hhs.fha.nhinc.admingui.services.exception.LoadTestDataException;
import gov.hhs.fha.nhinc.admingui.util.HelperUtil;
import gov.hhs.fha.nhinc.patientdb.dao.PatientDAO;
import gov.hhs.fha.nhinc.patientdb.dao.PersonnameDAO;
import gov.hhs.fha.nhinc.patientdb.model.Patient;
import gov.hhs.fha.nhinc.patientdb.model.Personname;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 *
 * @author Tran Tang
 */
@Service
public class LoadTestDataServiceImpl implements LoadTestDataService {
    private static final Logger LOG = LoggerFactory.getLogger(LoadTestDataServiceImpl.class);
    private PatientDAO patientDAO = PatientDAO.getPatientDAOInstance();
    private PersonnameDAO personnameDAO = PersonnameDAO.getPersonnameDAOInstance();

    @Override
    public List<Patient> getAllPatients() {
        LOG.info("Service-getAll-patients");
        return patientDAO.getAll();
    }

    @Override
    public boolean deletePatient(Patient patient) {
        LOG.info("Service-delete-patient");
        return patientDAO.deleteTransaction(patient);
    }

    @Override
    public Patient getPatientById(Long id) {
        LOG.info("Service-getPatient-byID");
        return patientDAO.read(id);
    }

    @Override
    public boolean savePatient(Patient patient) throws LoadTestDataException {
        boolean actionResult;
        LOG.info("Service-save-patient");

        if (CollectionUtils.isNotEmpty(patient.getPersonnames())) {
            if (HelperUtil.isId(patient.getPatientId())) {
                LOG.debug("update-patient-byID");
                actionResult = patientDAO.update(patient);
            } else {
                LOG.debug("create-patient-new-record");
                actionResult = patientDAO.create(patient);
            }

            for (Personname personnameRecord : patient.getPersonnames()) {
                LOG.debug("Personname-Patient-patientId: {} ", personnameRecord.getPatient().getPatientId());

                personnameRecord.setPatient(patient);

                if (HelperUtil.isId(personnameRecord.getPersonnameId())) {
                    LOG.debug("update-personname-byID");
                    actionResult = personnameDAO.update(personnameRecord);
                } else {
                    LOG.debug("create-personname-new-record");
                    actionResult = personnameDAO.create(personnameRecord);
                }
            }

            if (!actionResult) {
                throw new LoadTestDataException("Patient cannot be save.");
            }

        }
        else {
            throw new LoadTestDataException("Patient-Personname is required when trying to save-patient.");
        }

        LOG.info("save-patient: Action-result: {} ", actionResult);
        return actionResult;
    }

}
