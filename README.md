# SparkFundamentalsI
Big Data University: Spark Fundamentals I (BD095EN) Version #1: Updated: September 2015

### Course Description ###
http://bigdatauniversity.com/courses/spark-fundamentals/
Apache Spark is an open source processing engine built around speed, ease of use, and analytics. If you have large amounts of data that requires low latency processing that a typical Map Reduce program cannot provide, Spark is the alternative. Spark performs at speeds up to 100 times faster than Map Reduce for iterative algorithms or interactive data mining. Spark provides in-memory cluster computing for lightning fast speed and supports Java, Scala, and Python APIs for ease of development.

Spark combines SQL, streaming and complex analytics together seamlessly in the same application to handle a wide range of data processing scenarios. Spark runs on top of Hadoop, Mesos, standalone, or in the cloud. It can access diverse data sources such as HDFS, Cassandra, HBase, or S3.
- - - -
### Environment ###
**Hadoop** 2.6.0

**Spark** 1.5.1

I have containerized the Spark-Hadoop stack with the precompiled Scala projects. To demo the finished product, install Docker and follow the instructions below.


Start the container
```
docker kill sparkfun
docker rm sparkfun
docker run -d --name sparkfun kliew/sparkfundamentalsi -d
docker exec -it sparkfun bash
```

#### Exercise 3: Scala program to estimate the value of Pi ####
*A basic Spark application that parallelizes the computation of Pi*
```
$SPARK_HOME/bin/spark-submit \
    --class "SparkPi" \
    --master local[4] \
    SparkPi/target/scala-2.10/spark-pi-project_2.10-1.0.jar
```

#### Exercise 4.1: Spark SQL program to find the ten hottest days with precipitation in NYC, 2013
*Spark SQL provides the ability to write relational queries to be run on Spark. There is the abstraction SchemaRDD which is to create an RDD in which you can run SQL, HiveQL, and Scala. In this lab section, you will use SQL to find out the average weather and precipitation for a given time period in New York. The purpose is to demonstrate how to use the Spark SQL libraries on Spark.*
```
$SPARK_HOME/bin/spark-submit \
  --class "SparkSQL" \
  --master local[4] \
  SparkSQL/target/scala-2.10/spark-sql-project_2.10-1.0.jar
```
#### Exercise 4.2: Spark MLlib program to find the best location to hail a cab in NYC, 2013 ####
*In this section, the Spark shell will be used to acquire the K-Means clustering for drop-off latitudes and longitudes of taxis for 3 clusters. The sample data contains a subset of taxi trips with hack license, medallion, pickup date/time, drop off date/time, pickup/drop off latitude/longitude, passenger count, trip distance, trip time and other information. As such, this may give a good indication of where to best to hail a cab.  
The data file can be found on the HDFS under /tmp/labdata/sparkdata/nyctaxisub.csv. Remember, this is only a subset of the file that you used a previous exercise. If you ran this exercise on the full dataset, it would take a long time as we are only running on a test environment with limited resources.*
```
$SPARK_HOME/bin/spark-submit \
  --class "SparkMLlib" \
  --master local[4] \
  SparkMLlib/target/scala-2.10/spark-mllib-project_2.10-1.0.jar
```
#### Exercise 4.3: Spark Streaming program to process streams of data ####
*This section focuses on Spark Streams, an easy to build, scalable, stateful (e.g. sliding windows) stream processing library. Streaming jobs are written the same way Spark batch jobs are coded and support Java, Scala and Python. In this exercise, taxi trip data will be streamed using a socket connection and then analyzed to provide a summary of number of passengers by taxi vendor. This will be implemented in the Spark shell using Scala. 
There are two files under /home/virtuser/labdata/streams. The first one is the nyctaxi100.csv which will serve as the source of the stream. The other file is a python file, taxistreams.py, which will feed the csv file through a socket connection to simulate a stream.  
Once started, the program will bind and listen to the localhost socket 7777. When a connection is made, it will read ‘nyctaxi100.csv’ and send across the socket. The sleep is set such that one line will be sent every 0.5 seconds, or 2 rows a second. This was intentionally set to a high value to make it easier to view the data during execution.*

Start the stream in a second terminal
```
docker exec -it sparkfun python \
  /opt/ibm/labfiles/streams/taxistreams.py
```

Run the application in your main terminal
```
$SPARK_HOME/bin/spark-submit \
  --class "SparkStreaming" \
  --master local[4] \
  SparkStreaming/target/scala-2.10/spark-streaming-project_2.10-1.0.jar
```
Exit the stream terminal when you are satisfied.

#### Exercise 4.4: Spark GraphX program to compute the page rank of users in a sample social network graph ####
*The following exercise uses computes the page rank of users in a social network. It computes on a small set of data but can be applied to social networks such as Twitter and other graph relationships.*
```
$SPARK_HOME/bin/spark-submit \
  --class "SparkGraphX" \
  --master local[4] \
  SparkGraphX/target/scala-2.10/spark-graphx-project_2.10-1.0.jar
```
