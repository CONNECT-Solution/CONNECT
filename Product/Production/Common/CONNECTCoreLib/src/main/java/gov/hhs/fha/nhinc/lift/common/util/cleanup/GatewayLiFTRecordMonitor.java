package gov.hhs.fha.nhinc.lift.common.util.cleanup;

/**
 * Thread used to monitor Gateway LiFT messages that are processing. It calls a cleanup utility to 
 * perform maintenance on messages that have stalled in the processing status.
 *
 * @author Neil Webb
 */
public class GatewayLiFTRecordMonitor extends Thread
{
    @Override
    public void run()
    {
        CleanupUtil cleanupUtil = getCleanupUtil();
        cleanupUtil.cleanupRecords();
    }

    /**
     * Get an instance of the cleanup utility.
     *
     * @return CleanupUtil instance
     */
    protected CleanupUtil getCleanupUtil()
    {
        return CleanupUtil.getInstance();
    }

}
