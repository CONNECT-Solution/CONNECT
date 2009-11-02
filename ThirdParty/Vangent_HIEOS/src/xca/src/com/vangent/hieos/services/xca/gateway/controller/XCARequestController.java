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
package com.vangent.hieos.services.xca.gateway.controller;

import com.vangent.hieos.xutil.metadata.structure.MetadataSupport;
import com.vangent.hieos.xutil.response.Response;
import com.vangent.hieos.xutil.xlog.client.XLogMessage;

// Exceptions.
import com.vangent.hieos.xutil.exception.XdsException;
import com.vangent.hieos.xutil.exception.XdsWSException;

// Third-party.
import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashMap;
import org.apache.axiom.om.OMElement;
import java.util.Collection;

/**
 *
 * @author Bernie Thuman
 */
public class XCARequestController {

    private Response response;
    private XLogMessage logMessage;

    // Key = uniqueId (homeCommunityId or repositoryUniqueId), Value = XCAAbstractRequestCollection
    private HashMap requests = new HashMap();

    /**
     *
     * @param transactionContext
     */
    public XCARequestController(Response response, XLogMessage logMessage) {
        this.response = response;
        this.logMessage = logMessage;
    }

    /**
     *
     * @param uniqueId
     * @return
     */
    public XCAAbstractRequestCollection getRequestCollection(String uniqueId) {
        XCAAbstractRequestCollection requestCollection = null;
        if (requests.containsKey(uniqueId)) {
            requestCollection = (XCAAbstractRequestCollection) requests.get(uniqueId);
        }
        return requestCollection;
    }

    /**
     *
     * @param requestCollection
     */
    @SuppressWarnings("unchecked")
    public void setRequestCollection(XCAAbstractRequestCollection requestCollection) {
        requests.put(requestCollection.getUniqueId(), requestCollection);
    }

    /**
     *
     * @param uniqueId
     * @param request
     */
    public void addRequest(String uniqueId, XCARequest request) {
        XCAAbstractRequestCollection requestCollection = this.getRequestCollection(uniqueId);
        if (requestCollection != null) {
            requestCollection.addRequest(request);
        } else {
            // Throw exception (FIXME).
        }
    }

    /**
     *
     * @param request
     * @return
     */
    public ArrayList<OMElement> sendRequests() {
        ArrayList<OMElement> results = new ArrayList<OMElement>();
        Collection allRequests = requests.values();
        for (Iterator it = allRequests.iterator(); it.hasNext();) {
            // Each pass is for a single entity (Responding Gateway / Repository / Registry).
            XCAAbstractRequestCollection requestCollection = (XCAAbstractRequestCollection) it.next();

            try {
                OMElement result = requestCollection.sendRequests(this.response, this.logMessage);
                if (result != null) // To be safe.
                {
                    // Keep track of results for later consolidation.
                    results.add(result);
                }

            // BHT (FIXUP) -- need to find proper exceptions to return.
            } catch (XdsException e) {
                response.add_error(MetadataSupport.XDSUnavailableCommunity,
                        "Failure contacting community or repository = " + requestCollection.getUniqueId(),
                        e.toString(), logMessage);
            } catch (XdsWSException e) {
                response.add_error(MetadataSupport.XDSUnavailableCommunity,
                        "Failure contacting community or repository = " + requestCollection.getUniqueId(),
                        e.toString(), logMessage);
            }
        }
        return results;
    }
}
