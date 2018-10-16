/*
 * Copyright (c) 2009-2018, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.audit.retrieve;

import static gov.hhs.fha.nhinc.util.CoreHelpUtils.getXMLGregorianCalendarFrom;

import com.services.nhinc.schema.auditmessage.AuditMessageType;
import gov.hhs.fha.nhinc.auditrepository.hibernate.AuditRepositoryRecord;
import gov.hhs.fha.nhinc.common.auditquerylog.QueryAuditEventsBlobResponse;
import gov.hhs.fha.nhinc.common.auditquerylog.QueryAuditEventsResponseType;
import gov.hhs.fha.nhinc.common.auditquerylog.QueryAuditEventsResults;
import gov.hhs.fha.nhinc.transform.marshallers.JAXBContextHandler;
import gov.hhs.fha.nhinc.util.JAXBUnmarshallingUtil;
import gov.hhs.fha.nhinc.util.StreamUtils;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLStreamException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author tjafri
 */
public class AuditRetrieveEventsUtil {

    private static final Logger LOG = LoggerFactory.getLogger(AuditRetrieveEventsUtil.class);

    private AuditRetrieveEventsUtil() {
    }

    public static QueryAuditEventsResponseType getQueryAuditEventResponse(List<AuditRepositoryRecord> dbRecords) {
        QueryAuditEventsResponseType response = new QueryAuditEventsResponseType();
        if (dbRecords != null) {
            List<QueryAuditEventsResults> auditList = new ArrayList<>();
            QueryAuditEventsResults auditObj;
            for (AuditRepositoryRecord rec : dbRecords) {
                auditObj = new QueryAuditEventsResults();
                auditObj.setDirection(rec.getDirection());
                auditObj.setEventId(rec.getEventId());
                auditObj.setEventTimestamp(getXMLGregorianCalendarFrom(rec.getEventTimestamp()));
                auditObj.setEventType(rec.getEventType());
                auditObj.setId(rec.getId());
                auditObj.setRelatesTo(rec.getRelatesTo());
                auditObj.setRemoteHcid(rec.getRemoteHcid());
                auditObj.setRequestMessageId(rec.getMessageId());
                auditObj.setUserId(rec.getUserId());
                auditList.add(auditObj);
            }
            response.getQueryAuditEventsResults().addAll(auditList);
        }
        return response;
    }

    public static QueryAuditEventsBlobResponse getQueryAuditEventBlobResponse(Blob auditBlob) {
        QueryAuditEventsBlobResponse response = new QueryAuditEventsBlobResponse();
        if (auditBlob != null) {
            response.setAuditMessage(unmarshallBlobToAuditMsg(auditBlob));
        }
        return response;
    }

    private static AuditMessageType unmarshallBlobToAuditMsg(Blob auditBlob) {

        AuditMessageType auditMessageType = null;
        InputStream in = null;
        try {
            if (auditBlob != null && (int) auditBlob.length() > 0) {
                JAXBUnmarshallingUtil util = new JAXBUnmarshallingUtil();
                in = auditBlob.getBinaryStream();
                JAXBContextHandler oHandler = new JAXBContextHandler();
                JAXBContext jc = oHandler.getJAXBContext("com.services.nhinc.schema.auditmessage");
                Unmarshaller unmarshaller = jc.createUnmarshaller();
                JAXBElement jaxEle = (JAXBElement) unmarshaller.unmarshal(util.getSafeStreamReaderFromInputStream(in));
                auditMessageType = (AuditMessageType) jaxEle.getValue();
            }
        } catch (SQLException | JAXBException | XMLStreamException e) {
            LOG.error("Blob to Audit Message Conversion Error : " + e.getLocalizedMessage(), e);
        } finally {
            StreamUtils.closeStreamSilently(in);
        }

        return auditMessageType;
    }
}
