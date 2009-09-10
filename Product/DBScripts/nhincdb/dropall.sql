-- workaround for non-supported DROP USER IF EXISTS. see MySQL Bug #15287
GRANT USAGE ON *.* TO nhincuser identified by 'nhincpass';
DROP USER nhincuser;

DROP DATABASE IF EXISTS subscriptionrepository;

DROP DATABASE IF EXISTS aggregator;

DROP DATABASE IF EXISTS assigningauthoritydb;

DROP DATABASE IF EXISTS auditrepo;

DROP DATABASE IF EXISTS auditlog;

DROP DATABASE IF EXISTS docrepository;

DROP DATABASE IF EXISTS patientcorrelationdb;