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
package gov.hhs.fha.nhinc.admingui.util;

import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCacheHelper;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.util.HomeCommunityMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.uddi.api_v3.BusinessEntity;
import org.uddi.api_v3.KeyedReference;

public class ConnectionHelper {

    private static final Logger LOG = LoggerFactory.getLogger(ConnectionHelper.class);

    public HashMap<String, BusinessEntity> getRemoteHcidFromUUID() {

        HashMap<String, BusinessEntity> organizationMap = new HashMap<>();
        List<BusinessEntity> externalEntityList = getAllBusinessEntities();
        if (NullChecker.isNotNullish(externalEntityList)) {
            for (BusinessEntity businessEntity : externalEntityList) {
                List<KeyedReference> keyedReference = getKeyedReference(businessEntity);
                if (NullChecker.isNotNullish(keyedReference)) {
                    organizationMap.put(getEntityName(businessEntity), businessEntity);
                }
            }
        }
        return organizationMap;
    }

    public HashMap<String, BusinessEntity> getExternalEntitiesMap() {
        HashMap<String, BusinessEntity> organizationMap = getRemoteHcidFromUUID();
        if (organizationMap != null && !organizationMap.isEmpty()) {
            Iterator<Map.Entry<String, BusinessEntity>> it = organizationMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, BusinessEntity> entry = it.next();
                if (checkLocalHcid(
                        getHcidFromKeyedReference(entry.getValue().getIdentifierBag().getKeyedReference()))) {
                    it.remove();
                }
            }
        }
        return organizationMap;
    }

    private boolean checkLocalHcid(String EntityHcid) {
        return formatHcid(EntityHcid).equals(formatHcid(getLocalHcid()));
    }

    public String getHcidFromKeyedReference(List<KeyedReference> keyedRef) {
        String homeCommunityId = null;
        for (KeyedReference ref : keyedRef) {
            if (ref.getTModelKey().equals(ConnectionManagerCacheHelper.UDDI_HOME_COMMUNITY_ID_KEY)) {
                homeCommunityId = ref.getKeyValue();
                break;
            }
        }
        return homeCommunityId;
    }

    private String getEntityName(BusinessEntity businessEntity) {
        String homeCommunityName = null;
        if (businessEntity.getName() != null && !businessEntity.getName().isEmpty()) {
            homeCommunityName = businessEntity.getName().get(0).getValue();
        }
        return homeCommunityName;
    }

    private List<KeyedReference> getKeyedReference(BusinessEntity entity) {
        List<KeyedReference> keyedReference = new ArrayList<>();
        if (entity != null && entity.getIdentifierBag() != null && entity.getIdentifierBag().getKeyedReference() != null
                && !entity.getIdentifierBag().getKeyedReference().isEmpty()) {
            keyedReference = entity.getIdentifierBag().getKeyedReference();
        }
        return keyedReference;
    }

    protected List<BusinessEntity> getAllBusinessEntities() {
        List<BusinessEntity> businessEntities = null;
        try {
            businessEntities = ConnectionManagerCache.getInstance().getAllBusinessEntities();
        } catch (ConnectionManagerException ex) {
            LOG.error(
                    "Exception while retrieving BusinessEntities from UDDIConnectionInfo: " + ex.getLocalizedMessage(),
                    ex);
        }
        return businessEntities;
    }

    private String getLocalHcid() {
        String localHcid = null;
        try {
            localHcid = getPropertyAccessor().getProperty(NhincConstants.GATEWAY_PROPERTY_FILE,
                    NhincConstants.HOME_COMMUNITY_ID_PROPERTY);
        } catch (PropertyAccessException ex) {
            LOG.error("Exception retrieving property from gateway.properties: " + ex.getLocalizedMessage(), ex);
        }
        return localHcid;
    }

    private String formatHcid(String hcid) {
        String formattedHcid = hcid;
        if (hcid.startsWith(NhincConstants.HCID_PREFIX)) {
            formattedHcid = hcid.substring(NhincConstants.HCID_PREFIX.length(), hcid.length());
        }
        return formattedHcid;
    }

    protected PropertyAccessor getPropertyAccessor() {
        return PropertyAccessor.getInstance();
    }

    public Map<String, String> getOrgNameRemoteHcidExternalEntities() {
        HashMap<String, String> organizationMap = new HashMap<>();
        List<BusinessEntity> externalEntityList = new ArrayList<>(getExternalEntitiesMap().values());
        if (NullChecker.isNotNullish(externalEntityList)) {
            for (BusinessEntity businessEntity : externalEntityList) {
                List<KeyedReference> keyedReference = getKeyedReference(businessEntity);
                if (NullChecker.isNotNullish(keyedReference)) {
                    organizationMap.put(getEntityName(businessEntity), keyedReference.get(0).getKeyValue());
                }
            }
        }
        return organizationMap;
    }

    public Map<String, String> getRemoteHcidOrgNameMap() {
        HashMap<String, String> organizationMap = new HashMap<>();
        List<BusinessEntity> entities = getAllBusinessEntities();
        if (NullChecker.isNotNullish(entities)) {
            for (BusinessEntity businessEntity : entities) {
                List<KeyedReference> keyedReference = getKeyedReference(businessEntity);
                if (NullChecker.isNotNullish(keyedReference)
                        && NullChecker.isNotNullishIgnoreSpace(keyedReference.get(0).getKeyValue())) {
                    organizationMap.put(
                            HomeCommunityMap.getHomeCommunityWithoutPrefix(keyedReference.get(0).getKeyValue()),
                            getEntityName(businessEntity));
                }
            }
        }
        return organizationMap;
    }
}
