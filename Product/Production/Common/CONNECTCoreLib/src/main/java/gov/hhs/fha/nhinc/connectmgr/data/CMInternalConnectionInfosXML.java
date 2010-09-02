/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.connectmgr.data;

import com.thoughtworks.xstream.XStream;

/**
 * This class is used to serialize/deserialize to/from XML using XStream.
 * 
 * @author Les Westberg
 */
public class CMInternalConnectionInfosXML
{
    /**
     * This method serializes an InternalConnectionInfos object to an
     * XML string.
     * 
     * @param oInternalConnectionInfos The object to be serialized.
     * @return The XML string representation of the object.
     */
    public static String serialize(CMInternalConnectionInfos oInternalConnectionInfos)
    {
        String sXML = "";
        
        XStream oXStream = new XStream();
        oXStream.alias("InternalConnectionInfos", CMInternalConnectionInfos.class);
        oXStream.addImplicitCollection(CMInternalConnectionInfos.class, "internalConnectionInfoList");
        oXStream.alias("internalConnectionInfo", CMInternalConnectionInfo.class);
        oXStream.addImplicitCollection(CMInternalConnectionInfoLiftProtocols.class, "protocolList");
        oXStream.alias("liftProtocol", CMInternalConnectionInfoLiftProtocol.class);
        oXStream.addImplicitCollection(CMInternalConnInfoServices.class, "serviceList");
        oXStream.alias("service", CMInternalConnInfoService.class);
        oXStream.addImplicitCollection(CMInternalConnectionInfoStates.class, "stateList");
        oXStream.alias("state", CMInternalConnectionInfoState.class);
        oXStream.processAnnotations(CMInternalConnectionInfos.class);
        sXML = oXStream.toXML(oInternalConnectionInfos);
        
        return sXML;
    }
    
    /**
     * This method takes an XML representation of CMInternalConnectionInfos and
     * produces an instance of the object.
     * 
     * @param sXML The serialized representation of the CMInternalConnectionInfos object.
     * @return The object instance of the XML.
     */
    public static CMInternalConnectionInfos deserialize(String sXML)
    {
        CMInternalConnectionInfos oInternalConnectionInfos = new CMInternalConnectionInfos();
        
        XStream oXStream = new XStream();
        oXStream.alias("InternalConnectionInfos", CMInternalConnectionInfos.class);
        oXStream.addImplicitCollection(CMInternalConnectionInfos.class, "internalConnectionInfoList");
        oXStream.alias("internalConnectionInfo", CMInternalConnectionInfo.class);
        oXStream.addImplicitCollection(CMInternalConnectionInfoLiftProtocols.class, "protocolList");
        oXStream.alias("liftProtocol", CMInternalConnectionInfoLiftProtocol.class);
        oXStream.addImplicitCollection(CMInternalConnInfoServices.class, "serviceList");
        oXStream.alias("service", CMInternalConnInfoService.class);
        oXStream.addImplicitCollection(CMInternalConnectionInfoStates.class, "stateList");
        oXStream.alias("state", CMInternalConnectionInfoState.class);
        oXStream.processAnnotations(CMInternalConnectionInfos.class);
        Object oObject = oXStream.fromXML(sXML);
        if (oObject instanceof CMInternalConnectionInfos)
        {
            oInternalConnectionInfos = (CMInternalConnectionInfos) oObject;
        }
        
        return oInternalConnectionInfos;
    }
}
