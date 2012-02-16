package setvalidationsuitewsdlpath;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Finds the valdiation suite for the current install of CONNECT 3.3 and beyond and changes the project xml
 * files within to have the correct path to the appropriate WSDL files.
 * @author Jason Smith
 */
public class ValidationSuiteWsdlRunner {

    //Used for editing the soapui xml project files
    private DocumentBuilderFactory docBuilderFactory;
    private DocumentBuilder docBuilder;
    private XPathFactory xFactory;
    private XPath xPath;
    private Transformer xFormer;

    //For finding the correct paths to the WSDLs and the Validation Suite
    private final String sourcePath = "NHINC_SOURCE_DIR";
    private final String binaryPath = "NHINC_THIRDPARTY_DIR";
    private final String glassfishHome = "AS_HOME";
    private final String jBossHome = "JBOSS_HOME";
    private final String jBossConfig = "NHINC_PROPERTIES_DIR";

    private List<String> validationFiles;

    private final char slash = File.separatorChar;

    private String pathToWsdls;
    //private Boolean setSpecifiedPath;

    public ValidationSuiteWsdlRunner(){
        initialize();
        //this.setSpecifiedPath = false;
        validationFiles = new ArrayList<String>();
        setValidationFiles();
    }

    public ValidationSuiteWsdlRunner(String wsdlPath){
        initialize();
        this.pathToWsdls = wsdlPath;
        //this.setSpecifiedPath = true;
        validationFiles = new ArrayList<String>();
        setValidationFiles();
    }

    private void setValidationFiles(){
        File directory = new File(".");
        File[] files = directory.listFiles();

        for(File file : files){
            if(file.getName().toLowerCase().contains("soapui-project.xml")){
                System.out.println("Set: " + file.getName());
                validationFiles.add(file.getName());
            }//else Don't add file.
        }
    }

    /**
     * Initializes all the xml parsing objects and sets the appropriate namespace
     * for soapui.
     * @throws ParserConfigurationException
     * @throws TransformerConfigurationException
     */    
    private void initialize(){
        try {
            docBuilderFactory = DocumentBuilderFactory.newInstance();
            docBuilderFactory.setNamespaceAware(true);
            docBuilder = docBuilderFactory.newDocumentBuilder();
            xFactory = XPathFactory.newInstance();
            xPath = xFactory.newXPath();
            xPath.setNamespaceContext(new PersonalNamespaceContext());
            xFormer = TransformerFactory.newInstance().newTransformer();
        } catch (Exception ex) {
            Logger.getLogger(ValidationSuiteWsdlRunner.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException("Failed to initialize XML builder/xpath objects.");
        }
    }

    /**
     * Each validation suite test is altered to include the correct path
     * to the appropriate WSDL files.
     */
    public void setWsdls(){
        setPaths();

        for(String fileName : validationFiles){
            try {
                
                Document doc = docBuilder.parse(fileName);
                setAttributes(getNodes(doc), doc);
                xFormer.transform(new DOMSource(doc), new StreamResult(new File(fileName)));
            } catch (Exception ex) {
                Logger.getLogger(ValidationSuiteWsdlRunner.class.getName()).log(Level.SEVERE, null, ex);
                throw new RuntimeException("Failed to set wsdl paths.");
            } 
        }
    }

    /**
     * Sets the paths for where the Validation Suite and the WSDL files are.
     */
    private void setPaths(){
 
        if(pathToWsdls == null){
            if(System.getenv(jBossHome)!= null){
                pathToWsdls = System.getenv(jBossConfig) + slash + "wsdl" + slash;
            }
            else if(System.getenv(glassfishHome)!=null){
                pathToWsdls = System.getenv(glassfishHome) + slash + "domains" + slash +
                    "domain1" + slash + "config" + slash + "nhin" + slash + "wsdl" + slash;
            }else pathToWsdls = "No Valid WSDL Directory";
        }
        //else do nothing because already pathToWsdls is already set by command line arg
    }

    /**
     * Gets all the appropriate nodes for wsdl definition using xPath.
     * @param doc - The soapui project xml document.
     * @return
     * @throws XPathExpressionException
     */
    private NodeList getNodes(Document doc) throws XPathExpressionException{
        return (NodeList)xPath.evaluate("/con:soapui-project/con:interface", doc, XPathConstants.NODESET);
    }

    /**
     * Sets the definition attribute for each interface tag in the validation suite.
     * @param nodes - Each node is the interface node for changing the wsdl path
     * @param doc - The soapui-sml project document.
     */
    private void setAttributes(NodeList nodes, Document doc){

        for(int i=0; i<nodes.getLength(); i++){
            Node node = nodes.item(i);
            NamedNodeMap attributes = node.getAttributes();
            Node definitionAttr = attributes.getNamedItem("definition");

            if(definitionAttr != null){
                String attrValue = definitionAttr.getNodeValue();
                StringTokenizer stringTok = new StringTokenizer(attrValue, "#");

                if(!(attrValue.contains(pathToWsdls))&& attrValue.contains("WSDL path not set.")){
                    
                    stringTok.nextToken();
                    StringBuilder newPath = new StringBuilder(pathToWsdls);
                    newPath.append(stringTok.nextToken());
                    Attr newDefinitionAttr = doc.createAttribute("definition");
                    newDefinitionAttr.setValue(newPath.toString());
                    attributes.setNamedItem(newDefinitionAttr);
                }//else do not alter the attribute because the path has not been set
                 //already (no command line argument) or the path is not set to the
                 //default (original value) set in the downloaded install (before first deploy)
                 //Important--Does not change anything on cases where the path has been manually changed.
            }//else the attribute does not exist and therefore can not be altered.
        }
    }
}
