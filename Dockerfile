FROM python:3.12.1

USER root

RUN mkdir -p /root/jobs_builder

WORKDIR /root/jobs_builder

COPY . /root/jobs_builder/

RUN pip3 install jenkins-job-builder==6.4.2

RUN apt update -y \
&& apt install vim -y

ENTRYPOINT [ "/bin/sh", "./entrypoint.sh" ]