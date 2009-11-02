/*
 * This code is subject to the HIEOS License, Version 1.0
 *
 * Copyright(c) 2008-2009 Vangent, Inc.  All rights reserved.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

 /*
 * XConfig.java
 *
 * Created on January 14, 2009
 *
 */
package com.vangent.hieos.xutil.xconfig;

// Leveraging common2 library (quickest way to get there).
import com.vangent.hieos.xutil.http.HttpClient;
import com.vangent.hieos.xutil.xml.Util;
import com.vangent.hieos.xutil.exception.XdsInternalException;
import com.vangent.hieos.xutil.iosupport.Io;

// Third-party.
import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashMap;
import javax.xml.namespace.QName;
import org.apache.axiom.om.OMElement;
import java.util.Collection;
import java.io.File;
import java.io.FileInputStream;
import org.apache.log4j.Logger;

/**
 * Maintains system configuration parameters for IHE XDS.b and XCA profiles.  Acts as a Singleton.
 *
 * @author Bernie Thuman
 *
 */
public class XConfig {
    private final static Logger logger = Logger.getLogger(XConfig.class);

    // Location of XDS.b / XCA configuration file (looks in environment variable first.
    static private String _configURL = "http://localhost:8080/xref/config/xconfig.xml";
    static XConfig _instance = null;  // Singleton instance.

    // Internal data structure starts here.
    private XConfigHomeCommunity homeCommunity = null;
    private HashMap respondingGateways = new HashMap();     // Key = homeCommunityId, Value = XConfigGateway
    private HashMap registries = new HashMap();             // Key = registry name, Value = XConfigRegistry
    private HashMap repositories = new HashMap();           // Key = repository unique id, XConfigRepository
    private HashMap assigningAuthorities = new HashMap();   // Key = AA unique ID, XConfigAssigningAuthority

    /**
     * Returns Singleton instance of XConfig.
     *
     * @return Singleton instance of XConfig
     * @throws XdsInternalException
     */
    static public synchronized XConfig getInstance() throws XdsInternalException {
        if (_instance == null) {
            _instance = new XConfig();
        }
        return _instance;
    }

    /**
     * Private constructor responsible for loading configuration file into memory.
     */
    private XConfig() throws XdsInternalException {
        loadConfiguration();
    }

    /**
     * 
     * @return
     */
    public XConfigHomeCommunity getHomeCommunity() {
        return homeCommunity;
    }

    /**
     * Returns an instance of XConfigGateway based on a homeCommunityId.
     *
     * @param homeCommunityId
     * @return An instance of XConfigGateway or null (if not found).
     */
    public XConfigGateway getGateway(String homeCommunityId) {
        XConfigGateway gateway = null;
        if (this.respondingGateways.containsKey(homeCommunityId)) {
            gateway = (XConfigGateway) this.respondingGateways.get(homeCommunityId);
        }
        return gateway;
    }

    /**
     * 
     * @param gatewayName
     * @return
     */
    protected XConfigGateway getGatewayByName(String gatewayName) {
        XConfigGateway gateway = null;
        Collection gatewayList = this.respondingGateways.values();
        for (Iterator it = gatewayList.iterator(); (gateway == null) && it.hasNext();) {
            XConfigGateway currentGateway = (XConfigGateway) it.next();
            if (currentGateway.getName().equals(gatewayName)) {
                // Match found.
                gateway = currentGateway;
            }
        }
        return gateway;
    }

    /**
     * 
     * @param repositoryId
     * @return
     */
    public XConfigRepository getRepository(String repositoryId) {
        XConfigRepository repository = null;
        if (this.repositories.containsKey(repositoryId)) {
            repository = (XConfigRepository) this.repositories.get(repositoryId);
        }
        return repository;
    }

    /**
     *
     * @param repositoryName
     * @return
     */
    public XConfigRepository getRepositoryByName(String repositoryName) {
        XConfigRepository repository = null;
        Collection<XConfigRepository> values = this.repositories.values();
        for (Iterator it = values.iterator(); (repository == null) && it.hasNext();) {
            XConfigRepository repo = (XConfigRepository) it.next();
            if (repo.getName().equals(repositoryName)) {
                repository = repo;
            }
        }
        return repository;
    }

