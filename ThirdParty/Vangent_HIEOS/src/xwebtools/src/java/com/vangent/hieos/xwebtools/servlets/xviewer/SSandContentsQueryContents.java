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

import java.util.ArrayList;

import org.apache.axiom.om.OMElement;

public class SSandContentsQueryContents extends QueryContents {

	public void displayStructure(QueryControl q_cntl, int index, HttpUtils h, XView xv, String cntl)
	throws MetadataValidationException, MetadataException  {
		System.out.println("SSandContents Query Contents");

		Metadata m = getMetadata();		
		boolean object_refs = m.isObjectRefsOnly();
		if (m.getWrapper() != null) {
			xv.getMetadata().addMetadata(m);
			displayStructureHeader(index, xv, this.initial_evidence);
		} else {
			displayStructureHeader(index, xv, this.initial_evidence);
			return;
		}

		ArrayList<String> ids_displayed = new ArrayList<String>();

		if (m.getSubmissionSetIds().size() != 0) {
			String ss_id = m.getSubmissionSetId();
			ids_displayed.add(m.getSubmissionSetId());

			h.indent1(xv.build_details_link(ss_id, cntl) + " (" + xv.build_xml_link(ss_id, cntl) + ")");

			ids_displayed.add(ss_id);
			for (OMElement a_ele : m.getAssociations()) {
				ids_displayed.add(m.getId(a_ele));
				String a_id = m.getId(a_ele);
				String src_id = m.getAssocSource(a_ele);
				String tgt_id = m.getAssocTarget(a_ele);
				if ( src_id.equals(ss_id)) {
					h.indent2(xv.build_assoc_link(a_id, "contains", cntl) + " " + 
							xv.build_details_link(tgt_id, cntl) + " (" + 
							xv.build_xml_link(tgt_id, cntl) + " " + ") " +
							document_actions(q_cntl, m, xv, tgt_id)
					);
					ids_displayed.add(tgt_id);
				} 
			}
		}
		for (OMElement ele : m.getAllObjects()) {
			String id = m.getId(ele);
			String type = m.type(id);
			if ( !ids_displayed.contains(id)) {
				if (type == null) {
					//h.indent1("null");
				}
				else 
					if (type.equals("Association")) {
					h.indent1(xv.symbolic(m.getAssocSource(ele)) + " " + type + " " + xv.symbolic(m.getAssocTarget(ele)));
				} else if (!object_refs && type.equals("ObjectRef")) {

				} else {
					h.indent1(type + " " + " (" + xv.build_xml_link(id, cntl) + ")");
				}
			}
		}

	}

}
