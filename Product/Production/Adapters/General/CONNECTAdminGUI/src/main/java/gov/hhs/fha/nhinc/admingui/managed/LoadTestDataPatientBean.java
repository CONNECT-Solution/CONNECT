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
import gov.hhs.fha.nhinc.admingui.util.HelperUtil;
import gov.hhs.fha.nhinc.patientdb.model.Patient;
import gov.hhs.fha.nhinc.patientdb.model.Personname;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;
import org.apache.commons.collections.MapUtils;
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

    private List<Patient> patients = new ArrayList<>();
    private Map<String, String> genderList;

    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private String gender;

    @Autowired
    private LoadTestDataService loadTestDataService;

    public LoadTestDataPatientBean() {
        LOG.debug("Initualize LoadTestDataPatientBean");
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

    public void deletePatient(ActionEvent event) {
        if (selectedPatient != null) {
            LOG.debug("deletePatient() - selectedPatient-ID: " + selectedPatient.getPatientId());
            loadTestDataService.deletePatient(selectedPatient);
        }
    }

    public void savePatient() {
        Patient patient = new Patient();
        patient.setDateOfBirth(HelperUtil.toTimestamp(dateOfBirth));
        patient.setGender(gender);

        Personname personname = new Personname();
        personname.setFirstName(firstName);
        personname.setLastName(lastName);

        patient.getPersonnames().add(personname);

        loadTestDataService.savePatient(patient);
    }

    // CLEAR-TAB-FORM
    public String clearPatientTab() {
        firstName = null;
        lastName = null;
        dateOfBirth = null;
        gender = null;
        return NavigationConstant.LOAD_TEST_DATA_PAGE;
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

    public Map<String, String> getGenderList() {
        if (MapUtils.isEmpty(genderList)) {
            LOG.debug("populteGenderList()");
            genderList = HelperUtil.populteGenderList();
        }
        return genderList;
    }

}
