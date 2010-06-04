package nhinc

import java.io.File;  
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

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

class FileUtils {
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
    properties = new Properties();
    properties.load(frPropFile);

    properties.setProperty(propertyKey, propertyValue);

    FileWriter fwPropFile = new FileWriter(filename);
    fwPropFile = new FileWriter(file);
    properties.store(fwPropFile, "**DO NOT CHECK IN** - written by groovy script FileUtils.groovy->UpdateProperty");
    properties = null;
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

	static CreateOrUpdateConnection(String directory, String communityId, String serviceName, String serviceUrl, context, log) {
	    log.info("begin CreateOrUpdateConnection; directory='" + directory + "';community id='" + communityId + "';service name='" + serviceName + "';service url='" + serviceUrl + "';");
	
		String fullPath = directory + "/internalConnectionInfo.xml";
		log.info("Path to connection info file: " + fullPath);
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
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
    NodeList connectionInfos = doc.getElementsByTagName("internalConnectionInfo");
    for (int i = 0; i < connectionInfos.getLength(); i++) {
      Element connectionInfo = (Element) connectionInfos.item(i);
      NodeList homeCommunityIds = connectionInfo.getElementsByTagName("homeCommunityId");
      Element communityIdElement = (Element)homeCommunityIds.item(0);
      String communityIdValue = communityIdElement.getTextContent();
      if(communityId.equals(communityIdValue))
      {
        NodeList servicesList = connectionInfo.getElementsByTagName("services");
        Element servicesElement = (Element)servicesList.item(0);
        NodeList serviceList = servicesElement.getElementsByTagName("service");
        boolean serviceNodeFound = false;
        for(int serviceNodeIndex = 0; serviceNodeIndex < serviceList.getLength(); serviceNodeIndex++)
        {
            Element serviceElement = (Element)serviceList.item(serviceNodeIndex);
            Element serviceNameElement = (Element)serviceElement.getElementsByTagName("name").item(0);
            if(serviceName.equals(serviceNameElement.getTextContent()))
            {
                serviceNodeFound = true;
                Element endpointUrlElement = (Element)serviceElement.getElementsByTagName("endpointURL").item(0);
                if(!serviceUrl.equals(endpointUrlElement.getTextContent()))
                {
                    endpointUrlElement.setTextContent(serviceUrl);
                }
                break;
            }
          }
          if(!serviceNodeFound)
          {
            // Create new service and add it to the services node
            Element serviceElement = doc.createElement("service");

            Element nameElement = doc.createElement("name");
            nameElement.setTextContent(serviceName);
            serviceElement.appendChild(nameElement);

            Element descriptionElement = doc.createElement("description");
            descriptionElement.setTextContent(serviceName);
            serviceElement.appendChild(descriptionElement);

            Element endpointUrlElement = doc.createElement("endpointURL");
            endpointUrlElement.setTextContent(serviceUrl);
            serviceElement.appendChild(endpointUrlElement);

            servicesElement.appendChild(serviceElement);
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
      transformer.transform(source, new StreamResult(new FileOutputStream(fullPath)));
    }
    catch(Exception e)
    {
      log.error("Exception writing out connection info file: " + e.getMessage(), e);
    }

		log.info("end CreateOrUpdateConnection");
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
} 