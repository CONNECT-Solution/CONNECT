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

import com.vangent.hieos.xutil.exception.MetadataException;
import com.vangent.hieos.xutil.exception.XdsException;
import com.vangent.hieos.xutil.exception.XdsWSException;
import com.vangent.hieos.xutil.metadata.structure.Metadata;
import com.vangent.hieos.xutil.metadata.structure.MetadataSupport;
import com.vangent.hieos.xutil.response.RegistryResponseParser;
import com.vangent.hieos.xutil.client.RetrieveB;
import com.vangent.hieos.xutil.client.RetContext;
import com.vangent.hieos.xutil.client.RetInfo;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.axiom.om.OMElement;

public class RetrieveBEngine {
	boolean isXca = false;
	
	public void setIsXca(boolean isit) {
		isXca = isit;
	}

	public void retrieve(String id, RetrieveBQueryContents qc, Metadata m, HashMap<String, ArrayList<String>> repositories, String home)  {
		OMElement doc = null;
		try {
			doc = m.getObjectById(id);
		}
		catch (MetadataException e) {
			qc.addException(e);
		}
		if (doc == null) {
			qc.addFatalError("No Document with id of <" + id + "> present in this metadata");
			return;
		}
		String rep_uid = m.getSlotValue(doc, "repositoryUniqueId", 0);
		if (rep_uid == null) {
			qc.addFatalError("No repositoryUniqueId found in document <" + id + ">");
			return;
		}
		ArrayList<String> endpoints = repositories.get(rep_uid);
		if (endpoints == null) {
			qc.addFatalError("The repositoryUniqueId found in document <" + id + ">, is not configured in xwebtools");
			return;
		}
		for (String endpoint : endpoints) {
			try {
				qc.endpoint = endpoint;
				retrieve(id, qc, m, home);
				// seems to have worked
				return;
			}
			catch (XdsWSException e) {
				qc.addError(e.getMessage());
				// endpoint failed - try next one in list
			}
		}
		// none worked
		qc.addFatalError("Connection to all known endpoints for <" + rep_uid + "> " + endpoints + " failed");
	}	


	public void retrieve(String id, RetrieveBQueryContents qc, Metadata m, String home) throws XdsWSException {
		RetContext r_ctx = new RetContext();
		try {
			OMElement doc = m.getObjectById(id);
			if (doc == null) {
				qc.addFatalError("No Document with id of <" + id + "> present in this metadata");
				return;
			}
			RetInfo req_info = new RetInfo();
			req_info.setContent_type(doc.getAttributeValue(MetadataSupport.mime_type_qname));
			String uid = m.getUniqueIdValue(id);
			req_info.setDoc_uid(uid);
			req_info.setRep_uid(m.getSlotValue(doc, "repositoryUniqueId", 0));
			req_info.setHash(m.getSlotValue(doc, "hash", 0));
			req_info.setHome(home);
			
			String size = m.getSlotValue(doc, "size", 0);
			if (size != null)
				req_info.setSize(Integer.parseInt(size));

			r_ctx.addRequestInfo(uid, req_info);

			RetrieveB ret_b = new RetrieveB(r_ctx, qc.endpoint);
			ret_b.setIsXca(isXca);
			ret_b.run();
			
			ret_b.validate(false /* validateRequest */);
			
			qc.setRequestXML(r_ctx.getRequest());
			qc.setResultXML(r_ctx.getResult());
			qc.set_id(id);
			RegistryResponseParser rrp = r_ctx.getRrp();
			if (rrp == null) {
				qc.addFatalError("RetrieveBEngine.retrieve: RegistryResponseParser is null");
				return;
			}
			String status = rrp.get_registry_response_status();
			RetInfo ri = r_ctx.getResponseInfo().get(uid);
			qc.set_contents(ri.getContents(), ri.getContent_type());
			String errors = r_ctx.getResponseInfo().get(uid).getErrors();
			if (errors != null && !errors.equals(""))
				qc.addError(errors);
		}
		catch (MetadataException e) {
			qc.setResultXML(r_ctx.getResult());
			qc.addException(e);
		}
		catch (XdsException e) {
			qc.setResultXML(r_ctx.getResult());
			qc.addException(e);
		}
	}
}
