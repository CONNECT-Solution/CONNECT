/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.common.patientcorrelationfacade.dte.helpers;

import org.hl7.v3.II;

/**
 *
 * @author rayj
 */
public class UniqueIdHelper {

    public static II createUniqueId(String root) {
        II uniqueId = new II();
        String value;

        value = generateUID();
        uniqueId.setRoot(root);  //todo: refactor
        uniqueId.setExtension(value);

        return uniqueId;
    }

    public static II createUniqueId() {
        String myRoot = Configuration.getMyCommunityId();
        return createUniqueId(myRoot);

    }

    private static String generateUID() {
        java.rmi.server.UID uid = new java.rmi.server.UID();
        return uid.toString();
    }
}
