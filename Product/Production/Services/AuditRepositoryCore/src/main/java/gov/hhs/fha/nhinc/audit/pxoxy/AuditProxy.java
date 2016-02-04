/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.audit.pxoxy;

import gov.hhs.fha.nhinc.auditrepository.nhinc.proxy.AuditRepositoryProxyObjectFactory;
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;

/**
 *
 * @author achidamb
 */
public class AuditProxy {

    private void auditLogMessages(LogEventRequestType auditLogMsg, AssertionType assertion) {
        if (auditLogMsg != null && auditLogMsg.getAuditMessage() != null) {
            new AuditRepositoryProxyObjectFactory().getAuditRepositoryProxy().auditLog(auditLogMsg, assertion);
        } /*else {
         LOG.error("auditLogMsg is null, no auditing will take place.");
         }*/

    }

}
