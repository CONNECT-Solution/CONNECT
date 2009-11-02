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

import com.vangent.hieos.xwebtools.servlets.framework.HttpUtils;
import com.vangent.hieos.xutil.metadata.structure.MetadataTypes;
import com.vangent.hieos.xutil.exception.MetadataException;
import com.vangent.hieos.xutil.exception.MetadataValidationException;
import com.vangent.hieos.xutil.metadata.structure.Metadata;
import com.vangent.hieos.xutil.metadata.structure.MetadataParser;
import com.vangent.hieos.xutil.metadata.structure.MetadataSupport;
import com.vangent.hieos.xutil.response.RegistryErrorList;
import com.vangent.hieos.xutil.response.RegistryResponseParser;
import com.vangent.hieos.xutil.metadata.validation.Validator;
import com.vangent.hieos.xutil.xml.SchemaValidation;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import org.apache.axiom.om.OMElement;

public abstract class QueryContents {
	String reference_id;
	ArrayList<String> initial_evidence;
	OMElement result_xml;
	OMElement request_xml;
	Metadata m;
	String query_type;
	String endpoint = "";
	String header_extra = null;   // extra line to print as part of section header
	boolean secure = false;
	ArrayList<String> errors;
	ArrayList<String> fatal_errors;
	ArrayList<String> exceptions;

	abstract public void displayStructure(QueryControl q_cntl, int index, HttpUtils h, XView xv, String cntl)
	throws MetadataValidationException, MetadataException ;

	public QueryContents() {
		this.result_xml = null;
		this.request_xml = null;
		this.reference_id = null;
		this.m = new Metadata();
		this.query_type = "Unknown";
		this.initial_evidence = new ArrayList<String>();
		errors = new ArrayList<String>();
		exceptions = new ArrayList<String>();
		fatal_errors = new ArrayList<String>();
	}

	public QueryContents(Metadata m) {
		this.result_xml = null;
		this.request_xml = null;
		this.m = m;
		this.reference_id = null;
		this.query_type = "Unknown";
		this.initial_evidence = new ArrayList<String>();
		errors = new ArrayList<String>();
		exceptions = new ArrayList<String>();
		fatal_errors = new ArrayList<String>();
	}

	public void dup(QueryContents qc) {
		this.reference_id = qc.reference_id;
		this.initial_evidence = qc.initial_evidence;
		this.result_xml = qc.result_xml;
		this.request_xml = qc.request_xml;
		this.m = qc.m;
		this.query_type = qc.query_type;
		this.errors = qc.errors;
		this.fatal_errors = qc.fatal_errors;
		this.exceptions = qc.exceptions;
	}
	
	public void addHeaderExtra(String text) {
		if (header_extra == null) {
			header_extra = text;
		} else {
			header_extra = header_extra + text;
		}
	}

	void secure(boolean secure) {
		this.secure = secure;
	}

	void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public void setReferenceId(String ref_id) {
		this.reference_id = ref_id;
	}

	public void setInitialEvidence(ArrayList<String> value) {
		this.initial_evidence = value;
	}

	public String getReferenceId() { return this.reference_id; }

	void parse_exception(Exception e) {
		StackTraceElement[] st = e.getStackTrace();
		StringBuffer buf = new StringBuffer();
		for (StackTraceElement ste : st) {
			buf.append(ste.toString());
			buf.append("\n");
		}
		addException(e.getClass(), buf.toString());
	}

	void parse_metadata_from_registry_response(OMElement ele) {
		this.result_xml = ele;
		RegistryResponseParser rrp = new RegistryResponseParser(ele);
		ArrayList<String> errs = rrp.get_error_code_contexts();
		errors.addAll(errs);
		try {
			this.m = MetadataParser.parseNonSubmission(ele);
			validate();
		}
		catch (MetadataValidationException e) {
			parse_exception(e);
			this.m = MetadataParser.noParse(ele);
		}
		catch (MetadataException e) {
			parse_exception(e);
		}
	}

	void parse_metadata_from_log_file(OMElement ele) {
		this.m = null;
		try {
			this.m = MetadataParser.parseNonSubmission(ele);
		}
		catch (MetadataValidationException e) {
			parse_exception(e);
			this.m = MetadataParser.noParse(ele);
		}
		catch (MetadataException e) {
			parse_exception(e);
		}
		OMElement error = MetadataSupport.firstChildWithLocalName(ele, "Error");
		if (error != null)
			addError(error.getText());
	}



	public void setQueryType(String type) { this.query_type = type; }
	public String getQueryType() { return this.query_type; }

	public void setResultXML(OMElement xml) {
		result_xml = xml;
	}
	
