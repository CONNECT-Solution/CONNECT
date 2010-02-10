/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.documentassembly;

import gov.hhs.fha.nhinc.assemblymanager.CDAConstants;
import gov.hhs.fha.nhinc.assemblymanager.utils.XMLUtil;
import org.hl7.v3.CE;
import org.hl7.v3.POCDMT000040ClinicalDocument;
import org.hl7.v3.POCDMT000040Component2;
import org.hl7.v3.POCDMT000040Component3;
import org.hl7.v3.POCDMT000040Section;
import org.hl7.v3.POCDMT000040StructuredBody;
import org.hl7.v3.StrucDocContent;
import org.hl7.v3.StrucDocText;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Element;
import static org.junit.Assert.*;
import org.hl7.v3.STExplicit;

/**
 *
 * @author kim
 */
public class DomElementTest {

   public DomElementTest() {
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

   // TODO add test methods here.
   // The methods must be annotated with annotation @Test. For example:
   //
   @Test
   public void buildSection() {
      org.hl7.v3.ObjectFactory factory = new org.hl7.v3.ObjectFactory();

      POCDMT000040ClinicalDocument cdaDocument = factory.createPOCDMT000040ClinicalDocument();
      POCDMT000040Component2 clinicalComponent = new POCDMT000040Component2();
      POCDMT000040StructuredBody structBody = new POCDMT000040StructuredBody();
      POCDMT000040Component3 component = new POCDMT000040Component3();
      POCDMT000040Section allergySection = new POCDMT000040Section();

      CE loincAllergyCode = new CE();
      loincAllergyCode.setCode(CDAConstants.LOINC_ALLERGY_CODE);
      loincAllergyCode.setCodeSystem(CDAConstants.LOINC_CODE_SYS_OID);
      allergySection.setCode(loincAllergyCode);

//      try {
//         Element title = XMLUtil.createElement(CDAConstants.TITLE_TAG);
//         allergySection.setTitle(title);
//         title.setTextContent(CDAConstants.ALLERGIES_SECTION_TITLE);
//      //allergySection.setTitle(title);
//      } catch (Exception e) {
//         System.out.println("Failed to set POCDMT000040Section.title");
//      }
       STExplicit title = new STExplicit();
       title.getContent().add(CDAConstants.ALLERGIES_SECTION_TITLE);
       allergySection.setTitle(title);

      try {
         Element textElement = XMLUtil.createElement(CDAConstants.TEXT_TAG);
         StrucDocContent textContent = factory.createStrucDocContent();
         textContent.setID("reaction-1");
         textContent.getContent().add("Some reaction");

         String strucDocContent = XMLUtil.toCanonicalXML(factory.createStrucDocTextContent(textContent));
         System.out.println("strucDocContent=\n" + strucDocContent);

         textElement.setTextContent(XMLUtil.toCanonicalXML(factory.createStrucDocTextContent(textContent)));
         allergySection.setText(textElement);
      } catch (Exception e) {
         e.printStackTrace();
      }

      component.setSection(allergySection);
      structBody.getComponent().add(component);
      clinicalComponent.setStructuredBody(structBody);
      cdaDocument.setComponent(clinicalComponent);

      try {
         String cdaDoc = XMLUtil.toPrettyXML(cdaDocument);

         cdaDoc = cdaDoc.replaceAll("&lt;content", "<content");
         cdaDoc = cdaDoc.replaceAll("&lt;/content&gt;", "</content>");
         cdaDoc = cdaDoc.replaceAll("\"&gt;", "\">");
         
         System.out.println(cdaDoc);

      } catch (Exception e) {
         e.printStackTrace();
      }
   }
}