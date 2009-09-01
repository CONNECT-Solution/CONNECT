/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.auditquery.proxy;

import gov.hhs.fha.nhinc.nhincproxyauditlogquerysecured.NhincProxyAuditLogQuerySecuredPortType;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author jhoppesc
 */
@WebService(serviceName = "NhincProxyAuditLogQuerySecured", portName = "NhincProxyAuditLogQuerySecuredPortSoap11", endpointInterface = "gov.hhs.fha.nhinc.nhincproxyauditlogquerysecured.NhincProxyAuditLogQuerySecuredPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhincproxyauditlogquerysecured", wsdlLocation = "META-INF/wsdl/ProxyAuditLogQuery/NhincProxyAuditLogQuerySecured.wsdl")
@Stateless
public class ProxyAuditLogQuery implements NhincProxyAuditLogQuerySecuredPortType {

    @Resource
    private WebServiceContext context;

    public com.services.nhinc.schema.auditmessage.FindAuditEventsResponseType findAuditEvents(gov.hhs.fha.nhinc.common.nhinccommonproxy.FindAuditEventsSecuredRequestType findAuditEventsRequest) {
        ProxyAuditLogQueryImpl proxyAuditQuery = new ProxyAuditLogQueryImpl();
        return proxyAuditQuery.findAuditEvents(findAuditEventsRequest, context);
    }
}
