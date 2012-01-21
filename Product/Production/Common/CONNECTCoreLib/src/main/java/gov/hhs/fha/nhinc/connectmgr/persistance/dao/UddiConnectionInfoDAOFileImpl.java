package gov.hhs.fha.nhinc.connectmgr.persistance.dao;

import gov.hhs.fha.nhinc.properties.PropertyAccessor;

import java.io.File;

import javax.xml.bind.JAXBException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.uddi.api_v3.BusinessDetail;

/**
 *
 * @author kshtabnoy
 *
 * Implementation of ConnectionManagerDAO that stores connection information in local file
 *
 */
public class UddiConnectionInfoDAOFileImpl extends ConnectionManagerDAOBase implements ConnectionManagerDAO {

    private static UddiConnectionInfoDAOFileImpl instance = null;
    private File file = null;
    private Log log = null;

    public static UddiConnectionInfoDAOFileImpl getInstance() {
        if (instance != null) {
            return instance;
        } else {
            return new UddiConnectionInfoDAOFileImpl(PropertyAccessor.getPropertyFileLocation() + "uddiConnectionInfo.xml");
        }
    }

    UddiConnectionInfoDAOFileImpl(String fileName) {
        file = new File(fileName);
    }

    @Override
    public BusinessDetail loadBusinessDetail() throws Exception {
        BusinessDetail resp = null;
        try {
            resp = super.loadBusinessDetail(file);
        } catch (JAXBException ex) {
            getLogger().error("unable to load business entities from " + file.getName(), ex);
            resp = new BusinessDetail();
            throw new Exception("unable to load business entities from " + file.getName(), ex);
        }
        return resp;
    }

    @Override
    public void saveBusinessDetail(BusinessDetail BusinessDetail) {
        super.saveBusinessDetail(BusinessDetail, file);
    }

    public long getLastModified() {
        if (file.exists()) {
            return file.lastModified();
        } else {
            return 0;
        }
    }

    @Override
    protected Log getLogger() {
        if (log == null) {
            setLogger(LogFactory.getLog(this.getClass()));
        }
        return log;
    }

    @Override
    protected void setLogger(Log log) {
        this.log = log;
    }

    public void setFileName(String fileName) {
        file = new File(fileName);
    }
}
