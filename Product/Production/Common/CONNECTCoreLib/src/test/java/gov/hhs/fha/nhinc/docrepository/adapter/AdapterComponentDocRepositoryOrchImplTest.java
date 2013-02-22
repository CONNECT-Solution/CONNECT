/**
 * 
 */
package gov.hhs.fha.nhinc.docrepository.adapter;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import gov.hhs.fha.nhinc.docrepository.adapter.model.Document;
import gov.hhs.fha.nhinc.docrepository.adapter.model.DocumentQueryParams;
import gov.hhs.fha.nhinc.docrepository.adapter.model.EventCode;
import gov.hhs.fha.nhinc.docrepository.adapter.service.DocumentService;
import gov.hhs.fha.nhinc.largefile.LargeFileUtils;
import gov.hhs.fha.nhinc.util.format.UTCDateUtil;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType.DocumentRequest;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType.DocumentResponse;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.activation.DataHandler;
import javax.xml.bind.JAXBElement;

import oasis.names.tc.ebxml_regrep.xsd.lcm._3.SubmitObjectsRequest;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.AssociationType1;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ClassificationType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExternalIdentifierType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.IdentifiableType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.InternationalStringType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.LocalizedStringType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectListType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ValueListType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

import org.junit.Test;

/**
 * @author achidamb
 *
 */
public class AdapterComponentDocRepositoryOrchImplTest {
    
    
    
	@Test
    public void getDocumentServiceTest() {
    	AdapterComponentDocRepositoryOrchImpl docRepo = new AdapterComponentDocRepositoryOrchImpl();
    	assertTrue(docRepo.getDocumentService() instanceof DocumentService);
    }
	
	@Test
    public void getLargeFilesUtilTest() {
    	AdapterComponentDocRepositoryOrchImpl docRepo = new AdapterComponentDocRepositoryOrchImpl();
    	assertTrue(docRepo.getLargeFileUtils() instanceof LargeFileUtils);
    }
	
	@Test
	public void getDateUtilTest() {
    	AdapterComponentDocRepositoryOrchImpl docRepo = new AdapterComponentDocRepositoryOrchImpl();
    	assertTrue(docRepo.getDateUtil() instanceof UTCDateUtil);
    }
    
    @Test
    public void setDocumentResponse_Failure() {
    	AdapterComponentDocRepositoryOrchImpl docRepo = new AdapterComponentDocRepositoryOrchImpl();
    	Document doc = mock(Document.class);
    	
    	when(doc.getRawData()).thenReturn(null);
    	
    	assertFalse(docRepo.setDocumentResponse(doc, null));
    }
    
    @Test
    public void setDocumentResponse_Success_File() throws IOException {
    	Boolean result;
    	String url = "file:///FILE_NAME";
    	final LargeFileUtils largeFileUtils = mock(LargeFileUtils.class);
    	AdapterComponentDocRepositoryOrchImpl docRepo = new AdapterComponentDocRepositoryOrchImpl(){
    		@Override
    		public LargeFileUtils getLargeFileUtils(){
    			return largeFileUtils;
    		}	
    	};
    	
    	Document doc = mock(Document.class);
    	DocumentResponse oDocResponse = mock(DocumentResponse.class);
    	DataHandler dataHandler = mock(DataHandler.class);
    	
    	when(doc.getRawData()).thenReturn(url.getBytes());
    	when(largeFileUtils.convertToDataHandler(any(File.class))).thenReturn(dataHandler);
    	
    	result = docRepo.setDocumentResponse(doc, oDocResponse);
    	
    	verify(oDocResponse).setDocument(dataHandler);
    	
    	assertTrue(result);    	
    }
    
    @Test
    public void setDocumentResponse_Success_String() throws IOException {
    	Boolean result;
    	String url = "file:///FILE_NAME^";
    	final LargeFileUtils largeFileUtils = mock(LargeFileUtils.class);
    	
    	AdapterComponentDocRepositoryOrchImpl docRepo = new AdapterComponentDocRepositoryOrchImpl(){
    		@Override
			public LargeFileUtils getLargeFileUtils(){
				return largeFileUtils;
			}
		};
    	
    	Document doc = mock(Document.class);
    	DocumentResponse oDocResponse = mock(DocumentResponse.class);
    	DataHandler dataHandler = mock(DataHandler.class);
    	
    	when(doc.getRawData()).thenReturn(url.getBytes());
    	when(largeFileUtils.convertToDataHandler(any(byte[].class))).thenReturn(dataHandler);
    	
    	result = docRepo.setDocumentResponse(doc, oDocResponse);
    	
    	verify(oDocResponse).setDocument(dataHandler);
    	
    	assertTrue(result);    	
    }
    
