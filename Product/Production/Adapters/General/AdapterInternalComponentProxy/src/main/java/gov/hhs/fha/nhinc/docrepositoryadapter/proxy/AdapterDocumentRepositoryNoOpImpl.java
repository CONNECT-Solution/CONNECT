package gov.hhs.fha.nhinc.docrepositoryadapter.proxy;

import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

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

    public RegistryResponseType provideAndRegisterDocumentSet(ProvideAndRegisterDocumentSetRequestType body) {
        return new RegistryResponseType();
    }
    
}
