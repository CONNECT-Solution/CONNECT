/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.connectmgr.uddi;

import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class is responsible for handling the work that is done each time the timer
 * goes off.  It will read the data from the UDDI server, update the 
 * uddiConenctionInfo.xml file, and tell the connection manager to update its cache
 * with that data.
 * 
 * @author Les Westberg
 */
public class UDDITimerTask {

    private static Log log = null;
    private static final String GATEWAY_PROPERTY_FILE = "gateway";
    private static final String UDDI_SWITCH_PROPERTY = "UDDIRefreshActive";

    public UDDITimerTask() {
        log = createLogger();
    }

    protected Log createLogger() {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }

    protected boolean isLogEnabled() {
        boolean isEnabled = false;
        if (log.isDebugEnabled()) {
            isEnabled = true;
        }
        return isEnabled;
    }

    protected void forceRefreshUDDIFile() {
        try {
            UDDIUpdateManagerHelper helper = new UDDIUpdateManagerHelper();
            helper.forceRefreshUDDIFile();
        } catch (UDDIAccessorException ex) {
            log.debug("****** UDDITimerTask THROWABLE: " + ex.getMessage(), ex);

            StringWriter stackTrace = new StringWriter();
            ex.printStackTrace(new PrintWriter(stackTrace));
            String sValue = stackTrace.toString();
            if (sValue.indexOf("EJBClassLoader") >= 0) {
                UDDITimer.stopTimer();
            }
        }
    }

    /**
     * This method is called each time the timer thread wakes up.
     */
    public void run() {
        boolean bUDDIActive = true;
        try {
            bUDDIActive = PropertyAccessor.getPropertyBoolean(GATEWAY_PROPERTY_FILE, UDDI_SWITCH_PROPERTY);

            if (bUDDIActive) {
                if (isLogEnabled()) {
                    log.debug("Start: UDDITimerTask.run method - loading from UDDI server.");
                }

                forceRefreshUDDIFile();

                if (isLogEnabled()) {
                    log.debug("Done: UDDITimerTask.run method - loading from UDDI server.");
                }
            } else {
                if (isLogEnabled()) {
                    log.debug("UDDITimerTask is disabled by the UDDIRefreshActive property.");
                }
            }
        } catch (PropertyAccessException ex) {
            if (isLogEnabled()) {
                log.debug("UDDITimerTask.run method unable to read UDDIRefreshActive property: " + ex.getMessage());
            }
        }
    }

    /**
     * Main method used to test this class.   This one really should not be run under unit
     * test scenarios because it requires access to the UDDI server.
     * 
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("Starting test.");

        try {
            UDDITimerTask oTimerTask = new UDDITimerTask();
            oTimerTask.run();
        } catch (Exception e) {
            System.out.println("An unexpected exception occurred: " + e.getMessage());
            e.printStackTrace();
            System.exit(-1);
        }

        System.out.println("End of test.");

    }
}
