-- workaround for non-supported DROP USER IF EXISTS. see MySQL Bug #15287
GRANT USAGE ON *.* TO nhincuser identified by 'nhincpass';
DROP USER 'nhincuser';
DELETE FROM mysql.user WHERE User = 'nhincuser';

DROP DATABASE IF EXISTS assigningauthoritydb;

DROP DATABASE IF EXISTS auditrepo;

DROP DATABASE IF EXISTS auditlog;

DROP DATABASE IF EXISTS docrepository;

DROP DATABASE IF EXISTS patientcorrelationdb;

DROP DATABASE IF EXISTS asyncmsgs;

DROP DATABASE IF EXISTS logging;

DROP DATABASE IF EXISTS patientdb;

DROP DATABASE IF EXISTS transrepo;

DROP DATABASE IF EXISTS eventdb;

DROP DATABASE IF EXISTS adminguidb;
DROP DATABASE IF EXISTS configdb;

FLUSH PRIVILEGES;


