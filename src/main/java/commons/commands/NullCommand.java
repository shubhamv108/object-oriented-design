package commons.commands;

import commons.ICommand;

public final class NullCommand implements ICommand {

    @Override
    public final void execute() {}
    @Override
    public final void undo() {}

    public static NullCommand getInstance() {
        return SingletonHolder.INSTANCE;
    }
    private static class SingletonHolder {
        private static final NullCommand INSTANCE = new NullCommand();
    }
}
