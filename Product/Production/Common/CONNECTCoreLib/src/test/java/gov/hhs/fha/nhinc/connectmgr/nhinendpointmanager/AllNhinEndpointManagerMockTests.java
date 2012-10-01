/**
 * 
 */
package gov.hhs.fha.nhinc.connectmgr.nhinendpointmanager;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * @author mweaver
 *
 */
@RunWith(Suite.class)
@SuiteClasses({ ADNhinEndpointManagerMockTest.class, DQNhinEndpointManagerMockTest.class,
        DRNhinEndpointManagerMockTest.class, DSDReqNhinEndpointManagerMockTest.class,
        DSDRespNhinEndpointManagerMockTest.class, DSNhinEndpointManagerMockTest.class,
        PDDReqNhinEndpointManagerMockTest.class, PDDRespNhinEndpointManagerMockTest.class,
        PDNhinEndpointManagerMockTest.class, AbstractNhinEndpointManagerMockTest.class })
public class AllNhinEndpointManagerMockTests {

}
