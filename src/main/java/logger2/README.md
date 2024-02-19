LogLevel
+ INFO
+ WARN
+ ERROR
+ DEBUG

AbstractLoggerHandler
- logLevel: LogLevel
- next: AbstractLoggerHandler
+ AbstractLoggerHandler(logLevel: LogLevel)
+ next(): AbstractLoggerHandler
+ setNext(AbstractLogger): void
+ log(level: LogLevel, message: String): void

ConsoleLogger(AbstractLoggerHandler)
FileLogger(AbstractLoggerHandler)
CloudLogger(AbstractLoggerHandler)
TopicLogger(AbstractLoggerHandler)
MetricsLogger(AbstractLoggerHandler)

LoggerHandlerChainDirectorBuilder
+ buildAndGet(): AbstractLoggerHandler

LoggerClient
- loggerHandler: AbstractLoggerHandler
+ log(level: LogLevel, message: String): void

