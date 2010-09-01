package gov.hhs.fha.nhinc.util.config.app;

import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import java.io.File;
import java.io.FilenameFilter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Neil Webb
 */
public class ConfigurationUtilApp
{
    private static Log log = LogFactory.getLog(ConfigurationUtilApp.class);
    private static final String CONFIGURATION_FILE_SUFFIX = "Config.xml";
    private static final String DEFAULT_CONFIG_FILE_DIRECTORY;

    /**
     * Static initializer used to obtain the default properties file directory.
     */
    static
    {
        String propertyFileDirAbsolutePath = System.getProperty("nhinc.properties.dir");

        if(propertyFileDirAbsolutePath == null)
        {
            log.warn("The runtime property nhinc.properties.dir is not set!!!  " +
                    "Looking for the environment variable NHINC_PROPERTIES_DIR as a fall back.  " +
                    "Please set the runtime nhinc.properties.dir system property in your configuration files.");
            propertyFileDirAbsolutePath = System.getenv(NhincConstants.NHINC_PROPERTIES_DIR);
        }
        DEFAULT_CONFIG_FILE_DIRECTORY = propertyFileDirAbsolutePath;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        log.info("======================================");
        log.info("Begin ConfigurationUtilApp");
        log.info("======================================");
        if((args.length < 1) || (args.length > 2) || ("-help".equalsIgnoreCase(args[0])) || ("-h".equalsIgnoreCase(args[0])))
        {
            printUsage();
            return;
        }
        BeanImplementationType implType = parseMetaType(args[0]);
        if(implType == null)
        {
            printUsage();
            return;
        }
        log.info("Switching to: " + implType.implementationType());

        File configFileDir = getConfigFileDirectory(args);

        File[] configFiles = loadFiles(configFileDir);

        if((configFiles == null) || (configFiles.length < 1))
        {
            log.warn("No configuration files found in " + configFileDir);
            return;
        }
        log.info("File count: " + configFiles.length);

        File tempFile;
        for (int i = 0; i < configFiles.length; i++)
        {
            tempFile = (File) configFiles[i];
            log.info("* * * * Start processing " + tempFile.getName() + " * * * *");
            try
            {
                ConfigurationUtil switcher = new ConfigurationUtil();
                switcher.switchToType(tempFile, implType);
            }
            catch(Throwable t)
            {
                log.error("Error switching file (" + tempFile.getName() + ") to " + implType.implementationType() + ": " + t.getMessage(), t);
            }
            log.info("* * * * Completed processing " + tempFile.getName() + " * * * *");
        }
        log.info("======================================");
        log.info("End ConfigurationUtilApp");
        log.info("======================================");
    }

    /**
     * Retrieves a File object representing the root directory of configuration files that will be processed by the
     * configuration utility application. This location may be present as the second argument provided on the command
     * line (optional) or by using the properties directory specified as a JVM argument/environment variable.
     *
     * @param args Command line arguments. The properties file path is optionally provided as a second argument.
     * @return Configuration file directory.
     */
    private static File getConfigFileDirectory(String[] args)
    {
        File configDirectory = null;
        String configPath = null;
        if((args != null) && (args.length > 1))
        {
            configPath = args[1];
        }
        else
        {
            configPath = DEFAULT_CONFIG_FILE_DIRECTORY;
        }

        configDirectory = new File(configPath);
        if((configPath == null) || (!configDirectory.exists()))
        {
            throw new ConfigurationUtilException("The directory specified by " + configPath + " does not exist.");
        }

        return configDirectory;
    }

    /**
     * Load the configuration files from the configuration file directory provided.
     *
     * @param configFileDir Properties file directory.
     * @return Configuration files found in the directory.
     */
    private static File[] loadFiles(File configFileDir)
    {
        log.debug("Loading configuration files from " + configFileDir.getPath());
        File[] files = null;
        if(configFileDir != null)
        {
            FilenameFilter filter = new FilenameFilter()
            {
                public boolean accept(File dir, String name)
                {
                    return name.endsWith(CONFIGURATION_FILE_SUFFIX);
                }
            };
            files = configFileDir.listFiles(filter);
        }
        return files;
    }

    /**
     * Parse the enum value that corresponds to the provided target parameter.
     *
     * @param arg Argument provided for switching the bean implementation type.
     * @return Parsed implementation type.
     */
    private static BeanImplementationType parseMetaType(String arg)
    {
        BeanImplementationType parsed = null;
        if(arg != null)
        {
            try
            {
                parsed = BeanImplementationType.valueOf(arg.toUpperCase());
            }
            catch(IllegalArgumentException iae)
            {
                log.error("Error parsing unknown enum type: " + arg, iae);
            }
        }
        return parsed;
    }

    private static void printUsage()
    {
        log.info("==============================================================================");
        log.info("Context Switcher Application");
        log.info("Usage: java config.switcher.app.SwitcherApp noop|java|wssecure|wsunsecure [Directory path]");
        log.info("Note that only Spring configuration files that end in Config.xml will be processed.");
        log.info("The directory path is optional. If not provided, the value of the system environment variable ");
        log.info("for the properties directory will be used.");
        log.info("==============================================================================");
    }
}
