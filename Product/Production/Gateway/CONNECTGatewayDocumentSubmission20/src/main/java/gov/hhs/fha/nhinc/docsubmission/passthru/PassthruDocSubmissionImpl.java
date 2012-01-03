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

package gov.hhs.fha.nhinc.docsubmission.passthru;
import gov.hhs.fha.nhinc.async.AsyncMessageIdExtractor;
import javax.xml.ws.WebServiceContext;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetRequestType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import gov.hhs.fha.nhinc.docsubmission.passthru.PassthruDocSubmissionOrchImpl;

/**
 *
 * @author dunnek
 */
public class PassthruDocSubmissionImpl {
    

    public RegistryResponseType provideAndRegisterDocumentSetB(RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType body, WebServiceContext context)
    {
         // Create an assertion class from the contents of the SAML token
        AssertionType assertion = extractAssertionFromContext(context, null);

        return new PassthruDocSubmissionOrchImpl().provideAndRegisterDocumentSetB(body.getProvideAndRegisterDocumentSetRequest(), assertion, body.getNhinTargetSystem());
    }
    
    public RegistryResponseType provideAndRegisterDocumentSetB(RespondingGatewayProvideAndRegisterDocumentSetRequestType body, WebServiceContext context) {
        AssertionType assertion = extractAssertionFromContext(context, body.getAssertion());
        
        return new PassthruDocSubmissionOrchImpl().provideAndRegisterDocumentSetB(body.getProvideAndRegisterDocumentSetRequest(), assertion, body.getNhinTargetSystem());
    }

    protected AssertionType extractAssertionFromContext(WebServiceContext context, AssertionType oAssertionIn) {
        AssertionType assertion = null;
        if (oAssertionIn == null) {
            assertion = SamlTokenExtractor.GetAssertion(context);
        } else {
            assertion = oAssertionIn;
        }
        // Extract the message id value from the WS-Addressing Header and place it in the Assertion Class
        if (assertion != null) {
            assertion.setMessageId(AsyncMessageIdExtractor.GetAsyncMessageId(context));
        }

        return assertion;
    }
    
}
