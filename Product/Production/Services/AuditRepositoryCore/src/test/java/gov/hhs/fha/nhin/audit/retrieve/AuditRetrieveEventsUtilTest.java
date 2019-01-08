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
package gov.hhs.fha.nhin.audit.retrieve;

import static gov.hhs.fha.nhinc.audit.retrieve.AuditRetrieveEventsUtil.getQueryAuditEventBlobResponse;
import static gov.hhs.fha.nhinc.audit.retrieve.AuditRetrieveEventsUtil.getQueryAuditEventResponse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.services.nhinc.schema.auditmessage.AuditMessageType;
import com.services.nhinc.schema.auditmessage.ObjectFactory;
import gov.hhs.fha.nhinc.auditrepository.hibernate.AuditRepositoryRecord;
import gov.hhs.fha.nhinc.common.auditquerylog.QueryAuditEventsBlobResponse;
import gov.hhs.fha.nhinc.common.auditquerylog.QueryAuditEventsResponseType;
import gov.hhs.fha.nhinc.common.auditquerylog.QueryAuditEventsResults;
import gov.hhs.fha.nhinc.transform.marshallers.JAXBContextHandler;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author tjafri
 */
public class AuditRetrieveEventsUtilTest {

    private static final Logger LOG = LoggerFactory.getLogger(AuditRetrieveEventsUtilTest.class);
    private final String DIRECTION = "Outbound";
    private final String REMOTE_HCID = "2.2";
    private final String EVENT_TYPE = "patientDiscovery";
    private final String EVENT_ID = "Query";
    private final String MESSAGE_ID = "urn:uuid:4abc7cc8-1edc-409d-b061-bf65b21f7b1e";
    private final String RELATES_TO = "urn:uuid:4abc7cc8-1edc-409d-b061-bf65b21f7b1e";
    private final String USER_ID = "wanderson";
    private final Long ID = 1L;
    private final Date EVENT_TIMESTAMP = Calendar.getInstance().getTime();
    private final String AUDIT_MESSAGE = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
        + "<AuditMessage xmlns=\"http://nhinc.services.com/schema/auditmessage\">"
        + "<EventIdentification EventActionCode=\"E\" EventDateTime=\"2015-12-15T15:41:29.678Z\" EventOutcomeIndicator=\"0\">"
        + "<EventID code=\"110112\" displayName=\"Query\" codeSystemName=\"DCM\"/>"
        + "<EventTypeCode code=\"ITI-55\" displayName=\"Cross Gateway Patient Discovery\" codeSystemName=\"IHE Transactions\"/>"
        + "</EventIdentification>"
        + "<ActiveParticipant UserID=\"wanderson\" UserName=\"Wilma Anderson\" UserIsRequestor=\"true\">"
        + "<RoleIDCode code=\"307969004\" displayName=\"Public Health\" codeSystemName=\"SNOMED_CT\"/></ActiveParticipant>"
        + "<ActiveParticipant UserID=\"http://www.w3.org/2005/08/addressing/anonymous\" AlternativeUserID=\"6412@TJAFRI-F08051\" UserName=\"Wilma Anderson\" UserIsRequestor=\"true\" NetworkAccessPointID=\"192.168.1.155\" NetworkAccessPointTypeCode=\"2\">"
        + "<RoleIDCode code=\"110153\" displayName=\"Source\" codeSystemName=\"DCM\"/></ActiveParticipant>"
        + "<ActiveParticipant UserID=\"https://localhost:8181/Gateway/PatientDiscovery/1_0/NhinService/NhinPatientDiscovery\" UserIsRequestor=\"false\" NetworkAccessPointID=\"localhost\" NetworkAccessPointTypeCode=\"1\">"
        + "<RoleIDCode code=\"110152\" displayName=\"Destination\" codeSystemName=\"DCM\"/></ActiveParticipant>"
        + "<AuditSourceIdentification AuditEnterpriseSiteID=\"DoD\" AuditSourceID=\"urn:oid:1.1\"/>"
        + "<ParticipantObjectIdentification ParticipantObjectID=\"D123401^^^&amp;1.1&amp;ISO\" ParticipantObjectTypeCode=\"1\" ParticipantObjectTypeCodeRole=\"1\">"
        + "<ParticipantObjectIDTypeCode code=\"2\" displayName=\"Patient Number\" codeSystemName=\"RFC-3881\"/>"
        + "</ParticipantObjectIdentification>"
        + "<ParticipantObjectIdentification ParticipantObjectID=\"-abd3453dcd24wkkks545\" ParticipantObjectTypeCode=\"2\" ParticipantObjectTypeCodeRole=\"24\">"
        + "<ParticipantObjectIDTypeCode code=\"ITI-55\" displayName=\"Cross Gateway Patient Discovery\" codeSystemName=\"IHE Transactions\"/>"
        + "<ParticipantObjectQuery>PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiIHN0YW5kYWxvbmU9InllcyI/PjxxdWVyeUJ5UGFyYW1ldGVyIHhtbG5zPSJ1cm46aGw3LW9yZzp2MyIgeG1sbnM6bnMyPSJ1cm46aGw3LW9yZzpzZHRjIiB4bWxuczpuczM9InVybjpnb3Y6aGhzOmZoYTpuaGluYzpjb21tb246bmhpbmNjb21tb24iIHhtbG5zOm5zND0idXJuOmdvdjpoaHM6ZmhhOm5oaW5jOmNvbW1vbjpwYXRpZW50Y29ycmVsYXRpb25mYWNhZGUiIHhtbG5zOm5zNT0iaHR0cDovL3d3dy53My5vcmcvMjAwNS8wOC9hZGRyZXNzaW5nIj48cXVlcnlJZCByb290PSJ1cm46b2lkOjEuMSIgZXh0ZW5zaW9uPSItYWJkMzQ1M2RjZDI0d2tra3M1NDUiLz48c3RhdHVzQ29kZSBjb2RlPSJuZXciLz48cmVzcG9uc2VNb2RhbGl0eUNvZGUgY29kZT0iUiIvPjxyZXNwb25zZVByaW9yaXR5Q29kZSBjb2RlPSJJIi8+PG1hdGNoQ3JpdGVyaW9uTGlzdD48bWF0Y2hBbGdvcml0aG0+PHZhbHVlPiJYWVogTWF0Y2hBbGdvcml0aG0iPC92YWx1ZT48c2VtYW50aWNzVGV4dD5NYXRjaEFsZ29yaXRobTwvc2VtYW50aWNzVGV4dD48L21hdGNoQWxnb3JpdGhtPjxtaW5pbXVtRGVncmVlTWF0Y2g+PHZhbHVlIHZhbHVlPSI5OSIvPjxzZW1hbnRpY3NUZXh0Pk1pbmltdW1EZWdyZWVNYXRjaDwvc2VtYW50aWNzVGV4dD48L21pbmltdW1EZWdyZWVNYXRjaD48L21hdGNoQ3JpdGVyaW9uTGlzdD48cGFyYW1ldGVyTGlzdD48bGl2aW5nU3ViamVjdEFkbWluaXN0cmF0aXZlR2VuZGVyPjx2YWx1ZSBjb2RlPSJNIi8+PHNlbWFudGljc1RleHQgcmVwcmVzZW50YXRpb249IlRYVCI+TGl2aW5nU3ViamVjdC5hZG1pbmlzdHJhdGl2ZUdlbmRlcjwvc2VtYW50aWNzVGV4dD48L2xpdmluZ1N1YmplY3RBZG1pbmlzdHJhdGl2ZUdlbmRlcj48bGl2aW5nU3ViamVjdEJpcnRoVGltZT48dmFsdWUgdmFsdWU9IjE5NjMwODA0Ii8+PHNlbWFudGljc1RleHQgcmVwcmVzZW50YXRpb249IlRYVCI+TGl2aW5nU3ViamVjdC5iaXJ0aFRpbWU8L3NlbWFudGljc1RleHQ+PC9saXZpbmdTdWJqZWN0QmlydGhUaW1lPjxsaXZpbmdTdWJqZWN0SWQ+PHZhbHVlIHJvb3Q9IjEuMSIgZXh0ZW5zaW9uPSJEMTIzNDAxIi8+PHNlbWFudGljc1RleHQgcmVwcmVzZW50YXRpb249IlRYVCIvPjwvbGl2aW5nU3ViamVjdElkPjxsaXZpbmdTdWJqZWN0TmFtZT48dmFsdWU+PGZhbWlseSBwYXJ0VHlwZT0iRkFNIj5Zb3VuZ2VyPC9mYW1pbHk+PGdpdmVuIHBhcnRUeXBlPSJHSVYiPkdhbGxvdzwvZ2l2ZW4+CiAgICAgICAgICAgICAgICAgICAgICAgIDwvdmFsdWU+PHNlbWFudGljc1RleHQgcmVwcmVzZW50YXRpb249IlRYVCI+TGl2aW5nU3ViamVjdC5uYW1lPC9zZW1hbnRpY3NUZXh0PjwvbGl2aW5nU3ViamVjdE5hbWU+PC9wYXJhbWV0ZXJMaXN0PjwvcXVlcnlCeVBhcmFtZXRlcj4=</ParticipantObjectQuery>"
        + "</ParticipantObjectIdentification></AuditMessage>";

