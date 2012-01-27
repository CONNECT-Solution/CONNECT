/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.orchestration;

/**
 *
 * @author mweaver
 */
public interface InboundDelegate extends Delegate {
    public InboundOrchestratable process(InboundOrchestratable message);
    public void createErrorResponse(InboundOrchestratable message, String error);
}
