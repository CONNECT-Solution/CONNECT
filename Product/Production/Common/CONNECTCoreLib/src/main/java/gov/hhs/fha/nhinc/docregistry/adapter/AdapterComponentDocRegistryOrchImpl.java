/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docregistry.adapter;

import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;

/**
 *
 * @author jhoppesc
 */
public class AdapterComponentDocRegistryOrchImpl {
    public AdhocQueryResponse registryStoredQuery(AdhocQueryRequest request) {
        return new AdhocQueryResponse();
    }

}
