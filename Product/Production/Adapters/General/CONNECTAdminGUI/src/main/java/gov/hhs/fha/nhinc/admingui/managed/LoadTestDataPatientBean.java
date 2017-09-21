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
package gov.hhs.fha.nhinc.admingui.managed;


import gov.hhs.fha.nhinc.admingui.constant.NavigationConstant;
import gov.hhs.fha.nhinc.admingui.services.LoadTestDataService;
import gov.hhs.fha.nhinc.admingui.services.exception.LoadTestDataException;
import gov.hhs.fha.nhinc.admingui.util.HelperUtil;
import gov.hhs.fha.nhinc.patientdb.model.Patient;
import gov.hhs.fha.nhinc.patientdb.model.Personname;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Tran Tang
 *
 */
@ManagedBean(name = "loadTestDataPatientBean")
@SessionScoped
@Component
public class LoadTestDataPatientBean {
    private static final Logger LOG = LoggerFactory.getLogger(LoadTestDataPatientBean.class);

    private Patient selectedPatient;
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private String gender;
    private long updatePatientId = 0;
    private String ssn;

    @Autowired
    private LoadTestDataService loadTestDataService;

    public LoadTestDataPatientBean() {
        LOG.debug("Initualize LoadTestDataPatientBean");
        selectedPatient = new Patient();
    }

    public Patient getSelectedPatient() {
        return selectedPatient;
    }

    public void setSelectedPatient(Patient selectedPatient) {
        this.selectedPatient = selectedPatient;
    }

    // DATABASE-METHODS-getAll, delete, save
    public List<Patient> getPatients() {
        return loadTestDataService.getAllPatients();
    }

    public boolean deletePatient() {
        if (selectedPatient != null) {
            LOG.debug("deletePatient() - selectedPatient-ID: {} ", selectedPatient.getPatientId());
            return loadTestDataService.deletePatient(selectedPatient);
        } else {
            LOG.info("delete-patient required selected-patient");
            FacesContext.getCurrentInstance().addMessage("patientsListMessages",
                new FacesMessage(FacesMessage.SEVERITY_WARN, "selecting a patient is required for deleting", ""));
        }
        return false;
    }

    public void editPatient() {
        if (selectedPatient != null) {
            LOG.info("selected-patient-for-edit: {}", selectedPatient.getPatientId());
            selectedPatient = loadTestDataService.getPatientById(selectedPatient.getPatientId());
            updateBeanToPatient(selectedPatient);
        } else {
            LOG.info("edit-required a selected-patient.");
        }
    }

    public void newPatient() {
        LOG.info("new-patient clear the form.");
        clearPatientTab();
    }

    public boolean savePatient() {
        boolean actionResult = true;

        try {
            Patient patient;

            if (HelperUtil.isId(updatePatientId)) {
                patient = loadTestDataService.getPatientById(updatePatientId);
                if(patient != null){
                    patient = updatePatientFromBean(patient);
                }else{
                    patient = updatePatientFromBean(new Patient());
                }
            } else {
                patient = updatePatientFromBean(new Patient());
            }

            actionResult = loadTestDataService.savePatient(patient);

            if (actionResult) {
                if (!HelperUtil.isId(updatePatientId)) {
                    clearPatientTab();
                } else {
                    FacesContext.getCurrentInstance().addMessage("patientUpdateMessages", new FacesMessage(
                        FacesMessage.SEVERITY_INFO, "Save Patient Successful: " + patient.getPatientId(), ""));
                }
            }
        } catch (LoadTestDataException e) {
            actionResult = false;
            FacesContext.getCurrentInstance().validationFailed();
            FacesContext.getCurrentInstance().addMessage("patientAddMessages",
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Can not save patient: " + e.getLocalizedMessage(), ""));
            LOG.error("Error save-patient: {}", e.getLocalizedMessage(), e);
        }

        return actionResult;
    }

    // CLEAR-TAB-FORM
    public String clearPatientTab() {
        return updateBeanToPatient(null);
    }

    // PROPERTIES
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        if (StringUtils.isNotBlank(ssn) && ssn.indexOf("-") > -1) {
            this.ssn = ssn.replace("-", "");
        } else {
            this.ssn = ssn;
        }
    }

    public Long getPatientId() {
        return updatePatientId;
    }

    // private-methods
    private Patient updatePatientFromBean(Patient patient) {
        patient.setDateOfBirth(HelperUtil.toTimestamp(dateOfBirth));
        patient.setGender(gender);
        patient.setSsn(ssn);

        if (CollectionUtils.isEmpty(patient.getPersonnames())) {
            patient.getPersonnames().add(new Personname());
        }
        Personname personname = patient.getPersonnames().get(0);
        personname.setFirstName(firstName);
        personname.setLastName(lastName);

        return patient;
    }

    private String updateBeanToPatient(Patient patientRecord) {
        if (patientRecord != null && HelperUtil.isId(patientRecord.getPatientId())) {
            setFirstName(patientRecord.getFirstName());
            setLastName(patientRecord.getLastName());
            setDateOfBirth(patientRecord.getDateOfBirth());
            setGender(patientRecord.getGender());
            setSsn(patientRecord.getSsn());
            updatePatientId = patientRecord.getPatientId();
            LOG.info("set-patient-bean to record: {}, {}, {}, {}, {}", patientRecord.getPatientId(), firstName,
                lastName, dateOfBirth, gender);
        } else {
            LOG.info("set-patient-bean to empty");
            setFirstName(null);
            setLastName(null);
            setDateOfBirth(null);
            setGender(null);
            setSsn(null);
            updatePatientId = 0;
        }
        return NavigationConstant.LOAD_TEST_DATA_PAGE;
    }
}
