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
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.hl7.v3.COCTMT090003UV01AssignedEntity;
import org.hl7.v3.II;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.PRPAIN201306UV02MFMIMT700711UV01ControlActProcess;
import org.hl7.v3.PRPAIN201306UV02MFMIMT700711UV01Subject1;

class PRPAIN201306UV02HCIDExtractor implements Function<PRPAIN201306UV02, Set<String>> {

    @Override
    public Set<String> apply(PRPAIN201306UV02 input) {
        Set<String> hcids = new HashSet<>();
        PRPAIN201306UV02MFMIMT700711UV01ControlActProcess controlActProcess = input.getControlActProcess();
        if (controlActProcess != null) {
            for (PRPAIN201306UV02MFMIMT700711UV01Subject1 subject : controlActProcess.getSubject()) {
                hcids.addAll(getSubjectHCIDs(subject));
            }
        }
        return hcids;
    }

    private static Set<String> getSubjectHCIDs(PRPAIN201306UV02MFMIMT700711UV01Subject1 subject) {
        Set<String> result = new HashSet<>();
        if (hasAssignedEntity(subject)) {
            result.addAll(getIis(subject.getRegistrationEvent().getCustodian().getAssignedEntity()));
        }
        return result;
    }

    private static boolean hasAssignedEntity(PRPAIN201306UV02MFMIMT700711UV01Subject1 subject) {
        return subject != null && subject.getRegistrationEvent() != null
            && subject.getRegistrationEvent().getCustodian() != null
            && subject.getRegistrationEvent().getCustodian().getAssignedEntity() != null;
    }

    private static List<String> getIis(COCTMT090003UV01AssignedEntity assignedEntity) {
        List<String> result = new ArrayList<>();
        for (II ii : assignedEntity.getId()) {
            String fromResponse = ii.getRoot();
            result.add(NhincConstants.HCID_PREFIX + fromResponse);
        }
        return result;
    }
}
