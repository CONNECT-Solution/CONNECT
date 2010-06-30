/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.mpilib;

import java.util.ArrayList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author rayj
 */
public class PhoneNumbers extends ArrayList<PhoneNumber>
        implements java.io.Serializable {
    private static Log log = LogFactory.getLog(PhoneNumbers.class);
    static final long serialVersionUID =5000000000000000000L;

    public PhoneNumbers() {
        log.info("PhoneNumbers Initialized");
    }
}
