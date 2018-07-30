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
public class Personname implements Serializable {

    private static final long serialVersionUID = -6505174634226051668L;

    /**
     *
     * Attribute personnameId.
     */
    private Long personnameId;

    /**
     *
     * Attribute patient.
     */
    private Patient patient;

    /**
     *
     * Attribute prefix.
     */
    private String prefix;

    /**
     *
     * Attribute firstName.
     */
    private String firstName;

    /**
     *
     * Attribute middleName.
     */
    private String middleName;

    /**
     *
     * Attribute lastName.
     */
    private String lastName;

    /**
     *
     * Attribute suffix.
     */
    private String suffix;

    /**
     *
     * @return personnameId
     */
    public Long getPersonnameId() {

        return personnameId;

    }

    /**
     *
     * @param personnameId new value for personnameId
     */
    public void setPersonnameId(Long personnameId) {

        this.personnameId = personnameId;

    }

    /**
     *
     * @return patient
     */
    public Patient getPatient() {

        if (patient == null) {

            patient = new Patient();

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
     * @return prefix
     */
    public String getPrefix() {

        return prefix;

    }

    /**
     *
     * @param prefix new value for prefix
     */
    public void setPrefix(String prefix) {

        this.prefix = prefix;

    }

    /**
     *
     * @return firstName
     */
    public String getFirstName() {

        return firstName;

    }

    /**
     *
     * @param firstName new value for firstName
     */
    public void setFirstName(String firstName) {

        this.firstName = firstName;

    }

    /**
     *
     * @return middleName
     */
    public String getMiddleName() {

        return middleName;

    }

    /**
     *
     * @param middleName new value for middleName
     */
    public void setMiddleName(String middleName) {

        this.middleName = middleName;

    }

    /**
     *
     * @return lastName
     */
    public String getLastName() {

        return lastName;

    }

    /**
     *
     * @param lastName new value for lastName
     */
    public void setLastName(String lastName) {

        this.lastName = lastName;

    }

    /**
     *
     * @return suffix
     */
    public String getSuffix() {

        return suffix;

    }

    /**
     *
     * @param suffix new value for suffix
     */
    public void setSuffix(String suffix) {

        this.suffix = suffix;

    }

}
