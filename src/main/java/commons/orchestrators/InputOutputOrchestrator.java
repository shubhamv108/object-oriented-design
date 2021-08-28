package commons.orchestrators;

import commons.IFactory;
import commons.commands.IResponsiveCommand;
import commons.commands.invokers.ICommandInvoker;
import commons.readers.input.IInputReader;
import commons.readers.input.factories.InputReaderFactory;
import commons.writers.FileWriter;
import commons.writers.IWriter;
import commons.writers.factories.WriterFactory;

import java.io.IOException;
import java.util.Iterator;

public class InputOutputOrchestrator<AbstractCommandResponse> {

    private final IInputReader inputReader;
    private final IWriter fileOutputWriter;
    private final IFactory<String, IResponsiveCommand<AbstractCommandResponse>> commandFactory;
    private final ICommandInvoker commandInvoker;

    private InputOutputOrchestrator(final String inputFilePath, final String outputFilePath,
                                    final IFactory<String, IResponsiveCommand<AbstractCommandResponse>> commandFactory,
                                    final ICommandInvoker commandInvoker) throws IOException {
        this(InputReaderFactory.getInstance().get(inputFilePath),
             WriterFactory.getInstance().get(outputFilePath),
             commandFactory, commandInvoker);
    }

    InputOutputOrchestrator(final IInputReader inputReader, final IWriter writer,
                            final IFactory<String, IResponsiveCommand<AbstractCommandResponse>> commandFactory,
                            final ICommandInvoker commandInvoker) {
        this.inputReader = inputReader;
        this.fileOutputWriter = writer;
        this.commandFactory = commandFactory;
        this.commandInvoker = commandInvoker;
    }

//    public static InputOutputOrchestrator of(final String inputFilePath,
//                                             final String outputFilePath)
//            throws IOException {
//        return new InputOutputOrchestrator(inputFilePath, outputFilePath);
//    }
//
//
//    public static InputOutputOrchestrator of(final String inputFilePath)
//            throws IOException {
//        return new InputOutputOrchestrator(inputFilePath);
//    }

    public void execute() throws IOException {
        Iterator<String> fileInputIterator = this.inputReader.iterator();
        while (fileInputIterator.hasNext()) {
            IResponsiveCommand<AbstractCommandResponse> command = this.commandFactory.get(fileInputIterator.next());
            this.commandInvoker.invoke(command);
            if (command.getResponse() != null) {
                this.fileOutputWriter.append(command.getResponse().toString());
            }
        }
    }
}
