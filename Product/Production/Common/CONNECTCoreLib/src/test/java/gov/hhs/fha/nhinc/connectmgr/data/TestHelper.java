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

    public static CMInternalConnectionInfo createConnInfo (String commId, String commName, String desc, String servDesc, String url, String servName, String stateName, boolean supportsLift, String protocolName) {
        String hcid = commId;
        String name = commName;
        String description = desc;
        boolean flag = supportsLift;

        CMInternalConnectionInfoLiftProtocols protocols = new CMInternalConnectionInfoLiftProtocols();
        CMInternalConnectionInfoLiftProtocol protocol = new CMInternalConnectionInfoLiftProtocol();
        protocols.getProtocol().add(protocol);
        protocol.setLiftProtocol(protocolName);

        CMInternalConnInfoServices services = new CMInternalConnInfoServices();
        CMInternalConnInfoService service = new CMInternalConnInfoService();
        services.getService().add(service);
        service.setDescription(servDesc);
        service.setEndpointURL(url);
        service.setExternalService(false);
        service.setName(servName);
        service.setSupportsLIFTFlag(flag);
        service.setLiftProtocols(protocols);

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

     public static boolean assertBusinessEntityEmpty (CMBusinessEntity busEntity) {
        if (busEntity.getHomeCommunityId().equals("") &&
                busEntity.getBusinessKey().equals("") &&
                busEntity.getPublicKey().equals("") &&
                busEntity.getPublicKeyURI().equals("") &&
                busEntity.getBusinessServices() == null &&
                busEntity.getDescriptions() == null &&
                busEntity.getDiscoveryURLs() == null &&
                busEntity.isFederalHIE() == false &&
                busEntity.getContacts() == null &&
                busEntity.getStates() == null) {
            return true;
        }
        return false;
    }

     public static boolean assertConnInfoNotEmpty (CMBusinessEntity busEntity) {
       if (NullChecker.isNotNullish(busEntity.getBusinessServices().getBusinessService()) ||
               NullChecker.isNotNullish(busEntity.getDescriptions().getBusinessDescription()) ||
               NullChecker.isNotNullish(busEntity.getDiscoveryURLs().getDiscoveryURL()) ||
               NullChecker.isNotNullish(busEntity.getContacts().getContact()) ||
               NullChecker.isNotNullish(busEntity.getStates().getState()) ||
               !busEntity.getHomeCommunityId().equals("") ||
               !busEntity.getBusinessKey().equals("") ||
               !busEntity.getPublicKey().equals("") ||
               !busEntity.getPublicKeyURI().equals("")) {
           return true;
       }
       return false;
    }

}
