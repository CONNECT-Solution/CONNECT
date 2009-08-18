/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.adapterauditquery;

import gov.hhs.fha.nhinc.adapterauditlogquery.AdapterAuditLogQueryPortType;
import javax.ejb.Stateless;
import javax.jws.WebService;

/**
 * This web service class supports the adapter audit log query wsdl interface.
 *
 * @author Clark Shaw
 */
@WebService(serviceName = "AdapterAuditLogQuery", portName = "AdapterAuditLogQueryPortSoap11", endpointInterface = "gov.hhs.fha.nhinc.adapterauditlogquery.AdapterAuditLogQueryPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapterauditlogquery", wsdlLocation = "META-INF/wsdl/AdapterAuditQuery/AdapterAuditLogQuery.wsdl")
@Stateless
public class AdapterAuditQuery implements AdapterAuditLogQueryPortType {

    public com.services.nhinc.schema.auditmessage.FindAuditEventsResponseType findAuditEvents(gov.hhs.fha.nhinc.common.nhinccommonadapter.FindAuditEventsRequestType findAuditEventsRequest)
    {
       return new AdapterAuditQueryImpl().queryAdapter(findAuditEventsRequest);
    }

}
