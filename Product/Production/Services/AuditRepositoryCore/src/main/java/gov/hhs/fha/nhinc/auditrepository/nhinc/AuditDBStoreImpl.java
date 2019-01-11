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

import com.services.nhinc.schema.auditmessage.AuditMessageType;
import com.services.nhinc.schema.auditmessage.ObjectFactory;
import gov.hhs.fha.nhinc.auditrepository.hibernate.AuditRepositoryDAO;
import gov.hhs.fha.nhinc.auditrepository.hibernate.AuditRepositoryRecord;
import gov.hhs.fha.nhinc.auditrepository.hibernate.util.HibernateUtil;
import gov.hhs.fha.nhinc.auditrepository.hibernate.util.HibernateUtilFactory;
import gov.hhs.fha.nhinc.common.auditlog.LogEventSecureRequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.transform.marshallers.JAXBContextHandler;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.XMLGregorianCalendar;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author tjafri
 */
public class AuditDBStoreImpl implements AuditStore {

    private static final Logger LOG = LoggerFactory.getLogger(AuditDBStoreImpl.class);
    private static final AuditRepositoryDAO auditLogDao = new AuditRepositoryDAO();

    @Override
    public boolean saveAuditRecord(LogEventSecureRequestType request, AssertionType assertion) {
        List<AuditRepositoryRecord> auditRecList = new ArrayList<>();
        auditRecList.add(createDBAuditObj(request, assertion));
        LOG.trace("AuditRepositoryOrchImpl.logAudit() -- Calling auditLogDao to insert record into database.");
        return auditLogDao.insertAuditRepository(auditRecList);
    }

    protected final AuditRepositoryRecord createDBAuditObj(LogEventSecureRequestType mess, AssertionType assertion) {
        AuditRepositoryRecord auditRec = new AuditRepositoryRecord();
        String eventCommunityId = mess.getRemoteHCID();
        auditRec.setRemoteHcid(eventCommunityId);

        auditRec.setDirection(mess.getDirection());
        auditRec.setMessage(getBlobFromAuditMessage(mess.getAuditMessage()));

        XMLGregorianCalendar xMLCalDate = mess.getEventTimestamp();
        if (xMLCalDate != null) {
            auditRec.setEventTimestamp(convertXMLGregorianCalendarToDate(xMLCalDate));
        }

        auditRec.setEventType(mess.getEventType());
        auditRec.setEventId(mess.getEventID());
        auditRec.setMessageId(mess.getRequestMessageId());
        auditRec.setRelatesTo(mess.getRelatesTo());
        auditRec.setUserId(mess.getUserId());

        return auditRec;
    }

    protected Blob getBlobFromAuditMessage(AuditMessageType mess) {
        Blob eventMessage = null;
        Session session = null;
        try {
            JAXBContextHandler oHandler = new JAXBContextHandler();
            JAXBContext jc = oHandler.getJAXBContext("com.services.nhinc.schema.auditmessage");
            Marshaller marshaller = jc.createMarshaller();
            ByteArrayOutputStream baOutStrm = new ByteArrayOutputStream();
            baOutStrm.reset();
            ObjectFactory factory = new ObjectFactory();
            JAXBElement<AuditMessageType> oJaxbElement = factory.createAuditMessage(mess);
            baOutStrm.close();
            marshaller.marshal(oJaxbElement, baOutStrm);
            byte[] buffer = baOutStrm.toByteArray();
            session = getHibernateUtil().getSessionFactory().openSession();
            eventMessage = session.getLobHelper().createBlob(buffer);
        } catch (JAXBException | IOException e) {
            LOG.error("Exception during Blob conversion : {}", e.getLocalizedMessage(), e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return eventMessage;
    }

    /**
     * This method converts an XMLGregorianCalendar date to java.util.Date
     *
     * @param xmlCalDate
     * @return java.util.Date
     */
    private Date convertXMLGregorianCalendarToDate(XMLGregorianCalendar xmlCalDate) {
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        LOG.info("cal.getTime() -> " + cal.getTime());
        cal.setTime(xmlCalDate.toGregorianCalendar().getTime());
        Date eventDate = cal.getTime();
        LOG.info("eventDate -> " + eventDate);
        return eventDate;
    }

    /**
     * Load HibernateUtil bean.
     *
     * @return hibernateUtil
     */
    private static HibernateUtil getHibernateUtil() {
        return HibernateUtilFactory.getAuditRepoHibernateUtil();
    }

}
