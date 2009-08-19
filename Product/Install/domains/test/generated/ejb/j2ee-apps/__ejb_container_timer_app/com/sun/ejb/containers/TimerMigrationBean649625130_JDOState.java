
package com.sun.ejb.containers;

public class TimerMigrationBean649625130_JDOState
    implements com.sun.jdo.spi.persistence.support.sqlstore.PersistenceCapable, Cloneable
{
    private java.lang.String timerId;        
    private java.lang.String ownerId;        
    public transient com.sun.jdo.spi.persistence.support.sqlstore.StateManager jdoStateManager = null;        
    public transient byte jdoFlags = 0;        

    // ----------------------------------------------------------------------        
    // Class Members:        
    // ----------------------------------------------------------------------        
    public TimerMigrationBean649625130_JDOState ()        
    {        
        super();        
    }        
    // ----------------------------------------------------------------------        
    // Augmentation for Persistence-Capable Root Classes (added by enhancer):        
    // ----------------------------------------------------------------------        
    public TimerMigrationBean649625130_JDOState (com.sun.jdo.spi.persistence.support.sqlstore.StateManager jdoStateManager)        
    {        
        jdoFlags = (byte)1; // == LOAD_REQUIRED        
        this.jdoStateManager = jdoStateManager;        
    }        

    public Object clone ()        
        throws java.lang.CloneNotSupportedException        
    {        
        com.sun.ejb.containers.TimerMigrationBean649625130_JDOState clone = (com.sun.ejb.containers.TimerMigrationBean649625130_JDOState)super.clone();        
        clone.jdoFlags = (byte)0;        
        clone.jdoStateManager = null;        
        return clone;        
    }        
    public java.lang.String getTimerId ()        
    {        
        // annotation: grant direct read access        
        return timerId;        
    }        
    public void setTimerId (java.lang.String timerId)        
    {        
        // annotation: mediate write access        
        final com.sun.jdo.spi.persistence.support.sqlstore.StateManager stateManager = this.jdoStateManager;        
        if (stateManager == null) {        
            this.timerId = timerId;        
        } else {        
            stateManager.setObjectField(0, timerId);        
        }        
    }        
    public java.lang.String getOwnerId ()        
    {        
        // annotation: check read access        
        if (jdoFlags > 0) {        
           jdoStateManager.loadForRead();        
        }        
        return ownerId;        
    }        
    public void setOwnerId (java.lang.String ownerId)        
    {        
        // annotation: check write access        
        if (jdoFlags != 0) {        
            jdoStateManager.loadForUpdate();        
        }        
        this.ownerId = ownerId;        
    }        
    public final com.sun.jdo.spi.persistence.support.sqlstore.StateManager jdoGetStateManager ()        
    {        
        return jdoStateManager;        
    }        
    public final void jdoSetStateManager (com.sun.jdo.spi.persistence.support.sqlstore.StateManager jdoStateManager)        
    {        
        this.jdoStateManager = jdoStateManager;        
    }        
    public final byte jdoGetFlags ()        
    {        
        return jdoFlags;        
    }        
    public final void jdoSetFlags (byte jdoFlags)        
    {        
        this.jdoFlags = jdoFlags;        
    }        
    public final com.sun.jdo.api.persistence.support.PersistenceManager jdoGetPersistenceManager ()        
    {        
        final com.sun.jdo.spi.persistence.support.sqlstore.StateManager stateManager = this.jdoStateManager;        
        if (stateManager != null) {        
            return stateManager.getPersistenceManager();        
        }        
        return null;        
    }        
    public final java.lang.Object jdoGetObjectId ()        
    {        
        final com.sun.jdo.spi.persistence.support.sqlstore.StateManager stateManager = this.jdoStateManager;        
        if (stateManager != null) {        
            return stateManager.getObjectId();        
        }        
        return null;        
    }        
    public final boolean jdoIsPersistent ()        
    {        
        final com.sun.jdo.spi.persistence.support.sqlstore.StateManager stateManager = this.jdoStateManager;        
        if (stateManager != null) {        
            return stateManager.isPersistent();        
        }        
        return false;        
    }        
    public final boolean jdoIsTransactional ()        
    {        
        final com.sun.jdo.spi.persistence.support.sqlstore.StateManager stateManager = this.jdoStateManager;        
        if (stateManager != null) {        
            return stateManager.isTransactional();        
        }        
        return false;        
    }        
    public final boolean jdoIsNew ()        
    {        
        final com.sun.jdo.spi.persistence.support.sqlstore.StateManager stateManager = this.jdoStateManager;        
        if (stateManager != null) {        
            return stateManager.isNew();        
        }        
        return false;        
    }        
    public final boolean jdoIsDeleted ()        
    {        
        final com.sun.jdo.spi.persistence.support.sqlstore.StateManager stateManager = this.jdoStateManager;        
        if (stateManager != null) {        
            return stateManager.isDeleted();        
        }        
        return false;        
    }        
    public final boolean jdoIsDirty ()        
    {        
        final com.sun.jdo.spi.persistence.support.sqlstore.StateManager stateManager = this.jdoStateManager;        
        if (stateManager != null) {        
            return stateManager.isDirty();        
        }        
        return false;        
    }        
    public final void jdoMakeDirty (java.lang.String fieldName)        
    {        
        final com.sun.jdo.spi.persistence.support.sqlstore.StateManager stateManager = this.jdoStateManager;        
        if (stateManager != null) {        
            stateManager.makeDirty(fieldName);        
        }        
    }        
    // ----------------------------------------------------------------------        
    // Augmentation for Persistence-Capable Classes (added by enhancer):        
    // ----------------------------------------------------------------------        
    public Object jdoGetField (int fieldNumber)        
    {        
        switch (fieldNumber) {        
        case 0:        
            return timerId;        
        case 1:        
            return ownerId;        
        default:        
            throw new com.sun.jdo.api.persistence.support.JDOFatalException();        
        }        
    }        
    public void jdoSetField (int fieldNumber,         
        Object obj)        
    {        
        switch (fieldNumber) {        
        case 0:        
            this.timerId = (java.lang.String)obj;        
            return;        
        case 1:        
            this.ownerId = (java.lang.String)obj;        
            return;        
        default:        
            throw new com.sun.jdo.api.persistence.support.JDOFatalException();        
        }        
    }        
    public void jdoClear ()        
    {        
        ownerId = null;        
    }        
    public Object jdoNewInstance (com.sun.jdo.spi.persistence.support.sqlstore.StateManager sm)        
    {        
        return new TimerMigrationBean649625130_JDOState(sm);        
    }        

    public static class Oid        
        implements java.io.Serializable        
    {        
        public java.lang.String timerId;                    
        
        public Oid ()                    
        {                    
            super();                    
        }                    
        
        public int hashCode ()                    
        {                    
            int hash = 0;                    
            hash += (this.timerId != null ? this.timerId.hashCode() : 0);                    
            return hash;                    
        }                    
        public boolean equals (java.lang.Object pk)                    
        {                    
            if (pk == null || !this.getClass().equals(pk.getClass())) {                    
                return false;                    
            }                    
            Oid oid = (Oid)pk;                    
            if (this.timerId != oid.timerId && (this.timerId == null || !this.timerId.equals(oid.timerId))) return false;                    
            return true;                    
        }                    
        
    }        
        

}


