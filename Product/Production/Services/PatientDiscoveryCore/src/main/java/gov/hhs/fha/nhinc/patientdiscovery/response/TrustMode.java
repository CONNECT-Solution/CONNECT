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
package gov.hhs.fha.nhinc.patientdiscovery.response;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.proxy.PatientCorrelationProxy;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.proxy.PatientCorrelationProxyObjectFactory;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscovery201305Processor;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.transform.subdisc.HL7PRPA201301Transforms;
import java.util.ArrayList;
import java.util.List;
import org.hl7.v3.II;
import org.hl7.v3.PRPAIN201301UV02;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.PRPAIN201306UV02MFMIMT700711UV01Subject1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dunnek
 */
public class TrustMode implements ResponseMode {

    private static final Logger LOG = LoggerFactory.getLogger(TrustMode.class);

    public TrustMode() {
        super();
    }

    /**
     * Process Patient Discovery Response
     *
     * @param params
     * @return response
     */
    @Override
    public PRPAIN201306UV02 processResponse(ResponseParams params) {
        LOG.debug("Begin TrustMode.processResponse()...");
        PRPAIN201306UV02 response = null;

        if (params != null) {
            response = params.response;
            AssertionType assertion = params.assertion;
            PRPAIN201305UV02 requestMsg = params.origRequest.getPRPAIN201305UV02();

            List<PRPAIN201306UV02MFMIMT700711UV01Subject1> pRPAINSubjects = new ArrayList<>();
            boolean hasResponseSubjectObj = false;
            if (response != null && response.getControlActProcess() != null
                    && NullChecker.isNotNullish(response.getControlActProcess().getSubject())) {
                pRPAINSubjects = response.getControlActProcess().getSubject();
                LOG.debug("processResponse - Subjects size: {}",pRPAINSubjects.size());
                hasResponseSubjectObj = true;
            } else {
                LOG.debug("processResponse - response/subjects is null");
            }

         
            II localPatId = getPatientId(requestMsg);

            if (requestHasLivingSubjectId(requestMsg) && localPatId != null) {
                for (PRPAIN201306UV02MFMIMT700711UV01Subject1 pRPAINSubject : pRPAINSubjects) {
                    processTrustMode(hasResponseSubjectObj, response, pRPAINSubject, localPatId, assertion);
                }
            } else {
                LOG.debug("Local Patient Id was not provided, no correlation will be attempted");
            }
        } else {
            LOG.warn("processResponse - params is null");
        }
        LOG.debug("End TrustMode.processResponse()...");
        return response;
    }

