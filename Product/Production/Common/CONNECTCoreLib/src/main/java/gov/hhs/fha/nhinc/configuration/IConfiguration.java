/**
 * 
 */
package gov.hhs.fha.nhinc.configuration;

/**
 * @author msw
 *
 */
public interface IConfiguration {
    
    public String getProperty(String propertyFileName, String key);
    public void setProperty(String propertyFileName, String key, String value);
    
    public void persistConfiguration();

}
