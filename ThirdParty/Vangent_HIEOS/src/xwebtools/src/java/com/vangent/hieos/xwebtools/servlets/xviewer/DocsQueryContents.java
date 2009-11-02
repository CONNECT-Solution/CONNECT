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

public class DocsQueryContents extends PagedQueryContents {

	public void displayStructure(QueryControl q_cntl, int index, HttpUtils h, XView xv, String cntl)
	throws MetadataValidationException, MetadataException {
		System.out.println("Docs QueryContents");

		Metadata m = getMetadata();		
		boolean object_refs = m.isObjectRefsOnly();
		if (m.getWrapper() != null) {
			xv.getMetadata().addMetadata(m);
			this.displayStructureHeader(index, xv, this.initial_evidence);
		} else {
			this.displayStructureHeader(index, xv, this.initial_evidence);
			return;
		}

		if (object_refs) {
		} else {
			
			for (String id : m.getExtrinsicObjectIds()) {
				h.indent1(xv.build_details_link(id, cntl) + 
						" (" + xv.build_xml_link(id, cntl) + 
						")" + 
						" {" + 
						((m.isRetrievable_a(id)) ? xv.build_ret_a_link(id) + " "  : ""  ) +
						((m.isRetrievable_b(id)) ? xv.build_ret_b_link(id) + " "  : ""  ) +
						((q_cntl.hasEndpoint()) ? xv.build_related_link(id) : "") + " " +
						((q_cntl.hasEndpoint()) ? xv.build_ss_link(id) : "") +
						"}");
			}
		}

	}

}
