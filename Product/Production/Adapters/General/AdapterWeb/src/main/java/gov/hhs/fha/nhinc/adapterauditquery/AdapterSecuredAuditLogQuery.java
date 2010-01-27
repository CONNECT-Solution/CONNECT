package gov.hhs.fha.nhinc.adapterauditquery;

import javax.jws.WebService;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "AdapterAuditLogQuerySamlService", portName = "AdapterAuditLogQuerySamlPortTypeBindingPort", endpointInterface = "gov.hhs.fha.nhinc.adapterauditlogquerysaml.AdapterAuditLogQuerySamlPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapterauditlogquerysaml", wsdlLocation = "WEB-INF/wsdl/AdapterSecuredAuditLogQuery/AdapterAuditLogQuerySaml.wsdl")
public class AdapterSecuredAuditLogQuery {

    public com.services.nhinc.schema.auditmessage.FindAuditEventsResponseType findAuditEvents(com.services.nhinc.schema.auditmessage.FindAuditEventsType findAuditEventsRequest) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

}
