/**
 * 
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
        map = new HashMap<String, NhincConstants.NHIN_SERVICE_NAMES>();
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
        map.put("urn:Notify", NhincConstants.NHIN_SERVICE_NAMES.HIEM_NOTIFY);
        map.put("urn:Subscribe", NhincConstants.NHIN_SERVICE_NAMES.HIEM_SUBSCRIBE);
        map.put("urn:Unsubscribe", NhincConstants.NHIN_SERVICE_NAMES.HIEM_UNSUBSCRIBE);
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
