package logginglibrary;

import logginglibrary.api.Message;
import logginglibrary.configurations.LoggerConfigurationInput;
import logginglibrary.configurations.models.sinkinfo.ConsoleSinkInfo;
import logginglibrary.configurations.models.sinkinfo.FileSinkInfo;
import logginglibrary.constants.LogLevel;
import logginglibrary.constants.SinkType;
import logginglibrary.factories.LoggerFactory;
import logginglibrary.loggers.ILogger;

import java.util.List;

public class DemoMain {

    public static void main(String[] args) {
        List<LoggerConfigurationInput> loggerConfigurationInputs =
                LoggerConfigurationInput.builder()
                        .withLogLevel(LogLevel.INFO)
                        .withSinkType(SinkType.Console)
                        .withSinkInfo(ConsoleSinkInfo.builder().build())
                        .withTimeFormat("yyyy-MM-dd_HH:mm:ss:SZ")
                    .and()
                        .withLogLevel(LogLevel.DEBUG)
                        .withSinkType(SinkType.Console)
                        .withSinkInfo(ConsoleSinkInfo.builder().build())
                        .withTimeFormat("yyyy-MM-dd_HH:mm:ss:SZ")
                    .and()
                        .withLogLevel(LogLevel.INFO)
                        .withSinkType(SinkType.File)
                        .withSinkInfo(FileSinkInfo.builder().withFilePath("/logs/logginglibrary/test.log").build())
                        .withTimeFormat("yyyy-MM-dd_HH:mm:ss:SZ")
                    .and()
                    .build();

        LoggerFactory loggerFactory = new LoggerFactory();
        ILogger simpleLogger = loggerFactory.get(loggerConfigurationInputs);
        Message message = Message.builder()
                .withLogLevel(LogLevel.INFO)
                .withNamespace("Test")
                .withThread(Thread.currentThread().getName())
                .withContent("Test info message to simple logger")
                .build();
        simpleLogger.log(message);

        ILogger asynchronousLogger = loggerFactory.getAsynchronous(loggerConfigurationInputs);
        Message message2 = Message.builder()
                .withLogLevel(LogLevel.INFO)
                .withNamespace("Test")
                .withThread(Thread.currentThread().getName())
                .withContent("Test info message to asynchronous logger")
                .build();
        asynchronousLogger.log(message2);
    }
}
