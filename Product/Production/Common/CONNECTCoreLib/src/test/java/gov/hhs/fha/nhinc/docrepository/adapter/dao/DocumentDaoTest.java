package gov.hhs.fha.nhinc.docrepository.adapter.dao;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gov.hhs.fha.nhinc.docrepository.adapter.model.Document;
import gov.hhs.fha.nhinc.docrepository.adapter.model.DocumentQueryParams;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Before;
import org.junit.Test;

public class DocumentDaoTest {
	private final Session session = mock(Session.class);
	private Transaction transaction;
	private DocumentDao documentDao;
	
	@Before
	public void setUp(){
		transaction = mock(Transaction.class);
		documentDao = new DocumentDao(){
			@Override
			protected Session getSession(){
				return session;
			}
		};
		
		when(session.beginTransaction()).thenReturn(transaction);
	}
	
	@Test
	public void saveTest(){
		Document doc = new Document();
				
		documentDao.save(doc);
		
		verify(session).saveOrUpdate(doc);
	}
	
	@Test
	public void deleteTest(){
		Document doc = new Document();
		
		documentDao.delete(doc);
		
		verify(session).delete(doc);
	}
	
	@Test
	public void findByIdTest(){
		Document doc = new Document();
		final long ID = 12345;
		doc.setDocumentid(ID);
		
		when(session.get(Document.class, ID)).thenReturn(doc);
		
		Document resultDoc = documentDao.findById(ID);
		
		assertEquals(resultDoc.getDocumentid(), (Long) ID);
	}
	
	@Test
	public void findAllTest(){
		Document doc1 = new Document();
		final long ID_1 = 12345;
		doc1.setDocumentid(ID_1);
		
		Document doc2 = new Document();
		final long ID_2 = 12345;
		doc2.setDocumentid(ID_2);
		
		List<Document> documents = new ArrayList<Document>();
		documents.add(doc1);
		documents.add(doc2);
		
		Criteria criteria = mock(Criteria.class);
		
		when(session.createCriteria(Document.class)).thenReturn(criteria);
		when(criteria.list()).thenReturn(documents);
		
		List<Document> resultsDocuments = documentDao.findAll();
		
		assertEquals(resultsDocuments.size(), 2);
		assertEquals(resultsDocuments.get(0).getDocumentid(),
				(Long) ID_1);
		assertEquals(resultsDocuments.get(1).getDocumentid(),
				(Long) ID_2);
	}
	
	@Test
	public void findDocumentsTest(){
		DocumentQueryParams params = new DocumentQueryParams();
		
		params.setPatientId("patientID");
		List<String> classCodes = new ArrayList<String>();
		classCodes.add("class^^Code");
		params.setClassCodes(classCodes);
		params.setClassCodeScheme("class code scheme");
		Date creationTimeFrom = new Date();
		creationTimeFrom.setTime(01012222);
		params.setCreationTimeFrom(creationTimeFrom);
		Date creationTimeTo = new Date();
		creationTimeTo.setTime(01012222);
		params.setCreationTimeTo(creationTimeTo);
		Date serviceStartTimeFrom = new Date();
		serviceStartTimeFrom.setTime(01012222);
		params.setServiceStartTimeFrom(serviceStartTimeFrom);
		Date serviceStartTimeTo = new Date();
		serviceStartTimeTo.setTime(01012222);
		params.setServiceStartTimeTo(serviceStartTimeTo);
		Date serviceStopTimeFrom = new Date();
		serviceStopTimeFrom.setTime(01012222);
		params.setServiceStopTimeFrom(serviceStopTimeFrom);
		Date serviceStopTimeTo = new Date();
		serviceStopTimeTo.setTime(01012222);
		params.setServiceStopTimeTo(serviceStopTimeTo);
		List<String> statuses = new ArrayList<String>();
		statuses.add("status");
		params.setStatuses(statuses);
		List<String> documentUniqueIds = new ArrayList<String>();
		documentUniqueIds.add("doc unique id");
		params.setDocumentUniqueId(documentUniqueIds);
		params.setOnDemandParams(false);
		
		Document doc1 = new Document();
		final long ID_1 = 12345;
		doc1.setDocumentid(ID_1);
		
		Document doc2 = new Document();
		final long ID_2 = 12345;
		doc2.setDocumentid(ID_2);
		
		List<Document> documents = new ArrayList<Document>();
		documents.add(doc1);
		documents.add(doc2);
		
		Criteria criteria = mock(Criteria.class);
		
		when(session.createCriteria(Document.class)).thenReturn(criteria);
		when(criteria.list()).thenReturn(documents);
		
		when(session.createCriteria(Document.class)).thenReturn(criteria);
		when(criteria.list()).thenReturn(documents);
		
		List<Document> resultsDocuments = documentDao.findDocuments(params);
		
		assertEquals(resultsDocuments.size(), 2);
		assertEquals(resultsDocuments.get(0).getDocumentid(),
				(Long) ID_1);
		assertEquals(resultsDocuments.get(1).getDocumentid(),
				(Long) ID_2);
		
	}
}
