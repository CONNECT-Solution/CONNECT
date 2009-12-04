package gov.hhs.fha.nhinc.policyengine.adapterpip;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.FineGrainedPolicyCriteriaType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.FineGrainedPolicyCriterionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.PatientPreferencesType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.UserIdFormatType;
import oasis.names.tc.xacml._2_0.policy.schema.os.PolicyType;

import org.apache.commons.logging.Log;
import org.jmock.Mockery;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;

import org.junit.runner.RunWith;

import static org.junit.Assert.*;


/**
 * Unit tests for XACMLExtractor
 *
 * @author Les Westberg
 */
@RunWith(JMock.class)
public class XACMLExtractorTest
{
    Mockery context = new JUnit4Mockery();
    static PolicyType oXACMLPolicy1 = null;
    static PolicyType oXACMLPolicy2 = null;
    static String XACML_EXAMPLE_1 =   "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                                    "<Policy xmlns=\"urn:oasis:names:tc:xacml:2.0:policy:schema:os\"" +
                                    "  PolicyId=\"12345678-1234-1234-1234-123456781234\" " +
                                    "  RuleCombiningAlgId=\"urn:oasis:names:tc:xacml:1.0:rule-combining-algorithm:first-applicable\">" +
                                    "  <Description>CONSUMER CONSENT POLICY</Description>" +
                                    "" +
                                    "<!-- The Target element at the Policy level identifies the subject to whom the Policy applies -->" +
                                    "  <Target>" +
                                    "    <Resources>" +
                                    "      <Resource>" +
                                    "      <!-- Consumer ID -->" +
                                    "        <ResourceMatch MatchId=\"http://www.hhs.gov/healthit/nhin/function#instance-identifier-equal\">" +
                                    "          <AttributeValue DataType=\"urn:hl7-org:v3#II\" xmlns:hl7=\"urn:hl7-org:v3\">" +
                                    "            <hl7:PatientId root=\"1.1\" extension=\"D123401\"/>" +
                                    "          </AttributeValue>" +
                                    "          <ResourceAttributeDesignator AttributeId=\"http://www.hhs.gov/healthit/nhin#subject-id\" " +
                                    "                                       DataType=\"urn:hl7-org:v3#II\"/>" +
                                    "        </ResourceMatch>" +
                                    "      </Resource>" +
                                    "    </Resources>" +
                                    "    <Actions>" +
                                    "      <!-- This policy applies to all document query and document retrieve transactions -->" +
                                    "      <Action>" +
                                    "        <ActionMatch MatchId=\"urn:oasis:names:tc:xacml:1.0:function:anyURI-equal\">" +
                                    "          <AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#anyURI\">" +
                                    "            urn:ihe:iti:2007:CrossGatewayRetrieve" +
                                    "          </AttributeValue>" +
                                    "          <ActionAttributeDesignator AttributeId=\"urn:oasis:names:tc:xacml:2.0:action\" " +
                                    "                                     DataType=\"http://www.w3.org/2001/XMLSchema#anyURI\"/>" +
                                    "        </ActionMatch>" +
                                    "      </Action>" +
                                    "      <Action>" +
                                    "        <ActionMatch MatchId=\"urn:oasis:names:tc:xacml:1.0:function:anyURI-equal\">" +
                                    "          <AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#anyURI\">" +
                                    "            urn:ihe:iti:2007:CrossGatewayQuery" +
                                    "          </AttributeValue>" +
                                    "          <ActionAttributeDesignator AttributeId=\"urn:oasis:names:tc:xacml:2.0:action\" " +
                                    "                                     DataType=\"http://www.w3.org/2001/XMLSchema#anyURI\"/>" +
                                    "        </ActionMatch>" +
                                    "      </Action>" +
                                    "    </Actions>" +
                                    "  </Target>" +
                                    "  <Rule RuleId=\"1\" Effect=\"Permit\">" +
                                    "    <Description>Rule specifying fine grained criteria.</Description>" +
                                    "    <Target>" +
                                    "      <Subjects>" +
                                    "        <Subject>" +
                                    "          <!-- User Role -->" +
                                    "          <SubjectMatch MatchId=\"urn:oasis:names:tc:xacml:1.0:function:string-equal\">" +
                                    "            <!-- coded value for physicians -->" +
                                    "            <AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">112247003</AttributeValue>" +
                                    "            <SubjectAttributeDesignator AttributeId=\"urn:oasis:names:tc:xacml:2.0:subject:role\" " +
                                    "                                        DataType=\"http://www.w3.org/2001/XMLSchema#string\"/>" +
                                    "          </SubjectMatch>" +
                                    "        </Subject>" +
                                    "      </Subjects>" +
                                    "      <!-- since there is no Resource element, this rule applies to all resources -->" +
                                    "    </Target>" +
                                    "  </Rule>" +
                                    "  <Rule RuleId=\"2\" Effect=\"Permit\">" +
                                    "    <Description>Rule specifying fine grained criteria.</Description>" +
                                    "    <Target>" +
                                    "      <Subjects>" +
                                    "        <Subject>" +
                                    "          <!-- User Role -->" +
                                    "          <SubjectMatch MatchId=\"urn:oasis:names:tc:xacml:1.0:function:string-equal\">" +
                                    "            <!-- coded value for dentists -->" +
                                    "            <AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">106289002</AttributeValue>" +
                                    "            <SubjectAttributeDesignator AttributeId=\"urn:oasis:names:tc:xacml:2.0:subject:role\" " +
                                    "                                        DataType=\"http://www.w3.org/2001/XMLSchema#string\"/>" +
                                    "          </SubjectMatch>" +
                                    "        </Subject>" +
                                    "        <Subject>" +
                                    "          <!-- Organization ID-->" +
                                    "          <SubjectMatch MatchId=\"urn:oasis:names:tc:xacml:1.0:function:anyURI-equal\">" +
                                    "            <AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#anyURI\">http://www.happytoothdental.com</AttributeValue>" +
                                    "            <SubjectAttributeDesignator AttributeId=\"urn:oasis:names:tc:xspa:1.0:subject:organization-id\" " +
                                    "                                        DataType=\"http://www.w3.org/2001/XMLSchema#anyURI\"/>" +
                                    "          </SubjectMatch>" +
                                    "        </Subject>" +
                                    "      </Subjects>" +
                                    "      <Resources>" +
                                    "        <Resource>" +
                                    "          <!-- Confidentiality Code -->" +
                                    "          <ResourceMatch MatchId=\"urn:oasis:names:tc:xacml:1.0:function:string-equal\">" +
                                    "            <AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">N</AttributeValue>" +
                                    "            <ResourceAttributeDesignator AttributeId=\"urn:oasis:names:tc:xspa:1.0:resource:patient:hl7:confidentiality-code\" " +
                                    "                                         DataType=\"http://www.w3.org/2001/XMLSchema#string\"/>" +
                                    "          </ResourceMatch>" +
                                    "        </Resource>" +
                                    "      </Resources>" +
                                    "      <Environments>" +
                                    "        <Environment>" +
                                    "          <!-- Rule Start Date -->" +
                                    "            <EnvironmentMatch MatchId=\"urn:oasis:names:tc:xacml:1.0:function:date-greather-than-or-equal\">" +
                                    "              <AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#date\">2009-07-01</AttributeValue>" +
                                    "              <EnvironmentAttributeDesignator AttributeId=\"http://www.hhs.gov/healthit/nhin#rule-start-date\" " +
                                    "                                              DataType=\"http://www.w3.org/2001/XMLSchema#date\"/>" +
                                    "            </EnvironmentMatch>" +
                                    "        </Environment>" +
                                    "        <Environment>" +
                                    "          <!-- Rule End Date -->" +
                                    "          <EnvironmentMatch MatchId=\"urn:oasis:names:tc:xacml:1.0:function:date-less-than-or-equal\">" +
                                    "            <AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#date\">2009-12-31</AttributeValue>" +
                                    "            <EnvironmentAttributeDesignator AttributeId=\"http://www.hhs.gov/healthit/nhin#rule-end-date\" " +
                                    "                                            DataType=\"http://www.w3.org/2001/XMLSchema#date\"/>" +
                                    "          </EnvironmentMatch>" +
                                    "        </Environment>" +
                                    "      </Environments>      " +
                                    "    </Target>" +
                                    "  </Rule>" +
                                    "  <Rule RuleId=\"3\" Effect=\"Permit\">" +
                                    "    <Description>Rule specifying fine grained criteria.</Description>" +
                                    "    <Target>" +
                                    "      <Subjects>" +
                                    "        <Subject>" +
                                    "          <!-- Home Community Id -->" +
                                    "          <SubjectMatch MatchId=\"urn:oasis:names:tc:xacml:1.0:function:anyURI-equal\">" +
                                    "            <AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#anyURI\">1.1</AttributeValue>" +
                                    "            <SubjectAttributeDesignator AttributeId=\"http://www.hhs.gov/healthit/nhin#HomeCommunityId\" " +
                                    "                                        DataType=\"http://www.w3.org/2001/XMLSchema#anyURI\"/>" +
                                    "          </SubjectMatch>" +
                                    "        </Subject>" +
                                    "        <Subject>" +
                                    "          <!-- Subject ID as email -->" +
                                    "          <SubjectMatch MatchId=\"urn:oasis:names:tc:xacml:1.0:function:rfc822Name-match\">" +
                                    "            <AttributeValue DataType=\"urn:oasis:names:tc:xacml:1.0:data-type:rfc822Name\">john.doe@somewhere.com</AttributeValue>" +
                                    "            <SubjectAttributeDesignator AttributeId=\"urn:oasis:names:tc:xacml:1.0:subject:subject-id\" " +
                                    "                                        DataType=\"urn:oasis:names:tc:xacml:1.0:data-type:rfc822Name\"/>" +
                                    "          </SubjectMatch>" +
                                    "        </Subject>" +
                                    "      </Subjects>" +
                                    "      <Resources>" +
                                    "        <Resource>" +
                                    "          <!-- Document Type (Class) -->" +
                                    "          <ResourceMatch MatchId=\"urn:oasis:names:tc:xacml:1.0:function:string-equal\">" +
                                    "            <AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">45666666</AttributeValue>" +
                                    "            <ResourceAttributeDesignator AttributeId=\"urn:oasis:names:tc:xspa:1.0:resource:hl7:type\" " +
                                    "                                         DataType=\"http://www.w3.org/2001/XMLSchema#string\"/>" +
                                    "          </ResourceMatch>" +
                                    "        </Resource>" +
                                    "      </Resources>" +
                                    "    </Target>" +
                                    "  </Rule>" +
                                    "  <Rule RuleId=\"4\" Effect=\"Permit\">" +
                                    "    <Description>Rule specifying fine grained criteria.</Description>" +
                                    "    <Target>" +
                                    "      <Subjects>" +
                                    "        <Subject>" +
                                    "          <!-- User ID - X500 -->" +
                                    "          <SubjectMatch MatchId=\"urn:oasis:names:tc:xacml:1.0:function:x509Name-match\">" +
                                    "            <AttributeValue DataType=\"urn:oasis:names:tc:xacml:1.0:data-type:x500Name\">cn=John Doe, ou=People, o=Somewhere, c=us</AttributeValue>" +
                                    "            <SubjectAttributeDesignator AttributeId=\"urn:oasis:names:tc:xacml:1.0:subject:subject-id\" " +
                                    "                                        DataType=\"urn:oasis:names:tc:xacml:1.0:data-type:x500Name\"/>" +
                                    "          </SubjectMatch>" +
                                    "        </Subject>" +
                                    "        <Subject>" +
                                    "          <!-- Purpose for use -->" +
                                    "          <SubjectMatch MatchId=\"urn:oasis:names:tc:xacml:1.0:function:string-equal\">" +
                                    "            <AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">Treatment</AttributeValue>" +
                                    "            <SubjectAttributeDesignator AttributeId=\"urn:oasis:names:tc:xspa:1.0:subject:purposeofuse\" " +
                                    "                                        DataType=\"http://www.w3.org/2001/XMLSchema#string\"/>" +
                                    "          </SubjectMatch>" +
                                    "        </Subject>" +
                                    "      </Subjects>" +
                                    "      <Resources>" +
                                    "        <Resource>" +
                                    "          <!-- Document Id  -->" +
                                    "          <ResourceMatch MatchId=\"urn:oasis:names:tc:xacml:1.0:function:string-equal\">" +
                                    "            <AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">1234567</AttributeValue>" +
                                    "            <ResourceAttributeDesignator AttributeId=\"urn:oasis:names:tc:xacml:1.0:resource:resource-id\" " +
                                    "                  								       DataType=\"http://www.w3.org/2001/XMLSchema#string\"/>" +
                                    "          </ResourceMatch>" +
                                    "        </Resource>" +
                                    "      </Resources>" +
                                    "    </Target>" +
                                    "  </Rule>" +
                                    "  <Rule RuleId=\"5\" Effect=\"Deny\">" +
                                    "    <Description>Rule specifying fine grained criteria.</Description>" +
                                    "    <Target/>" +
                                    "  </Rule>" +
                                    "</Policy>";

