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
package gov.hhs.fha.nhinc.mpilib;

import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author rayj
 */
public class PersonName implements java.io.Serializable {
    private static final Logger LOG = LoggerFactory.getLogger(PersonName.class);
    private String lastName = "";
    private String firstName = "";
    private String middleName = "";
    private String title = "";
    private String suffix = "";

    /**
     * Public constructor for PersonName.
     */
    public PersonName() {
        LOG.debug("PersonName Initialized");
    }

    /**
     * Public constructor for PersonName, taking in last name and first name as paramaters.
     * @param lastname the Person's last name
     * @param firstname the Person's first name
     */
    public PersonName(String lastname, String firstname) {
        this.setLastName(lastname);
        this.setFirstName(firstname);
    }

    /**
     * @return the middle name value for this PersonName
     */
    public String getMiddleName() {
        if (middleName == null) {
            middleName = "";
        }
        return middleName;
    }

    /**
     * @return the middle initial for the middle name of this PersonName
     */
    public String getMiddleInitial() {
        if (middleName == null) {
            middleName = "";
        }
        return middleName.substring(0, 0);
    }

    /**
     * @param middle the middle name value for this PersonName
     */
    public void setMiddleName(String middle) {
        middleName = middle;
    }

    /**
     * @return the last name value of this PersonName
     */
    public String getLastName() {
        if (lastName == null) {
            lastName = "";
        }
        return lastName;
    }

    /**
     * @param lastName the last name to be associated with this PersonName
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return the first name value associated with this PersonName
     */
    public String getFirstName() {
        if (firstName == null) {
            firstName = "";
        }
        return firstName;
    }

    /**
     * @param firstName the first name value to be associated with this PersonName
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @param title the title to be associated with this PersonName
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the title associated with this PersonName
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param value the suffix to be associated with this PersonName
     */
    public void setSuffix(String value) {
        this.suffix = value;
    }

    /**
     * @return the suffix to be associated with for this PersonName
     */
    public String getSuffix() {
        return suffix;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        String result;
        result = lastName + ", " + firstName;
        return result;
    }

    /**
     * @return true if LastName and FirstName have both been set with actual values. False otherwise.
     */
    public boolean isValid() {
        return NullChecker.isNotNullish(getFirstName()) && NullChecker.isNotNullish(getLastName());
    }
}
