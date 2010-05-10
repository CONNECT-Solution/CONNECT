package gov.hhs.fha.nhinc.tools.ws.processor.generator;

import java.io.File;
import java.io.FileReader;
import java.util.logging.Logger;
import java.util.Properties;

/**
 * @author Neil Webb
 *
 */
public class ServicePropertyLoader
{
    private static final String NHINC_PROPERTIES_DIR = "NHINC_PROPERTIES_DIR";
	private static final String PROPERTIES_FILE_NAME = "connectCommon.properties";
	private static final String PROPERTY_KEY_WSDL_PATH = "wsdl.path";
	private static Properties serviceProps = null;
	private final static Logger logger = Logger.getLogger(ServicePropertyLoader.class.getName());
    private static String m_sPropertyFileDir = "";
    private static final String m_sFailedEnvVarMessage = "Unable to access environment variable: NHINC_PROPERTIES_DIR.";

    static
    {
        String sValue = System.getenv(NHINC_PROPERTIES_DIR);
        if ((sValue != null) && (sValue.length() > 0))
        {
            // Set it up so that we always have a "/" at the end - in case
            //------------------------------------------------------------
            if ((sValue.endsWith("/")) || (sValue.endsWith("\\")))
            {
                m_sPropertyFileDir = sValue;
            }
            else
            {
                String sFileSeparator = System.getProperty("file.separator");
                m_sPropertyFileDir = sValue + sFileSeparator;
            }
        }
        else
        {
            logger.warning(m_sFailedEnvVarMessage);
        }
    }

    private static Properties getProperties()
	{
		if(serviceProps == null)
		{
			if(m_sPropertyFileDir != null)
			{
		        FileReader frPropFile = null;

				try
				{
			        String sPropFilePathAndName = m_sPropertyFileDir + PROPERTIES_FILE_NAME;
					logger.info("Loading properties from : " + sPropFilePathAndName);
		            File fPropFile = new File(sPropFilePathAndName);
		            if (!fPropFile.exists())
		            {
		                logger.warning("Unable to load properties - " + sPropFilePathAndName + " does not exist.");
		            }
		            else
		            {
			            frPropFile = new FileReader(fPropFile);
						if(frPropFile != null)
						{
							serviceProps = new Properties();
							serviceProps.load(frPropFile);
						}
						else
						{
							logger.info("FileReader for properties file was null");
						}
		            }
				}
				catch(Throwable t)
				{
					serviceProps = null;
					logger.warning("Error loading properties file: " + t.getMessage());
				}
				finally
				{
					if(frPropFile != null)
					{
						try
						{
							frPropFile.close();
						}
						catch(Throwable toIgnore)
						{
							// eat error
						}
					}
				}
			}
			else
			{
				logger.warning("Did not attempt to load properties file because the NHINC_PROPERTIES_DIR environment variable value was not retrieved");
			}
		}
		return serviceProps;
	}

	public static String getBaseWsdlPath()
	{
		logger.info("Begin getBaseWsdlPath");
		String baseWsdlPath = null;

		Properties props = getProperties();
		if(props != null)
		{
			logger.info("Getting property using key: " + PROPERTY_KEY_WSDL_PATH);
			baseWsdlPath = props.getProperty(PROPERTY_KEY_WSDL_PATH);
		}
		else
		{
			logger.info("Not retrieving property because properties object was null");
		}

		logger.info("Returning property: " + baseWsdlPath);
		return baseWsdlPath;
	}
}
