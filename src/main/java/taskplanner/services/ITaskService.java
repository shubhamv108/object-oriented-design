package taskplanner.services;

import taskplanner.api.SubTrackCreateRequest;
import taskplanner.constants.enums.BugStatus;
import taskplanner.constants.enums.FeatureStatus;
import taskplanner.constants.enums.StoryStatus;
import taskplanner.constants.enums.SubTrackStatus;
import taskplanner.entities.Bug;
import taskplanner.entities.Feature;
import taskplanner.entities.Story;
import taskplanner.entities.SubTrack;
import taskplanner.entities.Task;

public interface ITaskService {

    Bug create(Bug bug);
    Feature create(Feature feature);
    Story create(Story story);
    SubTrack create(SubTrackCreateRequest request);
    Bug updateStatus(Bug bug, BugStatus status);
    Feature updateStatus(Feature feature, FeatureStatus status);
    Story updateStatus(Story story, StoryStatus status);
    SubTrack updateStatus(SubTrack subTrack, SubTrackStatus status);
    Task get(Task task);
    Bug get(Bug bug);
    Feature get(Feature feature);
    Story get(Story story);

}
