/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.logging;

import org.apache.commons.logging.Log;

/**
 *
 * @author zmelnick
 */
public interface NhinLog {

    public Log getGeneralLog();

    public void writeToErrorLog(String logMessage);

    public void writeToPerformanceLog(String logMessage);

    public void writeToTransactionLog(String logMessage);

}
