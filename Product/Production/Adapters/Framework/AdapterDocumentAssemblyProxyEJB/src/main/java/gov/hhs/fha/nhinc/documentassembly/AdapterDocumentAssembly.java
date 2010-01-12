/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.documentassembly;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayProvideAndRegisterDocumentSetRequestRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayQueryRequestType;
import javax.ejb.Stateless;
import javax.jws.WebService;

/**
 *
 * @author kim
 */
@WebService(serviceName = "DocumentAssembly", portName = "DocumentAssemblyPortSoap", endpointInterface = "gov.hhs.fha.nhinc.documentassembly.DocumentAssemblyPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:documentassembly", wsdlLocation = "META-INF/wsdl/AdapterDocumentAssembly/DocumentAssembly.wsdl")
@Stateless
public class AdapterDocumentAssembly implements DocumentAssemblyPortType {

   @Override
   public RespondingGatewayCrossGatewayProvideAndRegisterDocumentSetRequestRequestType dynamicAssemblyQuery(RespondingGatewayCrossGatewayQueryRequestType dynamicAssemblyQueryRequest) {
      return AdapterDocumentAssemblyHelper.dynamicAssemblyQuery(dynamicAssemblyQueryRequest);
   }

}
