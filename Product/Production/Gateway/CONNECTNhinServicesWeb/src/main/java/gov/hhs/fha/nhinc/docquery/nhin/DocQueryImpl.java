/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.docquery.nhin;

import gov.hhs.fha.nhinc.async.AsyncMessageIdExtractor;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import javax.xml.ws.WebServiceContext;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Neil Webb
 */
public class DocQueryImpl
{
    private Log log = null;

    public DocQueryImpl()
    {
        log = createLogger();
    }

    protected Log createLogger()
    {
        return LogFactory.getLog(getClass());
    }

    public AdhocQueryResponse respondingGatewayCrossGatewayQuery(AdhocQueryRequest body, WebServiceContext context) {
        log.debug("Entering DocQueryImpl.respondingGatewayCrossGatewayQuery");
        
        AssertionType assertion = SamlTokenExtractor.GetAssertion(context);

        // Extract the message id value from the WS-Addressing Header and place it in the Assertion Class
        if (assertion != null) {
            assertion.setMessageId(AsyncMessageIdExtractor.GetAsyncMessageId(context));
        }

        return new NhinDocQueryOrchImpl().respondingGatewayCrossGatewayQuery(body, assertion);
    }

    
}
