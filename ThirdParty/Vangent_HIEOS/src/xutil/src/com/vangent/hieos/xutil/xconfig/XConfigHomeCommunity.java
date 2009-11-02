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
package com.vangent.hieos.xutil.xconfig;

import org.apache.axiom.om.OMElement;

/**
 *
 * @author Bernie Thuman
 */
public class XConfigHomeCommunity {

    /**
     * This is an ugly way to do things, but it works (maybe changed to enum).
     */
    private static String prop_homeCommunityId = "HomeCommunityId";
    private static String prop_initiatingGateway = "InitiatingGateway";
    private static String prop_respondingGateway = "RespondingGateway";
    private static String prop_localRepository = "LocalRepository";
    private XConfigGateway initiatingGateway = null;
    private XConfigGateway respondingGateway = null;
    private XConfigRepository localRepository = null;
    private XConfigProperties properties = new XConfigProperties();

    /**
     * Get the value of initiatingGateway
     *
     * @return the value of initiatingGateway
     */
    public XConfigGateway getInitiatingGateway() {
        return this.initiatingGateway;
    }

    /**
     * Set the value of initiatingGateway
     *
     * @param initiatingGateway new value of initiatingGateway
     */
    protected void setInitiatingGateway(XConfigGateway initiatingGateway) {
        this.initiatingGateway = initiatingGateway;
    }

    /**
     *
     * @return
     */
    public XConfigGateway getRespondingGateway() {
        return this.respondingGateway;
    }

    /**
     * Set the value of respondingGateway
     *
     * @param respondingGateway new value of respondingGateway
     */
    protected void setRespondingGateway(XConfigGateway respondingGateway) {
        this.respondingGateway = respondingGateway;
    }

    /**
     *
     * @return
     */
    public XConfigRepository getLocalRepository() {
        return this.localRepository;
    }

    /**
     *
     * @param localRepository
     */
    protected void setLocalRepository(XConfigRepository localRepository) {
        this.localRepository = localRepository;
    }

    /**
     * 
     * @return
     */
    public String getHomeCommunityId() {
        return getProperty(prop_homeCommunityId);
    }

    /**
     *
     * @return
     */
    public String getInitiatingGatewayName() {
        return getProperty(prop_initiatingGateway);
    }

    /**
     *
     * @return
     */
    public String getRespondingGatewayName() {
        return getProperty(prop_respondingGateway);
    }

    /**
     * 
     * @return
     */
    public String getLocalRepositoryName() {
        return getProperty(prop_localRepository);
    }

    /**
     *
     * @param propKey
     * @return
     */
    public String getProperty(String propKey) {
        return (String) properties.getProperty(propKey);
    }

    /**
     * 
     * @param propKey
     * @return
     */
    public boolean getPropertyAsBoolean(String propKey) {
        return properties.getPropertyAsBoolean(propKey);
    }

    /**
     *
     * @param rootNode
     */
    protected void parse(OMElement rootNode) {
        properties.parse(rootNode);
    }
}
