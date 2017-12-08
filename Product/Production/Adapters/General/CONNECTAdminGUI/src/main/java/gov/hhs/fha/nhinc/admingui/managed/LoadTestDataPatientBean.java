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

import gov.hhs.fha.nhinc.admingui.services.LoadTestDataService;
import gov.hhs.fha.nhinc.admingui.services.exception.LoadTestDataException;
import gov.hhs.fha.nhinc.admingui.services.exception.UnSupportedMethodException;
import gov.hhs.fha.nhinc.admingui.util.HelperUtil;
import gov.hhs.fha.nhinc.patientdb.model.Address;
import gov.hhs.fha.nhinc.patientdb.model.Identifier;
import gov.hhs.fha.nhinc.patientdb.model.Patient;
import gov.hhs.fha.nhinc.patientdb.model.Personname;
import gov.hhs.fha.nhinc.patientdb.model.Phonenumber;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ImportResource;
import org.springframework.stereotype.Component;

/**
 * @author Tran Tang
 *
 */
@ManagedBean(name = "loadTestDataPatientBean")
@ImportResource("file:${nhinc.properties.dir}/LoadTestDataConfig.xml")
@SessionScoped
@Component
public class LoadTestDataPatientBean {
    private static final Logger LOG = LoggerFactory.getLogger(LoadTestDataPatientBean.class);
    private static final String ADDITIONAL_NAME = "Additional-name";
    private static final String ADDRESS = "Address";
    private static final String IDENTIFIER = "an Identifier";
    private static final String PHONE_NUMBER = "a Phonenumber";

    private String dialogTitle;

    private Patient selectedPatient;
    private Personname selectedPersonname;
    private Identifier selectedIdentifier;
    private Address selectedAddress;
    private Phonenumber selectedPhonenumber;

    private Patient withPatient;
    private Personname withPersonname;
    private Identifier withIdentifier;
    private Address withAddress;
    private Phonenumber withPhonenumber;

    private List<Personname> personnameList;
    private List<Identifier> identifierList;

    @Autowired
    private LoadTestDataService loadTestDataService;

    public Patient getSelectedPatient() {
        return selectedPatient;
    }

    public void setSelectedPatient(Patient selectedPatient) {
        this.selectedPatient = selectedPatient;
    }

    // selected-record
    public Personname getSelectedPersonname() {
        return selectedPersonname;
    }

    public void setSelectedPersonname(Personname selectedPersonname) {
        this.selectedPersonname = selectedPersonname;
    }

    public Identifier getSelectedIdentifier() {
        return selectedIdentifier;
    }

    public void setSelectedIdentifier(Identifier selectedIdentifier) {
        this.selectedIdentifier = selectedIdentifier;
    }

    public Address getSelectedAddress() {
        return selectedAddress;
    }

    public void setSelectedAddress(Address selectedAddress) {
        this.selectedAddress = selectedAddress;
    }

    public Phonenumber getSelectedPhonenumber() {
        return selectedPhonenumber;
    }

    public void setSelectedPhonenumber(Phonenumber selectedPhonenumber) {
        this.selectedPhonenumber = selectedPhonenumber;
    }

    // database-methods
    public List<Phonenumber> getPhonenumbers() {
        return loadTestDataService.getAllPhonenumbersBy(getPatientId());
    }

    public boolean deletePhonenumber() {
        boolean result = false;
        if (selectedPhonenumber != null) {
            result = loadTestDataService.deletePhonenumber(selectedPhonenumber);
            selectedPhonenumber = null;
        } else {
            addPatientErrorMessages(msgForSelectDelete(PHONE_NUMBER));
        }
        return result;
    }

