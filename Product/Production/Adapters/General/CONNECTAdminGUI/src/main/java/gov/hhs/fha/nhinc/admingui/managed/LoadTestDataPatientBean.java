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
package gov.hhs.fha.nhinc.admingui.managed;

import static gov.hhs.fha.nhinc.util.CoreHelpUtils.lastItem;

import gov.hhs.fha.nhinc.admingui.model.loadtestdata.Patient;
import gov.hhs.fha.nhinc.admingui.services.LoadTestDataWSService;
import gov.hhs.fha.nhinc.admingui.services.impl.LoadTestDataWSServiceImpl;
import gov.hhs.fha.nhinc.admingui.util.HelperUtil;
import gov.hhs.fha.nhinc.common.loadtestdatamanagement.AddressType;
import gov.hhs.fha.nhinc.common.loadtestdatamanagement.IdentifierType;
import gov.hhs.fha.nhinc.common.loadtestdatamanagement.PatientType;
import gov.hhs.fha.nhinc.common.loadtestdatamanagement.PersonNameType;
import gov.hhs.fha.nhinc.common.loadtestdatamanagement.PhoneNumberType;
import gov.hhs.fha.nhinc.loadtestdata.LoadTestDataException;
import gov.hhs.fha.nhinc.util.CoreHelpUtils;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author Tran Tang
 *
 */
@ManagedBean(name = "loadTestDataPatientBean")
@ViewScoped
@Component
public class LoadTestDataPatientBean {
    private static final Logger LOG = LoggerFactory.getLogger(LoadTestDataPatientBean.class);
    private static final String ADDITIONAL_NAME = "Additional-name";
    private static final String ADDRESS = "Address";
    private static final String IDENTIFIER = "an Identifier";
    private static final String PHONE_NUMBER = "a Phonenumber";
    private static final String PATIENT = "Patient";
    private static final String EDIT = "edit";
    private static final String UNSUCCESSFUL = "Operation was not successful. Please refresh your browser and try again.";

    private String dialogTitle;

    private Patient selectedPatient;
    private PersonNameType selectedPersonname;
    private IdentifierType selectedIdentifier;
    private AddressType selectedAddress;
    private PhoneNumberType selectedPhonenumber;

    private Patient withPatient;
    private PersonNameType withPersonname;
    private IdentifierType withIdentifier;
    private AddressType withAddress;
    private PhoneNumberType withPhonenumber;

    private List<Patient> patientList;

    private LoadTestDataWSService wsService = new LoadTestDataWSServiceImpl();

    public Patient getSelectedPatient() {
        return selectedPatient;
    }

    public void setSelectedPatient(Patient selectedPatient) {
        this.selectedPatient = selectedPatient;
    }

    // selected-record
    public PersonNameType getSelectedPersonname() {
        return selectedPersonname;
    }

    public void setSelectedPersonname(PersonNameType selectedPersonname) {
        this.selectedPersonname = selectedPersonname;
    }

    public IdentifierType getSelectedIdentifier() {
        return selectedIdentifier;
    }

    public void setSelectedIdentifier(IdentifierType selectedIdentifier) {
        this.selectedIdentifier = selectedIdentifier;
    }

    public AddressType getSelectedAddress() {
        return selectedAddress;
    }

    public void setSelectedAddress(AddressType selectedAddress) {
        this.selectedAddress = selectedAddress;
    }

    public PhoneNumberType getSelectedPhonenumber() {
        return selectedPhonenumber;
    }

    public void setSelectedPhonenumber(PhoneNumberType selectedPhonenumber) {
        this.selectedPhonenumber = selectedPhonenumber;
    }

    // database-methods
    public List<PhoneNumberType> getPhonenumbers() {
        if(null != withPatient){
            return withPatient.getPhoneNumberList();
        }
        return new ArrayList<>();
    }

