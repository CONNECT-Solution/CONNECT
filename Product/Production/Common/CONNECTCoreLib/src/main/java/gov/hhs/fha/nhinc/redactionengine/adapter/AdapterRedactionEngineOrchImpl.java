/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
 * @author JHOPPESC
 */
public class AdapterRedactionEngineOrchImpl {
    private Log log = null;

    public AdapterRedactionEngineOrchImpl()
    {
        log = createLogger();
    }

    protected Log createLogger()
    {
        return LogFactory.getLog(getClass());
    }

    protected RedactionEngine getRedactionEngine()
    {
        return new RedactionEngine();
    }

    public AdhocQueryResponse filterAdhocQueryResults(AdhocQueryRequest adhocQueryRequest, AdhocQueryResponse adhocQueryResponse)
    {
        log.debug("Begin filterAdhocQueryResults");
        AdhocQueryResponse response = null;
        RedactionEngine redactionEngine = getRedactionEngine();
        if(redactionEngine != null)
        {
            response = redactionEngine.filterAdhocQueryResults(adhocQueryRequest, adhocQueryResponse);
        }
        else
        {
            log.warn("RedactionEngine was null");
        }
        log.debug("End filterAdhocQueryResults");
        return response;
    }

    public RetrieveDocumentSetResponseType filterRetrieveDocumentSetResults(RetrieveDocumentSetRequestType retrieveDocumentSetRequest, RetrieveDocumentSetResponseType retrieveDocumentSetResponse)
    {
        log.debug("Begin filterRetrieveDocumentSetResults");
        RetrieveDocumentSetResponseType response = null;
        RedactionEngine redactionEngine = getRedactionEngine();
        if(redactionEngine != null)
        {
            response = redactionEngine.filterRetrieveDocumentSetResults(retrieveDocumentSetRequest, retrieveDocumentSetResponse);
        }
        else
        {
            log.warn("RedactionEngine was null");
        }
        log.debug("Begin filterRetrieveDocumentSetResults");
        return response;
    }

}
