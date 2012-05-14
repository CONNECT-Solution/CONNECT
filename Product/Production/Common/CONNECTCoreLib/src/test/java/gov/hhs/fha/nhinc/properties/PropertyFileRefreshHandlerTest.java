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

import java.io.File;

import org.apache.commons.logging.Log;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author akong
 *
 */
public class PropertyFileRefreshHandlerTest {

    private static final String PROPERTY_FILE_NAME = "mock";
    private static final int REFRESH_DURATION = 200;
    
    protected Mockery context = new JUnit4Mockery() {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };
    final Log mockLog = context.mock(Log.class);
    
    @Test
    public void testAddRefreshInfo_Periodic() {
        PropertyFileRefreshHandler refreshHandler = createPropertyFileRefreshHandler();
        
        refreshHandler.addRefreshInfo(PROPERTY_FILE_NAME, String.valueOf(REFRESH_DURATION));
        
        int refreshDuration = refreshHandler.getRefreshDuration(PROPERTY_FILE_NAME);
        assertEquals(REFRESH_DURATION, refreshDuration);
    }
    
    @Test
    public void testAddRefreshInfo_Always() throws InterruptedException {
        PropertyFileRefreshHandler refreshHandler = createPropertyFileRefreshHandler();
        
        refreshHandler.addRefreshInfo(PROPERTY_FILE_NAME, "0");       
        int duration = refreshHandler.getDurationBeforeNextRefresh(PROPERTY_FILE_NAME);
        assertEquals(0, duration);
    }
    
    @Test
    public void testAddRefreshInfo_nullRefreshDuration() {
        PropertyFileRefreshHandler refreshHandler = createPropertyFileRefreshHandler();
        refreshHandler.addRefreshInfo(PROPERTY_FILE_NAME, null);
        
        int refreshDuration = refreshHandler.getRefreshDuration(PROPERTY_FILE_NAME);
        assertEquals(-1, refreshDuration);
    }
    
    @Test
    public void testAddRefreshInfo_badRefreshDuration() {
        PropertyFileRefreshHandler refreshHandler = createPropertyFileRefreshHandler();
        refreshHandler.addRefreshInfo(PROPERTY_FILE_NAME, "abc");
        
        int refreshDuration = refreshHandler.getRefreshDuration(PROPERTY_FILE_NAME);
        assertEquals(-1, refreshDuration);
    }
    
    @Test
    public void testResetRefreshTime_Periodic_GetDuration() throws InterruptedException {
        PropertyFileRefreshHandler refreshHandler = createPropertyFileRefreshHandler();        
        refreshHandler.addRefreshInfo(PROPERTY_FILE_NAME, String.valueOf(REFRESH_DURATION));
        
        int initialDuration = refreshHandler.getDurationBeforeNextRefresh(PROPERTY_FILE_NAME);
        Thread.sleep(100);
        int afterSleepDuration = refreshHandler.getDurationBeforeNextRefresh(PROPERTY_FILE_NAME);
        assertTrue(afterSleepDuration < initialDuration);
        
        refreshHandler.resetRefreshTime(PROPERTY_FILE_NAME);
        int resetDuration = refreshHandler.getDurationBeforeNextRefresh(PROPERTY_FILE_NAME);
        assertTrue(resetDuration >= afterSleepDuration);
    }
    
    @Test
    public void testResetRefreshTime_Periodic_NeedsRefresh() throws InterruptedException {
        PropertyFileRefreshHandler refreshHandler = createPropertyFileRefreshHandler();       
        refreshHandler.addRefreshInfo(PROPERTY_FILE_NAME, String.valueOf(REFRESH_DURATION));
               
        Thread.sleep(REFRESH_DURATION + 100);
        assertTrue(refreshHandler.needsRefresh(PROPERTY_FILE_NAME));
        refreshHandler.resetRefreshTime(PROPERTY_FILE_NAME);
        assertFalse(refreshHandler.needsRefresh(PROPERTY_FILE_NAME));
    }
    
    @Test
    public void testResetRefreshTime_Always() throws InterruptedException {
        PropertyFileRefreshHandler refreshHandler = createPropertyFileRefreshHandler();        
        refreshHandler.addRefreshInfo(PROPERTY_FILE_NAME, "0");       
        
        assertTrue(refreshHandler.needsRefresh(PROPERTY_FILE_NAME));
        refreshHandler.resetRefreshTime(PROPERTY_FILE_NAME);
        assertTrue(refreshHandler.needsRefresh(PROPERTY_FILE_NAME));
        
        int duration = refreshHandler.getDurationBeforeNextRefresh(PROPERTY_FILE_NAME);
        assertEquals(0, duration);        
    }
    
    @Test
    public void testResetRefreshTime_Never() throws InterruptedException {
        PropertyFileRefreshHandler refreshHandler = createPropertyFileRefreshHandler();        
        refreshHandler.addRefreshInfo(PROPERTY_FILE_NAME, "-1");
        
        assertFalse(refreshHandler.needsRefresh(PROPERTY_FILE_NAME));
        refreshHandler.resetRefreshTime(PROPERTY_FILE_NAME);
        assertFalse(refreshHandler.needsRefresh(PROPERTY_FILE_NAME));
        
        int duration = refreshHandler.getDurationBeforeNextRefresh(PROPERTY_FILE_NAME);
        assertEquals(-1, duration);    
        
    }
    
    @Test
    public void testPrintToLog() {
        PropertyFileRefreshHandler refreshHandler = createPropertyFileRefreshHandler();
        context.checking(new Expectations() {
            {
                atLeast(1).of(mockLog).info(with(any(String.class)));
            }
        });        
        refreshHandler.printToLog(PROPERTY_FILE_NAME);
        
        refreshHandler.addRefreshInfo(PROPERTY_FILE_NAME, String.valueOf(REFRESH_DURATION));
        refreshHandler.printToLog(PROPERTY_FILE_NAME);
    }
    
    private PropertyFileRefreshHandler createPropertyFileRefreshHandler() {
        return new PropertyFileRefreshHandler() {
            protected Log getLogger() {
                return mockLog;
            }
        };
    }
    
}
