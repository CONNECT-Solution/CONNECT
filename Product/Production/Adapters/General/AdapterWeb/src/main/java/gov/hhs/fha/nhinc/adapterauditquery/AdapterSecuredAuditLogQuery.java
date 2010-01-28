package gov.hhs.fha.nhinc.adapterauditquery;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "AdapterAuditLogQuerySamlService", portName = "AdapterAuditLogQuerySamlPortTypeBindingPort", endpointInterface = "gov.hhs.fha.nhinc.adapterauditlogquerysaml.AdapterAuditLogQuerySamlPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapterauditlogquerysaml", wsdlLocation = "WEB-INF/wsdl/AdapterSecuredAuditLogQuery/AdapterAuditLogQuerySaml.wsdl")
public class AdapterSecuredAuditLogQuery {
    
    @Resource
    private WebServiceContext context;

    public com.services.nhinc.schema.auditmessage.FindAuditEventsResponseType findAuditEvents(com.services.nhinc.schema.auditmessage.FindAuditEventsType findAuditEventsRequest) {
        return new AdapterSecuredAuditLogQueryImpl().queryAdapter(findAuditEventsRequest, context);
    }

}
