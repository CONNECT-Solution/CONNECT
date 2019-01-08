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
package gov.hhs.fha.nhinc.connectmgr;

import gov.hhs.fha.nhinc.exchange.transform.UDDIConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.UDDI_SPEC_VERSION;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.uddi.api_v3.BindingTemplate;
import org.uddi.api_v3.BusinessEntity;
import org.uddi.api_v3.BusinessService;
import org.uddi.api_v3.BusinessServices;
import org.uddi.api_v3.KeyedReference;

public class ConnectionManagerCacheHelper {

    private static final Logger LOG = LoggerFactory.getLogger(ConnectionManagerCacheHelper.class);

    /**
     * This method merge the businessServices from the uddiEntity to the internalEntity.
     *
     * @param internalEntity Internal Business Entity
     * @param uddiEntity UDDI Business Entity
     */
    public BusinessEntity mergeBusinessEntityServices(BusinessEntity internalEntity, BusinessEntity uddiEntity) {
        Map<String, BusinessService> internalServiceNames = new HashMap<>();
        for (BusinessService internalService : internalEntity.getBusinessServices().getBusinessService()) {
            internalServiceNames.put(internalService.getServiceKey(), internalService);
        }
        for (BusinessService uddiService : uddiEntity.getBusinessServices().getBusinessService()) {
            if (!internalServiceNames.containsKey(uddiService.getServiceKey())) {
                internalServiceNames.put(uddiService.getServiceKey(), uddiService);
            }
        }
        BusinessEntity mergedEntity = new BusinessEntity();
        List<BusinessService> mergedList = new ArrayList<>(internalServiceNames.values());
        BusinessServices mergedServices = new BusinessServices();
        mergedServices.getBusinessService().addAll(mergedList);
        mergedEntity.setBusinessServices(mergedServices);
        mergedEntity.setBusinessKey(internalEntity.getBusinessKey());
        mergedEntity.setCategoryBag(internalEntity.getCategoryBag());
        mergedEntity.setContacts(internalEntity.getContacts());
        mergedEntity.setDiscoveryURLs(internalEntity.getDiscoveryURLs());
        mergedEntity.setIdentifierBag(internalEntity.getIdentifierBag());
        mergedEntity.getName().addAll(internalEntity.getName());
        mergedEntity.getDescription().addAll(internalEntity.getDescription());
        mergedEntity.getSignature().addAll(internalEntity.getSignature());

        return mergedEntity;
    }

    public String getCommunityId(BusinessEntity businessEntity) {
        KeyedReference ref = getCommunityIdKeyReference(businessEntity);
        if (ref != null) {
            return ref.getKeyValue().trim();
        }
        return null;
    }

    public KeyedReference getCommunityIdKeyReference(BusinessEntity businessEntity) {
        if (businessEntity.getIdentifierBag() == null) {
            return null;
        }
        for (KeyedReference reference : businessEntity.getIdentifierBag().getKeyedReference()) {
            if (reference.getTModelKey().equals(UDDIConstants.UDDI_HOME_COMMUNITY_ID_KEY)) {
                return reference;
            }
        }
        return null;
    }

    public List<String> getStates(BusinessEntity businessEntity) {
        List<String> result = new ArrayList<>();
        for (KeyedReference reference : businessEntity.getCategoryBag().getKeyedReference()) {
            String key = reference.getTModelKey();
            String value = reference.getKeyValue();
            if (UDDIConstants.UDDI_STATE_KEY.equals(key)) {
                result.add(value);
            }
        }
        if (CollectionUtils.isEmpty(result)) {
            result = null;
        }
        return result;
    }

    /**
     * This method looks for the entity with the given home community ID and returns it.
     *
     * @param oEntities The entities to be searched.
     * @param sHomeCommunityId The home community ID to search for.
     * @return The business entity for that home community.
     */
    public BusinessEntity extractBusinessEntity(List<BusinessEntity> oEntities, String sHomeCommunityId) {
        if (CollectionUtils.isEmpty(oEntities) || StringUtils.isEmpty(sHomeCommunityId)) {
            return null;
        }

        for (BusinessEntity oEntity : oEntities) {
            String homeCommunityId = getCommunityId(oEntity);
            if (homeCommunityId != null && homeCommunityId.equals(sHomeCommunityId)) {
                return oEntity;
            }
        }

        return null; // If we got here - we never found it.
    }

    /*
     * This method searches for the business entity in the list that has the same home community Id. If it finds it, it
     * replaces it with this one. If it does not find it, then it adds this one to the list.
     *
     * @param oEntities The entities to search.
     *
     * @param oEntity The entity to replace...
     */
    public void replaceBusinessEntity(List<BusinessEntity> oEntities, BusinessEntity oEntity) {
        if (oEntity != null) {
            if (oEntities == null) {
                oEntities = new ArrayList<>();
            }

            int iCnt = oEntities.size();
            if (iCnt == 0) {
                oEntities.add(oEntity);
                return;
            }
            String homeCommunityId = getCommunityId(oEntity);
            for (int i = 0; i < iCnt; i++) {
                BusinessEntity oLocalEntity = oEntities.get(i);
                String localHomeCommunityId = getCommunityId(oLocalEntity);
                if (localHomeCommunityId != null && homeCommunityId != null
                    && localHomeCommunityId.equals(homeCommunityId)) {
                    oEntities.set(i, oEntity);
                    return; // We are done
                }
            }

            oEntities.add(oEntity);
        }
    }

