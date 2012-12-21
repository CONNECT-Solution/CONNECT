package nhinc

import java.io.File;  
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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

class FileUtils{
  FileUtils() {}
   
  static String ReadFile(String FileName,context,log) {
			log.info("read file: " + FileName);
			File sourceFile = new File(FileName);  
			String filecontents = null;
			if(sourceFile.exists()) 
			{
					log.info("file exists");
					StringBuilder sb = new StringBuilder();

					BufferedReader input =  new BufferedReader(new FileReader(sourceFile));
					try {
					   String line = null; //not declared within while loop
					   /*
					   * readLine is a bit quirky :
					   * it returns the content of a line MINUS the newline.
					   * it returns null only for the END of the stream.
					   * it returns an empty String if two newlines appear in a row.
					   */
					   while (( line = input.readLine()) != null){
					      sb.append(line);
					      sb.append(System.getProperty("line.separator"));
					   }
					}
					finally {
					   input.close();
					}
					filecontents=sb.toString();
			} 
			else {
			   log.info("unable to find source file " + FileName);
			}   
			//log.info("read file contents (" + filecontents.length() + ")");
			return filecontents;
   }

  static MoveFile(sourceDirectory, sourceFileName, destinationDirectory, destinationFileName, context, log) {
      //entries can either be the actual entry or pointer to config item
      //bad naming convention. Developer should be flogged. :)
      sourceDirectory = context.expand( sourceDirectory )
      sourceFileName = context.expand( sourceFileName )
      destinationDirectory = context.expand( destinationDirectory )
      destinationFileName = context.expand( destinationFileName )
   
			File sourceFile = new File(sourceDirectory,sourceFileName);  
			File destinationFile = new File(destinationDirectory, destinationFileName);
			
			// Copy test file
			log.info("copying property file from " + sourceFile + " to " + destinationFile);

			if(sourceFile.exists())
			{
				def reader = sourceFile.newReader();
				destinationFile.withWriter 
				{ writer ->       
					writer << reader;   
				}   
				reader.close();
			} 
			else {
			   log.info("unable to find source file " + sourceFile);
			}   
	}

	static DeleteFile(sourceDirectory, sourceFileName, context, log) {
      sourceDirectory = context.expand( sourceDirectory )
      sourceFileName = context.expand( sourceFileName )
			File file = new File(sourceDirectory,sourceFileName);  
			if(file.exists()) {    
			   log.info("deleting file " + file);
			   file.delete(); 
			} else {
			   log.info("file not found, skipping delete for " + file);
			}
	}
	
	static UpdateProperty(String directory, String filename, String propertyKey, String propertyValue,context,log) {
    log.info("begin UpdateProperty; directory='" + directory + "';filename='" + filename + "';key='" + propertyKey + "';value='" + propertyValue + "';");
	
    File file = new File(directory,filename);
	
    Properties properties = new Properties();
    FileReader frPropFile = new FileReader(file);
    properties.load(frPropFile);

    properties.setProperty(propertyKey, propertyValue);

    FileWriter fwPropFile = new FileWriter(filename);
    fwPropFile = new FileWriter(file);
    properties.store(fwPropFile, "**DO NOT CHECK IN** - written by groovy script FileUtils.groovy->UpdateProperty");
    properties = null;
	}
	
	static updateGatewayProperty(String propertyKey, String propertyValue, context, log){
		UpdateProperty(System.getenv("NHINC_PROPERTIES_DIR"), "gateway.properties", propertyKey, propertyValue, context, log);
	}

	static String ReadProperty(String directory, String filename, String propertyKey, context, log) {
		log.info("begin ReadProperty; directory='" + directory + "';filename='" + filename + "';key='" + propertyKey + "';");
		File file = new File(directory,filename);
		Properties properties = new Properties();
		FileReader frPropFile = new FileReader(file);
		properties = new Properties();
		properties.load(frPropFile);
		String propertyValue = properties.getProperty(propertyKey);
		properties = null;
		return propertyValue;
	}
	
