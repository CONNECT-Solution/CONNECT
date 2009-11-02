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

import com.vangent.hieos.xutil.exception.MetadataException;
import com.vangent.hieos.xutil.exception.MetadataValidationException;
import com.vangent.hieos.xutil.exception.XdsException;
import com.vangent.hieos.xutil.exception.XdsInternalException;
import com.vangent.hieos.xutil.response.RegistryErrorList;
import com.vangent.hieos.xutil.query.RegistryObjectValidator;
import com.vangent.hieos.xutil.xlog.client.XLogMessage;

import java.util.ArrayList;

import org.apache.axiom.om.OMElement;
import org.apache.log4j.Logger;

public class Structure {
	Metadata m;
	RegistryErrorList rel;
	boolean is_submit;
	XLogMessage log_message;
	private final static Logger logger = Logger.getLogger(Structure.class);


	public Structure(Metadata m, boolean is_submit) throws XdsInternalException {
		this.m = m;
		rel = new RegistryErrorList( 
				(m.isVersion2()) ? RegistryErrorList.version_2 : RegistryErrorList.version_3, 
						false /* log */);
		this.is_submit = is_submit;
		log_message = null;
	}

	public Structure(Metadata m, boolean is_submit, RegistryErrorList rel, 	XLogMessage log_message) throws XdsInternalException {
		this.m = m;
		this.is_submit = is_submit;
		this.rel = rel;
		this.log_message = log_message;
	}

	public void run()  throws MetadataException, MetadataValidationException, XdsException {
		submission_structure();

	}


	void submission_structure()  throws MetadataException, MetadataValidationException, XdsException {
		ss_doc_fol_must_have_ids();
		if (is_submit) {
			doc_implies_ss();
			fol_implies_ss();
			ss_implies_doc_or_fol_or_assoc();
			docs_in_ss();
			fols_in_ss();
			rplced_doc_not_in_submission();
			ss_status_relates_to_ss();
			by_value_assoc_in_submission();
			folder_assocs();
		}
		ss_status_single_value();
		assocs_have_proper_namespace();
	}

	void ss_status_relates_to_ss() {
		String ss_id = m.getSubmissionSetId();
		ArrayList assocs = m.getAssociations();

		for (int i=0; i<assocs.size(); i++) {
			OMElement assoc = (OMElement) assocs.get(i);
			String a_target = assoc.getAttributeValue(MetadataSupport.target_object_qname);
			String a_type = assoc.getAttributeValue(MetadataSupport.association_type_qname);
			String a_source = assoc.getAttributeValue(MetadataSupport.source_object_qname);

			boolean target_is_included_is_doc = m.getExtrinsicObjectIds().contains(a_target); 

			if (a_source.equals(ss_id)) {

				if ( !MetadataSupport.xdsB_eb_assoc_type_has_member.equals(a_type)) {
					err("Association referencing Submission Set has type " + a_type + " but only type HasMember is allowed");
				}
				if (target_is_included_is_doc) {
					if ( ! m.hasSlot(assoc, "SubmissionSetStatus")) {
						err("Association " +
								assoc.getAttributeValue(MetadataSupport.id_qname) +
								" has sourceObject pointing to Submission Set but contains no SubmissionSetStatus Slot"
						);
					} 
				} else if (m.getFolderIds().contains(a_target)) {

				} else {

				}
			}
			else {
				if ( m.hasSlot(assoc, "SubmissionSetStatus") && !"Reference".equals(m.getSlotValue(assoc, "SubmissionSetStatus", 0)))
					err("Association " +
							assoc.getAttributeValue(MetadataSupport.id_qname) +
							" does not have sourceObject pointing to Submission Set but contains SubmissionSetStatus Slot with value Original"
					);
			}
		}
	}

	void ss_doc_fol_must_have_ids() {
		ArrayList docs = m.getExtrinsicObjects();
		ArrayList rps = m.getRegistryPackages();

		for (int i=0; i<docs.size(); i++) {
			OMElement doc = (OMElement) docs.get(i);
			String id = doc.getAttributeValue(MetadataSupport.id_qname);
			if (	id == null ||
					id.equals("")
			) {
				err("All RegistryPackage and ExtrinsicObject objects must have id attributes");
				return;
			}
		}
		for (int i=0; i<rps.size(); i++) {
			OMElement rp = (OMElement) rps.get(i);
			String id = rp.getAttributeValue(MetadataSupport.id_qname);
			if (	id == null ||
					id.equals("")
			) {
				err("All RegistryPackage and ExtrinsicObject objects must have id attributes");
				return;
			}
		}
	}

