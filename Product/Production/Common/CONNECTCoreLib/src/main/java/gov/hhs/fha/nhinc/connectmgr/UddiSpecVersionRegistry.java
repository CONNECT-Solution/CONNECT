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

import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.GATEWAY_API_LEVEL;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.UDDI_SPEC_VERSION;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UddiSpecVersionRegistry {

    private static UddiSpecVersionRegistry instance = null;
    private TransactionWrapper tw = null;

    protected UddiSpecVersionRegistry() {
        tw = new TransactionWrapper();
    }

    public static UddiSpecVersionRegistry getInstance() {
        if (instance == null) {
            instance = new UddiSpecVersionRegistry();
        }
        return instance;
    }

    public List<UDDI_SPEC_VERSION> getSupportedSpecs(final GATEWAY_API_LEVEL apiLevel,
        final NhincConstants.NHIN_SERVICE_NAMES serviceName) {
        List<UDDI_SPEC_VERSION> list = new ArrayList<>();
        final Map<GATEWAY_API_LEVEL, List<UDDI_SPEC_VERSION>> map = tw.getAPIToSpecMapping(serviceName);
        if (map != null) {
            list = map.get(apiLevel);
        }
        return list;
    }

    public GATEWAY_API_LEVEL getSupportedGatewayAPI(final UDDI_SPEC_VERSION specVersion,
        final NhincConstants.NHIN_SERVICE_NAMES serviceName) {
        GATEWAY_API_LEVEL api = null;
        final Map<UDDI_SPEC_VERSION, GATEWAY_API_LEVEL> map = tw.getSpecToAPIMapping(serviceName);
        if (map != null) {
            api = map.get(specVersion);
        }
        return api;
    }

    boolean isSupported(final GATEWAY_API_LEVEL apiLevel, final String specVersion,
        final NhincConstants.NHIN_SERVICE_NAMES serviceName) {
        if (apiLevel == null && NullChecker.isNullish(specVersion)) {
            return true;
        }
        final List<UDDI_SPEC_VERSION> specs = tw.getAPIToSpecMapping(serviceName).get(apiLevel);
        if (specs == null) {
            return false;
        }
        for (final UDDI_SPEC_VERSION spec : specs) {
            if (spec.toString().equals(specVersion)) {
                return true;
            }
        }
        return false;
    }

    private static class TransactionWrapper {
        private Map<NhincConstants.NHIN_SERVICE_NAMES, Map<GATEWAY_API_LEVEL, List<UDDI_SPEC_VERSION>>> apiToSpecMap = null;
        private Map<NhincConstants.NHIN_SERVICE_NAMES, Map<UDDI_SPEC_VERSION, GATEWAY_API_LEVEL>> specToApiMap = null;

        private TransactionWrapper() {
            apiToSpecMap = new EnumMap<>(NhincConstants.NHIN_SERVICE_NAMES.class);
            specToApiMap = new EnumMap<>(NhincConstants.NHIN_SERVICE_NAMES.class);

            // Patient Discovery
            final Map<GATEWAY_API_LEVEL, List<UDDI_SPEC_VERSION>> pdApiToSpecMap = new EnumMap<>(
                GATEWAY_API_LEVEL.class);
            final List<UDDI_SPEC_VERSION> pdG0SpecVersions = new ArrayList<>();
            pdG0SpecVersions.add(UDDI_SPEC_VERSION.SPEC_1_0);
            pdApiToSpecMap.put(GATEWAY_API_LEVEL.LEVEL_g0, pdG0SpecVersions);
            final List<UDDI_SPEC_VERSION> pdG1SpecVersions = new ArrayList<>();
            pdG1SpecVersions.add(UDDI_SPEC_VERSION.SPEC_2_0);
            pdApiToSpecMap.put(GATEWAY_API_LEVEL.LEVEL_g1, pdG1SpecVersions);
            apiToSpecMap.put(NhincConstants.NHIN_SERVICE_NAMES.PATIENT_DISCOVERY, pdApiToSpecMap);

            final Map<UDDI_SPEC_VERSION, GATEWAY_API_LEVEL> pdSpecToApiMap = new HashMap<>();
            pdSpecToApiMap.put(UDDI_SPEC_VERSION.SPEC_1_0, GATEWAY_API_LEVEL.LEVEL_g0);
            pdSpecToApiMap.put(UDDI_SPEC_VERSION.SPEC_2_0, GATEWAY_API_LEVEL.LEVEL_g1);
            specToApiMap.put(NhincConstants.NHIN_SERVICE_NAMES.PATIENT_DISCOVERY, pdSpecToApiMap);

            // Document Submission
            final Map<GATEWAY_API_LEVEL, List<UDDI_SPEC_VERSION>> dsApiToSpecMap = new EnumMap<>(
                GATEWAY_API_LEVEL.class);
            final List<UDDI_SPEC_VERSION> dsG0SpecVersions = new ArrayList<>();
            dsG0SpecVersions.add(UDDI_SPEC_VERSION.SPEC_1_1);
            dsApiToSpecMap.put(GATEWAY_API_LEVEL.LEVEL_g0, dsG0SpecVersions);
            final List<UDDI_SPEC_VERSION> dsG1SpecVersions = new ArrayList<>();
            dsG1SpecVersions.add(UDDI_SPEC_VERSION.SPEC_2_0);
            dsApiToSpecMap.put(GATEWAY_API_LEVEL.LEVEL_g1, dsG1SpecVersions);
            apiToSpecMap.put(NhincConstants.NHIN_SERVICE_NAMES.DOCUMENT_SUBMISSION, dsApiToSpecMap);

            final Map<UDDI_SPEC_VERSION, GATEWAY_API_LEVEL> dsSpecToApiMap = new HashMap<>();
            dsSpecToApiMap.put(UDDI_SPEC_VERSION.SPEC_1_1, GATEWAY_API_LEVEL.LEVEL_g0);
            dsSpecToApiMap.put(UDDI_SPEC_VERSION.SPEC_2_0, GATEWAY_API_LEVEL.LEVEL_g1);
            specToApiMap.put(NhincConstants.NHIN_SERVICE_NAMES.DOCUMENT_SUBMISSION, dsSpecToApiMap);

            // Administrative Distribution
            final Map<GATEWAY_API_LEVEL, List<UDDI_SPEC_VERSION>> adApiToSpecMap = new EnumMap<>(
                GATEWAY_API_LEVEL.class);
            final List<UDDI_SPEC_VERSION> adG0SpecVersions = new ArrayList<>();
            adG0SpecVersions.add(UDDI_SPEC_VERSION.SPEC_1_0);
            adApiToSpecMap.put(GATEWAY_API_LEVEL.LEVEL_g0, adG0SpecVersions);
            final List<UDDI_SPEC_VERSION> adG1SpecVersions = new ArrayList<>();
            adG1SpecVersions.add(UDDI_SPEC_VERSION.SPEC_2_0);
            adApiToSpecMap.put(GATEWAY_API_LEVEL.LEVEL_g1, adG1SpecVersions);
            apiToSpecMap.put(NhincConstants.NHIN_SERVICE_NAMES.ADMINISTRATIVE_DISTRIBUTION, adApiToSpecMap);

            final Map<UDDI_SPEC_VERSION, GATEWAY_API_LEVEL> adSpecToApiMap = new HashMap<>();
            adSpecToApiMap.put(UDDI_SPEC_VERSION.SPEC_1_0, GATEWAY_API_LEVEL.LEVEL_g0);
            adSpecToApiMap.put(UDDI_SPEC_VERSION.SPEC_2_0, GATEWAY_API_LEVEL.LEVEL_g1);
            specToApiMap.put(NhincConstants.NHIN_SERVICE_NAMES.ADMINISTRATIVE_DISTRIBUTION, adSpecToApiMap);

            // Document Query
            final Map<GATEWAY_API_LEVEL, List<UDDI_SPEC_VERSION>> dqApiToSpecMap = new EnumMap<>(
                GATEWAY_API_LEVEL.class);
            final List<UDDI_SPEC_VERSION> dqG0SpecVersions = new ArrayList<>();
            dqG0SpecVersions.add(UDDI_SPEC_VERSION.SPEC_2_0);
            dqApiToSpecMap.put(GATEWAY_API_LEVEL.LEVEL_g0, dqG0SpecVersions);
            final List<UDDI_SPEC_VERSION> dqG1SpecVersions = new ArrayList<>();
            dqG1SpecVersions.add(UDDI_SPEC_VERSION.SPEC_3_0);
            dqApiToSpecMap.put(GATEWAY_API_LEVEL.LEVEL_g1, dqG1SpecVersions);
            apiToSpecMap.put(NhincConstants.NHIN_SERVICE_NAMES.DOCUMENT_QUERY, dqApiToSpecMap);

            final Map<UDDI_SPEC_VERSION, GATEWAY_API_LEVEL> dqSpecToApiMap = new HashMap<>();
            dqSpecToApiMap.put(UDDI_SPEC_VERSION.SPEC_2_0, GATEWAY_API_LEVEL.LEVEL_g0);
            dqSpecToApiMap.put(UDDI_SPEC_VERSION.SPEC_3_0, GATEWAY_API_LEVEL.LEVEL_g1);
            specToApiMap.put(NhincConstants.NHIN_SERVICE_NAMES.DOCUMENT_QUERY, dqSpecToApiMap);

            // Document Retrieve
            final Map<GATEWAY_API_LEVEL, List<UDDI_SPEC_VERSION>> drApiToSpecMap = new EnumMap<>(
                GATEWAY_API_LEVEL.class);
            final List<UDDI_SPEC_VERSION> drG0SpecVersions = new ArrayList<>();
            drG0SpecVersions.add(UDDI_SPEC_VERSION.SPEC_2_0);
            drApiToSpecMap.put(GATEWAY_API_LEVEL.LEVEL_g0, drG0SpecVersions);
            final List<UDDI_SPEC_VERSION> drG1SpecVersions = new ArrayList<>();
            drG1SpecVersions.add(UDDI_SPEC_VERSION.SPEC_3_0);
            drApiToSpecMap.put(GATEWAY_API_LEVEL.LEVEL_g1, drG1SpecVersions);
            apiToSpecMap.put(NhincConstants.NHIN_SERVICE_NAMES.DOCUMENT_RETRIEVE, drApiToSpecMap);

            final Map<UDDI_SPEC_VERSION, GATEWAY_API_LEVEL> drSpecToApiMap = new EnumMap<>(UDDI_SPEC_VERSION.class);
            drSpecToApiMap.put(UDDI_SPEC_VERSION.SPEC_2_0, GATEWAY_API_LEVEL.LEVEL_g0);
            drSpecToApiMap.put(UDDI_SPEC_VERSION.SPEC_3_0, GATEWAY_API_LEVEL.LEVEL_g1);
            specToApiMap.put(NhincConstants.NHIN_SERVICE_NAMES.DOCUMENT_RETRIEVE, drSpecToApiMap);

            // Document Data Submission
            final Map<GATEWAY_API_LEVEL, List<UDDI_SPEC_VERSION>> ddsApiToSpecMap = new EnumMap<>(
                GATEWAY_API_LEVEL.class);
            final List<UDDI_SPEC_VERSION> ddsG0SpecVersions = new ArrayList<>();
            ddsG0SpecVersions.add(UDDI_SPEC_VERSION.SPEC_1_0);
            ddsApiToSpecMap.put(GATEWAY_API_LEVEL.LEVEL_g0, ddsG0SpecVersions);
            final List<UDDI_SPEC_VERSION> ddsG1SpecVersions = new ArrayList<>();
            ddsG1SpecVersions.add(UDDI_SPEC_VERSION.SPEC_1_0);
            ddsApiToSpecMap.put(GATEWAY_API_LEVEL.LEVEL_g1, ddsG1SpecVersions);
            apiToSpecMap.put(NhincConstants.NHIN_SERVICE_NAMES.DOCUMENT_DATA_SUBMISSION, ddsApiToSpecMap);

            final Map<UDDI_SPEC_VERSION, GATEWAY_API_LEVEL> ddsSpecToApiMap = new EnumMap<>(UDDI_SPEC_VERSION.class);
            ddsSpecToApiMap.put(UDDI_SPEC_VERSION.SPEC_1_0, GATEWAY_API_LEVEL.LEVEL_g0);
            ddsSpecToApiMap.put(UDDI_SPEC_VERSION.SPEC_1_0, GATEWAY_API_LEVEL.LEVEL_g1);
            specToApiMap.put(NhincConstants.NHIN_SERVICE_NAMES.DOCUMENT_DATA_SUBMISSION, ddsSpecToApiMap);

        }

        public Map<GATEWAY_API_LEVEL, List<UDDI_SPEC_VERSION>> getAPIToSpecMapping(
            final NhincConstants.NHIN_SERVICE_NAMES serviceName) {
            switch (serviceName) {
                case PATIENT_DISCOVERY_DEFERRED_REQUEST:
                case PATIENT_DISCOVERY_DEFERRED_RESPONSE:
                    return apiToSpecMap.get(NhincConstants.NHIN_SERVICE_NAMES.PATIENT_DISCOVERY);
                case DOCUMENT_SUBMISSION_DEFERRED_REQUEST:
                case DOCUMENT_SUBMISSION_DEFERRED_RESPONSE:
                    return apiToSpecMap.get(NhincConstants.NHIN_SERVICE_NAMES.DOCUMENT_SUBMISSION);
                default:
                    return apiToSpecMap.get(serviceName);
            }
        }

        public Map<UDDI_SPEC_VERSION, GATEWAY_API_LEVEL> getSpecToAPIMapping(
            final NhincConstants.NHIN_SERVICE_NAMES serviceName) {
            switch (serviceName) {
                case PATIENT_DISCOVERY_DEFERRED_REQUEST:
                case PATIENT_DISCOVERY_DEFERRED_RESPONSE:
                    return specToApiMap.get(NhincConstants.NHIN_SERVICE_NAMES.PATIENT_DISCOVERY);
                case DOCUMENT_SUBMISSION_DEFERRED_REQUEST:
                case DOCUMENT_SUBMISSION_DEFERRED_RESPONSE:
                    return specToApiMap.get(NhincConstants.NHIN_SERVICE_NAMES.DOCUMENT_SUBMISSION);
                default:
                    return specToApiMap.get(serviceName);
            }
        }
    }
}
