/**
 *
 */
package gov.hhs.fha.nhinc.test;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Loads up the testing context for Spring, and sets the nhinc properties directory to the test folder. </br>
 * Hibernate will try and load up the config files from "/CONNECTCoreLib/src/test/resources/config/hibernate". These
 * should match the general setup, with the exception of changing the JNDI name to use a JDBC connection.
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/config/CONNECT-testcontext.xml" })
public abstract class DAOIntegrationTest {
    {
        // We need to set this property so the PropertyAccessor class doesnt complain and error out.
        System.setProperty("nhinc.properties.dir", System.getProperty("user.dir") + "/src/test/resources/");
    }

}
