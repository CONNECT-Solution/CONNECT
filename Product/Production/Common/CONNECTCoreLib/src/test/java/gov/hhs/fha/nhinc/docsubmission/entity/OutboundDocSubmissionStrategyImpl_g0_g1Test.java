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
public class OutboundDocSubmissionStrategyImpl_g0_g1Test {
    public OutboundDocSubmissionStrategyImpl_g0_g1Test() {
    }
    
    @Test
    public void testExecute() {
        System.out.println("execute");
        OutboundDocSubmissionOrchestratable message = null;
        OutboundDocSubmissionStrategyImpl_g0_g1 instance = new OutboundDocSubmissionStrategyImpl_g0_g1();
        instance.execute(message);
    }
}
