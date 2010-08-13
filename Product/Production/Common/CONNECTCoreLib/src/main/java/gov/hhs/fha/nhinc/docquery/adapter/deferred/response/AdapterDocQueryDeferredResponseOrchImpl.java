/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.docquery.adapter.deferred.response;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.docquery.adapter.component.deferred.response.proxy.AdapterComponentDocQueryDeferredResponseProxy;
import gov.hhs.fha.nhinc.docquery.adapter.component.deferred.response.proxy.AdapterComponentDocQueryDeferredResponseProxyObjectFactory;
import gov.hhs.healthit.nhin.DocQueryAcknowledgementType;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;

/**
 *
 * @author JHOPPESC
 */
public class AdapterDocQueryDeferredResponseOrchImpl {

    public DocQueryAcknowledgementType respondingGatewayCrossGatewayQuery(AdhocQueryResponse msg, AssertionType assertion) {
        AdapterComponentDocQueryDeferredResponseProxyObjectFactory factory = new AdapterComponentDocQueryDeferredResponseProxyObjectFactory();
        AdapterComponentDocQueryDeferredResponseProxy proxy = factory.getAdapterDocQueryDeferredResponseProxy();

        return proxy.respondingGatewayCrossGatewayQuery(msg, assertion);
    }
}
