import org.apache.log4j.Logger
import org.apache.log4j.Level
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.streaming._
import org.apache.spark.streaming.StreamingContext._

object SparkStreaming
{
  def main(args: Array[String])
  {
    Logger.getLogger("org").setLevel(Level.ERROR)
    Logger.getLogger("akka").setLevel(Level.ERROR)
    
    // Initialize the contexts
    val conf = new SparkConf().setAppName("Spark Streaming")
    val sc = new SparkContext(conf)
    // Each 1 second batch becomes an RDD
    val ssc = new StreamingContext(sc, Seconds(1))
    
    // Listen to the Python script output
    val lines = ssc.socketTextStream("localhost",7777)
  
    // Create a RDD (vendor, # passengers)
    val pass = 
        lines
          .map(_.split(","))
          .map(pass=>(pass(15), pass(7).toInt))
          .reduceByKey(_ + _)
    
    // Print the RDD generated from streamed batches
    pass.print()
    
    // Start the application
    ssc.start() 
    ssc.awaitTermination() 
    
    sc.stop()
  }
}


