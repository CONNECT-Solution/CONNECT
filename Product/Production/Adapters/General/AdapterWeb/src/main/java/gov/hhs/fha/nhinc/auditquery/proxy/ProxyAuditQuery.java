package gov.hhs.fha.nhinc.auditquery.proxy;

import javax.jws.WebService;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "NhincProxyAuditLogQuery", portName = "NhincProxyAuditLogQueryPortSoap11", endpointInterface = "gov.hhs.fha.nhinc.nhincproxyauditlogquery.NhincProxyAuditLogQueryPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhincproxyauditlogquery", wsdlLocation = "WEB-INF/wsdl/ProxyAuditQuery/NhincProxyAuditLogQuery.wsdl")
public class ProxyAuditQuery {

    public com.services.nhinc.schema.auditmessage.FindAuditEventsResponseType findAuditEvents(gov.hhs.fha.nhinc.common.nhinccommonproxy.FindAuditEventsRequestType findAuditEventsRequest) {
        return new ProxyAuditQueryImpl().findAuditEvents(findAuditEventsRequest);
    }

}
