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

public class TranslateToV2 extends Translate {
	
	public OMElement translate(OMElement ro2, boolean must_dup) throws XdsInternalException {
		if (MetadataSupport.isV2Namespace(ro2.getNamespace()) && !must_dup) 
			return Util.deep_copy(ro2);
		return  deep_copy(ro2, MetadataSupport.ebRIMns2);
	}
	
	enum Att { Name, Description, Slot, Classification, ExternalIdentifier };

	OMElement deep_copy(OMElement from, OMNamespace new_namespace) {
		OMElement to = MetadataSupport.om_factory.createOMElement(from.getLocalName(), new_namespace);

		copy_attributes(from, to);
		OMElement x;
		
		for (Att att : Att.values()) {
			String att_name = att.name();
			for (Iterator it=from.getChildElements(); it.hasNext(); ) {
				OMElement child = (OMElement) it.next();
				if (child.getLocalName().equals(att_name)) {
					to.addChild(deep_copy(child, new_namespace));
				}
			}
		}


		for (Iterator it=from.getChildElements(); it.hasNext(); ) {
			x = (OMElement) it.next();

			if (x.getLocalName().equals("Name")) continue;
			if (x.getLocalName().equals("Description")) continue;
			if (x.getLocalName().equals("Slot")) continue;
			if (x.getLocalName().equals("Classification")) continue;
			if (x.getLocalName().equals("ExternalIdentifier")) continue;
			if (x.getLocalName().equals("ObjectRef")) continue;
			
			//System.out.println("deep_copy(): also copying a " + x.getLocalName());

			to.addChild(deep_copy(x, new_namespace));
		}

		String text = from.getText();
		to.setText(text);

		return to;
	}
	
	String last_part(String value, String separator) {
		String[] parts = value.split(separator);
		if (parts.length == 0)
			return value;
		return parts[parts.length-1];
	}
	
	String translate_att(String value) {
		value = last_part(value, ":");
		return value;
	}

	protected void copy_attributes(OMElement from, OMElement to) {
		String element_name = from.getLocalName();
		for (Iterator it=from.getAllAttributes(); it.hasNext();  ) {
			OMAttribute from_a = (OMAttribute) it.next();
			String name = from_a.getLocalName();
			String value = from_a.getAttributeValue();
			OMNamespace xml_namespace = MetadataSupport.xml_namespace;
			OMNamespace namespace = null;
			if (name.endsWith("status"))
				value = translate_att(value);
			else if (name.endsWith("associationType"))
				value = translate_att(value);
			else if (name.equals("minorVersion"))
				continue;
			else if (name.equals("majorVersion"))
				continue;
			else if (name.equals("lang"))
				namespace = xml_namespace;
			else if (name.equals("objectType") && value.startsWith("urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:")) {
				String[] parts = value.split(":");
				value = parts[parts.length-1];
			}
//			else if (name.equals("objectType") && ! value.startsWith("urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:"))
//			value = "urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:" + value;
			OMAttribute to_a = MetadataSupport.om_factory.createOMAttribute(name, namespace, value);
			to.addAttribute(to_a);
		}
		
	}

	
//	protected void copy_attributes(OMElement from, OMElement to) {
//		for (Iterator it=from.getAllAttributes(); it.hasNext();  ) {
//			OMAttribute from_a = (OMAttribute) it.next();
//			String name = from_a.getLocalName();
//			String value = from_a.getAttributeValue();
//			OMAttribute to_a = MetadataSupport.om_factory.createOMAttribute(name, null, value);
//			to.addAttribute(to_a);
//		}
//	}


}
