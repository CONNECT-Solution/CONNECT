/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services. 
 * All rights reserved. 
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met: 
 *     * Redistributions of source code must retain the above 
 *       copyright notice, this list of conditions and the following disclaimer. 
 *     * Redistributions in binary form must reproduce the above copyright 
 *       notice, this list of conditions and the following disclaimer in the documentation 
 *       and/or other materials provided with the distribution. 
 *     * Neither the name of the United States Government nor the 
 *       names of its contributors may be used to endorse or promote products 
 *       derived from this software without specific prior written permission. 
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
 * DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY 
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND 
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS 
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */
package gov.hhs.fha.nhinc.redactionengine.adapter;

import gov.hhs.fha.nhinc.common.nhinccommon.CeType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.FineGrainedPolicyCriteriaType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.FineGrainedPolicyCriterionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.PatientPreferencesType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtDocIdRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtDocIdResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtIdRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RetrievePtConsentByPtIdResponseType;
import gov.hhs.fha.nhinc.policyengine.adapter.pip.AdapterPIPException;
import gov.hhs.fha.nhinc.policyengine.adapter.pip.AdapterPIPImpl;
import gov.hhs.fha.nhinc.policyengine.adapter.pip.PatientConsentManager;
import gov.hhs.fha.nhinc.redactionengine.adapter.PatientConsentHelper;

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
public class PatientConsentHelperTest {
    Mockery context = new JUnit4Mockery() {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };

    final AdapterPIPImpl mockPIP = context.mock(AdapterPIPImpl.class);
    final PatientConsentManager mockPatientConsentMgr = context.mock(PatientConsentManager.class);

    @Test
    public void testGetAdapterPIP() {
        try {
            PatientConsentHelper patientConsentHelper = new PatientConsentHelper() {
                @Override
                protected AdapterPIPImpl getAdapterPIP() {
                    return mockPIP;
                }
            };
            AdapterPIPImpl adapterPIP = patientConsentHelper.getAdapterPIP();
            assertNotNull("AdapterPIPImpl was null", adapterPIP);
        } catch (Throwable t) {
            System.out.println("Error running testGetAdapterPIP test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetAdapterPIP test: " + t.getMessage());
        }
    }

    @Test
    public void testRetrievePatientConsentbyPatientIdHappy() {
        try {
            String patientId = "";
            String assigningAuthorityId = "";
            context.checking(new Expectations() {
                {
                    oneOf(mockPIP).retrievePtConsentByPtId(with(aNonNull(RetrievePtConsentByPtIdRequestType.class)));
                }
            });

            PatientPreferencesType response = testRetrievePatientConsentbyPatientId(patientId, assigningAuthorityId,
                    mockPIP);
            assertNotNull("PatientPreferencesType was null", response);
        } catch (Throwable t) {
            System.out.println("Error running testRetrievePatientConsentbyPatientIdHappy test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testRetrievePatientConsentbyPatientIdHappy test: " + t.getMessage());
        }
    }

    @Test
    public void testRetrievePatientConsentbyPatientIdNullInputs() {
        try {
            String patientId = null;
            String assigningAuthorityId = null;
            context.checking(new Expectations() {
                {
                    oneOf(mockPIP).retrievePtConsentByPtId(with(aNonNull(RetrievePtConsentByPtIdRequestType.class)));
                }
            });

            PatientPreferencesType response = testRetrievePatientConsentbyPatientId(patientId, assigningAuthorityId,
                    mockPIP);
            assertNotNull("PatientPreferencesType was null", response);
        } catch (Throwable t) {
            System.out.println("Error running testRetrievePatientConsentbyPatientIdNullInputs test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testRetrievePatientConsentbyPatientIdNullInputs test: " + t.getMessage());
        }
    }

