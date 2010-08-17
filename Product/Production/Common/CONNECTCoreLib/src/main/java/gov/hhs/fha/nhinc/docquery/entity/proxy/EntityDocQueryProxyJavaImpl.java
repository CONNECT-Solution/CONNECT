package gov.hhs.fha.nhinc.docquery.entity.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayQuerySecuredRequestType;
import gov.hhs.fha.nhinc.docquery.entity.EntityDocQueryOrchImpl;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;


public class EntityDocQueryProxyJavaImpl implements EntityDocQueryProxy
{

    public AdhocQueryResponse respondingGatewayCrossGatewayQuery(AdhocQueryRequest msg, AssertionType assertion, NhinTargetCommunitiesType targets)
    {
        AdhocQueryResponse response = null;

        RespondingGatewayCrossGatewayQuerySecuredRequestType request = new RespondingGatewayCrossGatewayQuerySecuredRequestType();
        request.setAdhocQueryRequest(msg);
        request.setNhinTargetCommunities(targets);

        EntityDocQueryOrchImpl orchImpl = new EntityDocQueryOrchImpl();
        response = orchImpl.respondingGatewayCrossGatewayQuery(request, assertion);

        return response;
    }
}
