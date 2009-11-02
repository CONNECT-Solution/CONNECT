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

public class GetFolderAndContents extends Sq {
	ArrayList<String> ids = null;

	String query_details() {
		return "GetFolderAndContents " + ids;
	}

	OMElement build(ArrayList<String> ids) {
		this.ids = ids;
		ArrayList<OMElement> query = build_query_wrapper("urn:uuid:b909a503-523d-4517-8acf-8e5834dfc4c7");
		String id = "'" + ids.get(0) + "'";
		if (id.startsWith("urn:uuid:")) 
			add_slot(query, "$XDSFolderEntryUUID", id);
		else
			add_slot(query, "$XDSFolderUniqueId", id);
		return query.get(0);
	}

}
