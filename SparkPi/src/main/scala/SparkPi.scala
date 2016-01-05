import scala.math.random

import org.apache.log4j.Logger
import org.apache.log4j.Level
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

object SparkPi {
  def main(args: Array[String]) {
    Logger.getLogger("org").setLevel(Level.ERROR) 
    Logger.getLogger("akka").setLevel(Level.ERROR)

    // Initialize the contexts
    val conf = new SparkConf().setAppName("Spark Pi")
    val sc = new SparkContext(conf)
    
    // Calculate Pi
    val slices = if (args.length > 0) args(0).toInt else 2
    val n = math.min(100000L * slices, Int.MaxValue).toInt // avoid overflow
    val count = sc.parallelize(1 until n, slices).map { i => 
      val x = random * 2 - 1
      val y = random * 2 - 1
      if (x*x + y*y < 1) 1 else 0
    }.reduce(_ + _)
    
    println("Pi is roughly " + 4.0 * count / n)
    
    sc.stop()
  } 
} 
