PnR.a Return Errors from Registry

Send a Provide and Register.a transaction to Repository which then 
forwards the metadata to the Public Registry.  The metadata contains
a known flaw for which the Public Registry will return a known
error code.  The test validates that the error code is returned
through the Repository back to the Document Source.