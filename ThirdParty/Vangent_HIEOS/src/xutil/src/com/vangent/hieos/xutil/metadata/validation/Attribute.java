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

package com.vangent.hieos.xutil.metadata.validation;

import com.vangent.hieos.xutil.exception.MetadataException;
import com.vangent.hieos.xutil.exception.MetadataValidationException;
import com.vangent.hieos.xutil.exception.XdsInternalException;
import com.vangent.hieos.xutil.metadata.structure.Metadata;
import com.vangent.hieos.xutil.metadata.structure.MetadataSupport;
import com.vangent.hieos.xutil.response.RegistryErrorList;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.axiom.om.OMElement;

public class Attribute {
	Metadata m;
	RegistryErrorList rel;
	boolean is_submit;
	boolean xds_b;

	ArrayList<String> ss_slots;
	ArrayList<String> doc_slots;
	ArrayList<String> fol_slots;

	public Attribute(Metadata m, boolean is_submit, boolean xds_b) throws XdsInternalException {
		this.m = m;
		rel = new RegistryErrorList( 
				(m.isVersion2()) ? RegistryErrorList.version_2 : RegistryErrorList.version_3, 
						false /* log */);
		this.is_submit = is_submit;
		this.xds_b = xds_b;

		init();

	}

	public Attribute(Metadata m, boolean is_submit, boolean xds_b, RegistryErrorList rel) throws XdsInternalException {
		this.m = m;
		this.rel = rel;
		this.is_submit = is_submit;
		this.xds_b = xds_b;

		init();

	}

	private void init() {
		ss_slots = new ArrayList<String>();
		// required
		ss_slots.add( "submissionTime");

		doc_slots = new ArrayList<String>();
		// required 
		doc_slots.add("creationTime");
		doc_slots.add("languageCode");
		doc_slots.add("hash");
		doc_slots.add("size");
		doc_slots.add("sourcePatientId");
		doc_slots.add("sourcePatientInfo");
		// optional
		doc_slots.add("intendedRecipient");
		doc_slots.add("legalAuthenticator");
		doc_slots.add("serviceStartTime");
		doc_slots.add("serviceStopTime");
		doc_slots.add("URI");
		doc_slots.add("repositoryUniqueId");

		fol_slots = new ArrayList<String>();
		// optional
		fol_slots.add("lastUpdateTime");
	}

	public void run()  throws MetadataException, MetadataValidationException {
		validate_ss();
		validate_documents();
		validate_fols();
		validate_necessary_atts();
	}

	void validate_documents() throws MetadataException {
		validate_document_slots_are_legal();
	}

	void validate_ss() throws MetadataException {
		validate_ss_slots_are_legal();
	}

	void validate_fols() throws MetadataException {
		validate_fol_slots_are_legal();
	}

	void validate_necessary_atts()  throws MetadataException {

		validate_ss_extids();
		validate_ss_slots();

		validate_doc_slots();
		validate_doc_extids();

		validate_folder_slots();
		validate_folder_extids();

		validate_ss_class();
		validate_doc_class();
		validate_fol_class();

		validate_package_class();

		validate_special_ss_slot_structure();
		validate_special_doc_slot_structure();
		validate_special_fol_slot_structure();
		validate_special_assoc_slot_structure();

		validate_special_class_structure();
		validate_special_fol_class_structure();


		// validate special structure in some classifications and slots and associations and externalids
	}


