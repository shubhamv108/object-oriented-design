package commons.commands.factories;

import commons.ICommand;
import commons.IFactory;

public interface ICommandFactory<Input> extends IFactory<Input, ICommand> {
}
