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
@WebService(serviceName = "EntityXDRSecured_Service", portName = "EntityXDRSecured_Port", endpointInterface = "gov.hhs.fha.nhinc.nhincentityxdrsecured.EntityXDRSecuredPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhincentityxdrsecured", wsdlLocation = "WEB-INF/wsdl/EntityXDRSecured/EntityXDRSecured.wsdl")
public class EntityXDRSecured {

    public oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType provideAndRegisterDocumentSetB(gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType body) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

}
