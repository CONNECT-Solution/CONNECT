/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.adaptercomponentmpi.hl7parsers;

//import gov.hhs.fha.nhinc.mpi.*;
//import gov.hhs.fha.nhinc.mpilib.*;
//import gov.hhs.fha.nhinc.nhinclib.NullChecker;
//import java.util.List;
//import java.io.Serializable;
//import java.util.Iterator;
//import javax.xml.bind.JAXBElement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.*;

/**
 *
 * @author rayj
 */
public class HL7Parser201305 {

    private static Log log = LogFactory.getLog(HL7Parser201305.class);

    public static PRPAMT201306UV02ParameterList ExtractHL7QueryParamsFromMessage(
            org.hl7.v3.PRPAIN201305UV02 message) {
        log.debug("Entering HL7Parser201305.ExtractHL7QueryParamsFromMessage method...");
        PRPAMT201306UV02ParameterList queryParamList = null;

        if (message == null) {
            log.warn("input message was null, no query parameters present in message");
            return null;
        }

        PRPAIN201305UV02QUQIMT021001UV01ControlActProcess controlActProcess = message.getControlActProcess();
        if (controlActProcess == null) {
            log.info("controlActProcess is null - no query parameters present in message");
            return null;
        }

        if (controlActProcess.getQueryByParameter() != null &&
                controlActProcess.getQueryByParameter().getValue() != null) {
            PRPAMT201306UV02QueryByParameter queryParams = (PRPAMT201306UV02QueryByParameter) controlActProcess.getQueryByParameter().getValue();

            if (queryParams.getParameterList() != null) {
                queryParamList = queryParams.getParameterList();
            }

        }

        log.debug("Exiting HL7Parser201305.ExtractHL7QueryParamsFromMessage method...");
        return queryParamList;
    }
}
