package gov.hhs.fha.nhinc.redactionengine.adapter;

import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;

public interface AdapterRedactionEngineOrch {

    public abstract AdhocQueryResponse filterAdhocQueryResults(AdhocQueryRequest adhocQueryRequest,
            AdhocQueryResponse adhocQueryResponse);

    public abstract RetrieveDocumentSetResponseType filterRetrieveDocumentSetResults(
            RetrieveDocumentSetRequestType retrieveDocumentSetRequest,
            RetrieveDocumentSetResponseType retrieveDocumentSetResponse);

}