    static String XACML_EXAMPLE_2 =   "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                                    "<Policy xmlns=\"urn:oasis:names:tc:xacml:2.0:policy:schema:os\"" +
                                    "  PolicyId=\"12345678-1234-1234-1234-123456781234\" " +
                                    "  RuleCombiningAlgId=\"urn:oasis:names:tc:xacml:1.0:rule-combining-algorithm:first-applicable\">" +
                                    "  <Description>CONSUMER CONSENT POLICY</Description>" +
                                    "" +
                                    "<!-- The Target element at the Policy level identifies the subject to whom the Policy applies -->" +
                                    "  <Target>" +
                                    "    <Resources>" +
                                    "      <Resource>" +
                                    "      <!-- Consumer ID -->" +
                                    "        <ResourceMatch MatchId=\"http://www.hhs.gov/healthit/nhin/function#instance-identifier-equal\">" +
                                    "          <AttributeValue DataType=\"urn:hl7-org:v3#II\" xmlns:hl7=\"urn:hl7-org:v3\">" +
                                    "            <hl7:PatientId root=\"1.1\" extension=\"D123401\"/>" +
                                    "          </AttributeValue>" +
                                    "          <ResourceAttributeDesignator AttributeId=\"http://www.hhs.gov/healthit/nhin#subject-id\" " +
                                    "                                       DataType=\"urn:hl7-org:v3#II\"/>" +
                                    "        </ResourceMatch>" +
                                    "      </Resource>" +
                                    "    </Resources>" +
                                    "    <Actions>" +
                                    "      <!-- This policy applies to all document query and document retrieve transactions -->" +
                                    "      <Action>" +
                                    "        <ActionMatch MatchId=\"urn:oasis:names:tc:xacml:1.0:function:anyURI-equal\">" +
                                    "          <AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#anyURI\">" +
                                    "            urn:ihe:iti:2007:CrossGatewayRetrieve" +
                                    "          </AttributeValue>" +
                                    "          <ActionAttributeDesignator AttributeId=\"urn:oasis:names:tc:xacml:2.0:action\" " +
                                    "                                     DataType=\"http://www.w3.org/2001/XMLSchema#anyURI\"/>" +
                                    "        </ActionMatch>" +
                                    "      </Action>" +
                                    "      <Action>" +
                                    "        <ActionMatch MatchId=\"urn:oasis:names:tc:xacml:1.0:function:anyURI-equal\">" +
                                    "          <AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#anyURI\">" +
                                    "            urn:ihe:iti:2007:CrossGatewayQuery" +
                                    "          </AttributeValue>" +
                                    "          <ActionAttributeDesignator AttributeId=\"urn:oasis:names:tc:xacml:2.0:action\" " +
                                    "                                     DataType=\"http://www.w3.org/2001/XMLSchema#anyURI\"/>" +
                                    "        </ActionMatch>" +
                                    "      </Action>" +
                                    "    </Actions>" +
                                    "  </Target>" +
                                    "  <Rule RuleId=\"5\" Effect=\"Deny\">" +
                                    "    <Description>Rule specifying fine grained criteria.</Description>" +
                                    "    <Target/>" +
                                    "  </Rule>" +
                                    "</Policy>";