    public List<UDDI_SPEC_VERSION> getSpecVersionsFromBusinessEntity(BusinessEntity businessEntity,
        NhincConstants.NHIN_SERVICE_NAMES serviceName) {
        List<UDDI_SPEC_VERSION> specVersionList = new ArrayList<>();
        if (businessEntity == null) {
            return specVersionList;
        }

        for (BusinessService service : businessEntity.getBusinessServices().getBusinessService()) {
            if (!isServiceNameEquals(service, serviceName.getUDDIServiceName())) {
                continue;
            }

            specVersionList = getSpecVersions(service);
        }

        return specVersionList;
    }

    public boolean isServiceNameEquals(BusinessService service, String serviceName) {
        List<String> snameList = getServiceNames(service);
        for (String sname : snameList) {
            if (sname.equalsIgnoreCase(serviceName)) {
                return true;
            }
        }
        return false;
    }

    public List<String> getServiceNames(BusinessService service) {
        List<String> serviceNameList = new ArrayList<>();

        if (service.getCategoryBag() != null && service.getCategoryBag().getKeyedReference() != null) {
            for (KeyedReference reference : service.getCategoryBag().getKeyedReference()) {
                String keyName = reference.getTModelKey();
                String keyValue = reference.getKeyValue();
                if (keyName.equals(UDDIConstants.UDD_SERVICE_NAMES_KEY)) {
                    serviceNameList.add(keyValue);
                }
            }
        }
        return serviceNameList;
    }

    public List<UDDI_SPEC_VERSION> getSpecVersions(BusinessService businessService) {
        List<UDDI_SPEC_VERSION> specVersionList = new ArrayList<>();
        if (businessService == null || businessService.getBindingTemplates() == null
            || businessService.getBindingTemplates().getBindingTemplate() == null) {
            return specVersionList;
        }

        for (BindingTemplate bindingTemplate : businessService.getBindingTemplates().getBindingTemplate()) {
            if (bindingTemplate.getCategoryBag() != null
                && bindingTemplate.getCategoryBag().getKeyedReference() != null) {
                for (KeyedReference reference : bindingTemplate.getCategoryBag().getKeyedReference()) {
                    String keyName = reference.getTModelKey();
                    String specVersionValue = reference.getKeyValue();
                    if (keyName.equals(UDDIConstants.UDDI_SPEC_VERSION_KEY)) {
                        specVersionList.add(UDDI_SPEC_VERSION.fromString(specVersionValue));
                    }
                }
            }
        }
        return specVersionList;
    }

    public UDDI_SPEC_VERSION getHighestUDDISpecVersion(List<UDDI_SPEC_VERSION> specVersions) {
        UDDI_SPEC_VERSION highestSpecVersion = null;

        try {
            for (UDDI_SPEC_VERSION specVersion : specVersions) {
                if (highestSpecVersion == null || specVersion.ordinal() > highestSpecVersion.ordinal()) {
                    highestSpecVersion = specVersion;
                }
            }
        } catch (Exception ex) {
            LOG.error("Error deducing highest spec version.", ex);
        }

        return highestSpecVersion;
    }

    public BindingTemplate findBindingTemplateByCategoryBagNameValue(BusinessEntity businessEntity, String serviceName,
        String key, String value) {
        BindingTemplate bindingTemplate;
        if (businessEntity != null && businessEntity.getBusinessServices() != null
            && businessEntity.getBusinessKey() != null) {
            for (BusinessService service : businessEntity.getBusinessServices().getBusinessService()) {
                if (!isServiceNameEquals(service, serviceName)) {
                    continue;
                }
                bindingTemplate = findBindingTemplateByKey(service, key, value);
                if (bindingTemplate != null) {
                    return bindingTemplate;
                }
            }
        }
        return null;
    }

    public BindingTemplate findBindingTemplateByKey(BusinessService service, String keyRefName, String keyRefValue) {
        BindingTemplate bindingTemplate = null;
        if (service.getBindingTemplates() != null && service.getBindingTemplates().getBindingTemplate() != null) {
            for (BindingTemplate template : service.getBindingTemplates().getBindingTemplate()) {
                if (template.getCategoryBag() != null && template.getCategoryBag().getKeyedReference() != null) {
                    for (KeyedReference reference : template.getCategoryBag().getKeyedReference()) {
                        String keyName = reference.getTModelKey();
                        String keyValue = reference.getKeyValue();
                        if (keyRefName.equals(keyName) && keyRefValue.equals(keyValue)) {
                            return template;
                        }
                    }
                }
            }
        }

        return bindingTemplate;
    }

    public BusinessService getBusinessServiceByServiceName(BusinessEntity entity, String sUniformServiceName)
        throws ConnectionManagerException {

        // Validation
        if (entity == null || entity.getBusinessServices() == null || sUniformServiceName == null
            || sUniformServiceName.length() <= 0) {
            return null;
        }

        for (BusinessService service : entity.getBusinessServices().getBusinessService()) {
            if (!isServiceNameEquals(service, sUniformServiceName)) {
                continue;
            }
            return service;
        }
        return null;
    }
}
