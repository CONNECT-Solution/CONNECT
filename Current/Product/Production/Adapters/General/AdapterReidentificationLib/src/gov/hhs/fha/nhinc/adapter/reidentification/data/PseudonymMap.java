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