	@Test
    public void documentRepositoryRetrieveDocumentSetTest_Success()
    {
        AdapterComponentDocRepositoryOrchImpl docrepo= new AdapterComponentDocRepositoryOrchImpl(){
            @Override
            protected void retrieveDocuments(boolean repositoryIdMatched, List<String>documentUniqueIds,ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType response,
                    String homeCommunityId, RegistryErrorList regerrList){
            }
           
        };
        ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType response = new ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType();
        response = docrepo.documentRepositoryRetrieveDocumentSet(createDocumentRequest_Success());
        assertSame(response.getRegistryResponse().getStatus(), "urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success");
      
    }
    
    @Test
    public void documentRepositoryRetrieveDocumentSetTest_Failure()
    {
        AdapterComponentDocRepositoryOrchImpl docrepo= new AdapterComponentDocRepositoryOrchImpl(){
            @Override
            protected void retrieveDocuments(boolean repositoryIdMatched, List<String>documentUniqueIds,ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType response,
                    String homeCommunityId, RegistryErrorList regerrList){
            }
           
        };
        ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType response = new ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType();
        response = docrepo.documentRepositoryRetrieveDocumentSet(createDocumentRequest_Failure());
        assertSame(response.getRegistryResponse().getStatus(), "urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure");
      
    }
    
    @Test
    public void documentRepositoryRetrieveDocumentSetTest_PartialSuccess()
    {
        AdapterComponentDocRepositoryOrchImpl docrepo= new AdapterComponentDocRepositoryOrchImpl(){
            @Override
            protected void retrieveDocuments(boolean repositoryIdMatched, List<String>documentUniqueIds,ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType response,
                    String homeCommunityId, RegistryErrorList regerrList){
                Document doc1 = new Document();
                doc1.setDocumentUniqueId("2.531.777");
                Document doc2 = new Document();
                doc2.setDocumentUniqueId("");
                ArrayList<Document> docs = new ArrayList<Document>();
                docs.add(doc1);
                docs.add(doc2);
                loadDocumentResponses(response, docs, homeCommunityId,documentUniqueIds,regerrList);
            }
            @Override
            protected boolean setDocumentResponse( Document doc, DocumentResponse oDocResponse){
                return true;
            }
           
        };
        ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType response = new ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType();
        response = docrepo.documentRepositoryRetrieveDocumentSet(createDocumentRequest_PartialSuccess());
        assertSame(response.getRegistryResponse().getStatus(), "urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:PartialSuccess");
      
    }
    
    @Test
    public void retrieveDocumentsTest_RepositoryIdMatched(){
    	final DocumentService docService = mock(DocumentService.class);
    	
    	AdapterComponentDocRepositoryOrchImpl docRepo = new AdapterComponentDocRepositoryOrchImpl(){
    		@Override
    		protected void loadDocumentResponses(ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType response,
    	            List<Document> docs, String homeCommunityId, List<String> documentUniqueId, RegistryErrorList regerrList) {
    			assertTrue(true);
    		}
    		
    		@Override
    		public DocumentService getDocumentService(){		
    			return docService;
    		}
    		
    	};
    	
    	boolean repositoryIdMatched = true;
    	docRepo.retrieveDocuments(repositoryIdMatched, null, null, null, null);
    	verify(docService).documentQuery(any(DocumentQueryParams.class));
    }
    
