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
import com.vangent.hieos.xutil.xml.Util;

import java.util.ArrayList;

import org.apache.axiom.om.OMElement;

public class AdhocQueryResponse extends Response {
	OMElement queryResult = null;

	public AdhocQueryResponse(short version, RegistryErrorList rel)  throws XdsInternalException {
		super(version, rel);

		init(version);
	}

	public AdhocQueryResponse(short version) throws XdsInternalException {
		super(version);

		init(version);
	}
	
	public OMElement getRoot() { return queryResult; }

	private void init(short version) {
		if (version == version_2) {
			response = MetadataSupport.om_factory.createOMElement("RegistryResponse", ebRSns);
			OMElement ahqr = MetadataSupport.om_factory.createOMElement("AdhocQueryResponse", ebQns);
			response.addChild(ahqr);
			OMElement sqr = null;
			sqr = MetadataSupport.om_factory.createOMElement("SQLQueryResult", ebQns);
			queryResult = sqr;
			ahqr.addChild(sqr);
		} else {
			response = MetadataSupport.om_factory.createOMElement("AdhocQueryResponse", ebQns);
		}
	}

	// called to get parent element of query results

	public OMElement getQueryResult() { 
		if (queryResult != null)
			return queryResult;


		if (version == version_2) {
			OMElement adhocQueryResponse = MetadataSupport.om_factory.createOMElement("AdhocQueryResponse", ebQns);
			response.addChild(adhocQueryResponse);
			queryResult = MetadataSupport.om_factory.createOMElement("SQLQueryResult", ebQns);
			adhocQueryResponse.addChild(queryResult);
		} else {  // add RegistryObjectList
			queryResult = MetadataSupport.om_factory.createOMElement("RegistryObjectList", ebRIMns);
			//response.addChild(queryResult);
		}
		return queryResult;
	}

	public void addQueryResults(OMElement metadata)  throws XdsInternalException {
		OMElement res = getQueryResult();  // used for side effect if v3 and error - must
		// still have empty RegistryObjectList after RegistryErrorList
		if (metadata != null)
			res.addChild(Util.deep_copy(metadata));
	}

	public void addQueryResults(ArrayList metadatas)  throws XdsInternalException {
		OMElement res = getQueryResult();  // used for side effect if v3 and error - must
		// still have empty RegistryObjectList after RegistryErrorList
		if (metadatas != null)
			for (int i=0; i<metadatas.size(); i++) {
				res.addChild(Util.deep_copy((OMElement) metadatas.get(i)));
			}
	}


}
