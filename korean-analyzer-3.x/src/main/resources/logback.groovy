import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.core.ConsoleAppender

import static ch.qos.logback.classic.Level.DEBUG
import static ch.qos.logback.classic.Level.INFO
import static ch.qos.logback.classic.Level.WARN
import static ch.qos.logback.classic.Level.ERROR

appender("STDOUT", ConsoleAppender) {
  encoder(PatternLayoutEncoder) {
    pattern = "%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n"
  }
}

appender("FILE", RollingFileAppender) {
  file = "logFile.log"
  rollingPolicy(TimeBasedRollingPolicy) {
    fileNamePattern = "logFile.log.%d{yyyy-MM-dd}"
    maxHistory = 30
  }
  encoder(PatternLayoutEncoder) {
    pattern = "%-4relative [%thread] %-5level %logger{35} - %msg%n"
  }
}

logger("com.tistory.devyongsik", DEBUG, ["STDOUT"])

root(WARN, ["STDOUT"])