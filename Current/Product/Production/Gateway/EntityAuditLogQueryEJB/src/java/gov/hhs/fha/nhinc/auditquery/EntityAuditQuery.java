/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.auditquery;

import gov.hhs.fha.nhinc.auditrepository.proxy.AuditRepositoryProxy;
import gov.hhs.fha.nhinc.auditrepository.proxy.AuditRepositoryProxyObjectFactory;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.FindCommunitiesAndAuditEventsRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.FindCommunitiesAndAuditEventsResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.FindAuditEventsRequestType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Jon Hoppesch
 */
public class EntityAuditQuery {

    private static Log log = LogFactory.getLog(EntityAuditQuery.class);

    /**
     * This method will query the audit log repository at a specified home community for audit
     * log records that match a specified criteria.
     *
     * @param auditQueryReq The search criteria for the audit log query
     * @param homeCommunityId The home community to perform an audit log query to.
     * @return A list of audit log records that match the specified criteria along with a list of referenced
     * communities from these records.
     */
    public FindCommunitiesAndAuditEventsResponseType query(FindAuditEventsRequestType auditQueryReq) {
        log.debug("Entering EntityAuditQuery.query...");
        
        FindCommunitiesAndAuditEventsRequestType request = new FindCommunitiesAndAuditEventsRequestType();
        request.setAssertion(auditQueryReq.getAssertion());
        request.setFindAuditEvents(auditQueryReq.getFindAuditEvents());

        AuditRepositoryProxyObjectFactory auditRepoFactory = new AuditRepositoryProxyObjectFactory();
        AuditRepositoryProxy proxy = auditRepoFactory.getAuditRepositoryProxy();

        log.debug("Exiting EntityAuditQuery.query...");
        return proxy.auditQuery(request);
    }

}
