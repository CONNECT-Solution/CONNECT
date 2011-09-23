JENKINS_URL=$1

NHINC_DIR=/nhin/NHINC
AS_HOME=/nhin/SUNWappserver
PASSWORD_FILE_TMP_DIR=/tmp

#pull down the full dist from wbld21
rm -Rf target
rm -f target.zip
wget $JENKINS_URL/lastSuccessfulBuild/artifact/target/\*zip\*/target.zip
unzip target.zip
#rm target.zip

#unzip the binaries into the ordained directory so brittle deployment scripts..
rm -Rf $NHINC_DIR/*
unzip target/CONNECT_Binaries*.zip -d /nhin/NHINC
mkdir $AS_HOME/domains/domain1/config/nhin
cp -r $NHINC_DIR/Dev/* $AS_HOME/domains/domain1/config/nhin

rm -Rf $NHINC_DIRC/ThirdParty/*
unzip target/CONNECT_ThirdParty*.zip -d $NHINC_DIR/ThirdParty

rm -Rf /nhin/ValidationSuite/*
unzip target/CONNECT_Validation*.zip -d /nhin/

echo "AS_ADMIN_PASSWORD=adminadmin" > $PASSWORD_FILE_TMP_DIR/password.txt

########

patch_dos_paths_in_internal_self_test() {
    echo -e "\n\n" > newlines
    #on solaris, sed seems to filter out a last line that ends with eof instead of eoln
    cat /nhin/ValidationSuite/1-InternalSelfTest-soapui-project.xml newlines > foo0.xml
    sed 's/C:\/Projects\/NHINC\/Current\/Product\/Production\/Common\/Interfaces\/src\//\/nhin\/SUNWappserver\/domains\/domain1\/config\/nhin\//g' < foo0.xml  > foo.xml
    sed 's/C:\/projects\/nhinc\/Current\/Product\/Production\/Common\/Interfaces\/src\//\/nhin\/SUNWappserver\/domains\/domain1\/config\/nhin\//g' < foo.xml > foo2.xml
    sed 's/file:\/\/nhin/file:\/\/\/nhin/g' < foo2.xml > /nhin/ValidationSuite/1-InternalSelfTest-patched.xml
    rm foo0.xml foo.xml foo2.xml newlines
}

patch_dos_paths_in_end_to_end_test() {
    echo -e "\n\n" > newlines
    cat /nhin/ValidationSuite/2-EndToEndSelfTest-soapui-project.xml newlines > foo0.xml
    sed 's/C:\/Projects\/NHINC\/Current\/Product\/Production\/Common\/Interfaces\/src\//\/nhin\/SUNWappserver\/domains\/domain1\/config\/nhin\//g' < foo0.xml > foo.xml
    sed 's/C:\/projects\/nhinc\/Current\/Product\/Production\/Common\/Interfaces\/src\//\/nhin\/SUNWappserver\/domains\/domain1\/config\/nhin\//g' < foo.xml > foo2.xml
    sed 's/file:\/\/nhin/file:\/\/\/nhin/g' < foo2.xml > /nhin/ValidationSuite/2-EndToEndSelfTest-patched.xml
    rm foo0.xml foo.xml foo2.xml newlines
}

patch_dos_paths_in_internal_self_test 
patch_dos_paths_in_end_to_end_test 
    