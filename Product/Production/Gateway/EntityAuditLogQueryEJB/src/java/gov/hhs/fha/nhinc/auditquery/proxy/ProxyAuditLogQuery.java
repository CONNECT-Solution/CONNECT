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
 * @author Jon Hoppesch
 */
@WebService(serviceName = "NhincProxyAuditLogQuery", portName = "NhincProxyAuditLogQueryPortSoap11", endpointInterface = "gov.hhs.fha.nhinc.nhincproxyauditlogquery.NhincProxyAuditLogQueryPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhincproxyauditlogquery", wsdlLocation = "META-INF/wsdl/ProxyAuditLogQuery/NhincProxyAuditLogQuery.wsdl")
@Stateless
public class ProxyAuditLogQuery implements NhincProxyAuditLogQueryPortType {

    /**
     * This method will perform an audit log query to a specified community and return a list
     * of audit log records will be returned to the user.
     *
     * @param findAuditEventsRequest The audit log query search criteria
     * @return A list of Audit Log records that match the specified criteria
     */
    public com.services.nhinc.schema.auditmessage.FindAuditEventsResponseType findAuditEvents(gov.hhs.fha.nhinc.common.nhinccommonproxy.FindAuditEventsRequestType findAuditEventsRequest) {
        ProxyAuditLogQueryImpl proxyAuditQuery = new ProxyAuditLogQueryImpl ();
        return proxyAuditQuery.findAuditEvents(findAuditEventsRequest);
    }

}
