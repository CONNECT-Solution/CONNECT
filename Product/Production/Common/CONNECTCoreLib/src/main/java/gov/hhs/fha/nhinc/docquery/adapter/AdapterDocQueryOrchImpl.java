/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.docquery.adapter;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.docregistry.adapter.proxy.AdapterComponentDocRegistryProxy;
import gov.hhs.fha.nhinc.docregistry.adapter.proxy.AdapterComponentDocRegistryProxyObjectFactory;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.perfrepo.PerformanceManager;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.redactionengine.adapter.proxy.AdapterRedactionEngineProxy;
import gov.hhs.fha.nhinc.redactionengine.adapter.proxy.AdapterRedactionEngineProxyObjectFactory;
import gov.hhs.fha.nhinc.util.HomeCommunityMap;
import java.sql.Timestamp;
import java.util.List;
import javax.xml.bind.JAXBElement;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.IdentifiableType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectRefType;
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

    /**
     *
     * @param request
     * @param assertion
     * @return AdhocQueryResponse
     */
    public AdhocQueryResponse respondingGatewayCrossGatewayQuery(AdhocQueryRequest request, AssertionType assertion) {
        log.debug("Enter AdapterDocQueryOrchImpl.respondingGatewayCrossGatewayQuery()");
        AdhocQueryResponse response = null;
        try {
            if (request != null) {
                // Log the start of the adapter performance record
                String homeCommunityId = HomeCommunityMap.getLocalHomeCommunityId();
                Timestamp starttime = new Timestamp(System.currentTimeMillis());
                Long logId = PerformanceManager.getPerformanceManagerInstance().logPerformanceStart(starttime, NhincConstants.DOC_QUERY_SERVICE_NAME, NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, homeCommunityId);

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

                /*
                 * AEGIS.net, Inc. (c) 2012 - VLER Support
                 * Richard Ettema | 2012-05-01 | CONNECT Gateway 2110
                 * Examine the AdhocQueryResponse for present and valid home attributes
                 */
                log.info("NhinDocQueryOrchImpl.forwardToAgency - Examine the AdhocQueryResponse for present and valid home attributes");
                if (response != null &&
                        response.getRegistryObjectList() != null &&
                        response.getRegistryObjectList().getIdentifiable() != null &&
                        response.getRegistryObjectList().getIdentifiable().size() > 0) {
                    
                    String communityID = PropertyAccessor.getProperty(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.HOME_COMMUNITY_ID_PROPERTY);
                    if (communityID != null && !communityID.startsWith("urn:oid:")) {
                        communityID = "urn:oid:" + communityID;
                    }
                    List<JAXBElement<? extends IdentifiableType>> extrinsicObjects = response.getRegistryObjectList().getIdentifiable();

                    if (extrinsicObjects != null && extrinsicObjects.size() > 0) {
                        for (JAXBElement<? extends IdentifiableType> jaxb : extrinsicObjects) {
                            if (jaxb.getValue() instanceof ObjectRefType) {
                                ObjectRefType objectRef = (ObjectRefType) jaxb.getValue();

                                if (objectRef != null &&
                                        (objectRef.getHome() == null || objectRef.getHome().isEmpty())) {
                                    log.info("NhinDocQueryOrchImpl.forwardToAgency - objectRef without valid home attribute found; setting local home community id " + communityID);
                                    objectRef.setHome(communityID);
                                }
                            }
                            if (jaxb.getValue() instanceof ExtrinsicObjectType) {
                                ExtrinsicObjectType extrinsicObject = (ExtrinsicObjectType) jaxb.getValue();

                                if (extrinsicObject != null &&
                                        (extrinsicObject.getHome() == null || extrinsicObject.getHome().isEmpty())) {
                                    log.info("NhinDocQueryOrchImpl.forwardToAgency - extrinsicObject without valid home attribute found; setting local home community id " + communityID);
                                    extrinsicObject.setHome(communityID);
                                }
                            }
                        }
                    }
                }
                // Log the end of the adapter performance record
                Timestamp stoptime = new Timestamp(System.currentTimeMillis());
                PerformanceManager.getPerformanceManagerInstance().logPerformanceStop(logId, starttime, stoptime);
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
