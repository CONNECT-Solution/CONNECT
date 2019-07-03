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
package gov.hhs.fha.nhinc.patientdb.model;

import gov.hhs.fha.nhinc.common.loadtestdatamanagement.PersonNameType;
import gov.hhs.fha.nhinc.util.CoreHelpUtils;
import java.io.Serializable;

/**
 *
 *
 *
 * @author richard.ettema
 */
public class Personname implements Serializable {

    private static final long serialVersionUID = -6505174634226051668L;

    private Long personnameId;
    private Patient patient;
    private String prefix;
    private String firstName;
    private String middleName;
    private String lastName;
    private String suffix;

    public Personname() {
    }

    public Personname(PersonNameType personname, Patient patient) {
        this.patient = patient;
        personnameId = CoreHelpUtils.isId(personname.getPersonNameId()) ? personname.getPersonNameId() : null;
        prefix = personname.getPrefix();
        firstName = personname.getFirstName();
        middleName = personname.getMiddleName();
        lastName = personname.getLastName();
        suffix = personname.getSuffix();
    }

    public PersonNameType getPersonNameType() {
        PersonNameType build = new PersonNameType();
        build.setPatientId(patient.getPatientId());
        build.setPersonNameId(personnameId);
        build.setPrefix(prefix);
        build.setFirstName(firstName);
        build.setMiddleName(middleName);
        build.setLastName(lastName);
        build.setSuffix(suffix);
        return build;
    }

    public Long getPersonnameId() {
        return personnameId;
    }

    public void setPersonnameId(Long personnameId) {
        this.personnameId = personnameId;
    }

    public Patient getPatient() {
        if (patient == null) {
            patient = new Patient();
        }
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

}
