package gov.hhs.fha.nhinc.patientdiscovery.entity;

import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.connectmgr.data.CMUrlInfo;
import gov.hhs.fha.nhinc.connectmgr.data.CMUrlInfos;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscovery201305Processor;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscovery201306Processor;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryAuditLogger;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryPolicyChecker;
import gov.hhs.fha.nhinc.patientdiscovery.proxy.NhincProxyPatientDiscoverySecuredImpl;
import gov.hhs.fha.nhinc.patientdiscovery.response.ResponseFactory;
import gov.hhs.fha.nhinc.patientdiscovery.response.ResponseMode;
import gov.hhs.fha.nhinc.patientdiscovery.response.ResponseParams;
import gov.hhs.fha.nhinc.patientdiscovery.response.VerifyMode;
import org.apache.commons.logging.Log;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.ProxyPRPAIN201305UVProxySecuredRequestType;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02ResponseType;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

/**
 *
 * @author Neil Webb
 */
@RunWith(JMock.class)
public class EntityPatientDiscoveryProcessorTest
{
    Mockery context = new JUnit4Mockery()
    {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };
    final Log mockLog = context.mock(Log.class);
    final PRPAIN201305UV02 mockPRPAIN201305UV02 = context.mock(PRPAIN201305UV02.class);
    final PRPAIN201306UV02 mockPRPAIN201306UV02 = context.mock(PRPAIN201306UV02.class);
    final AssertionType mockAssertion = context.mock(AssertionType.class);
    final NhinTargetCommunitiesType mockTargetCommunities = context.mock(NhinTargetCommunitiesType.class);
    final AcknowledgementType mockAck = context.mock(AcknowledgementType.class);
    final RespondingGatewayPRPAIN201306UV02ResponseType mockResponse = context.mock(RespondingGatewayPRPAIN201306UV02ResponseType.class);
    final PatientDiscoveryAuditLogger mockAuditLogger = context.mock(PatientDiscoveryAuditLogger.class);
    final PatientDiscoveryPolicyChecker mockPolicyChecker = context.mock(PatientDiscoveryPolicyChecker.class);
    final PatientDiscovery201305Processor mockPatientDiscovery201305Processor = context.mock(PatientDiscovery201305Processor.class);
    final NhincProxyPatientDiscoverySecuredImpl mockNhincProxyPatientDiscoverySecuredImpl = context.mock(NhincProxyPatientDiscoverySecuredImpl.class);
    final ResponseFactory mockResponseFactory = context.mock(ResponseFactory.class);
    final CMUrlInfos mockUrlInfos = context.mock(CMUrlInfos.class);
    final RespondingGatewayPRPAIN201305UV02RequestType mockRespondingGatewayPRPAIN201305UV02RequestType = context.mock(RespondingGatewayPRPAIN201305UV02RequestType.class);
    final PatientDiscovery201306Processor mockPatientDiscovery201306Processor = context.mock(PatientDiscovery201306Processor.class);
    
// <editor-fold defaultstate="collapsed" desc="Creation Methods">
    @Test
    public void testCreateLogger()
    {
        try
        {
            EntityPatientDiscoveryProcessor processor = new EntityPatientDiscoveryProcessor()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(aNonNull(String.class)));
                }
            });

            Log log = processor.createLogger();
            assertNotNull("Log was null", log);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testCreateLogger: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testCreateLogger: " + t.getMessage());
        }
    }

    @Test
    public void testGetPatientDiscoveryAuditLogger()
    {
        try
        {
            EntityPatientDiscoveryProcessor processor = new EntityPatientDiscoveryProcessor()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected PatientDiscoveryAuditLogger getPatientDiscoveryAuditLogger()
                {
                    return mockAuditLogger;
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(aNonNull(String.class)));
                }
            });

            PatientDiscoveryAuditLogger auditLogger = processor.getPatientDiscoveryAuditLogger();
            assertNotNull("PatientDiscoveryAuditLogger was null", auditLogger);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testGetPatientDiscoveryAuditLogger: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetPatientDiscoveryAuditLogger: " + t.getMessage());
        }
    }

    @Test
    public void testGetPatientDiscoveryPolicyChecker()
    {
        try
        {
            EntityPatientDiscoveryProcessor processor = new EntityPatientDiscoveryProcessor()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected PatientDiscoveryPolicyChecker getPatientDiscoveryPolicyChecker()
                {
                    return mockPolicyChecker;
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(aNonNull(String.class)));
                }
            });

            PatientDiscoveryPolicyChecker policyChecker = processor.getPatientDiscoveryPolicyChecker();
            assertNotNull("PatientDiscoveryPolicyChecker was null", policyChecker);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testGetPatientDiscoveryPolicyChecker: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetPatientDiscoveryPolicyChecker: " + t.getMessage());
        }
    }

    @Test
    public void testGetPatientDiscovery201305Processor()
    {
        try
        {
            EntityPatientDiscoveryProcessor processor = new EntityPatientDiscoveryProcessor()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected PatientDiscovery201305Processor getPatientDiscovery201305Processor()
                {
                    return mockPatientDiscovery201305Processor;
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(aNonNull(String.class)));
                }
            });

            PatientDiscovery201305Processor pdProcessor = processor.getPatientDiscovery201305Processor();
            assertNotNull("PatientDiscovery201305Processor was null", pdProcessor);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testGetPatientDiscovery201305Processor: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetPatientDiscovery201305Processor: " + t.getMessage());
        }
    }


    @Test
    public void testGetNhincProxyPatientDiscoverySecuredImpl()
    {
        try
        {
            EntityPatientDiscoveryProcessor processor = new EntityPatientDiscoveryProcessor()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected NhincProxyPatientDiscoverySecuredImpl getNhincProxyPatientDiscoverySecuredImpl()
                {
                    return mockNhincProxyPatientDiscoverySecuredImpl;
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(aNonNull(String.class)));
                }
            });

            NhincProxyPatientDiscoverySecuredImpl pdSecuredImpl = processor.getNhincProxyPatientDiscoverySecuredImpl();
            assertNotNull("NhincProxyPatientDiscoverySecuredImpl was null", pdSecuredImpl);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testGetNhincProxyPatientDiscoverySecuredImpl: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetNhincProxyPatientDiscoverySecuredImpl: " + t.getMessage());
        }
    }

    @Test
    public void testGetResponseFactory()
    {
        try
        {
            EntityPatientDiscoveryProcessor processor = new EntityPatientDiscoveryProcessor()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected ResponseFactory getResponseFactory()
                {
                    return mockResponseFactory;
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(aNonNull(String.class)));
                }
            });

            ResponseFactory responseFactory = processor.getResponseFactory();
            assertNotNull("ResponseFactory was null", responseFactory);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testGetResponseFactory: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetResponseFactory: " + t.getMessage());
        }
    }

    protected PatientDiscovery201306Processor getPatientDiscovery201306Processor()
    {
        return new PatientDiscovery201306Processor();
    }
    @Test
    public void testGetPatientDiscovery201306Processor()
    {
        try
        {
            EntityPatientDiscoveryProcessor processor = new EntityPatientDiscoveryProcessor()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected PatientDiscovery201306Processor getPatientDiscovery201306Processor()
                {
                    return mockPatientDiscovery201306Processor;
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(aNonNull(String.class)));
                }
            });

            PatientDiscovery201306Processor mappingStorage = processor.getPatientDiscovery201306Processor();
            assertNotNull("PRPAIN201306UV02MappingStorage was null", mappingStorage);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testGetPRPAIN201306UV02MappingStorage: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetPRPAIN201306UV02MappingStorage: " + t.getMessage());
        }
    }

