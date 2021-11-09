import sbt.internal.RelayAppender
import sbt.util.Level

class ErrorLog extends RelayAppender("error log") {

  var errors: List[String] = Nil

  override def close(): Unit = 
    super.close()
    errors = Nil

  override def appendLog(level: Level.Value, message: => String): Unit =
    if(level == Level.Error) 
      errors = message :: errors
}

object ErrorLog {
  val Instance = new ErrorLog
}
