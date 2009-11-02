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

import org.apache.axiom.om.OMElement;

public class RetrieveMultipleResponse extends Response {
	OMElement rdsr = null;
	
	public OMElement getRoot() { return rdsr; }

	public RetrieveMultipleResponse() throws XdsInternalException {
		super();
		response = MetadataSupport.om_factory.createOMElement("RegistryResponse", ebRSns);
		rdsr = MetadataSupport.om_factory.createOMElement("RetrieveDocumentSetResponse", MetadataSupport.xdsB);
		rdsr.addChild(response);
	}
	
	public RetrieveMultipleResponse(RegistryErrorList rel) throws XdsInternalException {
		super(rel);
		response = MetadataSupport.om_factory.createOMElement("RegistryResponse", ebRSns);
		rdsr = MetadataSupport.om_factory.createOMElement("RetrieveDocumentSetResponse", MetadataSupport.xdsB);
		rdsr.addChild(response);
	}
	
	public void addQueryResults(OMElement metadata) {
		
	}


}
