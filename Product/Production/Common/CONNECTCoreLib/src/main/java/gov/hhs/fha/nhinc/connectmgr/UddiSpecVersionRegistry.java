/*
 * Copyright (c) 2009-2016, United States Government, as represented by the Secretary of Health and Human Services.
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
import java.util.HashMap;

public class UddiSpecVersionRegistry {

    private static UddiSpecVersionRegistry instance = null;
    private transactionWrapper tw = null;

    protected UddiSpecVersionRegistry() {
        tw = new transactionWrapper();
    }

    public static UddiSpecVersionRegistry getInstance() {
        if (instance == null) {
            instance = new UddiSpecVersionRegistry();
        }
        return instance;
    }

    public ArrayList<UDDI_SPEC_VERSION> getSupportedSpecs(final GATEWAY_API_LEVEL apiLevel,
            final NhincConstants.NHIN_SERVICE_NAMES serviceName) {
        ArrayList<UDDI_SPEC_VERSION> list = new ArrayList<>();
        final HashMap<GATEWAY_API_LEVEL, ArrayList<UDDI_SPEC_VERSION>> map = tw.getAPIToSpecMapping(serviceName);
        if (map != null) {
            list = map.get(apiLevel);
        }
        return list;
    }

    public GATEWAY_API_LEVEL getSupportedGatewayAPI(final UDDI_SPEC_VERSION specVersion,
            final NhincConstants.NHIN_SERVICE_NAMES serviceName) {
        GATEWAY_API_LEVEL api = null;
        final HashMap<UDDI_SPEC_VERSION, GATEWAY_API_LEVEL> map = tw.getSpecToAPIMapping(serviceName);
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
        final ArrayList<UDDI_SPEC_VERSION> specs = tw.getAPIToSpecMapping(serviceName).get(apiLevel);
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

    private static class transactionWrapper {
        private HashMap<NhincConstants.NHIN_SERVICE_NAMES, HashMap<GATEWAY_API_LEVEL, ArrayList<UDDI_SPEC_VERSION>>> apiToSpecMap = null;
        private HashMap<NhincConstants.NHIN_SERVICE_NAMES, HashMap<UDDI_SPEC_VERSION, GATEWAY_API_LEVEL>> specToApiMap = null;

        private transactionWrapper() {
            apiToSpecMap = new HashMap<>();
            specToApiMap = new HashMap<>();

            // Patient Discovery
            final HashMap<GATEWAY_API_LEVEL, ArrayList<UDDI_SPEC_VERSION>> PDApiToSpecMap = new HashMap<>();
            final ArrayList<UDDI_SPEC_VERSION> PDG0SpecVersions = new ArrayList<>();
            PDG0SpecVersions.add(UDDI_SPEC_VERSION.SPEC_1_0);
            PDApiToSpecMap.put(GATEWAY_API_LEVEL.LEVEL_g0, PDG0SpecVersions);
            final ArrayList<UDDI_SPEC_VERSION> PDG1SpecVersions = new ArrayList<>();
            PDG1SpecVersions.add(UDDI_SPEC_VERSION.SPEC_2_0);
            PDApiToSpecMap.put(GATEWAY_API_LEVEL.LEVEL_g1, PDG1SpecVersions);
            apiToSpecMap.put(NhincConstants.NHIN_SERVICE_NAMES.PATIENT_DISCOVERY, PDApiToSpecMap);

            final HashMap<UDDI_SPEC_VERSION, GATEWAY_API_LEVEL> PDSpecToApiMap = new HashMap<>();
            PDSpecToApiMap.put(UDDI_SPEC_VERSION.SPEC_1_0, GATEWAY_API_LEVEL.LEVEL_g0);
            PDSpecToApiMap.put(UDDI_SPEC_VERSION.SPEC_2_0, GATEWAY_API_LEVEL.LEVEL_g1);
            specToApiMap.put(NhincConstants.NHIN_SERVICE_NAMES.PATIENT_DISCOVERY, PDSpecToApiMap);

            // Document Submission
            final HashMap<GATEWAY_API_LEVEL, ArrayList<UDDI_SPEC_VERSION>> DSApiToSpecMap = new HashMap<>();
            final ArrayList<UDDI_SPEC_VERSION> DSG0SpecVersions = new ArrayList<>();
            DSG0SpecVersions.add(UDDI_SPEC_VERSION.SPEC_1_1);
            DSApiToSpecMap.put(GATEWAY_API_LEVEL.LEVEL_g0, DSG0SpecVersions);
            final ArrayList<UDDI_SPEC_VERSION> DSG1SpecVersions = new ArrayList<>();
            DSG1SpecVersions.add(UDDI_SPEC_VERSION.SPEC_2_0);
            DSApiToSpecMap.put(GATEWAY_API_LEVEL.LEVEL_g1, DSG1SpecVersions);
            apiToSpecMap.put(NhincConstants.NHIN_SERVICE_NAMES.DOCUMENT_SUBMISSION, DSApiToSpecMap);

            final HashMap<UDDI_SPEC_VERSION, GATEWAY_API_LEVEL> DSSpecToApiMap = new HashMap<>();
            DSSpecToApiMap.put(UDDI_SPEC_VERSION.SPEC_1_1, GATEWAY_API_LEVEL.LEVEL_g0);
            DSSpecToApiMap.put(UDDI_SPEC_VERSION.SPEC_2_0, GATEWAY_API_LEVEL.LEVEL_g1);
            specToApiMap.put(NhincConstants.NHIN_SERVICE_NAMES.DOCUMENT_SUBMISSION, DSSpecToApiMap);

            // Administrative Distribution
            final HashMap<GATEWAY_API_LEVEL, ArrayList<UDDI_SPEC_VERSION>> ADApiToSpecMap = new HashMap<>();
            final ArrayList<UDDI_SPEC_VERSION> ADG0SpecVersions = new ArrayList<>();
            ADG0SpecVersions.add(UDDI_SPEC_VERSION.SPEC_1_0);
            ADApiToSpecMap.put(GATEWAY_API_LEVEL.LEVEL_g0, ADG0SpecVersions);
            final ArrayList<UDDI_SPEC_VERSION> ADG1SpecVersions = new ArrayList<>();
            ADG1SpecVersions.add(UDDI_SPEC_VERSION.SPEC_2_0);
            ADApiToSpecMap.put(GATEWAY_API_LEVEL.LEVEL_g1, ADG1SpecVersions);
            apiToSpecMap.put(NhincConstants.NHIN_SERVICE_NAMES.ADMINISTRATIVE_DISTRIBUTION, ADApiToSpecMap);

            final HashMap<UDDI_SPEC_VERSION, GATEWAY_API_LEVEL> ADSpecToApiMap = new HashMap<>();
            ADSpecToApiMap.put(UDDI_SPEC_VERSION.SPEC_1_0, GATEWAY_API_LEVEL.LEVEL_g0);
            ADSpecToApiMap.put(UDDI_SPEC_VERSION.SPEC_2_0, GATEWAY_API_LEVEL.LEVEL_g1);
            specToApiMap.put(NhincConstants.NHIN_SERVICE_NAMES.ADMINISTRATIVE_DISTRIBUTION, ADSpecToApiMap);

            // Document Query
            final HashMap<GATEWAY_API_LEVEL, ArrayList<UDDI_SPEC_VERSION>> DQApiToSpecMap = new HashMap<>();
            final ArrayList<UDDI_SPEC_VERSION> DQG0SpecVersions = new ArrayList<>();
            DQG0SpecVersions.add(UDDI_SPEC_VERSION.SPEC_2_0);
            DQApiToSpecMap.put(GATEWAY_API_LEVEL.LEVEL_g0, DQG0SpecVersions);
            final ArrayList<UDDI_SPEC_VERSION> DQG1SpecVersions = new ArrayList<>();
            DQG1SpecVersions.add(UDDI_SPEC_VERSION.SPEC_3_0);
            DQApiToSpecMap.put(GATEWAY_API_LEVEL.LEVEL_g1, DQG1SpecVersions);
            apiToSpecMap.put(NhincConstants.NHIN_SERVICE_NAMES.DOCUMENT_QUERY, DQApiToSpecMap);

            final HashMap<UDDI_SPEC_VERSION, GATEWAY_API_LEVEL> DQSpecToApiMap = new HashMap<>();
            DQSpecToApiMap.put(UDDI_SPEC_VERSION.SPEC_2_0, GATEWAY_API_LEVEL.LEVEL_g0);
            DQSpecToApiMap.put(UDDI_SPEC_VERSION.SPEC_3_0, GATEWAY_API_LEVEL.LEVEL_g1);
            specToApiMap.put(NhincConstants.NHIN_SERVICE_NAMES.DOCUMENT_QUERY, DQSpecToApiMap);

            // Document Retrieve
            final HashMap<GATEWAY_API_LEVEL, ArrayList<UDDI_SPEC_VERSION>> DRApiToSpecMap = new HashMap<>();
            final ArrayList<UDDI_SPEC_VERSION> DR0SpecVersions = new ArrayList<>();
            DR0SpecVersions.add(UDDI_SPEC_VERSION.SPEC_2_0);
            DRApiToSpecMap.put(GATEWAY_API_LEVEL.LEVEL_g0, DR0SpecVersions);
            final ArrayList<UDDI_SPEC_VERSION> DRG1SpecVersions = new ArrayList<>();
            DRG1SpecVersions.add(UDDI_SPEC_VERSION.SPEC_3_0);
            DRApiToSpecMap.put(GATEWAY_API_LEVEL.LEVEL_g1, DRG1SpecVersions);
            apiToSpecMap.put(NhincConstants.NHIN_SERVICE_NAMES.DOCUMENT_RETRIEVE, DRApiToSpecMap);

            final HashMap<UDDI_SPEC_VERSION, GATEWAY_API_LEVEL> DRSpecToApiMap = new HashMap<>();
            DRSpecToApiMap.put(UDDI_SPEC_VERSION.SPEC_2_0, GATEWAY_API_LEVEL.LEVEL_g0);
            DRSpecToApiMap.put(UDDI_SPEC_VERSION.SPEC_3_0, GATEWAY_API_LEVEL.LEVEL_g1);
            specToApiMap.put(NhincConstants.NHIN_SERVICE_NAMES.DOCUMENT_RETRIEVE, DRSpecToApiMap);

        }

        public HashMap<GATEWAY_API_LEVEL, ArrayList<UDDI_SPEC_VERSION>> getAPIToSpecMapping(
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

        public HashMap<UDDI_SPEC_VERSION, GATEWAY_API_LEVEL> getSpecToAPIMapping(
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
