/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.adaptermpimanager.HL7Parsers;

import gov.hhs.fha.nhinc.mpilib.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.*;

/**
 *
 * @author rayj
 */
public class HL7Parser {
    private static Log log = LogFactory.getLog(HL7Parser.class);
    public static final String SSN_OID = "2.16.840.1.113883.4.1";
    
    
    public static void PrintMessageIdFromMessage(Object message) {
        log.debug("Begin HL7Parser.PrintMessageIdFromMessage(Object)");
        if (message != null) {
            if (message instanceof PRPAIN201301UV02) {
                HL7Parser201301.PrintMessageIdFromMessage((PRPAIN201301UV02) message);
            }

        }
        log.debug("End HL7Parser.PrintMessageIdFromMessage(Object)");
    }

    public static void PrintId(II id, String idname) {
        if (idname == null) {
            idname = "";
        }
        if (!(id == null)) {
            log.info(idname + ".id.root=" + id.getRoot() + ";");
            log.info(idname + ".id.extension=" + id.getExtension() + ";");
        } else {
            log.info("id for " + idname + " is null");
        }
    }

    public static void PrintId(java.util.List<II> ids, String idname) {
        for (II id : ids) {
            PrintId(id, idname);
        }
    }

}
