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
public class Address implements Serializable {

    /**
     * Attribute addressId.
     */
    private Long addressId;

    /**
     * Attribute patient.
     */
    private Patient patient;

    /**
     * Attribute street1.
     */
    private String street1;

    /**
     * Attribute street2.
     */
    private String street2;

    /**
     * Attribute city.
     */
    private String city;

    /**
     * Attribute state.
     */
    private String state;

    /**
     * Attribute postal.
     */
    private String postal;


    /**
     * @return addressId
     */
    public Long getAddressId() {
        return addressId;
    }

    /**
     * @param addressId new value for addressId
     */
    public void setAddressId(Long addressId) {
        this.addressId = addressId;
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
     * @return street1
     */
    public String getStreet1() {
        return street1;
    }

    /**
     * @param street1 new value for street1
     */
    public void setStreet1(String street1) {
        this.street1 = street1;
    }

    /**
     * @return street2
     */
    public String getStreet2() {
        return street2;
    }

    /**
     * @param street2 new value for street2
     */
    public void setStreet2(String street2) {
        this.street2 = street2;
    }

    /**
     * @return city
     */
    public String getCity() {
        return city;
    }

    /**
     * @param city new value for city
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * @return state
     */
    public String getState() {
        return state;
    }

    /**
     * @param state new value for state
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * @return postal
     */
    public String getPostal() {
        return postal;
    }

    /**
     * @param postal new value for postal
     */
    public void setPostal(String postal) {
        this.postal = postal;
    }

}