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
package gov.hhs.fha.nhinc.auditrepository.hibernate;

import java.sql.Blob;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author MFLYNN02
 */
@Ignore
// TODO: Move to an integration test
public class AuditRepositoryDAOTest {

    private String messageId = null;
    private Integer outcome = null;
    private List<String> eventType = null;
    private String userId = null;
    private List<String> remoteHcid = null;
    private Date startDate = null;
    private Date endDate = null;
    private String relatesTo = null;
    private List<AuditRepositoryRecord> responseList = null;
    private static final AuditRepositoryDAO auditLogDao = new AuditRepositoryDAO();
    private static final Logger LOG = LoggerFactory.getLogger(AuditRepositoryDAOTest.class);

    private AuditRepositoryDAO auditDao = null;

    public AuditRepositoryDAOTest() {
        auditDao = new AuditRepositoryDAO();
    }

    @BeforeClass
    public static void setUpClass() throws Exception {

    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {

    }

    @After
    public void tearDown() {
    }

    /**
     * Test of insertAuditRepository method, of class AuditRepositoryDAO.
     */
    @Test
    public void testInsertAuditRepository() {
        List<AuditRepositoryRecord> eventLogList = new ArrayList();
        Date now = new Date();

        AuditRepositoryRecord record = new AuditRepositoryRecord();

        //All data elements for auditrepo should be added for junit Tests when work on auditrepo adapter data
        //persistence
        record.setDirection("Record 1 - Message Type");
        record.setEventTimestamp(now);
        record.setUserId("UnitTest1");
        eventLogList.add(record);

        boolean expResult = true;
        boolean result = auditDao.insertAuditRepository(eventLogList);
        assertEquals(expResult, result);

    }

    /**
     * Test of queryAuditRepositoryOnCriteria method, of class AuditRepositoryDAO.
     */
    @Test
    public void testQueryAuditRepositoryOnCriteria() {
        String eUserId = "UnitTest1";
        String ePatientId = "";
        String eCommunityId = "";
        Date startDate = null;
        Date endDate = null;
        List result = auditDao.queryAuditRepositoryOnCriteria(eUserId, ePatientId, startDate, endDate);
        assertNotNull(result);

    }

    /**
     * Test of queryAuditRepository Viewer with messageId and Outcome to test queryAuditViewer method, of class
     * AuditRepositoryDAO .
     */
    @Test
    public void testQueryByMessageIdAndCheckPrecedence() {
        messageId = "urn:uuid%";
        outcome = 0;
        responseList = auditLogDao.queryAuditRecords(messageId, relatesTo);
        assertNotNull(responseList);

    }

    /**
     * Test of queryAuditRepository Viewer with AuditId to test queryAuditViewerByAuditId method, of class
     * AuditRepositoryDAO .
     */
    @Test
    public void testQueryByAuditId() {
        int auditId = 15;
        Blob message = auditLogDao.queryByAuditId(auditId);
        assertNotNull(message);

    }

    /**
     * Test of queryAuditRepository Viewer with list passed for EventType to test queryAuditViewer method, of class
     * AuditRepositoryDAO .
     */
    @Test
    public void testQueryByEventTypeList() {
        eventType = new ArrayList<>(Arrays.asList("DocSubmissionDeferredReq", "DocSubmission", "QueryForDocuments"));
        responseList = auditLogDao.queryByAuditOptions(eventType, userId, remoteHcid, startDate, endDate);
        assertNotNull(responseList);
    }

    /**
     * Test of queryAuditRepository Viewer with date options to test queryAuditViewer method, of class
     * AuditRepositoryDAO .
     */
    @Test
    public void testQueryByDateSearch() {
        try {
            String dateStr = "12/13/2015 22:53:07";
            SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            startDate = formatter.parse(dateStr);
            responseList = auditLogDao.queryByAuditOptions(eventType, userId, remoteHcid, startDate, endDate);
            assertNotNull(responseList);
        } catch (ParseException ex) {
            LOG.error("Exception occurred due to :" + ex.getLocalizedMessage(), ex);
        }
    }

    /**
     * Test of queryAuditRepository Viewer with date options to test queryAuditViewer method, of class
     * AuditRepositoryDAO .
     */
    @Test
    public void testQueryByStartEndDateSearch() {
        try {
            String dateStartStr = "12/10/2015 00:00:00";
            String dateEndStr = "12/11/2015 00:00:00";
            SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            startDate = formatter.parse(dateStartStr);
            endDate = formatter.parse(dateEndStr);
            responseList = auditLogDao.queryByAuditOptions(eventType, userId, remoteHcid, startDate, endDate);
            assertNotNull(responseList);
        } catch (ParseException ex) {
            LOG.error("Exception occurred due to :" + ex.getLocalizedMessage(), ex);
        }
    }

}
