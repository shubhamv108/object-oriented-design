package atmmachine;

import commons.ICommand;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Screens {

    private static final BufferedReader INPUT_READER = new BufferedReader(new InputStreamReader(System.in));

    public interface Screen {

        static boolean showMessage(final String message) {
            System.out.println(message);
            return true;
        }

        String getInput();

        class ContinueCommand implements ICommand {
            @Override
            public void execute() {

            }

            @Override
            public void undo() {

            }
        }

        class CancelCommand implements ICommand {
            @Override
            public void execute() {

            }

            @Override
            public void undo() {

            }
        }

    }

    class WelcomeScreen implements Screen {

        private final static String message = "Welcome!\nInsert Card";

        @Override
        public String getInput() {
            throw new UnsupportedOperationException();
        }
    }

    class EnterPINScreen implements Screen {

        private final static String message = "Enter PIN";

        @Override
        public String getInput() {
            return null;
        }
    }
}