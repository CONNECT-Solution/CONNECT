/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.redactionengine.adapter;

import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Neil Webb
 */
public class RedactionEngine
{
    private Log log = null;

    public RedactionEngine()
    {
        log = createLogger();
    }

    protected Log createLogger()
    {
        return LogFactory.getLog(getClass());
    }

    protected DocQueryResponseProcessor getDocQueryResponseProcessor()
    {
        return new DocQueryResponseProcessor();
    }

    protected DocRetrieveResponseProcessor getDocRetrieveResponseProcessor()
    {
        return new DocRetrieveResponseProcessor();
    }

    public AdhocQueryResponse filterAdhocQueryResults(AdhocQueryRequest adhocQueryRequest, AdhocQueryResponse adhocQueryResponse)
    {
        log.debug("Begin filterAdhocQueryResults");
        AdhocQueryResponse response = null;
        DocQueryResponseProcessor processor = getDocQueryResponseProcessor();
        if(processor != null)
        {
            response = processor.filterAdhocQueryResults(adhocQueryRequest, adhocQueryResponse);
        }
        else
        {
            log.warn("DocQueryResponseProcessor was null.");
        }
        log.debug("End filterAdhocQueryResults");
        return response;
    }

    public RetrieveDocumentSetResponseType filterRetrieveDocumentSetResults(RetrieveDocumentSetRequestType retrieveDocumentSetRequest, RetrieveDocumentSetResponseType retrieveDocumentSetResponse)
    {
        log.debug("Begin filterRetrieveDocumentSetResults");
        RetrieveDocumentSetResponseType response = null;
        DocRetrieveResponseProcessor processor = getDocRetrieveResponseProcessor();
        if(processor != null)
        {
            return processor.filterRetrieveDocumentSetReults(retrieveDocumentSetRequest, retrieveDocumentSetResponse);
        }
        else
        {
            log.warn("DocRetrieveResponseProcessor was null.");
        }
        log.debug("End filterRetrieveDocumentSetResults");
        return response;
    }
}
