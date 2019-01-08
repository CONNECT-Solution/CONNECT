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
package gov.hhs.fha.nhinc.util;

import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import java.util.HashMap;
import java.util.Map;

/**
 * @author mweaver
 *
 */
public class AddressingActionToServiceNameMapping {

    private static Map<String, NhincConstants.NHIN_SERVICE_NAMES> map;

    static {
        map = new HashMap<>();
        map.put("urn:oasis:names:tc:emergency:EDXL:DE:1.0:SendAlertMessage",
                NhincConstants.NHIN_SERVICE_NAMES.ADMINISTRATIVE_DISTRIBUTION);
        map.put("urn:ihe:iti:2007:CrossGatewayQuery", NhincConstants.NHIN_SERVICE_NAMES.DOCUMENT_QUERY);
        map.put("urn:ihe:iti:2007:CrossGatewayRetrieve", NhincConstants.NHIN_SERVICE_NAMES.DOCUMENT_RETRIEVE);
        map.put("urn:ihe:iti:xdr:2007:ProvideAndRegisterDocumentSet-b",
                NhincConstants.NHIN_SERVICE_NAMES.DOCUMENT_SUBMISSION);
        map.put("urn:ihe:iti:2007:ProvideAndRegisterDocumentSet-b",
                NhincConstants.NHIN_SERVICE_NAMES.DOCUMENT_SUBMISSION);
        map.put("urn:nhin:Deferred:ProvideAndRegisterDocumentSet-b",
                NhincConstants.NHIN_SERVICE_NAMES.DOCUMENT_SUBMISSION_DEFERRED_REQUEST);
        map.put("urn:ihe:iti:xdr:2007:Deferred:XDRRequestInputMessage",
                NhincConstants.NHIN_SERVICE_NAMES.DOCUMENT_SUBMISSION_DEFERRED_REQUEST);
        map.put("urn:nhin:Deferred:ProvideAndRegisterDocumentSet-bResponse",
                NhincConstants.NHIN_SERVICE_NAMES.DOCUMENT_SUBMISSION_DEFERRED_RESPONSE);
        map.put("urn:ihe:iti:xdr:2007:Deferred:XDRResponseInputMessage",
                NhincConstants.NHIN_SERVICE_NAMES.DOCUMENT_SUBMISSION_DEFERRED_RESPONSE);
        map.put("urn:hl7-org:v3:PRPA_IN201305UV02:CrossGatewayPatientDiscovery",
                NhincConstants.NHIN_SERVICE_NAMES.PATIENT_DISCOVERY);
        map.put("urn:hl7-org:v3:PRPA_IN201305UV02:Deferred:CrossGatewayPatientDiscovery",
                NhincConstants.NHIN_SERVICE_NAMES.PATIENT_DISCOVERY_DEFERRED_REQUEST);
        map.put("urn:hl7-org:v3:PRPA_IN201306UV02:Deferred:CrossGatewayPatientDiscovery",
                NhincConstants.NHIN_SERVICE_NAMES.PATIENT_DISCOVERY_DEFERRED_RESPONSE);
    }

    /**
     * @param key wsa action to use as the key for looking up service name
     * @return result of the map lookup for the key provided
     */
    public static NhincConstants.NHIN_SERVICE_NAMES get(String key) {
        return map.get(key);
    }
}
