package gov.hhs.fha.nhinc.redaction;

import gov.hhs.fha.nhinc.common.nhinccommon.CeType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.FineGrainedPolicyCriteriaType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.FineGrainedPolicyCriterionType;
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
        try
        {
            String patientId = "";
            String assigningAuthorityId = "";
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(any(String.class)));
                    oneOf(mockPIP).retrievePtConsentByPtId(with(aNonNull(RetrievePtConsentByPtIdRequestType.class)));
                }
            });

            PatientPreferencesType response = testRetrievePatientConsentbyPatientId(patientId, assigningAuthorityId, mockPIP);
            assertNotNull("PatientPreferencesType was null", response);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testRetrievePatientConsentbyPatientIdHappy test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testRetrievePatientConsentbyPatientIdHappy test: " + t.getMessage());
        }
    }

    @Test
    public void testRetrievePatientConsentbyPatientIdNullInputs()
    {
        try
        {
            String patientId = null;
            String assigningAuthorityId = null;
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(any(String.class)));
                    oneOf(mockPIP).retrievePtConsentByPtId(with(aNonNull(RetrievePtConsentByPtIdRequestType.class)));
                }
            });

            PatientPreferencesType response = testRetrievePatientConsentbyPatientId(patientId, assigningAuthorityId, mockPIP);
            assertNotNull("PatientPreferencesType was null", response);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testRetrievePatientConsentbyPatientIdNullInputs test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testRetrievePatientConsentbyPatientIdNullInputs test: " + t.getMessage());
        }
    }

    @Test
    public void testRetrievePatientConsentbyPatientIdNullAdapterPIPResponse()
    {
        try
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
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(any(String.class)));
                }
            });
            PatientPreferencesType response = testRetrievePatientConsentbyPatientId(patientId, assigningAuthorityId, adapterPIP);
            assertNull("PatientPreferencesType was not null", response);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testRetrievePatientConsentbyPatientIdNullAdapterPIPResponse test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testRetrievePatientConsentbyPatientIdNullAdapterPIPResponse test: " + t.getMessage());
        }
    }

    @Test
    public void testRetrievePatientConsentbyPatientIdWithException()
    {
        try
        {
            String patientId = null;
            String assigningAuthorityId = null;
            final AdapterPIPImpl adapterPIP = new AdapterPIPImpl() {

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
                public RetrievePtConsentByPtIdResponseType retrievePtConsentByPtId(RetrievePtConsentByPtIdRequestType request) throws AdapterPIPException
                {
                    RetrievePtConsentByPtIdResponseType retrieveResponse = new RetrievePtConsentByPtIdResponseType() {

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
                    allowing(mockLog).error(with(any(String.class)), with(aNonNull(RuntimeException.class)));
                }
            });
            PatientPreferencesType response = testRetrievePatientConsentbyPatientId(patientId, assigningAuthorityId, adapterPIP);
            assertNull("PatientPreferencesType was not null", response);
        }
        catch (AdapterPIPException ex)
        {
            System.out.println("Error running testRetrievePatientConsentbyPatientIdWithException test: " + ex.getMessage());
            ex.printStackTrace();
            fail("Error running testRetrievePatientConsentbyPatientIdWithException test: " + ex.getMessage());
        }
    }

    public PatientPreferencesType testRetrievePatientConsentbyPatientId(String patientId, String assigningAuthorityId, final AdapterPIPImpl adapterPIP) throws AdapterPIPException
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

        response = patientConsentHelper.retrievePatientConsentbyPatientId(patientId, assigningAuthorityId);
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

    @Test
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
                    allowing(mockLog).error(with(any(String.class)), with(aNonNull(RuntimeException.class)));
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

    @Test
    public void testExtractDocTypeFromPatPref()
    {
        PatientConsentHelper testSubject = new PatientConsentHelper()
        {
            @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
        };

        CeType ceType = new CeType();
        ceType.setCode("testing");

        FineGrainedPolicyCriterionType criterionType = new FineGrainedPolicyCriterionType();
        criterionType.setDocumentTypeCode(ceType);

        FineGrainedPolicyCriteriaType findGrainedPolicy = new FineGrainedPolicyCriteriaType();
        findGrainedPolicy.getFineGrainedPolicyCriterion().add(criterionType);

        PatientPreferencesType ptPreferences = new PatientPreferencesType();
        ptPreferences.setFineGrainedPolicyCriteria(findGrainedPolicy);

        context.checking(new Expectations(){{
            ignoring(mockLog).debug(with(any(String.class)));
        }});

        assertNotNull(testSubject.extractDocTypeFromPatPref("testing", ptPreferences));
        assertEquals(testSubject.extractDocTypeFromPatPref("testing", ptPreferences), true);
        context.assertIsSatisfied();
    }

    @Test
    public void testExtractDocTypeFromPatPrefShouldFail()
    {
        PatientConsentHelper testSubject = new PatientConsentHelper()
        {
            @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
        };


        context.checking(new Expectations(){{
            allowing(mockLog).debug(with(any(String.class)));
            allowing(mockLog).error("Invalid documentType");
            allowing(mockLog).error("Error retrieving Fine Grained Policy Criteria");
        }});

        assertEquals(testSubject.extractDocTypeFromPatPref(null, null), false);
        PatientPreferencesType ptPreferences = new PatientPreferencesType();
        assertEquals(testSubject.extractDocTypeFromPatPref("testing", ptPreferences), false);
        FineGrainedPolicyCriteriaType fineGrainedPolicy = new FineGrainedPolicyCriteriaType();
        ptPreferences.setFineGrainedPolicyCriteria(fineGrainedPolicy);
        assertEquals(testSubject.extractDocTypeFromPatPref("testing", ptPreferences), false);
        fineGrainedPolicy.getFineGrainedPolicyCriterion().add(null);
        assertEquals(testSubject.extractDocTypeFromPatPref("testing", ptPreferences), false);
        FineGrainedPolicyCriterionType policyCrtiterion = new FineGrainedPolicyCriterionType();
        fineGrainedPolicy.getFineGrainedPolicyCriterion().add(policyCrtiterion);
        assertEquals(testSubject.extractDocTypeFromPatPref("testing", ptPreferences), false);
        CeType ceTypeCd = new CeType();
        ceTypeCd.setCode("testing1");
        policyCrtiterion.setDocumentTypeCode(ceTypeCd);
        assertEquals(testSubject.extractDocTypeFromPatPref("testing", ptPreferences), false);
        fineGrainedPolicy.getFineGrainedPolicyCriterion().clear();
        ceTypeCd.setCode("testing1");
        policyCrtiterion.setDocumentTypeCode(ceTypeCd);
        CeType ceTypeCd1 = new CeType();
        ceTypeCd1.setCode("testing");
        FineGrainedPolicyCriterionType policyCrtiterion1 = new FineGrainedPolicyCriterionType();
        policyCrtiterion1.setDocumentTypeCode(ceTypeCd1);
        fineGrainedPolicy.getFineGrainedPolicyCriterion().add(policyCrtiterion);
        fineGrainedPolicy.getFineGrainedPolicyCriterion().add(policyCrtiterion1);
        assertEquals(testSubject.extractDocTypeFromPatPref("testing", ptPreferences), true);
        context.assertIsSatisfied();
    }
}

