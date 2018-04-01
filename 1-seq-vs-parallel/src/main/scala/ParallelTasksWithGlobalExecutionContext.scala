import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

object ParallelTasksWithGlobalExecutionContext {

  private val data: Iterable[Input] = Range(1, 100).map(x => s"data-$x")

  def main(args: Array[String]): Unit = {

    val f: Future[Unit] = Future.traverse(data) { d =>

      println(s"[${Thread.currentThread().getName}]-Firing $d")
      processData(d)

    } map { processed =>
      processed.foreach(p => println(s"""[${Thread.currentThread().getName}]-$p"""))
    }

    Await.result(f, Duration.Inf)
  }

  type Input = String
  type Output = String

  def processData: (Input => Future[Output]) = data => {
    Future {
      Thread.sleep(1000)
      s"[Thread-${Thread.currentThread().getName}] data $data is processed."
    }
  }
}
