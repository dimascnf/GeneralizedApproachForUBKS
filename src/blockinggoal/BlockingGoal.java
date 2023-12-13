package blockinggoal;

@FunctionalInterface
public interface BlockingGoal<I, R> {
	
	/**
	 * @param I blocking goal input defined as <Dataset, Training set, Blocking Scheme>
	 * @return a real number measuring the quality of the blocking scheme, according to the training set
	 */
	public R objectiveFunction(I I);
	
}
