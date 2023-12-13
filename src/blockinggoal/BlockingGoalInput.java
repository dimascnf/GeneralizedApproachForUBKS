package blockinggoal;

import blockingscheme.BlockingScheme;
import record.Dataset;
import traininset.TrainingSet;

public class BlockingGoalInput {
	
	private TrainingSet set;
	private BlockingScheme scheme;
	private Dataset dataset;
	
	public BlockingGoalInput() {
	}
	
	public BlockingGoalInput(TrainingSet set, BlockingScheme scheme) {
		this.set = set;
		this.scheme = scheme;
	}
	
	public TrainingSet getSet() {
		return set;
	}

	public Dataset getDataset() {
		return dataset;
	}

	public void setDataset(Dataset dataset) {
		this.dataset = dataset;
	}

	public TrainingSet getTrainingSet() {
		return set;
	}
	public void setTrainingSet(TrainingSet set) {
		this.set = set;
	}
	public BlockingScheme getScheme() {
		return scheme;
	}
	public void setScheme(BlockingScheme scheme) {
		this.scheme = scheme;
	}
	
}
