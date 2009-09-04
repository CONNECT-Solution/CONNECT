/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.auditquery.proxy;

import gov.hhs.fha.nhinc.nhincproxyauditlogquery.NhincProxyAuditLogQueryPortType;
import javax.ejb.Stateless;
import javax.jws.WebService;

/**
 *
 * @author jhoppesc
 */
@WebService(serviceName = "NhincProxyAuditLogQuery", portName = "NhincProxyAuditLogQueryPortSoap11", endpointInterface = "gov.hhs.fha.nhinc.nhincproxyauditlogquery.NhincProxyAuditLogQueryPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhincproxyauditlogquery", wsdlLocation = "META-INF/wsdl/ProxyAuditQuery/NhincProxyAuditLogQuery.wsdl")
@Stateless
public class ProxyAuditQuery implements NhincProxyAuditLogQueryPortType {

    public com.services.nhinc.schema.auditmessage.FindAuditEventsResponseType findAuditEvents(gov.hhs.fha.nhinc.common.nhinccommonproxy.FindAuditEventsRequestType findAuditEventsRequest) {
        return new ProxyAuditQueryImpl().findAuditEvents(findAuditEventsRequest);
    }

}
