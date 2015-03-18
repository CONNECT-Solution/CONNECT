
package gov.hhs.fha.nhinc.admingui.client.fhir;

/**
 * Enumeration for preferred FHIR resource formats.
 * 
 *
 */
public enum ResourceFormat {
	
    RESOURCE_XML("application/xml+fhir"),
    RESOURCE_JSON("application/json+fhir");

	
	private String header;
	
	private ResourceFormat(String header) {
		this.header = header;
	}
	
	public String getHeader() {
		return this.header;
	}

}
