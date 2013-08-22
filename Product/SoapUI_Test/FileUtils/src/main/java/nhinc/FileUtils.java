package nhinc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.lang.Exception;

import java.io.FileOutputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import javax.xml.transform.OutputKeys;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * Utility class for SoapUI Connect testing.  Utility methods are for file
 * manipulation for CONNECT config files.  Logs to SoapUI logger.
 * @author jasonasmith, mtiller
 *
 */
public class FileUtils {
	
	static final String TEMP_DIR = "prop_temp";

	/**
	 * Reads in a file with a given file name and converts to a String.
	 * @param fileName Name of file to be read.
	 * @param log SoapUI logger.
	 * @return String value of file.
	 */
	public static String readFile(String fileName, Logger log) {
		try {

			log.info("read file: " + fileName);
			File sourceFile = new File(fileName);
			String filecontents = null;
			if (sourceFile.exists()) {
				log.info("file exists");
				StringBuilder sb = new StringBuilder();

				BufferedReader input = new BufferedReader(new FileReader(
						sourceFile));
				try {
					String line = null; // not declared within while loop
					/*
					 * readLine is a bit quirky : it returns the content of a
					 * line MINUS the newline. it returns null only for the END
					 * of the stream. it returns an empty String if two newlines
					 * appear in a row.
					 */
					while ((line = input.readLine()) != null) {
						sb.append(line);
						sb.append(System.getProperty("line.separator"));
					}
				} finally {
					input.close();
				}
				filecontents = sb.toString();
			} else {
				log.info("unable to find source file " + fileName);
			}

			log.info("read file contents (" + filecontents.length() + ")");
			return filecontents;

		} catch (IOException e) {
			log.error(e.getMessage());
			return null;
		}

	}

	/**
	 * Sets the connection info files in a given config directory to have
	 * only endpoints that support the CONNECT specs prior to July, 2011.
	 * @param sourceDirectory Directory the config  files are read from.
	 * @param destDirectory The config directory the files are copied to.
	 * @param log SoapUI logger.
	 */
	public static void setG0ConnectionInfo(String sourceDirectory,
			String destDirectory, Logger log) {
		copyFile(sourceDirectory, "uddiConnectionInfo_g0.xml", destDirectory,
				"uddiConnectionInfo.xml", log);
		copyFile(sourceDirectory, "internalConnectionInfo_g0.xml",
				destDirectory, "internalConnectionInfo.xml", log);
	}

	/**
	 * Sets the connection info files in a given config directory to have
	 * only endpoints that support the CONNECT specs post July, 2011.
	 * @param sourceDirectory Directory the config files are read from.
	 * @param destDirectory The config directory the files are copied to.
	 * @param log SoapUI logger.
	 */
	public static void setG1ConnectionInfo(String sourceDirectory,
			String destDirectory, Logger log) {
		copyFile(sourceDirectory, "uddiConnectionInfo_g1.xml", destDirectory,
				"uddiConnectionInfo.xml", log);
		copyFile(sourceDirectory, "internalConnectionInfo_g1.xml",
				destDirectory, "internalConnectionInfo.xml", log);
	}

	/**
	 * Copies given source file to the given destination file.
	 * @param sourceDirectory Directory of file to be copied.
	 * @param sourceFileName File name of file to be copied.
	 * @param destinationDirectory Directory that file is copied into.
	 * @param destinationFileName File name of file copy.
	 * @param log SoapUI logger.
	 */
	public static void copyFile(String sourceDirectory, String sourceFileName,
			String destinationDirectory, String destinationFileName, Logger log) {

		File sourceFile = new File(sourceDirectory, sourceFileName);
		File destinationFile = new File(destinationDirectory,
				destinationFileName);

		log.info("copying property file from " + sourceFile + " to "
				+ destinationFile);

		if (sourceFile.exists()) {

			try {

				InputStream in = new FileInputStream(sourceFile);
				OutputStream out = new FileOutputStream(destinationFile);

				byte[] buf = new byte[1024];
				int len;

				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}

				in.close();
				out.close();
				log.info("File " + sourceFileName + " copied to "
						+ destinationFileName + ".");

			} catch (FileNotFoundException ex) {
				log.error(ex.getMessage() + " in the specified directory.");
			} catch (IOException e) {
				log.error(e.getMessage());
			}
		}

