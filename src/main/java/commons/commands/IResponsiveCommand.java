package commons.commands;

import commons.ICommand;

public interface IResponsiveCommand<Response> extends ICommand {

    Response getResponse();

}
