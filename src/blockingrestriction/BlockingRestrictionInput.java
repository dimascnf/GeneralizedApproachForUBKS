package blockingrestriction;

import blockingscheme.BlockingScheme;
import traininset.TrainingSet;

public class BlockingRestrictionInput {
	
	//Training Set or Dataset
	private BlockingRestrictable restrictable;
	private BlockingScheme scheme;
	
	public BlockingRestrictionInput() {
		
	}

	public BlockingRestrictable getRestrictable() {
		return restrictable;
	}

	public void setRestrictable(BlockingRestrictable restrictable) {
		this.restrictable = restrictable;
	}

	public BlockingScheme getScheme() {
		return scheme;
	}

	public void setScheme(BlockingScheme scheme) {
		this.scheme = scheme;
	}
	
}
