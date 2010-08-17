package gov.hhs.fha.nhinc.redactionengine.adapter;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.PatientPreferencesType;
import gov.hhs.fha.nhinc.policyengine.adapter.pip.PatientConsentManager;
import ihe.iti.xds_b._2007.DocumentRegistryPortType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType.DocumentResponse;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ClassificationType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExternalIdentifierType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.IdentifiableType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.InternationalStringType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.LocalizedStringType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectListType;
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
public class DocRetrieveResponseProcessorTest {

    Mockery context = new JUnit4Mockery() {

        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };
    final Log mockLog = context.mock(Log.class);
    final PatientConsentHelper mockPatientConsentHelper = context.mock(PatientConsentHelper.class);
    final PatientConsentManager mockPatientConsentMgr = context.mock(PatientConsentManager.class);
    final DocumentRegistryPortType mockDocumentRegistryPort = context.mock(DocumentRegistryPortType.class);

    @Test
    public void filterAdhocQueryResultsHappy() {
        try
        {
            RetrieveDocumentSetRequestType retrieveRequest = new RetrieveDocumentSetRequestType();
            RetrieveDocumentSetResponseType retrieveResponse = new RetrieveDocumentSetResponseType();
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(any(String.class)));
                    allowing(mockPatientConsentHelper).retrievePatientConsentbyDocumentId(with(any(String.class)), with(any(String.class)), with(any(String.class)));
                    allowing(mockPatientConsentMgr).getDocumentRegistryPort();
                }
            });

            RetrieveDocumentSetResponseType response = testFilterRetrieveDocumentSetReults(retrieveRequest, retrieveResponse);
            assertNull("RetrieveDocumentSetResponseType was null", response);
            retrieveResponse = new RetrieveDocumentSetResponseType();
            retrieveResponse.getDocumentResponse().add(null);
            response = testFilterRetrieveDocumentSetReults(retrieveRequest, retrieveResponse);
            assertNotNull("RetrieveDocumentSetResponseType was null", response);
            retrieveResponse = new RetrieveDocumentSetResponseType();
            DocumentResponse aDocResponse = new DocumentResponse();
            createDocumentResponse(aDocResponse);
            retrieveResponse.getDocumentResponse().clear();
            retrieveResponse.getDocumentResponse().add(aDocResponse);
            response = testFilterRetrieveDocumentSetReults(retrieveRequest, retrieveResponse);
            assertNotNull("RetrieveDocumentSetResponseType was not null", response);
        }
        catch(Throwable t)
        {
            System.out.println("Error running filterAdhocQueryResultsHappy test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running filterAdhocQueryResultsHappy test: " + t.getMessage());
        }
    }

    @Test
    public void filterAdhocQueryResultsNullInputs() {
        RetrieveDocumentSetRequestType retrieveRequest = null;
        RetrieveDocumentSetResponseType retrieveResponse = null;
        context.checking(new Expectations()
        {
            {
                allowing(mockLog).debug(with(any(String.class)));
            }
        });
        RetrieveDocumentSetResponseType response = testFilterRetrieveDocumentSetReults(retrieveRequest, retrieveResponse);
        assertNull("RetrieveDocumentSetResponseType was not null", response);
    }

    @Test
    public void testFilterRetrieveDocumentSetReults(){
        RetrieveDocumentSetRequestType retrieveRequest = new RetrieveDocumentSetRequestType();
        RetrieveDocumentSetResponseType retrieveDocSetResponse = new RetrieveDocumentSetResponseType();
        DocumentResponse aDocResponse = new DocumentResponse();
        createDocumentResponse(aDocResponse);
        retrieveDocSetResponse.getDocumentResponse().add(aDocResponse);
        DocRetrieveResponseProcessor processor = new DocRetrieveResponseProcessor() {
            @Override
            protected Log createLogger()
            {
                return mockLog;
            }
            @Override
            protected PatientConsentManager getPatientConsentManager() {
                return mockPatientConsentMgr;
            }
            @Override
            protected PatientConsentHelper getPatientConsentHelper()
            {
                return mockPatientConsentHelper;
            }
            @Override
            protected boolean allowDocumentSharing(DocumentResponse retrieveResponse, PatientPreferencesType patientPreferences){
                return true;
            }
        };
        context.checking(new Expectations()
        {
            {
                allowing(mockLog).debug(with(any(String.class)));
                allowing(mockPatientConsentHelper).retrievePatientConsentbyDocumentId(with(any(String.class)), with(any(String.class)), with(any(String.class)));
            }
        });
        assertNotNull("All Values Set and filterResults() returns true", processor.filterRetrieveDocumentSetReults(retrieveRequest, retrieveDocSetResponse));
    }
    
    private RetrieveDocumentSetResponseType testFilterRetrieveDocumentSetReults(RetrieveDocumentSetRequestType retrieveRequest, RetrieveDocumentSetResponseType retrieveResponse) {
        RetrieveDocumentSetResponseType response = null;
        try {
            DocRetrieveResponseProcessor processor = new DocRetrieveResponseProcessor() {

                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected PatientConsentManager getPatientConsentManager() {
                    return mockPatientConsentMgr;
                }
                @Override
                protected PatientConsentHelper getPatientConsentHelper()
                {
                    return mockPatientConsentHelper;
                }
                @Override
                protected void extractIdentifiers(DocumentResponse docResponse)
                {

                }
                @Override
                protected boolean allowDocumentSharing(DocumentResponse retrieveResponse, PatientPreferencesType patientPreferences)
                {
                    return false;
                }
            };
            response = processor.filterRetrieveDocumentSetReults(retrieveRequest, retrieveResponse);
        } catch (Throwable t) {
            System.out.println("Error running testFilterRetrieveDocumentSetReults test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testFilterRetrieveDocumentSetReults test: " + t.getMessage());
        }
        return response;
    }

    @Test
    public void testGetDocumentId() {
        try {
            DocRetrieveResponseProcessor processor = new DocRetrieveResponseProcessor() {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
            };
            assertNull("Document identifier", processor.getDocumentId());
        } catch (Throwable t) {
            System.out.println("Error running testGetDocumentId test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetDocumentId test: " + t.getMessage());
        }

    }

    @Test
    public void testGetHomeCommunityId() {
        try {
            DocRetrieveResponseProcessor processor = new DocRetrieveResponseProcessor() {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
            };
            assertNull("Home community id", processor.getHomeCommunityId());
        } catch (Throwable t) {
            System.out.println("Error running testGetHomeCommunityId test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetHomeCommunityId test: " + t.getMessage());
        }

    }

    @Test
    public void testRepositoryId() {
        try {
            DocRetrieveResponseProcessor processor = new DocRetrieveResponseProcessor() {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
            };
            assertNull("Repository id", processor.getRepositoryId());
        } catch (Throwable t) {
            System.out.println("Error running testRepositoryId test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testRepositoryId test: " + t.getMessage());
        }

    }

    @Test
    public void testExtractIdentifiersHappy() {
        try {
            DocumentResponse retrieveResponse = new DocumentResponse();
            createDocumentResponse(retrieveResponse);
            DocRetrieveResponseProcessor processor = new DocRetrieveResponseProcessor() {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(any(String.class)));
                }
            });
            processor.extractIdentifiers(retrieveResponse);
            assertNotNull("Document identifier", processor.getDocumentId());
            assertNotNull("Home community id", processor.getHomeCommunityId());
            assertNotNull("Repository id", processor.getRepositoryId());
        } catch (Throwable t) {
            System.out.println("Error running testExtractIdentifiers test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testExtractIdentifiers test: " + t.getMessage());
        }
    }

    @Test
    public void testExtractIdentifiersFails() {
        try {
            DocumentResponse retrieveResponse = new DocumentResponse();
            DocRetrieveResponseProcessor processor = new DocRetrieveResponseProcessor() {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(any(String.class)));
                    allowing(mockLog).warn("Document Response is null");
                }
            });
            processor.extractIdentifiers(null);
            assertNull("Document identifier", processor.getDocumentId());
            assertNull("Home community id", processor.getHomeCommunityId());
            assertNull("Repository id", processor.getRepositoryId());
            processor.extractIdentifiers(retrieveResponse);
            assertNull("Document identifier", processor.getDocumentId());
            assertNull("Home community id", processor.getHomeCommunityId());
            assertNull("Repository id", processor.getRepositoryId());
        } catch (Throwable t) {
            System.out.println("Error running testExtractIdentifiersFails test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testExtractIdentifiersFails test: " + t.getMessage());
        }
    }

    @Test
    public void testFilterResultsNullInputs() {
        DocumentResponse docResponse = null;
        PatientPreferencesType patientPreferences = null;
        context.checking(new Expectations()
        {
            {
                allowing(mockLog).debug(with(any(String.class)));
                allowing(mockLog).error("Unable to filter results retrieveResponse was null");
                allowing(mockLog).error("Unable to filter results Patient Preferences was null");
            }
        });
        RetrieveDocumentSetResponseType response = testFilterResults(docResponse, patientPreferences);
        assertNull("RetrieveDocumentSetResponseType was null", response);
        docResponse = new DocumentResponse();
        response = testFilterResults(docResponse, null);
        assertNull("RetrieveDocumentSetResponseType was null", response);
        PatientPreferencesType patientPreferences1 = new PatientPreferencesType();
        patientPreferences1.setPatientId(null);
        patientPreferences1.setAssigningAuthority(null);
        response = testFilterResults(docResponse, patientPreferences1);
        assertNull("RetrieveDocumentSetResponseType was null", response);
        patientPreferences1 = new PatientPreferencesType();
        patientPreferences1.setPatientId("D123401");
        patientPreferences1.setAssigningAuthority(null);
        response = testFilterResults(docResponse, patientPreferences1);
        assertNull("RetrieveDocumentSetResponseType was null", response);
    }

    @Test
    public void testFilterResultsHappy() {
        DocumentResponse docResponse = new DocumentResponse();
        PatientPreferencesType patientPreferences = new PatientPreferencesType();
        context.checking(new Expectations()
        {
            {
                allowing(mockLog).debug(with(any(String.class)));
            }
        });
        RetrieveDocumentSetResponseType response = testFilterResults(docResponse, patientPreferences);
        assertNull("RetrieveDocumentSetResponseType was null", response);
        DocumentResponse aDocResponse = new DocumentResponse();
        createDocumentResponse(aDocResponse);
        PatientPreferencesType patientPreferences1 = new PatientPreferencesType();
        patientPreferences1.setAssigningAuthority("1.1.1");
        patientPreferences1.setPatientId("D123401");
        response = testFilterResults(aDocResponse, patientPreferences);
        assertNull("RetrieveDocumentSetResponseType was not null", response);
    }

    @Test
    public void testFilterResultsMoreHappy() {
        RetrieveDocumentSetResponseType retrieveDocSetResponse = new RetrieveDocumentSetResponseType();
        PatientPreferencesType patientPreferences = new PatientPreferencesType();
        DocumentResponse aDocResponse = new DocumentResponse();
        createDocumentResponse(aDocResponse);
        retrieveDocSetResponse.getDocumentResponse().add(aDocResponse);
        patientPreferences.setAssigningAuthority("1.1.1");
        patientPreferences.setPatientId("D123401");
        DocRetrieveResponseProcessor processor = new DocRetrieveResponseProcessor() {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected PatientConsentManager getPatientConsentManager() {
                    return mockPatientConsentMgr;
                }

                @Override
                protected DocumentRegistryPortType getDocumentRegistryPort() {
                    return mockDocumentRegistryPort;
                }

                @Override
                protected PatientConsentHelper getPatientConsentHelper() {
                    return mockPatientConsentHelper;
                }

                @Override
                protected AdhocQueryRequest createAdhocQueryRequest(String sPatId, String sAA)
                {
                    return new AdhocQueryRequest();
                }

                @Override
                protected AdhocQueryResponse getAdhocQueryResponse(AdhocQueryRequest oRequest) throws Exception
                {
                    return new AdhocQueryResponse();
                }

                @Override
                protected String extractDocTypeFromDocQueryResults(AdhocQueryResponse oResponse, DocumentResponse docResponse)
                {
                    return "Summurization of Epizode";
                }

                @Override
                protected boolean patientPrefAllowsSharing(String sDocTypeResult, PatientPreferencesType patientPreferences)
                {
                    return true;
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(any(String.class)));
                }
            });
            assertTrue("All Values Set expecting a non null response",processor.allowDocumentSharing(aDocResponse, patientPreferences));
    }

    private RetrieveDocumentSetResponseType testFilterResults(DocumentResponse docResponse, PatientPreferencesType patientPreferences) {
        RetrieveDocumentSetResponseType response = null;
        try {
            DocRetrieveResponseProcessor processor = new DocRetrieveResponseProcessor() {

                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected PatientConsentManager getPatientConsentManager() {
                    return mockPatientConsentMgr;
                }

                @Override
                protected DocumentRegistryPortType getDocumentRegistryPort() {
                    return mockDocumentRegistryPort;
                }

                @Override
                protected PatientConsentHelper getPatientConsentHelper() {
                    return mockPatientConsentHelper;
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(any(String.class)));
                }
            });
            if (processor.allowDocumentSharing(docResponse, patientPreferences)) {
                response = new RetrieveDocumentSetResponseType();
            } else {
                response = null;
            }
        } catch (Throwable t) {
            System.out.println("Error running testFilterResults test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testFilterResults test: " + t.getMessage());
        }
        return response;
    }

    @Test
    public void testGetUniqueIdIdentifier() {
        DocRetrieveResponseProcessor processor = new DocRetrieveResponseProcessor() {
            @Override
            protected Log createLogger()
            {
                return mockLog;
            }
        };
        context.checking(new Expectations()
        {
            {
                allowing(mockLog).debug(with(any(String.class)));
            }
        });
        assertNull("Unique Id returned for null EnternalIdentifier is also null", processor.getUniqueIdIdentifier(null));
        List<ExternalIdentifierType> externalIdentifierList = new ArrayList<ExternalIdentifierType>();
        assertNull("Unique Id returned for null EnternalIdentifierList is also null", processor.getUniqueIdIdentifier(externalIdentifierList));
        externalIdentifierList.add(null);
        assertNull("Unique Id returned for null EnternalIdentifierList is also null", processor.getUniqueIdIdentifier(externalIdentifierList));
        ExternalIdentifierType ext1 = new ExternalIdentifierType();
        externalIdentifierList.add(ext1);
        assertNull("Unique Id returned for null EnternalIdentifier is also null", processor.getUniqueIdIdentifier(externalIdentifierList));
        ExternalIdentifierType ext2 = new ExternalIdentifierType();
        ext2.setIdentificationScheme(null);
        externalIdentifierList.clear();
        externalIdentifierList.add(ext2);
        assertNull("Unique Id returned for null EnternalIdentifier is also null", processor.getUniqueIdIdentifier(externalIdentifierList));
        externalIdentifierList.clear();
        ext1.setIdentificationScheme("urn:uuid:2e82c1f6-a085-4c72-9da3-8640a32e42ab");
        ext1.setValue("1234");
        externalIdentifierList.add(ext1);
        assertEquals(processor.getUniqueIdIdentifier(externalIdentifierList), "1234");
        externalIdentifierList.clear();
        ext1 = new ExternalIdentifierType();
        ext1.setIdentificationScheme("urn:uuid:2e82c1f6-a085-4c72-9da3-8640a32e42ab");
        ext1.setValue("12345");
        externalIdentifierList.add(ext1);
        assertNotNull("External Identifier Not matching returns null value", processor.getUniqueIdIdentifier(externalIdentifierList));
        assertEquals(processor.getUniqueIdIdentifier(externalIdentifierList), "12345");
    }

    @Test
    public void testParseInternationalType() {
        DocRetrieveResponseProcessor processor = new DocRetrieveResponseProcessor() {
            @Override
            protected Log createLogger()
            {
                return mockLog;
            }
        };
        context.checking(new Expectations()
        {
            {
                allowing(mockLog).debug(with(any(String.class)));
            }
        });
        assertNull("Passing null Internal String type returns a null String", processor.parseInternationalType(null));
        InternationalStringType sIntStrType = new InternationalStringType();
        assertNull("Passing null Internal String type returns a null String", processor.parseInternationalType(sIntStrType));
        sIntStrType.getLocalizedString().add(null);
        assertNull("Passing null Internal String type returns a null String", processor.parseInternationalType(sIntStrType));
        LocalizedStringType l = new LocalizedStringType();
        sIntStrType.getLocalizedString().clear();
        sIntStrType.getLocalizedString().add(l);
        assertNull("Passing null Internal String type returns a null String", processor.parseInternationalType(sIntStrType));
        l.setValue("Summerization Of Epizode");
        assertNotNull("Passing Internal String type returns a String", processor.parseInternationalType(sIntStrType));
        assertEquals("Summerization Of Epizode", processor.parseInternationalType(sIntStrType));
    }

    @Test
    public void testExtractDocTypeFromMetaData() {
        DocRetrieveResponseProcessor processor = new DocRetrieveResponseProcessor() {
            @Override
            protected Log createLogger()
            {
                return mockLog;
            }
        };
        context.checking(new Expectations()
        {
            {
                allowing(mockLog).debug(with(any(String.class)));
            }
        });
        assertNull("Extrensic Object null returns a null docType", processor.extractDocTypeFromMetaData(null));
        ExtrinsicObjectType documentMetaData = new ExtrinsicObjectType();
        assertNull("Extrensic Object Classification Object is null returns a null docType", processor.extractDocTypeFromMetaData(documentMetaData));
        ClassificationType oClassification1 = null;
        documentMetaData.getClassification().add(oClassification1);
        assertNull("Extrensic Object Classification Object is null returns a null docType", processor.extractDocTypeFromMetaData(documentMetaData));
        documentMetaData.getClassification().clear();
        documentMetaData.getClassification().add(null);
        assertNull("Extrensic Object null returns a null docType", processor.extractDocTypeFromMetaData(documentMetaData));
        ClassificationType oClassification = new ClassificationType();
        oClassification.setClassificationScheme("");
        oClassification.setName(null);
        documentMetaData.getClassification().add(oClassification);
        assertNull("Extrensic Object null returns a null docType", processor.extractDocTypeFromMetaData(documentMetaData));
        documentMetaData.getClassification().clear();
        oClassification = new ClassificationType();
        documentMetaData.getClassification().add(oClassification);
        oClassification.setClassificationScheme("urn:uuid:f0306f51-975f-434e-a61c-c59651d33983");
        oClassification.setNodeRepresentation("Summerization Of Epizode");
        assertNotNull("Extrensic Object not null returns a docType", processor.extractDocTypeFromMetaData(documentMetaData));
        assertEquals("Summerization Of Epizode", processor.extractDocTypeFromMetaData(documentMetaData));
        documentMetaData.getClassification().clear();
        oClassification = new ClassificationType();
        documentMetaData.getClassification().add(oClassification);
        oClassification.setClassificationScheme("urn:uuid:41a5887f-8865-4c09-adf7-e362475b143");
        oClassification.setNodeRepresentation("Summerization Of Epizode");
        assertNull("Extrensic Object is null returns a null docType", processor.extractDocTypeFromMetaData(documentMetaData));
    }

    @Test
    public void testExtractDocTypeFromDocQueryResults() {
        DocRetrieveResponseProcessor processor = new DocRetrieveResponseProcessor() {
            @Override
            protected Log createLogger()
            {
                return mockLog;
            }
        };
        context.checking(new Expectations()
        {
            {
                allowing(mockLog).debug(with(any(String.class)));
            }
        });
        assertNull("All Inputs set to Null", processor.extractDocTypeFromDocQueryResults(null, null));
        AdhocQueryResponse adhocQueryRes = new AdhocQueryResponse();
        assertNull("All Inputs set to Null", processor.extractDocTypeFromDocQueryResults(adhocQueryRes, null));
        DocumentResponse docResponse = new DocumentResponse();
        assertNull("All Inputs set to Null", processor.extractDocTypeFromDocQueryResults(adhocQueryRes, docResponse));
        createDocumentResponse(docResponse);
        RegistryObjectListType oRegistryObjectList = new RegistryObjectListType();
        List<JAXBElement<? extends IdentifiableType>> oRegistryObjList = oRegistryObjectList.getIdentifiable();
        ExtrinsicObjectType documentMetaData = new ExtrinsicObjectType();
        documentMetaData.setId("1.123401.22222");
        documentMetaData.setHome("1.1");
        oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory oRimObjectFactory = new oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory();
        ClassificationType oClassification = new ClassificationType();
        documentMetaData.getClassification().add(oClassification);
        oClassification.setClassificationScheme("urn:uuid:f0306f51-975f-434e-a61c-c59651d33983");
        oClassification.setNodeRepresentation("Summerization Of Epizode");
        ExternalIdentifierType idType = oRimObjectFactory.createExternalIdentifierType();
        idType.setIdentificationScheme("urn:uuid:2e82c1f6-a085-4c72-9da3-8640a32e42ab");
        idType.setValue("1.123401.22222");
        documentMetaData.getExternalIdentifier().add(idType);
        JAXBElement<? extends IdentifiableType> oJAXBExtId = oRimObjectFactory.createExtrinsicObject(documentMetaData);
        oRegistryObjList.add(oJAXBExtId);
        adhocQueryRes.setRegistryObjectList(oRegistryObjectList);
        assertNotNull("All Inputs set", processor.extractDocTypeFromDocQueryResults(adhocQueryRes, docResponse));
        assertEquals("Summerization Of Epizode", processor.extractDocTypeFromDocQueryResults(adhocQueryRes, docResponse));
    }

    @Test
    public void testCreateAdhocQueryRequest() {
        DocRetrieveResponseProcessor processor = new DocRetrieveResponseProcessor() {
            @Override
            protected Log createLogger()
            {
                return mockLog;
            }
        };
        context.checking(new Expectations()
        {
            {
                allowing(mockLog).debug(with(any(String.class)));
            }
        });
        assertNull("Set null input returns null value", processor.createAdhocQueryRequest(null, null));
        String sPtId = "D123401";
        String sAA = null;
        assertNull("Set Patient Id and Assigning Authority as null inputs return null value", processor.createAdhocQueryRequest(sPtId, sAA));
        sPtId = null;
        sAA = "1.1.1";
        assertNull("Set Patient Id null and Assigning Authority with value return null value", processor.createAdhocQueryRequest(sPtId, sAA));
        sPtId = "";
        sAA = "";
        assertNull("Set both Patient Id and Assigning Authority with empty string values return null value", processor.createAdhocQueryRequest(sPtId, sAA));
        sPtId = "D123401";
        sAA = "1.1.1";
        assertNotNull("Set both Patient Id and Assigning Authority values return value", processor.createAdhocQueryRequest(sPtId, sAA));
    }

    private void createDocumentResponse(DocumentResponse aDocResponse) {
        aDocResponse.setDocumentUniqueId("1.123401.22222");
        aDocResponse.setHomeCommunityId("1.1");
        aDocResponse.setRepositoryUniqueId("1");
    }

    @Test
    public void testCreateLogger()
    {
        try
        {
            DocRetrieveResponseProcessor processor = new DocRetrieveResponseProcessor()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
            };
            Log log = processor.createLogger();
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
    public void testGetPatientConsentHelper()
    {
        try
        {
            DocRetrieveResponseProcessor processor = new DocRetrieveResponseProcessor()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected PatientConsentHelper getPatientConsentHelper()
                {
                    return mockPatientConsentHelper;
                }
            };
            PatientConsentHelper oPatientConsentHelper = processor.getPatientConsentHelper();
            assertNotNull("PatientConsentHelper was null", oPatientConsentHelper);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testCreateLogger test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testCreateLogger test: " + t.getMessage());
        }
    }

    @Test
    public void testGetPatientConsentManager()
    {
        try
        {
            DocRetrieveResponseProcessor processor = new DocRetrieveResponseProcessor()
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
            };
            PatientConsentManager oPatientConsentManager = processor.getPatientConsentManager();
            assertNotNull("PatientConsentManager was null", oPatientConsentManager);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testCreateLogger test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testCreateLogger test: " + t.getMessage());
        }
    }

    @Test
    public void testGetDocumentRegistryPort()
    {
        try
        {
            DocRetrieveResponseProcessor processor = new DocRetrieveResponseProcessor()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected DocumentRegistryPortType getDocumentRegistryPort()
                {
                    return mockDocumentRegistryPort;
                }
            };
            DocumentRegistryPortType oDocumentRegistryPortType = processor.getDocumentRegistryPort();
            assertNotNull("DocumentRegistryPortType was null", oDocumentRegistryPortType);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testCreateLogger test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testCreateLogger test: " + t.getMessage());
        }
    }
    
}
