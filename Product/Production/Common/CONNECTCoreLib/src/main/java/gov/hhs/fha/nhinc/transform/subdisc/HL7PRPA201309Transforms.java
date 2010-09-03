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

import org.hl7.v3.*;
import javax.xml.bind.JAXBElement;
import java.util.ArrayList;

/**
 *
 * @author mflynn02
 */
public class HL7PRPA201309Transforms {
    public static PRPAIN201309UV02 createPRPA201309 (String homeCommunityId, String patientId) {
        PRPAIN201309UV02 result = new PRPAIN201309UV02();

        // For Audit, need ControlActProcess.queryParameters.ParameterList.paitentIdentified.root
        // and .extension.
        PRPAIN201309UV02QUQIMT021001UV01ControlActProcess controlActProcess = new PRPAIN201309UV02QUQIMT021001UV01ControlActProcess();
        PRPAMT201307UV02QueryByParameter queryParameter = new PRPAMT201307UV02QueryByParameter();
        PRPAMT201307UV02ParameterList parameterList = new  PRPAMT201307UV02ParameterList();
        PRPAMT201307UV02PatientIdentifier patientIdentifier = new PRPAMT201307UV02PatientIdentifier();
        patientIdentifier.getValue().add(0, HL7DataTransformHelper.IIFactory(homeCommunityId, patientId));
        parameterList.getPatientIdentifier().add(patientIdentifier);
        queryParameter.setParameterList(parameterList);
        
        org.hl7.v3.ObjectFactory factory = new org.hl7.v3.ObjectFactory();
        JAXBElement oJaxbElement = factory.createPRPAIN201309UV02QUQIMT021001UV01ControlActProcessQueryByParameter(queryParameter);

        controlActProcess.setQueryByParameter(oJaxbElement);
        result.setControlActProcess(controlActProcess);
        
        return result;
    }
}
