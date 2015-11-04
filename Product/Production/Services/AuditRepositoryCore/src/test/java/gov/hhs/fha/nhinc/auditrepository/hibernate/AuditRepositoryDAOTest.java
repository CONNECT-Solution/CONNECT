/*
 * Copyright (c) 2009-2015, United States Government, as represented by the Secretary of Health and Human Services.
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

import java.util.Date;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import org.junit.Ignore;

/**
 *
 * @author MFLYNN02
 */
@Ignore
// TODO: Move to an integration test
public class AuditRepositoryDAOTest {

    private AuditRepositoryDAO auditDao = null;

    public AuditRepositoryDAOTest() {
        auditDao = AuditRepositoryDAO.getAuditRepositoryDAOInstance();
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
     * Test of getAuditRepositoryDAOInstance method, of class AuditRepositoryDAO.
     */
    @Test
    public void testGetAuditRepositoryDAOInstance() {
        AuditRepositoryDAO result = AuditRepositoryDAO.getAuditRepositoryDAOInstance();
    }

    /**
     * Test of insertAuditRepository method, of class AuditRepositoryDAO.
     */
    @Test
    public void testInsertAuditRepository() {
        List<AuditRepositoryRecord> eventLogList = new ArrayList();
        Date now = new Date();

        AuditRepositoryRecord record = new AuditRepositoryRecord();
        record.setMessageType("Record 1 - Message Type");
        record.setTimeStamp(now);
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

}
