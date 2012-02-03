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
package gov.hhs.fha.nhinc.adapter.reidentification.data;

/**
 * This represents one mapping between a pseudonym patient identifier and its 
 * corresponding real patient identifier.
 */
public class PseudonymMap {

    /** 
     * This represents the assigning authority that is associated with the 
     * pseudonym patient ID.
     */
    String pseudonymPatientIdAssigningAuthority;
    /**
     * This represents the pseudonym patient ID.
     */
    String pseudonymPatientId;
    /**
     * This represents the real assigning authority associated with the 
     * real patient ID.
     */
    String realPatientIdAssigningAuthority;
    /**
     * This represents the real patient ID.
     */
    String realPatientId;

    /**
     * This retrieves the assigning authority that is associated with the 
     * pseudonym patient ID.
     */
    public String getPseudonymPatientIdAssigningAuthority() {
        return pseudonymPatientIdAssigningAuthority;
    }

    /**
     * This sets the assigning authority that is associated with the 
     * pseudonym patient ID.
     */
    public void setPseudonymPatientIdAssigningAuthority(String pseudonymPatientIdAssigningAuthority) {
        this.pseudonymPatientIdAssigningAuthority = pseudonymPatientIdAssigningAuthority;
    }

    /**
     * This retrieves the pseudonym patient ID.
     */
    public String getPseudonymPatientId() {
        return pseudonymPatientId;
    }

    /**
     * This sets the pseudonym patient ID.
     */
    public void setPseudonymPatientId(String pseudonymPatientId) {
        this.pseudonymPatientId = pseudonymPatientId;
    }

    /**
     * This retrieves the real assigning authority associated with the real 
     * patient ID.
     */
    public String getRealPatientIdAssigningAuthority() {
        return realPatientIdAssigningAuthority;
    }

    /**
     * This sets the real assigning authority associated with the real 
     * patient ID.
     */
    public void setRealPatientIdAssigningAuthority(String realPatientIdAssigningAuthority) {
        this.realPatientIdAssigningAuthority = realPatientIdAssigningAuthority;
    }

    /**
     * This retrieves the real patient ID.
     */
    public String getRealPatientId() {
        return realPatientId;
    }

    /**
     * This sets the real patient ID.
     */
    public void setRealPatientId(String realPatientId) {
        this.realPatientId = realPatientId;
    }
    
    /**
     * Override to provide information on the settings of datamember values
     */
    public String toString(){
        StringBuilder builder = new StringBuilder();
        builder.append("pseudonymPatientIdAssigningAuthority = " 
                + pseudonymPatientIdAssigningAuthority);
        builder.append(", pseudonymPatientId = "
                + pseudonymPatientId);
        builder.append(", realPatientIdAssigningAuthority = " 
                + realPatientIdAssigningAuthority);
        builder.append(", realPatientId = "
                + realPatientId);
        return builder.toString();
    }
}
