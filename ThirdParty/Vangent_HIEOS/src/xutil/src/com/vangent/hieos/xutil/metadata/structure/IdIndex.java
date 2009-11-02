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

import com.vangent.hieos.xutil.metadata.structure.Metadata;
import com.vangent.hieos.xutil.metadata.structure.MetadataSupport;
import com.vangent.hieos.xutil.exception.MetadataException;
import com.vangent.hieos.xutil.xlog.client.XLogMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNode;

public class IdIndex {
	Metadata m;
	HashMap<String, OMElement> _object_by_id = null;   // id => OMElement
	HashMap<String, HashMap<String, ArrayList<OMElement>>> _object_parts_by_id = null;  // id => HashMap(type => ArrayList(OMElement))   type is Slot, Description, ...
	XLogMessage log_message = null;

	void set_log_message(XLogMessage log_message) {
		this.log_message = log_message;
	}

	public IdIndex() {

	}

	public void setMetadata(Metadata m)  throws MetadataException {
		this.m = m;
//		object_parts_by_id = new HashMap<String, HashMap<String,ArrayList<OMElement>>>();

		this.parse_objects_by_id(m.getNonObjectRefs());

	}

	public IdIndex(Metadata m) throws MetadataException {
		this.setMetadata(m);
	}

	public String getExternalIdentifierValue(String id, String identifier_scheme) {
		HashMap<String,ArrayList<OMElement>> part_map = object_parts_by_id().get(id);
		if (part_map == null)
			return null;
		try {
			OMElement obj = m.getObjectById(id);
		} catch (Exception e) {}
		ArrayList<OMElement> external_identifiers = part_map.get("ExternalIdentifier");
		for (int i=0; i<external_identifiers.size(); i++ ) {
			OMElement ei = (OMElement) external_identifiers.get(i);
			OMAttribute id_scheme_att = ei.getAttribute(MetadataSupport.identificationscheme_qname);
			String scheme = id_scheme_att.getAttributeValue();
			if (id_scheme_att != null && id_scheme_att.getAttributeValue().equals(identifier_scheme)) {
				OMAttribute value_att = ei.getAttribute(MetadataSupport.value_qname);
				if (value_att != null)
					return value_att.getAttributeValue();
			}
		}
		return null;
	}

	public ArrayList<OMElement> getSlots(String id) {
		HashMap part_map = (HashMap) object_parts_by_id().get(id);
		if (part_map == null)
			return new ArrayList();
		return (ArrayList) part_map.get("Slot");
	}

	public OMElement getSlot(String id, String name) {
		ArrayList<OMElement> slots = getSlots(id);
		for (OMElement ele : slots) {
			if (ele.getAttributeValue(MetadataSupport.slot_name_qname).equals(name))
				return ele;
		}
		return null;
	}

	public ArrayList getClassifications(String id) {
		HashMap part_map = (HashMap) object_parts_by_id().get(id);
		if (part_map == null)
			return new ArrayList();
		return (ArrayList) part_map.get("Classification");
	}

	public OMElement getName(String id) {
		HashMap part_map = (HashMap) object_parts_by_id().get(id);
		if (part_map == null)
			return null;
		ArrayList name_list = (ArrayList) part_map.get("Name");
		if (name_list.size() == 0)
			return null;
		return (OMElement) name_list.get(0);
	}

	public OMElement getDescription(String id) {
		HashMap part_map = (HashMap) object_parts_by_id().get(id);
		if (part_map == null)
			return null;
		ArrayList name_list = (ArrayList) part_map.get("Description");
		if (name_list.size() == 0)
			return null;
		return (OMElement) name_list.get(0);
	}

	public String getNameValue(String id) {
		OMElement name_ele = getName(id);
		if (name_ele == null)
			return null;
		OMElement loc_st = MetadataSupport.firstChildWithLocalName(name_ele, "LocalizedString"); 
		if (loc_st == null)
			return null;
		return loc_st.getAttributeValue(MetadataSupport.value_qname);
	}

