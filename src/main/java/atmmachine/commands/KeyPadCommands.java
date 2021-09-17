package atmmachine.commands;

import commons.ICommand;
import commons.commands.ResetCommand;
import commons.commands.invokers.ICommandInvoker;
import commons.commands.models.ResetState;

public class KeyPadCommands {

    class PressOneCommand implements ICommand {
        @Override
        public void execute() {

        }

        @Override
        public void undo() {

        }
    }

    class PressTwoCommand implements ICommand {
        @Override
        public void execute() {

        }

        @Override
        public void undo() {

        }
    }

    class PressThreeCommand implements ICommand {
        @Override
        public void execute() {

        }

        @Override
        public void undo() {

        }
    }


    class PressFourCommand implements ICommand {
        @Override
        public void execute() {

        }

        @Override
        public void undo() {

        }
    }

    class PressFiveCommand implements ICommand {
        @Override
        public void execute() {

        }

        @Override
        public void undo() {

        }
    }

    class PressSixCommand implements ICommand {
        @Override
        public void execute() {

        }

        @Override
        public void undo() {

        }
    }

    class PressSevenCommand implements ICommand {
        @Override
        public void execute() {

        }

        @Override
        public void undo() {

        }
    }

    class PressEightCommand implements ICommand {
        @Override
        public void execute() {

        }

        @Override
        public void undo() {

        }
    }

    class PressNineCommand implements ICommand {
        @Override
        public void execute() {

        }

        @Override
        public void undo() {

        }
    }


    class PressZeroCommand implements ICommand {
        @Override
        public void execute() {

        }

        @Override
        public void undo() {

        }
    }

    class PressEnterCommand implements ICommand {
        @Override
        public void execute() {

        }

        @Override
        public void undo() {

        }
    }

    class PressCancelCommand implements ICommand {
        @Override
        public void execute() {

        }

        @Override
        public void undo() {

        }
    }

    class PressResetCommand extends ResetCommand {
        protected PressResetCommand(ICommandInvoker commandInvoker, ResetState resetState) {
            super(commandInvoker, resetState);
        }

        @Override
        public void execute() {

        }

        @Override
        public void undo() {

        }
    }
}
