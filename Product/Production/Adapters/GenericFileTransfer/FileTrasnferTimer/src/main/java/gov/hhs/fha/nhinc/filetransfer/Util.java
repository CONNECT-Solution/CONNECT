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
package gov.hhs.fha.nhinc.filetransfer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;

/**
 * 
 * @author dunnek
 */
public class Util {
    private static Log log = LogFactory.getLog(CDCTimerTask.class);
    private static final String ADAPTER_PROPERTY_FILE = "adapter";

    public static byte[] convertToByte(String value) {
        byte[] rc = null;

        try {
            rc = value.getBytes("UTF8");
        } catch (Exception ex) {
            log.error("****** CDCTimerTask THROWABLE: " + ex.getMessage(), ex);
        }

        return rc;

    }

    public static String convertToString(byte[] value) {
        String rc = "";

        try {
            rc = new String(value, 0, value.length, "UTF8");// in string
        } catch (Exception ex) {
            log.error("****** CDCTimerTask THROWABLE: " + ex.getMessage(), ex);
        }
        return rc;
    }

    public static String getExportDirectory() {
        String dir = "";
        try {
            dir = PropertyAccessor.getInstance().getProperty(ADAPTER_PROPERTY_FILE, "CDCExportDir");
        } catch (Exception ex) {

        }

        return dir;
    }
}
