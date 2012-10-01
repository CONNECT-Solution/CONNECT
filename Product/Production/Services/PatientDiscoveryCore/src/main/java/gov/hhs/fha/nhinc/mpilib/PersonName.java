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

import gov.hhs.fha.nhinc.nhinclib.NullChecker;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author rayj
 */
public class PersonName implements java.io.Serializable {
    private static Log log = LogFactory.getLog(PersonName.class);
    private String lastName = "";
    private String firstName = "";
    private String middleName = "";
    private String title = "";
    private String suffix = "";

    public PersonName() {
        log.debug("PersonName Initialized");
    }

    public PersonName(String lastname, String firstname) {
        this.setLastName(lastname);
        this.setFirstName(firstname);
    }

    public String getMiddleName() {
        if (middleName == null) {
            middleName = "";
        }
        return middleName;
    }

    public String getMiddleInitial() {
        if (middleName == null) {
            middleName = "";
        }
        return middleName.substring(0, 0);
    }

    public void setMiddleName(String middle) {
        middleName = middle;
    }

    public String getLastName() {
        if (lastName == null) {
            lastName = "";
        }
        return lastName;
    }

    public void setLastName(String LastName) {
        this.lastName = LastName;
    }

    public String getFirstName() {
        if (firstName == null) {
            firstName = "";
        }
        return firstName;
    }

    public void setFirstName(String FirstName) {
        this.firstName = FirstName;
    }

    public void setTitle(String value) {
        this.title = value;
    }

    public String getTitle() {
        return title;
    }

    public void setSuffix(String value) {
        this.suffix = value;
    }

    public String getSuffix() {
        return suffix;
    }

    public String toString() {
        String result = "";
        result = lastName + ", " + firstName;

        return result;
    }
    
    public boolean isValid() {
        return NullChecker.isNotNullish(getFirstName()) && NullChecker.isNotNullish(getLastName());
    }
}
