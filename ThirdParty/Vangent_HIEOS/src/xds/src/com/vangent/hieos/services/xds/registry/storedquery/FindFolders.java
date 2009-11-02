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

import com.vangent.hieos.xutil.exception.MetadataException;
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

public class FindFolders extends StoredQuery {

	public FindFolders(HashMap<String, Object> params, boolean return_objects, Response response, XLogMessage log_message, boolean is_secure)
            throws MetadataValidationException {
		super(params, return_objects, response, log_message,  is_secure);


		//                    param name,                      required?, multiple?, is string?,   same size as,                        alternative
		validate_parm(params, "$XDSFolderPatientId",             true,      false,     true,         null,                                  null);
		validate_parm(params, "$XDSFolderLastUpdateTimeFrom",    false,     false,     false,        null,                                  null);
		validate_parm(params, "$XDSFolderLastUpdateTimeTo",      false,     false,     false,        null,                                  null);
		validate_parm(params, "$XDSFolderCodeList",              false,     true,      true,         "$XDSFolderCodeListScheme",            null);
		validate_parm(params, "$XDSFolderCodeListScheme",        false,     true,      true,         "$XDSFolderCodeList",                  null);
		validate_parm(params, "$XDSFolderStatus",                true,      true,      true,         null,                                  null);
        if (this.has_validation_errors) {
			throw new MetadataValidationException("Metadata Validation error present");
        }
    }

	public Metadata run_internal() throws XdsException {
		OMElement results = impl();
		
		Metadata m = MetadataParser.parseNonSubmission(results);
		
		if (log_message != null)
			log_message.addOtherParam("Results structure", m.structure());

		return m;
	}
	
	
	OMElement impl() throws XdsInternalException, MetadataException, XdsException {
		String patient_id              = get_string_parm("$XDSFolderPatientId");
		String update_time_from        = get_int_parm("$XDSFolderLastUpdateTimeFrom");
		String update_time_to          = get_int_parm("$XDSFolderLastUpdateTimeTo");
		ArrayList<String> codes        = get_arraylist_parm("$XDSFolderCodeList");
		ArrayList<String> code_schemes = get_arraylist_parm("$XDSFolderCodeListScheme");
		ArrayList<String> status       = get_arraylist_parm("$XDSFolderStatus");

		if (patient_id == null || patient_id.length() == 0) throw new XdsException("Patient ID parameter empty");
		if (status.size() == 0) throw new XdsException("Status parameter empty");

		init();
		
		if (this.return_leaf_class) {
		     a("SELECT *  "); n();
		} else {
		     a("SELECT fol.id  "); n();
		}
        
		a("FROM RegistryPackage fol, ExternalIdentifier patId"); n();
        if (update_time_from != null)         a(", Slot updateTimef"); n();
        if (update_time_to != null)         a(", Slot updateTimet"); n();
        if (codes != null)                  a(", Classification code"); n();
        if (code_schemes != null)           a(", Slot codescheme"); n();           
                                 a("WHERE"); n();

                         		// patientID
        a("(fol.id = patId.registryobject AND	"); n();
        a("  patId.identificationScheme='urn:uuid:f64ffdf0-4b97-4e06-b79f-a52b38ec2f8a' AND "); n();
        a("  patId.value = '"); a(patient_id); a("' ) AND"); n();
        a("  fol.status IN "); a(status); n();
		add_times("lastUpdateTime",     "updateTimef",       "updateTimet",       update_time_from,      update_time_to, "fol");

		return query(this.return_leaf_class);

	}
}
