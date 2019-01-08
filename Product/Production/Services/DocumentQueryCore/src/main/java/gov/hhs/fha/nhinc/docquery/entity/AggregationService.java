/*
 * Copyright (c) 2009-2019, United States Government, as represented by the Secretary of Health and Human Services.
 * All rights reserved.
 *  
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above
 *       copyright notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the documentation
 *       and/or other materials provided with the distribution.
 *     * Neither the name of the United States Government nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package gov.hhs.fha.nhinc.docquery.entity;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommon.QualifiedSubjectIdentifierType;
import gov.hhs.fha.nhinc.common.patientcorrelationfacade.RetrievePatientCorrelationsRequestType;
import gov.hhs.fha.nhinc.connectmgr.UrlInfo;
import gov.hhs.fha.nhinc.docquery.DQMessageGeneratorUtils;
import gov.hhs.fha.nhinc.docquery.outbound.StandardOutboundDocQueryHelper;
import gov.hhs.fha.nhinc.document.DocumentConstants;
import gov.hhs.fha.nhinc.event.error.ErrorEventException;
import gov.hhs.fha.nhinc.exchangemgr.ExchangeManager;
import gov.hhs.fha.nhinc.logging.transaction.TransactionLogger;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.orchestration.OutboundOrchestratable;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.parsers.PRPAIN201309UV.PixRetrieveBuilder;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.proxy.PatientCorrelationProxyFactory;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.proxy.PatientCorrelationProxyObjectFactory;
import gov.hhs.fha.nhinc.util.format.PatientIdFormatUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ValueListType;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hl7.v3.II;
import org.hl7.v3.RetrievePatientCorrelationsResponseType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author bhumphrey
 *
 */
public class AggregationService {

    private ExchangeManager exchangeManager;
    private static final Logger LOG = LoggerFactory.getLogger(AggregationService.class);
    private PatientCorrelationProxyFactory patientCorrelationProxyFactory;

    private OutboundDocQueryDelegate delegate = new OutboundDocQueryDelegate();
    private PixRetrieveBuilder pixRetrieveBuilder;
    private StandardOutboundDocQueryHelper standardOutboundDocQueryHelper;
    private TransactionLogger transactionLogger;

    /**
     * @param sHomeCommunity
     * @param connectionManager
     * @param patientCorrelationProxy
     */
    AggregationService(ExchangeManager exchangeManager,
        PatientCorrelationProxyFactory patientCorrelationProxyFactory, PixRetrieveBuilder pixRetrieveBuilder,
        StandardOutboundDocQueryHelper standardOutboundDocQueryHelper, TransactionLogger transactionLogger) {
        super();
        this.exchangeManager = exchangeManager;
        this.patientCorrelationProxyFactory = patientCorrelationProxyFactory;
        this.pixRetrieveBuilder = pixRetrieveBuilder;
        this.standardOutboundDocQueryHelper = standardOutboundDocQueryHelper;
        this.transactionLogger = transactionLogger;

    }

    /**
     * default constructor.
     */
    public AggregationService() {

        exchangeManager = ExchangeManager.getInstance();
        patientCorrelationProxyFactory = new PatientCorrelationProxyObjectFactory();
        pixRetrieveBuilder = new PixRetrieveBuilder();
        standardOutboundDocQueryHelper = new StandardOutboundDocQueryHelper();
        transactionLogger = new TransactionLogger();
    }

    public List<OutboundOrchestratable> createChildRequests(AdhocQueryRequest adhocQueryRequest,
        AssertionType assertion, NhinTargetCommunitiesType targets) {

        List<OutboundOrchestratable> list = new ArrayList<>();
        String exchangeName = targets.getExchangeName();
        try {

            QualifiedSubjectIdentifierType qualSubId = getQualifiedSubjectIndentifer(adhocQueryRequest);

            RetrievePatientCorrelationsRequestType patientCorrelationReq = new RetrievePatientCorrelationsRequestType();
            patientCorrelationReq.setQualifiedPatientIdentifier(qualSubId);

            patientCorrelationReq.setAssertion(assertion);

            for (UrlInfo urlInfo : exchangeManager.getEndpointURLFromNhinTargetCommunities(targets,
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
                target.setUseSpecVersion(targets.getUseSpecVersion());
                target.setExchangeName(exchangeName);
                AdhocQueryRequest childRequest = cloneRequest(adhocQueryRequest);
                setPatientIdOnRequest(childRequest, id.getExtension(), id.getRoot());

                // set the home community id to the target hcid
                setTargetHomeCommunityId(childRequest, target.getHomeCommunity().getHomeCommunityId());

                AssertionType newAssertion = createNewAssertion(assertion, uniqueIdentities.size());

                transactionLogger.logTransactionFromRelatedMessageId(assertion.getMessageId(),
                    newAssertion.getMessageId());

                OutboundDocQueryOrchestratable orchestratable = new OutboundDocQueryOrchestratable(delegate,
                    newAssertion, NhincConstants.DOC_QUERY_SERVICE_NAME, target, childRequest);

                list.add(orchestratable);
            }
        } catch (Exception e) {
            AdhocQueryResponse response = DQMessageGeneratorUtils.getInstance()
                .createAdhocQueryErrorResponse("XDSRegistryError", "Unable to create fanout requests for query",
                DocumentConstants.XDS_QUERY_RESPONSE_STATUS_FAILURE);
            throw new ErrorEventException(e, response,"Unable to create fanout requests for query");
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
            if (NhincConstants.EBXML_DOCENTRY_PATIENT_ID.equals(slot.getName())) {
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
            if (!sTargetHomeCommunityId.startsWith(NhincConstants.HCID_PREFIX)) {
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
        return DQMessageGeneratorUtils.getInstance().clone(request);
    }

    /**
     * Creates a new assertion with a new message id if there's more than one outbound targets. Otherwise use the same
     * message id passed into the assertion.
     *
     * @param assertion - the assertion to clone
     * @param numTargets - number of outbound targets
     * @return a cloned Assertion with the same message id if numTargets == 1, and a new message id otherwise
     */
    private AssertionType createNewAssertion(AssertionType assertion, int numTargets) {
        AssertionType newAssertion;
        if (numTargets == 1) {
            newAssertion = DQMessageGeneratorUtils.getInstance().clone(DQMessageGeneratorUtils.getInstance().generateMessageId(assertion));
        } else {
            newAssertion = DQMessageGeneratorUtils.getInstance().cloneWithNewMsgId(assertion);
        }

        return newAssertion;
    }

    protected Set<II> removeDuplicates(List<II> iiArray) {
        // remove duplicates
        Set<ComparableII> comparableIISet = new HashSet<>();
        for (II ii : iiArray) {
            comparableIISet.add(new ComparableII(ii));
        }

        // restore original instances
        Set<II> iiSet = new HashSet<>();
        for (ComparableII comparableII : comparableIISet) {
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
            return new HashCodeBuilder(17, 37)
                .append(ii.getExtension())
                .append(ii.getRoot())
                .append(ii.getAssigningAuthorityName())
                .append(ii.isDisplayable())
                .toHashCode();
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