    @Test
    public void documentRepositoryProvideAndRegisterDocumentSet_Success(){
    	ProvideAndRegisterDocumentSetRequestType body = mock(ProvideAndRegisterDocumentSetRequestType.class);
    	final HashMap<String, DataHandler> docMap = mock(HashMap.class);
    	SubmitObjectsRequest submitObjectsRequest = mock(SubmitObjectsRequest.class);
    	RegistryObjectListType regObjectList = mock(RegistryObjectListType.class);
    	List<JAXBElement<? extends IdentifiableType>> identifiableObjectList =
    			mock(List.class);
    	JAXBElement<? extends IdentifiableType> identifiableTypeElement = mock(JAXBElement.class);
    	ExtrinsicObjectType extrinsicObject = mock(ExtrinsicObjectType.class);
    	
    	AdapterComponentDocRepositoryOrchImpl docRepo = new AdapterComponentDocRepositoryOrchImpl(){
    		@Override
    		public AdapterComponentDocRepositoryHelper getHelper(){
    			return new AdapterComponentDocRepositoryHelper(){
    				@Override
    				HashMap<String, DataHandler> getDocumentMap(
    						ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType body){
    					return docMap;
    				}
    			};
    		}
    		
    		@Override
    		protected Document setDocument(List<JAXBElement<? extends oasis.names.tc.ebxml_regrep.xsd.rim._3.IdentifiableType>> identifiableObjectList,
    	    		RegistryErrorList errorList, int i, HashMap<String, DataHandler> docMap, boolean requestHasReplacementAssociation){
    			return null;
    		}
    		
    		@Override
    		protected boolean checkForReplacementAssociation(
    	            List<JAXBElement<? extends oasis.names.tc.ebxml_regrep.xsd.rim._3.IdentifiableType>> identifiableObjectList,
    	            RegistryErrorList errorList) {
    			return true;
    		}
    	};
    	
    	when(body.getSubmitObjectsRequest()).thenReturn(submitObjectsRequest);
    	when(submitObjectsRequest.getRegistryObjectList()).thenReturn(regObjectList);
    	when(regObjectList.getIdentifiable()).thenReturn(identifiableObjectList);
    	
    	when(identifiableObjectList.size()).thenReturn(1);
    	
    	RegistryResponseType registryResponse = docRepo.documentRepositoryProvideAndRegisterDocumentSet(body);
    	
    	assertEquals(registryResponse.getStatus(), DocRepoConstants.XDS_RETRIEVE_RESPONSE_STATUS_SUCCESS);
    }
    
    @Test
    public void documentRepositoryProvideAndRegisterDocumentSet_Failure(){
    	ProvideAndRegisterDocumentSetRequestType body = null;
    
    	AdapterComponentDocRepositoryOrchImpl docRepo = new AdapterComponentDocRepositoryOrchImpl();
    	RegistryResponseType registryResponse = docRepo.documentRepositoryProvideAndRegisterDocumentSet(body);
    	
    	RegistryErrorList errorList = registryResponse.getRegistryErrorList();
    	RegistryError error = errorList.getRegistryError().get(0);
    	
    	assertEquals(registryResponse.getStatus(), DocRepoConstants.XDS_RETRIEVE_RESPONSE_STATUS_FAILURE);
    	assertEquals(error.getValue(), DocRepoConstants.XDS_MISSING_REQUEST_MESSAGE_DATA + 
    			" ProvideAndRegisterDocumentSetRequestType element is null.");	
    }
    
    @Test
    public void checkForReplacementAssociationTest_Success() {
    	RegistryErrorList errorList = new RegistryErrorList();
    	List<JAXBElement<? extends oasis.names.tc.ebxml_regrep.xsd.rim._3.IdentifiableType>> identifiableObjectList = 
    			mock(ArrayList.class);
    	final AssociationType1 associationType1 = new AssociationType1();
    	associationType1.setAssociationType("urn:oasis:names:tc:ebxml-regrep:AssociationType:RPLC");
    	AdapterComponentDocRepositoryOrchImpl docRepo = new AdapterComponentDocRepositoryOrchImpl(){
    		@Override
    		protected Object getIdentifiableObjectValue(List<JAXBElement<? extends oasis.names.tc.ebxml_regrep.xsd.rim._3.IdentifiableType>> identifiableObjectList,
    	    		int i){
    			return associationType1;
    		}
    	};
    	
    	when(identifiableObjectList.size()).thenReturn(1);
    	boolean result = docRepo.checkForReplacementAssociation(identifiableObjectList, errorList);
    	assertTrue(result);
    }
    
    @Test
    public void checkForReplacementAssociationTest_Failure() {
    	RegistryErrorList errorList = new RegistryErrorList();
    	List<JAXBElement<? extends oasis.names.tc.ebxml_regrep.xsd.rim._3.IdentifiableType>> identifiableObjectList = 
    			mock(ArrayList.class);
    	final AssociationType1 associationType1 = new AssociationType1();
    	
    	AdapterComponentDocRepositoryOrchImpl docRepo = new AdapterComponentDocRepositoryOrchImpl(){
    		@Override
    		protected Object getIdentifiableObjectValue(List<JAXBElement<? extends oasis.names.tc.ebxml_regrep.xsd.rim._3.IdentifiableType>> identifiableObjectList,
    	    		int i){
    			return associationType1;
    		}
    	};
    	
    	when(identifiableObjectList.size()).thenReturn(1);
    	boolean result = docRepo.checkForReplacementAssociation(identifiableObjectList, errorList);
    	assertFalse(result);
    	assertEquals(errorList.getRegistryError().get(0).getValue(),
    			DocRepoConstants.XDS_MISSING_DOCUMENT_METADATA + " associationType element is null.");
    }
    
