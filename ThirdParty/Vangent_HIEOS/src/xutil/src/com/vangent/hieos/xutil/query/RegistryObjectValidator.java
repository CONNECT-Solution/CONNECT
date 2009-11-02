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

package com.vangent.hieos.xutil.query;

import com.vangent.hieos.xutil.response.ErrorLogger;
import com.vangent.hieos.xutil.metadata.structure.Metadata;
import com.vangent.hieos.xutil.metadata.structure.MetadataSupport;
import com.vangent.hieos.xutil.metadata.structure.MetadataParser;
import com.vangent.hieos.xutil.exception.MetadataValidationException;
import com.vangent.hieos.xutil.exception.XdsException;
import com.vangent.hieos.xutil.exception.XdsInternalException;
import com.vangent.hieos.xutil.xlog.client.XLogMessage;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.axiom.om.OMElement;

public class RegistryObjectValidator extends StoredQuery {

	public RegistryObjectValidator(ErrorLogger response, XLogMessage log_message) {
		super(response, log_message);
	}

	public ArrayList validateExists(ArrayList uuids)  throws XdsException {
		init();
		a("SELECT * FROM RegistryObject ro"); n();
		a("WHERE"); n();
		a("  ro.id IN "); a(uuids); n();

		ArrayList<String> results = this.query_for_object_refs();

		ArrayList missing = null;
		for (int i=0; i<uuids.size(); i++) {
			String uuid = (String) uuids.get(i);
			if ( !results.contains(uuid)) {
				if (missing == null)
					missing = new ArrayList();
				missing.add(uuid);
			}
		}
		return missing;

	}
	
	ArrayList<String> uuidsOnly(ArrayList<String> ids) {
		ArrayList<String> uuids = new ArrayList<String>();
		for (String id : ids) {
			if (id.startsWith("urn:uuid:"))
				uuids.add(id);
		}
		return uuids;
	}

	// returns UUIDs that do exist in registry
	public ArrayList<String> validateNotExists(ArrayList<String> ids)  throws XdsException {
		ArrayList<String> uuids = uuidsOnly(ids);
		init();
		a("SELECT * FROM RegistryObject ro"); n();
		a("WHERE"); n();
		a("  ro.id IN "); a(uuids); n();

		ArrayList results = this.query_for_object_refs();

		return results;		
	}


