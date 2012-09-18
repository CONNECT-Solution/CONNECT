mvn install:install-file -Dfile=jms1.1/lib/javax.jms.jar -DgroupId=javax.jms \
    -DartifactId=jms -Dversion=1.1 -Dpackaging=jar

mvn install:install-file -Dfile=JTA/javax.transaction.jar -DgroupId=javax.transaction \
    -DartifactId=jta -Dversion=1.0.1B -Dpackaging=jar


mvn install:install-file -Dfile=jmx-1_2_1-bin/lib/jmxri.jar -DgroupId=com.sun.jmx \
    -DartifactId=jmxri -Dversion=1.2.1 -Dpackaging=jar
mvn install:install-file -Dfile=jmx-1_2_1-bin/lib/jmxtools.jar -DgroupId=com.sun.jdmk \
    -DartifactId=jmxtools -Dversion=1.2.1 -Dpackaging=jar
