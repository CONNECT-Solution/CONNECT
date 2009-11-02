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

import com.vangent.hieos.xutil.exception.MetadataValidationException;
import com.vangent.hieos.xutil.exception.XdsInternalException;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import javax.xml.namespace.QName;
import org.apache.axiom.om.OMElement;
import org.apache.log4j.Logger;

public class ParamParser {
	private final static Logger logger = Logger.getLogger(ParamParser.class);

	QName name_qname = new QName("name");
	QName valuelist_qname = new QName("ValueList");

	public HashMap<String, Object> parse(OMElement query)  throws MetadataValidationException, XdsInternalException {
		HashMap<String, Object> parms = new HashMap<String, Object>();

		for (Iterator it=query.getChildElements(); it.hasNext(); ) {
			OMElement child1 = (OMElement) it.next();
			if (child1.getLocalName().equals("AdhocQuery")) {
				ArrayList names = new ArrayList();
				for (Iterator it2=child1.getChildElements(); it2.hasNext(); ) {
					OMElement slot = (OMElement) it2.next();
					String name = parse_slot(slot, parms);
					if (names.contains(name)) {
						//throw new MetadataValidationException("Parameter " + name + " is defined in multiple Slots");
					} else
						names.add(name);
				}
			}
		}

		return parms;
	}

	void add_parm(HashMap<String, Object> parms, String name, Object value) throws XdsInternalException {
		Object existing_value = parms.get(name);
		if (existing_value == null) {
			parms.put(name, value);
		} else if (existing_value instanceof Integer || existing_value instanceof String) {
			ArrayList a = new ArrayList(2);
			a.add(existing_value);
			a.add(value);
			parms.put(name, a);
		} else if (existing_value instanceof ArrayList) {
			ArrayList a = (ArrayList) existing_value;
			if (value instanceof ArrayList) 
				a.addAll((ArrayList) value);
			else
				a.add(value);
		} else {
			throw new XdsInternalException("Stored Query parameter parser: add_parm: existing_value is of type " + existing_value.getClass().getName());
		}
	}

	String parse_slot(OMElement slot, HashMap<String, Object> parms) throws MetadataValidationException, XdsInternalException {
		String name = slot.getAttributeValue(name_qname);
		
		ArrayList<Object> newHome;
		if (parms.containsKey(name)) {
			// this is not first slot with this name - AND logic applies here
			Object value = parms.get(name);
			if (value instanceof And) {
				// existing And - add to it
				And and = (And) value;
				newHome = new ArrayList<Object>();
				and.add(newHome);
			} else if (value instanceof ArrayList) {
				// create And
				And and = new And();
				and.add(value);
				newHome = new ArrayList<Object>();
				and.add(newHome);
				parms.put(name, and);  // replace old ArrayList with And
			} else {
				throw new XdsInternalException("ParamParser:parse_slot(): unknown data type in parms database: " + 
						value.getClass().getName() + 
						" found while parsing slot " +
						slot.toString());
			}
		} else {
			newHome = null;
			parms.put(name, newHome);
		}
		
		OMElement value_list = MetadataSupport.firstChildWithLocalName(slot, "ValueList"); 
		for (Iterator it=value_list.getChildElements(); it.hasNext(); ) {
			OMElement value_element = (OMElement) it.next();
			if (!value_element.getLocalName().equals("Value"))
				continue;
			String value_string = value_element.getText();
			try {
				Integer value_int = Integer.decode(value_string);
				//add_parm(parms, name, value_int);
				
				if (newHome == null) parms.put(name, value_int);
				else newHome.add(value_int);
				
				continue;
			} catch (NumberFormatException e) {
			}
			
			// date strings are technically numeric but too large to be parsed as integers
			try {
				BigInteger value_int = new BigInteger(value_string);
				//add_parm(parms, name, value_int);
				
				if (newHome == null) parms.put(name, value_int);
				else newHome.add(value_int);
				
				continue;
			} catch (NumberFormatException e) {
			}
			
			if (value_string.charAt(0) == '\'' &&
					value_string.charAt(value_string.length()-1) == '\'') {
				String val = value_string.substring(1, value_string.length()-1);
				//add_parm(parms, name, val);
				
				if (newHome == null) parms.put(name, val);
				else newHome.add(val);
				
				continue;
			}
			if (value_string.charAt(0) == '(' &&
					value_string.charAt(value_string.length()-1) == ')') {
				String val_list = value_string.substring(1, value_string.length()-1);
				String[] vals = val_list.split(",");
				//ArrayList a = new ArrayList();
				for (int i=0; i<vals.length; i++ ) {
					String value_string1 = vals[i].trim();
					if (value_string1 == null || value_string1.length() == 0)
						throw new MetadataValidationException("Error decoding Slot " + name + " - empty value");
					// each value could be 'string' or int
					if (value_string1.charAt(0) == '\'' &&
							value_string1.charAt(value_string1.length()-1) == '\'') {
						String v = value_string1.substring(1, value_string1.length()-1);
						if (v.indexOf("'") != -1)
							throw new MetadataValidationException("Could not decode the value " + 
									value_string1 + " of Slot " + name + " as part of Stored Query parameter value " +
									value_string);
						if (newHome == null) {
							newHome = new ArrayList<Object>();
							parms.put(name, newHome);  
						}
						newHome.add(v);
					} else {
						try {
							Integer value_int = Integer.decode(value_string1);
							newHome.add(value_int);
						} catch (NumberFormatException e) {
							throw new MetadataValidationException("Could not decode the value " + 
									value_string1 + " of Slot " + name + " as part of Stored Query parameter value " +
									value_string);
						}
					}
				}
				//add_parm(parms, name, a);
				continue;
			}
			throw new MetadataValidationException("Could not decode the value " +
					value_string + ". It does not parse as an integer, a '' delimited string or a () delimited list.");
		}
		return name;
	}

}