    @Test
    public void testRetrievePatientConsentbyPatientIdNullAdapterPIPResponse() {
        try {
            String patientId = null;
            String assigningAuthorityId = null;

            final AdapterPIPImpl adapterPIP = new AdapterPIPImpl() {
               @Override
                protected PatientConsentManager getPatientConsentManager() {
                    return mockPatientConsentMgr;
                }

                @Override
                public RetrievePtConsentByPtIdResponseType retrievePtConsentByPtId(
                        RetrievePtConsentByPtIdRequestType request) throws AdapterPIPException {
                    return null;
                }
            };
            PatientPreferencesType response = testRetrievePatientConsentbyPatientId(patientId, assigningAuthorityId,
                    adapterPIP);
            assertNull("PatientPreferencesType was not null", response);
        } catch (Throwable t) {
            System.out.println("Error running testRetrievePatientConsentbyPatientIdNullAdapterPIPResponse test: "
                    + t.getMessage());
            t.printStackTrace();
            fail("Error running testRetrievePatientConsentbyPatientIdNullAdapterPIPResponse test: " + t.getMessage());
        }
    }

    @Test
    public void testRetrievePatientConsentbyPatientIdWithException() {
        try {
            String patientId = null;
            String assigningAuthorityId = null;
            final AdapterPIPImpl adapterPIP = new AdapterPIPImpl() {

            	@Override
                protected PatientConsentManager getPatientConsentManager() {
                    return mockPatientConsentMgr;
                }

                @Override
                public RetrievePtConsentByPtIdResponseType retrievePtConsentByPtId(
                        RetrievePtConsentByPtIdRequestType request) throws AdapterPIPException {
                    RetrievePtConsentByPtIdResponseType retrieveResponse = new RetrievePtConsentByPtIdResponseType() {

                        @Override
                        public PatientPreferencesType getPatientPreferences() {
                            throw new RuntimeException();
                        }
                    };
                    return retrieveResponse;
                }
            };
            PatientPreferencesType response = testRetrievePatientConsentbyPatientId(patientId, assigningAuthorityId,
                    adapterPIP);
            assertNull("PatientPreferencesType was not null", response);
        } catch (AdapterPIPException ex) {
            System.out.println("Error running testRetrievePatientConsentbyPatientIdWithException test: "
                    + ex.getMessage());
            ex.printStackTrace();
            fail("Error running testRetrievePatientConsentbyPatientIdWithException test: " + ex.getMessage());
        }
    }

    public PatientPreferencesType testRetrievePatientConsentbyPatientId(String patientId, String assigningAuthorityId,
            final AdapterPIPImpl adapterPIP) throws AdapterPIPException {
        PatientPreferencesType response = null;
        PatientConsentHelper patientConsentHelper = new PatientConsentHelper() {
            @Override
            protected AdapterPIPImpl getAdapterPIP() {
                return adapterPIP;
            }
        };

        response = patientConsentHelper.retrievePatientConsentbyPatientId(patientId, assigningAuthorityId);
        return response;
    }

    @Test
    public void testRetrievePatientConsentbyDocumentIdHappy() {
        try {
            String homeCommunityId = "";
            String repositoryId = "";
            String documentId = "";
            context.checking(new Expectations() {
                {
                    oneOf(mockPIP).retrievePtConsentByPtDocId(
                            with(aNonNull(RetrievePtConsentByPtDocIdRequestType.class)));
                }
            });
            PatientPreferencesType patientPreferences = testRetrievePatientConsentbyDocumentId(homeCommunityId,
                    repositoryId, documentId, mockPIP);
            assertNotNull("PatientPreferencesType was null", patientPreferences);
        } catch (Throwable t) {
            System.out.println("Error running testRetrievePatientConsentbyDocumentIdHappy test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testRetrievePatientConsentbyDocumentIdHappy test: " + t.getMessage());
        }
    }

    @Test
    public void testRetrievePatientConsentbyDocumentIdNullInputs() {
        try {
            String homeCommunityId = null;
            String repositoryId = null;
            String documentId = null;
            context.checking(new Expectations() {
                {
                    oneOf(mockPIP).retrievePtConsentByPtDocId(
                            with(aNonNull(RetrievePtConsentByPtDocIdRequestType.class)));
                }
            });
            PatientPreferencesType patientPreferences = testRetrievePatientConsentbyDocumentId(homeCommunityId,
                    repositoryId, documentId, mockPIP);
            assertNotNull("PatientPreferencesType was null", patientPreferences);
        } catch (Throwable t) {
            System.out.println("Error running testRetrievePatientConsentbyDocumentId test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testRetrievePatientConsentbyDocumentId test: " + t.getMessage());
        }
    }

