package gov.hhs.fha.nhinc.docquery.adapter.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;

public interface AdapterDocQueryProxy
{
    public AdhocQueryResponse respondingGatewayCrossGatewayQuery(AdhocQueryRequest msg, AssertionType assertion);
}
