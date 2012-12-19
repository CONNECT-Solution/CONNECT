/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services. 
 * All rights reserved. 
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met: 
 *     * Redistributions of source code must retain the above 
 *       copyright notice, this list of conditions and the following disclaimer. 
 *     * Redistributions in binary form must reproduce the above copyright 
 *       notice, this list of conditions and the following disclaimer in the documentation 
 *       and/or other materials provided with the distribution. 
 *     * Neither the name of the United States Government nor the 
 *       names of its contributors may be used to endorse or promote products 
 *       derived from this software without specific prior written permission. 
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
 * DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY 
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND 
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS 
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */
package gov.hhs.fha.nhinc.connectmgr.uddi;

import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.apache.log4j.Logger;

/**
 * This class is responsible for handling the work that is done each time the timer goes off. It will read the data from
 * the UDDI server, update the uddiConenctionInfo.xml file, and tell the connection manager to update its cache with
 * that data.
 * 
 * @author Les Westberg
 */
public class UDDITimerTask {

    private static final Logger LOG = Logger.getLogger(UDDITimerTask.class);
    private static final String GATEWAY_PROPERTY_FILE = "gateway";
    private static final String UDDI_SWITCH_PROPERTY = "UDDIRefreshActive";

    protected boolean isLogEnabled() {
        boolean isEnabled = false;
        if (LOG.isDebugEnabled()) {
            isEnabled = true;
        }
        return isEnabled;
    }

    protected void forceRefreshUDDIFile() {
        try {
            UDDIUpdateManagerHelper helper = new UDDIUpdateManagerHelper();
            helper.forceRefreshUDDIFile();
        } catch (UDDIAccessorException ex) {
            LOG.debug("****** UDDITimerTask THROWABLE: " + ex.getMessage(), ex);

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
            bUDDIActive = PropertyAccessor.getInstance().getPropertyBoolean(GATEWAY_PROPERTY_FILE, UDDI_SWITCH_PROPERTY);

            if (bUDDIActive) {
                if (isLogEnabled()) {
                    LOG.debug("Start: UDDITimerTask.run method - loading from UDDI server.");
                }

                forceRefreshUDDIFile();

                if (isLogEnabled()) {
                    LOG.debug("Done: UDDITimerTask.run method - loading from UDDI server.");
                }
            } else {
                if (isLogEnabled()) {
                    LOG.debug("UDDITimerTask is disabled by the UDDIRefreshActive property.");
                }
            }
        } catch (PropertyAccessException ex) {
        	LOG.error("UDDITimerTask.run method unable to read UDDIRefreshActive property: " + ex.getMessage());
        }
    }

    /**
     * Main method used to test this class. This one really should not be run under unit test scenarios because it
     * requires access to the UDDI server.
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
