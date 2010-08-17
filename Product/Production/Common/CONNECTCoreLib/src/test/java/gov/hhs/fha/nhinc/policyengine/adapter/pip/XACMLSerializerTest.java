package gov.hhs.fha.nhinc.policyengine.adapter.pip;

import oasis.names.tc.xacml._2_0.policy.schema.os.PolicyType;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.runner.RunWith;
import org.jmock.Mockery;
import org.apache.commons.logging.Log;
import org.junit.Ignore;

/**
 * This class tests the XACMLSerializer methods.
 *
 * @author Les Westberg
 */
@RunWith(JMock.class)
@Ignore
public class XACMLSerializerTest
{
    Mockery context = new JUnit4Mockery();
    static String XACML_EXAMPLE_1 = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><Policy RuleCombiningAlgId=\"urn:oasis:names:tc:xacml:1.0:rule-combining-algorithm:first-applicable\" PolicyId=\"12345678-1234-1234-1234-123456781234\" xmlns=\"urn:oasis:names:tc:xacml:2.0:policy:schema:os\"><Description>CONSUMER CONSENT POLICY</Description><Target><Resources><Resource><ResourceMatch MatchId=\"http://www.hhs.gov/healthit/nhin/function#instance-identifier-equal\"><AttributeValue DataType=\"urn:hl7-org:v3#II\"><hl7:PatientId extension=\"D123401\" root=\"1.1\" xmlns=\"\" xmlns:ns3=\"urn:oasis:names:tc:xacml:2.0:policy:schema:os\" xmlns:hl7=\"urn:hl7-org:v3\"/>          </AttributeValue><ResourceAttributeDesignator DataType=\"urn:hl7-org:v3#II\" AttributeId=\"http://www.hhs.gov/healthit/nhin#subject-id\"/></ResourceMatch></Resource></Resources><Actions><Action><ActionMatch MatchId=\"urn:oasis:names:tc:xacml:1.0:function:anyURI-equal\"><AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#anyURI\">            urn:ihe:iti:2007:CrossGatewayRetrieve          </AttributeValue><ActionAttributeDesignator DataType=\"http://www.w3.org/2001/XMLSchema#anyURI\" AttributeId=\"urn:oasis:names:tc:xacml:2.0:action\"/></ActionMatch></Action><Action><ActionMatch MatchId=\"urn:oasis:names:tc:xacml:1.0:function:anyURI-equal\"><AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#anyURI\">            urn:ihe:iti:2007:CrossGatewayQuery          </AttributeValue><ActionAttributeDesignator DataType=\"http://www.w3.org/2001/XMLSchema#anyURI\" AttributeId=\"urn:oasis:names:tc:xacml:2.0:action\"/></ActionMatch></Action></Actions></Target><Rule Effect=\"Permit\" RuleId=\"1\"><Description>Rule specifying fine grained criteria.</Description><Target><Subjects><Subject><SubjectMatch MatchId=\"urn:oasis:names:tc:xacml:1.0:function:string-equal\"><AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">112247003</AttributeValue><SubjectAttributeDesignator DataType=\"http://www.w3.org/2001/XMLSchema#string\" AttributeId=\"urn:oasis:names:tc:xacml:2.0:subject:role\"/></SubjectMatch></Subject></Subjects></Target></Rule><Rule Effect=\"Permit\" RuleId=\"2\"><Description>Rule specifying fine grained criteria.</Description><Target><Subjects><Subject><SubjectMatch MatchId=\"urn:oasis:names:tc:xacml:1.0:function:string-equal\"><AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">106289002</AttributeValue><SubjectAttributeDesignator DataType=\"http://www.w3.org/2001/XMLSchema#string\" AttributeId=\"urn:oasis:names:tc:xacml:2.0:subject:role\"/></SubjectMatch></Subject><Subject><SubjectMatch MatchId=\"urn:oasis:names:tc:xacml:1.0:function:anyURI-equal\"><AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#anyURI\">http://www.happytoothdental.com</AttributeValue><SubjectAttributeDesignator DataType=\"http://www.w3.org/2001/XMLSchema#anyURI\" AttributeId=\"urn:oasis:names:tc:xspa:1.0:subject:organization-id\"/></SubjectMatch></Subject></Subjects><Resources><Resource><ResourceMatch MatchId=\"urn:oasis:names:tc:xacml:1.0:function:string-equal\"><AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">N</AttributeValue><ResourceAttributeDesignator DataType=\"http://www.w3.org/2001/XMLSchema#string\" AttributeId=\"urn:oasis:names:tc:xspa:1.0:resource:patient:hl7:confidentiality-code\"/></ResourceMatch></Resource></Resources><Environments><Environment><EnvironmentMatch MatchId=\"urn:oasis:names:tc:xacml:1.0:function:date-greather-than-or-equal\"><AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#date\">2009-07-01T00:00:00.000</AttributeValue><EnvironmentAttributeDesignator DataType=\"http://www.w3.org/2001/XMLSchema#date\" AttributeId=\"http://www.hhs.gov/healthit/nhin#rule-start-date\"/></EnvironmentMatch></Environment><Environment><EnvironmentMatch MatchId=\"urn:oasis:names:tc:xacml:1.0:function:date-less-than-or-equal\"><AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#date\">2009-12-31T00:00:00.000</AttributeValue><EnvironmentAttributeDesignator DataType=\"http://www.w3.org/2001/XMLSchema#date\" AttributeId=\"http://www.hhs.gov/healthit/nhin#rule-end-date\"/></EnvironmentMatch></Environment></Environments></Target></Rule><Rule Effect=\"Permit\" RuleId=\"3\"><Description>Rule specifying fine grained criteria.</Description><Target><Subjects><Subject><SubjectMatch MatchId=\"urn:oasis:names:tc:xacml:1.0:function:anyURI-equal\"><AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#anyURI\">1.1</AttributeValue><SubjectAttributeDesignator DataType=\"http://www.w3.org/2001/XMLSchema#anyURI\" AttributeId=\"http://www.hhs.gov/healthit/nhin#HomeCommunityId\"/></SubjectMatch></Subject><Subject><SubjectMatch MatchId=\"urn:oasis:names:tc:xacml:1.0:function:rfc822Name-match\"><AttributeValue DataType=\"urn:oasis:names:tc:xacml:1.0:data-type:rfc822Name\">john.doe@somewhere.com</AttributeValue><SubjectAttributeDesignator DataType=\"urn:oasis:names:tc:xacml:1.0:data-type:rfc822Name\" AttributeId=\"urn:oasis:names:tc:xacml:1.0:subject:subject-id\"/></SubjectMatch></Subject></Subjects><Resources><Resource><ResourceMatch MatchId=\"urn:oasis:names:tc:xacml:1.0:function:string-equal\"><AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">45666666</AttributeValue><ResourceAttributeDesignator DataType=\"http://www.w3.org/2001/XMLSchema#string\" AttributeId=\"urn:oasis:names:tc:xspa:1.0:resource:hl7:type\"/></ResourceMatch></Resource></Resources></Target></Rule><Rule Effect=\"Permit\" RuleId=\"4\"><Description>Rule specifying fine grained criteria.</Description><Target><Subjects><Subject><SubjectMatch MatchId=\"urn:oasis:names:tc:xacml:1.0:function:x509Name-match\"><AttributeValue DataType=\"urn:oasis:names:tc:xacml:1.0:data-type:x500Name\">cn=John Doe, ou=People, o=Somewhere, c=us</AttributeValue><SubjectAttributeDesignator DataType=\"urn:oasis:names:tc:xacml:1.0:data-type:x500Name\" AttributeId=\"urn:oasis:names:tc:xacml:1.0:subject:subject-id\"/></SubjectMatch></Subject><Subject><SubjectMatch MatchId=\"urn:oasis:names:tc:xacml:1.0:function:string-equal\"><AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">Treatment</AttributeValue><SubjectAttributeDesignator DataType=\"http://www.w3.org/2001/XMLSchema#string\" AttributeId=\"urn:oasis:names:tc:xspa:1.0:subject:purposeofuse\"/></SubjectMatch></Subject></Subjects><Resources><Resource><ResourceMatch MatchId=\"urn:oasis:names:tc:xacml:1.0:function:string-equal\"><AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">1234567</AttributeValue><ResourceAttributeDesignator DataType=\"http://www.w3.org/2001/XMLSchema#string\" AttributeId=\"urn:oasis:names:tc:xacml:1.0:resource:resource-id\"/></ResourceMatch></Resource></Resources></Target></Rule><Rule Effect=\"Deny\" RuleId=\"5\"><Description>Rule specifying fine grained criteria.</Description><Target/></Rule></Policy>";

