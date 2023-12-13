package blockingkey;


@FunctionalInterface
public interface BKFunction<R,S> {
	
	public S apply(R r);

}
