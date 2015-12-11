/*
 * Copyright (c) 2009-2015, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.admingui.util;

import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCacheHelper;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.LoggerFactory;
import org.uddi.api_v3.BusinessEntity;
import org.uddi.api_v3.KeyedReference;

/**
 *
 * @author achidamb
 */
public class RemoteOrganizationIdentifier {

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(RemoteOrganizationIdentifier.class);

    public Map<String, String> getRemoteHcidFromUUID() {

        Map<String, String> localOrganizationList = new HashMap<>();
        String homeCommunityId = null;
        String homeCommunityName = null;
        try {
            //get the all the entries from our UDDI file
            List<BusinessEntity> externalEntityList = ConnectionManagerCache.getInstance().getAllBusinessEntities();
            for (BusinessEntity businessEntity : externalEntityList) {
                //get the home community id and home community name
                if (businessEntity != null && businessEntity.getIdentifierBag() != null
                    && businessEntity.getIdentifierBag().getKeyedReference() != null
                    && !businessEntity.getIdentifierBag().getKeyedReference().isEmpty()) {
                    for (KeyedReference ref : businessEntity.getIdentifierBag().getKeyedReference()) {
                        if (ref.getTModelKey().equals(ConnectionManagerCacheHelper.UDDI_HOME_COMMUNITY_ID_KEY)) {
                            homeCommunityId = ref.getKeyValue();
                            break;
                        }
                    }
                    // get the homecommunity anme
                    if (businessEntity.getName() != null && !businessEntity.getName().isEmpty()) {
                        homeCommunityName = businessEntity.getName().get(0).getValue();
                    }
                    //add it to the organizationList
                    localOrganizationList.put(homeCommunityName, homeCommunityId);

                }
            }
        } catch (ConnectionManagerException ex) {
            LOG.error("Failed to retrieve Business Entities from UDDI file." + ex.getLocalizedMessage(), ex);
        }
        LOG.info("Organization name to display in ui:" + homeCommunityName);
        return localOrganizationList;
    }

}
