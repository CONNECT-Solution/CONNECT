/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docquery.nhin.deferred.response.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.healthit.nhin.DocQueryAcknowledgementType;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;

/**
 *
 * @author jhoppesc
 */
public interface NhinDocQueryDeferredResponseProxy {
    public DocQueryAcknowledgementType respondingGatewayCrossGatewayQuery(AdhocQueryResponse body, AssertionType assertion, NhinTargetSystemType target);

}
