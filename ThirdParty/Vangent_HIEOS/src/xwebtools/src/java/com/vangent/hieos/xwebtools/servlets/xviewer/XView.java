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
import com.vangent.hieos.xutil.exception.MetadataException;
import com.vangent.hieos.xutil.exception.MetadataValidationException;
import com.vangent.hieos.xutil.metadata.structure.Metadata;
import com.vangent.hieos.xutil.metadata.structure.MetadataSupport;
import com.vangent.hieos.xutil.xml.XmlFormatter;

import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMElement;

public class XView {
	Metadata m;
	HttpUtils oc;
	HashMap<String, String> symbolic_ids = new HashMap<String, String>(); // uuid -> symbolic


	public XView(Metadata m, HttpUtils oc) {
		this.oc = oc;
		this.m = m;
	}


	public XView(HttpUtils oc)  {
		this.oc = oc;
		this.m = new Metadata();
	}

	public HttpUtils h() { return oc; }

	void out(String s) {
		//System.out.println(s);
		oc.o(s);
	}

	String symbolic(String id, String type) {
		String symbolic = symbolic_ids.get(id);
		if (symbolic == null) {
			int i = 1;
			while (true) {
				String new_symbolic = type + Integer.toString(i);
				if (symbolic_ids.containsValue(new_symbolic)) {
					i++;
					continue;
				}
				symbolic_ids.put(id, new_symbolic);
				return new_symbolic;
			}
		}
		return symbolic;
	}

	String symbolic(String id) {
		String symbolic = symbolic_ids.get(id);
		if (symbolic != null) 
			return symbolic;
		try {
			String type = m.type(id);
			if (type != null) {
				if (type.equals("ExtrinsicObject") )
					type = "Doc";
				else if (type.equals("RegistryPackage")) {
					if (m.isSubmissionSet(id))
						type = "SS";
					else if (m.isFolder(id))
						type = "Fol";
				}
				else if (type.equals("Association"))
					type = "Assoc";
				else if (type.equals("ObjectRef"))
					return "Ref";
				return symbolic(id, type);
			}
		} catch (Exception e) {

		}
		return id;
	}

	static String build_link(String href, String text) {
		return "<a href=\"" + href + "\">" + text + "</a>"; 
	}

	String build_link(String href, String target, String text) {
		return "<a href=\"" + href + "\" target=\"" + target + "\">" + text + "</a>"; 
	}

	String build_ssac_link(String id) {
		return build_link("/xwebtools/xviewer/ssac?id=" + id, symbolic(id));
	}

	String build_sscontents_link(String id) {
		return build_link("/xwebtools/xviewer/ssac?id=" + id, "SSandcontents");
	}

	String build_details_link(String id, String cntl) {
		if (cntl == null)
			return build_link("/xwebtools/xviewer/details?id=" + id, symbolic(id));
		else
			return build_link("/xwebtools/xviewer/details?id=" + id + "&cntl=" + cntl, symbolic(id));
	}

	String build_related_link(String id) {
		return build_link("/xwebtools/xviewer/related?id=" + id, "related");
	}

	String build_ret_a_link(String id) {
		return build_link("/xwebtools/xviewer/ret_a?id=" + id, "reta");
	}

	String build_ret_b_link(String id) {
		return build_link("/xwebtools/xviewer/ret_b?id=" + id, "retb");
	}

	String build_display_doc_link(String cntl, String id) {
		return build_link("/xwebtools/xviewer/display_doc?cntl=" + cntl + "&id=" + id, "_new", "display");
	}

	String build_ss_link(String id) {
		return build_link("/xwebtools/xviewer/ss?id=" + id, "ss");
	}

	String build_assoc_link(String id, String type, String cntl) {
		if (cntl == null)
			return build_link("/xwebtools/xviewer/xml?id=" + id, type);
		else 
			return build_link("/xwebtools/xviewer/xml?id=" + id + "&cntl=" + cntl, type);
	}

	String build_xml_link(String id, String cntl) {
		if (cntl == null)
			return build_link("/xwebtools/xviewer/xml?id=" + id, "xml");
		else
			return build_link("/xwebtools/xviewer/xml?id=" + id + "&cntl=" + cntl, "xml");
	}

	String build_headers_link(String id, String cntl) {
		if (cntl == null)
			return build_link("/xwebtools/xviewer/display_headers?id=" + id, "headers");
		else
			return build_link("/xwebtools/xviewer/display_headers?id=" + id + "&cntl=" + cntl, "headers");
	}

	String build_query_results_xml_link(String index) {
		return build_link("/xwebtools/xviewer/queryresponsexml?cntl=" + index, "RawResult");
	}

