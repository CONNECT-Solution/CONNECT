package gov.hhs.fha.nhinc.docprovideandregister;

import javax.jws.WebService;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "AdapterDocProvideAndRegisterService", portName = "AdapterDocProvideAndRegisterPortTypeBindingPortSoap11", endpointInterface = "org.netbeans.j2ee.wsdl.interfaces.adapterdocprovideandregister.AdapterDocProvideAndRegisterPortType", targetNamespace = "http://j2ee.netbeans.org/wsdl/Interfaces/AdapterDocProvideAndRegister", wsdlLocation = "WEB-INF/wsdl/AdapterDocProvideAndRegisterservice/AdapterDocProvideAndRegister.wsdl")
public class AdapterDocProvideAndRegisterservice {

    public gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayProvideAndRegisterDocumentSetRequestResponseType adapterDocProvideAndRegisterOperation(gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayProvideAndRegisterDocumentSetRequestRequestType part1) {
        return new AdapterDocProvideAndRegisterImpl().adapterDocProvideAndRegisterOperation(part1);
    }

}
