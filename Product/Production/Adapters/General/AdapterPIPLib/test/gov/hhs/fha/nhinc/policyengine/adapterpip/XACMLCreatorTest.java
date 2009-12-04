package gov.hhs.fha.nhinc.policyengine.adapterpip;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.PatientPreferencesType;

import java.util.List;
import oasis.names.tc.xacml._2_0.policy.schema.os.ActionMatchType;
import oasis.names.tc.xacml._2_0.policy.schema.os.ActionType;
import oasis.names.tc.xacml._2_0.policy.schema.os.EffectType;
import oasis.names.tc.xacml._2_0.policy.schema.os.EnvironmentMatchType;
import oasis.names.tc.xacml._2_0.policy.schema.os.EnvironmentType;
import oasis.names.tc.xacml._2_0.policy.schema.os.PolicyType;
import oasis.names.tc.xacml._2_0.policy.schema.os.ResourceMatchType;
import oasis.names.tc.xacml._2_0.policy.schema.os.ResourceType;
import oasis.names.tc.xacml._2_0.policy.schema.os.RuleType;
import oasis.names.tc.xacml._2_0.policy.schema.os.SubjectMatchType;
import oasis.names.tc.xacml._2_0.policy.schema.os.SubjectType;
import oasis.names.tc.xacml._2_0.policy.schema.os.TargetType;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import org.junit.Test;
import org.w3c.dom.Element;
import static org.junit.Assert.*;




/**
 * This tests the XACMLCreator class.
 *
 * @author Les Westberg
 */
public class XACMLCreatorTest
{
    static PatientPreferencesType oPtPref1 = null;
    static PatientPreferencesType oPtPref2 = null;
    static String PTPREF_EXAMPLE_1 =
"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
"<PatientPreferences xmlns:ns16=\"http://www.hhs.gov/healthit/nhin/cdc\" xmlns=\"urn:gov:hhs:fha:nhinc:common:nhinccommonadapter\" xmlns:ns17=\"urn:gov:hhs:fha:nhinc:common:subscriptionb2overrideforcdc\" xmlns:ns14=\"http://docs.oasis-open.org/wsrf/bf-2\" xmlns:ns15=\"http://docs.oasis-open.org/wsn/t-1\" xmlns:ns9=\"urn:oasis:names:tc:ebxml-regrep:xsd:query:3.0\" xmlns:ns5=\"urn:oasis:names:tc:xacml:2.0:policy:schema:os\" xmlns:ns12=\"http://docs.oasis-open.org/wsn/b-2\" xmlns:ns6=\"urn:ihe:iti:xds-b:2007\" xmlns:ns13=\"urn:gov:hhs:fha:nhinc:common:subscriptionb2overridefordocuments\" xmlns:ns7=\"urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0\" xmlns:ns10=\"urn:oasis:names:tc:ebxml-regrep:xsd:lcm:3.0\" xmlns:ns8=\"urn:oasis:names:tc:ebxml-regrep:xsd:rs:3.0\" xmlns:ns11=\"http://www.w3.org/2005/08/addressing\" xmlns:ns2=\"urn:gov:hhs:fha:nhinc:common:nhinccommon\" xmlns:ns4=\"urn:oasis:names:tc:xacml:2.0:context:schema:os\" xmlns:ns3=\"http://nhinc.services.com/schema/auditmessage\">" +
"	<patientId>D123401</patientId>" +
"	<assigningAuthority>1.1</assigningAuthority>" +
"	<optIn>false</optIn>" +
"	<fineGrainedPolicyCriteria>" +
"		<fineGrainedPolicyCriterion>" +
"			<sequentialId>1</sequentialId>" +
"			<permit>true</permit>" +
"			<userRole>" +
"				<ns2:code>112247003</ns2:code>" +
"			</userRole>" +
"		</fineGrainedPolicyCriterion>" +
"		<fineGrainedPolicyCriterion>" +
"			<sequentialId>2</sequentialId>" +
"			<permit>true</permit>" +
"			<userRole>" +
"				<ns2:code>106289002</ns2:code>" +
"			</userRole>" +
"			<confidentialityCode>" +
"				<ns2:code>N</ns2:code>" +
"			</confidentialityCode>" +
"			<organizationId>http://www.happytoothdental.com</organizationId>" +
"			<ruleStartDate>20090701</ruleStartDate>" +
"			<ruleEndDate>20091231</ruleEndDate>" +
"		</fineGrainedPolicyCriterion>" +
"		<fineGrainedPolicyCriterion>" +
"			<sequentialId>3</sequentialId>" +
"			<permit>true</permit>" +
"			<documentTypeCode>" +
"				<ns2:code>45666666</ns2:code>" +
"			</documentTypeCode>" +
"			<homeCommunityId>1.1</homeCommunityId>" +
"			<userId>john.doe@somewhere.com</userId>" +
"			<userIdFormat>email</userIdFormat>" +
"		</fineGrainedPolicyCriterion>" +
"		<fineGrainedPolicyCriterion>" +
"			<sequentialId>4</sequentialId>" +
"			<permit>true</permit>" +
"			<purposeOfUse>" +
"				<ns2:code>Treatment</ns2:code>" +
"			</purposeOfUse>" +
"			<uniqueDocumentId>1234567</uniqueDocumentId>" +
"			<userId>cn=John Doe, ou=People, o=Somewhere, c=us</userId>" +
"			<userIdFormat>X500</userIdFormat>" +
"		</fineGrainedPolicyCriterion>" +
"		<fineGrainedPolicyCriterion>" +
"			<sequentialId>5</sequentialId>" +
"			<permit>false</permit>" +
"		</fineGrainedPolicyCriterion>" +
"	</fineGrainedPolicyCriteria>" +
"	<fineGrainedPolicyMetadata>" +
"		<policyOID>12345678-1234-1234-1234-123456781234</policyOID>" +
"	</fineGrainedPolicyMetadata>" +
"</PatientPreferences>";

