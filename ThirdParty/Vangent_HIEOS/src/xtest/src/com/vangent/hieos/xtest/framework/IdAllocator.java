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

import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;


public abstract class IdAllocator {

	static final String mgmt_dir = TestConfig.testmgmt_dir; 
	static final String assigning_authority_file = mgmt_dir + "assigning_authority.txt";
	static final String patient_id_base_file = mgmt_dir + "patientid_base.txt";
	static final String patient_id_incr_file = mgmt_dir + "patientid_incr.txt";
	protected static final String uniqueid_base_file = mgmt_dir + "uniqueid_base.txt";
	protected static final String uniqueid_index_file = mgmt_dir + "uniqueid_index.txt";
	static final String sourceId_file = mgmt_dir + "sourceId.txt";

	abstract String allocate() throws XdsInternalException;

	// object_type is ExtrinsicObject or RegistryPackage
	//
	// Although the variable names below imply uniqueId, they are use for patient ID also based on the sub-class
	// In reality, this is only used for uniqueIDs.  Patient IDs are handled in a new way
	//
	public String assign(Metadata metadata, String object_type, String external_identifier_uuid, HashMap<String, String> assignments, String no_assign_uid_to) 
	throws XdsInternalException {
		
		ArrayList eos = metadata.getMajorObjects(object_type);
		String new_value = null;
		for (int i=0; i<eos.size(); i++) {
			OMElement eo = (OMElement) eos.get(i);
			String id = eo.getAttributeValue(MetadataSupport.id_qname);
			if (no_assign_uid_to != null) {
				if (id.equals(no_assign_uid_to))
					continue;
			}
			// for all xxx.uniqueId
			ArrayList eis = metadata.getExternalIdentifiers(eo, external_identifier_uuid);
			for (int j=0; j<eis.size(); j++) {
				OMElement ei = (OMElement) eis.get(j);
				OMAttribute uniqueid_value_att = ei.getAttribute(new QName("value"));
				String old_value = uniqueid_value_att.getAttributeValue();
				new_value = allocate();
				uniqueid_value_att.setAttributeValue(new_value);
				assignments.put(id, new_value);
			}
		}
		return new_value;
	}

}
