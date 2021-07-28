package taskplanner;

import taskplanner.api.SubTrackCreateRequest;
import taskplanner.constants.enums.BugSeverity;
import taskplanner.constants.enums.BugStatus;
import taskplanner.constants.enums.FeatureImpact;
import taskplanner.constants.enums.FeatureStatus;
import taskplanner.constants.enums.StoryStatus;
import taskplanner.constants.enums.SubTrackStatus;
import taskplanner.entities.Bug;
import taskplanner.entities.Feature;
import taskplanner.entities.Sprint;
import taskplanner.entities.Story;
import taskplanner.entities.SubTrack;
import taskplanner.entities.User;
import taskplanner.services.SprintService;
import taskplanner.services.TaskService;
import taskplanner.services.UserService;

import java.util.Date;

public class DemoMain {

    public static void main(String[] args) {
        User user1 = User.builder().withUsername("ONE").withName("User-ONE").build();
        User user2 = User.builder().withUsername("TWO").withName("User-TWO").build();
        user1 = UserService.INSTANCE.createUser(user1);
        user2 = UserService.INSTANCE.createUser(user2);
        System.out.println(user1);
        System.out.println(user2);

        Bug bug = Bug.builder()
                .withTitle("Bug-1")
                .withCreator(user1)
                .withAssignee(user2)
                .withDueDate(new Date())
                .withSeverity(BugSeverity.P1)
                .withStatus(BugStatus.OPEN)
                .build();

        Feature feature = Feature.builder()
                .withTitle("Feature-1")
                .withSummary("Summary-Feature-1")
                .withCreator(user1)
                .withAssignee(user2)
                .withDueDate(new Date())
                .withImpact(FeatureImpact.MEDIUM)
                .withStatus(FeatureStatus.OPEN)
                .build();

        Story story = Story.builder()
                .withTitle("Story-1")
                .withCreator(user1)
                .withAssignee(user2)
                .withDueDate(new Date())
                .withSummary("Summary-Story-1")
                .withStatus(StoryStatus.INPROGRESS)
                .build();

        SubTrack subTrack = SubTrack.builder()
                .withTitle("SubTrack-1")
                .withStatus(SubTrackStatus.INPROGRESS)
                .build();

        bug = TaskService.INSTANCE.create(bug);
        feature = TaskService.INSTANCE.create(feature);
        story = TaskService.INSTANCE.create(story);
        subTrack = TaskService.INSTANCE.create(
                SubTrackCreateRequest.of(subTrack, Story.builder().withId(story.getId()).build()));
        System.out.println(bug);
        System.out.println(feature);
        System.out.println(story);
        System.out.println(subTrack);

        bug = TaskService.INSTANCE.updateStatus(Bug.builder().withId(bug.getId()).build(), BugStatus.FIXED);
        feature = TaskService.INSTANCE.updateStatus(Feature.builder().withId(feature.getId()).build(), FeatureStatus.INPROGREES);
        story = TaskService.INSTANCE.updateStatus(Story.builder().withId(story.getId()).build(), StoryStatus.COMPLETED);
        subTrack = TaskService.INSTANCE.updateStatus(SubTrack.builder().withId(subTrack.getId()).build(), SubTrackStatus.COMPLETED);
        System.out.println(bug);
        System.out.println(feature);
        System.out.println(story);
        System.out.println(subTrack);

        Sprint sprint = Sprint.builder().withName("Sprint-1").build();
        sprint = SprintService.INSTANCE.create(sprint);
        System.out.println(sprint);

        sprint = SprintService.INSTANCE.addTask(
                Sprint.builder().withName(sprint.getName()).build(),
                Bug.builder().withId(bug.getId()).build());
        System.out.println(sprint);

        SprintService.INSTANCE.removeTask(
                Sprint.builder().withName(sprint.getName()).build(),
                Bug.builder().withId(bug.getId()).build());
        System.out.println(sprint);
    }

}
