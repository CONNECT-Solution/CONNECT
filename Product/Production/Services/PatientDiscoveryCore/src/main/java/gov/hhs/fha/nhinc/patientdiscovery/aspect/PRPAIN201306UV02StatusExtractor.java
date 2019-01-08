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
package gov.hhs.fha.nhinc.patientdiscovery.aspect;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import gov.hhs.fha.nhinc.util.Base64Coder;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.hl7.v3.BinaryDataEncoding;
import org.hl7.v3.EDExplicit;
import org.hl7.v3.MCCIMT000300UV01Acknowledgement;
import org.hl7.v3.MCCIMT000300UV01AcknowledgementDetail;
import org.hl7.v3.PRPAIN201306UV02;

class PRPAIN201306UV02StatusExtractor implements Function<PRPAIN201306UV02, Set<String>> {
    private static final PRPAIN201306UV02StatusExtractor.StatusFunction STATUS_FUNCTION = new StatusFunction();

    @Override
    public Set<String> apply(PRPAIN201306UV02 input) {
        Set<String> statuses = new HashSet<>();

        for (MCCIMT000300UV01Acknowledgement acknowledgement : input.getAcknowledgement()) {
            List<Optional<String>> tmp = Lists.transform(acknowledgement.getAcknowledgementDetail(), STATUS_FUNCTION);
            statuses.addAll(Lists.newArrayList(Optional.presentInstances(tmp)));
        }
        return statuses;
    }

    private static class StatusFunction implements Function<MCCIMT000300UV01AcknowledgementDetail, Optional<String>> {

        @Override
        public Optional<String> apply(MCCIMT000300UV01AcknowledgementDetail detail) {
            EDExplicit edExplicit = detail.getText();
            if (edExplicit == null || edExplicit.getContent().isEmpty()) {
                return Optional.absent();
            }
            return Optional.of(convertContent(edExplicit));
        }

        private static String convertContent(EDExplicit edExplicit) {
            String rawText = edExplicit.getContent().get(0).toString(); // Not dealing with multiple contents
            if (edExplicit.getRepresentation().equals(BinaryDataEncoding.TXT)) {
                return rawText;
            } else {
                return Base64Coder.decodeString(rawText);
            }
        }
    }
}
