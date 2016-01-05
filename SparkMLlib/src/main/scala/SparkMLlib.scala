import org.apache.log4j.Logger
import org.apache.log4j.Level
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.mllib.clustering.KMeans
import org.apache.spark.mllib.linalg.Vectors

object SparkMLlib
{
  def main(args: Array[String])
  {
    Logger.getLogger("org").setLevel(Level.ERROR)
    Logger.getLogger("akka").setLevel(Level.ERROR)
    
    // Initialize the contexts
    val conf = new SparkConf().setAppName("Spark MLlib")
    val sc = new SparkContext(conf)
    
    // Create a RDD from a csv file
    val taxiFile = 
        sc.textFile("input/tmp/labdata/sparkdata/nyctaxisub.csv")
        
    // Filter for the year 2013 with non-empty longitude and latitude
    val taxiData = 
        taxiFile
          .filter(_.contains("2013"))
          .filter(_.split(",")(3)!="")
          .filter(_.split(",")(4)!="")
    
    // Filter for the NYC area
    val taxiFence = 
        taxiData
          .filter(_.split(",")(3).toDouble > 40.70)
          .filter(_.split(",")(3).toDouble < 40.86)
          .filter(_.split(",")(4).toDouble > (-74.02))
          .filter(_.split(",")(4).toDouble < (-73.93))
    
    val taxi = 
        taxiFence
          .map(line =>  Vectors.dense(line.split(',')
                          .slice(3,5)
                          .map(_.toDouble)
                          )
            )
    
    // Create 3 clusters over 10 iterations
    val iterationCount = 10
    val clusterCount = 3
    val model = KMeans.train(taxi, clusterCount, iterationCount)
    val clusterCenters = model.clusterCenters.map(_.toArray)
    
    println("k-means cost: " + model.computeCost(taxi))
    
    clusterCenters.foreach{lines =>  print("Location: ")
                    println(lines(0), lines(1))}
					
    sc.stop()
  }
}


