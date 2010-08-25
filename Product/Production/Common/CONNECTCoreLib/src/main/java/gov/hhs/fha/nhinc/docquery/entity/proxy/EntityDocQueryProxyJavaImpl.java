package gov.hhs.fha.nhinc.docquery.entity.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.docquery.entity.EntityDocQueryOrchImpl;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;

public class EntityDocQueryProxyJavaImpl implements EntityDocQueryProxy
{

    public AdhocQueryResponse respondingGatewayCrossGatewayQuery(AdhocQueryRequest msg, AssertionType assertion, NhinTargetCommunitiesType targets)
    {
        AdhocQueryResponse response = null;

        EntityDocQueryOrchImpl orchImpl = new EntityDocQueryOrchImpl();
        response = orchImpl.respondingGatewayCrossGatewayQuery(msg, assertion, targets);

        return response;
    }
}
