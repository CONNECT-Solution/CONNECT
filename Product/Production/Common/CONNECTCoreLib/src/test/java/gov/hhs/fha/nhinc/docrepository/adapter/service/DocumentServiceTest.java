/*
 * Copyright (c) 2009-2017, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.docrepository.adapter.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import gov.hhs.fha.nhinc.docrepository.adapter.dao.DocumentDao;
import gov.hhs.fha.nhinc.docrepository.adapter.dao.EventCodeDao;
import gov.hhs.fha.nhinc.docrepository.adapter.model.Document;
import gov.hhs.fha.nhinc.docrepository.adapter.model.DocumentQueryParams;
import gov.hhs.fha.nhinc.docrepository.adapter.model.EventCode;
import gov.hhs.fha.nhinc.docrepository.adapter.model.EventCodeParam;
import gov.hhs.fha.nhinc.util.hash.SHA1HashCode;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.Test;
import org.mockito.Mockito;

public class DocumentServiceTest {

    private final DocumentDao DOCUMENT_DAO = mock(DocumentDao.class);
    private final EventCodeDao EVENTCODE_DAO = mock(EventCodeDao.class);

    @Test
    public void testSaveDocument() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        DocumentService documentService = getDocumentServiceWithMockDaos();

        Document mockDoc = mock(Document.class);
        Set<EventCode> eventCodes = new HashSet<>();
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
    public void testDeleteDocument_Success() throws DocumentServiceException {
        final List<Document> DOC_LIST = new ArrayList<>();
        Document doc = new Document();
        String unique_id = "Doc_ID_1";
        doc.setDocumentUniqueId(unique_id);
        DOC_LIST.add(doc);
        DocumentService documentService = new DocumentService() {
            @Override
            protected DocumentDao getDocumentDao() {
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
    public void testDeleteDocument_NoDocumentsReturned() throws DocumentServiceException {
        final DocumentDao DOCUMENT_DAO = mock(DocumentDao.class);
        final List<Document> DOC_LIST = new ArrayList<>();
        Document doc = new Document();
        String unique_id = "Doc_ID_1";
        doc.setDocumentUniqueId(unique_id);
        DocumentService documentService = new DocumentService() {
            @Override
            protected DocumentDao getDocumentDao() {
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
    public void testDeleteDocument_NullDocumentReturned() throws DocumentServiceException {
        Document nullDoc = null;
        DocumentService documentService = new DocumentService();
        documentService.deleteDocument(nullDoc);
    }

    @Test(expected = DocumentServiceException.class)
    public void testDeleteDocument_NullDocumentId() throws DocumentServiceException {
        Document doc = new Document();
        DocumentService documentService = new DocumentService();
        documentService.deleteDocument(doc);
    }

    @Test
    public void testDocumentQuery() {
        DocumentService documentService = getDocumentServiceWithMockDaos();

        final long DOC_ID = 12345L;
        final String DOC_UNIQUE_ID = "Doc ID";
        Document doc = new Document();
        doc.setDocumentid(DOC_ID);
        doc.setDocumentUniqueId(DOC_UNIQUE_ID);
        List<Document> documents = new ArrayList<>();
        documents.add(doc);

        DocumentQueryParams dqParams = mock(DocumentQueryParams.class);
        // EventCodeParam ecParam = mock(EventCodeParam.class);
        EventCodeParam ecParam = new EventCodeParam();
        ecParam.setEventCode("T-32000");
        ecParam.setEventCodeScheme("SNM3");
        List<EventCodeParam> ecParamList = new ArrayList<>();
        ecParamList.add(ecParam);

        List<EventCode> eventCodeList = new ArrayList<>();
        EventCode eventcode = mock(EventCode.class);
        eventCodeList.add(eventcode);

        when(DOCUMENT_DAO.findDocuments(dqParams)).thenReturn(documents);
        when(dqParams.getEventCodeParams()).thenReturn(ecParamList);
        when(EVENTCODE_DAO.eventCodeQuery(Mockito.anyList())).thenReturn(eventCodeList);
        when(eventcode.getDocument()).thenReturn(doc);

        List<Document> resultDocList = documentService.documentQuery(dqParams);
        Document resultDoc = resultDocList.get(0);

        assertEquals(resultDocList.size(), 1);
        assertEquals(resultDoc.getDocumentid(), (Long) DOC_ID);
        assertEquals(resultDoc.getDocumentUniqueId(), DOC_UNIQUE_ID);
    }

    @Test
    public void testGetAllDocuments() {
        DocumentService documentService = getDocumentServiceWithMockDaos();

        List<Document> documents = new ArrayList<>();

        when(DOCUMENT_DAO.findAll()).thenReturn(documents);

        List<Document> results = documentService.getAllDocuments();

        assertTrue(results instanceof List);
    }

    @Test
    public void testGetDocumentDao() {
        DocumentService documentService = new DocumentService();
        DocumentDao documentDao = documentService.getDocumentDao();

        assertTrue(documentDao instanceof DocumentDao);
    }

    @Test
    public void testGetEventCodeDao() {
        DocumentService documentService = new DocumentService();
        EventCodeDao eventCodeDao = documentService.getEventCodeDao();

        assertTrue(eventCodeDao instanceof EventCodeDao);
    }

    private DocumentService getDocumentServiceWithMockDaos() {
        DocumentService documentService = new DocumentService() {
            @Override
            protected DocumentDao getDocumentDao() {
                return DOCUMENT_DAO;
            }

            @Override
            protected EventCodeDao getEventCodeDao() {
                return EVENTCODE_DAO;
            }
        };
        return documentService;
    }
}
