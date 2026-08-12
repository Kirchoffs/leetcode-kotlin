#!/bin/zsh

if [[ -z "$1" ]]; then
    echo "Usage: $0 <path/to/kotlin_file>"
    exit 1
fi

filepath=$1

if [[ "$filepath" != *.kt ]]; then
    filepath="${filepath}.kt"
fi

basepath="${filepath%.kt}"

kotlinc "$filepath" -include-runtime -d "${basepath}.jar"

if [[ $? -eq 0 ]]; then
    java -jar "${basepath}.jar"
    
    rm -f "${basepath}.jar"
else
    echo "Compilation failed."
    exit 1
fi
