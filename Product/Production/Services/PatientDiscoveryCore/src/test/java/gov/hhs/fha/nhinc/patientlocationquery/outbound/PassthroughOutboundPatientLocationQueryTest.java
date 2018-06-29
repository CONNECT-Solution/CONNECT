package gov.hhs.fha.nhinc.patientlocationquery.outbound;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayPatientLocationQueryResponseType;
import gov.hhs.fha.nhinc.patientlocationquery.entity.OutboundPatientLocationQueryDelegate;
import gov.hhs.fha.nhinc.patientlocationquery.entity.OutboundPatientLocationQueryOrchestratable;
import ihe.iti.xcpd._2009.PatientLocationQueryRequestType;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

public class PassthroughOutboundPatientLocationQueryTest {

    PassthroughOutboundPatientLocationQuery plqOutbound;
    PatientLocationQueryRequestType request;
    AssertionType assertion;
    NhinTargetCommunitiesType target;

    @BeforeClass
    public static void setNHINCPropertyDirectory()
    {
        // We need to set this property so the PropertyAccessor class doesnt complain and error out.
        System.setProperty("nhinc.properties.dir", System.getProperty("user.dir") + "/src/test/resources/");
    }
    @Before
    public void setup() {

        plqOutbound =  Mockito.spy(PassthroughOutboundPatientLocationQuery.class);
        request = new PatientLocationQueryRequestType();
        assertion = new AssertionType();
        target = new NhinTargetCommunitiesType();
    }


    @Test
    public void testProcessPatientLocationQuery() {

        //Future story: Check if Audit Request was sent.
        RespondingGatewayPatientLocationQueryResponseType result = plqOutbound.processPatientLocationQuery(request, assertion, target);
        Mockito.verify(plqOutbound).sendToNhinProxy(request, assertion, target);
        assertNotNull(result);

    }

    @Test
    public void testCreateOrchestratable() {
        OutboundPatientLocationQueryDelegate delegate = new OutboundPatientLocationQueryDelegate();
        OutboundPatientLocationQueryOrchestratable result = plqOutbound.createOrchestratable(delegate, request, assertion, target);

        assertSame(delegate, result.getDelegate());
        assertSame(request, result.getRequest());
        assertSame(assertion, result.getAssertion());
        assertSame(target, result.getTarget());

    }

}