    @Test
    public void setDocumentTest_SuccessALL() throws IOException{
    	List<JAXBElement<? extends IdentifiableType>> identifiableObjectList = mock(ArrayList.class);
    	JAXBElement<? extends IdentifiableType> jaxBElement = mock(JAXBElement.class);
    	final ExtrinsicObjectType extrinsicObject = mock(ExtrinsicObjectType.class);
    	List<ExternalIdentifierType> externalIdentifierList = new ArrayList<ExternalIdentifierType>();
    	ExternalIdentifierType externalIdentifier = mock(ExternalIdentifierType.class);
    	externalIdentifierList.add(externalIdentifier);
    	InternationalStringType name = mock(InternationalStringType.class);
    	List<LocalizedStringType> nameValueList = mock(ArrayList.class);
    	LocalizedStringType nameValue = mock(LocalizedStringType.class);
    	List<SlotType1> documentSlots = new ArrayList<SlotType1>();
    	SlotType1 slot = mock(SlotType1.class);
    	documentSlots.add(slot);
    	ValueListType valueListType = mock(ValueListType.class);
    	List<String> valueList = mock(ArrayList.class);
    	RegistryErrorList errorList = mock(RegistryErrorList.class);
    	final UTCDateUtil utcDateUtil = mock(UTCDateUtil.class);
    	final String DOC_UNIQUE_ID = "Doc_ID_1";
    	final String PATIENT_ID = "'Patient_ID_1'";
    	final String PATIENT_ID_NO_QUOTES = "Patient_ID_1";
    	final String DOCUMENT_TITLE = "Document_Title";
    	final String DOCUMENT_DESCRIPTION = "Document Description";
    	final String MIME_TYPE = "Mime Type";
    	final String XDS_INTENDED_RECIPIENT = "Organization|Person";
    	final String XDS_INTENDED_ORG = "Organization";
    	final String XDS_INTENDED_PERSON = "Person";
    	final String XDS_LANGUAGE_CODE = "Language Code";
    	final String XDS_LEGAL_AUTHENTICATOR = "Legal Authenticator";
    	final String CREATION_TIME = "Creation Time";
    	Date mockTimeDate = mock(Date.class);
    	final String START_TIME = "Start Time";
    	final String STOP_TIME = "Stop Time";
    	final String XDS_PATIENT_ID = "'Patient_ID_2'";
    	final String XDS_PATIENT_ID_NO_QUOTES = "Patient_ID_2";
    	HashMap<String,DataHandler> docMap = mock(HashMap.class);
    	DataHandler dataHandler = mock(DataHandler.class);
    	final LargeFileUtils largeFileUtils = mock(LargeFileUtils.class);
    	final String RAW_DATA = "Raw Data";
    	final String STATUS = "Status";
    	
    	
    	AdapterComponentDocRepositoryOrchImpl docRepo = new AdapterComponentDocRepositoryOrchImpl(){
    		@Override
    		protected Object getExtrinsicObjectValue(List<JAXBElement<? extends oasis.names.tc.ebxml_regrep.xsd.rim._3.IdentifiableType>> identifiableObjectList,
    	    		int i){
    			return extrinsicObject;
    		}
    		
    		
    		@Override
    		protected void setDocumentPidObjects(Document doc,
    	    		List<oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1> documentSlots){
    			//Do nothing
    		}
    		
    		@Override
    		protected void setDocumentObjectsFromClassifications(Document doc, 
    	    		List<oasis.names.tc.ebxml_regrep.xsd.rim._3.ClassificationType> classifications){
    			//Do nothing
    		}
    		
    		@Override
    		protected void extractEventCodes(List<oasis.names.tc.ebxml_regrep.xsd.rim._3.ClassificationType> classifications,
    	            gov.hhs.fha.nhinc.docrepository.adapter.model.Document doc) {
    			//Do nothing
    		}
    		
    		@Override
    		protected void logDeclaredType(List<JAXBElement<? extends oasis.names.tc.ebxml_regrep.xsd.rim._3.IdentifiableType>> identifiableObjectList,
    	    		int i){
    			//Do nothing
    		}
    		
    		@Override
			public LargeFileUtils getLargeFileUtils() {
				return largeFileUtils;
			}

    		@Override
			protected void saveDocument(Document doc,
					boolean requestHasReplacementAssociation,
					String documentUniqueId, RegistryErrorList errorList) {
				// Do nothing
			}
    		
    		@Override
    		public UTCDateUtil getDateUtil() {
				return utcDateUtil;
			}		

			
    	};
    	   	
    	when(extrinsicObject.getExternalIdentifier()).thenReturn(externalIdentifierList);
    	when(externalIdentifier.getName()).thenReturn(name);
    	when(name.getLocalizedString()).thenReturn(nameValueList);
    	when(nameValueList.get(0)).thenReturn(nameValue);
    	when(nameValue.getValue()).thenReturn(DocRepoConstants.XDS_DOCUMENT_UNIQUE_ID, 
    			DocRepoConstants.XDS_PATIENT_ID, DOCUMENT_TITLE,
    			DOCUMENT_DESCRIPTION);
    	when(externalIdentifier.getValue()).thenReturn(DOC_UNIQUE_ID, PATIENT_ID);
    	
    	when(extrinsicObject.getName()).thenReturn(name);
    	
    	when(extrinsicObject.getDescription()).thenReturn(name);
    	
    	when(extrinsicObject.getMimeType()).thenReturn(MIME_TYPE);
    	
    	when(extrinsicObject.getSlot()).thenReturn(documentSlots);
    	when(slot.getName()).thenReturn(DocRepoConstants.XDS_INTENDED_RECIPIENT_SLOT,
    			DocRepoConstants.XDS_LANGUAGE_CODE_SLOT, DocRepoConstants.XDS_LEGAL_AUTHENTICATOR_SLOT,
    			DocRepoConstants.XDS_CREATION_TIME_SLOT, DocRepoConstants.XDS_START_TIME_SLOT,
    			DocRepoConstants.XDS_STOP_TIME_SLOT, DocRepoConstants.XDS_SOURCE_PATIENT_ID_SLOT);
    	
    	when(slot.getValueList()).thenReturn(valueListType);
    	when(valueListType.getValue()).thenReturn(valueList);
    	when(valueList.size()).thenReturn(1);
    	when(valueList.get(0)).thenReturn(XDS_INTENDED_RECIPIENT, XDS_LANGUAGE_CODE,
    			XDS_LEGAL_AUTHENTICATOR, CREATION_TIME, START_TIME, STOP_TIME,
    			XDS_PATIENT_ID);
    	
    	when(utcDateUtil.parseUTCDateOptionalTimeZone(CREATION_TIME)).thenReturn(mockTimeDate);
    	when(mockTimeDate.toString()).thenReturn(CREATION_TIME, START_TIME, STOP_TIME);
    	
    	when(utcDateUtil.parseUTCDateOptionalTimeZone(START_TIME)).thenReturn(mockTimeDate);
    	
    	when(utcDateUtil.parseUTCDateOptionalTimeZone(STOP_TIME)).thenReturn(mockTimeDate);
    	
    	when(extrinsicObject.getId()).thenReturn(DOC_UNIQUE_ID);
    	when(docMap.get(DOC_UNIQUE_ID)).thenReturn(dataHandler);
    	when(largeFileUtils.convertToBytes(dataHandler)).thenReturn(RAW_DATA.getBytes());
    	
    	when(extrinsicObject.getStatus()).thenReturn(STATUS);
    	
    	Document doc = docRepo.setDocument(identifiableObjectList, errorList, 0, docMap, true);
    	
    	assertEquals(doc.getPatientId(), PATIENT_ID_NO_QUOTES);
    	assertEquals(doc.getDocumentTitle(), DOCUMENT_TITLE);
    	assertEquals(doc.getComments(), DOCUMENT_DESCRIPTION);
    	assertEquals(doc.getMimeType(), MIME_TYPE);
    	assertEquals(doc.getIntendedRecipientOrganization(), XDS_INTENDED_ORG);
    	assertEquals(doc.getIntendedRecipientPerson(), XDS_INTENDED_PERSON);
    	assertEquals(doc.getLanguageCode(), XDS_LANGUAGE_CODE);
    	assertEquals(doc.getLegalAuthenticator(), XDS_LEGAL_AUTHENTICATOR);
    	assertEquals(doc.getCreationTime().toString(), CREATION_TIME);
    	assertEquals(doc.getServiceStartTime().toString(), START_TIME);
    	assertEquals(doc.getServiceStopTime().toString(), STOP_TIME);
    	assertEquals(doc.getSourcePatientId(), XDS_PATIENT_ID_NO_QUOTES);
    	assertTrue(Arrays.equals(doc.getRawData(), RAW_DATA.getBytes()));
    	assertEquals(doc.getAvailablityStatus(), STATUS);
    	assertEquals(doc.getStatus(), DocRepoConstants.XDS_STATUS);
    	assertEquals(doc.getDocumentUniqueId(), DOC_UNIQUE_ID);
    	assertEquals(doc.getSize(), (Integer) RAW_DATA.getBytes().length);
    }
    
    
    
