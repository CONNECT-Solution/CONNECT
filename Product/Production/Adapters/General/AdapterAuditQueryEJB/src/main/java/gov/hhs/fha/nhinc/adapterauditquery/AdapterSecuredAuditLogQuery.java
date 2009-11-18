/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.adapterauditquery;

import gov.hhs.fha.nhinc.adapterauditlogquerysaml.AdapterAuditLogQuerySamlPortType;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author jhoppesc
 */
@WebService(serviceName = "AdapterAuditLogQuerySamlService", portName = "AdapterAuditLogQuerySamlPortTypeBindingPort", endpointInterface = "gov.hhs.fha.nhinc.adapterauditlogquerysaml.AdapterAuditLogQuerySamlPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapterauditlogquerysaml", wsdlLocation = "META-INF/wsdl/AdapterSecuredAuditLogQuery/AdapterAuditLogQuerySaml.wsdl")
@Stateless
public class AdapterSecuredAuditLogQuery implements AdapterAuditLogQuerySamlPortType {

    @Resource
    private WebServiceContext context;

    public com.services.nhinc.schema.auditmessage.FindAuditEventsResponseType findAuditEvents(com.services.nhinc.schema.auditmessage.FindAuditEventsType findAuditEventsRequest) {
        return new AdapterSecuredAuditLogQueryImpl().queryAdapter(findAuditEventsRequest, context);
    }
}
