/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.auditquery.entity;

import gov.hhs.fha.nhinc.entityauditlogquerysaml.EntityAuditLogQuerySamlPortType;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author jhoppesc
 */
@WebService(serviceName = "EntityAuditLogQuerySamlService", portName = "EntityAuditLogQuerySamlPortTypeBindingPort", endpointInterface = "gov.hhs.fha.nhinc.entityauditlogquerysaml.EntityAuditLogQuerySamlPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:entityauditlogquerysaml", wsdlLocation = "META-INF/wsdl/EntityAuditLogQuery/EntityAuditLogQuerySecured.wsdl")
@Stateless
public class EntityAuditLogQuery implements EntityAuditLogQuerySamlPortType {
    @Resource
    private WebServiceContext context;

    public com.services.nhinc.schema.auditmessage.FindAuditEventsResponseType findAuditEvents(gov.hhs.fha.nhinc.common.nhinccommonentity.FindAuditEventsSecuredRequestType findAuditEventsRequest) {
        EntityAuditLogImpl auditQuery = new EntityAuditLogImpl();
        return auditQuery.findAuditEvents(findAuditEventsRequest, context);
    }

}
