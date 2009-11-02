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

import com.vangent.hieos.xutil.exception.ExceptionUtil;
import com.vangent.hieos.xutil.exception.XdsException;
import com.vangent.hieos.xutil.exception.XdsInternalException;
import com.vangent.hieos.xutil.metadata.structure.Metadata;
import com.vangent.hieos.xutil.metadata.structure.MetadataParser;
import com.vangent.hieos.xutil.metadata.structure.MetadataSupport;
import com.vangent.hieos.xutil.registry.OmLogger;
import com.vangent.hieos.xutil.xml.Parse;
import com.vangent.hieos.xutil.xml.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.xml.namespace.QName;
import javax.xml.parsers.FactoryConfigurationError;

import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.xpath.AXIOMXPath;

public class Linkage {
	OMElement instruction_output;
	Metadata m;

	ArrayList<OMElement> use_id = null;
	ArrayList<OMElement> use_object_ref = null;
	ArrayList<OMElement> use_xpath = null;
	ArrayList<OMElement> use_repository_unique_id = null;
	HashMap<String, String> linkage = null;   // symbol => id_value
	String repUniqueId = null;
	boolean debug = false;

	public String getRepositoryUniqueId() { return repUniqueId; }


	// m - metadata to modify based on linkage  ( if null - no modifications made)
	// instruction_output - log output - place to search previous test steps for linkage targets 
	//     - if null only previous testplans will be searched 
	// use_id - linkage specification (requests) to previous steps
	public Linkage(OMElement instruction_output, Metadata m, ArrayList<OMElement> use_id) {
		this.instruction_output = instruction_output;
		this.m = m;
		this.use_id = use_id;
	}

	// use this when need to build use_id list manually
	// For manual usage, call sequence is
	// Linkage l = new Linkage(instruction_output, m);
	// l.add_use_value(symbol1, value1);
	// l.add_use_value(symbol2, value2);
	// l.compile();
	public Linkage(OMElement instruction_output, Metadata m) {
		this.instruction_output = instruction_output;
		this.m = m;
	}

	public Linkage() {
		instruction_output = null;
		m = null;
	}

	public Linkage(OMElement instruction_output) {
		this.instruction_output = instruction_output;
		m = null;
	}

	public void setUseObjectRef(ArrayList<OMElement> use_object_ref) {
		this.use_object_ref = use_object_ref;
	}

	public void setUseRepositoryUniqueId(ArrayList<OMElement> use_repository_unique_id) {
		this.use_repository_unique_id = use_repository_unique_id;
	}

	public void setUseXPath(ArrayList<OMElement> use_xpath) {
		this.use_xpath = use_xpath;
	}

	public String get_value(String id) {
		return linkage.get(id);
	}

	public void add(String symbol, String value) {
		if (linkage == null)
			linkage = new HashMap<String, String>();
		linkage.put(symbol, value);
	}

	public String get_value(String testdir, String id, String step, String section) throws XdsInternalException, XdsException {
		// when used this way, caller is interested in value of id, so use its value as the value for symbol (last parm)
		add_use_id(testdir, id, step, section, id);
		compileUseId();
		return get_value(id);
	}

	// <UseId testdir="../submit1" id="Document01" step="submit_doc"
	//     section="AssignedUids" symbol="$uid$"/>
	public void add_use_id(String testdir, String id, String step, String section, String symbol) {
		if (use_id == null)
			use_id = new ArrayList<OMElement>();
		OMElement use = MetadataSupport.om_factory.createOMElement(new QName("UseId"));
		use.addAttribute("testdir", testdir, null);
		use.addAttribute("id", id, null);
		use.addAttribute("step", step, null);
		use.addAttribute("section", section, null);
		use.addAttribute("symbol", symbol, null);
		use_id.add(use);
	}

	public void add_use_xpath(OMElement use_xpath) {
		if (this.use_xpath == null)
			this.use_xpath = new ArrayList<OMElement>();
		this.use_xpath.add(use_xpath);
	}

	public void add_use_value(String symbol, String value) {
		if (use_id == null)
			use_id = new ArrayList<OMElement>();
		OMElement use = MetadataSupport.om_factory.createOMElement(new QName("UseId"));
		use.addAttribute("symbol", symbol, null);
		use.addAttribute("value", value, null);
		use_id.add(use);

	}

