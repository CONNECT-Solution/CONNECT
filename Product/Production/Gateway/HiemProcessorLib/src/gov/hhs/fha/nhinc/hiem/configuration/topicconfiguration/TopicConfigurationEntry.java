/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
    public TopicConfigurationEntry()
    {
        clear();
    }

    /**
     * Clear the contents of this and set it to a default state.
     */
    public void clear()
    {
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