    static String PTPREF_EXAMPLE_2 = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><PatientPreferences xmlns:ns16=\"http://www.hhs.gov/healthit/nhin/cdc\" xmlns=\"urn:gov:hhs:fha:nhinc:common:nhinccommonadapter\" xmlns:ns17=\"urn:gov:hhs:fha:nhinc:common:subscriptionb2overrideforcdc\" xmlns:ns14=\"http://docs.oasis-open.org/wsrf/bf-2\" xmlns:ns15=\"http://docs.oasis-open.org/wsn/t-1\" xmlns:ns9=\"urn:oasis:names:tc:ebxml-regrep:xsd:query:3.0\" xmlns:ns5=\"urn:oasis:names:tc:xacml:2.0:policy:schema:os\" xmlns:ns12=\"http://docs.oasis-open.org/wsn/b-2\" xmlns:ns6=\"urn:ihe:iti:xds-b:2007\" xmlns:ns13=\"urn:gov:hhs:fha:nhinc:common:subscriptionb2overridefordocuments\" xmlns:ns7=\"urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0\" xmlns:ns10=\"urn:oasis:names:tc:ebxml-regrep:xsd:lcm:3.0\" xmlns:ns8=\"urn:oasis:names:tc:ebxml-regrep:xsd:rs:3.0\" xmlns:ns11=\"http://www.w3.org/2005/08/addressing\" xmlns:ns2=\"urn:gov:hhs:fha:nhinc:common:nhinccommon\" xmlns:ns4=\"urn:oasis:names:tc:xacml:2.0:context:schema:os\" xmlns:ns3=\"http://nhinc.services.com/schema/auditmessage\"><patientId>D123401</patientId><assigningAuthority>1.1</assigningAuthority><optIn>false</optIn><fineGrainedPolicyMetadata><policyOID>12345678-1234-1234-1234-123456781234</policyOID></fineGrainedPolicyMetadata></PatientPreferences>";

    public XACMLCreatorTest()
    {
    }

