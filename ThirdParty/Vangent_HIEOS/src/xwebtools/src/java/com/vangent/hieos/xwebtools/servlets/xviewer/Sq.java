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

package com.vangent.hieos.xwebtools.servlets.xviewer;

import com.vangent.hieos.xutil.metadata.structure.MetadataSupport;
import com.vangent.hieos.xutil.soap.Soap;

import java.util.ArrayList;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMText;
//import org.apache.axis2.AxisFault;
import org.apache.log4j.Logger;

public abstract class Sq {
	private final static Logger logger = Logger.getLogger(Sq.class);
	abstract OMElement build(ArrayList<String> ids);

	abstract String query_details();

	protected ArrayList<OMElement> build_query_wrapper(String query_id) {
		OMElement query = MetadataSupport.om_factory.createOMElement("AdhocQueryRequest", MetadataSupport.ebQns3);
		OMElement response_option = MetadataSupport.om_factory.createOMElement("ResponseOption", MetadataSupport.ebQns3);
		query.addChild(response_option);
		response_option.addAttribute("returnComposedObjects", "true", null);
		response_option.addAttribute("returnType", "LeafClass", null);
		OMElement adhoc_query = MetadataSupport.om_factory.createOMElement("AdhocQuery", MetadataSupport.ebRIMns3);
		query.addChild(adhoc_query);
		adhoc_query.addAttribute("id", query_id, null);
		ArrayList<OMElement> elements = new ArrayList<OMElement>();
		elements.add(query);
		elements.add(adhoc_query);
		return elements;
	}

	protected ArrayList<OMElement> build_objectref_query_wrapper(String query_id) {
		OMElement query = MetadataSupport.om_factory.createOMElement("AdhocQueryRequest", MetadataSupport.ebQns3);
		OMElement response_option = MetadataSupport.om_factory.createOMElement("ResponseOption", MetadataSupport.ebQns3);
		query.addChild(response_option);
		response_option.addAttribute("returnComposedObjects", "true", null);
		response_option.addAttribute("returnType", "ObjectRef", null);
		OMElement adhoc_query = MetadataSupport.om_factory.createOMElement("AdhocQuery", MetadataSupport.ebRIMns3);
		query.addChild(adhoc_query);
		adhoc_query.addAttribute("id", query_id, null);
		ArrayList<OMElement> elements = new ArrayList<OMElement>();
		elements.add(query);
		elements.add(adhoc_query);
		return elements;
	}

	protected void add_slot(ArrayList<OMElement> query, String name, String value) {
		OMElement parent = query.get(1);
		OMElement slot = MetadataSupport.om_factory.createOMElement("Slot", MetadataSupport.ebRIMns3);
		parent.addChild(slot);
		slot.addAttribute("name", name, null);
		OMElement valuelist = MetadataSupport.om_factory.createOMElement("ValueList", MetadataSupport.ebRIMns3);
		slot.addChild(valuelist);
		OMElement value_ele = MetadataSupport.om_factory.createOMElement("Value", MetadataSupport.ebRIMns3);
		valuelist.addChild(value_ele);
		OMText text = MetadataSupport.om_factory.createOMText(value);
		value_ele.addChild(text);
	}

	protected void add_slot(ArrayList<OMElement> query, String name, ArrayList<String> values) {
		OMElement parent = query.get(1);
		OMElement slot = MetadataSupport.om_factory.createOMElement("Slot", MetadataSupport.ebRIMns3);
		parent.addChild(slot);
		slot.addAttribute("name", name, null);
		OMElement valuelist = MetadataSupport.om_factory.createOMElement("ValueList", MetadataSupport.ebRIMns3);
		slot.addChild(valuelist);
		for (String value : values) {
			OMElement value_ele = MetadataSupport.om_factory.createOMElement("Value", MetadataSupport.ebRIMns3);
			valuelist.addChild(value_ele);
			OMText text = MetadataSupport.om_factory.createOMText(value);
			value_ele.addChild(text);
		}
	}

	public OMElement run(String endpoint, ArrayList<String> ids, String queryAction, String queryResultAction, boolean soap11) throws Exception {
		OMElement query = build(ids);
		logger.info("QUERY: " + query_details());
		Soap soap = new Soap();
		soap.soapCall(query, endpoint, false, true, !soap11, queryAction, queryResultAction);
		return soap.getResult();
	}

	String query_array_list(ArrayList<String> strings) {
		StringBuffer buf = new StringBuffer();
		buf.append("(");
		boolean first = true;
		for (String id : strings) {
			if ( !first ) 
				buf.append(", ");
			else
				first = false;
			buf.append("'" + id + "'");
		}
		buf.append(")");
		return buf.toString();
	}

	String query_singleton(ArrayList<String> strings) {
		return "'" + strings.get(0) + "'";
	}

}
