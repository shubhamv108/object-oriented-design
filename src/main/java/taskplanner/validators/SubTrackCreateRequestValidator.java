package taskplanner.validators;

import commons.validators.AbstractRequestValidator;
import commons.validators.IValidator;
import taskplanner.api.SubTrackCreateRequest;
import taskplanner.entities.Story;
import taskplanner.entities.SubTrack;

public class SubTrackCreateRequestValidator extends AbstractRequestValidator<SubTrackCreateRequest> {

    public static final SubTrackCreateRequestValidator INSTANCE = new SubTrackCreateRequestValidator();

    private SubTrackCreateRequestValidator() {}

    @Override
    public IValidator<SubTrackCreateRequest> validate(final SubTrackCreateRequest request) {
        super.validate(request);
        final SubTrack subTrack = request.getSubTrack();
        final Story story = request.getStory();
        if (subTrack == null) {
            this.putMessage("subTrack", MUST_NOT_BE_EMPTY, "subTrack");
        }
        if (story == null || story.getId() == null) {
            this.putMessage("story", MUST_NOT_BE_EMPTY, "story");
        }
        return this;
    }
}
