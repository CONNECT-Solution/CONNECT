/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.auditrepository;

import gov.hhs.fha.nhinc.nhinccomponentauditrepository.AuditRepositoryManagerSecuredPortType;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author jhoppesc
 */
@WebService(serviceName = "AuditRepositoryManagerSecuredService", portName = "AuditRepositoryManagerSecuredPort", endpointInterface = "gov.hhs.fha.nhinc.nhinccomponentauditrepository.AuditRepositoryManagerSecuredPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhinccomponentauditrepository", wsdlLocation = "META-INF/wsdl/AuditRepository/NhincComponentAuditRepositorySecured.wsdl")
@Stateless
public class AuditRepository implements AuditRepositoryManagerSecuredPortType {

    @Resource
    private WebServiceContext context;

    public gov.hhs.fha.nhinc.common.nhinccommonadapter.FindCommunitiesAndAuditEventsResponseType queryAuditEvents(gov.hhs.fha.nhinc.common.nhinccommonadapter.FindCommunitiesAndAuditEventsRequestType queryAuditEventsRequest) {
        return AuditRepositoryHelper.queryAuditEvents(queryAuditEventsRequest, context);
    }

    public gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType logEvent(gov.hhs.fha.nhinc.common.auditlog.LogEventSecureRequestType logEventRequest) {
        return AuditRepositoryHelper.logEvent(logEventRequest, context);
    }
}
