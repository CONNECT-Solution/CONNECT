/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.patientcorrelation.nhinc.parsers.helpers;

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
