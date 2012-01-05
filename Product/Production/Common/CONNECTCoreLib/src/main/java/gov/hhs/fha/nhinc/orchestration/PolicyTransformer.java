/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.orchestration;

/**
 *
 * @author mweaver
 */
public interface PolicyTransformer {
    public enum Direction {INBOUND, OUTBOUND};
    public gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType transform(Orchestratable message, Direction direction);
}
