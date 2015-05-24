FROM sxend/cassandra:2.2.0-beta1

MAINTAINER sxend

RUN apt-get -y install gettext

ADD cassandra.yaml.template ${CASSANDRA_HOME}/conf/cassandra.yaml.template

ADD cassandra-start ${CASSANDRA_HOME}/bin/cassandra-start