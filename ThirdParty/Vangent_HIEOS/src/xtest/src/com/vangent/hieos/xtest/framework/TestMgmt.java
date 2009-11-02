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

import com.vangent.hieos.xutil.exception.XdsInternalException;
import com.vangent.hieos.xutil.metadata.structure.Metadata;
import com.vangent.hieos.xutil.metadata.structure.MetadataSupport;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.apache.axiom.om.OMElement;

public class TestMgmt extends OMGenerator {


	public HashMap assignUniqueIds(Metadata metadata, String no_assign_uid_to) throws XdsInternalException {
		HashMap unique_ids = new HashMap();    // object id field => uniqueID assigned


			IdAllocator allocator = new UniqueIdAllocator();
			// for all ExtrinsicObjects
			allocator.assign(metadata, "ExtrinsicObject", MetadataSupport.XDSDocumentEntry_uniqueid_uuid, unique_ids, no_assign_uid_to);

			// for all SubmissionSets
			allocator.assign(metadata, "RegistryPackage", MetadataSupport.XDSSubmissionSet_uniqueid_uuid, unique_ids, no_assign_uid_to);

			// for all Folders
			allocator.assign(metadata, "RegistryPackage", MetadataSupport.XDSFolder_uniqueid_uuid, unique_ids, no_assign_uid_to);


		return unique_ids;
	}

	public HashMap<String, String> assignPatientId(Metadata metadata, String forced_patient_id) throws XdsInternalException {
		HashMap<String, String> ids = new HashMap<String, String>();    // object id field => patientID assigned

		IdAllocator allocator = (forced_patient_id == null) ? new PatientIdAllocator() : new PatientIdAllocator(forced_patient_id);
		// for all ExtrinsicObjects
		String pid = allocator.assign(metadata, "ExtrinsicObject", MetadataSupport.XDSDocumentEntry_patientid_uuid, ids, null);

		// for all SubmissionSets
		pid = allocator.assign(metadata, "RegistryPackage", MetadataSupport.XDSSubmissionSet_patientid_uuid, ids, null);

		// for all Folders
		pid = allocator.assign(metadata, "RegistryPackage", MetadataSupport.XDSFolder_patientid_uuid, ids, null);


		return ids;
	}

	public HashMap<String, String> assignSourceId(Metadata metadata) throws XdsInternalException {
		HashMap<String, String> ids = new HashMap<String, String>();    // object id field => patientID assigned

		IdAllocator allocator = new SourceIdAllocator();

		// for all SubmissionSets
		allocator.assign(metadata, "RegistryPackage", MetadataSupport.XDSSubmissionSet_sourceid_uuid, ids, null);

		return ids;
	}

	public OMElement hashAsXml(HashMap hmap, String element_name, String per_value_element_name) {
		OMElement xml = om_factory().createOMElement(element_name, null);
		Set keys = hmap.keySet();
		for (Iterator it=keys.iterator(); it.hasNext(); ) {
			OMElement per = om_factory().createOMElement(per_value_element_name, null);
			xml.addChild(per);
			String key = (String) it.next();
			String value = (String) hmap.get(key);
			OMElement v = om_factory().createOMElement("Old", null);
			per.addChild(v);
			v.addChild(om_factory().createOMText(key));
			OMElement w = om_factory().createOMElement("New", null);
			per.addChild(w);
			w.addChild(om_factory().createOMText(value));
		}
		return xml;
	}


}
