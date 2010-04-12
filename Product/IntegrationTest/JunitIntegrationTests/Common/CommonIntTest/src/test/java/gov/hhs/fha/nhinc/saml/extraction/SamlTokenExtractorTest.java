/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.saml.extraction;

import com.sun.xml.wss.saml.SAMLAssertionFactory;
import gov.hhs.fha.nhinc.util.StringUtil;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;

import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLInputFactory;
import java.io.StringReader;

//import com.sun.xml.ws.api.streaming.XMLStreamReaderFactory
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author westberg
 */
public class SamlTokenExtractorTest
{
    private static final String SAML_XML_FILE = "SAMLOnly.xml";

    public SamlTokenExtractorTest()
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
     * This creates an XML Stream reader from the example xml file.
     * 
     * @return The XML Stream reader repreenting the XML example.
     */
    private XMLStreamReader getSamlAssertionStreamReader()
    {
        XMLStreamReader xrSamlAssertionXML = null;

        try
        {
            String sSamlAssertionXML = StringUtil.readTextFile(SAML_XML_FILE);
            if ((sSamlAssertionXML != null) &&
                (sSamlAssertionXML.length() > 0))
            {
                StringReader srSamlAssertionXML = new StringReader(sSamlAssertionXML);
                XMLInputFactory oXMLInputFactory = XMLInputFactory.newInstance();
                xrSamlAssertionXML = oXMLInputFactory.createXMLStreamReader(srSamlAssertionXML);
            }
        }
        catch (Exception e)
        {
            System.out.println("An unexpected exception occurred: " + e.getMessage());
            fail("Failed to read example XML.");
        }

        return xrSamlAssertionXML;
    }

    /**
     * This creates a filled out AdhocQueryResponse message.
     *
     * @return A filled out AdhocQueryResponse message.
     */
    private com.sun.xml.wss.saml.Assertion createSamlAssertion()
    {
        com.sun.xml.wss.saml.Assertion oSamlAssertion = null;

        try
        {
            XMLStreamReader xrSamlAssertionXML = getSamlAssertionStreamReader();
            SAMLAssertionFactory factory = SAMLAssertionFactory.newInstance(SAMLAssertionFactory.SAML2_0);
            oSamlAssertion = factory.createAssertion(xrSamlAssertionXML);
        }
        catch (Exception e)
        {
            System.out.println("An unexpected exception occurred: " + e.getMessage());
            fail("Failed to read example XML.");
        }

        return oSamlAssertion;
    }


