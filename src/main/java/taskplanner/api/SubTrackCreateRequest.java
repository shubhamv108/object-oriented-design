package taskplanner.api;

import taskplanner.entities.Story;
import taskplanner.entities.SubTrack;

public class SubTrackCreateRequest {

    private SubTrack subTrack;
    private Story story;

    private SubTrackCreateRequest(final SubTrack subTrack, final Story story) {
        this.subTrack = subTrack;
        this.story = story;
    }

    public static SubTrackCreateRequest of(final SubTrack subTrack, final Story story) {
        return new SubTrackCreateRequest(subTrack, story);
    }

    public SubTrack getSubTrack() {
        return subTrack;
    }

    public Story getStory() {
        return story;
    }
}
