package gov.hhs.fha.nhinc.patientdiscovery.entity.deferred.response;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import javax.xml.ws.WebServiceContext;
import org.apache.commons.logging.Log;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02RequestType;
import org.jmock.Mockery;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author goldmanm
 */
public class EntityPatientDiscoveryDeferredResponseImplTest {

    Mockery mockery = new JUnit4Mockery()
    {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };
    final Log mockLog = mockery.mock(Log.class);
    final WebServiceContext mockContext = mockery.mock(WebServiceContext.class);
    final AssertionType mockAssertion = mockery.mock(AssertionType.class);
    final EntityPatientDiscoveryDeferredResponseOrchImpl mockOrchImpl = mockery.mock(EntityPatientDiscoveryDeferredResponseOrchImpl.class);
    final PRPAIN201306UV02 mockRequest = mockery.mock(PRPAIN201306UV02.class);
    final NhinTargetCommunitiesType mockTargetComm = mockery.mock(NhinTargetCommunitiesType.class);

    @Before
    public void setup() {
    }

    @Test
    public void testCreateLogger() {
        try {
            EntityPatientDiscoveryDeferredResponseImpl pdUnsecuredImpl = new EntityPatientDiscoveryDeferredResponseImpl() {

                @Override
                protected Log createLogger() {
                    return mockLog;
                }
            };
            mockery.checking(new Expectations() {

                {
                    allowing(mockLog).debug(with(aNonNull(String.class)));
                }
            });

            Log log = pdUnsecuredImpl.createLogger();
            assertNotNull("Log was null", log);
        } catch (Throwable t) {
            System.out.println("Error running testCreateLogger: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testCreateLogger: " + t.getMessage());
        }
    }

      /**
   * Test of processPatientDiscoveryAsyncResp method, of class EntityPatientDiscoveryDeferredResponseOrchImpl.
   */
  @Test
  public void testProcessPatientDiscoveryAsyncResp_RespondingGatewayPRPAIN201306UV02RequestType_WebServiceContext() {
    
    EntityPatientDiscoveryDeferredResponseImpl testSubject = new EntityPatientDiscoveryDeferredResponseImpl() {
      @Override
      protected EntityPatientDiscoveryDeferredResponseOrchImpl createEntityPatientDiscoveryDeferredResponseOrchImpl() {
        return mockOrchImpl;
      }
    };

    mockery.checking(new Expectations() {
      {
          allowing(mockLog).debug(with(aNonNull(String.class)));
          ignoring(mockContext).getMessageContext();
          ignoring(mockAssertion).setMessageId(with(aNonNull(String.class)));
          oneOf(mockOrchImpl).processPatientDiscoveryAsyncRespOrch(with(aNonNull(PRPAIN201306UV02.class)), with(aNonNull(AssertionType.class)), with(aNonNull(NhinTargetCommunitiesType.class)));
      }
    });

    RespondingGatewayPRPAIN201306UV02RequestType mockBody = new RespondingGatewayPRPAIN201306UV02RequestType();
    mockBody.setAssertion(mockAssertion);
    mockBody.setPRPAIN201306UV02(mockRequest);
    mockBody.setNhinTargetCommunities(mockTargetComm);

    testSubject.processPatientDiscoveryAsyncResp(mockBody, mockContext);
    mockery.assertIsSatisfied();
  }
}
