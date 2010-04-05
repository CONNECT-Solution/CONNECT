package gov.hhs.fha.nhinc.redaction;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.PatientPreferencesType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtDocIdRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtDocIdResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtIdRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtIdResponseType;
import gov.hhs.fha.nhinc.policyengine.adapterpip.AdapterPIPException;
import gov.hhs.fha.nhinc.policyengine.adapterpip.AdapterPIPImpl;
import gov.hhs.fha.nhinc.policyengine.adapterpip.PatientConsentManager;
import org.apache.commons.logging.Log;
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
public class PatientConsentHelperTest
{
    Mockery context = new JUnit4Mockery()
    {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };

    final Log mockLog = context.mock(Log.class);
    final AdapterPIPImpl mockPIP = context.mock(AdapterPIPImpl.class);
    final PatientConsentManager mockPatientConsentMgr = context.mock(PatientConsentManager.class);

    @Test
    public void testGetAdapterPIP()
    {
        try
        {
            PatientConsentHelper patientConsentHelper = new PatientConsentHelper();
            AdapterPIPImpl adapterPIP = patientConsentHelper.getAdapterPIP();
            assertNotNull("AdapterPIPImpl was null", adapterPIP);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testGetAdapterPIP test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetAdapterPIP test: " + t.getMessage());
        }
    }

    @Test
    public void testCreateLogger()
    {
        try
        {
            PatientConsentHelper patientConsentHelper = new PatientConsentHelper();
            Log log = patientConsentHelper.createLogger();
            assertNotNull("Log was null", log);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testCreateLogger test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testCreateLogger test: " + t.getMessage());
        }
    }

    @Test
    public void testRetrievePatientConsentbyPatientIdHappy()
    {
        String patientId = "";
        String assigningAuthorityId = "";

        PatientPreferencesType response = testRetrievePatientConsentbyPatientId(patientId, assigningAuthorityId, mockPIP);
        assertNotNull("PatientPreferencesType was null", response);
    }

    @Test
    public void testRetrievePatientConsentbyPatientIdNullInputs()
    {
        String patientId = null;
        String assigningAuthorityId = null;

        PatientPreferencesType response = testRetrievePatientConsentbyPatientId(patientId, assigningAuthorityId, mockPIP);
        assertNotNull("PatientPreferencesType was null", response);
    }

    @Test
    public void testRetrievePatientConsentbyPatientIdNullAdapterPIPResponse()
    {
        String patientId = null;
        String assigningAuthorityId = null;

        final AdapterPIPImpl adapterPIP = new AdapterPIPImpl()
        {
            @Override
            protected Log createLogger()
            {
                return mockLog;
            }

            @Override
            protected PatientConsentManager getPatientConsentManager()
            {
                return mockPatientConsentMgr;
            }

            @Override
            public RetrievePtConsentByPtIdResponseType retrievePtConsentByPtId(RetrievePtConsentByPtIdRequestType request)
                    throws AdapterPIPException
            {
                return null;
            }
        };
        PatientPreferencesType response = testRetrievePatientConsentbyPatientId(patientId, assigningAuthorityId, adapterPIP);
        assertNull("PatientPreferencesType was not null", response);
    }

    @Test (expected= RuntimeException.class)
    public void testRetrievePatientConsentbyPatientIdWithException()
    {
        String patientId = null;
        String assigningAuthorityId = null;

        final AdapterPIPImpl adapterPIP = new AdapterPIPImpl()
        {
            @Override
            protected Log createLogger()
            {
                return mockLog;
            }

            @Override
            protected PatientConsentManager getPatientConsentManager()
            {
                return mockPatientConsentMgr;
            }

            @Override
            public RetrievePtConsentByPtIdResponseType retrievePtConsentByPtId(RetrievePtConsentByPtIdRequestType request)
                    throws AdapterPIPException
            {
                    RetrievePtConsentByPtIdResponseType retrieveResponse = new RetrievePtConsentByPtIdResponseType()
                    {
                        @Override
                        public PatientPreferencesType getPatientPreferences()
                        {
                            throw new RuntimeException();
                        }
                    };
                    return retrieveResponse;
            }
        };
        PatientPreferencesType response = testRetrievePatientConsentbyPatientId(patientId, assigningAuthorityId, adapterPIP);
        assertNull("PatientPreferencesType was not null", response);
    }

