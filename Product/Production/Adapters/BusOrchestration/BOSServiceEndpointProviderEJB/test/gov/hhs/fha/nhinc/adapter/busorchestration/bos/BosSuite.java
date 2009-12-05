/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.adapter.busorchestration.bos;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author Jerry Goodnough
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({gov.hhs.fha.nhinc.adapter.busorchestration.bos.ServiceEndpointProviderImplTest.class,gov.hhs.fha.nhinc.adapter.busorchestration.bos.ServiceInfoXMLTest.class,gov.hhs.fha.nhinc.adapter.busorchestration.bos.ServiceEndpointCacheTest.class})
public class BosSuite {

    @BeforeClass
    public static void setUpClass() throws Exception
    {
    }

    @AfterClass
    public static void tearDownClass() throws Exception
    {
    }

    @Before
    public void setUp() throws Exception
    {
    }

    @After
    public void tearDown() throws Exception
    {
    }

}