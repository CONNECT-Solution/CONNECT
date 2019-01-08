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
import com.services.nhinc.schema.auditmessage.FindAuditEventsResponseType;
import com.services.nhinc.schema.auditmessage.FindAuditEventsType;
import gov.hhs.fha.nhinc.auditrepository.hibernate.AuditRepositoryDAO;
import gov.hhs.fha.nhinc.auditrepository.hibernate.AuditRepositoryRecord;
import gov.hhs.fha.nhinc.auditrepository.util.AuditUtils;
import gov.hhs.fha.nhinc.common.auditlog.LogEventSecureRequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.FindCommunitiesAndAuditEventsResponseType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import static gov.hhs.fha.nhinc.util.CoreHelpUtils.getDate;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import javax.xml.datatype.XMLGregorianCalendar;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author mflynn02
 */
public class AuditRepositoryOrchImpl {

    private static final Logger LOG = LoggerFactory.getLogger(AuditRepositoryOrchImpl.class);
    private static final AuditRepositoryDAO auditLogDao = new AuditRepositoryDAO();

    private AuditStore dbStore = null;
    private AuditStore fileStore = null;

    /**
     * constructor.
     */
    public AuditRepositoryOrchImpl() {
        LOG.trace("AuditRepositoryOrchImpl Initialized");
        dbStore = new AuditDBStoreImpl();
        fileStore = new AuditFileStoreImpl();
    }

    /**
     * This method is the actual implementation method for AuditLogMgr Service to Log the AuditEvents and responses the
     * status of logging.
     *
     * @param mess the message
     * @param assertion the assertion
     * @return AcknowledgementType
     */
    public AcknowledgementType logAudit(LogEventSecureRequestType mess, AssertionType assertion) {

        AcknowledgementType response = new AcknowledgementType();
        StringBuilder builder = new StringBuilder();
        if (isLoggingToDatabaseOn()) {
            builder.append(logToDatabase(mess, assertion));
        }
        if (isLoggingToAuditFileOn()) {
            builder.append(logToAuditFile(mess, assertion));
        }
        response.setMessage(builder.toString());
        return response;
    }

    /**
     * This is the actual implementation for AuditLogMgr Service for AuditQuery returns the AuditEventsReponse.
     *
     * @param query the query
     * @return the found FindAuditEventsResponseType
     */
    public FindCommunitiesAndAuditEventsResponseType findAudit(FindAuditEventsType query) {

        FindCommunitiesAndAuditEventsResponseType auditEvents;
        String patientId = query.getPatientId();
        String userId = query.getUserId();
        Date beginDate = null;
        Date endDate = null;
        XMLGregorianCalendar xmlBeginDate = query.getBeginDateTime();
        XMLGregorianCalendar xmlEndDate = query.getEndDateTime();

        if (xmlBeginDate != null) {
            beginDate = getDate(xmlBeginDate);
        }
        if (xmlEndDate != null) {
            endDate = getDate(xmlEndDate);
        }

        List<AuditRepositoryRecord> responseList = auditLogDao.queryAuditRepositoryOnCriteria(userId, patientId,
            beginDate, endDate);
        LOG.debug("after query call to logDAO.");
        LOG.debug("responseList is not NULL ");
        auditEvents = buildAuditReponseType(responseList);

        return auditEvents;
    }

    /**
     * This method builds the Actual Response from each of the EventLogList coming from Database.
     *
     * @param eventsList
     * @return CommunitiesAndFindAdutiEventResponse
     */
    private static FindCommunitiesAndAuditEventsResponseType buildAuditReponseType(
        List<AuditRepositoryRecord> auditRecList) {

        FindCommunitiesAndAuditEventsResponseType auditResType = new FindCommunitiesAndAuditEventsResponseType();
        FindAuditEventsResponseType response = new FindAuditEventsResponseType();
        AuditMessageType auditMessageType;
        Blob blobMessage;

        int size = auditRecList.size();
        AuditRepositoryRecord eachRecord;
        for (int i = 0; i < size; i++) {
            eachRecord = auditRecList.get(i);
            blobMessage = eachRecord.getMessage();
            if (blobMessage != null) {
                try {
                    auditMessageType = AuditUtils.unMarshallBlobToAuditMessage(blobMessage);
                    if (auditMessageType != null) {
                        response.getFindAuditEventsReturn().add(auditMessageType);

                        if (CollectionUtils.isNotEmpty(auditMessageType.getAuditSourceIdentification())
                            && auditMessageType.getAuditSourceIdentification().get(0) != null && StringUtils.isNotEmpty(
                            auditMessageType.getAuditSourceIdentification().get(0).getAuditSourceID())) {
                            String tempCommunity = auditMessageType.getAuditSourceIdentification().get(0)
                                .getAuditSourceID();
                            if (!auditResType.getCommunities().contains(tempCommunity)) {

                                auditResType.getCommunities().add(tempCommunity);
                                LOG.debug("Adding community: {}", tempCommunity);
                            }
                        }
                    }
                } finally {
                    try {
                        blobMessage.free();
                    } catch (SQLException e) {
                        LOG.error("Could not free Blob: {}", e.getLocalizedMessage(), e);
                    }
                }
            }
        }

        auditResType.setFindAuditEventResponse(response);
        return auditResType;
    }

    private String logToDatabase(LogEventSecureRequestType request, AssertionType assertion) {

        if (dbStore.saveAuditRecord(request, assertion)) {
            return "Created Log Message in Database...";
        }
        return "Unable to create Log Message in Database...";
    }

    private String logToAuditFile(LogEventSecureRequestType request, AssertionType assertion) {
        if (fileStore.saveAuditRecord(request, assertion)) {
            return "Created Log Message in Audit File...";
        }
        return "Unable to create Log Message in Audit File...";
    }

    protected boolean isLoggingToDatabaseOn() {
        try {
            return PropertyAccessor.getInstance().getPropertyBoolean(NhincConstants.AUDIT_LOGGING_PROPERTY_FILE,
                NhincConstants.LOG_TO_DATABASE);
        } catch (PropertyAccessException ex) {
            LOG.error("Unable to read the Audit logging property: {}", ex.getLocalizedMessage(), ex);
        }
        return false;
    }

    protected boolean isLoggingToAuditFileOn() {
        try {
            return PropertyAccessor.getInstance().getPropertyBoolean(NhincConstants.AUDIT_LOGGING_PROPERTY_FILE,
                NhincConstants.LOG_TO_FILE);
        } catch (PropertyAccessException ex) {
            LOG.error("Unable to read the Audit logging property: {}", ex.getLocalizedMessage(), ex);
        }
        return false;
    }
}
