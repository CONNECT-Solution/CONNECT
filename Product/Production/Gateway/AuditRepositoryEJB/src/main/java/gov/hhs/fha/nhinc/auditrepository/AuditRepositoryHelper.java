/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.auditrepository;
import gov.hhs.fha.nhinc.common.auditlog.LogEventSecureRequestType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.FindCommunitiesAndAuditEventsRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.FindCommunitiesAndAuditEventsResponseType;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author mflynn02
 */
public class AuditRepositoryHelper {
    private static Log log = LogFactory.getLog(AuditRepositoryHelper.class);
        
    public static FindCommunitiesAndAuditEventsResponseType queryAuditEvents(FindCommunitiesAndAuditEventsRequestType queryAuditEventsRequest, WebServiceContext context) {
        
        FindCommunitiesAndAuditEventsResponseType response = new FindCommunitiesAndAuditEventsResponseType();
        
        // Create an instance of the DAO and query the Audit Respository
        response = new AuditRepositoryImpl().findAudit(queryAuditEventsRequest.getFindAuditEvents(), context);
        
        return response;
    }
    
    public static AcknowledgementType logEvent(LogEventSecureRequestType logEventRequest, WebServiceContext context)
    {
        log.debug("Entering AuditRepositoryHelper.logEvent method. ");
        AcknowledgementType acknowledgement = new AcknowledgementType();
        
        acknowledgement = new AuditRepositoryImpl().logAudit(logEventRequest, context);

        log.debug("Exiting AuditRepositoryHelper.logEvent method. ");
        
        return acknowledgement;
    }
}