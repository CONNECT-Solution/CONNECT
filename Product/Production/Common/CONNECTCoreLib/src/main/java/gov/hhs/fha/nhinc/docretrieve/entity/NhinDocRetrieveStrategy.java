/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docretrieve.entity;

/**
 *
 * @author mweaver
 */
public interface NhinDocRetrieveStrategy {
    public void execute(EntityDocRetrieveOrchestratable message);
}