// </editor-fold>

// <editor-fold defaultstate="collapsed" desc="respondingGatewayPRPAIN201305UV02">
    @Test
    public void testRespondingGatewayPRPAIN201305UV02Happy()
    {
        try
        {
            EntityPatientDiscoveryProcessor processor = new EntityPatientDiscoveryProcessor()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected void logAggregatedResponseFromNhin(RespondingGatewayPRPAIN201306UV02ResponseType response, AssertionType assertion)
                {
                }
                @Override
                protected void logEntityPatientDiscoveryRequest(RespondingGatewayPRPAIN201305UV02RequestType request, AssertionType assertion)
                {
                }
                @Override
                protected RespondingGatewayPRPAIN201306UV02ResponseType getResponseFromCommunities(RespondingGatewayPRPAIN201305UV02RequestType request, AssertionType assertion)
                {
                    return mockResponse;
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(aNonNull(String.class)));
                }
            });

            RespondingGatewayPRPAIN201305UV02RequestType request = new RespondingGatewayPRPAIN201305UV02RequestType();
            request.setPRPAIN201305UV02(mockPRPAIN201305UV02);
            request.setNhinTargetCommunities(mockTargetCommunities);

            RespondingGatewayPRPAIN201306UV02ResponseType response = processor.respondingGatewayPRPAIN201305UV02(request, mockAssertion);
            assertNotNull("RespondingGatewayPRPAIN201306UV02ResponseType was null", response);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testRespondingGatewayPRPAIN201305UV02Happy: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testRespondingGatewayPRPAIN201305UV02Happy: " + t.getMessage());
        }
    }

    @Test
    public void testRespondingGatewayPRPAIN201305UV02NullRequest()
    {
        try
        {
            EntityPatientDiscoveryProcessor processor = new EntityPatientDiscoveryProcessor()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(aNonNull(String.class)));
                    oneOf(mockLog).warn("RespondingGatewayPRPAIN201305UV02RequestType was null.");
                }
            });

            RespondingGatewayPRPAIN201306UV02ResponseType response = processor.respondingGatewayPRPAIN201305UV02(null, mockAssertion);
            assertNull("RespondingGatewayPRPAIN201306UV02ResponseType was not null", response);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testRespondingGatewayPRPAIN201305UV02NullRequest: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testRespondingGatewayPRPAIN201305UV02NullRequest: " + t.getMessage());
        }
    }

    @Test
    public void testRespondingGatewayPRPAIN201305UV02NullAssertion()
    {
        try
        {
            EntityPatientDiscoveryProcessor processor = new EntityPatientDiscoveryProcessor()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(aNonNull(String.class)));
                    oneOf(mockLog).warn("AssertionType was null.");
                }
            });

            RespondingGatewayPRPAIN201305UV02RequestType request = new RespondingGatewayPRPAIN201305UV02RequestType();
            request.setPRPAIN201305UV02(mockPRPAIN201305UV02);
            request.setNhinTargetCommunities(mockTargetCommunities);

            RespondingGatewayPRPAIN201306UV02ResponseType response = processor.respondingGatewayPRPAIN201305UV02(request, null);
            assertNull("RespondingGatewayPRPAIN201306UV02ResponseType was not null", response);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testRespondingGatewayPRPAIN201305UV02NullAssertion: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testRespondingGatewayPRPAIN201305UV02NullAssertion: " + t.getMessage());
        }
    }

    @Test
    public void testRespondingGatewayPRPAIN201305UV02NullPdMessage()
    {
        try
        {
            EntityPatientDiscoveryProcessor processor = new EntityPatientDiscoveryProcessor()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(aNonNull(String.class)));
                    oneOf(mockLog).warn("PRPAIN201305UV02 was null.");
                }
            });

            RespondingGatewayPRPAIN201305UV02RequestType request = new RespondingGatewayPRPAIN201305UV02RequestType();
            request.setNhinTargetCommunities(mockTargetCommunities);

            RespondingGatewayPRPAIN201306UV02ResponseType response = processor.respondingGatewayPRPAIN201305UV02(request, mockAssertion);
            assertNull("RespondingGatewayPRPAIN201306UV02ResponseType was not null", response);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testRespondingGatewayPRPAIN201305UV02NullPdMessage: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testRespondingGatewayPRPAIN201305UV02NullPdMessage: " + t.getMessage());
        }
    }

    @Test
    public void testRespondingGatewayPRPAIN201305UV02NullNhinTargetCommunities()
    {
        try
        {
            EntityPatientDiscoveryProcessor processor = new EntityPatientDiscoveryProcessor()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected void logAggregatedResponseFromNhin(RespondingGatewayPRPAIN201306UV02ResponseType response, AssertionType assertion)
                {
                }
                @Override
                protected void logEntityPatientDiscoveryRequest(RespondingGatewayPRPAIN201305UV02RequestType request, AssertionType assertion)
                {
                }
                @Override
                protected CMUrlInfos getEndpoints(NhinTargetCommunitiesType targetCommunities)
                {
                    CMUrlInfos urlInfoList = new CMUrlInfos();
                    CMUrlInfo urlInfo = new CMUrlInfo();
                    urlInfoList.getUrlInfo().add(urlInfo);
                    urlInfo.setUrl("url");
                    return urlInfoList;
                }
                @Override
                protected RespondingGatewayPRPAIN201305UV02RequestType createNewRequest(RespondingGatewayPRPAIN201305UV02RequestType request, AssertionType assertion, CMUrlInfo urlInfo)
                {
                    return mockRespondingGatewayPRPAIN201305UV02RequestType;
                }
                @Override
                protected boolean checkPolicy(RespondingGatewayPRPAIN201305UV02RequestType request, AssertionType assertion)
                {
                    return true;
                }
                @Override
                protected PRPAIN201306UV02 sendToNhinProxy(RespondingGatewayPRPAIN201305UV02RequestType newRequest, AssertionType assertion, String url)
                {
                    return mockPRPAIN201306UV02;
                }
                @Override
                protected PatientDiscovery201306Processor getPatientDiscovery201306Processor()
                {
                    return mockPatientDiscovery201306Processor;
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(any(String.class)));
                    oneOf(mockPatientDiscovery201306Processor).storeMapping(with(aNonNull(PRPAIN201306UV02.class)));
                }
            });

            RespondingGatewayPRPAIN201305UV02RequestType request = new RespondingGatewayPRPAIN201305UV02RequestType();
            request.setPRPAIN201305UV02(mockPRPAIN201305UV02);

            RespondingGatewayPRPAIN201306UV02ResponseType response = processor.respondingGatewayPRPAIN201305UV02(request, mockAssertion);
            assertNotNull("RespondingGatewayPRPAIN201306UV02ResponseType was null", response);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testRespondingGatewayPRPAIN201305UV02NullNhinTargetCommunities: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testRespondingGatewayPRPAIN201305UV02NullNhinTargetCommunities: " + t.getMessage());
        }
    }