	// uid_hash is uid => hash (null for non documents)
	public void validateProperUids(Metadata metadata)  throws XdsException {

		HashMap<String, ArrayList<String>> uid_hash = metadata.getUidHashMap();
		
		ArrayList<String> uids = new ArrayList<String>();
		uids.addAll(uid_hash.keySet());

		ArrayList<String> uid_id_schemes = new ArrayList<String>();
		uid_id_schemes.add(MetadataSupport.XDSFolder_uniqueid_uuid);
		uid_id_schemes.add(MetadataSupport.XDSSubmissionSet_uniqueid_uuid);
		uid_id_schemes.add(MetadataSupport.XDSDocumentEntry_uniqueid_uuid);

		init();
		a("SELECT ro.id from RegistryObject ro, ExternalIdentifier ei"); n();
		a("WHERE"); n();
		a(" ei.registryobject = ro.id AND "); n(); 
		a("  ei.identificationScheme IN "); a(uid_id_schemes); a(" AND"); n();
		a("  ei.value IN "); a(uids); n();

		// these uuids identify objects that carry one of the uids passed in in the map
		ArrayList<String> uuids = this.query_for_object_refs();
		
		if (uuids.size() == 0)
			return;

		// at least one uniqueId is already present in the registry. If it is from a document
		// and the hashes are the same.  Otherwise it is an error.
		
		this.return_leaf_class = true;
		OMElement objects = get_objects_by_uuid(uuids);   // LeafClass for offending objects
		if (objects == null) 
			throw new XdsInternalException("RegistryObjectValidator.validateProperUids(): could not retrieve LeafClass for ObjectRef obtained from registry: UUIDs were " + uuids);
		
		Metadata m = MetadataParser.parseNonSubmission(objects);
		ArrayList<String> dup_uids = new ArrayList<String>();
		HashMap<String, OMElement> dup_objects = m.getUidMap();
		dup_uids.addAll(dup_objects.keySet());
		
		
		log_message.addOtherParam("dup uuids", uuids.toString());
		log_message.addOtherParam("dup uids", dup_uids.toString());
		
		for (String suuid : metadata.getSubmissionSetIds()) {
			String sid = metadata.getExternalIdentifierValue(suuid, MetadataSupport.XDSSubmissionSet_uniqueid_uuid);
			log_message.addOtherParam("ssuid", sid);
			if (dup_uids.contains(sid)) {
				throw new MetadataValidationException("SubmissionSet uniqueId " + 
						sid + 
						" ( id = " + suuid + " ) " +
						" already present in the registry");
				
			}
		}
		
		for (String fuuid : metadata.getFolderIds()) {
			String fuid = metadata.getExternalIdentifierValue(fuuid, MetadataSupport.XDSFolder_uniqueid_uuid);
			log_message.addOtherParam("fuid", fuid);
			if (dup_uids.contains(fuid)) {
				throw new MetadataValidationException("Folder uniqueId " + 
						fuid + 
						" ( id = " + fuuid + " ) " +
						" already present in the registry");
				
			}
		}

		HashMap<String, OMElement> docs_submit_uid_map = metadata.getUidMap(metadata.getExtrinsicObjects());
		for (String doc_uid : docs_submit_uid_map.keySet()) {
			if (dup_uids.contains(doc_uid)) {
				OMElement reg_obj = dup_objects.get(doc_uid);
				String type = reg_obj.getLocalName();
				if ( !type.equals("ExtrinsicObject")) 
					throw new MetadataValidationException("Document uniqueId " + 
							doc_uid + 
							" already present in the registry on a non-document object");
				OMElement sub_obj = docs_submit_uid_map.get(doc_uid);
				String sub_hash = m.getSlotValue(sub_obj, "hash", 0);
				String reg_hash = m.getSlotValue(reg_obj, "hash", 0);
				if (sub_hash != null && reg_hash != null && !sub_hash.equals(reg_hash))
					response.add_error(MetadataSupport.XDSNonIdenticalHash,
							"UniqueId " + doc_uid + " exists in both the submission and Registry and the hash value is not the same: " +
							"Submission Hash Value = " + sub_hash + " and " +
							"Registry Hash Value = " + reg_hash,
							this.getClass().getName(), log_message);

			}
		}
		
	}


	public ArrayList validateDocuments(ArrayList uuids)  throws  XdsException {
		init();
		a("SELECT * FROM ExtrinsicObject eo"); n();
		a("WHERE"); n();
		a("  eo.id IN "); a(uuids);  n();

		ArrayList results = this.query_for_object_refs();

		ArrayList missing = null;
		for (int i=0; i<uuids.size(); i++) {
			String uuid = (String) uuids.get(i);
			if ( !results.contains(uuid)) {
				if (missing == null)
					missing = new ArrayList();
				missing.add(uuid);
			}
		}
		return missing;

	}
	
	// validate the ids are in registry and belong to folders
	// return any that aren't
	public ArrayList<String> validateAreFolders(ArrayList<String> ids) throws  XdsException {
		init();
		a("SELECT rp.id FROM RegistryPackage rp, ExternalIdentifier ei"); n();
		a("WHERE"); n();
		a("  rp.status = '");
        a(MetadataSupport.status_type_approved);
        a("' AND"); n();
		a("  rp.id IN "); a(ids); a(" AND"); n();
		a("  ei.registryObject = rp.id AND"); n();
		a("  ei.identificationScheme = '" + MetadataSupport.XDSFolder_patientid_uuid + "'"); n();
		
		br.setReason("Verify are Folders");

		ArrayList results1 = this.query_for_object_refs();

		ArrayList<String> missing = null;
		for (String id : ids) {
			if ( !results1.contains(id)) {
				if (missing == null)
					missing = new ArrayList<String>();
				missing.add(id);
			}
		}
		
		return missing;
	}

