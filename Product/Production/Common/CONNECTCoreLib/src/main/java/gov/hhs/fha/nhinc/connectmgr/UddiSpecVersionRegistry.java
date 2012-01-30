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