		else {
			log.error("Unable to find source file " + sourceFile);
		}
	}

	/**
	 * Deletes a given file.
	 * @param sourceDirectory Directory of file to be deleted.
	 * @param sourceFileName File name of file to be deleted.
	 * @param log SoapUI logger.
	 */
	public static void deleteFile(String sourceDirectory,
			String sourceFileName, Logger log) {

		File file = new File(sourceDirectory, sourceFileName);
		if (file.exists()) {
			log.info("deleting file " + file);
			file.delete();
		} else {
			log.info("file not found, skipping delete for " + file);
		}
	}

	/**
	 * Updates a property file with the given key/value pair.
	 * @param directory Directory of properties file.
	 * @param filename File name of properties file.
	 * @param propertyKey Key value for property to be updated.
	 * @param propertyValue Value of property for updating.
	 * @param log SoapUI logger.
	 */
	public static void updateProperty(String directory, String filename,
			String propertyKey, String propertyValue, Logger log) {
		FileWriter fwPropFile = null;
		FileReader frPropFile = null;

		try {
			log.info("begin updateProperty; directory='" + directory
					+ "';filename='" + filename + "';key='" + propertyKey
					+ "';value='" + propertyValue + "';");

			File file = new File(directory, filename);
			Properties properties = new Properties();

			frPropFile = new FileReader(file);
			properties.load(frPropFile);

			properties.setProperty(propertyKey, propertyValue);
			fwPropFile = new FileWriter(file);
			properties
					.store(fwPropFile,
							"gateway properties generated by nhinc.FileUtils for testing purposes");
			properties = null;
		} catch (IOException e) {
			log.error(e.getMessage());
		} finally {

			if (frPropFile != null) {
				try {
					frPropFile.close();
				} catch (Exception e) {
				}
			}

			if (fwPropFile != null) {
				try {
					fwPropFile.close();
				} catch (Exception e) {
				}
			}
		}
	}

	/**
	 * Reads a property from the given property file with the given key.
	 * @param directory Directory of properties file.
	 * @param filename File name of properties file.
	 * @param propertyKey Key value of property to be read.
	 * @param log SoapUI logger.
	 * @return The string value of the read property.
	 */
	public static String readProperty(String directory, String filename,
			String propertyKey, Logger log) {
		try {

			log.info("begin ReadProperty; directory='" + directory
					+ "';filename='" + filename + "';key='" + propertyKey
					+ "';");
			File file = new File(directory, filename);
			Properties properties = new Properties();
			FileReader frPropFile;

			frPropFile = new FileReader(file);

			properties = new Properties();
			properties.load(frPropFile);
			String propertyValue = properties.getProperty(propertyKey);
			properties = null;
			return propertyValue;

		} catch (FileNotFoundException e) {
			log.error(e.getMessage());
			return null;
		} catch (IOException e) {
			log.error(e.getMessage());
			return null;
		}
	}
	
	/**
	 *
	 * Updates a Spring config file in the given CONNECT config directory
	 * with the desired implementation setting.
	 * @param configDir Directory of spring proxy file.
	 * @param fileName File name of spring proxy file.
	 * @param alias Alias of the spring configuration.
	 * @param oldAliasName Current name of spring alias.
	 * @param newAliasName Updated name of spring alias.
	 * @param log SoapUI logger.
	 */
	public static void updateSpringConfig(String configDir, String fileName,
			String alias, String oldAliasName, String newAliasName, Logger log){
		
		Boolean foundAlias = false;
		String oldAliasLine = "<alias alias=\"" + alias + "\" name=\"" + oldAliasName +
				"\" />";
		String newAliasLine = "<alias alias=\"" + alias + "\" name=\"" + newAliasName +
				"\" />";
		
		log.info("OLD: " + oldAliasLine + "NEW: " + newAliasLine);
		
		File configDirFile = new File(configDir);
		File tempConfigFile = new File(configDirFile.getAbsolutePath(),
				"tempConfigFile.xml");
		File sourceFile = new File(configDirFile.getAbsolutePath(),
				fileName);
		
		if (sourceFile.exists()) {
			try {		
				BufferedReader input =  new BufferedReader(new FileReader(
						sourceFile));
				FileWriter output = new FileWriter(tempConfigFile);
			
				try {
					 
					String line = null; // not declared within while loop
					/*
					 * readLine is a bit quirky : it returns the content of a
					 * line MINUS the newline. it returns null only for the END
					 * of the stream. it returns an empty String if two newlines
					 * appear in a row.
					 */
					while ((line = input.readLine()) != null) {
						if(line.contains(oldAliasLine)){
							line = newAliasLine;
							foundAlias = true;
						}
						output.append(line);
						output.append(System.getProperty("line.separator"));
					}
					
				} finally {
					input.close();
					output.close();
				}				
				
				sourceFile.delete();
				copyFile(configDir, "tempConfigFile.xml", configDir, fileName, log);
				tempConfigFile.delete();
				
				if(!foundAlias){
					log.error("Did not find alias line in Spring Config file." +
							"  No modification made.");
				}
				
			} catch (IOException e) {
				log.error(e.getMessage());
			} 
		}else {
			log.info("unable to find source file " + fileName);
		}	
	}

	/**
	 * Creates or edits an endpoint in the given connection info file in the 
	 * given CONNECT config directory.
	 * @param fileName File name of config file to be updated.
	 * @param directory Directory of config file to be updated.
	 * @param communityId Home Community ID of endpoint update.
	 * @param serviceName The name of the CONNECT service that is getting
	 * updated/added.
	 * @param serviceUrl URL of service for updating/adding.
	 * @param defaultVersion The default spec version.
	 * @param log SoapUI logger.
	 */
	public static void createOrUpdateConnection(String fileName,
			String directory, String communityId, String serviceName,
			String serviceUrl, String defaultVersion, Logger log) {

		log.info("begin CreateOrUpdateConnection; directory='" + directory
				+ "';community id='" + communityId + "';service name='"
				+ serviceName + "';service url='" + serviceUrl + "';");

		String fullPath = directory + "/" + fileName;
		log.info("Path to connection info file: " + fullPath);
		boolean serviceNodeFound = false;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		Document doc = null;
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			doc = builder.parse(fullPath);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		Element businessDetail = (Element) doc.getElementsByTagName(
				"businessDetail").item(0);
		NodeList businessEntities = businessDetail
				.getElementsByTagName("businessEntity");

		for (int i = 0; i < businessEntities.getLength(); i++) {
			Element businessEntity = (Element) businessEntities.item(i);
			String communityIdValue = businessEntity
					.getAttribute("businessKey");
			if (communityIdValue.equals("uddi:nhincnode:" + communityId)) {
				Element services = (Element) businessEntity
						.getElementsByTagName("businessServices").item(0);
				NodeList serviceList = services
						.getElementsByTagName("businessService");
				serviceNodeFound = false;

				for (int serviceNodeIndex = 0; serviceNodeIndex < serviceList
						.getLength(); serviceNodeIndex++) {
					Element serviceElement = (Element) serviceList
							.item(serviceNodeIndex);
					String name = serviceElement.getElementsByTagName("name")
							.item(0).getTextContent();
					if (serviceName.equals(name)) {
						log.info("Found service: " + serviceName);
						serviceNodeFound = true;
						Element bindingTemplates = (Element) serviceElement
								.getElementsByTagName("bindingTemplates").item(
										0);
						NodeList bindingTemplatesList = bindingTemplates
								.getElementsByTagName("bindingTemplate");
						float bindingTemplateVersion = 0;
						Element latestVersionBindingTemplate = null;

						if (bindingTemplatesList.getLength() > 1) {
							for (int bindingNodeIndex = 0; bindingNodeIndex < bindingTemplatesList
									.getLength(); bindingNodeIndex++) {
								Element currBindingTemplate = (Element) bindingTemplatesList
										.item(bindingNodeIndex);
								Element bindingCategoryBag = (Element) currBindingTemplate
										.getElementsByTagName("categoryBag")
										.item(0);
								Element bindingKeyedRef = (Element) bindingCategoryBag
										.getElementsByTagName("keyedReference")
										.item(0);
								String currVersionString = bindingKeyedRef
										.getAttribute("keyValue");
								if (currVersionString.contains("LEVEL")) {
									if (latestVersionBindingTemplate != null) {
										if (currVersionString
												.equals("LEVEL_a1")) {
											latestVersionBindingTemplate = currBindingTemplate;
										}// else template is prior version and
											// doesn't need to be set
									} else
										latestVersionBindingTemplate = currBindingTemplate;
								} else {
									float currVersion = new Float(
											currVersionString);
									if (currVersion > bindingTemplateVersion) {
										bindingTemplateVersion = currVersion;
										latestVersionBindingTemplate = currBindingTemplate;
									}
								}
							}
						} else {
							latestVersionBindingTemplate = (Element) bindingTemplatesList
									.item(0);
						}

						if (latestVersionBindingTemplate != null) {
							Element accessPoint = (Element) latestVersionBindingTemplate
									.getElementsByTagName("accessPoint")
									.item(0);
							if (!serviceUrl
									.equals(accessPoint.getTextContent())) {
								accessPoint.setTextContent(serviceUrl);
								log.info("AccessPoint Found, set to "
										+ serviceUrl);
							}
						}
						break;
					}

				}
				if (!serviceNodeFound) {
					log.info("Service not found for: " + communityId
							+ ".  Adding HCID and service");
					// Create new service and add it to the services node
					try {
						Element serviceElement = doc.createElementNS(
								"urn:uddi-org:api_v3", "businessService");
						serviceElement.setAttribute("serviceKey",
								"uddi:nhincnode:" + serviceName);
						serviceElement.setAttribute("businessKey",
								"uddi:nhincnode:" + communityId);

						Element name = doc.createElementNS(
								"urn:uddi-org:api_v3", "name");
						name.setAttribute("xml:lang", "en");
						name.setTextContent(serviceName);
						serviceElement.appendChild(name);

						Element bindingTemplates = doc.createElementNS(
								"urn:uddi-org:api_v3", "bindingTemplates");
						Element bindingTemplate = doc.createElementNS(
								"urn:uddi-org:api_v3", "bindingTemplate");
						bindingTemplate.setAttribute("bindingKey",
								"uddi:nhincnode:" + serviceName);
						bindingTemplate.setAttribute("serviceKey",
								"uddi:nhincnode:" + serviceName);
						Element accessPoint = doc.createElementNS(
								"urn:uddi-org:api_v3", "accessPoint");
						accessPoint.setAttribute("useType", "endPoint");
						accessPoint.setTextContent(serviceUrl);
						Element btCategoryBags = doc.createElementNS(
								"urn:uddi-org:api_v3", "categoryBag");

						if (serviceName.toLowerCase().contains("adapter")) {
							Element keyedRefAdap = doc.createElementNS(
									"urn:uddi-org:api_v3", "keyedReference");
							keyedRefAdap.setAttribute("tModelKey",
									"CONNECT:adapter:apilevel");
							keyedRefAdap.setAttribute("keyName", "");
							keyedRefAdap.setAttribute("keyValue", "LEVEL_a0");
							btCategoryBags.appendChild(keyedRefAdap);
						} else {
							Element btKeyedReference = doc.createElementNS(
									"urn:uddi-org:api_v3", "keyedReference");
							btKeyedReference.setAttribute("keyName", "");
							btKeyedReference.setAttribute("tModelKey",
									"uddi:nhin:versionofservice");
							btKeyedReference.setAttribute("keyValue",
									defaultVersion);
							btCategoryBags.appendChild(btKeyedReference);
						}
						bindingTemplate.appendChild(accessPoint);
						bindingTemplate.appendChild(btCategoryBags);
						bindingTemplates.appendChild(bindingTemplate);

						Element categoryBag = doc.createElementNS(
								"urn:uddi-org:api_v3", "categoryBag");
						Element keyedReference = doc.createElementNS(
								"urn:uddi-org:api_v3", "keyedReference");
						keyedReference.setAttribute("tModelKey",
								"uddi:nhin:standard-servicenames");
						keyedReference.setAttribute("keyName", serviceName);
						keyedReference.setAttribute("keyValue", serviceName);
						categoryBag.appendChild(keyedReference);

						serviceElement.appendChild(bindingTemplates);
						serviceElement.appendChild(categoryBag);

						services.appendChild(serviceElement);
					} catch (Exception ex) {
						log.error(ex.getMessage());
					}
				}
				break;
			}
		}
		try {
			Transformer transformer = TransformerFactory.newInstance()
					.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");

			// initialize StreamResult with File object to save to file
			DOMSource source = new DOMSource(doc);
			FileOutputStream fileOutput = new FileOutputStream(fullPath);
			StreamResult stream = new StreamResult(fileOutput);
			transformer.transform(source, stream);
			// fileOutput.finalize();
			fileOutput.close();
			log.info("Done createorupdate: " + fileName);
		} catch (Exception e) {
			log.error(
					"Exception writing out connection info file: "
							+ e.getMessage(), e);
		}
	}
	
	   /**
     * Creates or edits an endpoint in the given connection info file in the 
     * given CONNECT config directory.
     * @param fileName File name of config file to be updated.
     * @param directory Directory of config file to be updated.
     * @param communityId Home Community ID of endpoint update.
     * @param serviceName The name of the CONNECT service that is getting
     * updated/added.
     * @param serviceUrl URL of service for updating/adding.
     * @param endpointVersion The default spec version.
     * @param log SoapUI logger.
     */
    public static void configureConnection(String fileName,
            String directory, String communityId, String serviceName,
            String serviceUrl, String endpointVersion, Logger log) {

        log.info("begin CreateOrUpdateConnection; directory='" + directory
                + "';community id='" + communityId + "';service name='"
                + serviceName + "';service url='" + serviceUrl + "';");

        String fullPath = directory + "/" + fileName;
        log.info("Path to connection info file: " + fullPath);
        boolean serviceNodeFound = false;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        Document doc = null;
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            doc = builder.parse(fullPath);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        Element businessDetail = (Element) doc.getElementsByTagName(
                "businessDetail").item(0);
        NodeList businessEntities = businessDetail
                .getElementsByTagName("businessEntity");

        for (int i = 0; i < businessEntities.getLength(); i++) {
            Element businessEntity = (Element) businessEntities.item(i);
            String communityIdValue = businessEntity
                    .getAttribute("businessKey");
            if (communityIdValue.equals("uddi:nhincnode:" + communityId)) {
                Element services = (Element) businessEntity
                        .getElementsByTagName("businessServices").item(0);
                NodeList serviceList = services
                        .getElementsByTagName("businessService");
                serviceNodeFound = false;

                for (int serviceNodeIndex = 0; serviceNodeIndex < serviceList
                        .getLength(); serviceNodeIndex++) {
                    Element serviceElement = (Element) serviceList
                            .item(serviceNodeIndex);
                    String name = serviceElement.getElementsByTagName("name")
                            .item(0).getTextContent();
                    if (serviceName.equals(name)) {
                        log.info("Found service: " + serviceName);
                        serviceNodeFound = true;
                        Element bindingTemplates = (Element) serviceElement
                                .getElementsByTagName("bindingTemplates").item(
                                        0);
                        NodeList bindingTemplatesList = bindingTemplates
                                .getElementsByTagName("bindingTemplate");
                        Element latestVersionBindingTemplate = null;

                        if (bindingTemplatesList.getLength() > 1) {
                            for (int bindingNodeIndex = 0; bindingNodeIndex < bindingTemplatesList
                                    .getLength(); bindingNodeIndex++) {
                                Element currBindingTemplate = (Element) bindingTemplatesList
                                        .item(bindingNodeIndex);
                                Element bindingCategoryBag = (Element) currBindingTemplate
                                        .getElementsByTagName("categoryBag")
                                        .item(0);
                                Element bindingKeyedRef = (Element) bindingCategoryBag
                                        .getElementsByTagName("keyedReference")
                                        .item(0);
                                String currVersionString = bindingKeyedRef
                                        .getAttribute("keyValue");
                                if (StringUtils.equalsIgnoreCase(currVersionString, endpointVersion)) {
                                    latestVersionBindingTemplate = currBindingTemplate;
                                }
                            }
                        } else {
                            latestVersionBindingTemplate = (Element) bindingTemplatesList
                                    .item(0);
                        }

                        if (latestVersionBindingTemplate != null) {
                            Element accessPoint = (Element) latestVersionBindingTemplate
                                    .getElementsByTagName("accessPoint")
                                    .item(0);
                            if (!serviceUrl
                                    .equals(accessPoint.getTextContent())) {
                                accessPoint.setTextContent(serviceUrl);
                                log.info("AccessPoint Found, set to "
                                        + serviceUrl);
                            }
                        }
                        break;
                    }

                }
                if (!serviceNodeFound) {
                    log.info("Service not found for: " + communityId
                            + ".  Adding HCID and service");
                    // Create new service and add it to the services node
                    try {
                        Element serviceElement = doc.createElementNS(
                                "urn:uddi-org:api_v3", "businessService");
                        serviceElement.setAttribute("serviceKey",
                                "uddi:nhincnode:" + serviceName);
                        serviceElement.setAttribute("businessKey",
                                "uddi:nhincnode:" + communityId);

                        Element name = doc.createElementNS(
                                "urn:uddi-org:api_v3", "name");
                        name.setAttribute("xml:lang", "en");
                        name.setTextContent(serviceName);
                        serviceElement.appendChild(name);

                        Element bindingTemplates = doc.createElementNS(
                                "urn:uddi-org:api_v3", "bindingTemplates");
                        Element bindingTemplate = doc.createElementNS(
                                "urn:uddi-org:api_v3", "bindingTemplate");
                        bindingTemplate.setAttribute("bindingKey",
                                "uddi:nhincnode:" + serviceName);
                        bindingTemplate.setAttribute("serviceKey",
                                "uddi:nhincnode:" + serviceName);
                        Element accessPoint = doc.createElementNS(
                                "urn:uddi-org:api_v3", "accessPoint");
                        accessPoint.setAttribute("useType", "endPoint");
                        accessPoint.setTextContent(serviceUrl);
                        Element btCategoryBags = doc.createElementNS(
                                "urn:uddi-org:api_v3", "categoryBag");

                        if (serviceName.toLowerCase().contains("adapter")) {
                            Element keyedRefAdap = doc.createElementNS(
                                    "urn:uddi-org:api_v3", "keyedReference");
                            keyedRefAdap.setAttribute("tModelKey",
                                    "CONNECT:adapter:apilevel");
                            keyedRefAdap.setAttribute("keyName", "");
                            keyedRefAdap.setAttribute("keyValue", "LEVEL_a0");
                            btCategoryBags.appendChild(keyedRefAdap);
                        } else {
                            Element btKeyedReference = doc.createElementNS(
                                    "urn:uddi-org:api_v3", "keyedReference");
                            btKeyedReference.setAttribute("keyName", "");
                            btKeyedReference.setAttribute("tModelKey",
                                    "uddi:nhin:versionofservice");
                            btKeyedReference.setAttribute("keyValue",
                                    endpointVersion);
                            btCategoryBags.appendChild(btKeyedReference);
                        }
                        bindingTemplate.appendChild(accessPoint);
                        bindingTemplate.appendChild(btCategoryBags);
                        bindingTemplates.appendChild(bindingTemplate);

                        Element categoryBag = doc.createElementNS(
                                "urn:uddi-org:api_v3", "categoryBag");
                        Element keyedReference = doc.createElementNS(
                                "urn:uddi-org:api_v3", "keyedReference");
                        keyedReference.setAttribute("tModelKey",
                                "uddi:nhin:standard-servicenames");
                        keyedReference.setAttribute("keyName", serviceName);
                        keyedReference.setAttribute("keyValue", serviceName);
                        categoryBag.appendChild(keyedReference);

                        serviceElement.appendChild(bindingTemplates);
                        serviceElement.appendChild(categoryBag);

                        services.appendChild(serviceElement);
                    } catch (Exception ex) {
                        log.error(ex.getMessage());
                    }
                }
                break;
            }
        }
        try {
            Transformer transformer = TransformerFactory.newInstance()
                    .newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            // initialize StreamResult with File object to save to file
            DOMSource source = new DOMSource(doc);
            FileOutputStream fileOutput = new FileOutputStream(fullPath);
            StreamResult stream = new StreamResult(fileOutput);
            transformer.transform(source, stream);
            // fileOutput.finalize();
            fileOutput.close();
            log.info("Done createorupdate: " + fileName);
        } catch (Exception e) {
            log.error(
                    "Exception writing out connection info file: "
                            + e.getMessage(), e);
        }
    }
	
	/**
	 * Copies a list of configuration values to a temporary directory.
	 * @param configDir Configuration directory of files to be backed up.
	 * @param log SoapUI logger.
	 */
	public static void backupConfiguration(String configDir, Logger log) {
		log.info("Start backupConfiguration");
		try {
			File backupDir = new File(configDir, TEMP_DIR);
			File confDir = new File(configDir);

			backupDir.mkdir();

			for (File file : confDir.listFiles()) {
				copyFile(confDir.getAbsolutePath(), file.getName(),
						backupDir.getAbsolutePath(), file.getName(), log);
			}
		} catch (Throwable e) {
			log.error(e.getMessage());
		}
		log.info("End backupConfiguration");
	}

	/**
	 * Restores a list of configuration values from a temporary directory.
	 * The temporary directory is deleted after the restore based on the
	 * passed in optionDel boolean.
	 * @param configDir Configuration directory for files to be restored to.
	 * @param log SoapUI logger.
	 * @param optionDel Flag for deleting the temporary directory.
	 */
	public static void restoreConfiguration(String configDir, Logger log,
			Boolean optionDel) {
		log.info("Start restoreConfiguration");
		try {
			File backupDir = new File(configDir, TEMP_DIR);
			File confDir = new File(configDir);

			for (File file : backupDir.listFiles()) {
				copyFile(backupDir.getAbsolutePath(), file.getName(),
						confDir.getAbsolutePath(), file.getName(), log);
				if (optionDel == true) {
					File tmpFile = new File(backupDir.getAbsolutePath(),
							file.getName());
					tmpFile.delete();
				}
			}

			if (optionDel == true)
				backupDir.delete();

		} catch (Throwable e) {
			log.error(e.getMessage());
		}
		log.info("End restoreConfiguration");
	}

	/**
	 * Restores a list of configurable values from a temporary directory which
	 * is deleted on completion.
	 * @param configDir Configuration directory for files to be restored to.
	 * @param log SoapUI logger.
	 */
	public static void restoreConfiguration(String configDir, Logger log) {
		restoreConfiguration(configDir, log, true);
	}
	
	/**
	 * Backs up a single file.
	 * @param configDir The directory that the file to be backed up is in.
	 * @param fileName	The name of the file to be backed up.
	 * @param log SoapUI logger.
	 */
	public static void backupFile(String configDir, String fileName, Logger log){
		log.info("Backing up file " + fileName + " in " + configDir + " directory." );
		copyFile(configDir, fileName, configDir + File.separator +
				TEMP_DIR, fileName, log);
	}
	
	/**
	 * Restores a single file from backup.
	 * @param configDir Directory that the file is restored to.
	 * @param fileName The name of the file to be restored.
	 * @param log SoapUI logger.
	 */
	public static void restoreFile(String configDir, String fileName, Logger log){
		log.info("Restoring file " + fileName);
		File configDirFile = new File(configDir);
		File backupDir = new File(configDir, TEMP_DIR);
		copyFile(backupDir.getAbsolutePath(), fileName, configDirFile.getAbsolutePath(),
				fileName, log);
		
		new File(backupDir.getAbsolutePath(), fileName).delete();
		
		if(backupDir.listFiles().length == 0){
			backupDir.delete();
		}
	}
	
}