    @Test
    public void testRetrievePatientConsentbyDocumentIdNullAdapterPIPResponse() {
        try {
            String homeCommunityId = null;
            String repositoryId = null;
            String documentId = null;
            final AdapterPIPImpl adapterPIP = new AdapterPIPImpl() {
               @Override
                protected PatientConsentManager getPatientConsentManager() {
                    return mockPatientConsentMgr;
                }

                @Override
                public RetrievePtConsentByPtDocIdResponseType retrievePtConsentByPtDocId(
                        RetrievePtConsentByPtDocIdRequestType request) throws AdapterPIPException {
                    return null;
                }
            };
            
            PatientPreferencesType patientPreferences = testRetrievePatientConsentbyDocumentId(homeCommunityId,
                    repositoryId, documentId, adapterPIP);
            assertNull("PatientPreferencesType was not null", patientPreferences);
        } catch (Throwable t) {
            System.out.println("Error running testRetrievePatientConsentbyDocumentId test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testRetrievePatientConsentbyDocumentId test: " + t.getMessage());
        }
    }

    @Test
    public void testRetrievePatientConsentbyDocumentIdWithException() {
        try {
            String homeCommunityId = null;
            String repositoryId = null;
            String documentId = null;
            final AdapterPIPImpl adapterPIP = new AdapterPIPImpl() {
                @Override
                protected PatientConsentManager getPatientConsentManager() {
                    return mockPatientConsentMgr;
                }

                @Override
                public RetrievePtConsentByPtDocIdResponseType retrievePtConsentByPtDocId(
                        RetrievePtConsentByPtDocIdRequestType request) throws AdapterPIPException {
                    RetrievePtConsentByPtDocIdResponseType retrieveResponse = new RetrievePtConsentByPtDocIdResponseType() {
                        @Override
                        public PatientPreferencesType getPatientPreferences() {
                            throw new RuntimeException();
                        }
                    };
                    return retrieveResponse;
                }
            };
            
            PatientPreferencesType patientPreferences = testRetrievePatientConsentbyDocumentId(homeCommunityId,
                    repositoryId, documentId, adapterPIP);
            assertNull("PatientPreferencesType was not null", patientPreferences);
        } catch (Throwable t) {
            System.out.println("Error running testRetrievePatientConsentbyDocumentId test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testRetrievePatientConsentbyDocumentId test: " + t.getMessage());
        }
    }

    public PatientPreferencesType testRetrievePatientConsentbyDocumentId(String homeCommunityId, String repositoryId,
            String documentId, final AdapterPIPImpl adapterPIP) {
        PatientPreferencesType response = null;
        PatientConsentHelper patientConsentHelper = new PatientConsentHelper() {
            @Override
            protected AdapterPIPImpl getAdapterPIP() {
                return adapterPIP;
            }
        };

        response = patientConsentHelper.retrievePatientConsentbyDocumentId(homeCommunityId, repositoryId, documentId);
        return response;
    }

    @Test
    public void testDocumentSharingAllowedOptInPermitType() {
        try {
            PatientConsentHelper testSubject = new PatientConsentHelper() {
                @Override
                protected AdapterPIPImpl getAdapterPIP() {
                    return mockPIP;
                }
            };

            CeType ceType = new CeType();
            ceType.setCode("testing");

            FineGrainedPolicyCriterionType criterionType = new FineGrainedPolicyCriterionType();
            criterionType.setDocumentTypeCode(ceType);
            criterionType.setPermit(true);

            FineGrainedPolicyCriteriaType findGrainedPolicy = new FineGrainedPolicyCriteriaType();
            findGrainedPolicy.getFineGrainedPolicyCriterion().add(criterionType);

            PatientPreferencesType ptPreferences = new PatientPreferencesType();
            ptPreferences.setFineGrainedPolicyCriteria(findGrainedPolicy);
            ptPreferences.setOptIn(true);

            assertNotNull(testSubject.documentSharingAllowed("testing", ptPreferences));
            assertEquals(testSubject.documentSharingAllowed("testing", ptPreferences), true);
        } catch (Throwable t) {
            System.out.println("Error running testDocumentSharingAllowedOptInPermitType test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testDocumentSharingAllowedOptInPermitType test: " + t.getMessage());
        }
    }

