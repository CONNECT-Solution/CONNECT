/*
 * Copyright (c) 2009-2017, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.exchange.transform.uddi;

import gov.hhs.fha.nhinc.exchange.OrganizationListType;
import gov.hhs.fha.nhinc.exchange.directory.AddressType;
import gov.hhs.fha.nhinc.exchange.directory.ContactType;
import gov.hhs.fha.nhinc.exchange.directory.EndpointConfigurationListType;
import gov.hhs.fha.nhinc.exchange.directory.EndpointConfigurationType;
import gov.hhs.fha.nhinc.exchange.directory.EndpointListType;
import gov.hhs.fha.nhinc.exchange.directory.EndpointType;
import gov.hhs.fha.nhinc.exchange.directory.OrganizationType;
import gov.hhs.fha.nhinc.exchange.transform.ExchangeTransforms;
import gov.hhs.fha.nhinc.exchange.transform.UDDIConstants;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.uddi.api_v3.Address;
import org.uddi.api_v3.AddressLine;
import org.uddi.api_v3.BindingTemplate;
import org.uddi.api_v3.BusinessDetail;
import org.uddi.api_v3.BusinessEntity;
import org.uddi.api_v3.BusinessService;
import org.uddi.api_v3.Contact;
import org.uddi.api_v3.Description;
import org.uddi.api_v3.Email;
import org.uddi.api_v3.KeyedReference;
import org.uddi.api_v3.Name;
import org.uddi.api_v3.PersonName;
import org.uddi.api_v3.Phone;

/**
 *
 * @author tjafri
 */
public class UDDITransform implements ExchangeTransforms<BusinessDetail> {

    private static final Logger LOG = LoggerFactory.getLogger(UDDITransform.class);

    @Override
    public OrganizationListType transform(BusinessDetail bDetail) {
        return buildOrganization(bDetail);
    }

    private OrganizationListType buildOrganization(BusinessDetail srcDirectory) {
        OrganizationListType orgListType = new OrganizationListType();
        List<OrganizationType> orgList;
        if (null != srcDirectory && CollectionUtils.isNotEmpty(srcDirectory.getBusinessEntity())) {
            orgList = new ArrayList<>();
            for (BusinessEntity bEntity : srcDirectory.getBusinessEntity()) {
                OrganizationType org = new OrganizationType();
                org.setName(buildOrganizationName(bEntity));
                org.setHcid(getHomeCommunityID(bEntity));
                buildContacts(bEntity, org);
                buildEndpointList(bEntity, org);
                buildTargetRegions(bEntity, org);
                orgList.add(org);
            }
            orgListType.getOrganization().addAll(orgList);
        }
        return orgListType;
    }

    private static String getHomeCommunityID(BusinessEntity entity) {
        if (entity != null && entity.getIdentifierBag() != null
            && CollectionUtils.isNotEmpty(entity.getIdentifierBag().getKeyedReference())) {
            for (KeyedReference key : entity.getIdentifierBag().getKeyedReference()) {
                if (UDDIConstants.UDDI_HOME_COMMUNITY_ID_KEY.equalsIgnoreCase(key.getTModelKey())) {
                    return key.getKeyValue();
                }
            }
        }
        return null;
    }

    private void buildEndpointList(BusinessEntity entity, OrganizationType org) {
        EndpointListType epListType;
        if (null != entity && null != entity.getBusinessServices()
            && CollectionUtils.isNotEmpty(entity.getBusinessServices().getBusinessService())) {
            epListType = new EndpointListType();
            for (BusinessService service : entity.getBusinessServices().getBusinessService()) {
                buildServiceEndpoints(service, epListType);
            }
            org.setEndpointList(epListType);
        }
    }

    private static String buildOrganizationName(BusinessEntity entity) {
        if (entity != null && entity.getName() != null) {
            for (Name name : entity.getName()) {
                if (name != null && StringUtils.isNotEmpty(name.getValue())) {
                    return name.getValue();
                }
            }
        }
        return null;
    }

    private void buildServiceEndpoints(BusinessService service, EndpointListType epListType) {
        if (service != null && service.getBindingTemplates() != null
            && CollectionUtils.isNotEmpty(service.getBindingTemplates().getBindingTemplate())) {
            EndpointType endpoint = new EndpointType();
            buildDescription(service, endpoint);
            buildServiceName(service, endpoint);
            EndpointConfigurationListType epConfigType = new EndpointConfigurationListType();
            for (BindingTemplate bTemplate : service.getBindingTemplates().getBindingTemplate()) {
                List<EndpointConfigurationType> epConfigList = buildEndpointConfiguration(bTemplate);
                if (CollectionUtils.isNotEmpty(epConfigList)) {
                    epConfigType.getEndpointConfiguration().addAll(epConfigList);
                    endpoint.setEndpointConfigurationList(epConfigType);
                }
            }
            epListType.getEndpoint().add(endpoint);
        }
    }

    private static void buildDescription(BusinessService service, EndpointType endpoint) {
        StringBuilder description = new StringBuilder();
        if (CollectionUtils.isNotEmpty(service.getDescription())) {
            LOG.info("building endpoint decription(s)");
            for (Description desc : service.getDescription()) {
                if (desc != null) {
                    description.append(desc.getValue().trim());
                }
            }
            endpoint.setDescription(description.toString());
        }
    }

