/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docquery.entity;

import gov.hhs.fha.nhinc.entitydocquery.EntityDocQueryPortType;
import javax.ejb.Stateless;
import javax.jws.WebService;

/**
 *
 * @author jhoppesc
 */
@WebService(serviceName = "EntityDocQuery", portName = "EntityDocQueryPortSoap11", endpointInterface = "gov.hhs.fha.nhinc.entitydocquery.EntityDocQueryPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:entitydocquery", wsdlLocation = "META-INF/wsdl/EntityDocQuery/EntityDocQuery.wsdl")
@Stateless
public class EntityDocQuery implements EntityDocQueryPortType {

    public oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse respondingGatewayCrossGatewayQuery(gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayQueryRequestType respondingGatewayCrossGatewayQueryRequest) {
        EntityDocQueryImpl docQuery = new EntityDocQueryImpl();
        return docQuery.documentQuery(respondingGatewayCrossGatewayQueryRequest);
    }

}