	static changeSpringConfig(String fileName, String desiredImpltype, String beanName, log) throws Exception{
		log.info("begin changeSpringConfig; file='" + fileName + "';specified bean name='" + beanName + "';desired impl='" + desiredImpltype + "';");
		
		//find the config file and insert into document builder
		String fullPath = System.getenv("NHINC_PROPERTIES_DIR") + "/" + fileName;
		log.info("Path to config file: " + fullPath);
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		Document doc = null;
		try{
			DocumentBuilder builder = factory.newDocumentBuilder();
			doc = builder.parse(fullPath);
		}
		catch (Exception e){
			e.printStackTrace();
			return;
		}
		
		//grab the top-level element
		Element beans = (Element)doc.getElementsByTagName("beans").item(0);
		
		//grab the description element and parse out the string 
		//containing the bean name(s)
		Element description = (Element)beans.getElementsByTagName("description").item(0);
		Pattern p = Pattern.compile("\\{(\w*,?)*\\}");
		Matcher m = p.matcher(description.getTextContent());
		m.find();
		String beansInFile = m.group().substring(1, m.group().length() - 1);
		
		//if the bean is not specified and it needs to be because there is 
		//more than one bean in the file then throw an exception
		if ((beansInFile.split(",").getLength() > 1) && beanName.equals("none")){
			log.info("There are multiple beans present in the configuration file " + fileName + ". Please specify the bean to configure.");
			throw new Exception ("There are multiple beans present in the configuration file " + fileName + ". Must specify the bean to configure.");
		}
		
		boolean foundMatch = false;
		
		//if no beanName is given via the overloaded method
		if(beanName.equals("none")){
			//set beanName to the one present from the config file
			beanName = beansInFile;
			foundMatch = true;
			log.info("matched bean name:" + beanName);

		//otherwise ensure a match between the provided beanName and one present in the file
		}else{
			String[] beanNameArray = beansInFile.split(",");
			for (int i = 0; i < beanNameArray.getLength(); i++){
				if (beanNameArray[i].equals(beanName)){
					log.info("matched bean name:" + beanName);
					foundMatch = true;
				}
			}
		}
		
		//if no match for beanName is found, throw an exception
		if (!foundMatch){
			log.info("No bean by name " + beanName + "present in file " + fileName);
			throw new Exception ("No bean by name " + beanName + "present in file " + fileName);
		}
		
		
		//get the list of beans and iterate though them
		NodeList beanList = beans.getElementsByTagName("bean");
		for (int i = 0; i < beanList.getLength(); i++){
			Element bean = (Element)beanList.item(i);
			
			//skip bean if it is not one of the ones that we are interested in
			if (!bean.getAttribure("id").startsWith(beanName))continue;

			//find the active Spring implementation and modify
			//to the non-active form
			if (bean.getAttribute("id").equals(beanName)){
				NodeList metaList = bean.getElementsByTagName("meta");
				for (int j = 0; j < metaList.getLength(); j++){
					Element meta = metaList.item(j);
					if (meta.getAttribute("key").equals("impltype")){
						bean.setAttribute("id", beanName.concat(meta.getAttribute("value")));
					}
				}
			}
			
			//Search the meta list for the desired implementation type.
			//If found, change the bean attribute "id" to the beanName. 
			NodeList metaList = bean.getElementsByTagName("meta");
			for (int j = 0; j < metaList.getLength(); j++){
				Element meta = metaList.item(j);
				
				//if the desired implementation type is NOT "default"...
				if (!desiredImpltype.equals("default")){
					//... search for the desired implementation type and
					//set the bean to the active form
					if (meta.getAttribute("key").equals("impltype") && meta.getAttribute("value").equals(desiredImpltype)){
						bean.setAttribute("id", beanName);
					}
				
				//otherwise the desired implementation type is "default"...
				}else{
				//... search for "default" equals "true" and
				//set the bean to the active form
					if (meta.getAttribute("key").equals("default") && meta.getAttribute("value").equals("true")){
						bean.setAttribute("id", beanName);
					}
				}
			}
		}
		
		//write the document
		try{
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			
			//initialize StreamResult with File object to save to file
			DOMSource source = new DOMSource(doc);
			FileOutputStream fileOutput = new FileOutputStream(fullPath);
			StreamResult stream = new StreamResult(fileOutput);
			transformer.transform(source, stream);
			fileOutput.close();
			log.info("Done createorupdate: " + fileName);
		}
		catch(Exception e){
			log.error("Exception writing out connection info file: " + e.getMessage(), e);
		}
	}


	static changeSpringConfig(String fileName, String desiredImpltype, log){
	changeSpringConfig(fileName, desiredImpltype, "none", log);
	}