// </editor-fold>

// <editor-fold defaultstate="collapsed" desc="getResponseFromCommunities">
    @Test
    public void testGetResponseFromCommunitiesHappy()
    {
        try
        {
            EntityPatientDiscoveryProcessor processor = new EntityPatientDiscoveryProcessor()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected void logAggregatedResponseFromNhin(RespondingGatewayPRPAIN201306UV02ResponseType response, AssertionType assertion)
                {
                }
                @Override
                protected void logEntityPatientDiscoveryRequest(RespondingGatewayPRPAIN201305UV02RequestType request, AssertionType assertion)
                {
                }
                @Override
                protected CMUrlInfos getEndpoints(NhinTargetCommunitiesType targetCommunities)
                {
                    CMUrlInfos urlInfoList = new CMUrlInfos();
                    CMUrlInfo urlInfo = new CMUrlInfo();
                    urlInfoList.getUrlInfo().add(urlInfo);
                    urlInfo.setUrl("url");
                    return urlInfoList;
                }
                @Override
                protected RespondingGatewayPRPAIN201305UV02RequestType createNewRequest(RespondingGatewayPRPAIN201305UV02RequestType request, AssertionType assertion, CMUrlInfo urlInfo)
                {
                    return mockRespondingGatewayPRPAIN201305UV02RequestType;
                }
                @Override
                protected boolean checkPolicy(RespondingGatewayPRPAIN201305UV02RequestType request, AssertionType assertion)
                {
                    return true;
                }
                @Override
                protected PRPAIN201306UV02 sendToNhinProxy(RespondingGatewayPRPAIN201305UV02RequestType newRequest, AssertionType assertion, String url)
                {
                    return mockPRPAIN201306UV02;
                }
                @Override
                protected PatientDiscovery201306Processor getPatientDiscovery201306Processor()
                {
                    return mockPatientDiscovery201306Processor;
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(aNonNull(String.class)));
                    oneOf(mockPatientDiscovery201306Processor).storeMapping(with(aNonNull(PRPAIN201306UV02.class)));
                }
            });

            RespondingGatewayPRPAIN201305UV02RequestType request = new RespondingGatewayPRPAIN201305UV02RequestType();
            request.setPRPAIN201305UV02(mockPRPAIN201305UV02);
            request.setNhinTargetCommunities(mockTargetCommunities);

            RespondingGatewayPRPAIN201306UV02ResponseType response = processor.getResponseFromCommunities(request, mockAssertion);
            assertNotNull("RespondingGatewayPRPAIN201306UV02ResponseType was null", response);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testGetResponseFromCommunitiesHappy: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetResponseFromCommunitiesHappy: " + t.getMessage());
        }
    }

    @Test
    public void testGetResponseFromCommunitiesNullUrlList()
    {
        try
        {
            EntityPatientDiscoveryProcessor processor = new EntityPatientDiscoveryProcessor()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected void logAggregatedResponseFromNhin(RespondingGatewayPRPAIN201306UV02ResponseType response, AssertionType assertion)
                {
                }
                @Override
                protected void logEntityPatientDiscoveryRequest(RespondingGatewayPRPAIN201305UV02RequestType request, AssertionType assertion)
                {
                }
                @Override
                protected CMUrlInfos getEndpoints(NhinTargetCommunitiesType targetCommunities)
                {
                    return null;
                }
                @Override
                protected RespondingGatewayPRPAIN201305UV02RequestType createNewRequest(RespondingGatewayPRPAIN201305UV02RequestType request, AssertionType assertion, CMUrlInfo urlInfo)
                {
                    return mockRespondingGatewayPRPAIN201305UV02RequestType;
                }
                @Override
                protected boolean checkPolicy(RespondingGatewayPRPAIN201305UV02RequestType request, AssertionType assertion)
                {
                    return true;
                }
                @Override
                protected PRPAIN201306UV02 sendToNhinProxy(RespondingGatewayPRPAIN201305UV02RequestType newRequest, AssertionType assertion, String url)
                {
                    return mockPRPAIN201306UV02;
                }
                @Override
                protected PatientDiscovery201306Processor getPatientDiscovery201306Processor()
                {
                    return mockPatientDiscovery201306Processor;
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(aNonNull(String.class)));
                    oneOf(mockLog).warn("No targets were found for the Patient Discovery Request");
                }
            });

            RespondingGatewayPRPAIN201305UV02RequestType request = new RespondingGatewayPRPAIN201305UV02RequestType();
            request.setPRPAIN201305UV02(mockPRPAIN201305UV02);
            request.setNhinTargetCommunities(mockTargetCommunities);

            RespondingGatewayPRPAIN201306UV02ResponseType response = processor.getResponseFromCommunities(request, mockAssertion);
            assertNull("RespondingGatewayPRPAIN201306UV02ResponseType was not null", response);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testGetResponseFromCommunitiesNullUrlList: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetResponseFromCommunitiesNullUrlList: " + t.getMessage());
        }
    }

    @Test
    public void testGetResponseFromCommunitiesEmptyUrlList()
    {
        try
        {
            EntityPatientDiscoveryProcessor processor = new EntityPatientDiscoveryProcessor()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected void logAggregatedResponseFromNhin(RespondingGatewayPRPAIN201306UV02ResponseType response, AssertionType assertion)
                {
                }
                @Override
                protected void logEntityPatientDiscoveryRequest(RespondingGatewayPRPAIN201305UV02RequestType request, AssertionType assertion)
                {
                }
                @Override
                protected CMUrlInfos getEndpoints(NhinTargetCommunitiesType targetCommunities)
                {
                    CMUrlInfos urlInfoList = new CMUrlInfos();
                    return urlInfoList;
                }
                @Override
                protected RespondingGatewayPRPAIN201305UV02RequestType createNewRequest(RespondingGatewayPRPAIN201305UV02RequestType request, AssertionType assertion, CMUrlInfo urlInfo)
                {
                    return mockRespondingGatewayPRPAIN201305UV02RequestType;
                }
                @Override
                protected boolean checkPolicy(RespondingGatewayPRPAIN201305UV02RequestType request, AssertionType assertion)
                {
                    return true;
                }
                @Override
                protected PRPAIN201306UV02 sendToNhinProxy(RespondingGatewayPRPAIN201305UV02RequestType newRequest, AssertionType assertion, String url)
                {
                    return mockPRPAIN201306UV02;
                }
                @Override
                protected PatientDiscovery201306Processor getPatientDiscovery201306Processor()
                {
                    return mockPatientDiscovery201306Processor;
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(aNonNull(String.class)));
                    oneOf(mockLog).warn("No targets were found for the Patient Discovery Request");
                }
            });

            RespondingGatewayPRPAIN201305UV02RequestType request = new RespondingGatewayPRPAIN201305UV02RequestType();
            request.setPRPAIN201305UV02(mockPRPAIN201305UV02);
            request.setNhinTargetCommunities(mockTargetCommunities);

            RespondingGatewayPRPAIN201306UV02ResponseType response = processor.getResponseFromCommunities(request, mockAssertion);
            assertNull("RespondingGatewayPRPAIN201306UV02ResponseType was not null", response);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testGetResponseFromCommunitiesEmptyUrlList: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetResponseFromCommunitiesEmptyUrlList: " + t.getMessage());
        }
    }

    @Test
    public void testGetResponseFromCommunitiesFailedPolicyCheck()
    {
        try
        {
            EntityPatientDiscoveryProcessor processor = new EntityPatientDiscoveryProcessor()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected void logAggregatedResponseFromNhin(RespondingGatewayPRPAIN201306UV02ResponseType response, AssertionType assertion)
                {
                }
                @Override
                protected void logEntityPatientDiscoveryRequest(RespondingGatewayPRPAIN201305UV02RequestType request, AssertionType assertion)
                {
                }
                @Override
                protected CMUrlInfos getEndpoints(NhinTargetCommunitiesType targetCommunities)
                {
                    CMUrlInfos urlInfoList = new CMUrlInfos();
                    CMUrlInfo urlInfo = new CMUrlInfo();
                    urlInfoList.getUrlInfo().add(urlInfo);
                    urlInfo.setUrl("url");
                    return urlInfoList;
                }
                @Override
                protected RespondingGatewayPRPAIN201305UV02RequestType createNewRequest(RespondingGatewayPRPAIN201305UV02RequestType request, AssertionType assertion, CMUrlInfo urlInfo)
                {
                    return mockRespondingGatewayPRPAIN201305UV02RequestType;
                }
                @Override
                protected boolean checkPolicy(RespondingGatewayPRPAIN201305UV02RequestType request, AssertionType assertion)
                {
                    return false;
                }
                @Override
                protected PRPAIN201306UV02 sendToNhinProxy(RespondingGatewayPRPAIN201305UV02RequestType newRequest, AssertionType assertion, String url)
                {
                    return mockPRPAIN201306UV02;
                }
                @Override
                protected PatientDiscovery201306Processor getPatientDiscovery201306Processor()
                {
                    return mockPatientDiscovery201306Processor;
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(aNonNull(String.class)));
                    oneOf(mockLog).error("The policy engine evaluated the request and denied the request.");
                }
            });

            RespondingGatewayPRPAIN201305UV02RequestType request = new RespondingGatewayPRPAIN201305UV02RequestType();
            request.setPRPAIN201305UV02(mockPRPAIN201305UV02);
            request.setNhinTargetCommunities(mockTargetCommunities);

            RespondingGatewayPRPAIN201306UV02ResponseType response = processor.getResponseFromCommunities(request, mockAssertion);
            assertNotNull("RespondingGatewayPRPAIN201306UV02ResponseType was null", response);
            assertTrue("Community response list not empty", response.getCommunityResponse().isEmpty());
        }
        catch(Throwable t)
        {
            System.out.println("Error running testGetResponseFromCommunitiesFailedPolicyCheck: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetResponseFromCommunitiesFailedPolicyCheck: " + t.getMessage());
        }
    }


