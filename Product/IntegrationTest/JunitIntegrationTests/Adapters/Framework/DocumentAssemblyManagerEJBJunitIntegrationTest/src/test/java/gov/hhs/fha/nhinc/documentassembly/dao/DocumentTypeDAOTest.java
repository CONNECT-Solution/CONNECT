/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.documentassembly.dao;

import gov.hhs.fha.nhinc.assemblymanager.dao.DocumentTypeDAO;
import gov.hhs.fha.nhinc.assemblymanager.dao.model.DocumentType;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class DocumentTypeDAOTest {

   public DocumentTypeDAOTest() {
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
    * Test of getDocumentType method, of class DocumentTypeDAO.
    */
   @Test
   public void testGetDocumentType() {
      System.out.println("getDocumentType");
      String typeId = "34133-9";
      DocumentType expResult = null;
      DocumentType result = null;
      try {
         result = DocumentTypeDAO.getInstance().getDocumentType(typeId);
      } catch (Exception ex) {
         Logger.getLogger(DocumentTypeDAOTest.class.getName()).log(Level.SEVERE, null, ex);
      }
      System.out.println(result);
      assertEquals("34133-9", result.getTypeId());
   }

   /**
    * Test of isValidDocumentType method, of class DocumentTypeDAO.
    */
   @Test
   public void testIsValidDocumentType() {
      System.out.println("isValidDocumentType");
      String typeId = "34133-9";
      //DocumentType expResult = null;
      boolean result = DocumentTypeDAO.getInstance().isValidDocumentType(typeId);
      assertEquals(true, result);
   }

   @Test
   public void testGetDocumentTypes() {
      System.out.println("getDocumentTypes");
      List<DocumentType> result = DocumentTypeDAO.getInstance().getDocumentTypes();
      assertEquals(3, result == null? 0:result.size());
   }
}