	public String getDescriptionValue(String id) {
		OMElement desc_ele = getDescription(id);
		if (desc_ele == null)
			return null;
		OMElement loc_st = MetadataSupport.firstChildWithLocalName(desc_ele, "LocalizedString"); 
		if (loc_st == null)
			return null;
		return loc_st.getAttributeValue(MetadataSupport.value_qname);
	}

	public ArrayList getExternalIdentifiers(String id) {
		HashMap part_map = (HashMap) object_parts_by_id().get(id);
		if (part_map == null)
			return new ArrayList();
		return (ArrayList) part_map.get("ExternalIdentifier");
	}

	public String getSubmissionSetUniqueId() {
		OMElement ss = m.getSubmissionSet();
		String ss_id = ss.getAttributeValue(MetadataSupport.id_qname);
		return getExternalIdentifierValue(ss_id, "urn:uuid:96fdda7c-d067-4183-912e-bf5ee74998a8");
	}

	public String getSubmissionSetPatientId() {
		OMElement ss = m.getSubmissionSet();
		String ss_id = ss.getAttributeValue(MetadataSupport.id_qname);
		return getExternalIdentifierValue(ss_id, "urn:uuid:6b5aea1a-874d-4603-a4bc-96a0a7b38446");
	}

	public String getSubmissionSetSourceId() {
		OMElement ss = m.getSubmissionSet();
		String ss_id = ss.getAttributeValue(MetadataSupport.id_qname);
		HashMap by_att_type = (HashMap) object_parts_by_id().get(ss_id);
		ArrayList slots = (ArrayList) by_att_type.get("Slot");
		String source_id = get_slot_value(slots, "sourceId");
		return source_id;
	}

	String get_slot_value(ArrayList slots, String slot_name) {
		for (int i=0; i<slots.size(); i++) {
			OMElement slot = (OMElement) slots.get(i);
			String name = slot.getAttributeValue(MetadataSupport.name_qname);
			if (name == null)
				continue;
			if ( !name.equals(slot_name)) 
				continue;
			OMElement value_list = MetadataSupport.firstChildWithLocalName(slot, "ValueList"); 
			if (value_list == null)
				continue;
			OMElement value = MetadataSupport.firstChildWithLocalName(value_list, "Value");
			if (value == null)
				continue;
			return value.getText();
		}
		return null;
	}

	public OMElement getObjectById(String id) {
		return (OMElement) object_by_id().get(id);
	}

	public String getIdentifyingString(String id) {
		Object obj = object_by_id().get(id);
		if (obj == null)
			return "<Unknown object " + id + ">";

		OMElement ele;
		if (obj instanceof OMElement)
			ele = (OMElement) obj;
		else
			return "Unknown object type for id " + id + ">";

		return m.getIdentifyingString(ele);
	}

	public String getObjectTypeById(String id) {
		OMElement submission_set = m.getSubmissionSet();
		OMElement obj = getObjectById(id);
		if (obj == null)
			return null;
		String name = obj.getLocalName();
		if (name.equals("RegistryPackage")) {
			if (obj == submission_set)
				return "SubmissionSet";
			return "Folder";
		}
		return name;
	}

	void parse_objects_by_id(ArrayList objects) throws MetadataException {
		for (int i=0; i<objects.size(); i++) {
			OMElement obj = (OMElement) objects.get(i);
			parse_object_by_id(obj);
		}
	}

	private int count_iterator(Iterator it) {
		int i=0;
		for ( ; it.hasNext(); ) {
			it.next();
			i++;
		}
		return i;
	}
	
	private HashMap<String, OMElement> object_by_id() {
		if (_object_by_id == null) 
			_object_by_id = new HashMap<String, OMElement>();   // id => OMElement
		return _object_by_id;
	}
	
