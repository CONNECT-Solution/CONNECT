/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.orchestration;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;

/**
 *
 * @author mweaver
 */
public interface Orchestratable {

    public boolean isEnabled();
    public boolean isPassthru();
    public AuditTransformer getAuditTransformer();
    public PolicyTransformer getPolicyTransformer();
    public AssertionType getAssertion();
}