	void validate_special_class_structure() throws MetadataException {
		ArrayList<OMElement> classs = m.getClassifications();

		for (int i=0; i<classs.size(); i++) {
			OMElement class_ele = (OMElement) classs.get(i);
			String class_scheme = class_ele.getAttributeValue(MetadataSupport.classificationscheme_qname);
			String classified_object_id = class_ele.getAttributeValue(MetadataSupport.classified_object_qname);
			OMElement classified_object_ele = m.getObjectById(classified_object_id);
			String classified_object_type = (classified_object_ele == null) ? "Unknown type" : classified_object_ele.getLocalName();
			String nodeRepresentation = class_ele.getAttributeValue(MetadataSupport.noderepresentation_qname);

			if (	class_scheme != null &&
					(class_scheme.equals("urn:uuid:93606bcf-9494-43ec-9b4e-a7748d1a838d")  ||

							class_scheme.equals("urn:uuid:a7058bb9-b4e4-4307-ba5b-e3f0ab85e12d"))
			) {
				// doc.author or ss.author
				
				if (nodeRepresentation == null || (nodeRepresentation != null && !nodeRepresentation.equals("")))
					err(classified_object_type + " " + classified_object_id + " has a author type classification (classificationScheme=" +
							class_scheme + ") with no nodeRepresentation attribute.  It is required and must be the empty string."	);


				String author_person = m.getSlotValue(class_ele, "authorPerson", 0);
				if (author_person == null)
					err(classified_object_type + " " + classified_object_id + " has a author type classification (classificationScheme=" +
							class_scheme + ") with no authorPerson slot.  One is required."	);
//				if ( ! is_xcn_format(author_person))
//					err(classified_object_type + " " + classified_object_id + " has a author type classification (classificationScheme=" +
//							class_scheme + ") with authorPerson slot that is not in XCN format. The value found was " + author_person	);

				if (m.getSlotValue(class_ele, "authorPerson", 1) != null) 
					err(classified_object_type + " " + classified_object_id + " has a author type classification (classificationScheme=" +
							class_scheme + ") with multiple values in the authorPerson slot.  Only one is allowed. To document a second author, create a second Classification object"	);

				for (OMElement slot : MetadataSupport.childrenWithLocalName(class_ele, "Slot")) {
					String slot_name = slot.getAttributeValue(MetadataSupport.slot_name_qname);
					if ( 	slot_name != null &&
							(slot_name.equals("authorPerson") ||
									slot_name.equals("authorInstitution") ||
									slot_name.equals("authorRole") ||
									slot_name.equals("authorSpecialty") )){

					} else {
						err(classified_object_type + " " + classified_object_id + " has a author type classification (classificationScheme=" +
								class_scheme + ") with an unknown type of slot with name " + slot_name + ".  Only XDS prescribed slots are allowed inside this classification"	);

					}
				}
			}

		}
	}
	
	boolean is_xcn_format(String value) {
		int count = 0;
		for (int i=0; i<value.length(); i++) {
			if (value.charAt(i) == '^') count++;
		}
		return (count == 5);
	}

	void validate_special_fol_class_structure() {
		// none
	}

	void validate_special_assoc_slot_structure() {
		// none
	}

	void validate_special_ss_slot_structure() {
		// there is none
	}

	void validate_special_fol_slot_structure() throws MetadataException {
		// this is none
	}

	void validate_special_doc_slot_structure() throws MetadataException {
		ArrayList doc_ids = m.getExtrinsicObjectIds();

		for (int i=0; i<doc_ids.size(); i++) {
			String id = (String) doc_ids.get(i);
			ArrayList slots = m.getSlots(id);

			
			
			for (int s=0; s<slots.size(); s++) {
				OMElement slot = (OMElement) slots.get(s);
				String slot_name = slot.getAttributeValue(MetadataSupport.slot_name_qname);

				if (slot_name == null)
					continue;

				if (slot_name.equals("legalAuthenticator")) {

				} 
				else if (slot_name.equals("sourcePatientId")) {

				}
				else if (slot_name.equals("sourcePatientInfo")) {

				}
				else if (slot_name.equals("intendedRecipient")) {

				}
				else if (slot_name.equals("URI")) {

				}
			}
		}
	}

	void validate_package_class() throws MetadataException {
		ArrayList rp_ids = m.getRegistryPackageIds();

		for (int i=0; i<rp_ids.size(); i++) {
			String id = (String) rp_ids.get(i);

			int ss_class_count = 0;
			int fol_class_count = 0;

//			ArrayList classs = m.getClassifications(id);
//			for (int c=0; c<classs.size(); c++) {
//			OMElement class_ele = (OMElement) classs.get(c);
//			String classification_node = class_ele.getAttributeValue(MetadataSupport.classificationnode_qname);
//			if (classification_node.equals("urn:uuid:a54d6aa5-d40d-43f9-88c5-b4633d873bdd")) {
//			//ss
//			ss_class_count++;
//			} else if (classification_node.equals("urn:uuid:d9d542f3-6cc4-48b6-8870-ea235fbc94c2")) {
//			// fol
//			fol_class_count++;
//			}
//			}

			ArrayList ext_classs = m.getClassifications();
			for (int c=0; c<ext_classs.size(); c++) {
				OMElement class_ele = (OMElement) ext_classs.get(c);
				String classified_id = class_ele.getAttributeValue(MetadataSupport.classified_object_qname);
				if ( classified_id == null || !id.equals(classified_id))
					continue;
				String classification_node = class_ele.getAttributeValue(MetadataSupport.classificationnode_qname);
				if (classification_node != null && classification_node.equals("urn:uuid:a54d6aa5-d40d-43f9-88c5-b4633d873bdd")) {
					//ss
					ss_class_count++;
				} else if (classification_node != null && classification_node.equals("urn:uuid:d9d542f3-6cc4-48b6-8870-ea235fbc94c2")) {
					// fol
					fol_class_count++;
				}
			}

			if (ss_class_count + fol_class_count == 0)
				err("RegistryPackage" + " " + id + " : is not Classified as either a Submission Set or Folder: " +
						"Submission Set must have classification urn:uuid:a54d6aa5-d40d-43f9-88c5-b4633d873bdd " +
				"and Folder must have classification urn:uuid:d9d542f3-6cc4-48b6-8870-ea235fbc94c2");
			if (ss_class_count + fol_class_count > 1)
				err("RegistryPackage" + " " + id + " : is Classified multiple times: " +
						"Submission Set must have single classification urn:uuid:a54d6aa5-d40d-43f9-88c5-b4633d873bdd " +
				"and Folder must have single classification urn:uuid:d9d542f3-6cc4-48b6-8870-ea235fbc94c2");
		}
	}

