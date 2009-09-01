/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.adapterauditquery;

import gov.hhs.fha.nhinc.adapterauditlogquery.AdapterAuditLogQueryPortType;
import javax.ejb.Stateless;
import javax.jws.WebService;

/**
 *
 * @author jhoppesc
 */
@WebService(serviceName = "AdapterAuditLogQuery", portName = "AdapterAuditLogQueryPortSoap11", endpointInterface = "gov.hhs.fha.nhinc.adapterauditlogquery.AdapterAuditLogQueryPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapterauditlogquery", wsdlLocation = "META-INF/wsdl/AdapterAuditQueryService/AdapterAuditLogQuery.wsdl")
@Stateless
public class AdapterAuditQueryService implements AdapterAuditLogQueryPortType {

    public com.services.nhinc.schema.auditmessage.FindAuditEventsResponseType findAuditEvents(gov.hhs.fha.nhinc.common.nhinccommonadapter.FindAuditEventsRequestType findAuditEventsRequest) {
        return new AdapterAuditQueryServiceImpl().findAuditEvents(findAuditEventsRequest);
    }

}
