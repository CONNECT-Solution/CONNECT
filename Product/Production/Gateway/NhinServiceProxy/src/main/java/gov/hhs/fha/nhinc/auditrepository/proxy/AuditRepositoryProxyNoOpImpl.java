/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.auditrepository.proxy;

import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.FindCommunitiesAndAuditEventsRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.FindCommunitiesAndAuditEventsResponseType;

/**
 *
 * @author jhoppesc
 */
public class AuditRepositoryProxyNoOpImpl implements AuditRepositoryProxy {

    public FindCommunitiesAndAuditEventsResponseType auditQuery(FindCommunitiesAndAuditEventsRequestType request) {
        return new FindCommunitiesAndAuditEventsResponseType();
    }

    public AcknowledgementType auditLog(LogEventRequestType request, AssertionType assertion) {
        return new AcknowledgementType();
    }

}
