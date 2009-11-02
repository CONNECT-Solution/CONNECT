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
import com.vangent.hieos.xutil.metadata.structure.MetadataSupport;
import com.vangent.hieos.xutil.response.Response;
import com.vangent.hieos.xutil.query.StoredQuery;
import com.vangent.hieos.xutil.xlog.client.XLogMessage;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.axiom.om.OMElement;

public class GetSubmissionSets extends StoredQuery {

	public GetSubmissionSets(HashMap<String, Object> params, boolean return_objects, Response response, XLogMessage log_message, boolean is_secure)
        throws MetadataValidationException {
        super(params, return_objects, response, log_message,  is_secure);


		//                    param name,             required?, multiple?, is string?,   same size as,    alternative
		validate_parm(params, "$uuid",                 true,      true,     true,         null,            null);

        if (this.has_validation_errors) {
			throw new MetadataValidationException("Metadata Validation error present");
        }
    }

	public Metadata run_internal() throws XdsException {
		Metadata metadata;

		ArrayList<String> uuids = get_arraylist_parm("$uuid");

		if (uuids != null && uuids.size() > 0) {
			OMElement ele = get_submissionsets(uuids);
			// this may contain duplicates - parse differently
			metadata = new Metadata();
			//metadata.setGrokMetadata(false);
			metadata.addMetadata(ele, true);

			if (metadata.getSubmissionSetIds().size() > 0) {
				OMElement assocs_ele = this.get_associations(MetadataSupport.xdsB_eb_assoc_type_has_member, metadata.getSubmissionSetIds(), uuids);
				metadata.addMetadata(assocs_ele, true);
			}
		} 
		else throw new XdsInternalException("GetSubmissionSets Stored Query: $uuid not found");

		return metadata;
	}

	protected OMElement get_submissionsets(ArrayList<String> uuids) throws XdsException {
		init();
		if (this.return_leaf_class)
			a("SELECT * FROM RegistryPackage rp, Association a, ExternalIdentifier ei");
		else
			a("SELECT rp.id FROM RegistryPackage rp, Association a, ExternalIdentifier ei");  
		n();
		a("WHERE ");  n();
		a("	  a.sourceObject = rp.id AND");  n(); 
        a("   a.associationType = '");
        a(MetadataSupport.xdsB_eb_assoc_type_has_member);
        a("' AND"); n();
		a("	  a.targetObject IN "); a(uuids);  a(" AND"); n(); 
		a("   ei.registryObject = rp.id AND"); n();
		a("   ei.identificationScheme = 'urn:uuid:6b5aea1a-874d-4603-a4bc-96a0a7b38446'"); n();

		return query(this.return_leaf_class);
	}

	protected OMElement get_associations(String type, ArrayList<String> froms, ArrayList<String> tos) 
	throws XdsException {
		init();
		if (this.return_leaf_class)
			a("SELECT * FROM Association a");
		else
			a("SELECT a.id FROM Association a");  
		n();
		a("WHERE ");  n();
		a("  a.associationType = '" + type + "' AND"); n();
		a("  a.sourceObject IN"); a(froms); a(" AND"); n();
		a("  a.targetObject IN"); a(tos); n();


		return query(this.return_leaf_class);
	}

}
