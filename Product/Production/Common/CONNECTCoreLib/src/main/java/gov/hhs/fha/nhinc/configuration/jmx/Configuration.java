/**
 * 
 */
package gov.hhs.fha.nhinc.configuration.jmx;

import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;

/**
 * @author msw
 *
 */
public class Configuration implements ConfigurationMBean {
    
    public Configuration() {
        
    }

    /* (non-Javadoc)
     * @see gov.hhs.fha.nhinc.configuration.Configuration#getProperty(java.lang.String)
     */
    @Override
    public String getProperty(String propertyFileName, String key) {
        String value = null;
        try {
            value = PropertyAccessor.getInstance().getProperty(propertyFileName, key);
        } catch (PropertyAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return value;
    }

    /* (non-Javadoc)
     * @see gov.hhs.fha.nhinc.configuration.Configuration#setProperty(java.lang.String, java.lang.String)
     */
    @Override
    public void setProperty(String propertyFileName, String key, String value) {
        try {
            PropertyAccessor.getInstance().setProperty(propertyFileName, key, value);
        } catch (PropertyAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /* (non-Javadoc)
     * @see gov.hhs.fha.nhinc.configuration.Configuration#persistConfiguration()
     */
    @Override
    public void persistConfiguration() {
        // TODO Auto-generated method stub
        
    }

}
