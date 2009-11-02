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
import java.util.List;
import java.util.Map;

import com.vangent.hieos.xutil.exception.MetadataException;
import com.vangent.hieos.xutil.exception.MetadataValidationException;

public class RetrieveAQueryContents extends QueryContents {
	String content_type;
	byte[] contents;
	String id;
	Map<String, List<String>> header_fields;
	
	public void displayStructure(QueryControl q_cntl, int index, HttpUtils h, XView xv, String cntl)
	throws MetadataValidationException, MetadataException {

		displayStructureHeader(index, xv, this.initial_evidence);

		h.indent1(xv.build_details_link(id, cntl) + 
				" (" + xv.build_xml_link(id, cntl) + " " +
				xv.build_display_doc_link(String.valueOf(index), id) + " " +
				xv.build_headers_link(id, cntl) +
				")"
				);
	}	
	
	public void display_headers() {
		for (String header_name : header_fields.keySet()) {
			List<String> header_values = header_fields.get(header_name);
			
		}
	}
	
	public void set_contents(byte[] contents, String content_type) {
		this.contents = contents;
		this.content_type = content_type;
	}
	
	public void setHeaderFields(Map<String, List<String>> header_fields) {
		this.header_fields = header_fields;
	}
	
	public void set_id(String id) {
		this.id = id;
	}
	
	public String get_content_type() {
		return this.content_type;
	}
	
	public byte[] get_content() {
		return this.contents;
	}
	

}
