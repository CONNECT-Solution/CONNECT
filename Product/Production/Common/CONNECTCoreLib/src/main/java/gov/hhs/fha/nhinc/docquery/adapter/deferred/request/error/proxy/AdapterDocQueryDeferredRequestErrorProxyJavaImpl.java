/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docquery.adapter.deferred.request.error.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.docquery.adapter.deferred.request.error.AdapterDocQueryDeferredRequestErrorOrchImpl;
import gov.hhs.healthit.nhin.DocQueryAcknowledgementType;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;

/**
 *
 * @author jhoppesc
 */
public class AdapterDocQueryDeferredRequestErrorProxyJavaImpl implements AdapterDocQueryDeferredRequestErrorProxy {

    public DocQueryAcknowledgementType respondingGatewayCrossGatewayQuery(AdhocQueryRequest msg, AssertionType assertion, NhinTargetCommunitiesType targets, String errMsg) {
        return new AdapterDocQueryDeferredRequestErrorOrchImpl().respondingGatewayCrossGatewayQuery(msg, assertion, errMsg);
    }

}
