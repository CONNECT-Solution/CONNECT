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
package gov.hhs.fha.nhinc.exchange.transform.fhir;

import gov.hhs.fha.nhinc.exchange.OrganizationListType;
import gov.hhs.fha.nhinc.exchange.directory.AddressType;
import gov.hhs.fha.nhinc.exchange.directory.ContactType;
import gov.hhs.fha.nhinc.exchange.directory.EndpointConfigurationListType;
import gov.hhs.fha.nhinc.exchange.directory.EndpointConfigurationType;
import gov.hhs.fha.nhinc.exchange.directory.EndpointListType;
import gov.hhs.fha.nhinc.exchange.directory.EndpointType;
import gov.hhs.fha.nhinc.exchange.directory.OrganizationType;
import gov.hhs.fha.nhinc.exchange.directory.PurposeOfUseListType;
import gov.hhs.fha.nhinc.exchange.directory.RolesListType;
import gov.hhs.fha.nhinc.exchange.directory.UsecaseListType;
import gov.hhs.fha.nhinc.exchange.transform.ExchangeTransformException;
import gov.hhs.fha.nhinc.exchange.transform.ExchangeTransforms;
import gov.hhs.fha.nhinc.exchange.transform.FHIRConstants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.dstu3.model.Address;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Endpoint;
import org.hl7.fhir.dstu3.model.Organization;
import org.hl7.fhir.dstu3.model.Organization.OrganizationContactComponent;
import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.dstu3.model.Resource;
import org.hl7.fhir.dstu3.model.ResourceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author tjafri
 */
public class FHIRTransform implements ExchangeTransforms<Bundle> {

    private static final Logger LOG = LoggerFactory.getLogger(FHIRTransform.class);

    @Override
    public OrganizationListType transform(Bundle bundle) throws ExchangeTransformException {
        try {
            return buildOrganization(FHIRTransformHelper.extractFhirOrgResourceList(bundle));
        } catch (Exception ex) {
            LOG.error("Transforming FHIR data resulted in exception: {}", ex.getLocalizedMessage(), ex);
            throw new ExchangeTransformException(ex);
        }
    }

    private static OrganizationListType buildOrganization(List<Organization> fhirOrgs) {
        OrganizationListType orgListType = null;
        if (CollectionUtils.isNotEmpty(fhirOrgs)) {
            orgListType = new OrganizationListType();
            for (Organization orgResource : fhirOrgs) {
                buildOrganization(orgListType, orgResource);
            }
        }
        return orgListType;
    }

    private static void buildOrganization(OrganizationListType orgListType, Organization orgFhir) {
        if (null != orgFhir) {
            String hcid = FHIRTransformHelper.extractHCID(orgFhir.getIdentifier());
            if (StringUtils.isEmpty(hcid)) {
                LOG.warn("Skipping Organization {}. The organization is null or without an hcid ", orgFhir.getName());
                return;
            }
            OrganizationType org = new OrganizationType();
            org.setName(orgFhir.getName());
            org.setHcid(hcid);
            org.setStatus(orgFhir.getActive());
            List< String> desc = FHIRTransformHelper.getDescription(orgFhir.getText());
            if (CollectionUtils.isNotEmpty(desc)) {
                org.getDescription().addAll(desc);
            }
            org.setType(FHIRTransformHelper.getOrgType(orgFhir.getType()));
            org.getContact().addAll(buildContactList(orgFhir.getContact()));
            org.setEndpointList(buildEndpointListType(orgFhir.getContained()));
            org.getAddress().addAll(buildAddressList(orgFhir.getAddress()));
            org.setPartOf(buildPartOf(orgFhir.getPartOf()));
            //For Later: Need to map target region. Currently there is no data to map TargetRegions
            orgListType.getOrganization().add(org);
        }
    }

    private static String buildPartOf(Reference partOf) {
        if (null != partOf && null != partOf.getIdentifier()) {
            return FHIRTransformHelper.extractHCID(partOf.getIdentifier());
        }
        return null;
    }

