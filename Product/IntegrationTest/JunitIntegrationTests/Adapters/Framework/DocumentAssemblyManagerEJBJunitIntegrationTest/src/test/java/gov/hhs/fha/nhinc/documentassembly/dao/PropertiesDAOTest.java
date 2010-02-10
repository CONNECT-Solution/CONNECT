/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.documentassembly.dao;

import gov.hhs.fha.nhinc.assemblymanager.dao.PropertiesDAO;
import gov.hhs.fha.nhinc.assemblymanager.dao.model.DasConfig;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author kim
 */
public class PropertiesDAOTest {

    public PropertiesDAOTest() {
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

   /**
    * Test of getDASAttributeValue method, of class PropertiesDAO.
    */
   @Test
   public void testGetAttributeValue() {
      System.out.println("getDASAttributeValue");
      String attName = "das.dataservice.endpoint";
      boolean active = true;
      PropertiesDAO instance = PropertiesDAO.getInstance();
      String expResult = "http://localhost:8080/CommonDataLayerService/AdapterCommonDataLayer?wsdl";
      String result = instance.getAttributeValue(attName, active);
      System.out.println(result);
      assertEquals(expResult, result);
   }

   /**
    * Test of getDASAttribute method, of class PropertiesDAO.
    */
   @Test
   public void testGetDASAttribute() {
      System.out.println("getDASAttribute");
      String attName = "das.dataservice.endpoint";
      boolean active = true;
      PropertiesDAO instance = PropertiesDAO.getInstance();
      //DasConfig expResult = null;
      DasConfig result = instance.getAttribute(attName, active);
      System.out.println(result);
      assertEquals("http://localhost:8080/CommonDataLayerService/AdapterCommonDataLayer?wsdl", result.getAttValue());
   }
}