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
package com.vangent.hieos.services.xds.repository.storage;

import com.vangent.hieos.xutil.exception.XdsInternalException;
import com.vangent.hieos.xutil.xconfig.XConfig;
import org.apache.log4j.Logger;

/**
 *  Abstract class that can be extended to provide XDS.b document storage
 *  and retrieval operations.
 *
 * @author Bernie Thuman
 */
abstract public class XDSRepositoryStorage {
    private final static Logger logger = Logger.getLogger(XDSRepositoryStorage.class);

    /**
     *  Stores an XDS.b document into a data store.
     *
     * @param doc XDSDocument instance with all document vitals.
     * @throws com.vangent.hieos.xutil.exception.XdsInternalException
     */
    abstract public void store(XDSDocument doc) throws XdsInternalException;

    /**
     *  Retrieves an XDS.b document from a data source.
     *
     * @param doc XDSDocument instance with [repositoryid, uniqueid] set.
     * @return XDSDocument instance with all document vitals filled in (reused 'doc' instance).
     * @throws com.vangent.hieos.xutil.exception.XdsInternalException
     */
    abstract public XDSDocument retrieve(XDSDocument doc) throws XdsInternalException;

    /**
     *  Retrieves an XDS.b document from a data source.
     *
     * @return
     * @throws com.vangent.hieos.xutil.exception.XdsInternalException
     */
    synchronized static public XDSRepositoryStorage getInstance() throws XdsInternalException {

        // Get name of XDSRepositoryStorage subclass from configuration file.
        XConfig xconf;
        xconf = XConfig.getInstance();
        String repoClassImpl = xconf.getHomeCommunityProperty("RepoStorageClassImpl");

        // Dynamically load XDSRepositoryStorage class.
        Class repoStorageClass;
        try {
            repoStorageClass = Class.forName(repoClassImpl);
        } catch (ClassNotFoundException ex) {
            throw new XdsInternalException("Repository could not load RepoStorageClassImpl: " + ex.getMessage());
        }

        // Create a new instance of XDSRepositoryStorage subclass.
        XDSRepositoryStorage repoStorage;
        try {
            repoStorage = (XDSRepositoryStorage) repoStorageClass.newInstance();
        } catch (InstantiationException ex) {
            throw new XdsInternalException("Repository could not instantiate RepoStorageClassImpl: " + ex.getMessage());
        } catch (IllegalAccessException ex) {
            throw new XdsInternalException("Repository could not instantiate RepoStorageClassImpl: " + ex.getMessage());
        }
        return repoStorage;
    }
}