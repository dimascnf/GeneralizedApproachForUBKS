package blockingrestriction;

@FunctionalInterface
public interface BlockingRestriction<P,B> {
	
	/**
	 * @param P a pair in the form: <Training set, Blocking Scheme> or <Dataset, Blocking Scheme>
	 * @return a boolean value indicating if the input pair honors the blocking restriction
	 */
	public B honorsBlockingRestriction(P p);
	
}
