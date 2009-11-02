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

public class RegistryResponse extends Response {
	
	public RegistryResponse(short version) throws XdsInternalException {
		super(version);
		response = MetadataSupport.om_factory.createOMElement("RegistryResponse", ebRSns);
	}
	
	public RegistryResponse(short version, RegistryErrorList rel) throws XdsInternalException {
		super(version, rel);
		response = MetadataSupport.om_factory.createOMElement("RegistryResponse", ebRSns);
	}
	
	public void addQueryResults(OMElement metadata) {
		
	}
	
	public OMElement getRoot() { return response; }

}
