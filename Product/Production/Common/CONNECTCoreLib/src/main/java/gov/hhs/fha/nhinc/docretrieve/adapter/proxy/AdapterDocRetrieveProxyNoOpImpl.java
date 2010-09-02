/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.docretrieve.adapter.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
/**
 * This is the No Op implementation of the Adapter Doc Retrieve component
 * proxy.
 *
 * @author Neil Webb
 */
public class AdapterDocRetrieveProxyNoOpImpl implements AdapterDocRetrieveProxy
{

    /**
     * Retrieve the specified document.
     *
     * @param request The identifier(s) if the document(s) to be retrieved.
     * @param assertion The assertion information.
     * @return The retrieved documents.
     */
    public RetrieveDocumentSetResponseType retrieveDocumentSet(RetrieveDocumentSetRequestType request, AssertionType assertion)
    {
        return new RetrieveDocumentSetResponseType();
    }
    
}