    private static List<ContactType> buildContactList(List<OrganizationContactComponent> fhirContacts) {
        List<ContactType> list = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(fhirContacts)) {
            for (OrganizationContactComponent fContact : fhirContacts) {
                buildContact(list, fContact);
            }
        }
        return list;
    }

    private static void buildContact(List<ContactType> contactList, OrganizationContactComponent fContact) {
        if (null != fContact) {
            ContactType contact = new ContactType();
            buildContactName(contact, fContact);
            contact.setRole(FHIRTransformHelper.extractPurpose(fContact));
            contact.getEmail().addAll(FHIRTransformHelper.extractTelecomContacts(FHIRConstants.EMAIL, fContact));
            contact.getPhone().addAll(FHIRTransformHelper.extractTelecomContacts(FHIRConstants.PHONE, fContact));
            if (fContact.hasAddress()) {
                AddressType addr = buildAddress(fContact.getAddress());
                if (addr != null) {
                    contact.getAddress().add(addr);
                }
            }
            contactList.add(contact);
        }
    }

    private static List<AddressType> buildAddressList(List<Address> fhirAddrList) {
        List<AddressType> list = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(fhirAddrList)) {
            for (Address fAddr : fhirAddrList) {
                AddressType addr = buildAddress(fAddr);
                if (null != addr) {
                    list.add(addr);
                }
            }
        }
        return list;
    }

    private static AddressType buildAddress(Address fhirAddr) {
        AddressType addr = null;
        if (null != fhirAddr) {
            addr = new AddressType();
            addr.getAddressLine().addAll(FHIRTransformHelper.extractAddressLine(fhirAddr.getLine()));
            addr.setCity(fhirAddr.getCity());
            addr.setCountry(fhirAddr.getCountry());
            addr.setState(fhirAddr.getState());
            addr.setZipcode(fhirAddr.getPostalCode());
            addr.setUse((null != fhirAddr.getUse()) ? fhirAddr.getUse().toCode() : null);
        }
        return addr;
    }

    private static void buildContactName(ContactType contact, OrganizationContactComponent fhirContact) {
        if (null != fhirContact.getName()) {
            contact.setFamilyName(fhirContact.getName().getFamily());
            contact.setGivenName(fhirContact.getName().getGivenAsSingleString());
            if (StringUtils.isNotEmpty(fhirContact.getName().getText())) {
                contact.getFullName().add(fhirContact.getName().getText());
            }
        }
    }

    private static EndpointListType buildEndpointListType(List<Resource> contianedResources) {
        EndpointListType epListType = null;
        if (CollectionUtils.isNotEmpty(contianedResources)) {
            Map<String, EndpointType> endpointMap = new HashMap<>();
            for (Resource res : contianedResources) {
                if (ResourceType.Endpoint.toString().equals(FHIRTransformHelper.extractResourceType(res.
                    getResourceType()))) {
                    buildEndpoint(endpointMap, (Endpoint) res);
                }
            }
            epListType = new EndpointListType();
            epListType.getEndpoint().addAll(endpointMap.values());
        }
        return epListType;

    }

    private static void buildEndpoint(Map<String, EndpointType> endpointMap, Endpoint ep) {
        String nwhinServiceName = FHIRTransformHelper.extractEndpointName(ep.getName());
        if (null == nwhinServiceName) {
            LOG.warn("Skipping endpoint {}, not a valid exchange service", ep.getName());
            return;
        }
        EndpointType epType;
        if (!endpointMap.containsKey(nwhinServiceName)) {
            epType = new EndpointType();
            epType.getName().add(nwhinServiceName);
            epType.setProfile(FHIRTransformHelper.extractCodeFromCodingElement(ep.getConnectionType()));
            epType.setPurposeOfUseList(buildPurposeOfUse(ep));
            epType.setRolesList(buildRoles(ep));
            epType.setUsecaseList(buildUseCases(ep));
            epType.getPayloadFormat().addAll(FHIRTransformHelper.extractPayloadMimeType(ep.getPayloadMimeType()));
            epType.getPayloadType().addAll(FHIRTransformHelper.extractPayloadType(ep.getPayloadType()));
        } else {
            epType = endpointMap.get(nwhinServiceName);
        }
        epType.setEndpointConfigurationList(buildEndpointConfig(epType, ep));
        endpointMap.put(nwhinServiceName, epType);
    }

    private static PurposeOfUseListType buildPurposeOfUse(Endpoint ep) {
        PurposeOfUseListType puList = new PurposeOfUseListType();
        puList.getPurposeOfUse().addAll(
            FHIRTransformHelper.extractExtension(FHIRConstants.PURPOSES_OF_USE, ep));
        return puList;
    }

    private static RolesListType buildRoles(Endpoint ep) {
        RolesListType list = new RolesListType();
        list.getRole().addAll(
            FHIRTransformHelper.extractExtension(FHIRConstants.ROLES, ep));
        return list;
    }

    private static UsecaseListType buildUseCases(Endpoint ep) {
        UsecaseListType list = new UsecaseListType();
        list.getUsecase().addAll(
            FHIRTransformHelper.extractExtension(FHIRConstants.USECASES, ep));
        return list;
    }

    private static EndpointConfigurationListType buildEndpointConfig(EndpointType epType, Endpoint ep) {
        EndpointConfigurationListType epConfigList = epType.getEndpointConfigurationList();
        if (null == epConfigList) {
            epConfigList = new EndpointConfigurationListType();
        }
        EndpointConfigurationType epConfig = new EndpointConfigurationType();
        epConfig.setVersion(FHIRTransformHelper.extractVersion(FHIRConstants.VERSION, ep));
        epConfig.setUrl(ep.getAddress());
        epConfigList.getEndpointConfiguration().add(epConfig);
        return epConfigList;
    }
}