    public PatientPreferencesType testRetrievePatientConsentbyPatientId(String patientId, String assigningAuthorityId, final AdapterPIPImpl adapterPIP)
    {
        PatientPreferencesType response = null;
        try
        {
            PatientConsentHelper patientConsentHelper = new PatientConsentHelper()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }

                @Override
                protected AdapterPIPImpl getAdapterPIP()
                {
                    return adapterPIP;
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(any(String.class)));
                    oneOf(mockPIP).retrievePtConsentByPtId(with(aNonNull(RetrievePtConsentByPtIdRequestType.class)));
                }
            });

            response = patientConsentHelper.retrievePatientConsentbyPatientId(patientId, assigningAuthorityId);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testRetrievePatientConsentbyPatientId test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testRetrievePatientConsentbyPatientId test: " + t.getMessage());
        }
        return response;
    }

    @Test
    public void testRetrievePatientConsentbyDocumentIdHappy()
    {
        try
        {
            String homeCommunityId = "";
            String repositoryId = "";
            String documentId = "";
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(any(String.class)));
                    oneOf(mockPIP).retrievePtConsentByPtDocId(with(aNonNull(RetrievePtConsentByPtDocIdRequestType.class)));
                }
            });
            PatientPreferencesType patientPreferences = testRetrievePatientConsentbyDocumentId(homeCommunityId, repositoryId, documentId, mockPIP);
            assertNotNull("PatientPreferencesType was null", patientPreferences);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testRetrievePatientConsentbyDocumentIdHappy test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testRetrievePatientConsentbyDocumentIdHappy test: " + t.getMessage());
        }
    }

    @Test
    public void testRetrievePatientConsentbyDocumentIdNullInputs()
    {
        try
        {
            String homeCommunityId = null;
            String repositoryId = null;
            String documentId = null;
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(any(String.class)));
                    oneOf(mockPIP).retrievePtConsentByPtDocId(with(aNonNull(RetrievePtConsentByPtDocIdRequestType.class)));
                }
            });
            PatientPreferencesType patientPreferences = testRetrievePatientConsentbyDocumentId(homeCommunityId, repositoryId, documentId, mockPIP);
            assertNotNull("PatientPreferencesType was null", patientPreferences);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testRetrievePatientConsentbyDocumentId test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testRetrievePatientConsentbyDocumentId test: " + t.getMessage());
        }
    }

    @Test
    public void testRetrievePatientConsentbyDocumentIdNullAdapterPIPResponse()
    {
        try
        {
            String homeCommunityId = null;
            String repositoryId = null;
            String documentId = null;
            final AdapterPIPImpl adapterPIP = new AdapterPIPImpl()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }

                @Override
                protected PatientConsentManager getPatientConsentManager()
                {
                    return mockPatientConsentMgr;
                }

                @Override
                public RetrievePtConsentByPtDocIdResponseType retrievePtConsentByPtDocId(RetrievePtConsentByPtDocIdRequestType request)
                        throws AdapterPIPException
                {
                    return null;
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(any(String.class)));
                    //oneOf(mockPIP).retrievePtConsentByPtDocId(with(aNonNull(RetrievePtConsentByPtDocIdRequestType.class)));
                }
            });
            PatientPreferencesType patientPreferences = testRetrievePatientConsentbyDocumentId(homeCommunityId, repositoryId, documentId, adapterPIP);
            assertNull("PatientPreferencesType was not null", patientPreferences);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testRetrievePatientConsentbyDocumentId test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testRetrievePatientConsentbyDocumentId test: " + t.getMessage());
        }
    }

    @Test (expected= RuntimeException.class)
    public void testRetrievePatientConsentbyDocumentIdWithException()
    {
        try
        {
            String homeCommunityId = null;
            String repositoryId = null;
            String documentId = null;
            final AdapterPIPImpl adapterPIP = new AdapterPIPImpl()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }

                @Override
                protected PatientConsentManager getPatientConsentManager()
                {
                    return mockPatientConsentMgr;
                }

                @Override
                public RetrievePtConsentByPtDocIdResponseType retrievePtConsentByPtDocId(RetrievePtConsentByPtDocIdRequestType request)
                        throws AdapterPIPException
                {
                    RetrievePtConsentByPtDocIdResponseType retrieveResponse = new RetrievePtConsentByPtDocIdResponseType()
                    {
                        @Override
                        public PatientPreferencesType getPatientPreferences()
                        {
                            throw new RuntimeException();
                        }
                    };
                    return retrieveResponse;
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(any(String.class)));
                    
                }
            });
            PatientPreferencesType patientPreferences = testRetrievePatientConsentbyDocumentId(homeCommunityId, repositoryId, documentId, adapterPIP);
            assertNull("PatientPreferencesType was not null", patientPreferences);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testRetrievePatientConsentbyDocumentId test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testRetrievePatientConsentbyDocumentId test: " + t.getMessage());
        }
    }

    public PatientPreferencesType testRetrievePatientConsentbyDocumentId(String homeCommunityId, String repositoryId, String documentId, final AdapterPIPImpl adapterPIP)
    {
        PatientPreferencesType response = null;
            PatientConsentHelper patientConsentHelper = new PatientConsentHelper()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }

                @Override
                protected AdapterPIPImpl getAdapterPIP()
                {
                    return adapterPIP;
                }
            };

            response = patientConsentHelper.retrievePatientConsentbyDocumentId(homeCommunityId, repositoryId, documentId);
        return response;
    }

}