#!/bin/bash

#Me am savage.  Smash bits.
#TODO replace this with make or cmake

SRC=`find . -name "*.java" -print 2> /dev/null`
JAR_NAME="dischord.jar"
EXE=""

echo COMPILING
#Who needs compilation error handling anyhow?
for file in $SRC
do
    javac $file
    EXE="$EXE$(basename $file .java).class "
done

echo PACKAGING
#The following line doesn't work.  Fix this with CMake or something later.
#jar cfe $JAR_NAME DisChord $EXE
jar cfe dischord.jar DisChord ./*.class

echo LAUNCHING JAR
java -jar $JAR_NAME
