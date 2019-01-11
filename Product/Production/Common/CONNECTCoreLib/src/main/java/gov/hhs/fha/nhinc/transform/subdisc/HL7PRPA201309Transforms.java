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

import javax.xml.bind.JAXBElement;
import org.hl7.v3.PRPAIN201309UV02;
import org.hl7.v3.PRPAIN201309UV02QUQIMT021001UV01ControlActProcess;
import org.hl7.v3.PRPAMT201307UV02ParameterList;
import org.hl7.v3.PRPAMT201307UV02PatientIdentifier;
import org.hl7.v3.PRPAMT201307UV02QueryByParameter;

/**
 *
 * @author mflynn02
 */
public class HL7PRPA201309Transforms {
    public static PRPAIN201309UV02 createPRPA201309(String homeCommunityId, String patientId) {
        PRPAIN201309UV02 result = new PRPAIN201309UV02();

        // For Audit, need ControlActProcess.queryParameters.ParameterList.paitentIdentified.root
        // and .extension.
        PRPAIN201309UV02QUQIMT021001UV01ControlActProcess controlActProcess = new PRPAIN201309UV02QUQIMT021001UV01ControlActProcess();
        PRPAMT201307UV02QueryByParameter queryParameter = new PRPAMT201307UV02QueryByParameter();
        PRPAMT201307UV02ParameterList parameterList = new PRPAMT201307UV02ParameterList();
        PRPAMT201307UV02PatientIdentifier patientIdentifier = new PRPAMT201307UV02PatientIdentifier();
        patientIdentifier.getValue().add(0, HL7DataTransformHelper.IIFactory(homeCommunityId, patientId));
        parameterList.getPatientIdentifier().add(patientIdentifier);
        queryParameter.setParameterList(parameterList);

        org.hl7.v3.ObjectFactory factory = new org.hl7.v3.ObjectFactory();
        JAXBElement oJaxbElement = factory
                .createPRPAIN201309UV02QUQIMT021001UV01ControlActProcessQueryByParameter(queryParameter);

        controlActProcess.setQueryByParameter(oJaxbElement);
        result.setControlActProcess(controlActProcess);

        return result;
    }
}
