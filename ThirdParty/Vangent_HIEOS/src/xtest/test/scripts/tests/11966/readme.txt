PnR.b Accept document

submit - Generate a Provide and Register.b transaction to the Repository
under test which is already configured to forward Register.b
transactions to the Public Registry.

eval - The Public Registry is queried to validate the metadata forwarded.
When using xdstest to run this step, make sure it sends to the Public
Registry.  This can be done using the -s pub command line option to
xdstest.