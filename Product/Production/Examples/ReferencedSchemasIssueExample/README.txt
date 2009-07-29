Steps to reproduce issue:
1.  Build NhinBPEL and NhinCA projects
2.  Try to deploy NhinCA project
	-  This will fail because it cannot find a referenced schema.

Look in NhinBPEL\build\META-INF\src\_references\_projects\Interfaces\src
   Notice none of the schemas got copied over.  These schemas are referenced in the NhinSubjectDisocovery.wsdl as includes

   All referenced schemas in the HL7V3 schemas are also includes and those do not get copied as well.