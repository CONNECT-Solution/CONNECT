package gov.hhs.fha.nhinc.auditquery.entity;

import javax.jws.WebService;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "EntityAuditLogQuery", portName = "EntityAuditLogQueryPortSoap11", endpointInterface = "gov.hhs.fha.nhinc.entityauditlogquery.EntityAuditLogQueryPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:entityauditlogquery", wsdlLocation = "WEB-INF/wsdl/EntityAuditQuery/EntityAuditLogQuery.wsdl")
public class EntityAuditQuery {

    public com.services.nhinc.schema.auditmessage.FindAuditEventsResponseType findAuditEvents(gov.hhs.fha.nhinc.common.nhinccommonentity.FindAuditEventsRequestType findAuditEventsRequest) {
        return new EntityAuditQueryImpl().findAuditEvents(findAuditEventsRequest);
    }

}
