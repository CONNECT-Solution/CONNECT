/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docquery.adapter.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.docquery.adapter.AdapterDocQueryOrchImpl;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author jhoppesc
 */
public class AdapterDocQueryProxyJavaImpl implements AdapterDocQueryProxy {
    private static Log log = LogFactory.getLog(AdapterDocQueryProxyJavaImpl.class);

    public AdhocQueryResponse respondingGatewayCrossGatewayQuery(AdhocQueryRequest msg, AssertionType assertion) {
        log.debug("Using Java Implementation for Adapter Doc Query Service");
        return new AdapterDocQueryOrchImpl().respondingGatewayCrossGatewayQuery(msg, assertion);
    }

}
