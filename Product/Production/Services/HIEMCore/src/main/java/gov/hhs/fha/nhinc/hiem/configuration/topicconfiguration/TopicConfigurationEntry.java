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
package gov.hhs.fha.nhinc.hiem.configuration.topicconfiguration;

/**
 * 
 * @author rayj
 */
public class TopicConfigurationEntry {
    private String topic;
    private String description;
    private boolean isSupported;
    private boolean isPatientCentric;
    private boolean isPatientRequired;
    private String patientIdentifierSubscribeLocation;
    private String patientIdentifierNotifyLocation;
    private String patientIdentifierFormat;

    /**
     * Default Constructor.
     */
    public TopicConfigurationEntry() {
        clear();
    }

    /**
     * Clear the contents of this and set it to a default state.
     */
    public void clear() {
        topic = "";
        isSupported = false;
        isPatientCentric = false;
        isPatientRequired = false;
        patientIdentifierSubscribeLocation = "";
        patientIdentifierNotifyLocation = "";
        patientIdentifierFormat = "";
    }

    /**
     * @return the Topic
     */
    public String getTopic() {
        return topic;
    }

    /**
     * @param Topic the Topic to set
     */
    public void setTopic(String topic) {
        this.topic = topic;
    }

    /**
     * @return the Supported
     */
    public boolean isSupported() {
        return isSupported;
    }

    /**
     * @param Supported the Supported to set
     */
    public void setSupported(boolean isSupported) {
        this.isSupported = isSupported;
    }

    /**
     * @return the PatientCentric
     */
    public boolean isPatientCentric() {
        return isPatientCentric;
    }

    /**
     * @param PatientCentric the PatientCentric to set
     */
    public void setPatientCentric(boolean isPatientCentric) {
        this.isPatientCentric = isPatientCentric;
    }

    /**
     * @return the PatientRequired
     */
    public boolean isPatientRequired() {
        return isPatientRequired;
    }

    /**
     * @param PatientRequired the PatientRequired to set
     */
    public void setPatientRequired(boolean isPatientRequired) {
        this.isPatientRequired = isPatientRequired;
    }

    /**
     * @return the PatientIdentifierSubscribeLocation
     */
    public String getPatientIdentifierSubscribeLocation() {
        return patientIdentifierSubscribeLocation;
    }

    /**
     * @param PatientIdentifierSubscribeLocation the PatientIdentifierSubscribeLocation to set
     */
    public void setPatientIdentifierSubscribeLocation(String patientIdentifierSubscribeLocation) {
        this.patientIdentifierSubscribeLocation = patientIdentifierSubscribeLocation;
    }

    /**
     * @return the PatientIdentifierNotifyLocation
     */
    public String getPatientIdentifierNotifyLocation() {
        return patientIdentifierNotifyLocation;
    }

    /**
     * @param PatientIdentifierNotifyLocation the PatientIdentifierNotifyLocation to set
     */
    public void setPatientIdentifierNotifyLocation(String patientIdentifierNotifyLocation) {
        this.patientIdentifierNotifyLocation = patientIdentifierNotifyLocation;
    }

    /**
     * @return the PatientIdentifierFormat
     */
    public String getPatientIdentifierFormat() {
        return patientIdentifierFormat;
    }

    /**
     * @param PatientIdentifierFormat the PatientIdentifierFormat to set
     */
    public void setPatientIdentifierFormat(String patientIdentifierFormat) {
        this.patientIdentifierFormat = patientIdentifierFormat;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

}