	String build_query_request_xml_link(String index) {
		return build_link("/xwebtools/xviewer/queryrequestxml?cntl=" + index, "RawRequest");
	}

	static String build_next_page_link(String index) {
		return build_link("/xwebtools/xviewer/nextpage?cntl=" + index, "next");
	}


	public void displayOutline(QueryControl q_cntl, int index) 
	throws MetadataException, MetadataValidationException {
		QueryContents qc = q_cntl.getAllQueryContents().get(index);
		qc.displayStructure(q_cntl, index, oc, this, null);
	}


	void scan() {
		OMElement ss = m.getSubmissionSet();
		if (ss != null)
			symbolic(m.getId(ss), "SS");

		for (String id : m.getExtrinsicObjectIds())
			symbolic(id, "Doc");

		for (String id : m.getSubmissionSetIds())
			symbolic(id, "SS");

		for (String id : m.getFolderIds())
			symbolic(id, "Fol");

		for (OMElement ele : m.getAssociations()) {
			String id = m.getId(ele);
			symbolic(id, "Assoc");
		}
	}

	String xml_anchor(String id) {
		return " (<a href=\"#" + symbolic(id) + "XML" + "\">" + "XML" + "</a>)";
	}

	String display_anchor(String id) {
		String sym = symbolic(id);
		return "<a href=\"#" + sym + "\">" + sym + "</a>";
	}

	void doc_code_row(String id, String uuid, String label) throws MetadataException {
		ArrayList<OMElement> classifications = m.getClassifications(id);
		for (OMElement class_ele : classifications) {
			String class_uuid = class_ele.getAttributeValue(MetadataSupport.classificationscheme_qname);
			if (class_uuid != null && class_uuid.equals(uuid)) {
				row11111(null, 
						label,
						class_ele.getAttributeValue(MetadataSupport.noderepresentation_qname),
						m.getNameValue(class_ele),
						m.getSlotValue(class_ele, "codingScheme", 0));
			}
		}
	}

	public void run() throws MetadataException {
		scan();

		start();
		for (OMElement ele: m.getAssociations()) {
			String a_id = m.getId(ele);
			String src_id = m.getSourceObject(ele);
			String tar_id = m.getTargetObject(ele);
			row (display_anchor(src_id), 
					strip_namespace(m.getAssocType(ele)) + xml_anchor(a_id), 
					display_anchor(tar_id));
		}
		end();

		if (m.getAssociations().size() != 0)
			out("<hr />");

		for (String id : m.getSubmissionSetIds()) {
			displaySS(m, id);
		}

		for (String id: m.getExtrinsicObjectIds()) {
			displayDoc(m, id);
		}

		for (String id: m.getFolderIds()) {
			displayFolder(m, id);
		}

        /* BHT (DISABLED)
		Validator v = new Validator(m);
		// two element - testname, test status
		ArrayList<ArrayList> al = v.getPatterns();
		for (ArrayList<String> elements : al) {
			row (elements.get(0), elements.get(1), elements.get(2));
		}
        */
	}

	private void displayFolder(Metadata m, String id) throws MetadataException {
		OMElement fol = m.getObjectById(id);
		out("<div id=\"" + symbolic(id) + "\">");
		start();
		row(null, "is", "Folder");
		row(null, "Name", m.getNameValue(id));
		row(null, "Description", m.getDescriptionValue(id));
		row113(null, "status", m.getStatus(fol));
		row113(null, "home", m.getHome(fol));
		row113(null, "patient ID", m.getExternalIdentifierValue(id, MetadataSupport.XDSFolder_patientid_uuid));
		row113(null, "unique ID", m.getExternalIdentifierValue(id, MetadataSupport.XDSFolder_uniqueid_uuid));
		row113(null, "update time", m.getSlotValue(fol, "lastUpdateTime", 0));
		doc_code_row(id, "urn:uuid:1ba97051-7806-41a8-a48b-8fce7af683c5", "codeList");
		end();
	}

	public void displayRegistryObject(String id) throws MetadataException {
		displayRegistryObject(m, id);
	}

	public void displayRegistryObject(Metadata m, String id) throws MetadataException {
		if (m.isDocument(id)) {
			displayDoc(m, id);
		} else if (m.isSubmissionSet(id)) {
			displaySS(m, id);
		} else if (m.isFolder(id)) {
			this.displayFolder(m, id);
		} else {
			System.out.println("id " + id + "  is not a document or submission set");
		}
	}