    public XACMLSerializerTest()
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
     * This method removes a known anomoly that occurs in the string by JAXB.
     * There is an extra namespace that gets placed in the hl7:PatientId field.
     * Since this is out of our control and benign, we are going to remove it
     * from the string before we do the comparison.
     *
     * @param sConsentXACML The XACML to be fixed.
     * @return The fixed XACML
     */
    private String FixHL7PatientIdField(String sConsentXACML)
    {
        String sNewConsentXACML = sConsentXACML;
        String sCompareString = "xmlns:ns4=\"urn:oasis:names:tc:xacml:2.0:policy:schema:os\" ";
        int iIndex = sConsentXACML.indexOf(sCompareString);
        if (iIndex >= 0)
        {
            sNewConsentXACML = sConsentXACML.substring(0, iIndex);
            sNewConsentXACML += sConsentXACML.substring(iIndex + sCompareString.length());
        }
        return sNewConsentXACML;

    }

    /**
     * This tests the serialize and deserialize methods.  Note normally
     * you would test them independently.  The easiest way to test this is to
     * deserialize the message and then serialize it again.  If you get back out
     * what you started with then it works.
     *
     */
    @Test
    public void testDeserializeAndSerialize()
    {
        final Log mockLog = context.mock(Log.class);

        XACMLSerializer oSerializer = new XACMLSerializer()
        {
            @Override
            protected Log createLogger()
            {
                return mockLog;
            }
        };

        try
        {
            PolicyType oConsentXACML = oSerializer.deserializeConsentXACMLDoc(XACML_EXAMPLE_1);
            assertNotNull(oConsentXACML);
            String sConsentXACML = oSerializer.serializeConsentXACMLDoc(oConsentXACML);
            // Note that the hl7:PatientId tag is an HL7 tag that is embedded into the a XACML Policy.
            // When you do a deserialization immediately followed by a serialization, JAXB adds an extra
            // namespace tag to that item.  Since we do not have control over that part of JAXB and
            // since this is benign and not important for this test.  So we are going to remove it
            // before we check to see if the strings are the same.
            //------------------------------------------------------------------------------------------------------------------------------------------------
            sConsentXACML = FixHL7PatientIdField(sConsentXACML);

//            System.out.println(XACML_EXAMPLE_1);
//            System.out.println(sConsentXACML);

            assertEquals(XACML_EXAMPLE_1, sConsentXACML);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            fail("An unexpected exception occurred.  Error: " + e.getMessage());
        }


    }
}
