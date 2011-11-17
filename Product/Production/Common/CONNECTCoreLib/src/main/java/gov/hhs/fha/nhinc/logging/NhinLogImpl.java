/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.logging;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author zmelnick
 */
public class NhinLogImpl implements NhinLog {

    private static Log generalLog;
    private static Log errorLog;
    private static Log performanceLog;
    private static Log transactionLog;

    public NhinLogImpl(Class<?> aClass) {
        generalLog = LogFactory.getLog(aClass);
        errorLog = LogFactory.getLog("Error");
        performanceLog = LogFactory.getLog("Performance");
        transactionLog = LogFactory.getLog("Transaction");
    }

    public Log getGeneralLog() {
        return generalLog;
    }

    public void writeToErrorLog(String message) {
        errorLog.error(message);
    }

    public void writeToPerformanceLog(String message) {
        performanceLog.info(message);
    }

    public void writeToTransactionLog(String message) {
        transactionLog.info(message);
    }

}
