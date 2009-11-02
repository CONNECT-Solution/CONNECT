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

import org.apache.axiom.om.OMElement;

public class RelatedQueryContents extends QueryContents {

	public void displayStructure(QueryControl q_cntl, int index, HttpUtils h, XView xv, String cntl)
	throws MetadataValidationException, MetadataException {
		System.out.println("Related QueryContents");
		this.displayStructureHeader(index, xv);

		Metadata m = getMetadata();		
		boolean object_refs = m.isObjectRefsOnly();
		if (m.getWrapper() == null) return; 

		xv.getMetadata().addMetadata(m);

		
		if (object_refs) {
			// no idea what to do here
		} else {
			String ref_id = this.getReferenceId();
			h.indent1(xv.build_details_link(ref_id, cntl) + " (" + xv.build_xml_link(ref_id, cntl) + " " +
					((m.isRetrievable_a(ref_id)) ? xv.build_ret_a_link(ref_id) + " "  : ""  ) +
					((m.isRetrievable_b(ref_id)) ? xv.build_ret_b_link(ref_id) + " "  : ""  ) +
					")");

            // AMS 04/24/2009 - FIXME - Refactor this - 'else-if' and 'else' are pretty similar
			for (OMElement a_ele : m.getAssociations()) {
				String src_id = m.getAssocSource(a_ele);
				String a_type = m.getAssocType(a_ele);
				String a_id = m.getId(a_ele);
				String tgt_id = m.getAssocTarget(a_ele);
				if (src_id == null) {
					
				} else if (src_id != null && !src_id.equals(this.getReferenceId())) {
                    if (MetadataSupport.xdsB_ihe_assoc_type_rplc.equals(a_type)) a_type = "replaced by";
					else if (MetadataSupport.xdsB_ihe_assoc_type_apnd.equals(a_type)) a_type = "has appended";
					else if (MetadataSupport.xdsB_ihe_assoc_type_xfrm.equals(a_type)) a_type = "transformed to";
					else a_type = a_type + " by";
					h.indent2(xv.build_assoc_link(a_id, a_type, cntl) + " " +
							xv.build_details_link(src_id, cntl) + 
							" (" + xv.build_xml_link(src_id, cntl)  + " " +
							((m.isRetrievable_a(tgt_id)) ? xv.build_ret_a_link(tgt_id) + " "  : ""  ) +
							((m.isRetrievable_b(tgt_id)) ? xv.build_ret_b_link(tgt_id) + " "  : ""  ) +
							") " +
							" [" + ((q_cntl.hasEndpoint()) ? xv.build_related_link(src_id) : "") +
					"]");
				} else {
                    if (MetadataSupport.xdsB_ihe_assoc_type_rplc.equals(a_type)) a_type = "replaces";
					else if (MetadataSupport.xdsB_ihe_assoc_type_apnd.equals(a_type)) a_type = "appended to";
					else if (MetadataSupport.xdsB_ihe_assoc_type_xfrm.equals(a_type)) a_type = "transform of";

					h.indent2(xv.build_assoc_link(a_id, a_type, cntl) + " " +
							xv.build_details_link(tgt_id, cntl) + 
							" (" + xv.build_xml_link(tgt_id, cntl) + " " + 
							((m.isRetrievable_a(tgt_id)) ? xv.build_ret_a_link(tgt_id) + " "  : ""  ) +
							((m.isRetrievable_b(tgt_id)) ? xv.build_ret_b_link(tgt_id) + " "  : ""  ) +
							") " +
							" [" + ((q_cntl.hasEndpoint()) ? xv.build_related_link(tgt_id) : "") +
					"]");
				}
			}
		}

	}

}
