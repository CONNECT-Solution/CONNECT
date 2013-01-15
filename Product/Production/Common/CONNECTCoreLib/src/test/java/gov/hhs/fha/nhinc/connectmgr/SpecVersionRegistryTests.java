/**
 * 
 */
package gov.hhs.fha.nhinc.connectmgr;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * @author mweaver
 *
 */
@RunWith(Suite.class)
@SuiteClasses({ UddiSpecVersionRegistryTest.class, ADSpecVersionRegistryTest.class, PDSpecVersionRegistryTest.class, 
    DSSpecVersionRegistryTest.class, DQSpecVersionRegistryTest.class, DRSpecVersionRegistryTest.class, HIEMSpecVersionRegistryTest.class })
public class SpecVersionRegistryTests {

}
