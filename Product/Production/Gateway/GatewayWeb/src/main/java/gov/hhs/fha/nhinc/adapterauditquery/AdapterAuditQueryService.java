package gov.hhs.fha.nhinc.adapterauditquery;

import javax.jws.WebService;

/**
 *
 * @author Neil Webb
 */
@WebService(serviceName = "AdapterAuditLogQuery", portName = "AdapterAuditLogQueryPortSoap11", endpointInterface = "gov.hhs.fha.nhinc.adapterauditlogquery.AdapterAuditLogQueryPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapterauditlogquery", wsdlLocation = "WEB-INF/wsdl/AdapterAuditQueryService/AdapterAuditLogQuery.wsdl")
public class AdapterAuditQueryService
{

    public com.services.nhinc.schema.auditmessage.FindAuditEventsResponseType findAuditEvents(gov.hhs.fha.nhinc.common.nhinccommonadapter.FindAuditEventsRequestType findAuditEventsRequest)
    {
        return new AdapterAuditQueryServiceImpl().findAuditEvents(findAuditEventsRequest);
    }

}
