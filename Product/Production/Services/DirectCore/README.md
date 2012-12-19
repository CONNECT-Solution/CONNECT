Setting up DIRECT in Connect

1. Install upgraded security policy files (This is now a 4.0 dependency -- see main README)
- download the jars from:
http://www.oracle.com/technetwork/java/javase/downloads/jce-7-download-432124.html
- install under
$JAVA_HOME/jre/lib/security 

2. Installing and configuring mail servers:

3. Configuring the DIRECT Smtp Agent in XML
- Key Stores:
 <AnchorStore type="Uniform" storeType="KeyStore" file="${directAnchorStore}" filePass="changeit" privKeyPass="changeit"/>    
 <PublicCertStore type="Keystore" file="${directPublicCertStore}" filePass="changeit" privKeyPass="changeit"/>
 <PrivateCertStore type="Keystore" file="${directPrivateCertStore}" filePass="changeit" privKeyPass="changeit"/>
- Domains:



