/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.universalclientgui.objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PatientSearchData {

    private String assigningAuthorityID;
    private String lastName;
    private String firstName;
    private String patientId;
    private String ssn;
    private String gender;
    private String dob;

    public PatientSearchData() {
    }

    /**
     * @return the assigningAuthorityID
     */
    public String getAssigningAuthorityID() {
        return assigningAuthorityID;
    }

    /**
     * @param assigningAuthorityID the assigningAuthorityID to set
     */
    public void setAssigningAuthorityID(String assigningAuthorityID) {
        this.assigningAuthorityID = assigningAuthorityID;
    }

    /**
     * @return the lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return the patientId
     */
    public String getPatientId() {
        return patientId;
    }

    /**
     * @param patientId the patientId to set
     */
    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    /**
     * @return the ssn
     */
    public String getSsn() {
        return ssn;
    }

    /**
     * @param ssn the ssn to set
     */
    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    /**
     * @return the gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * @param gender the gender to set
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * @return the dob
     */
    public String getDob() {
        return dob;
    }

    /**
     * @param dob the dob to set
     */
    public void setDob(String dob) {
        this.dob = dob;
    }

    
}
