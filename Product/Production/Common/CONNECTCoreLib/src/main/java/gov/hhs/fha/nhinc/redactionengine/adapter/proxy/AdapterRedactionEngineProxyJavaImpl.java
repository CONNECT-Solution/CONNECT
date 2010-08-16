package gov.hhs.fha.nhinc.redactionengine.adapter.proxy;

import gov.hhs.fha.nhinc.redactionengine.adapter.AdapterRedactionEngineOrchImpl;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Simple redirection to the Java implementation of the redaction engine.
 * 
 * @author Neil Webb
 */
public class AdapterRedactionEngineProxyJavaImpl implements AdapterRedactionEngineProxy {
    private static Log log = LogFactory.getLog(AdapterRedactionEngineProxyJavaImpl.class);

    public AdhocQueryResponse filterAdhocQueryResults(AdhocQueryRequest adhocQueryRequest, AdhocQueryResponse adhocQueryResponse) {
        log.debug("Using Java Implementation for Adapter Redaction Engine Service");
        return new AdapterRedactionEngineOrchImpl().filterAdhocQueryResults(adhocQueryRequest, adhocQueryResponse);
    }

    public RetrieveDocumentSetResponseType filterRetrieveDocumentSetResults(RetrieveDocumentSetRequestType retrieveDocumentSetRequest, RetrieveDocumentSetResponseType retrieveDocumentSetResponse) {
        log.debug("Using Java Implementation for Adapter Redaction Engine Service");
        return new AdapterRedactionEngineOrchImpl().filterRetrieveDocumentSetResults(retrieveDocumentSetRequest, retrieveDocumentSetResponse);
    }
}
