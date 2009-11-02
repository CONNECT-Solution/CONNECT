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

public class FindDocuments extends Sq {
	ArrayList<String> ids = null;
	boolean useLeafClass = true;
	QueryContents queryContents;

	String query_details() {
		return "FindDocuments " + ids;
	}

	public FindDocuments(boolean useLeafClass, QueryContents queryContents) {
		this.useLeafClass = useLeafClass;
		this.queryContents = queryContents;
	}

	OMElement build(ArrayList<String> ids) {
		this.ids = ids;
		ArrayList<OMElement> query;
		if (useLeafClass)
			query = build_query_wrapper("urn:uuid:14d4debf-8f97-4251-9a74-a90016b0af0d");
		else
			query = build_objectref_query_wrapper("urn:uuid:14d4debf-8f97-4251-9a74-a90016b0af0d");
		String stat_list = "('urn:oasis:names:tc:ebxml-regrep:StatusType:Approved', 'urn:oasis:names:tc:ebxml-regrep:StatusType:Deprecated')";
		add_slot(query, "$XDSDocumentEntryPatientId", this.query_singleton(ids));
		add_slot(query, "$XDSDocumentEntryStatus", stat_list);
		queryContents.setRequestXML(query.get(0));
		return query.get(0);
	}

}
