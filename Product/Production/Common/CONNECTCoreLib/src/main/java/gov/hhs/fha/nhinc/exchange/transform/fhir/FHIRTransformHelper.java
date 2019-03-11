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

import gov.hhs.fha.nhinc.exchange.transform.FHIRConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.CodeType;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.ContactPoint;
import org.hl7.fhir.dstu3.model.Endpoint;
import org.hl7.fhir.dstu3.model.Extension;
import org.hl7.fhir.dstu3.model.Identifier;
import org.hl7.fhir.dstu3.model.Narrative;
import org.hl7.fhir.dstu3.model.Organization;
import org.hl7.fhir.dstu3.model.Organization.OrganizationContactComponent;
import org.hl7.fhir.dstu3.model.ResourceType;
import org.hl7.fhir.dstu3.model.StringType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author tjafri
 */
public class FHIRTransformHelper {

    private static final String SEPARATOR = ",";
    private static final Logger LOG = LoggerFactory.getLogger(FHIRTransformHelper.class);

    private FHIRTransformHelper() {
    }

    public static List<Organization> extractFhirOrgResourceList(Bundle bundle) {
        List<Organization> orglist = null;
        if (null != bundle && CollectionUtils.isNotEmpty(bundle.getEntry()) && null != bundle.getEntry().listIterator()) {
            Iterator<Bundle.BundleEntryComponent> it = bundle.getEntry().iterator();
            orglist = new ArrayList<>();
            while (it.hasNext()) {
                Bundle.BundleEntryComponent entry = it.next();
                orglist.add((Organization) entry.getResource());
            }
        }
        return orglist;
    }

    public static List<String> extractTelecomContacts(String system, OrganizationContactComponent fhirContact) {
        if (null != fhirContact && CollectionUtils.isNotEmpty(fhirContact.getTelecom())) {
            return extractTelecomList(system, fhirContact.getTelecom());
        }
        return new ArrayList<>();
    }

    public static List<String> extractExtension(String extensionUrl, Endpoint ep) {
        List<String> displayList = new ArrayList<>();
        Iterator<Extension> itr = extractExtensionListFromParentExt(ep);
        if (null != itr) {
            while (itr.hasNext()) {
                Extension ext = itr.next();
                displayList.addAll(extractExtensionDisplayList(ext.getExtensionsByUrl(extensionUrl)));
            }
        }
        return displayList;
    }

    public static String extractVersion(String extensionUrl, Endpoint ep) {
        return extractExtension(extensionUrl, ep).get(0);
    }

    public static List<String> extractPayloadMimeType(List<CodeType> payloadList) {
        List<String> list = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(payloadList)) {
            for (CodeType code : payloadList) {
                list.add(code.getValue());
            }
        }
        return list;
    }

    public static List<String> extractPayloadType(List<CodeableConcept> payloadList) {
        List<String> list = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(payloadList)) {
            for (CodeableConcept code : payloadList) {
                list.addAll(extractCodeFromCodeableConcept(code));
            }
        }
        return list;
    }

    public static List<String> extractAddressLine(List<StringType> addrLine) {
        List<String> lines = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(addrLine)) {
            for (StringType strType : addrLine) {
                if (null != strType) {
                    lines.add(strType.toString());
                }
            }
        }
        return lines;
    }

    public static String extractCodeFromCodingElement(Coding coding) {
        return null != coding ? coding.getCode() : null;
    }

    public static String extractResourceType(ResourceType type) {
        return null != type ? type.toString() : null;
    }

    public static String extractPurpose(OrganizationContactComponent contact) {
        if (null != contact && null != contact.getPurpose()) {
            return contact.getPurpose().getText();
        } else {
            return null;
        }
    }

    public static String extractHCID(List<Identifier> idList) {
        String hcid = null;
        if (CollectionUtils.isNotEmpty(idList)) {
            for (Identifier id : idList) {
                hcid = extractHCID(id);
                if (null != hcid) {
                    break;
                }
            }
        }
        return hcid;
    }

    public static String extractHCID(Identifier id) {
        String hcid = null;
        if (null != id && null != id.getUse() && FHIRConstants.USE_OFFICIAL.equalsIgnoreCase(id.getUse().toString())
            && FHIRConstants.HCID_SYSTEM.equalsIgnoreCase(id.getSystem())) {
            hcid = id.getValue();
        }
        return hcid;
    }

    public static String extractEndpointName(String name) {
        NhincConstants.NHIN_SERVICE_NAMES[] nhinServices = NhincConstants.NHIN_SERVICE_NAMES.values();
        for (NhincConstants.NHIN_SERVICE_NAMES nhinService : nhinServices) {
            String serviceNames = PropertyAccessor.getInstance().getProperty(NhincConstants.FHIR_DIRECTORY_FILE,
                nhinService.getUDDIServiceName(), null);
            if (serviceNames == null) {
                LOG.warn("Service {} does not have any FHIR property defined", nhinService.getUDDIServiceName());
            } else if (isServiceNameMatch(serviceNames.split(SEPARATOR), name)) {
                return nhinService.getUDDIServiceName();
            }
        }
        return null;
    }

    public static List<String> getDescription(Narrative narritive) {
        List<String> desc = null;
        if (null != narritive) {
            desc = new ArrayList<>();
            desc.add(narritive.getDivAsString());
        }
        return desc;
    }

    public static String getOrgType(List<CodeableConcept> types) {
        return CollectionUtils.isNotEmpty(types) && null != types.get(0).getCodingFirstRep()
            ? types.get(0).getCodingFirstRep().getCode() : null;
    }

    private static boolean isServiceNameMatch(String[] altNames, String name) {
        for (String altName : altNames) {
            if (altName.equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    private static Iterator<Extension> extractExtensionListFromParentExt(Endpoint ep) {
        List<Extension> exList = ep.getExtension();
        if (CollectionUtils.isNotEmpty(exList)) {
            return exList.iterator();
        }
        return null;
    }

    private static List<String> extractExtensionDisplayList(List<Extension> extList) {
        List<String> displayList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(extList)) {
            for (Extension ext : extList) {
                extractDisplayStringFromCodeableConcept(displayList, (CodeableConcept) ext.getValue());
            }
        }
        return displayList;
    }

    private static void extractDisplayStringFromCodeableConcept(List<String> displayList,
        CodeableConcept codeableConcept) {
        if (null != codeableConcept && CollectionUtils.isNotEmpty(codeableConcept.getCoding())) {
            displayList.add(codeableConcept.getCoding().get(0).getDisplay());
        }
    }

    private static List<String> extractTelecomList(String system, List<ContactPoint> cpList) {
        List<String> list = new ArrayList<>();
        for (ContactPoint cp : cpList) {
            if (null != cp.getSystem() && system.equalsIgnoreCase(cp.getSystem().toString())
                && StringUtils.isNotEmpty(cp.getValue())) {
                list.add(cp.getValue());
            }
        }
        return list;
    }

    private static List<String> extractCodeFromCodeableConcept(CodeableConcept concept) {
        List<String> codes = new ArrayList<>();
        if (null != concept && CollectionUtils.isNotEmpty(concept.getCoding())) {
            for (Coding coding : concept.getCoding()) {
                codes.add(coding.getCode());
            }
        }
        return codes;
    }
}