    @Test
    public void testDocumentSharingAllowedOptInDenyType() {
        try {
            PatientConsentHelper testSubject = new PatientConsentHelper() {
                @Override
                protected AdapterPIPImpl getAdapterPIP() {
                    return mockPIP;
                }
            };

            CeType ceType = new CeType();
            ceType.setCode("testing");

            FineGrainedPolicyCriterionType criterionType = new FineGrainedPolicyCriterionType();
            criterionType.setDocumentTypeCode(ceType);
            criterionType.setPermit(false);

            FineGrainedPolicyCriteriaType findGrainedPolicy = new FineGrainedPolicyCriteriaType();
            findGrainedPolicy.getFineGrainedPolicyCriterion().add(criterionType);

            PatientPreferencesType ptPreferences = new PatientPreferencesType();
            ptPreferences.setFineGrainedPolicyCriteria(findGrainedPolicy);
            ptPreferences.setOptIn(true);

            assertNotNull(testSubject.documentSharingAllowed("testing", ptPreferences));
            assertEquals(testSubject.documentSharingAllowed("testing", ptPreferences), false);
        } catch (Throwable t) {
            System.out.println("Error running testDocumentSharingAllowedOptInDenyType test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testDocumentSharingAllowedOptInDenyType test: " + t.getMessage());
        }
    }

    @Test
    public void testDocumentSharingAllowedOptOutPermitType() {
        try {
            PatientConsentHelper testSubject = new PatientConsentHelper() {
                @Override
                protected AdapterPIPImpl getAdapterPIP() {
                    return mockPIP;
                }
            };

            CeType ceType = new CeType();
            ceType.setCode("testing");

            FineGrainedPolicyCriterionType criterionType = new FineGrainedPolicyCriterionType();
            criterionType.setDocumentTypeCode(ceType);
            criterionType.setPermit(true);

            FineGrainedPolicyCriteriaType findGrainedPolicy = new FineGrainedPolicyCriteriaType();
            findGrainedPolicy.getFineGrainedPolicyCriterion().add(criterionType);

            PatientPreferencesType ptPreferences = new PatientPreferencesType();
            ptPreferences.setFineGrainedPolicyCriteria(findGrainedPolicy);
            ptPreferences.setOptIn(false);

            assertNotNull(testSubject.documentSharingAllowed("testing", ptPreferences));
            assertEquals(testSubject.documentSharingAllowed("testing", ptPreferences), true);
            
        } catch (Throwable t) {
            System.out.println("Error running testDocumentSharingAllowedOptOutPermitType test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testDocumentSharingAllowedOptOutPermitType test: " + t.getMessage());
        }
    }

    @Test
    public void testDocumentSharingAllowedOptOutDenyType() {
        try {
            PatientConsentHelper testSubject = new PatientConsentHelper() {
                @Override
                protected AdapterPIPImpl getAdapterPIP() {
                    return mockPIP;
                }
            };

            CeType ceType = new CeType();
            ceType.setCode("testing");

            FineGrainedPolicyCriterionType criterionType = new FineGrainedPolicyCriterionType();
            criterionType.setDocumentTypeCode(ceType);
            criterionType.setPermit(false);

            FineGrainedPolicyCriteriaType findGrainedPolicy = new FineGrainedPolicyCriteriaType();
            findGrainedPolicy.getFineGrainedPolicyCriterion().add(criterionType);

            PatientPreferencesType ptPreferences = new PatientPreferencesType();
            ptPreferences.setFineGrainedPolicyCriteria(findGrainedPolicy);
            ptPreferences.setOptIn(false);

            assertNotNull(testSubject.documentSharingAllowed("testing", ptPreferences));
            assertEquals(testSubject.documentSharingAllowed("testing", ptPreferences), false);
            
        } catch (Throwable t) {
            System.out.println("Error running testDocumentSharingAllowedOptOutDenyType test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testDocumentSharingAllowedOptOutDenyType test: " + t.getMessage());
        }
    }

