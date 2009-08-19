
package com.sun.ejb.containers;

import java.util.Collection;
import java.util.ResourceBundle;
import javax.ejb.*;
import com.sun.jdo.spi.persistence.support.ejb.cmp.EJBHashSet;

// This is the generated concrete CMP bean class to be used with Transparent Persistence runtime.
public class TimerMigrationBean649625130_ConcreteImpl
    extends com.sun.ejb.containers.TimerMigrationBean
    implements javax.transaction.Synchronization, com.sun.ejb.spi.container.BeanStateSynchronization
{
    private transient com.sun.ejb.containers.TimerMigrationBean649625130_JDOState _jdoInstance = null;        
    private transient com.sun.jdo.api.persistence.support.PersistenceManager _jdoPersistenceManager = null;        
    private static com.sun.jdo.api.persistence.support.PersistenceManagerFactory _jdoPersistenceManagerFactory = null;        
    private static com.sun.jdo.spi.persistence.support.sqlstore.utility.NumericConverter _jdoNumericConverter = null;        
    private static final Class _jdoInstanceClass = com.sun.ejb.containers.TimerMigrationBean649625130_JDOState.class;        
    private static final ResourceBundle _jdoMessages = com.sun.jdo.spi.persistence.utility.I18NHelper.loadBundle("com.sun.jdo.spi.persistence.support.ejb.ejbc.Bundle");        
    private static final String _jdoBeanName = "TimerMigrationBean";        
    public static final String _jdoGeneratorClassesSignature = "$RCSfile: JDOConcreteBeanGenerator.java,v $ $Revision: 1.4 $ ## $RCSfile: CMPTemplates.properties,v $ $Revision: 1.4 $ ## $RCSfile: DeploymentDescriptorModel.java,v $ $Revision: 1.3 $ ## $RCSfile: JDOConcreteBean20Generator.java,v $ $Revision: 1.3 $ ## $RCSfile: CMP20Templates.properties,v $ $Revision: 1.3 $ ## $RCSfile: EJBQLC.java,v $ $Revision: 1.4 $ ## $RCSfile: JDOCodeGenerator.java,v $ $Revision: 1.8 $ ## $RCSfile: NameMapper.java,v $ $Revision: 1.3 $";        
    public static final String _jdoInputFilesSignature = "C:/GlassFishESB/glassfish/domains/domain1/applications/j2ee-apps/__ejb_container_timer_app/ejb_jar/META-INF/ejb-jar.xml 14401 bytes ## C:/GlassFishESB/glassfish/domains/domain1/applications/j2ee-apps/__ejb_container_timer_app/ejb_jar/META-INF/sun-ejb-jar.xml 2427 bytes ## C:/GlassFishESB/glassfish/domains/domain1/applications/j2ee-apps/__ejb_container_timer_app/ejb_jar/META-INF/sun-cmp-mappings.xml 4479 bytes";        
    private EntityContext _jdoEntityContext = null;        
    private static com.sun.jdo.api.persistence.support.Query _jdoQuery_ejbSelectAllTimersByOwner_0 = null;        
    private static final Object _jdoMonitor_ejbSelectAllTimersByOwner_0 = new Object();        

    public TimerMigrationBean649625130_ConcreteImpl ()        
    {        
        super();        
    }        

    public java.lang.String getTimerId ()        
    {        
        try {        
            return _jdoInstance.getTimerId();        
        } catch (com.sun.jdo.api.persistence.support.JDOException ex) {        
            com.sun.jdo.spi.persistence.support.ejb.cmp.CMPBeanHelper.logJDOExceptionWithInternalLogger(_jdoBeanName, ex);        
            throw ex;        
        }        
    }        
    public void setTimerId (java.lang.String param0)        
    {        
        com.sun.ejb.containers.TimerMigrationBean649625130_ConcreteImpl_JDOHelper.getHelperInstance().assertPrimaryKeyFieldNotNull(param0, "timerId", _jdoBeanName);        
        try {        
            _jdoInstance.setTimerId(param0.trim());        
        } catch (com.sun.jdo.api.persistence.support.JDOException ex) {         
            String msg = com.sun.jdo.spi.persistence.support.ejb.cmp.CMPBeanHelper.logJDOExceptionFromPKSetter(_jdoBeanName, ex);        
            throw new IllegalStateException(msg);        
        }        
    }        
    public java.lang.String getOwnerId ()        
    {        
        try {        
            return _jdoInstance.getOwnerId();        
        } catch (com.sun.jdo.api.persistence.support.JDOException ex) {        
            com.sun.jdo.spi.persistence.support.ejb.cmp.CMPBeanHelper.logJDOExceptionWithInternalLogger(_jdoBeanName, ex);        
            throw ex;        
        }        
    }        
    public void setOwnerId (java.lang.String param0)        
    {        
        try {        
            _jdoInstance.setOwnerId(param0);        
        } catch (com.sun.jdo.api.persistence.support.JDOException ex) {        
            com.sun.jdo.spi.persistence.support.ejb.cmp.CMPBeanHelper.logJDOExceptionWithInternalLogger(_jdoBeanName, ex);        
            throw ex;        
        }        
    }        
    private void jdoCleanCollectionRef ()        
    {        
    }        
    public java.util.Set ejbSelectAllTimersByOwner (java.lang.String param0)        
        throws javax.ejb.FinderException        
    {        
        com.sun.jdo.spi.persistence.support.sqlstore.ejb.CMPHelper.preSelect(        
            com.sun.ejb.containers.TimerMigrationBean649625130_ConcreteImpl_JDOHelper.getHelperInstance().getContainer());        
        java.util.Set rs;        
        com.sun.jdo.api.persistence.support.Query q = null;        
        Object[] params = null;        
        com.sun.jdo.api.persistence.support.PersistenceManager _jdoPersistenceManager = null;        
        try {        
            _jdoPersistenceManager = jdoGetPersistenceManager();        
            synchronized(_jdoMonitor_ejbSelectAllTimersByOwner_0) {         
                if (_jdoQuery_ejbSelectAllTimersByOwner_0 != null) {         
                    q = _jdoPersistenceManager.newQuery(_jdoQuery_ejbSelectAllTimersByOwner_0);        
                } else {        
                    q = _jdoPersistenceManager.newQuery();        
                    q.setClass(com.sun.ejb.containers.TimerMigrationBean649625130_ConcreteImpl.jdoGetJdoInstanceClass());        
                    q.setFilter("(this.ownerId == _jdoParam1)");        
                    q.declareParameters("java.lang.String _jdoParam1, ");        
                    q.declareVariables("");        
                    q.setResult("distinct this");        
                    q.setOrdering("");        
                    q.setPrefetchEnabled(true);        
                    q.compile();        
                    _jdoQuery_ejbSelectAllTimersByOwner_0 = q;         
                }         
            }         
            params = new Object[]{param0};        
            java.util.Collection result = (java.util.Collection)q.executeWithArray(params);         
            rs = com.sun.ejb.containers.TimerMigrationBean649625130_ConcreteImpl_JDOHelper.getHelperInstance().convertCollectionPCToEJBLocalObjectSet(result, _jdoPersistenceManager);        
        }        
        catch (com.sun.jdo.api.persistence.support.JDOException ex) {        
            com.sun.jdo.spi.persistence.support.ejb.cmp.CMPBeanHelper.logJDOExceptionWithFinderLogger(_jdoBeanName, params, ex);        
            throw ex;        
        }        
        finally {        
            jdoReleasePersistenceManager(_jdoPersistenceManager);        
        }        
        return rs;         
    }        
    public com.sun.ejb.containers.TimerPrimaryKey ejbFindByPrimaryKey (com.sun.ejb.containers.TimerPrimaryKey key)        
        throws javax.ejb.FinderException        
    {        
        assertPersistenceManagerIsNull();        
        com.sun.jdo.api.persistence.support.PersistenceManager _jdoPersistenceManager = null;        
        Object jdoObjectId = jdoGetObjectId(key);         
        try {        
            _jdoPersistenceManager = jdoGetPersistenceManager0(key);        
            Object _jdoInstance = _jdoPersistenceManager.getObjectById(jdoObjectId, true);        
            if (com.sun.jdo.api.persistence.support.JDOHelper.isDeleted(_jdoInstance)) {        
                throw new com.sun.jdo.api.persistence.support.JDOObjectNotFoundException(        
                    com.sun.jdo.spi.persistence.utility.I18NHelper.getMessage(        
                    _jdoMessages, "JDO.instancedeleted_exception", key.toString()));        
            }        
        } catch (com.sun.jdo.api.persistence.support.JDOObjectNotFoundException ex) {        
            com.sun.jdo.spi.persistence.support.ejb.cmp.CMPBeanHelper.handleJDOObjectNotFoundException(        
                key, _jdoBeanName, ex);        
        } catch (com.sun.jdo.api.persistence.support.JDOException ex) {        
            com.sun.jdo.spi.persistence.support.ejb.cmp.CMPBeanHelper.logFinderException(        
                com.sun.jdo.spi.persistence.utility.logging.Logger.WARNING, _jdoBeanName, ex);        
            throw ex;        
        } finally {        
            jdoReleasePersistenceManager0(_jdoPersistenceManager);        
        }        
        return key;        
    }        
    public void unsetEntityContext ()        
    {        
        super.unsetEntityContext();        
        _jdoEntityContext = null;        
    }        
    public void ejbPassivate ()        
    {        
        super.ejbPassivate();        
        jdoCleanAllRefs();        
    }        
    public void ejbRemove ()        
    {        
        com.sun.jdo.spi.persistence.support.ejb.cmp.CMPBeanHelper.assertPersistenceManagerNotNull(_jdoPersistenceManager, this);        
        try {        
            super.ejbRemove();        
            _jdoPersistenceManager.deletePersistent(_jdoInstance);        
        } catch (com.sun.jdo.api.persistence.support.JDOException ex) {        
            com.sun.jdo.spi.persistence.support.ejb.cmp.CMPBeanHelper.logJDOExceptionWithLifecycleLogger(        
                "GEN.generic_method_exception", _jdoBeanName, ex);        
            throw ex;        
        }        
    }        
    public void beforeCompletion ()        
    {        
    }        
    public void ejb__flush ()        
        throws javax.ejb.DuplicateKeyException        
    {        
        com.sun.jdo.api.persistence.support.PersistenceManager _jdoPersistenceManager = null;        
        try {        
            _jdoPersistenceManager = jdoGetPersistenceManager();        
            com.sun.jdo.spi.persistence.support.sqlstore.ejb.CMPHelper.flush(_jdoPersistenceManager);        
        } finally {        
            jdoReleasePersistenceManager(_jdoPersistenceManager);        
        }        
    }        
    public void afterCompletion (int param0)        
    {        
        jdoCleanAllRefs();        
    }        
    public void ejb__remove (Object param0)        
    {        
    }        
    private byte[] jdoArrayCopy (byte[] param0)        
    {        
        if (param0 == null)         
            return null;        
        byte[] tmp = new byte[param0.length];        
        System.arraycopy(param0, 0, tmp, 0, param0.length);        
        return tmp;        
    }        
    public void setEntityContext (javax.ejb.EntityContext param0)        
    {        
        super.setEntityContext(param0);        
        try {        
            _jdoEntityContext = param0;        
            jdoLookupPersistenceManagerFactory();        
        } catch (com.sun.jdo.api.persistence.support.JDOException ex) {        
            com.sun.jdo.spi.persistence.support.ejb.cmp.CMPBeanHelper.logJDOExceptionWithLifecycleLogger(        
                "GEN.generic_method_exception", _jdoBeanName, ex);        
            throw ex;        
        }        
    }        
    private Object jdoGetObjectId (com.sun.ejb.containers.TimerPrimaryKey key)        
    {        
        return com.sun.ejb.containers.TimerMigrationBean649625130_ConcreteImpl_JDOHelper.getHelperInstance().convertPrimaryKeyToObjectId(key);        
    }        
    public static java.lang.Class jdoGetJdoInstanceClass ()        
    {        
        return _jdoInstanceClass;        
    }        
    private void assertPersistenceManagerIsNull ()        
    {        
        com.sun.jdo.spi.persistence.support.ejb.cmp.CMPBeanHelper.assertPersistenceManagerIsNull(_jdoPersistenceManager, this);        
    }        
    private void assertInTransaction ()        
    {        
        if (_jdoPersistenceManager != null &&         
            !_jdoPersistenceManager.currentTransaction().isActive()) {        
                throw new EJBException("JDO74001: Transaction is not active.");        
        }        
    }        
    private void jdoClosePersistenceManager ()        
    {        
        if (_jdoPersistenceManager != null) {        
            _jdoPersistenceManager.close();        
            jdoCleanAllRefs(); // This nullifies all references.        
        }        
    }        
    private com.sun.jdo.api.persistence.support.PersistenceManager jdoGetPersistenceManager ()        
    {        
        if(_jdoPersistenceManagerFactory == null) {        
            throw new com.sun.jdo.api.persistence.support.JDOFatalInternalException(        
                com.sun.jdo.spi.persistence.utility.I18NHelper.getMessage(        
                    _jdoMessages, "JDO.pmfnull_exception"));        
        }        
        return _jdoPersistenceManagerFactory.getPersistenceManager();        
    }        
    public com.sun.jdo.api.persistence.support.PersistenceManager jdoGetPersistenceManager0 (com.sun.ejb.containers.TimerPrimaryKey key)        
    {        
        return jdoGetPersistenceManager();        
    }        
    private static synchronized void jdoLookupPersistenceManagerFactory ()        
    {        
        if (_jdoPersistenceManagerFactory == null) {        
            _jdoPersistenceManagerFactory = com.sun.jdo.spi.persistence.support.sqlstore.ejb.CMPHelper.getPersistenceManagerFactory(        
                com.sun.ejb.containers.TimerMigrationBean649625130_ConcreteImpl_JDOHelper.getHelperInstance().getContainer());        
            _jdoPersistenceManagerFactory.setRequireCopyObjectId(false);        
            _jdoPersistenceManagerFactory.setRequireTrackedSCO(false);        
            _jdoNumericConverter = com.sun.ejb.containers.TimerMigrationBean649625130_ConcreteImpl_JDOHelper.getHelperInstance().getNumericConverter();        
        }        
    }        
    private void jdoGetInstance ()        
    {        
        com.sun.ejb.containers.TimerPrimaryKey key = (com.sun.ejb.containers.TimerPrimaryKey)  _jdoEntityContext.getPrimaryKey();        
        Object jdoObjectId = jdoGetObjectId(key);         
        _jdoInstance = (com.sun.ejb.containers.TimerMigrationBean649625130_JDOState) _jdoPersistenceManager.getObjectById(jdoObjectId, true);        
        if (com.sun.jdo.api.persistence.support.JDOHelper.isDeleted(_jdoInstance)) {        
            throw new com.sun.jdo.api.persistence.support.JDOObjectNotFoundException(        
                com.sun.jdo.spi.persistence.utility.I18NHelper.getMessage(_jdoMessages,         
                "JDO.instancedeleted_exception", key.toString()));        
        }        
    }        
    public void ejb__refresh (Object param0)        
    {        
    }        
    private void jdoReleasePersistenceManager (com.sun.jdo.api.persistence.support.PersistenceManager param0)        
    {        
        if (param0 != null) {        
            param0.close();        
        }        
    }        
    private void jdoReleasePersistenceManager0 (com.sun.jdo.api.persistence.support.PersistenceManager param0)        
    {        
        if (param0 != null) {        
            param0.close();        
        }        
    }        
    public void ejbLoad ()        
    {        
        assertPersistenceManagerIsNull();        
        boolean success = false;        
        try {        
            _jdoPersistenceManager = jdoGetPersistenceManager();        
            jdoGetInstance();        
            super.ejbLoad();        
            success = true;        
        } catch (com.sun.jdo.api.persistence.support.JDOException ex) {        
            com.sun.jdo.spi.persistence.support.ejb.cmp.CMPBeanHelper.logJDOExceptionWithLifecycleLogger(        
                "GEN.generic_method_exception", _jdoBeanName, ex);        
            throw (ex instanceof com.sun.jdo.api.persistence.support.JDOObjectNotFoundException) ?         
                (RuntimeException) new NoSuchEntityException(ex) : ex;        
        } finally {        
            //Do not register if there is no active transaction (in RO beans).        
            if (success && _jdoPersistenceManager.currentTransaction().isActive())        
                com.sun.jdo.api.persistence.support.SynchronizationManager.registerSynchronization(this, _jdoPersistenceManager);        
            else jdoClosePersistenceManager();        
        }        
    }        
    public void ejbStore ()        
    {        
        com.sun.jdo.spi.persistence.support.ejb.cmp.CMPBeanHelper.assertPersistenceManagerNotNull(_jdoPersistenceManager, this);        
        super.ejbStore();        
    }        
    public void jdoCleanAllRefs ()        
    {        
        _jdoInstance = null;        
        _jdoPersistenceManager = null;        
        jdoCleanCollectionRef();        
    }        

}


