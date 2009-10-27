package gov.hhs.fha.nhinc.policyengine.adapterpip;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.PatientPreferencesType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.StorePtConsentResponseType;
import java.io.StringReader;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.apache.commons.logging.Log;

/**
 *
 * @author webbn
 */
@RunWith(JMock.class)
public class AdapterPIPImplTest {
    Mockery context = new JUnit4Mockery(){{
        setImposteriser(ClassImposteriser.INSTANCE);
    }};

    public AdapterPIPImplTest() {
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
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void hello() {
        // Create mock objects
        final Log mockLog = context.mock(Log.class);
        final PatientConsentManager consentManager = context.mock(PatientConsentManager.class);

        AdapterPIPImpl pipImpl = new AdapterPIPImpl(){

            @Override
            protected Log createLogger()
            {
                return mockLog;
            }

            @Override
            protected PatientConsentManager getPatientConsentManager()
            {
                return consentManager;
            }

        };
        try
        {
            // Set expectations
            context.checking(new Expectations(){{
                allowing (mockLog).isDebugEnabled();
                allowing (mockLog).debug(with(any(String.class)));
                oneOf (consentManager).storePatientConsent(with(aNonNull(PatientPreferencesType.class)));
            }});
            
            gov.hhs.fha.nhinc.common.nhinccommonadapter.StorePtConsentRequestType request = loadRequestMessage(STORE_CONSENT_MESSAGE);
            StorePtConsentResponseType response = pipImpl.storePtConsent(request);
            assertNotNull("Store consent response was null", response);
            assertEquals("Status not as expected", "SUCCESS", response.getStatus());
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            fail("Exception calling storePtConsent: " + ex.getMessage());
        }
    }

    public gov.hhs.fha.nhinc.common.nhinccommonadapter.StorePtConsentRequestType loadRequestMessage(String message)
    {
        gov.hhs.fha.nhinc.common.nhinccommonadapter.StorePtConsentRequestType request = null;
        String contextPath = "gov.hhs.fha.nhinc.common.nhinccommonadapter";
        Object unmarshalledObject = null;

        try
        {
            JAXBContext jc = JAXBContext.newInstance(contextPath);
            javax.xml.bind.Unmarshaller unmarshaller = jc.createUnmarshaller();
            StringReader stringReader = new StringReader(message);
            unmarshalledObject = unmarshaller.unmarshal(stringReader);
            if (unmarshalledObject instanceof JAXBElement)
            {
                JAXBElement jaxb = (JAXBElement) unmarshalledObject;
                unmarshalledObject = jaxb.getValue();
            }
        } catch (Exception e)
        {
            unmarshalledObject = null;
            e.printStackTrace();
            fail("Exception unmarshalling store patient consent message: " + e.getMessage());
        }
        if(unmarshalledObject instanceof gov.hhs.fha.nhinc.common.nhinccommonadapter.StorePtConsentRequestType)
        {
            request = (gov.hhs.fha.nhinc.common.nhinccommonadapter.StorePtConsentRequestType)unmarshalledObject;
        }
        else
        {
            fail("Unmarshalled object is not a store patient consent message. Is: " + ((unmarshalledObject == null) ? "null" : unmarshalledObject.getClass().getName()));
        }
        return request;
    }

    private static final String STORE_CONSENT_MESSAGE =
        "<urn:StorePtConsentRequest xmlns:urn=\"urn:gov:hhs:fha:nhinc:common:nhinccommonadapter\">" +
        "	<urn:patientPreferences>" +
        "		<urn:patientId>ADPTPIPTST98769876Z</urn:patientId>" +
        "		<urn:assigningAuthority>1.1</urn:assigningAuthority>" +
        "		<urn:optIn>true</urn:optIn>" +
        "	</urn:patientPreferences>" +
        "</urn:StorePtConsentRequest>";

}