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

import gov.hhs.fha.nhinc.docrepository.adapter.model.EventCode;
import gov.hhs.fha.nhinc.docrepository.adapter.model.EventCodeParam;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Before;
import org.junit.Test;

/**
 * The Class EventCodeDaoTest.
 *
 * @author unknown, msw
 */
public class EventCodeDaoTest {

    private final File file = mock(File.class);
    /** The session. */
    private final Session session = mock(Session.class);

    /** The transaction. */
    private Transaction transaction;

    /** The event code dao. */
    private EventCodeDao eventCodeDao;

    /**
     * Sets the up.
     */
    @Before
    public void setUp() {
        transaction = mock(Transaction.class);

        eventCodeDao = new EventCodeDao() {
            @Override
            protected Session getSession() {
                return session;
            }

            @Override
            protected List<Long> getDocumentIds(List<EventCode> eventCodes) {
                List<Long> DocumentIds = new ArrayList<>();
                return DocumentIds;
            }

        };

        when(session.beginTransaction()).thenReturn(transaction);
    }

    /**
     * Test delete.
     */
    @Test
    public void testDelete() {
        EventCode eventCode = new EventCode();

        eventCodeDao.delete(eventCode);

        verify(session).delete(eventCode);
    }

    /**
     * Test event code query.
     */
    @Test
    public void testEventCodeQuery() {
        List<EventCode> eventCodeList = new ArrayList<>();
        EventCode eventCode = new EventCode();
        final long EVENT_CODE_ID = 12345;
        eventCode.setEventCodeId(EVENT_CODE_ID);
        eventCodeList.add(eventCode);
        EventCodeParam eventCodeParam = new EventCodeParam();
        eventCodeParam.setEventCode("Event Code");
        eventCodeParam.setEventCodeScheme("Event Code Scheme");
        List<SlotType1> slots = null;
        Criteria criteria = mock(Criteria.class);

        when(session.createCriteria(EventCode.class)).thenReturn(criteria);
        when(criteria.list()).thenReturn(eventCodeList);

        List<EventCode> resultEventCodes = eventCodeDao.eventCodeQuery(slots);

        assertEquals(resultEventCodes.size(), 1);
        assertEquals(resultEventCodes.get(0).getEventCodeId(), (Long) EVENT_CODE_ID);

    }
}