	public void displayDoc(Metadata m, String id) throws MetadataException {
		OMElement eo = m.getObjectById(id);
		if (eo == null) {
			System.out.println("displayDoc: unknown id " + id);
			return;
		}
		start();
		row(null, "is", "ExtrinsicObject");
		row(null, "id", id);
		row113(null, "name", m.getNameValue(id));
		row113(null, "desc", m.getDescriptionValue(id));
		row113(null, "status", m.getStatus(eo));
		row113(null, "home", m.getHome(eo));
		row113(null, "URI", m.getSlotValue(eo, "URI", 0));
		row113(null, "repositoryUniqueId", m.getSlotValue(eo, "repositoryUniqueId", 0));
		row11111(null, "size/mime_type", m.getSlotValue(eo, "size", 0), eo.getAttributeValue(MetadataSupport.mime_type_qname),"");
		row113(null, "hash", m.getSlotValue(eo, "hash", 0));
		row113(null, "patient ID", m.getExternalIdentifierValue(id, MetadataSupport.XDSDocumentEntry_patientid_uuid));
		row113(null, "unique ID", m.getExternalIdentifierValue(id, MetadataSupport.XDSDocumentEntry_uniqueid_uuid));
		row11111(null, "creation/service time", m.getSlotValue(eo, "creationTime", 0), m.getSlotValue(eo, "serviceStartTime", 0), m.getSlotValue(eo, "serviceStopTime", 0));
		doc_code_row(id, "urn:uuid:41a5887f-8865-4c09-adf7-e362475b143a", "classCode");
		doc_code_row(id, "urn:uuid:f4f85eac-e6cb-4883-b524-f2705394840f", "confCode");
		doc_code_row(id, "urn:uuid:2c6b8cb7-8b2a-4051-b291-b1ae6a575ef4", "eventCode");
		doc_code_row(id, "urn:uuid:a09d5840-386c-46f2-b5ad-9c3699a4309d", "formatCode");
		doc_code_row(id, "urn:uuid:f33fb8ac-18af-42cc-ae0e-ed0b0bdb91e1", "hcftCode");
		doc_code_row(id, "urn:uuid:cccf5598-8b07-4b77-a05e-ae952c785ead", "practiceSettingCode");
		end();
	}

	public void displaySS(Metadata m, String id) throws MetadataException {
		OMElement ss = m.getObjectById(id);
		if (ss == null) {
			System.out.println("displaySS: unknown id " + id);
			return;
		}
		out("<div id=\"" + symbolic(id) + "\">");

		start();
		row(null, "is", "SubmissionSet");
		row(null, "id", id);
		row113(null, "name", m.getNameValue(id));
		row113(null, "desc", m.getDescriptionValue(id));
		row113(null, "status", m.getStatus(ss));
		row113(null, "home", m.getHome(ss));
		row113(null, "patient ID", m.getExternalIdentifierValue(id, MetadataSupport.XDSSubmissionSet_patientid_uuid));
		row113(null, "unique ID", m.getExternalIdentifierValue(id, MetadataSupport.XDSSubmissionSet_uniqueid_uuid));
		row113(null, "source ID", m.getExternalIdentifierValue(id, MetadataSupport.XDSSubmissionSet_sourceid_uuid));
		row113(null, "submisssion time", m.getSlotValue(id, "submissionTime", 0));
		ArrayList<String> ctypes = m.getClassificationsValues(id, "urn:uuid:aa543740-bdda-424e-8c96-df4873be8500");
		for (String ctype : ctypes) {
			row113(null, "content type", ctype);
		}
		ArrayList<OMElement> authors = m.getClassifications(id, "urn:uuid:a7058bb9-b4e4-4307-ba5b-e3f0ab85e12d");
		for (OMElement author : authors) {
			String author_name = m.getSlotValue(author, "authorPerson", 0);
			row113(null, "author", author_name);
			for (int i=0; i<10; i++) {
				String author_role = m.getSlotValue(author, "authorRole", i);
				if (author_role != null && !author_role.equals(""))
					row1112(null, null, "role", author_role);
			}
			for (int i=0; i<10; i++) {
				String author_role = m.getSlotValue(author, "authorSpecialty", i);
				if (author_role != null && !author_role.equals(""))
					row1112(null, null, "specialty", author_role);
			}
			for (int i=0; i<10; i++) {
				String author_role = m.getSlotValue(author, "authorInstitution", i);
				if (author_role != null && !author_role.equals(""))
					row1112(null, null, "institution", author_role);
			}
		}
		end();
	}

	String strip_namespace(String name) {
		String[] parts = name.split(":");
		if (parts.length == 0) return name;
		return parts[parts.length - 1];
	}

	void displayXml(String id) throws MetadataException {
		displayXml(m, id);
	}

