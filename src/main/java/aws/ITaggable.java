package aws;

import java.util.Collection;

public interface ITaggable {
    void addTag(Tag tag);
    Collection<Tag> getTags();
}
