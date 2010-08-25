package gov.hhs.fha.nhinc.patientdiscovery.entity.deferred.request;

import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.patientdiscovery.testhelper.TestHelper;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.connectmgr.data.CMUrlInfo;
import gov.hhs.fha.nhinc.connectmgr.data.CMUrlInfos;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryAuditLogger;
import gov.hhs.fha.nhinc.transform.subdisc.HL7AckTransforms;
import gov.hhs.fha.nhinc.transform.subdisc.HL7PRPA201305Transforms;
import gov.hhs.fha.nhinc.transform.subdisc.HL7PatientTransforms;
import javax.xml.bind.JAXBElement;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAMT201301UV02Patient;
import org.hl7.v3.PRPAMT201301UV02Person;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02SecuredRequestType;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class EntityPatientDiscoverDeferredRequestImplTest
{

    public EntityPatientDiscoverDeferredRequestImplTest()
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
     * Test of processPatientDiscoveryAsyncReq method, of class EntityPatientDiscoveryAsyncRequestOrchImpl.
     */
    @Test
    public void testProcessPatientDiscoveryAsyncReq()
    {
        System.out.println("testProcessPatientDiscoveryAsyncReq");

        EntityPatientDiscoveryDeferredRequestOrchImpl instance = new EntityPatientDiscoveryDeferredRequestOrchImpl()
        {

            @Override
            protected CMUrlInfos getTargets(NhinTargetCommunitiesType targetCommunities)
            {
                CMUrlInfos urlInfo = new CMUrlInfos();

                CMUrlInfo target = new CMUrlInfo();
                target.setHcid("2.2");
                target.setUrl("https://test.com:8181/NhinPatientDiscoveryAsync");
                urlInfo.getUrlInfo().add(target);

                return urlInfo;
            }

            @Override
            protected boolean checkPolicy(RespondingGatewayPRPAIN201305UV02RequestType request)
            {
                return true;
            }

            @Override
            protected MCCIIN000002UV01 sendToProxy(RespondingGatewayPRPAIN201305UV02RequestType request, CMUrlInfo urlInfo)
            {
                return HL7AckTransforms.createAckFrom201305(request.getPRPAIN201305UV02(), "Success");
            }

            @Override
            protected void addEntryToDatabase(RespondingGatewayPRPAIN201305UV02RequestType request)
            {
                return;
            }

            @Override
            protected PatientDiscoveryAuditLogger createAuditLogger()
            {
                PatientDiscoveryAuditLogger auditLogger = new PatientDiscoveryAuditLogger()
                {

                    @Override
                    public AcknowledgementType auditEntity201305(RespondingGatewayPRPAIN201305UV02RequestType request, AssertionType assertion, String direction)
                    {
                        return new AcknowledgementType();
                    }

                    @Override
                    public AcknowledgementType auditAck(MCCIIN000002UV01 request, AssertionType assertion, String direction, String _interface)
                    {
                        return new AcknowledgementType();
                    }
                };
                return auditLogger;
            }
        };

        AssertionType assertion = new AssertionType();
        NhinTargetCommunitiesType targets = new NhinTargetCommunitiesType();

        JAXBElement<PRPAMT201301UV02Person> person = HL7PatientTransforms.create201301PatientPerson("Joe", "Smith", "M", null, null);
        PRPAMT201301UV02Patient patient = HL7PatientTransforms.create201301Patient(person, "1234", "1.1.1");
        PRPAIN201305UV02 msg = HL7PRPA201305Transforms.createPRPA201305(patient, "1.1", "2.2", "1.1.1");

        MCCIIN000002UV01 result = instance.processPatientDiscoveryAsyncReq(msg, assertion, targets);

        assertNotNull(result);
        TestHelper.assertAckMsgEquals("Success", result);
        TestHelper.assertReceiverEquals("1.1", result);
        TestHelper.assertSenderEquals("2.2", result);
        TestHelper.assertAckMsgIdEquals(msg.getId(), result);
    }

    /**
     * Test of processPatientDiscoveryAsyncReq method, of class EntityPatientDiscoveryAsyncRequestOrchImpl.
     */
    @Test
    public void testProcessPatientDiscoveryAsyncReqNoTargets()
    {
        System.out.println("testProcessPatientDiscoveryAsyncReqNoTargets");

        EntityPatientDiscoveryDeferredRequestOrchImpl instance = new EntityPatientDiscoveryDeferredRequestOrchImpl()
        {

            @Override
            protected CMUrlInfos getTargets(NhinTargetCommunitiesType targetCommunities)
            {
                return null;
            }

            @Override
            protected boolean checkPolicy(RespondingGatewayPRPAIN201305UV02RequestType request)
            {
                return true;
            }

            @Override
            protected MCCIIN000002UV01 sendToProxy(RespondingGatewayPRPAIN201305UV02RequestType request, CMUrlInfo urlInfo)
            {
                return HL7AckTransforms.createAckFrom201305(request.getPRPAIN201305UV02(), "Success");
            }

            @Override
            protected void addEntryToDatabase(RespondingGatewayPRPAIN201305UV02RequestType request)
            {
                return;
            }

            @Override
            protected PatientDiscoveryAuditLogger createAuditLogger()
            {
                PatientDiscoveryAuditLogger auditLogger = new PatientDiscoveryAuditLogger()
                {

                    @Override
                    public AcknowledgementType auditEntity201305(RespondingGatewayPRPAIN201305UV02RequestType request, AssertionType assertion, String direction)
                    {
                        return new AcknowledgementType();
                    }

                    @Override
                    public AcknowledgementType auditAck(MCCIIN000002UV01 request, AssertionType assertion, String direction, String _interface)
                    {
                        return new AcknowledgementType();
                    }
                };
                return auditLogger;
            }
        };

        AssertionType assertion = new AssertionType();
        NhinTargetCommunitiesType targets = new NhinTargetCommunitiesType();

        JAXBElement<PRPAMT201301UV02Person> person = HL7PatientTransforms.create201301PatientPerson("Joe", "Smith", "M", null, null);
        PRPAMT201301UV02Patient patient = HL7PatientTransforms.create201301Patient(person, "1234", "1.1.1");
        PRPAIN201305UV02 msg = HL7PRPA201305Transforms.createPRPA201305(patient, "1.1", "2.2", "1.1.1");

        MCCIIN000002UV01 result = instance.processPatientDiscoveryAsyncReq(msg, assertion, targets);

        assertNotNull(result);
        TestHelper.assertAckMsgEquals("No Targets Found", result);
        TestHelper.assertReceiverEquals("1.1", result);
        TestHelper.assertSenderEquals("2.2", result);
        TestHelper.assertAckMsgIdEquals(msg.getId(), result);
    }

    /**
     * Test of processPatientDiscoveryAsyncReq method, of class EntityPatientDiscoveryAsyncRequestOrchImpl.
     */
    @Test
    public void testProcessPatientDiscoveryAsyncReqPolicyFailed()
    {
        System.out.println("testProcessPatientDiscoveryAsyncReqPolicyFailed");

        EntityPatientDiscoveryDeferredRequestOrchImpl instance = new EntityPatientDiscoveryDeferredRequestOrchImpl()
        {

            @Override
            protected CMUrlInfos getTargets(NhinTargetCommunitiesType targetCommunities)
            {
                CMUrlInfos urlInfo = new CMUrlInfos();

                CMUrlInfo target = new CMUrlInfo();
                target.setHcid("2.2");
                target.setUrl("https://test.com:8181/NhinPatientDiscoveryAsync");
                urlInfo.getUrlInfo().add(target);

                return urlInfo;
            }

            @Override
            protected boolean checkPolicy(RespondingGatewayPRPAIN201305UV02RequestType request)
            {
                return false;
            }

            @Override
            protected MCCIIN000002UV01 sendToProxy(RespondingGatewayPRPAIN201305UV02RequestType request, CMUrlInfo urlInfo)
            {
                return HL7AckTransforms.createAckFrom201305(request.getPRPAIN201305UV02(), "Success");
            }

            @Override
            protected void addEntryToDatabase(RespondingGatewayPRPAIN201305UV02RequestType request)
            {
                return;
            }

            @Override
            protected PatientDiscoveryAuditLogger createAuditLogger()
            {
                PatientDiscoveryAuditLogger auditLogger = new PatientDiscoveryAuditLogger()
                {

                    @Override
                    public AcknowledgementType auditEntity201305(RespondingGatewayPRPAIN201305UV02RequestType request, AssertionType assertion, String direction)
                    {
                        return new AcknowledgementType();
                    }

                    @Override
                    public AcknowledgementType auditAck(MCCIIN000002UV01 request, AssertionType assertion, String direction, String _interface)
                    {
                        return new AcknowledgementType();
                    }
                };
                return auditLogger;
            }
        };

        AssertionType assertion = new AssertionType();
        NhinTargetCommunitiesType targets = new NhinTargetCommunitiesType();

        JAXBElement<PRPAMT201301UV02Person> person = HL7PatientTransforms.create201301PatientPerson("Joe", "Smith", "M", null, null);
        PRPAMT201301UV02Patient patient = HL7PatientTransforms.create201301Patient(person, "1234", "1.1.1");
        PRPAIN201305UV02 msg = HL7PRPA201305Transforms.createPRPA201305(patient, "1.1", "2.2", "1.1.1");

        MCCIIN000002UV01 result = instance.processPatientDiscoveryAsyncReq(msg, assertion, targets);

        assertNotNull(result);
        TestHelper.assertAckMsgEquals("Policy Failed", result);
        TestHelper.assertReceiverEquals("1.1", result);
        TestHelper.assertSenderEquals("2.2", result);
        TestHelper.assertAckMsgIdEquals(msg.getId(), result);
    }
}
