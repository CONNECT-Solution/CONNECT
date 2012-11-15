/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docretrieve.nhin;

import gov.hhs.fha.nhinc.orchestration.OrchestrationStrategy;

/**
 * 
 * @author mweaver
 */
public interface InboundDocRetrieveStrategy extends OrchestrationStrategy {
    public void execute(InboundDocRetrieveOrchestratable message);
}