    protected ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType createDocumentRequest_Success()
    {
        
        ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType requestBody = 
        		new ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType();
        DocumentRequest req1 = new DocumentRequest();
        req1.setDocumentUniqueId("1.546.678");
        req1.setHomeCommunityId("1.1");
        req1.setRepositoryUniqueId("1");
        requestBody.getDocumentRequest().add(req1);
        DocumentRequest req2 = new DocumentRequest();
        req2.setDocumentUniqueId("2.531.777");
        req2.setHomeCommunityId("1.1");
        req2.setRepositoryUniqueId("1");
        requestBody.getDocumentRequest().add(req2);
        return requestBody;
        
    }
    
    @Test
	public void saveDocumentTest_Success() {
		final DocumentService docService = mock(DocumentService.class);
		final String PATIENT_ID = "ID_1";
		final String DOC_UNIQUE_ID = "DOC_ID_1";
		final String CLASS_CODE = "CLASS_CODE";
		final String STATUS = "STATUS";
		final long DOC_ID = 101;
		Document doc = mock(Document.class);
		RegistryErrorList errorList = new RegistryErrorList();
		List<Document> docList = mock(List.class);
	
		AdapterComponentDocRepositoryOrchImpl docRepo = new AdapterComponentDocRepositoryOrchImpl() {
					@Override
					public DocumentService getDocumentService() {
						return docService;
					}
		};

		when(doc.getPatientId()).thenReturn(PATIENT_ID);
		when(doc.getDocumentUniqueId()).thenReturn(DOC_UNIQUE_ID);
		when(doc.getClassCode()).thenReturn(CLASS_CODE);
		when(doc.getStatus()).thenReturn(STATUS);
		when(docService.documentQuery(any(DocumentQueryParams.class)))
				.thenReturn(docList);
		when(doc.getDocumentid()).thenReturn(DOC_ID);

		docRepo.saveDocument(doc, true, DOC_UNIQUE_ID, errorList);

		verify(docService).saveDocument(doc);
		assertEquals(errorList.getRegistryError().size(), 0);

	}
	
