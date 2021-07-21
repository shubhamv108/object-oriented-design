package commons.commands.models;

import commons.builder.IBuilder;
import commons.ICommand;
import commons.commands.UnDoCommand;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ResetState {
    private List<ICommand> commands;
    private List<UnDoCommand> unDoCommands;
    private Integer currentSequenceNumber;
    private ResetState(final List<ICommand> commands, final Integer currentSequenceNumber) {
        this.commands = commands;
        this.currentSequenceNumber = currentSequenceNumber;
    }

    public List<ICommand> getCommands() {
        return this.commands;
    }

    public Integer getCurrentSequenceNumber() {
        return this.currentSequenceNumber;
    }

    public List<UnDoCommand> getUnDoCommands() {
        return this.unDoCommands;
    }

    public static ResetStateBuilder builder() {
        return new ResetStateBuilder();
    }

    public static class ResetStateBuilder implements IBuilder<ResetState> {
        private List<ICommand> commands;
        private List<UnDoCommand> unDoCommands;
        private int currentSequenceNumber;

        public ResetStateBuilder withCommands(final ArrayList<ICommand> commands) {
            this.commands = new ArrayList<>(commands);
            return this;
        }

        public ResetStateBuilder withCommands(final LinkedList<ICommand> commands) {
            this.commands = new LinkedList<>(commands);
            return this;
        }

        public ResetStateBuilder withUnDoCommands(final LinkedList<UnDoCommand> commands) {
            this.unDoCommands = new LinkedList<>(commands);
            return this;
        }

        public ResetStateBuilder withCurrentSequenceMember(final int currentSequenceNumber) {
            this.currentSequenceNumber = currentSequenceNumber;
            return this;
        }

        @Override
        public ResetState build() {
            return new ResetState(this.commands, this.currentSequenceNumber);
        }
    }
}