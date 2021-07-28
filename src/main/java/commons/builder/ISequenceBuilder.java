package commons.builder;

import java.util.List;

public interface ISequenceBuilder<ISequenceBuilder, Object> extends IBuilder<List<Object>> {

    ISequenceBuilder and();

}