    /**
     *
     * @param registryName
     * @return
     */
    public XConfigRegistry getRegistryByName(String registryName) {
        XConfigRegistry registry = null;
        if (this.registries.containsKey(registryName)) {
            registry = (XConfigRegistry) this.registries.get(registryName);
        }
        return registry;
    }

    /**
     *
     * @return
     */
    public ArrayList<XConfigGateway> getRespondingGateways() {
        ArrayList<XConfigGateway> gateways = new ArrayList<XConfigGateway>();
        Collection<XConfigGateway> values = this.respondingGateways.values();
        gateways.addAll(values);
        return gateways;
    }

    /**
     *
     * @return
     */
    public ArrayList<XConfigAssigningAuthority> getAssigningAuthorities() {
        ArrayList<XConfigAssigningAuthority> assigningAuthorities = new ArrayList<XConfigAssigningAuthority>();
        Collection<XConfigAssigningAuthority> values = this.assigningAuthorities.values();
        assigningAuthorities.addAll(values);
        return assigningAuthorities;
    }

    /**
     *
     * @param uniqueId
     * @return
     */
    public XConfigAssigningAuthority getAssigningAuthority(String uniqueId) {
        XConfigAssigningAuthority assigningAuthority = null;
        if (this.assigningAuthorities.containsKey(uniqueId)) {
            assigningAuthority = (XConfigAssigningAuthority) this.assigningAuthorities.get(uniqueId);
        }
        return assigningAuthority;
    }

    /**
     *
     * @param propKey
     * @return
     */
    public String getHomeCommunityProperty(String propKey) {
        return _instance.homeCommunity.getProperty(propKey);
    }

    /**
     *
     * @param propKey
     * @return
     */
    public boolean getHomeCommunityPropertyAsBoolean(String propKey) {
        return _instance.homeCommunity.getPropertyAsBoolean(propKey);
    }

    /**
     * 
     * @param propKey
     * @return
     */
    public long getHomeCommunityPropertyAsLong(String propKey) {
        String longVal = _instance.getHomeCommunityProperty(propKey);
        return (new Long(longVal)).longValue();
    }

    /**
     * Retrieves XML configuration file, parses and places into an easily accessible data structure.
     */
    private void loadConfiguration() throws XdsInternalException {

        // Look for environment variable first.
        String configLocation = System.getenv("HIEOSxConfigFile");
        String configXML = null;
        if (configLocation != null) {
            try {
                logger.info("Loading XConfig from: " + configLocation);
                // Get the configuration file from the file system.
                configXML = Io.getStringFromInputStream(new FileInputStream(new File(configLocation)));
            } catch (Exception e) {
                throw new XdsInternalException(
                        "XConfig: Could not load configuration from " + configLocation + " " + e.getMessage());
            }
        } else {
            configLocation = _configURL;
            logger.info("Loading XConfig from: " + configLocation);
            // Get the configuration file from web server.
            try {
                configXML = HttpClient.httpGet(configLocation);
            } catch (Exception e) {
                throw new XdsInternalException(
                        "XConfig: Could not load configuration from " + configLocation + " " + e.getMessage());
            }
        }
        // Parse the XML file.
        OMElement configXMLRoot = Util.parse_xml(configXML);
        if (configXMLRoot == null) {
            throw new XdsInternalException(
                    "XConfig: Could not parse configuration from " + configLocation);
        }
        buildInternalStructure(configXMLRoot);
    }

    /**
     *
     * @param configXML Holds XML string of configuration file.
     */
    private void buildInternalStructure(OMElement rootNode) {
        // Keep in this order!
        parseHomeCommunity(rootNode);
        parseRegistries(rootNode);
        parseRepositories(rootNode);
        parseGateways(rootNode);
        parseAssigningAuthorities(rootNode);
    }

    /**
     *
     * @param rootNode
     */
    private void parseHomeCommunity(OMElement rootNode) {
        OMElement currentNode = rootNode.getFirstChildWithName(new QName("HomeCommunity"));
        this.homeCommunity = new XConfigHomeCommunity();
        this.homeCommunity.parse(currentNode);
    }

