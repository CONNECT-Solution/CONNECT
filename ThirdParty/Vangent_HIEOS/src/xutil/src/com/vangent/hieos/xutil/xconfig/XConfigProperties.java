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

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMAttribute;
import javax.xml.namespace.QName;

/**
 *
 * @author Bernie Thuman
 */
public class XConfigProperties {

    private HashMap properties = new HashMap();

    /**
     *
     * @param propKey
     * @return
     */
    public String getProperty(String propKey) {
        // Convert to lower case to avoid case sensitivity.
        return (String) properties.get(propKey.toLowerCase());
    }

    /**
     * 
     * @param propKey
     * @return
     */
    public boolean getPropertyAsBoolean(String propKey) {
        return this.getProperty(propKey).equals("true") ? true : false;
    }

    /**
     *
     * @param rootNode
     */
    protected void parse(OMElement rootNode) {
        ArrayList<OMElement> list = XConfig.parseLevelOneNode(rootNode, "Property");
        for (OMElement currentNode : list) {
            OMAttribute attribute = currentNode.getAttribute(new QName("name"));
            String propKey = attribute.getAttributeValue();
            String propValue = currentNode.getText();
            // Convert to lower case to avoid case sensitivity.
            properties.put(propKey.toLowerCase(), propValue);
        }
    }
}
