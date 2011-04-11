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

DROP DATABASE IF EXISTS lift;

DROP DATABASE IF EXISTS asyncmsgs;

DROP DATABASE IF EXISTS logging;

DROP DATABASE IF EXISTS patientdb;

DROP DATABASE IF EXISTS perfrepo;