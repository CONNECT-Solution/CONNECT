/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docretrieve.nhin.deferred.request;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author jhoppesc
 */
@WebService(serviceName = "RespondingGatewayDeferredRequest_Retrieve_Service", portName = "RespondingGatewayDeferredRequest_Retrieve_Port_Soap", endpointInterface = "ihe.iti.xds_b._2007.RespondingGatewayDeferredRequestRetrievePortType", targetNamespace = "urn:ihe:iti:xds-b:2007", wsdlLocation = "WEB-INF/wsdl/NhinDocRetrieveDeferredRequest/NhinDocRetrieveDeferredReq.wsdl")
@BindingType(value = "http://www.w3.org/2003/05/soap/bindings/HTTP/")

public class NhinDocRetrieveDeferredRequest {
    @Resource
    private WebServiceContext context;

    public gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType respondingGatewayDeferredRequestCrossGatewayRetrieve(ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType body) {
        return new NhinDocRetrieveDeferredRequestImpl().respondingGatewayDeferredRequestCrossGatewayRetrieve(body, context);
    }

}
