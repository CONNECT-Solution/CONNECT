package gov.hhs.fha.nhinc.auditquery.entity;

import javax.jws.WebService;
import javax.xml.ws.BindingType;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "EntityAuditLogQuery", portName = "EntityAuditLogQueryPortSoap", endpointInterface = "gov.hhs.fha.nhinc.entityauditlogquery.EntityAuditLogQueryPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:entityauditlogquery", wsdlLocation = "WEB-INF/wsdl/EntityAuditQuery/EntityAuditLogQuery.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class EntityAuditQuery {

    public com.services.nhinc.schema.auditmessage.FindAuditEventsResponseType findAuditEvents(gov.hhs.fha.nhinc.common.nhinccommonentity.FindAuditEventsRequestType findAuditEventsRequest) {
        return new EntityAuditQueryImpl().findAuditEvents(findAuditEventsRequest);
    }

}
