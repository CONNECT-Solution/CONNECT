/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docquery.adapter.component.deferred.response.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.healthit.nhin.DocQueryAcknowledgementType;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import gov.hhs.fha.nhinc.docquery.adapter.component.deferred.response.AdapterComponentDocQueryDeferredResponseOrchImpl;

/**
 *
 * @author jhoppesc
 */
public class AdapterComponentDocQueryDeferredResponseProxyJavaImpl implements AdapterComponentDocQueryDeferredResponseProxy {

    public DocQueryAcknowledgementType respondingGatewayCrossGatewayQuery(AdhocQueryResponse msg, AssertionType assertion, NhinTargetCommunitiesType targets) {
        return new AdapterComponentDocQueryDeferredResponseOrchImpl().respondingGatewayCrossGatewayQuery(msg, assertion);
    }

}