	void validate_doc_class() throws MetadataException {
		ArrayList doc_ids = m.getExtrinsicObjectIds();

		for (int i=0; i<doc_ids.size(); i++) {
			String id = (String) doc_ids.get(i);
			ArrayList classs = m.getClassifications(id);

			//                                               classificationScheme								name							required	multiple
			this.validate_class("Document", id, classs, "urn:uuid:41a5887f-8865-4c09-adf7-e362475b143a" , 	"classCode",						true, 		false);
			this.validate_class("Document", id, classs, "urn:uuid:f4f85eac-e6cb-4883-b524-f2705394840f" , 	"confidentialityCode",		true, 		true);
			this.validate_class("Document", id, classs, "urn:uuid:2c6b8cb7-8b2a-4051-b291-b1ae6a575ef4" , 	"eventCodeList",				false, 		true);
			this.validate_class("Document", id, classs, "urn:uuid:a09d5840-386c-46f2-b5ad-9c3699a4309d" , 	"formatCode",				true, 		false);
			this.validate_class("Document", id, classs, "urn:uuid:f33fb8ac-18af-42cc-ae0e-ed0b0bdb91e1" , 	"healthCareFacilityTypeCode",				true, 		false);
			this.validate_class("Document", id, classs, "urn:uuid:cccf5598-8b07-4b77-a05e-ae952c785ead" , 	"practiceSettingCode",				true, 		false);
			this.validate_class("Document", id, classs, "urn:uuid:41a5887f-8865-4c09-adf7-e362475b143a" , 	"typeCode",				true, 		false);
		}
	}

	void validate_fol_class() throws MetadataException {
		ArrayList fol_ids = m.getFolderIds();

		for (int i=0; i<fol_ids.size(); i++) {
			String id = (String) fol_ids.get(i);
			ArrayList classs = m.getClassifications(id);

			//                                               classificationScheme								name							required	multiple
			if (this.is_submit)
				this.validate_class("Folder", id, classs, "urn:uuid:1ba97051-7806-41a8-a48b-8fce7af683c5" , 	"codeList",						false, 		false);
			else
				this.validate_class("Folder", id, classs, "urn:uuid:1ba97051-7806-41a8-a48b-8fce7af683c5" , 	"codeList",						false, 		true);
		}
	}

	void validate_ss_class() throws MetadataException {
		ArrayList ss_ids = m.getSubmissionSetIds();

		for (int i=0; i<ss_ids.size(); i++) {
			String id = (String) ss_ids.get(i);
			ArrayList classs = m.getClassifications(id);

			//                                               classificatinScheme								name							required	multiple
			this.validate_class("SubmissionSet", id, classs, "urn:uuid:aa543740-bdda-424e-8c96-df4873be8500" , 	"contentTypeCode",		true, 		 false);
		}		
	}