	public OMElement find_instruction_output(OMElement wrapper, String target_test_step_id, 
			String target_transaction_type) throws XdsException {
		for (Iterator it=wrapper.getChildElements(); it.hasNext(); ) {
			OMElement section = (OMElement) it.next();
			if ( !section.getLocalName().equals("TestStep"))
				continue;
			if ( ! section.getAttributeValue(MetadataSupport.id_qname).equals(target_test_step_id))
				continue;
			for (Iterator it1=section.getChildElements(); it1.hasNext(); ) {
				OMElement transaction_output = (OMElement) it1.next();
				if ( target_transaction_type == null) {
					if ( !transaction_output.getLocalName().endsWith("Transaction"))
						continue;
				} else {
					if ( !transaction_output.getLocalName().equals(target_transaction_type))
						continue;
				}
				return transaction_output;
			}
		}
		return null;
	}

	public String format_section_and_step(String step_id, String section_name) {
		String section_and_step = "section " + section_name + " step " + step_id;
		return section_and_step;
	}

	public OMElement get_log_file(String test_dir)
	throws FactoryConfigurationError, XdsInternalException {
		if (debug) System.out.println("LogFileName is " + getLogFileName(test_dir));
		return Util.parse_xml(new File(getLogFileName(test_dir)));
	}

	public String getLogFileName(String test_dir) {
		return TestConfig.log_dir + test_dir + "/"  + "log.xml";
	}

	public void replace_string_in_text_and_attributes(OMElement root, String old_text, String new_text) throws XdsException {
		for (Iterator it=root.getChildElements(); it.hasNext(); ) {
			OMElement e = (OMElement) it.next();

			// text
			String text = e.getText();
			if (text.indexOf(old_text) != -1) {
				text = text.replaceAll(escape_pattern(old_text), new_text);
				e.setText(text);
			}

			// attributes
			for (Iterator ita=e.getAllAttributes(); ita.hasNext(); ) {
				OMAttribute att = (OMAttribute) ita.next();
				String value = att.getAttributeValue();
				if (value.equals(old_text)) {
					value = new_text;
					att.setAttributeValue(value);
				}
			}


			// recurse
			replace_string_in_text_and_attributes(e, old_text, new_text);
		}

	}

	String escape_pattern(String pattern) {  
		//	String new_pattern = "\\" + pattern.substring(0, pattern.length()-1) + "\\$";
		StringBuffer buf = new StringBuffer();
		for(int i=0; i<pattern.length(); i++) {
			char c = pattern.charAt(i);
			if (c == '$') {
				buf.append("\\");
				buf.append(c);
			} else buf.append(c);
		}
		return buf.toString();
	}

	public HashMap<String, String> compile() throws XdsException,
	FactoryConfigurationError, XdsInternalException {
		HashMap<String, String> result = new HashMap<String, String>();
		if ( this.use_id != null)
			result.putAll(compileUseId());
		if (this.use_object_ref != null)
			result.putAll(compileUseObjectRef());
		if (this.use_xpath != null)
			result.putAll(compileUseXPath());
		if (this.use_repository_unique_id != null)
			result.putAll(this.compileUseRepostoryUniqueId());
		return result;
	}

	HashMap<String, String> compileUseRepostoryUniqueId() throws XdsException {
		HashMap<String, String> result = new HashMap<String, String>();

		OMElement metadata_ele = (m == null) ? null : m.getRoot();

		for (OMElement use : use_repository_unique_id) {
			String testdir = use.getAttributeValue(new QName("testdir"));
			String step = use.getAttributeValue(new QName("step"));
			String symbol = use.getAttributeValue(new QName("symbol"));

			if (debug) 
				System.out.println("compileUseRepositoryUniqueId:" +
						"\ntestdir = " + testdir +
						"\nstep = " + step +
						"\nsymbol = " + symbol
				);

			Metadata m = getResult(testdir, step);
			if (m == null)
				return result;

			// this call should include the DocumentEntry.id since a SQ
			// could return DEs from multiple repositories.  To do this,
			// the id (which would be coded symbolically) would first
			// have to be converted to uuid form.
			String ruid = getRepositoryUniqueId(m);
			if (ruid == null || ruid.equals(""))
				throw new XdsInternalException("repositoryUniqueId from query result metadata is null");
			result.put(symbol, ruid);

			if (metadata_ele != null)
				replace_string_in_text_and_attributes(metadata_ele, symbol, ruid);
		}
		if(result != null)
			new OmLogger().add_name_value(instruction_output, "UseRepositoryUniqueId", result.toString());
		return result;
	}

