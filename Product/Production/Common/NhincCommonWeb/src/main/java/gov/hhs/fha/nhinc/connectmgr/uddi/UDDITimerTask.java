package gov.hhs.fha.nhinc.connectmgr.uddi;

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
public class UDDITimerTask
{
    private static Log log = LogFactory.getLog(UDDITimerTask.class);

    /**
     * This method is called each time the timer thread wakes up.
     */
    public void run()
    {
        try
        {
            if (log.isDebugEnabled())
            {
                log.debug("Start: UDDITimerTask.run method - loading from UDDI server.");
            }
            
            UDDIUpdateManagerHelper.forceRefreshUDDIFile();

            if (log.isDebugEnabled())
            {
                log.debug("Done: UDDITimerTask.run method - loading from UDDI server.");
            }
        }
        catch (Throwable t)
        {
            log.debug("****** UDDITimerTask THROWABLE: " + t.getMessage(), t);
            
            StringWriter stackTrace = new StringWriter();
            t.printStackTrace(new PrintWriter(stackTrace));
            String sValue = stackTrace.toString();
            if (sValue.indexOf("EJBClassLoader") >= 0)
            {
                UDDITimer.stopTimer();
            }
        }
    }
    
    /**
     * Main method used to test this class.   This one really should not be run under unit
     * test scenarios because it requires access to the UDDI server.
     * 
     * @param args
     */
    public static void main(String[] args)
    {
        System.out.println("Starting test.");
        
        try
        {
            UDDITimerTask oTimerTask = new UDDITimerTask();
            oTimerTask.run();
        }
        catch (Exception e)
        {
            System.out.println("An unexpected exception occurred: " + e.getMessage());
            e.printStackTrace();
            System.exit(-1);
        }

        System.out.println("End of test.");
        
    }
}
