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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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

import org.apache.log4j.Logger;

class FileUtils {

	static String[] files2restore = { "hiemTopicConfiguration.xml",
			"internalConnectionInfo.xml", "PCConfiguration.xml",
			"uddiConnectionInfo.xml", "XDSUniqueIds.properties",
			"gateway.properties", "adapter.properties", "purposeUse.properties" };
	// should these match?
	static String[] files2backup = { "hiemTopicConfiguration.xml",
			"internalConnectionInfo.xml", "PCConfiguration.xml",
			"uddiConnectionInfo.xml", "XDSUniqueIds.properties",
			"gateway.properties", "adapter.properties", "purposeUse.properties" };

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

	public static void setG0ConnectionInfo(String sourceDirectory,
			String destDirectory, Logger log) {
		moveFile(sourceDirectory, "uddiConnectionInfo_g0.xml", destDirectory,
				"uddiConnectionInfo.xml", log);
		moveFile(sourceDirectory, "internalConnectionInfo_g0.xml",
				destDirectory, "internalConnectionInfo.xml", log);
	}

	public static void setG1ConnectionInfo(String sourceDirectory,
			String destDirectory, Logger log) {
		moveFile(sourceDirectory, "uddiConnectionInfo_g1.xml", destDirectory,
				"uddiConnectionInfo.xml", log);
		moveFile(sourceDirectory, "internalConnectionInfo_g1.xml",
				destDirectory, "internalConnectionInfo.xml", log);
	}

	public static void moveFile(String sourceDirectory, String sourceFileName,
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

	public static void changeSpringConfig(String configDir, String fileName,
			String desiredImpltype, String beanName, Logger log)
			throws Exception {
		log.info("begin changeSpringConfig; file='" + fileName
				+ "';specified bean name='" + beanName + "';desired impl='"
				+ desiredImpltype + "';");

		// find the config file and insert into document builder
		String fullPath = configDir + "/" + fileName;
		log.info("Path to config file: " + fullPath);
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

		// grab the top-level element
		Element beans = (Element) doc.getElementsByTagName("beans").item(0);

		// grab the description element and parse out the string
		// containing the bean name(s)
		Element description = (Element) beans.getElementsByTagName(
				"description").item(0);
		Pattern p = Pattern.compile("\\{(\\w*,?)*\\}");
		Matcher m = p.matcher(description.getTextContent());
		m.find();
		String beansInFile = m.group().substring(1, m.group().length() - 1);

		// if the bean is not specified and it needs to be because there is
		// more than one bean in the file then throw an exception
		if ((beansInFile.split(",").length > 1) && beanName.equals("none")) {
			log.info("There are multiple beans present in the configuration file "
					+ fileName + ". Please specify the bean to configure.");
			throw new Exception(
					"There are multiple beans present in the configuration file "
							+ fileName
							+ ". Must specify the bean to configure.");
		}

		boolean foundMatch = false;

		// if no beanName is given via the overloaded method
		if (beanName.equals("none")) {
			// set beanName to the one present from the config file
			beanName = beansInFile;
			foundMatch = true;
			log.info("matched bean name:" + beanName);

			// otherwise ensure a match between the provided beanName and one
			// present in the file
		} else {
			String[] beanNameArray = beansInFile.split(",");
			for (int i = 0; i < beanNameArray.length; i++) {
				if (beanNameArray[i].equals(beanName)) {
					log.info("matched bean name:" + beanName);
					foundMatch = true;
				}
			}
		}

		// if no match for beanName is found, throw an exception
		if (!foundMatch) {
			log.info("No bean by name " + beanName + "present in file "
					+ fileName);
			throw new Exception("No bean by name " + beanName
					+ "present in file " + fileName);
		}

		// get the list of beans and iterate though them
		NodeList beanList = beans.getElementsByTagName("bean");
		for (int i = 0; i < beanList.getLength(); i++) {
			Element bean = (Element) beanList.item(i);

			// skip bean if it is not one of the ones that we are interested in
			if (!bean.getAttribute("id").startsWith(beanName))
				continue;

			// find the active Spring implementation and modify
			// to the non-active form
			if (bean.getAttribute("id").equals(beanName)) {
				NodeList metaList = bean.getElementsByTagName("meta");
				for (int j = 0; j < metaList.getLength(); j++) {
					Element meta = (Element) metaList.item(j);
					if (meta.getAttribute("key").equals("impltype")) {
						bean.setAttribute("id",
								beanName.concat(meta.getAttribute("value")));
					}
				}
			}

			// Search the meta list for the desired implementation type.
			// If found, change the bean attribute "id" to the beanName.
			NodeList metaList = bean.getElementsByTagName("meta");
			for (int j = 0; j < metaList.getLength(); j++) {
				Element meta = (Element) metaList.item(j);

				// if the desired implementation type is NOT "default"...
				if (!desiredImpltype.equals("default")) {
					// ... search for the desired implementation type and
					// set the bean to the active form
					if (meta.getAttribute("key").equals("impltype")
							&& meta.getAttribute("value").equals(
									desiredImpltype)) {
						bean.setAttribute("id", beanName);
					}

					// otherwise the desired implementation type is "default"...
				} else {
					// ... search for "default" equals "true" and
					// set the bean to the active form
					if (meta.getAttribute("key").equals("default")
							&& meta.getAttribute("value").equals("true")) {
						bean.setAttribute("id", beanName);
					}
				}
			}
		}

		// write the document
		try {
			Transformer transformer = TransformerFactory.newInstance()
					.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");

			// initialize StreamResult with File object to save to file
			DOMSource source = new DOMSource(doc);
			FileOutputStream fileOutput = new FileOutputStream(fullPath);
			StreamResult stream = new StreamResult(fileOutput);
			transformer.transform(source, stream);
			fileOutput.close();
			log.info("Done createorupdate: " + fileName);
		} catch (Exception e) {
			log.error(
					"Exception writing out connection info file: "
							+ e.getMessage(), e);
		}
	}

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

	public static void backupConfiguration(String configDir, Logger log) {
		log.info("Start backupConfiguration");
		try {
			File backupDir = new File(configDir, "prop_temp");
			File confDir = new File(configDir);

			backupDir.mkdir();

			for (String fileName : files2backup) {
				moveFile(confDir.getAbsolutePath(), fileName,
						backupDir.getAbsolutePath(), fileName, log);
			}
		} catch (Throwable e) {
			log.error(e.getMessage());
		}
		log.info("End backupConfiguration");
	}

	public static void restoreConfiguration(String configDir, Logger log,
			Boolean optionDel) {
		log.info("Start restoreConfiguration");
		try {
			File backupDir = new File(configDir, "prop_temp");
			File confDir = new File(configDir);

			for (String fileName : files2restore) {
				moveFile(backupDir.getAbsolutePath(), fileName,
						confDir.getAbsolutePath(), fileName, log);
				if (optionDel == true) {
					File tmpFile = new File(backupDir.getAbsolutePath(),
							fileName);
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

	public static void restoreConfiguration(String configDir, Logger log) {
		restoreConfiguration(configDir, log, true);
	}

	public static void restoreToMasterConfiguration(String configFile,
			Logger log) {

		log.info("Start restoreToMasterConfiguration");
		try {

			String masterDirFile = configFile + "/master";
			File masterDir = new File(masterDirFile);
			File confDir = new File(configFile);

			for (String fileName : files2restore) {
				moveFile(masterDir.getAbsolutePath(), fileName,
						confDir.getAbsolutePath(), fileName, log);
			}

		} catch (Throwable e) {
			log.error(e.getMessage());
		}
		log.info("End restoreToMasterConfiguration");
	}

	

}