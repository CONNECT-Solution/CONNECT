/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.docregistry.adapter.proxy;

import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author svalluripalli
 */
public class AdapterComponentDocRegistryProxyNoOpImpl implements AdapterComponentDocRegistryProxy {

    private static Log log = LogFactory.getLog(AdapterComponentDocRegistryProxyNoOpImpl.class);

    public AdhocQueryResponse registryStoredQuery(AdhocQueryRequest request) {
        log.debug("Using NoOp Implementation for Adapter Component Doc Registry Service");
        return new AdhocQueryResponse();
    }
}
