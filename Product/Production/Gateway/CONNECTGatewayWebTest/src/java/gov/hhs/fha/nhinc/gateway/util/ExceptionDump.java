package gov.hhs.fha.nhinc.gateway.util;

import java.io.PrintWriter;
import java.io.CharArrayWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Utility to dump exception stack trace
 * 
 * @author paul.eftis
 */
public class ExceptionDump {

    private static Log log = LogFactory.getLog(ExceptionDump.class);

    public static void outputCompleteException(Exception ex) {
        String err = "EXCEPTION:" + ex.getMessage() + "\r\n";
        CharArrayWriter caw = new CharArrayWriter();
        ex.printStackTrace(new PrintWriter(caw));
        err += caw.toString();
        log.error(err);
    }
}
