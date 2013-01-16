/**
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services.
 * All rights reserved.
 *  
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *   * Redistributions of source code must retain the above
 *     copyright notice, this list of conditions and the following disclaimer.
 *   * Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the documentation
 *     and/or other materials provided with the distribution.
 *   * Neither the name of the United States Government nor the
 *     names of its contributors may be used to endorse or promote products
 *     derived from this software without specific prior written permission.
 *
 *THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 *ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY
 *DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package gov.hhs.fha.nhinc.docquery.entity;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommon.QualifiedSubjectIdentifierType;
import gov.hhs.fha.nhinc.common.patientcorrelationfacade.RetrievePatientCorrelationsRequestType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManager;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.connectmgr.UrlInfo;
import gov.hhs.fha.nhinc.docquery.MessageGeneratorUtils;
import gov.hhs.fha.nhinc.docquery.outbound.StandardOutboundDocQueryHelper;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.orchestration.OutboundOrchestratable;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.parsers.PRPAIN201309UV.PixRetrieveBuilder;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.proxy.PatientCorrelationProxyFactory;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.proxy.PatientCorrelationProxyObjectFactory;
import gov.hhs.fha.nhinc.transform.document.DocumentTransformConstants;
import gov.hhs.fha.nhinc.util.format.PatientIdFormatUtil;
import gov.hhs.fha.nhinc.wsa.WSAHeaderHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ValueListType;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.log4j.Logger;
import org.hl7.v3.II;
import org.hl7.v3.RetrievePatientCorrelationsResponseType;

/**
 * @author bhumphrey
 * 
 */
public class AggregationService {

    private ConnectionManager connectionManager;
    private static final Logger LOG = Logger.getLogger(AggregationService.class);
    private PatientCorrelationProxyFactory patientCorrelationProxyFactory;

    private OutboundDocQueryDelegate delegate = new OutboundDocQueryDelegate();
    private PixRetrieveBuilder pixRetrieveBuilder;
    private StandardOutboundDocQueryHelper standardOutboundDocQueryHelper;

    /**
     * @param sHomeCommunity
     * @param connectionManager
     * @param patientCorrelationProxy
     */
    AggregationService(ConnectionManager connectionManager,
            PatientCorrelationProxyFactory patientCorrelationProxyFactory, PixRetrieveBuilder pixRetrieveBuilder,
            StandardOutboundDocQueryHelper standardOutboundDocQueryHelper) {
        super();
        this.connectionManager = connectionManager;
        this.patientCorrelationProxyFactory = patientCorrelationProxyFactory;
        this.pixRetrieveBuilder = pixRetrieveBuilder;
        this.standardOutboundDocQueryHelper = standardOutboundDocQueryHelper;
    }

    /**
     * default constructor.
     */
    public AggregationService() {

        this.connectionManager = ConnectionManagerCache.getInstance();
        this.patientCorrelationProxyFactory = new PatientCorrelationProxyObjectFactory();
        this.pixRetrieveBuilder = new PixRetrieveBuilder();
        this.standardOutboundDocQueryHelper = new StandardOutboundDocQueryHelper();
    }

    public List<OutboundOrchestratable> createChildRequests(AdhocQueryRequest adhocQueryRequest,
            AssertionType assertion, NhinTargetCommunitiesType targets) {
        List<OutboundOrchestratable> list = new ArrayList<OutboundOrchestratable>();

        try {

            QualifiedSubjectIdentifierType qualSubId = getQualifiedSubjectIndentifer(adhocQueryRequest);

            RetrievePatientCorrelationsRequestType patientCorrelationReq = new RetrievePatientCorrelationsRequestType();
            patientCorrelationReq.setQualifiedPatientIdentifier(qualSubId);

            patientCorrelationReq.setAssertion(assertion);

            for (UrlInfo urlInfo : connectionManager.getEndpointURLFromNhinTargetCommunities(targets,
                    NhincConstants.DOC_QUERY_SERVICE_NAME)) {
                patientCorrelationReq.getTargetHomeCommunity().add(urlInfo.getHcid());
            }

            RetrievePatientCorrelationsResponseType results = patientCorrelationProxyFactory
                    .getPatientCorrelationProxy().retrievePatientCorrelations(
                            pixRetrieveBuilder.createPixRetrieve(patientCorrelationReq), assertion);

            List<II> identities = Collections.emptyList();
            if (hasIdentities(results)) {
                identities = results.getPRPAIN201310UV02().getControlActProcess().getSubject().get(0)
                        .getRegistrationEvent().getSubject1().getPatient().getId();
            }

            Set<II> uniqueIdentities = removeDuplicates(identities);

            for (II id : uniqueIdentities) {
                NhinTargetSystemType target = new NhinTargetSystemType();
                HomeCommunityType targetCommunity = standardOutboundDocQueryHelper.lookupHomeCommunityId(id.getRoot(),
                        qualSubId.getAssigningAuthorityIdentifier());
                target.setHomeCommunity(targetCommunity);

                AdhocQueryRequest childRequest = cloneRequest(adhocQueryRequest);
                setPatientIdOnRequest(childRequest, id.getExtension(), id.getRoot());

                // set the home community id to the target hcid
                setTargetHomeCommunityId(childRequest, target.getHomeCommunity().getHomeCommunityId());
                                
                AssertionType newAssertion = cloneAssertionWithNewMsgId(assertion);

                OutboundDocQueryOrchestratable orchestratable = new OutboundDocQueryOrchestratable(delegate, newAssertion,
                        NhincConstants.DOC_QUERY_SERVICE_NAME, target, childRequest);

                list.add(orchestratable);
            }

        } catch (ConnectionManagerException e) {
            LOG.error(e);
        }

        return list;
    }
    
