The Vangent-HIEOS project source code that was used for CONNECT release 2.2 is 
being provided in the CONNECT source code repository should anyone in the 
CONNECT developer community need to review or re-build the HIEOS version that 
was used. At the time of release 2.2, the HIEOS binaries were not available and 
there was a slight modification made to two source code files to help with
the CONNECT-HIEOS integration. The two source code files were:

<HIEOS_Source_Install_dir>\src\xds\src\com\vangent\hieos\services\xds\registry\serviceimpl\XDSbRegistry.java 
<HIEOS_Source_Install_dir>\src\xds\src\com\vangent\hieos\services\xds\repository\serviceimpl\XDSbRepository.java 

The original files are also included as:

<HIEOS_Source_Install_dir>\src\xds\src\com\vangent\hieos\services\xds\registry\serviceimpl\XDSbRegistry.java_original_version 
<HIEOS_Source_Install_dir>\src\xds\src\com\vangent\hieos\services\xds\repository\serviceimpl\XDSbRepository.java_original_version 

Modifications were made to the <HIEOS_Source_Install_dir>\src\xref\web\codes\codes.xml and 
<HIEOS_Source_Install_dir>\src\xref\web\config\xconfig.xml files as referenced in the CONNECT-Vangent 
installation manual found on the release 2.2 web site.  These changes are more installation specific
and do not affect the core functionality of HIEOS. There are a few other modifications that were made
for installation specific testing in harmony with the Vangent HIEOS testing instructions (also referenced
in the CONNECT-Vangent installation instructions). 

All modified files have a corresponding "_original_version" suffix attached to the filename to more clearly 
identify it for referemce amd comparison.

The original SetupGuide.htm page from HIEOS is also included for reference.
