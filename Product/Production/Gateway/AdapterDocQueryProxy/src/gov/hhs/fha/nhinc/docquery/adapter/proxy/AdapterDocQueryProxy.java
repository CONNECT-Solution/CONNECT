package gov.hhs.fha.nhinc.docquery.adapter.proxy;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayQueryRequestType;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;

public interface AdapterDocQueryProxy
{
    public AdhocQueryResponse respondingGatewayCrossGatewayQuery(RespondingGatewayCrossGatewayQueryRequestType respondingGatewayCrossGatewayQueryRequest);
}
