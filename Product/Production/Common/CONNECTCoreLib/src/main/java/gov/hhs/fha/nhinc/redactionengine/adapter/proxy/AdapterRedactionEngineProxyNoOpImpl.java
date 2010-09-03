/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.redactionengine.adapter.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Return the response object that was provided.
 * 
 * @author Neil Webb
 */
public class AdapterRedactionEngineProxyNoOpImpl implements AdapterRedactionEngineProxy
{
    private static Log log = LogFactory.getLog(AdapterRedactionEngineProxyNoOpImpl.class);

    public AdhocQueryResponse filterAdhocQueryResults(AdhocQueryRequest adhocQueryRequest, AdhocQueryResponse adhocQueryResponse, AssertionType assertion)
    {
        log.debug("Using NoOp Implementation for Adapter Redaction Engine Service");
        return adhocQueryResponse;
    }

    public RetrieveDocumentSetResponseType filterRetrieveDocumentSetResults(RetrieveDocumentSetRequestType retrieveDocumentSetRequest, RetrieveDocumentSetResponseType retrieveDocumentSetResponse, AssertionType assertion)
    {
        log.debug("Using NoOp Implementation for Adapter Redaction Engine Service");
        return retrieveDocumentSetResponse;
    }

}
