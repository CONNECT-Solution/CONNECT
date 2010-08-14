/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docregistry.adapter.proxy;

import gov.hhs.fha.nhinc.docregistry.adapter.AdapterComponentDocRegistryOrchImpl;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author jhoppesc
 */
public class AdapterComponentDocRegistryProxyJavaImpl implements AdapterComponentDocRegistryProxy {
    private static Log log = LogFactory.getLog(AdapterComponentDocRegistryProxyJavaImpl.class);

    public AdhocQueryResponse registryStoredQuery(AdhocQueryRequest request) {
        log.debug("Using Java Implementation for Adapter Component Doc Registry Service");
        return new AdapterComponentDocRegistryOrchImpl().registryStoredQuery(request);
    }

}
