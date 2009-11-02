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

package com.vangent.hieos.services.xds.registry.storedquery;

import com.vangent.hieos.xutil.exception.MetadataValidationException;
import com.vangent.hieos.xutil.exception.XdsException;
import com.vangent.hieos.xutil.exception.XdsInternalException;
import com.vangent.hieos.xutil.metadata.structure.Metadata;
import com.vangent.hieos.xutil.metadata.structure.MetadataParser;
import com.vangent.hieos.xutil.metadata.structure.MetadataSupport;
import com.vangent.hieos.xutil.response.Response;
import com.vangent.hieos.xutil.query.StoredQuery;
import com.vangent.hieos.xutil.xlog.client.XLogMessage;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.axiom.om.OMElement;

public class GetRelatedDocuments extends StoredQuery {

	public GetRelatedDocuments(HashMap<String, Object> params, boolean return_objects, Response response, XLogMessage log_message, boolean is_secure)
            throws MetadataValidationException {
		super(params, return_objects, response, log_message,  is_secure);

		//                         param name,                             required?, multiple?, is string?,   same size as,    alternative
		validate_parm(params, "$XDSDocumentEntryUniqueId",                 true,      false,     true,         null,            "$XDSDocumentEntryEntryUUID");
		validate_parm(params, "$XDSDocumentEntryEntryUUID",                true,      false,     true,         null,            "$XDSDocumentEntryUniqueId");
		validate_parm(params, "$AssociationTypes",                         true,      true,      true,         null,            null);

        if (this.has_validation_errors) {
			throw new MetadataValidationException("Metadata Validation error present");
        }
    }

	public Metadata run_internal() throws XdsException {
		Metadata metadata = new Metadata();

		String uid 			= get_string_parm("$XDSDocumentEntryUniqueId");
		String uuid 		= get_string_parm("$XDSDocumentEntryEntryUUID");
		ArrayList<String> assoc_types 	= get_arraylist_parm("$AssociationTypes");
		ArrayList<String> doc_ids_to_query_for = new ArrayList<String>();
		ArrayList<String> doc_ids = new ArrayList<String>();

		if (assoc_types == null || assoc_types.size() == 0) {
			throw new XdsInternalException("No $AssociationTypes specified in query");
		}
		
		// filter HasMember out of assoc_types if it exists
		ArrayList<String> assoc_types2 = new ArrayList<String>();
		for (String type : assoc_types) {
            if (!MetadataSupport.xdsB_eb_assoc_type_has_member.equals(type))
				assoc_types2.add(type);
		}
		assoc_types = assoc_types2;

		// if uuid supplied, add id to doc_ids_to_query_for since metadata not yet fetched
		// if uid supplied, query to get metadata and add uuid to doc_ids since we do have metadata
		// already loaded into Metadata metadata (which is the return vessel)
		if (uid != null) {
			OMElement ele = get_doc_by_uid(uid);
			metadata = MetadataParser.parseNonSubmission(ele);
			doc_ids.addAll(metadata.getExtrinsicObjectIds());
		} else {
			if ( uuid != null ) {
				doc_ids_to_query_for.add(uuid);
			}
			else throw new XdsInternalException("GetDocuments Stored Query: uuid not found, uid not found");
		}

		if (doc_ids.size() == 0 && doc_ids_to_query_for.size() == 0) 
			return metadata;

		// combined_ids is all relevant doc ids, whether or not metadata has been retrieved yet
		ArrayList<String> combined_ids = new ArrayList<String>();
		combined_ids.addAll(doc_ids);
		combined_ids.addAll(doc_ids_to_query_for);
		
		// load all associations related to combined_ids
		OMElement associations = get_associations(combined_ids, assoc_types);
		Metadata association_metadata = MetadataParser.parseNonSubmission(associations);
		
		// no associations => empty return
		if (association_metadata.getAssociations().size() == 0) {
			return new Metadata();
		}

		// discover ids (potentially for documents) that are referenced by the associations
		// but are not yet on one of the lists
		for (OMElement assoc : association_metadata.getAssociations()) {
			String obj1_id = assoc.getAttributeValue(MetadataSupport.source_object_qname);
			String obj2_id = assoc.getAttributeValue(MetadataSupport.target_object_qname);

			if ( !metadata.getExtrinsicObjectIds().contains(obj1_id)  &&
					!doc_ids_to_query_for.contains(obj1_id)) {
				doc_ids_to_query_for.add(obj1_id);
			}
			if ( !metadata.getExtrinsicObjectIds().contains(obj2_id)  &&
					!doc_ids_to_query_for.contains(obj2_id)) {
				doc_ids_to_query_for.add(obj2_id);
			}
		}
		
		// load documents from our potential-document ids
		metadata.addMetadata(get_doc_by_uuid(doc_ids_to_query_for));

		// add associations to final list that reference included documents at both ends
		ArrayList<String> loaded_doc_ids = metadata.getExtrinsicObjectIds();
		for (OMElement assoc : association_metadata.getAssociations()) {
			String obj1_id = assoc.getAttributeValue(MetadataSupport.source_object_qname);
			String obj2_id = assoc.getAttributeValue(MetadataSupport.target_object_qname);
			
			if (loaded_doc_ids.contains(obj1_id) && loaded_doc_ids.contains(obj2_id)) 
				metadata.add_association(assoc);
		}	

	return metadata;
}


}