	@Test
    public void saveDocumentTest_Failure(){
    	final DocumentService docService = mock(DocumentService.class);
    	final String PATIENT_ID = "ID_1";
    	final String DOC_UNIQUE_ID = "DOC_ID_1";
    	final String CLASS_CODE = "CLASS_CODE";
    	final String STATUS = "STATUS";
    	final long DOC_ID = 0;
    	Document doc = mock(Document.class);
    	RegistryErrorList errorList = new RegistryErrorList();
    	List<Document> docList = mock(List.class);
    	
    	AdapterComponentDocRepositoryOrchImpl docRepo = new AdapterComponentDocRepositoryOrchImpl(){
    				@Override
    				public DocumentService getDocumentService(){
    					return docService;
    				}
    	};
    	
    	when(doc.getPatientId()).thenReturn(PATIENT_ID);
    	when(doc.getDocumentUniqueId()).thenReturn(DOC_UNIQUE_ID);
    	when(doc.getClassCode()).thenReturn(CLASS_CODE);
    	when(doc.getStatus()).thenReturn(STATUS);
    	when(docService.documentQuery(any(DocumentQueryParams.class))).thenReturn(docList);
    	when(doc.getDocumentid()).thenReturn(DOC_ID);
    	
    	docRepo.saveDocument(doc, true, DOC_UNIQUE_ID, errorList);
    	
    	RegistryError error = errorList.getRegistryError().get(0);
    	
    	verify(docService).saveDocument(doc);
    	assertEquals(error.getValue(), DocRepoConstants.XDS_REPOSITORY_ERROR +
    			" DocumentUniqueId: " + DOC_UNIQUE_ID);
    	
    }
	
	@Test
	public void setDocumentPidObjectsTest(){
		final AdapterComponentDocRepositoryHelper docRepoHelper = mock(AdapterComponentDocRepositoryHelper.class);
		AdapterComponentDocRepositoryOrchImpl docRepo = new AdapterComponentDocRepositoryOrchImpl(){
			@Override
			protected AdapterComponentDocRepositoryHelper getHelper(){
		    	return docRepoHelper;
		    }
		};
		final List<SlotType1> documentSlots = mock(ArrayList.class);
		Document doc = new Document();
		
		final String PID3 = "PID3";
		final String PID5 = "PID5";
		final String PID7 = "PID7";
		final String PID8 = "PID8";
		final String PID11 = "PID11";
		
		when(docRepoHelper.extractPatientInfo(documentSlots, DocRepoConstants.XDS_SOURCE_PATIENT_INFO_PID3)).thenReturn(PID3);
		when(docRepoHelper.extractPatientInfo(documentSlots, DocRepoConstants.XDS_SOURCE_PATIENT_INFO_PID5)).thenReturn(PID5);
		when(docRepoHelper.extractPatientInfo(documentSlots, DocRepoConstants.XDS_SOURCE_PATIENT_INFO_PID7)).thenReturn(PID7);
		when(docRepoHelper.extractPatientInfo(documentSlots, DocRepoConstants.XDS_SOURCE_PATIENT_INFO_PID8)).thenReturn(PID8);
		when(docRepoHelper.extractPatientInfo(documentSlots, DocRepoConstants.XDS_SOURCE_PATIENT_INFO_PID11)).thenReturn(PID11);
		
		docRepo.setDocumentPidObjects(doc, documentSlots);
		
		assertEquals(doc.getPid3(), PID3);
		assertEquals(doc.getPid5(), PID5);
		assertEquals(doc.getPid7(), PID7);
		assertEquals(doc.getPid8(), PID8);
		assertEquals(doc.getPid11(), PID11);
	}
	
