package gov.hhs.fha.nhinc.util;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *  This tests the HomeCommunityMap class.
 * 
 * @author Les Westberg
 */
public class HomeCommunityMapTest
{

    public HomeCommunityMapTest()
    {
    }

    @BeforeClass
    public static void setUpClass() throws Exception
    {
    }

    @AfterClass
    public static void tearDownClass() throws Exception
    {
    }

    @Before
    public void setUp()
    {
    }

    @After
    public void tearDown()
    {
    }

    /**
     * Test of getHomeCommunityName method, of class HomeCommunityMap.
     */
    @Test
    public void testGetHomeCommunityName()
    {
        System.out.println("getHomeCommunityName");

        // The values here are completely dependent on values in UDDI that we are not
        // in charge of.  We should not have our unit test fail for that reason.  It is commented
        // out but can be uncommented for debugging...
        //----------------------------------------------------------------------------------------
        
//        HomeCommunityMap oHomeCommunityMap = new HomeCommunityMap();
//        String sResult = oHomeCommunityMap.getHomeCommunityName("2.16.840.1.113883.3.200");
//        assertEquals("Federal Agency - VA", sResult);
//
//        sResult = oHomeCommunityMap.getHomeCommunityName("2.16.840.1.113883.13.25");
//        assertEquals("Bloomington Hospital", sResult);
    }
}