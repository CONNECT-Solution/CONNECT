package gov.hhs.fha.nhinc.docregistry.adapter;

import javax.xml.ws.WebServiceContext;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;

/**
 * Helper class for performing document registry operations
 * 
 * @author Neil Webb
 */
public class DocumentRegistryImpl
{

    public AdhocQueryResponse documentRegistryRegistryStoredQuery(AdhocQueryRequest body, WebServiceContext context)
    {
        return new AdapterComponentDocRegistryOrchImpl().registryStoredQuery(body);
    }

    
}
