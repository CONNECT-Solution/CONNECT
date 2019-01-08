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
package gov.hhs.fha.nhinc.docquery.outbound;

import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCommunityMapping;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.util.format.PatientIdFormatUtil;
import java.util.List;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jhoppesc
 */
public class StandardOutboundDocQueryHelper {

    private static final Logger LOG = LoggerFactory.getLogger(StandardOutboundDocQueryHelper.class);
    private ConnectionManagerCommunityMapping connectionManagerCommunityMapping;
    private String sHomeCommunity;

    /**
     * Add default Constructor.
     */
    public StandardOutboundDocQueryHelper() {
        connectionManagerCommunityMapping = new ConnectionManagerCommunityMapping();
        try {
            this.sHomeCommunity = PropertyAccessor.getInstance().getProperty(NhincConstants.GATEWAY_PROPERTY_FILE,
                NhincConstants.HOME_COMMUNITY_ID_PROPERTY);
        } catch (Exception ex) {
            LOG.error("Error getting HCID: {}", ex.getLocalizedMessage(), ex);
        }
    }

    /**
     * @param hcid
     * @param connectionManagerCommunityMapping
     */
    public StandardOutboundDocQueryHelper(String hcid, ConnectionManagerCommunityMapping connectionManagerCommunityMapping) {
        this.sHomeCommunity = hcid;
        this.connectionManagerCommunityMapping = connectionManagerCommunityMapping;
    }

    /**
     * Return target communities to send DocQuery request.
     *
     * @param sAssigningAuthorityId Target AssigningAuthorityId.
     * @param sLocalAssigningAuthorityId AA Id of requesting Gateway.
     * @return targetCommunity where request needs to be targeted.
     */
    public HomeCommunityType lookupHomeCommunityId(String sAssigningAuthorityId, String sLocalAssigningAuthorityId) {
        HomeCommunityType targetCommunity;
        if ((sAssigningAuthorityId != null) && (sAssigningAuthorityId.equals(sLocalAssigningAuthorityId))) {
            /*
             * If the target is the local home community, the local assigning authority may not be mapped to the local
             * home community in the community mapping. Set manually.
             */
            targetCommunity = new HomeCommunityType();
            targetCommunity.setHomeCommunityId(sHomeCommunity);
            LOG.debug("Assigning authority was for the local home community. "
                + "Set target to manual local home community id");
        } else {
            targetCommunity = connectionManagerCommunityMapping
                .getHomeCommunityByAssigningAuthority(sAssigningAuthorityId);
        }
        return targetCommunity;
    }

    /**
     * Returns Local AssigningAuthorityId.
     *
     * @param slotList slotList extracted from AdhocQuery Request message.
     * @return AssigingAuthorityId of local HomeCommunity.
     */
    public String getLocalAssigningAuthority(List<SlotType1> slotList) {
        String localAssigningAuthorityId = null;

        // For each slot process each of the Patient Id slots
        for (SlotType1 slot : slotList) {
            if (slot.getName().equalsIgnoreCase(NhincConstants.DOC_QUERY_XDS_PATIENT_ID_SLOT_NAME)) {
                if (slot.getValueList() != null && NullChecker.isNotNullish(slot.getValueList().getValue())
                    && NullChecker.isNotNullish(slot.getValueList().getValue().get(0))) {
                    localAssigningAuthorityId = PatientIdFormatUtil.parseCommunityId(slot.getValueList().getValue()
                        .get(0));
                }
                break;
            }
        }

        return localAssigningAuthorityId;
    }

    /**
     * This method returns uniquePatientId from slot list.
     *
     * @param slotList The slotList from AdhocQuery Request message.
     * @return uniquePatientId extracted form slotList.
     */
    public String getUniquePatientId(List<SlotType1> slotList) {
        String uniquePatientId = null;

        // For each slot process each of the Patient Id slots
        for (SlotType1 slot : slotList) {
            if (NhincConstants.DOC_QUERY_XDS_PATIENT_ID_SLOT_NAME.equalsIgnoreCase(slot.getName())) {
                if (slot.getValueList() != null && NullChecker.isNotNullish(slot.getValueList().getValue())
                    && NullChecker.isNotNullish(slot.getValueList().getValue().get(0))) {

                    uniquePatientId = PatientIdFormatUtil.parsePatientId(slot.getValueList().getValue().get(0));
                }
                break;
            }
        }

        return uniquePatientId;
    }

    /**
     * @param slotList The slotList from AdhocQueryRequest message.
     * @return true if patientId slot id present or false if not present.
     */
    protected boolean patientIdSlot(List<SlotType1> slotList) {
        boolean slotPresent = false;
        for (SlotType1 slot1 : slotList) {
            if (slot1.getName().equalsIgnoreCase(NhincConstants.DOC_QUERY_XDS_PATIENT_ID_SLOT_NAME)) {
                if (slot1.getValueList() != null) {
                    if (NullChecker.isNotNullish(slot1.getValueList().getValue())) {
                        slotPresent = true;
                        LOG.debug("retreiveCorrelations slot value: " + slot1.getValueList().getValue());
                    }
                } else {
                    LOG.debug("retreiveCorrelations slot1.getValueList(): null");
                }
            } else {
                LOG.debug("retreiveCorrelations " + NhincConstants.DOC_QUERY_XDS_PATIENT_ID_SLOT_NAME + " not found");
            }
        }
        return slotPresent;
    }
}
