/*
 * Copyright (c) 2009-2015, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.adapter.cppgui.valueobject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author patlollav
 */
public class PatientPreferencesVO implements Serializable {

    private static final long serialVersionUID = 7671707960303952407L;

    private Boolean optIn;

    private List<FineGrainedPolicyCriterionVO> fineGrainedPolicyCriteria;

    public List<FineGrainedPolicyCriterionVO> getFineGrainedPolicyCriteria() {
        return fineGrainedPolicyCriteria;
    }

    public void setFineGrainedPolicyCriteria(List<FineGrainedPolicyCriterionVO> fineGrainedPolicyCriteria) {
        this.fineGrainedPolicyCriteria = fineGrainedPolicyCriteria;
    }

    public Boolean getOptIn() {
        return optIn;
    }

    public void setOptIn(Boolean optIn) {
        this.optIn = optIn;
    }

    public void addFineGrainedPolicyCriterion(FineGrainedPolicyCriterionVO criterion) {
        if (fineGrainedPolicyCriteria == null) {
            fineGrainedPolicyCriteria = new ArrayList<>();
            if (criterion.getPolicyOID() != null && !criterion.getPolicyOID().isEmpty()) {
                fineGrainedPolicyCriteria.add(criterion);
            } else {
                criterion.setPolicyOID("1");
                fineGrainedPolicyCriteria.add(criterion);
            }
        } else {
            if (criterion.getPolicyOID() != null && !criterion.getPolicyOID().isEmpty()) {
                fineGrainedPolicyCriteria.add(criterion);
            } else {
                int pOID = 0;
                // Find the max policyOID
                for (FineGrainedPolicyCriterionVO fineGrainedPolicyCriterionVO : this.fineGrainedPolicyCriteria) {
                    int tempPOID = Integer.parseInt(fineGrainedPolicyCriterionVO.getPolicyOID());

                    if (tempPOID > pOID) {
                        pOID = tempPOID;
                    }
                }

                StringBuffer newPolicyOID = new StringBuffer();
                newPolicyOID.append(pOID + 1);
                criterion.setPolicyOID(newPolicyOID.toString());
                fineGrainedPolicyCriteria.add(criterion);
            }
        }
    }

}
