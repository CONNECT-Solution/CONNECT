/* Create a new database - separate from the documents database for Dynamic Documents */
DROP DATABASE IF EXISTS dyndocrepos;

-- begin dyndocrepos
CREATE DATABASE IF NOT EXISTS dyndocrepos;

/* Grant necessary rights to the nhincuser */
GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, ALTER, DROP ON dyndocrepos.* to nhincuser;
-- end docrepository