    /**
     * Test of createAssertion method, of class SamlTokenExtractor.
     */
    @Test
    public void testCreateAssertion()
    {
        System.out.println("Starting testCreateAssertion");

        XMLStreamReader xrSamlAssertionXML = getSamlAssertionStreamReader();
        AssertionType oAssertOut = SamlTokenExtractor.CreateAssertion(xrSamlAssertionXML);
        assertNotNull(oAssertOut);

        // Authn Statement
        //----------------
        assertNotNull("oAssertOut.SamlAuthnStatement was null:", oAssertOut.getSamlAuthnStatement());
        assertEquals("oAssertOut.SamlAuthnStatement.AuthInstant failed.", "2109-09-01T16:42:23.445Z", oAssertOut.getSamlAuthnStatement().getAuthInstant());
        assertEquals("oAssertOut.SamlAuthnStatement.SessionIndex failed.", "123456", oAssertOut.getSamlAuthnStatement().getSessionIndex());
        assertEquals("oAssertOut.SamlAuthnStatement.AuthContextClassRef failed.", "urn:oasis:names:tc:SAML:2.0:ac:classes:X509", oAssertOut.getSamlAuthnStatement().getAuthContextClassRef());
        assertEquals("oAssertOut.SamlAuthnStatement.SubjectLocalityAddress", "1.1.1.1", oAssertOut.getSamlAuthnStatement().getSubjectLocalityAddress());
        assertEquals("oAssertOut.SamlAuthnStatement.SubjectLocalityDNSName", "xx.somewhere.com", oAssertOut.getSamlAuthnStatement().getSubjectLocalityDNSName());

        // User Name
        //----------
        assertNotNull("oAssertOut.UserInfo was null.", oAssertOut.getUserInfo());
        assertNotNull("oAssertOut.UserInfo.PersonName was null.", oAssertOut.getUserInfo().getPersonName());
        assertEquals("oAssertOut.UserInfo.PersonName.FullName", "MARK ALAN FRANKLIN", oAssertOut.getUserInfo().getPersonName().getFullName());
        assertEquals("oAssertOut.UserInfo.PersonName.FamilyName", "FRANKLIN", oAssertOut.getUserInfo().getPersonName().getFamilyName());
        assertEquals("oAssertOut.UserInfo.PersonName.GivenName", "MARK", oAssertOut.getUserInfo().getPersonName().getGivenName());
        assertEquals("oAssertOut.UserInfo.PersonName.SecondNameOrInitials", "ALAN", oAssertOut.getUserInfo().getPersonName().getSecondNameOrInitials());

        // User Organization
        //------------------
        assertNotNull("oAssertOut.UserInfo.Org was null.", oAssertOut.getUserInfo().getOrg());
        assertEquals("oAssertOut.UserInfo.Org.Name", "InternalSelfTest2", oAssertOut.getUserInfo().getOrg().getName());

        // User Organization ID
        //---------------------
        assertEquals("oAssertOut.UserInfo.Org.Id", "2.2", oAssertOut.getUserInfo().getOrg().getHomeCommunityId());

        // Home Community ID
        //------------------
        assertNotNull("oAssertOut.HomeCommunity was null.", oAssertOut.getHomeCommunity());
        assertEquals("oAssertOut.HomeCommunity.Id", "urn:oid:2.16.840.1.113883.3.190", oAssertOut.getHomeCommunity().getHomeCommunityId());

        // User Role
        //----------
        assertNotNull("oAssertOut.UserInfo.RoleCoded was null.", oAssertOut.getUserInfo().getRoleCoded());
        assertEquals("oAssertOut.UserInfo.RoleCoded.Code", "80584001", oAssertOut.getUserInfo().getRoleCoded().getCode());
        assertEquals("oAssertOut.UserInfo.RoleCoded.CodeSystem", "2.16.840.1.113883.6.96", oAssertOut.getUserInfo().getRoleCoded().getCodeSystem());
        assertEquals("oAssertOut.UserInfo.RoleCoded.CodeSystemName", "SNOMED_CT", oAssertOut.getUserInfo().getRoleCoded().getCodeSystemName());
        assertEquals("oAssertOut.UserInfo.RoleCoded.DisplayName", "Psychiatrist", oAssertOut.getUserInfo().getRoleCoded().getDisplayName());

        // Purpose For Use
        //----------------
        assertNotNull("oAssertOut.PurposeOfDisclosureCoded was null.", oAssertOut.getPurposeOfDisclosureCoded());
        assertEquals("oAssertOut.PurposeOfDisclosureCoded.Code", "PSYCHOTHERAPY", oAssertOut.getPurposeOfDisclosureCoded().getCode());
        assertEquals("oAssertOut.PurposeOfDisclosureCoded.CodeSystem", "2.16.840.1.113883.3.18.7.1", oAssertOut.getPurposeOfDisclosureCoded().getCodeSystem());
        assertEquals("oAssertOut.PurposeOfDisclosureCoded.CodeSystemName", "nhin-purpose", oAssertOut.getPurposeOfDisclosureCoded().getCodeSystemName());
        assertEquals("oAssertOut.PurposeOfDisclosureCoded.DisplayName", "Use or disclosure of Psychotherapy Notes", oAssertOut.getPurposeOfDisclosureCoded().getDisplayName());

        // Patient ID
        //------------------
        assertNotNull("oAssertOut.UniquePatient was null.", oAssertOut.getUniquePatientId());
        assertEquals("oAssertOut.UniquePatientId", "543797436^^^&1.2.840.113619.6.197&ISO", oAssertOut.getUniquePatientId().get(0));

        // Authorization Decision Statement
        //----------------------------------
        assertNotNull("oAssertOut.SamlAuthzDecisionStatement was null.", oAssertOut.getSamlAuthzDecisionStatement());
        assertEquals("oAssertOut.SamlAuthzDecisionStatement.Decision", "Permit", oAssertOut.getSamlAuthzDecisionStatement().getDecision());
        assertEquals("oAssertOut.SamlAuthzDecisionStatement.Resource", "https://localhost:8181/findAuditEvents/AuditQuery", oAssertOut.getSamlAuthzDecisionStatement().getResource());
        assertEquals("oAssertOut.SamlAuthzDecisionStatement.Action", "Execute", oAssertOut.getSamlAuthzDecisionStatement().getAction());

        // Authorization Decision Statement Evidence
        //-------------------------------------------
        assertNotNull("oAssertOut.SamlAuthzDecisionStatement.Evidence was null.", oAssertOut.getSamlAuthzDecisionStatement().getEvidence());
        assertNotNull("oAssertOut.SamlAuthzDecisionStatement.Evidence.Assertion was null.", oAssertOut.getSamlAuthzDecisionStatement().getEvidence().getAssertion());
        assertEquals("oAssertOut.SamlAuthzDecisionStatement.Evidence.Assertion.Id", "5b0e3336-3c51-4df3-88a3-6622c8c41165", oAssertOut.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getId());
        assertEquals("oAssertOut.SamlAuthzDecisionStatement.Evidence.Assertion.IssueInstant", "2009-09-01T16:42:23.445Z", oAssertOut.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getIssueInstant());
        assertEquals("oAssertOut.SamlAuthzDecisionStatement.Evidence.Assertion.Version", "2.0", oAssertOut.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getVersion());
        assertEquals("oAssertOut.SamlAuthzDecisionStatement.Evidence.Assertion.IssuerFormat", "urn:oasis:names:tc:SAML:1.1:nameid-format:X509SubjectName", oAssertOut.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getIssuerFormat());
        assertEquals("oAssertOut.SamlAuthzDecisionStatement.Evidence.Assertion.Issuer", "CN=SAML User,OU=SU,O=SAML User,L=Los Angeles,ST=CA,C=US", oAssertOut.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getIssuer());
        assertNotNull("oAssertOut.SamlAuthzDecisionStatement.Evidence.Assertion.Conditions was null.", oAssertOut.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getConditions());
        assertEquals("oAssertOut.SamlAuthzDecisionStatement.Evidence.Assertion.Conditions.NotBefore", "2009-09-01T21:42:23.000Z", oAssertOut.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getConditions().getNotBefore());
        assertEquals("oAssertOut.SamlAuthzDecisionStatement.Evidence.Assertion.Conditions.NotOnOrAfter", "2009-10-01T22:42:23.000Z", oAssertOut.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getConditions().getNotOnOrAfter());
        assertEquals("oAssertOut.SamlAuthzDecisionStatement.Evidence.Assertion.AccessConsentPolicy", "urn:oid:1.2.3.4", oAssertOut.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getAccessConsentPolicy());
        assertEquals("oAssertOut.SamlAuthzDecisionStatement.Evidence.Assertion.InstanceAccessConsentPolicy", "urn:oid:1.2.3.4.123456789", oAssertOut.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getInstanceAccessConsentPolicy());







//        XMLStreamReader reader = null;
//        AssertionType expResult = null;
//        AssertionType result = SamlTokenExtractor.CreateAssertion(reader);
//        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }
}