	void validate_ss_extids() throws MetadataException {
		ArrayList ss_ids = m.getSubmissionSetIds();

		for (int i=0; i<ss_ids.size(); i++) {
			String id = (String) ss_ids.get(i);
			ArrayList slots = m.getSlots(id);
			ArrayList ext_ids = m.getExternalIdentifiers(id);

			//													name							identificationScheme                          OID required
			this.validate_ext_id("Submission Set", id, ext_ids, "XDSSubmissionSet.patientId", "urn:uuid:6b5aea1a-874d-4603-a4bc-96a0a7b38446", false);
			this.validate_ext_id("Submission Set", id, ext_ids, "XDSSubmissionSet.sourceId", "urn:uuid:554ac39e-e3fe-47fe-b233-965d2a147832",  true);
			this.validate_ext_id("Submission Set", id, ext_ids, "XDSSubmissionSet.uniqueId", "urn:uuid:96fdda7c-d067-4183-912e-bf5ee74998a8",  true);

		}
	}

	void validate_ss_slots() throws MetadataException {
		ArrayList ss_ids = m.getSubmissionSetIds();

		for (int i=0; i<ss_ids.size(); i++) {
			String id = (String) ss_ids.get(i);
			ArrayList slots = m.getSlots(id);
			ArrayList ext_ids = m.getExternalIdentifiers(id);

			//                      					name						multi	required	number
			validate_slot("Submission Set", id, slots, 	"submissionTime", 			false, 	true, 		true);

		}
	}

	void validate_doc_extids() throws MetadataException {
		ArrayList doc_ids = m.getExtrinsicObjectIds();

		for (int i=0; i<doc_ids.size(); i++) {
			String id = (String) doc_ids.get(i);
			ArrayList slots = m.getSlots(id);
			ArrayList ext_ids = m.getExternalIdentifiers(id);


			//													name							identificationScheme                    OID required
			this.validate_ext_id("Document", id, ext_ids, "XDSDocumentEntry.patientId", "urn:uuid:58a6f841-87b3-4a3e-92fd-a8ffeff98427", false);
			// the oid^ext format is tested in UniqueId.java?
			this.validate_ext_id("Document", id, ext_ids, "XDSDocumentEntry.uniqueId", "urn:uuid:2e82c1f6-a085-4c72-9da3-8640a32e42ab",  false);

		}
	}


	void validate_doc_slots() throws MetadataException {
		ArrayList doc_ids = m.getExtrinsicObjectIds();

		for (int i=0; i<doc_ids.size(); i++) {
			String id = (String) doc_ids.get(i);
			OMElement docEle = m.getObjectById(id);
			ArrayList slots = m.getSlots(id);
			ArrayList ext_ids = m.getExternalIdentifiers(id);

			//                      				name						multi	required	number
			validate_slot("Document", id, slots, 	"creationTime", 			false, 	true, 		true);
			validate_slot("Document", id, slots, 	"hash",			 			false, 	true, 		false);
			validate_slot("Document", id, slots, 	"intendedRecipient",		true, 	false, 		false);
			validate_slot("Document", id, slots, 	"languageCode",				false, 	true, 		false);
			validate_slot("Document", id, slots, 	"legalAuthenticator",		false, 	false, 		false);
			validate_slot("Document", id, slots, 	"serviceStartTime",			false, 	false, 		true);
			validate_slot("Document", id, slots, 	"serviceStopTime",			false, 	false, 		true);
			validate_slot("Document", id, slots, 	"size",						false, 	true, 		true);
			validate_slot("Document", id, slots, 	"sourcePatientInfo",		true, 	true, 		false);
			
			// These are tricky since the validation is based on the metadata format (xds.a or xds.b) instead of
			// on the transaction. All Stored Queries are encoded in ebRIM 3.0 (xds.b format) but they could
			// represent an xds.a registry.  The use of is_submit below is an adequate stop-gap measure
			if (xds_b) {
				validate_slot("Document", id, slots, 	"URI",					true, 	false, 		false);
				validate_slot("Document", id, slots, 	"repositoryUniqueId",	false, 	is_submit, 		false);
				if (m.getSlot(id, "URI") != null)
					m.getURIAttribute(docEle);
			}
			else {
				validate_slot("Document", id, slots, 	"URI",					true, 	is_submit, 		false);
				m.getURIAttribute(docEle);
			}

		}
	}

	void validate_folder_extids()  throws MetadataException {
		ArrayList fol_ids = m.getFolderIds();
		for (int i=0; i<fol_ids.size(); i++) {
			String id = (String) fol_ids.get(i);
			ArrayList slots = m.getSlots(id);
			ArrayList ext_ids = m.getExternalIdentifiers(id);

			//													name							identificationScheme            OID required
			this.validate_ext_id("Folder", id, ext_ids, "XDSFolder.patientId", "urn:uuid:f64ffdf0-4b97-4e06-b79f-a52b38ec2f8a", false);
			this.validate_ext_id("Folder", id, ext_ids, "XDSFolder.uniqueId", "urn:uuid:75df8f67-9973-4fbe-a900-df66cefecc5a",  true);

		}

	}

