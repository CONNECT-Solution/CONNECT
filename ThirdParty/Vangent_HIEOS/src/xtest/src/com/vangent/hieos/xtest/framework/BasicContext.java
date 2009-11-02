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

package com.vangent.hieos.xtest.framework;

import com.vangent.hieos.xutil.exception.MetadataValidationException;
import com.vangent.hieos.xutil.exception.XdsInternalException;
import com.vangent.hieos.xutil.metadata.structure.MetadataSupport;
import com.vangent.hieos.xutil.registry.OmLogger;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMElement;

public class BasicContext  extends OmLogger {
	BasicContext parent_context;
	HashMap atts;
	
	public BasicContext(BasicContext parent_context) {
		this.parent_context = parent_context;
		atts = null;
	}
	
	public void set(String attname, Object attvalue) {
		if (atts == null) atts = new HashMap();
		atts.put(attname, attvalue);
	}
	
	public Object getObj(String attname) {
		if (atts == null) atts = new HashMap();
		Object value = atts.get(attname);
		if (value != null)
			return value;
		if (parent_context != null)
			return parent_context.getObj(attname);
		return null;
	}
	
	public String get(String attname) {
		return (String) getObj(attname);
	}
	
	public PlanContext getPlan() {
		if (this instanceof PlanContext) 
			return (PlanContext) this;
		if (parent_context != null)
			return parent_context.getPlan();
		return null;
	}
	
	public String getRecursive(String attname) {
		String value = get(attname);
		//System.out.println("getRecursive (" + attname + ") : " + getClass().getName() + ": " + value);
		if (value != null) return value;
		if (parent() != null) {
			return parent().getRecursive(attname);
		}
		return null;
	}
	
	public void dumpContext() {
		System.out.println(getClass().getName());
		if (atts == null)
			return;
		Set keys = atts.keySet();
		for (Iterator it=keys.iterator(); it.hasNext(); ) {
			String key = (String) it.next();
			Object value = atts.get(key);
			System.out.println("    " + key + " : " + value.toString() );
		}
	}
	
	public void dumpContextRecursive() {
		dumpContext();
		if (parent() != null) 
			parent().dumpContextRecursive();
	}
	
	public void dumpContextIntoOutput(OMElement output) {
		OMElement context = MetadataSupport.om_factory.createOMElement(new QName("Context"));
		context.addAttribute("class", getClass().getName(), null);
		output.addChild(context);
		if (atts != null) {
			Set keys = atts.keySet();
			for (Iterator it=keys.iterator(); it.hasNext(); ) {
				String key = (String) it.next();
				Object value = atts.get(key);
				OMElement att_ele = MetadataSupport.om_factory.createOMElement(new QName(key));
				att_ele.addChild(MetadataSupport.om_factory.createOMText(value.toString()));
				context.addChild(att_ele);
			}
		}
		if (parent() != null) {
			parent().dumpContextIntoOutput(context);
		}
	}
	
	public BasicContext parent() { return parent_context; }


	private void fatal_error(String msg) throws XdsInternalException {
		dumpContextRecursive();
		throw new XdsInternalException(msg);
	}

	void metadata_validation_error(String msg) throws MetadataValidationException {
		throw new MetadataValidationException(msg);
	}

	String error(String msg) {
		String out = "Error: stepId=" + get("step_id") + " : " + msg;
		System.out.println(out);
		return out;
	}

	void error(OMElement test_step_output, String msg) {
		add_name_value(test_step_output, "Error", msg);
		error(msg);
	}

	OMElement build_results_document() {
		OMElement result = MetadataSupport.om_factory.createOMElement("TestResults", null);
		return result;
	}


	
}
