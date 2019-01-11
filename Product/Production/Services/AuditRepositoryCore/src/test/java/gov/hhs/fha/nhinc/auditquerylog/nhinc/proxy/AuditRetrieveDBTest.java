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
package gov.hhs.fha.nhinc.auditquerylog.nhinc.proxy;

import static org.junit.Assert.assertNotNull;

import gov.hhs.fha.nhinc.asyncmsgs.persistence.HibernateUtil;
import gov.hhs.fha.nhinc.auditrepository.hibernate.AuditRepositoryDAO;
import gov.hhs.fha.nhinc.auditrepository.hibernate.AuditRepositoryRecord;
import gov.hhs.fha.nhinc.common.auditquerylog.EventTypeList;
import gov.hhs.fha.nhinc.common.auditquerylog.QueryAuditEventsBlobRequest;
import gov.hhs.fha.nhinc.common.auditquerylog.QueryAuditEventsBlobResponse;
import gov.hhs.fha.nhinc.common.auditquerylog.QueryAuditEventsRequestByRequestMessageId;
import gov.hhs.fha.nhinc.common.auditquerylog.QueryAuditEventsRequestType;
import gov.hhs.fha.nhinc.common.auditquerylog.QueryAuditEventsResponseType;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author tjafri
 */
@Ignore
public class AuditRetrieveDBTest {

    private static final Logger LOG = LoggerFactory.getLogger(AuditRetrieveDBTest.class);
    private final String DIRECTION = "Outbound";
    private final String REMOTE_HCID = "2.2";
    private final String EVENT_TYPE = "patientDiscovery";
    private final String EVENT_ID = "Query";
    private final String MESSAGE_ID = "urn:uuid:TestMEssageId";
    private final String RELATES_TO = "urn:uuid:TestRelatesToId";
    private final String USER_ID = "wanderson";
    private final Date EVENT_TIMESTAMP = Calendar.getInstance().getTime();
    private long ID;
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
        System.setProperty("nhinc.properties.dir", ".\\src\\test\\resources");
        AuditRepositoryDAO dao = new AuditRepositoryDAO();
        List<AuditRepositoryRecord> insertList = createAuditRepositoryRecord();
        dao.insertAuditRepository(insertList);
        ID = insertList.get(0).getId();
    }

    @Test
    public void testAuditRetrieveBasedOnUserId() {
        AuditQueryLogProxyJavaImpl auditImpl = new AuditQueryLogProxyJavaImpl();
        QueryAuditEventsRequestType request = new QueryAuditEventsRequestType();
        request.setUserId(USER_ID);
        QueryAuditEventsResponseType response = auditImpl.retrieveAudits(request);
        assertNotNull(response);
    }

    @Test
    public void testAuditRetrieveBasedOnEventOutcome() {
        AuditQueryLogProxyJavaImpl auditImpl = new AuditQueryLogProxyJavaImpl();
        QueryAuditEventsRequestType request = new QueryAuditEventsRequestType();
        request.setEventOutcomeIndicator(BigInteger.valueOf(0L));
        QueryAuditEventsResponseType response = auditImpl.retrieveAudits(request);
        assertNotNull(response);
    }

    @Test
    public void testAuditRetrieveBasedOnEventBeginDate() {
        AuditQueryLogProxyJavaImpl auditImpl = new AuditQueryLogProxyJavaImpl();
        QueryAuditEventsRequestType request = new QueryAuditEventsRequestType();
        request.setEventBeginDate(convertDateToXMLGregorianCalendar(EVENT_TIMESTAMP));
        QueryAuditEventsResponseType response = auditImpl.retrieveAudits(request);
        assertNotNull(response);
    }

    @Test
    public void testAuditRetrieveBasedOnEventTypes() {
        List<String> eventTypes = new ArrayList<>(Arrays.asList(EVENT_TYPE));
        AuditQueryLogProxyJavaImpl auditImpl = new AuditQueryLogProxyJavaImpl();
        QueryAuditEventsRequestType request = new QueryAuditEventsRequestType();
        EventTypeList events = new EventTypeList();
        events.getEventType().addAll(eventTypes);
        request.setEventTypeList(events);
        QueryAuditEventsResponseType response = auditImpl.retrieveAudits(request);
        assertNotNull(response);
    }

    @Test
    public void testAuditRetrieveBasedOnAuditId() {
        AuditQueryLogProxyJavaImpl auditImpl = new AuditQueryLogProxyJavaImpl();
        QueryAuditEventsBlobRequest request = new QueryAuditEventsBlobRequest();
        request.setId(ID);
        QueryAuditEventsBlobResponse blobResponse = auditImpl.retrieveAuditBlob(request);
        assertNotNull(blobResponse);
    }

    @Test
    public void testAuditRetrieveBasedOnMessageId() {
        AuditQueryLogProxyJavaImpl auditImpl = new AuditQueryLogProxyJavaImpl();
        QueryAuditEventsRequestByRequestMessageId request = new QueryAuditEventsRequestByRequestMessageId();
        request.setRequestMessageId(MESSAGE_ID);
        auditImpl.retrieveAuditsByMsgIdAndRelatesToId(request);
    }

    private List<AuditRepositoryRecord> createAuditRepositoryRecord() {
        List<AuditRepositoryRecord> list = new ArrayList<>();

        HibernateUtil hibernateUtil = new HibernateUtil();
        hibernateUtil.buildSessionFactory();

        AuditRepositoryRecord dbRec = new AuditRepositoryRecord();
        dbRec.setDirection(DIRECTION);
        dbRec.setRemoteHcid(REMOTE_HCID);
        dbRec.setEventType(EVENT_TYPE);
        dbRec.setEventId(EVENT_ID);
        dbRec.setMessageId(MESSAGE_ID);
        dbRec.setRelatesTo(RELATES_TO);
        dbRec.setUserId(USER_ID);
        dbRec.setEventTimestamp(EVENT_TIMESTAMP);
        // dbRec.setMessage(Hibernate.createBlob(AUDIT_MESSAGE.getBytes()));
        dbRec.setMessage(
                hibernateUtil.getSessionFactory().openSession().getLobHelper().createBlob(AUDIT_MESSAGE.getBytes()));
        list.add(dbRec);
        return list;
    }

    private XMLGregorianCalendar convertDateToXMLGregorianCalendar(Date date) {
        if (date != null) {
            GregorianCalendar gregorianCalendar = new GregorianCalendar();
            gregorianCalendar.setTime(date);
            try {
                return DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
            } catch (DatatypeConfigurationException ex) {
                LOG.error("Unable to convert date: {}", ex.getLocalizedMessage(), ex);
                return null;
            }
        }
        return null;
    }

}
