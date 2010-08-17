package gov.hhs.fha.nhinc.policyengine.adapter.pip;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.PatientPreferencesType;
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


/**
 * This class tests the patient preferences serializer class.
 *
 * @author Les Westberg
 */
@RunWith(JMock.class)
public class PatientPreferencesSerializerTest
{
    Mockery context = new JUnit4Mockery();
    static String PTPREF_EXAMPLE_1 = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><PatientPreferences xmlns:ns16=\"http://www.hhs.gov/healthit/nhin/cdc\" xmlns=\"urn:gov:hhs:fha:nhinc:common:nhinccommonadapter\" xmlns:ns17=\"urn:gov:hhs:fha:nhinc:common:subscriptionb2overrideforcdc\" xmlns:ns14=\"http://docs.oasis-open.org/wsrf/bf-2\" xmlns:ns15=\"http://docs.oasis-open.org/wsn/t-1\" xmlns:ns9=\"urn:oasis:names:tc:ebxml-regrep:xsd:query:3.0\" xmlns:ns5=\"urn:oasis:names:tc:xacml:2.0:policy:schema:os\" xmlns:ns12=\"http://docs.oasis-open.org/wsn/b-2\" xmlns:ns6=\"urn:ihe:iti:xds-b:2007\" xmlns:ns13=\"urn:gov:hhs:fha:nhinc:common:subscriptionb2overridefordocuments\" xmlns:ns7=\"urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0\" xmlns:ns10=\"urn:oasis:names:tc:ebxml-regrep:xsd:lcm:3.0\" xmlns:ns8=\"urn:oasis:names:tc:ebxml-regrep:xsd:rs:3.0\" xmlns:ns11=\"http://www.w3.org/2005/08/addressing\" xmlns:ns2=\"urn:gov:hhs:fha:nhinc:common:nhinccommon\" xmlns:ns4=\"urn:oasis:names:tc:xacml:2.0:context:schema:os\" xmlns:ns3=\"http://nhinc.services.com/schema/auditmessage\"><patientId>D123401</patientId><assigningAuthority>1.1</assigningAuthority><optIn>false</optIn><fineGrainedPolicyCriteria><fineGrainedPolicyCriterion><sequentialId>1</sequentialId><permit>true</permit><userRole><ns2:code>112247003</ns2:code></userRole></fineGrainedPolicyCriterion><fineGrainedPolicyCriterion><sequentialId>2</sequentialId><permit>true</permit><userRole><ns2:code>106289002</ns2:code></userRole><confidentialityCode><ns2:code>N</ns2:code></confidentialityCode><organizationId>http://www.happytoothdental.com</organizationId><ruleStartDate>2009-07-01T00:00:00.000</ruleStartDate><ruleEndDate>2009-12-31T00:00:00.000</ruleEndDate></fineGrainedPolicyCriterion><fineGrainedPolicyCriterion><sequentialId>3</sequentialId><permit>true</permit><documentTypeCode><ns2:code>45666666</ns2:code></documentTypeCode><homeCommunityId>1.1</homeCommunityId><userId>john.doe@somewhere.com</userId><userIdFormat>email</userIdFormat></fineGrainedPolicyCriterion><fineGrainedPolicyCriterion><sequentialId>4</sequentialId><permit>true</permit><purposeOfUse><ns2:code>Treatment</ns2:code></purposeOfUse><uniqueDocumentId>1234567</uniqueDocumentId><userId>cn=John Doe, ou=People, o=Somewhere, c=us</userId><userIdFormat>X500</userIdFormat></fineGrainedPolicyCriterion><fineGrainedPolicyCriterion><sequentialId>5</sequentialId><permit>false</permit></fineGrainedPolicyCriterion></fineGrainedPolicyCriteria><fineGrainedPolicyMetadata><policyOID>12345678-1234-1234-1234-123456781234</policyOID></fineGrainedPolicyMetadata></PatientPreferences>";

    public PatientPreferencesSerializerTest()
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
     * This method removes the xmlns namespace tags from the XML message.
     *
     * @param sXML
     * @return the XML without the xmlns tags.
     */
    private String removeXMLNSTags(String sXML)
    {
        boolean bDone = false;
        StringBuffer sbReturnXML = new StringBuffer();

        int iStartIdx = 0;
        while (!bDone)
        {
            int iIndex = sXML.indexOf("xmlns", iStartIdx);
            if (iIndex >= 0)
            {
                sbReturnXML.append(sXML.substring(iStartIdx, iIndex));
                String sTemp = sbReturnXML.toString();
                String sTemp2 = sXML.substring(iIndex+5, iIndex+6);
                String sTemp3 = "";
                if ((iIndex >= 0) &&
                    ((iIndex + 5) < sXML.length()) &&
                    ((sXML.substring(iIndex+5, iIndex+6).equals(":")) ||
                     (sXML.substring(iIndex+5, iIndex+6).equals("="))))
                {
                    // Find the opening quote
                    //-----------------------
                    int iStartQuoteIdx = sXML.indexOf("\"", iIndex+5);
                    if (iStartQuoteIdx < 0)
                    {
                        fail("The xmlns tag did not have a starting quote.");
                    }

                    // Find the closing quote
                    //-----------------------
                    int iCloseQuoteIdx = sXML.indexOf("\"", iStartQuoteIdx+1);
                    if (iCloseQuoteIdx >= 0)
                    {
                        iStartIdx = iCloseQuoteIdx + 1;
                        if ((iStartIdx < sXML.length()) &&
                            (sXML.charAt(iStartIdx) == ' '))
                        {
                            iStartIdx++;        // skip the space
                        }
                        sTemp3 = sXML.substring(iIndex, iStartIdx);
                        String sTemp4 = "";
                    }
                    else
                    {
                        fail("Invalid XML string.  Missing closing quote.");
                    }
                }
            }
            else
            {
                sbReturnXML.append(sXML.substring(iStartIdx));
                bDone = true;
            }
        }

        return sbReturnXML.toString();

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

        PatientPreferencesSerializer oSerializer = new PatientPreferencesSerializer()
        {
            @Override
            protected Log createLogger()
            {
                return mockLog;
            }
        };

        try
        {
            PatientPreferencesType oPtPref = oSerializer.deserialize(PTPREF_EXAMPLE_1);
            assertNotNull(oPtPref);
            String sPtPref = oSerializer.serialize(oPtPref);

            // Since there is no consistency to the order of the xmlns tags, we need to remove them from the comparisonn.
            //-----------------------------------------------------------------------------------------------------------
            String PTPREF_EXAMPLE_1_CLEAN = removeXMLNSTags(PTPREF_EXAMPLE_1);
            String sPtPrefClean = removeXMLNSTags(sPtPref);
            assertEquals(PTPREF_EXAMPLE_1_CLEAN, sPtPrefClean);
        }
        catch (Exception e)
        {
            fail("An unexpected exception occurred.  Error: " + e.getMessage());
        }


    }
}
