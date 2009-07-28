/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.common.connectionmanager.dao;

import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author svalluripalli
 */
public class AssigningAuthorityHomeCommunityDAOTest {

    public AssigningAuthorityHomeCommunityDAOTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

     @Test
    public void testStoreMapping() {
        AssigningAuthorityHomeCommunityMappingDAO aDao = new AssigningAuthorityHomeCommunityMappingDAO();
        //assertTrue(aDao.storeMapping("1.2.3.4.55.500", "1.6"));
    }
     
    @Test
    public void testGetHomeCommunity() {
        AssigningAuthorityHomeCommunityMappingDAO aDao = new AssigningAuthorityHomeCommunityMappingDAO();
        assertNotNull(aDao.getHomeCommunityId("1.1"));
    }

    @Test
    public void testGetAssigningAuthority() {
        AssigningAuthorityHomeCommunityMappingDAO aDao = new AssigningAuthorityHomeCommunityMappingDAO();
        assertNotNull(aDao.getAssigningAuthority("1.2.3.4.55.500"));
    }

   @Test
   public void testGetAssigningAuthoritiesByHomeCommunity()
   {
       AssigningAuthorityHomeCommunityMappingDAO aDao = new AssigningAuthorityHomeCommunityMappingDAO();
       List<String> aaList = aDao.getAssigningAuthoritiesByHomeCommunity("1.2.3.4.55.500");
       assertNotNull(aaList);
//       for(String a : aaList)
//           System.out.println("AA := "+a);
//       assertTrue(aaList.size() == 2);
   }
}