    public XACMLExtractorTest()
    {
    }

    @BeforeClass
    public static void setUpClass() throws Exception
    {
        XACMLSerializer oSerializer = new XACMLSerializer();
        oXACMLPolicy1 = oSerializer.deserializeConsentXACMLDoc(XACML_EXAMPLE_1);
        oXACMLPolicy2 = oSerializer.deserializeConsentXACMLDoc(XACML_EXAMPLE_2);

//        try
//        {
//            String sTest = oSerializer.serializeConsentXACMLDoc(oXACMLPolicy1);
//            String sStop = "";
//        }
//        catch (Exception e)
//        {
//            String sJunk = "text";
//            String sJunk2 = "text";
//        }

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
     * This asserts the data in Fine Grained Policy Criterion 1 of the data.
     *
     * @param oCriterion The criterion to be validated.
     */
    public void assertValidExample1Criterion1(FineGrainedPolicyCriterionType oCriterion)
    {
        assertNotNull("Criterion 1", oCriterion);
        assertEquals("SequentialId", "1", oCriterion.getSequentialId());
        assertEquals("Permit", true, oCriterion.isPermit());
        assertNotNull("UserRole", oCriterion.getUserRole());
        assertEquals("UserRole.Code", "112247003", oCriterion.getUserRole().getCode());

        // That is all that should be in here.  Everything else should be null - make sure that is true.
        //----------------------------------------------------------------------------------------------
        assertNull("UserRole.DisplayName", oCriterion.getUserRole().getDisplayName());
        assertNull("UserRole.CodeSystem", oCriterion.getUserRole().getCodeSystem());
        assertNull("UserRole.CodeSystemName", oCriterion.getUserRole().getCodeSystemName());
        assertNull("PolicyOID", oCriterion.getPolicyOID());
        assertNull("DocumentTypeCode", oCriterion.getDocumentTypeCode());
        assertNull("PurposeOfUse", oCriterion.getPurposeOfUse());
        assertNull("ConfidentialityCode", oCriterion.getConfidentialityCode());
        assertNull("Action", oCriterion.getAction());
        assertNull("OrganizationId", oCriterion.getOrganizationId());
        assertNull("HomeCommunityId", oCriterion.getHomeCommunityId());
        assertNull("UniqueDocumentId", oCriterion.getUniqueDocumentId());
        assertNull("RuleStartDate", oCriterion.getRuleStartDate());
        assertNull("RuleEndDate", oCriterion.getRuleEndDate());
        assertNull("UserId", oCriterion.getUserId());
        assertNull("UserIdFormat", oCriterion.getUserIdFormat());
    }

    /**
     * This asserts the data in Fine Grained Policy Criterion 2 of the data.
     *
     * @param oCriterion The criterion to be validated.
     */
    public void assertValidExample1Criterion2(FineGrainedPolicyCriterionType oCriterion)
    {
        assertNotNull("Criterion 2", oCriterion);
        assertEquals("SequentialId", "2", oCriterion.getSequentialId());
        assertEquals("Permit", true, oCriterion.isPermit());
        assertNotNull("UserRole", oCriterion.getUserRole());
        assertEquals("UserRole.Code", "106289002", oCriterion.getUserRole().getCode());
        assertEquals("OrganizationId", "http://www.happytoothdental.com", oCriterion.getOrganizationId());
        assertNotNull("ConfidentialityCode", oCriterion.getConfidentialityCode());
        assertEquals("ConfidentialityCode.Code", "N", oCriterion.getConfidentialityCode().getCode());
        assertNotNull("RuleStartDate", oCriterion.getRuleStartDate());
        assertEquals("RuleStartDate", "20090701", oCriterion.getRuleStartDate());
        assertNotNull("RuleEndDate", oCriterion.getRuleEndDate());
        assertEquals("RuleEndDate", "20091231", oCriterion.getRuleEndDate());

        // That is all that should be in here.  Everything else should be null - make sure that is true.
        //----------------------------------------------------------------------------------------------
        assertNull("UserRole.DisplayName", oCriterion.getUserRole().getDisplayName());
        assertNull("UserRole.CodeSystem", oCriterion.getUserRole().getCodeSystem());
        assertNull("UserRole.CodeSystemName", oCriterion.getUserRole().getCodeSystemName());
        assertNull("ConfidentialityCode.DisplayName", oCriterion.getConfidentialityCode().getDisplayName());
        assertNull("ConfidentialityCode.CodeSystem", oCriterion.getConfidentialityCode().getCodeSystem());
        assertNull("ConfidentialityCode.CodeSystemName", oCriterion.getConfidentialityCode().getCodeSystemName());
        assertNull("PolicyOID", oCriterion.getPolicyOID());
        assertNull("DocumentTypeCode", oCriterion.getDocumentTypeCode());
        assertNull("PurposeOfUse", oCriterion.getPurposeOfUse());
        assertNull("Action", oCriterion.getAction());
        assertNull("HomeCommunityId", oCriterion.getHomeCommunityId());
        assertNull("UniqueDocumentId", oCriterion.getUniqueDocumentId());
        assertNull("UserId", oCriterion.getUserId());
        assertNull("UserIdFormat", oCriterion.getUserIdFormat());
    }

    /**
     * This asserts the data in Fine Grained Policy Criterion 3 of the data.
     *
     * @param oCriterion The criterion to be validated.
     */
    public void assertValidExample1Criterion3(FineGrainedPolicyCriterionType oCriterion)
    {
        assertNotNull("Criterion 3", oCriterion);
        assertEquals("SequentialId", "3", oCriterion.getSequentialId());
        assertEquals("Permit", true, oCriterion.isPermit());
        assertEquals("HomeCommunityId", "1.1", oCriterion.getHomeCommunityId());
        assertEquals("UserId", "john.doe@somewhere.com", oCriterion.getUserId());
        assertEquals("UserIdFormat", UserIdFormatType.EMAIL , oCriterion.getUserIdFormat());
        assertNotNull("DocumentTypeCode", oCriterion.getDocumentTypeCode());
        assertEquals("DocumentTypeCode.Code", "45666666", oCriterion.getDocumentTypeCode().getCode());


        // That is all that should be in here.  Everything else should be null - make sure that is true.
        //----------------------------------------------------------------------------------------------
        assertNull("DocumentTypeCode.DisplayName", oCriterion.getDocumentTypeCode().getDisplayName());
        assertNull("DocumentTypeCode.CodeSystem", oCriterion.getDocumentTypeCode().getCodeSystem());
        assertNull("DocumentTypeCode.CodeSystemName", oCriterion.getDocumentTypeCode().getCodeSystemName());
        assertNull("UserRole", oCriterion.getUserRole());
        assertNull("PolicyOID", oCriterion.getPolicyOID());
        assertNull("PurposeOfUse", oCriterion.getPurposeOfUse());
        assertNull("ConfidentialityCode", oCriterion.getConfidentialityCode());
        assertNull("Action", oCriterion.getAction());
        assertNull("OrganizationId", oCriterion.getOrganizationId());
        assertNull("UniqueDocumentId", oCriterion.getUniqueDocumentId());
        assertNull("RuleStartDate", oCriterion.getRuleStartDate());
        assertNull("RuleEndDate", oCriterion.getRuleEndDate());
    }

    /**
     * This asserts the data in Fine Grained Policy Criterion 4 of the data.
     *
     * @param oCriterion The criterion to be validated.
     */
    public void assertValidExample1Criterion4(FineGrainedPolicyCriterionType oCriterion)
    {
        assertNotNull("Criterion 4", oCriterion);
        assertEquals("SequentialId", "4", oCriterion.getSequentialId());
        assertEquals("Permit", true, oCriterion.isPermit());
        assertEquals("UserId", "cn=John Doe, ou=People, o=Somewhere, c=us", oCriterion.getUserId());
        assertEquals("UserIdFormat", UserIdFormatType.X_500 , oCriterion.getUserIdFormat());
        assertNotNull("PurposeOfUse", oCriterion.getPurposeOfUse());
        assertEquals("PurposeOfUse.Code", "Treatment", oCriterion.getPurposeOfUse().getCode());
        assertEquals("UniqueDocumentId", "1234567", oCriterion.getUniqueDocumentId());

        // That is all that should be in here.  Everything else should be null - make sure that is true.
        //----------------------------------------------------------------------------------------------
        assertNull("PurposeOfUse.DisplayName", oCriterion.getPurposeOfUse().getDisplayName());
        assertNull("PurposeOfUse.CodeSystem", oCriterion.getPurposeOfUse().getCodeSystem());
        assertNull("PurposeOfUse.CodeSystemName", oCriterion.getPurposeOfUse().getCodeSystemName());
        assertNull("UserRole", oCriterion.getUserRole());
        assertNull("PolicyOID", oCriterion.getPolicyOID());
        assertNull("DocumentTypeCode", oCriterion.getDocumentTypeCode());
        assertNull("ConfidentialityCode", oCriterion.getConfidentialityCode());
        assertNull("Action", oCriterion.getAction());
        assertNull("OrganizationId", oCriterion.getOrganizationId());
        assertNull("HomeCommunityId", oCriterion.getHomeCommunityId());
        assertNull("RuleStartDate", oCriterion.getRuleStartDate());
        assertNull("RuleEndDate", oCriterion.getRuleEndDate());
    }

    /**
     * This asserts the data in Fine Grained Policy Criterion 5 of the data.
     *
     * @param oCriterion The criterion to be validated.
     */
    public void assertValidExample1Criterion5(FineGrainedPolicyCriterionType oCriterion)
    {
        assertNotNull("Criterion 5", oCriterion);
        assertEquals("SequentialId", "5", oCriterion.getSequentialId());
        assertEquals("Permit", false, oCriterion.isPermit());

        // That is all that should be in here.  Everything else should be null - make sure that is true.
        //----------------------------------------------------------------------------------------------
        assertNull("UserRole", oCriterion.getUserRole());
        assertNull("PolicyOID", oCriterion.getPolicyOID());
        assertNull("DocumentTypeCode", oCriterion.getDocumentTypeCode());
        assertNull("PurposeOfUse", oCriterion.getPurposeOfUse());
        assertNull("ConfidentialityCode", oCriterion.getConfidentialityCode());
        assertNull("Action", oCriterion.getAction());
        assertNull("OrganizationId", oCriterion.getOrganizationId());
        assertNull("HomeCommunityId", oCriterion.getHomeCommunityId());
        assertNull("UniqueDocumentId", oCriterion.getUniqueDocumentId());
        assertNull("RuleStartDate", oCriterion.getRuleStartDate());
        assertNull("RuleEndDate", oCriterion.getRuleEndDate());
        assertNull("UserId", oCriterion.getUserId());
        assertNull("UserIdFormat", oCriterion.getUserIdFormat());
    }

    /**
     * This validates that the information in the FineGrainedPolicyCriteria is what
     * we expected it to be.
     *
     * @param oFineGrainedPolicyCriteria The policy to validate.
     */
    public void assertValidExample1FineGrainedPolicyCriteria(FineGrainedPolicyCriteriaType oFineGrainedPolicyCriteria)
    {
        assertNotNull("FineGrainedPolicyCriteria", oFineGrainedPolicyCriteria);
        assertEquals("Size of FineGrainedPolicyCriterion", 5, oFineGrainedPolicyCriteria.getFineGrainedPolicyCriterion().size());

        for (int i = 0; i < oFineGrainedPolicyCriteria.getFineGrainedPolicyCriterion().size(); i++)
        {
            FineGrainedPolicyCriterionType oCriterion = oFineGrainedPolicyCriteria.getFineGrainedPolicyCriterion().get(i);

            switch (i + 1)      // 0 Offset
            {
                case 1:
                {
                    assertValidExample1Criterion1(oCriterion);
                    break;
                }
                case 2:
                {
                    assertValidExample1Criterion2(oCriterion);
                    break;
                }
                case 3:
                {
                    assertValidExample1Criterion3(oCriterion);
                    break;
                }
                case 4:
                {
                    assertValidExample1Criterion4(oCriterion);
                    break;
                }
                case 5:
                {
                    assertValidExample1Criterion5(oCriterion);
                    break;
                }
                default:
                {
                    fail("Unexpected rule was encountered.");
                    break;
                }
            }
        }

        

    }

    /**
     * Test out the ExtractPatientPreferences method.
     * 
     */
    @Test
    public void testExtractPatientPreferences()
    {
        final Log mockLog = context.mock(Log.class);

        XACMLExtractor oExtractor = new XACMLExtractor()
        {
            @Override
            protected Log createLogger()
            {
                return mockLog;
            }
        };

        // Test Policy 1
        //--------------
        PatientPreferencesType oPtPref = null;
        try
        {
            oPtPref = oExtractor.extractPatientPreferences(oXACMLPolicy1);
        }
        catch (Exception e)
        {
            fail("Received unexpected exception when extracting patient preferences.  Error: " + e.getMessage());
        }

        assertNotNull("PatientPreferences", oPtPref);
        assertEquals("AssigningAuthority", "1.1", oPtPref.getAssigningAuthority());
        assertEquals("PatientId", "D123401", oPtPref.getPatientId());
        assertNotNull("FineGrainedPolicyMetadata", oPtPref.getFineGrainedPolicyMetadata());
        assertEquals("PolicyOIDXacml", "12345678-1234-1234-1234-123456781234", oPtPref.getFineGrainedPolicyMetadata().getPolicyOID());
        assertValidExample1FineGrainedPolicyCriteria(oPtPref.getFineGrainedPolicyCriteria());


        // Test Policy 2
        //---------------
        oPtPref = null;
        try
        {
            oPtPref = oExtractor.extractPatientPreferences(oXACMLPolicy2);
        }
        catch (Exception e)
        {
            fail("Received unexpected exception when extracting patient preferences.  Error: " + e.getMessage());
        }

        assertNotNull("PatientPreferences", oPtPref);
        assertEquals("AssigningAuthority", "1.1", oPtPref.getAssigningAuthority());
        assertEquals("PatientId", "D123401", oPtPref.getPatientId());
        assertNotNull("FineGrainedPolicyMetadata", oPtPref.getFineGrainedPolicyMetadata());
        assertEquals("PolicyOIDXacml", "12345678-1234-1234-1234-123456781234", oPtPref.getFineGrainedPolicyMetadata().getPolicyOID());
        assertEquals("OptIn", false, oPtPref.isOptIn());
        assertNull("FineGranedPolicyCriteria", oPtPref.getFineGrainedPolicyCriteria());
        
    }
}