    private static void buildServiceName(BusinessService service, EndpointType endpoint) {
        if (service.getCategoryBag() != null
            && CollectionUtils.isNotEmpty(service.getCategoryBag().getKeyedReference())) {
            List<String> serviceNames = new ArrayList<>();
            for (KeyedReference reference : service.getCategoryBag().getKeyedReference()) {
                if (UDDIConstants.UDD_SERVICE_NAMES_KEY.equals(reference.getTModelKey())) {
                    serviceNames.add(reference.getKeyValue());
                }
            }
            endpoint.getName().addAll(serviceNames);
        }
    }

    private List<EndpointConfigurationType> buildEndpointConfiguration(BindingTemplate bTemplate) {
        List<EndpointConfigurationType> epConfigList = null;
        List<String> serviceVersions = getServiceVersion(bTemplate);
        if (CollectionUtils.isNotEmpty(serviceVersions)) {
            epConfigList = new ArrayList<>();
            for (String version : serviceVersions) {
                EndpointConfigurationType epConfig = new EndpointConfigurationType();
                epConfig.setVersion(version);
                epConfig.setUrl(bTemplate.getAccessPoint() != null ? bTemplate.getAccessPoint().getValue() : null);
                epConfigList.add(epConfig);
            }
        }
        return epConfigList;
    }

    private static List<String> getServiceVersion(BindingTemplate bTemplate) {
        List<String> versions = null;
        if (bTemplate.getCategoryBag() != null
            && CollectionUtils.isNotEmpty(bTemplate.getCategoryBag().getKeyedReference())) {
            versions = new ArrayList<>();
            for (KeyedReference keyRef : bTemplate.getCategoryBag().getKeyedReference()) {
                if (UDDIConstants.UDDI_SPEC_VERSION_KEY.equalsIgnoreCase(keyRef.getTModelKey())
                    || UDDIConstants.ADAPTER_API_KEY.equalsIgnoreCase(keyRef.getTModelKey())) {
                    versions.add(keyRef.getKeyValue());
                }
            }
        }
        return versions;
    }

    private static void buildContacts(BusinessEntity entity, OrganizationType org) {
        List<ContactType> contacts;
        if (null != entity && entity.getContacts() != null
            && CollectionUtils.isNotEmpty(entity.getContacts().getContact())) {
            contacts = new ArrayList<>();
            for (Contact contact : entity.getContacts().getContact()) {
                ContactType cType = new ContactType();
                buildPersonNames(contact, cType);
                buildEmails(contact, cType);
                buildPhones(contact, cType);
                buildAddresses(contact, cType);
                cType.setRole(buildRole(contact));
                contacts.add(cType);
            }
            org.getContact().addAll(contacts);
        }
    }

    private static void buildTargetRegions(BusinessEntity entity, OrganizationType org) {
        List<String> regions;
        if (null != entity && entity.getCategoryBag() != null
            && CollectionUtils.isNotEmpty(entity.getCategoryBag().getKeyedReference())) {
            regions = new ArrayList<>();
            for (KeyedReference keyRef : entity.getCategoryBag().getKeyedReference()) {
                if (UDDIConstants.UDDI_STATE_KEY.equalsIgnoreCase(keyRef.getTModelKey())) {
                    regions.add(keyRef.getKeyValue());
                }
            }
            org.getTargetRegion().addAll(regions);
        }
    }

    private static void buildEmails(Contact contact, ContactType cType) {
        List<String> emails;
        if (contact != null && CollectionUtils.isNotEmpty(contact.getEmail())) {
            emails = new ArrayList<>();
            for (Email obj : contact.getEmail()) {
                emails.add(obj.getValue());
            }
            cType.getEmail().addAll(emails);
        }
    }

    private static void buildPersonNames(Contact contact, ContactType cType) {
        List<String> personNames;
        if (contact != null && CollectionUtils.isNotEmpty(contact.getPersonName())) {
            personNames = new ArrayList<>();
            for (PersonName obj : contact.getPersonName()) {
                personNames.add(obj.getValue());
            }
            cType.getFullName().addAll(personNames);
        }
    }

    private static void buildPhones(Contact contact, ContactType cType) {
        List<String> phones;
        if (contact != null && CollectionUtils.isNotEmpty(contact.getPhone())) {
            phones = new ArrayList<>();
            for (Phone obj : contact.getPhone()) {
                phones.add(obj.getValue());
            }
            cType.getPhone().addAll(phones);
        }
    }

    private static void buildAddresses(Contact contact, ContactType cType) {
        List<AddressType> addressList;
        if (contact != null && CollectionUtils.isNotEmpty(contact.getAddress())) {
            addressList = new ArrayList<>();
            AddressType addressType = new AddressType();
            for (Address obj : contact.getAddress()) {
                buildAddressLine(obj, addressType);
            }
            addressList.add(addressType);
            cType.getAddress().addAll(addressList);
        }
    }

    private static void buildAddressLine(Address addr, AddressType addrType) {
        List<String> addrLineList;
        if (addr != null && CollectionUtils.isNotEmpty(addr.getAddressLine())) {
            addrLineList = new ArrayList<>();
            for (AddressLine addrLine : addr.getAddressLine()) {
                addrLineList.add(addrLine.getValue());
            }
            addrType.getAddressLine().addAll(addrLineList);
        }
    }

    private static String buildRole(Contact contact) {
        if (contact != null && StringUtils.isNotBlank(contact.getUseType())) {
            return contact.getUseType();
        }
        return null;
    }
}
