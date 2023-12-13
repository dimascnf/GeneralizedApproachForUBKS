package blockingrestriction;

public class ConfigurableBlockingRestriction extends GenericBlockingRestriction {

	@Override
	public Boolean honorsRestriction(BlockingRestrictionInput blockingRestrictionInput) {
		
		return restriction.honorsBlockingRestriction(blockingRestrictionInput);
	}

}
