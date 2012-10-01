/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docretrieve.nhin;

/**
 * 
 * @author mweaver
 */
public interface InboundDocRetrieveStrategy {
    public void execute(InboundDocRetrieveOrchestratable message);
}
