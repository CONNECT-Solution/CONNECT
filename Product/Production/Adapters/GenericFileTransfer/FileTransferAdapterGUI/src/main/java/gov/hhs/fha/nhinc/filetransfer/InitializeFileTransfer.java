/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.filetransfer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author svalluripalli
 */
public class InitializeFileTransfer implements ServletContextListener {
    private static Log log = LogFactory.getLog(InitializeFileTransfer.class);

    public void contextInitialized(ServletContextEvent sce) {
        log.info("Servlet Context Listner - Begin");
        initializeFileTransfer();
        log.info("Servlet Context Listner - End");
    }
    private static void initializeFileTransfer()
    {
        log.info("Servlet Context initializeFileTransfer - Begin");
        try{
        CDCTimer.startTimer();
        }catch(CDCTimerException exp)
        {
            exp.printStackTrace();
        }
        log.info("Servlet Context initializeFileTransfer - End");
    }
    public void contextDestroyed(ServletContextEvent sce) {
        log.info("Servlet Context contextDestroyed - Begin");
        CDCTimer.stopTimer();
        log.info("Servlet Context contextDestroyed - End");
    }
}