    protected boolean hasIdentities(RetrievePatientCorrelationsResponseType results) {
        return results != null
                && results.getPRPAIN201310UV02() != null
                && results.getPRPAIN201310UV02().getControlActProcess() != null
                && NullChecker.isNotNullish(results.getPRPAIN201310UV02().getControlActProcess().getSubject())
                && results.getPRPAIN201310UV02().getControlActProcess().getSubject().get(0) != null
                && results.getPRPAIN201310UV02().getControlActProcess().getSubject().get(0).getRegistrationEvent() != null
                && results.getPRPAIN201310UV02().getControlActProcess().getSubject().get(0).getRegistrationEvent()
                        .getSubject1() != null
                && results.getPRPAIN201310UV02().getControlActProcess().getSubject().get(0).getRegistrationEvent()
                        .getSubject1().getPatient() != null
                && NullChecker.isNotNullish(results.getPRPAIN201310UV02().getControlActProcess().getSubject().get(0)
                        .getRegistrationEvent().getSubject1().getPatient().getId());
    }

    /**
     * @param adhocQueryRequest
     * @return
     */
    protected QualifiedSubjectIdentifierType getQualifiedSubjectIndentifer(AdhocQueryRequest adhocQueryRequest) {
        QualifiedSubjectIdentifierType qualSubId = new QualifiedSubjectIdentifierType();
        List<SlotType1> slotList = adhocQueryRequest.getAdhocQuery().getSlot();
        String localAA = new StandardOutboundDocQueryHelper().getLocalAssigningAuthority(slotList);
        String uniquePatientId = new StandardOutboundDocQueryHelper().getUniquePatientId(slotList);
        qualSubId.setSubjectIdentifier(uniquePatientId);
        qualSubId.setAssigningAuthorityIdentifier(localAA);
        LOG.debug("EntityDocQueryOrchImpl uniquePatientId: " + uniquePatientId + " and localAA=" + localAA);
        return qualSubId;
    }

    /**
     * @param request
     * @param subjectIdentifier
     * @param assigningAuthorityIdentifier
     * @return
     */
    protected String setPatientIdOnRequest(AdhocQueryRequest request, String subjectIdentifier,
            String assigningAuthorityIdentifier) {
        String formattedPatientId = PatientIdFormatUtil.hl7EncodePatientId(subjectIdentifier,
                assigningAuthorityIdentifier);

        for (SlotType1 slot : request.getAdhocQuery().getSlot()) {
            if (DocumentTransformConstants.EBXML_DOCENTRY_PATIENT_ID.equals(slot.getName())) {
                ValueListType slotValueList = new ValueListType();
                slotValueList.getValue().add(formattedPatientId);
                slot.setValueList(slotValueList);
            }
        }
        return formattedPatientId;
    }

    /**
     * @param request
     * @param sTargetHomeCommunityId
     * @return
     */
    protected AdhocQueryRequest setTargetHomeCommunityId(AdhocQueryRequest request, String sTargetHomeCommunityId) {
        if (NullChecker.isNotNullish(sTargetHomeCommunityId)) {
            if (!(sTargetHomeCommunityId.startsWith(NhincConstants.HCID_PREFIX))) {
                sTargetHomeCommunityId = NhincConstants.HCID_PREFIX + sTargetHomeCommunityId;
            }
            request.getAdhocQuery().setHome(sTargetHomeCommunityId);
        }
        return request;
    }

    /**
     * @param request AdhocQuery request received.
     * @return AdhocQUery Request.
     */
    protected AdhocQueryRequest cloneRequest(AdhocQueryRequest request) {
        return MessageGeneratorUtils.getInstance().clone(request);
    }
    
    protected AssertionType cloneAssertionWithNewMsgId(AssertionType assertion) {
        AssertionType newAssertion = MessageGeneratorUtils.getInstance().clone(assertion);
        
        newAssertion.setMessageId(new WSAHeaderHelper().generateMessageID());
        
        return newAssertion;
    }
    
    protected Set<II> removeDuplicates(List<II> iiArray) {
        // remove duplicates
        Set<ComparableII> comparableIISet = new HashSet<ComparableII>();
        for (II ii: iiArray) {
            comparableIISet.add(new ComparableII(ii));
        }
        
        // restore original instances
        Set<II> iiSet = new HashSet<II>();
        for (ComparableII comparableII: comparableIISet) {
            iiSet.add(comparableII.getII());
        }
        
        return iiSet;
    }

    /**
     * A class that wraps the II xml object to implement the hashCode() and equals() methods for comparison. 
     */
    private class ComparableII {
        
        private II ii;

        private ComparableII(II ii) {
            this.ii = ii;
        }
        
        public II getII() {
            return ii;
        }

        @Override
        public int hashCode() {
            int hashCode = new HashCodeBuilder(17, 37)
                    .append(ii.getExtension())
                    .append(ii.getRoot())
                    .append(ii.getAssigningAuthorityName())
                    .append(ii.isDisplayable())
                    .toHashCode();
                        
            return hashCode;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (obj == this) {
                return true;
            }
            if (obj.getClass() != getClass()) {
                return false;
            }

            ComparableII otherId = (ComparableII) obj;
            return new EqualsBuilder()
                    .append(ii.getExtension(), otherId.getII().getExtension())
                    .append(ii.getRoot(), otherId.getII().getRoot())
                    .append(ii.getAssigningAuthorityName(), otherId.getII().getAssigningAuthorityName())
                    .append(ii.isDisplayable(), otherId.getII().isDisplayable())
                    .isEquals();
        }

    }
}
