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

package gov.hhs.fha.nhinc.mpi.adapter;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.logger.ConnectLogFactory;
import gov.hhs.fha.nhinc.logger.TransactionType;
import gov.hhs.fha.nhinc.logger.defaulttransaction.DefaultTransactionLog;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;

import javax.xml.ws.WebServiceContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;

/**
 *
 * @author Jon Hoppesch
 */

public class AdapterMpiImpl {
   private static Log log = LogFactory.getLog(AdapterMpiImpl.class);
   private DefaultTransactionLog transactionLog;

    /**
     * Perform a look up on the MPI.
     *
     * @param bIsSecure  TRUE if this is being called from a secure web service.
     * @param findCandidatesRequest The information about the patient that is being used for the lookup.
     * @param context The web service context information.
     * @return The results of the lookup.
     */
     
    public  PRPAIN201306UV02 query(PRPAIN201305UV02 findCandidatesRequest, AssertionType assertionFromBody)
    {
        log.debug("Entering AdapterMpiImpl.query");

        AssertionType assertion = (assertionFromBody != null) ? assertionFromBody : new AssertionType();
        
        transactionLog = (DefaultTransactionLog) ConnectLogFactory.getTransactionLog(
        		assertion.getMessageId(), TransactionType.PD_ADAPTER_TRANSACTION);
        transactionLog.begin();

        AdapterMpiOrchImpl oOrchestrator = new AdapterMpiOrchImpl();
        PRPAIN201306UV02 response = oOrchestrator.query(findCandidatesRequest, assertion);

        // Send response back to the initiating Gateway
        log.debug("Exiting AdapterMpiImpl.query - unsecured");
        transactionLog.end(NhincConstants.FINISHED);
        return response;
    }
 
    public  PRPAIN201306UV02 query(boolean bIsSecure, PRPAIN201305UV02 findCandidatesRequest, WebServiceContext context)
    {
        log.debug("Entering AdapterMpiImpl.query - secured");

        AssertionType assertion = ((bIsSecure) && (context != null)) ?
        		SamlTokenExtractor.GetAssertion(context) : new AssertionType();
        
        transactionLog = (DefaultTransactionLog) ConnectLogFactory.getTransactionLog(
        		assertion.getMessageId(), TransactionType.PD_ADAPTER_TRANSACTION);
        transactionLog.begin();

        AdapterMpiOrchImpl oOrchestrator = new AdapterMpiOrchImpl();
        PRPAIN201306UV02 response = oOrchestrator.query(findCandidatesRequest, assertion);

        // Send response back to the initiating Gateway
        log.debug("Exiting AdapterMpiImpl.query - secured");
        transactionLog.end(NhincConstants.FINISHED);
        return response;
    }
}
