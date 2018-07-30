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
public class Phonenumber implements Serializable {

    private static final long serialVersionUID = -8179798064168982080L;

    /**
     *
     * Attribute phonenumberId.
     */
    private Long phonenumberId;

    /**
     *
     * Attribute patient.
     */
    private Patient patient;

    /**
     *
     * Attribute value.
     */
    private String value;

    /**
     *
     * @return phonenumberId
     */
    public Long getPhonenumberId() {

        return phonenumberId;

    }

    /**
     *
     * @param phonenumberId new value for phonenumberId
     */
    public void setPhonenumberId(Long phonenumberId) {

        this.phonenumberId = phonenumberId;

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
     * @return value
     */
    public String getValue() {

        return value;

    }

    /**
     *
     * @param value new value for value
     */
    public void setValue(String value) {

        this.value = value;

    }

}
