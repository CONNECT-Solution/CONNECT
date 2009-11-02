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

//import java.lang.StringBuffer;
import javax.xml.namespace.QName;
import org.apache.axiom.om.OMElement;

/**
 *
 * @author Bernie Thuman
 */
public class XConfigTransaction {

    private String endpointURL = "";
    private boolean secureEndpoint;
    private String name = "";

    /**
     * Get the value of name
     *
     * @return the value of name
     */
    public String getName() {
        return name;
    }

    /**
     * Get the value of secureEndpoint
     *
     * @return the value of secureEndpoint
     */
    public boolean isSecureEndpoint() {
        return secureEndpoint;
    }
    ;

    /**
     * Get the value of endPointURL
     *
     * @return the value of endPointURL
     */
    public String getEndpointURL() {
        return endpointURL;
    }

    /**
     *
     * @param rootNode
     */
    protected void parse(OMElement rootNode) {
        this.name = rootNode.getAttributeValue(new QName("name"));
        this.endpointURL = rootNode.getText();
        // A bit of a hack, but determine secure endpoints if https.
        this.secureEndpoint = this.endpointURL.startsWith("https");
    }

    /**
     * 
     * @return
     */
    public String toString() {
        StringBuffer sbuf = new StringBuffer();
        sbuf.append("\n Transaction:");
        sbuf.append("\n   name: " + this.getName());
        sbuf.append("\n   endpointURL: " + this.getEndpointURL());
        sbuf.append("\n   secureEndpoint: " + (this.isSecureEndpoint() ? "true" : "false"));
        return sbuf.toString();
    }
}