// </editor-fold>

// <editor-fold defaultstate="collapsed" desc="sendToNhinProxy">
    @Test
    public void testSendToNhinProxyHappy()
    {
        try
        {
            EntityPatientDiscoveryProcessor processor = new EntityPatientDiscoveryProcessor()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected void logAggregatedResponseFromNhin(RespondingGatewayPRPAIN201306UV02ResponseType response, AssertionType assertion)
                {
                }
                @Override
                protected void logEntityPatientDiscoveryRequest(RespondingGatewayPRPAIN201305UV02RequestType request, AssertionType assertion)
                {
                }
                @Override
                protected RespondingGatewayPRPAIN201306UV02ResponseType getResponseFromCommunities(RespondingGatewayPRPAIN201305UV02RequestType request, AssertionType assertion)
                {
                    return mockResponse;
                }
                @Override
                protected NhincProxyPatientDiscoverySecuredImpl getNhincProxyPatientDiscoverySecuredImpl()
                {
                    return mockNhincProxyPatientDiscoverySecuredImpl;
                }
                @Override
                protected ResponseFactory getResponseFactory()
                {
                    return mockResponseFactory;
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(aNonNull(String.class)));
                    oneOf(mockNhincProxyPatientDiscoverySecuredImpl).proxyPRPAIN201305UV(with(aNonNull(ProxyPRPAIN201305UVProxySecuredRequestType.class)), with(aNonNull(AssertionType.class)));
                    oneOf(mockResponseFactory).getResponseMode();
                }
            });

            RespondingGatewayPRPAIN201305UV02RequestType request = new RespondingGatewayPRPAIN201305UV02RequestType();
            request.setPRPAIN201305UV02(mockPRPAIN201305UV02);
            request.setNhinTargetCommunities(mockTargetCommunities);

            PRPAIN201306UV02 response = processor.sendToNhinProxy(request, mockAssertion, "url");
            assertNotNull("PRPAIN201306UV02 was null", response);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testSendToNhinProxyHappy: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testSendToNhinProxyHappy: " + t.getMessage());
        }
    }

    @Test
    public void testSendToNhinProxyNullRequest()
    {
        try
        {
            EntityPatientDiscoveryProcessor processor = new EntityPatientDiscoveryProcessor()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected void logAggregatedResponseFromNhin(RespondingGatewayPRPAIN201306UV02ResponseType response, AssertionType assertion)
                {
                }
                @Override
                protected void logEntityPatientDiscoveryRequest(RespondingGatewayPRPAIN201305UV02RequestType request, AssertionType assertion)
                {
                }
                @Override
                protected RespondingGatewayPRPAIN201306UV02ResponseType getResponseFromCommunities(RespondingGatewayPRPAIN201305UV02RequestType request, AssertionType assertion)
                {
                    return mockResponse;
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(aNonNull(String.class)));
                    oneOf(mockLog).warn("RespondingGatewayPRPAIN201305UV02RequestType was null.");
                }
            });

            RespondingGatewayPRPAIN201305UV02RequestType request = null;

            PRPAIN201306UV02 response = processor.sendToNhinProxy(request, mockAssertion, "url");
            assertNull("PRPAIN201306UV02 was not null", response);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testSendToNhinProxyNullRequest: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testSendToNhinProxyNullRequest: " + t.getMessage());
        }
    }

    @Test
    public void testSendToNhinProxyNullAssertion()
    {
        try
        {
            EntityPatientDiscoveryProcessor processor = new EntityPatientDiscoveryProcessor()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected void logAggregatedResponseFromNhin(RespondingGatewayPRPAIN201306UV02ResponseType response, AssertionType assertion)
                {
                }
                @Override
                protected void logEntityPatientDiscoveryRequest(RespondingGatewayPRPAIN201305UV02RequestType request, AssertionType assertion)
                {
                }
                @Override
                protected RespondingGatewayPRPAIN201306UV02ResponseType getResponseFromCommunities(RespondingGatewayPRPAIN201305UV02RequestType request, AssertionType assertion)
                {
                    return mockResponse;
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(aNonNull(String.class)));
                    oneOf(mockLog).warn("AssertionType was null.");
                }
            });

            RespondingGatewayPRPAIN201305UV02RequestType request = new RespondingGatewayPRPAIN201305UV02RequestType();
            request.setPRPAIN201305UV02(mockPRPAIN201305UV02);
            request.setNhinTargetCommunities(mockTargetCommunities);

            PRPAIN201306UV02 response = processor.sendToNhinProxy(request, null, "url");
            assertNull("PRPAIN201306UV02 was not null", response);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testSendToNhinProxyNullAssertion: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testSendToNhinProxyNullAssertion: " + t.getMessage());
        }
    }

    @Test
    public void testSendToNhinProxyNullUrl()
    {
        try
        {
            EntityPatientDiscoveryProcessor processor = new EntityPatientDiscoveryProcessor()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected void logAggregatedResponseFromNhin(RespondingGatewayPRPAIN201306UV02ResponseType response, AssertionType assertion)
                {
                }
                @Override
                protected void logEntityPatientDiscoveryRequest(RespondingGatewayPRPAIN201305UV02RequestType request, AssertionType assertion)
                {
                }
                @Override
                protected RespondingGatewayPRPAIN201306UV02ResponseType getResponseFromCommunities(RespondingGatewayPRPAIN201305UV02RequestType request, AssertionType assertion)
                {
                    return mockResponse;
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(aNonNull(String.class)));
                    oneOf(mockLog).warn("URL was null.");
                }
            });

            RespondingGatewayPRPAIN201305UV02RequestType request = new RespondingGatewayPRPAIN201305UV02RequestType();
            request.setPRPAIN201305UV02(mockPRPAIN201305UV02);
            request.setNhinTargetCommunities(mockTargetCommunities);

            PRPAIN201306UV02 response = processor.sendToNhinProxy(request, mockAssertion, null);
            assertNull("PRPAIN201306UV02 was not null", response);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testSendToNhinProxyNullUrl: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testSendToNhinProxyNullUrl: " + t.getMessage());
        }
    }

    @Test
    public void testSendToNhinProxyRespProcessException()
    {
        try
        {
            EntityPatientDiscoveryProcessor processor = new EntityPatientDiscoveryProcessor()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected void logAggregatedResponseFromNhin(RespondingGatewayPRPAIN201306UV02ResponseType response, AssertionType assertion)
                {
                }
                @Override
                protected void logEntityPatientDiscoveryRequest(RespondingGatewayPRPAIN201305UV02RequestType request, AssertionType assertion)
                {
                }
                @Override
                protected RespondingGatewayPRPAIN201306UV02ResponseType getResponseFromCommunities(RespondingGatewayPRPAIN201305UV02RequestType request, AssertionType assertion)
                {
                    return mockResponse;
                }
                @Override
                protected NhincProxyPatientDiscoverySecuredImpl getNhincProxyPatientDiscoverySecuredImpl()
                {
                    return mockNhincProxyPatientDiscoverySecuredImpl;
                }
                @Override
                protected ResponseFactory getResponseFactory()
                {
                    ResponseFactory respFactory = new ResponseFactory()
                    {
                        @Override
                        public ResponseMode getResponseMode()
                        {
                            ResponseMode respMode = new VerifyMode()
                            {
                                @Override
                                protected Log createLogger()
                                {
                                    return mockLog;
                                }
                                @Override
                                public PRPAIN201306UV02 processResponse(ResponseParams params)
                                {
                                    throw new RuntimeException("Thrown from test");
                                }
                            };
                            return respMode;
                        }
                    };
                    return respFactory;
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(aNonNull(String.class)));
                    oneOf(mockNhincProxyPatientDiscoverySecuredImpl).proxyPRPAIN201305UV(with(aNonNull(ProxyPRPAIN201305UVProxySecuredRequestType.class)), with(aNonNull(AssertionType.class)));
                    oneOf(mockLog).error(with(aNonNull(String.class)), with(aNonNull(RuntimeException.class)));
                }
            });

            RespondingGatewayPRPAIN201305UV02RequestType request = new RespondingGatewayPRPAIN201305UV02RequestType();
            request.setPRPAIN201305UV02(mockPRPAIN201305UV02);
            request.setNhinTargetCommunities(mockTargetCommunities);

            PRPAIN201306UV02 response = processor.sendToNhinProxy(request, mockAssertion, "url");
            assertNotNull("PRPAIN201306UV02 was null", response);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testSendToNhinProxyRespProcessException: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testSendToNhinProxyRespProcessException: " + t.getMessage());
        }
    }

