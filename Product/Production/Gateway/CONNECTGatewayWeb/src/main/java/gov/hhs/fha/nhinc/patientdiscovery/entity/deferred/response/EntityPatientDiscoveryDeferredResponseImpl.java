/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.patientdiscovery.entity.deferred.response;

import gov.hhs.fha.nhinc.async.AsyncMessageIdExtractor;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import java.util.List;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02SecuredRequestType;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02RequestType;
import org.hl7.v3.MCCIIN000002UV01;
import javax.xml.ws.WebServiceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.PRPAIN201306UV02;

/**
 *
 * @author Neil Webb
 */
public class EntityPatientDiscoveryDeferredResponseImpl
{
    private Log log = null;

    public EntityPatientDiscoveryDeferredResponseImpl()
    {
        log = createLogger();
    }

    protected Log createLogger()
    {
        return LogFactory.getLog(getClass());
    }

    public MCCIIN000002UV01 processPatientDiscoveryAsyncResp(RespondingGatewayPRPAIN201306UV02SecuredRequestType request, WebServiceContext context)
    {
        log.debug("Begin EntityPatientDiscoveryDeferredResponseImpl.processPatientDiscoveryAsyncResp(secured)");
        MCCIIN000002UV01 response = null;
        AssertionType assertion = getAssertion(context, null);
        PRPAIN201306UV02 body = null;
        NhinTargetCommunitiesType target = null;
        if(request != null)
        {
            body = request.getPRPAIN201306UV02();
            target = request.getNhinTargetCommunities();
        }
        response = new EntityPatientDiscoveryDeferredResponseOrchImpl().processPatientDiscoveryAsyncRespOrch(body, assertion, target);
        log.debug("End EntityPatientDiscoveryDeferredResponseImpl.processPatientDiscoveryAsyncResp(secured)");
        return response;
    }

    public MCCIIN000002UV01 processPatientDiscoveryAsyncResp(RespondingGatewayPRPAIN201306UV02RequestType request, WebServiceContext context)
    {
        log.debug("Begin EntityPatientDiscoveryDeferredResponseImpl.processPatientDiscoveryAsyncResp(unsecured)");
        MCCIIN000002UV01 response = null;
        AssertionType assertion = null;
        PRPAIN201306UV02 body = null;
        NhinTargetCommunitiesType target = null;
        if(request != null)
        {
            body = request.getPRPAIN201306UV02();
            assertion = request.getAssertion();
            target = request.getNhinTargetCommunities();
        }
        assertion = getAssertion(context, assertion);
        response = new EntityPatientDiscoveryDeferredResponseOrchImpl().processPatientDiscoveryAsyncRespOrch(body, assertion, target);
        log.debug("End EntityPatientDiscoveryDeferredResponseImpl.processPatientDiscoveryAsyncResp(unsecured)");
        return response;
    }

    private AssertionType getAssertion(WebServiceContext context, AssertionType oAssertionIn)
    {
        AssertionType assertion = null;
        if (oAssertionIn == null)
        {
            assertion = SamlTokenExtractor.GetAssertion(context);
        }
        else
        {
            assertion = oAssertionIn;
        }

        // Extract the message id value from the WS-Addressing Header and place it in the Assertion Class
        if (assertion != null)
        {
            assertion.setMessageId(AsyncMessageIdExtractor.GetAsyncMessageId(context));
            List<String> relatesToList = AsyncMessageIdExtractor.GetAsyncRelatesTo(context);
            if (NullChecker.isNotNullish(relatesToList))
            {
                assertion.getRelatesToList().add(AsyncMessageIdExtractor.GetAsyncRelatesTo(context).get(0));
            }
        }

        return assertion;
    }
}
