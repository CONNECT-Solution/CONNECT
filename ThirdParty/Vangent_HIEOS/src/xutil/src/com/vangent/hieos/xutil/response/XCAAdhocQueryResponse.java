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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vangent.hieos.xutil.response;

import com.vangent.hieos.xutil.metadata.structure.MetadataSupport;
import org.apache.axiom.om.OMElement;
import com.vangent.hieos.xutil.exception.XdsInternalException;

/**
 *
 * @author Bernie Thuman
 */
public class XCAAdhocQueryResponse extends Response {

    OMElement queryResult = null;

    /**
     *
     * @throws com.vangent.hieos.xutil.exception.XdsInternalException
     */
    public XCAAdhocQueryResponse() throws XdsInternalException {
        super(version_3);
        response = MetadataSupport.om_factory.createOMElement("AdhocQueryResponse", ebQns);
        queryResult = MetadataSupport.om_factory.createOMElement("RegistryObjectList", ebRIMns);
    }

    /**
     *
     * @return
     */
    public OMElement getRoot() {
        return response;
    }

    /**
     *
     * @return
     */
    public OMElement getQueryResult() {
        return this.queryResult;
    }

    /**
     *
     * @param metadata
     * @throws XdsInternalException
     */
    public void addQueryResults(OMElement metadata) throws XdsInternalException {
        //if (queryResult == null) {
        //    queryResult = MetadataSupport.om_factory.createOMElement("RegistryObjectList", ebRIMns);
        //    response.addChild(queryResult);
        //}
        //if (queryResult == null) {
        //    queryResult = MetadataSupport.om_factory.createOMElement("RegistryObjectList", ebRIMns);
        //}
        if (metadata != null) {
            queryResult.addChild(metadata);
        }
    }
}
