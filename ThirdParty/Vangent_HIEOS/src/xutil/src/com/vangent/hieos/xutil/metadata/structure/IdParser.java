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

import com.vangent.hieos.xutil.uuid.UuidAllocator;
import com.vangent.hieos.xutil.exception.XdsInternalException;
//import com.vangent.hieos.xutil.registry.validation.Structure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

//import javax.xml.namespace.QName;

import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNamespace;
//import org.apache.log4j.Logger;

public class IdParser {
	Metadata m;
	// symbolic compiler
	ArrayList referencingAttributes = null;
	ArrayList identifyingAttributes = null;
	ArrayList symbolicIdReplacements = null;
	HashMap assignedUuids = null;

	public IdParser(Metadata m) {
		referencingAttributes = new ArrayList();
		identifyingAttributes = new ArrayList();
		this.m = m;
		parse();
	}

	public ArrayList getDefinedIds() {
		ArrayList defined = new ArrayList();

		for (Iterator it=this.identifyingAttributes.iterator(); it.hasNext(); ) {
			OMAttribute attr = (OMAttribute) it.next();
			String id = attr.getAttributeValue();

			if ( !defined.contains(id))
				defined.add(id);
		}

		return defined;
	}

	public ArrayList getReferencedIds() {
		ArrayList refer = new ArrayList();
		for (Iterator it=this.referencingAttributes.iterator(); it.hasNext(); ) {
			OMAttribute attr = (OMAttribute) it.next();
			String id = attr.getAttributeValue();

			if ( !refer.contains(id))
				refer.add(id);
		}

		return refer;
	}

	public ArrayList getUndefinedIds() {
		ArrayList referenced = this.getReferencedIds();
		ArrayList defined = this.getDefinedIds();
		ArrayList undefined = new ArrayList();

		for (Iterator it=referenced.iterator(); it.hasNext(); ) {
			String id = (String) it.next();

			if ( ! defined.contains(id))
				undefined.add(id);

		}

		return undefined;
	}

	void parse() {
		ArrayList<OMElement> allObjects = m.getAllObjects();

		for (int i=0; i<allObjects.size(); i++) {
			OMElement obj = allObjects.get(i);
			OMAttribute idAtt = obj.getAttribute(MetadataSupport.id_qname);
			String type = obj.getLocalName();

			if (idAtt != null)
				identifyingAttributes.add(idAtt);

			if (type.equals("Classification")) {
				OMAttribute a = obj.getAttribute(MetadataSupport.classified_object_qname);
				if (a != null)
					referencingAttributes.add(a);
				a = obj.getAttribute(MetadataSupport.classificationscheme_qname);
				if (a != null)
					referencingAttributes.add(a);
			} else if (type.equals("Association")) {
				OMAttribute a = obj.getAttribute(MetadataSupport.source_object_qname);
				if (a != null)
					referencingAttributes.add(a);
				a = obj.getAttribute(MetadataSupport.target_object_qname);
				if (a != null)
					referencingAttributes.add(a);
			}

			for (Iterator it1=obj.getChildElements(); it1.hasNext(); ) {
				OMElement objI = (OMElement) it1.next();
				String typeI = objI.getLocalName();

				if (typeI.equals("ExternalIdentifier")) {
					OMAttribute att = objI.getAttribute(MetadataSupport.registry_object_qname);
					if (att != null) 
						referencingAttributes.add(att);
					att = objI.getAttribute(MetadataSupport.identificationscheme_qname);
					if (att != null) 
						referencingAttributes.add(att);
				} else if (typeI.equals("Classification")) {
					OMAttribute a = objI.getAttribute(MetadataSupport.classified_object_qname);
					if (a != null)
						referencingAttributes.add(a);
					a = objI.getAttribute(MetadataSupport.classificationscheme_qname);
					if (a != null)
						referencingAttributes.add(a);
				}
			}
		}

	}

	/*
	 * Symbol Compiler
	 */
	public void compileSymbolicNamesIntoUuids () throws XdsInternalException {

		// make list of all symbolic names used in metadata
		// allocate UUID for these names
		// update attributes that define these symbolic names with UUIDs
		ArrayList symbolicNames = new ArrayList();
		ArrayList uuids = new ArrayList();
		assignedUuids = new HashMap();
		for (int i=0; i<identifyingAttributes.size(); i++) {
			OMAttribute att = (OMAttribute) identifyingAttributes.get(i);
			String name = att.getAttributeValue();
			if (name.startsWith("urn:uuid:"))
				continue;
			symbolicNames.add(name);
			String uuid = UuidAllocator.allocate();
			uuids.add(uuid);    // can index uuids like symbolic_names
			att.setAttributeValue(uuid);
			assignedUuids.put(name, uuid);
		}

		// update all references to objects that we just allocated uuids for
		for (int i=0; i<referencingAttributes.size(); i++) {
			OMAttribute att = (OMAttribute) referencingAttributes.get(i);
			String symbolicName = att.getAttributeValue();
			if (symbolicName.startsWith("urn:uuid:"))
				continue;
			int idIndex = symbolicNames.indexOf(symbolicName);
			if (idIndex == -1)
				throw new XdsInternalException("Metadata:compileSymbolicNamesIntoUuids(): cannot find symbolic name " + symbolicName + " in tables");
			String uuid = (String) uuids.get(idIndex);
			att.setAttributeValue(uuid);
		}
	}

	public HashMap getSymbolicNameUuidMap() {
		return assignedUuids;
	}

	public OMElement getApproveObjectsRequest(ArrayList uuids) {
        OMNamespace lcm = MetadataSupport.ebLcm3;
		OMElement req = MetadataSupport.om_factory.createOMElement("ApproveObjectsRequest", lcm);
		req.addChild(mk_object_ref_list(uuids));
		return req;
	}

	public OMElement getDeprecateObjectsRequest(ArrayList uuids) {
        OMNamespace lcm = MetadataSupport.ebLcm3;
		OMElement req = MetadataSupport.om_factory.createOMElement("DeprecateObjectsRequest", lcm);
		req.addChild(mk_object_ref_list(uuids));
		return req;

	}

	private OMElement mk_object_ref_list(ArrayList uuids) {
        OMNamespace rim  = MetadataSupport.ebRIMns3;
		OMElement object_ref_list = MetadataSupport.om_factory.createOMElement("ObjectRefList", rim);
		for (Iterator it=uuids.iterator(); it.hasNext(); ) {
			String uuid = (String) it.next();
			OMAttribute att = MetadataSupport.om_factory.createOMAttribute("id", null, uuid);
			OMElement object_ref = MetadataSupport.om_factory.createOMElement("ObjectRef", rim);
			object_ref.addAttribute(att);
			object_ref_list.addChild(object_ref);
		}
		return object_ref_list;
	}

	/*
	 * Registry adaptor - approve objects
	 */

	ArrayList approveable_objects(Metadata m) {
		ArrayList o = new ArrayList();
		o.addAll(m.getExtrinsicObjects());
		o.addAll(m.getRegistryPackages());
		return o;
	}

	public ArrayList approvable_object_ids(Metadata m) {
		return m.getObjectIds(approveable_objects(m));
	}

	boolean is_approveable_object(Metadata m, OMElement o) {
		if (m.getExtrinsicObjects().contains(o))
			return true;
		if (m.getRegistryPackages().contains(o))
			return true;
		return false;
	}



}
