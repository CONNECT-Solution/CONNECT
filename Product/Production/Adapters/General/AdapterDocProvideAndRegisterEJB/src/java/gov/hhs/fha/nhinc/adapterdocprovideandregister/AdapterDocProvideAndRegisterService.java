/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.adapterdocprovideandregister;

import javax.ejb.Stateless;
import javax.jws.WebService;
import org.netbeans.j2ee.wsdl.interfaces.adapterdocprovideandregister.AdapterDocProvideAndRegisterPortType;

/**
 *
 * @author svalluripalli
 */
@WebService(serviceName = "AdapterDocProvideAndRegisterService", portName = "AdapterDocProvideAndRegisterPortTypeBindingPortSoap11", endpointInterface = "org.netbeans.j2ee.wsdl.interfaces.adapterdocprovideandregister.AdapterDocProvideAndRegisterPortType", targetNamespace = "http://j2ee.netbeans.org/wsdl/Interfaces/AdapterDocProvideAndRegister", wsdlLocation = "META-INF/wsdl/AdapterDocProvideAndRegisterService/AdapterDocProvideAndRegister.wsdl")
@Stateless
public class AdapterDocProvideAndRegisterService implements AdapterDocProvideAndRegisterPortType {

    public gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayProvideAndRegisterDocumentSetRequestResponseType adapterDocProvideAndRegisterOperation(gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayProvideAndRegisterDocumentSetRequestRequestType part1) {
        return new AdapterDocProvideAndRegisterImpl().adapterDocProvideAndRegisterOperation(part1);
    }

}
