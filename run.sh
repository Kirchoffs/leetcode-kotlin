#!/bin/zsh

number=$1

kotlinc LC-${number}.kt -include-runtime -d LC-${number}.jar

java -jar LC-${number}.jar

rm LC-${number}.jar
