package gov.hhs.fha.nhinc.adapterdocretrieve;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "AdapterDocRetrieveSecured", portName = "AdapterDocRetrieveSecuredPortSoap", endpointInterface = "gov.hhs.fha.nhinc.adapterdocretrievesecured.AdapterDocRetrieveSecuredPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapterdocretrievesecured", wsdlLocation = "WEB-INF/wsdl/AdapterDocRetrieveSecured/AdapterDocRetrieveSecured.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class AdapterDocRetrieveSecured {

    @Resource
    private WebServiceContext context;

    public ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType respondingGatewayCrossGatewayRetrieve(ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType body) {
        return new AdapterDocRetrieveSecuredImpl().respondingGatewayCrossGatewayRetrieve(body, context);
    }

}
