/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docretrieve.nhin;

import gov.hhs.fha.nhinc.orchestration.InboundOrchestratable;

/**
 * 
 * @author mweaver
 */
public interface InboundDocRetrieveContextBuilder {
    public void setContextMessage(InboundOrchestratable message);
}
