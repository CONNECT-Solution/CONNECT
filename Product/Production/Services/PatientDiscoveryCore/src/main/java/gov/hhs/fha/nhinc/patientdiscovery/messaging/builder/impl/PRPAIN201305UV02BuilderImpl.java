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
package gov.hhs.fha.nhinc.patientdiscovery.messaging.builder.impl;

import gov.hhs.fha.nhinc.patientdiscovery.model.Patient;
import gov.hhs.fha.nhinc.util.HomeCommunityMap;
import javax.xml.bind.JAXBElement;
import org.hl7.v3.CE;
import org.hl7.v3.CS;
import org.hl7.v3.ENExplicit;
import org.hl7.v3.EnExplicitFamily;
import org.hl7.v3.EnExplicitGiven;
import org.hl7.v3.II;
import org.hl7.v3.IVLTSExplicit;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectAdministrativeGender;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectBirthTime;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectId;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectName;
import org.hl7.v3.PRPAMT201306UV02ParameterList;
import org.hl7.v3.PRPAMT201306UV02QueryByParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class builds a PRPAIN201305UV02 request.
 *
 * @author tjafri
 */
public class PRPAIN201305UV02BuilderImpl extends AbstractPRPAIN201305UV02Builder {

    private Patient patient;

    private PRPAIN201305UV02 request;

    private static final Logger LOG = LoggerFactory.getLogger(PRPAIN201305UV02BuilderImpl.class);

    @Override
    public void build() {
        if (patient == null) {
            throw new IllegalArgumentException("Patient value is required.");
        }
        super.build();
        request = super.getMessage();
        buildQueryParams();
    }

    @Override
    public PRPAIN201305UV02 getMessage() {
        return request;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    private void buildQueryParams() {
        PRPAMT201306UV02QueryByParameter query = new PRPAMT201306UV02QueryByParameter();
        CS statusCode = new CS();
        statusCode.setCode("new");
        query.setStatusCode(statusCode);
        CS respModal = new CS();
        respModal.setCode("R");
        query.setResponseModalityCode(respModal);
        CS priority = new CS();
        priority.setCode("I");
        query.setResponsePriorityCode(priority);

        query.setParameterList(buildParameterList());
        JAXBElement<PRPAMT201306UV02QueryByParameter> queryJAX
            = new org.hl7.v3.ObjectFactory().createPRPAIN201305UV02QUQIMT021001UV01ControlActProcessQueryByParameter(query);
        request.getControlActProcess().setQueryByParameter(queryJAX);
        LOG.debug("ControlActProcess set for request: " + request);
    }

    private PRPAMT201306UV02ParameterList buildParameterList() {
        org.hl7.v3.ObjectFactory factory = new org.hl7.v3.ObjectFactory();

        PRPAMT201306UV02ParameterList parameterList = new PRPAMT201306UV02ParameterList();

        if (patient.getLastName() != null) {
            setNames(parameterList, factory);
        }

        if (patient.getBirthDate() != null) {
            setBirthTime(parameterList);
        }

        if (patient.getGender() != null) {
            setGender(parameterList);
        }

        if (patient.getPid() != null && patient.getDomain() != null) {
            setPid(parameterList);
        }

        return parameterList;
    }

    private void setNames(PRPAMT201306UV02ParameterList parameterList, org.hl7.v3.ObjectFactory factory) {
        PRPAMT201306UV02LivingSubjectName name = new PRPAMT201306UV02LivingSubjectName();
        EnExplicitFamily familyName = new EnExplicitFamily();
        familyName.setContent(patient.getLastName());
        familyName.setPartType("FAM");

        ENExplicit namesWrapper = new ENExplicit();
        namesWrapper.getContent().add(factory.createENExplicitFamily(familyName));

        if (patient.getFirstName() != null && !patient.getFirstName().isEmpty()) {
            EnExplicitGiven givenName = new EnExplicitGiven();
            givenName.setContent(patient.getFirstName());
            givenName.setPartType("GIV");

            namesWrapper.getContent().add(factory.createENExplicitGiven(givenName));

            if (patient.getMiddleName() != null && !patient.getMiddleName().isEmpty()) {
                EnExplicitGiven middleName = new EnExplicitGiven();
                middleName.setContent(patient.getMiddleName());
                middleName.setPartType("GIV");
                namesWrapper.getContent().add(factory.createENExplicitGiven(middleName));
            }
        }

        name.getValue().add(namesWrapper);

        parameterList.getLivingSubjectName().add(name);
    }

    private void setBirthTime(PRPAMT201306UV02ParameterList parameterList) {
        PRPAMT201306UV02LivingSubjectBirthTime birthTime = new PRPAMT201306UV02LivingSubjectBirthTime();
        IVLTSExplicit birthValue = new IVLTSExplicit();
        birthValue.setValue(patient.getBirthDate());
        birthTime.getValue().add(birthValue);
        parameterList.getLivingSubjectBirthTime().add(birthTime);
    }

    private void setGender(PRPAMT201306UV02ParameterList parameterList) {
        PRPAMT201306UV02LivingSubjectAdministrativeGender adminGender
            = new PRPAMT201306UV02LivingSubjectAdministrativeGender();
        CE code = new CE();
        code.setCode(patient.getGender());
        adminGender.getValue().add(code);
        parameterList.getLivingSubjectAdministrativeGender().add(adminGender);
    }

    private void setPid(PRPAMT201306UV02ParameterList parameterList) {
        PRPAMT201306UV02LivingSubjectId subjectId = new PRPAMT201306UV02LivingSubjectId();
        II id = new II();
        id.setExtension(patient.getPid());
        id.setRoot(patient.getDomain());
        subjectId.getValue().add(id);
        parameterList.getLivingSubjectId().add(subjectId);
    }

    @Override
    protected String getRemoteHcid() {
        return HomeCommunityMap.formatHomeCommunityId(patient.getOrganization());
    }
}
