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
package gov.hhs.fha.nhinc.exchangemgr;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunityType;
import gov.hhs.fha.nhinc.connectmgr.UrlInfo;
import gov.hhs.fha.nhinc.connectmgr.persistance.dao.ExchangeInfoDAOFileImpl;
import gov.hhs.fha.nhinc.exchange.directory.EndpointConfigurationType;
import gov.hhs.fha.nhinc.exchange.directory.EndpointType;
import gov.hhs.fha.nhinc.exchange.directory.OrganizationType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.UDDI_SPEC_VERSION;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.Test;

/**
 *
 * @author tjafri
 */
public class ExchangeManagerTest extends BaseExchangeManager {

    private static final String HCID_1 = "urn:oid:1.1";
    private static final String HCID_2 = "urn:oid:2.2";
    private static final String HCID_3 = "urn:oid:3.3";
    private static final String DEFAULT_EXCHANGE = "NationwideExchange";
    private static final String STATE_EXCHANGE = "StateExchange";
    private static final String PD = "PatientDiscovery";
    private static final String QD = "QueryForDocements";
    private static final String RD = "RetrieveDocuments";

    @Test
    public void testGetOrganization() {
        Exchange<UDDI_SPEC_VERSION> exMgr = createExternalExchangeManager();
        try {
            OrganizationType org = exMgr.getOrganization(HCID_2);
            assertEquals("DefaultExchange does not match", DEFAULT_EXCHANGE, exMgr.getDefaultExchange());
            assertEquals("Organization's HCID does not match", HCID_2, org.getHcid());
        } catch (ExchangeManagerException ex) {
            fail("Error running testGetOrganization: " + ex.getMessage());
        }
    }

    @Test
    public void testGetOrganizationByHcids() {
        Exchange<UDDI_SPEC_VERSION> exMgr = createExternalExchangeManagerForMulitpleExchange();
        try {
            List<String> hcids = new ArrayList<>();
            hcids.add(HCID_1);
            hcids.add(HCID_2);
            Set<OrganizationType> orgSet = exMgr.getOrganizationSet(hcids, null);
            assertEquals("DefaultExchange does not match", null, exMgr.getDefaultExchange());
            assertEquals("Search Result size not correct", hcids.size(), orgSet.size());
        } catch (ExchangeManagerException ex) {
            fail("Error running testGetOrganization: " + ex.getMessage());
        }
    }

    @Test
    public void testEndpointUrl() {
        try {
            Exchange<UDDI_SPEC_VERSION> exMgr = createExternalExchangeManager();
            List<UrlInfo> list = exMgr.getEndpointURLFromNhinTargetCommunities(createNhinTargetCommunitiesType(HCID_1),
                NhincConstants.PATIENT_DISCOVERY_SERVICE_NAME);
            assertTrue(list.size() == 1);
        } catch (ExchangeManagerException ex) {
            fail("Error running testGetOrganization: " + ex.getMessage());
        }
    }

    @Test
    public void testDefaultExchange() {
        Exchange<UDDI_SPEC_VERSION> exMgr = createExternalExchangeManagerForMulitpleHcid();
        try {
            OrganizationType org = exMgr.getOrganization(null, HCID_2);
            assertEquals("DefaultExchange does not match", exMgr.getDefaultExchange(), "StateExchange");
            assertEquals("Organization HCID does not match", org.getHcid(), HCID_2);
        } catch (ExchangeManagerException ex) {
            fail("Error running testDefaultExchange: " + ex.getMessage());
        }
    }

    @Test
    public void testOverrideExchange() {
        Exchange<UDDI_SPEC_VERSION> exMgr = createExternalExchangeManagerForOverride();
        try {
            OrganizationType org = exMgr.getOrganization("3.3");
            assertNotNull("Organization is Null", org);
            verifyOverridesEndpoint(org.getEndpointList().getEndpoint());
        } catch (ExchangeManagerException ex) {
            fail("Error running testOverrideExchange: " + ex.getMessage());
        }
    }

