/**
 *Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services.
 *All rights reserved.
 *
 *Redistribution and use in source and binary forms, with or without
 *modification, are permitted provided that the following conditions are met:
 *    * Redistributions of source code must retain the above
 *      copyright notice, this list of conditions and the following disclaimer.
 *    * Redistributions in binary form must reproduce the above copyright
 *      notice, this list of conditions and the following disclaimer in the documentation
 *      and/or other materials provided with the distribution.
 *    * Neither the name of the United States Government nor the
 *      names of its contributors may be used to endorse or promote products
 *      derived from this software without specific prior written permission.
 *
 *THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 *ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY
 *DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package gov.hhs.fha.nhinc.properties;

import gov.hhs.fha.nhinc.nhinclib.NullChecker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author akong
 * 
 */
public class PropertyFileRefreshHandler {

    private static Log log = LogFactory.getLog(PropertyFileRefreshHandler.class);
    private Hashtable<String, RefreshInfo> refreshInfoHashtable = new Hashtable<String, RefreshInfo>();

    PropertyFileRefreshHandler() {
        
    }
    
    public void addRefreshInfo(String propertyFile, String cacheRefreshDuration) {

        if (NullChecker.isNotNullish(cacheRefreshDuration)) {
            int refreshDurationMillisec = parseCacheRefreshDuration(cacheRefreshDuration);
            RefreshInfo newRefreshInfo = createRefreshInfo(refreshDurationMillisec);
            refreshInfoHashtable.put(propertyFile, newRefreshInfo);
        } else {
            resetRefreshTime(propertyFile);
        }

    }

    public void resetRefreshTime(String propertyFile) {
        RefreshInfo refreshInfo = refreshInfoHashtable.get(propertyFile);
        if (refreshInfo != null) {
            if (refreshInfo.m_oRefreshMode == RefreshInfo.Mode.PERIODIC) {
                Calendar oRefreshDate = Calendar.getInstance();
                oRefreshDate.add(Calendar.MILLISECOND, refreshInfo.m_iRefreshMilliseconds);
                refreshInfo.m_dtRefreshDate = oRefreshDate.getTime();
            } else if (refreshInfo.m_oRefreshMode == RefreshInfo.Mode.ALWAYS) {
                refreshInfo.m_dtRefreshDate = Calendar.getInstance().getTime();
            }
        }
    }

    public boolean hasRefreshInfo(String propertyFile) {
        if (refreshInfoHashtable.get(propertyFile) == null) {
            return false;
        }
        return true;
    }

    public boolean needsRefresh(String propertyFile) {
        boolean needsRefresh = false;
        Date dtNow = new Date();

        RefreshInfo refreshInfo = refreshInfoHashtable.get(propertyFile);
        if (refreshInfo != null) {
            if ((refreshInfo.m_oRefreshMode == RefreshInfo.Mode.ALWAYS)
                    || ((refreshInfo.m_oRefreshMode == RefreshInfo.Mode.PERIODIC))
                    && (refreshInfo.m_dtRefreshDate.before(dtNow))) {
                needsRefresh = true;
            }
        } else {
            needsRefresh = true;
        }

        return needsRefresh;
    }

    /**
     * This will return the in milliseconds the refresh duration on the property file. A setting of -1 means it never
     * refreshes.
     * 
     * @param propertyFile The name of the property file.
     * @throws PropertyAccessException This is thrown if an error occurs accessing the property.
     */
    public int getRefreshDuration(String propertyFile) {
        int refreshDuration = -1;

        RefreshInfo refreshInfo = refreshInfoHashtable.get(propertyFile);
        if (refreshInfo != null) {
            refreshDuration = refreshInfo.m_iRefreshMilliseconds;
        }

        return refreshDuration;
    }

    /**
     * This will return the duration in milliseconds before the next refresh of the properties file. A value of -1
     * indicates that no refresh will occur.
     * 
     * @param propertyFile The name of the property file.
     * @throws PropertyAccessException This is thrown if an error occurs accessing the property.
     */
    public int getDurationBeforeNextRefresh(String propertyFile) {
        int nextRefreshDuration = -1;

        RefreshInfo refreshInfo = refreshInfoHashtable.get(propertyFile);
        if (refreshInfo != null) {
            if (refreshInfo.m_oRefreshMode == RefreshInfo.Mode.ALWAYS) {
                nextRefreshDuration = 0;
            } else if (refreshInfo.m_oRefreshMode == RefreshInfo.Mode.NEVER) {
                nextRefreshDuration = -1;
            } else {
                long nowMilli = new Date().getTime();
                long refreshMilli = refreshInfo.m_dtRefreshDate.getTime();
                nextRefreshDuration = (int) (refreshMilli - nowMilli);
            }
        }

        return nextRefreshDuration;
    }

    public void printToLog(String propertyFile) {
        Log log = getLogger();
        RefreshInfo refreshInfo = refreshInfoHashtable.get(propertyFile);
        log.info("Dumping refresh information for property file: " + propertyFile);
        if (refreshInfo != null) {
            log.info("RefreshMode=" + refreshInfo.m_oRefreshMode);
            log.info("RefreshMilliseconds=" + refreshInfo.m_iRefreshMilliseconds);
            if (refreshInfo.m_dtRefreshDate != null) {
                SimpleDateFormat oFormat = new SimpleDateFormat("MM/dd/yyyy.HH:mm:ss");
                log.info("RefreshDate=" + oFormat.format(refreshInfo.m_dtRefreshDate));
            } else {
                log.info("RefreshDate=null");
            }
        } else {
            log.info("No refresh information found.");
        }
    }
    
    protected Log getLogger() {
        return log;
    }

    private int parseCacheRefreshDuration(String cacheRefreshDuration) {
        int refreshDurationMillisec = -1;
        try {
            refreshDurationMillisec = Integer.parseInt(cacheRefreshDuration.trim());
        } catch (Exception e1) {
            log.warn("Invalid CacheRefreshValue found in the property file: " + cacheRefreshDuration
                    + "'.  Treating this property file as 'refresh never'.");
        }

        return refreshDurationMillisec;
    }

    private RefreshInfo createRefreshInfo(int refreshDurationMillisec) {
        RefreshInfo newRefreshInfo = new RefreshInfo();
        if (refreshDurationMillisec <= -1) {
            newRefreshInfo.m_oRefreshMode = RefreshInfo.Mode.NEVER;
            newRefreshInfo.m_iRefreshMilliseconds = -1;
            newRefreshInfo.m_dtRefreshDate = null;
        } else if (refreshDurationMillisec == 0) {
            newRefreshInfo.m_oRefreshMode = RefreshInfo.Mode.ALWAYS;
            newRefreshInfo.m_iRefreshMilliseconds = 0;
            newRefreshInfo.m_dtRefreshDate = new Date();
        } else {
            newRefreshInfo.m_oRefreshMode = RefreshInfo.Mode.PERIODIC;
            newRefreshInfo.m_iRefreshMilliseconds = refreshDurationMillisec;
            Calendar oRefreshDate = Calendar.getInstance();
            oRefreshDate.add(Calendar.MILLISECOND, refreshDurationMillisec);
            newRefreshInfo.m_dtRefreshDate = oRefreshDate.getTime();
        }

        return newRefreshInfo;
    }

    /**
     * This class is used to hold refresh information for a properties file.
     */
    private static class RefreshInfo {
        public static enum Mode {
            NEVER, ALWAYS, PERIODIC
        };

        Mode m_oRefreshMode;
        Date m_dtRefreshDate;
        int m_iRefreshMilliseconds;
    }

}
