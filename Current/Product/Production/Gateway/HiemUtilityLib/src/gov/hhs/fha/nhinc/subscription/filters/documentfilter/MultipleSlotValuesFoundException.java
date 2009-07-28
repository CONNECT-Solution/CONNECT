/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.subscription.filters.documentfilter;

/**
 *
 * @author rayj
 */
public class MultipleSlotValuesFoundException extends Exception {

    /**
     * Creates a new instance of <code>MultipleSlotValuesFound</code> without detail message.
     */
    public MultipleSlotValuesFoundException() {
        super("Multiple slot values found when expected single result.");
    }

    /**
     * Constructs an instance of <code>MultipleSlotValuesFound</code> with the specified detail message.
     * @param msg the detail message.
     */
    public MultipleSlotValuesFoundException(String msg) {
        super(msg);
    }
}
