/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.docquery.nhin.deferred.response;

import gov.hhs.healthit.nhin.DocQueryAcknowledgementType;
import javax.xml.ws.WebServiceContext;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import gov.hhs.fha.nhinc.service.WebServiceHelper;

/**
 *
 * @author jhoppesc
 */
public class NhinDocQueryDeferredResponseImpl {

    private static final Log log = LogFactory.getLog(NhinDocQueryDeferredResponseImpl.class);

    public DocQueryAcknowledgementType respondingGatewayCrossGatewayQuery(AdhocQueryResponse body, WebServiceContext context) {
        DocQueryAcknowledgementType response = null;
        WebServiceHelper oHelper = new WebServiceHelper();
        NhinDocQueryDeferredResponseOrchImpl proxy = new NhinDocQueryDeferredResponseOrchImpl();
        try {
            if (body != null) {
                response = (DocQueryAcknowledgementType) oHelper.invokeSecureDeferredResponseWebService(proxy, proxy.getClass(), "respondingGatewayCrossGatewayQuery", body, context);
            } else {
                log.error("Failed to call the web orchestration (" + proxy.getClass() + ".respondingGatewayCrossGatewayQuery).  The input parameter is null.");
            }
        } catch (Exception e) {
            log.error("Failed to call the web orchestration (" + proxy.getClass() + ".respondingGatewayCrossGatewayQuery).  An unexpected exception occurred.  " +
                    "Exception: " + e.getMessage(), e);
        }

        return response;

    }
}