	static CreateOrUpdateConnection(String fileName, String directory, String communityId, String serviceName, String serviceUrl, String defaultVersion, context, log) {
		
		log.info("begin CreateOrUpdateConnection; directory='" + directory + "';community id='" + communityId + "';service name='" + serviceName + "';service url='" + serviceUrl + "';");

		String fullPath = directory + "/" + fileName;
		log.info("Path to connection info file: " + fullPath);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
        Document doc = null;
        try
            {
              DocumentBuilder builder = factory.newDocumentBuilder();
              doc = builder.parse(fullPath);
            }
        catch (Exception e) {
              e.printStackTrace();
              return;
            }

        Element businessDetail = (Element)doc.getElementsByTagName("businessDetail").item(0);
        NodeList businessEntities = businessDetail.getElementsByTagName("businessEntity");

        for (int i = 0; i < businessEntities.getLength(); i++) {
           Element businessEntity = (Element) businessEntities.item(i);
           String communityIdValue = businessEntity.getAttribute("businessKey");
           if(communityIdValue.equals("uddi:testnhincnode:"+communityId))
            {
            Element services = (Element) businessEntity.getElementsByTagName("businessServices").item(0);
            NodeList serviceList = services.getElementsByTagName("businessService");
            boolean serviceNodeFound = false;

            for(int serviceNodeIndex = 0; serviceNodeIndex < serviceList.getLength(); serviceNodeIndex++)
            {
                Element serviceElement = (Element)serviceList.item(serviceNodeIndex);
                String name = serviceElement.getElementsByTagName("name").item(0).getTextContent();
                if(serviceName.equals(name))
                {
                    serviceNodeFound = true;
                    Element bindingTemplates = (Element)serviceElement.getElementsByTagName("bindingTemplates").item(0);
                    NodeList bindingTemplatesList = bindingTemplates.getElementsByTagName("bindingTemplate");
                    float bindingTemplateVersion = 0;
                    Element latestVersionBindingTemplate = null;
                    
                    if(bindingTemplatesList.getLength() > 1){
                        for(int bindingNodeIndex = 0; bindingNodeIndex < bindingTemplatesList.getLength(); bindingNodeIndex++){
                            Element currBindingTemplate = (Element)bindingTemplatesList.item(bindingNodeIndex);
                            Element bindingCategoryBag = (Element)currBindingTemplate.getElementsByTagName("categoryBag").item(0);
                            Element bindingKeyedRef = (Element)bindingCategoryBag.getElementsByTagName("keyedReference").item(0);
                            String currVersionString = bindingKeyedRef.getAttribute("keyValue");
							if(currVersionString.contains("LEVEL")){
								if(latestVersionBindingTemplate != null){
									if(currVersionString.equals("LEVEL_a1")){
										latestVersionBindingTemplate = currBindingTemplate;
									}//else template is prior version and doesn't need to be set
								}else latestVersionBindingTemplate = currBindingTemplate;
							}
							else{
								float currVersion = new Float(currVersionString);
								if(currVersion > bindingTemplateVersion){
									bindingTemplateVersion = currVersion;
									latestVersionBindingTemplate = currBindingTemplate;
								}
							}
                        }
                    }else{
                        latestVersionBindingTemplate = (Element)bindingTemplatesList.item(0);
                    }

                    if(latestVersionBindingTemplate != null){
                        Element accessPoint = (Element)latestVersionBindingTemplate.getElementsByTagName("accessPoint").item(0);
                        if(!serviceUrl.equals(accessPoint.getTextContent())){
                            accessPoint.setTextContent(serviceUrl);
                        }
                    }
                        break;
                   }

            }
          if(!serviceNodeFound)
          {
            // Create new service and add it to the services node
            Element serviceElement = doc.createElementNS("urn:uddi-org:api_v3", "businessService");
            serviceElement.setAttribute("serviceKey", "uddi:testnhincnode:"+serviceName);
            serviceElement.setAttribute("businessKey", "uddi:testnhieonenode:"+communityId);

            Element name = doc.createElementNS("urn:uddi-org:api_v3", "name");
            name.setAttribute("xml:lang", "en");
            name.setTextContent(serviceName);
            serviceElement.appendChild(name);

            Element bindingTemplates = doc.createElementNS("urn:uddi-org:api_v3","bindingTemplates");
            Element bindingTemplate = doc.createElementNS("urn:uddi-org:api_v3", "bindingTemplate");
            bindingTemplate.setAttribute("bindingKey", "uddi:testnhincnode:"+serviceName);
            bindingTemplate.setAttribute("serviceKey", "uddi:testnhincnode:"+serviceName);
            Element accessPoint = doc.createElementNS("urn:uddi-org:api_v3", "accessPoint");
            accessPoint.setAttribute("useType", "endPoint");
            accessPoint.setTextContent(serviceUrl);
            Element btCategoryBags = doc.createElementNS("urn:uddi-org:api_v3", "categoryBag");
            
            if(serviceName.toLowerCase().contains("adapter")){
                Element keyedRefAdap = doc.createElementNS("urn:uddi-org:api_v3", "keyedReference");
                keyedRefAdap.setAttribute("tModelKey", "CONNECT:adapter:apilevel");
                keyedRefAdap.setAttribute("keyName", "");
                keyedRefAdap.setAttribute("keyValue", "LEVEL_a0");
                btCategoryBags.appendChild(keyedRefAdap);
            }else {
                Element btKeyedReference = doc.createElementNS("urn:uddi-org:api_v3", "keyedReference");
                btKeyedReference.setAttribute("keyName", "");
				btKeyedReference.setAttribute("tModelKey","uddi:nhin:versionofservice");
                btKeyedReference.setAttribute("keyValue",defaultVersion);
                btCategoryBags.appendChild(btKeyedReference);
            }
            bindingTemplate.appendChild(accessPoint);
            bindingTemplate.appendChild(btCategoryBags);
            bindingTemplates.appendChild(bindingTemplate);

            Element categoryBag = doc.createElementNS("urn:uddi-org:api_v3", "categoryBag");
            Element keyedReference = doc.createElementNS("urn:uddi-org:api_v3", "keyedReference");
            keyedReference.setAttribute("tModelKey", "uddi:nhin:standard-servicenames");
            keyedReference.setAttribute("keyName", serviceName);
            keyedReference.setAttribute("keyValue", serviceName);
            categoryBag.appendChild(keyedReference);
            

            serviceElement.appendChild(bindingTemplates);
            serviceElement.appendChild(categoryBag);

            services.appendChild(serviceElement);
          }
          break;
        }
    }
try
    {
      Transformer transformer = TransformerFactory.newInstance().newTransformer();
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");

      //initialize StreamResult with File object to save to file
      DOMSource source = new DOMSource(doc);
   FileOutputStream fileOutput = new FileOutputStream(fullPath);
   StreamResult stream = new StreamResult(fileOutput);
      transformer.transform(source, stream);
	//fileOutput.finalize();
	fileOutput.close();
	log.info("Done createorupdate: " + fileName);
    }
    catch(Exception e)
    {
      log.error("Exception writing out connection info file: " + e.getMessage(), e);
    }
}
	
