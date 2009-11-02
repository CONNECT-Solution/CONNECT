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
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vangent.hieos.xutil.xconfig;

import org.apache.axiom.om.OMElement;
import javax.xml.namespace.QName;
import java.util.ArrayList;

/**
 *
 * @author thumbe
 */
public class XConfigAssigningAuthority {

    private String name = "";
    private String uniqueId = "";
    private ArrayList<XConfigGateway> respondingGateways = new ArrayList<XConfigGateway>();
    private XConfigRegistry localRegistry = null;

    /**
     * Get the value of uniqueId
     *
     * @return the value of uniqueId
     */
    public String getUniqueId() {
        return uniqueId;
    }

    /**
     * Get the value of name
     *
     * @return the value of name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return
     */
    public ArrayList<XConfigGateway> getGateways()
    {
        return this.respondingGateways;
    }

    /**
     *
     * @return
     */
    public XConfigRegistry getLocalRegistry()
    {
        return this.localRegistry;
    }

    /**
     *
     * Precondition: Gateways / Registries are already known to xconf.
     *
     * @param rootNode
     */
    protected void parse(OMElement rootNode, XConfig xconf) {

        // Parse basic information about the assigning authority.
        OMElement node;
        this.name = rootNode.getAttributeValue(new QName("name"));
        node = rootNode.getFirstChildWithName(new QName("UniqueId"));
        this.uniqueId = node.getText();

        // Now, get the list of gateways that know about this authority.
        ArrayList<OMElement> gatewayList = XConfig.parseLevelOneNode(rootNode, "Gateway");
        for (OMElement currentNode : gatewayList) {
            String gatewayName = currentNode.getText();
            XConfigGateway gateway = xconf.getGatewayByName(gatewayName);
            respondingGateways.add(gateway);
        }

        // Now, get the local registry that knows about this authority.
        ArrayList<OMElement> registryList = XConfig.parseLevelOneNode(rootNode, "Registry");
        for (OMElement currentNode : registryList) {
            String registryName = currentNode.getText();
            this.localRegistry = xconf.getRegistryByName(registryName);
            break;  // Found a match ... should only have been one.
        }
    }
}
