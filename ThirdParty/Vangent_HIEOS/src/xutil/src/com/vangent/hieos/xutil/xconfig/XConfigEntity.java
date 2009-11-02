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

import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.axiom.om.OMElement;

/**
 * Mini framework for Entity types defined in XDS.b/XCA configuration file.  Subclasses can implement
 * the "parseSpecializedParts()" method.
 *
 * @author Bernie Thuman
 */
abstract public class XConfigEntity {

    private String name = "";
    private String uniqueId = "";
    private boolean useSecureEndpoints = true;
    private ArrayList<XConfigTransaction> transactions = new ArrayList<XConfigTransaction>();
    private XConfigProperties properties = new XConfigProperties();

    /**
     *
     * @param propKey
     * @return
     */
    public String getProperty(String propKey) {
        return (String) properties.getProperty(propKey);
    }

    /**
     *
     * @param propKey
     * @return
     */
    public boolean getPropertyAsBoolean(String propKey) {
        return properties.getPropertyAsBoolean(propKey);
    }

    /**
     * Get the value of transactions
     *
     * @return the value of transactions
     */
    public ArrayList<XConfigTransaction> getTransactions() {
        return transactions;
    }

    /**
     * 
     * @param txnName
     * @return
     */
    public XConfigTransaction getTransaction(String txnName) {
        XConfigTransaction txn = null;
        for (Iterator it = transactions.iterator(); (txn == null) && it.hasNext();) {

            XConfigTransaction current = (XConfigTransaction) it.next();

            // Did we find a transaction that matches?
            if (current.getName().equals(txnName)) {

                // Are we using secure end points?
                if (this.useSecureEndpoints) {

                    // Is this the secure endpoint for the transaction?
                    if (current.isSecureEndpoint()) {
                        txn = current;  // MATCH
                    }
                } else {  // We are not using secure end points.

                    // Is this the non-secure endpoint for the transaction?
                    if (!current.isSecureEndpoint()) {
                        txn = current;  // MATCH
                    }
                }
            }
        }
        return txn;
    }

    /**
     * Get the value of uniqueId
     *
     * @return the value of uniqueId
     */
    public String getUniqueId() {
        return uniqueId;
    }

    /**
     * Get the value of useSecureEndpoints
     *
     * @return the value of useSecureEndpoints
     */
    public boolean useSecureEndpoints() {
        return useSecureEndpoints;
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
     * @param rootNode
     */
    protected void parse(OMElement rootNode, XConfig xconf) {
        // Do local parsing and then let the subclass do the rest.
        OMElement node;
        this.name = rootNode.getAttributeValue(new QName("name"));
        node = rootNode.getFirstChildWithName(new QName("UniqueId"));
        this.uniqueId = node.getText();
        node = rootNode.getFirstChildWithName(new QName("UseSecureEndpoints"));
        if (node != null) {
            this.useSecureEndpoints = (node.getText().equalsIgnoreCase("true")) ? true : false;
        }

        parseTransactions(rootNode);
        properties.parse(rootNode);
        parseSpecializedParts(rootNode, xconf);
    }

    /**
     *
     * @param rootNode
     */
    private void parseTransactions(OMElement rootNode) {
        ArrayList<OMElement> list = XConfig.parseLevelOneNode(rootNode, "Transaction");
        for (OMElement currentNode : list) {
            XConfigTransaction txn = new XConfigTransaction();
            txn.parse(currentNode);
            this.transactions.add(txn);
        }
    }

    /**
     *
     * @param rootNode
     */
    abstract protected void parseSpecializedParts(OMElement rootNode, XConfig xconf);
}