    @Test
    public void testDocumentSharingAllowedShouldFail() {
        try {
            PatientConsentHelper testSubject = new PatientConsentHelper() {
                @Override
                protected AdapterPIPImpl getAdapterPIP() {
                    return mockPIP;
                }
            };

            assertEquals(testSubject.documentSharingAllowed(null, null), false);
            PatientPreferencesType ptPreferences = new PatientPreferencesType();
            assertFalse("Test with only patient preferences instantiated",
                    testSubject.documentSharingAllowed("testing", ptPreferences));
            FineGrainedPolicyCriteriaType fineGrainedPolicy = new FineGrainedPolicyCriteriaType();
            ptPreferences.setFineGrainedPolicyCriteria(fineGrainedPolicy);
            assertFalse("Test with default fine grained policy",
                    testSubject.documentSharingAllowed("testing", ptPreferences));
            fineGrainedPolicy.getFineGrainedPolicyCriterion().add(null);
            assertFalse("Test with null criterion", testSubject.documentSharingAllowed("testing", ptPreferences));
            FineGrainedPolicyCriterionType policyCrtiterion = new FineGrainedPolicyCriterionType();
            fineGrainedPolicy.getFineGrainedPolicyCriterion().add(policyCrtiterion);
            assertFalse("Test with default crition", testSubject.documentSharingAllowed("testing", ptPreferences));
            CeType ceTypeCd = new CeType();
            ceTypeCd.setCode("testing1");
            policyCrtiterion.setDocumentTypeCode(ceTypeCd);
            assertFalse("Test with no document match and no default criterion",
                    testSubject.documentSharingAllowed("testing", ptPreferences));
            fineGrainedPolicy.getFineGrainedPolicyCriterion().clear();
            ceTypeCd.setCode("testing1");
            policyCrtiterion.setDocumentTypeCode(ceTypeCd);
            CeType ceTypeCd1 = new CeType();
            ceTypeCd1.setCode("testing");
            FineGrainedPolicyCriterionType policyCrtiterion1 = new FineGrainedPolicyCriterionType();
            policyCrtiterion1.setDocumentTypeCode(ceTypeCd1);
            policyCrtiterion1.setPermit(true);
            fineGrainedPolicy.getFineGrainedPolicyCriterion().add(policyCrtiterion);
            fineGrainedPolicy.getFineGrainedPolicyCriterion().add(policyCrtiterion1);
            assertTrue("Test with document match should pass",
                    testSubject.documentSharingAllowed("testing", ptPreferences));
            context.assertIsSatisfied();
        } catch (Throwable t) {
            System.out.println("Error running testDocumentSharingAllowedShouldFail test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testDocumentSharingAllowedShouldFail test: " + t.getMessage());
        }
    }

    @Test
    public void testDocumentSharingAllowedNullPatientPreferences() {
        try {
            PatientConsentHelper testSubject = new PatientConsentHelper() {
                @Override
                protected AdapterPIPImpl getAdapterPIP() {
                    return mockPIP;
                }
            };

            PatientPreferencesType ptPreferences = null;

            assertEquals(testSubject.documentSharingAllowed("testing", ptPreferences), false);
            context.assertIsSatisfied();
        } catch (Throwable t) {
            System.out
                    .println("Error running testDocumentSharingAllowedNullPatientPreferences test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testDocumentSharingAllowedNullPatientPreferences test: " + t.getMessage());
        }
    }

