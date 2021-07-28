package logginglibrary.configurations.models.sinkinfo;

import commons.builder.IBuilder;

public class FileSinkInfo implements ISinkInfo {

    private final String path;

    private FileSinkInfo(final String filePath) {
        this.path = filePath;
    }

    public String getPath() {
        return this.path;
    }

    public static FileSinkInfoBuilder builder() {
        return new FileSinkInfoBuilder();
    }

    public static class FileSinkInfoBuilder implements IBuilder<FileSinkInfo> {
        private String filePath;

        public final FileSinkInfoBuilder withFilePath(final String filePath) {
            this.filePath = filePath;
            return this;
        }

        @Override
        public FileSinkInfo build() {
            return new FileSinkInfo(this.filePath);
        }
    }
}
