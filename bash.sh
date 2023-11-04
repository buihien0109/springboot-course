#!/bin/bash

# Specify the directory where you want to remove .DS_Store files
directory="/Users/buihien/Documents/own-github/springboot-course"

# Use the find command to locate and delete .DS_Store files
find "$directory" -type f -name .DS_Store -exec rm -f {} \;

echo "All .DS_Store files in $directory have been removed."
