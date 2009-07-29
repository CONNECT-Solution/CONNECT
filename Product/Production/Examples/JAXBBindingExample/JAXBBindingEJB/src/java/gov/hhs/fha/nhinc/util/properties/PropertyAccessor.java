/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.util.properties;

import gov.hhs.fha.nhinc.nhinccomponentpropaccessor.NhincComponentPropAccessorPortType;
import javax.ejb.Stateless;
import javax.jws.WebService;

/**
 *
 * @author westbergl
 */
@WebService(serviceName = "NhincComponentPropAccessor", portName = "NhincComponentPropAccessorPort", endpointInterface = "gov.hhs.fha.nhinc.nhinccomponentpropaccessor.NhincComponentPropAccessorPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhinccomponentpropaccessor", wsdlLocation = "META-INF/wsdl/PropertyAccessor/NhincComponentPropAccessor.wsdl")
@Stateless
public class PropertyAccessor implements NhincComponentPropAccessorPortType {

    public gov.hhs.fha.nhinc.common.propertyaccess.GetPropertyResponseType getProperty(gov.hhs.fha.nhinc.common.propertyaccess.GetPropertyRequestType getPropertyRequest)
    {
        System.out.println("getproperty was called.");
        return null;
    }

    public gov.hhs.fha.nhinc.common.propertyaccess.GetPropertyBooleanResponseType getPropertyBoolean(gov.hhs.fha.nhinc.common.propertyaccess.GetPropertyBooleanRequestType getPropertyBooleanRequest)
    {
        System.out.println("getPropertyBoolean was called.");
        return null;
    }

    public gov.hhs.fha.nhinc.common.propertyaccess.GetPropertyNamesResponseType getPropertyNames(gov.hhs.fha.nhinc.common.propertyaccess.GetPropertyNamesRequestType getPropertyNamesRequest)
    {
        System.out.println("getPropertyNames was called.");
        return null;
    }

    public gov.hhs.fha.nhinc.common.propertyaccess.GetPropertiesResponseType getProperties(gov.hhs.fha.nhinc.common.propertyaccess.GetPropertiesRequestType getPropertiesRequest)
    {
        System.out.println("getProperties was called.");
        return null;
    }

    public gov.hhs.fha.nhinc.common.propertyaccess.GetRefreshDurationResponseType getRefreshDuration(gov.hhs.fha.nhinc.common.propertyaccess.GetRefreshDurationRequestType getRefreshDurationRequest)
    {
        System.out.println("getRefreshDuration was called.");
        return null;
    }

    public gov.hhs.fha.nhinc.common.propertyaccess.GetDurationBeforeNextRefreshResponseType getDurationBeforeNextRefresh(gov.hhs.fha.nhinc.common.propertyaccess.GetDurationBeforeNextRefreshRequestType getDurationBeforeNextRefreshRequest)
    {
        System.out.println("getDurationBeforenextRefresh was called.");
        return null;
    }

    public gov.hhs.fha.nhinc.common.propertyaccess.ForceRefreshResponseType forceRefresh(gov.hhs.fha.nhinc.common.propertyaccess.ForceRefreshRequestType forceRefreshRequest)
    {
        System.out.println("forceRefresh was called.");
        return null;
    }

    public gov.hhs.fha.nhinc.common.propertyaccess.GetPropertyFileLocationResponseType getPropertyFileLocation(gov.hhs.fha.nhinc.common.propertyaccess.GetPropertyFileLocationRequestType getPropertyFileLocationRequest)
    {
        System.out.println("getPropertyFileLocation was called.");
        return null;
    }

    public gov.hhs.fha.nhinc.common.propertyaccess.DumpPropsToLogResponseType dumpPropsToLog(gov.hhs.fha.nhinc.common.propertyaccess.DumpPropsToLogRequestType dumpPropsToLogRequest)
    {
        System.out.println("getDumpPropsToLog was called.");
        return null;
    }

    public gov.hhs.fha.nhinc.common.propertyaccess.WritePropertyFileResponseType writePropertyFile(gov.hhs.fha.nhinc.common.propertyaccess.WritePropertyFileRequestType writePropertyFileRequest)
    {
        System.out.println("writePropertyFile was called.");
        return null;
    }

    public gov.hhs.fha.nhinc.common.propertyaccess.DeletePropertyFileResponseType deletePropertyFile(gov.hhs.fha.nhinc.common.propertyaccess.DeletePropertyFileRequestType deletePropertyFileRequest)
    {
        System.out.println("DeletePropertyFile was called.");
        return null;
    }

}
