#!/bin/ksh

for x in `ls -1 *.cfg.xml`
do
	echo $x
	sed -f toOracle.sed $x > $x.new
	mv $x.new $x
done
