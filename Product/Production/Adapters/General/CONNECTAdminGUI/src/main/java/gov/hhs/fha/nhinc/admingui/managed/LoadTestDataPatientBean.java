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
import gov.hhs.fha.nhinc.patientdb.model.Address;
import gov.hhs.fha.nhinc.patientdb.model.Identifier;
import gov.hhs.fha.nhinc.patientdb.model.Patient;
import gov.hhs.fha.nhinc.patientdb.model.Personname;
import gov.hhs.fha.nhinc.patientdb.model.Phonenumber;
import java.text.MessageFormat;
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
    private static final String IDENTIFIER = "Identifier";
    private static final String PHONE_NUMBER = "Phonenumber";
    private String dialogTitle;

    private String firstName;
    private String lastName;
    private String prefix;
    private String middleName;
    private String suffix;
    private Date dateOfBirth;
    private String gender;
    private String ssn;

    private String identifierPatientID;
    private String organizationId;

    private String street1;
    private String street2;
    private String city;
    private String state;
    private String postal;

    private String phonenumberValue;

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

    @Autowired
    private LoadTestDataService loadTestDataService;

    public LoadTestDataPatientBean() {
        selectedPatient = new Patient();
    }

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
        } else {
            addPatientErrorMessages(msgForSelectDelete(PHONE_NUMBER));
        }
        return result;
    }

    public boolean savePhonenumber() {
        boolean actionResult = false;

        if (isValidPatientId()) {
            try {
                Phonenumber phonenumber = readPhonenumber();
                phonenumber.setPatient(withPatient);
                actionResult = loadTestDataService.savePhonenumber(phonenumber);

                if (actionResult) {
                    addPatientInfoMessages(msgForSaveSuccess(PHONE_NUMBER, phonenumber.getPhonenumberId()));
                    clearInputsPhonenumbers();
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
            selectedPhonenumber = loadTestDataService.getPhonenumberBy(selectedPhonenumber.getPhonenumberId());
            updateBeanWithPhonenumber(selectedPhonenumber);
        } else {
            addPatientErrorMessages(msgForSelectEdit(PHONE_NUMBER));
        }
    }

    public void newPhonenumber() {
        clearInputsPhonenumbers();
    }

    public List<Address> getAddresses() {
        return loadTestDataService.getAllAddressesBy(getPatientId());
    }

    public boolean deleteAddress() {
        boolean result = false;
        if (selectedAddress != null) {
            result = loadTestDataService.deleteAddress(selectedAddress);
        } else {
            addPatientErrorMessages(msgForSelectDelete(ADDRESS));
        }
        return result;
    }

    public boolean saveAddress() {
        boolean actionResult = false;
        if (isValidPatientId()) {
            try {
                Address address = readAddress();
                address.setPatient(withPatient);
                actionResult = loadTestDataService.saveAddress(address);

                if (actionResult) {
                    addPatientInfoMessages(msgForSaveSuccess(ADDRESS, address.getAddressId()));
                    clearInputsAddresses();
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
            selectedAddress = loadTestDataService.getAddressBy(selectedAddress.getAddressId());
            updateBeanWithAddress(selectedAddress);
        } else {
            addPatientErrorMessages(msgForSelectEdit(ADDRESS));
        }
    }

    public void newAddress() {
        clearInputsAddresses();
    }

    public List<Identifier> getIdentifiers() {
        return loadTestDataService.getAllIdentiersBy(getPatientId());
    }

    public boolean deleteIdentifier() {
        boolean result = false;
        if (selectedIdentifier != null) {
            result = loadTestDataService.deleteIdentifier(selectedIdentifier);
        } else {
            addPatientErrorMessages(msgForSelectDelete(IDENTIFIER));
        }
        return result;
    }

    public boolean saveIdentifier() {
        boolean actionResult = false;

        if (isValidPatientId()) {
            try {
                Identifier identifier = readIdentifier();
                identifier.setPatient(withPatient);
                actionResult = loadTestDataService.saveIdentifier(identifier);

                if (actionResult) {
                    addPatientInfoMessages(msgForSaveSuccess(IDENTIFIER, identifier.getIdentifierId()));
                    clearInputsIdentifiers();
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
            selectedIdentifier = loadTestDataService.getIdentifierBy(selectedIdentifier.getIdentifierId());
            updateBeanWithIdentifier(selectedIdentifier);
        } else {
            addPatientErrorMessages(msgForSelectEdit(IDENTIFIER));
        }
    }

    public void newIdentifier() {
        clearInputsIdentifiers();
    }

    public List<Personname> getPersonnames() {
        personnameList = loadTestDataService.getAllPersonnamesBy(getPatientId());
        return personnameList;
    }

    public boolean deletePersonname() {
        boolean result = false;
        if (selectedPersonname != null) {
            if (CollectionUtils.isNotEmpty(personnameList) && personnameList.size() > 1) {
                result = loadTestDataService.deletePersonname(selectedPersonname);
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
                Personname personname = readPersonname();
                personname.setPatient(withPatient);
                actionResult = loadTestDataService.savePersonname(personname);

                if (actionResult) {
                    addPatientInfoMessages(msgForSaveSuccess(ADDITIONAL_NAME, personname.getPersonnameId()));
                    clearInputsAdditionalNames();
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
            selectedPersonname = loadTestDataService.getPersonnameBy(selectedPersonname.getPersonnameId());
            updateBeanWithPersonname(selectedPersonname);
        } else {
            addPatientErrorMessages(msgForSelectEdit(ADDITIONAL_NAME));
        }
    }

    public void newPersonname() {
        clearInputsAdditionalNames();
    }

    public List<Patient> getPatients() {
        return loadTestDataService.getAllPatients();
    }

    public boolean deletePatient() {
        boolean result = false;
        if (selectedPatient != null) {
            result = loadTestDataService.deletePatient(selectedPatient);
        } else {
            addPatientsListMessages(msgForSelectDelete("patient"));
        }
        return result;
    }

    public void editPatient() {
        if (selectedPatient != null) {
            selectedPatient = loadTestDataService.getPatientBy(selectedPatient.getPatientId());
            updateBeanWithPatient(selectedPatient);
        } else {
            addPatientErrorMessages(msgForSelectEdit("patient"));
        }
    }

    public void newPatient() {
        clearInputsAll();
    }

    public boolean savePatient() {
        boolean actionResult = false;
        try {
            Patient patient = readPatient();
            actionResult = loadTestDataService.savePatient(patient);

            if (actionResult) {
                withPatient = patient;
                addPatientInfoMessages(msgForSaveSuccess("patient basic-info", patient.getPatientId()));
            }
        } catch (LoadTestDataException e) {
            logPatientError("basic-info", e);
        }
        return actionResult;
    }

    // bean-property
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

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getDialogTitle() {
        return dialogTitle;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setIdentifierPatientID(String identifierPatientID) {
        this.identifierPatientID = identifierPatientID;
    }

    public String getIdentifierPatientID() {
        return identifierPatientID;
    }

    public void setStreet1(String street1) {
        this.street1 = street1;
    }

    public String getStreet1() {
        return street1;
    }

    public void setStreet2(String street2) {
        this.street2 = street2;
    }

    public String getStreet2() {
        return street2;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }

    public void setPostal(String postal) {
        this.postal = postal;
    }

    public String getPostal() {
        return postal;
    }

    public void setPhonenumberValue(String phonenumberValue) {
        this.phonenumberValue = phonenumberValue;
    }

    public String getPhonenumberValue() {
        return phonenumberValue;
    }

    // IDs and isValidId
    private boolean isValidPatientId() {
        return withPatient != null && HelperUtil.isId(withPatient.getPatientId());
    }

    public Long getPatientId() {
        Long localId = 0L;
        if (isValidPatientId()) {
            localId = withPatient.getPatientId();
        }
        return localId;
    }

    private boolean isValidPersonnameId() {
        return withPersonname != null && HelperUtil.isId(withPersonname.getPersonnameId());
    }

    public Long getPersonnameId() {
        Long localId = 0L;
        if (isValidPersonnameId()) {
            localId = withPersonname.getPersonnameId();
        }
        return localId;
    }

    private boolean isValidIdentifierId() {
        return withIdentifier != null && HelperUtil.isId(withIdentifier.getIdentifierId());
    }

    public Long getIdentifierId() {
        Long localId = 0L;
        if (isValidIdentifierId()) {
            localId = withIdentifier.getIdentifierId();
        }
        return localId;
    }

    private boolean isValidAddressId() {
        return withAddress != null && HelperUtil.isId(withAddress.getAddressId());
    }

    public Long getAddressId() {
        Long localId = 0L;
        if (isValidAddressId()) {
            localId = withAddress.getAddressId();
        }
        return localId;
    }

    private boolean isValidPhonenumberId() {
        return withPhonenumber != null && HelperUtil.isId(withPhonenumber.getPhonenumberId());
    }

    public Long getPhonenumberId() {
        if (isValidPhonenumberId()) {
            return withPhonenumber.getPhonenumberId();
        }
        return 0L;
    }

    //read-record
    private Phonenumber readPhonenumber(){
        Phonenumber phonenumber;
        if (isValidPhonenumberId()) {
            phonenumber = loadTestDataService.getPhonenumberBy(getPhonenumberId());
            if (phonenumber != null) {
                phonenumber = updateFromBeanToPhonenumber(phonenumber);
            } else {
                phonenumber = updateFromBeanToPhonenumber(new Phonenumber());
            }
        } else {
            phonenumber = updateFromBeanToPhonenumber(new Phonenumber());
        }
        return phonenumber;
    }

    private Address readAddress() {
        Address address;
        if (isValidAddressId()) {
            address = loadTestDataService.getAddressBy(getAddressId());
            if (address != null) {
                address = updateFromBeanToAddress(address);
            } else {
                address = updateFromBeanToAddress(new Address());
            }
        } else {
            address = updateFromBeanToAddress(new Address());
        }
        return address;
    }

    private Identifier readIdentifier() {
        Identifier identifier;
        if (isValidIdentifierId()) {
            identifier = loadTestDataService.getIdentifierBy(getIdentifierId());
            if (identifier != null) {
                identifier = updateFromBeanToIdentifier(identifier);
            } else {
                identifier = updateFromBeanToIdentifier(new Identifier());
            }
        } else {
            identifier = updateFromBeanToIdentifier(new Identifier());
        }
        return identifier;
    }

    private Patient readPatient() {
        Patient patient;
        if (isValidPatientId()) {
            patient = loadTestDataService.getPatientBy(getPatientId());
            if (patient != null) {
                patient = updateFromBeanToPatient(patient);
            } else {
                patient = updateFromBeanToPatient(new Patient());
            }
        } else {
            patient = updateFromBeanToPatient(new Patient());
        }
        return patient;
    }

    private Personname readPersonname() {
        Personname personname;
        if (isValidPersonnameId()) {
            personname = loadTestDataService.getPersonnameBy(getPersonnameId());
            if (personname != null) {
                personname = updateFromBeanToPersonname(personname);
            } else {
                personname = updateFromBeanToPersonname(new Personname());
            }
        } else {
            personname = updateFromBeanToPersonname(new Personname());
        }
        return personname;
    }

    // bean-entity
    private Patient updateFromBeanToPatient(Patient patient) {
        patient.setDateOfBirth(HelperUtil.toTimestamp(dateOfBirth));
        patient.setGender(gender);
        patient.setSsn(ssn);

        if (CollectionUtils.isEmpty(patient.getPersonnames())) {
            patient.getPersonnames().add(new Personname());
        }
        updateFromBeanToPersonname(patient.getLastPersonname());

        return patient;
    }

    private String updateBeanWithPatient(Patient patientRecord) {
        if (patientRecord != null && HelperUtil.isId(patientRecord.getPatientId())) {
            dialogTitle = "Edit Patient";
            setDateOfBirth(patientRecord.getDateOfBirth());
            setGender(patientRecord.getGender());
            setSsn(patientRecord.getSsn());
            updateBeanWithPersonname(patientRecord.getLastPersonname());
            withPatient = patientRecord;
        } else {
            dialogTitle = "Create Patient";
            setDateOfBirth(null);
            setGender(null);
            setSsn(null);
            updateBeanWithPersonname(null);
            withPatient = null;
        }
        return NavigationConstant.LOAD_TEST_DATA_PAGE;
    }

    private Personname updateFromBeanToPersonname(Personname personname) {
        personname.setFirstName(firstName);
        personname.setLastName(lastName);
        personname.setPrefix(prefix);
        personname.setMiddleName(middleName);
        personname.setSuffix(suffix);

        return personname;
    }

    private String updateBeanWithPersonname(Personname personname){
        if(personname != null){
            setFirstName(personname.getFirstName());
            setLastName(personname.getLastName());
            setPrefix(personname.getPrefix());
            setMiddleName(personname.getMiddleName());
            setSuffix(personname.getSuffix());
            withPersonname = personname;
        }else{
            setFirstName(null);
            setLastName(null);
            setPrefix(null);
            setMiddleName(null);
            setSuffix(null);
            withPersonname = null;
        }

        return NavigationConstant.LOAD_TEST_DATA_PAGE;
    }

    private String updateBeanWithIdentifier(Identifier identifier) {
        if (identifier != null) {
            setIdentifierPatientID(identifier.getId());
            setOrganizationId(identifier.getOrganizationId());
            withIdentifier = identifier;
        } else {
            setIdentifierPatientID(null);
            setOrganizationId(null);
            withIdentifier = null;
        }

        return NavigationConstant.LOAD_TEST_DATA_PAGE;
    }

    private Identifier updateFromBeanToIdentifier(Identifier identifier) {
        identifier.setId(identifierPatientID);
        identifier.setOrganizationId(organizationId);

        return identifier;
    }

    private String updateBeanWithAddress(Address address) {
        if (address != null) {
            setStreet1(address.getStreet1());
            setStreet2(address.getStreet2());
            setCity(address.getCity());
            setPostal(address.getPostal());
            setState(address.getState());
            withAddress = address;
        } else {
            setStreet1(null);
            setStreet2(null);
            setCity(null);
            setPostal(null);
            setState(null);
            withAddress = null;
        }

        return NavigationConstant.LOAD_TEST_DATA_PAGE;
    }

    private Address updateFromBeanToAddress(Address address) {
        address.setStreet1(street1);
        address.setStreet2(street2);
        address.setCity(city);
        address.setState(state);
        address.setPostal(postal);

        return address;
    }

    private String updateBeanWithPhonenumber(Phonenumber phonenumber) {
        if (phonenumber != null) {
            setPhonenumberValue(phonenumber.getValue());
            withPhonenumber = phonenumber;
        } else {
            setPhonenumberValue(null);
            withPhonenumber = null;
        }

        return NavigationConstant.LOAD_TEST_DATA_PAGE;
    }

    private Phonenumber updateFromBeanToPhonenumber(Phonenumber phonenumber) {
        phonenumber.setValue(phonenumberValue);

        return phonenumber;
    }

    // clear input forms
    public String clearInputsAll() {
        clearInputsBasicInfo();
        clearInputsAdditionalNames();
        clearInputsIdentifiers();
        clearInputsAddresses();
        clearInputsPhonenumbers();
        return NavigationConstant.LOAD_TEST_DATA_PAGE;
    }

    public String clearInputsBasicInfo() {
        return updateBeanWithPatient(null);
    }

    public String clearInputsAdditionalNames() {
        return updateBeanWithPersonname(null);
    }

    public String clearInputsIdentifiers() {
        return updateBeanWithIdentifier(null);
    }

    public String clearInputsAddresses() {
        return updateBeanWithAddress(null);
    }

    public String clearInputsPhonenumbers() {
        return updateBeanWithPhonenumber(null);
    }

    private static void logPatientError(String logOf, LoadTestDataException e) {
        FacesContext.getCurrentInstance().validationFailed();
        addPatientErrorMessages(MessageFormat.format("Cannot save patient {0}: {1}", logOf.toLowerCase(), e.getLocalizedMessage()));
        LOG.error("Error save-patient-{0}: {}", logOf.toLowerCase(), e.getLocalizedMessage(), e);
    }

    private static String msgForSaveSuccess(String ofType, Long ofId) {
        return MessageFormat.format("Save {0} successful: {1}", ofType.toLowerCase(), ofId);
    }

    private static String msgForSelectEdit(String ofType) {
        return MessageFormat.format("Select a/an {0} for edit.", ofType.toLowerCase());
    }

    private static String msgForSelectDelete(String ofType) {
        return MessageFormat.format("Select a/an {0} for delete.", ofType.toLowerCase());
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