	void by_value_assoc_in_submission() throws MetadataValidationException, MetadataException {
		ArrayList assocs = m.getAssociations();
		String ss_id = m.getSubmissionSetId();

		for (int i=0; i<assocs.size(); i++) {
			OMElement assoc = (OMElement) assocs.get(i);
			String source = assoc.getAttributeValue(MetadataSupport.source_object_qname);
			String type = assoc.getAttributeValue(MetadataSupport.association_type_qname);
			String target = assoc.getAttributeValue(MetadataSupport.target_object_qname);

			if ( !source.equals(ss_id))
				continue;

			boolean target_is_included_doc = m.getExtrinsicObjectIds().contains(target);

			String ss_status = m.getSlotValue(assoc, "SubmissionSetStatus", 0);

			if ( target_is_included_doc ) {

				if (ss_status == null || ss_status.equals("")) {
					err("SubmissionSetStatus Slot on Submission Set association has no value");
				} else if (	ss_status.equals("Original")) {
					if ( !m.containsObject(target)) 
						err("SubmissionSetStatus Slot on Submission Set association has value 'Original' but the targetObject " + target + " references an object not in the submission");
				} else if (	ss_status.equals("Reference")) {
					if (m.containsObject(target))
						err("SubmissionSetStatus Slot on Submission Set association has value 'Reference' but the targetObject " + target + " references an object in the submission");
				} else {
					err("SubmissionSetStatus Slot on Submission Set association has unrecognized value: " + ss_status);
				}
			} else {
				if (ss_status != null && !ss_status.equals("Reference")) 
					err("A SubmissionSet Assocation has the SubmissionSetStatus Slot but the target ExtrinsicObject is not part of the Submission");
					
			}
		}
	}


	void ss_status_single_value() {
		ArrayList assocs = m.getAssociations();
		String ss_id = m.getSubmissionSetId();

		for (int i=0; i<assocs.size(); i++) {
			OMElement assoc = (OMElement) assocs.get(i);
			String source = assoc.getAttributeValue(MetadataSupport.source_object_qname);
			String type = assoc.getAttributeValue(MetadataSupport.association_type_qname);
			String target = assoc.getAttributeValue(MetadataSupport.target_object_qname);

			if ( !source.equals(ss_id))
				continue;

			String ss_status = m.getSlotValue(assoc, "SubmissionSetStatus", 1);
			if (ss_status != null)
				err("SubmissionSetStatus Slot on Submission Set association has more than one value");
		}
	}

	void ss_implies_doc_or_fol_or_assoc() {
		if (	m.getSubmissionSet() != null &&
				! (
						m.getExtrinsicObjects().size() > 0 ||
						m.getFolders().size() > 0 ||
						m.getAssociations().size() > 0
				))
			err("Submission contains Submission Set but no Documents or Folders or Associations");
	}


	// does this id represent a folder in this metadata or in registry?
	public boolean isFolder(String id) throws XdsException {
		if (m.getFolderIds().contains(id))
			return true;

		if ( !id.startsWith("urn:uuid:"))
			return false;

		RegistryObjectValidator rov = new RegistryObjectValidator(rel, log_message);

		ArrayList<String> ids = new ArrayList<String>();
		ids.add(id);

		ArrayList<String> missing = rov.validateAreFolders(ids);
		if (missing != null && missing.contains(id))
			return false;


		return true;
	}

	// Folder Assocs must be linked to SS by a secondary Assoc
	void folder_assocs() throws XdsException {
		String ssId = m.getSubmissionSetId();
		ArrayList<OMElement> non_ss_assocs = null;
		for (OMElement a : m.getAssociations()) {
			String sourceId = m.getAssocSource(a);
			if (m.getAssocTarget(a) == ssId)
				err("SubmissionSet may not the be target of an Association");
			// if sourceId points to a SubmissionSet in this metadata then no further work is needed
			// if sourceId points to a Folder (in or out of this metadata) then secondary Assoc required
			if (sourceId.equals(ssId))
				continue;
			if (isFolder(sourceId)) {
				if (non_ss_assocs == null)
					non_ss_assocs = new ArrayList<OMElement>();
				non_ss_assocs.add(a);
			}
		}
		if (non_ss_assocs == null) return;

		// Show that the non-ss associations are linked to ss via a HasMember association
		// This only applies when the association's sourceObject is a Folder
		for (OMElement a : non_ss_assocs) {
			String aId = a.getAttributeValue(MetadataSupport.id_qname);
			boolean good = false;
			for (OMElement a2 : m.getAssociations()) {
				if (m.getAssocSource(a2).equals(ssId) &&
						m.getAssocTarget(a2).equals(aId) &&
						MetadataSupport.xdsB_eb_assoc_type_has_member.equals(m.getAssocType(a2))) {
					if (good) {
						err("Multiple HasMember Associations link Submission Set " + ssId + 
								" and Association\n" + a  );
					} else {
						good = true;
					}

				}
			}
			if (good == false)
				err("A HasMember Association is required to link Submission Set " + ssId + 
						" and Folder/Document Association\n" + a);
		}

	}

