TARGET_DIST_URL=$1

NHINC_DIR=/nhin/NHINC
AS_HOME=/nhin/SUNWappserver
PASSWORD_FILE_TMP_DIR=/tmp

#pull down the full dist from wbld21
rm -Rf target
rm -f target.zip
wget --no-check-certificate $TARGET_DIST_URL
unzip target.zip
#rm target.zip

#unzip the binaries into the ordained directory so brittle deployment scripts..
rm -Rf $NHINC_DIR/*
unzip target/CONNECT_Binaries*.zip -d /nhin/NHINC
mkdir $AS_HOME/domains/domain1/config/nhin
cp -r $NHINC_DIR/Dev/* $AS_HOME/domains/domain1/config/nhin

rm -Rf $NHINC_DIR/ThirdParty/*
unzip target/CONNECT_ThirdParty*.zip -d $NHINC_DIR/ThirdParty

rm -Rf /nhin/ValidationSuite/*
unzip target/CONNECT_Validation*.zip -d /nhin/

echo "AS_ADMIN_PASSWORD=adminadmin" > $PASSWORD_FILE_TMP_DIR/password.txt

cp $NHINC_DIR/Install/SetValidationSuiteWsdlPath.jar /nhin/NHINC/ThirdParty/ThirdParty/ValidationSuite
java -jar /nhin/NHINC/ThirdParty/ThirdParty/ValidationSuite/SetValidationSuiteWsdlPath.jar
