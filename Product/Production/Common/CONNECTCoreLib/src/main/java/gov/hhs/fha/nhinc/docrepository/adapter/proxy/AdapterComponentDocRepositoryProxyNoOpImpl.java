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
package gov.hhs.fha.nhinc.docrepository.adapter.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author svalluripalli
 */
public class AdapterComponentDocRepositoryProxyNoOpImpl implements AdapterComponentDocRepositoryProxy {

    private static Log log = LogFactory.getLog(AdapterComponentDocRepositoryProxyNoOpImpl.class);

    public RetrieveDocumentSetResponseType retrieveDocument(RetrieveDocumentSetRequestType request, AssertionType assertion) {
        log.debug("Using NoOp Implementation for Adapter Component Doc Repository Service");
        return new RetrieveDocumentSetResponseType();
    }
}
