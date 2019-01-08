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
package gov.hhs.fha.nhinc.connectmgr;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunityType;
import gov.hhs.fha.nhinc.connectmgr.persistance.dao.InternalConnectionInfoDAOFileImpl;
import gov.hhs.fha.nhinc.connectmgr.persistance.dao.UddiConnectionInfoDAOFileImpl;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.UDDI_SPEC_VERSION;
import org.junit.Test;

/**
 * @author msw
 *
 */
public class ConnectionManagerCacheGuidanceTest extends BaseConnctionManagerCache {

    protected ConnectionManagerCache createConnectionManager() throws ConnectionManagerException {
        return new ConnectionManagerCache() {
            @Override
            protected UddiConnectionInfoDAOFileImpl getUddiConnectionManagerDAO() {
                return createUddiConnectionInfoDAO("/config/ConnectionManagerCacheTest/uddiConnectionInfoTest.xml");
            }

            @Override
            protected InternalConnectionInfoDAOFileImpl getInternalConnectionManagerDAO() {
                return createInternalConnectionInfoDAO(
                        "/config/ConnectionManagerCacheTest/internalConnectionInfoTest.xml");
            }
        };
    }

    @Test
    public void testDQNoMatchingTargetEndpoint20Guidance() {
        try {
            ConnectionManagerCache connectionManager = createConnectionManager();

            connectionManager.getEndpointURLByServiceNameSpecVersion(HCID_1, QUERY_FOR_DOCUMENTS_NAME,
                    UDDI_SPEC_VERSION.fromString("2.0"));

            fail();
        } catch (Throwable t) {
            assertTrue(t.getMessage().contains("No matching target endpoint for guidance: 2.0"));
        }
    }

    @Test
    public void testDQNoMatchingTargetEndpoint30Guidance() {
        try {
            ConnectionManagerCache connectionManager = createConnectionManager();

            connectionManager.getEndpointURLByServiceNameSpecVersion(HCID_1, QUERY_FOR_DOCUMENTS_NAME,
                    UDDI_SPEC_VERSION.fromString("3.0"));

            fail();
        } catch (Throwable t) {
            assertTrue(t.getMessage().contains("No matching target endpoint for guidance: 3.0"));
        }
    }

    @Test
    public void testDRNoMatchingTargetEndpoint20Guidance() {
        try {
            ConnectionManagerCache connectionManager = createConnectionManager();

            connectionManager.getEndpointURLByServiceNameSpecVersion(HCID_1, RETRIEVE_DOCUMENTS_NAME,
                    UDDI_SPEC_VERSION.fromString("2.0"));
            fail();
        } catch (Throwable t) {
            assertTrue(t.getMessage().contains("No matching target endpoint for guidance: 2.0"));
        }
    }

    @Test
    public void testDRNoMatchingTargetEndpoint30Guidance() {
        try {
            ConnectionManagerCache connectionManager = createConnectionManager();

            connectionManager.getEndpointURLByServiceNameSpecVersion(HCID_1, RETRIEVE_DOCUMENTS_NAME,
                    UDDI_SPEC_VERSION.fromString("3.0"));

            fail();
        } catch (Throwable t) {
            assertTrue(t.getMessage().contains("No matching target endpoint for guidance: 3.0"));
        }
    }

    protected NhinTargetCommunitiesType createNhinTargetCommunites(String guidance) {
        NhinTargetCommunitiesType targetCommunities = new NhinTargetCommunitiesType();
        NhinTargetCommunityType targetCommunity = new NhinTargetCommunityType();
        HomeCommunityType homeCommunity = new HomeCommunityType();
        homeCommunity.setHomeCommunityId(HCID_1);
        targetCommunity.setHomeCommunity(homeCommunity);
        targetCommunity.setRegion(FL_REGION_VALUE);
        targetCommunity.setList("Unimplemented");
        targetCommunities.getNhinTargetCommunity().add(targetCommunity);
        targetCommunities.setUseSpecVersion(guidance);

        return targetCommunities;
    }

}
