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
package gov.hhs.fha.nhinc.admingui.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * POJO class used by the UI to render the data
 *
 * @author Naresh Subramanyan
 */
public class Patient {

    // used in the UI
    private int patientIndex;

    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private String gender;
    private String ssn;
    private String organization;
    private String organizationName;
    private String patientId;
    // Not sure if we need these fields, Remove it if we don't
    // need it
    private String middleName;
    private String streetname;
    private String city;
    private String state;
    private String zip;
    private String phone;
    private String drivinglicense;
    private String assigningAuthorityId;
    private String domain;
    private String correlation = "No Correlation";

    private List<Document> documentList;

    public Patient() {
        documentList = new ArrayList<>();
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
     * @return the dateOfBirth
     */
    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * @param dateOfBirth the dateOfBirth to set
     */
    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
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
     * @return the SSN
     */
    public String getSSN() {
        return ssn;
    }

    /**
     * @param ssn the SSN to set
     */
    public void setSSN(String ssn) {
        this.ssn = ssn;
    }

    /**
     * @return the organization
     */
    public String getOrganization() {
        return organization;
    }

    /**
     * @param organization the organization to set
     */
    public void setOrganization(String organization) {
        this.organization = organization;
        // find if the organization name is available
        organizationName = organization;
    }

    /**
     * @return the name
     */
    public String getName() {
        return firstName + " " + lastName;
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

    public void clearAll() {
        // clear the patient info

        // clear the document info
    }

    /**
     * @return the documentList
     */
    public List<Document> getDocumentList() {
        return documentList;
    }

    /**
     * @param documentList the documentList to set
     */
    public void setDocumentList(List<Document> documentList) {
        this.documentList = documentList;
    }

    /**
     * @return the organizationName
     */
    public String getOrganizationName() {
        return organizationName;
    }

    /**
     *
     * @param organizationName
     */
    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    /**
     * @return the assigningAuthorityId
     */
    public String getAssigningAuthorityId() {
        return assigningAuthorityId;
    }

    /**
     * @param assigningAuthorityId the assigningAuthorityId to set
     */
    public void setAssigningAuthorityId(String assigningAuthorityId) {
        this.assigningAuthorityId = assigningAuthorityId;
    }

    /**
     * @return the patientIndex
     */
    public int getPatientIndex() {
        return patientIndex;
    }

    /**
     * @param patientIndex the patientIndex to set
     */
    public void setPatientIndex(int patientIndex) {
        this.patientIndex = patientIndex;
    }

    /**
     * @return the drivinglicense
     */
    public String getDrivinglicense() {
        return drivinglicense;
    }

    /**
     * @param drivinglicense the drivinglicense to set
     */
    public void setDrivinglicense(String drivinglicense) {
        this.drivinglicense = drivinglicense;
    }

    /**
     * @return the middleName
     */
    public String getMiddleName() {
        return middleName;
    }

    /**
     * @param middleName the middleName to set
     */
    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    /**
     * @return the streetname
     */
    public String getStreetname() {
        return streetname;
    }

    /**
     * @param streetname the streetname to set
     */
    public void setStreetname(String streetname) {
        this.streetname = streetname;
    }

    /**
     * @return the city
     */
    public String getCity() {
        return city;
    }

    /**
     * @param city the city to set
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * @return the state
     */
    public String getState() {
        return state;
    }

    /**
     * @param state the state to set
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * @return the zip
     */
    public String getZip() {
        return zip;
    }

    /**
     * @param zip the zip to set
     */
    public void setZip(String zip) {
        this.zip = zip;
    }

    /**
     * @return the phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone the phone to set
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * @return the domain
     */
    public String getDomain() {
        return domain;
    }

    /**
     * @param domain the domain to set
     */
    public void setDomain(String domain) {
        this.domain = domain;
    }

    public void setCorrelation(String correlation) {
        this.correlation = correlation;
    }
    
    public String getCorrelation() {
        return correlation;
    }

}
