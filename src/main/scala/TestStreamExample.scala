import com.lineate.scala.computation._
import com.lineate.scala.source.In.input

object TestStreamExample extends App {
  import com.lineate.scala.conversion.test._

  /*Computation.from(input("abc cde fgh 1111", "aa bb cc dd"))
    .flatMap(_.split("\\W+"))
    .filter(_.length < 4)
    .map(w => s"result: $w!")
    .to(println(_))*/

  val words = for {
    line <- Computation.from(input("abc cde fgh 1111", "aa bb cc dd"))
    word <- line.split("\\W+")
    if word.length < 4
    result = s"result: $word!"
  } yield result

  words.to(println(_))

}