package gov.hhs.fha.nhinc.connectmgr.persistance.dao;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.uddi.api_v3.BusinessDetail;
import org.uddi.api_v3.ObjectFactory;

/**
 * 
 * @author kshtabnoy
 * 
 * Implementation of ConnectionManagerDAO that stores connection information in local file
 * 
 */
public class ConnectionManagerDAOFileImpl implements ConnectionManagerDAO {

	private static final int NUMBER_OF_ATTEMPTS = 10;
	private static final long REATTEMPT_DELAY = 100;
	private static final String CONTEXT_PATH = "org.uddi.api_v3";
	private String fileName = null;
	private Log log = null;
	
	private Log getLog() {
		if (log == null) {
			log = LogFactory.getLog(this.getClass());
		}
		return log;
	}
	
	ConnectionManagerDAOFileImpl(String fileName_) {
		fileName = fileName_;
	}
	
	@SuppressWarnings("unchecked")
	private BusinessDetail readFile() throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(CONTEXT_PATH);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		return ((JAXBElement<BusinessDetail>)unmarshaller.unmarshal(new File(fileName))).getValue();
	}
	
	@Override
	public BusinessDetail loadBusinessDetail() {
		BusinessDetail result = null;
		boolean done = false;
		int attempt = 1;
		String attemptsLog = "\n";
		while(!done) {
			try{
				result = readFile();
				done = true;
			} catch (JAXBException ex) {
				String errorMsg = "Attempt " + attempt + " failed. due to " + ex.toString();
				attemptsLog += errorMsg + "\n";
				if (attempt >= NUMBER_OF_ATTEMPTS) {
					throw new RuntimeException("Unable to load from " + fileName + " in " + attempt + " attempts." + attemptsLog);
				}
				attempt ++;
				try {
					Thread.sleep(REATTEMPT_DELAY);
				} catch (InterruptedException e) {
				}
			}
		}
		getLog().info("Connection info loaded from " + fileName);
		return result;
	}

	@Override
	public void saveBusinessDetail(BusinessDetail BusinessDetail) {
		try {
			JAXBContext context = JAXBContext.newInstance(CONTEXT_PATH);
			ObjectFactory factory = new ObjectFactory();
			Marshaller marshaller = context.createMarshaller();
			marshaller.marshal(factory.createBusinessDetail(BusinessDetail), new File(fileName));
		} catch (JAXBException ex) {
			throw new RuntimeException("Unable to save to Connection Information File " + fileName , ex);
		}
		getLog().info("Connection info saved to " + fileName);
	}
	
	
	public long getLastModifie() {
		File file = new File(fileName);
        if (file.exists()) {
            return file.lastModified();
        } else {
        	return 0;
        }
	}
	
	public void setFileName(String fileName_) {
		fileName = fileName_;
	}

}