    public boolean savePhonenumber() {
        boolean actionResult = false;

        if (isValidPatientId()) {
            try {
                withPhonenumber.setPatient(withPatient);
                actionResult = loadTestDataService.savePhonenumber(withPhonenumber);

                if (actionResult) {
                    addPatientInfoMessages(msgForSaveSuccess(PHONE_NUMBER, withPhonenumber.getPhonenumberId()));
                    withPhonenumber = null;
                }
            } catch (LoadTestDataException e) {
                logPatientError(PHONE_NUMBER, e);
            }
        } else {
            addPatientErrorMessages(msgForInvalidPatient(PHONE_NUMBER));
        }
        return actionResult;
    }

    public void editPhonenumber() {
        if (selectedPhonenumber != null) {
            withPhonenumber = loadTestDataService.getPhonenumberBy(selectedPhonenumber.getPhonenumberId());
        } else {
            addPatientErrorMessages(msgForSelectEdit(PHONE_NUMBER));
        }
    }

    public void newPhonenumber() {
        withPhonenumber = null;
    }

    public List<Address> getAddresses() {
        return loadTestDataService.getAllAddressesBy(getPatientId());
    }

    public boolean deleteAddress() {
        boolean result = false;
        if (selectedAddress != null) {
            result = loadTestDataService.deleteAddress(selectedAddress);
            selectedAddress = null;
        } else {
            addPatientErrorMessages(msgForSelectDelete(ADDRESS));
        }
        return result;
    }

    public boolean saveAddress() {
        boolean actionResult = false;
        if (isValidPatientId()) {
            try {
                withAddress.setPatient(withPatient);
                actionResult = loadTestDataService.saveAddress(withAddress);

                if (actionResult) {
                    addPatientInfoMessages(msgForSaveSuccess(ADDRESS, withAddress.getAddressId()));
                    withAddress = null;
                }
            } catch (LoadTestDataException e) {
                logPatientError(ADDRESS, e);
            }
        } else {
            addPatientErrorMessages(msgForInvalidPatient(ADDRESS));
        }

        return actionResult;
    }

    public void editAddress() {
        if (selectedAddress != null) {
            withAddress = loadTestDataService.getAddressBy(selectedAddress.getAddressId());
        } else {
            addPatientErrorMessages(msgForSelectEdit(ADDRESS));
        }
    }

    public void newAddress() {
        withAddress = null;
    }

    public List<Identifier> getIdentifiers() {
        identifierList = loadTestDataService.getAllIdentiersBy(getPatientId());
        return identifierList;
    }

    public boolean deleteIdentifier() {
        boolean result = false;
        if (selectedIdentifier != null) {
            // Patient-Personname&Identifier: are required for patient-record
            if (CollectionUtils.isNotEmpty(identifierList) && identifierList.size() > 1) {
                result = loadTestDataService.deleteIdentifier(selectedIdentifier);
                selectedIdentifier = null;
            } else {
                addPatientErrorMessages("Patient-identifier cannot be empty: fail to delete the last record");
            }
        } else {
            addPatientErrorMessages(msgForSelectDelete(IDENTIFIER));
        }
        return result;
    }

    public boolean saveIdentifier() {
        boolean actionResult = false;

        if (isValidPatientId()) {
            try {
                withIdentifier.setPatient(withPatient);
                actionResult = loadTestDataService.saveIdentifier(withIdentifier);

                if (actionResult) {
                    addPatientInfoMessages(msgForSaveSuccess(IDENTIFIER, withIdentifier.getIdentifierId()));
                    withIdentifier = null;
                }
            } catch (LoadTestDataException e) {
                logPatientError(IDENTIFIER, e);
            }
        } else {
            addPatientErrorMessages(msgForInvalidPatient(IDENTIFIER));
        }

        return actionResult;
    }

    public void editIdentifier() {
        if (selectedIdentifier != null) {
            withIdentifier = loadTestDataService.getIdentifierBy(selectedIdentifier.getIdentifierId());
        } else {
            addPatientErrorMessages(msgForSelectEdit(IDENTIFIER));
        }
    }

    public void newIdentifier() {
        withIdentifier = null;
    }

