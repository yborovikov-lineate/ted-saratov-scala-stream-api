import com.lineate.scala.computation._
import com.lineate.scala.source.In.spark

object SparkStreamExample extends App {
  import com.lineate.scala.conversion.spark._

  Computation.from(spark("IN_1")(mapper = x => x))
    .flatMap(_.split("\\W+"))
    .filter(_.length < 4)
    .map(w => s"result: $w!")
    .to(println(_))
}