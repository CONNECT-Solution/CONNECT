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

import com.vangent.hieos.xutil.exception.XdsInternalException;
import com.vangent.hieos.xutil.xml.Util;

import java.util.Iterator;

import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNamespace;

public class TranslateToV3 extends Translate {
	int id_count = 1;
	Metadata m;

	public OMElement translate(OMElement ro2, boolean must_dup) throws XdsInternalException {
		if (MetadataSupport.isV3Namespace(ro2.getNamespace()) && !must_dup) 
			return Util.deep_copy(ro2);
		m = new Metadata();
		return deep_copy(ro2, MetadataSupport.ebRIMns3);
	}
 
	String new_id() {
		return "id_" + id_count++;
	}

	OMElement add_id(OMElement ele) {
		String att_value = ele.getAttributeValue(id_qname);
		if (att_value == null || att_value.equals(""))
			ele.addAttribute("id", new_id(), null);
		return ele;
	}

	enum Att { Slot, Name, Description, VersionInfo, Classification, ExternalIdentifier };

	
	OMElement deep_copy(OMElement from, OMNamespace new_namespace) {
		String to_id = from.getAttributeValue(id_qname);
		String to_name = from.getLocalName();
				
		if (to_name.equals("ObjectRef"))
			return null;


		OMElement to = MetadataSupport.om_factory.createOMElement(from.getLocalName(), new_namespace);

		copy_attributes(from, to);

		if (to_name.equals("Association"))
			add_id(to);

		if (to_name.equals("Classification"))
			add_id(to);

		for (Att att : Att.values()) {
			String att_name = att.name();
			for (Iterator it=from.getChildElements(); it.hasNext(); ) {
				OMElement child = (OMElement) it.next();
				if (child.getLocalName().equals(att_name)) {
					OMElement newx = deep_copy(child, new_namespace);
					if (att_name.equals("ExternalIdentifier")) {
						add_id(newx);
						newx.addAttribute("registryObject", to_id, null);
					}
					to.addChild(newx);
				}
			}
		}

			
		OMElement x;

		for (Iterator it=from.getChildElements(); it.hasNext(); ) {
			x = (OMElement) it.next();

			if (x.getLocalName().equals("Name")) continue;
			if (x.getLocalName().equals("Description")) continue;
			if (x.getLocalName().equals("Slot")) continue;
			if (x.getLocalName().equals("VersionInfo")) continue;
			if (x.getLocalName().equals("Classification")) continue;
			if (x.getLocalName().equals("ExternalIdentifier")) continue;
			if (x.getLocalName().equals("ObjectRef")) continue;

			to.addChild(deep_copy(x, new_namespace));
		}

		String text = from.getText();
		to.setText(text);

		return to;
	}
	
	
	protected void copy_attributes(OMElement from, OMElement to) {
		String element_name = from.getLocalName();
		for (Iterator it=from.getAllAttributes(); it.hasNext();  ) {
			OMAttribute from_a = (OMAttribute) it.next();
			String name = from_a.getLocalName();
			String value = from_a.getAttributeValue();
			OMNamespace xml_namespace = MetadataSupport.xml_namespace;
			OMNamespace namespace = null;
			if (name.equals("status"))
				value = m.addNamespace(value, MetadataSupport.status_type_namespace);
			else if (name.equals("associationType"))
				value = m.addNamespace(value, m.v3AssociationNamespace(value));
			else if (name.equals("minorVersion"))
				continue;
			else if (name.equals("majorVersion"))
				continue;
			else if (name.equals("lang"))
				namespace = xml_namespace;
//			else if (name.equals("objectType") && ! value.startsWith("urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:"))
//			value = "urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:" + value;
			OMAttribute to_a = MetadataSupport.om_factory.createOMAttribute(name, namespace, value);
			to.addAttribute(to_a);
		}
		if (element_name.equals("RegistryPackage")) {
			OMAttribute object_type_att = to.getAttribute(MetadataSupport.object_type_qname);
			if (object_type_att == null) {
				object_type_att = MetadataSupport.om_factory.createOMAttribute("objectType", null, "urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:RegistryPackage");
				to.addAttribute(object_type_att);
			} else {
				object_type_att.setAttributeValue("urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:RegistryPackage");
			}
		}
		else if (element_name.equals("Association")) {
			OMAttribute object_type_att = to.getAttribute(MetadataSupport.object_type_qname);
			if (object_type_att == null) {
				object_type_att = MetadataSupport.om_factory.createOMAttribute("objectType", null, "urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:Association");
				to.addAttribute(object_type_att);
			} else {
				object_type_att.setAttributeValue("urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:Association");
			}
		}
		else if (element_name.equals("Classification")) {
			OMAttribute object_type_att = to.getAttribute(MetadataSupport.object_type_qname);
			if (object_type_att == null) {
				object_type_att = MetadataSupport.om_factory.createOMAttribute("objectType", null, "urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:Classification");
				to.addAttribute(object_type_att);
			} else {
				object_type_att.setAttributeValue("urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:Classification");
			}
		}
		else if (element_name.equals("ExternalIdentifier")) {
			OMAttribute object_type_att = to.getAttribute(MetadataSupport.object_type_qname);
			if (object_type_att == null) {
				object_type_att = MetadataSupport.om_factory.createOMAttribute("objectType", null, "urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ExternalIdentifier");
				to.addAttribute(object_type_att);
			} else {
				object_type_att.setAttributeValue("urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ExternalIdentifier");
			}
		}
		else if (element_name.equals("ObjectRef")) {
//			OMAttribute object_type_att = to.getAttribute(MetadataSupport.object_type_qname);
//			if (object_type_att == null) {
//				object_type_att = MetadataSupport.om_factory.createOMAttribute("objectType", null, "urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ObjectRef");
//				to.addAttribute(object_type_att);
//			} else {
//				object_type_att.setAttributeValue("urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ObjectRef");
//			}
		}
	}

}
