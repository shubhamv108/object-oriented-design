package logginglibrary.configurations.models.sinkinfo;

import commons.builder.IBuilder;

public class ConsoleSinkInfo implements ISinkInfo {

    private ConsoleSinkInfo() {}

    public static ConsoleSinkInfoBuilder builder() {
        return new ConsoleSinkInfoBuilder();
    }

    public static class ConsoleSinkInfoBuilder implements IBuilder<ConsoleSinkInfo> {
        @Override
        public ConsoleSinkInfo build() {
            return new ConsoleSinkInfo();
        }
    }

}
