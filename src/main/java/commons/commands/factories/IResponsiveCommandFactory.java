package commons.commands.factories;

import commons.IFactory;
import commons.commands.IResponsiveCommand;

public interface IResponsiveCommandFactory<Input, Response> extends IFactory<Input, IResponsiveCommand<Response>> {
}