    @BeforeClass
    public static void setUpClass() throws Exception
    {
        PatientPreferencesSerializer oSerializer = new PatientPreferencesSerializer();
        oPtPref1 = oSerializer.deserialize(PTPREF_EXAMPLE_1);
        oPtPref2 = oSerializer.deserialize(PTPREF_EXAMPLE_2);

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
     * This asserts a valid resource with the given data.
     *
     * @param oResource The resource being checked.
     * @param sMatchId The MatchId value.
     * @param sDataType The data type value.
     * @param sAttributeId The attribute Id value
     * @param sPatientId The patient ID to check.
     * @param sAssigningAuthority The assigning authority to check.
     */
    private void assertValidPatientIdResource(ResourceType oResource, String sMatchId, String sDataType, String sAttributeId, String sPatientId, String sAssigningAuthority)
    {
        assertNotNull("ResourceMatch", oResource.getResourceMatch());
        assertEquals("Size of ResourceMatch", 1, oResource.getResourceMatch().size());

        ResourceMatchType oResMatch = oResource.getResourceMatch().get(0);
        assertEquals("'" + sAttributeId + "':MatchId", sMatchId, oResMatch.getMatchId());

        assertNotNull("'" + sAttributeId + "':AttributeValue", oResMatch.getAttributeValue());
        assertEquals("'" + sAttributeId + "':AttributeValue.DataType", sDataType, oResMatch.getAttributeValue().getDataType());

        assertNotNull("'" + sAttributeId + "':Content", oResMatch.getAttributeValue().getContent());
        assertTrue("'" + sAttributeId + "':Content.size >= 1", (oResMatch.getAttributeValue().getContent().size() >= 1));
        assertTrue("'" + sAttributeId + "':Content[0] is Element", (oResMatch.getAttributeValue().getContent().get(0) instanceof Element));
        Element oContent = (Element) oResMatch.getAttributeValue().getContent().get(0);
        assertEquals("'" + sAttributeId + "':Content.TagName", "hl7:PatientId", oContent.getTagName());
        assertEquals("'" + sAttributeId + "':Content.Attribute('root')", sAssigningAuthority, oContent.getAttribute("root"));
        assertEquals("'" + sAttributeId + "':Content.Attribute('extension')", sPatientId, oContent.getAttribute("extension"));

        assertNotNull("'" + sAttributeId + "':ResourceAttributeDesignator", oResMatch.getResourceAttributeDesignator());
        assertEquals("'" + sAttributeId + "':ResourceAttributeDesignator.AttributeId", sAttributeId, oResMatch.getResourceAttributeDesignator().getAttributeId());
        assertEquals("'" + sAttributeId + "':ResourceAttributeDesignator.DataType", sDataType, oResMatch.getResourceAttributeDesignator().getDataType());
    }

    /**
     * This asserts a valid resource with the given data.
     *
     * @param oResource The resource being checked.
     * @param sMatchId The MatchId value.
     * @param sDataType The data type value.
     * @param sAttributeId The attribute Id value
     * @param sContent The content of the data.
     */
    private void assertValidStringResource(ResourceType oResource, String sMatchId, String sDataType, String sAttributeId, String sContent)
    {
        assertNotNull("ResourceMatch", oResource.getResourceMatch());
        assertEquals("Size of ResourceMatch", 1, oResource.getResourceMatch().size());

        ResourceMatchType oResMatch = oResource.getResourceMatch().get(0);
        assertEquals("'" + sAttributeId + "':MatchId", sMatchId, oResMatch.getMatchId());

        assertNotNull("'" + sAttributeId + "':AttributeValue", oResMatch.getAttributeValue());
        assertEquals("'" + sAttributeId + "':AttributeValue.DataType", sDataType, oResMatch.getAttributeValue().getDataType());

        assertNotNull("'" + sAttributeId + "':Content", oResMatch.getAttributeValue().getContent());
        assertEquals("'" + sAttributeId + "':Content.size", 1, oResMatch.getAttributeValue().getContent().size());
        assertTrue("'" + sAttributeId + "':Content is String", (oResMatch.getAttributeValue().getContent().get(0) instanceof String));
        String sContentValue = (String) oResMatch.getAttributeValue().getContent().get(0);
        assertEquals("'" + sAttributeId + "':Content value", sContent, sContentValue);

        assertNotNull("'" + sAttributeId + "':ResourceAttributeDesignator", oResMatch.getResourceAttributeDesignator());
        assertEquals("'" + sAttributeId + "':ResourceAttributeDesignator.AttributeId", sAttributeId, oResMatch.getResourceAttributeDesignator().getAttributeId());
        assertEquals("'" + sAttributeId + "':ResourceAttributeDesignator.DataType", sDataType, oResMatch.getResourceAttributeDesignator().getDataType());
    }

    /**
     * This asserts a valid resource with the given data.
     *
     * @param oAction The action being checked.
     * @param sMatchId The MatchId value.
     * @param sDataType The data type value.
     * @param sAttributeId The attribute Id value
     * @param sContent The content of the data.
     */
    private void assertValidStringAction(ActionType oAction, String sMatchId, String sDataType, String sAttributeId, String sContent)
    {
        assertNotNull("ActionMatch", oAction.getActionMatch());
        assertEquals("Size of ActionMatch", 1, oAction.getActionMatch().size());

        ActionMatchType oActionMatch = oAction.getActionMatch().get(0);
        assertEquals("'" + sAttributeId + "':MatchId", sMatchId, oActionMatch.getMatchId());

        assertNotNull("'" + sAttributeId + "':AttributeValue", oActionMatch.getAttributeValue());
        assertEquals("'" + sAttributeId + "':AttributeValue.DataType", sDataType, oActionMatch.getAttributeValue().getDataType());

        assertNotNull("'" + sAttributeId + "':Content", oActionMatch.getAttributeValue().getContent());
        assertEquals("'" + sAttributeId + "':Content.size", 1, oActionMatch.getAttributeValue().getContent().size());
        assertTrue("'" + sAttributeId + "':Content is String", (oActionMatch.getAttributeValue().getContent().get(0) instanceof String));
        String sContentValue = (String) oActionMatch.getAttributeValue().getContent().get(0);
        assertEquals("'" + sAttributeId + "':Content value", sContent, sContentValue);

        assertNotNull("'" + sAttributeId + "':ActionAttributeDesignator", oActionMatch.getActionAttributeDesignator());
        assertEquals("'" + sAttributeId + "':ActionAttributeDesignator.AttributeId", sAttributeId, oActionMatch.getActionAttributeDesignator().getAttributeId());
        assertEquals("'" + sAttributeId + "':ActionAttributeDesignator.DataType", sDataType, oActionMatch.getActionAttributeDesignator().getDataType());
    }

    /**
     * This asserts a valid Environment with the given data.
     *
     * @param oEnvironment The environment being checked.
     * @param sMatchId The MatchId value.
     * @param sDataType The data type value.
     * @param sAttributeId The attribute Id value
     * @param sContent The content of the data.
     */
    private void assertValidStringEnvironment(EnvironmentType oEnvironment, String sMatchId, String sDataType, String sAttributeId, String sContent)
    {
        assertNotNull("EnvironmentMatch", oEnvironment.getEnvironmentMatch());
        assertEquals("Size of EnvironmentMatch", 1, oEnvironment.getEnvironmentMatch().size());

        EnvironmentMatchType oEnvironmentMatch = oEnvironment.getEnvironmentMatch().get(0);
        assertEquals("'" + sAttributeId + "':MatchId", sMatchId, oEnvironmentMatch.getMatchId());

        assertNotNull("'" + sAttributeId + "':AttributeValue", oEnvironmentMatch.getAttributeValue());
        assertEquals("'" + sAttributeId + "':AttributeValue.DataType", sDataType, oEnvironmentMatch.getAttributeValue().getDataType());

        assertNotNull("'" + sAttributeId + "':Content", oEnvironmentMatch.getAttributeValue().getContent());
        assertEquals("'" + sAttributeId + "':Content.size", 1, oEnvironmentMatch.getAttributeValue().getContent().size());
        assertTrue("'" + sAttributeId + "':Content is String", (oEnvironmentMatch.getAttributeValue().getContent().get(0) instanceof String));
        String sContentValue = (String) oEnvironmentMatch.getAttributeValue().getContent().get(0);
        assertEquals("'" + sAttributeId + "':Content value", sContent, sContentValue);

        assertNotNull("'" + sAttributeId + "':EnvironmentAttributeDesignator", oEnvironmentMatch.getEnvironmentAttributeDesignator());
        assertEquals("'" + sAttributeId + "':EnvironmentAttributeDesignator.AttributeId", sAttributeId, oEnvironmentMatch.getEnvironmentAttributeDesignator().getAttributeId());
        assertEquals("'" + sAttributeId + "':EnvironmentAttributeDesignator.DataType", sDataType, oEnvironmentMatch.getEnvironmentAttributeDesignator().getDataType());
    }


    /**
     * This asserts a valid subject with the given data.
     *
     * @param oSubject The action being checked.
     * @param sMatchId The MatchId value.
     * @param sDataType The data type value.
     * @param sAttributeId The attribute Id value
     * @param sContent The content of the data.
     */
    private void assertValidStringSubject(SubjectType oSubject, String sMatchId, String sDataType, String sAttributeId, String sContent)
    {
        assertNotNull("SubjectMatch", oSubject.getSubjectMatch());
        assertEquals("Size of SubjectMatch", 1, oSubject.getSubjectMatch().size());

        SubjectMatchType oSubjectMatch = oSubject.getSubjectMatch().get(0);
        assertEquals("'" + sAttributeId + "':MatchId", sMatchId, oSubjectMatch.getMatchId());

        assertNotNull("'" + sAttributeId + "':AttributeValue", oSubjectMatch.getAttributeValue());
        assertEquals("'" + sAttributeId + "':AttributeValue.DataType", sDataType, oSubjectMatch.getAttributeValue().getDataType());

        assertNotNull("'" + sAttributeId + "':Content", oSubjectMatch.getAttributeValue().getContent());
        assertEquals("'" + sAttributeId + "':Content.size", 1, oSubjectMatch.getAttributeValue().getContent().size());
        assertTrue("'" + sAttributeId + "':Content is String", (oSubjectMatch.getAttributeValue().getContent().get(0) instanceof String));
        String sContentValue = (String) oSubjectMatch.getAttributeValue().getContent().get(0);
        assertEquals("'" + sAttributeId + "':Content value", sContent, sContentValue);

        assertNotNull("'" + sAttributeId + "':SubjectAttributeDesignator", oSubjectMatch.getSubjectAttributeDesignator());
        assertEquals("'" + sAttributeId + "':SubjectAttributeDesignator.AttributeId", sAttributeId, oSubjectMatch.getSubjectAttributeDesignator().getAttributeId());
        assertEquals("'" + sAttributeId + "':SubjectAttributeDesignator.DataType", sDataType, oSubjectMatch.getSubjectAttributeDesignator().getDataType());
    }


    /**
     * This class validates the data in the Target to make sure it has what
     * we are expecting.
     *
     * @param oTarget The target information from the XACML policy
     */
    private void assertValidExampleTarget(TargetType oTarget)
    {
        assertNotNull("Target", oTarget);
        assertNotNull("Resources", oTarget.getResources());
        assertNotNull("Resource", oTarget.getResources().getResource());
        assertEquals("Size of Resource List", 1, oTarget.getResources().getResource().size());
        assertValidPatientIdResource(oTarget.getResources().getResource().get(0),
                                     "http://www.hhs.gov/healthit/nhin/function#instance-identifier-equal",
                                     "urn:hl7-org:v3#II", "http://www.hhs.gov/healthit/nhin#subject-id",
                                     "D123401", "1.1");

        assertNotNull("Actions", oTarget.getActions());
        assertNotNull("Actions.Action", oTarget.getActions().getAction());
        assertEquals("Actions.Action List Size = 2", 2, oTarget.getActions().getAction().size());
        ActionType oAction = oTarget.getActions().getAction().get(0);
        assertValidStringAction(oAction, "urn:oasis:names:tc:xacml:1.0:function:anyURI-equal",
                                "http://www.w3.org/2001/XMLSchema#anyURI",
                                "urn:oasis:names:tc:xacml:1.0:action:action-id",
                                "urn:ihe:iti:2007:CrossGatewayRetrieve");
        oAction = oTarget.getActions().getAction().get(1);
        assertValidStringAction(oAction, "urn:oasis:names:tc:xacml:1.0:function:anyURI-equal",
                                "http://www.w3.org/2001/XMLSchema#anyURI",
                                "urn:oasis:names:tc:xacml:1.0:action:action-id",
                                "urn:ihe:iti:2007:CrossGatewayQuery");
    }
    
    /**
     * Validate Example 1 Rule 1 to be sure that the data is correct.
     * 
     * @param oRule The rule to be validated.
     */
    private void assertValidExample1Rule1(RuleType oRule)
    {
        assertEquals("Rule.RuleId: ", "1", oRule.getRuleId());
        assertEquals("Rule.Effect:", EffectType.PERMIT, oRule.getEffect());
        assertEquals("Rule.Description:", "Rule specifying fine grained criteria.", oRule.getDescription());
        assertNotNull("Rule.Target", oRule.getTarget());
        assertNotNull("Rule.Target.Subjects", oRule.getTarget().getSubjects());
        assertNotNull("Rule.Target.Subjects.Subject", oRule.getTarget().getSubjects().getSubject());
        assertEquals("Rule.Target.Sibjects.Subject size = 1", 1, oRule.getTarget().getSubjects().getSubject().size());
        assertValidStringSubject(oRule.getTarget().getSubjects().getSubject().get(0),
                                 "urn:oasis:names:tc:xacml:1.0:function:string-equal",
                                 "http://www.w3.org/2001/XMLSchema#string",
                                 "urn:oasis:names:tc:xacml:2.0:subject:role",
                                 "112247003");


        assertNull("Rule.Target.Actions", oRule.getTarget().getActions());
        assertNull("Rule.Target.Environments", oRule.getTarget().getEnvironments());
        assertNull("Rule.Target.Resources", oRule.getTarget().getResources());

    }

    /**
     * Validate Example 1 Rule 2 to be sure that the data is correct.
     *
     * @param oRule The rule to be validated.
     */
    private void assertValidExample1Rule2(RuleType oRule)
    {
        assertEquals("Rule.RuleId: ", "2", oRule.getRuleId());
        assertEquals("Rule.Effect:", EffectType.PERMIT, oRule.getEffect());
        assertEquals("Rule.Description:", "Rule specifying fine grained criteria.", oRule.getDescription());
        assertNotNull("Rule.Target", oRule.getTarget());
        assertNotNull("Rule.Target.Subjects", oRule.getTarget().getSubjects());
        assertNotNull("Rule.Target.Subjects.Subject", oRule.getTarget().getSubjects().getSubject());
        assertEquals("Rule.Target.Subjects.Subject size = 2", 2, oRule.getTarget().getSubjects().getSubject().size());
        assertValidStringSubject(oRule.getTarget().getSubjects().getSubject().get(0),
                                 "urn:oasis:names:tc:xacml:1.0:function:string-equal",
                                 "http://www.w3.org/2001/XMLSchema#string",
                                 "urn:oasis:names:tc:xacml:2.0:subject:role",
                                 "106289002");
        assertValidStringSubject(oRule.getTarget().getSubjects().getSubject().get(1),
                                 "urn:oasis:names:tc:xacml:1.0:function:anyURI-equal",
                                 "http://www.w3.org/2001/XMLSchema#anyURI",
                                 "urn:oasis:names:tc:xspa:1.0:subject:organization-id",
                                 "http://www.happytoothdental.com");

        assertNotNull("Rule.Target.Resources", oRule.getTarget().getResources());
        assertNotNull("Rule.Target.Resources.Resource", oRule.getTarget().getResources().getResource());
        assertEquals("Rule.Target.Resources.resource size = 1", 1, oRule.getTarget().getResources().getResource().size());
        assertValidStringResource(oRule.getTarget().getResources().getResource().get(0),
                                 "urn:oasis:names:tc:xacml:1.0:function:string-equal",
                                 "http://www.w3.org/2001/XMLSchema#string",
                                 "urn:oasis:names:tc:xspa:1.0:resource:patient:hl7:confidentiality-code",
                                 "N");

        assertNotNull("Rule.Target.Environments", oRule.getTarget().getEnvironments());
        assertNotNull("Rule.Target.Environments.Environment", oRule.getTarget().getEnvironments().getEnvironment());
        assertEquals("Rule.Target.Environments.Environment size = 2", 2, oRule.getTarget().getEnvironments().getEnvironment().size());
        assertValidStringEnvironment(oRule.getTarget().getEnvironments().getEnvironment().get(0),
                                 "urn:oasis:names:tc:xacml:1.0:function:date-greater-than-or-equal",
                                 "http://www.w3.org/2001/XMLSchema#date",
                                 "http://www.hhs.gov/healthit/nhin#rule-start-date",
                                 "2009-07-01");
        assertValidStringEnvironment(oRule.getTarget().getEnvironments().getEnvironment().get(1),
                                 "urn:oasis:names:tc:xacml:1.0:function:date-less-than-or-equal",
                                 "http://www.w3.org/2001/XMLSchema#date",
                                 "http://www.hhs.gov/healthit/nhin#rule-end-date",
                                 "2009-12-31");

        assertNull("Rule.Target.Actions", oRule.getTarget().getActions());

    }

    /**
     * Validate Example 1 Rule 1 to be sure that the data is correct.
     *
     * @param oRule The rule to be validated.
     */
    private void assertValidExample1Rule3(RuleType oRule)
    {
        assertEquals("Rule.RuleId: ", "3", oRule.getRuleId());
        assertEquals("Rule.Effect:", EffectType.PERMIT, oRule.getEffect());
        assertEquals("Rule.Description:", "Rule specifying fine grained criteria.", oRule.getDescription());
        assertNotNull("Rule.Target", oRule.getTarget());
        assertNotNull("Rule.Target.Subjects", oRule.getTarget().getSubjects());
        assertNotNull("Rule.Target.Subjects.Subject", oRule.getTarget().getSubjects().getSubject());
        assertEquals("Rule.Target.Sibjects.Subject size = 2", 2, oRule.getTarget().getSubjects().getSubject().size());
        assertValidStringSubject(oRule.getTarget().getSubjects().getSubject().get(0),
                                 "urn:oasis:names:tc:xacml:1.0:function:anyURI-equal",
                                 "http://www.w3.org/2001/XMLSchema#anyURI",
                                 "http://www.hhs.gov/healthit/nhin#HomeCommunityId",
                                 "1.1");
        assertValidStringSubject(oRule.getTarget().getSubjects().getSubject().get(1),
                                 "urn:oasis:names:tc:xacml:1.0:function:rfc822Name-match",
                                 "urn:oasis:names:tc:xacml:1.0:data-type:rfc822Name",
                                 "urn:oasis:names:tc:xacml:1.0:subject:subject-id",
                                 "john.doe@somewhere.com");

        assertNotNull("Rule.Target.Resources", oRule.getTarget().getResources());
        assertNotNull("Rule.Target.Resources.Resource", oRule.getTarget().getResources().getResource());
        assertEquals("Rule.Target.Resources.resource size = 1", 1, oRule.getTarget().getResources().getResource().size());
        assertValidStringResource(oRule.getTarget().getResources().getResource().get(0),
                                 "urn:oasis:names:tc:xacml:1.0:function:string-equal",
                                 "http://www.w3.org/2001/XMLSchema#string",
                                 "urn:oasis:names:tc:xspa:1.0:resource:hl7:type",
                                 "45666666");

        assertNull("Rule.Target.Actions", oRule.getTarget().getActions());
        assertNull("Rule.Target.Environments", oRule.getTarget().getEnvironments());

    }


    /**
     * Validate Example 1 Rule 4 to be sure that the data is correct.
     *
     * @param oRule The rule to be validated.
     */
    private void assertValidExample1Rule4(RuleType oRule)
    {
        assertEquals("Rule.RuleId: ", "4", oRule.getRuleId());
        assertEquals("Rule.Effect:", EffectType.PERMIT, oRule.getEffect());
        assertEquals("Rule.Description:", "Rule specifying fine grained criteria.", oRule.getDescription());
        assertNotNull("Rule.Target", oRule.getTarget());
        assertNotNull("Rule.Target.Subjects", oRule.getTarget().getSubjects());
        assertNotNull("Rule.Target.Subjects.Subject", oRule.getTarget().getSubjects().getSubject());
        assertEquals("Rule.Target.Sibjects.Subject size = 2", 2, oRule.getTarget().getSubjects().getSubject().size());
        assertValidStringSubject(oRule.getTarget().getSubjects().getSubject().get(0),
                                 "urn:oasis:names:tc:xacml:1.0:function:x500Name-match",
                                 "urn:oasis:names:tc:xacml:1.0:data-type:x500Name",
                                 "urn:oasis:names:tc:xacml:1.0:subject:subject-id",
                                 "cn=John Doe, ou=People, o=Somewhere, c=us");
        assertValidStringSubject(oRule.getTarget().getSubjects().getSubject().get(1),
                                 "urn:oasis:names:tc:xacml:1.0:function:string-equal",
                                 "http://www.w3.org/2001/XMLSchema#string",
                                 "urn:oasis:names:tc:xspa:1.0:subject:purposeofuse",
                                 "Treatment");

        assertNotNull("Rule.Target.Resources", oRule.getTarget().getResources());
        assertNotNull("Rule.Target.Resources.Resource", oRule.getTarget().getResources().getResource());
        assertEquals("Rule.Target.Resources.resource size = 1", 1, oRule.getTarget().getResources().getResource().size());
        assertValidStringResource(oRule.getTarget().getResources().getResource().get(0),
                                 "urn:oasis:names:tc:xacml:1.0:function:string-equal",
                                 "http://www.w3.org/2001/XMLSchema#string",
                                 "urn:oasis:names:tc:xacml:1.0:resource:resource-id",
                                 "1234567");

        assertNull("Rule.Target.Actions", oRule.getTarget().getActions());
        assertNull("Rule.Target.Environments", oRule.getTarget().getEnvironments());

    }

    /**
     * Validate Example 1 Rule 5 to be sure that the data is correct.
     *
     * @param oRule The rule to be validated.
     */
    private void assertValidExample1Rule5(RuleType oRule)
    {
        assertEquals("Rule.RuleId: ", "5", oRule.getRuleId());
        assertEquals("Rule.Effect:", EffectType.DENY, oRule.getEffect());
        assertEquals("Rule.Description:", "Rule specifying fine grained criteria.", oRule.getDescription());
        assertNotNull("Rule.Target", oRule.getTarget());

        assertNull("Rule.Target.Subjects", oRule.getTarget().getSubjects());
        assertNull("Rule.Target.Actions", oRule.getTarget().getActions());
        assertNull("Rule.Target.Environments", oRule.getTarget().getEnvironments());
        assertNull("Rule.Target.Resources", oRule.getTarget().getResources());
    }

    /**
     * Validate Example 2 Rule 1 to be sure that the data is correct.
     *
     * @param oRule The rule to be validated.
     */
    private void assertValidExample2Rule1(RuleType oRule)
    {
        assertEquals("Rule.RuleId: ", "1", oRule.getRuleId());
        assertEquals("Rule.Effect:", EffectType.DENY, oRule.getEffect());
        assertEquals("Rule.Description:", "Rule specifying preference for global opt in/opt out of NHIN.", oRule.getDescription());
        assertNotNull("Rule.Target", oRule.getTarget());

        assertNull("Rule.Target.Subjects", oRule.getTarget().getSubjects());
        assertNull("Rule.Target.Actions", oRule.getTarget().getActions());
        assertNull("Rule.Target.Environments", oRule.getTarget().getEnvironments());
        assertNull("Rule.Target.Resources", oRule.getTarget().getResources());
    }


    /**
     * This validates that the rules are correct.
     *
     * @param oConsentXACML The XACML consent document.
     */
    private void assertValidExample1Rules(PolicyType oConsentXACML)
    {
        assertNotNull("Rule List", oConsentXACML.getCombinerParametersOrRuleCombinerParametersOrVariableDefinition());
        assertEquals("Size of Rule List = 5", 5, oConsentXACML.getCombinerParametersOrRuleCombinerParametersOrVariableDefinition().size());
        List<Object> olRuleObject = oConsentXACML.getCombinerParametersOrRuleCombinerParametersOrVariableDefinition();

        assertTrue("Rule 1 correct object type.", olRuleObject.get(0) instanceof RuleType);
        assertValidExample1Rule1((RuleType)olRuleObject.get(0));

        assertTrue("Rule 2 correct object type.", olRuleObject.get(1) instanceof RuleType);
        assertValidExample1Rule2((RuleType)olRuleObject.get(1));

        assertTrue("Rule 3 correct object type.", olRuleObject.get(2) instanceof RuleType);
        assertValidExample1Rule3((RuleType)olRuleObject.get(2));

        assertTrue("Rule 4 correct object type.", olRuleObject.get(3) instanceof RuleType);
        assertValidExample1Rule4((RuleType)olRuleObject.get(3));

        assertTrue("Rule 5 correct object type.", olRuleObject.get(4) instanceof RuleType);
        assertValidExample1Rule5((RuleType)olRuleObject.get(4));
    }

    /**
     * This validates that the rules are correct.
     *
     * @param oConsentXACML The XACML consent document.
     */
    private void assertValidExample2Rules(PolicyType oConsentXACML)
    {
        assertNotNull("Rule List", oConsentXACML.getCombinerParametersOrRuleCombinerParametersOrVariableDefinition());
        assertEquals("Size of Rule List = 1", 1, oConsentXACML.getCombinerParametersOrRuleCombinerParametersOrVariableDefinition().size());
        List<Object> olRuleObject = oConsentXACML.getCombinerParametersOrRuleCombinerParametersOrVariableDefinition();

        assertTrue("Rule 1 correct object type.", olRuleObject.get(0) instanceof RuleType);
        assertValidExample2Rule1((RuleType)olRuleObject.get(0));
    }

    /**
     * Test out the CreateConsentXACMLDoc method.
     *
     */
    @Test
    public void testCreateXACML()
    {
        XACMLCreator oCreator = new XACMLCreator();

        // Test Policy 1
        //--------------
        PolicyType oConsentXACML = null;
        try
        {
            oConsentXACML = oCreator.createConsentXACMLDoc(oPtPref1);
        }
        catch (Exception e)
        {
            fail("Received unexpected exception when creating XACML.  Error: " + e.getMessage());
        }

        assertNotNull("Policy", oConsentXACML);
        assertEquals("PolicyId", "12345678-1234-1234-1234-123456781234", oConsentXACML.getPolicyId());
        assertEquals("RuleCombiningAlgId", "urn:oasis:names:tc:xacml:1.0:rule-combining-algorithm:first-applicable", oConsentXACML.getRuleCombiningAlgId());
        assertEquals("Description", "CONSUMER CONSENT POLICY", oConsentXACML.getDescription());
        assertValidExampleTarget(oConsentXACML.getTarget());
        assertValidExample1Rules(oConsentXACML);
        // Test code to extract a serialized message.
        //--------------------------------------------
//        XACMLSerializer oSerializer = new XACMLSerializer();
//        try
//        {
//            String sConsentXACML = oSerializer.serializeConsentXACMLDoc(oConsentXACML);
//            oConsentXACML = oSerializer.deserializeConsentXACMLDoc(sConsentXACML);
//        }
//        catch (Exception e)
//        {
//
//        }
        

        // Test Policy 2
        //---------------
        oConsentXACML = null;
        try
        {
            oConsentXACML = oCreator.createConsentXACMLDoc(oPtPref2);
        }
        catch (Exception e)
        {
            fail("Received unexpected exception when creating XACML.  Error: " + e.getMessage());
        }

        assertNotNull("Policy", oConsentXACML);
        assertEquals("PolicyId", "12345678-1234-1234-1234-123456781234", oConsentXACML.getPolicyId());
        assertEquals("RuleCombiningAlgId", "urn:oasis:names:tc:xacml:1.0:rule-combining-algorithm:first-applicable", oConsentXACML.getRuleCombiningAlgId());
        assertEquals("Description", "CONSUMER CONSENT POLICY", oConsentXACML.getDescription());
        assertValidExampleTarget(oConsentXACML.getTarget());
        assertValidExample2Rules(oConsentXACML);

    }
}