	public Metadata getResult(String testdir, String step) throws XdsException {
		OMElement result_ele = findResultInLog(step, testdir);
		if (result_ele == null)
			return null;
		OMElement ele = result_ele.getFirstElement();
		Metadata m = MetadataParser.parseNonSubmission(ele);
		if (debug) 
			System.out.println("getResult:" + 
					"\ntestdir = " + testdir + 
					"\nstep = " + step +
					"\nresult =  " + result_ele.getLocalName() +
					"\nele = " + ((ele == null) ? "null" : ele.getLocalName()) +
					"\nmetadata is " + m.getMetadataDescription());
		return m;
	}

	String getRepositoryUniqueId(Metadata m) throws XdsInternalException {
		repUniqueId =  null;
		for (OMElement eo : m.getExtrinsicObjects()) {
			String rui = m.getSlotValue(eo, "repositoryUniqueId", 0);
			if (debug) 
				System.out.println("eo = " + eo.getAttributeValue(MetadataSupport.id_qname) +
						" repositoryUniqueId = " + rui);
			if (rui == null || rui.equals(""))
				throw new XdsInternalException("RetrieveTransaction: getRepositoryUniqueId(): ExtrinsicObject " + 
						eo.getAttributeValue(MetadataSupport.id_qname) + 
				"does not have a repositoryUniqueId attribute");
			if (repUniqueId == null)
				repUniqueId = rui;
			else if (!rui.equals(repUniqueId)) {
				throw new XdsInternalException("RetrieveTransaction: getRepositoryUniqueId(): this metadata contains multiple repositorUniqueIds, this tool is not able to deal with this configuration");
			}
		}
		return repUniqueId;
	}



	HashMap<String, String> compileUseXPath() throws XdsException {
		// symbol => id_value
		linkage = new HashMap<String, String>();

		OMElement metadata_ele = (m == null) ? null : m.getRoot();
		for (int i=0; i<use_xpath.size(); i++) {
			OMElement use = (OMElement) use_xpath.get(i);
			String testdir = use.getAttributeValue(new QName("testdir"));
			String step = use.getAttributeValue(new QName("step"));
			String xpath = use.getText();
			String file = use.getAttributeValue(new QName("file"));
			String symbol = use.getAttributeValue(new QName("symbol"));

			boolean is_testdir = 
				(testdir != null && !testdir.equals("") &&
						step != null && !step.equals("") &&
						xpath != null && !xpath.equals("") &&
						symbol != null && !symbol.equals(""));
			boolean is_mgmt =
				(file != null && !file.equals("") &&
						xpath != null && !xpath.equals("")	&&
						symbol != null && !symbol.equals(""));

			if (is_testdir == false && is_mgmt == false )
				throw new XdsException(": <UseXPath element must contain of these sets of attributes:\n" +
						"testdir, step, symbol, with the body of the element holding the xpath OR\n" +
				"file, symbol, with the body of the element holding the xpath");
			try {
				if (is_testdir) {
					OMElement root = Parse.parse_xml_file(getLogFileName(testdir));
					ArrayList<OMElement> test_steps = MetadataSupport.decendentsWithLocalName(root, "TestStep");
					OMElement step_ele = null;
					for (OMElement test_step : test_steps) {
						String step_id = test_step.getAttributeValue(new QName("id"));
						if (step.equals(step_id)) {
							step_ele = test_step;
							break;
						}
					}
					if (step_ele == null)
						throw new XdsException("Linkage:compileUseXPath(): Cannot find TestStep " + step + " in " + getLogFileName(testdir));

					AXIOMXPath xpathExpression = new AXIOMXPath (xpath);
					String result = xpathExpression.stringValueOf(step_ele);
					linkage.put(symbol, result);
					if (metadata_ele != null)
						replace_string_in_text_and_attributes(metadata_ele, symbol, result);

				} else {
					if (file.contains("MGMT")) 
						file = file.replaceFirst("MGMT", TestConfig.testmgmt_dir);
					OMElement root = Parse.parse_xml_file(file);
					AXIOMXPath xpathExpression = new AXIOMXPath (xpath);
					String result = xpathExpression.stringValueOf(root);
					linkage.put(symbol, result);
					if (metadata_ele != null)
						replace_string_in_text_and_attributes(metadata_ele, symbol, result);
				}
			}
			catch (Exception e) {
				throw new XdsException("Linkage:compileUseXPath(): problem compiling xpath expression\n" + xpath + "\nunable to access referenced data:\n" + e.getMessage() + "\n" +
						ExceptionUtil.exception_details(e));
			}
		}
		if (linkage != null)
			new OmLogger().add_name_value(instruction_output, "UseXPath", linkage.toString());

		return linkage;
	}

