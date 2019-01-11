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


/**
 *
 * @author dunnek
 */
public class Address {
    private String street1 = "";
    private String street2 = "";
    private String city = "";
    private String state = "";
    private String zip = "";
    private String country = "";

    /**
     * @return the Street1 value from this Address
     */
    public String getStreet1() {
        return street1;
    }

    /**
     * @param value the Street1 value for this Address
     */
    public void setStreet1(String value) {
        street1 = value;
    }

    /**
     * @param value the Street2 value for this Address
     */
    public void setStreet2(String value) {
        street2 = value;
    }

    /**
     * @return the Street2 value for Address
     */
    public String getStreet2() {
        return street2;
    }

    /**
     * @param value the City value for this Address
     */
    public void setCity(String value) {
        city = value;
    }

    /**
     * @return the City value for this Address
     */
    public String getCity() {
        return city;
    }

    /**
     * @param value the State value for this Address
     */
    public void setState(String value) {
        state = value;
    }

    /**
     * @return the Sate value for this Address
     */
    public String getState() {
        return state;
    }

    /**
     * @param value the Zip value for this Address
     */
    public void setZip(String value) {
        zip = value;
    }

    /**
     * @return the Zip value for this Address
     */
    public String getZip() {
        return zip;
    }

    /**
     * @return the country
     */
    public String getCountry() {
        return country;
    }

    /**
     * @param country the country to set
     */
    public void setCountry(String country) {
        this.country = country;
    }


}
