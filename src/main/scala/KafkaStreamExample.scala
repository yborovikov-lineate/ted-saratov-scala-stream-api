import com.lineate.scala.computation._
import com.lineate.scala.source.In.kafka

object KafkaStreamExample extends App {
  import com.lineate.scala.conversion.kafka._

  Computation.from(kafka(topic = "IN_1")(mapper = x => x))
    .flatMap(_.split("\\W+"))
    .filter(_.length < 4)
    .map(w => s"result: $w!")
    .to(println(_))
}