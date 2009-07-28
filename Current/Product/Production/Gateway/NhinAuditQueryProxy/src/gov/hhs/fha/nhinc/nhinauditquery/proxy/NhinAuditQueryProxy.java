/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.nhinauditquery.proxy;

import com.services.nhinc.schema.auditmessage.FindAuditEventsResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.FindAuditEventsRequestType;

/**
 *
 * @author Jon Hoppesch
 */
public interface NhinAuditQueryProxy {

    /**
     * Performs a query to the audit repository.
     *
     * @param request Audit query search criteria.
     * @return List of Audit records that match the search criteria along with a list of referenced communities.
     */
    public FindAuditEventsResponseType auditQuery(FindAuditEventsRequestType request);
}
