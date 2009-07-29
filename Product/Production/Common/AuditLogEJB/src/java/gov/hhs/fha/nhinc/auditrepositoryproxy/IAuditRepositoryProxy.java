/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.auditrepositoryproxy;

import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;

/**
 *
 * @author jhoppesc
 */
public interface IAuditRepositoryProxy {

    public AcknowledgementType logEvent(LogEventRequestType logEventRequest);
}
