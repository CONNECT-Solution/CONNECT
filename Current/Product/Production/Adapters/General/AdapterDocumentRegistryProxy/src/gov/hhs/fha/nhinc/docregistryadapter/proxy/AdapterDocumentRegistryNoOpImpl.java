/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docregistryadapter.proxy;

import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;

/**
 *
 * @author svalluripalli
 */
public class AdapterDocumentRegistryNoOpImpl implements AdapterDocumentRegistryProxy
{

    /**
     * No-op implementation class for Component proxy pattern
     * @param request
     * @return AdhocQueryResponse
     */
    public AdhocQueryResponse registryStoredQuery(AdhocQueryRequest request) {
        return new AdhocQueryResponse();
    }
    
}
