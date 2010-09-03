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

package gov.hhs.fha.nhinc.docquery.adapter;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.docregistry.adapter.proxy.AdapterComponentDocRegistryProxy;
import gov.hhs.fha.nhinc.docregistry.adapter.proxy.AdapterComponentDocRegistryProxyObjectFactory;
import gov.hhs.fha.nhinc.redactionengine.adapter.proxy.AdapterRedactionEngineProxy;
import gov.hhs.fha.nhinc.redactionengine.adapter.proxy.AdapterRedactionEngineProxyObjectFactory;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author jhoppesc
 */
public class AdapterDocQueryOrchImpl {
    private Log log = null;
    private static final String ERROR_CODE_CONTEXT = AdapterDocQueryOrchImpl.class.getName();
    private static final String ERROR_VALUE = "Input has null value";
    private static final String ERROR_SEVERITY = "Error";

    public AdapterDocQueryOrchImpl() {
        log = createLogger();
    }

    protected Log createLogger() {
        return LogFactory.getLog(getClass());
    }

    public AdhocQueryResponse respondingGatewayCrossGatewayQuery(AdhocQueryRequest request, AssertionType assertion) {
        log.debug("Enter AdapterDocQueryOrchImpl.respondingGatewayCrossGatewayQuery()");
        AdhocQueryResponse response = null;
        try {
            if (request != null) {
                AdapterComponentDocRegistryProxyObjectFactory objFactory = new AdapterComponentDocRegistryProxyObjectFactory();
                AdapterComponentDocRegistryProxy registryProxy = objFactory.getAdapterComponentDocRegistryProxy();
                AdhocQueryRequest adhocQueryRequest = new AdhocQueryRequest();
                adhocQueryRequest.setAdhocQuery(request.getAdhocQuery());
                adhocQueryRequest.setResponseOption(request.getResponseOption());
                adhocQueryRequest.setComment(request.getComment());
                adhocQueryRequest.setFederated(request.isFederated());
                adhocQueryRequest.setFederation(request.getFederation());
                adhocQueryRequest.setId(request.getId());
                adhocQueryRequest.setMaxResults(request.getMaxResults());
                adhocQueryRequest.setRequestSlotList(request.getRequestSlotList());
                adhocQueryRequest.setStartIndex(request.getStartIndex());
                response = registryProxy.registryStoredQuery(request, assertion);
                response = callRedactionEngine(request, response, assertion);
            } else {
                RegistryErrorList errorList = new RegistryErrorList();
                response = new AdhocQueryResponse();
                RegistryError e = new RegistryError();
                errorList.getRegistryError().add(e);
                response.setRegistryErrorList(errorList);
                e.setValue(ERROR_VALUE);
                e.setSeverity(ERROR_SEVERITY);
                e.setCodeContext(ERROR_CODE_CONTEXT);
            }
        } catch (Exception exp) {
            log.error(exp.getMessage());
            exp.printStackTrace();
        }
        log.debug("End AdapterDocQueryOrchImpl.respondingGatewayCrossGatewayQuery()");
        return response;

    }

    protected AdhocQueryResponse callRedactionEngine(AdhocQueryRequest queryRequest, AdhocQueryResponse queryResponse, AssertionType assertion) {
        AdhocQueryResponse response = null;
        if (queryResponse == null) {
            log.warn("Did not call redaction engine because the query response was null.");
        } else {
            log.debug("Calling Redaction Engine");
            response = getRedactionEngineProxy().filterAdhocQueryResults(queryRequest, queryResponse, assertion);
        }
        return response;
    }

    protected AdapterRedactionEngineProxy getRedactionEngineProxy() {
        return new AdapterRedactionEngineProxyObjectFactory().getRedactionEngineProxy();
    }

}
