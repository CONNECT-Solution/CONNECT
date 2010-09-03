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

package gov.hhs.fha.nhinc.adapter.cppgui.valueobject;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author patlollav
 */
public class PatientPreferencesVO
{
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

    public void addFineGrainedPolicyCriterion(FineGrainedPolicyCriterionVO criterion)
    {
        if (fineGrainedPolicyCriteria == null)
        {
            fineGrainedPolicyCriteria = new ArrayList<FineGrainedPolicyCriterionVO>();
            if (criterion.getPolicyOID() != null && !criterion.getPolicyOID().isEmpty()) {
                fineGrainedPolicyCriteria.add(criterion);
            }
            else
            {
                criterion.setPolicyOID("1");
                fineGrainedPolicyCriteria.add(criterion);
           }
        }
        else
        {
            if (criterion.getPolicyOID() != null && !criterion.getPolicyOID().isEmpty()) {
                fineGrainedPolicyCriteria.add(criterion);
            }
            else
            {
                int pOID = 0;
                // Find the max policyOID
                for(FineGrainedPolicyCriterionVO fineGrainedPolicyCriterionVO : this.fineGrainedPolicyCriteria)
                {
                    int tempPOID = Integer.parseInt(fineGrainedPolicyCriterionVO.getPolicyOID());

                    if (tempPOID > pOID)
                    {
                        pOID = tempPOID;
                    }
                }
                
                StringBuffer newPolicyOID = new StringBuffer();
                newPolicyOID.append(pOID+1);
                criterion.setPolicyOID(newPolicyOID.toString());
                fineGrainedPolicyCriteria.add(criterion);
            }
        }
    }

}
