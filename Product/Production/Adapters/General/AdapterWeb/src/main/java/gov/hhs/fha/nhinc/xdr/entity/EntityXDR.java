/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.xdr.entity;

import javax.jws.WebService;

/**
 *
 * @author dunnek
 */
@WebService(serviceName = "EntityXDR_Service", portName = "EntityXDR_Port", endpointInterface = "gov.hhs.fha.nhinc.nhincentityxdr.EntityXDRPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhincentityxdr", wsdlLocation = "WEB-INF/wsdl/EntityXDR/EntityXDR.wsdl")
public class EntityXDR {

    public oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType provideAndRegisterDocumentSetB(gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetRequestType respondingGatewayProvideAndRegisterDocumentSetRequest) {
        return new EntityXDRImpl().provideAndRegisterDocumentSetB(respondingGatewayProvideAndRegisterDocumentSetRequest);
    }

}
