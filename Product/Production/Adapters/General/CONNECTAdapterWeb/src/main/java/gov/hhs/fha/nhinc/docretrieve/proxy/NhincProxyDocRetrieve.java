package gov.hhs.fha.nhinc.docretrieve.proxy;

import javax.jws.WebService;
import javax.xml.ws.BindingType;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "NhincProxyDocRetrieve", portName = "NhincProxyDocRetrievePortSoap", endpointInterface = "gov.hhs.fha.nhinc.nhincproxydocretrieve.NhincProxyDocRetrievePortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhincproxydocretrieve", wsdlLocation = "WEB-INF/wsdl/NhincProxyDocRetrieve/NhincProxyDocRetrieve.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class NhincProxyDocRetrieve {

    public ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType respondingGatewayCrossGatewayRetrieve(gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayRetrieveRequestType respondingGatewayCrossGatewayRetrieveRequest) {
        return new NhincProxyDocRetrieveImpl().respondingGatewayCrossGatewayRetrieve(respondingGatewayCrossGatewayRetrieveRequest);
    }

}
