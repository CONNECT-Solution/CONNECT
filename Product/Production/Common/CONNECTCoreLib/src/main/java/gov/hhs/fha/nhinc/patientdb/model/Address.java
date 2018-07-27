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

/**
 *
 *
 *
 * @author richard.ettema
 */
public class Address implements Serializable {

    private static final long serialVersionUID = -5639606167157785176L;

    /**
     *
     * Attribute addressId.
     */
    private Long addressId;

    /**
     *
     * Attribute patient.
     */
    private Patient patient;

    /**
     *
     * Attribute street1.
     */
    private String street1;

    /**
     *
     * Attribute street2.
     */
    private String street2;

    /**
     *
     * Attribute city.
     */
    private String city;

    /**
     *
     * Attribute state.
     */
    private String state;

    /**
     *
     * Attribute postal.
     */
    private String postal;

    /**
     *
     * @return addressId
     */
    public Long getAddressId() {

        return addressId;

    }

    /**
     *
     * @param addressId new value for addressId
     */
    public void setAddressId(Long addressId) {

        this.addressId = addressId;

    }

    /**
     *
     * @return patient
     */
    public Patient getPatient() {

        if (this.patient == null) {

            this.patient = new Patient();

        }

        return patient;

    }

    /**
     *
     * @param patient new value for patient
     */
    public void setPatient(Patient patient) {

        this.patient = patient;

    }

    /**
     *
     * @return street1
     */
    public String getStreet1() {

        return street1;

    }

    /**
     *
     * @param street1 new value for street1
     */
    public void setStreet1(String street1) {

        this.street1 = street1;

    }

    /**
     *
     * @return street2
     */
    public String getStreet2() {

        return street2;

    }

    /**
     *
     * @param street2 new value for street2
     */
    public void setStreet2(String street2) {

        this.street2 = street2;

    }

    /**
     *
     * @return city
     */
    public String getCity() {

        return city;

    }

    /**
     *
     * @param city new value for city
     */
    public void setCity(String city) {

        this.city = city;

    }

    /**
     *
     * @return state
     */
    public String getState() {

        return state;

    }

    /**
     *
     * @param state new value for state
     */
    public void setState(String state) {

        this.state = state;

    }

    /**
     *
     * @return postal
     */
    public String getPostal() {

        return postal;

    }

    /**
     *
     * @param postal new value for postal
     */
    public void setPostal(String postal) {

        this.postal = postal;

    }

}
