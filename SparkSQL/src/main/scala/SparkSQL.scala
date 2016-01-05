import org.apache.log4j.Logger
import org.apache.log4j.Level
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext


object SparkSQL
{
  // Define the schema
  case class Weather( date: String,
            temp: Int,
            precipitation: Double)
            
  def main(args: Array[String])
  {
    Logger.getLogger("org").setLevel(Level.ERROR)
    Logger.getLogger("akka").setLevel(Level.ERROR)
  
    // Initialize the contexts
    val conf = new SparkConf().setAppName("Spark SQL")
    val sc = new SparkContext(conf)
    val sqlContext= new org.apache.spark.sql.SQLContext(sc)
    import sqlContext.implicits._
    
    
    // Create a DataFrame from a csv file
    val weather = 
      sc.textFile("input/tmp/labdata/sparkdata/nycweather.csv")
      .map(_.split(","))
      .map(w =>   Weather(w(0),
                w(1).trim.toInt,
                w(2).trim.toDouble))
      .toDF()
    
    // Register the RDD as a table
    weather.registerTempTable("weather")
    
    val hottest_with_precip = sqlContext.sql("SELECT * FROM weather WHERE precipitation > 0.0 ORDER BY temp DESC")
    
    // Create an RDD of formatted strings for the 10 hottest days with precipitation and print them out
    hottest_with_precip
      .take(10)
      .map(x =>   ("Date: " + x(0),
            "Temp : " + x(1),
            "Precip: " + x(2)))
      .foreach(println)
	  
	  
    sc.stop()
  }
}
