
package com.sun.ejb.containers;

public class TimerBean_2100919770_JDOState
    implements com.sun.jdo.spi.persistence.support.sqlstore.PersistenceCapable, Cloneable
{
    private long creationTimeRaw;        
    private byte[] blob;        
    private long lastExpirationRaw;        
    private java.lang.String timerId;        
    private long containerId;        
    private java.lang.String ownerId;        
    private int state;        
    private int pkHashCode;        
    private long intervalDuration;        
    private long initialExpirationRaw;        
    public transient com.sun.jdo.spi.persistence.support.sqlstore.StateManager jdoStateManager = null;        
    public transient byte jdoFlags = 0;        

    // ----------------------------------------------------------------------        
    // Class Members:        
    // ----------------------------------------------------------------------        
    public TimerBean_2100919770_JDOState ()        
    {        
        super();        
    }        
    // ----------------------------------------------------------------------        
    // Augmentation for Persistence-Capable Root Classes (added by enhancer):        
    // ----------------------------------------------------------------------        
    public TimerBean_2100919770_JDOState (com.sun.jdo.spi.persistence.support.sqlstore.StateManager jdoStateManager)        
    {        
        jdoFlags = (byte)1; // == LOAD_REQUIRED        
        this.jdoStateManager = jdoStateManager;        
    }        

    public Object clone ()        
        throws java.lang.CloneNotSupportedException        
    {        
        com.sun.ejb.containers.TimerBean_2100919770_JDOState clone = (com.sun.ejb.containers.TimerBean_2100919770_JDOState)super.clone();        
        clone.jdoFlags = (byte)0;        
        clone.jdoStateManager = null;        
        return clone;        
    }        
    public long getCreationTimeRaw ()        
    {        
        // annotation: check read access        
        if (jdoFlags > 0) {        
           jdoStateManager.loadForRead();        
        }        
        return creationTimeRaw;        
    }        
    public void setCreationTimeRaw (long creationTimeRaw)        
    {        
        // annotation: check write access        
        if (jdoFlags != 0) {        
            jdoStateManager.loadForUpdate();        
        }        
        this.creationTimeRaw = creationTimeRaw;        
    }        
    public byte[] getBlob ()        
    {        
        // annotation: mediate read access        
        final com.sun.jdo.spi.persistence.support.sqlstore.StateManager stateManager = this.jdoStateManager;        
        if (stateManager != null) {        
            stateManager.prepareGetField(1);        
        }        
        return blob;        
    }        
    public void setBlob (byte[] blob)        
    {        
        // annotation: mediate write access        
        final com.sun.jdo.spi.persistence.support.sqlstore.StateManager stateManager = this.jdoStateManager;        
        if (stateManager == null) {        
            this.blob = blob;        
        } else {        
            stateManager.setObjectField(1, blob);        
        }        
    }        
    public long getLastExpirationRaw ()        
    {        
        // annotation: check read access        
        if (jdoFlags > 0) {        
           jdoStateManager.loadForRead();        
        }        
        return lastExpirationRaw;        
    }        
    public void setLastExpirationRaw (long lastExpirationRaw)        
    {        
        // annotation: check write access        
        if (jdoFlags != 0) {        
            jdoStateManager.loadForUpdate();        
        }        
        this.lastExpirationRaw = lastExpirationRaw;        
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
            stateManager.setObjectField(3, timerId);        
        }        
    }        
    public long getContainerId ()        
    {        
        // annotation: check read access        
        if (jdoFlags > 0) {        
           jdoStateManager.loadForRead();        
        }        
        return containerId;        
    }        
    public void setContainerId (long containerId)        
    {        
        // annotation: check write access        
        if (jdoFlags != 0) {        
            jdoStateManager.loadForUpdate();        
        }        
        this.containerId = containerId;        
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
    public int getState ()        
    {        
        // annotation: check read access        
        if (jdoFlags > 0) {        
           jdoStateManager.loadForRead();        
        }        
        return state;        
    }        
    public void setState (int state)        
    {        
        // annotation: check write access        
        if (jdoFlags != 0) {        
            jdoStateManager.loadForUpdate();        
        }        
        this.state = state;        
    }        
    public int getPkHashCode ()        
    {        
        // annotation: check read access        
        if (jdoFlags > 0) {        
           jdoStateManager.loadForRead();        
        }        
        return pkHashCode;        
    }        
    public void setPkHashCode (int pkHashCode)        
    {        
        // annotation: check write access        
        if (jdoFlags != 0) {        
            jdoStateManager.loadForUpdate();        
        }        
        this.pkHashCode = pkHashCode;        
    }        
    public long getIntervalDuration ()        
    {        
        // annotation: check read access        
        if (jdoFlags > 0) {        
           jdoStateManager.loadForRead();        
        }        
        return intervalDuration;        
    }        
    public void setIntervalDuration (long intervalDuration)        
    {        
        // annotation: check write access        
        if (jdoFlags != 0) {        
            jdoStateManager.loadForUpdate();        
        }        
        this.intervalDuration = intervalDuration;        
    }        
    public long getInitialExpirationRaw ()        
    {        
        // annotation: check read access        
        if (jdoFlags > 0) {        
           jdoStateManager.loadForRead();        
        }        
        return initialExpirationRaw;        
    }        
    public void setInitialExpirationRaw (long initialExpirationRaw)        
    {        
        // annotation: check write access        
        if (jdoFlags != 0) {        
            jdoStateManager.loadForUpdate();        
        }        
        this.initialExpirationRaw = initialExpirationRaw;        
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
            return new Long(creationTimeRaw);        
        case 1:        
            return blob;        
        case 2:        
            return new Long(lastExpirationRaw);        
        case 3:        
            return timerId;        
        case 4:        
            return new Long(containerId);        
        case 5:        
            return ownerId;        
        case 6:        
            return new Integer(state);        
        case 7:        
            return new Integer(pkHashCode);        
        case 8:        
            return new Long(intervalDuration);        
        case 9:        
            return new Long(initialExpirationRaw);        
        default:        
            throw new com.sun.jdo.api.persistence.support.JDOFatalException();        
        }        
    }        
    public void jdoSetField (int fieldNumber,         
        Object obj)        
    {        
        switch (fieldNumber) {        
        case 0:        
            this.creationTimeRaw = ((Long)obj).longValue();        
            return;        
        case 1:        
            this.blob = (byte[])obj;        
            return;        
        case 2:        
            this.lastExpirationRaw = ((Long)obj).longValue();        
            return;        
        case 3:        
            this.timerId = (java.lang.String)obj;        
            return;        
        case 4:        
            this.containerId = ((Long)obj).longValue();        
            return;        
        case 5:        
            this.ownerId = (java.lang.String)obj;        
            return;        
        case 6:        
            this.state = ((Integer)obj).intValue();        
            return;        
        case 7:        
            this.pkHashCode = ((Integer)obj).intValue();        
            return;        
        case 8:        
            this.intervalDuration = ((Long)obj).longValue();        
            return;        
        case 9:        
            this.initialExpirationRaw = ((Long)obj).longValue();        
            return;        
        default:        
            throw new com.sun.jdo.api.persistence.support.JDOFatalException();        
        }        
    }        
    public void jdoClear ()        
    {        
        creationTimeRaw = 0;        
        blob = null;        
        lastExpirationRaw = 0;        
        containerId = 0;        
        ownerId = null;        
        state = 0;        
        pkHashCode = 0;        
        intervalDuration = 0;        
        initialExpirationRaw = 0;        
    }        
    public Object jdoNewInstance (com.sun.jdo.spi.persistence.support.sqlstore.StateManager sm)        
    {        
        return new TimerBean_2100919770_JDOState(sm);        
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


