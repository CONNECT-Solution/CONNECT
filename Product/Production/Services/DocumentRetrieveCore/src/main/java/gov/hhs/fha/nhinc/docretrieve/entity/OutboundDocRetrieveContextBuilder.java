/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docretrieve.entity;

import gov.hhs.fha.nhinc.orchestration.OutboundOrchestratable;

/**
 * 
 * @author mweaver
 */
public interface OutboundDocRetrieveContextBuilder {
    public void setContextMessage(OutboundOrchestratable message);
}