    @Test
    public void testGetAllOrganizationExchangeWithOverrides() {
        Exchange<UDDI_SPEC_VERSION> exMgr = createExternalExchangeManagerForOverride();
        try {
            List<OrganizationType> orgList = exMgr.getAllOrganizations();
            assertEquals("List of Organization returned should be 3", 3, orgList.size());
            for (OrganizationType org : orgList) {
                if (HCID_3.equalsIgnoreCase(org.getHcid())) {
                    verifyOverridesEndpoint(org.getEndpointList().getEndpoint());
                }
            }
        } catch (ExchangeManagerException ex) {
            fail("Error running testOverrideExchange: " + ex.getMessage());
        }
    }

    @Test
    public void testGetAllOrganizationForAnExchangeWithOverrides() {
        Exchange<UDDI_SPEC_VERSION> exMgr = createExternalExchangeManagerForOverride();
        try {
            List<OrganizationType> orgList = exMgr.getAllOrganizations(STATE_EXCHANGE);
            assertEquals("List of Organization returned for default Exchange should be 1", 1, orgList.size());
            verifyOverridesEndpoint(orgList.get(0).getEndpointList().getEndpoint());
        } catch (ExchangeManagerException ex) {
            fail("Error running testOverrideExchange: " + ex.getMessage());
        }
    }

    @Test
    public void testDefaultExchangeDelete() throws IOException, URISyntaxException {
        ExchangeManager exMgr = createExternalExchangeManagerForDeletingDefaultExchange();
        try {
            boolean result = exMgr.deleteExchange("StateExchange");
            //This is called to ensure that the test file is reread and there is no defaultExchange defined in the test file.
            exMgr.getAllExchanges();
            assertTrue("Exchange not deleted", result);
            assertNull("DefaultExchange is not deleted", exMgr.getDefaultExchange());
        } catch (ExchangeManagerException ex) {
            fail("Error running testDefaultExchange: " + ex.getMessage());
        }
    }

