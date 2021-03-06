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

import gov.hhs.fha.nhinc.nhinclib.NhincConstants.GATEWAY_API_LEVEL;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.NHIN_SERVICE_NAMES;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.UDDI_SPEC_VERSION;
import org.junit.BeforeClass;
import org.junit.Test;

public class UddiSpecVersionRegistryTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSupportedSpecsWrongAPI() {
        GATEWAY_API_LEVEL api = GATEWAY_API_LEVEL.valueOf("wrongValue");
        UddiSpecVersionRegistry.getInstance().getSupportedSpecs(api, NHIN_SERVICE_NAMES.DOCUMENT_SUBMISSION);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSupportedSpecsWrongService() {
        GATEWAY_API_LEVEL api = GATEWAY_API_LEVEL.LEVEL_g1;
        UddiSpecVersionRegistry.getInstance().getSupportedSpecs(api, NHIN_SERVICE_NAMES.valueOf("wrongValue"));
    }

    @Test
    public void getSupportedGatewayAPI_1_0() {
        UDDI_SPEC_VERSION spec = UDDI_SPEC_VERSION.SPEC_1_0;
        GATEWAY_API_LEVEL api = UddiSpecVersionRegistry.getInstance().getSupportedGatewayAPI(spec,
                NHIN_SERVICE_NAMES.ADMINISTRATIVE_DISTRIBUTION);
        assertTrue(api == GATEWAY_API_LEVEL.LEVEL_g0);
        api = UddiSpecVersionRegistry.getInstance().getSupportedGatewayAPI(spec, NHIN_SERVICE_NAMES.PATIENT_DISCOVERY);
        assertTrue(api == GATEWAY_API_LEVEL.LEVEL_g0);
    }

    @Test
    public void getSupportedGatewayAPI_1_1() {
        UDDI_SPEC_VERSION spec = UDDI_SPEC_VERSION.SPEC_1_1;
        GATEWAY_API_LEVEL api = UddiSpecVersionRegistry.getInstance().getSupportedGatewayAPI(spec,
                NHIN_SERVICE_NAMES.DOCUMENT_SUBMISSION);
        assertTrue(api == GATEWAY_API_LEVEL.LEVEL_g0);
    }

    @Test
    public void getSupportedGatewayAPI_2_0() {
        UDDI_SPEC_VERSION spec = UDDI_SPEC_VERSION.SPEC_2_0;
        GATEWAY_API_LEVEL api = UddiSpecVersionRegistry.getInstance().getSupportedGatewayAPI(spec,
                NHIN_SERVICE_NAMES.ADMINISTRATIVE_DISTRIBUTION);
        assertTrue(api == GATEWAY_API_LEVEL.LEVEL_g1);
        api = UddiSpecVersionRegistry.getInstance().getSupportedGatewayAPI(spec, NHIN_SERVICE_NAMES.DOCUMENT_QUERY);
        assertTrue(api == GATEWAY_API_LEVEL.LEVEL_g0);
        api = UddiSpecVersionRegistry.getInstance().getSupportedGatewayAPI(spec, NHIN_SERVICE_NAMES.DOCUMENT_RETRIEVE);
        assertTrue(api == GATEWAY_API_LEVEL.LEVEL_g0);
        api = UddiSpecVersionRegistry.getInstance().getSupportedGatewayAPI(spec,
                NHIN_SERVICE_NAMES.DOCUMENT_SUBMISSION);
        assertTrue(api == GATEWAY_API_LEVEL.LEVEL_g1);
        api = UddiSpecVersionRegistry.getInstance().getSupportedGatewayAPI(spec, NHIN_SERVICE_NAMES.PATIENT_DISCOVERY);
        assertTrue(api == GATEWAY_API_LEVEL.LEVEL_g1);
    }

    @Test
    public void getSupportedGatewayAPI_3_0() {
        UDDI_SPEC_VERSION spec = UDDI_SPEC_VERSION.SPEC_3_0;
        GATEWAY_API_LEVEL api = UddiSpecVersionRegistry.getInstance().getSupportedGatewayAPI(spec,
                NHIN_SERVICE_NAMES.DOCUMENT_QUERY);
        assertTrue(api == GATEWAY_API_LEVEL.LEVEL_g1);
        api = UddiSpecVersionRegistry.getInstance().getSupportedGatewayAPI(spec, NHIN_SERVICE_NAMES.DOCUMENT_RETRIEVE);
        assertTrue(api == GATEWAY_API_LEVEL.LEVEL_g1);
    }

}
