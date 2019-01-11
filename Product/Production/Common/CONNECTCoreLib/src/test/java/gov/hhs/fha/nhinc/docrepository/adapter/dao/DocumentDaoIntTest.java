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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import gov.hhs.fha.nhinc.docrepository.adapter.model.Document;
import gov.hhs.fha.nhinc.docrepository.adapter.model.DocumentMetadata;
import gov.hhs.fha.nhinc.docrepository.adapter.model.DocumentQueryParams;
import gov.hhs.fha.nhinc.test.DAOIntegrationTest;
import java.util.Arrays;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

//Uncomment to run DAO Integration test which will use a MySQL DB connection.
@Ignore
public class DocumentDaoIntTest extends DAOIntegrationTest {

    private static DocumentDao documentDao;
    private DocumentMetadata metadata;

    @BeforeClass
    public static void beforeClass() {
        documentDao = new DocumentDao();
    }

    @Before
    public void setUp() {
        metadata = new DocumentMetadata();
    }

    @After
    public void tearDown() {
        // Since we have no transaction manager in this project, we cannot rollback with @Rollback on this test.
        // Do it the good ol' fashioned way
        documentDao.delete(metadata);
    }

    @Test
    public void testFindDocuments() {

        DocumentQueryParams params = new DocumentQueryParams();

        params.setClassCodes(Arrays.asList("testFindDocuments"));
        params.setClassCodeScheme("testFindDocuments");
        params.setPatientId("testFindDocuments");
        params.setOnDemandParams(true);
        params.setDocumentUniqueId(Arrays.asList("testFindDocuments"));

        metadata.setOnDemand(true);
        metadata.setDocumentUniqueId("testFindDocuments");
        metadata.setPatientRecordId(9000L);
        metadata.setPatientId("testFindDocuments");
        metadata.setNewRepositoryUniqueId("1.1");
        metadata.setClassCodeScheme("testFindDocuments");
        metadata.setClassCode("testFindDocuments");

        documentDao.save(metadata);

        List<DocumentMetadata> data = documentDao.findDocuments(params);
        assertEquals(1, data.size());
    }

    @Test
    public void testFindAllByPatientId() {
        metadata.setOnDemand(false);
        metadata.setDocumentUniqueId("testFindAllByPatientId");
        metadata.setPatientRecordId(9000L);
        metadata.setPatientId("testFindAllByPatientId");
        metadata.setNewRepositoryUniqueId("1.1");

        assertTrue(documentDao.save(metadata));

        List<DocumentMetadata> data = documentDao.findAllByPatientId(9000L);
        assertEquals(1, data.size());
    }

    @Test
    public void testFindAll() {
        metadata.setOnDemand(false);
        metadata.setDocumentUniqueId("testFindAll");
        metadata.setNewRepositoryUniqueId("1.1");

        assertTrue(documentDao.save(metadata));

        List<DocumentMetadata> data = documentDao.findAll();
        assertTrue(data.size() >= 1); // Might have pre-existing rows in the db.
    }

    @Test
    public void testFindById() {
        metadata.setOnDemand(false);
        metadata.setDocumentUniqueId("testFindById");
        metadata.setNewRepositoryUniqueId("1.1");

        metadata = documentDao.saveAndGetDocument(metadata);

        DocumentMetadata data = documentDao.findById(metadata.getDocumentid());
        assertNotNull(data);

        // So it will get cleaned up from the DB in @After
        metadata = data;
    }

    @Test
    public void testSaveAndGetDocument() {
        metadata.setOnDemand(false);
        metadata.setDocumentUniqueId("testSaveAndGetDocument");
        metadata.setNewRepositoryUniqueId("1.1");

        DocumentMetadata data = documentDao.saveAndGetDocument(metadata);
        assertNotNull(data);

        // So it will get cleaned up from the DB in @After
        metadata = data;
    }

    @Test
    public void testSaveAll() {
        metadata.setOnDemand(false);
        metadata.setDocumentUniqueId("testSaveAll");
        metadata.setNewRepositoryUniqueId("1.1");

        Document doc = new Document(metadata);
        doc.setDocumentUniqueId("blah");
        doc.setRepositoryUniqueId("1.1");
        doc.setRawData(new byte[100]);

        metadata.setDocument(doc);

        assertTrue(documentDao.saveAll(Arrays.asList(metadata)));

    }

    @Test
    public void testSave() {
        metadata.setOnDemand(false);
        metadata.setDocumentUniqueId("testSave");
        metadata.setNewRepositoryUniqueId("1.1");

        Document doc = new Document(metadata);
        doc.setDocumentUniqueId("blah");
        doc.setRepositoryUniqueId("1.1");
        doc.setRawData(new byte[100]);

        metadata.setDocument(doc);

        assertTrue(documentDao.save(metadata));

    }
}
