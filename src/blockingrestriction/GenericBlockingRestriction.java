package blockingrestriction;

import blockingscheme.BlockingScheme;

public abstract class GenericBlockingRestriction {
	
	//TrainingData or Dataset or DatasetPair
	protected BlockingRestriction<BlockingRestrictionInput, Boolean> restriction;
	protected RestrictableType type;
	protected BlockingRestrictionInput blockingRestrictionInput;
	
	//checks whether the blocking restrictions is honored by the Blockin gRestriction Input
	//considering the configured blocking scheme and resrictable (dataset or training set)
	public abstract Boolean honorsRestriction(BlockingRestrictionInput blockingRestrictionInput);
	
	public RestrictableType getType() {
		return type;
	}

	public void setType(RestrictableType type) {
		this.type = type;
	}

	public BlockingRestrictionInput getBlockingRestrictionInput() {
		return blockingRestrictionInput;
	}

	public void setBlockingRestrictionInput(BlockingRestrictionInput blockingRestrictionInput) {
		this.blockingRestrictionInput = blockingRestrictionInput;
	}

	public BlockingRestriction<BlockingRestrictionInput, Boolean> getRestriction() {
		return restriction;
	}

	public void setRestriction(BlockingRestriction<BlockingRestrictionInput, Boolean> restriction) {
		this.restriction = restriction;
	}
	
}