	public HashMap<String, String> compileUseId() throws XdsException,
	FactoryConfigurationError, XdsInternalException {
		// symbol => id_value
		linkage = new HashMap<String, String>();
		OMElement metadata_ele = (m == null) ? null : m.getRoot();
		for (int i=0; i<use_id.size(); i++) {
			OMElement use = (OMElement) use_id.get(i);
			String id = use.getAttributeValue(new QName("id"));
			String step_id = use.getAttributeValue(new QName("step"));
			String section_name = use.getAttributeValue(new QName("section"));
			String symbol = use.getAttributeValue(new QName("symbol"));
			String value = use.getAttributeValue(new QName("value"));
			String test_dir = use.getAttributeValue(new QName("testdir"));
			boolean by_value = false;

			if ( symbol != null && !symbol.equals("") &&
					value != null && !value.equals(""))
				by_value = true; // ok combination
			else
				if( 
						id == null || id.equals("") ||
						step_id == null || step_id.equals("") ||
						section_name == null || section_name.equals("") ||
						symbol == null || symbol.equals("")
				) {
					throw new XdsException(": <UseId element must have id, type, and symbol attributes" +
							"\n OR  symbol and value must be set programatically "+
							"\nid = " + id +
							"\nstep = " + step_id +
							"\nsection = " + section_name +
							"\nsymbol = " + symbol +
							"\nvalue = " + value +
							"\ntestdir = " + test_dir);
				}

			OMElement transaction_output;
			if (by_value) {
				linkage.put(symbol, value);
				if (metadata_ele == null) throw new XdsException("metadata_ele is null");
				if (metadata_ele != null)
					replace_string_in_text_and_attributes(metadata_ele, symbol, value);
			}
			else {
				if (test_dir != null && test_dir.length() > 0) {
					// look in previous log file
					OMElement log = get_log_file(test_dir);
					transaction_output = find_instruction_output(log, step_id, null);
					if (transaction_output == null) {
						throw new XdsException("Linkage:CompileUseId(): " + format_section_and_step(step_id, section_name) +  
								" Transaction with step_id " + step_id + " cannot be found in log file " + getLogFileName(test_dir));
					}
				} else {
					// look in this log file
					transaction_output = null;
					if (instruction_output != null)
						transaction_output = find_previous_instruction_output(instruction_output, step_id, null);
					if (transaction_output == null) {
						throw new XdsException("Linkage compiler: cannot find transaction output in this log file for " + format_section_and_step(step_id, section_name));
					}
				}

				OMElement section = MetadataSupport.firstChildWithLocalName(transaction_output, section_name); 
				if (section == null)
					throw new XdsException(format_section_and_step(step_id, section_name) + " not found in any previous step");

				for (OMElement assign : MetadataSupport.childrenWithLocalName(section, "Assign")) {
					String symbol_value = assign.getAttributeValue(new QName("symbol"));
					String id_value = assign.getAttributeValue(new QName("id"));
					//System.out.println("symbol_value = " + symbol_value + "  id_value = " + id_value);
					if (symbol_value == null || symbol_value.equals(""))
						throw new XdsException(format_section_and_step(step_id, section_name) + " empty assign section (no symbol attribute)");
					if (id_value == null || id_value.equals(""))
						throw new XdsException(format_section_and_step(step_id, section_name) + " empty assign section (no id attribute)");
					if (! symbol_value.equals(id))
						continue;
					if (section.equals("AssignedPatientId")) {
						new TestMgmt().assignPatientId(m, id_value);
					} else {
						linkage.put(symbol, id_value);
						if (metadata_ele != null)
							replace_string_in_text_and_attributes(metadata_ele, symbol, id_value);
					}
				}
			}

		}
		if (linkage != null)
			new OmLogger().add_name_value(instruction_output, "UseId", linkage.toString());
		return linkage;
	}

	public void compileLinkage() throws XdsException {

		OMElement metadata_ele = (m == null) ? null : m.getRoot();

		for (String key : linkage.keySet()) {
			String value = linkage.get(key);
			replace_string_in_text_and_attributes(metadata_ele, key, value);
		}
	}

