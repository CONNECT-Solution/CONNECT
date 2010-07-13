/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.adapterauditquery.proxy;

import com.services.nhinc.schema.auditmessage.FindAuditEventsResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.FindAuditEventsRequestType;

/**
 *
 * @author Jon Hoppesch
 */
public class AdapterAuditQueryNoOpImpl implements AdapterAuditQueryProxy {

    public FindAuditEventsResponseType auditQuery(FindAuditEventsRequestType request) {
        return new FindAuditEventsResponseType();
    }

}
