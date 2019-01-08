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
package gov.hhs.fha.nhinc.mpi.adapter.component.hl7parsers;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hl7.v3.CE;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201305UV02QUQIMT021001UV01ControlActProcess;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectAdministrativeGender;
import org.hl7.v3.PRPAMT201306UV02ParameterList;
import org.hl7.v3.PRPAMT201306UV02PatientTelecom;
import org.hl7.v3.PRPAMT201306UV02QueryByParameter;
import org.hl7.v3.TELExplicit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author PVenkatakrishnan
 *
 */
public class HL7Parser201305Utils {

    private static final Logger LOG = LoggerFactory.getLogger(HL7Parser201305Utils.class);
    private static final HL7Parser201305Utils instance = new HL7Parser201305Utils();

    private HL7Parser201305Utils() {
    }

    public static HL7Parser201305Utils getInstance() {
        return instance;
    }

    /**
     * Method to extract Gender Code from a PRPAMT201306UV02ParameterList.
     *
     * @param params the Paramater list from which to extract a Gender Code
     * @return The Gender Code is returned
     */
    public String extractGender(PRPAMT201306UV02ParameterList params) {
        LOG.trace("Entering HL7Parser201305Utils.ExtractGender method...");

        String genderCode = null;

        // Extract the gender from the query parameters - Assume only one was specified
        if (CollectionUtils.isNotEmpty(params.getLivingSubjectAdministrativeGender())) {
            PRPAMT201306UV02LivingSubjectAdministrativeGender gender = params.getLivingSubjectAdministrativeGender()
                .get(0);

            if (CollectionUtils.isNotEmpty(gender.getValue()) && gender.getValue().get(0) != null) {
                CE administrativeGenderCode = gender.getValue().get(0);

                LOG.info("Found gender in query parameters : {} ", administrativeGenderCode.getCode());
                genderCode = administrativeGenderCode.getCode();
            } else {
                LOG.info("query does not contain a gender code");
            }
        } else {
            LOG.info("query does not contain a gender code");
        }

        LOG.trace("Exiting HL7Parser201305Utils.ExtractGender method...");
        return genderCode;
    }

    /**
     * Method to extract the Phone Number from a PRPAMT201306UV02ParameterList.
     *
     * @param params the PRPAMT201306UV02ParameterList from which to extract Phone Number
     * @return Phone Number from the PRPAMT201306UV02ParameterList
     */
    public String extractTelecom(PRPAMT201306UV02PatientTelecom patientTelecom) {
        LOG.trace("Entering HL7Parser201305Utils.ExtractTelecom method...");

        String telecom = null;
        if (CollectionUtils.isNotEmpty(patientTelecom.getValue())) {
            TELExplicit telecomValue = patientTelecom.getValue().get(0);
            LOG.info("Found patientTelecom in query parameters : {} ", telecomValue.getValue());
            telecom = telecomValue.getValue();
            if (!StringUtils.startsWith(telecom, "tel:")) {
                // telecom is not valid without tel: prefix
                telecom = null;
                LOG.info("Found patientTelecom in query parameters is not in the correct uri format");
            }
        } else {
            LOG.info("message does not contain a patientTelecom");
        }

        LOG.trace("Exiting HL7Parser201305Utils.ExtractTelecom method...");
        return telecom;
    }

    public PRPAMT201306UV02ParameterList extractHL7QueryParamsFromMessage(PRPAIN201305UV02 message) {
        LOG.trace("Entering HL7Parser201305.ExtractHL7QueryParamsFromMessage method...");
        PRPAMT201306UV02ParameterList queryParamList = null;

        if (message == null) {
            LOG.warn("input message was null, no query parameters present in message");
            return null;
        }

        PRPAIN201305UV02QUQIMT021001UV01ControlActProcess controlActProcess = message.getControlActProcess();
        if (controlActProcess == null) {
            LOG.info("controlActProcess is null - no query parameters present in message");
            return null;
        }

        if (controlActProcess.getQueryByParameter() != null
            && controlActProcess.getQueryByParameter().getValue() != null) {
            PRPAMT201306UV02QueryByParameter queryParams = controlActProcess.getQueryByParameter().getValue();

            if (queryParams.getParameterList() != null) {
                queryParamList = queryParams.getParameterList();
            }
        }

        LOG.trace("Exiting HL7Parser201305Utils.ExtractHL7QueryParamsFromMessage method...");
        return queryParamList;
    }

    public String formatNameString(String strValue, String str) {
        String nameString = str;
        LOG.info("contentItem is string");
        if (nameString != null) {
            nameString += strValue;
        } else {
            nameString = strValue;
        }
        LOG.info("nameString : {} ", nameString);
        return nameString;
    }
}
