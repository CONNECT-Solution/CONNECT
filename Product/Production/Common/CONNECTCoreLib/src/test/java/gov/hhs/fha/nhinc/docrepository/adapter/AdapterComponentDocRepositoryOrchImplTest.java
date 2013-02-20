/**
 * 
 */
package gov.hhs.fha.nhinc.docrepository.adapter;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import gov.hhs.fha.nhinc.docrepository.adapter.model.Document;
import gov.hhs.fha.nhinc.docrepository.adapter.model.DocumentQueryParams;
import gov.hhs.fha.nhinc.docrepository.adapter.service.DocumentService;
import gov.hhs.fha.nhinc.largefile.LargeFileUtils;
import gov.hhs.fha.nhinc.util.format.UTCDateUtil;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType.DocumentRequest;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType.DocumentResponse;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.activation.DataHandler;
import javax.xml.bind.JAXBElement;

import oasis.names.tc.ebxml_regrep.xsd.lcm._3.SubmitObjectsRequest;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.AssociationType1;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.IdentifiableType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectListType;
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
	public void createDateUtilTest() {
    	AdapterComponentDocRepositoryOrchImpl docRepo = new AdapterComponentDocRepositoryOrchImpl();
    	assertTrue(docRepo.createDateUtil() instanceof UTCDateUtil);
    }
    
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
    		protected LargeFileUtils getLargeFileUtils(){
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
    		protected LargeFileUtils getLargeFileUtils(){
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
    		protected HashMap<String, DataHandler> getDocumentMap(
    				ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType body){
    			return docMap;
    		}
    		
    		@Override
    		protected void setDocument(List<JAXBElement<? extends oasis.names.tc.ebxml_regrep.xsd.rim._3.IdentifiableType>> identifiableObjectList,
    	    		RegistryErrorList errorList, int i, HashMap<String, DataHandler> docMap, boolean requestHasReplacementAssociation){
    			//Do nothing
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
    	
    	assertEquals(registryResponse.getStatus(), docRepo.XDS_RETRIEVE_RESPONSE_STATUS_SUCCESS);
    }
    
    @Test
    public void documentRepositoryProvideAndRegisterDocumentSet_Failure(){
    	ProvideAndRegisterDocumentSetRequestType body = null;
    
    	AdapterComponentDocRepositoryOrchImpl docRepo = new AdapterComponentDocRepositoryOrchImpl();
    	RegistryResponseType registryResponse = docRepo.documentRepositoryProvideAndRegisterDocumentSet(body);
    	
    	RegistryErrorList errorList = registryResponse.getRegistryErrorList();
    	RegistryError error = errorList.getRegistryError().get(0);
    	
    	assertEquals(registryResponse.getStatus(), docRepo.XDS_RETRIEVE_RESPONSE_STATUS_FAILURE);
    	assertEquals(error.getValue(), docRepo.XDS_MISSING_REQUEST_MESSAGE_DATA + 
    			" ProvideAndRegisterDocumentSetRequestType element is null.");	
    }
    
    @Test
    public void saveDocumentTest_Success(){
    	final DocumentService docService = mock(DocumentService.class);
    	final String PATIENT_ID = "ID_1";
    	final String DOC_UNIQUE_ID = "DOC_ID_1";
    	final String CLASS_CODE = "CLASS_CODE";
    	final String STATUS = "STATUS";
    	final long DOC_ID = 101;
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
    	assertEquals(error.getValue(), docRepo.XDS_REPOSITORY_ERROR +
    			" DocumentUniqueId: " + DOC_UNIQUE_ID);
    	
    }
    
    @Test
    public void getDocumentMapTest() throws IOException {
    	AdapterComponentDocRepositoryOrchImpl docRepo = new AdapterComponentDocRepositoryOrchImpl();
    	ProvideAndRegisterDocumentSetRequestType body = 
    			mock(ProvideAndRegisterDocumentSetRequestType.class);
    	List<ProvideAndRegisterDocumentSetRequestType.Document> docList= 
    			new ArrayList<ProvideAndRegisterDocumentSetRequestType.Document>();
    	ProvideAndRegisterDocumentSetRequestType.Document doc = new ProvideAndRegisterDocumentSetRequestType.Document();
    	final String ID = "MOCK_ID";
    	final String VALUE = "MOCK_VALUE";
    	DataHandler dataHandler = LargeFileUtils.getInstance().convertToDataHandler(VALUE);
    	doc.setId(ID);
    	doc.setValue(dataHandler);
    	
    	docList.add(doc);
    	
    	when(body.getDocument()).thenReturn(docList);
    	
    	HashMap<String, DataHandler> docMap = docRepo.getDocumentMap(body);
    	
    	assertEquals(docMap.get(ID), dataHandler);
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
    			docRepo.XDS_MISSING_DOCUMENT_METADATA + " associationType element is null.");
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