// </editor-fold>

// <editor-fold defaultstate="collapsed" desc="Logging Methods">
    @Test
    public void testLogEntityPatientDiscoveryRequest()
    {
        try
        {
            EntityPatientDiscoveryProcessor processor = new EntityPatientDiscoveryProcessor()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected PatientDiscoveryAuditLogger getPatientDiscoveryAuditLogger()
                {
                    return mockAuditLogger;
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(aNonNull(String.class)));
                    oneOf(mockAuditLogger).auditEntity201305(with(aNonNull(RespondingGatewayPRPAIN201305UV02RequestType.class)), with(aNonNull(AssertionType.class)), with(aNonNull(String.class)));
                }
            });

            RespondingGatewayPRPAIN201305UV02RequestType request = new RespondingGatewayPRPAIN201305UV02RequestType();
            processor.logEntityPatientDiscoveryRequest(request, mockAssertion);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testLogEntityPatientDiscoveryRequest: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testLogEntityPatientDiscoveryRequest: " + t.getMessage());
        }
    }

    @Test
    public void testLogAggregatedResponseFromNhin()
    {
        try
        {
            EntityPatientDiscoveryProcessor processor = new EntityPatientDiscoveryProcessor()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected PatientDiscoveryAuditLogger getPatientDiscoveryAuditLogger()
                {
                    return mockAuditLogger;
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(aNonNull(String.class)));
                    oneOf(mockAuditLogger).auditEntity201306(with(aNonNull(RespondingGatewayPRPAIN201306UV02ResponseType.class)), with(aNonNull(AssertionType.class)), with(aNonNull(String.class)));
                }
            });

            RespondingGatewayPRPAIN201306UV02ResponseType response = new RespondingGatewayPRPAIN201306UV02ResponseType();
            processor.logAggregatedResponseFromNhin(response, mockAssertion);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testLogAggregatedResponseFromNhin: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testLogAggregatedResponseFromNhin: " + t.getMessage());
        }
    }

