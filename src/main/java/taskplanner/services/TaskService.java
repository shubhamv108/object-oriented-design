package taskplanner.services;

import taskplanner.api.SubTrackCreateRequest;
import taskplanner.constants.enums.BugSeverity;
import taskplanner.constants.enums.BugStatus;
import taskplanner.constants.enums.FeatureImpact;
import taskplanner.constants.enums.FeatureStatus;
import taskplanner.constants.enums.StoryStatus;
import taskplanner.constants.enums.SubTrackStatus;
import taskplanner.exceptions.TaskPlannerException;
import taskplanner.locks.TaskPlannerLockManager;
import taskplanner.entities.Bug;
import taskplanner.entities.Feature;
import taskplanner.entities.Story;
import taskplanner.entities.SubTrack;
import taskplanner.entities.Task;
import taskplanner.repositories.BugRepository;
import taskplanner.repositories.FeatureRepository;
import taskplanner.repositories.StoryRepository;
import taskplanner.repositories.SubTrackRepository;
import taskplanner.validators.BugCreateRequestValidator;
import taskplanner.validators.FeatureCreateRequestValidator;
import taskplanner.validators.StoryCreateRequestValidator;
import taskplanner.validators.SubTrackCreateRequestValidator;

public enum TaskService implements ITaskService {
    INSTANCE;

    private final BugRepository bugRepository;
    private final FeatureRepository featureRepository;
    private final StoryRepository storyRepository;
    private final SubTrackRepository subTrackRepository;
    private final BugCreateRequestValidator bugCreateRequestValidator;
    private final FeatureCreateRequestValidator featureCreateRequestValidator;
    private final StoryCreateRequestValidator storyCreateRequestValidator;
    private final SubTrackCreateRequestValidator subTrackCreateRequestValidator;

    TaskService() {
        this(BugRepository.INSTANCE, FeatureRepository.INSTANCE, StoryRepository.INSTANCE, SubTrackRepository.INSTANCE,
             BugCreateRequestValidator.INSTANCE, FeatureCreateRequestValidator.INSTANCE,
             StoryCreateRequestValidator.INSTANCE, SubTrackCreateRequestValidator.INSTANCE);
    }

    TaskService(final BugRepository bugRepository, final FeatureRepository featureRepository,
                final StoryRepository storyRepository, final SubTrackRepository subTrackRepository,
                final BugCreateRequestValidator bugCreateRequestValidator,
                final FeatureCreateRequestValidator featureCreateRequestValidator,
                final StoryCreateRequestValidator storyCreateRequestValidator,
                final SubTrackCreateRequestValidator subTrackCreateRequestValidator) {
        this.bugRepository = bugRepository;
        this.featureRepository = featureRepository;
        this.storyRepository = storyRepository;
        this.subTrackRepository = subTrackRepository;
        this.bugCreateRequestValidator = bugCreateRequestValidator;
        this.featureCreateRequestValidator = featureCreateRequestValidator;
        this.storyCreateRequestValidator = storyCreateRequestValidator;
        this.subTrackCreateRequestValidator = subTrackCreateRequestValidator;
    }

    @Override
    public Bug create(final Bug bug) {
        this.bugCreateRequestValidator.validateOrThrowException(bug);
        if (bug.getSeverity() == null) {
            bug.setSeverity(BugSeverity.P2);
        }
        if (bug.getStatus() == null) {
            bug.setStatus(BugStatus.OPEN);
        }
        return this.bugRepository.save(bug);
    }

    @Override
    public Feature create(final Feature feature) {
        this.featureCreateRequestValidator.validateOrThrowException(feature);
        if (feature.getImpact() == null) {
            feature.setImpact(FeatureImpact.LOW);
        }
        if (feature.getStatus() == null) {
            feature.setStatus(FeatureStatus.OPEN);
        }
        return this.featureRepository.save(feature);
    }

    @Override
    public Story create(final Story story) {
        this.storyCreateRequestValidator.validateOrThrowException(story);
        if (story.getStatus() == null) {
            story.setStatus(StoryStatus.OPEN);
        }
        return this.storyRepository.save(story);
    }

    @Override
    public SubTrack create(final SubTrackCreateRequest request) {
        this.subTrackCreateRequestValidator.validateOrThrowException(request);
        final SubTrack subTrack = request.getSubTrack();
        final Story story = request.getStory();
        if (subTrack.getStatus() == null) {
            subTrack.setStatus(SubTrackStatus.OPEN);
        }
        try {
            TaskPlannerLockManager.get(story).writeLock().lock();
            final Story existingStory = this.storyRepository.getByKey(story.getId());
            if (existingStory == null) {
                throw new TaskPlannerException("Invalid story");
            }
            this.subTrackRepository.save(subTrack);
            if (existingStory.addSubTrack(subTrack)) {
                subTrack.setStory(existingStory);
            } else {
                throw new TaskPlannerException(String.format(
                        "SubTrack cannot be created: Reason: { StoryId: %s, StoryStatus: %s }",
                        subTrack.getId(), subTrack.getStatus()));
            }
        } finally {
            TaskPlannerLockManager.get(story).writeLock().unlock();
        }
        return subTrack;
    }

