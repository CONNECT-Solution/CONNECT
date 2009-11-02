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

public class FindSubmissionSets extends Sq {
	ArrayList<String> ids = null;

	String query_details() {
		return "FindSubmissionSets " + ids;
	}

	OMElement build(ArrayList<String> ids) {
		this.ids = ids;
		ArrayList<OMElement> query = build_objectref_query_wrapper("urn:uuid:f26abbcb-ac74-4422-8a30-edb644bbc1a9");
		String stat_list = "('urn:oasis:names:tc:ebxml-regrep:StatusType:Approved', 'urn:oasis:names:tc:ebxml-regrep:StatusType:Deprecated')";
		add_slot(query, "$XDSSubmissionSetPatientId", this.query_singleton(ids));
		add_slot(query, "$XDSSubmissionSetStatus", stat_list);
		return query.get(0);
	}

}
