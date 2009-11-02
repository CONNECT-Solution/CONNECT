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

public class FindSubmissionSets extends StoredQuery {

	public FindSubmissionSets(HashMap params, boolean return_objects, Response response, XLogMessage log_message, boolean is_secure)
            throws MetadataValidationException {
		super(params, return_objects, response, log_message,  is_secure);


		//                         param name,                                      required?, multiple?, is string?,   same size as,                                alternative
		validate_parm(params, "$XDSSubmissionSetPatientId",                         true,      false,     true,         null,                                          null												);
		validate_parm(params, "$XDSSubmissionSetSourceId",                          false,     true,      true,         null,                                          null												);
		validate_parm(params, "$XDSSubmissionSetSubmissionTimeFrom",                false,     false,     true,         null,                                          null												);
		validate_parm(params, "$XDSSubmissionSetSubmissionTimeTo",                  false,     false,     true,         null,                                          null												);
		validate_parm(params, "$XDSSubmissionSetAuthorPerson",                      false,     false,     true,         null,                                          null												);
		validate_parm(params, "$XDSSubmissionSetContentType",                       false,     true,      true,         null,                                          null												);
		validate_parm(params, "$XDSSubmissionSetStatus",                            true,      true,      true,         null,                                          null												);
		
		if (this.has_validation_errors) {
			throw new MetadataValidationException("Metadata Validation error present");
        }
	}

	public Metadata run_internal() throws XdsInternalException, XdsException {

		OMElement results = impl();
				
		Metadata m = MetadataParser.parseNonSubmission(results);
		
		if (log_message != null)
			log_message.addOtherParam("Results structure", m.structure());

		return m;
	}
	
	OMElement impl() throws XdsInternalException, XdsException {
		
		String               patient_id                               = this.get_string_parm   ("$XDSSubmissionSetPatientId");
		ArrayList<String>    source_id                                = this.get_arraylist_parm("$XDSSubmissionSetSourceId");
		String               submission_time_from                     = this.get_int_parm      ("$XDSSubmissionSetSubmissionTimeFrom");
		String               submission_time_to                       = this.get_int_parm      ("$XDSSubmissionSetSubmissionTimeTo");
		String               author_person                            = this.get_string_parm   ("$XDSSubmissionSetAuthorPerson");
		ArrayList<String>    content_type                             = this.get_arraylist_parm("$XDSSubmissionSetContentType");
		ArrayList<String>    status                                   = this.get_arraylist_parm("$XDSSubmissionSetStatus");

		init();

		if (this.return_leaf_class) {
		     a("SELECT *  "); n();
		} else {
		     a("SELECT doc.id  "); n();
		}
		                                                  a("FROM RegistryPackage doc, ExternalIdentifier patId"); n();
		if (source_id != null)                            a(", ExternalIdentifier srcId"); n();
		if (submission_time_from != null)                 a(", Slot sTimef"); n();              
		if (submission_time_to != null)                   a(", Slot sTimet"); n();              
		if (author_person != null)                        a(", Classification author"); n();
		if (author_person != null)                        a(", Slot authorperson"); n();
		if (content_type != null)                         a(", Classification contentT"); n();
				
		
		                                                  a("WHERE"); n();
		// patientID
		                                                  a("(doc.id = patId.registryobject AND	"); n();
		                                                  a("  patId.identificationScheme='urn:uuid:6b5aea1a-874d-4603-a4bc-96a0a7b38446' AND "); n();
		                                                  a("  patId.value = '"); a(patient_id); a("' ) "); n();
		                      
		if (source_id != null) {
			a("AND"); n();
            a("(doc.id = srcId.registryobject AND	"); n();
            a("  srcId.identificationScheme='urn:uuid:554ac39e-e3fe-47fe-b233-965d2a147832' AND "); n();
            a("  srcId.value IN "); a(source_id); a(" ) "); n();
		}
		this.add_times("submissionTime",     "sTimef",       "sTimet",       submission_time_from,      submission_time_to, "doc");
		
		if (author_person != null) {
			a("AND"); n();
			a("(doc.id = author.classifiedObject AND "); n();
			a("  author.classificationScheme='urn:uuid:a7058bb9-b4e4-4307-ba5b-e3f0ab85e12d' AND "); n();
			a("  authorperson.parent = author.id AND"); n();
			a("  authorperson.name = 'authorPerson' AND"); n();
			a("  authorperson.value LIKE '" + author_person + "' )"); n();
		}
		                                                  
		this.add_code("contentT", null, "urn:uuid:aa543740-bdda-424e-8c96-df4873be8500", content_type,            null);
		
		a("AND doc.status IN "); a(status);
		
		
			return query(this.return_leaf_class);
		

	}
	
	
}