	void doc_implies_ss() {
		if (	m.getExtrinsicObjects().size() > 0 &&
				m.getSubmissionSet() == null )
			err("Submission contains an Document but no Submission Set");
	}

	void fol_implies_ss() {
		if (	m.getFolders().size() > 0 &&
				m.getSubmissionSet() == null )
			err("Submission contains a Folder but no Submission Set");
	}

	void docs_in_ss() {
		ArrayList docs = m.getExtrinsicObjects();

		for (int i=0; i<docs.size(); i++) {
			OMElement doc = (OMElement) docs.get(i);

			if ( !has_assoc(m.getSubmissionSetId(), MetadataSupport.xdsB_eb_assoc_type_has_member, doc.getAttributeValue(MetadataSupport.id_qname)))
				err("Document " + doc.getAttributeValue(MetadataSupport.id_qname) + " is not linked to Submission Set with " + MetadataSupport.xdsB_eb_assoc_type_has_member + " Association");

		}
	}

	void fols_in_ss() {
		ArrayList fols = m.getExtrinsicObjects();

		for (int i=0; i<fols.size(); i++) {
			OMElement fol = (OMElement) fols.get(i);

            if ( !has_assoc(m.getSubmissionSetId(), MetadataSupport.xdsB_eb_assoc_type_has_member, fol.getAttributeValue(MetadataSupport.id_qname)))
				err("Folder " + fol.getAttributeValue(MetadataSupport.id_qname) + " is not linked to Submission Set with " + MetadataSupport.xdsB_eb_assoc_type_has_member + " Association");

		}
	}

	String associationSimpleType(String assocType) {
		String[] parts = assocType.split(":");
		if (parts.length < 2)
			return assocType;
		return parts[parts.length-1];
	}

	void assocs_have_proper_namespace() {
		ArrayList<OMElement> assocs = m.getAssociations();

		for (OMElement a_ele : assocs) {
			String a_type = a_ele.getAttributeValue(MetadataSupport.association_type_qname);
			if (m.isVersion2() && a_type.startsWith("urn"))
				err("XDS.a does not accept namespace prefix on association type:  found " + a_type);
			if ( ! m.isVersion2()) {
				String simpleType = associationSimpleType(a_type);
				if (Metadata.iheAssocTypes.contains(simpleType)) {
					if ( !a_type.startsWith(MetadataSupport.xdsB_ihe_assoc_namespace_uri))
						err("XDS.b requires namespace prefix urn:ihe:iti:2007:AssociationType on association type " + simpleType )	;
				} else {
					if ( !a_type.startsWith(MetadataSupport.xdsB_eb_assoc_namespace_uri))
						err("XDS.b requires namespace prefix urn:oasis:names:tc:ebxml-regrep:AssociationType on association type " + simpleType )	;

				}
			}
		}
	}

	void rplced_doc_not_in_submission() throws MetadataException, MetadataValidationException {
		ArrayList assocs = m.getAssociations();

		for (int i=0; i<assocs.size(); i++) {
			OMElement assoc = (OMElement) assocs.get(i);
			String id = assoc.getAttributeValue(MetadataSupport.target_object_qname);
			String type = assoc.getAttributeValue(MetadataSupport.association_type_qname);
			if ((MetadataSupport.xdsB_ihe_assoc_type_rplc.equals(type)) && ! m.isReferencedObject(id))
				err("Replaced document (RPLC assocation type) cannot be in submission\nThe following objects were found in the submission:" 
						+ m.idsForObjects(m.getReferencedObjects()).toString());
		}
	}

	boolean has_assoc(String source, String type, String target) {
		ArrayList assocs = m.getAssociations();

		for (int i=0; i<assocs.size(); i++) {
			OMElement assoc = (OMElement) assocs.get(i);
			String a_target = assoc.getAttributeValue(MetadataSupport.target_object_qname);
			String a_type = assoc.getAttributeValue(MetadataSupport.association_type_qname);
			String a_source = assoc.getAttributeValue(MetadataSupport.source_object_qname);

			if (	a_target != null && a_target.equals(target) &&
					a_type   != null && a_type.  equals(type) &&
					a_source != null && a_source.equals(source)
			)
				return true;
		}		
		return false;
	}

	void err(String msg) {
		rel.add_error(MetadataSupport.XDSRegistryMetadataError, msg, this.getClass().getName(), null);
	}
}
