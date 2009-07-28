package gov.hhs.fha.nhinc.hiem.processor.common;

/**
 *
 * @author Jon Hoppesch
 */
public class HiemProcessorConstants {
    public static final String HIEM_SERVICE_MODE_SUBSCRIPTION_PROPERTY = "hiem.subscription.servicemode";
    public static final String HIEM_SERVICE_MODE_PASSTHROUGH = "passthrough";
    public static final String HIEM_SERVICE_MODE_NOT_SUPPORTED = "notsupported";
    public static final String HIEM_SERVICE_MODE_SUPPORTED = "supported";

    public static final String PRODUCER_NHIN = "nhin";
    public static final String PRODUCER_GATEWAY = "gateway";
    public static final String PRODUCER_ADAPTER = "adapter";
    public static final String CONSUMER_NHIN = "nhin";
    public static final String CONSUMER_GATEWAY = "gateway";
    public static final String CONSUMER_ADAPTER = "adapter";
}
