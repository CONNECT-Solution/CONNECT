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
package com.vangent.hieos.services.xds.repository.support;

// NOTE: BHT (FIXME) - eventually, we will need to deal with multiple repositories in the same instance.
// This code was based on original NIST code full of static methods.
import com.vangent.hieos.xutil.exception.XdsInternalException;
import com.vangent.hieos.xutil.xconfig.XConfig;
import com.vangent.hieos.xutil.xconfig.XConfigHomeCommunity;
import com.vangent.hieos.xutil.xconfig.XConfigRepository;
import com.vangent.hieos.xutil.xconfig.XConfigRegistry;
import com.vangent.hieos.xutil.xconfig.XConfigTransaction;

public class Repository {


    /**
     * 
     * @param xds_version
     * @return
     * @throws com.vangent.hieos.xutil.exception.XdsInternalException
     */
    static public String getRegisterTransactionEndpoint(short xds_version) throws XdsInternalException {
        if (xds_version == 3) {
            XConfigRepository repository = Repository.getRepositoryConfig();
            XConfigRegistry localRegistry = repository.getLocalRegistry();
            XConfigTransaction txn = localRegistry.getTransaction("RegisterDocumentSet-b");
            return txn.getEndpointURL();
        }
        // Note (BHT): we don't care about XDS.a so ok to leave hardcoded.
        return "http://localhost:8080/axis2/services/xdsregistryainternal";
    }

    /**
     * 
     * @return
     * @throws com.vangent.hieos.xutil.exception.XdsInternalException
     */
    static public String getRepositoryUniqueId() throws XdsInternalException {
        XConfigRepository repository = Repository.getRepositoryConfig();
        return repository.getUniqueId();
    }

    /**
     *
     * @return
     * @throws com.vangent.hieos.xutil.exception.XdsInternalException
     */
    static private XConfigRepository getRepositoryConfig() throws XdsInternalException {
        try {
            XConfig xconf = XConfig.getInstance();
            XConfigHomeCommunity homeCommunity = xconf.getHomeCommunity();
            XConfigRepository repository = homeCommunity.getLocalRepository();
            return repository;
        } catch (Exception e) {
            throw new XdsInternalException("Unable to get Repository configuration + " + e.getMessage());
        }
    }
}
