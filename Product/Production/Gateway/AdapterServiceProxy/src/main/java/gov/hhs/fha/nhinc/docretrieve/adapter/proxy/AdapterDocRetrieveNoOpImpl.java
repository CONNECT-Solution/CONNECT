package gov.hhs.fha.nhinc.docretrieve.adapter.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
/**
 *
 *
 * @author Neil Webb
 */
public class AdapterDocRetrieveNoOpImpl implements AdapterDocRetrieveProxy
{

    public RetrieveDocumentSetResponseType retrieveDocumentSet(RetrieveDocumentSetRequestType request, AssertionType assertion)
    {
        return new RetrieveDocumentSetResponseType();
    }
    
}
