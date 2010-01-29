package gov.hhs.fha.nhinc.auditquery.entity;

import javax.jws.WebService;
import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author Neil Webb
 */
@WebService(serviceName = "EntityAuditLogQuerySamlService", portName = "EntityAuditLogQuerySamlPortTypeBindingPort", endpointInterface = "gov.hhs.fha.nhinc.entityauditlogquerysaml.EntityAuditLogQuerySamlPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:entityauditlogquerysaml", wsdlLocation = "WEB-INF/wsdl/EntityAuditLogQuery/EntityAuditLogQuerySecured.wsdl")
public class EntityAuditLogQuery
{
    @Resource
    private WebServiceContext context;

    public com.services.nhinc.schema.auditmessage.FindAuditEventsResponseType findAuditEvents(gov.hhs.fha.nhinc.common.nhinccommonentity.FindAuditEventsSecuredRequestType findAuditEventsRequest)
    {
        return new EntityAuditLogImpl().findAuditEvents(findAuditEventsRequest, context);
    }

}
