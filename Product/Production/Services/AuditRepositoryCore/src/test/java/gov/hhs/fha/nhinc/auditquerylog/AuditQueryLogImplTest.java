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
package gov.hhs.fha.nhinc.auditquerylog;

import gov.hhs.fha.nhinc.auditrepository.hibernate.AuditRepositoryDAO;
import gov.hhs.fha.nhinc.auditrepository.hibernate.AuditRepositoryRecord;
import gov.hhs.fha.nhinc.common.auditquerylog.QueryAuditEventsRequestType;
import gov.hhs.fha.nhinc.common.auditquerylog.QueryAuditEventsResponseType;
import gov.hhs.fha.nhinc.common.auditquerylog.QueryAuditEventsResults;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 * @author tjafri
 */
public class AuditQueryLogImplTest {

    private final String DIRECTION = "Outbound";
    private final String REMOTE_HCID = "2.2";
    private final String EVENT_TYPE = "patientDiscovery";
    private final String EVENT_ID = "Query";
    private final String MESSAGE_ID = "urn:uuid:4abc7cc8-1edc-409d-b061-bf65b21f7b1e";
    private final String RELATES_TO = "urn:uuid:4abc7cc8-1edc-409d-b061-bf65b21f7b1e";
    private final String USER_ID = "wanderson";
    private final Long ID = 1L;
    private final int OUTCOME = 0;
    private final Date EVENT_TIMESTAMP = Calendar.getInstance().getTime();
    private final Session mockSession = mock(Session.class);
    private final Criteria mockCriteria = mock(Criteria.class);
    private AuditRepositoryDAO dao = null;
    private AuditQueryLogImpl auditRetrieve = null;

    @Before
    public void setup() {
        dao = new AuditRepositoryDAO() {
            @Override
            protected Session getSession() {
                return mockSession;
            }
        };
        auditRetrieve = new AuditQueryLogImpl() {
            @Override
            protected AuditRepositoryDAO getAuditRepositoryDao() {
                return dao;
            }
        };
    }

    @Test
    public void testRetrieveAuditsBasedOnUserId() {
        when(mockSession.createCriteria(AuditRepositoryRecord.class)).thenReturn(mockCriteria);
        when(mockCriteria.list()).thenReturn(getAuditRepositoryRecordList());
        QueryAuditEventsRequestType request = new QueryAuditEventsRequestType();
        request.setUserId("testUserId");
        QueryAuditEventsResponseType response = auditRetrieve.queryAuditEvents(request);
        assertEquals("Response Object size mismatch", response.getQueryAuditEventsResults().size(), 1);
        QueryAuditEventsResults obj = response.getQueryAuditEventsResults().get(0);
        assertEquals("QueryAuditEventsResults.Direction mismatch", obj.getDirection(), DIRECTION);
        assertEquals("QueryAuditEventsResults.RemoteHcid mismatch", obj.getRemoteHcid(), REMOTE_HCID);
        assertEquals("QueryAuditEventsResults.EventType mismatch", obj.getEventType(), EVENT_TYPE);
        assertEquals("QueryAuditEventsResults.getEventId mismatch", obj.getEventId(), EVENT_ID);
        assertEquals("QueryAuditEventsResults.RequestMessageId mismatch", obj.getRequestMessageId(), MESSAGE_ID);
        assertEquals("QueryAuditEventsResults.RelatesTo(mismatch", obj.getRelatesTo(), RELATES_TO);
        assertEquals("QueryAuditEventsResults.UserId mismatch", obj.getUserId(), USER_ID);
        assertEquals("QueryAuditEventsResults.id mismatch", obj.getId(), ID.longValue());
        assertEquals("QueryAuditEventsResults.EventTimestamp mismatch",
            obj.getEventTimestamp().toGregorianCalendar().getTime(), EVENT_TIMESTAMP);
    }

    private List<AuditRepositoryRecord> getAuditRepositoryRecordList() {
        List<AuditRepositoryRecord> list = new ArrayList<>();
        AuditRepositoryRecord dbRec = new AuditRepositoryRecord();
        dbRec.setDirection(DIRECTION);
        dbRec.setRemoteHcid(REMOTE_HCID);
        dbRec.setEventType(EVENT_TYPE);
        dbRec.setEventId(EVENT_ID);
        dbRec.setMessageId(MESSAGE_ID);
        dbRec.setRelatesTo(RELATES_TO);
        dbRec.setUserId(USER_ID);
        dbRec.setId(ID);
        dbRec.setEventTimestamp(EVENT_TIMESTAMP);
        list.add(dbRec);
        return list;
    }
}