    /**
     *
     * @param rootNode
     */
    private void parseRegistries(OMElement rootNode) {
        ArrayList<OMElement> list = this.parseLevelOneNode(rootNode, "Registry");
        for (OMElement currentNode : list) {
            XConfigRegistry registry = new XConfigRegistry();
            registry.parse(currentNode, this);
            this.registries.put(registry.getName(), registry);
        }
    }

    /**
     *
     * @param rootNode
     */
    private void parseRepositories(OMElement rootNode) {
        ArrayList<OMElement> list = this.parseLevelOneNode(rootNode, "Repository");
        for (OMElement currentNode : list) {
            XConfigRepository repository = new XConfigRepository();
            repository.parse(currentNode, this);
            //System.out.println("*** Adding Repository ID = " + repository.getUniqueId());
            this.repositories.put(repository.getUniqueId(), repository);

            String repoName = repository.getName();
            if (repoName.equals(this.homeCommunity.getLocalRepositoryName())) {
                this.homeCommunity.setLocalRepository(repository);
            }
        }
    }

    /**
     * 
     * @param rootNode
     */
    private void parseGateways(OMElement rootNode) {
        ArrayList<OMElement> list = this.parseLevelOneNode(rootNode, "Gateway");
        for (OMElement currentNode : list) {
            XConfigGateway gateway = new XConfigGateway();
            gateway.parse(currentNode, this);
            String gatewayName = gateway.getName();
            if (gatewayName.equals(this.homeCommunity.getInitiatingGatewayName())) {
                this.homeCommunity.setInitiatingGateway(gateway);
            } else {
                if (gatewayName.equals(this.homeCommunity.getRespondingGatewayName())) {
                    this.homeCommunity.setRespondingGateway(gateway);
                }

                // Only place responding gateways in this list (even the local one).
                this.respondingGateways.put(gateway.getHomeCommunityId(), gateway);
            }
            // Now hookup the local registry instance.
            String localRegistryName = gateway.getLocalRegistryName();
            if (localRegistryName != null) {
                // Get the local registry config (already parsed) and set in gateway.
                XConfigRegistry registry = this.getRegistryByName(localRegistryName);
                gateway.setLocalRegistry(registry);
            }
        }
    }

    /**
     *
     * @param rootNode
     */
    private void parseAssigningAuthorities(OMElement rootNode) {
        ArrayList<OMElement> list = this.parseLevelOneNode(rootNode, "AssigningAuthority");
        for (OMElement currentNode : list) {
            XConfigAssigningAuthority assigningAuthority = new XConfigAssigningAuthority();
            assigningAuthority.parse(currentNode, this);
            this.assigningAuthorities.put(assigningAuthority.getUniqueId(), assigningAuthority);
        }
    }

    /**
     * 
     * @param rootNode 
     * @param localName
     * @return
     */
    protected static ArrayList<OMElement> parseLevelOneNode(OMElement rootNode, String localName) {
        ArrayList<OMElement> al = new ArrayList<OMElement>();
        for (Iterator it = rootNode.getChildElements(); it.hasNext();) {
            OMElement child = (OMElement) it.next();
            if (child.getLocalName().equals(localName)) {
                al.add(child);
            }
        }
        return al;
    }
    /**
     * Just a test driver to exercise XConfig operations.
     *
     * @param args the command line arguments
     */
    /*
    public static void main(String[] args) {
    try {
    XConfig xconf = XConfig.getInstance();
    System.out.println("------------------------");
    System.out.println("Local Initiating Gateway = " + xconf.getHomeCommunity().getInitiatingGateway());
    System.out.println("------------------------");
    System.out.println("Local Responding Gateway = " + xconf.getHomeCommunity().getRespondingGateway());
    System.out.println("------------------------");
    System.out.println("Gateway = " + xconf.getGateway("urn:oid:1.19.6.24.109.42.1.3"));
    System.out.println("TXN = " + xconf.getGateway("urn:oid:1.19.6.24.109.42.1.3").getTransaction("CrossGatewayQuery"));
    System.out.println("------------------------");
    System.out.println("Success!");
    } catch (Exception e) {
    System.out.println(e.toString());
    }
    }*/
}
