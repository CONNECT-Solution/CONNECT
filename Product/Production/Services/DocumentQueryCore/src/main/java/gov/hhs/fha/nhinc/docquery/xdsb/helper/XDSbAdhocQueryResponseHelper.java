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

import gov.hhs.fha.nhinc.docquery.xdsb.helper.XDSbConstants.ClassificationScheme;
import gov.hhs.fha.nhinc.docquery.xdsb.helper.XDSbConstants.IdentificationScheme;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.IdentifiableType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectType;

/**
 *
 * @author tjafri
 */
public interface XDSbAdhocQueryResponseHelper {

    /**
     * Gets the single slot value.
     *
     * @param slotName the slot name
     * @param identifiableType the identifiable type
     * @return the single slot value
     */
    public String getSingleSlotValue(XDSbConstants.ResponseSlotName slotName, IdentifiableType identifiableType);

    /**
     * Gets the single slot value.
     *
     * @param customSlotName the custom slot name
     * @param identifiableType the identifiable type
     * @return the single slot value
     */
    public String getSingleSlotValue(String customSlotName, IdentifiableType identifiableType);

    /**
     * Gets the status.
     *
     * @param extrinsicObject the extrinsic object
     * @return the status
     */
    public String getStatus(ExtrinsicObjectType extrinsicObject);

    /**
     * Gets the title.
     *
     * @param extrinsicObject the extrinsic object
     * @return the title
     */
    public String getTitle(ExtrinsicObjectType extrinsicObject);

    /**
     * Gets the classification value.
     *
     * @param classification the classification
     * @param extrinsicObject the extrinsic object
     * @return the classification value
     */
    public String getClassificationValue(ClassificationScheme classification, ExtrinsicObjectType extrinsicObject);

    /**
     * Gets the classification.
     *
     * @param classification the classification
     * @param extrinsicObject the extrinsic object
     * @return the classification
     */
    public RegistryObjectType getClassification(ClassificationScheme classification, ExtrinsicObjectType extrinsicObject);

    /**
     * Gets the external identifier value.
     *
     * @param patientid the patientid
     * @param extrinsicObject the extrinsic object
     * @return the external identifier value
     */
    public String getExternalIdentifierValue(IdentificationScheme patientid, ExtrinsicObjectType extrinsicObject);

}
