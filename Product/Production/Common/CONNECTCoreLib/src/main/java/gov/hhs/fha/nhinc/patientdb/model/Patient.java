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
    private List<Address> addressess = null;

    /**
     * List of Identifiers
     */
    private List<Identifier> identifierss = null;

    /**
     * List of Personnames
     */
    private List<Personname> personnamess = null;

    /**
     * List of Phonenumbers
     */
    private List<Phonenumber> phonenumberss = null;


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
     public List<Address> getAddressess() {
        return this.addressess;
     }

    /**
     * Set the list of Addresses
     */
     public void setAddressess(List<Address> addressess) {
        this.addressess = addressess;
     }
    /**
     * Get the list of Identifiers
     */
     public List<Identifier> getIdentifierss() {
        return this.identifierss;
     }

    /**
     * Set the list of Identifiers
     */
     public void setIdentifierss(List<Identifier> identifierss) {
        this.identifierss = identifierss;
     }
    /**
     * Get the list of Personnames
     */
     public List<Personname> getPersonnamess() {
        return this.personnamess;
     }

    /**
     * Set the list of Personnames
     */
     public void setPersonnamess(List<Personname> personnamess) {
        this.personnamess = personnamess;
     }
    /**
     * Get the list of Phonenumbers
     */
     public List<Phonenumber> getPhonenumberss() {
        return this.phonenumberss;
     }

    /**
     * Set the list of Phonenumbers
     */
     public void setPhonenumberss(List<Phonenumber> phonenumberss) {
        this.phonenumberss = phonenumberss;
     }


}