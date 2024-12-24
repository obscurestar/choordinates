#This doesn't work yet.
cmake_minimum_required (VERSION 3.22)

find_package(Java REQUIRED)
include(UseJava)

enable_testing()

project (DisChord NONE)

set(CMAKE_JAVA_COMPILE_FLAGS "-source" "23.0" "-target" "23.0")

add_jar(DisChord DisChord.java)

get_target_property(_jarFile DisChord JAR_FILE)
get_target_property(_classDir DisChord CLASSDIR)

message(STATUS "Jar file ${_jarFile}")
message(STATUS "Class compiled to ${_classDir}")