	void displayXml(Metadata m, String id) throws MetadataException {
		OMElement ele = m.getObjectById(id);
		if (ele == null) throw new MetadataException("displayXml(): id " + id + " is not a known object");
		out(XmlFormatter.htmlize(ele.toString(), false));
	}

	void xml() throws MetadataException {

		for (String ss_id : m.getSubmissionSetIds()) {
			OMElement ss = m.getObjectById(ss_id);
			out("<h3>" + symbolic(ss_id) + "</h3>");
			out("<div id=\"" + symbolic(ss_id) + "XML" + "\">");
			out(XmlFormatter.htmlize(ss.toString(), false));
			out("</div>");
		}


		for (String id: m.getExtrinsicObjectIds()) {
			OMElement eo = m.getObjectById(id);
			out("<h3>" + symbolic(id) + "</h3>");
			out("<div id=\"" + symbolic(id) + "XML" + "\">");
			out(XmlFormatter.htmlize(eo.toString(), false));
			out("</div>");
		}

		for (String id: m.getFolderIds()) {
			OMElement eo = m.getObjectById(id);
			out("<h3>" + symbolic(id) + "</h3>");
			out("<div id=\"" + symbolic(id) + "XML" + "\">");
			out(XmlFormatter.htmlize(eo.toString(), false));
			out("</div>");
		}

		for (OMElement ele: m.getAssociations()) {
			String id = ele.getAttributeValue(new QName("id"));
			out("<h3>" + symbolic(id) + "</h3>");
			out("<div id=\"" + symbolic(id) + "XML" + "\">");
			out(XmlFormatter.htmlize(ele.toString(), false));
			out("</div>");
		}


	}

	public void header() {
		System.out.println(
				"<html>\n" +
				"<head/>\n" +
				"<body>\n" 
		);
	}

	public void footer() {
		System.out.println(
				"</body>\n" +
				"</html>\n"
		);
	}

	public void start() {
		out("<table>");
	}

	public void end() {
		out("</table>");
	}

	public void row(String l, String m, String r) {
		if (l == null) l = "";
		if (m == null) m = "";
		if (r == null) r = "";
		out("<tr>");
		out("<td>" + l + "</td>");
		out("<td>" + m + "</td>");
		out("<td colspan=3>" + r + "</td>");
		out("</tr>");
	}

	public void row122(String l, String m, String r) {
		if (l == null) l = "";
		if (m == null) m = "";
		if (r == null) r = "";
		out("<tr>");
		out("<td>" + l + "</td>");
		out("<td colspan=2>" + m + "</td>");
		out("<td colspan=2>" + r + "</td>");
		out("</tr>");
	}

	public void row1112(String l, String m, String mm, String r) {
		if (l == null) l = "";
		if (m == null) m = "";
		if (mm == null) mm = "";
		if (r == null) r = "";
		out("<tr>");
		out("<td>" + l + "</td>");
		out("<td>" + m + "</td>");
		out("<td>" + mm + "</td>");
		out("<td colspan=2>" + r + "</td>");
		out("</tr>");
	}

	public void row11111(String l, String m, String mm, String r, String rr) {
		if (l == null) l = "";
		if (m == null) m = "";
		if (mm == null) mm = "";
		if (r == null) r = "";
		if (rr == null) rr = "";
		out("<tr>");
		out("<td>" + l + "</td>");
		out("<td>" + m + "</td>");
		out("<td>" + mm + "</td>");
		out("<td>" + r + "</td>");
		out("<td>" + rr + "</td>");
		out("</tr>");
	}

	public void row113(String l, String m, String r) {
		if (l == null) l = "";
		if (m == null) m = "";
		if (r == null) r = "";
		out("<tr>");
		out("<td>" + l + "</td>");
		out("<td colspan=1>" + m + "</td>");
		out("<td colspan=3>" + r + "</td>");
		out("</tr>");
	}

	public void row(String only) {
		out( "<tr><td colspan=5>" + only + "</td></tr>");
	}

	public Metadata getMetadata() { return m; }

//	static public void main(String[] args) {
//	if (args.length == 0) {
//	System.err.println("filename arg required");
//	System.exit(-1);
//	}
//	Xdsview xv = new Xdsview(new File(args[0]));
//	xv.header();
//	xv.start();
//	try {
//	xv.run();
//	}
//	catch (Exception e) {
//	System.err.println(e.getMessage());
//	}
//	xv.end();
//	try {
//	xv.xml();
//	}
//	catch (Exception e) {
//	System.err.println(e.getMessage());
//	}
//	xv.footer();
//	}

}
