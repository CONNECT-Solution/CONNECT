/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nhinc.wsn.entities.filters;

/**
 *
 * @author rayj
 */
public interface IFilterCheck {
    boolean MeetsCriteria(org.oasis_open.docs.wsn.b_2.Notify notify);
}
