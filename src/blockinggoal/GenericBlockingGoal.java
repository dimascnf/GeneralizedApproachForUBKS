package blockinggoal;

import record.Record;

public abstract class GenericBlockingGoal {
	
	protected BlockingGoal<BlockingGoalInput, Double> objFunction;
	
	//return overrall blocking quality, based on the input BlockingGoalInput: <Training Set,Blocking Scheme>
	public abstract Double objFunction(BlockingGoalInput blockingGoalInput);

	public BlockingGoal<BlockingGoalInput, Double> getObjFunction() {
		return objFunction;
	}

	public void setObjFunction(BlockingGoal<BlockingGoalInput, Double> objFunction) {
		this.objFunction = objFunction;
	}

}
