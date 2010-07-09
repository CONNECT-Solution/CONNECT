/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.adapter.patientdiscovery.async.request.queue;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author JHOPPESC
 */
@WebService(serviceName = "AdapterPatientDiscoveryAsyncReqQueue", portName = "AdapterPatientDiscoveryAsyncReqQueuePortSoap", endpointInterface = "gov.hhs.fha.nhinc.adapterpatientdiscoveryasyncreqqueue.AdapterPatientDiscoveryAsyncReqQueuePortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapterpatientdiscoveryasyncreqqueue", wsdlLocation = "WEB-INF/wsdl/AdapterPatientDiscoveryAsyncReqQueue/AdapterPatientDiscoveryAsyncReqQueue.wsdl")
@BindingType(value = "http://www.w3.org/2003/05/soap/bindings/HTTP/")
public class AdapterPatientDiscoveryAsyncReqQueue {
    @Resource
    private WebServiceContext context;

    public org.hl7.v3.MCCIIN000002UV01 addPatientDiscoveryAsyncReq(org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType addPatientDiscoveryAsyncReqAsyncRequest) {
        return new AdapterPatientDiscoveryAsyncReqQueueImpl().addPatientDiscoveryAsyncReq(addPatientDiscoveryAsyncReqAsyncRequest, context);
    }

}
