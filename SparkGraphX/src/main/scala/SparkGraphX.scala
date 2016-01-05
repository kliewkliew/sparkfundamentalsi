import org.apache.log4j.Logger
import org.apache.log4j.Level
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.graphx._

object SparkGraphX
{
  def main(args: Array[String])
  {
    Logger.getLogger("org").setLevel(Level.ERROR)
    Logger.getLogger("akka").setLevel(Level.ERROR)
    
    // Initialize the contexts
    val conf = new SparkConf().setAppName("Spark GraphX")
    val sc = new SparkContext(conf)
    
    // Create the users RDD (uid, username, name)
    val users = 
      sc.textFile("input/tmp/users.txt")
        .map(line => line.split(","))
                         .map(parts => (parts.head.toLong, parts.tail))
    
    // Create the graph from a list of edges
    val followerGraph = GraphLoader.edgeListFile(sc, "input/tmp/followers.txt")

    // Attach the user attributes to the graph vertices
    val graph = followerGraph.outerJoinVertices(users) {
      case (uid, deg, Some(attrList)) => attrList
      case (uid, deg, None) => Array.empty[String]
    }
    
    // Restrict the graph to users with usernames and names
    val subgraph = graph.subgraph(vpred = (vid, attr) => attr.size == 2)

    // Compute the page rank
    val pagerankGraph = subgraph.pageRank(0.001)
    
    // Get the attributes of the top pagerank users 
    val userInfoWithPageRank = 
      subgraph.outerJoinVertices(pagerankGraph.vertices)
      { 
        case (uid, attrList, Some(pr)) => (pr, attrList.toList)
        case (uid, attrList, None) => (0.0, attrList.toList)
      }
      
      println(userInfoWithPageRank.vertices.top(5)(Ordering.by(_._2._1)).mkString("\n")) 
    
    sc.stop()
  }
}


