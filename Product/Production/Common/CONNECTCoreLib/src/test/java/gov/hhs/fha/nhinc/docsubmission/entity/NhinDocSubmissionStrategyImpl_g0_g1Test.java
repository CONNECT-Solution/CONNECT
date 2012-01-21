/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docsubmission.entity;

import org.junit.Test;

/**
 *
 * @author zmelnick
 */
public class NhinDocSubmissionStrategyImpl_g0_g1Test {
    public NhinDocSubmissionStrategyImpl_g0_g1Test() {
    }
    
    @Test
    public void testExecute() {
        System.out.println("execute");
        EntityDocSubmissionOrchestratable message = null;
        NhinDocSubmissionStrategyImpl_g0_g1 instance = new NhinDocSubmissionStrategyImpl_g0_g1();
        instance.execute(message);
    }
}
