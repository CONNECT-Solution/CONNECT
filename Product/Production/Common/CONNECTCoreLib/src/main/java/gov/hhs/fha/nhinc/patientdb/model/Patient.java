/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2011(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
package gov.hhs.fha.nhinc.patientdb.model;

import java.util.List;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;

/**
 *
 * @author richard.ettema
 */
public class Patient implements Serializable {

    /**
     * Attribute patientId.
     */
    private Long patientId;
    /**
     * Attribute dateOfBirth.
     */
    private Timestamp dateOfBirth;
    /**
     * Attribute gender.
     */
    private String gender;
    /**
     * Attribute ssn.
     */
    private String ssn;
    /**
     * List of Addresses
     */
    private List<Address> addresses = null;
    /**
     * List of Identifiers
     */
    private List<Identifier> identifiers = null;
    /**
     * List of Personnames
     */
    private List<Personname> personnames = null;
    /**
     * List of Phonenumbers
     */
    private List<Phonenumber> phonenumbers = null;

    /**
     * @return patientId
     */
    public Long getPatientId() {
        return patientId;
    }

    /**
     * @param patientId new value for patientId
     */
    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    /**
     * @return dateOfBirth
     */
    public Timestamp getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * @param dateOfBirth new value for dateOfBirth
     */
    public void setDateOfBirth(Timestamp dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    /**
     * @return gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * @param gender new value for gender
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * @return ssn
     */
    public String getSsn() {
        return ssn;
    }

    /**
     * @param ssn new value for ssn
     */
    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    /**
     * Get the list of Addresses
     */
    public List<Address> getAddresses() {
        if (this.addresses == null) {
            this.addresses = new ArrayList<Address>();
        }
        return this.addresses;
    }

    /**
     * Set the list of Addresses
     */
    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    /**
     * Get the list of Identifiers
     */
    public List<Identifier> getIdentifiers() {
        if (this.identifiers == null) {
            this.identifiers = new ArrayList<Identifier>();
        }
        return this.identifiers;
    }

    /**
     * Set the list of Identifiers
     */
    public void setIdentifiers(List<Identifier> identifiers) {
        this.identifiers = identifiers;
    }

    /**
     * Get the list of Personnames
     */
    public List<Personname> getPersonnames() {
        if (this.personnames == null) {
            this.personnames = new ArrayList<Personname>();
        }
        return this.personnames;
    }

    /**
     * Set the list of Personnames
     */
    public void setPersonnames(List<Personname> personnames) {
        this.personnames = personnames;
    }

    /**
     * Get the list of Phonenumbers
     */
    public List<Phonenumber> getPhonenumbers() {
        if (this.phonenumbers == null) {
            this.phonenumbers = new ArrayList<Phonenumber>();
        }
        return this.phonenumbers;
    }

    /**
     * Set the list of Phonenumbers
     */
    public void setPhonenumbers(List<Phonenumber> phonenumbers) {
        this.phonenumbers = phonenumbers;
    }

    @Override
    public String toString() {
        StringBuffer output = new StringBuffer("");

        int counter = 0;
        for (Identifier identifier : this.getIdentifiers()) {
            output.append("Identifer[").append(counter).append("] = '").append(identifier.getId()).append("^^^&").append(identifier.getOrganizationId()).append("&ISO'; ");
        }

        if (this.getPersonnames().size() > 0) {
            output.append("Personname = '").append(this.getPersonnames().get(0).getLastName()).append(", ").append(this.getPersonnames().get(0).getFirstName()).append("'; ");
        }

        output.append("Gender = '").append(this.gender).append("'; ");
        output.append("DateOfBirth = '").append(this.dateOfBirth);

        return output.toString();
    }
}