    @Test
    public void testInvalidExchange() {
        ExchangeManager exMgr = createExchangeManagerWithInvalidExType();
        try {
            assertTrue(exMgr.getCache().isEmpty());
        } catch (ExchangeManagerException ex) {
            fail("Error running testInvalidExchange: " + ex.getMessage());
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

    private Exchange<UDDI_SPEC_VERSION> createExternalExchangeManagerForMulitpleExchange() {
        return new ExchangeManager() {
            @Override
            protected ExchangeInfoDAOFileImpl getExchangeInfoDAO() {
                return createExchangeInfoDAO("/config/ExchangeManagerTest/exchangeInfoMulitpleHcidSearchTesT.xml");
            }
        };
    }

    private Exchange<UDDI_SPEC_VERSION> createExternalExchangeManagerForMulitpleHcid() {
        return new ExchangeManager() {
            @Override
            protected ExchangeInfoDAOFileImpl getExchangeInfoDAO() {
                return createExchangeInfoDAO(
                    "/config/ExchangeManagerTest/exchangeInfoMulitpleHcidInASingleExchangeTest.xml");
            }
        };
    }

    private ExchangeManager createExternalExchangeManagerForDeletingDefaultExchange() throws IOException, URISyntaxException {

        copyFile( "/config/ExchangeManagerTest/exchangeInfoDefaultExDeleteTest.xml",
            "exchangeInfoDefaultExDeleteTest-TEST.xml");

        return new ExchangeManager() {
            @Override
            protected ExchangeInfoDAOFileImpl getExchangeInfoDAO() {
                return createExchangeInfoDAO(
                    "/config/ExchangeManagerTest/exchangeInfoDefaultExDeleteTest-TEST.xml");
            }
        };
    }

    private ExchangeManager createExternalExchangeManagerForOverride() {
        return new ExchangeManager() {
            @Override
            protected ExchangeInfoDAOFileImpl getExchangeInfoDAO() {
                return createExchangeInfoDAO(
                    "/config/ExchangeManagerTest/exchangeInfoTestWithOverrides.xml");
            }
        };
    }

    private ExchangeManager createExchangeManagerWithInvalidExType() {
        return new ExchangeManager() {
            @Override
            protected ExchangeInfoDAOFileImpl getExchangeInfoDAO() {
                return createExchangeInfoDAO(
                    "/config/ExchangeManagerTest/exchangeInfoWithInvalidType.xml");
            }
        };
    }

    private NhinTargetCommunitiesType createNhinTargetCommunitiesType(String hcid) {
        NhinTargetCommunitiesType targetCommunities = new NhinTargetCommunitiesType();
        NhinTargetCommunityType targetCommunity = new NhinTargetCommunityType();
        targetCommunity.setHomeCommunity(createHomeCommunity(hcid));
        targetCommunities.setExchangeName("StateExchange");
        targetCommunities.getNhinTargetCommunity().add(targetCommunity);
        return targetCommunities;
    }

    private HomeCommunityType createHomeCommunity(String hcid) {
        HomeCommunityType homeCommunity = new HomeCommunityType();
        homeCommunity.setHomeCommunityId(hcid);
        return homeCommunity;
    }

    private void verifyOverridesEndpoint(List<EndpointType> endpoints) {
        assertEquals("After sync with overrides, there should be 3 endpoints", 3, endpoints.size());
        for (EndpointType ep : endpoints) {
            if (PD.equalsIgnoreCase(ExchangeManagerHelper.getNhinServiceName(ep.getName()))) {
                verifyEndpoints(ep, getPDEndpointsMap());
            } else if (QD.equalsIgnoreCase(ExchangeManagerHelper.getNhinServiceName(ep.getName()))) {
                verifyEndpoints(ep, getQDEndpointsMap());
            } else if (RD.equalsIgnoreCase(ExchangeManagerHelper.getNhinServiceName(ep.getName()))) {
                verifyEndpoints(ep, getRDEndpointsMap());
            }
        }
    }

    private Map<String, String> getPDEndpointsMap() {
        Map<String, String> map = new HashMap<>();
        map.put("1.0", "https://stateExchange.com:443/CONNECTGateway/NhinService/NhinPatientDiscovery");
        return map;
    }

    private Map<String, String> getQDEndpointsMap() {
        Map<String, String> map = new HashMap<>();
        map.put("2.0",
            "https://stateExchange.com:443/CONNECTGateway/NhinService/RespondingGateway_Query_Service/DocQuery");
        map.put("3.0", "https://stateExchange.com:443/CONNECTGateway/NhinService/NhinPatientDiscovery");
        return map;
    }

    private Map<String, String> getRDEndpointsMap() {
        Map<String, String> map = new HashMap<>();
        map.put("2.0", "https://localhost.com:443/NhinService/RespondingGateway_Retrieve_Service/DocRetrieve");
        map.put("3.0", "https://stateExchange.com:443/NhinService/Retrieve_Service/3_0/DocRetrieve");
        return map;
    }

    private void verifyEndpoints(EndpointType ep, Map<String, String> specUrls) {
        for (EndpointConfigurationType config : ep.getEndpointConfigurationList().getEndpointConfiguration()) {
            assertTrue("Endppoint Spec Version do not match", specUrls.containsKey(config.getVersion()));
            assertEquals("Endpoint url do not match", specUrls.get(config.getVersion()), config.getUrl());
        }
    }

    public void copyFile(String filename, String destFilename) throws IOException, URISyntaxException {


        URL url = this.getClass().getResource(filename);
        File sourceExchange = new File(url.toURI());

        String destPath = sourceExchange.getParentFile().getAbsolutePath() + File.separator + destFilename;
        File destExchange = new File(destPath);

        if(!destExchange.exists()) {
            destExchange.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;

        try(FileInputStream sourceFile = new FileInputStream(sourceExchange);
            FileOutputStream destFile = new FileOutputStream(destExchange);) {
            source = sourceFile.getChannel();
            destination = destFile.getChannel();
            destination.transferFrom(source, 0, source.size());
        }
        finally {
            if(source != null) {
                source.close();
            }
            if(destination != null) {
                destination.close();
            }

        }
    }
}
