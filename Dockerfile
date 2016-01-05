FROM sequenceiq/spark:1.5.1
MAINTAINER kliew

# Setup sbt for building Scala apps
RUN curl https://bintray.com/sbt/rpm/rpm \
		| tee /etc/yum.repos.d/bintray-sbt-rpm.repo && \
	yum install -y sbt
RUN rm /usr/bin/java && \
	ln -s /usr/java/default/bin/java /usr/bin/java

# Add work files
COPY labfiles /opt/ibm/labfiles
RUN sh /etc/bootstrap.sh && \
	hdfs dfsadmin -safemode leave && \
	hdfs dfs -mkdir input/tmp && \
	hdfs dfs -mkdir input/tmp/labdata && \
	hdfs dfs -mkdir input/tmp/labdata/sparkdata && \
	hdfs dfs -put /opt/ibm/labfiles/nyctaxi/nyctaxi.csv input/tmp/labdata/sparkdata/ && \
	hdfs dfs -put /opt/ibm/labfiles/nyctaxisub/nyctaxisub.csv input/tmp/labdata/sparkdata/ && \
	hdfs dfs -put /opt/ibm/labfiles/nycweather/nycweather.csv input/tmp/labdata/sparkdata/ && \
	hdfs dfs -put /opt/ibm/labfiles/users.txt input/tmp && \
	hdfs dfs -put /opt/ibm/labfiles/followers.txt input/tmp 

# Compile projects
COPY SparkPi /SparkFundamentalsI/SparkPi
WORKDIR /SparkFundamentalsI/SparkPi
RUN sbt package

COPY SparkSQL /SparkFundamentalsI/SparkSQL
WORKDIR /SparkFundamentalsI/SparkSQL
RUN sbt package

COPY SparkMLlib /SparkFundamentalsI/SparkMLlib
WORKDIR /SparkFundamentalsI/SparkMLlib
RUN sbt package

COPY SparkStreaming /SparkFundamentalsI/SparkStreaming
WORKDIR /SparkFundamentalsI/SparkStreaming
RUN sbt package

COPY SparkGraphX /SparkFundamentalsI/SparkGraphX
WORKDIR /SparkFundamentalsI/SparkGraphX
RUN sbt package

WORKDIR /SparkFundamentalsI

ENTRYPOINT ["/etc/bootstrap.sh"]

