Steps to Run this example:

1.  Copy policy.xml file to C:\GlassFishESB\glassfish\domains\domain1\config
2.  Copy TokenInfoManagerEJB.jar to C:\GlassFishESB\glassfish\lib (be careful to remove this later
    it will conflict with the runtime system)
3.  Deploy NhinCA, NhincProxyCA, and TokenInfoManagerCA
4.  Run 201301 Case from DynamicPolicy SOAP UI test

Verify SAML is present

To remove SAML:
1.  Edit policy.xml file in C:\GlassFishESB\glassfish\domains\domain1\config by commenting out the contents of the policy
2.  Restart Glassfish
3.  Run the SOAP UI test again and verify that SAML is no longer present