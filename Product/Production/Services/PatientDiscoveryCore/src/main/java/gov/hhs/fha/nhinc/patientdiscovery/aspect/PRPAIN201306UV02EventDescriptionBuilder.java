/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services. 
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
package gov.hhs.fha.nhinc.patientdiscovery.aspect;

import gov.hhs.fha.nhinc.event.AssertionEventDescriptionBuilder;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.util.Base64Coder;

import java.util.ArrayList;
import java.util.List;

import org.hl7.v3.BinaryDataEncoding;
import org.hl7.v3.COCTMT090003UV01AssignedEntity;
import org.hl7.v3.EDExplicit;
import org.hl7.v3.II;
import org.hl7.v3.MCCIMT000300UV01Acknowledgement;
import org.hl7.v3.MCCIMT000300UV01AcknowledgementDetail;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.PRPAIN201306UV02MFMIMT700711UV01ControlActProcess;
import org.hl7.v3.PRPAIN201306UV02MFMIMT700711UV01Subject1;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;

public class PRPAIN201306UV02EventDescriptionBuilder extends AssertionEventDescriptionBuilder {

    private static final StatusExtractor STATUS_EXTRACTOR = new StatusExtractor();

    private Optional<PRPAIN201306UV02> body = Optional.absent();

    public PRPAIN201306UV02EventDescriptionBuilder() {
    }

    @Override
    public void buildErrorCodes() {
    }

    @Override
    public void buildPayloadSizes() {
    }

    @Override
    public void buildPayloadTypes() {
    }

    @Override
    public void buildRespondingHCIDs() {
        if (!body.isPresent()) {
            return;
        }
        List<String> hcids = new ArrayList<String>();
        PRPAIN201306UV02MFMIMT700711UV01ControlActProcess controlActProcess = body.get().getControlActProcess();
        if (controlActProcess != null) {
            for (PRPAIN201306UV02MFMIMT700711UV01Subject1 subject : controlActProcess.getSubject()) {
                hcids.addAll(getSubjectHCIDs(subject));
            }
        }
        setRespondingHCIDs(hcids);
    }

    private List<String> getSubjectHCIDs(PRPAIN201306UV02MFMIMT700711UV01Subject1 subject) {
        List<String> result = new ArrayList<String>();
        if (hasAssignedEntity(subject)) {
            result.addAll(getIis(subject.getRegistrationEvent().getCustodian().getAssignedEntity()));
        }
        return result;
    }

    private boolean hasAssignedEntity(PRPAIN201306UV02MFMIMT700711UV01Subject1 subject) {
        return subject != null && subject.getRegistrationEvent() != null
                && subject.getRegistrationEvent().getCustodian() != null
                && subject.getRegistrationEvent().getCustodian().getAssignedEntity() != null;
    }

    private List<String> getIis(COCTMT090003UV01AssignedEntity assignedEntity) {
        List<String> result = new ArrayList<String>();
        for (II ii : assignedEntity.getId()) {
            String fromResponse = ii.getRoot();
            result.add(NhincConstants.HCID_PREFIX + fromResponse);
        }
        return result;
    }

    @Override
    public void buildStatuses() {
        if (!body.isPresent()) {
            return;
        }
        List<String> statuses = new ArrayList<String>();

        for (MCCIMT000300UV01Acknowledgement acknowledgement : body.get().getAcknowledgement()) {
            List<Optional<String>> tmp = Lists.transform(acknowledgement.getAcknowledgementDetail(), STATUS_EXTRACTOR);
            statuses.addAll(Lists.newArrayList(Optional.presentInstances(tmp)));
        }
        setStatuses(statuses);
    }

    @Override
    public void buildTimeStamp() {
    }

    @Override
    public void setArguments(Object... arguments) {
        extractAssertion(arguments);
    }

    @Override
    public void setReturnValue(Object returnValue) {
        if (returnValue == null || !(returnValue instanceof PRPAIN201306UV02)) {
            body = Optional.absent();
        } else {
            body = Optional.of((PRPAIN201306UV02) returnValue);
        }
    }

    private static class StatusExtractor implements Function<MCCIMT000300UV01AcknowledgementDetail, Optional<String>> {

        @Override
        public Optional<String> apply(MCCIMT000300UV01AcknowledgementDetail detail) {
            EDExplicit edExplicit = detail.getText();
            if (edExplicit == null || edExplicit.getContent().size() == 0) {
                return Optional.absent();
            }
            return Optional.of(convertContent(edExplicit));
        }

        private String convertContent(EDExplicit edExplicit) {
            String rawText = edExplicit.getContent().get(0).toString(); // Not dealing with multiple contents
            if (edExplicit.getRepresentation().equals(BinaryDataEncoding.TXT)) {
                return rawText;
            } else {
                return Base64Coder.decodeString(rawText);
            }
        }
    }
}
