/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.mail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 *
 * @author jasonasmith
 */
public class ManageTaskSchedulerTest {
    
    private final ThreadPoolTaskScheduler scheduler = mock(ThreadPoolTaskScheduler.class);

    /**
     * Test of clean method, of class ManageTaskScheduler.
     */
    @Test
    public void testClean() {
        ManageTaskScheduler taskScheduler = new ManageTaskScheduler(scheduler);
        taskScheduler.clean();
        
        verify(scheduler, times(1)).shutdown();
    }
    
}
