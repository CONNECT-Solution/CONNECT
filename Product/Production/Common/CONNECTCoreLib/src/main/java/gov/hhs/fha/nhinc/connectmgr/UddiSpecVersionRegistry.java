/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services. 
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

import java.util.ArrayList;
import java.util.HashMap;

import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.GATEWAY_API_LEVEL;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.UDDI_SPEC_VERSION;

public class UddiSpecVersionRegistry {

    private static UddiSpecVersionRegistry instance = null;

    private HashMap<GATEWAY_API_LEVEL, ArrayList<UDDI_SPEC_VERSION>> apiToSpecMap = null;
    private HashMap<UDDI_SPEC_VERSION, GATEWAY_API_LEVEL> specToApiMap = null;

    protected UddiSpecVersionRegistry() {
        apiToSpecMap = createGatewayAPItoSpecVersionRegistryMap();
        specToApiMap = createSpecVersionToGatewayAPIRegistryMap();
    }

    private HashMap<GATEWAY_API_LEVEL, ArrayList<UDDI_SPEC_VERSION>> createGatewayAPItoSpecVersionRegistryMap() {
        HashMap<GATEWAY_API_LEVEL, ArrayList<UDDI_SPEC_VERSION>> map = new HashMap<GATEWAY_API_LEVEL, ArrayList<UDDI_SPEC_VERSION>>();
        ArrayList<UDDI_SPEC_VERSION> gw1Specs = new ArrayList<UDDI_SPEC_VERSION>();
        ArrayList<UDDI_SPEC_VERSION> gw2Specs = new ArrayList<UDDI_SPEC_VERSION>();

        gw1Specs.add(UDDI_SPEC_VERSION.SPEC_1_0);        
        gw2Specs.add(UDDI_SPEC_VERSION.SPEC_1_0);
        gw2Specs.add(UDDI_SPEC_VERSION.SPEC_2_0);
        
        map.put(GATEWAY_API_LEVEL.LEVEL_g0, gw1Specs);
        map.put(GATEWAY_API_LEVEL.LEVEL_g1, gw2Specs);

        return map;
    }

    private HashMap<UDDI_SPEC_VERSION, GATEWAY_API_LEVEL> createSpecVersionToGatewayAPIRegistryMap() {
        HashMap<UDDI_SPEC_VERSION, GATEWAY_API_LEVEL> map = new HashMap<UDDI_SPEC_VERSION, GATEWAY_API_LEVEL>();

        map.put(UDDI_SPEC_VERSION.SPEC_1_0, GATEWAY_API_LEVEL.LEVEL_g0);
        map.put(UDDI_SPEC_VERSION.SPEC_2_0, GATEWAY_API_LEVEL.LEVEL_g1);

        return map;
    }

    public static UddiSpecVersionRegistry getInstance() {
        if (instance == null) {
            return new UddiSpecVersionRegistry();
        }
        return instance;
    }

    public ArrayList<UDDI_SPEC_VERSION> getSupportedSpecs(GATEWAY_API_LEVEL apiLevel) {
        return apiToSpecMap.get(apiLevel);
    }

    public GATEWAY_API_LEVEL getSupportedGatewayAPI(UDDI_SPEC_VERSION specVersion) {
        return specToApiMap.get(specVersion);
    }

    boolean isSupported(GATEWAY_API_LEVEL apiLevel, String specVersion) {
        if (apiLevel == null && NullChecker.isNullish(specVersion)) {
            return true;
        }
        ArrayList<UDDI_SPEC_VERSION> specs = apiToSpecMap.get(apiLevel);
        if (specs == null) {
            return false;
        }
        for (UDDI_SPEC_VERSION spec : specs) {
            if (spec.toString().equals(specVersion)) {
                return true;
            }
        }
        return false;
    }
}
