/**
 * 
 */
package gov.hhs.fha.nhinc.mpi.adapter.component;

import javax.xml.namespace.QName;
import javax.xml.ws.soap.SOAPFaultException;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPFault;

import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;



/**
 * @author bhumphrey
 *
 */
public class AdapterComponentMpiCheckerFaultImpl implements
		AdapterComponentMpiChecker {
	public PRPAIN201306UV02 FindPatient(PRPAIN201305UV02 query) {
		
		runtimeError("[TEST ERROR] and error occurend");
		return null;
	}

	@Override
	public boolean isNhinRequiredParamsFound(PRPAIN201305UV02 query) {
		runtimeError("[TEST ERROR] and error occurend");
		return false;
	}
	
	public void soapError(String message) {
		SOAPFault sf = null;
		try {
			SOAPFactory fac;
			fac = SOAPFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
			sf = fac.createFault(message, new QName(SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE, "Reciever"));
		} catch (SOAPException e) {
			e.printStackTrace();
		}
		throw new SOAPFaultException(sf);
	}
	
	
	public void runtimeError(String message) {
		
		throw new RuntimeException(message);
	}


}
