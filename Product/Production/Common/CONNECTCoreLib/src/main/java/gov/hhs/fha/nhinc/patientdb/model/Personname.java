/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2011(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
package gov.hhs.fha.nhinc.patientdb.model;

import java.io.Serializable;

/**
 *
 * @author richard.ettema
 */
public class Personname implements Serializable {

    /**
     * Attribute personnameId.
     */
    private Long personnameId;

    /**
     * Attribute patient.
     */
    private Patient patient;

    /**
     * Attribute prefix.
     */
    private String prefix;

    /**
     * Attribute firstName.
     */
    private String firstName;

    /**
     * Attribute middleName.
     */
    private String middleName;

    /**
     * Attribute lastName.
     */
    private String lastName;

    /**
     * Attribute suffix.
     */
    private String suffix;


    /**
     * @return personnameId
     */
    public Long getPersonnameId() {
        return personnameId;
    }

    /**
     * @param personnameId new value for personnameId
     */
    public void setPersonnameId(Long personnameId) {
        this.personnameId = personnameId;
    }

    /**
     * @return patient
     */
    public Patient getPatient() {
        if (this.patient == null) {
            this.patient = new Patient();
        }
        return patient;
    }

    /**
     * @param patient new value for patient
     */
    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    /**
     * @return prefix
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * @param prefix new value for prefix
     */
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    /**
     * @return firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName new value for firstName
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return middleName
     */
    public String getMiddleName() {
        return middleName;
    }

    /**
     * @param middleName new value for middleName
     */
    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    /**
     * @return lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName new value for lastName
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return suffix
     */
    public String getSuffix() {
        return suffix;
    }

    /**
     * @param suffix new value for suffix
     */
    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

}