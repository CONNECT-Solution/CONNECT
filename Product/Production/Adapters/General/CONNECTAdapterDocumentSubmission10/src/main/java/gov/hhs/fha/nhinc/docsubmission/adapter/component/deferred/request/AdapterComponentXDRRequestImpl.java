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
package gov.hhs.fha.nhinc.docsubmission.adapter.component.deferred.request;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.AdapterProvideAndRegisterDocumentSetRequestType;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import javax.xml.ws.WebServiceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author JHOPPESC
 */
public class AdapterComponentXDRRequestImpl
{

   private static Log log = LogFactory.getLog(AdapterComponentXDRRequestImpl.class);

   /**
    * Extract information from the context and place it into the assertion.
    *
    * @param context The web service context.
    * @param assertion The assertion information.
    */
   private void loadAssertion(WebServiceContext context, AssertionType assertion)
   {
       // TODO: extract information from context and place it in the assertion.
   }

    /**
     * This method is called by the web service.  It calls the orchestration code.
     *
     * @param body The message payload.
     * @param context The web service context.
     * @return Returns the response from the Adapter patient discovery request.
     */
    public XDRAcknowledgementType provideAndRegisterDocumentSetBRequest(AdapterProvideAndRegisterDocumentSetRequestType body, WebServiceContext context)
    {
        log.debug("Entering AdapterComponentXDRRequestImpl.provideAndRegisterDocumentSetBRequest");

        AssertionType assertion = null;
        ProvideAndRegisterDocumentSetRequestType request = null;
        String url = "";
        if (body != null)
        {
            request = body.getProvideAndRegisterDocumentSetRequest();
            assertion = body.getAssertion();
            url = body.getUrl();
        }

        // Load any information from the web service context into the assertion.
        //----------------------------------------------------------------------
        loadAssertion(context, assertion);

        AdapterComponentDocSubmissionRequestOrchImpl oOrchestrator = new AdapterComponentDocSubmissionRequestOrchImpl();
        XDRAcknowledgementType response = oOrchestrator.provideAndRegisterDocumentSetBRequest(request, assertion, url);

        // Send response back to the initiating Gateway
        log.debug("Exiting AdapterComponentXDRRequestImpl.provideAndRegisterDocumentSetBRequest");
        return response;
    }
}
