/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.patientcorrelation.nhinc.parsers.PRPAIN201309UV.helpers;

import gov.hhs.fha.nhinc.common.nhinccommon.QualifiedSubjectIdentifierType;
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

    public static II IIFactory(QualifiedSubjectIdentifierType qualifiedSubjectIdentifier) {
        return IIFactory(qualifiedSubjectIdentifier.getAssigningAuthorityIdentifier(), qualifiedSubjectIdentifier.getSubjectIdentifier());
    }
}
