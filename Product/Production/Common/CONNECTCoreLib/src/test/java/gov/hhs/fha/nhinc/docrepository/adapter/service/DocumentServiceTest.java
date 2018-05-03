/*
 * Copyright (c) 2009-2018, United States Government, as represented by the Secretary of Health and Human Services.
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
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import gov.hhs.fha.nhinc.docrepository.adapter.dao.DocumentDao;
import gov.hhs.fha.nhinc.docrepository.adapter.dao.EventCodeDao;
import gov.hhs.fha.nhinc.docrepository.adapter.model.Document;
import gov.hhs.fha.nhinc.docrepository.adapter.model.DocumentMetadata;
import gov.hhs.fha.nhinc.docrepository.adapter.model.DocumentQueryParams;
import gov.hhs.fha.nhinc.docrepository.adapter.model.EventCode;
import gov.hhs.fha.nhinc.docrepository.adapter.model.EventCodeParam;
import gov.hhs.fha.nhinc.util.hash.SHA1HashCode;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.util.ReflectionUtils;

public class DocumentServiceTest {

    private final DocumentDao DOCUMENT_DAO = mock(DocumentDao.class);
    private final EventCodeDao EVENTCODE_DAO = mock(EventCodeDao.class);

    @Test
    public void testSaveDocument() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        DocumentService documentService = getDocumentServiceWithMockDaos();

        DocumentMetadata doc = new DocumentMetadata();
        Document document = new Document(doc);
        long id = 12345L;
        String rawDataString = "RAW DATA";
        byte[] rawData = rawDataString.getBytes();
        document.setRawData(rawData);

        doc.setDocumentid(id);

        Set<EventCode> eventCodes = new HashSet<>();
        eventCodes.add(new EventCode());
        doc.setEventCodes(eventCodes);

        doc.setDocument(document);

        documentService.saveDocument(doc);

        when(DOCUMENT_DAO.findById(anyLong())).thenReturn(doc);

        verify(DOCUMENT_DAO).save(doc);
        assertEquals(SHA1HashCode.calculateSHA1(rawDataString), doc.getHash());
    }

    @Test
    public void testDeleteDocument_Success() throws DocumentServiceException {
        final List<DocumentMetadata> DOC_LIST = new ArrayList<>();
        DocumentMetadata doc = new DocumentMetadata();
        String unique_id = "Doc_ID_1";
        doc.setDocumentUniqueId(unique_id);
        DOC_LIST.add(doc);
        DocumentService documentService = getDocumentServiceWithMockDaos();

        when(DOCUMENT_DAO.findDocuments(Mockito.isA(DocumentQueryParams.class))).thenReturn(DOC_LIST);

        documentService.deleteDocument(doc);
        verify(DOCUMENT_DAO).delete(doc);
    }

    @Test(expected = DocumentServiceException.class)
    public void testDeleteDocument_NoDocumentsReturned() throws DocumentServiceException {
        DocumentMetadata doc = new DocumentMetadata();
        String unique_id = "Doc_ID_1";
        doc.setDocumentUniqueId(unique_id);
        DocumentService documentService = getDocumentServiceWithMockDaos();

        // TODO: Make test better.
        documentService.deleteDocument(doc);
    }

    @Test(expected = DocumentServiceException.class)
    public void testDeleteDocument_NullDocumentReturned() throws DocumentServiceException {
        DocumentMetadata nullDoc = null;
        DocumentService documentService = new DocumentService();
        documentService.deleteDocument(nullDoc);
    }

    @Test(expected = DocumentServiceException.class)
    public void testDeleteDocument_NullDocumentId() throws DocumentServiceException {
        DocumentMetadata doc = new DocumentMetadata();
        DocumentService documentService = new DocumentService();
        documentService.deleteDocument(doc);
    }

    @Test
    public void testDocumentQuery() {
        DocumentService documentService = getDocumentServiceWithMockDaos();

        final long DOC_ID = 12345L;
        final String DOC_UNIQUE_ID = "Doc ID";
        DocumentMetadata doc = new DocumentMetadata();
        doc.setDocumentid(DOC_ID);
        doc.setDocumentUniqueId(DOC_UNIQUE_ID);
        List<DocumentMetadata> documents = new ArrayList<>();
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

        List<DocumentMetadata> resultDocList = documentService.documentQuery(dqParams);
        DocumentMetadata resultDoc = resultDocList.get(0);

        assertEquals(1, resultDocList.size());
        assertEquals((Long) DOC_ID, resultDoc.getDocumentid());
        assertEquals(DOC_UNIQUE_ID, resultDoc.getDocumentUniqueId());
    }

    @Test
    public void testGetAllDocuments() {
        DocumentService documentService = getDocumentServiceWithMockDaos();

        List<DocumentMetadata> documents = new ArrayList<>();

        when(DOCUMENT_DAO.findAll()).thenReturn(documents);

        List<DocumentMetadata> results = documentService.getAllDocuments();

        verify(DOCUMENT_DAO).findAll();
        assertEquals(documents, results);
    }

    private DocumentService getDocumentServiceWithMockDaos() {

        // Since we aren't using Autowiring for these types of things for some reason... Reflection to the rescue.
        DocumentService documentService = new DocumentService();
        Field docDao = ReflectionUtils.findField(DocumentService.class, "documentDao");
        ReflectionUtils.makeAccessible(docDao);
        ReflectionUtils.setField(docDao, documentService, DOCUMENT_DAO);

        Field eventDao = ReflectionUtils.findField(DocumentService.class, "eventCodeDao");
        ReflectionUtils.makeAccessible(eventDao);
        ReflectionUtils.setField(eventDao, documentService, EVENTCODE_DAO);
        return documentService;
    }
}
