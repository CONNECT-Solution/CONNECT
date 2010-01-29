package gov.hhs.fha.nhinc.auditquery.proxy;

import javax.jws.WebService;
import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author Neil Webb
 */
@WebService(serviceName = "NhincProxyAuditLogQuerySecured", portName = "NhincProxyAuditLogQuerySecuredPortSoap11", endpointInterface = "gov.hhs.fha.nhinc.nhincproxyauditlogquerysecured.NhincProxyAuditLogQuerySecuredPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhincproxyauditlogquerysecured", wsdlLocation = "WEB-INF/wsdl/NhincAuditQueryProxy/NhincProxyAuditLogQuerySecured.wsdl")
public class NhincAuditQueryProxy
{
    @Resource
    private WebServiceContext context;

    public com.services.nhinc.schema.auditmessage.FindAuditEventsResponseType findAuditEvents(gov.hhs.fha.nhinc.common.nhinccommonproxy.FindAuditEventsSecuredRequestType findAuditEventsRequest)
    {
        return new ProxyAuditLogQueryImpl().findAuditEvents(findAuditEventsRequest, context);
    }

}
