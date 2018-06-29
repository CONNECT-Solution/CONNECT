package gov.hhs.fha.nhinc.patientlocationquery.inbound;

import static org.junit.Assert.assertNotNull;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import ihe.iti.xcpd._2009.PatientLocationQueryRequestType;
import ihe.iti.xcpd._2009.PatientLocationQueryResponseType;
import java.util.Properties;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;


public class PassthroughInboundPatientLocationQueryTest {

    PassthroughInboundPatientLocationQuery plqInbound;
    PatientLocationQueryRequestType request;
    AssertionType assertion;
    Properties properties;

    @BeforeClass
    public static void setNHINCPropertyDirectory()
    {
        // We need to set this property so the PropertyAccessor class doesnt complain and error out.
        System.setProperty("nhinc.properties.dir", System.getProperty("user.dir") + "/src/test/resources/");
    }
    @Before
    public void setup() {

        plqInbound =  Mockito.spy(PassthroughInboundPatientLocationQuery.class);
        request = new PatientLocationQueryRequestType();
        assertion = new AssertionType();
        properties = new Properties();
    }



    @Test
    public void testProcessPatientLocationQuery() {
        //Future story: Check if Audit Request was sent.
        PatientLocationQueryResponseType result = plqInbound.processPatientLocationQuery(request, assertion, properties);
        Mockito.verify(plqInbound).sendToAdapter(request, assertion);
        assertNotNull(result);

    }

    @Test
    public void testSendToAdapter() {
        PatientLocationQueryResponseType result = plqInbound.sendToAdapter(request, assertion);
        assertNotNull(result);
    }

}
