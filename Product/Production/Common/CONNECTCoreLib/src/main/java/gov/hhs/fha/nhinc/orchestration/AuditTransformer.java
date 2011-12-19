/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.orchestration;

/**
 *
 * @author mweaver
 */
public interface AuditTransformer {
    public enum Direction {INBOUND, OUTBOUND};
    public gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType transform(Orchestratable message);
}
