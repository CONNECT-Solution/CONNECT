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
package gov.hhs.fha.nhinc.docquery.xdsb.helper;

/**
 *
 * @author tjafri
 */
import gov.hhs.fha.nhinc.docquery.xdsb.helper.XDSbConstants.ClassificationScheme;
import gov.hhs.fha.nhinc.docquery.xdsb.helper.XDSbConstants.IdentificationScheme;
import gov.hhs.fha.nhinc.docquery.xdsb.helper.XDSbConstants.ResponseSlotName;
import java.util.List;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ClassificationType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExternalIdentifierType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.IdentifiableType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.LocalizedStringType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ValueListType;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

/**
 * @author tjafri
 *
 */
public class XDSbAdhocQueryResponseHelperImpl implements XDSbAdhocQueryResponseHelper {

    @Override
    public String getSingleSlotValue(final ResponseSlotName slotName, final IdentifiableType identifiableType) {
        return getSingleSlotValue(slotName.toString(), identifiableType);
    }

    @Override
    public String getSingleSlotValue(final String customSlotName, final IdentifiableType identifiableType) {
        if (identifiableType != null && identifiableType.getSlot() != null) {
            for (final SlotType1 s : identifiableType.getSlot()) {
                if (StringUtils.equalsIgnoreCase(customSlotName, s.getName()) && s.getValueList() != null) {
                    final ValueListType vlt = s.getValueList();
                    if (vlt.getValue() != null && !vlt.getValue().isEmpty()) {
                        return vlt.getValue().get(0);
                    }
                }
            }
        }
        return null;
    }

    @Override
    public String getStatus(final ExtrinsicObjectType extrinsicObject) {
        String status = null;
        if (extrinsicObject != null) {
            status = extrinsicObject.getStatus();
        }
        return status;
    }

    @Override
    public String getTitle(final ExtrinsicObjectType extrinsicObject) {
        String title = null;
        if (extrinsicObject != null && extrinsicObject.getName() != null
            && extrinsicObject.getName().getLocalizedString() != null) {
            final List<LocalizedStringType> names = extrinsicObject.getName().getLocalizedString();
            if (CollectionUtils.isNotEmpty(names) && names.get(0) != null) {
                title = extrinsicObject.getName().getLocalizedString().get(0).getValue();
            }

        }
        return title;
    }

    @Override
    public String getClassificationValue(final ClassificationScheme classification,
        final ExtrinsicObjectType extrinsicObject) {
        final RegistryObjectType registryObjectType = getClassification(classification, extrinsicObject);
        if (registryObjectType != null && registryObjectType instanceof ClassificationType) {
            return ((ClassificationType) registryObjectType).getNodeRepresentation();
        }
        return null;
    }

    @Override
    public String getExternalIdentifierValue(final IdentificationScheme idScheme,
        final ExtrinsicObjectType extrinsicObject) {
        String value = null;
        final List<ExternalIdentifierType> externalIds = extrinsicObject.getExternalIdentifier();
        for (final ExternalIdentifierType id : externalIds) {
            if (StringUtils.equalsIgnoreCase(idScheme.toString(), id.getIdentificationScheme())) {
                value = id.getValue();
            }
        }
        return value;
    }

    @Override
    public RegistryObjectType getClassification(final ClassificationScheme classification,
        final ExtrinsicObjectType extrinsicObject) {
        RegistryObjectType registryObject = null;
        if (extrinsicObject != null) {
            final List<ClassificationType> classifications = extrinsicObject.getClassification();
            for (final ClassificationType c : classifications) {
                if (StringUtils.equalsIgnoreCase(c.getClassificationScheme(), classification.toString())) {
                    registryObject = c;
                }
            }
        }
        return registryObject;
    }

}
