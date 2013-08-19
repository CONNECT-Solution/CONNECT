/**
 * 
 */
package gov.hhs.fha.nhinc.patientcorrelation.nhinc.parsers.helpers;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * @author achidambaram
 *
 */
public class ConfigurationTest {    
    @Test
    public void testgetMyCommunityId() {
        Configuration config = new Configuration();
        assertEquals(config.getMyCommunityId(),"1.1");
    }

}
