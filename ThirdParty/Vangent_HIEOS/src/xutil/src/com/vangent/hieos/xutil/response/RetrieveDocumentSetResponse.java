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

package com.vangent.hieos.xutil.response;

import com.vangent.hieos.xutil.metadata.structure.MetadataSupport;
import com.vangent.hieos.xutil.exception.XdsInternalException;

import javax.xml.namespace.QName;
import org.apache.axiom.om.OMElement;

public class RetrieveDocumentSetResponse {
	RegistryResponse rr;
	
	public RetrieveDocumentSetResponse(RegistryResponse rr) {
		this.rr = rr;
	}
	
	public OMElement getResponse() throws XdsInternalException {
		OMElement response = MetadataSupport.om_factory.createOMElement(new QName(MetadataSupport.xdsB_uri, "RetrieveDocumentSetResponse"));
		response.addChild(rr.getResponse());
		//System.out.println("response is \n" + response.toString());
		return response;
	}

}
