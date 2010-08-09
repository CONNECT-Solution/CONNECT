/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docquery.adapter.component.deferred.request.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.docquery.adapter.component.deferred.request.AdapterComponentDocQueryDeferredRequestOrchImpl;
import gov.hhs.healthit.nhin.DocQueryAcknowledgementType;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;

/**
 *
 * @author jhoppesc
 */
public class AdapterComponentDocQueryDeferredRequestProxyJavaImpl implements AdapterComponentDocQueryDeferredRequestProxy {

    public DocQueryAcknowledgementType respondingGatewayCrossGatewayQuery(AdhocQueryRequest msg, AssertionType assertion, NhinTargetCommunitiesType targets) {
        return new AdapterComponentDocQueryDeferredRequestOrchImpl().respondingGatewayCrossGatewayQuery(msg, assertion, targets);
    }

}