    private void processTrustMode(boolean hasResponseSubjectObj, PRPAIN201306UV02 response,
            PRPAIN201306UV02MFMIMT700711UV01Subject1 pRPAINSubject, II localPatId, AssertionType assertion) {

        int pRPAINSubjectInd = -1;
        if (hasResponseSubjectObj) {
            pRPAINSubjectInd = response.getControlActProcess().getSubject().indexOf(pRPAINSubject);
        }
        LOG.debug("processResponse - SubjectIndex: {}", pRPAINSubjectInd);

        PRPAIN201306UV02MFMIMT700711UV01Subject1 subjReplaced = response.getControlActProcess().getSubject()
                .set(0, pRPAINSubject);
        response.getControlActProcess().getSubject().set(pRPAINSubjectInd, subjReplaced);

        try {
            II remotePatId = getPatientId(response);
            if (remotePatId != null) {
                sendToPatientCorrelationComponent(localPatId, remotePatId, assertion, response);
            } else {
                LOG.error("One or more of the Patient Id values are null");
            }
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
        response.getControlActProcess().getSubject().set(pRPAINSubjectInd, pRPAINSubject);
        response.getControlActProcess().getSubject().set(0, subjReplaced);

    }

    /**
     * Process Patient Discovery Response
     *
     * @param response
     * @param assertion
     * @param localPatId
     * @return response
     */
    @Override
    public PRPAIN201306UV02 processResponse(PRPAIN201306UV02 response, AssertionType assertion, II localPatId) {
        LOG.debug("begin processResponse");
        if (response != null) {
            if (localPatId != null) {
                List<PRPAIN201306UV02MFMIMT700711UV01Subject1> pRPAINSubjects = new ArrayList<>();
                if (response.getControlActProcess() != null
                        && NullChecker.isNotNullish(response.getControlActProcess().getSubject())) {
                    pRPAINSubjects = response.getControlActProcess().getSubject();
                    LOG.debug("processResponse - Subjects size: " + pRPAINSubjects.size());
                } else {
                    LOG.debug("processResponse - response/subjects is null");
                }

                II remotePatId;
                for (PRPAIN201306UV02MFMIMT700711UV01Subject1 pRPAINSubject : pRPAINSubjects) {
                    int pRPAINSubjectInd = response.getControlActProcess().getSubject().indexOf(pRPAINSubject);
                    LOG.debug("processResponse - SubjectIndex: " + pRPAINSubjectInd);

                    PRPAIN201306UV02MFMIMT700711UV01Subject1 subjReplaced = response.getControlActProcess()
                            .getSubject().set(0, pRPAINSubject);
                    response.getControlActProcess().getSubject().set(pRPAINSubjectInd, subjReplaced);

                    try {
                        remotePatId = getPatientId(response);
                        if (remotePatId != null) {
                            sendToPatientCorrelationComponent(localPatId, remotePatId, assertion, response);
                        } else {
                            LOG.error("One or more of the Patient Id values are null");
                        }
                    } catch (Exception ex) {
                        LOG.error(ex.getMessage(), ex);
                    }

                    response.getControlActProcess().getSubject().set(pRPAINSubjectInd, pRPAINSubject);
                    response.getControlActProcess().getSubject().set(0, subjReplaced);
                }
            } else {
                LOG.debug("Local Patient Id was not provided, no correlation will be attempted");
            }
        } else {
            LOG.warn("processResponse - response is null, no correlation will be attempted");
        }
        LOG.debug("End TrustMode.processResponse()...");
        return response;
    }

    protected void sendToPatientCorrelationComponent(II localPatId, II remotePatId, AssertionType assertion,
            PRPAIN201306UV02 response) {

        if (localPatId != null && NullChecker.isNotNullish(localPatId.getRoot())
                && NullChecker.isNotNullish(localPatId.getExtension()) && remotePatId != null
                && NullChecker.isNotNullish(remotePatId.getRoot())
                && NullChecker.isNotNullish(remotePatId.getExtension())) {
            PRPAIN201301UV02 request = HL7PRPA201301Transforms.createPRPA201301(response, localPatId.getRoot());

            if (request != null
                    && request.getControlActProcess() != null
                    && NullChecker.isNotNullish(request.getControlActProcess().getSubject())
                    && request.getControlActProcess().getSubject().get(0) != null
                    && request.getControlActProcess().getSubject().get(0).getRegistrationEvent() != null
                    && request.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1() != null
                    && request.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1()
                            .getPatient() != null
                    && NullChecker.isNotNullish(request.getControlActProcess().getSubject().get(0)
                            .getRegistrationEvent().getSubject1().getPatient().getId())) {

                request.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient()
                        .getId().add(localPatId);

                PatientCorrelationProxyObjectFactory patCorrelationFactory = new PatientCorrelationProxyObjectFactory();
                PatientCorrelationProxy patCorrelationProxy = patCorrelationFactory.getPatientCorrelationProxy();
                patCorrelationProxy.addPatientCorrelation(request, assertion);
            }
        }
    }

    protected boolean requestHasLivingSubjectId(PRPAIN201305UV02 request) {
        boolean result;

        result = (getPatientId(request) != null);

        return result;
    }

    protected II getPatientId(PRPAIN201305UV02 request) {
        II localPatId = null;
        try {
            localPatId = new PatientDiscovery201305Processor().extractPatientIdFrom201305(request);
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
        return localPatId;
    }

    protected II getPatientId(PRPAIN201306UV02 request) {
        II patId = null;

        if (request != null
                && request.getControlActProcess() != null
                && NullChecker.isNotNullish(request.getControlActProcess().getSubject())
                && request.getControlActProcess().getSubject().get(0) != null
                && request.getControlActProcess().getSubject().get(0).getRegistrationEvent() != null
                && request.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1() != null
                && request.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient() != null
                && NullChecker.isNotNullish(request.getControlActProcess().getSubject().get(0).getRegistrationEvent()
                        .getSubject1().getPatient().getId())
                && request.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient()
                        .getId().get(0) != null
                && NullChecker.isNotNullish(request.getControlActProcess().getSubject().get(0).getRegistrationEvent()
                        .getSubject1().getPatient().getId().get(0).getExtension())
                && NullChecker.isNotNullish(request.getControlActProcess().getSubject().get(0).getRegistrationEvent()
                        .getSubject1().getPatient().getId().get(0).getRoot())) {

            patId = new II();
            patId.setRoot(request.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1()
                    .getPatient().getId().get(0).getRoot());
            patId.setExtension(request.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1()
                    .getPatient().getId().get(0).getExtension());
        }

        return patId;
    }

    protected PRPAIN201301UV02 createPRPA201301(PRPAIN201306UV02 input) {
        PRPAIN201301UV02 result;

        result = HL7PRPA201301Transforms.createPRPA201301(input, getLocalHomeCommunityId());

        return result;
    }

    protected String getLocalHomeCommunityId() {
        String result = "";

        try {
            result = PropertyAccessor.getInstance().getProperty(NhincConstants.GATEWAY_PROPERTY_FILE,
                    NhincConstants.HOME_COMMUNITY_ID_PROPERTY);
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
        return result;
    }

}
