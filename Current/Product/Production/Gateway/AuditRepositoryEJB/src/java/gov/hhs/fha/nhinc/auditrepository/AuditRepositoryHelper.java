/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.auditrepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.FindCommunitiesAndAuditEventsRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.FindCommunitiesAndAuditEventsResponseType;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;

/**
 *
 * @author mflynn02
 */
public class AuditRepositoryHelper {
    private static Log log = LogFactory.getLog(AuditRepositoryHelper.class);
        
    public static FindCommunitiesAndAuditEventsResponseType queryAuditEvents(FindCommunitiesAndAuditEventsRequestType queryAuditEventsRequest) {
        
        FindCommunitiesAndAuditEventsResponseType response = new FindCommunitiesAndAuditEventsResponseType();
        
        // Create an instance of the DAO and query the Audit Respository
        response = AuditRepositoryImpl.findAudit(queryAuditEventsRequest.getFindAuditEvents());
        
        return response;
    }
    
    public static AcknowledgementType logEvent(LogEventRequestType logEventRequest)
    {
        AcknowledgementType acknowledgement = new AcknowledgementType();
        
        acknowledgement = AuditRepositoryImpl.logAudit(logEventRequest);
        
        return acknowledgement;
    }
}