	public void setRequestXML(OMElement xml) {
		request_xml = xml;
	}

	public void setMetadata(Metadata m) {
		this.m = m;
	}

	void add_lines(String lines, ArrayList<String> section) {
		String[] line = lines.split("\n");
		for (int i=0; i<line.length; i++)
			section.add(line[i]);
	}

	public void addError(String error) {
		add_lines(error, errors);
		add_lines("----------------------------------------------------------------------------", errors);
	}

	public void addFatalError(String error) {
		add_lines(error, fatal_errors);
		add_lines("----------------------------------------------------------------------------", fatal_errors);
	}

	public void addException(Class class_obj, String exception_message) {
		add_lines(class_obj.getName() + ": " + exception_message, exceptions);
		add_lines("----------------------------------------------------------------------------", exceptions);
	}

	public void addException(Exception e) {
		//addException(e.getClass(), e.getMessage());
		add_lines(this.exception_details(e, 10), exceptions);
		add_lines("----------------------------------------------------------------------------", exceptions);
	}

	public boolean hasExceptions() { return exceptions.size() > 0; }
	public boolean hasErrors() { return errors.size() > 0; }
	public boolean hasFatalErrors() { return fatal_errors.size() > 0; }

	public ArrayList<String> getErrors() {
		return errors;
	}

	public ArrayList<String> getExceptions() {
		return exceptions;
	}

	public ArrayList<String> getFatalErrors() {
		return fatal_errors;
	}

	public Metadata getMetadata() {
		return m;
	}

	public OMElement getResultXml() {
		return result_xml;
	}

	public OMElement getRequestXml() {
		return request_xml;
	}

	protected void displayStructureHeader(int index, XView xv) {
		StringBuffer buf = new StringBuffer();
		buf.append(this.getQueryType());
		if (secure) 
			buf.append(" SEC");
		if (request_xml != null) {
			buf.append(" (" + xv.build_query_request_xml_link(Integer.toString(index)) + ")");
		}
		if (result_xml != null) {
			buf.append(" (" + xv.build_query_results_xml_link(Integer.toString(index)) + ")");
		}
		if (hasExceptions()) buf.append(xv.h().link("___except", "/xwebtools/xviewer/exceptions?cntl=" + index));
		if (hasErrors()) buf.append(xv.h().link("___error", "/xwebtools/xviewer/errors?cntl=" + index));
		if (hasFatalErrors()) buf.append(xv.h().link("___fatal", "/xwebtools/xviewer/fatal?cntl=" + index));

		xv.h().indent0(buf.toString());
		xv.h().indent1(endpoint);
		
		if (this.header_extra != null)
			xv.h().indent1(this.header_extra);
	}


	protected void displayStructureHeader(int cntl, XView xv, ArrayList<String> value) {
		displayStructureHeader(cntl, xv);
	}

	void validate() {
		if (result_xml == null) {
			this.addError("No XML loaded to validate");
			return;
		}
		run_schema();
		run_validator();
	}

	void run_schema() {
		try {
			SchemaValidation.validate_local(result_xml	, MetadataTypes.METADATA_TYPE_SQ);
		} catch (Exception e) {
			this.addException(e);
		}
	}

	void run_validator() {
		try {
			RegistryErrorList registryErrorList = new RegistryErrorList(RegistryErrorList.version_3);

			Validator val = new Validator(	m, 
					registryErrorList, 
					false /* issubmit */, 
					true /* xds.b */,
					null /* logger */);
			val.run();
			String errors_and_warnings = registryErrorList.getErrorsAndWarnings();
			if (errors_and_warnings != null && !errors_and_warnings.equals("")) {
				this.addError(errors_and_warnings);
			}
		} catch (Exception e) {
			this.addException(e);
		}
	}

	protected String document_actions(QueryControl q_cntl, Metadata m, XView xv,
			String id) throws MetadataException {
				return " {" +
				((m.isRetrievable_a(id)) ? xv.build_ret_a_link(id) + " "  : ""  ) +
				((m.isRetrievable_b(id)) ? xv.build_ret_b_link(id) + " " : ""  ) +
				((q_cntl.hasEndpoint()) ? xv.build_related_link(id) : "") +
				"}";
			
			}

	public static String exception_details(Exception e, int levels) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(baos);
		e.printStackTrace(ps);

		String[] lines = (new String(baos.toByteArray())).split("\n");
		StringBuffer buf = new StringBuffer();
		for (int i=0; i<lines.length && i< levels; i++)
			buf.append(lines[i] + "\n");

		return "Exception thrown: " + e.getClass().getName() + "\n" + e.getMessage() + "\n" + buf.toString();
	}


}
