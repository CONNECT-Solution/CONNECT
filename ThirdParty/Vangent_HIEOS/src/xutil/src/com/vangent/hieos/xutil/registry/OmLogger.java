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

package com.vangent.hieos.xutil.registry;


import com.vangent.hieos.xutil.metadata.structure.MetadataSupport;
import com.vangent.hieos.xutil.xml.Util;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNode;

public class OmLogger {

	public OMElement add_simple_element(OMElement parent, String name) {
		OMElement ele = MetadataSupport.om_factory.createOMElement(name, null);
		parent.addChild(ele);
		return ele;
	}

	public OMElement add_simple_element_with_id(OMElement parent, String name,
			String id) {
		OMElement ele = add_simple_element(parent, name);
		ele.addAttribute("id", id, null);
		return ele;
	}

	public OMElement add_simple_element_with_id(OMElement parent, String name,
			String id, String value) {
		OMElement ele = add_simple_element(parent, name);
		ele.addAttribute("id", id, null);
		ele.addChild(MetadataSupport.om_factory.createOMText(value));
		return ele;
	}

	public OMElement add_name_value(OMElement parent, String name, String value) {
		OMElement ele = MetadataSupport.om_factory.createOMElement(name, null);
		ele.addChild(MetadataSupport.om_factory.createOMText(value));
		parent.addChild(ele);
		return ele;
	}

	public OMElement add_name_value_with_id(OMElement parent, String name, String id, String value) {
		OMElement ele = MetadataSupport.om_factory.createOMElement(name, null);
		ele.addAttribute("id", id, null);
		ele.addChild(MetadataSupport.om_factory.createOMText(value));
		parent.addChild(ele);
		return ele;
	}

	public OMElement add_name_value(OMElement parent, String name, OMElement value) {
		OMNode val = value;
		OMElement ele = MetadataSupport.om_factory.createOMElement(name, null);
		if (val == null)
			val = MetadataSupport.om_factory.createOMText("null");
		else {
			try {
				val = Util.deep_copy(value);
			} catch (Exception e) {}
		}
		ele.addChild(val);
		parent.addChild(ele);
		return ele;
	}

	public OMElement add_name_value(OMElement parent, String name, OMElement value1, OMElement value2) {
		OMElement ele = MetadataSupport.om_factory.createOMElement(name, null);
		OMNode val1 = value1;
		if (val1 == null)
			val1 = MetadataSupport.om_factory.createOMText("null");
		ele.addChild(val1);
		OMNode val2 = value2;
		if (val2 == null)
			val2 = MetadataSupport.om_factory.createOMText("null");
		ele.addChild(val2);
		parent.addChild(ele);
		return ele;
	}

	public OMElement add_name_value(OMElement parent, String name, OMElement value1, OMElement value2, OMElement value3) {
		OMElement ele = MetadataSupport.om_factory.createOMElement(name, null);
		OMNode val1 = value1;
		if (val1 == null)
			val1 = MetadataSupport.om_factory.createOMText("null");
		ele.addChild(val1);
		OMNode val2 = value2;
		if (val2 == null)
			val2 = MetadataSupport.om_factory.createOMText("null");
		ele.addChild(val2);
		OMNode val3 = value3;
		if (val3 == null)
			val3 = MetadataSupport.om_factory.createOMText("null");
		ele.addChild(val3);
		parent.addChild(ele);
		return ele;
	}

	public OMElement create_name_value(String name, OMElement value) {
		OMNode val = value;
		OMElement ele = MetadataSupport.om_factory.createOMElement(name, null);
		if (val == null)
			val = MetadataSupport.om_factory.createOMText("null");
		ele.addChild(val);
		return ele;
	}

	public OMElement create_name_value(String name, String value) {
		OMElement ele = MetadataSupport.om_factory.createOMElement(name, null);
		ele.addChild(MetadataSupport.om_factory.createOMText(value));
		return ele;
	}

	public OMElement add_name_value(OMElement parent, OMElement element) {
		parent.addChild(element);
		return element;
	}

}