	@Test
	public void setDocumentObjectsFromClassificationsTest(){
		final AdapterComponentDocRepositoryHelper docRepoHelper = mock(AdapterComponentDocRepositoryHelper.class);
		AdapterComponentDocRepositoryOrchImpl docRepo = new AdapterComponentDocRepositoryOrchImpl(){
			@Override
			protected AdapterComponentDocRepositoryHelper getHelper(){
		    	return docRepoHelper;
		    }
		};
		Document doc = new Document();
		List<ClassificationType> classifications = mock(ArrayList.class);
		
		final String AUTHOR_PERSON = "author";
		final String AUTHOR_INSTITUTION = "author institution";
		final String AUTHOR_ROLE = "author role";
		final String AUTHOR_SPECIALTY = "author specialty";
		final String CLASS_CODE_SCHEME = "code scheme";
		final String CLASS_CODE = "class code";
		final String CONFIDENTIALITY_CODE_SCHEME = "conf code scheme";
		final String FORMAT_CODE_SCHEME = "format code scheme";
		final String HC_CODE_SCHEME = "facility code scheme";
		final String PRACTICE_SETTING_SCHEME= "practice setting scheme";
		final String TYPE_CODE_SCHEME = "type code scheme";
		final String CLASS_CODE_NAME = "class code name";
		final String CONFIDENTIALITY_CODE = "confidentiality code";
		final String CONFIDENTIALITY_CODE_NAME = "confidentiality code name";
		final String FORMAT_CODE = "format code";
		final String FORMAT_CODE_NAME = "formant code name";
		final String HC_CODE = "hc code";
		final String HC_CODE_NAME = "hc code name";
		final String PRACTICE_SETTING = "practice setting";
		final String PRACTICE_SETTING_NAME = "practice setting name";
		final String TYPE_CODE = "type code";
		final String TYPE_CODE_NAME = "type code name";
		
		when(docRepoHelper.extractClassificationMetadata(any(List.class), 
        		anyString(), anyString(), anyInt()))
        		.thenReturn(AUTHOR_PERSON, AUTHOR_INSTITUTION, AUTHOR_ROLE, AUTHOR_SPECIALTY, CLASS_CODE_SCHEME,
        				CONFIDENTIALITY_CODE_SCHEME, FORMAT_CODE_SCHEME, HC_CODE_SCHEME, PRACTICE_SETTING_SCHEME,
        				TYPE_CODE_SCHEME);
		when(docRepoHelper.extractClassificationMetadata(any(List.class), 
        		anyString(), anyString())).thenReturn(CLASS_CODE, CLASS_CODE_NAME, CONFIDENTIALITY_CODE,
        				CONFIDENTIALITY_CODE_NAME, FORMAT_CODE, FORMAT_CODE_NAME, HC_CODE, HC_CODE_NAME,
        				PRACTICE_SETTING, PRACTICE_SETTING_NAME, TYPE_CODE, TYPE_CODE_NAME);
		
		docRepo.setDocumentObjectsFromClassifications(doc, classifications);
		
		assertEquals(doc.getAuthorInstitution(),AUTHOR_INSTITUTION);
		assertEquals(doc.getAuthorPerson(), AUTHOR_PERSON);
		assertEquals(doc.getAuthorRole(), AUTHOR_ROLE);
		assertEquals(doc.getAuthorSpecialty(), AUTHOR_SPECIALTY);
		assertEquals(doc.getClassCode(), CLASS_CODE);
		assertEquals(doc.getClassCodeDisplayName(), CLASS_CODE_NAME);
		assertEquals(doc.getClassCodeScheme(), CLASS_CODE_SCHEME);
		assertEquals(doc.getConfidentialityCode(), CONFIDENTIALITY_CODE);
		assertEquals(doc.getConfidentialityCodeDisplayName(), CONFIDENTIALITY_CODE_NAME);
		assertEquals(doc.getConfidentialityCodeScheme(), CONFIDENTIALITY_CODE_SCHEME);
		assertEquals(doc.getFacilityCode(), HC_CODE);
		assertEquals(doc.getFacilityCodeDisplayName(), HC_CODE_NAME);
		assertEquals(doc.getFacilityCodeScheme(), HC_CODE_SCHEME);
		assertEquals(doc.getFormatCode(), FORMAT_CODE);
		assertEquals(doc.getFormatCodeDisplayName(), FORMAT_CODE_NAME);
		assertEquals(doc.getFormatCodeScheme(), FORMAT_CODE_SCHEME);
		assertEquals(doc.getPracticeSetting(), PRACTICE_SETTING);
		assertEquals(doc.getPracticeSettingDisplayName(), PRACTICE_SETTING_NAME);
		assertEquals(doc.getPracticeSettingScheme(), PRACTICE_SETTING_SCHEME);
		assertEquals(doc.getTypeCode(), TYPE_CODE);
		assertEquals(doc.getTypeCodeDisplayName(), TYPE_CODE_NAME);
		assertEquals(doc.getTypeCodeScheme(), TYPE_CODE_SCHEME);
	}
	
