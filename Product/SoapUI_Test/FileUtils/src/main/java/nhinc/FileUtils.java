/*
 * Copyright (c) 2009-2019, United States Government, as represented by the Secretary of Health and Human Services.
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
package nhinc;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Properties;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Utility class for SoapUI Connect testing. Utility methods are for file manipulation for CONNECT config files. Logs to
 * SoapUI logger.
 *
 * @author jasonasmith, mtiller
 *
 */
public class FileUtils {
    static final String TEMP_DIR = "prop_temp";
    private static final String NS_NEW_ELEMENT = "urn:uddi-org:api_v3";
    private static final String XML_ALL_NS = "*";
    private static final String COMPLETION_CHECKPOINT = "completion-checkpoint: {0}";
    private static final boolean REQUIRED_ELEMENT_TRUE = true;

    /**
     * Reads in a file with a given file name and converts to a String.
     *
     * @param fileName Name of file to be read.
     * @param log SoapUI logger.
     * @return String value of file.
     */
    public static String readFile(String fileName, Logger log) {
        try {
            log.debug(MessageFormat.format("read file: {0}", fileName));
            File sourceFile = new File(fileName);
            String filecontents = null;
            if (sourceFile.exists()) {
                log.debug("file exists");
                StringBuilder sb = new StringBuilder();

                try (BufferedReader input = new BufferedReader(new FileReader(sourceFile))) {
                    // not declared within while loop
                    String line;
                    /*
                     * readLine is a bit quirky : it returns the content of a line MINUS the newline. it returns null
                     * only for the END of the stream. it returns an empty String if two newlines appear in a row.
                     */
                    while ((line = input.readLine()) != null) {
                        sb.append(line);
                        sb.append(System.getProperty("line.separator"));
                    }
                    log.debug(getCheckpoint("readFile"));
                }
                filecontents = sb.toString();
            } else {
                log.warn(MessageFormat.format("unable to find source fileName: {0}", fileName));
            }

            return filecontents;

        } catch (IOException e) {
            log.error(e.getLocalizedMessage(), e);
            return null;
        }

    }

    /**
     * Sets the connection info files in a given config directory to have only endpoints that support the CONNECT specs
     * prior to July, 2011.
     *
     * @param sourceDirectory Directory the config files are read from.
     * @param destDirectory The config directory the files are copied to.
     * @param log SoapUI logger.
     */
    public static void setG0ConnectionInfo(String sourceDirectory, String destDirectory, Logger log) {
        copyFile(sourceDirectory, "exchangeInfo_g0.xml", destDirectory, "exchangeInfo.xml", log);
        copyFile(sourceDirectory, "internalExchangeInfo_g0.xml", destDirectory, "internalExchangeInfo.xml", log);
    }

    /**
     * Sets the connection info files in a given config directory to have only endpoints that support the CONNECT specs
     * post July, 2011.
     *
     * @param sourceDirectory Directory the config files are read from.
     * @param destDirectory The config directory the files are copied to.
     * @param log SoapUI logger.
     */
    public static void setG1ConnectionInfo(String sourceDirectory, String destDirectory, Logger log) {
        copyFile(sourceDirectory, "exchangeInfo_g1.xml", destDirectory, "exchangeInfo.xml", log);
        copyFile(sourceDirectory, "internalExchangeInfo_g1.xml", destDirectory, "internalExchangeInfo.xml", log);
    }

    /**
     * Copies given source file to the given destination file.
     *
     * @param sourceDirectory Directory of file to be copied.
     * @param sourceFileName File name of file to be copied.
     * @param destinationDirectory Directory that file is copied into.
     * @param destinationFileName File name of file copy.
     * @param log SoapUI logger.
     */
    public static void copyFile(String sourceDirectory, String sourceFileName, String destinationDirectory,
        String destinationFileName, Logger log) {

        File sourceFile = new File(sourceDirectory, sourceFileName);
        File destinationFile = new File(destinationDirectory, destinationFileName);

        log.debug(MessageFormat.format("copying property file from ''{0}'' to ''{1}''", sourceFile, destinationFile));

        if (sourceFile.exists()) {
            try {
                org.apache.commons.io.FileUtils.copyFile(sourceFile, destinationFile, false);
                log.debug(MessageFormat.format("File ''{0}'' copied to ''{1}''.", sourceFileName, destinationFileName));
            } catch (FileNotFoundException ex) {
                log.error(MessageFormat.format("{0} in the specified directory.", ex.getLocalizedMessage()), ex);
            } catch (IOException e) {
                log.error(e.getLocalizedMessage(), e);
            }
        } else {
            log.error(MessageFormat.format("Unable to find source file: {0}", sourceFile));
        }
    }

