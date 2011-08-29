/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.saml.extraction;

import gov.hhs.fha.nhinc.util.jaxb.JAXBXMLUtils;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import com.sun.xml.wss.saml.internal.saml20.jaxb20.AttributeType;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.ObjectFactory;

/**
 *
 * @author mweaver
 */
public class AttributeTypeHelperTest {

    JAXBContext context = null;

    public AttributeTypeHelperTest() {
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
    public void testExtractAttributeValueString() {
        try {
            String xml = "<saml2:Attribute xmlns:saml2=\"urn:oasis:names:tc:SAML:2.0:assertion\" Name=\"urn:oasis:names:tc:xspa:1.0:subject:subject-id\"><saml2:AttributeValue ns6:type=\"ns7:string\" xmlns:ns6=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:ns7=\"http://www.w3.org/2001/XMLSchema\">pie</saml2:AttributeValue></saml2:Attribute>";
            JAXBXMLUtils jaxb = new JAXBXMLUtils();
            JAXBElement<AttributeType> jaxbElement = (JAXBElement<AttributeType>) jaxb.parseXML(xml, "com.sun.xml.wss.saml.internal.saml20.jaxb20");
            AttributeType attr = (AttributeType) jaxbElement.getValue();


            String result = AttributeTypeHelper.extractAttributeValueString(attr);
            System.out.println("extractAttributeValueString result: " + result);
            Assert.assertEquals("pie", result);

        } catch (JAXBException ex) {
            Logger.getLogger(AttributeTypeHelperTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void testExtractAttributeValueStringNoTypeNoNS() {
        try {
            String xml = "<saml2:Attribute xmlns:saml2=\"urn:oasis:names:tc:SAML:2.0:assertion\" Name=\"urn:oasis:names:tc:xspa:1.0:subject:subject-id\"><saml2:AttributeValue>pie</saml2:AttributeValue></saml2:Attribute>";
            JAXBXMLUtils jaxb = new JAXBXMLUtils();
            JAXBElement jaxbElement = (JAXBElement) jaxb.parseXML(xml, "com.sun.xml.wss.saml.internal.saml20.jaxb20");
            AttributeType attr = (AttributeType) jaxbElement.getValue();

            String result = AttributeTypeHelper.extractAttributeValueString(attr);
            System.out.println("extractAttributeValueStringNoTypeNoNS result: " + result);
            Assert.assertEquals("pie", result);

        } catch (JAXBException ex) {
            Logger.getLogger(AttributeTypeHelperTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void testExtractAttributeValueStringNull() {
        try {
            String result = AttributeTypeHelper.extractAttributeValueString(null);
            System.out.println("extractAttributeValueStringNull result: " + result);

            Assert.assertEquals("", result);

        } catch (Exception ex) {
            Logger.getLogger(AttributeTypeHelperTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void testExtractAttributeValueStringEmpty() {
        try {
            String xml = "<saml2:Attribute xmlns:saml2=\"urn:oasis:names:tc:SAML:2.0:assertion\" Name=\"urn:oasis:names:tc:xspa:1.0:subject:subject-id\"></saml2:Attribute>";
            JAXBXMLUtils jaxb = new JAXBXMLUtils();
            JAXBElement jaxbElement = (JAXBElement) jaxb.parseXML(xml, "com.sun.xml.wss.saml.internal.saml20.jaxb20");
            AttributeType attr = (AttributeType) jaxbElement.getValue();

            String result = AttributeTypeHelper.extractAttributeValueString(attr);
            System.out.println("extractAttributeValueStringEmpty result: " + result);
            Assert.assertEquals("", result);

        } catch (JAXBException ex) {
            Logger.getLogger(AttributeTypeHelperTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void testExtractNameParts() {
        try {
            String xml = "<saml2:Attribute xmlns:saml2=\"urn:oasis:names:tc:SAML:2.0:assertion\" Name=\"urn:oasis:names:tc:xspa:1.0:subject:subject-id\"><saml2:AttributeValue xmlns:ns6=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:ns7=\"http://www.w3.org/2001/XMLSchema\" ns6:type=\"ns7:string\">Karl S Skagerberg</saml2:AttributeValue></saml2:Attribute>";
            JAXBXMLUtils jaxb = new JAXBXMLUtils();
            JAXBElement jaxbElement = (JAXBElement) jaxb.parseXML(xml, "com.sun.xml.wss.saml.internal.saml20.jaxb20");
            AttributeType attr = (AttributeType) jaxbElement.getValue();

            ObjectFactory objFact = new ObjectFactory();
            AssertionType assertion = objFact.createAssertionType();
            assertion.setUserInfo(objFact.createUserType());
            assertion.getUserInfo().setPersonName(objFact.createPersonNameType());


            AttributeTypeHelper.extractNameParts(attr, assertion);
            String givenName = assertion.getUserInfo().getPersonName().getGivenName();
            String familyName = assertion.getUserInfo().getPersonName().getFamilyName();
            String secondName = assertion.getUserInfo().getPersonName().getSecondNameOrInitials();

            System.out.println("extractAttributeValueStringEmpty givenName: " + givenName);
            System.out.println("extractAttributeValueStringEmpty familyName: " + familyName);
            System.out.println("extractAttributeValueStringEmpty secondName: " + secondName);

            Assert.assertEquals("Karl", givenName);
            Assert.assertEquals("Skagerberg", familyName);
            Assert.assertEquals("S", secondName);

        } catch (JAXBException ex) {
            Logger.getLogger(AttributeTypeHelperTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void testExtractNamePartsNoTypeNoNS() {
        try {
            String xml = "<saml2:Attribute xmlns:saml2=\"urn:oasis:names:tc:SAML:2.0:assertion\" Name=\"urn:oasis:names:tc:xspa:1.0:subject:subject-id\"><saml2:AttributeValue>Karl S Skagerberg</saml2:AttributeValue></saml2:Attribute>";
            JAXBXMLUtils jaxb = new JAXBXMLUtils();
            JAXBElement jaxbElement = (JAXBElement) jaxb.parseXML(xml, "com.sun.xml.wss.saml.internal.saml20.jaxb20");
            AttributeType attr = (AttributeType) jaxbElement.getValue();

            ObjectFactory objFact = new ObjectFactory();
            AssertionType assertion = objFact.createAssertionType();
            assertion.setUserInfo(objFact.createUserType());
            assertion.getUserInfo().setPersonName(objFact.createPersonNameType());


            AttributeTypeHelper.extractNameParts(attr, assertion);
            String givenName = assertion.getUserInfo().getPersonName().getGivenName();
            String familyName = assertion.getUserInfo().getPersonName().getFamilyName();
            String secondName = assertion.getUserInfo().getPersonName().getSecondNameOrInitials();

            System.out.println("extractAttributeValueStringEmpty givenName: " + givenName);
            System.out.println("extractAttributeValueStringEmpty familyName: " + familyName);
            System.out.println("extractAttributeValueStringEmpty secondName: " + secondName);

            Assert.assertEquals("Karl", givenName);
            Assert.assertEquals("Skagerberg", familyName);
            Assert.assertEquals("S", secondName);

        } catch (JAXBException ex) {
            Logger.getLogger(AttributeTypeHelperTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
