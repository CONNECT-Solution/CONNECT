/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.adapter.busorchestration.nhinadapteserviceejb;

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
@Suite.SuiteClasses({gov.hhs.fha.nhinc.adapter.busorchestration.nhinadapteserviceejb.documentretrieve.DocumentretrieveSuite.class,gov.hhs.fha.nhinc.adapter.busorchestration.nhinadapteserviceejb.ServiceHelperTest.class,gov.hhs.fha.nhinc.adapter.busorchestration.nhinadapteserviceejb.documentquery.DocumentquerySuite.class})
public class NhinadapteserviceejbSuite {

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

}