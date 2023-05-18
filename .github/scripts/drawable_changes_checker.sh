#!/bin/bash

# Run the git diff command and store the result in a variable
files=$(git diff --name-only)

# Iterate over the lines in the result and store the relevant files in an array
IFS=$'\n' read -d '' -ra file_list <<< "$files"

# Initialize the drawables_optimized variable
drawables_optimized=0

# Iterate over the array and check if each file is in a "drawable" directory
for file in "${file_list[@]}"; do
    # Check if the file is part of a "drawable" directory
    if [[ $file == *"drawable/"* ]]; then
        ((drawables_optimized++))
    fi
done

# Print the final value of the drawables_optimized variable
echo "$drawables_optimized"