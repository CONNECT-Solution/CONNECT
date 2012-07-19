/**
*Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services.
*All rights reserved.
*
*Redistribution and use in source and binary forms, with or without
*modification, are permitted provided that the following conditions are met:
*    * Redistributions of source code must retain the above
*      copyright notice, this list of conditions and the following disclaimer.
*    * Redistributions in binary form must reproduce the above copyright
*      notice, this list of conditions and the following disclaimer in the documentation
*      and/or other materials provided with the distribution.
*    * Neither the name of the United States Government nor the
*      names of its contributors may be used to endorse or promote products
*      derived from this software without specific prior written permission.
*
*THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
*ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
*WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
*DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY
*DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
*(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
*LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
*ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
*(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
*SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package gov.hhs.fha.nhinc.openSAML.extraction;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opensaml.saml2.core.Attribute;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.ObjectFactory;
import gov.hhs.fha.nhinc.saml.extraction.AttributeTypeHelper;
import gov.hhs.fha.nhinc.util.jaxb.JAXBXMLUtils;

/**
 * @author zmelnick
 *
 */
public class AttributeHelperTest {
    JAXBContext context = null;
    AttributeHelper attributeHelper = new AttributeHelper();

    public AttributeHelperTest() {
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
    public void testExtractAttributeValueString() {
        try {
            String xml = "<saml2:Attribute xmlns:saml2=\"urn:oasis:names:tc:SAML:2.0:assertion\" Name=\"urn:oasis:names:tc:xspa:1.0:subject:subject-id\"><saml2:AttributeValue ns6:type=\"ns7:string\" xmlns:ns6=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:ns7=\"http://www.w3.org/2001/XMLSchema\">pie</saml2:AttributeValue></saml2:Attribute>";
            JAXBXMLUtils jaxb = new JAXBXMLUtils();
            JAXBElement jaxbElement = (JAXBElement) jaxb.parseXML(xml,
                    "org.opensaml.saml2.core");
            Attribute attr = (Attribute) jaxbElement.getValue();
            String result = attributeHelper.extractAttributeValueString(attr);
            System.out.println("extractAttributeValueString result: " + result);
            Assert.assertEquals("pie", result);
        } catch (JAXBException ex) {
            Logger.getLogger(AttributeHelperTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void testExtractAttributeValueStringNoTypeNoNS() {
        try {
            String xml = "<saml2:Attribute xmlns:saml2=\"urn:oasis:names:tc:SAML:2.0:assertion\" Name=\"urn:oasis:names:tc:xspa:1.0:subject:subject-id\"><saml2:AttributeValue>pie</saml2:AttributeValue></saml2:Attribute>";
            JAXBXMLUtils jaxb = new JAXBXMLUtils();
            JAXBElement jaxbElement = (JAXBElement) jaxb.parseXML(xml, "org.opensaml.saml2.core");;
            Attribute attr = (Attribute) jaxbElement.getValue();
            String result = attributeHelper.extractAttributeValueString(attr);
            System.out.println("extractAttributeValueStringNoTypeNoNS result: " + result);
            Assert.assertEquals("pie", result);
        } catch (JAXBException ex) {
            Logger.getLogger(AttributeHelperTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void testExtractAttributeValueStringNull() {
        try {
            String result = AttributeTypeHelper.extractAttributeValueString(null);
            System.out.println("extractAttributeValueStringNull result: " + result);
            Assert.assertEquals("", result);
        } catch (Exception ex) {
            Logger.getLogger(AttributeHelperTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void testExtractAttributeValueStringEmpty() {
        try {
            String xml = "<saml2:Attribute xmlns:saml2=\"urn:oasis:names:tc:SAML:2.0:assertion\" Name=\"urn:oasis:names:tc:xspa:1.0:subject:subject-id\"></saml2:Attribute>";
            JAXBXMLUtils jaxb = new JAXBXMLUtils();
            JAXBElement jaxbElement = (JAXBElement) jaxb.parseXML(xml, "org.opensaml.saml2.core");
            Attribute attr = (Attribute) jaxbElement.getValue();
            String result = attributeHelper.extractAttributeValueString(attr);
            System.out.println("extractAttributeValueStringEmpty result: " + result);
            Assert.assertEquals("", result);
        } catch (JAXBException ex) {
            Logger.getLogger(AttributeHelperTest.class.getName()).log(Level.SEVERE, null, ex);

        }

    }

    @Test
    public void testExtractNameParts() {
        try {
            String xml = "<saml2:Attribute xmlns:saml2=\"urn:oasis:names:tc:SAML:2.0:assertion\" Name=\"urn:oasis:names:tc:xspa:1.0:subject:subject-id\"><saml2:AttributeValue xmlns:ns6=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:ns7=\"http://www.w3.org/2001/XMLSchema\" ns6:type=\"ns7:string\">Karl S Skagerberg</saml2:AttributeValue></saml2:Attribute>";

            JAXBXMLUtils jaxb = new JAXBXMLUtils();
            JAXBElement jaxbElement = (JAXBElement) jaxb.parseXML(xml, "org.opensaml.saml2.core");
            Attribute attr = (Attribute) jaxbElement.getValue();

            ObjectFactory objFact = new ObjectFactory();

            AssertionType assertion = objFact.createAssertionType();
            assertion.setUserInfo(objFact.createUserType());
            assertion.getUserInfo().setPersonName(objFact.createPersonNameType());

            attributeHelper.extractNameParts(attr, assertion);

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
            Logger.getLogger(AttributeHelperTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void testExtractNamePartsNoTypeNoNS() {
        try {
            String xml = "<saml2:Attribute xmlns:saml2=\"urn:oasis:names:tc:SAML:2.0:assertion\" Name=\"urn:oasis:names:tc:xspa:1.0:subject:subject-id\"><saml2:AttributeValue>Karl S Skagerberg</saml2:AttributeValue></saml2:Attribute>";

            JAXBXMLUtils jaxb = new JAXBXMLUtils();
            JAXBElement jaxbElement = (JAXBElement) jaxb.parseXML(xml, "org.opensaml.saml2.core");
            Attribute attr = (Attribute) jaxbElement.getValue();

            ObjectFactory objFact = new ObjectFactory();
            AssertionType assertion = objFact.createAssertionType();
            assertion.setUserInfo(objFact.createUserType());
            assertion.getUserInfo().setPersonName(objFact.createPersonNameType());

            attributeHelper.extractNameParts(attr, assertion);

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
            Logger.getLogger(AttributeHelperTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
