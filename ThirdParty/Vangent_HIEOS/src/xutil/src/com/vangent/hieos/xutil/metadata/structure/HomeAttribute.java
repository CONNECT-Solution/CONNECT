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

package com.vangent.hieos.xutil.metadata.structure;

import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNode;

public class HomeAttribute {

    String home;
    String errs = "";

    public HomeAttribute(String home) {
        this.home = home;
    }

    boolean isIdentifiable(String name) {
        return name.equals("ObjectRef") ||
                name.equals("ExtrinsicObject") ||
                name.equals("RegistryPackage") ||
                name.equals("ExternalIdentifier") ||
                name.equals("Association") ||
                name.equals("Classification");

    }

    public void set(OMElement root) {
        String localname = root.getLocalName();

        if (isIdentifiable(localname)) {
            OMAttribute home_att = root.getAttribute(MetadataSupport.home_qname);
            if (home_att != null) {
                // Override any existing value.
                home_att.setAttributeValue(home);
            } else {
                root.addAttribute("home", home, null);
            }
        }

        for (OMNode child = root.getFirstElement(); child != null; child = child.getNextOMSibling()) {
            if(child instanceof OMElement){  // FIXME (BHT): Not sure why had to change with OMAR. Need to invesetigate.
                OMElement child_e = (OMElement) child;
                set(child_e);
            }
        }
    }

    public String validate(OMElement root) {
        errs = "";
        validate1(root);
        return errs;
    }

    public void validate1(OMElement root) {
        String localname = root.getLocalName();

        if (isIdentifiable(localname)) {

            OMAttribute home_att = root.getAttribute(MetadataSupport.home_qname);

            if (home_att == null) {
                errs += "\nElement of type " + localname + " does not contain a home attribute";
            } else {
                String home1 = home_att.getAttributeValue();
                if (home1 == null) {
                    home1 = "";
                }
                if (!home1.equals(home)) {
                    errs += "\nElement of type " + localname + " has home of [" + home1 + "] which does not match expected value of [" + home + "]";
                }
            }
        }

        for (OMNode child = root.getFirstElement(); child != null; child = child.getNextOMSibling()) {
            OMElement child_e = (OMElement) child;
            validate1(child_e);
        }
    }
}
