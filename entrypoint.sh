#!/bin/bash

JENKINS_URL=$1
JENKINS_PASSWORD=$2
JENKINS_USERNAME=$3

cat <<EOF > ./job.ini
[job_builder]
ignore_cache=True
keep_description=False
recursive=True

[jenkins]
user=$JENKINS_USERNAME
password=$JENKINS_PASSWORD
url=$JENKINS_URL
EOF

jenkins-jobs --conf ./job.ini update .