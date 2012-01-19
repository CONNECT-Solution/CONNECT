package gov.hhs.fha.nhinc.connectmgr;

import java.util.ArrayList;
import java.util.HashMap;

import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.GATEWAY_API_LEVEL;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.UDDI_SPEC_VERSION;

public class UddiSpecVersionRegestry {
	
	static private HashMap<GATEWAY_API_LEVEL, ArrayList<UDDI_SPEC_VERSION>> map = null;
	
	static HashMap<GATEWAY_API_LEVEL, ArrayList<UDDI_SPEC_VERSION>> getMap() {
		if (map == null) {
			map = new HashMap<GATEWAY_API_LEVEL, ArrayList<UDDI_SPEC_VERSION>>();
			ArrayList<UDDI_SPEC_VERSION> gw1Specs = new ArrayList<UDDI_SPEC_VERSION>();
			gw1Specs.add(UDDI_SPEC_VERSION.SPEC_1_0);
			map.put(GATEWAY_API_LEVEL.LEVEL_g0, gw1Specs);

			ArrayList<UDDI_SPEC_VERSION> gw2Specs = new ArrayList<UDDI_SPEC_VERSION>();
			gw2Specs.add(UDDI_SPEC_VERSION.SPEC_2_0);
			gw2Specs.add(UDDI_SPEC_VERSION.SPEC_1_0);
			map.put(GATEWAY_API_LEVEL.LEVEL_g1, gw2Specs);
		}
		return map;
	}
	
	static public ArrayList<UDDI_SPEC_VERSION> getSupportedSpecs(GATEWAY_API_LEVEL apiLevel){
		return getMap().get(apiLevel);
	}
	
	static boolean isSupported (GATEWAY_API_LEVEL apiLevel, String specVersion) {
		if (apiLevel == null && NullChecker.isNullish(specVersion)) {
			return true;
		}
		ArrayList<UDDI_SPEC_VERSION> specs = getMap().get(apiLevel);
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
