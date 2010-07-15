package gov.hhs.fha.nhinc.lift.common.util.cleanup;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

/**
 *
 * @author Neil Webb
 */
@RunWith(JMock.class)
public class GatewayLiFTRecordMonitorTest
{
    Mockery context = new JUnit4Mockery()
    {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };
    final CleanupUtil mockCleanupUtil = context.mock(CleanupUtil.class);

    @Test
    public void testGetCleanupUtil()
    {
        try
        {
            GatewayLiFTRecordMonitor monitor = new GatewayLiFTRecordMonitor()
            {
                @Override
                protected CleanupUtil getCleanupUtil()
                {
                    return mockCleanupUtil;
                }
            };
            CleanupUtil cleanupUtil = monitor.getCleanupUtil();
            assertNotNull("CleanupUtil was null", cleanupUtil);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testGetCleanupUtil test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetCleanupUtil test: " + t.getMessage());
        }
    }

    @Test
    public void testRun()
    {
        try
        {
            GatewayLiFTRecordMonitor monitor = new GatewayLiFTRecordMonitor()
            {
                @Override
                protected CleanupUtil getCleanupUtil()
                {
                    return mockCleanupUtil;
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockCleanupUtil).cleanupRecords();
                }
            });
            monitor.run();
        }
        catch(Throwable t)
        {
            System.out.println("Error running testRun test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testRun test: " + t.getMessage());
        }
    }

}