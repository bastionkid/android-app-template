#!/bin/bash

commit_id=$(git rev-parse HEAD)
echo "${commit_id:0:7}"