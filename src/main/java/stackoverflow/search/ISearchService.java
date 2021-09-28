package stackoverflow.search;

import stackoverflow.Question;

import java.util.List;

public interface ISearchService {

    List<Question> getQuestions(String searchString);

}
