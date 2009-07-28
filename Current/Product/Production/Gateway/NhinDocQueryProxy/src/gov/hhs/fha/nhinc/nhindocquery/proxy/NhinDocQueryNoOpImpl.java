/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.nhindocquery.proxy;

import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayQueryRequestType;

/**
 *
 * @author jhoppesc
 */
public class NhinDocQueryNoOpImpl implements NhinDocQueryProxy {

    public AdhocQueryResponse respondingGatewayCrossGatewayQuery(RespondingGatewayCrossGatewayQueryRequestType request) {
        return new AdhocQueryResponse();
    }

}
