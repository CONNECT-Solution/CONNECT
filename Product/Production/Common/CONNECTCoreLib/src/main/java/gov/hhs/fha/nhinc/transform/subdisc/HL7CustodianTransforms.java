/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.transform.subdisc;

import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import org.hl7.v3.COCTMT090003UV01AssignedEntity;
import org.hl7.v3.MFMIMT700701UV01Custodian;
import org.hl7.v3.MFMIMT700711UV01Custodian;

/**
 *
 * @author jhoppesc
 */
public class HL7CustodianTransforms {

    public static MFMIMT700701UV01Custodian createMFMIMT700701UV01Custodian(String localDeviceId) {
        MFMIMT700701UV01Custodian custodian = new MFMIMT700701UV01Custodian();
        custodian.getTypeCode().add("CST");
        if (NullChecker.isNullish(localDeviceId)) {
            localDeviceId = getDefaultLocalDeviceId();
        }

        custodian.setAssignedEntity(createCOCTMT090003UVAssignedEntity(localDeviceId));

        return custodian;
    }

    public static COCTMT090003UV01AssignedEntity createCOCTMT090003UVAssignedEntity(String localDeviceId) {
        COCTMT090003UV01AssignedEntity entity = new COCTMT090003UV01AssignedEntity();
        entity.setClassCode(HL7Constants.ASSIGNED_DEVICE_CLASS_CODE);
        if (NullChecker.isNullish(localDeviceId)) {
            localDeviceId = getDefaultLocalDeviceId();
        }
        entity.getId().add(HL7DataTransformHelper.IIFactory(localDeviceId));

        return entity;
    }

    static MFMIMT700711UV01Custodian createMFMIMT700711UV01Custodian(String localDeviceId) {
        MFMIMT700711UV01Custodian custodian = new MFMIMT700711UV01Custodian();
        custodian.getTypeCode().add("CST");
        if (NullChecker.isNullish(localDeviceId)) {
            localDeviceId = getDefaultLocalDeviceId();
        }
        custodian.setAssignedEntity(createCOCTMT090003UVAssignedEntity(localDeviceId));

        return custodian;
    }

    private static String getDefaultLocalDeviceId() {
        return HL7Constants.DEFAULT_LOCAL_DEVICE_ID;
    }
}
