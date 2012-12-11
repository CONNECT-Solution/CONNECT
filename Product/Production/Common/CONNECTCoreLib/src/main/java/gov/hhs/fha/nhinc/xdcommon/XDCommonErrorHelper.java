/**
 * 
 */
package gov.hhs.fha.nhinc.xdcommon;

import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

/**
 * @author mweaver
 *
 */
public class XDCommonErrorHelper {
    
    public enum ErrorCodes {
        XDSRegistryError,
        XDSRepositoryError
    }
    
    public RegistryResponseType createError(String message) {
        return createRegistryResponse(message, ErrorCodes.XDSRegistryError, "CONNECT");
    }
    
    public RegistryResponseType createError(Throwable e) {
        return createRegistryResponse(e.getLocalizedMessage(), ErrorCodes.XDSRegistryError, e.getStackTrace().toString());
    }
    
    private RegistryResponseType createRegistryResponse(String error, ErrorCodes code, String location) {
        RegistryResponseType response = new RegistryResponseType();
        response.setStatus("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure");
        
        RegistryErrorList registryErrorList = new RegistryErrorList();
        RegistryError registryError = new RegistryError();
        registryError.setCodeContext(error);
        registryError.setErrorCode(code.toString());
        registryError.setLocation(location);
        registryErrorList.getRegistryError().add(registryError );
        response.setRegistryErrorList(registryErrorList);
        return response;
    }

}
