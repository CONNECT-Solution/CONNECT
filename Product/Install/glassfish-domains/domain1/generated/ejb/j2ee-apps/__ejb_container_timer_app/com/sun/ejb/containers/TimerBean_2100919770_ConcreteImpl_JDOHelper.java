
package com.sun.ejb.containers;

import javax.ejb.*;

// This is the generated helper class for TimerBean_2100919770_ConcreteImpl to be used with Transparent Persistence runtime.
public class TimerBean_2100919770_ConcreteImpl_JDOHelper
    extends com.sun.jdo.spi.persistence.support.ejb.cmp.JDOEJB20HelperImpl
    implements com.sun.jdo.spi.persistence.support.sqlstore.ejb.JDOEJB20Helper
{
    private static transient com.sun.ejb.containers.TimerBean_2100919770_ConcreteImpl_JDOHelper instance = new com.sun.ejb.containers.TimerBean_2100919770_ConcreteImpl_JDOHelper();        
    private static transient String _jdoBeanName = "TimerBean";        
    private static transient Object container = com.sun.jdo.spi.persistence.support.sqlstore.ejb.CMPHelper.getContainer(new Object[]{com.sun.ejb.containers.TimerBean_2100919770_ConcreteImpl_JDOHelper.class, "com.sun.ejb.containers.TimerBean_2100919770_ConcreteImpl"});        

    public void assertInstanceOfRemoteInterfaceImpl (Object param0)        
    {        
        assertInstanceOfRemoteInterfaceImpl(param0, _jdoBeanName);        
    }        
    public static TimerBean_2100919770_ConcreteImpl_JDOHelper getHelperInstance ()        
    {        
        return instance;        
    }        
    public Object getContainer ()        
    {        
        return container;         
    }        
    public java.lang.Class getPCClass ()        
    {        
        return com.sun.ejb.containers.TimerBean_2100919770_ConcreteImpl.jdoGetJdoInstanceClass();         
    }        
    public void assertInstanceOfLocalInterfaceImpl (Object param0)        
    {        
        assertInstanceOfLocalInterfaceImpl(param0, _jdoBeanName);        
    }        
    public EJBObject convertPCToEJBObject (Object pc,         
        com.sun.jdo.api.persistence.support.PersistenceManager _jdoPersistenceManager)        
    {        
        return null;        
    }        
    public Object convertEJBObjectToPC (EJBObject param0,         
        com.sun.jdo.api.persistence.support.PersistenceManager _jdoPersistenceManager)        
    {        
        return null;        
    }        
    public Object convertPrimaryKeyToObjectId (Object param0)        
    {        
        assertPrimaryKeyNotNull(param0);        
        com.sun.ejb.containers.TimerBean_2100919770_JDOState.Oid jdoObjectId = new com.sun.ejb.containers.TimerBean_2100919770_JDOState.Oid();        
        com.sun.ejb.containers.TimerPrimaryKey key = (com.sun.ejb.containers.TimerPrimaryKey)param0;        
        jdoObjectId.timerId = key.timerId;        
        return jdoObjectId;        
    }        
    public Object convertObjectIdToPrimaryKey (Object param0)        
    {        
        assertObjectIdNotNull(param0);        
        com.sun.ejb.containers.TimerBean_2100919770_JDOState.Oid jdoObjectId = (com.sun.ejb.containers.TimerBean_2100919770_JDOState.Oid)param0;        
        com.sun.ejb.containers.TimerPrimaryKey key = new com.sun.ejb.containers.TimerPrimaryKey();        
        key.timerId = jdoObjectId.timerId;        
        return key;        
    }        

}


