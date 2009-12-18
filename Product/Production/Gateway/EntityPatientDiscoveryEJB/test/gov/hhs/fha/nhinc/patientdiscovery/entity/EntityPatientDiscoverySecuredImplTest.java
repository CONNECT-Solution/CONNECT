/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.patientdiscovery.entity;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunityType;
import gov.hhs.fha.nhinc.connectmgr.data.CMHomeCommunity;
import java.util.ArrayList;
import java.util.List;
import javax.xml.ws.WebServiceContext;
import org.apache.commons.logging.Log;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02ResponseType;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 *
 * @author shawc
 */
public class EntityPatientDiscoverySecuredImplTest {

    private Mockery context;

    public EntityPatientDiscoverySecuredImplTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setup() {
        context = new Mockery() {

            {
                //setImposteriser(ClassImposteriser.INSTANCE);
            }
        };
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testRespondingGatewayPRPAIN201305UV02WillFailForNullRequest() {
        final Log mockLogger = context.mock(Log.class);
        EntityPatientDiscoverySecuredImpl testSubject = new EntityPatientDiscoverySecuredImpl() {

            @Override
            protected Log createLogger() {
                return mockLogger;
            }
        };
        context.checking(new Expectations() {

            {
                allowing(mockLogger).info(with(any(String.class)));
                allowing(mockLogger).debug(with(any(String.class)));
                one(mockLogger).error("The incomming request was null.");
                will(returnValue(null));
            }
        });

        testSubject.respondingGatewayPRPAIN201305UV02(null, null);
        context.assertIsSatisfied();
    }

    @Test
    public void testRespondingGatewayPRPAIN201305UV02WillFailForNullWebServiceContext() {
        final Log mockLogger = context.mock(Log.class);
        EntityPatientDiscoverySecuredImpl testSubject = new EntityPatientDiscoverySecuredImpl() {

            @Override
            protected Log createLogger() {
                return mockLogger;
            }
        };
        context.checking(new Expectations() {

            {
                allowing(mockLogger).info(with(any(String.class)));
                allowing(mockLogger).debug(with(any(String.class)));
                one(mockLogger).error("The incomming WebServiceContext parameter was null.");
                will(returnValue(null));
            }
        });

        RespondingGatewayPRPAIN201305UV02RequestType oRequest = new RespondingGatewayPRPAIN201305UV02RequestType();
        testSubject.respondingGatewayPRPAIN201305UV02(oRequest, null);
        context.assertIsSatisfied();
    }

    @Test
    public void testRespondingGatewayPRPAIN201305UV02WillFailForNullNhinTargetCommunites() {
        final Log mockLogger = context.mock(Log.class);
        final WebServiceContext mockContext = context.mock(WebServiceContext.class);
        EntityPatientDiscoverySecuredImpl testSubject = new EntityPatientDiscoverySecuredImpl() {

            @Override
            protected Log createLogger() {
                return mockLogger;
            }

            @Override
            protected void logEntityPatientDiscoveryRequest(RespondingGatewayPRPAIN201305UV02RequestType request) {
                //don't send the adapter request to the audit log proxy as it is an external resource - not for unit testing
            }

            @Override
            protected void logAggregatedResponseFromNhin(RespondingGatewayPRPAIN201306UV02ResponseType response, AssertionType assertion) {
                //don't send the response from Nhin to the audit log proxy as it is an external resource - not for unit testing
            }

            @Override
            protected RespondingGatewayPRPAIN201306UV02ResponseType getResponseFromCommunities(RespondingGatewayPRPAIN201305UV02RequestType request, AssertionType assertion, WebServiceContext context) {
                return new RespondingGatewayPRPAIN201306UV02ResponseType();
            }

            @Override
            protected AssertionType getAssertionTypeFromSAMLTokenInWSContext(WebServiceContext context) {
                return new AssertionType();
            }
        };
        context.checking(new Expectations() {

            {
                allowing(mockLogger).info(with(any(String.class)));
                allowing(mockLogger).debug(with(any(String.class)));
                one(mockLogger).error("The incomming request did not have any NhinTargetCommunities (i.e. request.getNhinTargetCommunities() was null.");
                will(returnValue(null));
            }
        });

        RespondingGatewayPRPAIN201305UV02RequestType oRequest = new RespondingGatewayPRPAIN201305UV02RequestType();
//        WebServiceContext oContext = new WebServiceContext();
        testSubject.respondingGatewayPRPAIN201305UV02(oRequest, mockContext);
        context.assertIsSatisfied();
    }

    @Test
    public void testRespondingGatewayPRPAIN201305UV02WillPass() {
        final Log mockLogger = context.mock(Log.class);
        final WebServiceContext mockContext = context.mock(WebServiceContext.class);
        EntityPatientDiscoverySecuredImpl testSubject = new EntityPatientDiscoverySecuredImpl() {

            @Override
            protected Log createLogger() {
                return mockLogger;
            }

            @Override
            protected void logEntityPatientDiscoveryRequest(RespondingGatewayPRPAIN201305UV02RequestType request) {
                //don't send the adapter request to the audit log proxy as it is an external resource - not for unit testing
            }

            @Override
            protected void logAggregatedResponseFromNhin(RespondingGatewayPRPAIN201306UV02ResponseType response, AssertionType assertion) {
                //don't send the response from Nhin to the audit log proxy as it is an external resource - not for unit testing
            }

            @Override
            protected RespondingGatewayPRPAIN201306UV02ResponseType getResponseFromCommunities(RespondingGatewayPRPAIN201305UV02RequestType request, AssertionType assertion, WebServiceContext context) {
                return new RespondingGatewayPRPAIN201306UV02ResponseType();
            }

            @Override
            protected AssertionType getAssertionTypeFromSAMLTokenInWSContext(WebServiceContext context) {
                return new AssertionType();
            }

            @Override
            protected List<NhinTargetCommunityType> getDefaultTargetCommunities(){
                return new ArrayList<NhinTargetCommunityType>();
            }
        };
        context.checking(new Expectations() {

            {
                allowing(mockLogger).info(with(any(String.class)));
                allowing(mockLogger).debug(with(any(String.class)));
                never(mockLogger).error(with(any(String.class)));
                will(returnValue(with(any(RespondingGatewayPRPAIN201306UV02ResponseType.class))));
            }
        });

        RespondingGatewayPRPAIN201305UV02RequestType oRequest = new RespondingGatewayPRPAIN201305UV02RequestType();
        NhinTargetCommunitiesType oNhinTargetCommunities = new NhinTargetCommunitiesType();
        oRequest.setNhinTargetCommunities(oNhinTargetCommunities);
        testSubject.respondingGatewayPRPAIN201305UV02(oRequest, mockContext);
        context.assertIsSatisfied();
    }

    @Test
    public void testGetResponseFromCommunitiesForNonExistentTargetCommunity() {
        final Log mockLogger = context.mock(Log.class);
        final WebServiceContext mockContext = context.mock(WebServiceContext.class);
        EntityPatientDiscoverySecuredImpl testSubject = new EntityPatientDiscoverySecuredImpl() {

            @Override
            protected Log createLogger() {
                return mockLogger;
            }

            @Override
            protected boolean isTargetCommunityInConnectionManager(NhinTargetCommunityType targetCommunity) {
                return false;
            }
        };
        context.checking(new Expectations() {

            {
                allowing(mockLogger).info("The target community, ");
                allowing(mockLogger).debug(with(any(String.class)));
                never(mockLogger).error(with(any(String.class)));
                will(returnValue(with(any(RespondingGatewayPRPAIN201306UV02ResponseType.class))));
            }
        });

        RespondingGatewayPRPAIN201305UV02RequestType oRequest = new RespondingGatewayPRPAIN201305UV02RequestType();
        NhinTargetCommunitiesType oNhinTargetCommunities = new NhinTargetCommunitiesType();
        oRequest.setNhinTargetCommunities(oNhinTargetCommunities);
        AssertionType assertion = new AssertionType();
        testSubject.getResponseFromCommunities(oRequest, assertion, mockContext);
        context.assertIsSatisfied();
    }

    @Test
    public void testIsTargetCommunityInConnectionManagerWillFail() {
        final Log mockLogger = context.mock(Log.class);
        final WebServiceContext mockContext = context.mock(WebServiceContext.class);
        EntityPatientDiscoverySecuredImpl testSubject = new EntityPatientDiscoverySecuredImpl() {

            @Override
            protected Log createLogger() {
                return mockLogger;
            }

            @Override
            protected List<CMHomeCommunity> getCommunitiesFromConnectionManager() {
                List<CMHomeCommunity> returnList = new ArrayList<CMHomeCommunity>();
                CMHomeCommunity oCMHomeCommunity = new CMHomeCommunity();
                oCMHomeCommunity.setHomeCommunityId("1.1");
                returnList.add(oCMHomeCommunity);
                return returnList;
            }

        };
        context.checking(new Expectations() {

            {
                allowing(mockLogger).info("Could not find the target community in the connection manager.");
                allowing(mockLogger).debug(with(any(String.class)));
                never(mockLogger).error(with(any(String.class)));
                will(returnValue(false));
            }
        });

        NhinTargetCommunityType oTargetCommunity = new NhinTargetCommunityType();
        HomeCommunityType oHomeCommunity = new HomeCommunityType();
        oHomeCommunity.setHomeCommunityId("2.5.3");
        oTargetCommunity.setHomeCommunity(oHomeCommunity);
        testSubject.isTargetCommunityInConnectionManager(oTargetCommunity);
        context.assertIsSatisfied();
    }

    @Test
    public void testIsTargetCommunityInConnectionManagerWillPass() {
        final Log mockLogger = context.mock(Log.class);
        final WebServiceContext mockContext = context.mock(WebServiceContext.class);
        EntityPatientDiscoverySecuredImpl testSubject = new EntityPatientDiscoverySecuredImpl() {

            @Override
            protected Log createLogger() {
                return mockLogger;
            }

            @Override
            protected List<CMHomeCommunity> getCommunitiesFromConnectionManager() {
                List<CMHomeCommunity> returnList = new ArrayList<CMHomeCommunity>();
                CMHomeCommunity oCMHomeCommunity = new CMHomeCommunity();
                oCMHomeCommunity.setHomeCommunityId("1.1");
                returnList.add(oCMHomeCommunity);
                return returnList;
            }

        };
        context.checking(new Expectations() {

            {
                allowing(mockLogger).info("Found target community in connection manager.");
                allowing(mockLogger).debug(with(any(String.class)));
                never(mockLogger).error(with(any(String.class)));
                will(returnValue(true));
            }
        });

        NhinTargetCommunityType oTargetCommunity = new NhinTargetCommunityType();
        HomeCommunityType oHomeCommunity = new HomeCommunityType();
        oHomeCommunity.setHomeCommunityId("1.1");
        oTargetCommunity.setHomeCommunity(oHomeCommunity);
        testSubject.isTargetCommunityInConnectionManager(oTargetCommunity);
        context.assertIsSatisfied();
    }

}
