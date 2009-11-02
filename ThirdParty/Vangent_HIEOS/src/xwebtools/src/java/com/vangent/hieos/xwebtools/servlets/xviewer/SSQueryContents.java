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

import org.apache.axiom.om.OMElement;

import com.vangent.hieos.xutil.exception.MetadataException;
import com.vangent.hieos.xutil.exception.MetadataValidationException;
import com.vangent.hieos.xutil.metadata.structure.Metadata;

public class SSQueryContents extends PagedQueryContents {

	public void displayStructure(QueryControl q_cntl, int index, HttpUtils h, XView xv, String cntl)
	throws MetadataValidationException, MetadataException  {
		System.out.println("SS Query Contents");

		Metadata m = getMetadata();	
		if (m == null) {
			displayStructureHeader(index, xv);
			return;
		}
		
		if (m.getWrapper() != null) {
			xv.getMetadata().addMetadata(m);
			displayStructureHeader(index, xv);
		} else {
			displayStructureHeader(index, xv);
			return;
		}

		
		boolean object_refs = m.isObjectRefsOnly();
		if (object_refs) {
			h.indent1(this.size() + " elements " + xv.build_next_page_link(String.valueOf(index)));
		} else {
			String ref_id = this.getReferenceId();
			for (OMElement assoc_ele : m.getAssociations()) {
				String source_id = m.getAssocSource(assoc_ele);
				h.indent1(xv.build_details_link(ref_id, cntl) + " (" + xv.build_xml_link(ref_id, cntl) + ")");

				h.indent2(xv.build_assoc_link(m.getId(assoc_ele), "member of", cntl) +
						" (" + xv.build_details_link(source_id, cntl) +
						" " + xv.build_xml_link(source_id, cntl) +
						") {" + xv.build_sscontents_link(source_id) +
						"} "
				);
			}
		}

	}


}
