#!/bin/bash

# navigate to the root directory
cd ../..

echo "root directory: $(pwd)"

# navigate to the app modules' res directory
cd app/src/main/res/

# navigate to the drawable directories and print the current directory path
for dir in $(find . -type d -name "drawable*"); do
  cd "$dir"
  echo "drawable directory: $(pwd)"

  for vector in $(find . -type f -name "*.xml"); do
    echo "optimizing file: $vector"
    avocado "$vector"
  done

  cd ./..
done

# navigate to the root directory
cd ../../../..

echo "root directory: $(pwd)"

# navigate to the core-ui modules' res directory
cd core/ui/src/main/res/

# navigate to the drawable directories and print the current directory path
for dir in $(find . -type d -name "drawable*"); do
  cd "$dir"
  echo "drawable directory: $(pwd)"

  for vector in $(find . -type f -name "*.xml"); do
    echo "optimizing file: $vector"
    avocado "$vector"
  done

  cd ./..
done

# navigate to the root directory
cd ../../../../..

echo "root directory: $(pwd)"

# navigate to the feature directory having all the feature modules
cd feature

# iterate through all the modules present inside feature directory
for dir in */; do
  # navigate to the current core modules' res directory
  cd "$dir"presentation/src/main/res/

  echo "current directory: $(pwd)"

  # navigate to the drawable directories and print the current directory path
  for dir in $(find . -type d -name "drawable*"); do
  	cd "$dir"
  	echo "drawable directory: $(pwd)"

  	for vector in $(find . -type f -name "*.xml"); do
  	  echo "optimizing file: $vector"
  	  avocado "$vector"
  	done

  	cd ./..
  done

  cd ../../../../..
done