    public boolean deletePhonenumber() {
        boolean result = false;
        if (selectedPhonenumber != null) {
            result = wsService.deletePhoneNumber(selectedPhonenumber);
            refreshPatient();
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
                withPhonenumber.setPatientId(withPatient.getPatientId());
                actionResult = wsService.savePhoneNumber(withPhonenumber);

                if (actionResult) {
                    refreshPatient();
                    addPatientInfoMessages(msgForSaveSuccess(PHONE_NUMBER, withPhonenumber.getPhoneNumberId()));
                    withPhonenumber = null;
                } else {
                    addPatientErrorMessages(UNSUCCESSFUL);
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
            withPhonenumber = wsService.getPhoneNumberBy(selectedPhonenumber.getPhoneNumberId());
            if (null == withPhonenumber) {
                refreshPatient();
                addPatientErrorMessages(msgForLoadFor(PHONE_NUMBER, EDIT));
            }
        } else {
            addPatientErrorMessages(msgForSelectEdit(PHONE_NUMBER));
        }
    }

    public void newPhonenumber() {
        withPhonenumber = null;
    }

    public List<AddressType> getAddresses() {
        if (null != withPatient) {
            return withPatient.getAddressList();
        }
        return new ArrayList<>();
    }

    public boolean deleteAddress() {
        boolean result = false;
        if (selectedAddress != null) {
            result = wsService.deleteAddress(selectedAddress);
            refreshPatient();
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
                withAddress.setPatientId(withPatient.getPatientId());
                actionResult = wsService.saveAddress(withAddress);

                if (actionResult) {
                    refreshPatient();
                    addPatientInfoMessages(msgForSaveSuccess(ADDRESS, withAddress.getAddressId()));
                    withAddress = null;
                } else {
                    addPatientErrorMessages(UNSUCCESSFUL);
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
            withAddress = wsService.getAddressBy(selectedAddress.getAddressId());
            if (null == withAddress) {
                refreshPatient();
                addPatientErrorMessages(msgForLoadFor(ADDRESS, EDIT));
            }
        } else {
            addPatientErrorMessages(msgForSelectEdit(ADDRESS));
        }
    }

    public void newAddress() {
        withAddress = null;
    }

    public List<IdentifierType> getIdentifiers() {
        if (null != withPatient) {
            return withPatient.getIdentifierList();
        }
        return new ArrayList<>();
    }

    public boolean deleteIdentifier() {
        boolean result = false;
        if (selectedIdentifier != null) {
            // Patient-Personname&Identifier: are required for patient-record
            if (CollectionUtils.isNotEmpty(getIdentifiers()) && getIdentifiers().size() > 1) {
                result = wsService.deleteIdentifier(selectedIdentifier);
                refreshPatient();
                selectedIdentifier = null;
            } else {
                addPatientErrorMessages("Last identifier cannot be deleted.");
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
                withIdentifier.setPatientId(withPatient.getPatientId());
                actionResult = wsService.saveIdentifier(withIdentifier);

                if (actionResult) {
                    refreshPatient();
                    addPatientInfoMessages(msgForSaveSuccess(IDENTIFIER, withIdentifier.getIdentifierId()));
                    withIdentifier = null;
                } else {
                    addPatientErrorMessages(UNSUCCESSFUL);
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
            withIdentifier = wsService.getIdentifierBy(selectedIdentifier.getIdentifierId());
            if (null == withIdentifier) {
                refreshPatient();
                addPatientErrorMessages(msgForLoadFor(IDENTIFIER, EDIT));
            }
        } else {
            addPatientErrorMessages(msgForSelectEdit(IDENTIFIER));
        }
    }

    public void newIdentifier() {
        withIdentifier = null;
    }

    public List<PersonNameType> getPersonnames() {
        if (null != withPatient) {
            return withPatient.getPersonNameList();
        }
        return new ArrayList<>();
    }

    public boolean deletePersonname() {
        boolean result = false;
        if (selectedPersonname != null) {
            // Patient-Personname&Identifier: are required for patient-record
            if (CollectionUtils.isNotEmpty(getPersonnames()) && getPersonnames().size() > 1) {
                result = wsService.deletePersonName(selectedPersonname);
                refreshPatientList();
                refreshPatient();
                selectedPersonname = null;
            } else {
                addPatientErrorMessages("Last person name cannot be deleted.");
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
                withPersonname.setPatientId(withPatient.getPatientId());
                actionResult = wsService.savePersonName(withPersonname);
                refreshPatientList();
                if (actionResult) {
                    refreshPatient();
                    addPatientInfoMessages(msgForSaveSuccess(ADDITIONAL_NAME, withPersonname.getPersonNameId()));
                    withPersonname = null;
                } else {
                    addPatientErrorMessages(UNSUCCESSFUL);
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
            withPersonname = wsService.getPersonNameBy(selectedPersonname.getPersonNameId());
            if (null == withPersonname) {
                refreshPatient();
                addPatientErrorMessages(msgForLoadFor(ADDITIONAL_NAME, EDIT));
            }
        } else {
            addPatientErrorMessages(msgForSelectEdit(ADDITIONAL_NAME));
        }
    }

    public void newPersonname() {
        withPersonname = null;
    }

    public List<Patient> getPatients() {
        if (CollectionUtils.isEmpty(patientList)) {
            patientList = HelperUtil.convertPatients(wsService.getAllPatients());
        }
        return patientList;
    }

    public boolean deletePatient() {
        boolean result = false;
        if (selectedPatient != null) {
            result = wsService.deletePatient(selectedPatient);
            refreshPatientList();
            selectedPatient = null;
        } else {
            addPatientsListMessages(msgForSelectDelete("patient"));
        }
        return result;
    }

    public void editPatient() {
        if (selectedPatient != null) {
            dialogTitle = "Edit Patient";
            PatientType tempPatient = wsService.getPatientBy(selectedPatient.getPatientId());
            if (null != tempPatient) {
                withPatient = new Patient(tempPatient);
            } else {
                refreshPatientList();
                addPatientErrorMessages(msgForLoadFor(PATIENT, EDIT));
            }
        } else {
            newPatient();
            addPatientErrorMessages(msgForSelectEdit(PATIENT));
        }
    }

    public void duplicatePatient() {
        if (selectedPatient != null) {
            dialogTitle = "Edit Patient";
            withPatient = new Patient(wsService.duplicatePatient(selectedPatient.getPatientId()));
            if (null != withPatient) {
                selectedPatient = withPatient;
                patientList.add(withPatient);
            }
        }
    }

    public void newPatient() {
        dialogTitle = "Create Patient";
        withPatient = null;
        withPersonname = null;
        withIdentifier = null;
        withAddress = null;
        withPhonenumber = null;
        selectedPatient = null;
    }

    public boolean savePatient() {
        boolean actionResult = false;
        try {
            actionResult = wsService.savePatient(withPatient);
            refreshPatientList();
            if (actionResult) {
                addPatientInfoMessages(msgForSaveSuccess("patient basic-info", withPatient.getPatientId()));
            } else {
                addPatientErrorMessages(UNSUCCESSFUL);
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

    public PersonNameType getPatientPersonnameForm() {
        PersonNameType record = lastItem(getPatientForm().getPersonNameList());
        if (null == record) {
            record = new PersonNameType();
            getPatientForm().getPersonNameList().add(record);
        }
        return record;
    }

    public IdentifierType getPatientIdentifierForm() {
        IdentifierType record = lastItem(getPatientForm().getIdentifierList());
        if (null == record) {
            record = new IdentifierType();
            getPatientForm().getIdentifierList().add(record);
        }
        return record;
    }

    public PersonNameType getPersonnameForm() {
        if (null == withPersonname) {
            withPersonname = new PersonNameType();
        }
        return withPersonname;
    }

    public IdentifierType getIdentifierForm() {
        if (null == withIdentifier) {
            withIdentifier = new IdentifierType();
        }
        return withIdentifier;
    }

    public AddressType getAddressForm() {
        if (null == withAddress) {
            withAddress = new AddressType();
        }
        return withAddress;
    }

    public PhoneNumberType getPhonenumberForm() {
        if (null == withPhonenumber) {
            withPhonenumber = new PhoneNumberType();
        }
        return withPhonenumber;
    }

    public Date getDateOfBirth() {
        return CoreHelpUtils.getDate(getPatientForm().getDateOfBirth());
    }

    public void setDateOfBirth(Date dateOfBirth) {
        getPatientForm().setDateOfBirth(CoreHelpUtils.getXMLGregorianCalendarFrom(dateOfBirth));
    }

    public String getSsn() {
        return getPatientForm().getSsn();
    }

    public void setSsn(String ssn) {
        if (StringUtils.isNotBlank(ssn) && ssn.indexOf('-') > -1) {
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
        return withPatient != null && CoreHelpUtils.isId(withPatient.getPatientId());
    }

    private static void logPatientError(String logOf, LoadTestDataException e) {
        FacesContext.getCurrentInstance().validationFailed();
        String lowerLogOf = logOf.toLowerCase();
        addPatientErrorMessages(
            MessageFormat.format("Cannot save patient {0}: {1}", lowerLogOf, e.getLocalizedMessage()));
        LOG.error("Error save-patient-{0}: {}", lowerLogOf, e.getLocalizedMessage(), e);
    }

    private static String msgForSaveSuccess(String ofType, Long ofId) {
        return MessageFormat.format("Save {0} successful.", ofType.toLowerCase());
    }

    private static String msgForSelectEdit(String ofType) {
        return MessageFormat.format("Select {0} for edit.", ofType.toLowerCase());
    }

    private static String msgForLoadFor(String ofType, String action) {
        return MessageFormat.format("Loading {0} for {1}.", ofType.toLowerCase(), action.toLowerCase());
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

    private void refreshPatient(){
        if(null == selectedPatient){
            selectedPatient = withPatient;
        }
        editPatient();
    }

    private void refreshPatientList() {
        patientList = HelperUtil.convertPatients(wsService.getAllPatients());
    }

}
