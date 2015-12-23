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
package gov.hhs.fha.nhinc.admingui.services.impl;

import gov.hhs.fha.nhinc.admingui.event.model.Audit;
import gov.hhs.fha.nhinc.admingui.services.AuditService;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author achidamb
 */
public class AuditServiceImpl implements AuditService {

    private static final Logger LOG = LoggerFactory.getLogger(AuditServiceImpl.class);

    @Override
    public ArrayList<Audit> createMockAuditRecord() {
        Audit audit1 = new Audit();
        audit1.setEventOutcomeIndicator("Success");
        audit1.setEventType("QueryForDocuments");
        audit1.setMessageId("MessageId-1");
        audit1.setRemoteHcid("2.2");
        audit1.setUserId("Thomas");

        Audit audit2 = new Audit();
        audit2.setEventOutcomeIndicator("Success");
        audit2.setEventType("RetrieveDocuments");
        audit2.setMessageId("MessageId-2");
        audit2.setRemoteHcid("3.3");
        audit2.setUserId("Joe");
        try {
            audit1.setEventTimestamp(getTimeStamp());
            audit2.setEventTimestamp(getTimeStamp());
        } catch (ParseException ex) {
            LOG.error("Timestamp  formatting Exception: " + ex.getLocalizedMessage(), ex);
        }
        ArrayList<Audit> auditRecord = new ArrayList<>();
        auditRecord.add(audit1);
        auditRecord.add(audit2);
        return auditRecord;
    }

    private Timestamp getTimeStamp() throws ParseException {
        Date date = new Date();
        return new Timestamp(date.getTime());
    }

    public String createMockAuditMessage(long id) {
        Audit audit = new Audit();
        audit.setMessage("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><AuditMessage xmlns=\"http://nhinc.services.com/schema/auditmessage\"><EventIdentification EventActionCode=\"C\" EventDateTime=\"2015-12-17T00:01:34.113Z\" EventOutcomeIndicator=\"0\"><EventID code=\"110107\" displayName=\"Import\" codeSystemName=\"DCM\"/><EventTypeCode code=\"ITI-41\" displayName=\"Provide and Register Document Set-b\" codeSystemName=\"IHE Transactions\"/></EventIdentification><ActiveParticipant UserID=\"http://www.w3.org/2005/08/addressing/anonymous\" UserName=\"Karl Skagerberg\" UserIsRequestor=\"true\" NetworkAccessPointID=\"127.0.0.1\" NetworkAccessPointTypeCode=\"2\"><RoleIDCode code=\"110153\" displayName=\"Source\" codeSystemName=\"DCM\"/></ActiveParticipant><ActiveParticipant UserID=\"https://localhost:8181/Gateway/DocumentSubmission/2_0/NhinService/XDRRequest_Service\" AlternativeUserID=\"6636@VIMEHTA-F05773\" UserIsRequestor=\"false\" NetworkAccessPointID=\"localhost\" NetworkAccessPointTypeCode=\"1\"><RoleIDCode code=\"110152\" displayName=\"Destination\" codeSystemName=\"DCM\"/></ActiveParticipant><AuditSourceIdentification AuditEnterpriseSiteID=\"DoD\" AuditSourceID=\"urn:oid:1.1\"/><ParticipantObjectIdentification ParticipantObjectID=\"SELF-5^^^&amp;1.3.6.1.4.1.21367.2005.3.7&amp;ISO\" ParticipantObjectTypeCode=\"1\" ParticipantObjectTypeCodeRole=\"1\"><ParticipantObjectIDTypeCode code=\"2\" displayName=\"Patient Number\" codeSystemName=\"RFC-3881\"/></ParticipantObjectIdentification><ParticipantObjectIdentification ParticipantObjectID=\"1.3.6.1.4.1.21367.2005.3.9999.33\" ParticipantObjectTypeCode=\"2\" ParticipantObjectTypeCodeRole=\"20\"><ParticipantObjectIDTypeCode code=\"urn:uuid:a54d6aa5-d40d-43f9-88c5-b4633d873bdd\" displayName=\"submission set classificationNode\" codeSystemName=\"IHE XDS Metadata\"/></ParticipantObjectIdentification></AuditMessage>");

        return audit.getMessage();
    }
}
