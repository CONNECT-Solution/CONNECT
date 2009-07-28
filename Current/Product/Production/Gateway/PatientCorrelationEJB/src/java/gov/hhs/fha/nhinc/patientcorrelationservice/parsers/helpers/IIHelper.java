/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.patientcorrelationservice.parsers.helpers;

import org.hl7.v3.II;

/**
 *
 * @author rayj
 */
public class IIHelper {

    public static II IIFactoryCreateNull(String nullFlavor) {
        II ii = new II();
        ii.getNullFlavor().add(Constants.NullFlavor_NotAvailable);
        return ii;
    }

    public static II IIFactoryCreateNull() {
        return IIFactoryCreateNull(Constants.NullFlavor_NotAvailable);
    }

    public static II IIFactory(String root, String extension) {
        II ii = new II();
        ii.setExtension(extension);
        ii.setRoot(root);
        return ii;

    }
}
