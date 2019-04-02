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
package gov.hhs.fha.nhinc.auditrepository.nhinc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

import com.services.nhinc.schema.auditmessage.AuditMessageType;
import com.services.nhinc.schema.auditmessage.EventIdentificationType;
import gov.hhs.fha.nhinc.auditrepository.hibernate.AuditRepositoryRecord;
import gov.hhs.fha.nhinc.common.auditlog.LogEventSecureRequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.UserType;
import gov.hhs.fha.nhinc.transform.audit.AuditDataTransformHelper;
import java.sql.Blob;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author tjafri
 */
public class AuditDBStoreImplTest {

    private final String EVENT_TYPE = "TestService";
    private final String EVENT_ID_CODE = "110112";
    private final String EVENT_ID_CODE_SYS_NAME = "DCM";
    private final String EVENT_ID_CODE_DISP_NAME = "Query";
    private final String DIRECTION = "Outbound";
    private final String EVENT_ACTION_CODE = "R";
    private final String USER_NAME = "testUser";
    private final String RELATES_TO_1 = "val1";
    private final String RELATES_TO_2 = "val2";
    private final String MESSAGE_ID = "MessageId";

    @Before
    public void setup() {
        System.setProperty("nhinc.properties.dir", ".//src//test//resources");
    }

    @Test
    public void testCreateDBAuditObj() {

        AuditDBStoreImpl dbStore = new AuditDBStoreImpl() {
            @Override
            protected Blob getBlobFromAuditMessage(AuditMessageType mess) {
                return mock(Blob.class);
            }
        };
        AssertionType assertion = createAssertion();
        LogEventSecureRequestType auditObj = createLogEventSecureObj(assertion);
        AuditRepositoryRecord dbRec = dbStore.createDBAuditObj(auditObj, assertion);
        assertEquals("AuditRepositoryRecord.Direction mismatch", dbRec.getDirection(), auditObj.getDirection());
        assertEquals("AuditRepositoryRecord.EventType mismatch", dbRec.getEventType(), EVENT_TYPE);
        assertEquals("AuditRepositoryRecord.EventId mismatch", dbRec.getEventId(), EVENT_ID_CODE_DISP_NAME);
        assertEquals("AuditRepositoryRecord.RelatesTo", dbRec.getRelatesTo(), RELATES_TO_1);
        assertNotNull("AuditRepositoryRecord.MessageId", dbRec.getMessageId());
    }

    private LogEventSecureRequestType createLogEventSecureObj(AssertionType assertion) {
        LogEventSecureRequestType logObj = new LogEventSecureRequestType();
        AuditMessageType audit = createAuditMessageType();
        logObj.setEventType(EVENT_TYPE);
        logObj.setDirection(DIRECTION);
        logObj.setAuditMessage(audit);
        logObj.setEventID(audit.getEventIdentification().getEventID().getDisplayName());
        logObj.setEventOutcomeIndicator(audit.getEventIdentification().getEventOutcomeIndicator());
        logObj.setRelatesTo(assertion.getRelatesToList().get(0));
        logObj.setRequestMessageId(MESSAGE_ID);
        return logObj;
    }

    private AuditMessageType createAuditMessageType() {
        AuditMessageType auditObj = new AuditMessageType();
        auditObj.setEventIdentification(createEventIdentification());
        return auditObj;
    }

    private EventIdentificationType createEventIdentification() {
        return AuditDataTransformHelper.createEventIdentification(EVENT_ACTION_CODE, 0, AuditDataTransformHelper
                .createEventId(EVENT_ID_CODE, null, EVENT_ID_CODE_SYS_NAME, EVENT_ID_CODE_DISP_NAME));
    }

    private AssertionType createAssertion() {
        AssertionType assertion = new AssertionType();
        UserType user = new UserType();
        user.setUserName(USER_NAME);
        assertion.setUserInfo(user);
        assertion.getRelatesToList().add(RELATES_TO_1);
        assertion.getRelatesToList().add(RELATES_TO_2);
        return assertion;
    }

}
