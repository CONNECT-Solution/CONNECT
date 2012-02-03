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

    public static CMInternalConnectionInfo createConnInfo (String commId, String commName, String desc, String servDesc,
            String url, String servName, String stateName, boolean supportsLift, String protocolName) {
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
