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
 * @author user1
 */
@WebService(serviceName = "EntityAuditLogQuery", portName = "EntityAuditLogQueryPortSoap11", endpointInterface = "gov.hhs.fha.nhinc.entityauditlogquery.EntityAuditLogQueryPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:entityauditlogquery", wsdlLocation = "META-INF/wsdl/EntityAuditLogQuery/EntityAuditLogQuery.wsdl")
@Stateless
public class EntityAuditLogQuery implements EntityAuditLogQueryPortType {

    /**
     * This method will receive and service Audit Log Query requests from the Entity Interface
     *
     * @param findAuditEventsRequest The audit log query search criteria
     * @return A list of Audit Log records that match the specified criteria
     */
    public com.services.nhinc.schema.auditmessage.FindAuditEventsResponseType findAuditEvents(gov.hhs.fha.nhinc.common.nhinccommonentity.FindAuditEventsRequestType findAuditEventsRequest) {
        EntityAuditLogImpl auditQuery = new EntityAuditLogImpl();
        return auditQuery.findAuditEvents(findAuditEventsRequest);
    }

}
