package cartrulengine.commons.rulechain;

public abstract class AbstractAggregateRuleChain<Aggregate, Aggregated, Return> extends AbstractRuleChain<Aggregate, Return> {

    private final AbstractRuleChain<Aggregated, Return> aggregatedRuleChain;

    public AbstractAggregateRuleChain(final AbstractRuleChain<Aggregated, Return> aggregatedRuleChain) {
        this.aggregatedRuleChain = aggregatedRuleChain;
    }

    @Override
    public Return apply(final Aggregate aggregate) {
        return this.aggregatedRuleChain.apply(this.getAggregated(aggregate));
    }

    protected abstract Aggregated getAggregated(final Aggregate aggregate);
}
