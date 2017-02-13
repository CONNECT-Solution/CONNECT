/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.admingui.managed.pdmp;

import gov.hhs.fha.nhinc.admingui.event.model.PdmpPatient;
import gov.hhs.fha.nhinc.admingui.event.model.PrescriptionInfo;
import gov.hhs.fha.nhinc.admingui.services.HtmlParserService;
import gov.hhs.fha.nhinc.admingui.services.PdmpService;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.pdmp.AddressRequiredZipType;
import gov.hhs.fha.nhinc.pdmp.PatientType;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author jassmit
 */
@ManagedBean(name = "pdmpBean")
@ViewScoped
@Component
public class PdmpBean {

    @Autowired
    private PdmpService pdmpService;
    @Autowired
    private HtmlParserService htmlParser;
    
    private static final Logger LOG = LoggerFactory.getLogger(PdmpBean.class);

    private String patientFirstName;
    private String patientLastName;
    private Date birthDate;
    private String gender;
    private String phone;
    private String zip;
    
    private Date beginRange;
    private Date endRange;
    
    private List<String> genderList;
    
    private List<PdmpPatient> resultPatients = new ArrayList<>();
    private List<PrescriptionInfo> prescriptionList = new ArrayList<>();
    private String resultMessage;
    private boolean patientFound;
    private boolean prescriptionsFound;
    
    private int activeIndex = 0;
    
    public PdmpBean() {
        populateGenderList();
    }

    public void searchForPatient() {
        PatientType patient = new PatientType();
        PatientType.Name name = new PatientType.Name();
        name.setFirst(patientFirstName);
        name.setLast(patientLastName);

        gov.hhs.fha.nhinc.pdmp.ObjectFactory of = new gov.hhs.fha.nhinc.pdmp.ObjectFactory();
        patient.getContent().add(of.createPatientTypeName(name));
        patient.getContent().add(of.createPatientTypeBirthdate(pdmpService.getGregorianCalendar(birthDate)));
        patient.getContent().add(of.createPatientTypeSexCode(pdmpService.getSexCodeType(gender)));
        AddressRequiredZipType address = new AddressRequiredZipType();
        address.setZipCode(zip);
        patient.getContent().add(of.createPatientTypeAddress(address));
        patient.getContent().add(of.createPatientTypePhone(new BigInteger(phone)));
        
        PdmpPatient patientResult = pdmpService.searchForPdmpInfo(patient, pdmpService.buildDateRange(beginRange, endRange));
        
        if(patientResult != null) {
            patientFound = true;
            patientResult.setFirstName(patientFirstName);
            patientResult.setLastName(patientLastName);
            patientResult.setDateOfBirth(birthDate);
            patientResult.setGender(gender);
            patientResult.setPhone(phone);
            patientResult.setZip(zip);
            resultPatients.add(patientResult);
            
            if(NullChecker.isNotNullish(patientResult.getReportUrl())) {
                try {
                    prescriptionList = htmlParser.getAllPrescriptions(patientResult.getReportUrl());
                    prescriptionsFound = true;
                } catch (IOException ex) {
                    LOG.warn("Unable to get prescriptions from report {}", patientResult.getReportUrl(), ex);
                }
            }
        }
    }
    
    public void clearValues() {
        patientLastName = patientFirstName = gender = null;
        beginRange = endRange = birthDate = null;
        resultPatients.clear();
        patientFound = prescriptionsFound = false;
        prescriptionList.clear();
    }

    public String getPatientFirstName() {
        return patientFirstName;
    }

    public void setPatientFirstName(String patientFirstName) {
        this.patientFirstName = patientFirstName;
    }

    public String getPatientLastName() {
        return patientLastName;
    }

    public void setPatientLastName(String patientLastName) {
        this.patientLastName = patientLastName;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getBeginRange() {
        return beginRange;
    }

    public void setBeginRange(Date beginRange) {
        this.beginRange = beginRange;
    }

    public Date getEndRange() {
        return endRange;
    }

    public void setEndRange(Date endRange) {
        this.endRange = endRange;
    }

    public List<String> getGenderList() {
        return genderList;
    }

    public void setGenderList(List<String> genderList) {
        this.genderList = genderList;
    }

    public List<PdmpPatient> getResultPatients() {
        return resultPatients;
    }

    public void setResultPatients(List<PdmpPatient> resultPatients) {
        this.resultPatients = resultPatients;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
    }

    public boolean isPatientFound() {
        return patientFound;
    }

    public void setPatientFound(boolean patientFound) {
        this.patientFound = patientFound;
    }

    public int getActiveIndex() {
        return activeIndex;
    }

    public void setActiveIndex(int activeIndex) {
        this.activeIndex = activeIndex;
    }

    public List<PrescriptionInfo> getPrescriptionList() {
        return prescriptionList;
    }

    public void setPrescriptionList(List<PrescriptionInfo> prescriptionList) {
        this.prescriptionList = prescriptionList;
    }

    public boolean isPrescriptionsFound() {
        return prescriptionsFound;
    }

    public void setPrescriptionsFound(boolean prescriptionsFound) {
        this.prescriptionsFound = prescriptionsFound;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }
    
    private void populateGenderList() {
        genderList = new ArrayList<>();
        genderList.add("M");
        genderList.add("F");
        genderList.add("U");
    }

}
