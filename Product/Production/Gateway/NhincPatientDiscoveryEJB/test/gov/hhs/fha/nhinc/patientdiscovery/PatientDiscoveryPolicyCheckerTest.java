/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.patientdiscovery;

import gov.hhs.fha.nhinc.common.eventcommon.PatDiscReqEventType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import java.util.List;
import org.hl7.v3.II;
import org.hl7.v3.PNExplicit;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.jmock.Mockery;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jhoppesc
 */
public class PatientDiscoveryPolicyCheckerTest {

    private Mockery context;

    public PatientDiscoveryPolicyCheckerTest() {
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

    /**
     * This unit test will test that the parameters will be passed to the lower
     * level method without being modified.
     */
    @Test
    public void testCheckPolicyParamsPass() {
        PatientDiscoveryPolicyChecker instance = new PatientDiscoveryPolicyChecker() {

            @Override
            protected boolean invokePolicyEngine(PatDiscReqEventType policyCheckReq) {
                if (policyCheckReq != null &&
                        policyCheckReq.getReceivingHomeCommunity() != null &&
                        NullChecker.isNotNullish(policyCheckReq.getReceivingHomeCommunity().getHomeCommunityId()) &&
                        policyCheckReq.getSendingHomeCommunity() != null &&
                        NullChecker.isNotNullish(policyCheckReq.getSendingHomeCommunity().getHomeCommunityId()) &&
                        policyCheckReq.getPRPAIN201306UV02() != null &&
                        policyCheckReq.getPRPAIN201306UV02().getControlActProcess() != null &&
                        NullChecker.isNotNullish(policyCheckReq.getPRPAIN201306UV02().getControlActProcess().getSubject()) &&
                        policyCheckReq.getPRPAIN201306UV02().getControlActProcess().getSubject().get(0) != null &&
                        policyCheckReq.getPRPAIN201306UV02().getControlActProcess().getSubject().get(0).getRegistrationEvent() != null &&
                        policyCheckReq.getPRPAIN201306UV02().getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1() != null &&
                        policyCheckReq.getPRPAIN201306UV02().getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient() != null &&
                        policyCheckReq.getPRPAIN201306UV02().getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getPatientPerson() != null &&
                        policyCheckReq.getPRPAIN201306UV02().getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getPatientPerson().getValue() != null &&
                        policyCheckReq.getPRPAIN201306UV02().getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getPatientPerson().getValue().getName() != null) {
                    List<PNExplicit> name = policyCheckReq.getPRPAIN201306UV02().getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getPatientPerson().getValue().getName();
                    String first = TestHelper.getFirstName(name.get(0).getContent().iterator());
                    String last = TestHelper.getLastName(name.get(0).getContent().iterator());
                    if (policyCheckReq.getReceivingHomeCommunity().getHomeCommunityId().equals("2.2") &&
                            policyCheckReq.getSendingHomeCommunity().getHomeCommunityId().equals("1.1") &&
                            first.equals("Joe") && last.equals("Smith")) {
                        return true;
                    }
                }
                return false;
            }
        };

        PRPAIN201305UV02 query = TestHelper.create201305("Joe", "Smith", null, null, null, "1.1", "2.2");
        PRPAIN201306UV02 message = TestHelper.create201306("1.1", "2.2", query);

        II patIdOverride = new II();
        patIdOverride.setRoot("2.2");
        patIdOverride.setExtension("5678");

        AssertionType assertion = null;

        boolean result = instance.check201305Policy(message, patIdOverride, assertion);

        assertEquals(true, result);
    }

    /**
     * This unit test will test that the "true" return value will be passed back up
     * to the calling methods without being modified
     */
    @Test
    public void testCheckPolicyPass() {
        PatientDiscoveryPolicyChecker instance = new PatientDiscoveryPolicyChecker() {

            @Override
            protected boolean invokePolicyEngine(PatDiscReqEventType policyCheckReq) {
                return true;
            }
        };

        PRPAIN201305UV02 query = TestHelper.create201305("Joe", "Smith", null, null, null, "1.1", "2.3");
        PRPAIN201306UV02 message = TestHelper.create201306("1.1", "2.2", query);

        II patIdOverride = new II();
        patIdOverride.setRoot("2.2");
        patIdOverride.setExtension("5678");

        AssertionType assertion = null;

        boolean result = instance.check201305Policy(message, patIdOverride, assertion);

        assertEquals(true, result);
    }

    /**
     * This unit test will test that the "false" return value will be passed back up
     * to the calling methods without being modified
     */
    @Test
    public void testCheckPolicyFail() {
        PatientDiscoveryPolicyChecker instance = new PatientDiscoveryPolicyChecker() {

            @Override
            protected boolean invokePolicyEngine(PatDiscReqEventType policyCheckReq) {
                return false;
            }
        };

        PRPAIN201305UV02 query = TestHelper.create201305("Joe", "Smith", null, null, null, "1.1", "2.3");
        PRPAIN201306UV02 message = TestHelper.create201306("1.1", "2.2", query);

        II patIdOverride = new II();
        patIdOverride.setRoot("2.2");
        patIdOverride.setExtension("5678");

        AssertionType assertion = null;

        boolean result = instance.check201305Policy(message, patIdOverride, assertion);

        assertEquals(false, result);
    }
}
