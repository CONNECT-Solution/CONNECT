/*
 * Copyright (c) 2009-2018, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.patientdb.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;

/**
 *
 *
 *
 * @author richard.ettema
 */
public class Patient implements Serializable {

    private static final long serialVersionUID = -5941897049106149743L;

    /**
     *
     * Attribute patientId.
     */
    private Long patientId;

    /**
     *
     * Attribute dateOfBirth.
     */
    private Timestamp dateOfBirth;

    /**
     *
     * Attribute gender.
     */
    private String gender;

    /**
     *
     * Attribute ssn.
     */
    private String ssn;

    /**
     *
     * List of Addresses
     */
    private List<Address> addresses;

    /**
     *
     * List of Identifiers
     */
    private List<Identifier> identifiers;

    /**
     *
     * List of Personnames
     */
    private List<Personname> personnames;

    /**
     *
     * List of Phonenumbers
     */
    private List<Phonenumber> phonenumbers;

    private int nameIndex = 0;
    private int identifierIndex = 0;

    public Patient() {
        // default-constructor
    }

    public Patient(Patient patient, Personname personname) {
        patient.getPersonnames().add(personname);
        setPatient(patient);
    }

    public Patient(Patient patient) {
        setPatient(patient);
        loadAllLazyObjects(false);
    }

    /**
     *
     * @return patientId
     */
    public Long getPatientId() {

        return patientId;

    }

    /**
     *
     * @param patientId new value for patientId
     */
    public void setPatientId(Long patientId) {

        this.patientId = patientId;

    }

    /**
     *
     * @return dateOfBirth
     */
    public Timestamp getDateOfBirth() {

        return dateOfBirth;

    }

    /**
     *
     * @param dateOfBirth new value for dateOfBirth
     */
    public void setDateOfBirth(Timestamp dateOfBirth) {

        this.dateOfBirth = dateOfBirth;

    }

    /**
     *
     * @return gender
     */
    public String getGender() {

        return gender;

    }

    /**
     *
     * @param gender new value for gender
     */
    public void setGender(String gender) {

        this.gender = gender;

    }

    /**
     *
     * @return ssn
     */
    public String getSsn() {

        return ssn;

    }

    /**
     *
     * @param ssn new value for ssn
     */
    public void setSsn(String ssn) {

        this.ssn = ssn;

    }

    /**
     *
     * Get the list of Addresses
     */
    public List<Address> getAddresses() {

        if (addresses == null) {

            addresses = new ArrayList<>();

        }

        return addresses;

    }

    /**
     *
     * Set the list of Addresses
     */
    public void setAddresses(List<Address> addresses) {

        this.addresses = addresses;

    }

    /**
     *
     * Get the list of Identifiers
     */
    public List<Identifier> getIdentifiers() {

        if (identifiers == null) {

            identifiers = new ArrayList<>();

        }

        return identifiers;

    }

    /**
     *
     * Set the list of Identifiers
     */
    public void setIdentifiers(List<Identifier> identifiers) {

        this.identifiers = identifiers;

    }

    /**
     *
     * Get the list of Personnames
     */
    public List<Personname> getPersonnames() {

        if (personnames == null) {

            personnames = new ArrayList<>();

        }

        return personnames;

    }

    /**
     *
     * Set the list of Personnames
     */
    public void setPersonnames(List<Personname> personnames) {

        this.personnames = personnames;

    }

    /**
     *
     * Get the list of Phonenumbers
     */
    public List<Phonenumber> getPhonenumbers() {

        if (phonenumbers == null) {

            phonenumbers = new ArrayList<>();

        }

        return phonenumbers;

    }

    /**
     *
     * Set the list of Phonenumbers
     */
    public void setPhonenumbers(List<Phonenumber> phonenumbers) {

        this.phonenumbers = phonenumbers;

    }

    @Override
    public String toString() {

        StringBuffer output = new StringBuffer("");

        int counter = 0;

        for (Identifier identifier : getIdentifiers()) {

            output.append("Identifer[").append(counter).append("] = '").append(identifier.getId()).append("^^^&")
                .append(identifier.getOrganizationId()).append("&ISO'; ");

        }

        if (CollectionUtils.isNotEmpty(getPersonnames())) {

            output.append("Personname = '").append(getPersonnames().get(0).getLastName()).append(", ")
                .append(getPersonnames().get(0).getFirstName()).append("'; ");

        }

        output.append("Gender = '").append(gender).append("'; ");

        output.append("DateOfBirth = '").append(dateOfBirth);

        return output.toString();

    }

    // READ-ONLY PROPERITES
    public String getFirstName() {
        if (CollectionUtils.isNotEmpty(getPersonnames())) {
            return getPersonnames().get(nameIndex).getFirstName();
        }
        return "";
    }

    public String getLastName() {
        if (CollectionUtils.isNotEmpty(getPersonnames())) {
            return getPersonnames().get(nameIndex).getLastName();
        }
        return "";
    }

    public String getMiddleName() {
        if (CollectionUtils.isNotEmpty(getPersonnames())) {
            return getPersonnames().get(nameIndex).getMiddleName();
        }
        return "";
    }

    public String getPrefix() {
        if (CollectionUtils.isNotEmpty(getPersonnames())) {
            return getPersonnames().get(nameIndex).getPrefix();
        }
        return "";
    }

    public String getSuffix() {
        if (CollectionUtils.isNotEmpty(getPersonnames())) {
            return getPersonnames().get(nameIndex).getSuffix();
        }
        return "";
    }

    public Personname getLastPersonname() {
        if (CollectionUtils.isNotEmpty(getPersonnames())) {
            return getPersonnames().get(nameIndex);
        }
        return null;
    }

    public Identifier getLastIdentifier() {
        if (CollectionUtils.isNotEmpty(getIdentifiers())) {
            return getIdentifiers().get(identifierIndex);
        }
        return null;
    }

    public String getPatientIdentifier() {
        if (CollectionUtils.isNotEmpty(getIdentifiers()) && CollectionUtils.isNotEmpty(getPersonnames())) {
            return MessageFormat.format("{0} {1} - {2}", getFirstName(), getLastName(), getLastIdentifier().getId());
        }
        return null;
    }

    public String getPatientIdentifierIso() {
        if (CollectionUtils.isNotEmpty(getIdentifiers())) {
            return MessageFormat.format("{0}^^^&{1}&ISO", getLastIdentifier().getId(),
                getLastIdentifier().getOrganizationId());
        }
        return null;
    }

    public String getPatientIdentifierId() {
        if (CollectionUtils.isNotEmpty(getIdentifiers())) {
            return getLastIdentifier().getId();
        }
        return null;
    }

    private void setPatient(Patient patient) {
        addresses = patient.addresses;
        dateOfBirth = patient.dateOfBirth;
        gender = patient.gender;
        identifiers = patient.identifiers;
        patientId = patient.patientId;
        personnames = patient.personnames;
        phonenumbers = patient.phonenumbers;
        ssn = patient.ssn;
    }

    public long[] loadAllLazyObjects(boolean allRecords) {
        nameIndex = CollectionUtils.isNotEmpty(getPersonnames()) ? personnames.size() - 1 : 0;
        identifierIndex = CollectionUtils.isNotEmpty(getIdentifiers()) ? identifiers.size() - 1 : 0;

        return new long[]{patientId, personnames != null ? (long) personnames.size() : 0,
            identifiers != null ? (long) identifiers.size() : 0,
            // Optional
            addresses != null && allRecords ? (long) addresses.size() : 0,
            phonenumbers != null && allRecords ? (long) phonenumbers.size() : 0};
    }

}