    /**
     * Deletes a given file.
     *
     * @param sourceDirectory Directory of file to be deleted.
     * @param sourceFileName File name of file to be deleted.
     * @param log SoapUI logger.
     */
    public static void deleteFile(String sourceDirectory, String sourceFileName, Logger log) {
        File file = new File(sourceDirectory, sourceFileName);
        if (file.exists()) {
            file.delete();
            log.debug(MessageFormat.format("file deleted: {0}", file.getName()));
        } else {
            log.warn(MessageFormat.format("file not found, skipping delete for: {0}", file));
        }
    }

    /**
     * Updates a property file with the given key/value pair.
     *
     * @param directory Directory of properties file.
     * @param filename File name of properties file.
     * @param propertyKey Key value for property to be updated.
     * @param propertyValue Value of property for updating.
     * @param log SoapUI logger.
     */
    public static void updateProperty(String directory, String filename, String propertyKey, String propertyValue,
        Logger log) {

        FileWriter fwPropFile = null;
        FileReader frPropFile = null;

        try {
            log.debug(MessageFormat.format(
                "begin updateProperty; directory=''{0}'';filename=''{1}'';key=''{2}'';value=''{3}'';", directory,
                filename, propertyKey, propertyValue));

            File file = new File(directory, filename);
            frPropFile = new FileReader(file);

            Properties properties = new Properties();
            properties.load(frPropFile);
            properties.setProperty(propertyKey, propertyValue);

            fwPropFile = new FileWriter(file);
            properties.store(fwPropFile, "gateway properties generated by nhinc.FileUtils for testing purposes");
        } catch (IOException e) {
            log.error(e.getLocalizedMessage(), e);
        } finally {
            closeFile(frPropFile, log);
            closeFile(fwPropFile, log);
        }
    }

    /**
     * Reads a property from the given property file with the given key.
     *
     * @param directory Directory of properties file.
     * @param filename File name of properties file.
     * @param propertyKey Key value of property to be read.
     * @param log SoapUI logger.
     * @return The string value of the read property.
     */
    public static String readProperty(String directory, String filename, String propertyKey, Logger log) {
        FileReader frPropFile = null;
        String propertyValue = null;

        try {
            log.debug(MessageFormat.format("begin ReadProperty; directory=''{0}'';filename=''{1}'';key=''{2}'';",
                directory, filename, propertyKey));

            frPropFile = new FileReader(new File(directory, filename));

            Properties properties = new Properties();
            properties.load(frPropFile);

            propertyValue = properties.getProperty(propertyKey);
        } catch (IOException e) {
            log.error(e.getLocalizedMessage(), e);
        } finally {
            closeFile(frPropFile, log);
        }

        return propertyValue;
    }

