In order to compile and run the "HardCode" examples, the WSDL in this folder need to be copied to the C:\projects\NHINC\Current\Product\Production\Common\Interfaces\src\wsdl directory.

Steps to run the test:

1.  Copy WSDLs into C:\projects\NHINC\Current\Product\Production\Common\Interfaces\src\wsdl directory.
2.  Compile and deploy HardCodeTestExample
3.  Compile and deploy HardCodeTestExample2
3.  Run SoapUI Test: HardCodeSoapUITests\HardCodeTest2-soapui-project.xml
4.  Rename the C:\projects\NHINC\Current\Product\Production\Common\Interfaces directory to something else.
5.  Restart glassfish.
6.  Re-run SoapUI test: HardCodeSoapUITests\HardCodeTest2-soapui-project.xml
These steps go with the following projects under Examples:


HardCodeSoapUITests
HardCodeTestClient
HardCodeTestExample
HardCodeTestExample2