    public List<Personname> getPersonnames() {
        personnameList = loadTestDataService.getAllPersonnamesBy(getPatientId());
        return personnameList;
    }

    public boolean deletePersonname() {
        boolean result = false;
        if (selectedPersonname != null) {
            // Patient-Personname&Identifier: are required for patient-record
            if (CollectionUtils.isNotEmpty(personnameList) && personnameList.size() > 1) {
                result = loadTestDataService.deletePersonname(selectedPersonname);
                selectedPersonname = null;
            } else {
                addPatientErrorMessages("Patient-personname cannot be empty: fail to delete the last record");
            }
        } else {
            addPatientErrorMessages(msgForSelectDelete(ADDITIONAL_NAME));
        }
        return result;
    }

    public boolean savePersonname() {
        boolean actionResult = false;
        if (isValidPatientId()) {
            try {
                withPersonname.setPatient(withPatient);
                actionResult = loadTestDataService.savePersonname(withPersonname);

                if (actionResult) {
                    addPatientInfoMessages(msgForSaveSuccess(ADDITIONAL_NAME, withPersonname.getPersonnameId()));
                    withPersonname = null;
                }
            } catch (LoadTestDataException e) {
                logPatientError("Personname", e);
            }
        } else {
            addPatientErrorMessages(msgForInvalidPatient(ADDITIONAL_NAME));
        }
        return actionResult;
    }

    public void editPersonname() {
        if (selectedPersonname != null) {
            withPersonname = loadTestDataService.getPersonnameBy(selectedPersonname.getPersonnameId());
        } else {
            addPatientErrorMessages(msgForSelectEdit(ADDITIONAL_NAME));
        }
    }

    public void newPersonname() {
        withPersonname = null;
    }

    public List<Patient> getPatients() {
        return loadTestDataService.getAllPatients();
    }

    public boolean deletePatient() {
        boolean result = false;
        if (selectedPatient != null) {
            result = loadTestDataService.deletePatient(selectedPatient);
            selectedPatient = null;
        } else {
            addPatientsListMessages(msgForSelectDelete("patient"));
        }
        return result;
    }

    public void editPatient() {
        if (selectedPatient != null) {
            dialogTitle = "Edit Patient";
            withPatient = loadTestDataService.getPatientBy(selectedPatient.getPatientId());
        } else {
            newPatient();
            addPatientErrorMessages(msgForSelectEdit("patient"));
        }
    }

    public void duplicatePatient() {
        if (selectedPatient != null) {
            dialogTitle = "Edit Patient";
            withPatient = loadTestDataService.duplicatePatient(selectedPatient.getPatientId());
        }
    }

    public void newPatient() {
        dialogTitle = "Create Patient";
        withPatient = null;
        withPersonname = null;
        withIdentifier = null;
        withAddress = null;
        withPhonenumber = null;
    }

    public boolean savePatient() {
        boolean actionResult = false;
        try {
            actionResult = loadTestDataService.savePatient(withPatient);

            if (actionResult) {
                addPatientInfoMessages(msgForSaveSuccess("patient basic-info", withPatient.getPatientId()));
            }
        } catch (LoadTestDataException e) {
            logPatientError("basic-info", e);
        }
        return actionResult;
    }

    // with-property
    public Patient getPatientForm() {
        if (null == withPatient) {
            withPatient = new Patient();
        }
        return withPatient;
    }

    public Personname getPatientPersonnameForm() {
        Personname record = getPatientForm().getLastPersonname();
        if (null == record) {
            record = new Personname();
            getPatientForm().getPersonnames().add(record);
        }
        return record;
    }

    public Identifier getPatientIdentifierForm() {
        Identifier record = getPatientForm().getLastIdentifier();
        if (null == record) {
            record = new Identifier();
            getPatientForm().getIdentifiers().add(record);
        }
        return record;
    }

    public Personname getPersonnameForm() {
        if (null == withPersonname) {
            withPersonname = new Personname();
        }
        return withPersonname;
    }

    public Identifier getIdentifierForm() {
        if (null == withIdentifier) {
            withIdentifier = new Identifier();
        }
        return withIdentifier;
    }

    public Address getAddressForm() {
        if (null == withAddress) {
            withAddress = new Address();
        }
        return withAddress;
    }

    public Phonenumber getPhonenumberForm() {
        if (null == withPhonenumber) {
            withPhonenumber = new Phonenumber();
        }
        return withPhonenumber;
    }

    public Date getDateOfBirth() {
        return HelperUtil.toDate(getPatientForm().getDateOfBirth());
    }

    public void setDateOfBirth(Date dateOfBirth) {
        getPatientForm().setDateOfBirth(HelperUtil.toTimestamp(dateOfBirth));
    }

    public String getSsn() {
        return getPatientForm().getSsn();
    }

    public void setSsn(String ssn) {
        if (StringUtils.isNotBlank(ssn) && ssn.indexOf("-") > -1) {
            getPatientForm().setSsn(ssn.replace("-", ""));
        } else {
            getPatientForm().setSsn(ssn);
        }
    }

    public boolean getDisableAccordion() {
        return !isValidPatientId();
    }

    public boolean getDisablePatientButtons() {
        return selectedPatient == null;
    }

    public boolean getDisableAddNameButtons() {
        return selectedPersonname == null;
    }

    public boolean getDisableIdentifierButtons() {
        return selectedIdentifier == null;
    }

    public boolean getDisableAddressButtons() {
        return selectedAddress == null;
    }

    public boolean getDisablePhoneNumButtons() {
        return selectedPhonenumber == null;
    }

    public String getDialogTitle() {
        return dialogTitle;
    }

    public String getDateNow() {
        return HelperUtil.getDateNow();
    }

    public Map<String, String> getListStates() {
        return HelperUtil.populateListStates();
    }

    // IDs and isValidId
    private boolean isValidPatientId() {
        return withPatient != null && HelperUtil.isId(withPatient.getPatientId());
    }

    private Long getPatientId() {
        Long localId = 0L;
        if (isValidPatientId()) {
            localId = withPatient.getPatientId();
        }
        return localId;
    }

    private static void logPatientError(String logOf, LoadTestDataException e) {
        FacesContext.getCurrentInstance().validationFailed();
        addPatientErrorMessages(
            MessageFormat.format("Cannot save patient {0}: {1}", logOf.toLowerCase(), e.getLocalizedMessage()));
        LOG.error("Error save-patient-{0}: {}", logOf.toLowerCase(), e.getLocalizedMessage(), e);
    }

    private static String msgForSaveSuccess(String ofType, Long ofId) {
        return MessageFormat.format("Save {0} successful.", ofType.toLowerCase());
    }

    private static String msgForSelectEdit(String ofType) {
        return MessageFormat.format("Select {0} for edit.", ofType.toLowerCase());
    }

    private static String msgForSelectDelete(String ofType) {
        return MessageFormat.format("Select {0} for delete.", ofType.toLowerCase());
    }

    private static String msgForInvalidPatient(String ofType) {
        return MessageFormat.format("{0} cannot be saved: patient does not exist.", ofType);
    }

    // manage-FacesMessage
    private static void addPatientErrorMessages(String messageText) {
        HelperUtil.addFacesMessageBy("patientErrorMessages", FacesMessage.SEVERITY_ERROR, messageText);
    }

    private static void addPatientInfoMessages(String messageText) {
        HelperUtil.addFacesMessageBy("patientInfoMessages", FacesMessage.SEVERITY_INFO, messageText);
    }

    private static void addPatientsListMessages(String messageText) {
        HelperUtil.addFacesMessageBy("patientsListMessages", FacesMessage.SEVERITY_WARN, messageText);
    }
}
