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

import java.util.ArrayList;
import org.apache.axiom.om.OMElement;

public class GetDocument extends Sq {
	ArrayList<String> ids = null;

	String query_details() {
		return "GetDocument " + ids;
	}
	
	OMElement build(ArrayList<String> ids) {
		this.ids = ids;
		ArrayList<OMElement> query = build_query_wrapper("urn:uuid:5c4f972b-d56b-40ac-a5fc-c8ca9b40b9d4");
		if (ids.get(0).startsWith("urn:uuid:")) 
			add_slot(query, "$XDSDocumentEntryEntryUUID", this.query_array_list(ids));
		else
			add_slot(query, "$XDSDocumentEntryUniqueId", this.query_array_list(ids));
		return query.get(0);
	}

}