    @Override
    public Bug updateStatus(final Bug bug, final BugStatus status) {
        if (bug.getId() == null) {
            throw new TaskPlannerException("{bug:{id:}} is empty");
        }
        Bug existing = this.bugRepository.getByKey(bug.getId());
        if (existing == null) {
            throw new TaskPlannerException(String.format("Invalid bug with {id:%s}", bug.getId()));
        }
        try {
            TaskPlannerLockManager.get(existing).writeLock().lock();
            if (existing.getStatus().getSequenceNumber() + 1 != status.getSequenceNumber()) {
                throw new TaskPlannerException(String.format("BUG-%s current status is %s and cannot be transitioned to %s",
                        existing.getId(), existing.getStatus(), status));
            }
            existing.setStatus(status);
        } finally {
            TaskPlannerLockManager.get(existing).writeLock().unlock();
        }
        return existing;
    }

    @Override
    public Feature updateStatus(final Feature feature, final FeatureStatus status) {
        if (feature.getId() == null) {
            throw new TaskPlannerException("({feature:{id:}} is empty");
        }
        final Feature existing = this.featureRepository.getByKey(feature.getId());
        if (existing == null) {
            throw new TaskPlannerException(String.format("Invalid feature with {id:%s}", feature.getId()));
        }
        try {
            TaskPlannerLockManager.get(existing).writeLock().lock();
            if (existing.getStatus().getSequenceNumber() + 1 != status.getSequenceNumber()) {
                throw new TaskPlannerException(String.format("Feature-%s current status is %s and cannot be transitioned to %s",
                        existing.getId(), existing.getStatus(), status));
            }
            existing.setStatus(status);
        } finally {
            TaskPlannerLockManager.get(existing).writeLock().unlock();
        }
        return existing;
    }

    @Override
    public Story updateStatus(final Story story, final StoryStatus status) {
        if (story.getId() == null) {
            throw new TaskPlannerException("({story:{id:}} is empty");
        }
        final Story existing = this.storyRepository.getByKey(story.getId());
        if (existing == null) {
            throw new TaskPlannerException(String.format("Invalid story with {id:%s}", existing.getId()));
        }
        try {
            TaskPlannerLockManager.get(existing).writeLock().lock();
            if (existing.getStatus().getSequenceNumber() + 1 != status.getSequenceNumber()) {
                throw new TaskPlannerException(String.format("story-%s current status is %s and cannot be transitioned to %s",
                        existing.getId(), existing.getStatus(), status));
            }
            existing.setStatus(status);
        } finally {
            TaskPlannerLockManager.get(existing).writeLock().unlock();
        }
        return existing;
    }

    @Override
    public SubTrack updateStatus(final SubTrack subTrack, final SubTrackStatus status) {
        if (subTrack.getId() == null) {
            throw new TaskPlannerException("({subTrack:{id:}} is empty");
        }
        final SubTrack existing = this.subTrackRepository.getByKey(subTrack.getId());
        if (existing == null) {
            throw new TaskPlannerException(String.format("Invalid subTrack with {id:%s}", existing.getId()));
        }
        try {
            TaskPlannerLockManager.get("SUB-TRACK-" + existing.getId()).writeLock().lock();
            if (existing.getStatus().getSequenceNumber() + 1 != status.getSequenceNumber()) {
                throw new TaskPlannerException(String.format("story-%s current status is %s and cannot be transitioned to %s",
                        existing.getId(), existing.getStatus(), status));
            }
            existing.setStatus(status);
        } finally {
            TaskPlannerLockManager.get("SUB-TRACK-" + existing.getId()).writeLock().unlock();
        }
        return existing;
    }

    @Override
    public Task get(final Task task) {
        if (task.getClass().isAssignableFrom(Bug.class)) {
            return this.get((Bug) task);
        } else if (task.getClass().isAssignableFrom(Feature.class)) {
            return this.get((Feature) task);
        } else if (task.getClass().isAssignableFrom(Story.class)) {
            return this.get((Story) task);
        }
        return null;
    }

    @Override
    public Bug get(final Bug bug) {
        return this.bugRepository.getByKey(bug.getId());
    }

    @Override
    public Feature get(final Feature feature) {
        return this.featureRepository.getByKey(feature.getId());
    }

    @Override
    public Story get(final Story story) {
        return this.storyRepository.getByKey(story.getId());
    }

}
