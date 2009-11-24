/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.auditquery.entity;

import gov.hhs.fha.nhinc.entityauditlogquery.EntityAuditLogQueryPortType;
import javax.ejb.Stateless;
import javax.jws.WebService;

/**
 *
 * @author jhoppesc
 */
@WebService(serviceName = "EntityAuditLogQuery", portName = "EntityAuditLogQueryPortSoap11", endpointInterface = "gov.hhs.fha.nhinc.entityauditlogquery.EntityAuditLogQueryPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:entityauditlogquery", wsdlLocation = "META-INF/wsdl/EntityAuditQuery/EntityAuditLogQuery.wsdl")
@Stateless
public class EntityAuditQuery implements EntityAuditLogQueryPortType {

    public com.services.nhinc.schema.auditmessage.FindAuditEventsResponseType findAuditEvents(gov.hhs.fha.nhinc.common.nhinccommonentity.FindAuditEventsRequestType findAuditEventsRequest) {
        return new EntityAuditQueryImpl().findAuditEvents(findAuditEventsRequest);
    }

}
