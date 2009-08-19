package gov.hhs.fha.nhinc.docrepositoryadapter.proxy;

import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;

/**
 * No-Op implementation of the adapter document repository proxy.
 *
 * @author Neil Webb
 */
public class AdapterDocumentRepositoryNoOpImpl implements AdapterDocumentRepositoryProxy
{

    public RetrieveDocumentSetResponseType retrieveDocumentSet(RetrieveDocumentSetRequestType request)
    {
        return new RetrieveDocumentSetResponseType();
    }
    
}
