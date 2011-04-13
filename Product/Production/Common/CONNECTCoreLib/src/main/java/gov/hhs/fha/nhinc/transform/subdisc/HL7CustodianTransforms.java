/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.transform.subdisc;

import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.COCTMT090003UV01AssignedEntity;
import org.hl7.v3.MFMIMT700701UV01Custodian;
import org.hl7.v3.MFMIMT700711UV01Custodian;

/**
 *
 * @author jhoppesc
 */
public class HL7CustodianTransforms {

    private static Log log = LogFactory.getLog(HL7MessageIdGenerator.class);
    private static final String PROPERTY_FILE = "adapter";
    private static final String PROPERTY_NAME = "assigningAuthorityId";

    /**
     * Create custodian element based on the local device id.  The device id is
     * the corresponding assigning authority id.
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
     * Create assignedEntity element based on the local device id.  The device id is
     * the corresponding assigning authority id.
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
     * Create custodian element based on the local device id.  The device id is
     * the corresponding assigning authority id.
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

    private static String getDefaultLocalDeviceId() {
        String defaultLocalId = "";

        try {
            defaultLocalId = PropertyAccessor.getProperty(PROPERTY_FILE, PROPERTY_NAME);
        } catch (PropertyAccessException e) {
            log.error("PropertyAccessException - Default Assigning Authority property not defined in adapter.properties", e);
        }

        return defaultLocalId;
    }
}
