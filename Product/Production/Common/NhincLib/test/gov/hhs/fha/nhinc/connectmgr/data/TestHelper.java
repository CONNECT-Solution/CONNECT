/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.connectmgr.data;

import gov.hhs.fha.nhinc.nhinclib.NullChecker;

/**
 *
 * @author JHOPPESC
 */
public class TestHelper {

    public static boolean assertConnInfoEmpty (CMInternalConnectionInfo connInfo) {
        if (NullChecker.isNullish(connInfo.getDescription()) &&
                NullChecker.isNullish(connInfo.getHomeCommunityId()) &&
                NullChecker.isNullish(connInfo.getName()) &&
                connInfo.getServices() == null &&
                connInfo.getStates() == null) {
            return true;
        }
        return false;
    }

    public static boolean assertConnInfoNotEmpty (CMInternalConnectionInfo connInfo) {
       if (NullChecker.isNotNullish(connInfo.getDescription()) ||
               NullChecker.isNotNullish(connInfo.getHomeCommunityId()) ||
               NullChecker.isNotNullish(connInfo.getName()) ||
               NullChecker.isNotNullish(connInfo.getServices().getService()) ||
               NullChecker.isNotNullish(connInfo.getStates().getState())) {
           return true;
       }
       return false;
    }

    public static CMInternalConnectionInfo createConnInfo (String commId, String commName, String desc, String servDesc, String url, String servName, String stateName ) {
        String hcid = commId;
        String name = commName;
        String description = desc;

        CMInternalConnInfoServices services = new CMInternalConnInfoServices();
        CMInternalConnInfoService service = new CMInternalConnInfoService();
        services.getService().add(service);
        service.setDescription(servDesc);
        service.setEndpointURL(url);
        service.setExternalService(false);
        service.setName(servName);

        CMInternalConnectionInfoStates states = new CMInternalConnectionInfoStates();
        CMInternalConnectionInfoState state = new CMInternalConnectionInfoState();
        states.getState().add(state);
        state.setName(stateName);

        CMInternalConnectionInfo instance = new CMInternalConnectionInfo();
        instance.setDescription(description);
        instance.setHomeCommunityId(hcid);
        instance.setName(name);
        instance.setServices(services);
        instance.setStates(states);

        return instance;
    }

}
