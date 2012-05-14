/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services. 
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
package gov.hhs.fha.nhinc.mpilib;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author rayj
 */
public class Patient implements java.io.Serializable {
    static final long serialVersionUID = 449060013287108229L;
    private String dateOfBirth = null;
    private String gender = "";
    private String ssn = "";
    private PersonNames names = new PersonNames();
    private Identifiers patientIdentifiers = new Identifiers();
    private Addresses adds = new Addresses();
    private PhoneNumbers phoneNumbers = new PhoneNumbers();
    private boolean optedIn = true;

    public Patient() {
    }

    public boolean isOptedIn() {
        return optedIn;
    }

    public void setOptedIn(boolean optedIn) {
        this.optedIn = optedIn;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String newVal) {
        this.dateOfBirth = newVal;
    }

    public void setPhoneNumbers(PhoneNumbers val) {
        this.phoneNumbers = val;
    }

    public PhoneNumbers getPhoneNumbers() {
        return phoneNumbers;
    }

    public Identifiers getIdentifiers() {
        return patientIdentifiers;
    }

    public void setIdentifiers(Identifiers newVal) {
        this.patientIdentifiers = newVal;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String newVal) {
        this.gender = newVal;
    }

    public String getSSN() {
        return ssn;
    }

    public void setSSN(String val) {
        this.ssn = val;
    }

    public Addresses getAddresses() {
        return adds;
    }

    public void setAddresses(Addresses val) {
        this.adds = val;
    }

    public void setNames(PersonNames newVal) {
        this.names = newVal;
    }

    public PersonNames getNames() {
        return names;
    }

    public String toString() {
        String result = "";

        if (this.names.size() > 0) {

            for (PersonName personName : this.names) {
                result += "|" + personName.toString();
            }

            result.replaceFirst("|", "");
        } else {
            //result = this.name.toString();
        }
        return result;
    }
}
