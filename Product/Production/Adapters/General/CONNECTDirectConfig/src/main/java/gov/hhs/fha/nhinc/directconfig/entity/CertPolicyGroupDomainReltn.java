package gov.hhs.fha.nhinc.directconfig.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "certpolicygroupdomainreltn")
public class CertPolicyGroupDomainReltn 
{
    private Long id;
    
    private Domain domain;
    
    private CertPolicyGroup policyGroup;
    
    public CertPolicyGroupDomainReltn()
    {
        
    }
    
    /**
     * Get the value of id.
     * 
     * @return the value of id.
     */
    @Column(name = "id", nullable = false)
    @Id
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
    * Gets the value of the policy group.
    * 
    * @return The value of the policy group.
    */
   @ManyToOne(optional = false, fetch = FetchType.EAGER)
   @JoinColumn(name = "policy_group_id")
   public CertPolicyGroup getCertPolicyGroup() 
   {
       return policyGroup;
   }
   
   /**
    * Sets the value of the policy group.
    * 
    * @param policyGroup The value of the policy group.
    */
   public void setCertPolicyGroup(CertPolicyGroup policyGroup)
   {
       this.policyGroup = policyGroup;
   }
   
   
   /**
    * Gets the value of the domain.
    * 
    * @return The value of the domain.
    */
   @ManyToOne(optional = false, fetch = FetchType.EAGER)
   @JoinColumn(name = "domain_id")
   public Domain getDomain() 
   {
       return domain;
   }
   
   /**
    * Sets the value of the domain.
    * 
    * @param bundle The value of the domain.
    */
   public void setDomain(Domain domain)
   {
       this.domain = domain;
   }  
   
}