	public ArrayList validateApproved(ArrayList uuids)  throws  XdsException {
		init();
		a("SELECT * FROM ExtrinsicObject eo"); n();
		a("WHERE"); n();
		a("  eo.status = '");
        a(MetadataSupport.status_type_approved);
        a("' AND"); n();
		a("  eo.id IN "); a(uuids);  n();

		ArrayList results1 = this.query_for_object_refs();

		init();
		a("SELECT * FROM RegistryPackage eo"); n();
		a("WHERE"); n();
		a("  eo.status = '");
        a(MetadataSupport.status_type_approved);
        a("' AND"); n();
		a("  eo.id IN "); a(uuids);  n();

		ArrayList results = this.query_for_object_refs();

		results.addAll(results1); 

		ArrayList missing = null;
		for (int i=0; i<uuids.size(); i++) {
			String uuid = (String) uuids.get(i);
			if ( !results.contains(uuid)) {
				if (missing == null)
					missing = new ArrayList();
				missing.add(uuid);
			}
		}
		return missing;
	}

	public ArrayList validateSamePatientId(ArrayList uuids, String patient_id)
            throws  XdsException {
		if (uuids.size() == 0)
			return null;
		init();
		a("SELECT eo.id FROM ExtrinsicObject eo, ExternalIdentifier pid"); n();
		a("WHERE"); n();
		a("  eo.id IN "); a(uuids); a(" AND "); n();
		a("  pid.registryobject = eo.id AND");  n();
		a("  pid.identificationScheme='urn:uuid:58a6f841-87b3-4a3e-92fd-a8ffeff98427' AND"); n();
		a("  pid.value = '"); a(patient_id); a("'"); n();

		ArrayList results1 = this.query_for_object_refs();

		init();
		a("SELECT eo.id FROM RegistryPackage eo, ExternalIdentifier pid"); n();
		a("WHERE"); n();
		a("  eo.id IN "); a(uuids);  a(" AND"); n();
		a("  pid.registryobject = eo.id AND");  n();
		a("  pid.identificationScheme IN ('urn:uuid:6b5aea1a-874d-4603-a4bc-96a0a7b38446','urn:uuid:f64ffdf0-4b97-4e06-b79f-a52b38ec2f8a') AND"); n();
		a("  pid.value = '"); a(patient_id); a("'"); n();

		ArrayList results = this.query_for_object_refs();

		results.addAll(results1); 

		ArrayList missing = null;
		for (int i=0; i<uuids.size(); i++) {
			String uuid = (String) uuids.get(i);
			if ( !results.contains(uuid)) {
				if (missing == null)
					missing = new ArrayList();
				missing.add(uuid);
			}
		}
		return missing;
	}

	public ArrayList getXFRMandAPNDDocuments(ArrayList uuids) throws  XdsException {
		if (uuids.size() == 0)
			return new ArrayList();
		init();
		a("SELECT eo.id FROM ExtrinsicObject eo, Association a"); n();
		a("WHERE"); n();
		a("  a.associationType in ('");
        a(MetadataSupport.xdsB_ihe_assoc_type_xfrm);
        a("', '");
        a(MetadataSupport.xdsB_ihe_assoc_type_apnd);
        a("') AND"); n();
		a("  a.targetObject IN "); a(uuids); a(" AND"); n();
		a("  a.sourceObject = eo.id"); n();

		return this.query_for_object_refs();
	}

	@Override
	public Metadata run_internal() throws XdsException {
		// TODO Auto-generated method stub
		return null;
	}

}
