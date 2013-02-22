package gov.hhs.fha.nhinc.docrepository.adapter.service;

import gov.hhs.fha.nhinc.docrepository.adapter.dao.DocumentDao;
import gov.hhs.fha.nhinc.docrepository.adapter.dao.EventCodeDao;
import gov.hhs.fha.nhinc.docrepository.adapter.model.Document;
import gov.hhs.fha.nhinc.docrepository.adapter.model.DocumentQueryParams;
import gov.hhs.fha.nhinc.docrepository.adapter.model.EventCode;
import gov.hhs.fha.nhinc.docrepository.adapter.model.EventCodeParam;
import gov.hhs.fha.nhinc.util.hash.SHA1HashCode;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Matchers.anyLong;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DocumentServiceTest {

	@Test
	public void saveDocumentTest() throws NoSuchAlgorithmException, UnsupportedEncodingException{
		final DocumentDao DOCUMENT_DAO = mock(DocumentDao.class);
		final EventCodeDao EVENTCODE_DAO = mock(EventCodeDao.class);
		DocumentService documentService = new DocumentService(){
			@Override
			protected DocumentDao getDocumentDao(){
				return DOCUMENT_DAO;
			}
			@Override
			protected EventCodeDao getEventCodeDao(){
				return EVENTCODE_DAO;
			}
		};
		
		Document mockDoc = mock(Document.class);
		Set<EventCode> eventCodes = new HashSet<EventCode>();
		EventCode eventCode = mock(EventCode.class);
		eventCodes.add(eventCode);
		
		when(DOCUMENT_DAO.findById(anyLong())).thenReturn(mockDoc);
		when(mockDoc.getEventCodes()).thenReturn(eventCodes);

		Document doc = new Document();
		long id = 12345L;
		String rawDataString = "RAW DATA";
		byte[] rawData = rawDataString.getBytes();
		doc.setDocumentid(id);
		doc.setRawData(rawData);
		doc.setEventCodes(eventCodes);
		
		documentService.saveDocument(doc);
		
		verify(DOCUMENT_DAO).save(doc);
		assertEquals(doc.getHash(), SHA1HashCode.calculateSHA1(rawDataString));
	}
	
	@Test
	public void deleteDocumentTest_Success() throws DocumentServiceException{
		final DocumentDao DOCUMENT_DAO = mock(DocumentDao.class);
		final List<Document> DOC_LIST = new ArrayList<Document>();
		Document doc = new Document();
		String unique_id = "Doc_ID_1";
		doc.setDocumentUniqueId(unique_id);
		DOC_LIST.add(doc);
		DocumentService documentService = new DocumentService(){
			@Override
			protected DocumentDao getDocumentDao(){
				return DOCUMENT_DAO;
			}
			@Override
			public List<Document> documentQuery(DocumentQueryParams params) {
				return DOC_LIST;
			}
		};
		
		documentService.deleteDocument(doc);
		verify(DOCUMENT_DAO).delete(doc);
	}
	
	@Test(expected = DocumentServiceException.class)
	public void deleteDocumentTest_NoDocumentsReturned() throws DocumentServiceException{
		final DocumentDao DOCUMENT_DAO = mock(DocumentDao.class);
		final List<Document> DOC_LIST = new ArrayList<Document>();
		Document doc = new Document();
		String unique_id = "Doc_ID_1";
		doc.setDocumentUniqueId(unique_id);
		DocumentService documentService = new DocumentService(){
			@Override
			protected DocumentDao getDocumentDao(){
				return DOCUMENT_DAO;
			}
			@Override
			public List<Document> documentQuery(DocumentQueryParams params) {
				return DOC_LIST;
			}
		};
		
		documentService.deleteDocument(doc);
	}
	
	@Test(expected = DocumentServiceException.class)
	public void deleteDocumentTest_NullDocumentReturned() throws DocumentServiceException{
		Document nullDoc = null;
		DocumentService documentService = new DocumentService();
		documentService.deleteDocument(nullDoc);
	}
	
	@Test(expected = DocumentServiceException.class)
	public void deleteDocumentTest_NullDocumentId() throws DocumentServiceException{
		Document doc = new Document();
		DocumentService documentService = new DocumentService();
		documentService.deleteDocument(doc);
	}
	
	@Test
	public void documentQueryTest(){
		final DocumentDao documentDao = mock(DocumentDao.class);
		final EventCodeDao eventCodeDao = mock(EventCodeDao.class);
		DocumentService documentService = new DocumentService(){
			@Override
			protected DocumentDao getDocumentDao(){
				return documentDao;
			}
			@Override
			protected EventCodeDao getEventCodeDao(){
				return eventCodeDao;
			}
		};
		
		final long DOC_ID = 12345L;
		final String DOC_UNIQUE_ID = "Doc ID";
		Document doc = new Document();
		doc.setDocumentid(DOC_ID);
		doc.setDocumentUniqueId(DOC_UNIQUE_ID);
		List<Document> documents = new ArrayList<Document>();
		documents.add(doc);
		
		DocumentQueryParams dqParams = mock(DocumentQueryParams.class);
		EventCodeParam ecParam = mock(EventCodeParam.class);
		List<EventCodeParam> ecParamList = new ArrayList<EventCodeParam>();
		ecParamList.add(ecParam);
		
		List<EventCode> eventCodeList = new ArrayList<EventCode>();
		EventCode eventCode = mock(EventCode.class);
		eventCodeList.add(eventCode);
		
		when(documentDao.findDocuments(dqParams)).thenReturn(documents);
		when(dqParams.getEventCodeParams()).thenReturn(ecParamList);
		when(eventCodeDao.eventCodeQuery(ecParam)).thenReturn(eventCodeList);
		when(eventCode.getDocument()).thenReturn(doc);
		
		List<Document> resultDocList = documentService.documentQuery(dqParams);
		Document resultDoc = resultDocList.get(0);
		
		assertEquals(resultDocList.size(), 1);
		assertEquals(resultDoc.getDocumentid(),(Long) DOC_ID);
		assertEquals(resultDoc.getDocumentUniqueId(), DOC_UNIQUE_ID);
	}
	
	@Test
	public void getAllDocumentsTest(){
		final DocumentDao documentDao = mock(DocumentDao.class);
		DocumentService documentService = new DocumentService(){
			@Override
			protected DocumentDao getDocumentDao(){
				return documentDao;
			}
		};
		List<Document> documents = new ArrayList<Document>();
		
		when(documentDao.findAll()).thenReturn(documents);
		
		List<Document> results = documentService.getAllDocuments();
		
		assertTrue(results instanceof List);
	}
	
	@Test
	public void getDocumentDaoTest(){
		DocumentService documentService = new DocumentService();
		DocumentDao documentDao = documentService.getDocumentDao();
		
		assertTrue(documentDao instanceof DocumentDao);
	}
	
	@Test
	public void getEventCodeDaoTest(){
		DocumentService documentService = new DocumentService();
		EventCodeDao eventCodeDao = documentService.getEventCodeDao();
		
		assertTrue(eventCodeDao instanceof EventCodeDao);
	}
	
}