    /**
     * Updates a Spring config file in the given CONNECT config directory with the desired implementation setting.
     *
     * @param configDir Directory of spring proxy file.
     * @param fileName File name of spring proxy file.
     * @param alias Alias of the spring configuration.
     * @param oldAliasName Current name of spring alias.
     * @param newAliasName Updated name of spring alias.
     * @param log SoapUI logger.
     */
    public static void updateSpringConfig(String configDir, String fileName, String alias, String oldAliasName,
        String newAliasName, Logger log) {

        Boolean foundAlias = false;
        String oldAliasLine = MessageFormat.format("<alias alias=\"{0}\" name=\"{1}\" />", alias, oldAliasName);
        String newAliasLine = MessageFormat.format("<alias alias=\"{0}\" name=\"{1}\" />", alias, newAliasName);

        log.debug(MessageFormat.format("OLD: {0} NEW: {1}", oldAliasLine, newAliasLine));

        File configDirFile = new File(configDir);
        File tempConfigFile = new File(configDirFile.getAbsolutePath(), "tempConfigFile.xml");
        File sourceFile = new File(configDirFile.getAbsolutePath(), fileName);

        if (sourceFile.exists()) {
            try (BufferedReader input = new BufferedReader(new FileReader(sourceFile));
                FileWriter output = new FileWriter(tempConfigFile)) {

                try {
                    int lineCount = 1;
                    log.debug(MessageFormat.format("looking for: ''{0}''", oldAliasLine));
                    // not declared within while loop
                    String line;
                    /*
                     * readLine is a bit quirky : it returns the content of a line MINUS the newline. it returns null
                     * only for the END of the stream. it returns an empty String if two newlines appear in a row.
                     */
                    while ((line = input.readLine()) != null) {
                        log.debug(MessageFormat.format("File ''{0}'': ''{1}''", fileName, line));
                        if (containsIgnoreCaseOf(line, oldAliasLine)) {
                            line = newAliasLine;
                            foundAlias = true;
                            log.debug(getCheckpoint("updateSpringConfig-found"));
                        }
                        output.append(line);
                        output.append(System.getProperty("line.separator"));
                        lineCount++;
                    }
                    log.debug(MessageFormat.format("File ''{0}'': end with count: {1}", fileName, lineCount));
                } finally {
                    closeFile(input, log);
                    closeFile(output, log);
                }

                sourceFile.delete();
                copyFile(configDir, "tempConfigFile.xml", configDir, fileName, log);
                tempConfigFile.delete();

                if (!foundAlias) {
                    log.warn(MessageFormat.format(
                        "Did not find alias line in Spring Config file.  No modification made. ''{0}''", oldAliasLine));
                }
                log.debug(getCheckpoint("updateSpringConfig"));
            } catch (IOException e) {
                log.error(e.getLocalizedMessage(), e);
            }
        } else {
            log.debug(MessageFormat.format("Unable to find source file: {0}.", fileName));
        }
    }

