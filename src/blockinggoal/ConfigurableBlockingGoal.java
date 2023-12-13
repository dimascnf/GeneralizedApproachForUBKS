package blockinggoal;

public class ConfigurableBlockingGoal extends GenericBlockingGoal{

	@Override
	public Double objFunction(BlockingGoalInput blockingGoalInput) {
		return objFunction.objectiveFunction(blockingGoalInput);
	}

}
