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

package com.vangent.hieos.xwebtools.servlets.xviewer;

import java.util.ArrayList;
import org.apache.axiom.om.OMElement;

import com.vangent.hieos.xutil.metadata.structure.MetadataSupport;

public class GetRelatedDocuments extends Sq {
	ArrayList<String> ids = null;

	String query_details() {
		return "GetRelatedDocuments " + ids;
	}

	OMElement build(ArrayList<String> ids) {
		this.ids = ids;
		ArrayList<OMElement> query = build_query_wrapper("urn:uuid:d90e5407-b356-4d91-a89f-873917b4b0e6");

		if (ids.get(0).startsWith("urn:uuid:")) 
			add_slot(query, "$XDSDocumentEntryEntryUUID", this.query_singleton(ids));
		else
			add_slot(query, "$XDSDocumentEntryUniqueId", this.query_singleton(ids));

		ArrayList<String> atypes = new ArrayList<String>();


		atypes.add("('" + MetadataSupport.xdsB_ihe_assoc_type_rplc + "')");
        atypes.add("('" + MetadataSupport.xdsB_ihe_assoc_type_xfrm + "')");
		atypes.add("('" + MetadataSupport.xdsB_ihe_assoc_type_apnd + "')");
		atypes.add("('" + MetadataSupport.xdsB_ihe_assoc_type_xfrm_rplc + "')");
		atypes.add("('" + MetadataSupport.xdsB_ihe_assoc_type_signs + "')");
		atypes.add("('" + MetadataSupport.xdsB_eb_assoc_type_has_member + "')");

		add_slot(query, "$AssociationTypes", atypes);
		return query.get(0);
	}


}
