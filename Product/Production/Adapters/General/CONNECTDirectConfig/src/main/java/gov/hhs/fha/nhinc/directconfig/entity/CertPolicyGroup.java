package gov.hhs.fha.nhinc.directconfig.entity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
@Table(name = "certpolicygroup")
public class CertPolicyGroup 
{
    private Long id;
    private String policyGroupName;
    private Collection<CertPolicyGroupReltn> policies;
    private Calendar createTime;      
    
    
    public CertPolicyGroup()
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
    public Long getId() 
    {
        return id;
    }
    
    /**
     * Set the value of id.
     * 
     * @param id
     *            The value of id.
     */
    public void setId(Long id) 
    {
        this.id = id;
    } 
    
    /**
     * Get the value of policyGroupName.
     * 
     * @return the value of policyGroupName.
     */
    @Column(name = "policyGroupName", unique = true)
    public String getPolicyGroupName() 
    {
        return policyGroupName;
    }    

    
    /**
     * Gets the value of policyGroupName.
     * @param policyGroupName Get the value of policyGroupName.
     */
    public void setPolicyGroupName(String policyGroupName)
    {
        this.policyGroupName = policyGroupName;
    }
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "certPolicyGroup")
    public Collection<CertPolicyGroupReltn> getCertPolicyGroupReltn() 
    {

        if (policies == null) 
        {
            policies = new ArrayList<CertPolicyGroupReltn>();
        }
        return policies;
    } 
    
    public void setCertPolicyGroupReltn(Collection<CertPolicyGroupReltn> policies)
    {
        this.policies = policies;
    }
    
    /**
     * Get the value of createTime.
     * 
     * @return the value of createTime.
     */
    @Column(name = "createTime", nullable = false)    
    @Temporal(TemporalType.TIMESTAMP)
    public Calendar getCreateTime() 
    {
        return createTime;
    }

    /**
     * Set the value of createTime.
     * 
     * @param timestamp
     *            The value of createTime.
     */
    public void setCreateTime(Calendar timestamp) 
    {
        createTime = timestamp;
    }    
}
