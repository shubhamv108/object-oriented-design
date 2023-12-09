package aws.iam;

import aws.ITaggable;
import aws.Service;
import aws.Tag;

import java.util.ArrayList;
import java.util.Collection;

public class Role implements ITaggable {

    private EntityType entityType;

    private Collection<Policy> policies = new ArrayList<>();

    public class ServiceRole {
        private Service service;
    }

    @Override
    public void addTag(Tag tag) {

    }

    @Override
    public Collection<Tag> getTags() {
        return null;
    }
    private enum EntityType {
        AWSService, AWSAccount, WebIdentity, SAML2Federation, CustomTrustPolicy
    }
}
