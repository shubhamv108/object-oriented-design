package commons.validators;

import bowlingalley.exceptions.GameException;
import com.sun.tools.javac.util.List;

import java.util.Map;

public interface IValidator<Data> {

    Map<String, List<String>> validate(Data data);
    void validateOrThrowException(Data data) throws GameException;

}
