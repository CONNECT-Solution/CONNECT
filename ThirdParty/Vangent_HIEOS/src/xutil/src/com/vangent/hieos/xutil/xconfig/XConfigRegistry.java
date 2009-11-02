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

import java.util.ArrayList;
import java.util.Iterator;
import org.apache.axiom.om.OMElement;

/**
 *
 * @author thumbe
 */
public class XConfigRegistry extends XConfigEntity {

    /**
     *
     * @param rootNode
     */
    protected void parseSpecializedParts(OMElement rootNode, XConfig xconf) {
    }

    /**
     * 
     * @return
     */
    public String toString() {
        StringBuffer sbuf = new StringBuffer();
        sbuf.append("\n Registry:");
        sbuf.append("\n   name: " + this.getName());
        sbuf.append("\n   uniqueId: " + this.getUniqueId());
        ArrayList<XConfigTransaction> al = this.getTransactions();
        for (Iterator it = al.iterator(); it.hasNext(); ) {
            XConfigTransaction txn = (XConfigTransaction) it.next();
            sbuf.append(txn.toString());
        }
        return sbuf.toString();
    }
}
