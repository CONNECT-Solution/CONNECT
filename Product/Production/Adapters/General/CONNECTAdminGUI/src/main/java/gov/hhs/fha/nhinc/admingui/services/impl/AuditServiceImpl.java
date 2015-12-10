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
import org.slf4j.LoggerFactory;

/**
 *
 * @author achidamb
 */
public class AuditServiceImpl implements AuditService {

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(AuditServiceImpl.class);

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

}