	void validate_folder_slots() throws MetadataException {
		ArrayList fol_ids = m.getFolderIds();
		for (int i=0; i<fol_ids.size(); i++) {
			String id = (String) fol_ids.get(i);
			ArrayList slots = m.getSlots(id);
			ArrayList ext_ids = m.getExternalIdentifiers(id);

			//                      					name						multi	required	number
			// query only
			if (is_submit)
				validate_slot("Folder", id, slots, 		"lastUpdateTime", 			false, 	false, 		true);
			else
				validate_slot("Folder", id, slots, 		"lastUpdateTime", 			false, 	true, 		true);

		}

	}

	void validate_class(String type, String id, ArrayList classs, String classification_scheme, String class_name, boolean required, boolean multiple) {
		int count = 0;
		for (int i=0; i<classs.size(); i++ ) {
			OMElement classif = (OMElement) classs.get(i);

			String scheme = classif.getAttributeValue(MetadataSupport.classificationscheme_qname);
			if ( scheme == null || !scheme.equals(classification_scheme))
				continue;
			count++;

			OMElement name_ele = MetadataSupport.firstChildWithLocalName(classif, "Name") ;
			if (name_ele == null) 
				err(type + " " + id + " : Classification of type " + classification_scheme + " ( " + class_name + " ) the name attribute is missing");

			OMElement slot_ele = MetadataSupport.firstChildWithLocalName(classif, "Slot") ;
			if (slot_ele == null) {
				err(type + " " + id + " : Classification of type " + classification_scheme + " ( " + class_name + " ) the slot 'codingScheme' is missing");
				continue;
			}
			String slot_name = slot_ele.getAttributeValue(MetadataSupport.slot_name_qname);
			if (slot_name == null || slot_name.equals(""))
				err(type + " " + id + " : Classification of type " + classification_scheme + " ( " + class_name + " ) the slot 'codingScheme' is missing");
		}
		if (count == 0 && required)
			err(type + " " + id + " : Classification of type " + classification_scheme + " ( " + class_name + " ) is missing");
		if (count > 1 && !multiple)
			err(type + " " + id + " : Classification of type " + classification_scheme + " ( " + class_name + " ) is duplicated");
	}

	void validate_ext_id(String type, String id, ArrayList e_ids, String name, String id_scheme, boolean is_oid) {
		int count = 0;
		for (int i=0; i<e_ids.size(); i++) {
			OMElement e_id = (OMElement) e_ids.get(i);
			String idscheme = e_id.getAttributeValue(MetadataSupport.identificationscheme_qname);
			if ( idscheme == null || ! idscheme.equals(id_scheme))
				continue;
			count++;

			String name_value = m.getNameValue(e_id);
			if (name_value == null)
				err(type + " " + id + " : ExternalIdentifier of type " + id_scheme + " (" + name + ") has no internal Name element");
			else if ( !name_value.equals(name))
				err(type + " " + id + " : ExternalIdentifier of type " + id_scheme + " (" + name + ") has incorrect internal Name element (" + name_value + ")");


			int child_count = 0;
			for (Iterator it = e_id.getChildElements(); it.hasNext(); ) {
				OMElement child = (OMElement) it.next();
				child_count++;
				String child_type = child.getLocalName();
				if ( !child_type.equals("Name") && !child_type.equals("Description") && !child_type.equals("VersionInfo")) 
					err(type + " " + id + " : ExternalIdentifier of type " + id_scheme + " (" + name + ") has invalid internal element (" + child_type + ")");
			}
			if (is_oid) {
				String value = e_id.getAttributeValue(MetadataSupport.value_qname);
				if (value == null || value.equals("") || !is_oid(value))
					err(type + " " + id + " : ExternalIdentifier of type " + id_scheme + " (" + name + ") requires an OID format value, " + value + " was found");
			}

		}
		if (count == 0)
			err(type + " " + id + " : ExternalIdentifier of type " + id_scheme + " (" + name + ") is missing");
		if (count > 1)
			err(type + " " + id + " : ExternalIdentifier of type " + id_scheme + " (" + name + ") is duplicated");
	}
	
