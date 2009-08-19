
package com.sun.ejb.containers;

import java.util.Collection;
import java.util.ResourceBundle;
import javax.ejb.*;
import com.sun.jdo.spi.persistence.support.ejb.cmp.EJBHashSet;

// This is the generated concrete CMP bean class to be used with Transparent Persistence runtime.
public class TimerBean_2100919770_ConcreteImpl
    extends com.sun.ejb.containers.TimerBean
    implements javax.transaction.Synchronization, com.sun.ejb.spi.container.BeanStateSynchronization
{
    private transient com.sun.ejb.containers.TimerBean_2100919770_JDOState _jdoInstance = null;        
    private transient com.sun.jdo.api.persistence.support.PersistenceManager _jdoPersistenceManager = null;        
    private static com.sun.jdo.api.persistence.support.PersistenceManagerFactory _jdoPersistenceManagerFactory = null;        
    private static com.sun.jdo.spi.persistence.support.sqlstore.utility.NumericConverter _jdoNumericConverter = null;        
    private static final Class _jdoInstanceClass = com.sun.ejb.containers.TimerBean_2100919770_JDOState.class;        
    private static final ResourceBundle _jdoMessages = com.sun.jdo.spi.persistence.utility.I18NHelper.loadBundle("com.sun.jdo.spi.persistence.support.ejb.ejbc.Bundle");        
    private static final String _jdoBeanName = "TimerBean";        
    public static final String _jdoGeneratorClassesSignature = "$RCSfile: JDOConcreteBeanGenerator.java,v $ $Revision: 1.4 $ ## $RCSfile: CMPTemplates.properties,v $ $Revision: 1.4 $ ## $RCSfile: DeploymentDescriptorModel.java,v $ $Revision: 1.3 $ ## $RCSfile: JDOConcreteBean20Generator.java,v $ $Revision: 1.3 $ ## $RCSfile: CMP20Templates.properties,v $ $Revision: 1.3 $ ## $RCSfile: EJBQLC.java,v $ $Revision: 1.4 $ ## $RCSfile: JDOCodeGenerator.java,v $ $Revision: 1.8 $ ## $RCSfile: NameMapper.java,v $ $Revision: 1.3 $";        
    public static final String _jdoInputFilesSignature = "C:/GlassFishESB/glassfish/domains/domain1/applications/j2ee-apps/__ejb_container_timer_app/ejb_jar/META-INF/ejb-jar.xml 14401 bytes ## C:/GlassFishESB/glassfish/domains/domain1/applications/j2ee-apps/__ejb_container_timer_app/ejb_jar/META-INF/sun-ejb-jar.xml 2427 bytes ## C:/GlassFishESB/glassfish/domains/domain1/applications/j2ee-apps/__ejb_container_timer_app/ejb_jar/META-INF/sun-cmp-mappings.xml 4479 bytes";        
    private EntityContext _jdoEntityContext = null;        
    private static com.sun.jdo.api.persistence.support.Query _jdoQuery_ejbSelectTimersByContainerAndOwnerAndState_0 = null;        
    private static final Object _jdoMonitor_ejbSelectTimersByContainerAndOwnerAndState_0 = new Object();        
    private static com.sun.jdo.api.persistence.support.Query _jdoQuery_ejbSelectCountTimersByContainerAndState_1 = null;        
    private static final Object _jdoMonitor_ejbSelectCountTimersByContainerAndState_1 = new Object();        
    private static com.sun.jdo.api.persistence.support.Query _jdoQuery_ejbSelectTimerIdsByContainerAndOwnerAndState_2 = null;        
    private static final Object _jdoMonitor_ejbSelectTimerIdsByContainerAndOwnerAndState_2 = new Object();        
    private static com.sun.jdo.api.persistence.support.Query _jdoQuery_ejbSelectCountTimersByContainer_3 = null;        
    private static final Object _jdoMonitor_ejbSelectCountTimersByContainer_3 = new Object();        
    private static com.sun.jdo.api.persistence.support.Query _jdoQuery_ejbSelectAllTimerIdsByOwnerAndState_4 = null;        
    private static final Object _jdoMonitor_ejbSelectAllTimerIdsByOwnerAndState_4 = new Object();        
    private static com.sun.jdo.api.persistence.support.Query _jdoQuery_ejbSelectTimersByContainerAndState_5 = null;        
    private static final Object _jdoMonitor_ejbSelectTimersByContainerAndState_5 = new Object();        
    private static com.sun.jdo.api.persistence.support.Query _jdoQuery_ejbSelectTimerIdsByContainerAndState_6 = null;        
    private static final Object _jdoMonitor_ejbSelectTimerIdsByContainerAndState_6 = new Object();        
    private static com.sun.jdo.api.persistence.support.Query _jdoQuery_ejbSelectCountAllTimersByOwnerAndState_7 = null;        
    private static final Object _jdoMonitor_ejbSelectCountAllTimersByOwnerAndState_7 = new Object();        
    private static com.sun.jdo.api.persistence.support.Query _jdoQuery_ejbSelectCountTimersByContainerAndOwner_8 = null;        
    private static final Object _jdoMonitor_ejbSelectCountTimersByContainerAndOwner_8 = new Object();        
    private static com.sun.jdo.api.persistence.support.Query _jdoQuery_ejbSelectTimerIdsByContainer_9 = null;        
    private static final Object _jdoMonitor_ejbSelectTimerIdsByContainer_9 = new Object();        
    private static com.sun.jdo.api.persistence.support.Query _jdoQuery_ejbSelectTimersByContainer_10 = null;        
    private static final Object _jdoMonitor_ejbSelectTimersByContainer_10 = new Object();        
    private static com.sun.jdo.api.persistence.support.Query _jdoQuery_ejbSelectAllTimersByOwner_11 = null;        
    private static final Object _jdoMonitor_ejbSelectAllTimersByOwner_11 = new Object();        
    private static com.sun.jdo.api.persistence.support.Query _jdoQuery_ejbSelectAllTimerIdsByOwner_12 = null;        
    private static final Object _jdoMonitor_ejbSelectAllTimerIdsByOwner_12 = new Object();        
    private static com.sun.jdo.api.persistence.support.Query _jdoQuery_ejbSelectAllTimersByOwnerAndState_13 = null;        
    private static final Object _jdoMonitor_ejbSelectAllTimersByOwnerAndState_13 = new Object();        
    private static com.sun.jdo.api.persistence.support.Query _jdoQuery_ejbSelectCountAllTimersByOwner_14 = null;        
    private static final Object _jdoMonitor_ejbSelectCountAllTimersByOwner_14 = new Object();        
    private static com.sun.jdo.api.persistence.support.Query _jdoQuery_ejbSelectCountTimersByContainerAndOwnerAndState_15 = null;        
    private static final Object _jdoMonitor_ejbSelectCountTimersByContainerAndOwnerAndState_15 = new Object();        
    private static com.sun.jdo.api.persistence.support.Query _jdoQuery_ejbSelectTimerIdsByContainerAndOwner_16 = null;        
    private static final Object _jdoMonitor_ejbSelectTimerIdsByContainerAndOwner_16 = new Object();        
    private static com.sun.jdo.api.persistence.support.Query _jdoQuery_ejbSelectTimersByContainerAndOwner_17 = null;        
    private static final Object _jdoMonitor_ejbSelectTimersByContainerAndOwner_17 = new Object();        

    public TimerBean_2100919770_ConcreteImpl ()        
    {        
        super();        
    }        

    public long getCreationTimeRaw ()        
    {        
        try {        
            return _jdoInstance.getCreationTimeRaw();        
        } catch (com.sun.jdo.api.persistence.support.JDOException ex) {        
            com.sun.jdo.spi.persistence.support.ejb.cmp.CMPBeanHelper.logJDOExceptionWithInternalLogger(_jdoBeanName, ex);        
            throw ex;        
        }        
    }        
    public void setCreationTimeRaw (long param0)        
    {        
        try {        
            _jdoInstance.setCreationTimeRaw(param0);        
        } catch (com.sun.jdo.api.persistence.support.JDOException ex) {        
            com.sun.jdo.spi.persistence.support.ejb.cmp.CMPBeanHelper.logJDOExceptionWithInternalLogger(_jdoBeanName, ex);        
            throw ex;        
        }        
    }        
    public com.sun.ejb.containers.TimerBean.Blob getBlob ()        
    {        
        try {        
            return (com.sun.ejb.containers.TimerBean.Blob)        
                com.sun.ejb.containers.TimerBean_2100919770_ConcreteImpl_JDOHelper.getHelperInstance().readSerializableObjectFromByteArray ( _jdoInstance.getBlob() );        
        } catch (com.sun.jdo.api.persistence.support.JDOException ex) {        
            com.sun.jdo.spi.persistence.support.ejb.cmp.CMPBeanHelper.logJDOExceptionWithInternalLogger(_jdoBeanName, ex);        
            throw ex;        
        }        
    }        
    public void setBlob (com.sun.ejb.containers.TimerBean.Blob param0)        
    {        
        try {        
            _jdoInstance.setBlob(com.sun.ejb.containers.TimerBean_2100919770_ConcreteImpl_JDOHelper.getHelperInstance().writeSerializableObjectToByteArray(param0) );        
        } catch (com.sun.jdo.api.persistence.support.JDOException ex) {        
            com.sun.jdo.spi.persistence.support.ejb.cmp.CMPBeanHelper.logJDOExceptionWithInternalLogger(_jdoBeanName, ex);        
            throw ex;        
        }        
    }        
    public long getLastExpirationRaw ()        
    {        
        try {        
            return _jdoInstance.getLastExpirationRaw();        
        } catch (com.sun.jdo.api.persistence.support.JDOException ex) {        
            com.sun.jdo.spi.persistence.support.ejb.cmp.CMPBeanHelper.logJDOExceptionWithInternalLogger(_jdoBeanName, ex);        
            throw ex;        
        }        
    }        
    public void setLastExpirationRaw (long param0)        
    {        
        try {        
            _jdoInstance.setLastExpirationRaw(param0);        
        } catch (com.sun.jdo.api.persistence.support.JDOException ex) {        
            com.sun.jdo.spi.persistence.support.ejb.cmp.CMPBeanHelper.logJDOExceptionWithInternalLogger(_jdoBeanName, ex);        
            throw ex;        
        }        
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
        com.sun.ejb.containers.TimerBean_2100919770_ConcreteImpl_JDOHelper.getHelperInstance().assertPrimaryKeyFieldNotNull(param0, "timerId", _jdoBeanName);        
        try {        
            _jdoInstance.setTimerId(param0.trim());        
        } catch (com.sun.jdo.api.persistence.support.JDOException ex) {         
            String msg = com.sun.jdo.spi.persistence.support.ejb.cmp.CMPBeanHelper.logJDOExceptionFromPKSetter(_jdoBeanName, ex);        
            throw new IllegalStateException(msg);        
        }        
    }        
    public long getContainerId ()        
    {        
        try {        
            return _jdoInstance.getContainerId();        
        } catch (com.sun.jdo.api.persistence.support.JDOException ex) {        
            com.sun.jdo.spi.persistence.support.ejb.cmp.CMPBeanHelper.logJDOExceptionWithInternalLogger(_jdoBeanName, ex);        
            throw ex;        
        }        
    }        
    public void setContainerId (long param0)        
    {        
        try {        
            _jdoInstance.setContainerId(param0);        
        } catch (com.sun.jdo.api.persistence.support.JDOException ex) {        
            com.sun.jdo.spi.persistence.support.ejb.cmp.CMPBeanHelper.logJDOExceptionWithInternalLogger(_jdoBeanName, ex);        
            throw ex;        
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
    public int getState ()        
    {        
        try {        
            return _jdoInstance.getState();        
        } catch (com.sun.jdo.api.persistence.support.JDOException ex) {        
            com.sun.jdo.spi.persistence.support.ejb.cmp.CMPBeanHelper.logJDOExceptionWithInternalLogger(_jdoBeanName, ex);        
            throw ex;        
        }        
    }        
    public void setState (int param0)        
    {        
        try {        
            _jdoInstance.setState(param0);        
        } catch (com.sun.jdo.api.persistence.support.JDOException ex) {        
            com.sun.jdo.spi.persistence.support.ejb.cmp.CMPBeanHelper.logJDOExceptionWithInternalLogger(_jdoBeanName, ex);        
            throw ex;        
        }        
    }        
    public int getPkHashCode ()        
    {        
        try {        
            return _jdoInstance.getPkHashCode();        
        } catch (com.sun.jdo.api.persistence.support.JDOException ex) {        
            com.sun.jdo.spi.persistence.support.ejb.cmp.CMPBeanHelper.logJDOExceptionWithInternalLogger(_jdoBeanName, ex);        
            throw ex;        
        }        
    }        
    public void setPkHashCode (int param0)        
    {        
        try {        
            _jdoInstance.setPkHashCode(param0);        
        } catch (com.sun.jdo.api.persistence.support.JDOException ex) {        
            com.sun.jdo.spi.persistence.support.ejb.cmp.CMPBeanHelper.logJDOExceptionWithInternalLogger(_jdoBeanName, ex);        
            throw ex;        
        }        
    }        
    public long getIntervalDuration ()        
    {        
        try {        
            return _jdoInstance.getIntervalDuration();        
        } catch (com.sun.jdo.api.persistence.support.JDOException ex) {        
            com.sun.jdo.spi.persistence.support.ejb.cmp.CMPBeanHelper.logJDOExceptionWithInternalLogger(_jdoBeanName, ex);        
            throw ex;        
        }        
    }        
    public void setIntervalDuration (long param0)        
    {        
        try {        
            _jdoInstance.setIntervalDuration(param0);        
        } catch (com.sun.jdo.api.persistence.support.JDOException ex) {        
            com.sun.jdo.spi.persistence.support.ejb.cmp.CMPBeanHelper.logJDOExceptionWithInternalLogger(_jdoBeanName, ex);        
            throw ex;        
        }        
    }        
    public long getInitialExpirationRaw ()        
    {        
        try {        
            return _jdoInstance.getInitialExpirationRaw();        
        } catch (com.sun.jdo.api.persistence.support.JDOException ex) {        
            com.sun.jdo.spi.persistence.support.ejb.cmp.CMPBeanHelper.logJDOExceptionWithInternalLogger(_jdoBeanName, ex);        
            throw ex;        
        }        
    }        
    public void setInitialExpirationRaw (long param0)        
    {        
        try {        
            _jdoInstance.setInitialExpirationRaw(param0);        
        } catch (com.sun.jdo.api.persistence.support.JDOException ex) {        
            com.sun.jdo.spi.persistence.support.ejb.cmp.CMPBeanHelper.logJDOExceptionWithInternalLogger(_jdoBeanName, ex);        
            throw ex;        
        }        
    }        
    private void jdoCleanCollectionRef ()        
    {        
    }        
    public java.util.Set ejbSelectTimersByContainerAndOwnerAndState (long param0,         
        java.lang.String param1,         
        int param2)        
        throws javax.ejb.FinderException        
    {        
        com.sun.jdo.spi.persistence.support.sqlstore.ejb.CMPHelper.preSelect(        
            com.sun.ejb.containers.TimerBean_2100919770_ConcreteImpl_JDOHelper.getHelperInstance().getContainer());        
        java.util.Set rs;        
        com.sun.jdo.api.persistence.support.Query q = null;        
        Object[] params = null;        
        com.sun.jdo.api.persistence.support.PersistenceManager _jdoPersistenceManager = null;        
        try {        
            _jdoPersistenceManager = jdoGetPersistenceManager();        
            synchronized(_jdoMonitor_ejbSelectTimersByContainerAndOwnerAndState_0) {         
                if (_jdoQuery_ejbSelectTimersByContainerAndOwnerAndState_0 != null) {         
                    q = _jdoPersistenceManager.newQuery(_jdoQuery_ejbSelectTimersByContainerAndOwnerAndState_0);        
                } else {        
                    q = _jdoPersistenceManager.newQuery();        
                    q.setClass(com.sun.ejb.containers.TimerBean_2100919770_ConcreteImpl.jdoGetJdoInstanceClass());        
                    q.setFilter("(((this.containerId == _jdoParam1) & (this.ownerId == _jdoParam2)) & (this.state == _jdoParam3))");        
                    q.declareParameters("long _jdoParam1, java.lang.String _jdoParam2, int _jdoParam3, ");        
                    q.declareVariables("");        
                    q.setResult("distinct this");        
                    q.setOrdering("");        
                    q.setPrefetchEnabled(true);        
                    q.compile();        
                    _jdoQuery_ejbSelectTimersByContainerAndOwnerAndState_0 = q;         
                }         
            }         
            params = new Object[]{new java.lang.Long(param0), param1, new java.lang.Integer(param2)};        
            java.util.Collection result = (java.util.Collection)q.executeWithArray(params);         
            rs = com.sun.ejb.containers.TimerBean_2100919770_ConcreteImpl_JDOHelper.getHelperInstance().convertCollectionPCToEJBLocalObjectSet(result, _jdoPersistenceManager);        
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
    public int ejbSelectCountTimersByContainerAndState (long param0,         
        int param1)        
        throws javax.ejb.FinderException        
    {        
        com.sun.jdo.spi.persistence.support.sqlstore.ejb.CMPHelper.preSelect(        
            com.sun.ejb.containers.TimerBean_2100919770_ConcreteImpl_JDOHelper.getHelperInstance().getContainer());        
        int rs;        
        com.sun.jdo.api.persistence.support.Query q = null;        
        Object[] params = null;        
        com.sun.jdo.api.persistence.support.PersistenceManager _jdoPersistenceManager = null;        
        try {        
            _jdoPersistenceManager = jdoGetPersistenceManager();        
            synchronized(_jdoMonitor_ejbSelectCountTimersByContainerAndState_1) {         
                if (_jdoQuery_ejbSelectCountTimersByContainerAndState_1 != null) {         
                    q = _jdoPersistenceManager.newQuery(_jdoQuery_ejbSelectCountTimersByContainerAndState_1);        
                } else {        
                    q = _jdoPersistenceManager.newQuery();        
                    q.setClass(com.sun.ejb.containers.TimerBean_2100919770_ConcreteImpl.jdoGetJdoInstanceClass());        
                    q.setFilter("((this.containerId == _jdoParam1) & (this.state == _jdoParam2))");        
                    q.declareParameters("long _jdoParam1, int _jdoParam2, ");        
                    q.declareVariables("");        
                    q.setResult("count(this)");        
                    q.setOrdering("");        
                    q.setPrefetchEnabled(true);        
                    q.compile();        
                    _jdoQuery_ejbSelectCountTimersByContainerAndState_1 = q;         
                }         
            }         
            params = new Object[]{new java.lang.Long(param0), new java.lang.Integer(param1)};        
            Object result = q.executeWithArray(params);         
            if (result == null) {         
                String msg = com.sun.jdo.spi.persistence.utility.I18NHelper.getMessage(        
                    _jdoMessages, "GEN.primitivetypenull_exception", "ejbSelectCountTimersByContainerAndState");        
                if (params != null) {        
                    msg = msg + " " + com.sun.jdo.spi.persistence.utility.I18NHelper.getMessage(_jdoMessages, "GEN.parameters") +         
            java.util.Arrays.asList(params);        
                }        
                ObjectNotFoundException ex = new ObjectNotFoundException(msg);        
                com.sun.jdo.spi.persistence.support.ejb.cmp.CMPBeanHelper.logFinderException(        
                    com.sun.jdo.spi.persistence.utility.logging.Logger.FINER, _jdoBeanName, ex);        
                throw ex;        
            }         
            rs = ((java.lang.Long)result).intValue();        
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
    public java.util.Set ejbSelectTimerIdsByContainerAndOwnerAndState (long param0,         
        java.lang.String param1,         
        int param2)        
        throws javax.ejb.FinderException        
    {        
        com.sun.jdo.spi.persistence.support.sqlstore.ejb.CMPHelper.preSelect(        
            com.sun.ejb.containers.TimerBean_2100919770_ConcreteImpl_JDOHelper.getHelperInstance().getContainer());        
        java.util.Set rs;        
        com.sun.jdo.api.persistence.support.Query q = null;        
        Object[] params = null;        
        com.sun.jdo.api.persistence.support.PersistenceManager _jdoPersistenceManager = null;        
        try {        
            _jdoPersistenceManager = jdoGetPersistenceManager();        
            synchronized(_jdoMonitor_ejbSelectTimerIdsByContainerAndOwnerAndState_2) {         
                if (_jdoQuery_ejbSelectTimerIdsByContainerAndOwnerAndState_2 != null) {         
                    q = _jdoPersistenceManager.newQuery(_jdoQuery_ejbSelectTimerIdsByContainerAndOwnerAndState_2);        
                } else {        
                    q = _jdoPersistenceManager.newQuery();        
                    q.setClass(com.sun.ejb.containers.TimerBean_2100919770_ConcreteImpl.jdoGetJdoInstanceClass());        
                    q.setFilter("(((this.containerId == _jdoParam1) & (this.ownerId == _jdoParam2)) & (this.state == _jdoParam3))");        
                    q.declareParameters("long _jdoParam1, java.lang.String _jdoParam2, int _jdoParam3, ");        
                    q.declareVariables("");        
                    q.setResult("distinct this.timerId");        
                    q.setOrdering("");        
                    q.setPrefetchEnabled(true);        
                    q.compile();        
                    _jdoQuery_ejbSelectTimerIdsByContainerAndOwnerAndState_2 = q;         
                }         
            }         
            params = new Object[]{new java.lang.Long(param0), param1, new java.lang.Integer(param2)};        
            java.util.Collection result = (java.util.Collection)q.executeWithArray(params);         
            rs = new java.util.HashSet(result);         
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
    public int ejbSelectCountTimersByContainer (long param0)        
        throws javax.ejb.FinderException        
    {        
        com.sun.jdo.spi.persistence.support.sqlstore.ejb.CMPHelper.preSelect(        
            com.sun.ejb.containers.TimerBean_2100919770_ConcreteImpl_JDOHelper.getHelperInstance().getContainer());        
        int rs;        
        com.sun.jdo.api.persistence.support.Query q = null;        
        Object[] params = null;        
        com.sun.jdo.api.persistence.support.PersistenceManager _jdoPersistenceManager = null;        
        try {        
            _jdoPersistenceManager = jdoGetPersistenceManager();        
            synchronized(_jdoMonitor_ejbSelectCountTimersByContainer_3) {         
                if (_jdoQuery_ejbSelectCountTimersByContainer_3 != null) {         
                    q = _jdoPersistenceManager.newQuery(_jdoQuery_ejbSelectCountTimersByContainer_3);        
                } else {        
                    q = _jdoPersistenceManager.newQuery();        
                    q.setClass(com.sun.ejb.containers.TimerBean_2100919770_ConcreteImpl.jdoGetJdoInstanceClass());        
                    q.setFilter("(this.containerId == _jdoParam1)");        
                    q.declareParameters("long _jdoParam1, ");        
                    q.declareVariables("");        
                    q.setResult("count(this)");        
                    q.setOrdering("");        
                    q.setPrefetchEnabled(true);        
                    q.compile();        
                    _jdoQuery_ejbSelectCountTimersByContainer_3 = q;         
                }         
            }         
            params = new Object[]{new java.lang.Long(param0)};        
            Object result = q.executeWithArray(params);         
            if (result == null) {         
                String msg = com.sun.jdo.spi.persistence.utility.I18NHelper.getMessage(        
                    _jdoMessages, "GEN.primitivetypenull_exception", "ejbSelectCountTimersByContainer");        
                if (params != null) {        
                    msg = msg + " " + com.sun.jdo.spi.persistence.utility.I18NHelper.getMessage(_jdoMessages, "GEN.parameters") +         
            java.util.Arrays.asList(params);        
                }        
                ObjectNotFoundException ex = new ObjectNotFoundException(msg);        
                com.sun.jdo.spi.persistence.support.ejb.cmp.CMPBeanHelper.logFinderException(        
                    com.sun.jdo.spi.persistence.utility.logging.Logger.FINER, _jdoBeanName, ex);        
                throw ex;        
            }         
            rs = ((java.lang.Long)result).intValue();        
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
    public java.util.Set ejbSelectAllTimerIdsByOwnerAndState (java.lang.String param0,         
        int param1)        
        throws javax.ejb.FinderException        
    {        
        com.sun.jdo.spi.persistence.support.sqlstore.ejb.CMPHelper.preSelect(        
            com.sun.ejb.containers.TimerBean_2100919770_ConcreteImpl_JDOHelper.getHelperInstance().getContainer());        
        java.util.Set rs;        
        com.sun.jdo.api.persistence.support.Query q = null;        
        Object[] params = null;        
        com.sun.jdo.api.persistence.support.PersistenceManager _jdoPersistenceManager = null;        
        try {        
            _jdoPersistenceManager = jdoGetPersistenceManager();        
            synchronized(_jdoMonitor_ejbSelectAllTimerIdsByOwnerAndState_4) {         
                if (_jdoQuery_ejbSelectAllTimerIdsByOwnerAndState_4 != null) {         
                    q = _jdoPersistenceManager.newQuery(_jdoQuery_ejbSelectAllTimerIdsByOwnerAndState_4);        
                } else {        
                    q = _jdoPersistenceManager.newQuery();        
                    q.setClass(com.sun.ejb.containers.TimerBean_2100919770_ConcreteImpl.jdoGetJdoInstanceClass());        
                    q.setFilter("((this.ownerId == _jdoParam1) & (this.state == _jdoParam2))");        
                    q.declareParameters("java.lang.String _jdoParam1, int _jdoParam2, ");        
                    q.declareVariables("");        
                    q.setResult("distinct this.timerId");        
                    q.setOrdering("");        
                    q.setPrefetchEnabled(true);        
                    q.compile();        
                    _jdoQuery_ejbSelectAllTimerIdsByOwnerAndState_4 = q;         
                }         
            }         
            params = new Object[]{param0, new java.lang.Integer(param1)};        
            java.util.Collection result = (java.util.Collection)q.executeWithArray(params);         
            rs = new java.util.HashSet(result);         
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
    public java.util.Set ejbSelectTimersByContainerAndState (long param0,         
        int param1)        
        throws javax.ejb.FinderException        
    {        
        com.sun.jdo.spi.persistence.support.sqlstore.ejb.CMPHelper.preSelect(        
            com.sun.ejb.containers.TimerBean_2100919770_ConcreteImpl_JDOHelper.getHelperInstance().getContainer());        
        java.util.Set rs;        
        com.sun.jdo.api.persistence.support.Query q = null;        
        Object[] params = null;        
        com.sun.jdo.api.persistence.support.PersistenceManager _jdoPersistenceManager = null;        
        try {        
            _jdoPersistenceManager = jdoGetPersistenceManager();        
            synchronized(_jdoMonitor_ejbSelectTimersByContainerAndState_5) {         
                if (_jdoQuery_ejbSelectTimersByContainerAndState_5 != null) {         
                    q = _jdoPersistenceManager.newQuery(_jdoQuery_ejbSelectTimersByContainerAndState_5);        
                } else {        
                    q = _jdoPersistenceManager.newQuery();        
                    q.setClass(com.sun.ejb.containers.TimerBean_2100919770_ConcreteImpl.jdoGetJdoInstanceClass());        
                    q.setFilter("((this.containerId == _jdoParam1) & (this.state == _jdoParam2))");        
                    q.declareParameters("long _jdoParam1, int _jdoParam2, ");        
                    q.declareVariables("");        
                    q.setResult("distinct this");        
                    q.setOrdering("");        
                    q.setPrefetchEnabled(true);        
                    q.compile();        
                    _jdoQuery_ejbSelectTimersByContainerAndState_5 = q;         
                }         
            }         
            params = new Object[]{new java.lang.Long(param0), new java.lang.Integer(param1)};        
            java.util.Collection result = (java.util.Collection)q.executeWithArray(params);         
            rs = com.sun.ejb.containers.TimerBean_2100919770_ConcreteImpl_JDOHelper.getHelperInstance().convertCollectionPCToEJBLocalObjectSet(result, _jdoPersistenceManager);        
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
    public java.util.Set ejbSelectTimerIdsByContainerAndState (long param0,         
        int param1)        
        throws javax.ejb.FinderException        
    {        
        com.sun.jdo.spi.persistence.support.sqlstore.ejb.CMPHelper.preSelect(        
            com.sun.ejb.containers.TimerBean_2100919770_ConcreteImpl_JDOHelper.getHelperInstance().getContainer());        
        java.util.Set rs;        
        com.sun.jdo.api.persistence.support.Query q = null;        
        Object[] params = null;        
        com.sun.jdo.api.persistence.support.PersistenceManager _jdoPersistenceManager = null;        
        try {        
            _jdoPersistenceManager = jdoGetPersistenceManager();        
            synchronized(_jdoMonitor_ejbSelectTimerIdsByContainerAndState_6) {         
                if (_jdoQuery_ejbSelectTimerIdsByContainerAndState_6 != null) {         
                    q = _jdoPersistenceManager.newQuery(_jdoQuery_ejbSelectTimerIdsByContainerAndState_6);        
                } else {        
                    q = _jdoPersistenceManager.newQuery();        
                    q.setClass(com.sun.ejb.containers.TimerBean_2100919770_ConcreteImpl.jdoGetJdoInstanceClass());        
                    q.setFilter("((this.containerId == _jdoParam1) & (this.state == _jdoParam2))");        
                    q.declareParameters("long _jdoParam1, int _jdoParam2, ");        
                    q.declareVariables("");        
                    q.setResult("distinct this.timerId");        
                    q.setOrdering("");        
                    q.setPrefetchEnabled(true);        
                    q.compile();        
                    _jdoQuery_ejbSelectTimerIdsByContainerAndState_6 = q;         
                }         
            }         
            params = new Object[]{new java.lang.Long(param0), new java.lang.Integer(param1)};        
            java.util.Collection result = (java.util.Collection)q.executeWithArray(params);         
            rs = new java.util.HashSet(result);         
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
    public int ejbSelectCountAllTimersByOwnerAndState (java.lang.String param0,         
        int param1)        
        throws javax.ejb.FinderException        
    {        
        com.sun.jdo.spi.persistence.support.sqlstore.ejb.CMPHelper.preSelect(        
            com.sun.ejb.containers.TimerBean_2100919770_ConcreteImpl_JDOHelper.getHelperInstance().getContainer());        
        int rs;        
        com.sun.jdo.api.persistence.support.Query q = null;        
        Object[] params = null;        
        com.sun.jdo.api.persistence.support.PersistenceManager _jdoPersistenceManager = null;        
        try {        
            _jdoPersistenceManager = jdoGetPersistenceManager();        
            synchronized(_jdoMonitor_ejbSelectCountAllTimersByOwnerAndState_7) {         
                if (_jdoQuery_ejbSelectCountAllTimersByOwnerAndState_7 != null) {         
                    q = _jdoPersistenceManager.newQuery(_jdoQuery_ejbSelectCountAllTimersByOwnerAndState_7);        
                } else {        
                    q = _jdoPersistenceManager.newQuery();        
                    q.setClass(com.sun.ejb.containers.TimerBean_2100919770_ConcreteImpl.jdoGetJdoInstanceClass());        
                    q.setFilter("((this.ownerId == _jdoParam1) & (this.state == _jdoParam2))");        
                    q.declareParameters("java.lang.String _jdoParam1, int _jdoParam2, ");        
                    q.declareVariables("");        
                    q.setResult("count(this)");        
                    q.setOrdering("");        
                    q.setPrefetchEnabled(true);        
                    q.compile();        
                    _jdoQuery_ejbSelectCountAllTimersByOwnerAndState_7 = q;         
                }         
            }         
            params = new Object[]{param0, new java.lang.Integer(param1)};        
            Object result = q.executeWithArray(params);         
            if (result == null) {         
                String msg = com.sun.jdo.spi.persistence.utility.I18NHelper.getMessage(        
                    _jdoMessages, "GEN.primitivetypenull_exception", "ejbSelectCountAllTimersByOwnerAndState");        
                if (params != null) {        
                    msg = msg + " " + com.sun.jdo.spi.persistence.utility.I18NHelper.getMessage(_jdoMessages, "GEN.parameters") +         
            java.util.Arrays.asList(params);        
                }        
                ObjectNotFoundException ex = new ObjectNotFoundException(msg);        
                com.sun.jdo.spi.persistence.support.ejb.cmp.CMPBeanHelper.logFinderException(        
                    com.sun.jdo.spi.persistence.utility.logging.Logger.FINER, _jdoBeanName, ex);        
                throw ex;        
            }         
            rs = ((java.lang.Long)result).intValue();        
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
    public int ejbSelectCountTimersByContainerAndOwner (long param0,         
        java.lang.String param1)        
        throws javax.ejb.FinderException        
    {        
        com.sun.jdo.spi.persistence.support.sqlstore.ejb.CMPHelper.preSelect(        
            com.sun.ejb.containers.TimerBean_2100919770_ConcreteImpl_JDOHelper.getHelperInstance().getContainer());        
        int rs;        
        com.sun.jdo.api.persistence.support.Query q = null;        
        Object[] params = null;        
        com.sun.jdo.api.persistence.support.PersistenceManager _jdoPersistenceManager = null;        
        try {        
            _jdoPersistenceManager = jdoGetPersistenceManager();        
            synchronized(_jdoMonitor_ejbSelectCountTimersByContainerAndOwner_8) {         
                if (_jdoQuery_ejbSelectCountTimersByContainerAndOwner_8 != null) {         
                    q = _jdoPersistenceManager.newQuery(_jdoQuery_ejbSelectCountTimersByContainerAndOwner_8);        
                } else {        
                    q = _jdoPersistenceManager.newQuery();        
                    q.setClass(com.sun.ejb.containers.TimerBean_2100919770_ConcreteImpl.jdoGetJdoInstanceClass());        
                    q.setFilter("((this.containerId == _jdoParam1) & (this.ownerId == _jdoParam2))");        
                    q.declareParameters("long _jdoParam1, java.lang.String _jdoParam2, ");        
                    q.declareVariables("");        
                    q.setResult("count(this)");        
                    q.setOrdering("");        
                    q.setPrefetchEnabled(true);        
                    q.compile();        
                    _jdoQuery_ejbSelectCountTimersByContainerAndOwner_8 = q;         
                }         
            }         
            params = new Object[]{new java.lang.Long(param0), param1};        
            Object result = q.executeWithArray(params);         
            if (result == null) {         
                String msg = com.sun.jdo.spi.persistence.utility.I18NHelper.getMessage(        
                    _jdoMessages, "GEN.primitivetypenull_exception", "ejbSelectCountTimersByContainerAndOwner");        
                if (params != null) {        
                    msg = msg + " " + com.sun.jdo.spi.persistence.utility.I18NHelper.getMessage(_jdoMessages, "GEN.parameters") +         
            java.util.Arrays.asList(params);        
                }        
                ObjectNotFoundException ex = new ObjectNotFoundException(msg);        
                com.sun.jdo.spi.persistence.support.ejb.cmp.CMPBeanHelper.logFinderException(        
                    com.sun.jdo.spi.persistence.utility.logging.Logger.FINER, _jdoBeanName, ex);        
                throw ex;        
            }         
            rs = ((java.lang.Long)result).intValue();        
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
    public java.util.Set ejbSelectTimerIdsByContainer (long param0)        
        throws javax.ejb.FinderException        
    {        
        com.sun.jdo.spi.persistence.support.sqlstore.ejb.CMPHelper.preSelect(        
            com.sun.ejb.containers.TimerBean_2100919770_ConcreteImpl_JDOHelper.getHelperInstance().getContainer());        
        java.util.Set rs;        
        com.sun.jdo.api.persistence.support.Query q = null;        
        Object[] params = null;        
        com.sun.jdo.api.persistence.support.PersistenceManager _jdoPersistenceManager = null;        
        try {        
            _jdoPersistenceManager = jdoGetPersistenceManager();        
            synchronized(_jdoMonitor_ejbSelectTimerIdsByContainer_9) {         
                if (_jdoQuery_ejbSelectTimerIdsByContainer_9 != null) {         
                    q = _jdoPersistenceManager.newQuery(_jdoQuery_ejbSelectTimerIdsByContainer_9);        
                } else {        
                    q = _jdoPersistenceManager.newQuery();        
                    q.setClass(com.sun.ejb.containers.TimerBean_2100919770_ConcreteImpl.jdoGetJdoInstanceClass());        
                    q.setFilter("(this.containerId == _jdoParam1)");        
                    q.declareParameters("long _jdoParam1, ");        
                    q.declareVariables("");        
                    q.setResult("distinct this.timerId");        
                    q.setOrdering("");        
                    q.setPrefetchEnabled(true);        
                    q.compile();        
                    _jdoQuery_ejbSelectTimerIdsByContainer_9 = q;         
                }         
            }         
            params = new Object[]{new java.lang.Long(param0)};        
            java.util.Collection result = (java.util.Collection)q.executeWithArray(params);         
            rs = new java.util.HashSet(result);         
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
    public java.util.Set ejbSelectTimersByContainer (long param0)        
        throws javax.ejb.FinderException        
    {        
        com.sun.jdo.spi.persistence.support.sqlstore.ejb.CMPHelper.preSelect(        
            com.sun.ejb.containers.TimerBean_2100919770_ConcreteImpl_JDOHelper.getHelperInstance().getContainer());        
        java.util.Set rs;        
        com.sun.jdo.api.persistence.support.Query q = null;        
        Object[] params = null;        
        com.sun.jdo.api.persistence.support.PersistenceManager _jdoPersistenceManager = null;        
        try {        
            _jdoPersistenceManager = jdoGetPersistenceManager();        
            synchronized(_jdoMonitor_ejbSelectTimersByContainer_10) {         
                if (_jdoQuery_ejbSelectTimersByContainer_10 != null) {         
                    q = _jdoPersistenceManager.newQuery(_jdoQuery_ejbSelectTimersByContainer_10);        
                } else {        
                    q = _jdoPersistenceManager.newQuery();        
                    q.setClass(com.sun.ejb.containers.TimerBean_2100919770_ConcreteImpl.jdoGetJdoInstanceClass());        
                    q.setFilter("(this.containerId == _jdoParam1)");        
                    q.declareParameters("long _jdoParam1, ");        
                    q.declareVariables("");        
                    q.setResult("distinct this");        
                    q.setOrdering("");        
                    q.setPrefetchEnabled(true);        
                    q.compile();        
                    _jdoQuery_ejbSelectTimersByContainer_10 = q;         
                }         
            }         
            params = new Object[]{new java.lang.Long(param0)};        
            java.util.Collection result = (java.util.Collection)q.executeWithArray(params);         
            rs = com.sun.ejb.containers.TimerBean_2100919770_ConcreteImpl_JDOHelper.getHelperInstance().convertCollectionPCToEJBLocalObjectSet(result, _jdoPersistenceManager);        
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
    public java.util.Set ejbSelectAllTimersByOwner (java.lang.String param0)        
        throws javax.ejb.FinderException        
    {        
        com.sun.jdo.spi.persistence.support.sqlstore.ejb.CMPHelper.preSelect(        
            com.sun.ejb.containers.TimerBean_2100919770_ConcreteImpl_JDOHelper.getHelperInstance().getContainer());        
        java.util.Set rs;        
        com.sun.jdo.api.persistence.support.Query q = null;        
        Object[] params = null;        
        com.sun.jdo.api.persistence.support.PersistenceManager _jdoPersistenceManager = null;        
        try {        
            _jdoPersistenceManager = jdoGetPersistenceManager();        
            synchronized(_jdoMonitor_ejbSelectAllTimersByOwner_11) {         
                if (_jdoQuery_ejbSelectAllTimersByOwner_11 != null) {         
                    q = _jdoPersistenceManager.newQuery(_jdoQuery_ejbSelectAllTimersByOwner_11);        
                } else {        
                    q = _jdoPersistenceManager.newQuery();        
                    q.setClass(com.sun.ejb.containers.TimerBean_2100919770_ConcreteImpl.jdoGetJdoInstanceClass());        
                    q.setFilter("(this.ownerId == _jdoParam1)");        
                    q.declareParameters("java.lang.String _jdoParam1, ");        
                    q.declareVariables("");        
                    q.setResult("distinct this");        
                    q.setOrdering("");        
                    q.setPrefetchEnabled(true);        
                    q.compile();        
                    _jdoQuery_ejbSelectAllTimersByOwner_11 = q;         
                }         
            }         
            params = new Object[]{param0};        
            java.util.Collection result = (java.util.Collection)q.executeWithArray(params);         
            rs = com.sun.ejb.containers.TimerBean_2100919770_ConcreteImpl_JDOHelper.getHelperInstance().convertCollectionPCToEJBLocalObjectSet(result, _jdoPersistenceManager);        
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
    public java.util.Set ejbSelectAllTimerIdsByOwner (java.lang.String param0)        
        throws javax.ejb.FinderException        
    {        
        com.sun.jdo.spi.persistence.support.sqlstore.ejb.CMPHelper.preSelect(        
            com.sun.ejb.containers.TimerBean_2100919770_ConcreteImpl_JDOHelper.getHelperInstance().getContainer());        
        java.util.Set rs;        
        com.sun.jdo.api.persistence.support.Query q = null;        
        Object[] params = null;        
        com.sun.jdo.api.persistence.support.PersistenceManager _jdoPersistenceManager = null;        
        try {        
            _jdoPersistenceManager = jdoGetPersistenceManager();        
            synchronized(_jdoMonitor_ejbSelectAllTimerIdsByOwner_12) {         
                if (_jdoQuery_ejbSelectAllTimerIdsByOwner_12 != null) {         
                    q = _jdoPersistenceManager.newQuery(_jdoQuery_ejbSelectAllTimerIdsByOwner_12);        
                } else {        
                    q = _jdoPersistenceManager.newQuery();        
                    q.setClass(com.sun.ejb.containers.TimerBean_2100919770_ConcreteImpl.jdoGetJdoInstanceClass());        
                    q.setFilter("(this.ownerId == _jdoParam1)");        
                    q.declareParameters("java.lang.String _jdoParam1, ");        
                    q.declareVariables("");        
                    q.setResult("distinct this.timerId");        
                    q.setOrdering("");        
                    q.setPrefetchEnabled(true);        
                    q.compile();        
                    _jdoQuery_ejbSelectAllTimerIdsByOwner_12 = q;         
                }         
            }         
            params = new Object[]{param0};        
            java.util.Collection result = (java.util.Collection)q.executeWithArray(params);         
            rs = new java.util.HashSet(result);         
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
    public java.util.Set ejbSelectAllTimersByOwnerAndState (java.lang.String param0,         
        int param1)        
        throws javax.ejb.FinderException        
    {        
        com.sun.jdo.spi.persistence.support.sqlstore.ejb.CMPHelper.preSelect(        
            com.sun.ejb.containers.TimerBean_2100919770_ConcreteImpl_JDOHelper.getHelperInstance().getContainer());        
        java.util.Set rs;        
        com.sun.jdo.api.persistence.support.Query q = null;        
        Object[] params = null;        
        com.sun.jdo.api.persistence.support.PersistenceManager _jdoPersistenceManager = null;        
        try {        
            _jdoPersistenceManager = jdoGetPersistenceManager();        
            synchronized(_jdoMonitor_ejbSelectAllTimersByOwnerAndState_13) {         
                if (_jdoQuery_ejbSelectAllTimersByOwnerAndState_13 != null) {         
                    q = _jdoPersistenceManager.newQuery(_jdoQuery_ejbSelectAllTimersByOwnerAndState_13);        
                } else {        
                    q = _jdoPersistenceManager.newQuery();        
                    q.setClass(com.sun.ejb.containers.TimerBean_2100919770_ConcreteImpl.jdoGetJdoInstanceClass());        
                    q.setFilter("((this.ownerId == _jdoParam1) & (this.state == _jdoParam2))");        
                    q.declareParameters("java.lang.String _jdoParam1, int _jdoParam2, ");        
                    q.declareVariables("");        
                    q.setResult("distinct this");        
                    q.setOrdering("");        
                    q.setPrefetchEnabled(true);        
                    q.compile();        
                    _jdoQuery_ejbSelectAllTimersByOwnerAndState_13 = q;         
                }         
            }         
            params = new Object[]{param0, new java.lang.Integer(param1)};        
            java.util.Collection result = (java.util.Collection)q.executeWithArray(params);         
            rs = com.sun.ejb.containers.TimerBean_2100919770_ConcreteImpl_JDOHelper.getHelperInstance().convertCollectionPCToEJBLocalObjectSet(result, _jdoPersistenceManager);        
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
    public int ejbSelectCountAllTimersByOwner (java.lang.String param0)        
        throws javax.ejb.FinderException        
    {        
        com.sun.jdo.spi.persistence.support.sqlstore.ejb.CMPHelper.preSelect(        
            com.sun.ejb.containers.TimerBean_2100919770_ConcreteImpl_JDOHelper.getHelperInstance().getContainer());        
        int rs;        
        com.sun.jdo.api.persistence.support.Query q = null;        
        Object[] params = null;        
        com.sun.jdo.api.persistence.support.PersistenceManager _jdoPersistenceManager = null;        
        try {        
            _jdoPersistenceManager = jdoGetPersistenceManager();        
            synchronized(_jdoMonitor_ejbSelectCountAllTimersByOwner_14) {         
                if (_jdoQuery_ejbSelectCountAllTimersByOwner_14 != null) {         
                    q = _jdoPersistenceManager.newQuery(_jdoQuery_ejbSelectCountAllTimersByOwner_14);        
                } else {        
                    q = _jdoPersistenceManager.newQuery();        
                    q.setClass(com.sun.ejb.containers.TimerBean_2100919770_ConcreteImpl.jdoGetJdoInstanceClass());        
                    q.setFilter("(this.ownerId == _jdoParam1)");        
                    q.declareParameters("java.lang.String _jdoParam1, ");        
                    q.declareVariables("");        
                    q.setResult("count(this)");        
                    q.setOrdering("");        
                    q.setPrefetchEnabled(true);        
                    q.compile();        
                    _jdoQuery_ejbSelectCountAllTimersByOwner_14 = q;         
                }         
            }         
            params = new Object[]{param0};        
            Object result = q.executeWithArray(params);         
            if (result == null) {         
                String msg = com.sun.jdo.spi.persistence.utility.I18NHelper.getMessage(        
                    _jdoMessages, "GEN.primitivetypenull_exception", "ejbSelectCountAllTimersByOwner");        
                if (params != null) {        
                    msg = msg + " " + com.sun.jdo.spi.persistence.utility.I18NHelper.getMessage(_jdoMessages, "GEN.parameters") +         
            java.util.Arrays.asList(params);        
                }        
                ObjectNotFoundException ex = new ObjectNotFoundException(msg);        
                com.sun.jdo.spi.persistence.support.ejb.cmp.CMPBeanHelper.logFinderException(        
                    com.sun.jdo.spi.persistence.utility.logging.Logger.FINER, _jdoBeanName, ex);        
                throw ex;        
            }         
            rs = ((java.lang.Long)result).intValue();        
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
    public int ejbSelectCountTimersByContainerAndOwnerAndState (long param0,         
        java.lang.String param1,         
        int param2)        
        throws javax.ejb.FinderException        
    {        
        com.sun.jdo.spi.persistence.support.sqlstore.ejb.CMPHelper.preSelect(        
            com.sun.ejb.containers.TimerBean_2100919770_ConcreteImpl_JDOHelper.getHelperInstance().getContainer());        
        int rs;        
        com.sun.jdo.api.persistence.support.Query q = null;        
        Object[] params = null;        
        com.sun.jdo.api.persistence.support.PersistenceManager _jdoPersistenceManager = null;        
        try {        
            _jdoPersistenceManager = jdoGetPersistenceManager();        
            synchronized(_jdoMonitor_ejbSelectCountTimersByContainerAndOwnerAndState_15) {         
                if (_jdoQuery_ejbSelectCountTimersByContainerAndOwnerAndState_15 != null) {         
                    q = _jdoPersistenceManager.newQuery(_jdoQuery_ejbSelectCountTimersByContainerAndOwnerAndState_15);        
                } else {        
                    q = _jdoPersistenceManager.newQuery();        
                    q.setClass(com.sun.ejb.containers.TimerBean_2100919770_ConcreteImpl.jdoGetJdoInstanceClass());        
                    q.setFilter("(((this.containerId == _jdoParam1) & (this.ownerId == _jdoParam2)) & (this.state == _jdoParam3))");        
                    q.declareParameters("long _jdoParam1, java.lang.String _jdoParam2, int _jdoParam3, ");        
                    q.declareVariables("");        
                    q.setResult("count(this)");        
                    q.setOrdering("");        
                    q.setPrefetchEnabled(true);        
                    q.compile();        
                    _jdoQuery_ejbSelectCountTimersByContainerAndOwnerAndState_15 = q;         
                }         
            }         
            params = new Object[]{new java.lang.Long(param0), param1, new java.lang.Integer(param2)};        
            Object result = q.executeWithArray(params);         
            if (result == null) {         
                String msg = com.sun.jdo.spi.persistence.utility.I18NHelper.getMessage(        
                    _jdoMessages, "GEN.primitivetypenull_exception", "ejbSelectCountTimersByContainerAndOwnerAndState");        
                if (params != null) {        
                    msg = msg + " " + com.sun.jdo.spi.persistence.utility.I18NHelper.getMessage(_jdoMessages, "GEN.parameters") +         
            java.util.Arrays.asList(params);        
                }        
                ObjectNotFoundException ex = new ObjectNotFoundException(msg);        
                com.sun.jdo.spi.persistence.support.ejb.cmp.CMPBeanHelper.logFinderException(        
                    com.sun.jdo.spi.persistence.utility.logging.Logger.FINER, _jdoBeanName, ex);        
                throw ex;        
            }         
            rs = ((java.lang.Long)result).intValue();        
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
    public java.util.Set ejbSelectTimerIdsByContainerAndOwner (long param0,         
        java.lang.String param1)        
        throws javax.ejb.FinderException        
    {        
        com.sun.jdo.spi.persistence.support.sqlstore.ejb.CMPHelper.preSelect(        
            com.sun.ejb.containers.TimerBean_2100919770_ConcreteImpl_JDOHelper.getHelperInstance().getContainer());        
        java.util.Set rs;        
        com.sun.jdo.api.persistence.support.Query q = null;        
        Object[] params = null;        
        com.sun.jdo.api.persistence.support.PersistenceManager _jdoPersistenceManager = null;        
        try {        
            _jdoPersistenceManager = jdoGetPersistenceManager();        
            synchronized(_jdoMonitor_ejbSelectTimerIdsByContainerAndOwner_16) {         
                if (_jdoQuery_ejbSelectTimerIdsByContainerAndOwner_16 != null) {         
                    q = _jdoPersistenceManager.newQuery(_jdoQuery_ejbSelectTimerIdsByContainerAndOwner_16);        
                } else {        
                    q = _jdoPersistenceManager.newQuery();        
                    q.setClass(com.sun.ejb.containers.TimerBean_2100919770_ConcreteImpl.jdoGetJdoInstanceClass());        
                    q.setFilter("((this.containerId == _jdoParam1) & (this.ownerId == _jdoParam2))");        
                    q.declareParameters("long _jdoParam1, java.lang.String _jdoParam2, ");        
                    q.declareVariables("");        
                    q.setResult("distinct this.timerId");        
                    q.setOrdering("");        
                    q.setPrefetchEnabled(true);        
                    q.compile();        
                    _jdoQuery_ejbSelectTimerIdsByContainerAndOwner_16 = q;         
                }         
            }         
            params = new Object[]{new java.lang.Long(param0), param1};        
            java.util.Collection result = (java.util.Collection)q.executeWithArray(params);         
            rs = new java.util.HashSet(result);         
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
    public java.util.Set ejbSelectTimersByContainerAndOwner (long param0,         
        java.lang.String param1)        
        throws javax.ejb.FinderException        
    {        
        com.sun.jdo.spi.persistence.support.sqlstore.ejb.CMPHelper.preSelect(        
            com.sun.ejb.containers.TimerBean_2100919770_ConcreteImpl_JDOHelper.getHelperInstance().getContainer());        
        java.util.Set rs;        
        com.sun.jdo.api.persistence.support.Query q = null;        
        Object[] params = null;        
        com.sun.jdo.api.persistence.support.PersistenceManager _jdoPersistenceManager = null;        
        try {        
            _jdoPersistenceManager = jdoGetPersistenceManager();        
            synchronized(_jdoMonitor_ejbSelectTimersByContainerAndOwner_17) {         
                if (_jdoQuery_ejbSelectTimersByContainerAndOwner_17 != null) {         
                    q = _jdoPersistenceManager.newQuery(_jdoQuery_ejbSelectTimersByContainerAndOwner_17);        
                } else {        
                    q = _jdoPersistenceManager.newQuery();        
                    q.setClass(com.sun.ejb.containers.TimerBean_2100919770_ConcreteImpl.jdoGetJdoInstanceClass());        
                    q.setFilter("((this.containerId == _jdoParam1) & (this.ownerId == _jdoParam2))");        
                    q.declareParameters("long _jdoParam1, java.lang.String _jdoParam2, ");        
                    q.declareVariables("");        
                    q.setResult("distinct this");        
                    q.setOrdering("");        
                    q.setPrefetchEnabled(true);        
                    q.compile();        
                    _jdoQuery_ejbSelectTimersByContainerAndOwner_17 = q;         
                }         
            }         
            params = new Object[]{new java.lang.Long(param0), param1};        
            java.util.Collection result = (java.util.Collection)q.executeWithArray(params);         
            rs = com.sun.ejb.containers.TimerBean_2100919770_ConcreteImpl_JDOHelper.getHelperInstance().convertCollectionPCToEJBLocalObjectSet(result, _jdoPersistenceManager);        
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
    public com.sun.ejb.containers.TimerPrimaryKey ejbCreate (java.lang.String param0,         
        long param1,         
        java.lang.String param2,         
        java.lang.Object param3,         
        java.util.Date param4,         
        long param5,         
        java.io.Serializable param6)        
        throws javax.ejb.CreateException        
    {        
        assertPersistenceManagerIsNull();        
        boolean success = false;        
        try {        
            _jdoInstance = new com.sun.ejb.containers.TimerBean_2100919770_JDOState();        
            _jdoPersistenceManager = jdoGetPersistenceManager();        
            super.ejbCreate(param0, param1, param2, param3, param4, param5, param6);        
            _jdoPersistenceManager.makePersistent(_jdoInstance);        
            success = true;        
            return (com.sun.ejb.containers.TimerPrimaryKey)com.sun.ejb.containers.TimerBean_2100919770_ConcreteImpl_JDOHelper.getHelperInstance().convertPCToPrimaryKey(        
                _jdoInstance, _jdoPersistenceManager);        
        } catch (com.sun.jdo.api.persistence.support.JDODuplicateObjectIdException ex) {        
            com.sun.jdo.spi.persistence.support.ejb.cmp.CMPBeanHelper.handleJDODuplicateObjectIdAsDuplicateKeyException(        
                _jdoBeanName, "" + param0 + ", " + param1 + ", " + param2 + ", " + param3 + ", " + param4 + ", " + param5 + ", " + param6, ex);        
            return null; // this is for compilation only - an exception will be thrown         
        } catch (com.sun.jdo.api.persistence.support.JDOException ex) {        
            com.sun.jdo.spi.persistence.support.ejb.cmp.CMPBeanHelper.logJDOExceptionWithLifecycleLogger(        
                "GEN.ejbcreate_exception_othr", _jdoBeanName, "" + param0 + ", " + param1 + ", " + param2 + ", " + param3 + ", " + param4 + ", " + param5 + ", " + param6, ex);        
            throw ex;        
        } finally {        
            if (success)        
                com.sun.jdo.api.persistence.support.SynchronizationManager.registerSynchronization(this, _jdoPersistenceManager);        
            else jdoClosePersistenceManager();        
        }        
    }        
    public void ejbPostCreate (java.lang.String param0,         
        long param1,         
        java.lang.String param2,         
        java.lang.Object param3,         
        java.util.Date param4,         
        long param5,         
        java.io.Serializable param6)        
        throws javax.ejb.CreateException        
    {        
        try {        
            super.ejbPostCreate(param0, param1, param2, param3, param4, param5, param6);        
        } catch (com.sun.jdo.api.persistence.support.JDOException ex) {        
            com.sun.jdo.spi.persistence.support.ejb.cmp.CMPBeanHelper.logJDOExceptionWithLifecycleLogger(        
                "GEN.generic_method_exception", _jdoBeanName, ex);        
            throw ex;        
        }        
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
        return com.sun.ejb.containers.TimerBean_2100919770_ConcreteImpl_JDOHelper.getHelperInstance().convertPrimaryKeyToObjectId(key);        
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
                com.sun.ejb.containers.TimerBean_2100919770_ConcreteImpl_JDOHelper.getHelperInstance().getContainer());        
            _jdoPersistenceManagerFactory.setRequireCopyObjectId(false);        
            _jdoPersistenceManagerFactory.setRequireTrackedSCO(false);        
            _jdoNumericConverter = com.sun.ejb.containers.TimerBean_2100919770_ConcreteImpl_JDOHelper.getHelperInstance().getNumericConverter();        
        }        
    }        
    private void jdoGetInstance ()        
    {        
        com.sun.ejb.containers.TimerPrimaryKey key = (com.sun.ejb.containers.TimerPrimaryKey)  _jdoEntityContext.getPrimaryKey();        
        Object jdoObjectId = jdoGetObjectId(key);         
        _jdoInstance = (com.sun.ejb.containers.TimerBean_2100919770_JDOState) _jdoPersistenceManager.getObjectById(jdoObjectId, true);        
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


