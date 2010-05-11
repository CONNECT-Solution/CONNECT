package gov.hhs.fha.nhinc.tools.ws.processor.generator;

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
    private static final String JVM_OPTION_WSDL_DIR = "-Dwsdl.path";
    private static final String ERROR_MSG_NO_WSDL_PATH_JVM_OPTION = "Unable to access the JVM option: " + JVM_OPTION_WSDL_DIR + ".";
    private static final String WSDL_PATH;
	private final static Logger logger = Logger.getLogger(ServicePropertyLoader.class.getName());

    static
    {
        String sValue = null;
        try
        {
            RuntimeMXBean runtimeMxBean = ManagementFactory.getRuntimeMXBean();
            if(runtimeMxBean != null)
            {
                List<String> inputArgs = runtimeMxBean.getInputArguments();
                if(inputArgs != null)
                {
                    for(String inputArg : inputArgs)
                    {
                        logger.info("Input argument: " + inputArg);
                        if((inputArg != null) && (inputArg.startsWith(JVM_OPTION_WSDL_DIR)))
                        {
                            int eqIndex = inputArg.indexOf("=");
                            if(eqIndex != -1)
                            {
                                sValue = inputArg.substring(eqIndex + 1);
                            }
                            else
                            {
                                logger.info("JVM option did not contain '=' to assign a value");
                            }
                            break;
                        }
                    }
                }
                else
                {
                    logger.warning("Input arguments list from RuntimeMXBean was null");
                }
            }
            else
            {
                logger.warning("RuntimeMXBean was null.");
            }
        }
        catch(Throwable t)
        {
            logger.warning("Error encountered collecting the '" + JVM_OPTION_WSDL_DIR + "' JVM option: " + t.getMessage());
            t.printStackTrace();
        }
        if ((sValue == null) || (sValue.length() < 1))
        {
            logger.warning(ERROR_MSG_NO_WSDL_PATH_JVM_OPTION);
        }
        WSDL_PATH = sValue;
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
