// Connect OpenSSO PDP COT
ssoadm create-cot -t opensso-pdp-cot -u amadmin -f password.txt
ssoadm create-metadata-templ -y ConnectOpenSSOPdpEntity -p /openssoPdp -m openssoPdp.xml -x openssoPdp-x.xml -u amadmin -f password.txt
ssoadm import-entity -t opensso-pdp-cot -m openssoPdp.xml -x openssoPdp-x.xml -u amadmin -f password.txt

// Connect OpenSSO PEP COT
ssoadm create-cot -t opensso-pep-cot -u amadmin -f password.txt
ssoadm create-metadata-templ -y ConnectOpenSSOPepEntity -e /openssoPep -m openssoPep.xml -x openssoPep-x.xml -u amadmin -f password.txt
ssoadm import-entity -t opensso-pep-cot -m openssoPep.xml -x openssoPep-x.xml -u amadmin -f password.txt

// Connect Jerico PDP COT
ssoadm create-cot -t jericho-pdp-cot -u amadmin -f password.txt
ssoadm import-entity -t jericho-pdp-cot -m jerichoPdp.xml -u amadmin -f password.txt