	@Test
	public void extractEventCodesTest(){
		final AdapterComponentDocRepositoryHelper docRepoHelper = mock(AdapterComponentDocRepositoryHelper.class);
		AdapterComponentDocRepositoryOrchImpl docRepo = new AdapterComponentDocRepositoryOrchImpl(){
			@Override
			protected AdapterComponentDocRepositoryHelper getHelper(){
		    	return docRepoHelper;
		    }
		};
		List<ClassificationType> classifications = new ArrayList();
		ClassificationType classificationType = mock(ClassificationType.class);
		classifications.add(classificationType);
		SlotType1 slot = mock(SlotType1.class);
		List<SlotType1> slotList = new ArrayList();
		slotList.add(slot);
		final String EVENT_SCHEME = "event scheme";
		final String NODE_REP = "node rep";
		final String EVENT_CODE_DISPLAY_NAME = "event display name";
		InternationalStringType internationalString = mock(InternationalStringType.class);
		LocalizedStringType localizedString = new LocalizedStringType();
		localizedString.setValue(EVENT_CODE_DISPLAY_NAME);
		List<LocalizedStringType> eventCodeDisplayNameList = new ArrayList<LocalizedStringType>();
		eventCodeDisplayNameList.add(localizedString);
		Document doc = new Document();
		
		when(classificationType.getClassificationScheme()).thenReturn(DocRepoConstants.XDS_EVENT_CODE_LIST_CLASSIFICATION);
		when(classificationType.getSlot()).thenReturn(slotList);
		when(classificationType.getNodeRepresentation()).thenReturn(NODE_REP);
		when(docRepoHelper.extractMetadataFromSlots(slotList,
                		DocRepoConstants.XDS_CODING_SCHEME_SLOT, 0)).thenReturn(EVENT_SCHEME);
		when(classificationType.getName()).thenReturn(internationalString);
		when(internationalString.getLocalizedString()).thenReturn(eventCodeDisplayNameList);
		
		docRepo.extractEventCodes(classifications, doc);
		
		EventCode event = doc.getEventCodes().iterator().next();
		
		assertEquals(event.getEventCode(), NODE_REP);
		assertEquals(event.getEventCodeDisplayName(), EVENT_CODE_DISPLAY_NAME);
		assertEquals(event.getEventCodeScheme(), EVENT_SCHEME);
	}
    
    protected ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType createDocumentRequest_Failure(){
        ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType requestBody = new ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType();
        DocumentRequest req1 = new DocumentRequest();
        req1.setDocumentUniqueId("");
        req1.setHomeCommunityId("1.1");
        req1.setRepositoryUniqueId("1");
        requestBody.getDocumentRequest().add(req1);
        return requestBody;
    }
    
    protected ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType createDocumentRequest_PartialSuccess(){
        ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType requestBody = new ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType();
        DocumentRequest req1 = new DocumentRequest();
        req1.setDocumentUniqueId("");
        req1.setHomeCommunityId("1.1");
        req1.setRepositoryUniqueId("1");
        requestBody.getDocumentRequest().add(req1);
        DocumentRequest req2 = new DocumentRequest();
        req2.setDocumentUniqueId("2.531.777");
        req2.setHomeCommunityId("1.1");
        req2.setRepositoryUniqueId("1");
        requestBody.getDocumentRequest().add(req2);
        return requestBody;
    }

}
