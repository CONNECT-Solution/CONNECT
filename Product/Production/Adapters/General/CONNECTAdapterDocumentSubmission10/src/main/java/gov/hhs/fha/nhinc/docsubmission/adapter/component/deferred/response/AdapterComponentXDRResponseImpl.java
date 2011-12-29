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
package gov.hhs.fha.nhinc.docsubmission.adapter.component.deferred.response;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.AdapterRegistryResponseType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import javax.xml.ws.WebServiceContext;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author JHOPPESC
 */
public class AdapterComponentXDRResponseImpl
{
   private static Log log = LogFactory.getLog(AdapterComponentXDRResponseImpl.class);

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
   public XDRAcknowledgementType provideAndRegisterDocumentSetBResponse(AdapterRegistryResponseType body, WebServiceContext context)
    {
        log.debug("Entering AdapterComponentXDRResponseImpl.provideAndRegisterDocumentSetBResponse");

        AssertionType assertion = null;
        RegistryResponseType regResponse = null;
        if (body != null)
        {
            regResponse = body.getRegistryResponse();
            assertion = body.getAssertion();
        }

        // Load any information from the web service context into the assertion.
        //----------------------------------------------------------------------
        loadAssertion(context, assertion);

        AdapterComponentDocSubmissionResponseOrchImpl oOrchestrator = new AdapterComponentDocSubmissionResponseOrchImpl();
        XDRAcknowledgementType response = oOrchestrator.provideAndRegisterDocumentSetBResponse(regResponse, assertion);

        // Send response back to the initiating Gateway
        log.debug("Exiting AdapterComponentXDRResponseImpl.provideAndRegisterDocumentSetBResponse");
        return response;
    }

}
