/*
 * Copyright (c) 2009-2017, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.exchangemgr;

import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunityType;
import gov.hhs.fha.nhinc.connectmgr.persistance.dao.ExchangeInfoDAOFileImpl;
import gov.hhs.fha.nhinc.exchange.directory.OrganizationType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.UDDI_SPEC_VERSION;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author tjafri
 */
public class ExchangeManagerTest extends BaseExchangeManager {

    private static final String HCID_1 = "urn:oid:1.1";
    private static final String HCID_2 = "urn:oid:2.2";
    private static final String DEFAULT_EXCHANGE = "NationwideExchange";

    @Ignore
    public void testGetOrganization() {
        Exchange<UDDI_SPEC_VERSION> exMgr = createExternalExchangeManager();
        try {
            OrganizationType org = exMgr.getOrganization(HCID_2);
            assertEquals("DefaultExchange does not match", DEFAULT_EXCHANGE, exMgr.getDefaultExchange());
            assertEquals("Organization's HCID does not match", HCID_2, org.getHcid());
        } catch (ExchangeManagerException ex) {
            ex.printStackTrace();
            fail("Error running testGetOrganization: " + ex.getMessage());
        }
    }

    @Ignore
    public void testGetOrganizationByHcids() {
        Exchange<UDDI_SPEC_VERSION> exMgr = createExternalExchangeManagerForMulitpleHcid();
        try {
            List<String> hcids = new ArrayList<>();
            hcids.add(HCID_1);
            hcids.add(HCID_2);
            Set<OrganizationType> orgSet = exMgr.getOrganizationSet(hcids);
            assertEquals("DefaultExchange does not match", null, exMgr.getDefaultExchange());
            assertEquals("Search Result size not correct", hcids.size(), orgSet.size());
        } catch (ExchangeManagerException ex) {
            ex.printStackTrace();
            fail("Error running testGetOrganization: " + ex.getMessage());
        }
    }

    @Test
    public void testEndpointUrl() {
        Exchange<UDDI_SPEC_VERSION> exMgr = createExternalExchangeManager();
        try {
            exMgr.getEndpointURLFromNhinTargetCommunities(createNhinTargetCommunitiesType(HCID_1),
                NhincConstants.PATIENT_DISCOVERY_SERVICE_NAME);
        } catch (ExchangeManagerException ex) {
            fail("Error running testEndpointUrl: " + ex.getMessage());
        }
    }

    private Exchange<UDDI_SPEC_VERSION> createExternalExchangeManager() {
        return new ExchangeManager() {
            @Override
            protected ExchangeInfoDAOFileImpl getExchangeInfoDAO() {
                return createExchangeInfoDAO("/config/ExchangeManagerTest/exchangeInfoTest.xml");
            }
        };
    }

    private Exchange<UDDI_SPEC_VERSION> createExternalExchangeManagerForMulitpleHcid() {
        return new ExchangeManager() {
            @Override
            protected ExchangeInfoDAOFileImpl getExchangeInfoDAO() {
                return createExchangeInfoDAO("/config/ExchangeManagerTest/exchangeInfoMulitpleHcidSearchTesT.xml");
            }
        };
    }

    private NhinTargetCommunitiesType createNhinTargetCommunitiesType(String hcid) {
        NhinTargetCommunitiesType targetCommunities = new NhinTargetCommunitiesType();
        NhinTargetCommunityType targetCommunity = new NhinTargetCommunityType();
        targetCommunity.setHomeCommunity(createHomeCommunity(hcid));
        targetCommunities.setExchangeName("StatewideExchange");
        targetCommunities.getNhinTargetCommunity().add(targetCommunity);
        return targetCommunities;
    }

    private HomeCommunityType createHomeCommunity(String hcid) {
        HomeCommunityType homeCommunity = new HomeCommunityType();
        homeCommunity.setHomeCommunityId(hcid);
        return homeCommunity;
    }
}
