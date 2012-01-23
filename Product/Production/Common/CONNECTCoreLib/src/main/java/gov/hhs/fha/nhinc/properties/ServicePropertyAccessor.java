package gov.hhs.fha.nhinc.properties;

public interface ServicePropertyAccessor {

	
	 /**
     * Checks to see if a specific Service is enabled.
     * @return Returns true if a specified  Service is enabled in the properties file.
     */
	boolean isServiceEnabled();

	 /**
     * Checks to see if the query should  be handled internally or passed through to an adapter.
     * @return Returns true if the pass through property for a specified Service is true.
     */
	boolean isInPassThroughMode();

}
