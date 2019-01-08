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
package gov.hhs.fha.nhinc.transform.subdisc;

import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import org.hl7.v3.COCTMT090003UV01AssignedEntity;
import org.hl7.v3.MFMIMT700701UV01Custodian;
import org.hl7.v3.MFMIMT700711UV01Custodian;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jhoppesc
 */
public class HL7CustodianTransforms {

    private static final Logger LOG = LoggerFactory.getLogger(HL7MessageIdGenerator.class);
    private static final String PROPERTY_FILE = "adapter";
    private static final String PROPERTY_NAME = "assigningAuthorityId";

    /**
     * Create custodian element based on the local device id. The device id is the corresponding assigning authority id.
     *
     * @param localDeviceId
     * @return custodian
     */
    public static MFMIMT700701UV01Custodian createMFMIMT700701UV01Custodian(String localDeviceId) {
        MFMIMT700701UV01Custodian custodian = new MFMIMT700701UV01Custodian();
        custodian.getTypeCode().add("CST");
        if (NullChecker.isNullish(localDeviceId)) {
            localDeviceId = getDefaultLocalDeviceId();
        }

        custodian.setAssignedEntity(createCOCTMT090003UVAssignedEntity(localDeviceId));

        return custodian;
    }

    /**
     * Create assignedEntity element based on the local device id. The device id is the corresponding assigning
     * authority id.
     *
     * @param localDeviceId
     * @return entity
     */
    public static COCTMT090003UV01AssignedEntity createCOCTMT090003UVAssignedEntity(String localDeviceId) {
        COCTMT090003UV01AssignedEntity entity = new COCTMT090003UV01AssignedEntity();
        entity.setClassCode(HL7Constants.ASSIGNED_DEVICE_CLASS_CODE);
        if (NullChecker.isNullish(localDeviceId)) {
            localDeviceId = getDefaultLocalDeviceId();
        }
        entity.getId().add(HL7DataTransformHelper.IIFactory(localDeviceId));

        return entity;
    }

    /**
     * Create custodian element based on the local device id. The device id is the corresponding assigning authority id.
     *
     * @param localDeviceId
     * @return custodian
     */
    public static MFMIMT700711UV01Custodian createMFMIMT700711UV01Custodian(String localDeviceId) {
        MFMIMT700711UV01Custodian custodian = new MFMIMT700711UV01Custodian();
        custodian.getTypeCode().add("CST");
        if (NullChecker.isNullish(localDeviceId)) {
            localDeviceId = getDefaultLocalDeviceId();
        }
        custodian.setAssignedEntity(createCOCTMT090003UVAssignedEntity(localDeviceId));

        return custodian;
    }

    protected static String getDefaultLocalDeviceId() {
        String defaultLocalId = "";

        try {
            defaultLocalId = PropertyAccessor.getInstance().getProperty(PROPERTY_FILE, PROPERTY_NAME);
        } catch (PropertyAccessException e) {
            LOG.error(
                    "PropertyAccessException - Default Assigning Authority property not defined in adapter.properties",
                    e);
        }

        return defaultLocalId;
    }
}
