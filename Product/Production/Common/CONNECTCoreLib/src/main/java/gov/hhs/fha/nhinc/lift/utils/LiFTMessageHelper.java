/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.lift.utils;

import java.util.Iterator;
import javax.xml.bind.JAXBElement;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.IdentifiableType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectListType;

public class LiFTMessageHelper {

    public static ExtrinsicObjectType extractExtrinsicObject(RegistryObjectListType registryList) {
        ExtrinsicObjectType extObj = null;

        if (registryList != null &&
                registryList.getIdentifiable() != null) {
            Iterator<JAXBElement<? extends IdentifiableType>> iterator = registryList.getIdentifiable().iterator();

            // Extract the ExtrinsicObject for this request message.
            while (iterator.hasNext()) {
                JAXBElement<? extends IdentifiableType> jaxbElem = iterator.next();

                if ((jaxbElem != null) &&
                        (jaxbElem.getDeclaredType() != null) &&
                        (jaxbElem.getDeclaredType().getCanonicalName() != null) &&
                        (jaxbElem.getDeclaredType().getCanonicalName().equals("oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType")) &&
                        (jaxbElem.getValue() != null)) {
                    extObj = (ExtrinsicObjectType) jaxbElem.getValue();
                    break;
                }
            }
        }

        return extObj;
    }
}
