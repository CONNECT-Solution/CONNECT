/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.policyengineproxy;

/**
 *
 * @author jhoppesc
 */
public class PolicyEngineProxyFactory {
    public static IPolicyEngineProxy getPolicyEngineProxy() {
        return new PolicyEngineWebServiceProxy();
    }
}
