/*
 * Copyright (c) 2009-2019, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.docrepository.adapter.dao;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import gov.hhs.fha.nhinc.docrepository.adapter.model.DocumentMetadata;
import gov.hhs.fha.nhinc.docrepository.adapter.model.DocumentQueryParams;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
    public void setUp() {
        transaction = mock(Transaction.class);
        documentDao = new DocumentDao() {
            @Override
            protected Session getSession() {
                return session;
            }
        };

        when(session.beginTransaction()).thenReturn(transaction);
    }

    @Test
    public void testSave() {
        DocumentMetadata doc = new DocumentMetadata();

        documentDao.save(doc);

        verify(session).saveOrUpdate(doc);
    }

    @Test
    public void testDelete() {
        DocumentMetadata doc = new DocumentMetadata();

        documentDao.delete(doc);

        verify(session).delete(doc);
    }

    @Test
    public void testDeleteAll() {
        List<DocumentMetadata> documents = new ArrayList<>();
        DocumentMetadata document1 = new DocumentMetadata();
        documents.add(document1);
        DocumentMetadata document2 = new DocumentMetadata();
        documents.add(document2);
        DocumentMetadata document3 = new DocumentMetadata();
        documents.add(document3);

        when(session.isOpen()).thenReturn(true);
        documentDao.deleteAll(documents);

        verify(session).beginTransaction();
        verify(session).delete(document1);
        verify(session).delete(document2);
        verify(session).delete(document3);
        verify(transaction).commit();
        verify(session).close();
    }

    @Test
    public void testFindById() {
        DocumentMetadata doc = new DocumentMetadata();
        final long ID = 12345;
        doc.setDocumentid(ID);

        when(session.get(DocumentMetadata.class, ID)).thenReturn(doc);

        DocumentMetadata resultDoc = documentDao.findById(ID);

        assertEquals((Long) ID, resultDoc.getDocumentid());
    }

    @Test
    public void testFindAll() {
        DocumentMetadata doc1 = new DocumentMetadata();
        final long ID_1 = 12345;
        doc1.setDocumentid(ID_1);

        DocumentMetadata doc2 = new DocumentMetadata();
        final long ID_2 = 12345;
        doc2.setDocumentid(ID_2);

        List<DocumentMetadata> documents = new ArrayList<>();
        documents.add(doc1);
        documents.add(doc2);

        Criteria criteria = mock(Criteria.class);

        when(session.createCriteria(DocumentMetadata.class)).thenReturn(criteria);
        when(criteria.list()).thenReturn(documents);

        List<DocumentMetadata> resultsDocuments = documentDao.findAll();

        assertEquals(2, resultsDocuments.size());
        assertEquals((Long) ID_1, resultsDocuments.get(0).getDocumentid());
        assertEquals((Long) ID_2, resultsDocuments.get(1).getDocumentid());
    }

    @Test
    public void testFindDocuments() {
        DocumentQueryParams params = new DocumentQueryParams();

        params.setPatientId("patientID");
        List<String> classCodes = new ArrayList<>();
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
        List<String> statuses = new ArrayList<>();
        statuses.add("status");
        params.setStatuses(statuses);
        List<String> documentUniqueIds = new ArrayList<>();
        documentUniqueIds.add("doc unique id");
        params.setDocumentUniqueId(documentUniqueIds);
        params.setOnDemandParams(false);

        DocumentMetadata doc1 = new DocumentMetadata();
        final long ID_1 = 12345;
        doc1.setDocumentid(ID_1);

        DocumentMetadata doc2 = new DocumentMetadata();
        final long ID_2 = 12345;
        doc2.setDocumentid(ID_2);

        List<DocumentMetadata> documents = new ArrayList<>();
        documents.add(doc1);
        documents.add(doc2);

        Criteria criteria = mock(Criteria.class);


        when(session.createCriteria(DocumentMetadata.class)).thenReturn(criteria);
        when(criteria.list()).thenReturn(documents);

        List<DocumentMetadata> resultsDocuments = documentDao.findDocuments(params);

        assertEquals(2, resultsDocuments.size());
        assertEquals((Long) ID_1, resultsDocuments.get(0).getDocumentid());
        assertEquals((Long) ID_2, resultsDocuments.get(1).getDocumentid());

    }
}
