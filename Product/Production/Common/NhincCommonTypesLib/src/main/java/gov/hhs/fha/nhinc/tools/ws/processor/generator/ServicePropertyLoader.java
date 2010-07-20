package gov.hhs.fha.nhinc.tools.ws.processor.generator;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.List;
import java.util.logging.Logger;

/**
 * Loads properties needed for web service implementation service classes
 *
 * @author Neil Webb
 */
public class ServicePropertyLoader
{
    private static final String JVM_OPTION_WSDL_DIR = "wsdl.path";
    private static final String ERROR_MSG_NO_WSDL_PATH_JVM_PROPERTY = "Unable to access the JVM property: " + JVM_OPTION_WSDL_DIR + ".";
    private static String WSDL_PATH;
	private final static Logger logger = Logger.getLogger(ServicePropertyLoader.class.getName());

    static
    {
        WSDL_PATH = System.getProperty(JVM_OPTION_WSDL_DIR);

        if(WSDL_PATH == null) {
            logger.severe(ERROR_MSG_NO_WSDL_PATH_JVM_PROPERTY);
            WSDL_PATH = "unknown";
        }
        else {
            if (WSDL_PATH.endsWith(File.separator) == false) {
                WSDL_PATH = WSDL_PATH + File.separator;
            }
        }
    }

	/**
     * Retrieve the base path where WSDL files are located. This is retrieved from the 'wsdl.path' JVM option.
     *
     * @return Path to WSDL files on the system.
     */
    public static String getBaseWsdlPath()
	{
		logger.info("getBaseWsdlPath - returning: " + WSDL_PATH);
		return WSDL_PATH;
	}
}