	private HashMap<String, HashMap<String,ArrayList<OMElement>>> object_parts_by_id() {
		if (_object_parts_by_id == null)
			_object_parts_by_id = new HashMap<String, HashMap<String,ArrayList<OMElement>>>();   // id => HashMap(type => ArrayList(OMElement))   type is Slot, Description, ...
		return _object_parts_by_id;
	}

	private void parse_object_by_id(OMElement obj) throws MetadataException {
		String id = obj.getAttributeValue(MetadataSupport.id_qname);
		if (id == null) {
			return;
		}
		if (id.length() == 0) {
			return;
		}

		if (log_message != null)
			try {log_message.addOtherParam("ii object to parse", obj.getLocalName() + " " + "id=" + id + " " + count_iterator(obj.getChildElements()) + " minor elements"); } catch (Exception e) {}

			// ebxmlrr gens ObjectRefs even when real object is returned
			OMElement existing = (OMElement) object_by_id().get(id);
			if (existing != null) {
				String existing_type = existing.getLocalName();
				if ( existing_type.equals("ObjectRef") )
					object_by_id().put(id, obj);
			} else
				object_by_id().put(id, obj);

			HashMap<String, ArrayList<OMElement>> parts = new HashMap<String, ArrayList<OMElement>>();
			ArrayList<OMElement> name = new ArrayList<OMElement>();
			ArrayList<OMElement> description = new ArrayList<OMElement>();
			ArrayList<OMElement> slots = new ArrayList<OMElement>();
			ArrayList<OMElement> external_identifiers = new ArrayList<OMElement>();
			ArrayList<OMElement> classifications = new ArrayList<OMElement>();



			for (Iterator<OMNode> it=obj.getChildren(); it.hasNext(); ) {
				OMNode part_n =  it.next();
				if ( !(part_n instanceof OMElement))
					continue;
				OMElement part = (OMElement) part_n;
				String part_type = part.getLocalName();
				if (log_message != null)
					try {log_message.addOtherParam("part", part.toString()); } catch (Exception e) {}

					if (part_type.equals("Name")) 
						name.add(part);
					else if (part_type.equals("Description"))
						description.add(part);
					else if (part_type.equals("Slot"))
						slots.add(part);
					else if (part_type.equals("ExternalIdentifier")) {
						external_identifiers.add(part);
						if (log_message != null)
							try {log_message.addOtherParam("adding", external_identifiers.size() + " eis so far"); } catch (Exception e) {}
					}
					else if (part_type.equals("Classification"))
						classifications.add(part);
					else if (part_type.equals("ObjectRef"))
						;
					else
						;
			}
			parts.put("Name", name);
			parts.put("Description", description);
			parts.put("Slot", slots);
			parts.put("ExternalIdentifier", external_identifiers);
			parts.put("Classification", classifications);
			parts.put("Element", singleton(obj));

			object_parts_by_id().put(id, parts);
	}

	public String toString() {
		StringBuffer buf = new StringBuffer();

		buf.append("Metadata Index:\n");

		for (String id : object_parts_by_id().keySet()) {
			OMElement obj = null;
			try { 
				obj = m.getObjectById(id);
			} catch(Exception e) {break;}
			buf.append(id + "(" + obj.getLocalName() + ")\n");
			HashMap<String, ArrayList<OMElement>> type_map = object_parts_by_id().get(id);
			for (String type : type_map.keySet()) {
				buf.append("\t" + type + "\n");
				ArrayList<OMElement> elements = type_map.get(type);
				for (OMElement element : elements) {
					buf.append("\t\t" + element.getLocalName());
					if (element.getLocalName().equals("ExternalIdentifier")) {
						buf.append(" idscheme=" + element.getAttributeValue(MetadataSupport.identificationscheme_qname));
						buf.append(" value=" + element.getAttributeValue(MetadataSupport.value_qname));
					}
					buf.append("\n");
				}
			}
		}

		return buf.toString();
	}
	
	ArrayList<OMElement> singleton(OMElement o) {
		ArrayList<OMElement> al = new ArrayList<OMElement>();
		al.add(o);
		return al;
	}



}
