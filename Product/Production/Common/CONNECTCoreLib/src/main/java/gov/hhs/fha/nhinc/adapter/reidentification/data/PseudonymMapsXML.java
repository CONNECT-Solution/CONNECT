/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.adapter.reidentification.data;

import com.thoughtworks.xstream.XStream;

/**
 * This class is used to serialize the POJO reidentification classes into an 
 * XML string and also to de-serialize an XML string into a set of POJO 
 * reidentification classes.
 */
public class PseudonymMapsXML {

    /**
     * XStream initialized for PseudonymMaps
     */
    private static XStream xStream;

    /**
     * Lazy initializer for the definition of the xstream
     */
    private static XStream getXStream() {
        if (xStream == null) {
            xStream = new XStream();
            xStream.alias("pseudonymMaps", PseudonymMaps.class);
            xStream.addImplicitCollection(PseudonymMaps.class, "pseudonymMaps");
            xStream.alias("pseudonymMap", PseudonymMap.class);
        }
        return xStream;
    }

    /**
     * This method serializes a PseudonymsMaps object into an XML string and 
     * returns that string.
     */
    public static String serialize(PseudonymMaps pseudonymMaps) {
        return getXStream().toXML(pseudonymMaps);
    }

    /**
     * This method takes an XML representation of the PseudonymMaps and 
     * returns the POJO objects that represent that same data.
     */
    public static PseudonymMaps deserialize(String xml) {

        PseudonymMaps pseudonymMaps = new PseudonymMaps();
        Object obj = getXStream().fromXML(xml);
        if (obj instanceof PseudonymMaps) {
            pseudonymMaps = (PseudonymMaps) obj;
        }

        return pseudonymMaps;
    }
}