    @Before
    public void setup() {
        System.setProperty("nhinc.properties.dir", ".//src//test//resources");
    }

    @Test
    public void testGetQueryAuditEventResponse() {
        QueryAuditEventsResponseType response = getQueryAuditEventResponse(createAuditRepositoryRecord());
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

    @Test
    public void testGetQueryAuditEventResponseWithNoData() {
        QueryAuditEventsResponseType response = getQueryAuditEventResponse(new ArrayList<AuditRepositoryRecord>());
        assertTrue("Response Object is not empty", response.getQueryAuditEventsResults().isEmpty());
        response = getQueryAuditEventResponse(null);
        assertTrue("Response Object is not empty", response.getQueryAuditEventsResults().isEmpty());
    }

    @Test
    public void testGetQueryAuditEventResponseWithMultipleResults() {
        List<AuditRepositoryRecord> dbList = createAuditRepositoryRecord();
        dbList.addAll(createAuditRepositoryRecord());
        QueryAuditEventsResponseType response = getQueryAuditEventResponse(dbList);
        assertEquals("Response Object size mismatch", response.getQueryAuditEventsResults().size(), 2);
    }

    @Test
    public void testGetQueryAuditEventBlobResponse() {
        /*
         * Use a mock Blob object here to avoid using HibernateUtil. There are issues with using HibernateUtil for test
         * /* classes during the CI jobs.
         */
        Blob mockBlob = mock(Blob.class);
        try {
            when(mockBlob.length()).thenReturn(new Long("30"));
            when(mockBlob.getBinaryStream()).thenReturn(new ByteArrayInputStream(AUDIT_MESSAGE.getBytes()));
        } catch (NumberFormatException | SQLException e) {
            LOG.error("Exception during mock setup for blob : {}", e.getLocalizedMessage(), e);
        }
        QueryAuditEventsBlobResponse response = getQueryAuditEventBlobResponse(mockBlob);
        AuditMessageType audit = response.getAuditMessage();
        assertNotNull("Audit Blob mismatch", getAuditString(audit));
        assertEquals("Audit Blob mismatch", getAuditString(audit), AUDIT_MESSAGE);
    }

    private List<AuditRepositoryRecord> createAuditRepositoryRecord() {
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

    private static String getAuditString(AuditMessageType request) {
        try {
            JAXBContextHandler oHandler = new JAXBContextHandler();
            JAXBContext jc = oHandler.getJAXBContext("com.services.nhinc.schema.auditmessage");
            Marshaller marshaller = jc.createMarshaller();

            ByteArrayOutputStream baOutStrm = new ByteArrayOutputStream();
            baOutStrm.reset();
            ObjectFactory factory = new ObjectFactory();
            JAXBElement<AuditMessageType> oJaxbElement = factory.createAuditMessage(request);
            baOutStrm.close();
            marshaller.marshal(oJaxbElement, baOutStrm);
            return new String(baOutStrm.toByteArray());
        } catch (JAXBException | IOException e) {
            LOG.error("Exception during Blob conversion : {}", e.getLocalizedMessage(), e);
        }
        return null;
    }
}
