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
package gov.hhs.fha.nhinc.docsubmission.adapter.component;

import gov.hhs.fha.nhinc.async.AsyncMessageIdExtractor;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.AdapterProvideAndRegisterDocumentSetRequestType;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import gov.hhs.fha.nhinc.docsubmission.adapter.component.AdapterComponentDocSubmissionOrchImpl;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author dunnek
 */
public class AdapterComponentDocSubmissionImpl {

    public RegistryResponseType provideAndRegisterDocumentSetb(AdapterProvideAndRegisterDocumentSetRequestType body, WebServiceContext context) {
        AssertionType assertion = getAssertion(context, body.getAssertion());

        return new AdapterComponentDocSubmissionOrchImpl().provideAndRegisterDocumentSetB(body.getProvideAndRegisterDocumentSetRequest(), assertion);
    }

    public RegistryResponseType provideAndRegisterDocumentSetb(ProvideAndRegisterDocumentSetRequestType body, WebServiceContext context) {
        AssertionType assertion = getAssertion(context, null);

        return new AdapterComponentDocSubmissionOrchImpl().provideAndRegisterDocumentSetB(body, assertion);
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
