/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services. 
 * All rights reserved. 
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met: 
 *     * Redistributions of source code must retain the above 
 *       copyright notice, this list of conditions and the following disclaimer. 
 *     * Redistributions in binary form must reproduce the above copyright 
 *       notice, this list of conditions and the following disclaimer in the documentation 
 *       and/or other materials provided with the distribution. 
 *     * Neither the name of the United States Government nor the 
 *       names of its contributors may be used to endorse or promote products 
 *       derived from this software without specific prior written permission. 
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
 * DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY 
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND 
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS 
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */
package gov.hhs.fha.nhinc.util.config.app;

import java.io.File;
import java.io.FilenameFilter;

import org.apache.log4j.Logger;

/**
 * 
 * @author Neil Webb
 */
public class ConfigurationUtilApp {
    private static final Logger LOG = Logger.getLogger(ConfigurationUtilApp.class);
    private static final String CONFIGURATION_FILE_SUFFIX = "Config.xml";
    private static final String DEFAULT_CONFIG_FILE_DIRECTORY;

    /**
     * Static initializer used to obtain the default properties file directory.
     */
    static {
        String propertyFileDirAbsolutePath = System.getProperty("nhinc.properties.dir");

        DEFAULT_CONFIG_FILE_DIRECTORY = propertyFileDirAbsolutePath;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        LOG.info("======================================");
        LOG.info("Begin ConfigurationUtilApp");
        LOG.info("======================================");
        if ((args.length < 1) || (args.length > 2) || ("-help".equalsIgnoreCase(args[0]))
                || ("-h".equalsIgnoreCase(args[0]))) {
            printUsage();
            return;
        }
        BeanImplementationType implType = parseMetaType(args[0]);
        if (implType == null) {
            printUsage();
            return;
        }
        LOG.info("Switching to: " + implType.implementationType());

        File configFileDir = getConfigFileDirectory(args);

        File[] configFiles = loadFiles(configFileDir);

        if ((configFiles == null) || (configFiles.length < 1)) {
            LOG.warn("No configuration files found in " + configFileDir);
            return;
        }
        LOG.info("File count: " + configFiles.length);

        File tempFile;
        for (int i = 0; i < configFiles.length; i++) {
            tempFile = (File) configFiles[i];
            LOG.info("* * * * Start processing " + tempFile.getName() + " * * * *");
            try {
                ConfigurationUtil switcher = new ConfigurationUtil();
                switcher.switchToType(tempFile, implType);
            } catch (Throwable t) {
                LOG.error("Error switching file (" + tempFile.getName() + ") to " + implType.implementationType()
                        + ": " + t.getMessage(), t);
            }
            LOG.info("* * * * Completed processing " + tempFile.getName() + " * * * *");
        }
        LOG.info("======================================");
        LOG.info("End ConfigurationUtilApp");
        LOG.info("======================================");
    }

    /**
     * Retrieves a File object representing the root directory of configuration files that will be processed by the
     * configuration utility application. This location may be present as the second argument provided on the command
     * line (optional) or by using the properties directory specified as a JVM argument/environment variable.
     * 
     * @param args Command line arguments. The properties file path is optionally provided as a second argument.
     * @return Configuration file directory.
     */
    private static File getConfigFileDirectory(String[] args) {
        File configDirectory = null;
        String configPath = null;
        if ((args != null) && (args.length > 1)) {
            configPath = args[1];
        } else {
            configPath = DEFAULT_CONFIG_FILE_DIRECTORY;
        }

        configDirectory = new File(configPath);
        if ((configPath == null) || (!configDirectory.exists())) {
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
    private static File[] loadFiles(File configFileDir) {
        LOG.debug("Loading configuration files from " + configFileDir.getPath());
        File[] files = null;
        if (configFileDir != null) {
            FilenameFilter filter = new FilenameFilter() {
                public boolean accept(File dir, String name) {
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
    private static BeanImplementationType parseMetaType(String arg) {
        BeanImplementationType parsed = null;
        if (arg != null) {
            try {
                parsed = BeanImplementationType.valueOf(arg.toUpperCase());
            } catch (IllegalArgumentException iae) {
                LOG.error("Error parsing unknown enum type: " + arg, iae);
            }
        }
        return parsed;
    }

    private static void printUsage() {
        LOG.info("==============================================================================");
        LOG.info("Context Switcher Application");
        LOG.info("Usage: java config.switcher.app.SwitcherApp noop|java|wssecure|wsunsecure [Directory path]");
        LOG.info("Note that only Spring configuration files that end in Config.xml will be processed.");
        LOG.info("The directory path is optional. If not provided, the value of the system environment variable ");
        LOG.info("for the properties directory will be used.");
        LOG.info("==============================================================================");
    }
}
