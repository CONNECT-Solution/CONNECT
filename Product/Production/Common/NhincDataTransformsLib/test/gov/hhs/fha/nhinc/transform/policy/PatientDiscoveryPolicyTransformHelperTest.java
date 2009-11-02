/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.transform.policy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import oasis.names.tc.xacml._2_0.context.schema.os.RequestType;
import org.apache.commons.logging.Log;
import org.hl7.v3.PRPAIN201305UV;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author svalluripalli
 */
public class PatientDiscoveryPolicyTransformHelperTest {

    private Mockery context;

    public PatientDiscoveryPolicyTransformHelperTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        context = new Mockery();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testTransformPatientDiscoveryEntityToCheckPolicyWillFailForNullRequest() {
        final Log mockLogger = context.mock(Log.class);
        PatientDiscoveryPolicyTransformHelper testSubject = new PatientDiscoveryPolicyTransformHelper() {
            @Override
            protected Log createLogger() {
                return mockLogger;
            }
        };
        context.checking(new Expectations() {
            {
                exactly(1).of(mockLogger).debug(with(any(String.class)));
                exactly(1).of(mockLogger).error("Request is null.");
                will(returnValue(with(any(CheckPolicyRequestType.class))));
            }
        });

        testSubject.transformPatientDiscoveryEntityToCheckPolicy(null);
        context.assertIsSatisfied();
    }

    @Test
    public void testTransformPatientDiscoveryEntityToCheckPolicyWillPass() {
        final Log mockLogger = context.mock(Log.class);
        PRPAIN201305UV requestPRPAIN201305UV = new PRPAIN201305UV();
        //AssertionType assertionType = requestPRPAIN201305UV.getAssertion();

        final CheckPolicyRequestType expectedReturnValue = new CheckPolicyRequestType();
        expectedReturnValue.setAssertion(new AssertionType());
        expectedReturnValue.setRequest(new RequestType());

        PatientDiscoveryPolicyTransformHelper testSubject = new PatientDiscoveryPolicyTransformHelper() {
            @Override
            protected Log createLogger() {
                return mockLogger;
            }
        };
        context.checking(new Expectations() {
            {
                exactly(3).of(mockLogger).debug(with(any(String.class)));
                will(returnValue(with(any(CheckPolicyRequestType.class))));
//                aNonNull(AssertionType.class);
//                aNonNull(ResourceType.class);
//                aNull(ResourceType.class);
            }
        });

        testSubject.transformPatientDiscoveryEntityToCheckPolicy(requestPRPAIN201305UV);
        context.assertIsSatisfied();
        //assertNotNull(expectedReturnValue.getAssertion());
    }
}
