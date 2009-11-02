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
import com.vangent.hieos.xutil.response.Response;
import com.vangent.hieos.xutil.query.StoredQuery;
import com.vangent.hieos.xutil.xlog.client.XLogMessage;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.axiom.om.OMElement;

public class GetDocuments extends StoredQuery {

	public GetDocuments(HashMap<String, Object> params, boolean return_objects, Response response, XLogMessage log_message, boolean is_secure)
            throws MetadataValidationException {
		super(params, return_objects, response, log_message,  is_secure);


		//                         param name,                             required?, multiple?, is string?,   same size as,    alternative
		validate_parm(params, "$XDSDocumentEntryUniqueId",                 true,      true,     true,         null,            "$XDSDocumentEntryEntryUUID");
		validate_parm(params, "$XDSDocumentEntryEntryUUID",                true,      true,     true,         null,            "$XDSDocumentEntryUniqueId");

        if (this.has_validation_errors) {
			throw new MetadataValidationException("Metadata Validation error present");
        }
    }

	public Metadata run_internal() throws XdsException {
		Metadata metadata;

		ArrayList<String> uids = get_arraylist_parm("$XDSDocumentEntryUniqueId");
		ArrayList<String> uuids = get_arraylist_parm("$XDSDocumentEntryEntryUUID");

		if (uids != null) {
			OMElement ele = get_doc_by_uid(uids);
			metadata = MetadataParser.parseNonSubmission(ele);
		} else
			if ( uuids != null ) {
				OMElement ele = get_doc_by_uuid(uuids);
				metadata = MetadataParser.parseNonSubmission(ele);
			}
			else throw new XdsInternalException("GetDocuments Stored Query: uuid not found, uid not found");

		return metadata;
	}


}