// </editor-fold>

// <editor-fold defaultstate="collapsed" desc="checkPolicy">
    @Test
    public void testCheckPolicy()
    {
        try
        {
            EntityPatientDiscoveryProcessor processor = new EntityPatientDiscoveryProcessor()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected PatientDiscoveryPolicyChecker getPatientDiscoveryPolicyChecker()
                {
                    return mockPolicyChecker;
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(aNonNull(String.class)));
                    oneOf(mockPolicyChecker).checkOutgoingPolicy(with(aNonNull(RespondingGatewayPRPAIN201305UV02RequestType.class)));
                }
            });

            RespondingGatewayPRPAIN201305UV02RequestType request = new RespondingGatewayPRPAIN201305UV02RequestType();

            processor.checkPolicy(request, mockAssertion);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testLogAggregatedResponseFromNhin: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testLogAggregatedResponseFromNhin: " + t.getMessage());
        }
    }

// </editor-fold>

// <editor-fold defaultstate="collapsed" desc="Misc Utilities">
    @Test
    public void testGetEndpointsNullCommunities()
    {
        try
        {
            EntityPatientDiscoveryProcessor processor = new EntityPatientDiscoveryProcessor()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(aNonNull(String.class)));
                }
            });

            CMUrlInfos urlList = processor.getEndpoints(null);
            assertNotNull("CMUrlInfos was null", urlList);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testGetEndpointsNullCommunities: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetEndpointsNullCommunities: " + t.getMessage());
        }
    }

// </editor-fold>

}