	static boolean is_oid(String value) {
		if (value == null) return false;
		return value.matches("\\d(?=\\d*\\.)(?:\\.(?=\\d)|\\d){0,63}");
//		for (int i=0; i<value.length(); i++) {
//			char c = value.charAt(i);
//			if ( !(c == '.') && !Character.isDigit(c)) return false;
//		}
//		return true;
	}

	void validate_slot(String type, String id, ArrayList slots, String name, boolean multivalue, boolean required, boolean number) {
		boolean found = false;
		for (int i=0; i<slots.size(); i++) {
			OMElement slot = (OMElement) slots.get(i);
			String slot_name = slot.getAttributeValue(MetadataSupport.slot_name_qname);
			if ( slot_name == null || !slot_name.equals(name))
				continue;
			if (found)
				err(type + " " + id + " has multiple slots with name " + name);
			found = true;
			OMElement value_list = MetadataSupport.firstChildWithLocalName(slot, "ValueList");
			int value_count = 0;
			for (Iterator it=value_list.getChildElements(); it.hasNext(); ) {
				OMElement value = (OMElement) it.next();
				String value_string = value.getText();
				value_count++;
				if (number) {
					try {
						prove_int(value_string);
					} catch (Exception e) {
						err(type + " " + id + " the value of slot " + name + "(" + value_string + ") is required to be an integer");
					}
				} 
			}
			if (	(value_count > 1 && ! multivalue)   ||
					value_count == 0
			) 
				err(type + " " + id + " has slot " + name + " is required to have a single value");

		}
		if ( !found && required)
			err(type + " " + id + " does not have the required slot " + name);
	}

	void prove_int(String value) throws Exception {
		for (int i=0; i<value.length(); i++) {
			char c = value.charAt(i);
			switch (c) {
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':
			case '8':
			case '9':
			case '0':
				return;
			}
			throw new Exception("Not an integer");
		}
	}

	void validate_ss_slots_are_legal() throws MetadataException {
		String ss_id = m.getSubmissionSetId();

		ArrayList slots = m.getSlots(ss_id);
		for (int j=0; j<slots.size(); j++) {
			OMElement slot = (OMElement) slots.get(j);

			String slot_name = slot.getAttributeValue(MetadataSupport.slot_name_qname);
			if (slot_name == null) slot_name = "";
			if ( ! legal_ss_slot_name(slot_name))
				err("Submission Set " + ss_id + ": " + slot_name + " is not a legal slot name for a Submission Set");
		}
	}

	void validate_fol_slots_are_legal() throws MetadataException {
		ArrayList fol_ids = m.getFolderIds();

		for (int i=0; i<fol_ids.size(); i++) {
			String id = (String) fol_ids.get(i);

			ArrayList slots = m.getSlots(id);
			for (int j=0; j<slots.size(); j++) {
				OMElement slot = (OMElement) slots.get(j);

				String slot_name = slot.getAttributeValue(MetadataSupport.slot_name_qname);
				if (slot_name == null) slot_name = "";
				if ( ! legal_fol_slot_name(slot_name))
					err("Folder " + id + ": " + slot_name + " is not a legal slot name for a Folder");
			}
		}
	}

	void validate_document_slots_are_legal() throws MetadataException {
		ArrayList doc_ids = m.getExtrinsicObjectIds();

		for (int i=0; i<doc_ids.size(); i++) {
			String id = (String) doc_ids.get(i);

			ArrayList slots = m.getSlots(id);
			for (int j=0; j<slots.size(); j++) {
				OMElement slot = (OMElement) slots.get(j);

				String slot_name = slot.getAttributeValue(MetadataSupport.slot_name_qname);
				if (slot_name == null) slot_name = "";
				if ( ! legal_doc_slot_name(slot_name))
					err("Document " + id + ": " + slot_name + " is not a legal slot name for a Document");
			}
		}
	}

	boolean legal_ss_slot_name(String name) {
		if (name == null) return false;
		if (name.startsWith("urn:")) return true;
		return ss_slots.contains(name);
	}

	boolean legal_doc_slot_name(String name) {
		if (name == null) return false;
		if (name.startsWith("urn:")) return true;
		return doc_slots.contains(name);
	}

	boolean legal_fol_slot_name(String name) {
		if (name == null) return false;
		if (name.startsWith("urn:")) return true;
		return fol_slots.contains(name);
	}

	void err(String msg) {
		rel.add_error(MetadataSupport.XDSRegistryMetadataError, msg, this.getClass().getName(), null);
	}

}
