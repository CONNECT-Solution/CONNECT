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

package gov.hhs.fha.nhinc.docsubmission.adapter;

import gov.hhs.fha.nhinc.async.AsyncMessageIdExtractor;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.AdapterProvideAndRegisterDocumentSetRequestType;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import javax.xml.ws.WebServiceContext;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

/**
 *
 * @author JHOPPESC
 */
public class AdapterDocSubmissionImpl {
    
    public RegistryResponseType provideAndRegisterDocumentSetb(ProvideAndRegisterDocumentSetRequestType body, WebServiceContext context) {
        AssertionType assertion = getAssertion(context, null);

        return new AdapterDocSubmissionOrchImpl().provideAndRegisterDocumentSetB(body, assertion);
    }

    public RegistryResponseType provideAndRegisterDocumentSetb(AdapterProvideAndRegisterDocumentSetRequestType body, WebServiceContext context) {
        AssertionType assertion = getAssertion(context, body.getAssertion());

        return new AdapterDocSubmissionOrchImpl().provideAndRegisterDocumentSetB(body.getProvideAndRegisterDocumentSetRequest(), assertion);
    }
    
    private AssertionType getAssertion(WebServiceContext context, AssertionType oAssertionIn) {
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