  static InitializeNHINCProperties(context, log) {}
  
  static backupConfiguration(context, log) {
    log.info("Start backupConfiguration(context, log)")
    try{
      def backupDir = new File(System.getProperty("java.io.tmpdir"), "nhinc_conf")
      def confDir = new File(System.env['NHINC_PROPERTIES_DIR'])
      
      def files2backup = ["hiemTopicConfiguration.xml","internalConnectionInfo.xml","PCConfiguration.xml","uddiConnectionInfo.xml","XDSUniqueIds.properties","gateway.properties","adapter.properties"]
      files2backup.each{
        def file2backup = new File(confDir, it)
        org.apache.commons.io.FileUtils.copyFileToDirectory(file2backup, backupDir, true)
      }
    } catch(Throwable e) {
      log.error(e.getMessage())
      context.getTestRunner().fail("Failed to backup NHINC configuration: " + e.getMessage())
    }
    log.info("End backupConfiguration(context, log)")
  }
  
  static restoreConfiguration(context, log) {
    log.info("Start restoreConfiguration(context, log)")
    try{
      def backupDir = new File(System.getProperty("java.io.tmpdir"), "nhinc_conf")
      def confDir = new File(System.env['NHINC_PROPERTIES_DIR'])
      
      def files2resore = ["hiemTopicConfiguration.xml","internalConnectionInfo.xml","PCConfiguration.xml","uddiConnectionInfo.xml","XDSUniqueIds.properties","gateway.properties","adapter.properties"]
      
      files2resore.each{
        def file2resore = new File(backupDir, it)
        org.apache.commons.io.FileUtils.copyFileToDirectory(file2resore, confDir, true)
      }
    }catch(Throwable e) {
      log.error(e.getMessage())
      context.getTestRunner().fail("Failed to restore NHINC configuration: " + e.getMessage())
    }
    log.info("End restoreConfiguration(context, log)")
  }
  
  static restoreToMasterConfiguration(context, log) {
    log.info("Start restoreToMasterConfiguration(context, log)")
    try{
	
	  def masterDirFile = System.env['NHINC_PROPERTIES_DIR']+"/master"
      def masterDir = new File(masterDirFile)
      def confDir = new File(System.env['NHINC_PROPERTIES_DIR'])
      
      def files2restore = ["internalConnectionInfo.xml","adapter.properties","gateway.properties","hiemTopicConfiguration.xml","XDSUniqueIds.properties","PCConfiguration.xml"]
      
	  files2restore.each{
        def file2restore = new File(masterDir, it)
        org.apache.commons.io.FileUtils.copyFileToDirectory(file2restore, confDir, true)
	  }
    }catch(Throwable e) {
      log.error(e.getMessage())
      context.getTestRunner().fail("Failed to restore NHINC configuration: " + e.getMessage())
    }
    log.info("End restoreToMasterConfiguration(context, log)")
  }
} 