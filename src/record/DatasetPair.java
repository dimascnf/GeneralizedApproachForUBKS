package record;

import blockingrestriction.BlockingRestrictable;

public class DatasetPair implements BlockingRestrictable{
	
	private Dataset d1, d2;
	
	public DatasetPair(Dataset d1, Dataset d2) {
		this.d1 = d1;
		this.d2 = d2;
	}

	public Dataset getD1() {
		return d1;
	}

	public void setD1(Dataset d1) {
		this.d1 = d1;
	}

	public Dataset getD2() {
		return d2;
	}

	public void setD2(Dataset d2) {
		this.d2 = d2;
	}
	
}
