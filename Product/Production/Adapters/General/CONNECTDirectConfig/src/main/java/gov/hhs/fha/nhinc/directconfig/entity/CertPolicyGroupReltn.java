package gov.hhs.fha.nhinc.directconfig.entity;

import gov.hhs.fha.nhinc.directconfig.entity.helpers.CertPolicyUse;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "certpolicygroupreltn")
public class CertPolicyGroupReltn 
{
    private long id;
    private CertPolicyGroup policyGroup;
    private CertPolicy policy;
    private CertPolicyUse policyUse;
    private boolean incoming;
    private boolean outgoing;

    public CertPolicyGroupReltn()
    {
        
    }
    
    /**
     * Get the value of id.
     * 
     * @return the value of id.
     */
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long getId() 
    {
        return id;
    }
    
    /**
     * Set the value of id.
     * 
     * @param id
     *            The value of id.
     */
    public void setId(long id) 
    {
        this.id = id;
    } 
    
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "certPolicyGroupId")
    @XmlTransient 
    public  CertPolicyGroup getCertPolicyGroup()
    {
        return policyGroup;
    }
    
    public void setCertPolicyGroup( CertPolicyGroup policyGroup)
    {
        this.policyGroup = policyGroup;
    }
    
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "certPolicyId")
    public CertPolicy getCertPolicy()
    {
        return policy;
    }  
    
    public void setCertPolicy(CertPolicy policy)
    {
        this.policy = policy;
    } 
    
    
    @Column(name = "policyUse", nullable=false)
    @Enumerated
    @XmlAttribute
    public CertPolicyUse getPolicyUse()
    {
        return policyUse;
    }

    public void setPolicyUse(CertPolicyUse policyUse)
    {
        this.policyUse = policyUse;
    }
    

    @Column(name = "incoming")
    public boolean isIncoming() 
    {
        return incoming;
    }


    public void setIncoming(boolean incoming) 
    {
        this.incoming = incoming;
    }


    @Column(name = "outgoing")
    public boolean isOutgoing() 
    {
        return outgoing;
    }


    public void setOutgoing(boolean outgoing) 
    {
        this.outgoing = outgoing;
    }
}
