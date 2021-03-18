package chess.validators;

import chess.enums.ChessGameCommand;
import chess.exceptions.InvalidCommandInputException;
import commons.exceptions.GameException;
import commons.validators.AbstractValidator;

import java.util.Arrays;
import java.util.LinkedList;

public class ChessGameCommandInputValidator extends AbstractValidator<String[]> {

    private ChessGameCommand command;

    @Override
    public ChessGameCommandInputValidator validate(final String... commandInput) {
        if (this.validateOrThrowException(commandInput).hasMessages()) {
            throw new InvalidCommandInputException(this.toString());
        }
        return this;
    }

    @Override
    public ChessGameCommandInputValidator validateOrThrowException(final String... commandInput) throws GameException {
        LinkedList<String> input = new LinkedList<>(Arrays.asList(commandInput));
        String commandString = input.poll();
        this.setCommand(ChessGameCommand.valueOf(commandString.toUpperCase()));
        if (this.getCommand() == null) {
            throw new InvalidCommandInputException(String.format("Invalid input command: %s", commandString));
        }

        this.validateArgumentsCount(input);
        this.validateArguments(input);

        return this;
    }

    private void validateArgumentsCount(final LinkedList<String> arguments) {
        String[] argumentsArray = arguments.stream().toArray(String[]::new);
        if (!this.getCommand().isArgumentsCountValid(argumentsArray)) {
            this.putMessage("Arguments", "Invalid count of arguments %s for command %s", argumentsArray, this.command);
        }
    }

    private void validateArguments(final LinkedList<String> input) {
        while (!input.isEmpty()) {
            String argument = input.poll();
            if (!this.getCommand().isArgumentValid(argument)) {
                this.putMessage("Arguments", "Invalid argument %s", argument);
            }
        }
    }

    public void setCommand(final ChessGameCommand command) {
        this.command = command;
    }

    public ChessGameCommand getCommand() {
        return this.command;
    }
}
