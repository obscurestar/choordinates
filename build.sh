#!/bin/bash

#Me am savage.  Smash bits.
#TODO replace this with make or cmake

JAR_NAME="dischord.jar"
EXE=""
OUTDIR="./bin"
SRCDIR="./src"

pushd .
cd $SRCDIR
SRC=`find . -name "*.java" -print 2> /dev/null`

[[ -d $OUTDIR ]] || mkdir $OUTDIR

echo COMPILING
#Who needs compilation error handling anyhow?
for file in $SRC
do
    javac -d $OUTDIR $file
    EXE="$EXE$(basename $file .java).class "
done
popd

echo PACKAGING
#The following line doesn't work.  Fix this with CMake or something later.
#jar cfe $JAR_NAME DisChord $EXE

cd $OUTDIR

jar cfe dischord.jar DisChord ./*.class

echo LAUNCHING JAR
java -jar $JAR_NAME
