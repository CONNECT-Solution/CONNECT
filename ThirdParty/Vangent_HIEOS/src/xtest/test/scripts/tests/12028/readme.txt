Ret.b Accept retrieve document set - single document with TLS

submit - Issue a Provide and Registry.b transaction to load your
repository with a document.  Your repository must forward the metadata
to the Public Registry.

query - Query the Public Registry to get the metadata as submitted
by the Repository.

retrieve - Issue a Retrieve Document Set transaction to your repository
to get the document.  The -S option must be used on xdstest to force
TLS to be used.