	public HashMap<String, String> compileUseObjectRef() throws XdsException,
	FactoryConfigurationError, XdsInternalException {
		// symbol => id_value
		linkage = new HashMap<String, String>();
		int use_id;  // just to find references
		OMElement metadata_ele = (m == null) ? null : m.getRoot();
		for (int i=0; i<use_object_ref.size(); i++) {
			OMElement use = (OMElement) use_object_ref.get(i);
			String step_id = use.getAttributeValue(new QName("step"));  
			String index = use.getAttributeValue(new QName("index"));
			String symbol = use.getAttributeValue(new QName("symbol")); 
			String test_dir = use.getAttributeValue(new QName("testdir"));  

			if ( step_id != null && !step_id.equals("") && 
					index != null && !index.equals("") &&
					symbol != null && !symbol.equals("") )
				;
			else
				throw new XdsException("<UseObjectRef element must have testdir, step, index, and symbol attributes" +
						"\nindex = " + index +
						"\nstep = " + step_id +
						"\nsymbol = " + symbol +
						"\ntestdir = " + test_dir);
			int index_i = Integer.parseInt(index);
			if (index_i < 0)
				throw new XdsException("<UseObjectRef element has index less than zero [" + index + "] - invalid index");

			OMElement transaction_output = find_transaction_in_log(step_id, test_dir);

			OMElement result = MetadataSupport.firstChildWithLocalName(transaction_output, "Result");
			if (result == null)
				throw new XdsException("Cannot find Result section in log of step " + step_id + " in test directory " + test_dir);

			Metadata m = MetadataParser.parseNonSubmission(result.getFirstElement());

			ArrayList<OMElement> object_refs = m.getObjectRefs();
			if (index_i >= object_refs.size())
				throw new XdsException("<UseObjectRef requests index of " + index_i + " but query retured only [" + object_refs.size() + "] ObjectRefs");

			String value = object_refs.get(index_i).getAttributeValue(MetadataSupport.id_qname);
			linkage.put(symbol, value);
			if (metadata_ele != null)
				replace_string_in_text_and_attributes(metadata_ele, symbol, value);
		}
		if(linkage != null)
			new OmLogger().add_name_value(instruction_output, "UseObjectRef", linkage.toString());
		return linkage;
	}

	private OMElement find_transaction_in_log(String step_id, String test_dir)
	throws FactoryConfigurationError, XdsInternalException,
	XdsException {
		OMElement transaction_output;
		if (test_dir != null && test_dir.length() > 0) {
			// look in previous log file
			OMElement log = get_log_file(test_dir);
			transaction_output = find_instruction_output(log, step_id, null);
			if (transaction_output == null) {
				throw new XdsInternalException(format_section_and_step(step_id, "any") +  
						" Transaction with step_id " + step_id + " cannot be found in log file " + getLogFileName(test_dir));
			}
		} else {
			// look in this log file
			transaction_output = null;
			if (instruction_output != null)
				transaction_output = find_previous_instruction_output(instruction_output, step_id, null);
			if (transaction_output == null) {
				throw new XdsException(format_section_and_step(step_id, "any") + " Transaction not found in any previous step");
			}
		}
		return transaction_output ;
	}

	public OMElement findResultInLog(String step_id, String test_dir) throws FactoryConfigurationError, XdsInternalException,
	XdsException {

		OMElement transaction_output = find_transaction_in_log(step_id, test_dir);

		if (transaction_output == null) 
			throw new XdsInternalException("Linkage:findResultInLog(): Cannot find *Transaction in log of step " + step_id + " in " + test_dir + "/log.xml");

		OMElement result = MetadataSupport.firstChildWithLocalName(transaction_output, "Result");
		if (result == null) 
			throw new XdsInternalException("Linkage:findResultInLog(): Cannot find Result in log of step " + step_id + " in " + test_dir + "/log.xml");

		if (debug) 
			System.out.println("findResultInLog\n" + result.toString());

		return result;
	}

	protected OMElement find_previous_instruction_output(OMElement this_instruction_output, String target_test_step_id, String target_transaction_type) throws XdsException {
		// example target_transaction_type is "RegisterTransaction"
		//System.out.println("Searching for step id=" + target_test_step_id + " and transaction type " + target_transaction_type);
		OMElement this_step_output = (OMElement) this_instruction_output.getParent();
		OMElement step_output = null;
		step_output = (OMElement) this_step_output.getPreviousOMSibling();
		while (step_output != null) {
			String step_output_id = step_output.getAttributeValue(new QName("id"));
			if (step_output_id != null && step_output_id.equals(target_test_step_id)) {
				OMElement transaction_output = (target_transaction_type == null)  
				? MetadataSupport.firstChildWithLocalNameEndingWith(step_output, "Transaction")
						: MetadataSupport.firstChildWithLocalName(step_output, target_transaction_type) ;
				return transaction_output;
			}
			step_output = (OMElement) step_output.getPreviousOMSibling();
		}
		return null;
	}




}