    /**
     * Creates or edits an endpoint in the given connection info file in the given CONNECT config directory.
     *
     * @param fileName File name of config file to be updated.
     * @param directory Directory of config file to be updated.
     * @param communityId Home Community ID of endpoint update.
     * @param serviceName The name of the CONNECT service that is getting updated/added.
     * @param serviceUrl URL of service for updating/adding.
     * @param defaultVersion The default spec version.
     * @param log SoapUI logger.
     */
    public static void createOrUpdateConnection(String fileName, String directory, String communityId,
        String serviceName, String serviceUrl, String defaultVersion, Logger log) {

        log.debug(MessageFormat.format(
            "begin CreateOrUpdateConnection; directory=''{0}'';community-id=''{1}'';service-name=''{2}'';service-url=''{3}'';",
            directory, communityId, serviceName, serviceUrl));

        String fullPath = MessageFormat.format("{0}/{1}", directory, fileName);
        Document doc = getDocument(log, fullPath);
        if (null == doc) {
            return;
        }

        NodeList organizationList = getOrganizationList(log, doc);

        if (organizationList != null) {
            for (int i = 0; i < organizationList.getLength(); i++) {
                Element organization = (Element) organizationList.item(i);
                String hcidValue = getTextContentOf(organization, "hcid");

                if (equalsIgnoreCaseForHCID(communityId, hcidValue)) {
                    log.debug(MessageFormat.format("Found-HCID: {0}", hcidValue));
                    boolean serviceNodeFound = checkServiceEndpointByLatest(log, organization, serviceName, serviceUrl);

                    if (!serviceNodeFound) {
                        log.debug(MessageFormat.format("Service not found for: Adding HCID ''{0}'' and service ''{1}''",
                            communityId, serviceName));
                        try {
                            getFirstElementOf(organization, "endpointList", REQUIRED_ELEMENT_TRUE)
                            .appendChild(createElementEndpointBy(doc, serviceName, serviceUrl, defaultVersion));
                            log.debug(getCheckpoint(MessageFormat
                            .format("createOrUpdateConnection-createElementEndpointBy: {0}", serviceName)));
                        } catch (DOMException de) {
                            log.error(de.getLocalizedMessage(), de);
                        }
                    }

                    break;
                }
            }
        }

        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            // initialize StreamResult with File object to save to file
            DOMSource source = new DOMSource(doc);
            try (FileOutputStream fileOutput = new FileOutputStream(fullPath)) {
                StreamResult stream = new StreamResult(fileOutput);
                transformer.transform(source, stream);
            }
            log.debug(MessageFormat.format("Done createorupdate: {0}", fileName));
        } catch (IllegalArgumentException | TransformerException | IOException e) {
            log.error(MessageFormat.format("Exception writing out connection info file: {0}", e.getLocalizedMessage()),
                e);
        }
    }

    /**
     * Creates or edits an endpoint in the given connection info file in the given CONNECT config directory.
     *
     * @param fileName File name of config file to be updated.
     * @param directory Directory of config file to be updated.
     * @param communityId Home Community ID of endpoint update.
     * @param serviceName The name of the CONNECT service that is getting updated/added.
     * @param serviceUrl URL of service for updating/adding.
     * @param endpointVersion The default spec version.
     * @param log SoapUI logger.
     */
    public static void configureConnection(String fileName, String directory, String communityId, String serviceName,
        String serviceUrl, String endpointVersion, Logger log) {

        log.debug(MessageFormat.format(
            "begin CreateOrUpdateConnection; directory=''{0}'';community-id=''{1}'';service-name=''{2}'';service-url=''{3}'';",
            directory, communityId, serviceName, serviceUrl));

        String fullPath = MessageFormat.format("{0}/{1}", directory, fileName);
        Document doc = getDocument(log, fullPath);
        if (null == doc) {
            return;
        }

        NodeList organizationList = getOrganizationList(log, doc);
        if (organizationList != null) {
            for (int i = 0; i < organizationList.getLength(); i++) {
                Element organization = (Element) organizationList.item(i);
                String hcidValue = getFirstElementOf(organization, "hcid").getTextContent();

                if (equalsIgnoreCaseForHCID(communityId, hcidValue)) {
                    log.debug(MessageFormat.format("Found-HCID: {0}", hcidValue));
                    boolean serviceNodeFound = checkServiceEndpoint(log, organization, serviceName, serviceUrl,
                        endpointVersion);

                    if (!serviceNodeFound) {
                        log.debug(MessageFormat.format("Service not found for: Adding HCID ''{0}'' and service ''{1}''",
                            communityId, serviceName));
                        try {
                            getFirstElementOf(organization, "endpointList", REQUIRED_ELEMENT_TRUE)
                            .appendChild(createElementEndpointBy(doc, serviceName, serviceUrl, endpointVersion));
                            log.debug(getCheckpoint(
                                MessageFormat.format("configureConnection-createElementEndpointBy: {0}", serviceName)));
                        } catch (DOMException de) {
                            log.error(de.getLocalizedMessage(), de);
                        }
                    }
                    break;
                }
            }
        }
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            // initialize StreamResult with File object to save to file
            DOMSource source = new DOMSource(doc);
            try (FileOutputStream fileOutput = new FileOutputStream(fullPath)) {
                StreamResult stream = new StreamResult(fileOutput);
                transformer.transform(source, stream);
            }
            log.debug(MessageFormat.format("Done configureConnection: {0}", fileName));
        } catch (IllegalArgumentException | TransformerException | IOException e) {
            log.error(MessageFormat.format("Exception writing out connection info file: {0}", e.getLocalizedMessage()),
                e);
        }
    }

    /**
     * Copies a list of configuration values to a temporary directory.
     *
     * @param configDir Configuration directory of files to be backed up.
     * @param log SoapUI logger.
     */
    public static void backupConfiguration(String configDir, Logger log) {
        log.debug("Start backupConfiguration");
        try {
            File backupDir = new File(configDir, TEMP_DIR);
            File confDir = new File(configDir);
            backupDir.mkdir();
            // copies the files from the source directory to the destination directory
            copyFolder(confDir, backupDir, log);
            log.debug(getCheckpoint("backupConfiguration"));
        } catch (IOException ioe) {
            log.error(ioe.getLocalizedMessage(), ioe);
        }
        log.debug("End backupConfiguration");
    }

    /**
     * Restores a list of configuration values from a temporary directory. The temporary directory is deleted after the
     * restore based on the passed in optionDel boolean.
     *
     * @param configDir Configuration directory for files to be restored to.
     * @param log SoapUI logger.
     * @param optionDel Flag for deleting the temporary directory.
     */
    public static void restoreConfiguration(String configDir, Logger log, Boolean optionDel) {
        log.debug("Start restoreConfiguration");
        try {
            log.debug(MessageFormat.format("configDir: {0}", configDir));
            File backupDir = new File(configDir, TEMP_DIR);
            File confDir = new File(configDir);
            // restore the files back
            copyFolder(backupDir, confDir, log);

            // Deletes the temp folder
            deleteFolder(backupDir, log);
            // if the optionDel is true then create the temp folder
            if (!optionDel) {
                // create the temp directory again
                backupDir.mkdir();
            }
            log.debug(getCheckpoint("restoreConfiguration"));
        } catch (IOException ioe) {
            log.error(ioe.getLocalizedMessage(), ioe);
        }
        log.debug("End restoreConfiguration");
    }

    /**
     * Restores a list of configurable values from a temporary directory which is deleted on completion.
     *
     * @param configDir Configuration directory for files to be restored to.
     * @param log SoapUI logger.
     */
    public static void restoreConfiguration(String configDir, Logger log) {
        restoreConfiguration(configDir, log, true);
    }

    /**
     * Backs up a single file.
     *
     * @param configDir The directory that the file to be backed up is in.
     * @param fileName The name of the file to be backed up.
     * @param log SoapUI logger.
     */
    public static void backupFile(String configDir, String fileName, Logger log) {
        log.debug(MessageFormat.format("Backing up file {0} in {1} directory.", fileName, configDir));
        copyFile(configDir, fileName, configDir + File.separator + TEMP_DIR, fileName, log);
    }

    /**
     * Restores a single file from backup.
     *
     * @param configDir Directory that the file is restored to.
     * @param fileName The name of the file to be restored.
     * @param log SoapUI logger.
     */
    public static void restoreFile(String configDir, String fileName, Logger log) {
        log.debug(MessageFormat.format("Restoring file: {0}", fileName));
        File configDirFile = new File(configDir);
        File backupDir = new File(configDir, TEMP_DIR);
        copyFile(backupDir.getAbsolutePath(), fileName, configDirFile.getAbsolutePath(), fileName, log);

        new File(backupDir.getAbsolutePath(), fileName).delete();

        if (isEmpty(backupDir.listFiles())) {
            backupDir.delete();
        }
    }

    /**
     * Copies files and folders from the source directory to the destination directory
     *
     * @param src Source Directory
     * @param dest Destination Directory
     * @param log SoapUI logger.
     * @throws java.io.IOException
     */
    public static void copyFolder(File src, File dest, Logger log) throws IOException {
        // loop the through source folder list and copy the files/folders to the destination folder
        for (File fileName : src.listFiles()) {
            if (fileName.isDirectory()) {
                // Don't copy the backup folder
                if (!fileName.getName().equalsIgnoreCase(TEMP_DIR)) {
                    org.apache.commons.io.FileUtils.copyDirectoryToDirectory(fileName, dest);
                }
            } else {
                org.apache.commons.io.FileUtils.copyFileToDirectory(fileName, dest, false);
            }
        }
    }

    /**
     * Deletes files and folders from a directory
     *
     * @param directory Directory to delete
     * @param log SoapUI logger.
     * @throws java.io.IOException
     */
    public static void deleteFolder(File directory, Logger log) throws IOException {
        org.apache.commons.io.FileUtils.deleteDirectory(directory);
        log.debug(getCheckpoint("deleteFolder"));
    }

    public static String getCheckpoint(String namedCheckpoint) {
        return MessageFormat.format(COMPLETION_CHECKPOINT, namedCheckpoint);
    }

    private static void closeFile(Closeable file, Logger log) {
        if (file != null) {
            try {
                file.close();
            } catch (IOException ioe) {
                log.error(MessageFormat.format("Could not close property file: {0}", ioe.getLocalizedMessage()), ioe);
            }
        }
    }

    private static String appendPrefixHomeCommunityID(final String homeCommunityId) {
        return checkPrefixBeforeAppend(homeCommunityId, "urn:oid:");
    }

    private static boolean equalsIgnoreCaseForHCID(final String communityId, final String hcidValue) {
        String homeCommunityIdPrefix = appendPrefixHomeCommunityID(communityId);
        String hcidValuePrefix = appendPrefixHomeCommunityID(hcidValue);
        return homeCommunityIdPrefix.equalsIgnoreCase(hcidValuePrefix);
    }

    private static String checkPrefixBeforeAppend(final String checkValue, final String checkPrefix) {
        final String tempValue = checkValue.trim().toLowerCase();
        final String tempPrefix = checkPrefix.toLowerCase();
        if (!tempValue.startsWith(tempPrefix)) {
            return MessageFormat.format("{0}{1}", checkPrefix, checkValue);
        } else {
            return checkValue;
        }
    }

    private static Element createElementBy(Document document, String qualifiedName) {
        return document.createElementNS(NS_NEW_ELEMENT, qualifiedName);
    }

    private static Element createElementBy(Element appendTo, String qualifiedName) {
        return createElementBy(appendTo, qualifiedName, null);
    }

    private static Element createElementBy(Element appendTo, String qualifiedName, String textContent) {
        Element retElement = appendTo.getOwnerDocument().createElementNS(NS_NEW_ELEMENT, qualifiedName);
        if (textContent != null) {
            retElement.setTextContent(textContent);
        }

        appendTo.appendChild(retElement);

        return retElement;
    }

    private static Element getFirstElementOf(Element target, String nameOf) {
        return getFirstElementOf(target, nameOf, false);
    }

    private static Element getFirstElementOf(Element target, String nameOf, boolean isRequired) {
        Element firstElement = (Element) target.getElementsByTagNameNS(XML_ALL_NS, nameOf).item(0);
        if (isRequired && null == firstElement) {
            firstElement = createElementBy(target, nameOf);
        }

        return firstElement;
    }

    private static String getTextContentOf(Element target, String nameOf) {
        Element namedElement = getFirstElementOf(target, nameOf);
        if (namedElement != null) {
            return namedElement.getTextContent();
        }
        return "";
    }

    private static NodeList getElementsByTagNameOf(Logger log, Element parent, String ofList, String ofName) {
        NodeList listElements = null;
        Element rootElement = getFirstElementOf(parent, ofList);
        if (rootElement != null) {
            listElements = rootElement.getElementsByTagNameNS(XML_ALL_NS, ofName);
            if (null == listElements) {
                log.warn(MessageFormat.format("''{0}-{1}'' is null", ofList, ofName));
            }
        } else {
            log.warn(MessageFormat.format("''{0}'' is null", ofList));
        }
        return listElements;
    }

    private static Element createElementEndpointBy(Document document, String serviceName, String serviceUrl,
        String endpointVersion) {

        Element endpoint = createElementBy(document, "endpoint");

        createElementBy(endpoint, "name", serviceName);
        Element configurationList = createElementBy(endpoint, "endpointConfigurationList");

        Element configuration = createElementBy(configurationList, "endpointConfiguration");
        createElementBy(configuration, "url", serviceUrl);
        if (serviceName != null) {
            createElementBy(configuration, "version",
                containsIgnoreCaseOf(serviceName, "adapter") ? "LEVEL_a0" : endpointVersion);
        }
        return endpoint;
    }

    private static <T> boolean isEmpty(final T[] arrayCollection) {
        return !(arrayCollection != null && arrayCollection.length > 0);
    }

    private static Element getEndpointConfigurationBy(Logger log, Element endpoint, String matchEndpointVersion) {
        Element latestVersion = null;
        NodeList configurationList = getElementsByTagNameOf(log, endpoint, "endpointConfigurationList",
            "endpointConfiguration");

        if (configurationList != null) {
            log.debug(MessageFormat.format("found-endpoint-configuration: {0}", configurationList.getLength()));
            if (configurationList.getLength() > 1) {
                if (matchEndpointVersion != null) {
                    latestVersion = getEndpointConfigurationVersionBy(configurationList, matchEndpointVersion);
                } else {
                    latestVersion = getEndpointConfigurationVersionByLatest(configurationList, log);
                }
            } else {
                latestVersion = (Element) configurationList.item(0);
            }

            if (latestVersion != null) {
                log.debug(MessageFormat.format("version: {0}", getTextContentOf(latestVersion, "version")));
            }
        } else {
            log.error(
                MessageFormat.format("endpointConfigurationList is required: {0}", getTextContentOf(endpoint, "name")));
        }
        return latestVersion;
    }

    private static Element getEndpointConfigurationVersionBy(NodeList configurationList, String matchEndpointVersion) {
        Element retEndpointVersion = null;
        for (int confIndex = 0; confIndex < configurationList.getLength(); confIndex++) {
            Element epConfigurationCurrent = (Element) configurationList.item(confIndex);
            String versionCurrent = getFirstElementOf(epConfigurationCurrent, "version").getTextContent();
            if (StringUtils.equalsIgnoreCase(versionCurrent, matchEndpointVersion)) {
                retEndpointVersion = epConfigurationCurrent;
            }
        }
        return retEndpointVersion;
    }

    private static Element getEndpointConfigurationVersionByLatest(NodeList configurationList, Logger log) {
        Element latestVersion = null;
        Float valueVersion = 0.0F;
        for (int confIndex = 0; confIndex < configurationList.getLength(); confIndex++) {
            Element epConfiguration = (Element) configurationList.item(confIndex);
            String currentVersionString = getTextContentOf(epConfiguration, "version");

            if (containsIgnoreCaseOf(currentVersionString, "LEVEL")) {
                if (latestVersion != null) {
                    if (currentVersionString.equalsIgnoreCase("LEVEL_a1")) {
                        latestVersion = epConfiguration;
                    }
                } else {
                    latestVersion = epConfiguration;
                }
            } else {
                float currentVersionFloat = toFloat(currentVersionString, log);
                if (currentVersionFloat > valueVersion) {
                    valueVersion = currentVersionFloat;
                    latestVersion = epConfiguration;
                }
            }
        }
        return latestVersion;
    }

    private static boolean checkServiceEndpointByLatest(Logger log, Element organization, String serviceName,
        String serviceUrl) {
        return checkServiceEndpoint(log, organization, serviceName, serviceUrl, null);
    }

    private static boolean checkServiceEndpoint(Logger log, Element organization, String serviceName, String serviceUrl,
        String matchEndpointVersion) {
        boolean serviceNodeFound = false;
        NodeList endpointList = getElementsByTagNameOf(log, organization, "endpointList", "endpoint");

        if (endpointList != null) {
            for (int epIndex = 0; epIndex < endpointList.getLength(); epIndex++) {
                Element endpoint = (Element) endpointList.item(epIndex);
                String endpointName = getFirstElementOf(endpoint, "name").getTextContent();

                if (endpointName.equalsIgnoreCase(serviceName)) {
                    log.debug(MessageFormat.format("Found service: {0}", serviceName));
                    serviceNodeFound = true;
                    Element epConfigurationLatestVersion = getEndpointConfigurationBy(log, endpoint,
                        matchEndpointVersion);

                    if (epConfigurationLatestVersion != null) {
                        Element url = getFirstElementOf(epConfigurationLatestVersion, "url");
                        if (url != null) {
                            if (!serviceUrl.equalsIgnoreCase(url.getTextContent())) {
                                url.setTextContent(serviceUrl);
                                log.debug(
                                    MessageFormat.format("setting the endpoint-configuration-url: {0}", serviceUrl));
                            }
                        } else {
                            createElementBy(epConfigurationLatestVersion, "url", serviceUrl);
                            log.debug(MessageFormat.format("created the endpoint-configuration-url: {0}", serviceUrl));
                        }
                    }
                    log.debug(getCheckpoint(MessageFormat.format("checkServiceEndpoint-found: {0}", serviceName)));
                    break;
                }

            }
        }

        return serviceNodeFound;
    }

    private static Document getDocument(Logger log, String fullPath) {
        log.debug(MessageFormat.format("Path to connection info file: {0}", fullPath));

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);

        Document doc = null;
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            doc = builder.parse(fullPath);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            log.error(e.getLocalizedMessage(), e);
        }
        return doc;
    }

    private static NodeList getOrganizationList(Logger log, Document doc) {
        Element orgRoot = (Element) doc.getElementsByTagNameNS(XML_ALL_NS, "organizationList").item(0);
        NodeList organizationList = null;
        if (orgRoot != null) {
            organizationList = orgRoot.getElementsByTagNameNS(XML_ALL_NS, "organization");
            if (organizationList != null) {
                log.debug(MessageFormat.format("organization-list: {0}", organizationList.getLength()));
            } else {
                log.warn("organizationList-organization element is null");
            }
        } else {
            log.warn("organizationList element is null");
        }
        return organizationList;
    }

    private static Float toFloat(String numberString, Logger log) {
        try {
            return Float.parseFloat(numberString);
        } catch (NumberFormatException e) {
            log.error(e.getLocalizedMessage(), e);
        }
        return 0.0F;
    }

    private static boolean containsIgnoreCaseOf(final String stringOf, final String charSequence) {
        return stringOf.toLowerCase().contains(charSequence.toLowerCase());
    }
}