    @Test
    public void testDocumentSharingAllowedNullDocType() {
        try {
            PatientConsentHelper testSubject = new PatientConsentHelper() {
                @Override
                protected AdapterPIPImpl getAdapterPIP() {
                    return mockPIP;
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

            assertEquals(testSubject.documentSharingAllowed(null, ptPreferences), false);
            
        } catch (Throwable t) {
            System.out.println("Error running testDocumentSharingAllowedNullDocType test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testDocumentSharingAllowedNullDocType test: " + t.getMessage());
        }
    }

    @Test
    public void testDocumentSharingAllowedEmptyDocType() {
        try {
            PatientConsentHelper testSubject = new PatientConsentHelper() {
                @Override
                protected AdapterPIPImpl getAdapterPIP() {
                    return mockPIP;
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

            assertEquals(testSubject.documentSharingAllowed("", ptPreferences), false);
            
        } catch (Throwable t) {
            System.out.println("Error running testDocumentSharingAllowedEmptyDocType test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testDocumentSharingAllowedEmptyDocType test: " + t.getMessage());
        }
    }

    @Test
    public void testDocumentSharingAllowedNullPolicyCriteria() {
        try {
            PatientConsentHelper testSubject = new PatientConsentHelper() {
                @Override
                protected AdapterPIPImpl getAdapterPIP() {
                    return mockPIP;
                }
            };

            FineGrainedPolicyCriteriaType findGrainedPolicy = null;
            PatientPreferencesType ptPreferences = new PatientPreferencesType();
            ptPreferences.setFineGrainedPolicyCriteria(findGrainedPolicy);

            assertEquals(testSubject.documentSharingAllowed("testing", ptPreferences), false);
            
        } catch (Throwable t) {
            System.out.println("Error running testDocumentSharingAllowedNullPolicyCriteria test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testDocumentSharingAllowedNullPolicyCriteria test: " + t.getMessage());
        }
    }

    @Test
    public void testDocumentSharingAllowedPolicyCriteriaEmpty() {
        try {
            PatientConsentHelper testSubject = new PatientConsentHelper() {
                @Override
                protected AdapterPIPImpl getAdapterPIP() {
                    return mockPIP;
                }
            };

            FineGrainedPolicyCriteriaType findGrainedPolicy = new FineGrainedPolicyCriteriaType();

            PatientPreferencesType ptPreferences = new PatientPreferencesType();
            ptPreferences.setFineGrainedPolicyCriteria(findGrainedPolicy);

            assertEquals(testSubject.documentSharingAllowed("testing", ptPreferences), false);
            
        } catch (Throwable t) {
            System.out.println("Error running testDocumentSharingAllowedPolicyCriteriaEmpty test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testDocumentSharingAllowedPolicyCriteriaEmpty test: " + t.getMessage());
        }
    }

    @Test
    public void testDocumentSharingAllowedNullPolicyCriterion() {
        try {
            PatientConsentHelper testSubject = new PatientConsentHelper() {
                @Override
                protected AdapterPIPImpl getAdapterPIP() {
                    return mockPIP;
                }
            };

            FineGrainedPolicyCriterionType criterionType = null;

            FineGrainedPolicyCriteriaType findGrainedPolicy = new FineGrainedPolicyCriteriaType();
            findGrainedPolicy.getFineGrainedPolicyCriterion().add(criterionType);

            PatientPreferencesType ptPreferences = new PatientPreferencesType();
            ptPreferences.setFineGrainedPolicyCriteria(findGrainedPolicy);

            // Default is false
            assertFalse("Document sharing allowed was not false",
                    testSubject.documentSharingAllowed("testing", ptPreferences));
            
        } catch (Throwable t) {
            System.out.println("Error running testDocumentSharingAllowedNullPolicyCriterion test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testDocumentSharingAllowedNullPolicyCriterion test: " + t.getMessage());
        }
    }

    @Test
    public void testDocumentSharingAllowedMissingPolicyCriterionDocType() {
        try {
            PatientConsentHelper testSubject = new PatientConsentHelper() {
                @Override
                protected AdapterPIPImpl getAdapterPIP() {
                    return mockPIP;
                }
            };

            FineGrainedPolicyCriterionType criterionType = new FineGrainedPolicyCriterionType();

            FineGrainedPolicyCriteriaType findGrainedPolicy = new FineGrainedPolicyCriteriaType();
            findGrainedPolicy.getFineGrainedPolicyCriterion().add(criterionType);

            PatientPreferencesType ptPreferences = new PatientPreferencesType();
            ptPreferences.setFineGrainedPolicyCriteria(findGrainedPolicy);

            // Default is false - this is the default criterion which when not set is false
            assertFalse("Default criterion value was not false",
                    testSubject.documentSharingAllowed("testing", ptPreferences));
            
        } catch (Throwable t) {
            System.out.println("Error running testDocumentSharingAllowedMissingPolicyCriterionDocType test: "
                    + t.getMessage());
            t.printStackTrace();
            fail("Error running testDocumentSharingAllowedMissingPolicyCriterionDocType test: " + t.getMessage());
        }
    }

    @Test
    public void testDocumentSharingAllowedDefaultCriterionTrue() {
        try {
            PatientConsentHelper testSubject = new PatientConsentHelper() {
                @Override
                protected AdapterPIPImpl getAdapterPIP() {
                    return mockPIP;
                }
            };

            FineGrainedPolicyCriterionType criterionType = new FineGrainedPolicyCriterionType();
            criterionType.setPermit(true);

            FineGrainedPolicyCriteriaType findGrainedPolicy = new FineGrainedPolicyCriteriaType();
            findGrainedPolicy.getFineGrainedPolicyCriterion().add(criterionType);

            PatientPreferencesType ptPreferences = new PatientPreferencesType();
            ptPreferences.setFineGrainedPolicyCriteria(findGrainedPolicy);

            assertTrue("Default criterion value was not true",
                    testSubject.documentSharingAllowed("testing", ptPreferences));
           
        } catch (Throwable t) {
            System.out.println("Error running testDocumentSharingAllowedDefaultCriterionTrue test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testDocumentSharingAllowedDefaultCriterionTrue test: " + t.getMessage());
        }
    }

    @Test
    public void testDocumentSharingAllowedNoMatch() {
        try {
            PatientConsentHelper testSubject = new PatientConsentHelper() {
                @Override
                protected AdapterPIPImpl getAdapterPIP() {
                    return mockPIP;
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

            // Default is false
            assertFalse("No match on criterion was not false",
                    testSubject.documentSharingAllowed("willnotexiest", ptPreferences));
            
        } catch (Throwable t) {
            System.out.println("Error running testDocumentSharingAllowedNoMatch test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testDocumentSharingAllowedNoMatch test: " + t.getMessage());
        }
    }

    @Test
    public void testDocumentSharingAllowedOptIn() {
        try {
            PatientConsentHelper testSubject = new PatientConsentHelper() {
                @Override
                protected AdapterPIPImpl getAdapterPIP() {
                    return mockPIP;
                }
            };

            PatientPreferencesType ptPreferences = new PatientPreferencesType();
            ptPreferences.setOptIn(true);

            assertTrue("Doc type check for global opt in", testSubject.documentSharingAllowed("testing", ptPreferences));
            
        } catch (Throwable t) {
            System.out.println("Error running testDocumentSharingAllowedOptIn test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testDocumentSharingAllowedOptIn test: " + t.getMessage());
        }
    }

    @Test
    public void testDocumentSharingAllowedOptOut() {
        try {
            PatientConsentHelper testSubject = new PatientConsentHelper() {
                @Override
                protected AdapterPIPImpl getAdapterPIP() {
                    return mockPIP;
                }
            };

            PatientPreferencesType ptPreferences = new PatientPreferencesType();
            ptPreferences.setOptIn(false);

            assertFalse("Doc type check for global opt out",
                    testSubject.documentSharingAllowed("testing", ptPreferences));
            
        } catch (Throwable t) {
            System.out.println("Error running testDocumentSharingAllowedOptOut test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testDocumentSharingAllowedOptOut test: " + t.getMessage());
        }
    }

    @Test
    public void testIsDefaultFineGrainedPolicyTrue() {
        try {
            PatientConsentHelper testSubject = new PatientConsentHelper();

            FineGrainedPolicyCriterionType criterion = new FineGrainedPolicyCriterionType();

            assertTrue("Default fine grained policy check not true",
                    testSubject.isDefaultFineGrainedPolicyCriterion(criterion));
            
        } catch (Throwable t) {
            System.out.println("Error running testIsDefaultFineGrainedPolicyTrue test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testIsDefaultFineGrainedPolicyTrue test: " + t.getMessage());
        }
    }

    @Test
    public void testIsDefaultFineGrainedPolicyFalse() {
        try {
            PatientConsentHelper testSubject = new PatientConsentHelper();

            FineGrainedPolicyCriterionType criterion = new FineGrainedPolicyCriterionType();
            CeType docTypeCE = new CeType();
            docTypeCE.setCode("Test Code");
            criterion.setDocumentTypeCode(docTypeCE);

            assertFalse("Default fine grained policy check not false",
                    testSubject.isDefaultFineGrainedPolicyCriterion(criterion));
            
        } catch (Throwable t) {
            System.out.println("Error running testIsDefaultFineGrainedPolicyFalse test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testIsDefaultFineGrainedPolicyFalse test: " + t.getMessage());
        }
    }

}
