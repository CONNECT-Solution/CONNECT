package gov.hhs.fha.nhinc.docrepositoryadapter.proxy;

import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;

/**
 * Interface for the adapter document repository proxy.
 *
 * @author Neil Webb
 */
public interface AdapterDocumentRepositoryProxy
{
    /**
     * Retrieve a document set.
     * 
     * @param request Retrieve document set request
     * @return Retrieve document set response
     */
    public RetrieveDocumentSetResponseType retrieveDocumentSet(RetrieveDocumentSetRequestType request);
}
