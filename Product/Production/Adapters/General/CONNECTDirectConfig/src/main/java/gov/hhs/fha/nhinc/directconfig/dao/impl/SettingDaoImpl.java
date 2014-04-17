package gov.hhs.fha.nhinc.directconfig.dao.impl;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;

import gov.hhs.fha.nhinc.directconfig.entity.Setting;
import gov.hhs.fha.nhinc.directconfig.exception.ConfigurationStoreException;
import gov.hhs.fha.nhinc.directconfig.dao.SettingDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementing class for Setting DAO methods.
 *
 * @author Greg Meyer
 */
//@Repository
@Service
public class SettingDaoImpl implements SettingDao {
    @Autowired
    protected SessionFactory sessionFactory;

    private static final Log log = LogFactory.getLog(SettingDaoImpl.class);

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = false)
    public void add(String name, String value) {
        log.debug("Enter");

        if (name == null || name.isEmpty() || value == null) {
            return;
        }
        
        // make sure this setting doesn't already exist
        if (this.getByNames(Arrays.asList(name)).size() > 0) {
            throw new ConfigurationStoreException("Setting " + name + " already exists.");
        }
        
        Setting setting = new Setting();
        
        setting.setName(name);
        setting.setValue(value);
        setting.setCreateTime(Calendar.getInstance());
        setting.setUpdateTime(setting.getCreateTime());

        log.debug("Calling JPA to persist the setting");

        sessionFactory.getCurrentSession().persist(setting);
        sessionFactory.getCurrentSession().flush();

        log.debug("Returned from JPA: Setting ID=" + setting.getId());

        log.debug("Exit");
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = false)
    public void delete(Collection<String> names) {
        log.debug("Enter");

        if (names != null && names.size() > 0) {
            StringBuffer queryNames = new StringBuffer("(");
            
            for (String name : names) {
                if (queryNames.length() > 1) {
                    queryNames.append(", ");
                }
                
                queryNames.append("'").append(name.toUpperCase(Locale.getDefault())).append("'");
            }
            
            queryNames.append(")");
            String query = "DELETE FROM Setting s WHERE UPPER(s.name) IN " + queryNames.toString();

            int count = 0;
            Query delete = sessionFactory.getCurrentSession().createQuery(query);
            count = delete.executeUpdate();

            log.debug("Exit: " + count + " setting records deleted");
        }

        log.debug("Exit");
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public Collection<Setting> getAll() {
        log.debug("Enter");

        List<Setting> result = Collections.emptyList();

        Query select = null;
        select = sessionFactory.getCurrentSession().createQuery("SELECT s from Setting s");

        @SuppressWarnings("rawtypes")
        List rs = select.getResultList();

        if (rs != null && (rs.size() != 0) && (rs.get(0) instanceof Setting)) {
            result = (List<Setting>) rs;
        }

        log.debug("Exit");
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public Collection<Setting> getByNames(Collection<String> names) {
        log.debug("Enter");

        if (names == null || names.size() == 0)
            return getAll();

        List<Setting> result = Collections.emptyList();

        Query select = null;
        StringBuffer nameList = new StringBuffer("(");
        
        for (String name : names) {
            if (nameList.length() > 1) {
                nameList.append(", ");
            }
            
            nameList.append("'").append(name.toUpperCase(Locale.getDefault())).append("'");
        }
        
        nameList.append(")");
        String query = "SELECT s from Setting s WHERE UPPER(s.name) IN " + nameList.toString();

        select = sessionFactory.getCurrentSession().createQuery(query);
        
        @SuppressWarnings("rawtypes")
        List rs = select.getResultList();
        
        if (rs != null && (rs.size() != 0) && (rs.get(0) instanceof Setting))
        {
            result = (List<Setting>) rs;
        }

        log.debug("Exit");

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = false)
    public void update(String name, String value) {
        log.debug("Enter");

        if (name == null || name.isEmpty()) {
            return;
        }
        
        Collection<Setting> settings = getByNames(Arrays.asList(name));

        for (Setting setting : settings) {
            setting.setValue(value);
            setting.setUpdateTime(Calendar.getInstance());
            sessionFactory.getCurrentSession().merge(setting);
        }

        log.debug("Exit");
    }
}
