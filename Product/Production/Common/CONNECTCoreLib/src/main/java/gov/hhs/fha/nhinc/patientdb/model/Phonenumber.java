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
public class Phonenumber implements Serializable {

    /**
     * Attribute phonenumberId.
     */
    private Long phonenumberId;

    /**
     * Attribute patient.
     */
    private Patient patient;

    /**
     * Attribute value.
     */
    private String value;


    /**
     * @return phonenumberId
     */
    public Long getPhonenumberId() {
        return phonenumberId;
    }

    /**
     * @param phonenumberId new value for phonenumberId
     */
    public void setPhonenumberId(Long phonenumberId) {
        this.phonenumberId = phonenumberId;
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
     * @return value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value new value for value
     */
    public void setValue(String value) {
        this.value = value;
    }

}